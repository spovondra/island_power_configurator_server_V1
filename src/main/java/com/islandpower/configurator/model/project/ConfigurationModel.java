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
    private String batteryId; // ID of the selected battery
    private double requiredBatteryCapacity; // Capacity of the battery in kWh
    private double batteryAutonomy; // Battery autonomy calculated based on capacity and energy needs
    private int parallelBatteries; // Number of parallel batteries required
    private int seriesBatteries; // Number of series batteries required

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

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public double getRequiredBatteryCapacity() {
        return requiredBatteryCapacity;
    }

    public void setRequiredBatteryCapacity(double requiredBatteryCapacity) {
        this.requiredBatteryCapacity = requiredBatteryCapacity;
    }

    public double getBatteryAutonomy() {
        return batteryAutonomy;
    }

    public void setBatteryAutonomy(double batteryAutonomy) {
        this.batteryAutonomy = batteryAutonomy;
    }

    public int getParallelBatteries() {
        return parallelBatteries;
    }

    public void setParallelBatteries(int parallelBatteries) {
        this.parallelBatteries = parallelBatteries;
    }

    public int getSeriesBatteries() {
        return seriesBatteries;
    }

    public void setSeriesBatteries(int seriesBatteries) {
        this.seriesBatteries = seriesBatteries;
    }
}
