package com.islandpower.configurator.model.project;

/**
 * Model representing aggregated appliance data in a project.
 * This class stores total energy and peak power consumption for AC and DC appliances.
 * It is used to calculate the overall power and energy requirements of the project.
 *
 * @version 1.0
 */
public class ProjectAppliance {

    private double totalAcPower; // total AC power consumption in watts
    private double totalDcPower; // total DC power consumption in watts
    private double totalAcEnergy; // total AC energy consumption in kilowatt-hours
    private double totalDcEnergy; // total DC energy consumption in kilowatt-hours
    private double totalAcPeakPower; // total peak power for AC appliances in watts
    private double totalDcPeakPower; // total peak power for DC appliances in watts

    /**
     * Retrieves the total AC power consumption.
     *
     * @return double The total AC power in watts
     */
    public double getTotalAcPower() {
        return totalAcPower;
    }

    /**
     * Updates the total AC power consumption.
     *
     * @param totalAcPower The new total AC power in watts
     */
    public void setTotalAcPower(double totalAcPower) {
        this.totalAcPower = totalAcPower;
    }

    /**
     * Retrieves the total DC power consumption.
     *
     * @return double The total DC power in watts
     */
    public double getTotalDcPower() {
        return totalDcPower;
    }

    /**
     * Updates the total DC power consumption.
     *
     * @param totalDcPower The new total DC power in watts
     */
    public void setTotalDcPower(double totalDcPower) {
        this.totalDcPower = totalDcPower;
    }

    /**
     * Retrieves the total AC energy consumption.
     *
     * @return double The total AC energy in kWh
     */
    public double getTotalAcEnergy() {
        return totalAcEnergy;
    }

    /**
     * Updates the total AC energy consumption.
     *
     * @param totalAcEnergy The new total AC energy in kWh
     */
    public void setTotalAcEnergy(double totalAcEnergy) {
        this.totalAcEnergy = totalAcEnergy;
    }

    /**
     * Retrieves the total DC energy consumption.
     *
     * @return double The total DC energy in kWh
     */
    public double getTotalDcEnergy() {
        return totalDcEnergy;
    }

    /**
     * Updates the total DC energy consumption.
     *
     * @param totalDcEnergy The new total DC energy in kWh
     */
    public void setTotalDcEnergy(double totalDcEnergy) {
        this.totalDcEnergy = totalDcEnergy;
    }

    /**
     * Retrieves the total peak power for AC appliances.
     *
     * @return double The total AC peak power in watts
     */
    public double getTotalAcPeakPower() {
        return totalAcPeakPower;
    }

    /**
     * Updates the total peak power for AC appliances.
     *
     * @param totalAcPeakPower The new total AC peak power in watts
     */
    public void setTotalAcPeakPower(double totalAcPeakPower) {
        this.totalAcPeakPower = totalAcPeakPower;
    }

    /**
     * Retrieves the total peak power for DC appliances.
     *
     * @return double The total DC peak power in watts
     */
    public double getTotalDcPeakPower() {
        return totalDcPeakPower;
    }

    /**
     * Updates the total peak power for DC appliances.
     *
     * @param totalDcPeakPower The new total DC peak power in watts
     */
    public void setTotalDcPeakPower(double totalDcPeakPower) {
        this.totalDcPeakPower = totalDcPeakPower;
    }
}
