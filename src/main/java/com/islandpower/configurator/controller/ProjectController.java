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

    // Method to get username from token
    private String getUsernameFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUsername(token);
        }
        throw new RuntimeException("No token found in request");
    }

    // Method to get user ID from username
    private String getUserIdFromUsername(String username) {
        Optional<OneUser> user = userDetailsService.getUserByUsername(username);
        if (user.isPresent()) {
            return user.get().getId();
        }
        throw new RuntimeException("User not found: " + username);
    }

    @PostMapping
    public Project createProject(@RequestBody Project project, HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        String userId = getUserIdFromUsername(username);
        logger.info("User {} (ID: {}) is creating a project: {}", username, userId, project);
        return projectService.createProject(project, userId);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable String projectId, HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        String userId = getUserIdFromUsername(username);
        logger.info("User {} (ID: {}) is deleting project with ID: {}", username, userId, projectId);
        projectService.deleteProject(projectId, userId);
    }

    @GetMapping
    public List<Project> getProjects(HttpServletRequest request) {
        String username = getUsernameFromToken(request);
        String userId = getUserIdFromUsername(username);
        logger.info("User {} (ID: {}) is retrieving their projects", username, userId);
        return projectService.getProjectsByUserId(userId);
    }
}
