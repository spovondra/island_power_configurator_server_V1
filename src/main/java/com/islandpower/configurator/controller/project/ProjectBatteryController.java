package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.dto.BatteryConfigurationResponse;
import com.islandpower.configurator.model.Battery;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.service.project.ProjectBatteryService;
import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.repository.ProjectRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/batteries")
public class ProjectBatteryController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectBatteryController.class);

    @Autowired
    private ProjectBatteryService projectBatteryService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private ProjectRepository projectRepository;

    @PostMapping
    public Project addOrUpdateBattery(@PathVariable String projectId, @RequestBody Battery battery, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is adding/updating a battery in project with ID: {}", username, userId, projectId);
        return projectBatteryService.addOrUpdateBattery(projectId, battery);
    }

    @DeleteMapping("/{batteryId}")
    public void removeBattery(@PathVariable String projectId, @PathVariable String batteryId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is attempting to remove battery with ID: {} from project with ID: {}", username, userId, batteryId, projectId);
        projectBatteryService.removeBattery(projectId, batteryId);
        logger.info("Battery with ID: {} was successfully removed from project with ID: {} by user {} (ID: {})", batteryId, projectId, username, userId);
    }

    @GetMapping("/suitable")
    public ResponseEntity<List<Battery>> getSuitableBatteries(
            @PathVariable String projectId,
            @RequestParam String technology) { // Added Technology parameter

        List<Battery> suitableBatteries = projectBatteryService.getSuitableBatteries(projectId, technology); // Pass Technology
        return ResponseEntity.ok(suitableBatteries);
    }

    @PostMapping("/select-battery/{batteryId}")
    public ResponseEntity<BatteryConfigurationResponse> selectBattery(
            @PathVariable String projectId,
            @PathVariable String batteryId,
            @RequestParam int autonomyDays,
            @RequestParam int temperature) {

        BatteryConfigurationResponse response = projectBatteryService.selectBattery(projectId, batteryId, autonomyDays, temperature);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/batteries/{batteryId}")
    public ResponseEntity<Battery> getBatteryDetails(@PathVariable String projectId, @PathVariable String batteryId) {
        Battery battery = projectBatteryService.getSelectedBattery(batteryId);
        if (battery != null) {
            return ResponseEntity.ok(battery);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
