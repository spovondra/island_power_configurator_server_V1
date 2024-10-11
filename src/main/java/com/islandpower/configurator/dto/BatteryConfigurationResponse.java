package com.islandpower.configurator.dto;

public class BatteryConfigurationResponse {
    private String projectId;
    private int parallelBatteries;
    private int seriesBatteries;
    private double requiredCapacity;
    private double batteryCapacityDOD; // Depth of Discharge
    private double operationalVoltage; // Assume this value will be calculated or retrieved

    // Constructors, Getters, and Setters

    public BatteryConfigurationResponse(String projectId, int parallelBatteries, int seriesBatteries,
                                        double requiredCapacity, double batteryCapacityDOD, double operationalVoltage) {
        this.projectId = projectId;
        this.parallelBatteries = parallelBatteries;
        this.seriesBatteries = seriesBatteries;
        this.requiredCapacity = requiredCapacity;
        this.batteryCapacityDOD = batteryCapacityDOD;
        this.operationalVoltage = operationalVoltage;
    }

    // Getters
    public String getProjectId() {
        return projectId;
    }

    public int getParallelBatteries() {
        return parallelBatteries;
    }

    public int getSeriesBatteries() {
        return seriesBatteries;
    }

    public double getRequiredCapacity() {
        return requiredCapacity;
    }

    public double getBatteryCapacityDOD() {
        return batteryCapacityDOD;
    }

    public double getOperationalVoltage() {
        return operationalVoltage;
    }
}
