package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing projects.
 * <p>
 * Provides endpoints for creating, retrieving, updating, and deleting projects,
 * as well as handling user-specific and step-completion operations.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JwtUtilService jwtUtilService;

    /**
     * Creates a new project for the authenticated user.
     *
     * @param project - the project details to create
     * @param request - the HTTP request containing the JWT token
     * @return Project - the created project
     */
    @PostMapping
    public Project createProject(@RequestBody Project project, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is creating a project: {}", username, userId, project);
        return projectService.createProject(project, userId);
    }

    /**
     * Deletes a project for the authenticated user.
     *
     * @param projectId - the ID of the project to delete
     * @param request - the HTTP request containing the JWT token
     */
    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable String projectId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is deleting project with ID: {}", username, userId, projectId);
        projectService.deleteProject(projectId, userId);
    }

    /**
     * Retrieves all projects in the system.
     *
     * @return List<Project> - a list of all projects
     */
    @GetMapping
    public List<Project> getAllProjects() {
        logger.info("Retrieving all projects");
        return projectService.getAllProjects();
    }

    /**
     * Retrieves all projects associated with the authenticated user.
     *
     * @param request - the HTTP request containing the JWT token
     * @return List<Project> - a list of the user's projects
     */
    @GetMapping("/user-projects")
    public List<Project> getProjectsByUserId(HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is retrieving their projects", username, userId);
        return projectService.getProjectsByUserId(userId);
    }

    /**
     * Retrieves a specific project by ID for the authenticated user.
     *
     * @param projectId - the ID of the project to retrieve
     * @param request - the HTTP request containing the JWT token
     * @return Project - the requested project
     */
    @GetMapping("/{projectId}")
    public Project getProjectById(@PathVariable String projectId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is retrieving project with ID: {}", username, userId, projectId);
        return projectService.getProjectById(projectId, userId);
    }

    /**
     * Updates a project for the authenticated user.
     *
     * @param projectId - the ID of the project to update
     * @param project - the updated project details
     * @param request - the HTTP request containing the JWT token
     * @return Project - the updated project
     */
    @PutMapping("/{projectId}")
    public Project updateProject(@PathVariable String projectId, @RequestBody Project project, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is updating project with ID: {}", username, userId, projectId);
        return projectService.updateProject(projectId, project, userId);
    }

    /**
     * Marks a step as completed for a specific project.
     *
     * @param projectId - the ID of the project
     * @param step - the step number to mark as completed
     * @return ResponseEntity<Void> - HTTP OK status if successful
     */
    @PostMapping("/{projectId}/complete-step")
    public ResponseEntity<Void> completeStep(@PathVariable String projectId, @RequestParam int step) {
        projectService.updateLastCompletedStep(projectId, step);
        return ResponseEntity.ok().build();
    }
}
