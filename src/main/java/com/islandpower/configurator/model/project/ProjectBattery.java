package com.islandpower.configurator.model.project;

public class ProjectBattery {
    private String batteryId;
    private double batteryCapacityDod;
    private int parallelBatteries;
    private int seriesBatteries;
    private double requiredBatteryCapacity;
    private double batteryAutonomy; // Battery autonomy calculated based on capacity and energy needs
    private double totalAvailableCapacity;
    private double operationalDays;

    public ProjectBattery() {
    }

    public ProjectBattery(String batteryId, double batteryCapacityDod, int parallelBatteries, int seriesBatteries,
                          double requiredBatteryCapacity, double batteryAutonomy, double totalAvailableCapacity, double operationalDays) {
        this.batteryId = batteryId;
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

    public double getBatteryCapacityDod() {
        return batteryCapacityDod;
    }

    public void setBatteryCapacityDod(double batteryCapacityDod) {
        this.batteryCapacityDod = batteryCapacityDod;
    }

    public double getOperationalDays() {
        return operationalDays;
    }

    public void setOperationalDays(double operationalDays) {
        this.operationalDays = operationalDays;
    }

    public double getTotalAvailableCapacity() {
        return totalAvailableCapacity;
    }

    public void setTotalAvailableCapacity(double totalAvailableCapacity) {
        this.totalAvailableCapacity = totalAvailableCapacity;
    }

    public double getBatteryAutonomy() {
        return batteryAutonomy;
    }

    public void setBatteryAutonomy(double batteryAutonomy) {
        this.batteryAutonomy = batteryAutonomy;
    }
}
