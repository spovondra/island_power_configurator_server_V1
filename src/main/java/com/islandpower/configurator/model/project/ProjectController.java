package com.islandpower.configurator.model.project;

/**
 * Model representing the controller configuration in a project.
 * <p>
 * This class defines attributes related to the configuration of a controller,
 * including its type, required electrical properties, module arrangements,
 * and validity status.
 * </p>
 *
 * @version 1.0
 */
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
    private String statusMessage; // Status message for the configuration

    /**
     * Default constructor for ProjectController.
     */
    public ProjectController() {}

    // Getters and Setters

    /**
     * Retrieves the ID of the selected controller.
     *
     * @return String - the ID of the selected controller
     */
    public String getControllerId() {
        return controllerId;
    }

    /**
     * Updates the ID of the selected controller.
     *
     * @param controllerId - the new controller ID
     */
    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    /**
     * Retrieves the type of the controller (PWM or MPPT).
     *
     * @return String - the type of the controller
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type of the controller (PWM or MPPT).
     *
     * @param type - the new controller type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the required current for the controller.
     *
     * @return double - the required current in amperes (A)
     */
    public double getRequiredCurrent() {
        return requiredCurrent;
    }

    /**
     * Updates the required current for the controller.
     *
     * @param requiredCurrent - the new required current in amperes (A)
     */
    public void setRequiredCurrent(double requiredCurrent) {
        this.requiredCurrent = requiredCurrent;
    }

    /**
     * Retrieves the required power for the controller.
     *
     * @return double - the required power in watts (W)
     */
    public double getRequiredPower() {
        return requiredPower;
    }

    /**
     * Updates the required power for the controller.
     *
     * @param requiredPower - the new required power in watts (W)
     */
    public void setRequiredPower(double requiredPower) {
        this.requiredPower = requiredPower;
    }

    /**
     * Retrieves the number of modules in series.
     *
     * @return int - the number of modules in series
     */
    public int getSeriesModules() {
        return seriesModules;
    }

    /**
     * Updates the number of modules in series.
     *
     * @param seriesModules - the new number of modules in series
     */
    public void setSeriesModules(int seriesModules) {
        this.seriesModules = seriesModules;
    }

    /**
     * Retrieves the number of modules in parallel.
     *
     * @return int - the number of modules in parallel
     */
    public int getParallelModules() {
        return parallelModules;
    }

    /**
     * Updates the number of modules in parallel.
     *
     * @param parallelModules - the new number of modules in parallel
     */
    public void setParallelModules(int parallelModules) {
        this.parallelModules = parallelModules;
    }

    /**
     * Retrieves the maximum number of modules allowed in series.
     *
     * @return int - the maximum number of modules in series
     */
    public int getMaxModulesInSerial() {
        return maxModulesInSerial;
    }

    /**
     * Updates the maximum number of modules allowed in series.
     *
     * @param maxModulesInSerial - the new maximum number of modules in series
     */
    public void setMaxModulesInSerial(int maxModulesInSerial) {
        this.maxModulesInSerial = maxModulesInSerial;
    }

    /**
     * Retrieves the minimum number of modules required in series.
     *
     * @return int - the minimum number of modules in series
     */
    public int getMinModulesInSerial() {
        return minModulesInSerial;
    }

    /**
     * Updates the minimum number of modules required in series.
     *
     * @param minModulesInSerial - the new minimum number of modules in series
     */
    public void setMinModulesInSerial(int minModulesInSerial) {
        this.minModulesInSerial = minModulesInSerial;
    }

    /**
     * Checks if the controller configuration is valid.
     *
     * @return boolean - true if the configuration is valid, false otherwise
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * Updates the validity status of the controller configuration.
     *
     * @param isValid - the new validity status
     */
    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * Retrieves the adjusted open-circuit voltage for MPPT controllers.
     *
     * @return double - the adjusted open-circuit voltage in volts (V)
     */
    public double getAdjustedOpenCircuitVoltage() {
        return adjustedOpenCircuitVoltage;
    }

    /**
     * Updates the adjusted open-circuit voltage for MPPT controllers.
     *
     * @param adjustedOpenCircuitVoltage - the new adjusted open-circuit voltage in volts (V)
     */
    public void setAdjustedOpenCircuitVoltage(double adjustedOpenCircuitVoltage) {
        this.adjustedOpenCircuitVoltage = adjustedOpenCircuitVoltage;
    }

    /**
     * Retrieves the adjusted voltage at maximum power for MPPT controllers.
     *
     * @return double - the adjusted voltage at maximum power in volts (V)
     */
    public double getAdjustedVoltageAtMaxPower() {
        return adjustedVoltageAtMaxPower;
    }

    /**
     * Updates the adjusted voltage at maximum power for MPPT controllers.
     *
     * @param adjustedVoltageAtMaxPower - the new adjusted voltage at maximum power in volts (V)
     */
    public void setAdjustedVoltageAtMaxPower(double adjustedVoltageAtMaxPower) {
        this.adjustedVoltageAtMaxPower = adjustedVoltageAtMaxPower;
    }

    /**
     * Retrieves the status message for the controller configuration.
     *
     * @return String - the status message
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Updates the status message for the controller configuration.
     *
     * @param statusMessage - the new status message
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
