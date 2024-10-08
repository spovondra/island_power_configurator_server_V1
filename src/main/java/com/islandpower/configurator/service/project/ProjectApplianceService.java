package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.project.Appliance;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectApplianceService {

    @Autowired
    private ProjectRepository projectRepository;

    // Add or update an appliance in a project
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
            appliance.setId(UUID.randomUUID().toString());
        }

        Optional<Appliance> existingAppliance = appliances.stream()
                .filter(a -> a.getId().equals(appliance.getId()))
                .findFirst();

        if (existingAppliance.isPresent()) {
            appliances.remove(existingAppliance.get());
        }
        appliances.add(appliance);

        // Recalculate energy consumption
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

        calculateEnergyConsumption(project);

        projectRepository.save(project);
    }

    // Calculate total energy consumption for a project
    private void calculateEnergyConsumption(Project project) {
        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            configModel = new ConfigurationModel();
            project.setConfigurationModel(configModel);
        }

        double totalAcEnergy = 0;
        double totalDcEnergy = 0;
        double totalAcPeakPower = 0;
        double totalDcPeakPower = 0;

        List<Appliance> appliances = project.getAppliances();
        if (appliances != null) {
            for (Appliance appliance : appliances) {
                double dailyEnergy = appliance.getPower() * appliance.getHours() * appliance.getDays() / 7; // Daily energy calculation
                appliance.setEnergy(dailyEnergy);

                // Null check before comparing appliance type
                if (appliance.getType() != null && appliance.getType().equals("AC")) {
                    totalAcEnergy += dailyEnergy;
                    totalAcPeakPower += appliance.getPeakPower() * appliance.getQuantity();
                } else if (appliance.getType() != null && appliance.getType().equals("DC")) {
                    totalDcEnergy += dailyEnergy;
                    totalDcPeakPower += appliance.getPeakPower() * appliance.getQuantity();
                }
            }
        }

        configModel.setTotalAcEnergy(totalAcEnergy);
        configModel.setTotalDcEnergy(totalDcEnergy);
        configModel.setTotalAcPeakPower(totalAcPeakPower);
        configModel.setTotalDcPeakPower(totalDcPeakPower);

        configModel.setRecommendedSystemVoltage(calculateRecommendedSystemVoltage(totalAcEnergy + totalDcEnergy));
    }

    // Calculate recommended system voltage based on energy consumption
    private double calculateRecommendedSystemVoltage(double totalEnergy) {
        // Example logic to recommend system voltage
        if (totalEnergy < 1000) {
            return 12;
        } else if (totalEnergy < 3000) {
            return 24;
        } else {
            return 48;
        }
    }
}
