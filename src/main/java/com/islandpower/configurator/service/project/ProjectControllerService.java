package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.Controller;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.SolarPanel;
import com.islandpower.configurator.model.project.ProjectController;
import com.islandpower.configurator.model.project.ProjectSolarPanel;
import com.islandpower.configurator.repository.ControllerRepository;
import com.islandpower.configurator.repository.ProjectRepository;
import com.islandpower.configurator.repository.SolarPanelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectControllerService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectControllerService.class);

    @Autowired
    private ControllerRepository controllerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SolarPanelRepository solarPanelRepository;

    private static final double K_CONTROLLER_OVERSIZE = 1.25;
    private static final double T_STC = 25.0;

    public List<Controller> getSuitableControllers(String projectId, String regulatorType) {
        Project project = findProjectById(projectId);
        double systemVoltage = project.getConfigurationModel().getSystemVoltage();

        logger.info("Fetching suitable controllers for project ID: {}, regulator type: {}", projectId, regulatorType);

        return controllerRepository.findAll().stream()
                .filter(controller -> isControllerSuitable(controller, regulatorType, systemVoltage))
                .collect(Collectors.toList());
    }

    public ProjectController selectControllerForProject(String projectId, String controllerId) {
        Project project = findProjectById(projectId);
        Controller controller = findControllerById(controllerId);
        SolarPanel solarPanel = findSolarPanelById(project.getConfigurationModel().getProjectSolarPanel().getSolarPanelId());

        ProjectSolarPanel projectSolarPanel = project.getConfigurationModel().getProjectSolarPanel();
        double systemVoltage = project.getConfigurationModel().getSystemVoltage();
        int numPanels = projectSolarPanel.getNumberOfPanels();

        double ambientMin = project.getSite().getMinTemperature();
        double ambientMax = project.getSite().getMaxTemperature();
        double installationTemp = getInstallationTemperatureIncrease(projectSolarPanel.getInstallationType());
        double ratedPower = controller.getRatedPower();
        double I_mp = solarPanel.getImp();

        logger.info("System Voltage: {}", systemVoltage);
        logger.info("Ambient Min Temperature: {}", ambientMin);
        logger.info("Ambient Max Temperature: {}", ambientMax);
        logger.info("Installation Temperature Increase: {}", installationTemp);

        double U_oc_adjusted = calculateAdjustedOpenCircuitVoltage(solarPanel, ambientMin, installationTemp);
        double U_mp_adjusted = calculateAdjustedVoltageAtMaxPower(solarPanel, ambientMax, installationTemp);

        logger.info("Adjusted Open Circuit Voltage (Voc): {}", U_oc_adjusted);
        logger.info("Adjusted Voltage at Max Power (Vmp): {}", U_mp_adjusted);

        ProjectController projectController = Optional.ofNullable(project.getConfigurationModel().getProjectController())
                .orElseGet(ProjectController::new);

        if (controller.getType().equalsIgnoreCase("PWM")) {
            configurePWMController(systemVoltage, solarPanel.getVmp(), I_mp, numPanels, controller, projectController);
        } else if (controller.getType().equalsIgnoreCase("MPPT")) {
            configureMPPTController(systemVoltage, ratedPower, solarPanel.getpRated(), numPanels, U_oc_adjusted, U_mp_adjusted, I_mp, controller, projectController);
        }

        project.getConfigurationModel().setProjectController(projectController);
        projectRepository.save(project);

        return projectController;
    }

    private boolean isControllerSuitable(Controller controller, String regulatorType, double systemVoltage) {
        boolean suitable = controller.getType().equalsIgnoreCase(regulatorType)
                && controller.getMinVoltage() <= systemVoltage
                && controller.getMaxVoltage() >= systemVoltage;
        logger.info("Controller {} is suitable: {}", controller.getName(), suitable);
        return suitable;
    }

    private void configurePWMController(double systemVoltage, double U_vmp, double I_mp, int numPanels, Controller controller, ProjectController projectController) {
        // Výpočet počtu modulů v sérii
        int n_serial = (int) Math.floor(systemVoltage / U_vmp);
        logger.info("Calculated PWM Serial Modules (n_serial): {}", n_serial);

        // Výpočet počtu modulů paralelně
        double ratedPower = numPanels * U_vmp * I_mp;
        int n_parallel = (int) Math.floor(ratedPower / (I_mp * U_vmp));
        logger.info("Calculated PWM Parallel Modules (n_parallel): {}", n_parallel);

        // Nastavení hodnot do ProjectController objektu
        projectController.setSeriesModules(n_serial);
        projectController.setParallelModules(n_parallel);

        // Validace konfigurace
        double requiredCurrent = K_CONTROLLER_OVERSIZE * n_parallel * I_mp;
        boolean isValid = controller.getCurrentRating() >= requiredCurrent;
        projectController.setRequiredCurrent(requiredCurrent);
        projectController.setValid(isValid);

        logger.info("PWM Configuration - Serial Modules: {}, Parallel Modules: {}, Required Current: {}, Valid Configuration: {}", n_serial, n_parallel, requiredCurrent, isValid);
    }

    private void configureMPPTController(double systemVoltage, double ratedPower, double P_rated, int numPanels, double U_oc_adjusted, double U_mp_adjusted, double I_mp, Controller controller, ProjectController projectController) {
        // Výpočet maximálního a minimálního počtu modulů v sérii
        int maxSerial = (int) Math.floor(controller.getMaxVoltage() / U_oc_adjusted);
        int minSerial = (int) Math.ceil(controller.getMinVoltage() / U_mp_adjusted);

        // Výpočet počtu modulů v sérii
        int n_serial = Math.min(maxSerial, Math.max(minSerial, (int) Math.floor(systemVoltage / U_mp_adjusted)));
        logger.info("Calculated Serial Modules (n_serial): {}", n_serial);

        // Výpočet celkového výkonu a požadovaného proudu
        double totalPower = numPanels * P_rated;
        double requiredCurrent = totalPower / systemVoltage;

        // Výpočet počtu modulů paralelně
        int n_parallel = (int) Math.floor((ratedPower)/(I_mp*U_mp_adjusted));
        logger.info("Calculated Parallel Modules (n_parallel): {}; P_rated: {} ", n_parallel, ratedPower);

        // Nastavení hodnot do ProjectController objektu
        projectController.setSeriesModules(n_serial);
        projectController.setParallelModules(n_parallel);
        projectController.setMaxModulesInSerial(maxSerial);
        projectController.setMinModulesInSerial(minSerial);
        projectController.setRequiredCurrent(requiredCurrent);

        // Validace konfigurace
        boolean isValid = totalPower <= ratedPower && requiredCurrent <= controller.getCurrentRating();
        projectController.setValid(isValid);

        logger.info("MPPT Configuration - Max Serial: {}, Min Serial: {}, Serial Modules: {}, Parallel Modules: {}, Required Current: {}, Total Power: {}, Valid Configuration: {}", maxSerial, minSerial, n_serial, n_parallel, requiredCurrent, totalPower, isValid);
    }

    private double calculateAdjustedOpenCircuitVoltage(SolarPanel solarPanel, double ambientMin, double installationTemp) {
        double adjustment = solarPanel.getTempCoefficientVoc() * (ambientMin - T_STC + installationTemp) / 100;
        double adjustedVoc = solarPanel.getVoc() + solarPanel.getVoc() * adjustment;
        logger.info("Adjusted Open Circuit Voltage (Voc) calculation - Voc: {}, Adjustment: {}, Result: {}", solarPanel.getVoc(), adjustment, adjustedVoc);
        return adjustedVoc;
    }

    private double calculateAdjustedVoltageAtMaxPower(SolarPanel solarPanel, double ambientMax, double installationTemp) {
        double adjustment = solarPanel.getTempCoefficientPMax() * (ambientMax - T_STC + installationTemp) / 100;
        double adjustedVmp = solarPanel.getVmp() + solarPanel.getVmp() * adjustment;
        logger.info("Adjusted Voltage at Max Power (Vmp) calculation - Vmp: {}, Adjustment: {}, Result: {}", solarPanel.getVmp(), adjustment, adjustedVmp);
        return adjustedVmp;
    }

    private int getInstallationTemperatureIncrease(String installationType) {
        int increase = switch (installationType) {
            case "ground", "roof_angle" -> 25;
            case "parallel_greater_150mm" -> 30;
            case "parallel_less_150mm" -> 35;
            default -> 0;
        };
        logger.info("Installation Temperature Increase for type {}: {}", installationType, increase);
        return increase;
    }

    public ProjectController getProjectController(String projectId) {
        Project project = findProjectById(projectId);
        return Optional.ofNullable(project.getConfigurationModel().getProjectController())
                .orElseThrow(() -> new RuntimeException("ProjectController not configured for project: " + projectId));
    }

    private Project findProjectById(String projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
    }

    private Controller findControllerById(String controllerId) {
        return controllerRepository.findById(controllerId)
                .orElseThrow(() -> new RuntimeException("Controller not found: " + controllerId));
    }

    private SolarPanel findSolarPanelById(String solarPanelId) {
        return solarPanelRepository.findById(solarPanelId)
                .orElseThrow(() -> new RuntimeException("Solar panel not found: " + solarPanelId));
    }
}
