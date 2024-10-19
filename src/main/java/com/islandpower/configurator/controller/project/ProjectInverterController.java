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

    @DeleteMapping("/{inverterId}")
    public void removeInverter(@PathVariable String projectId, @PathVariable String inverterId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is attempting to remove inverter with ID: {} from project with ID: {}", username, userId, inverterId, projectId);
        projectInverterService.removeInverter(projectId, inverterId);
        logger.info("Inverter with ID: {} was successfully removed from project with ID: {} by user {} (ID: {})", inverterId, projectId, username, userId);
    }

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

    @GetMapping("/suitable")
    public ResponseEntity<List<Inverter>> getSuitableInverters(
            @PathVariable String projectId,
            @RequestParam double systemVoltage,
            @RequestParam double temperature) {

        // Fetch the project by ID
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        // Get the configuration model from the project
        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            configModel = new ConfigurationModel();
            project.setConfigurationModel(configModel);
        }

        // Update the system voltage and temperature in the configuration model
        configModel.setSystemVoltage(systemVoltage);

        // Since inverter-related fields are now inside ProjectInverter, update ProjectInverter
        if (configModel.getProjectInverter() == null) {
            configModel.setProjectInverter(new com.islandpower.configurator.model.project.ProjectInverter());
        }

        configModel.getProjectInverter().setInverterTemperature(temperature);
        projectRepository.save(project);

        // Extract total appliance power and peak power from the ProjectAppliance object
        double totalAppliancePower = configModel.getProjectAppliance().getTotalAcEnergy(); // Adjust as necessary if using DC
        double totalPeakAppliancePower = configModel.getProjectAppliance().getTotalAcPeakPower(); // Adjust if using DC peak power

        // Get suitable inverters based on the provided inputs
        List<Inverter> suitableInverters = projectInverterService.getSuitableInverters(
                systemVoltage, temperature, totalAppliancePower, totalPeakAppliancePower);

        // Return the suitable inverters wrapped in a ResponseEntity
        return ResponseEntity.ok(suitableInverters);
    }

    @PostMapping("/select-inverter/{inverterId}")
    public ResponseEntity<ProjectInverter> selectInverter(
            @PathVariable String projectId,
            @PathVariable String inverterId) {

        ProjectInverter updatedInverter = projectInverterService.selectInverter(projectId, inverterId);
        return ResponseEntity.ok(updatedInverter);
    }

    @GetMapping("/")
    public ResponseEntity<ProjectInverter> getProjectInverter(@PathVariable String projectId) {
        ProjectInverter projectInverter = projectInverterService.getProjectInverter(projectId);
        return ResponseEntity.ok(projectInverter);
    }
}
