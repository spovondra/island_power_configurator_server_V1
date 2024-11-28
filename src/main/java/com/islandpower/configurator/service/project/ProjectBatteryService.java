package com.islandpower.configurator.service.project;

import com.islandpower.configurator.model.Battery;
import com.islandpower.configurator.model.project.ConfigurationModel;
import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.ProjectBattery;
import com.islandpower.configurator.repository.BatteryRepository;
import com.islandpower.configurator.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class to handle operations related to project batteries,
 * including selection, configuration, and calculations.
 */
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

    /**
     * Removes a battery from the project by clearing its ID.
     *
     * @param projectId the ID of the project
     */
    public void removeBattery(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel != null && configModel.getProjectBattery() != null) {
            configModel.getProjectBattery().setBatteryId(null);
        }

        projectRepository.save(project);
    }

    /**
     * Retrieves a list of suitable batteries based on system voltage and battery technology type.
     *
     * @param projectId the ID of the project
     * @param technology the technology type of the batteries (e.g., Li-ion, LiFePO4)
     * @return a list of suitable batteries
     */
    public List<Battery> getSuitableBatteries(String projectId, String technology) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();

        return batteryRepository.findAll().stream()
                .filter(battery -> isSuitableBattery(battery, systemVoltage, technology))
                .collect(Collectors.toList());
    }

    /**
     * Checks if a battery is suitable based on voltage and type.
     *
     * @param battery the battery object to evaluate
     * @param systemVoltage the system voltage of the project
     * @param type the desired battery technology type
     * @return true if the battery matches criteria, false otherwise
     */
    private boolean isSuitableBattery(Battery battery, double systemVoltage, String type) {
        boolean voltageMatch = (battery.getVoltage() <= systemVoltage);
        boolean technologyMatch = battery.getType().equalsIgnoreCase(type);

        return voltageMatch && technologyMatch;
    }

    /**
     * Selects a battery for a project, updates the configuration, and calculates the battery setup.
     *
     * @param projectId the ID of the project
     * @param batteryId the ID of the selected battery
     * @param autonomyDays the desired autonomy days
     * @param temperature the operating temperature
     * @return the updated ProjectBattery configuration
     */
    public ProjectBattery selectBattery(String projectId, String batteryId, int autonomyDays, int temperature) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel == null) {
            configModel = new ConfigurationModel();
            project.setConfigurationModel(configModel);
        }

        ProjectBattery projectBattery = configModel.getProjectBattery();
        if (projectBattery == null) {
            projectBattery = new ProjectBattery();
            configModel.setProjectBattery(projectBattery);
        }

        Battery selectedBattery = getSelectedBattery(batteryId);

        projectBattery.setBatteryId(batteryId);
        projectBattery.setType(selectedBattery.getType());
        projectBattery.setTemperature(temperature);
        projectBattery.setBatteryAutonomy(autonomyDays);

        projectRepository.save(project);

        return calculateBatteryConfiguration(projectId, batteryId, autonomyDays, temperature);
    }

    private static final Map<String, Map<Integer, Double>> temperatureCoefficients = Map.of(
            "Li-ion", Map.of(-30, 0.52, -20, 0.51, -10, 0.70, 0, 0.82, 10, 0.89, 20, 0.93, 25, 1.00, 30, 1.00, 40, 1.00),
            "LiFePO4", Map.of(-10, 0.75, 0, 0.91, 10, 0.97, 20, 1.01, 25, 1.00, 30, 1.02, 40, 1.02),
            "Lead Acid", Map.of(-30, 0.52, -20, 0.64, -10, 0.76, 0, 0.85, 10, 0.92, 20, 0.98, 25, 1.00, 30, 1.02, 40, 1.04)
    );

    /**
     * Calculates the battery configuration for a project.
     *
     * @param projectId the ID of the project
     * @param batteryId the ID of the selected battery
     * @param autonomyDays the desired autonomy days
     * @param temperature the operating temperature
     * @return the calculated ProjectBattery configuration
     */
    public ProjectBattery calculateBatteryConfiguration(String projectId, String batteryId, int autonomyDays, int temperature) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        ProjectBattery projectBattery = configModel.getProjectBattery();

        Battery selectedBattery = getSelectedBattery(batteryId);

        double temperatureCoefficient = getTemperatureCoefficient(selectedBattery.getType(), temperature);
        double totalDailyEnergy = configModel.getProjectInverter().getTotalDailyEnergy();
        double systemVoltage = configModel.getSystemVoltage();
        double batteryVoltage = selectedBattery.getVoltage();

        double requiredCapacity = (totalDailyEnergy / systemVoltage) * autonomyDays * (1 / temperatureCoefficient);
        double batteryCapacityDOD = selectedBattery.getCapacity() * selectedBattery.getDod();
        int parallelBatteries = (int) Math.ceil(requiredCapacity / batteryCapacityDOD);
        int seriesBatteries = (int) Math.ceil(systemVoltage / batteryVoltage);
        double totalAvailableCapacity = batteryCapacityDOD * parallelBatteries;
        double operationalDays = totalAvailableCapacity / ((totalDailyEnergy / systemVoltage) * (1 / temperatureCoefficient));


        double maxChargingPower = (selectedBattery.getVoltage() * seriesBatteries)
                * (selectedBattery.getMaxChargingCurrent() * parallelBatteries);

        double optimalChargingPower = (selectedBattery.getVoltage() * seriesBatteries)
                * (selectedBattery.getOptimalChargingCurrent() * parallelBatteries);

        // Save values to ProjectBattery
        projectBattery.setBatteryCapacityDod(batteryCapacityDOD);
        projectBattery.setParallelBatteries(parallelBatteries);
        projectBattery.setSeriesBatteries(seriesBatteries);
        projectBattery.setRequiredBatteryCapacity(requiredCapacity);
        projectBattery.setTotalAvailableCapacity(totalAvailableCapacity);
        projectBattery.setOperationalDays(operationalDays);
        projectBattery.setMaxChargingPower(maxChargingPower);
        projectBattery.setOptimalChargingPower(optimalChargingPower);

        projectRepository.save(project);

        logger.info("Battery configuration calculated: project {}, battery {}, parallel {}, series {}, requiredCapacity {}, operationalDays {}, maxChargingPower {}, optimalChargingPower {}",
                projectId, batteryId, parallelBatteries, seriesBatteries, requiredCapacity, operationalDays, maxChargingPower, optimalChargingPower);

        return projectBattery;
    }

    /**
     * Retrieves the temperature coefficient for a given battery type and temperature.
     *
     * @param batteryType the type of the battery
     * @param temperature the operating temperature
     * @return the temperature coefficient
     */
    private double getTemperatureCoefficient(String batteryType, int temperature) {
        Map<Integer, Double> coefficientsForType = temperatureCoefficients.get(batteryType);
        if (coefficientsForType == null) {
            throw new RuntimeException("Unknown battery type: " + batteryType);
        }
        return coefficientsForType.getOrDefault(temperature, 1.0);
    }

    /**
     * Retrieves a battery by its ID.
     *
     * @param batteryId the ID of the battery
     * @return the selected battery
     */
    public Battery getSelectedBattery(String batteryId) {
        return batteryRepository.findById(batteryId)
                .orElseThrow(() -> new RuntimeException("Battery not found: " + batteryId));
    }

    /**
     * Retrieves the ProjectBattery configuration for a project.
     *
     * @param projectId the ID of the project
     * @return the ProjectBattery configuration
     */
    public ProjectBattery getProjectBattery(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();

        return configModel.getProjectBattery();
    }
}
