package com.islandpower.configurator.model.project;

public class ProjectBattery {
    private String batteryId;
    private String type; // New field for battery type
    private int temperature; // New field for temperature
    private double batteryCapacityDod;
    private int parallelBatteries;
    private int seriesBatteries;
    private double requiredBatteryCapacity;
    private double batteryAutonomy;
    private double totalAvailableCapacity;
    private double operationalDays;

    public ProjectBattery() {
    }

    public ProjectBattery(String batteryId, String type, int temperature, double batteryCapacityDod, int parallelBatteries,
                          int seriesBatteries, double requiredBatteryCapacity, double batteryAutonomy, double totalAvailableCapacity,
                          double operationalDays) {
        this.batteryId = batteryId;
        this.type = type;
        this.temperature = temperature;
        this.batteryCapacityDod = batteryCapacityDod;
        this.parallelBatteries = parallelBatteries;
        this.seriesBatteries = seriesBatteries;
        this.requiredBatteryCapacity = requiredBatteryCapacity;
        this.batteryAutonomy = batteryAutonomy;
        this.totalAvailableCapacity = totalAvailableCapacity;
        this.operationalDays = operationalDays;
    }

    // Getters and Setters
    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public double getBatteryCapacityDod() {
        return batteryCapacityDod;
    }

    public void setBatteryCapacityDod(double batteryCapacityDod) {
        this.batteryCapacityDod = batteryCapacityDod;
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

    public double getTotalAvailableCapacity() {
        return totalAvailableCapacity;
    }

    public void setTotalAvailableCapacity(double totalAvailableCapacity) {
        this.totalAvailableCapacity = totalAvailableCapacity;
    }

    public double getOperationalDays() {
        return operationalDays;
    }

    public void setOperationalDays(double operationalDays) {
        this.operationalDays = operationalDays;
    }
}
