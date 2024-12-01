package com.islandpower.configurator.model.project;

/**
 * Model representing the inverter configuration in a project.
 * <p>
 * This class defines attributes related to the selected inverter,
 * including its temperature settings and energy calculations.
 * </p>
 *
 * @version 1.0
 */
public class ProjectInverter {

    private String inverterId; // ID of the selected inverter
    private double inverterTemperature; // Installation temperature of the inverter
    private double totalAdjustedAcEnergy; // Adjusted AC Load
    private double totalDailyEnergy; // Total Daily Energy

    /**
     * Retrieves the ID of the selected inverter.
     *
     * @return String - the ID of the selected inverter
     */
    public String getInverterId() {
        return inverterId;
    }

    /**
     * Updates the ID of the selected inverter.
     *
     * @param inverterId - the new inverter ID
     */
    public void setInverterId(String inverterId) {
        this.inverterId = inverterId;
    }

    /**
     * Retrieves the installation temperature of the inverter.
     *
     * @return double - the installation temperature in degrees Celsius
     */
    public double getInverterTemperature() {
        return inverterTemperature;
    }

    /**
     * Updates the installation temperature of the inverter.
     *
     * @param inverterTemperature - the new installation temperature in degrees Celsius
     */
    public void setInverterTemperature(double inverterTemperature) {
        this.inverterTemperature = inverterTemperature;
    }

    /**
     * Retrieves the total adjusted AC energy consumption.
     *
     * @return double - the adjusted AC load in kilowatt-hours (kWh)
     */
    public double getTotalAdjustedAcEnergy() {
        return totalAdjustedAcEnergy;
    }

    /**
     * Updates the total adjusted AC energy consumption.
     *
     * @param totalAdjustedAcEnergy - the new adjusted AC load in kilowatt-hours (kWh)
     */
    public void setTotalAdjustedAcEnergy(double totalAdjustedAcEnergy) {
        this.totalAdjustedAcEnergy = totalAdjustedAcEnergy;
    }

    /**
     * Retrieves the total daily energy consumption.
     *
     * @return double - the total daily energy consumption in kilowatt-hours (kWh)
     */
    public double getTotalDailyEnergy() {
        return totalDailyEnergy;
    }

    /**
     * Updates the total daily energy consumption.
     *
     * @param totalDailyEnergy - the new total daily energy consumption in kilowatt-hours (kWh)
     */
    public void setTotalDailyEnergy(double totalDailyEnergy) {
        this.totalDailyEnergy = totalDailyEnergy;
    }
}
