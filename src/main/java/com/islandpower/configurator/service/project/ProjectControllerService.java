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

    // Fetch suitable controllers based on the project configuration and regulator type (PWM or MPPT)
    public List<Controller> getSuitableControllers(String projectId, String regulatorType) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();
        logger.info("Fetching suitable controllers for project ID: {} with system voltage: {} and regulator type: {}",
                projectId, systemVoltage, regulatorType);

        // Filter the controllers based on the type (PWM or MPPT)
        List<Controller> suitableControllers = controllerRepository.findAll().stream()
                .filter(controller -> controller.getType().equalsIgnoreCase(regulatorType))
                .collect(Collectors.toList());

        logger.info("Found {} suitable controllers for regulator type: {}", suitableControllers.size(), regulatorType);

        return suitableControllers;
    }

    // Select a controller by its ID and perform calculations using ConfigurationModel
    public ProjectController selectControllerForProject(String projectId, String controllerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        Controller controller = controllerRepository.findById(controllerId)
                .orElseThrow(() -> new RuntimeException("Controller not found: " + controllerId));

        // Retrieve necessary data from the ConfigurationModel
        ProjectSolarPanel projectSolarPanel = project.getConfigurationModel().getProjectSolarPanel();
        String solarPanelId = projectSolarPanel.getSolarPanelId();
        SolarPanel solarPanel = solarPanelRepository.findById(solarPanelId)
                .orElseThrow(() -> new RuntimeException("Solar panel not found: " + solarPanelId));

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();

        ProjectController projectController = project.getConfigurationModel().getProjectController();
        if (projectController == null) {
            projectController = new ProjectController();
        }

        // Perform necessary calculations based on controller and solar panel
        double I_sc = solarPanel.getIsc();
        double U_oc_adjusted = calculateAdjustedOpenCircuitVoltage(solarPanel);
        double U_mp_adjusted = calculateAdjustedVoltageAtMaxPower(solarPanel);

        int seriesModules = (int) Math.floor(systemVoltage / U_mp_adjusted);
        int parallelModules = (int) Math.floor(controller.getCurrentRating() / I_sc);

        projectController.setSeriesModules(seriesModules);
        projectController.setParallelModules(parallelModules);
        projectController.setValid(true);

        // Save the updated project with the controller configuration
        project.getConfigurationModel().setProjectController(projectController);
        projectRepository.save(project);

        return projectController;
    }

    // Helper method to calculate adjusted open-circuit voltage
    private double calculateAdjustedOpenCircuitVoltage(SolarPanel solarPanel) {
        double K_U_oc = solarPanel.getTempCoefficientVoc();
        double voc = solarPanel.getVoc();
        double ambientMin = 25; // Example value for simplicity
        double t_min = ambientMin - 25;
        return voc + (K_U_oc * t_min);
    }

    // Helper method to calculate adjusted voltage at maximum power
    private double calculateAdjustedVoltageAtMaxPower(SolarPanel solarPanel) {
        double K_P_max = solarPanel.getTempCoefficientPMax();
        double vmp = solarPanel.getVmp();
        double ambientMax = 25; // Example value for simplicity
        double t_max = ambientMax - 25;
        return vmp + (K_P_max * t_max);
    }

    // Method to fetch the controller configuration for a specific project
    public ProjectController getProjectController(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        logger.info("Fetching controller configuration for project: {}", projectId);

        return project.getConfigurationModel().getProjectController();
    }
}
