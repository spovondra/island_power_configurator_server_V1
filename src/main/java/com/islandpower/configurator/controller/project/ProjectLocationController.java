package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.project.ProjectLocationService;
import com.islandpower.configurator.model.project.Site; // Ensure you import the Site model
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/location")
public class ProjectLocationController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectLocationController.class);

    @Autowired
    private ProjectLocationService projectLocationService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping("/process")
    public ResponseEntity<Site> processLocationData(
            @PathVariable String projectId,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam int angle,
            @RequestParam int aspect,
            @RequestParam(required = false, defaultValue = "false") boolean useOptimalValues,
            HttpServletRequest request) {

        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is processing location data for project with ID: {}", username, userId, projectId);

        try {
            projectLocationService.processLocationData(projectId, userId, latitude, longitude, angle, aspect, useOptimalValues);
            Site site = projectLocationService.getSitesByProjectId(projectId, userId); // Method in your service
            return ResponseEntity.ok(site);
        } catch (Exception e) {
            logger.error("Error processing location data: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    // New method to get site data
    @GetMapping("/sites")
    public ResponseEntity<Site> getSites(
            @PathVariable String projectId,
            HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is processing location data for project with ID: {}", username, userId, projectId);

        logger.info("Fetching sites for project ID: {}", projectId);
        try {
            Site site = projectLocationService.getSitesByProjectId(projectId, userId); // Method in your service
            return ResponseEntity.ok(site);
        } catch (Exception e) {
            logger.error("Error fetching sites for project ID: {}", projectId);
            return ResponseEntity.status(500).body(null);
        }
    }
}
