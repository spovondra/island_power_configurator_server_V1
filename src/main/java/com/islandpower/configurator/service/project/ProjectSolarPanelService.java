package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.SolarPanel;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.project.ProjectSolarPanel;
import com.islandpower.configurator.model.project.Site;
import com.islandpower.configurator.repository.ProjectRepository;
import com.islandpower.configurator.repository.SolarPanelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing solar panels in a project configuration.
 * Provides methods for selecting suitable solar panels, calculating configuration details, and storing results.
 */
@Service
public class ProjectSolarPanelService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectSolarPanelService.class);

    @Autowired
    private SolarPanelRepository solarPanelRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private static final double T_STC = 25;

    /**
     * Fetches suitable solar panels for a project based on system voltage.
     * @param projectId The ID of the project
     * @return List of suitable solar panels
     */
    public List<SolarPanel> getSuitableSolarPanels(String projectId) {
        return solarPanelRepository.findAll();
    }

    /**
     * Calculates the solar panel configuration for the project and saves it to the database.
     * @param projectId The ID of the project
     * @param solarPanelId The ID of the selected solar panel
     * @param panelOversizeCoefficient The coefficient for panel oversizing
     * @param batteryEfficiency The efficiency of the battery
     * @param cableEfficiency The efficiency of the cables
     * @param selectedMonths The list of selected months for energy calculations
     * @param installationType The type of installation (e.g., roof, ground)
     * @param manufacturerTolerance The tolerance factor for the solar panel manufacturer
     * @param agingLoss The loss factor due to aging of the panel
     * @param dirtLoss The loss factor due to dirt accumulation on the panels
     * @return ProjectSolarPanel The calculated solar panel configuration
     */
    public ProjectSolarPanel calculateSolarPanelConfiguration(String projectId, String solarPanelId,
                                                              double panelOversizeCoefficient, double batteryEfficiency,
                                                              double cableEfficiency, List<Integer> selectedMonths,
                                                              String installationType, double manufacturerTolerance,
                                                              double agingLoss, double dirtLoss) {

        /* fetch the project */
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        /* fetch the selected solar panel */
        SolarPanel selectedPanel = solarPanelRepository.findById(solarPanelId)
                .orElseThrow(() -> new RuntimeException("Solar Panel not found: " + solarPanelId));

        /* fetch ConfigurationModel and ensure it is not null */
        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            configModel = new ConfigurationModel(); // Initialize ConfigurationModel if null
            project.setConfigurationModel(configModel);
        }

        /* rnsure ProjectSolarPanel exists */
        ProjectSolarPanel projectSolarPanel = configModel.getProjectSolarPanel();
        if (projectSolarPanel == null) {
            projectSolarPanel = new ProjectSolarPanel(); // Initialize ProjectSolarPanel if null
            configModel.setProjectSolarPanel(projectSolarPanel);
        }

        /* get total daily energy required for AC and DC appliances */
        double totalDailyEnergyRequired = project.getConfigurationModel().getProjectInverter().getTotalDailyEnergy();

        /* fetch monthly data from site */
        List<Site.MonthlyData> monthlyDataList = project.getSite().getMonthlyDataList();
        List<ProjectSolarPanel.MonthlySolarData> monthlyCalculations = new ArrayList<>();

        int maxPanelsRequired = 0;
        double totalDailyEnergySum = 0;
        double finalEfficiency = 0.0;
        int monthsCount = monthlyDataList.size();

        /* fetch ProjectBattery configuration */
        if (project.getConfigurationModel().getProjectBattery() == null) {
            throw new RuntimeException("ProjectBattery configuration not found for project: " + projectId);
        }

        double maxChargingPower = project.getConfigurationModel().getProjectBattery().getMaxChargingPower();
        double optimalChargingPower = project.getConfigurationModel().getProjectBattery().getOptimalChargingPower();

        /* perform calculations for each selected month */
        for (Site.MonthlyData monthlyData : monthlyDataList) {
            if (selectedMonths.contains(monthlyData.getMonth())) {
                double psh = monthlyData.getIrradiance();
                double ambientTemperature = monthlyData.getAmbientTemperature();

                /* calculate required energy  from the battery */
                double requiredEnergy = totalDailyEnergyRequired / (batteryEfficiency * cableEfficiency);

                /* calculate required output power from solar panels */
                double requiredPower = (requiredEnergy / psh) * panelOversizeCoefficient;

                /* validate power requirements with battery constraints */
                String statusMessage = getString(requiredPower, maxChargingPower, optimalChargingPower);
                project.getConfigurationModel().getProjectSolarPanel().setStatusMessage(statusMessage);

                /* calculate temperature efficiency for solar panels */
                double tempEfficiencyFactor = calculateTemperatureEfficiency(selectedPanel, ambientTemperature, installationType);

                /* calculate total panel efficiency */
                finalEfficiency = calculateTotalEfficiency(tempEfficiencyFactor, manufacturerTolerance, agingLoss, dirtLoss);

                /* calculate derated power of the solar panel */
                double deratedPower = selectedPanel.getpRated() * finalEfficiency;

                /* calculate the number of solar panels required */
                int numPanelsRequired = (int) Math.ceil(requiredPower / deratedPower);
                maxPanelsRequired = Math.max(maxPanelsRequired, numPanelsRequired);

                /* calculate the estimated daily energy production by solar panels */
                double estimatedDailySolarEnergy = deratedPower * psh;

                /* add monthly calculation data */
                ProjectSolarPanel.MonthlySolarData monthlySolarData = new ProjectSolarPanel.MonthlySolarData(
                        monthlyData.getMonth(),
                        psh,
                        ambientTemperature,
                        totalDailyEnergyRequired,
                        requiredEnergy,
                        requiredPower,
                        finalEfficiency,
                        deratedPower,
                        numPanelsRequired,
                        estimatedDailySolarEnergy
                );
                monthlyCalculations.add(monthlySolarData);
                totalDailyEnergySum += estimatedDailySolarEnergy;
            }
        }

        /* calculate average daily energy production */
        double averageDailyProduction = totalDailyEnergySum / monthsCount;

        /* save the calculated solar panel configuration, including efficiency and loss factors */
        projectSolarPanel.setSolarPanelId(solarPanelId);
        projectSolarPanel.setNumberOfPanels(maxPanelsRequired);
        projectSolarPanel.setTotalPowerGenerated(selectedPanel.getpRated() * maxPanelsRequired);
        projectSolarPanel.setEfficiencyLoss(1 - finalEfficiency);
        projectSolarPanel.setEstimatedDailyEnergyProduction(averageDailyProduction);
        projectSolarPanel.setPanelOversizeCoefficient(panelOversizeCoefficient);
        projectSolarPanel.setBatteryEfficiency(batteryEfficiency);
        projectSolarPanel.setCableEfficiency(cableEfficiency);
        projectSolarPanel.setManufacturerTolerance(manufacturerTolerance);
        projectSolarPanel.setAgingLoss(agingLoss);
        projectSolarPanel.setDirtLoss(dirtLoss);
        projectSolarPanel.setInstallationType(installationType);
        projectSolarPanel.setMonthlyData(monthlyCalculations);

        /* save the project back to the repository */
        projectRepository.save(project);

        return projectSolarPanel;
    }

    /**
     * Validates the power requirements based on the battery constraints.
     * @param requiredPower The required power to charge the battery
     * @param maxChargingPower The maximum charging power of the battery
     * @param optimalChargingPower The optimal charging power of the battery
     * @return Status message indicating success or error
     */
    private static String getString(double requiredPower, double maxChargingPower, double optimalChargingPower) {
        String statusMessage = "Configuration calculated successfully.";
        if (requiredPower > maxChargingPower) {
            statusMessage = String.format("Error: Required power (%.2f W) exceeds max charging power (%.2f W).", requiredPower, maxChargingPower);
        } else if (Math.abs(requiredPower - optimalChargingPower) > 0.1 * optimalChargingPower) {
            statusMessage = String.format("Warning: Required power (%.2f W) significantly differs from optimal charging power (%.2f W).", requiredPower, optimalChargingPower);
        }
        return statusMessage;
    }

    /**
     * Calculates the temperature efficiency of the solar panel based on the ambient temperature and installation type.
     * @param solarPanel The selected solar panel
     * @param ambientTemperature The ambient temperature
     * @param installationType The type of installation
     * @return The temperature efficiency factor
     */
    private double calculateTemperatureEfficiency(SolarPanel solarPanel, double ambientTemperature, String installationType) {
        double tempCoefficientPMax = solarPanel.getTempCoefficientPMax();
        int installationTemperatureIncrease = getInstallationTemperatureIncrease(installationType);
        logger.info("installationTemperatureIncrease {}", installationTemperatureIncrease);
        return (100 + (ambientTemperature + installationTemperatureIncrease - T_STC) * tempCoefficientPMax) / 100;
    }

    /**
     * Calculates the total efficiency of the solar panel including temperature, manufacturer tolerance, aging loss, and dirt loss.
     * @param tempEfficiency The temperature efficiency factor
     * @param manufacturerTolerance The tolerance factor for the solar panel manufacturer
     * @param agingLoss The loss factor due to aging of the panel
     * @param dirtLoss The loss factor due to dirt accumulation on the panels
     * @return The total efficiency of the panel
     */
    private double calculateTotalEfficiency(double tempEfficiency, double manufacturerTolerance, double agingLoss, double dirtLoss) {
        return tempEfficiency * manufacturerTolerance * agingLoss * dirtLoss;
    }

    /**
     * Fetches the temperature increase based on the installation type.
     * @param installationType The type of installation (e.g., ground, roof_angle)
     * @return The temperature increase (in °C)
     */
    private int getInstallationTemperatureIncrease(String installationType) {
        return switch (installationType) {
            case "ground", "roof_angle" -> 25;
            case "parallel_greater_150mm" -> 30;
            case "parallel_less_150mm" -> 35;
            default -> 0; //default to 25°C for unknown types
        };
    }

    /**
     * Fetches the solar panel configuration for the specified project.
     * @param projectId The ID of the project
     * @return The ProjectSolarPanel configuration for the project
     */
    public ProjectSolarPanel getProjectSolarPanel(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        return project.getConfigurationModel().getProjectSolarPanel();
    }
}
