package com.islandpower.configurator.model.project;

/**
 * Model representing the battery configuration in a project.
 * <p>
 * This class defines attributes for battery specifications, configurations, and
 * calculated values, such as the number of series and parallel batteries, adjusted capacity,
 * and operational parameters.
 * </p>
 *
 * @version 1.0
 */
public class ProjectBattery {

    private String batteryId; // ID of the selected battery
    private String type; // Battery type (e.g., Li-ion, Lead Acid)
    private int temperature; // Operating temperature (°C)
    private double batteryCapacityDod; // Adjusted capacity for depth of discharge (Ah)
    private int parallelBatteries; // Number of parallel-connected batteries
    private int seriesBatteries; // Number of series-connected batteries
    private double requiredBatteryCapacity; // Required battery capacity (Ah)
    private double batteryAutonomy; // Autonomy in days
    private double totalAvailableCapacity; // Total available capacity (Ah)
    private double operationalDays; // Number of operational days calculated
    private double maxChargingPower; // Maximum charging power (W)
    private double optimalChargingPower; // Optimal charging power (W)

    /**
     * Default constructor for ProjectBattery.
     */
    public ProjectBattery() {
    }

    /**
     * Retrieves the ID of the battery.
     *
     * @return String - the ID of the battery
     */
    public String getBatteryId() {
        return batteryId;
    }

    /**
     * Updates the ID of the battery.
     *
     * @param batteryId - the new ID of the battery
     */
    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    /**
     * Retrieves the type of the battery.
     *
     * @return String - the type of the battery
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type of the battery.
     *
     * @param type - the new type of the battery
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the operating temperature of the battery.
     *
     * @return int - the operating temperature (°C)
     */
    public int getTemperature() {
        return temperature;
    }

    /**
     * Updates the operating temperature of the battery.
     *
     * @param temperature - the new operating temperature (°C)
     */
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    /**
     * Retrieves the battery capacity adjusted for depth of discharge (DoD).
     *
     * @return double - the adjusted battery capacity (Ah)
     */
    public double getBatteryCapacityDod() {
        return batteryCapacityDod;
    }

    /**
     * Updates the battery capacity adjusted for depth of discharge (DoD).
     *
     * @param batteryCapacityDod - the new adjusted battery capacity (Ah)
     */
    public void setBatteryCapacityDod(double batteryCapacityDod) {
        this.batteryCapacityDod = batteryCapacityDod;
    }

    /**
     * Retrieves the number of parallel-connected batteries.
     *
     * @return int - the number of parallel batteries
     */
    public int getParallelBatteries() {
        return parallelBatteries;
    }

    /**
     * Updates the number of parallel-connected batteries.
     *
     * @param parallelBatteries - the new number of parallel batteries
     */
    public void setParallelBatteries(int parallelBatteries) {
        this.parallelBatteries = parallelBatteries;
    }

    /**
     * Retrieves the number of series-connected batteries.
     *
     * @return int - the number of series batteries
     */
    public int getSeriesBatteries() {
        return seriesBatteries;
    }

    /**
     * Updates the number of series-connected batteries.
     *
     * @param seriesBatteries - the new number of series batteries
     */
    public void setSeriesBatteries(int seriesBatteries) {
        this.seriesBatteries = seriesBatteries;
    }

    /**
     * Retrieves the required battery capacity for the project.
     *
     * @return double - the required capacity (Ah)
     */
    public double getRequiredBatteryCapacity() {
        return requiredBatteryCapacity;
    }

    /**
     * Updates the required battery capacity for the project.
     *
     * @param requiredBatteryCapacity - the new required capacity (Ah)
     */
    public void setRequiredBatteryCapacity(double requiredBatteryCapacity) {
        this.requiredBatteryCapacity = requiredBatteryCapacity;
    }

    /**
     * Retrieves the battery autonomy in days.
     *
     * @return double - the autonomy in days
     */
    public double getBatteryAutonomy() {
        return batteryAutonomy;
    }

    /**
     * Updates the battery autonomy in days.
     *
     * @param batteryAutonomy - the new autonomy in days
     */
    public void setBatteryAutonomy(double batteryAutonomy) {
        this.batteryAutonomy = batteryAutonomy;
    }

    /**
     * Retrieves the total available battery capacity.
     *
     * @return double - the total available capacity (Ah)
     */
    public double getTotalAvailableCapacity() {
        return totalAvailableCapacity;
    }

    /**
     * Updates the total available battery capacity.
     *
     * @param totalAvailableCapacity - the new total capacity (Ah)
     */
    public void setTotalAvailableCapacity(double totalAvailableCapacity) {
        this.totalAvailableCapacity = totalAvailableCapacity;
    }

    /**
     * Retrieves the calculated operational days.
     *
     * @return double - the calculated operational days
     */
    public double getOperationalDays() {
        return operationalDays;
    }

    /**
     * Updates the calculated operational days.
     *
     * @param operationalDays - the new operational days
     */
    public void setOperationalDays(double operationalDays) {
        this.operationalDays = operationalDays;
    }

    /**
     * Retrieves the maximum charging power for the battery.
     *
     * @return double - the maximum charging power (W)
     */
    public double getMaxChargingPower() {
        return maxChargingPower;
    }

    /**
     * Updates the maximum charging power for the battery.
     *
     * @param maxChargingPower - the new maximum charging power (W)
     */
    public void setMaxChargingPower(double maxChargingPower) {
        this.maxChargingPower = maxChargingPower;
    }

    /**
     * Retrieves the optimal charging power for the battery.
     *
     * @return double - the optimal charging power (W)
     */
    public double getOptimalChargingPower() {
        return optimalChargingPower;
    }

    /**
     * Updates the optimal charging power for the battery.
     *
     * @param optimalChargingPower - the new optimal charging power (W)
     */
    public void setOptimalChargingPower(double optimalChargingPower) {
        this.optimalChargingPower = optimalChargingPower;
    }
}
