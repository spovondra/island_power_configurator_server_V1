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

    // Oversizing factor for controller
    private static final double K_CONTROLLER_OVERSIZE = 1.25;
    private static final double SAFETY_FACTOR = 1.25; // Safety factor for current calculation
    private static final double T_STC = 25.0; // Standard Test Conditions temperature

    public List<Controller> getSuitableControllers(String projectId, String regulatorType) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();
        logger.info("Fetching suitable controllers for project ID: {} with system voltage: {} and regulator type: {}",
                projectId, systemVoltage, regulatorType);

        return controllerRepository.findAll().stream()
                .filter(controller -> controller.getType().equalsIgnoreCase(regulatorType))
                .collect(Collectors.toList());
    }

    public ProjectController selectControllerForProject(String projectId, String controllerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        Controller controller = controllerRepository.findById(controllerId)
                .orElseThrow(() -> new RuntimeException("Controller not found: " + controllerId));

        ProjectSolarPanel projectSolarPanel = project.getConfigurationModel().getProjectSolarPanel();
        String solarPanelId = projectSolarPanel.getSolarPanelId();
        SolarPanel solarPanel = solarPanelRepository.findById(solarPanelId)
                .orElseThrow(() -> new RuntimeException("Solar panel not found: " + solarPanelId));

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();
        ProjectController projectController = project.getConfigurationModel().getProjectController();
        if (projectController == null) {
            projectController = new ProjectController();
        }

        double U_modul = solarPanel.getVmp();
        double I_mp = solarPanel.getImp();
        double I_sc = solarPanel.getIsc();
        double P_rated = solarPanel.getpRated();

        double U_oc_adjusted = calculateAdjustedOpenCircuitVoltage(solarPanel, project.getSite().getMinTemperature());
        double U_mp_adjusted = calculateAdjustedVoltageAtMaxPower(solarPanel, project.getSite().getMaxTemperature());

        if (controller.getType().equalsIgnoreCase("PWM")) {
            calculatePWMConfiguration(systemVoltage, U_modul, P_rated, I_mp, I_sc, controller, projectController);
        } else if (controller.getType().equalsIgnoreCase("MPPT")) {
            calculateMPPTConfiguration(systemVoltage, solarPanel, controller, projectController, U_oc_adjusted, U_mp_adjusted);
        }

        // Calculate Maximum and Minimum Modules in Serial
        int maxModulesInSerial = (int) Math.floor(controller.getMaxVoltage() / U_oc_adjusted);
        int minModulesInSerial = (int) Math.ceil(systemVoltage/ U_mp_adjusted);  // Assuming 12V as the minimum operating voltage

        projectController.setControllerId(controllerId);
        projectController.setType(controller.getType());
        projectController.setMaxModulesInSerial(maxModulesInSerial);
        projectController.setMinModulesInSerial(minModulesInSerial);
        projectController.setAdjustedOpenCircuitVoltage(U_oc_adjusted);
        projectController.setAdjustedVoltageAtMaxPower(U_mp_adjusted);
        projectController.setTotalControllerEfficiency(controller.getEfficiency());

        project.getConfigurationModel().setProjectController(projectController);
        projectRepository.save(project);

        return projectController;
    }

    private void calculatePWMConfiguration(double systemVoltage, double U_modul, double P_rated, double I_mp, double I_sc, Controller controller, ProjectController projectController) {
        int n_PWM_serial = Math.max((int) Math.floor(systemVoltage / U_modul), 1);
        int n_PWM_parallel = Math.max((int) Math.floor(controller.getCurrentRating() / (I_sc * SAFETY_FACTOR)), 1);

        double requiredCurrent = K_CONTROLLER_OVERSIZE * n_PWM_parallel * I_sc;
        boolean valid = controller.getCurrentRating() >= requiredCurrent;

        projectController.setSeriesModules(n_PWM_serial);
        projectController.setParallelModules(n_PWM_parallel);
        projectController.setRequiredCurrent(requiredCurrent);
        projectController.setValid(valid);
    }

    private void calculateMPPTConfiguration(double systemVoltage, SolarPanel solarPanel, Controller controller, ProjectController projectController, double U_oc_adjusted, double U_mp_adjusted) {
        double P_rated = solarPanel.getpRated();
        double I_mp = solarPanel.getImp();

        int n_MPPT_serial = Math.max((int) Math.floor(systemVoltage / U_mp_adjusted), 1);
        int n_MPPT_parallel = Math.max((int) Math.floor(P_rated / (I_mp * U_mp_adjusted)), 1);

        double requiredPower = K_CONTROLLER_OVERSIZE * n_MPPT_serial * P_rated;

        boolean validVoltage = controller.getMaxVoltage() >= U_oc_adjusted * n_MPPT_serial;
        boolean validPower = controller.getRatedPower() >= requiredPower;

        projectController.setSeriesModules(n_MPPT_serial);
        projectController.setParallelModules(n_MPPT_parallel);
        projectController.setRequiredPower(requiredPower);
        projectController.setValid(validVoltage && validPower);
    }

    private double calculateAdjustedOpenCircuitVoltage(SolarPanel solarPanel, double ambientMin) {
        double K_U_oc = solarPanel.getTempCoefficientVoc();
        double voc = solarPanel.getVoc();
        double t_min = ambientMin - T_STC;
        return voc + (K_U_oc * t_min * voc / 100);
    }

    private double calculateAdjustedVoltageAtMaxPower(SolarPanel solarPanel, double ambientMax) {
        double K_P_max = solarPanel.getTempCoefficientPMax();
        double vmp = solarPanel.getVmp();
        double t_max = ambientMax - T_STC;
        return vmp + (K_P_max * t_max * vmp / 100);
    }

    public ProjectController getProjectController(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        return project.getConfigurationModel().getProjectController();
    }
}
