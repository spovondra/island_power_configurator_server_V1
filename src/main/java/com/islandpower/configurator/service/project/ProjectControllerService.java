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
    private static final double T_STC = 25;

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
        double controller_ratedPower = controller.getRatedPower();
        double solar_ratedPower = solarPanel.getpRated();
        double I_mp = solarPanel.getImp();
        double I_sc = solarPanel.getIsc();

        logger.info("System Voltage: {}", systemVoltage);
        logger.info("Ambient Min Temperature: {}", ambientMin);
        logger.info("Ambient Max Temperature: {}", ambientMax);
        logger.info("Installation Temperature Increase: {}", installationTemp);

        // Výpočet upravených napětí
        double U_oc_adjusted = calculateAdjustedOpenCircuitVoltage(solarPanel, ambientMin, installationTemp);
        double U_mp_adjusted = calculateAdjustedVoltageAtMaxPower(solarPanel, ambientMax, installationTemp);

        logger.info("Adjusted Open Circuit Voltage (Voc): {}", U_oc_adjusted);
        logger.info("Adjusted Voltage at Max Power (Vmp): {}", U_mp_adjusted);

        // Načtení nebo vytvoření ProjectController
        ProjectController projectController = Optional.ofNullable(project.getConfigurationModel().getProjectController())
                .orElseGet(ProjectController::new);

        projectController.setControllerId(controllerId);
        projectController.setType(controller.getType());
        projectController.setAdjustedOpenCircuitVoltage(U_oc_adjusted);
        projectController.setAdjustedVoltageAtMaxPower(U_mp_adjusted);

        if (controller.getType().equalsIgnoreCase("PWM")) {
            configurePWMController(systemVoltage, controller_ratedPower, solarPanel.getVmp(), I_mp, I_sc, numPanels, controller, projectController);
        } else if (controller.getType().equalsIgnoreCase("MPPT")) {
            configureMPPTController(systemVoltage, controller_ratedPower, solar_ratedPower, numPanels, U_oc_adjusted, U_mp_adjusted, I_mp, controller, projectController);
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

    private void configurePWMController(double systemVoltage, double controller_ratedPower, double U_vmp, double I_mp, double I_sc, int numPanels, Controller controller, ProjectController projectController) {
        // Počet modulů v sérii
        int n_serial = (int) Math.ceil(systemVoltage / U_vmp);
        // Počet modulů paralelně
        int n_parallel = (int) Math.ceil(controller_ratedPower / (I_mp * U_vmp));

        logger.info("PWM - Serial Modules: {}, Parallel Modules: {}", n_serial, n_parallel);

        // Výpočet požadovaného proudu
        double requiredCurrent = K_CONTROLLER_OVERSIZE * n_parallel * I_sc;
        projectController.setSeriesModules(n_serial);
        projectController.setParallelModules(n_parallel);
        projectController.setRequiredCurrent(requiredCurrent);

        // Validace
        boolean isValid = controller.getCurrentRating() >= requiredCurrent;
        projectController.setValid(isValid);

        // Nastavení zprávy
        if (isValid) {
            projectController.setStatusMessage("Configuration is valid.");
        } else {
            projectController.setStatusMessage(
                    String.format("Error: Required current (%.2f A) exceeds controller's rating (%.2f A).",
                            requiredCurrent, controller.getCurrentRating()));
        }

        logger.info("PWM - Required Current: {}, Valid: {}", requiredCurrent, isValid);
    }

    private void configureMPPTController(double systemVoltage, double controller_ratedPower, double solar_ratedPower, int numPanels,
                                         double U_oc_adjusted, double U_mp_adjusted, double I_mp,
                                         Controller controller, ProjectController projectController) {
        int maxSerial = (int) Math.floor(controller.getMaxVoltage() / U_oc_adjusted);
        int minSerial = (int) Math.ceil(controller.getMinVoltage() / U_mp_adjusted);
        int n_serial = Math.min(maxSerial, Math.max(minSerial, (int) Math.floor(systemVoltage / U_mp_adjusted)));
        int n_parallel = (int) Math.floor((controller_ratedPower) / (I_mp * U_mp_adjusted));

        logger.info("MPPT - Serial Modules: {}, Parallel Modules: {}", n_serial, n_parallel);

        double totalPower = K_CONTROLLER_OVERSIZE * numPanels * solar_ratedPower;
        double requiredCurrent = totalPower / systemVoltage;

        // Validace: kontrola maximálního napětí regulátoru
        if (controller.getMaxVoltage() < U_oc_adjusted * n_serial) {
            logger.error("Adjusted open circuit voltage exceeds controller's maximum voltage.");
            projectController.setStatusMessage(
                    String.format("Error: Adjusted open circuit voltage (%.2f V) exceeds controller's maximum voltage (%.2f V).",
                            U_oc_adjusted * n_serial, controller.getMaxVoltage()));
            projectController.setValid(false);
            return;
        }

        // Validace: kontrola výkonu panelů
        if (totalPower > systemVoltage * controller.getCurrentRating()) {
            logger.error("Derated power exceeds controller's capacity.");
            projectController.setStatusMessage(
                    String.format("Error: Derated power (%.2f W) exceeds controller's capacity (%.2f W).",
                            totalPower, systemVoltage * controller.getCurrentRating()));
            projectController.setValid(false);
            return;
        } else if (totalPower > 0.9 * systemVoltage * controller.getCurrentRating()) {
            logger.warn("Derated power is close to controller's maximum capacity.");
            projectController.setStatusMessage(
                    String.format("Warning: Derated power (%.2f W) is close to controller's maximum capacity (%.2f W).",
                            totalPower, systemVoltage * controller.getCurrentRating()));
        } else {
            projectController.setStatusMessage("Configuration is valid.");
        }

        projectController.setMaxModulesInSerial(maxSerial);
        projectController.setMinModulesInSerial(minSerial);
        projectController.setSeriesModules(n_serial);
        projectController.setParallelModules(n_parallel);
        projectController.setRequiredCurrent(requiredCurrent);
        projectController.setRequiredPower(totalPower);

        boolean isValid = controller.getCurrentRating() >= requiredCurrent && controller.getRatedPower() >= totalPower;
        projectController.setValid(isValid);

        if (!isValid) {
            logger.error("MPPT configuration invalid. Required Current: {}, Required Power: {}", requiredCurrent, totalPower);
        }
    }

    private double calculateAdjustedOpenCircuitVoltage(SolarPanel solarPanel, double ambientMin, double installationTemp) {
        double adjustment = solarPanel.getTempCoefficientVoc() * (ambientMin - T_STC + installationTemp) / 100;
        return solarPanel.getVoc() * (1 + adjustment);
    }

    private double calculateAdjustedVoltageAtMaxPower(SolarPanel solarPanel, double ambientMax, double installationTemp) {
        double adjustment = solarPanel.getTempCoefficientPMax() * (ambientMax - T_STC + installationTemp) / 100;
        return solarPanel.getVmp() * (1 + adjustment);
    }

    private int getInstallationTemperatureIncrease(String installationType) {
        return switch (installationType) {
            case "ground", "roof_angle" -> 25;
            case "parallel_greater_150mm" -> 30;
            case "parallel_less_150mm" -> 35;
            default -> 0;
        };
    }

    public ProjectController getProjectController(String projectId) {
        Project project = findProjectById(projectId);
        if (project.getConfigurationModel().getProjectController() == null) {
            logger.warn("ProjectController is not configured for project: {}", projectId);
            return new ProjectController();
        }
        return project.getConfigurationModel().getProjectController();
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
