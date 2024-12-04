package com.islandpower.configurator.model.project;

/**
 * Model representing the inverter configuration in a project.
 * This class defines attributes related to the selected inverter,
 * including its temperature settings and energy calculations.
 *
 * @version 1.0
 */
public class ProjectInverter {

    private String inverterId; // id of the selected inverter
    private double inverterTemperature; // installation temperature of the inverter in degrees Celsius
    private double totalAdjustedAcEnergy; // adjusted AC load in kilowatt-hours
    private double totalDailyEnergy; // total daily energy consumption in kilowatt-hours

    /**
     * Retrieves the ID of the selected inverter.
     *
     * @return String The ID of the selected inverter
     */
    public String getInverterId() {
        return inverterId;
    }

    /**
     * Updates the ID of the selected inverter.
     *
     * @param inverterId The new inverter ID
     */
    public void setInverterId(String inverterId) {
        this.inverterId = inverterId;
    }

    /**
     * Retrieves the installation temperature of the inverter.
     *
     * @return double The installation temperature in degrees Celsius
     */
    public double getInverterTemperature() {
        return inverterTemperature;
    }

    /**
     * Updates the installation temperature of the inverter.
     *
     * @param inverterTemperature The new installation temperature in degrees Celsius
     */
    public void setInverterTemperature(double inverterTemperature) {
        this.inverterTemperature = inverterTemperature;
    }

    /**
     * Retrieves the total adjusted AC energy consumption.
     *
     * @return double The adjusted AC load in kilowatt-hours
     */
    public double getTotalAdjustedAcEnergy() {
        return totalAdjustedAcEnergy;
    }

    /**
     * Updates the total adjusted AC energy consumption.
     *
     * @param totalAdjustedAcEnergy The new adjusted AC load in kilowatt-hours
     */
    public void setTotalAdjustedAcEnergy(double totalAdjustedAcEnergy) {
        this.totalAdjustedAcEnergy = totalAdjustedAcEnergy;
    }

    /**
     * Retrieves the total daily energy consumption.
     *
     * @return double The total daily energy consumption in kilowatt-hours
     */
    public double getTotalDailyEnergy() {
        return totalDailyEnergy;
    }

    /**
     * Updates the total daily energy consumption.
     *
     * @param totalDailyEnergy The new total daily energy consumption in kilowatt-hours
     */
    public void setTotalDailyEnergy(double totalDailyEnergy) {
        this.totalDailyEnergy = totalDailyEnergy;
    }
}
