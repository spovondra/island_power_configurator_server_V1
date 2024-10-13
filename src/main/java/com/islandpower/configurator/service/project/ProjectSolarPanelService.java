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
                                                              double cableEfficiency, int panelTemperature,
                                                              List<Integer> selectedMonths, String installationType) {

        // Fetch the project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        // Fetch the selected solar panel
        SolarPanel selectedPanel = solarPanelRepository.findById(solarPanelId)
                .orElseThrow(() -> new RuntimeException("Solar Panel not found: " + solarPanelId));

        // Get total daily energy required
        double totalDailyEnergyRequired = project.getConfigurationModel().getProjectInverter().getTotalDailyEnergy();

        // Fetch monthly data from site
        List<Site.MonthlyData> monthlyDataList = project.getSite().getMonthlyDataList();
        List<ProjectSolarPanel.MonthlySolarData> monthlyCalculations = new ArrayList<>();

        int maxPanelsRequired = 0;
        double totalDailyEnergyProduction = 0;

        // Perform calculations for each selected month
        for (Site.MonthlyData monthlyData : monthlyDataList) {
            if (selectedMonths.contains(monthlyData.getMonth())) {
                double psh = monthlyData.getIrradiance(); // Get irradiance (PSH)
                double ambientTemperature = monthlyData.getAmbientTemperature(); // Get ambient temperature

                // Calculate required energy and power
                double requiredEnergy = totalDailyEnergyRequired / (batteryEfficiency * cableEfficiency);
                double requiredPower = (requiredEnergy / psh) * panelOversizeCoefficient;

                // Calculate temperature efficiency factor
                double tempEfficiencyFactor = calculateTemperatureEfficiency(selectedPanel, ambientTemperature);
                double deratedPower = selectedPanel.getpRated() * tempEfficiencyFactor;

                // Calculate number of panels required
                int numPanelsRequired = (int) Math.ceil(requiredPower / deratedPower);
                maxPanelsRequired = Math.max(maxPanelsRequired, numPanelsRequired);

                // Calculate estimated daily solar energy production
                double estimatedDailySolarEnergy = deratedPower * psh;

                // Add monthly calculation data, including PSH and Ambient Temperature
                ProjectSolarPanel.MonthlySolarData monthlySolarData = new ProjectSolarPanel.MonthlySolarData(
                        monthlyData.getMonth(),
                        psh, // Store PSH
                        ambientTemperature, // Store ambient temperature
                        totalDailyEnergyRequired, // This remains constant
                        requiredEnergy,
                        requiredPower,
                        tempEfficiencyFactor,
                        tempEfficiencyFactor, // Efficiency is tempEfficiencyFactor for simplicity
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
        projectSolarPanel.setEfficiencyLoss(0.05); // Example efficiency loss
        projectSolarPanel.setEstimatedDailyEnergyProduction(totalDailyEnergyProduction);
        projectSolarPanel.setMonthlyData(monthlyCalculations); // Store monthly data including PSH and ambient temperature

        // Save the project back to the repository
        projectRepository.save(project);

        return projectSolarPanel; // Return the result
    }

    // Method to calculate temperature efficiency
    private double calculateTemperatureEfficiency(SolarPanel solarPanel, double ambientTemperature) {
        double tempCoefficientPMax = solarPanel.getTempCoefficientPMax();
        return (100 + (ambientTemperature - 25) * tempCoefficientPMax) / 100;
    }

    // Method to fetch the solar panel configuration for the project
    public ProjectSolarPanel getProjectSolarPanel(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        return project.getConfigurationModel().getProjectSolarPanel();
    }
}
