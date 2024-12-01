package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.project.ProjectLocationService;
import com.islandpower.configurator.model.project.Site;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing location-related data for a project.
 * <p>
 * Provides endpoints for processing location data, updating the project's site information,
 * and retrieving site details for a specific project.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projects/{projectId}/location")
public class ProjectLocationController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectLocationController.class);

    @Autowired
    private ProjectLocationService projectLocationService;

    @Autowired
    private JwtUtilService jwtUtilService;

    /**
     * Processes location data and updates the project's site information.
     *
     * @param projectId - the ID of the project
     * @param latitude - the latitude of the location
     * @param longitude - the longitude of the location
     * @param angle - the panel angle
     * @param aspect - the panel aspect
     * @param useOptimalValues - whether to use optimal angle and aspect values
     * @param request - the HTTP request containing the JWT token
     * @return ResponseEntity<Site> - the updated site data or an error response
     */
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

        try {
            projectLocationService.processLocationData(projectId, userId, latitude, longitude, angle, aspect, useOptimalValues);
            Site site = projectLocationService.getSitesByProjectId(projectId, userId);
            return ResponseEntity.ok(site);
        } catch (Exception e) {
            logger.error("Error processing location data: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Retrieves site data for a specific project.
     *
     * @param projectId - the ID of the project
     * @param request - the HTTP request containing the JWT token
     * @return ResponseEntity<Site> - the site data or an error response
     */
    @GetMapping("/sites")
    public ResponseEntity<Site> getSites(
            @PathVariable String projectId,
            HttpServletRequest request) {

        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);

        logger.info("Fetching sites for project ID: {}", projectId);
        try {
            Site site = projectLocationService.getSitesByProjectId(projectId, userId);
            return ResponseEntity.ok(site);
        } catch (Exception e) {
            logger.error("Error fetching sites for project ID: {}", projectId);
            return ResponseEntity.status(500).body(null);
        }
    }
}
