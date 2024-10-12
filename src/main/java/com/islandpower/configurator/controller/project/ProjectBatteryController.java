package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.model.Battery;
import com.islandpower.configurator.model.project.ProjectBattery;
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
    public ResponseEntity<ProjectBattery> selectBattery(
            @PathVariable String projectId,
            @PathVariable String batteryId,
            @RequestParam int autonomyDays,
            @RequestParam int temperature) {

        ProjectBattery projectBattery = projectBatteryService.selectBattery(projectId, batteryId, autonomyDays, temperature);
        return ResponseEntity.ok(projectBattery);
    }

    @GetMapping("/")
    public ResponseEntity<ProjectBattery> getProjectBattery(@PathVariable String projectId) {
        ProjectBattery projectBattery = projectBatteryService.getProjectBattery(projectId);
        return ResponseEntity.ok(projectBattery);
    }
}
