package com.islandpower.configurator.model.project;

/**
 * Model representing the battery configuration in a project.
 * This class defines attributes for battery specifications, configurations, and
 * calculated values, such as the number of series and parallel batteries, adjusted capacity,
 * and operational parameters.
 *
 * @version 1.0
 */
public class ProjectBattery {

    private String batteryId; // id of the selected battery
    private String type; // battery type
    private int temperature; // operating temperature in degrees Celsius
    private double batteryCapacityDod; // adjusted capacity for depth of discharge in ampere-hours
    private int parallelBatteries; // number of parallel-connected batteries
    private int seriesBatteries; // number of series-connected batteries
    private double requiredBatteryCapacity; // required battery capacity in ampere-hours
    private double batteryAutonomy; // autonomy in days
    private double totalAvailableCapacity; // total available capacity in ampere-hours
    private double operationalDays; // number of operational days calculated
    private double maxChargingPower; // maximum charging power in watts
    private double optimalChargingPower; // optimal charging power in watts

    /**
     * Default constructor for ProjectBattery.
     */
    public ProjectBattery() {}

    /**
     * Retrieves the ID of the battery.
     *
     * @return String The ID of the battery
     */
    public String getBatteryId() {
        return batteryId;
    }

    /**
     * Updates the ID of the battery.
     *
     * @param batteryId The new ID of the battery
     */
    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    /**
     * Retrieves the type of the battery.
     *
     * @return String The type of the battery
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type of the battery.
     *
     * @param type The new type of the battery
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the operating temperature of the battery.
     *
     * @return int The operating temperature in degrees Celsius
     */
    public int getTemperature() {
        return temperature;
    }

    /**
     * Updates the operating temperature of the battery.
     *
     * @param temperature The new operating temperature in degrees Celsius
     */
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    /**
     * Retrieves the battery capacity adjusted for depth of discharge.
     *
     * @return double The adjusted battery capacity in ampere-hours
     */
    public double getBatteryCapacityDod() {
        return batteryCapacityDod;
    }

    /**
     * Updates the battery capacity adjusted for depth of discharge.
     *
     * @param batteryCapacityDod The new adjusted battery capacity in ampere-hours
     */
    public void setBatteryCapacityDod(double batteryCapacityDod) {
        this.batteryCapacityDod = batteryCapacityDod;
    }

    /**
     * Retrieves the number of parallel-connected batteries.
     *
     * @return int The number of parallel batteries
     */
    public int getParallelBatteries() {
        return parallelBatteries;
    }

    /**
     * Updates the number of parallel-connected batteries.
     *
     * @param parallelBatteries The new number of parallel batteries
     */
    public void setParallelBatteries(int parallelBatteries) {
        this.parallelBatteries = parallelBatteries;
    }

    /**
     * Retrieves the number of series-connected batteries.
     *
     * @return int The number of series batteries
     */
    public int getSeriesBatteries() {
        return seriesBatteries;
    }

    /**
     * Updates the number of series-connected batteries.
     *
     * @param seriesBatteries The new number of series batteries
     */
    public void setSeriesBatteries(int seriesBatteries) {
        this.seriesBatteries = seriesBatteries;
    }

    /**
     * Retrieves the required battery capacity for the project.
     *
     * @return double The required battery capacity in ampere-hours
     */
    public double getRequiredBatteryCapacity() {
        return requiredBatteryCapacity;
    }

    /**
     * Updates the required battery capacity for the project.
     *
     * @param requiredBatteryCapacity The new required battery capacity in ampere-hours
     */
    public void setRequiredBatteryCapacity(double requiredBatteryCapacity) {
        this.requiredBatteryCapacity = requiredBatteryCapacity;
    }

    /**
     * Retrieves the battery autonomy in days.
     *
     * @return double The autonomy in days
     */
    public double getBatteryAutonomy() {
        return batteryAutonomy;
    }

    /**
     * Updates the battery autonomy in days.
     *
     * @param batteryAutonomy The new autonomy in days
     */
    public void setBatteryAutonomy(double batteryAutonomy) {
        this.batteryAutonomy = batteryAutonomy;
    }

    /**
     * Retrieves the total available battery capacity.
     *
     * @return double The total available capacity in ampere-hours
     */
    public double getTotalAvailableCapacity() {
        return totalAvailableCapacity;
    }

    /**
     * Updates the total available battery capacity.
     *
     * @param totalAvailableCapacity The new total available capacity in ampere-hours
     */
    public void setTotalAvailableCapacity(double totalAvailableCapacity) {
        this.totalAvailableCapacity = totalAvailableCapacity;
    }

    /**
     * Retrieves the calculated operational days.
     *
     * @return double The calculated operational days
     */
    public double getOperationalDays() {
        return operationalDays;
    }

    /**
     * Updates the calculated operational days.
     *
     * @param operationalDays The new calculated operational days
     */
    public void setOperationalDays(double operationalDays) {
        this.operationalDays = operationalDays;
    }

    /**
     * Retrieves the maximum charging power for the battery.
     *
     * @return double The maximum charging power in watts
     */
    public double getMaxChargingPower() {
        return maxChargingPower;
    }

    /**
     * Updates the maximum charging power for the battery.
     *
     * @param maxChargingPower The new maximum charging power in watts
     */
    public void setMaxChargingPower(double maxChargingPower) {
        this.maxChargingPower = maxChargingPower;
    }

    /**
     * Retrieves the optimal charging power for the battery.
     *
     * @return double The optimal charging power in watts
     */
    public double getOptimalChargingPower() {
        return optimalChargingPower;
    }

    /**
     * Updates the optimal charging power for the battery.
     *
     * @param optimalChargingPower The new optimal charging power in watts
     */
    public void setOptimalChargingPower(double optimalChargingPower) {
        this.optimalChargingPower = optimalChargingPower;
    }
}
