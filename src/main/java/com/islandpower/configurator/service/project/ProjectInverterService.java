package com.islandpower.configurator.service.project;

import com.islandpower.configurator.dto.InverterDTO;
import com.islandpower.configurator.model.Inverter;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.repository.InverterRepository;
import com.islandpower.configurator.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectInverterService {

    private final InverterRepository inverterRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectInverterService(InverterRepository inverterRepository, ProjectRepository projectRepository) {
        this.inverterRepository = inverterRepository;
        this.projectRepository = projectRepository;
    }

    // Add or update an inverter in a project
    public Project addOrUpdateInverter(String projectId, Inverter inverter) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        if (inverter.getId() == null || inverter.getId().isEmpty()) {
            inverter.setId(UUID.randomUUID().toString());
        }

        // Update the configuration model with inverter details
        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            configModel = new ConfigurationModel();
            project.setConfigurationModel(configModel);
        }

        configModel.setInverterId(inverter.getId());

        projectRepository.save(project);
        return project;
    }

    // Remove inverter from a project
    public void removeInverter(String projectId, String inverterId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel != null) {
            configModel.setInverterId(null); // Remove the inverter ID
        }

        projectRepository.save(project);
    }

    // Method to fetch and filter inverters based on power requirements and return DTOs
    public List<InverterDTO> getSuitableInverters(double systemVoltage, double temperature,
                                                  double totalAppliancePower, double totalPeakAppliancePower) {
        List<Inverter> allInverters = inverterRepository.findAll();
        List<InverterDTO> suitableInverterDTOs = new ArrayList<>();

        for (Inverter inverter : allInverters) {
            // Check if inverter voltage matches the system voltage
            if (inverter.getVoltage() != systemVoltage) {
                continue; // Skip this inverter if the voltage doesn't match
            }

            // Get the continuous power based on the temperature
            double continuousPower = getContinuousPowerByTemperature(inverter, temperature);

            // Get the peak power rating of the inverter
            double peakPower = inverter.getMaxPower();

            // Check if the inverter can handle the total appliance power and peak power requirements
            boolean isPowerSufficient = isInverterPowerSufficient(continuousPower, totalAppliancePower);
            boolean isPeakPowerSufficient = isInverterPeakPowerSufficient(peakPower, totalPeakAppliancePower);

            // If both power checks are satisfied, create a DTO and add to the suitable DTO list
            if (isPowerSufficient && isPeakPowerSufficient) {
                InverterDTO inverterDTO = new InverterDTO(
                        inverter.getId(),
                        inverter.getName(),
                        continuousPower,
                        inverter.getMaxPower(),
                        inverter.getEfficiency(),
                        inverter.getVoltage()
                );
                suitableInverterDTOs.add(inverterDTO);
            }
        }
        return suitableInverterDTOs;
    }

    // Example implementation for getting continuous power based on temperature
    private double getContinuousPowerByTemperature(Inverter inverter, double temperature) {
        if (temperature == 25) {
            return inverter.getContinuousPower25C();
        } else if (temperature == 40) {
            return inverter.getContinuousPower40C();
        } else {
            return inverter.getContinuousPower65C();
        }
    }

    // Check if inverter power meets appliance power requirements
    public boolean isInverterPowerSufficient(double inverterPower, double totalAppliancePower) {
        return inverterPower > totalAppliancePower;
    }

    // Check if inverter peak power meets appliance peak requirements
    public boolean isInverterPeakPowerSufficient(double inverterPeakPower, double totalPeakAppliancePower) {
        return inverterPeakPower > totalPeakAppliancePower;
    }
}
