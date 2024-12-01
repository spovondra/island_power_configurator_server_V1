package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.project.ProjectLocationService;
import com.islandpower.configurator.model.project.Site;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing location-related data for a project.
 * Provides endpoints for processing location data, updating the project's site information,
 * and retrieving site details for a specific project.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projects/{projectId}/location")
public class ProjectLocationController {
    @Autowired
    private ProjectLocationService projectLocationService;

    @Autowired
    private JwtUtilService jwtUtilService;

    /**
     * Processes location data and updates the project's site information.
     *
     * @param projectId The ID of the project
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     * @param angle The panel angle
     * @param aspect The panel aspect
     * @param useOptimalValues Whether to use optimal angle and aspect values
     * @param request The HTTP request containing the JWT token
     * @return {@code ResponseEntity<Site>} The updated site data or an error response
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
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Retrieves site data for a specific project.
     *
     * @param projectId The ID of the project
     * @param request The HTTP request containing the JWT token
     * @return {@code ResponseEntity<Site>} The site data or an error response
     */
    @GetMapping("/sites")
    public ResponseEntity<Site> getSites(
            @PathVariable String projectId,
            HttpServletRequest request) {

        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);

        try {
            Site site = projectLocationService.getSitesByProjectId(projectId, userId);
            return ResponseEntity.ok(site);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
