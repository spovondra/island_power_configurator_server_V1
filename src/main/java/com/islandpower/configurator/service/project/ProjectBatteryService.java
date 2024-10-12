package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.Battery;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.repository.BatteryRepository;
import com.islandpower.configurator.repository.ProjectRepository;
import com.islandpower.configurator.dto.BatteryConfigurationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public BatteryConfigurationResponse selectBattery(String projectId, String batteryId, int autonomyDays, int temperature) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            throw new RuntimeException("Configuration model not found for project: " + projectId);
        }

        configModel.setBatteryId(batteryId); // Nastav battery ID
        projectRepository.save(project);

        return calculateBatteryConfiguration(projectId, batteryId, autonomyDays, temperature);
    }

    private static final Map<String, Map<Integer, Double>> temperatureCoefficients = Map.of(
            "Li-ion", Map.of(
                    -30, 0.52, -20, 0.51, -10, 0.70, 0, 0.82,
                    10, 0.89, 20, 0.93, 25, 1.00, 30, 1.00, 40, 1.00
            ),
            "LiFePO4", Map.of(
                    -10, 0.75, 0, 0.91, 10, 0.97, 20, 1.01,
                    25, 1.00, 30, 1.02, 40, 1.02
            ),
            "Lead Acid", Map.of(
                    -30, 0.52, -20, 0.64, -10, 0.76, 0, 0.85,
                    10, 0.92, 20, 0.98, 25, 1.00, 30, 1.02, 40, 1.04
            )
    );

    public BatteryConfigurationResponse calculateBatteryConfiguration(String projectId, String batteryId, int autonomyDays, int temperature) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        Battery selectedBattery = getSelectedBattery(batteryId);

        // Získání teplotního koeficientu
        double temperatureCoefficient = getTemperatureCoefficient(selectedBattery.getType(), temperature);
        double totalDailyEnergy = configModel.getTotalDailyEnergy();
        double systemVoltage = configModel.getSystemVoltage();

        double batteryVoltage = selectedBattery.getVoltage();

        // Výpočet požadované kapacity
        double requiredCapacity = (totalDailyEnergy / systemVoltage) * autonomyDays * (1 / temperatureCoefficient); // Ah
        double batteryCapacityDOD = selectedBattery.getCapacity() * selectedBattery.getDod();

        // Výpočet počtu paralelních a sériových baterií
        int parallelBatteries = (int) Math.ceil(requiredCapacity / batteryCapacityDOD);
        int seriesBatteries = (int) Math.ceil(systemVoltage / batteryVoltage);

        // Výpočet celkové dostupné kapacity a provozních dnů
        double totalAvailableCapacity = (batteryCapacityDOD * parallelBatteries); // Celková dostupná kapacita
        double operationalDays = totalAvailableCapacity / ((totalDailyEnergy / systemVoltage) * (1 / temperatureCoefficient));

        // Uložení vypočítaných hodnot do modelu
        configModel.setParallelBatteries(parallelBatteries);
        configModel.setSeriesBatteries(seriesBatteries);
        configModel.setRequiredBatteryCapacity(requiredCapacity);
        configModel.setBatteryAutonomy(operationalDays);

        projectRepository.save(project);

        // Instead of returning the BatteryConfigurationResponse, you can log the values or handle them differently
        logger.info("Battery configuration calculated for project: {}, battery: {}, parallel: {}, series: {}, requiredCapacity: {}, operationalDays: {}, totalCapacity: {}",
                projectId, batteryId, parallelBatteries, seriesBatteries, requiredCapacity, operationalDays, totalAvailableCapacity);

        // Vrácení vypočítaných hodnot v odpovědi
        return new BatteryConfigurationResponse(batteryId, batteryCapacityDOD, parallelBatteries, seriesBatteries, requiredCapacity, totalAvailableCapacity, operationalDays);
    }

    private double getTemperatureCoefficient(String batteryType, int temperature) {
        Map<Integer, Double> coefficientsForType = temperatureCoefficients.get(batteryType);
        if (coefficientsForType == null) {
            throw new RuntimeException("Unknown battery type: " + batteryType);
        }
        return coefficientsForType.getOrDefault(temperature, 1.0); // Defaultní koeficient je 1.0
    }

    public Battery getSelectedBattery(String batteryId) {
        return batteryRepository.findById(batteryId)
                .orElseThrow(() -> new RuntimeException("Battery not found: " + batteryId));
    }
}