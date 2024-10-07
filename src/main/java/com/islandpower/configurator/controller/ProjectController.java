package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping
    public Project createProject(@RequestBody Project project, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is creating a project: {}", username, userId, project);
        return projectService.createProject(project, userId);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable String projectId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is deleting project with ID: {}", username, userId, projectId);
        projectService.deleteProject(projectId, userId);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        logger.info("Retrieving all projects");
        return projectService.getAllProjects();
    }

    @GetMapping("/user-projects")
    public List<Project> getProjectsByUserId(HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is retrieving their projects", username, userId);
        return projectService.getProjectsByUserId(userId);
    }

    @GetMapping("/{projectId}")
    public Project getProjectById(@PathVariable String projectId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is retrieving project with ID: {}", username, userId, projectId);
        return projectService.getProjectById(projectId, userId);
    }

    @PutMapping("/{projectId}")
    public Project updateProject(@PathVariable String projectId, @RequestBody Project project, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is updating project with ID: {}", username, userId, projectId);
        return projectService.updateProject(projectId, project, userId);
    }
}
