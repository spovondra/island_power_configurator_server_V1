package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing projects.
 * Provides endpoints for creating, retrieving, updating, and deleting projects,
 * as well as handling user-specific and step-completion operations.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final JwtUtilService jwtUtilService;

    /**
     * Constructs a ProjectController with the specified services.
     *
     * @param projectService The service handling project operations
     * @param jwtUtilService The utility service for handling JWT operations
     */
    @Autowired
    public ProjectController(ProjectService projectService, JwtUtilService jwtUtilService) {
        this.projectService = projectService;
        this.jwtUtilService = jwtUtilService;
    }

    /**
     * Creates a new project for the authenticated user.
     *
     * @param project The project details to create
     * @param request The HTTP request containing the JWT token
     * @return {@link Project} The created project
     */
    @PostMapping
    public Project createProject(@RequestBody Project project, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        return projectService.createProject(project, userId);
    }

    /**
     * Deletes a project for the authenticated user.
     *
     * @param projectId The ID of the project to delete
     * @param request The HTTP request containing the JWT token
     */
    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable String projectId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        projectService.deleteProject(projectId, userId);
    }

    /**
     * Retrieves all projects in the system.
     *
     * @return List of {@link Project} objects
     */
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    /**
     * Retrieves all projects associated with the authenticated user.
     *
     * @param request The HTTP request containing the JWT token
     * @return List of {@link Project} objects associated with the user
     */
    @GetMapping("/user-projects")
    public List<Project> getProjectsByUserId(HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        return projectService.getProjectsByUserId(userId);
    }

    /**
     * Retrieves a specific project by ID for the authenticated user.
     *
     * @param projectId The ID of the project to retrieve
     * @param request The HTTP request containing the JWT token
     * @return {@link Project} The requested project
     */
    @GetMapping("/{projectId}")
    public Project getProjectById(@PathVariable String projectId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        return projectService.getProjectById(projectId, userId);
    }

    /**
     * Updates a project for the authenticated user.
     *
     * @param projectId The ID of the project to update
     * @param project The updated project details
     * @param request The HTTP request containing the JWT token
     * @return {@link Project} The updated project
     */
    @PutMapping("/{projectId}")
    public Project updateProject(@PathVariable String projectId, @RequestBody Project project, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        return projectService.updateProject(projectId, project, userId);
    }

    /**
     * Marks a step as completed for a specific project.
     *
     * @param projectId The ID of the project
     * @param step The step number to mark as completed
     * @return {@link ResponseEntity} with HTTP OK status if successful
     */
    @PostMapping("/{projectId}/complete-step")
    public ResponseEntity<Void> completeStep(@PathVariable String projectId, @RequestParam int step) {
        projectService.updateLastCompletedStep(projectId, step);
        return ResponseEntity.ok().build();
    }
}
