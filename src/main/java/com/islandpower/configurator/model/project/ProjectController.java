package com.islandpower.configurator.model.project;

public class ProjectController {
    private String controllerId; // ID of the selected controller
    private String type; // Controller type (PWM, MPPT)
    private double requiredCurrent; // Required current for the controller [A]
    private double requiredPower; // Required power for the controller [W]
    private int seriesModules; // Number of modules in series
    private int parallelModules; // Number of modules in parallel
    private int maxModulesInSerial; // Maximum number of modules in serial
    private int minModulesInSerial; // Minimum number of modules in serial
    private boolean isValid; // Indicates if the controller configuration is valid
    private double adjustedOpenCircuitVoltage; // Adjusted open-circuit voltage for MPPT
    private double adjustedVoltageAtMaxPower; // Adjusted voltage at maximum power for MPPT
    private String statusMessage;

    // Constructors, getters, and setters
    public ProjectController() {}

    public ProjectController(String controllerId, String type, double requiredCurrent, double requiredPower,
                             int seriesModules, int parallelModules, int maxModulesInSerial, int minModulesInSerial,
                             boolean isValid, double adjustedOpenCircuitVoltage, double adjustedVoltageAtMaxPower) {
        this.controllerId = controllerId;
        this.type = type;
        this.requiredCurrent = requiredCurrent;
        this.requiredPower = requiredPower;
        this.seriesModules = seriesModules;
        this.parallelModules = parallelModules;
        this.maxModulesInSerial = maxModulesInSerial;
        this.minModulesInSerial = minModulesInSerial;
        this.isValid = isValid;
        this.adjustedOpenCircuitVoltage = adjustedOpenCircuitVoltage;
        this.adjustedVoltageAtMaxPower = adjustedVoltageAtMaxPower;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getRequiredCurrent() {
        return requiredCurrent;
    }

    public void setRequiredCurrent(double requiredCurrent) {
        this.requiredCurrent = requiredCurrent;
    }

    public double getRequiredPower() {
        return requiredPower;
    }

    public void setRequiredPower(double requiredPower) {
        this.requiredPower = requiredPower;
    }

    public int getSeriesModules() {
        return seriesModules;
    }

    public void setSeriesModules(int seriesModules) {
        this.seriesModules = seriesModules;
    }

    public int getParallelModules() {
        return parallelModules;
    }

    public void setParallelModules(int parallelModules) {
        this.parallelModules = parallelModules;
    }

    public int getMaxModulesInSerial() {
        return maxModulesInSerial;
    }

    public void setMaxModulesInSerial(int maxModulesInSerial) {
        this.maxModulesInSerial = maxModulesInSerial;
    }

    public int getMinModulesInSerial() {
        return minModulesInSerial;
    }

    public void setMinModulesInSerial(int minModulesInSerial) {
        this.minModulesInSerial = minModulesInSerial;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public double getAdjustedOpenCircuitVoltage() {
        return adjustedOpenCircuitVoltage;
    }

    public void setAdjustedOpenCircuitVoltage(double adjustedOpenCircuitVoltage) {
        this.adjustedOpenCircuitVoltage = adjustedOpenCircuitVoltage;
    }

    public double getAdjustedVoltageAtMaxPower() {
        return adjustedVoltageAtMaxPower;
    }

    public void setAdjustedVoltageAtMaxPower(double adjustedVoltageAtMaxPower) {
        this.adjustedVoltageAtMaxPower = adjustedVoltageAtMaxPower;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
