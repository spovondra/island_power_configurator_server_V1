package com.islandpower.configurator.service;

import com.islandpower.configurator.model.OneUser;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.Appliance;
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

        // Perform calculations after updating the project
        calculateEnergyConsumption(existingProject);

        return projectRepository.save(existingProject);
    }

    // Create or update appliance in a project
    public Project addOrUpdateAppliance(String projectId, Appliance appliance) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        List<Appliance> appliances = project.getAppliances();
        if (appliances == null) {
            appliances = new ArrayList<>();
            project.setAppliances(appliances);
        }

        // Ensure appliance ID is set
        if (appliance.getId() == null || appliance.getId().isEmpty()) {
            appliance.setId(UUID.randomUUID().toString()); // Generate new ID if missing
        }

        Optional<Appliance> existingAppliance = appliances.stream()
                .filter(a -> a.getId().equals(appliance.getId()))
                .findFirst();

        if (existingAppliance.isPresent()) {
            appliances.remove(existingAppliance.get());
        }
        appliances.add(appliance);

        // Perform calculations after adding/updating appliance
        calculateEnergyConsumption(project);

        return projectRepository.save(project);
    }

    // Remove appliance from a project
    public void removeAppliance(String projectId, String applianceId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        List<Appliance> appliances = project.getAppliances();
        if (appliances != null) {
            appliances.removeIf(appliance -> appliance.getId().equals(applianceId));
        }

        // Perform calculations after removing appliance
        calculateEnergyConsumption(project);

        projectRepository.save(project);
    }

    private void calculateEnergyConsumption(Project project) {
        double totalAcEnergy = 0;
        double totalDcEnergy = 0;
        double totalAcPeakPower = 0;
        double totalDcPeakPower = 0;

        // Corrected condition to check if appliances are not null
        if (project.getAppliances() != null) {
            for (Appliance appliance : project.getAppliances()) {
                double dailyEnergy = appliance.getPower() * appliance.getHours() * appliance.getDays() / 7; // Daily energy calculation
                appliance.setEnergy(dailyEnergy);

                if (appliance.getType().equals("AC")) { // Use equals to compare strings
                    totalAcEnergy += dailyEnergy; // Accumulate AC energy
                    totalAcPeakPower += appliance.getPeakPower() * appliance.getQuantity(); // Accumulate AC peak power
                } else if (appliance.getType().equals("DC")) {
                    totalDcEnergy += dailyEnergy; // Accumulate DC energy
                    totalDcPeakPower += appliance.getPeakPower() * appliance.getQuantity(); // Accumulate DC peak power
                }
            }
        }

        // Set calculated values in project
        project.setTotalAcEnergy(totalAcEnergy);
        project.setTotalDcEnergy(totalDcEnergy);
        project.setTotalAcPeakPower(totalAcPeakPower);
        project.setTotalDcPeakPower(totalDcPeakPower);

        // Set system voltage based on the project conditions
        project.setSystemVoltage(calculateSystemVoltage(project)); // Calculate and set system voltage
    }

    // Method to calculate system voltage based on appliances
    private String calculateSystemVoltage(Project project) {
        double totalEnergy = project.getTotalAcEnergy() + project.getTotalDcEnergy();

        // Implement your logic to determine the system voltage based on the combined peak power
        // Example thresholds for demonstration purposes:
        if (totalEnergy < 1000) {
            return "12V";
        } else if (totalEnergy < 3000) {
            return "24V";
        } else {
            return "48V";
        }
    }
}
