package com.islandpower.configurator.service;

import com.islandpower.configurator.model.OneUser;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.repository.ProjectRepository;
import com.islandpower.configurator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // Method to create a new project
    public Project createProject(Project project, String userId) {
        project.setUserId(userId);
        Project savedProject = projectRepository.save(project);

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.getProjects().add(savedProject.getId());
        userRepository.save(user);

        return savedProject;
    }

    // Method to delete a project
    public void deleteProject(String projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        if (!project.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to delete this project. Project User ID: " + project.getUserId() + ", Requesting User ID: " + userId);
        }

        projectRepository.deleteById(projectId);

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        user.getProjects().remove(projectId);
        userRepository.save(user);
    }

    // Method to get all projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Method to get projects by user ID
    public List<Project> getProjectsByUserId(String userId) {
        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        return projectRepository.findAllById(user.getProjects());
    }

    // Method to get a project by its ID
    public Project getProjectById(String projectId, String userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        if (!project.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to access this project. Project User ID: " + project.getUserId() + ", Requesting User ID: " + userId);
        }

        return project;
    }

    // Method to update an existing project
    public Project updateProject(String projectId, Project updatedProject, String userId) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        OneUser user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        boolean isAdmin = user.getRole().contains("ADMIN");

        if (!isAdmin && !existingProject.getUserId().equals(userId)) {
            throw new RuntimeException("User does not have permission to update this project. Project User ID: "
                    + existingProject.getUserId() + ", Requesting User ID: " + userId);
        }

        existingProject.setName(updatedProject.getName());
        existingProject.setAppliances(updatedProject.getAppliances());
        existingProject.setSite(updatedProject.getSite());
        existingProject.setSolarComponents(updatedProject.getSolarComponents());

        return projectRepository.save(existingProject);
    }

    // Create or update appliance in a project
    public Project addOrUpdateAppliance(String projectId, Project.Appliance appliance) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        List<Project.Appliance> appliances = project.getAppliances();
        if (appliances == null) {
            appliances = new ArrayList<>();
            project.setAppliances(appliances);
        }

        // Ensure appliance ID is set
        if (appliance.getId() == null || appliance.getId().isEmpty()) {
            appliance.setId(UUID.randomUUID().toString()); // Generate new ID if missing
        }

        Optional<Project.Appliance> existingAppliance = appliances.stream()
                .filter(a -> a.getId().equals(appliance.getId()))
                .findFirst();

        if (existingAppliance.isPresent()) {
            appliances.remove(existingAppliance.get());
        }
        appliances.add(appliance);

        return projectRepository.save(project);
    }

    // Remove appliance from a project
    public Project removeAppliance(String projectId, String applianceId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        List<Project.Appliance> appliances = project.getAppliances();
        if (appliances != null) {
            appliances.removeIf(appliance -> appliance.getId().equals(applianceId));
        }

        return projectRepository.save(project);
    }
}
