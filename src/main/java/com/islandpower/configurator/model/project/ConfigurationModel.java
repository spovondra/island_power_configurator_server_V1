package com.islandpower.configurator.model.project;

public class ConfigurationModel {
    private double totalAcEnergy; // Total AC energy consumption
    private double totalDcEnergy; // Total DC energy consumption
    private double systemVoltage; // Current system voltage
    private double recommendedSystemVoltage; // Recommended system voltage
    private double totalAcPeakPower; // Total peak power for AC
    private double totalDcPeakPower; // Total peak power for DC
    private String inverterId; // ID of the selected inverter
    private double inverterTemperature; // Installation temperature of the inverter

    // New fields for energy calculations
    private double totalAdjustedAcEnergy; // Adjusted AC Load
    private double totalDailyEnergy; // Total Daily Energy

    // Battery configuration fields
    private ProjectBattery projectBattery; // Battery configuration object

    // Getters and Setters
    public double getTotalAcEnergy() {
        return totalAcEnergy;
    }

    public void setTotalAcEnergy(double totalAcEnergy) {
        this.totalAcEnergy = totalAcEnergy;
    }

    public double getTotalDcEnergy() {
        return totalDcEnergy;
    }

    public void setTotalDcEnergy(double totalDcEnergy) {
        this.totalDcEnergy = totalDcEnergy;
    }

    public double getSystemVoltage() {
        return systemVoltage;
    }

    public void setSystemVoltage(double systemVoltage) {
        this.systemVoltage = systemVoltage;
    }

    public double getRecommendedSystemVoltage() {
        return recommendedSystemVoltage;
    }

    public void setRecommendedSystemVoltage(double recommendedSystemVoltage) {
        this.recommendedSystemVoltage = recommendedSystemVoltage;
    }

    public double getTotalAcPeakPower() {
        return totalAcPeakPower;
    }

    public void setTotalAcPeakPower(double totalAcPeakPower) {
        this.totalAcPeakPower = totalAcPeakPower;
    }

    public double getTotalDcPeakPower() {
        return totalDcPeakPower;
    }

    public void setTotalDcPeakPower(double totalDcPeakPower) {
        this.totalDcPeakPower = totalDcPeakPower;
    }

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

    // New getters and setters for Adjusted AC Load and Total Daily Energy
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

    // Getters and Setters for ProjectBattery
    public ProjectBattery getProjectBattery() {
        return projectBattery;
    }

    public void setProjectBattery(ProjectBattery projectBattery) {
        this.projectBattery = projectBattery;
    }
}
