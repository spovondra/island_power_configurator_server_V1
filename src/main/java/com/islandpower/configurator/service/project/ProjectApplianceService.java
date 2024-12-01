package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.project.Appliance;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.project.ProjectAppliance;
import com.islandpower.configurator.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing appliances in a project.
 * Provides methods for adding, updating, removing appliances, and recalculating energy requirements.
 */
@Service
public class ProjectApplianceService {

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * Adds or updates an appliance in the specified project.
     * If the appliance ID is null or empty, a new UUID is generated.
     * Recalculates energy requirements after the update.
     *
     * @param projectId The ID of the project
     * @param appliance The appliance to add or update
     * @return Project The updated project
     */
    public Project addOrUpdateAppliance(String projectId, Appliance appliance) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        List<Appliance> appliances = project.getAppliances();
        if (appliances == null) {
            appliances = new ArrayList<>();
            project.setAppliances(appliances);
        }

        /* Ensure the appliance has a unique ID */
        if (appliance.getId() == null || appliance.getId().isEmpty()) {
            appliance.setId(UUID.randomUUID().toString());
        }

        /* Check if the appliance already exists and replace it */
        Optional<Appliance> existingAppliance = appliances.stream()
                .filter(a -> a.getId().equals(appliance.getId()))
                .findFirst();

        existingAppliance.ifPresent(appliances::remove);
        appliances.add(appliance);

        /* Recalculate energy requirements */
        calculateEnergy(project);

        return projectRepository.save(project);
    }

    /**
     * Removes an appliance from the specified project.
     * Recalculates energy requirements after the appliance is removed.
     *
     * @param projectId   The ID of the project
     * @param applianceId The ID of the appliance to remove
     */
    public void removeAppliance(String projectId, String applianceId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        List<Appliance> appliances = project.getAppliances();
        if (appliances != null) {
            appliances.removeIf(appliance -> appliance.getId().equals(applianceId));
        }

        /* Recalculate energy requirements */
        calculateEnergy(project);

        projectRepository.save(project);
    }

    /**
     * Recalculates the energy requirements for the specified project.
     * Computes total AC/DC power, energy, and peak power for appliances.
     * Updates the recommended system voltage based on total daily energy.
     *
     * @param project The project for which to calculate energy
     */
    private void calculateEnergy(Project project) {
        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            configModel = new ConfigurationModel();
            project.setConfigurationModel(configModel);
        }

        ProjectAppliance projectAppliance = configModel.getProjectAppliance();
        if (projectAppliance == null) {
            projectAppliance = new ProjectAppliance();
            configModel.setProjectAppliance(projectAppliance);
        }

        double totalAcPower = 0;
        double totalDcPower = 0;
        double totalAcEnergyDaily = 0;
        double totalDcEnergyDaily = 0;
        double totalAcPeakPower = 0;
        double totalDcPeakPower = 0;

        List<Appliance> appliances = project.getAppliances();
        if (appliances != null) {
            for (Appliance appliance : appliances) {
                /* Calculate weekly energy: E_week = P * t_hours * t_days */
                double energyWeekly = appliance.getPower() * appliance.getHours() * appliance.getDays();

                /* Calculate daily energy: E_day = E_week / 7 */
                double energyDaily = energyWeekly / 7;

                /* Update energy for each appliance */
                appliance.setEnergy(energyDaily * appliance.getQuantity());

                /* Categorize appliances by their type (AC or DC) */
                if ("AC".equals(appliance.getType())) {
                    totalAcPower += appliance.getPower() * appliance.getQuantity();
                    totalAcEnergyDaily += energyDaily * appliance.getQuantity();
                    totalAcPeakPower += appliance.getPeakPower() * appliance.getQuantity();
                } else if ("DC".equals(appliance.getType())) {
                    totalDcPower += appliance.getPower() * appliance.getQuantity();
                    totalDcEnergyDaily += energyDaily * appliance.getQuantity();
                    totalDcPeakPower += appliance.getPeakPower() * appliance.getQuantity();
                }
            }
        }

        /* Update configuration model with calculated values */
        projectAppliance.setTotalAcPower(totalAcPower);
        projectAppliance.setTotalDcPower(totalDcPower);
        projectAppliance.setTotalAcEnergy(totalAcEnergyDaily);
        projectAppliance.setTotalDcEnergy(totalDcEnergyDaily);
        projectAppliance.setTotalAcPeakPower(totalAcPeakPower);
        projectAppliance.setTotalDcPeakPower(totalDcPeakPower);

        /* Set the recommended system voltage based on total daily energy */
        configModel.setRecommendedSystemVoltage(calculateRecommendedSystemVoltage(totalAcEnergyDaily + totalDcEnergyDaily));
    }

    /**
     * Calculates the recommended system voltage based on total daily energy consumption.
     * <p>
     * 12V for energy < 1000 Wh,
     * 24V for energy between 1000 and 3000 Wh,
     * 48V for energy > 3000 Wh
     * <p>
     *
     * @param totalEnergyDaily The total daily energy consumption
     * @return double The recommended system voltage
     */
    private double calculateRecommendedSystemVoltage(double totalEnergyDaily) {
        if (totalEnergyDaily < 1000) {
            return 12;
        } else if (totalEnergyDaily < 3000) {
            return 24;
        } else {
            return 48;
        }
    }
}
