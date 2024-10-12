package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.dto.InverterDTO;
import com.islandpower.configurator.model.Inverter;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.project.ProjectInverterService;
import com.islandpower.configurator.repository.ProjectRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Project addOrUpdateInverter(@PathVariable String projectId, @RequestBody Inverter inverter, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is adding/updating an inverter in project with ID: {}", username, userId, projectId);
        return projectInverterService.addOrUpdateInverter(projectId, inverter);
    }

    @DeleteMapping("/{inverterId}")
    public void removeInverter(@PathVariable String projectId, @PathVariable String inverterId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is attempting to remove inverter with ID: {} from project with ID: {}", username, userId, inverterId, projectId);
        projectInverterService.removeInverter(projectId, inverterId);
        logger.info("Inverter with ID: {} was successfully removed from project with ID: {} by user {} (ID: {})", inverterId, projectId, username, userId);
    }

    @GetMapping("/suitable")
    public ResponseEntity<List<InverterDTO>> getSuitableInverters(
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
        configModel.setInverterTemperature(temperature);
        projectRepository.save(project);

        // Extract total appliance power and peak power from the configuration model
        double totalAppliancePower = configModel.getTotalAcEnergy(); // Adjust as necessary if using DC
        double totalPeakAppliancePower = configModel.getTotalAcPeakPower(); // Adjust if using DC peak power

        // Get suitable inverters based on the provided inputs
        List<InverterDTO> suitableInverters = projectInverterService.getSuitableInverters(
                systemVoltage, temperature, totalAppliancePower, totalPeakAppliancePower);

        // Return the suitable inverters wrapped in a ResponseEntity
        return ResponseEntity.ok(suitableInverters);
    }

    @PostMapping("/select-inverter/{inverterId}")
    public ResponseEntity<Void> selectInverter(
            @PathVariable String projectId,
            @PathVariable String inverterId) {

        // Delegate the logic to the service
        projectInverterService.selectInverter(projectId, inverterId);

        return ResponseEntity.noContent().build();
    }

    // Method to fetch inverter details by project ID and inverter ID
    @GetMapping("/inverters/{inverterId}")
    public ResponseEntity<Inverter> getInverterDetails(@PathVariable String projectId, @PathVariable String inverterId) {
        Inverter inverter = projectInverterService.getSelectedInverter(inverterId); // Call a service method to get the inverter
        if (inverter != null) {
            return ResponseEntity.ok(inverter); // Return the inverter details
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }
}
