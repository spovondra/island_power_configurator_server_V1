package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.model.Inverter;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.project.ProjectInverter;
import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.project.ProjectInverterService;
import com.islandpower.configurator.repository.ProjectRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing inverters within a specific project.
 * Provides endpoints for removing an inverter, retrieving system voltage and recommended system voltage,
 * fetching suitable inverters based on configuration, selecting an inverter, and retrieving the current inverter configuration.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projects/{projectId}/inverters")
public class ProjectInverterController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectInverterController.class);

    @Autowired
    private ProjectInverterService projectInverterService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Removes an inverter from a project.
     *
     * @param projectId The ID of the project from which the inverter is removed
     * @param inverterId The ID of the inverter to be removed
     * @param request The HTTP request containing the JWT token
     */
    @DeleteMapping("/{inverterId}")
    public void removeInverter(@PathVariable String projectId, @PathVariable String inverterId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is attempting to remove inverter with ID: {} from project with ID: {}", username, userId, inverterId, projectId);
        projectInverterService.removeInverter(projectId);
        logger.info("Inverter with ID: {} was successfully removed from project with ID: {} by user {} (ID: {})", inverterId, projectId, username, userId);
    }

    /**
     * Retrieves the system voltage and recommended system voltage for a project.
     *
     * @param projectId The ID of the project
     * @return {@code ResponseEntity<Map<String, Double>>} A map containing system voltage and recommended system voltage
     */
    @GetMapping("/voltage")
    public ResponseEntity<Map<String, Double>> getVoltage(@PathVariable String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            throw new RuntimeException("Configuration model not found for project: " + projectId);
        }

        Map<String, Double> response = new HashMap<>();
        response.put("systemVoltage", configModel.getSystemVoltage() != 0 ? configModel.getSystemVoltage() : null);
        response.put("recommendedSystemVoltage", configModel.getRecommendedSystemVoltage());

        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves a list of suitable inverters based on the project's configuration and user inputs.
     *
     * @param projectId The ID of the project
     * @param systemVoltage The system voltage for the configuration
     * @param temperature The installation temperature of the inverters
     * @return {@code ResponseEntity<List<Inverter>>} A list of suitable inverters
     */
    @GetMapping("/suitable")
    public ResponseEntity<List<Inverter>> getSuitableInverters(
            @PathVariable String projectId,
            @RequestParam double systemVoltage,
            @RequestParam double temperature) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        /* get the configuration model from the project */
        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            configModel = new ConfigurationModel();
            project.setConfigurationModel(configModel);
        }

        /* update the system voltage and temperature */
        configModel.setSystemVoltage(systemVoltage);

        if (configModel.getProjectInverter() == null) {
            configModel.setProjectInverter(new ProjectInverter());
        }
        configModel.getProjectInverter().setInverterTemperature(temperature);

        projectRepository.save(project);

        double totalAppliancePower = configModel.getProjectAppliance().getTotalAcPower();
        double totalPeakAppliancePower = configModel.getProjectAppliance().getTotalAcPeakPower();

        /*  get suitable inverters based on the provided inputs */
        List<Inverter> suitableInverters = projectInverterService.getSuitableInverters(
                systemVoltage, temperature, totalAppliancePower, totalPeakAppliancePower);

        return ResponseEntity.ok(suitableInverters);
    }

    /**
     * Selects an inverter for a specific project.
     *
     * @param projectId The ID of the project
     * @param inverterId The ID of the selected inverter
     * @return {@code ResponseEntity<ProjectInverter>} The updated project inverter configuration
     */
    @PostMapping("/select-inverter/{inverterId}")
    public ResponseEntity<ProjectInverter> selectInverter(
            @PathVariable String projectId,
            @PathVariable String inverterId) {

        ProjectInverter updatedInverter = projectInverterService.selectInverter(projectId, inverterId);
        return ResponseEntity.ok(updatedInverter);
    }

    /**
     * Retrieves the current inverter configuration for a project.
     *
     * @param projectId The ID of the project
     * @return {@code ResponseEntity<ProjectInverter>} The current inverter configuration
     */
    @GetMapping("/")
    public ResponseEntity<ProjectInverter> getProjectInverter(@PathVariable String projectId) {
        ProjectInverter projectInverter = projectInverterService.getProjectInverter(projectId);

        /* return null if no inverter is found in project */
        if (projectInverter == null) {
            return ResponseEntity.ok(null);
        }

        /* return the inverter in project */
        return ResponseEntity.ok(projectInverter);
    }
}
