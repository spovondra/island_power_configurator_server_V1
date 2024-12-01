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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing project controllers.
 * Provides methods for retrieving, selecting, and configuring controllers for a specific project.
 */
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

    /**
     * Retrieves a list of suitable controllers for the specified project based on the system voltage and regulator type.
     *
     * @param projectId     The ID of the project
     * @param regulatorType The type of regulator required (e.g., PWM, MPPT)
     * @return List<Controller> A list of controllers suitable for the project's configuration
     */
    public List<Controller> getSuitableControllers(String projectId, String regulatorType) {
        Project project = findProjectById(projectId);
        double systemVoltage = project.getConfigurationModel().getSystemVoltage();

        logger.info("Fetching suitable controllers for project ID: {}, regulator type: {}", projectId, regulatorType);

        return controllerRepository.findAll().stream()
                .filter(controller -> isControllerSuitable(controller, regulatorType, systemVoltage))
                .collect(Collectors.toList());
    }

    /**
     * Selects a controller for the specified project and configures it based on project and solar panel data.
     * Updates the project configuration with the selected controller's details and saves the project.
     *
     * @param projectId   The ID of the project
     * @param controllerId The ID of the controller to select
     * @return ProjectController The configured ProjectController with updated settings
     */
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
        double I_mp = solarPanel.getImp();
        double I_sc = solarPanel.getIsc();

        double solar_deratedPower = projectSolarPanel.getMonthlyData()
                .stream()
                .max(Comparator.comparingDouble(ProjectSolarPanel.MonthlySolarData::getDeratedPower))
                .map(ProjectSolarPanel.MonthlySolarData::getDeratedPower)
                .orElse(0.0);

        /* Calculate adjusted voltages for the solar panel */
        double U_oc_adjusted = calculateAdjustedOpenCircuitVoltage(solarPanel, ambientMin, installationTemp);
        double U_mp_adjusted = calculateAdjustedVoltageAtMaxPower(solarPanel, ambientMax, installationTemp);

        /* Get or create a new ProjectController configuration */
        ProjectController projectController = Optional.ofNullable(project.getConfigurationModel().getProjectController())
                .orElseGet(ProjectController::new);

        projectController.setControllerId(controllerId);
        projectController.setType(controller.getType());
        projectController.setAdjustedOpenCircuitVoltage(U_oc_adjusted);
        projectController.setAdjustedVoltageAtMaxPower(U_mp_adjusted);

        /* Configure the controller based on its type (PWM x MPPT) */
        if (controller.getType().equalsIgnoreCase("PWM")) {
            configurePWMController(systemVoltage, controller_ratedPower, solarPanel.getVmp(), I_mp, I_sc, controller, projectController);
        } else if (controller.getType().equalsIgnoreCase("MPPT")) {
            configureMPPTController(systemVoltage, controller_ratedPower, solar_deratedPower, numPanels, U_oc_adjusted, U_mp_adjusted, I_mp, controller, projectController);
        }

        project.getConfigurationModel().setProjectController(projectController);
        projectRepository.save(project);

        return projectController;
    }

    /**
     * Checks if a controller is suitable for the project based on the regulator type and system voltage.
     *
     * @param controller The controller to evaluate
     * @param regulatorType The type of regulator required (e.g., PWM, MPPT)
     * @param systemVoltage The system voltage of the project
     * @return boolean True if the controller meets the requirements, false otherwise
     */
    private boolean isControllerSuitable(Controller controller, String regulatorType, double systemVoltage) {
        boolean suitableType = controller.getType().equalsIgnoreCase(regulatorType);
        boolean suitableMinVoltage = controller.getMinVoltage() == systemVoltage;
        boolean suitableMaxVoltage = controller.getMaxVoltage() >= systemVoltage;
        return suitableType && suitableMinVoltage && suitableMaxVoltage;
    }

    /**
     * Configures a PWM controller for the project based on system and panel characteristics.
     *
     * @param systemVoltage The system voltage of the project
     * @param controller_ratedPower The rated power of the controller
     * @param U_vmp The voltage at maximum power of the solar panel
     * @param I_mp The current at maximum power of the solar panel
     * @param I_sc The short-circuit current of the solar panel
     * @param controller The controller being configured
     * @param projectController The project controller configuration to update
     */
    private void configurePWMController(double systemVoltage, double controller_ratedPower, double U_vmp, double I_mp, double I_sc, Controller controller, ProjectController projectController) {
        /* Number of modules in series and parallel */
        int n_serial = (int) Math.ceil(systemVoltage / U_vmp);
        int n_parallel = (int) Math.ceil(controller_ratedPower / (I_mp * U_vmp));

        /* Calculate required current */
        double requiredCurrent = K_CONTROLLER_OVERSIZE * n_parallel * I_sc;
        projectController.setSeriesModules(n_serial);
        projectController.setParallelModules(n_parallel);
        projectController.setRequiredCurrent(requiredCurrent);

        /* Validate the configuration */
        boolean isValid = controller.getCurrentRating() >= requiredCurrent;
        projectController.setValid(isValid);

        /* Set status message to valid or error (if Required current exceeds controller's rating) */
        if (isValid) {
            projectController.setStatusMessage("Configuration is valid.");
        } else {
            projectController.setStatusMessage(
                    String.format("Error: Required current (%.2f A) exceeds controller's rating (%.2f A).",
                            requiredCurrent, controller.getCurrentRating()));
        }

        logger.info("PWM - Required Current: {}, Valid: {}", requiredCurrent, isValid);
    }

    /**
     * Configures an MPPT controller for the project based on system and panel characteristics.
     *
     * @param systemVoltage The system voltage of the project
     * @param controller_ratedPower The rated power of the controller
     * @param solar_deratedPower The derated power of the solar system
     * @param numPanels The number of panels in the system
     * @param U_oc_adjusted The adjusted open-circuit voltage
     * @param U_mp_adjusted The adjusted voltage at maximum power
     * @param I_mp The current at maximum power of the solar panel
     * @param controller The controller being configured
     * @param projectController The project controller configuration to update
     */
    private void configureMPPTController(double systemVoltage, double controller_ratedPower, double solar_deratedPower, int numPanels,
                                         double U_oc_adjusted, double U_mp_adjusted, double I_mp,
                                         Controller controller, ProjectController projectController) {

        /* Determine series, parallel module configuration, totalPower and requiredCurrent */
        int maxSerial = (int) Math.floor(controller.getMaxVoltage() / U_oc_adjusted);
        int minSerial = (int) Math.ceil(controller.getMinVoltage() / U_mp_adjusted);
        int n_serial = Math.min(maxSerial, Math.max(minSerial, (int) Math.floor(systemVoltage / U_mp_adjusted)));
        int n_parallel = (int) Math.floor((controller_ratedPower) / (I_mp * U_mp_adjusted));
        double totalPower = numPanels * solar_deratedPower;
        double requiredCurrent = totalPower / systemVoltage;

        /* Check for controller sizing */
        if (controller_ratedPower > 1.5 * totalPower) {
            logger.warn("Controller is overdimensioned. Controller power: {}, System power: {}", controller_ratedPower, totalPower);
            projectController.setStatusMessage(
                    String.format("Warning: Controller is overdimensioned. Controller power (%.2f W) exceeds system needs (%.2f W).",
                            controller_ratedPower, totalPower));
        }

        /* Validate against power and current requirements */
        if (controller_ratedPower <= totalPower) {
            projectController.setStatusMessage(
                    String.format("Error: Controller power (%.2f W) is less than the total required system power (%.2f W).",
                            controller_ratedPower, totalPower));
            projectController.setValid(false);
            return;
        }

        /* Check maximum voltage compatibility */
        if (controller.getMaxVoltage() < U_oc_adjusted * n_serial) {
            logger.error("Adjusted open circuit voltage exceeds controller's maximum voltage.");
            projectController.setStatusMessage(
                    String.format("Error: Adjusted open circuit voltage (%.2f V) exceeds controller's maximum voltage (%.2f V).",
                            U_oc_adjusted * n_serial, controller.getMaxVoltage()));
            projectController.setValid(false);
            return;
        }

        /* Validate panel power compatibility */
        if (totalPower > systemVoltage * controller.getCurrentRating()) {
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

        /* save */
        projectController.setMaxModulesInSerial(maxSerial);
        projectController.setMinModulesInSerial(minSerial);
        projectController.setSeriesModules(n_serial);
        projectController.setParallelModules(n_parallel);
        projectController.setRequiredCurrent(requiredCurrent);
        projectController.setRequiredPower(totalPower);

        boolean isValid = controller.getCurrentRating() >= requiredCurrent && controller.getRatedPower() >= totalPower;
        projectController.setValid(isValid);
    }

    /**
     * Calculates the adjusted open-circuit voltage for the solar panel.
     *
     * @param solarPanel       The solar panel details
     * @param ambientMin       The minimum ambient temperature
     * @param installationTemp The temperature increase due to installation type
     * @return double The adjusted open-circuit voltage
     */
    private double calculateAdjustedOpenCircuitVoltage(SolarPanel solarPanel, double ambientMin, double installationTemp) {
        double adjustment = solarPanel.getTempCoefficientVoc() * (ambientMin - T_STC + installationTemp) / 100;
        return solarPanel.getVoc() * (1 + adjustment);
    }

    /**
     * Calculates the adjusted voltage at maximum power for the solar panel.
     *
     * @param solarPanel       The solar panel details
     * @param ambientMax       The maximum ambient temperature
     * @param installationTemp The temperature increase due to installation type
     * @return double The adjusted voltage at maximum power
     */
    private double calculateAdjustedVoltageAtMaxPower(SolarPanel solarPanel, double ambientMax, double installationTemp) {
        double adjustment = solarPanel.getTempCoefficientPMax() * (ambientMax - T_STC + installationTemp) / 100;
        return solarPanel.getVmp() * (1 + adjustment);
    }

    /**
     * Retrieves the installation temperature increase based on installation type.
     *
     * @param installationType The type of installation (e.g., ground, roof_angle)
     * @return int The temperature increase in degrees Celsius
     */
    private int getInstallationTemperatureIncrease(String installationType) {
        return switch (installationType) {
            case "ground", "roof_angle" -> 25;
            case "parallel_greater_150mm" -> 30;
            case "parallel_less_150mm" -> 35;
            default -> 0;
        };
    }

    /**
     * Retrieves the project controller configuration for a specific project.
     * Returns a new configuration if none exists.
     *
     * @param projectId The ID of the project
     * @return ProjectController The current or new project controller configuration
     */
    public ProjectController getProjectController(String projectId) {
        Project project = findProjectById(projectId);
        if (project.getConfigurationModel().getProjectController() == null) {
            logger.warn("ProjectController is not configured for project: {}", projectId);
            return new ProjectController();
        }
        return project.getConfigurationModel().getProjectController();
    }

    // Helper methods for retrieving entities...

    /**
     * Finds a project by its ID.
     *
     * @param projectId The ID of the project
     * @return Project The retrieved project
     */
    private Project findProjectById(String projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));
    }

    /**
     * Finds a controller by its ID.
     *
     * @param controllerId The ID of the controller
     * @return Controller The retrieved controller
     */
    private Controller findControllerById(String controllerId) {
        return controllerRepository.findById(controllerId)
                .orElseThrow(() -> new RuntimeException("Controller not found: " + controllerId));
    }

    /**
     * Finds a solar panel by its ID.
     *
     * @param solarPanelId The ID of the solar panel
     * @return SolarPanel The retrieved solar panel
     */
    private SolarPanel findSolarPanelById(String solarPanelId) {
        return solarPanelRepository.findById(solarPanelId)
                .orElseThrow(() -> new RuntimeException("Solar panel not found: " + solarPanelId));
    }
}
