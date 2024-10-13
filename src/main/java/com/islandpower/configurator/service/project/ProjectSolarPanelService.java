package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.SolarPanel;
import com.islandpower.configurator.model.Project;
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

    // Method to fetch suitable solar panels for a project
    public List<SolarPanel> getSuitableSolarPanels(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();

        // Filter the solar panels based on certain project characteristics (if needed).
        return solarPanelRepository.findAll(); // Returning all for now, filtering logic can be added here.
    }

    // Method to calculate solar panel configuration
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
        monthlyDataList.forEach(monthlyData -> {
            System.out.println("Month: " + monthlyData.getMonth());
            System.out.println("Irradiance (PSH): " + monthlyData.getIrradiance());
            System.out.println("Ambient Temperature: " + monthlyData.getAmbientTemperature());
        });

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

                // Log the retrieved PSH and ambient temperature
                System.out.println("PSH for month " + monthlyData.getMonth() + ": " + psh);
                System.out.println("Ambient Temperature for month " + monthlyData.getMonth() + ": " + ambientTemperature);
                System.out.println("Required Energy: " + requiredEnergy);
                System.out.println("Required Power: " + requiredPower);

                // Calculate temperature efficiency factor
                double tempEfficiencyFactor = calculateTemperatureEfficiency(selectedPanel, ambientTemperature);
                double deratedPower = selectedPanel.getpRated() * tempEfficiencyFactor;

                // Calculate number of panels required
                int numPanelsRequired = (int) Math.ceil(requiredPower / deratedPower);
                maxPanelsRequired = Math.max(maxPanelsRequired, numPanelsRequired);

                // Calculate estimated daily solar energy production
                double estimatedDailySolarEnergy = deratedPower * psh;

                // Add monthly calculation data
                ProjectSolarPanel.MonthlySolarData monthlySolarData = new ProjectSolarPanel.MonthlySolarData(
                        monthlyData.getMonth(),
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

        // Prepare the final solar panel configuration data

        return new ProjectSolarPanel(
                solarPanelId,
                maxPanelsRequired,
                selectedPanel.getpRated() * maxPanelsRequired,
                0.0,
                0.05, // Example efficiency loss
                totalDailyEnergyProduction,
                monthlyCalculations
        ); // Return the result, but don't save it in the database
    }

    // Method to calculate temperature efficiency
    private double calculateTemperatureEfficiency(SolarPanel solarPanel, double ambientTemperature) {
        double tempCoefficientPMax = solarPanel.getTempCoefficientPMax();
        return (100 + (ambientTemperature - 25) * tempCoefficientPMax) / 100;
    }

    // Method to fetch the project solar panel configuration
    public ProjectSolarPanel getProjectSolarPanel(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        return project.getConfigurationModel().getProjectSolarPanel();
    }
}
