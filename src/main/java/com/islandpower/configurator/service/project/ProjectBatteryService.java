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

    // Remove a battery from a project
    public void removeBattery(String projectId, String batteryId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        if (configModel != null && configModel.getProjectBattery() != null) {
            configModel.getProjectBattery().setBatteryId(null); // Remove battery ID
        }

        projectRepository.save(project);
    }

    // Fetch suitable batteries based on project voltage and technology
    public List<Battery> getSuitableBatteries(String projectId, String technology) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        double systemVoltage = project.getConfigurationModel().getSystemVoltage();

        return batteryRepository.findAll().stream()
                .filter(battery -> isSuitableBattery(battery, systemVoltage, technology))
                .collect(Collectors.toList());
    }

    // Filter for suitable batteries
    private boolean isSuitableBattery(Battery battery, double systemVoltage, String type) {
        boolean voltageMatch = (battery.getVoltage() <= systemVoltage);
        boolean technologyMatch = battery.getType().equalsIgnoreCase(type);

        return voltageMatch && technologyMatch;
    }

    // Select battery for a project and calculate configuration
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
            configModel.setProjectBattery(projectBattery); // Initialize ProjectBattery
        }

        Battery selectedBattery = getSelectedBattery(batteryId);

        // Save battery details
        projectBattery.setBatteryId(batteryId);
        projectBattery.setType(selectedBattery.getType()); // Save battery type
        projectBattery.setTemperature(temperature); // Save temperature
        projectBattery.setBatteryAutonomy(autonomyDays); // Save autonomy days

        projectRepository.save(project);

        return calculateBatteryConfiguration(projectId, batteryId, autonomyDays, temperature);
    }

    // Predefined temperature coefficients based on battery type
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

    // Calculate battery configuration for a project
    // Calculate battery configuration for a project
    public ProjectBattery calculateBatteryConfiguration(String projectId, String batteryId, int autonomyDays, int temperature) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();
        ProjectBattery projectBattery = configModel.getProjectBattery();

        Battery selectedBattery = getSelectedBattery(batteryId);

        // Get temperature coefficient
        double temperatureCoefficient = getTemperatureCoefficient(selectedBattery.getType(), temperature);
        double totalDailyEnergy = configModel.getProjectInverter().getTotalDailyEnergy(); // From ProjectInverter
        double systemVoltage = configModel.getSystemVoltage();
        double batteryVoltage = selectedBattery.getVoltage();

        // Calculate required battery capacity using the formula:
        // C_require = (E_daily_AC_DC / U_system) * t_days * (1 / T_temperature)
        double requiredCapacity = (totalDailyEnergy / systemVoltage) * autonomyDays * (1 / temperatureCoefficient); // Ah

        // Calculate battery capacity with DOD (Depth of Discharge)
        // C_batteryDOD = C_battery * DOD
        double batteryCapacityDOD = selectedBattery.getCapacity() * selectedBattery.getDod();

        // Calculate the number of parallel batteries required
        // n_batt_parallel = C_require / C_batteryDOD
        int parallelBatteries = (int) Math.ceil(requiredCapacity / batteryCapacityDOD);

        // Calculate the number of series batteries required
        // n_batt_serial = U_system / U_battery
        int seriesBatteries = (int) Math.ceil(systemVoltage / batteryVoltage);

        // Calculate total available capacity
        // Total available capacity = C_batteryDOD * parallelBatteries
        double totalAvailableCapacity = batteryCapacityDOD * parallelBatteries;

        // Calculate operational days
        // Operational days = Total available capacity / (Total daily energy / System voltage)
        double operationalDays = totalAvailableCapacity / ((totalDailyEnergy / systemVoltage) * (1 / temperatureCoefficient));

        // Set calculated values in ProjectBattery
        projectBattery.setBatteryCapacityDod(batteryCapacityDOD);
        projectBattery.setParallelBatteries(parallelBatteries);
        projectBattery.setSeriesBatteries(seriesBatteries);
        projectBattery.setRequiredBatteryCapacity(requiredCapacity);
        projectBattery.setTotalAvailableCapacity(totalAvailableCapacity);
        projectBattery.setOperationalDays(operationalDays);

        projectRepository.save(project);

        // Log calculated values
        logger.info("Battery configuration calculated for project: {}, battery: {}, parallel: {}, series: {}, requiredCapacity: {}, operationalDays: {}, totalCapacity: {}",
                projectId, batteryId, parallelBatteries, seriesBatteries, requiredCapacity, operationalDays, totalAvailableCapacity);

        return projectBattery;
    }

    // Get temperature coefficient based on battery type and temperature
    private double getTemperatureCoefficient(String batteryType, int temperature) {
        Map<Integer, Double> coefficientsForType = temperatureCoefficients.get(batteryType);
        if (coefficientsForType == null) {
            throw new RuntimeException("Unknown battery type: " + batteryType);
        }
        return coefficientsForType.getOrDefault(temperature, 1.0); // Default coefficient is 1.0
    }

    // Fetch selected battery by ID
    public Battery getSelectedBattery(String batteryId) {
        return batteryRepository.findById(batteryId)
                .orElseThrow(() -> new RuntimeException("Battery not found: " + batteryId));
    }

    // Get ProjectBattery configuration for a project
    public ProjectBattery getProjectBattery(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        ConfigurationModel configModel = project.getConfigurationModel();

        return configModel.getProjectBattery();
    }
}
