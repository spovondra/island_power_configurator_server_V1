package com.islandpower.configurator.service;

import com.islandpower.configurator.model.OneUser;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.repository.ProjectRepository;
import com.islandpower.configurator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        existingProject.setSite(updatedProject.getSite());
        existingProject.setSolarComponents(updatedProject.getSolarComponents());

        return projectRepository.save(existingProject);
    }
}
