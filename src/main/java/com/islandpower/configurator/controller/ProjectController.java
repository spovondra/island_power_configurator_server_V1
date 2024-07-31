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

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OneUserDetailService userDetailsService;

    // Extract username from the JWT token
    private String extractUsernameFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUsername(token);
        }
        throw new RuntimeException("No token found in request");
    }

    // Retrieve user ID based on username
    private String retrieveUserIdByUsername(String username) {
        Optional<OneUser> user = userDetailsService.getUserByUsername(username);
        if (user.isPresent()) {
            return user.get().getId();
        }
        throw new RuntimeException("User not found: " + username);
    }

    // Endpoint to create a new project
    @PostMapping
    public Project createProject(@RequestBody Project project, HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is creating a project: {}", username, userId, project);
        return projectService.createProject(project, userId);
    }

    // Endpoint to delete a project
    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable String projectId, HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is deleting project with ID: {}", username, userId, projectId);
        projectService.deleteProject(projectId, userId);
    }

    // Endpoint to get all projects
    @GetMapping
    public List<Project> getAllProjects(HttpServletRequest request) {
        logger.info("Retrieving all projects");
        return projectService.getAllProjects();
    }

    // Endpoint to get projects by user ID
    @GetMapping("/user-projects")
    public List<Project> getProjectsByUserId(HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is retrieving their projects", username, userId);
        return projectService.getProjectsByUserId(userId);
    }

    // Endpoint to get a specific project by ID
    @GetMapping("/{projectId}")
    public Project getProjectById(@PathVariable String projectId, HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is retrieving project with ID: {}", username, userId, projectId);
        return projectService.getProjectById(projectId, userId);
    }

    // Endpoint to update an existing project
    @PutMapping("/{projectId}")
    public Project updateProject(@PathVariable String projectId, @RequestBody Project project, HttpServletRequest request) {
        String username = extractUsernameFromToken(request);
        String userId = retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is updating project with ID: {}", username, userId, projectId);
        return projectService.updateProject(projectId, project, userId);
    }
}
