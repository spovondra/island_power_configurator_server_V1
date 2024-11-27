package com.islandpower.configurator.service.project;

import com.islandpower.configurator.exceptions.InverterNotFoundException;
import com.islandpower.configurator.exceptions.ProjectNotFoundException;
import com.islandpower.configurator.model.Inverter;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.project.ProjectInverter;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.repository.InverterRepository;
import com.islandpower.configurator.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing inverters in a project configuration.
 */
@Service
public class ProjectInverterService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectInverterService.class);
    private final InverterRepository inverterRepository;
    private final ProjectRepository projectRepository;

    /**
     * Constructs a ProjectInverterService.
     * @param inverterRepository repository for managing inverters.
     * @param projectRepository repository for managing projects.
     */
    public ProjectInverterService(InverterRepository inverterRepository, ProjectRepository projectRepository) {
        this.inverterRepository = inverterRepository;
        this.projectRepository = projectRepository;
    }

    /**
     * Removes an inverter from the specified project.
     * @param projectId ID of the project.
     * @param inverterId ID of the inverter to remove.
     * @throws ProjectNotFoundException if the project is not found.
     */
    public void removeInverter(String projectId, String inverterId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel != null && configModel.getProjectInverter() != null) {
            configModel.getProjectInverter().setInverterId(null); // Remove the inverter ID
        }

        projectRepository.save(project);
    }

    /**
     * Retrieves a list of suitable inverters based on system voltage, temperature, and power requirements.
     * @param systemVoltage system voltage of the project.
     * @param temperature installation temperature.
     * @param totalAppliancePower total appliance power requirement (continuous).
     * @param totalPeakAppliancePower total appliance peak power requirement.
     * @return list of suitable inverters.
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
     * @param inverter the inverter to evaluate.
     * @param temperature installation temperature.
     * @return continuous power rating at the specified temperature.
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
     * @param inverterPower inverter's continuous power rating.
     * @param totalAppliancePower total continuous power requirement of appliances.
     * @param inverterEfficiency efficiency of the inverter.
     * @return true if sufficient, otherwise false.
     */
    public boolean isInverterPowerSufficient(double inverterPower, double totalAppliancePower, double inverterEfficiency) {
        return inverterPower > totalAppliancePower / inverterEfficiency;
    }

    /**
     * Checks if the inverter's peak power is sufficient for the appliance peak load.
     * @param inverterPeakPower inverter's peak power rating.
     * @param totalPeakAppliancePower total peak power requirement of appliances.
     * @param inverterEfficiency efficiency of the inverter.
     * @return true if sufficient, otherwise false.
     */
    public boolean isInverterPeakPowerSufficient(double inverterPeakPower, double totalPeakAppliancePower, double inverterEfficiency) {
        return inverterPeakPower > totalPeakAppliancePower / inverterEfficiency;
    }

    /**
     * Selects an inverter for the specified project and calculates energy requirements.
     * @param projectId ID of the project.
     * @param inverterId ID of the inverter to select.
     * @return the selected ProjectInverter object.
     * @throws ProjectNotFoundException if the project is not found.
     * @throws InverterNotFoundException if the inverter is not found.
     */
    public ProjectInverter selectInverter(String projectId, String inverterId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

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
        Inverter selectedInverter = getSelectedInverter(inverterId);

        calculateEnergyValues(project, selectedInverter);
        projectRepository.save(project);

        return projectInverter;
    }

    /**
     * Calculates energy values for the project based on the selected inverter.
     * @param project the project for which energy values are calculated.
     * @param selectedInverter the selected inverter.
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
     * @param inverterId ID of the inverter.
     * @return the selected inverter object.
     * @throws InverterNotFoundException if the inverter is not found.
     */
    public Inverter getSelectedInverter(String inverterId) {
        return inverterRepository.findById(inverterId)
                .orElseThrow(() -> new InverterNotFoundException(inverterId));
    }

    /**
     * Retrieves the inverter configuration for a specific project.
     * @param projectId ID of the project.
     * @return ProjectInverter object, or an empty object if not configured.
     * @throws ProjectNotFoundException if the project is not found.
     */
    public ProjectInverter getProjectInverter(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (project.getConfigurationModel() == null || project.getConfigurationModel().getProjectInverter() == null) {
            return new ProjectInverter(); // Returns an empty object instead of null
        }

        return project.getConfigurationModel().getProjectInverter();
    }
}
