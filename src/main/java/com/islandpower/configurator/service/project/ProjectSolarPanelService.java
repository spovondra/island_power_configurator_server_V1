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

@Service
public class ProjectSolarPanelService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectSolarPanelService.class);

    @Autowired
    private SolarPanelRepository solarPanelRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // Fetch suitable solar panels based on project needs
    public List<SolarPanel> getSuitableSolarPanels(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();
        // Add filtering logic if necessary based on project properties
        return solarPanelRepository.findAll(); // Return all solar panels for now
    }

    // Calculate and save the solar panel configuration
    public ProjectSolarPanel calculateSolarPanelConfiguration(String projectId, String solarPanelId,
                                                              double panelOversizeCoefficient, double batteryEfficiency,
                                                              double cableEfficiency, List<Integer> selectedMonths,
                                                              String installationType, double manufacturerTolerance,
                                                              double agingLoss, double dirtLoss) {

        // Fetch the project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        // Fetch the selected solar panel
        SolarPanel selectedPanel = solarPanelRepository.findById(solarPanelId)
                .orElseThrow(() -> new RuntimeException("Solar Panel not found: " + solarPanelId));

        // Get total daily energy required for AC and DC appliances
        double totalDailyEnergyRequired = project.getConfigurationModel().getProjectInverter().getTotalDailyEnergy();

        // Fetch monthly data from site
        List<Site.MonthlyData> monthlyDataList = project.getSite().getMonthlyDataList();
        List<ProjectSolarPanel.MonthlySolarData> monthlyCalculations = new ArrayList<>();

        int maxPanelsRequired = 0;
        double totalDailyEnergySum = 0;
        double finalEfficiency = 0.0;
        int monthsCount = monthlyDataList.size();

        // Perform calculations for each selected month
        for (Site.MonthlyData monthlyData : monthlyDataList) {
            if (selectedMonths.contains(monthlyData.getMonth())) {
                double psh = monthlyData.getIrradiance(); // Get irradiance (PSH)
                double ambientTemperature = monthlyData.getAmbientTemperature(); // Get ambient temperature

                // Calculate energy required from the battery
                double requiredEnergy = totalDailyEnergyRequired / (batteryEfficiency * cableEfficiency);

                // Calculate required output power from solar panels
                double requiredPower = (requiredEnergy / psh) * panelOversizeCoefficient;

                // Calculate temperature efficiency for solar panels
                double tempEfficiencyFactor = calculateTemperatureEfficiency(selectedPanel, ambientTemperature, installationType);

                // Calculate total panel efficiency
                finalEfficiency = calculateTotalEfficiency(tempEfficiencyFactor, manufacturerTolerance, agingLoss, dirtLoss);

                // Calculate derated power of the solar panel
                double deratedPower = selectedPanel.getpRated() * finalEfficiency;

                // Calculate the number of solar panels required
                int numPanelsRequired = (int) Math.ceil(requiredPower / deratedPower);
                maxPanelsRequired = Math.max(maxPanelsRequired, numPanelsRequired);

                // Calculate the estimated daily energy production by solar panels
                double estimatedDailySolarEnergy = deratedPower * psh;

                // Add monthly calculation data, including PSH and Ambient Temperature
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

        double averageDailyProduction = totalDailyEnergySum / monthsCount;

        // Get the configuration model
        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            configModel = new ConfigurationModel();
            project.setConfigurationModel(configModel);
        }

        ProjectSolarPanel projectSolarPanel = configModel.getProjectSolarPanel();
        if (projectSolarPanel == null) {
            projectSolarPanel = new ProjectSolarPanel();
            configModel.setProjectSolarPanel(projectSolarPanel);
        }

        // Save the calculated solar panel configuration, including efficiency and loss factors
        projectSolarPanel.setSolarPanelId(solarPanelId);
        projectSolarPanel.setNumberOfPanels(maxPanelsRequired);
        projectSolarPanel.setTotalPowerGenerated(selectedPanel.getpRated() * maxPanelsRequired);
        projectSolarPanel.setEfficiencyLoss(1 - finalEfficiency); // Store efficiency loss as 1 - final efficiency
        projectSolarPanel.setEstimatedDailyEnergyProduction(averageDailyProduction);
        projectSolarPanel.setPanelOversizeCoefficient(panelOversizeCoefficient);
        projectSolarPanel.setBatteryEfficiency(batteryEfficiency);
        projectSolarPanel.setCableEfficiency(cableEfficiency);
        projectSolarPanel.setManufacturerTolerance(manufacturerTolerance);
        projectSolarPanel.setAgingLoss(agingLoss);
        projectSolarPanel.setDirtLoss(dirtLoss);
        projectSolarPanel.setInstallationType(installationType); // Add installationType to the saved configuration
        projectSolarPanel.setMonthlyData(monthlyCalculations);

        // Log the project solar panel configuration
        logger.info("Calculated solar panel configuration: {}", projectSolarPanel);

        // Save the project back to the repository
        projectRepository.save(project);

        return projectSolarPanel;
    }

    // Method to calculate temperature efficiency
    private double calculateTemperatureEfficiency(SolarPanel solarPanel, double ambientTemperature, String installationType) {
        double tempCoefficientPMax = solarPanel.getTempCoefficientPMax();
        int installationTemperatureIncrease = getInstallationTemperatureIncrease(installationType);
        logger.info("installationTemperatureIncrease {}", installationTemperatureIncrease);
        return (100 + (ambientTemperature + installationTemperatureIncrease - 25) * tempCoefficientPMax) / 100;
    }

    // Method to calculate total efficiency
    private double calculateTotalEfficiency(double tempEfficiency, double manufacturerTolerance, double agingLoss, double dirtLoss) {
        return tempEfficiency * manufacturerTolerance * agingLoss * dirtLoss;
    }

    // Fetch the temperature increase based on the installation type
    private int getInstallationTemperatureIncrease(String installationType) {
        return switch (installationType) {
            case "ground", "roof_angle" -> 25;
            case "parallel_greater_150mm" -> 30;
            case "parallel_less_150mm" -> 35;
            default -> 0; // Default to 25°C for unknown types
        };
    }

    // Method to fetch the solar panel configuration for the project
    public ProjectSolarPanel getProjectSolarPanel(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        return project.getConfigurationModel().getProjectSolarPanel();
    }
}
