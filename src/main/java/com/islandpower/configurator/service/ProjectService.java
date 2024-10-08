package com.islandpower.configurator.service;

import com.islandpower.configurator.model.OneUser;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.Site;
import com.islandpower.configurator.repository.ProjectRepository;
import com.islandpower.configurator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new project
    public Project createProject(Project project, String userId) {
        project.setUserId(userId);
        Project savedProject = projectRepository.save(project);

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.getProjects().add(savedProject.getId());
        userRepository.save(user);

        return savedProject;
    }

    // Delete a project
    public void deleteProject(String projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        if (!project.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to delete this project.");
        }

        projectRepository.deleteById(projectId);

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.getProjects().remove(projectId);
        userRepository.save(user);
    }

    // Get all projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Get projects by user ID
    public List<Project> getProjectsByUserId(String userId) {
        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return projectRepository.findAllById(user.getProjects());
    }

    // Get a project by its ID
    public Project getProjectById(String projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        if (!project.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to access this project.");
        }

        return project;
    }

    // Update an existing project
    public Project updateProject(String projectId, Project updatedProject, String userId) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        boolean isAdmin = user.getRole().contains("ADMIN");

        if (!isAdmin && !existingProject.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to update this project.");
        }

        existingProject.setName(updatedProject.getName());
        existingProject.setSite(updatedProject.getSite());
        existingProject.setSolarComponents(updatedProject.getSolarComponents());
        existingProject.setConfigurationModel(updatedProject.getConfigurationModel());

        return projectRepository.save(existingProject);
    }

    public void updateSiteWithLocationData(String projectId, String userId, double latitude, double longitude,
                                           double[] minMaxTemperatures, int panelAngle, int panelAspect,
                                           boolean usedOptimalValues, List<Site.MonthlyIrradiance> monthlyIrradiance) {
        // Fetch the project by ID
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        // Check if the user has permission to update this project
        if (!project.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to access this project.");
        }

        // Create a new Site object if it does not exist
        Site site = project.getSite();
        if (site == null) {
            site = new Site();
            project.setSite(site);
        }

        // Set location data
        site.setLatitude(latitude);
        site.setLongitude(longitude);
        site.setMinTemperature(minMaxTemperatures[0]);
        site.setMaxTemperature(minMaxTemperatures[1]);
        site.setPanelAngle(panelAngle);
        site.setPanelAspect(panelAspect);
        site.setUsedOptimalValues(usedOptimalValues);

        // Create and set monthly irradiance list
        site.setMonthlyIrradianceList(monthlyIrradiance);

        // Save the updated project
        projectRepository.save(project);
    }
}
