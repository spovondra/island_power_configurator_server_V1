package com.islandpower.configurator.model.project;

public class ProjectInverter {
    private String inverterId; // ID of the selected inverter
    private double inverterTemperature; // Installation temperature of the inverter

    // New fields for energy calculations
    private double totalAdjustedAcEnergy; // Adjusted AC Load
    private double totalDailyEnergy; // Total Daily Energy

    // Getters and Setters for Inverter ID and Temperature
    public String getInverterId() {
        return inverterId;
    }

    public void setInverterId(String inverterId) {
        this.inverterId = inverterId;
    }

    public double getInverterTemperature() {
        return inverterTemperature;
    }

    public void setInverterTemperature(double inverterTemperature) {
        this.inverterTemperature = inverterTemperature;
    }

    // Getters and Setters for energy calculations
    public double getTotalAdjustedAcEnergy() {
        return totalAdjustedAcEnergy;
    }

    public void setTotalAdjustedAcEnergy(double totalAdjustedAcEnergy) {
        this.totalAdjustedAcEnergy = totalAdjustedAcEnergy;
    }

    public double getTotalDailyEnergy() {
        return totalDailyEnergy;
    }

    public void setTotalDailyEnergy(double totalDailyEnergy) {
        this.totalDailyEnergy = totalDailyEnergy;
    }
}
