package com.islandpower.configurator.dto;

public class BatteryConfigurationResponse {
    private String batteryId;
    private double batteryCapacityDod;
    private int parallelBatteries;
    private int seriesBatteries;
    private double requiredCapacity;
    private double totalAvailableCapacity;
    private double operationalDays;

    public BatteryConfigurationResponse() {
    }

    public BatteryConfigurationResponse (String batteryId, double batteryCapacityDod, int parallelBatteries, int seriesBatteries,
                                        double requiredCapacity, double totalAvailableCapacity, double operationalDays) {
        this.batteryId = batteryId;
        this.batteryCapacityDod = batteryCapacityDod;
        this.parallelBatteries = parallelBatteries;
        this.seriesBatteries = seriesBatteries;
        this.requiredCapacity = requiredCapacity;
        this.totalAvailableCapacity = totalAvailableCapacity;
        this.operationalDays = operationalDays;
    }

    // Getters a setters
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

    public double getRequiredCapacity() {
        return requiredCapacity;
    }

    public void setRequiredCapacity(double requiredCapacity) {
        this.requiredCapacity = requiredCapacity;
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
}
