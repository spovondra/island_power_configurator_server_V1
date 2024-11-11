package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.Inverter;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.project.ProjectInverter;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.repository.InverterRepository;
import com.islandpower.configurator.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectInverterService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectInverterService.class);
    private final InverterRepository inverterRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectInverterService(InverterRepository inverterRepository, ProjectRepository projectRepository) {
        this.inverterRepository = inverterRepository;
        this.projectRepository = projectRepository;
    }

    // Remove inverter from a project
    public void removeInverter(String projectId, String inverterId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel != null && configModel.getProjectInverter() != null) {
            configModel.getProjectInverter().setInverterId(null); // Remove the inverter ID
        }

        projectRepository.save(project);
    }

    // Fetch and filter inverters based on power requirements
    public List<Inverter> getSuitableInverters(double systemVoltage, double temperature,
                                                  double totalAppliancePower, double totalPeakAppliancePower) {
        List<Inverter> allInverters = inverterRepository.findAll();
        List<Inverter> suitableInverterDTOs = new ArrayList<>();

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
            if (isInverterPowerSufficient(continuousPower, totalAppliancePower)
                    && isInverterPeakPowerSufficient(peakPower, totalPeakAppliancePower)) {
                suitableInverterDTOs.add(inverter);
            }
        }
        return suitableInverterDTOs;
    }

    // Get continuous power based on temperature
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

    public ProjectInverter selectInverter(String projectId, String inverterId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            throw new RuntimeException("Configuration model not found for project: " + projectId);
        }

        ProjectInverter projectInverter = configModel.getProjectInverter();
        if (projectInverter == null) {
            projectInverter = new ProjectInverter();
            configModel.setProjectInverter(projectInverter);
        }

        // Set the selected inverter ID
        projectInverter.setInverterId(inverterId);

        // Fetch the selected inverter
        Inverter selectedInverter = getSelectedInverter(inverterId);

        // Perform energy calculations after selecting the inverter
        calculateEnergyValues(project, selectedInverter);

        // Save the project
        projectRepository.save(project);

        // Return the updated ProjectInverter
        return projectInverter;
    }

    // Calculate energy values for the project based on selected inverter
    private void calculateEnergyValues(Project project, Inverter selectedInverter) {
        ConfigurationModel configModel = project.getConfigurationModel();
        ProjectInverter projectInverter = configModel.getProjectInverter();

        double totalDailyDCEnergy = configModel.getProjectAppliance().getTotalDcEnergy();
        double totalDailyACEnergy = configModel.getProjectAppliance().getTotalAcEnergy();

        logger.info("Total Daily DC Energy: {}", totalDailyDCEnergy);
        logger.info("Total Daily AC Energy: {}", totalDailyACEnergy);

        double inverterEfficiency = selectedInverter.getEfficiency() / 100.0;
        logger.info("Inverter Efficiency: {}%", selectedInverter.getEfficiency());

        // Calculate adjusted AC energy
        double totalAdjustedAcEnergy = totalDailyACEnergy / inverterEfficiency;
        double totalDailyEnergy = totalDailyDCEnergy + totalAdjustedAcEnergy;

        logger.info("Total Adjusted AC Energy: {}", totalAdjustedAcEnergy);
        logger.info("Total Daily Energy: {}", totalDailyEnergy);

        // Set values in ProjectInverter
        projectInverter.setTotalAdjustedAcEnergy(totalAdjustedAcEnergy);
        projectInverter.setTotalDailyEnergy(totalDailyEnergy);

        projectRepository.save(project);
        logger.info("Project updated successfully with inverter calculations for project ID: {}", project.getId());
    }

    // Fetch selected inverter by ID
    public Inverter getSelectedInverter(String inverterId) {
        return inverterRepository.findById(inverterId)
                .orElseThrow(() -> new RuntimeException("Inverter not found: " + inverterId));
    }

    public ProjectInverter getProjectInverter(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            logger.error("ConfigurationModel is missing for project ID: {}", projectId);
            throw new RuntimeException("Configuration model not found for project: " + projectId);
        }

        ProjectInverter projectInverter = configModel.getProjectInverter();
        if (projectInverter == null) {
            logger.warn("ProjectInverter not found for project ID: {}", projectId);
            throw new RuntimeException("ProjectInverter not found for project: " + projectId);
        }

        logger.info("ProjectInverter successfully retrieved for project ID: {}", projectId);
        return projectInverter;
    }

}
