package com.islandpower.configurator.model.project;

/**
 * Model representing the controller configuration in a project.
 *
 * @version 1.0
 */
public class ProjectController {

    private String controllerId; // id of the selected controller
    private String type; // controller type (PWM x MPPT)
    private double requiredCurrent; // required current for the controller in amperes
    private double requiredPower; // required power for the controller in watts (W)
    private int seriesModules; // number of modules in series
    private int parallelModules; // number of modules in parallel
    private int maxModulesInSerial; // maximum number of modules in series
    private int minModulesInSerial; // minimum number of modules in series
    private boolean isValid; // indicates if the controller configuration is valid
    private double adjustedOpenCircuitVoltage; // adjusted open-circuit voltage for MPPT in volts
    private double adjustedVoltageAtMaxPower; // adjusted voltage at maximum power for MPPT in volts
    private String statusMessage; // status message for the configuration

    /**
     * Retrieves the ID of the selected controller.
     *
     * @return String The ID of the selected controller
     */
    public String getControllerId() {
        return controllerId;
    }

    /**
     * Updates the ID of the selected controller.
     *
     * @param controllerId The new controller ID
     */
    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    /**
     * Retrieves the type of the controller (PWM or MPPT).
     *
     * @return String The type of the controller
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type of the controller (PWM or MPPT).
     *
     * @param type The new controller type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the required current for the controller.
     *
     * @return double The required current in amperes
     */
    public double getRequiredCurrent() {
        return requiredCurrent;
    }

    /**
     * Updates the required current for the controller.
     *
     * @param requiredCurrent The new required current in amperes
     */
    public void setRequiredCurrent(double requiredCurrent) {
        this.requiredCurrent = requiredCurrent;
    }

    /**
     * Retrieves the required power for the controller.
     *
     * @return double The required power in watts
     */
    public double getRequiredPower() {
        return requiredPower;
    }

    /**
     * Updates the required power for the controller.
     *
     * @param requiredPower The new required power in watts
     */
    public void setRequiredPower(double requiredPower) {
        this.requiredPower = requiredPower;
    }

    /**
     * Retrieves the number of modules in series.
     *
     * @return int The number of modules in series
     */
    public int getSeriesModules() {
        return seriesModules;
    }

    /**
     * Updates the number of modules in series.
     *
     * @param seriesModules The new number of modules in series
     */
    public void setSeriesModules(int seriesModules) {
        this.seriesModules = seriesModules;
    }

    /**
     * Retrieves the number of modules in parallel.
     *
     * @return int The number of modules in parallel
     */
    public int getParallelModules() {
        return parallelModules;
    }

    /**
     * Updates the number of modules in parallel.
     *
     * @param parallelModules The new number of modules in parallel
     */
    public void setParallelModules(int parallelModules) {
        this.parallelModules = parallelModules;
    }

    /**
     * Retrieves the maximum number of modules allowed in series.
     *
     * @return int The maximum number of modules in series
     */
    public int getMaxModulesInSerial() {
        return maxModulesInSerial;
    }

    /**
     * Updates the maximum number of modules allowed in series.
     *
     * @param maxModulesInSerial The new maximum number of modules in series
     */
    public void setMaxModulesInSerial(int maxModulesInSerial) {
        this.maxModulesInSerial = maxModulesInSerial;
    }

    /**
     * Retrieves the minimum number of modules required in series.
     *
     * @return int The minimum number of modules in series
     */
    public int getMinModulesInSerial() {
        return minModulesInSerial;
    }

    /**
     * Updates the minimum number of modules required in series.
     *
     * @param minModulesInSerial The new minimum number of modules in series
     */
    public void setMinModulesInSerial(int minModulesInSerial) {
        this.minModulesInSerial = minModulesInSerial;
    }

    /**
     * Checks if the controller configuration is valid.
     *
     * @return boolean True if the configuration is valid, false otherwise
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Updates the validity status of the controller configuration.
     *
     * @param isValid The new validity status
     */
    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * Retrieves the adjusted open-circuit voltage for MPPT controllers.
     *
     * @return double The adjusted open-circuit voltage in volts
     */
    public double getAdjustedOpenCircuitVoltage() {
        return adjustedOpenCircuitVoltage;
    }

    /**
     * Updates the adjusted open-circuit voltage for MPPT controllers.
     *
     * @param adjustedOpenCircuitVoltage The new adjusted open-circuit voltage in volts
     */
    public void setAdjustedOpenCircuitVoltage(double adjustedOpenCircuitVoltage) {
        this.adjustedOpenCircuitVoltage = adjustedOpenCircuitVoltage;
    }

    /**
     * Retrieves the adjusted voltage at maximum power for MPPT controllers.
     *
     * @return double The adjusted voltage at maximum power in volts
     */
    public double getAdjustedVoltageAtMaxPower() {
        return adjustedVoltageAtMaxPower;
    }

    /**
     * Updates the adjusted voltage at maximum power for MPPT controllers.
     *
     * @param adjustedVoltageAtMaxPower The new adjusted voltage at maximum power in volts
     */
    public void setAdjustedVoltageAtMaxPower(double adjustedVoltageAtMaxPower) {
        this.adjustedVoltageAtMaxPower = adjustedVoltageAtMaxPower;
    }

    /**
     * Retrieves the status message for the controller configuration.
     *
     * @return String The status message
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Updates the status message for the controller configuration.
     *
     * @param statusMessage The new status message
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
