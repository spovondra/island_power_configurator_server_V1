package com.islandpower.configurator.model.project;

public class ProjectController {
    private String controllerId; // ID vybraného regulátoru
    private String type; // Typ regulátoru (např. PWM, MPPT)
    private double requiredCurrent; // Požadovaný proud pro regulátor [A]
    private double requiredPower; // Požadovaný výkon pro regulátor [W]
    private int seriesModules; // Počet modulů v sérii
    private int parallelModules; // Počet modulů paralelně
    private boolean isValid; // Příznak, zda je regulátor validní pro tuto konfiguraci
    private double adjustedOpenCircuitVoltage; // Upravené otevřené napětí pro MPPT
    private double adjustedVoltageAtMaxPower; // Upravené napětí při maximálním výkonu pro MPPT
    private double totalControllerEfficiency; // Účinnost regulátoru

    public ProjectController() {
    }

    public ProjectController(String controllerId, String type, double requiredCurrent, double requiredPower,
                             int seriesModules, int parallelModules, boolean isValid, double adjustedOpenCircuitVoltage,
                             double adjustedVoltageAtMaxPower, double totalControllerEfficiency) {
        this.controllerId = controllerId;
        this.type = type;
        this.requiredCurrent = requiredCurrent;
        this.requiredPower = requiredPower;
        this.seriesModules = seriesModules;
        this.parallelModules = parallelModules;
        this.isValid = isValid;
        this.adjustedOpenCircuitVoltage = adjustedOpenCircuitVoltage;
        this.adjustedVoltageAtMaxPower = adjustedVoltageAtMaxPower;
        this.totalControllerEfficiency = totalControllerEfficiency;
    }

    // Gettery a Settery
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

    public double getTotalControllerEfficiency() {
        return totalControllerEfficiency;
    }

    public void setTotalControllerEfficiency(double totalControllerEfficiency) {
        this.totalControllerEfficiency = totalControllerEfficiency;
    }
}
