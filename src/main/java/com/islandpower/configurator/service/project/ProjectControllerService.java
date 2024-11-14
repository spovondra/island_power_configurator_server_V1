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
    private static final double SAFETY_FACTOR = 1.25;
    private static final double T_STC = 25.0; // Standard Test Conditions temperature

    public List<Controller> getSuitableControllers(String projectId, String regulatorType) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();
        logger.info("Fetching suitable controllers for project ID: {} with system voltage: {} and regulator type: {}",
                projectId, systemVoltage, regulatorType);

        return controllerRepository.findAll().stream()
                .filter(controller -> controller.getType().equalsIgnoreCase(regulatorType))
                .filter(controller -> controller.getMinVoltage() <= systemVoltage && controller.getMaxVoltage() >= systemVoltage) // Compatibility filter
                .collect(Collectors.toList());
    }

    public ProjectController selectControllerForProject(String projectId, String controllerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        Controller controller = controllerRepository.findById(controllerId)
                .orElseThrow(() -> new RuntimeException("Controller not found: " + controllerId));

        SolarPanel solarPanel = solarPanelRepository.findById(project.getConfigurationModel().getProjectSolarPanel().getSolarPanelId())
                .orElseThrow(() -> new RuntimeException("Solar panel not found"));

        ProjectSolarPanel projectSolarPanel = project.getConfigurationModel().getProjectSolarPanel();

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();
        int numPanels = project.getConfigurationModel().getProjectSolarPanel().getNumberOfPanels();
        double P_rated = solarPanel.getpRated();
        double I_sc = solarPanel.getIsc();
        double I_mp = solarPanel.getImp();
        double U_modul = solarPanel.getVmp();
        double K_U_oc = solarPanel.getTempCoefficientVoc();
        double K_P_max = solarPanel.getTempCoefficientPMax();
        double ambientMin = project.getSite().getMinTemperature();
        double ambientMax = project.getSite().getMaxTemperature();
        double installationTemp = getInstallationTemperatureIncrease(projectSolarPanel.getInstallationType());
        double efficiency = controller.getEfficiency();

        // Retrieve existing ProjectController or create a new one if it doesn't exist
        ProjectController projectController = project.getConfigurationModel().getProjectController();
        if (projectController == null) {
            projectController = new ProjectController();
            project.getConfigurationModel().setProjectController(projectController);
        }

        // Calculate total power generated by panels
        double totalPanelPower = numPanels * P_rated;

        // Adjusted Voc and Vmp based on temperatures
        double U_oc_adjusted = calculateAdjustedOpenCircuitVoltage(solarPanel, ambientMin);
        double U_mp_adjusted = calculateAdjustedVoltageAtMaxPower(solarPanel, ambientMax);

        projectController.setControllerId(controllerId);
        projectController.setType(controller.getType());

        if (controller.getType().equalsIgnoreCase("PWM")) {
            calculatePWMConfig(systemVoltage, U_modul, I_mp, numPanels, controller, projectController);
        } else if (controller.getType().equalsIgnoreCase("MPPT")) {
            calculateMPPTConfig(systemVoltage, U_modul, totalPanelPower, P_rated, I_mp, U_oc_adjusted, U_mp_adjusted, ambientMax, installationTemp, efficiency, controller, projectController, projectSolarPanel);
        }

        projectRepository.save(project);

        return projectController;
    }

    private int getInstallationTemperatureIncrease(String installationType) {
        return switch (installationType) {
            case "ground", "roof_angle" -> 25;
            case "parallel_greater_150mm" -> 30;
            case "parallel_less_150mm" -> 35;
            default -> 0; // Default to 25°C for unknown types
        };
    }

    private void calculatePWMConfig(double systemVoltage, double U_modul, double I_mp, int numPanels, Controller controller, ProjectController projectController) {
        int n_serial = (int) Math.floor(systemVoltage / U_modul);
        if (n_serial < 1) {
            n_serial = 1;
            logger.warn("Number of serial panels less than 1, setting to 1.");
            //projectController.setWarningMessage("Number of serial panels is less than 1. Setting it to 1, but this may not be optimal.");
        }

        int maxShortCircuitCurrent = (int) controller.getCurrentRating();
        int n_parallel = (int) Math.floor(maxShortCircuitCurrent / (I_mp * SAFETY_FACTOR));

        if (n_serial * n_parallel < numPanels) {
            projectController.setSeriesModules(0);
            projectController.setParallelModules(0);

        } else {
            findOptimalPanelConfig(numPanels, n_serial, n_parallel, projectController);
        }

        projectController.setSeriesModules(n_serial);
        projectController.setParallelModules(n_parallel);
        projectController.setRequiredCurrent(maxShortCircuitCurrent); // je to tak?
        projectController.setValid(controller.getCurrentRating() >= maxShortCircuitCurrent);
    }

    private void calculateMPPTConfig(double systemVoltage, double U_modul, double totalPanelPower, double P_rated, double I_sc, double U_oc_adjusted, double U_mp_adjusted,
                                     double ambientMax, double installationTemp, double efficiency, Controller controller, ProjectController projectController, ProjectSolarPanel projectSolarPanel) {
        int n_modules_serial_max = (int) Math.floor(controller.getMaxVoltage() / U_oc_adjusted);
        int n_modules_serial_min = (int) Math.ceil(12 / U_mp_adjusted);

        double requiredCurrentMPPT = totalPanelPower / systemVoltage;
        boolean controllerPowerCheck = controller.getRatedPower() >= totalPanelPower;

        projectController.setAdjustedOpenCircuitVoltage(U_oc_adjusted);
        projectController.setAdjustedVoltageAtMaxPower(U_mp_adjusted);
        projectController.setMaxModulesInSerial(n_modules_serial_max);
        projectController.setMinModulesInSerial(n_modules_serial_min);
        projectController.setRequiredCurrent(requiredCurrentMPPT);
        projectController.setValid(controllerPowerCheck);

        if (!controllerPowerCheck) {
            //projectController.setMPPTValidationMessage("Controller power insufficient. Required: " + totalPanelPower + " W, available: " + controller.getRatedPower() + " W.");
        }

        int maxParallelPanels = (int) Math.floor(controller.getCurrentRating() / requiredCurrentMPPT);

        if (requiredCurrentMPPT > controller.getCurrentRating()) {
            //projectController.setCurrentValidationMessage("Required current exceeds controller rating. Required: " + requiredCurrentMPPT + " A, available: " + controller.getCurrentRating() + " A.");
            //projectController.setValidCurrent(false);
        } else {
            //projectController.setValidCurrent(true);
        }

        if (n_modules_serial_max * maxParallelPanels < projectSolarPanel.getNumberOfPanels()) {
            //projectController.setSeriesPanels("Not possible with this configuration");
            //projectController.setParallelPanels("Not possible with this configuration");
        } else {
            findOptimalPanelConfig(projectSolarPanel.getNumberOfPanels(), n_modules_serial_max, maxParallelPanels, projectController);
        }
    }

    private void findOptimalPanelConfig(int numPanels, int maxSerial, int maxParallel, ProjectController projectController) {
        boolean found = false;
        for (int i = 1; i <= maxSerial; i++) {
            if (numPanels % i == 0 && numPanels / i <= maxParallel) {
                projectController.setSeriesModules(i);
                projectController.setParallelModules(numPanels / i);
                found = true;
                break;
            }
        }
        if (!found) {
            //projectController.setSeriesPanels("Not possible to configure");
            //projectController.setParallelPanels("Not possible to configure");
        }
    }

    // Adjust Voc based on ambient minimum temperature
    private double calculateAdjustedOpenCircuitVoltage(SolarPanel solarPanel, double ambientMin) {
        double K_U_oc = solarPanel.getTempCoefficientVoc();
        double voc = solarPanel.getVoc();
        double t_min = ambientMin - T_STC;
        return voc + (K_U_oc * t_min);
    }

    // Adjust Vmp based on ambient maximum temperature
    private double calculateAdjustedVoltageAtMaxPower(SolarPanel solarPanel, double ambientMax) {
        double K_P_max = solarPanel.getTempCoefficientPMax();
        double vmp = solarPanel.getVmp();
        double t_max = ambientMax - T_STC;
        return vmp + (K_P_max * t_max);
    }

    public ProjectController getProjectController(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        return project.getConfigurationModel().getProjectController();
    }
}
