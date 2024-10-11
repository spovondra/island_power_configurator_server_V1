package com.islandpower.configurator.service.project;

import com.islandpower.configurator.dto.BatteryConfigurationResponse;
import com.islandpower.configurator.model.Battery;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.repository.BatteryRepository;
import com.islandpower.configurator.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjectBatteryService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectBatteryService.class);
    private final BatteryRepository batteryRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectBatteryService(BatteryRepository batteryRepository, ProjectRepository projectRepository) {
        this.batteryRepository = batteryRepository;
        this.projectRepository = projectRepository;
    }

    // Add or update a battery in a project
    public Project addOrUpdateBattery(String projectId, Battery battery) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        if (battery.getId() == null || battery.getId().isEmpty()) {
            battery.setId(UUID.randomUUID().toString());
        }

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            configModel = new ConfigurationModel();
            project.setConfigurationModel(configModel);
        }

        configModel.setBatteryId(battery.getId());
        projectRepository.save(project);
        return project;
    }

    // Remove a battery from a project
    public void removeBattery(String projectId, String batteryId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel != null) {
            configModel.setBatteryId(null);
        }

        projectRepository.save(project);
    }

    public List<Battery> getSuitableBatteries(String projectId, String technology) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();

        return batteryRepository.findAll().stream()
                .filter(battery -> isSuitableBattery(battery, systemVoltage, technology))
                .collect(Collectors.toList());
    }

    private boolean isSuitableBattery(Battery battery, double systemVoltage, String type) {
        // Implement your filtering logic here based on battery attributes
        // Example conditions (customize as needed):
        boolean voltageMatch = (battery.getVoltage() <= systemVoltage);
        boolean technologyMatch = battery.getType().equalsIgnoreCase(type); // Assuming Battery has a getTechnology() method

        return voltageMatch && technologyMatch;
    }


    public void selectBattery(String projectId, String batteryId, int temperature, int autonomyDays) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            throw new RuntimeException("Configuration model not found for project: " + projectId);
        }

        Battery selectedBattery = getSelectedBattery(batteryId);
        configModel.setBatteryId(batteryId); // Set battery ID

        // Calculate required battery configuration
        calculateBatteryConfiguration(projectId, batteryId, autonomyDays, temperature);

        projectRepository.save(project);
    }

    public BatteryConfigurationResponse calculateBatteryConfiguration(String projectId, String batteryId, int autonomyDays, int temperature) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();

        Battery selectedBattery = getSelectedBattery(batteryId);
        double realBatteryCapacity = selectedBattery.getCapacity() * selectedBattery.getDod();

        // Total daily energy in Ah (to be defined in the ConfigurationModel)
        double totalDailyEnergy = configModel.getTotalDailyEnergy(); // Assuming this value is set elsewhere

        // Required capacity for the number of autonomy days in Ah
        double requiredCapacity = totalDailyEnergy * autonomyDays;

        // Calculate number of parallel and series batteries needed
        int parallelBatteries = (int) Math.ceil(requiredCapacity / realBatteryCapacity); // Required parallel batteries
        int seriesBatteries = (int) Math.ceil(configModel.getSystemVoltage()/ selectedBattery.getVoltage());

        // Store results in the configuration model
        configModel.setRequiredBatteryCapacity(requiredCapacity);
        configModel.setParallelBatteries(parallelBatteries);
        configModel.setSeriesBatteries(seriesBatteries);
        configModel.setBatteryAutonomy((realBatteryCapacity * parallelBatteries) / totalDailyEnergy); // Update autonomy

        // Calculate battery capacity DOD and operational voltage if needed
        double batteryVoltage = selectedBattery.getVoltage(); // Replace with actual method to get voltage

        logger.info("Selected Battery: {}", selectedBattery);
        logger.info("Required Capacity: {} Ah", requiredCapacity);
        logger.info("Parallel Batteries: {}", parallelBatteries);
        logger.info("Series Batteries: {}", seriesBatteries);
        logger.info("Battery Autonomy: {}", configModel.getBatteryAutonomy());

        // Return the configuration response
        return new BatteryConfigurationResponse(projectId, parallelBatteries, seriesBatteries, requiredCapacity, realBatteryCapacity, batteryVoltage);
    }


    public Battery getSelectedBattery(String batteryId) {
        return batteryRepository.findById(batteryId)
                .orElseThrow(() -> new RuntimeException("Battery not found: " + batteryId));
    }
}
