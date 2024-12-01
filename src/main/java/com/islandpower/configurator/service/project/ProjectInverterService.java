package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.Inverter;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.project.ProjectInverter;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.repository.InverterRepository;
import com.islandpower.configurator.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service for managing inverters in a project configuration.
 * Provides methods for selecting, configuring, and retrieving inverters, and calculating energy requirements.
 */
@Service
public class ProjectInverterService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectInverterService.class);
    private final InverterRepository inverterRepository;
    private final ProjectRepository projectRepository;

    /**
     * Constructs a ProjectInverterService.
     * @param inverterRepository Repository for managing inverters
     * @param projectRepository Repository for managing projects
     */
    public ProjectInverterService(InverterRepository inverterRepository, ProjectRepository projectRepository) {
        this.inverterRepository = inverterRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * Removes an inverter from the specified project by clearing its ID in the configuration model.
     * @param projectId ID of the project
     */
    public void removeInverter(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project with ID " + projectId + " was not found"));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel != null && configModel.getProjectInverter() != null) {
            configModel.getProjectInverter().setInverterId(null);
        }

        projectRepository.save(project);
    }

    /**
     * Retrieves a list of suitable inverters based on system voltage, temperature, and power requirements.
     * @param systemVoltage System voltage of the project
     * @param temperature Installation temperature
     * @param totalAppliancePower Total continuous power requirement of appliances
     * @param totalPeakAppliancePower Total peak power requirement of appliances
     * @return List of suitable inverters
     */
    public List<Inverter> getSuitableInverters(double systemVoltage, double temperature,
                                               double totalAppliancePower, double totalPeakAppliancePower) {
        List<Inverter> allInverters = inverterRepository.findAll();
        List<Inverter> suitableInverters = new ArrayList<>();

        for (Inverter inverter : allInverters) {
            if (inverter.getVoltage() != systemVoltage) {
                continue;
            }

            double continuousPower = getContinuousPowerByTemperature(inverter, temperature);
            double peakPower = inverter.getMaxPower();
            double inverterEfficiency = inverter.getEfficiency();

            if (isInverterPowerSufficient(continuousPower, totalAppliancePower, inverterEfficiency)
                    && isInverterPeakPowerSufficient(peakPower, totalPeakAppliancePower, inverterEfficiency)) {
                suitableInverters.add(inverter);
            }
        }
        return suitableInverters;
    }

    /**
     * Retrieves the continuous power rating of an inverter based on the installation temperature.
     * @param inverter The inverter to evaluate
     * @param temperature Installation temperature
     * @return Continuous power rating at the specified temperature
     */
    private double getContinuousPowerByTemperature(Inverter inverter, double temperature) {
        if (temperature == 25) {
            return inverter.getContinuousPower25C();
        } else if (temperature == 40) {
            return inverter.getContinuousPower40C();
        } else {
            return inverter.getContinuousPower65C();
        }
    }

    /**
     * Checks if the inverter's continuous power is sufficient for the appliance load.
     * @param inverterPower Inverter's continuous power rating
     * @param totalAppliancePower Total continuous power requirement of appliances
     * @param inverterEfficiency Efficiency of the inverter
     * @return true if sufficient, otherwise false
     */
    public boolean isInverterPowerSufficient(double inverterPower, double totalAppliancePower, double inverterEfficiency) {
        return inverterPower > totalAppliancePower / inverterEfficiency;
    }

    /**
     * Checks if the inverter's peak power is sufficient for the appliance peak load.
     * @param inverterPeakPower Inverter's peak power rating
     * @param totalPeakAppliancePower Total peak power requirement of appliances
     * @param inverterEfficiency Efficiency of the inverter
     * @return true if sufficient, otherwise false
     */
    public boolean isInverterPeakPowerSufficient(double inverterPeakPower, double totalPeakAppliancePower, double inverterEfficiency) {
        return inverterPeakPower > totalPeakAppliancePower / inverterEfficiency;
    }

    /**
     * Selects an inverter for the specified project and calculates energy requirements.
     * @param projectId ID of the project
     * @param inverterId ID of the inverter to select
     * @return ProjectInverter The selected ProjectInverter object
     */
    public ProjectInverter selectInverter(String projectId, String inverterId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project with ID " + projectId + " was not found"));

        ProjectInverter projectInverter = getProjectInverter(projectId, inverterId, project);
        Inverter selectedInverter = getSelectedInverter(inverterId);

        calculateEnergyValues(project, selectedInverter);
        projectRepository.save(project);

        return projectInverter;
    }

    /**
     * Retrieves or initializes the ProjectInverter object for a given project.
     * @param projectId ID of the project
     * @param inverterId ID of the selected inverter
     * @param project The project instance
     * @return ProjectInverter The retrieved or newly initialized ProjectInverter object
     */
    private static ProjectInverter getProjectInverter(String projectId, String inverterId, Project project) {
        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            throw new IllegalArgumentException("Configuration model not found for project: " + projectId);
        }

        ProjectInverter projectInverter = configModel.getProjectInverter();
        if (projectInverter == null) {
            projectInverter = new ProjectInverter();
            configModel.setProjectInverter(projectInverter);
        }

        projectInverter.setInverterId(inverterId);
        return projectInverter;
    }

    /**
     * Calculates energy values for the project based on the selected inverter.
     * @param project The project for which energy values are calculated
     * @param selectedInverter The selected inverter
     */
    private void calculateEnergyValues(Project project, Inverter selectedInverter) {
        ConfigurationModel configModel = project.getConfigurationModel();
        ProjectInverter projectInverter = configModel.getProjectInverter();

        double totalDailyDCEnergy = configModel.getProjectAppliance().getTotalDcEnergy();
        double totalDailyACEnergy = configModel.getProjectAppliance().getTotalAcEnergy();

        double inverterEfficiency = selectedInverter.getEfficiency() / 100.0;
        double totalAdjustedAcEnergy = totalDailyACEnergy / inverterEfficiency;
        double totalDailyEnergy = totalDailyDCEnergy + totalAdjustedAcEnergy;

        projectInverter.setTotalAdjustedAcEnergy(totalAdjustedAcEnergy);
        projectInverter.setTotalDailyEnergy(totalDailyEnergy);

        logger.info("Project [{}]: Adjusted AC energy: {}, Total daily energy: {}",
                project.getId(), totalAdjustedAcEnergy, totalDailyEnergy);
    }

    /**
     * Retrieves the selected inverter by its ID.
     * @param inverterId ID of the inverter
     * @return Inverter The selected inverter object
     * @throws NoSuchElementException if the inverter is not found
     */
    public Inverter getSelectedInverter(String inverterId) {
        return inverterRepository.findById(inverterId)
                .orElseThrow(() -> new NoSuchElementException("Inverter with ID " + inverterId + " was not found"));
    }

    /**
     * Retrieves the inverter configuration for a specific project.
     * @param projectId ID of the project
     * @return ProjectInverter The ProjectInverter object, or an empty object if not configured
     */
    public ProjectInverter getProjectInverter(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project with ID " + projectId + " was not found"));

        if (project.getConfigurationModel() == null || project.getConfigurationModel().getProjectInverter() == null) {
            return new ProjectInverter();
        }

        return project.getConfigurationModel().getProjectInverter();
    }
}
