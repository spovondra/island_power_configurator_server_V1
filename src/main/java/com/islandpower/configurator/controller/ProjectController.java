package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.OneUser;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.service.OneUserDetailService;
import com.islandpower.configurator.service.ProjectService;
import com.islandpower.configurator.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling project-related requests.
 * This class provides endpoints for creating, retrieving, updating, and deleting projects.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    /**
     * Service for managing project data and operations.
     */
    @Autowired
    private ProjectService projectService;

    /**
     * Utility class for handling JWT operations.
     */
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Service for managing user details.
     */
    @Autowired
    private OneUserDetailService userDetailsService;

    /**
     * Extracts the username from the JWT token in the request.
     *
     * @param request - The HTTP servlet request containing the JWT token
     * @return String - The username extracted from the token
     * @throws RuntimeException - If the token is missing or invalid
     */
    private String extractUsernameFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUsername(token);
        }
        throw new RuntimeException("No token found in request");
    }

    /**
     * Retrieves the user ID based on the username.
     *
     * @param username - The username of the user
     * @return String - The ID of the user
     * @throws RuntimeException - If the user is not found
     */
    private String retrieveUserIdByUsername(String username) {
        Optional<OneUser> user = userDetailsService.getUserByUsername(username);
        if (user.isPresent()) {
            return user.get().getId();
        }
        throw new RuntimeException("User not found: " + username);
    }

    /**
     * Endpoint to create a new project.
     *
     * @param project - The project details to create
     * @param request - The HTTP servlet request
     * @return Project - The created project
     */
    @PostMapping
    public Project createProject(@RequestBody Project project, HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is creating a project: {}", username, userId, project);
        return projectService.createProject(project, userId);
    }

    /**
     * Endpoint to delete a project.
     *
     * @param projectId - The ID of the project to delete
     * @param request - The HTTP servlet request
     */
    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable String projectId, HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is deleting project with ID: {}", username, userId, projectId);
        projectService.deleteProject(projectId, userId);
    }

    /**
     * Endpoint to get all projects.
     *
     * @return List<Project> - A list of all projects
     */
    @GetMapping
    public List<Project> getAllProjects() {
        logger.info("Retrieving all projects");
        return projectService.getAllProjects();
    }

    /**
     * Endpoint to get projects by user ID.
     *
     * @param request - The HTTP servlet request
     * @return List<Project> - A list of projects associated with the user
     */
    @GetMapping("/user-projects")
    public List<Project> getProjectsByUserId(HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is retrieving their projects", username, userId);
        return projectService.getProjectsByUserId(userId);
    }

    /**
     * Endpoint to get a specific project by ID.
     *
     * @param projectId - The ID of the project to retrieve
     * @param request - The HTTP servlet request
     * @return Project - The requested project
     */
    @GetMapping("/{projectId}")
    public Project getProjectById(@PathVariable String projectId, HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is retrieving project with ID: {}", username, userId, projectId);
        return projectService.getProjectById(projectId, userId);
    }

    /**
     * Endpoint to update an existing project.
     *
     * @param projectId - The ID of the project to update
     * @param project - The updated project details
     * @param request - The HTTP servlet request
     * @return Project - The updated project
     */
    @PutMapping("/{projectId}")
    public Project updateProject(@PathVariable String projectId, @RequestBody Project project, HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is updating project with ID: {}", username, userId, projectId);
        return projectService.updateProject(projectId, project, userId);
    }

    /**
     * Endpoint to add or update an appliance in a project.
     *
     * @param projectId - The ID of the project to update
     * @param appliance - The appliance details to add or update
     * @param request - The HTTP servlet request
     * @return Project - The updated project with the appliance
     */
    @PostMapping("/{projectId}/appliances")
    public Project addOrUpdateAppliance(@PathVariable String projectId,
                                        @RequestBody Project.Appliance appliance,
                                        HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is adding/updating an appliance in project with ID: {}", username, userId, projectId);
        return projectService.addOrUpdateAppliance(projectId, appliance);
    }

    /**
     * Endpoint to remove an appliance from a project.
     *
     * @param projectId - The ID of the project
     * @param applianceId - The ID of the appliance to remove
     * @param request - The HTTP servlet request
     */
    @DeleteMapping("/{projectId}/appliances/{applianceId}")
    public void removeAppliance(@PathVariable String projectId,
                                @PathVariable String applianceId,
                                HttpServletRequest request) {
        try {
            String username = extractUsernameFromToken(request);
            String userId = retrieveUserIdByUsername(username);

            logger.info("User {} (ID: {}) is attempting to remove appliance with ID: {} from project with ID: {}",
                    username, userId, applianceId, projectId);

            projectService.removeAppliance(projectId, applianceId);

            logger.info("Appliance with ID: {} was successfully removed from project with ID: {} by user {} (ID: {})",
                    applianceId, projectId, username, userId);
        } catch (Exception e) {
            logger.error("Error occurred while user {} was attempting to remove appliance with ID: {} from project with ID: {}",
                    extractUsernameFromToken(request), applianceId, projectId, e);
            throw new RuntimeException("Failed to remove appliance", e);
        }
    }
}
