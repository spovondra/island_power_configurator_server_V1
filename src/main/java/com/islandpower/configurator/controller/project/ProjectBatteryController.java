package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.model.Battery;
import com.islandpower.configurator.model.project.ProjectBattery;
import com.islandpower.configurator.service.project.ProjectBatteryService;
import com.islandpower.configurator.service.JwtUtilService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing battery configurations within a project.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projects/{projectId}/batteries")
public class ProjectBatteryController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectBatteryController.class);

    @Autowired
    private ProjectBatteryService projectBatteryService;

    @Autowired
    private JwtUtilService jwtUtilService;

    /**
     * Removes the battery configuration from a project.
     *
     * @param projectId The ID of the project from which the battery is removed
     * @param request The HTTP request containing the JWT token
     */
    @DeleteMapping("/{batteryId}")
    public void removeBattery(@PathVariable String projectId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        projectBatteryService.removeBattery(projectId);
        logger.info("Battery was successfully removed from project with ID: {} by user {} (ID: {})", projectId, username, userId);
    }

    /**
     * Retrieves a list of suitable batteries for a specific project based on the battery technology.
     *
     * @param projectId The ID of the project for which suitable batteries are fetched
     * @param technology The battery technology type (e.g., Li-ion, Lead Acid)
     * @return ResponseEntity containing a list of {@link com.islandpower.configurator.model.Battery} objects
     */
    @GetMapping("/suitable")
    public ResponseEntity<List<Battery>> getSuitableBatteries(
            @PathVariable String projectId,
            @RequestParam String technology) {
        List<Battery> suitableBatteries = projectBatteryService.getSuitableBatteries(projectId, technology);
        return ResponseEntity.ok(suitableBatteries);
    }

    /**
     * Selects a battery for a specific project and sets its configuration.
     *
     * @param projectId The ID of the project for which the battery is selected
     * @param batteryId The ID of the selected battery
     * @param autonomyDays The number of autonomy days to configure
     * @param temperature The temperature for battery configuration
     * @return ResponseEntity containing the updated {@link com.islandpower.configurator.model.project.ProjectBattery} configuration
     */
    @PostMapping("/select-battery/{batteryId}")
    public ResponseEntity<ProjectBattery> selectBattery(
            @PathVariable String projectId,
            @PathVariable String batteryId,
            @RequestParam int autonomyDays,
            @RequestParam int temperature) {
        ProjectBattery projectBattery = projectBatteryService.selectBattery(projectId, batteryId, autonomyDays, temperature);
        return ResponseEntity.ok(projectBattery);
    }

    /**
     * Retrieves the current battery configuration for a specific project.
     *
     * @param projectId The ID of the project for which the battery configuration is retrieved
     * @return ResponseEntity containing the current {@link com.islandpower.configurator.model.project.ProjectBattery} configuration
     */
    @GetMapping("/")
    public ResponseEntity<ProjectBattery> getProjectBattery(@PathVariable String projectId) {
        ProjectBattery projectBattery = projectBatteryService.getProjectBattery(projectId);
        return ResponseEntity.ok(projectBattery);
    }
}
