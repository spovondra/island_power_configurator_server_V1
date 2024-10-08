package com.islandpower.configurator.model.project;

public class ConfigurationModel {
    private double totalAcEnergy;
    private double totalDcEnergy;
    private double systemVoltage;
    private double recommendedSystemVoltage; // Recommended system voltage
    private double totalAcPeakPower; // Total peak power for AC
    private double totalDcPeakPower; // Total peak power for DC
    private String inverterId; // Field for the inverter ID
    private double inverterTemperature; // Field for the inverter installation temperature

    // New fields for energy calculations
    private double totalAdjustedAcEnergy; // Adjusted AC Load
    private double totalDailyEnergy; // Total Daily Energy

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
}
