package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.SolarPanel;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.project.ProjectSolarPanel;
import com.islandpower.configurator.model.project.Site;
import com.islandpower.configurator.repository.ProjectRepository;
import com.islandpower.configurator.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectSolarPanelService {

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
        double totalDailyEnergyProduction = 0;
        double finalEfficiency = 0.0;

        // Perform calculations for each selected month
        for (Site.MonthlyData monthlyData : monthlyDataList) {
            if (selectedMonths.contains(monthlyData.getMonth())) {
                double psh = monthlyData.getIrradiance(); // Get irradiance (PSH)
                double ambientTemperature = monthlyData.getAmbientTemperature(); // Get ambient temperature

                // Calculate energy required from the battery (equation 13):
                double requiredEnergy = totalDailyEnergyRequired / (batteryEfficiency * cableEfficiency);

                // Calculate required output power from solar panels (equation 14):
                double requiredPower = (requiredEnergy / psh) * panelOversizeCoefficient;

                // Calculate temperature efficiency for solar panels (equation 16):
                double tempEfficiencyFactor = calculateTemperatureEfficiency(selectedPanel, ambientTemperature, installationType);

                // Calculate total panel efficiency (equation 15):
                finalEfficiency = calculateTotalEfficiency(tempEfficiencyFactor, manufacturerTolerance, agingLoss, dirtLoss);

                // Calculate derated power of the solar panel (equation 17):
                double deratedPower = selectedPanel.getpRated() * finalEfficiency;

                // Calculate the number of solar panels required (equation 18):
                int numPanelsRequired = (int) Math.ceil(requiredPower / deratedPower);
                maxPanelsRequired = Math.max(maxPanelsRequired, numPanelsRequired);

                // Calculate the estimated daily energy production by solar panels (equation 19):
                double estimatedDailySolarEnergy = deratedPower * psh;

                // Add monthly calculation data, including PSH and Ambient Temperature
                ProjectSolarPanel.MonthlySolarData monthlySolarData = new ProjectSolarPanel.MonthlySolarData(
                        monthlyData.getMonth(),
                        psh, // Store PSH
                        ambientTemperature, // Store ambient temperature
                        totalDailyEnergyRequired, // This remains constant
                        requiredEnergy,
                        requiredPower,
                        finalEfficiency, // Only the final efficiency is stored
                        deratedPower,
                        numPanelsRequired,
                        estimatedDailySolarEnergy
                );
                monthlyCalculations.add(monthlySolarData);
                totalDailyEnergyProduction += estimatedDailySolarEnergy;
            }
        }

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

        // Save the calculated solar panel configuration, including PSH and ambient temperature
        projectSolarPanel.setSolarPanelId(solarPanelId);
        projectSolarPanel.setNumberOfPanels(maxPanelsRequired);
        projectSolarPanel.setTotalPowerGenerated(selectedPanel.getpRated() * maxPanelsRequired);
        projectSolarPanel.setEfficiencyLoss(1 - finalEfficiency); // Store efficiency loss as 1 - final efficiency
        projectSolarPanel.setEstimatedDailyEnergyProduction(totalDailyEnergyProduction);
        projectSolarPanel.setMonthlyData(monthlyCalculations); // Store monthly data including PSH and ambient temperature

        // Save the project back to the repository
        projectRepository.save(project);

        return projectSolarPanel; // Return the result
    }

    // Method to calculate temperature efficiency (equation 16)
    private double calculateTemperatureEfficiency(SolarPanel solarPanel, double ambientTemperature, String installationType) {
        double tempCoefficientPMax = solarPanel.getTempCoefficientPMax();
        int installationTemperatureIncrease = getInstallationTemperatureIncrease(installationType);
        return (100 + (ambientTemperature + installationTemperatureIncrease - 25) * tempCoefficientPMax) / 100;
    }

    // Method to calculate total efficiency (equation 15)
    private double calculateTotalEfficiency(double tempEfficiency, double manufacturerTolerance, double agingLoss, double dirtLoss) {
        // η_efficiency = η_temp * η_man * η_aging * η_dirt
        return tempEfficiency * manufacturerTolerance * agingLoss * dirtLoss;
    }

    // Fetch the temperature increase based on the installation type (provided table)
    private int getInstallationTemperatureIncrease(String installationType) {
        return switch (installationType) {
            case "ground", "angle_above_20" -> 25;
            case "parallel_gap_above_150" -> 30;
            case "parallel_gap_below_150" -> 35;
            default -> 25; // Default to 25°C for unknown types
        };
    }

    // Method to fetch the solar panel configuration for the project
    public ProjectSolarPanel getProjectSolarPanel(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        return project.getConfigurationModel().getProjectSolarPanel();
    }
}
