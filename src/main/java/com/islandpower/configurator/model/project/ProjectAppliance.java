package com.islandpower.configurator.model.project;

/**
 * Model representing aggregated appliance data in a project.
 * <p>
 * This class stores total energy and peak power consumption for AC and DC appliances.
 * It is used to calculate the overall power and energy requirements of the project.
 * </p>
 *
 * @version 1.0
 */
public class ProjectAppliance {

    private double totalAcPower; // Total AC energy consumption
    private double totalDcPower; // Total DC energy consumption
    private double totalAcEnergy; // Total AC energy consumption
    private double totalDcEnergy; // Total DC energy consumption
    private double totalAcPeakPower; // Total peak power for AC
    private double totalDcPeakPower; // Total peak power for DC

    // Getters and Setters

    /**
     * Retrieves the total AC power consumption.
     *
     * @return double - the total AC power in Watts
     */
    public double getTotalAcPower() {
        return totalAcPower;
    }

    /**
     * Updates the total AC power consumption.
     *
     * @param totalAcPower - the new total AC power in Watts
     */
    public void setTotalAcPower(double totalAcPower) {
        this.totalAcPower = totalAcPower;
    }

    /**
     * Retrieves the total DC power consumption.
     *
     * @return double - the total DC power in Watts
     */
    public double getTotalDcPower() {
        return totalDcPower;
    }

    /**
     * Updates the total DC power consumption.
     *
     * @param totalDcPower - the new total DC power in Watts
     */
    public void setTotalDcPower(double totalDcPower) {
        this.totalDcPower = totalDcPower;
    }

    /**
     * Retrieves the total AC energy consumption.
     *
     * @return double - the total AC energy in kWh
     */
    public double getTotalAcEnergy() {
        return totalAcEnergy;
    }

    /**
     * Updates the total AC energy consumption.
     *
     * @param totalAcEnergy - the new total AC energy in kWh
     */
    public void setTotalAcEnergy(double totalAcEnergy) {
        this.totalAcEnergy = totalAcEnergy;
    }

    /**
     * Retrieves the total DC energy consumption.
     *
     * @return double - the total DC energy in kWh
     */
    public double getTotalDcEnergy() {
        return totalDcEnergy;
    }

    /**
     * Updates the total DC energy consumption.
     *
     * @param totalDcEnergy - the new total DC energy in kWh
     */
    public void setTotalDcEnergy(double totalDcEnergy) {
        this.totalDcEnergy = totalDcEnergy;
    }

    /**
     * Retrieves the total peak power for AC appliances.
     *
     * @return double - the total AC peak power in Watts
     */
    public double getTotalAcPeakPower() {
        return totalAcPeakPower;
    }

    /**
     * Updates the total peak power for AC appliances.
     *
     * @param totalAcPeakPower - the new total AC peak power in Watts
     */
    public void setTotalAcPeakPower(double totalAcPeakPower) {
        this.totalAcPeakPower = totalAcPeakPower;
    }

    /**
     * Retrieves the total peak power for DC appliances.
     *
     * @return double - the total DC peak power in Watts
     */
    public double getTotalDcPeakPower() {
        return totalDcPeakPower;
    }

    /**
     * Updates the total peak power for DC appliances.
     *
     * @param totalDcPeakPower - the new total DC peak power in Watts
     */
    public void setTotalDcPeakPower(double totalDcPeakPower) {
        this.totalDcPeakPower = totalDcPeakPower;
    }
}
