package com.islandpower.configurator.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a solar charge controller, including its technical specifications and performance parameters.
 * This model defines the attributes of a controller, such as its rated power, voltage range, type, and efficiency.
 *
 * @version 1.0
 */
@Document(collection = "controllers")
public class Controller {

    private String id; // unique identifier for the controller
    private String name; // name of the controller
    private double ratedPower; // rated power of the controller in watts
    private double currentRating; // maximum current rating of the controller in amperes
    private double maxVoltage; // maximum input voltage of the controller in volts
    private double minVoltage; // minimum input voltage of the controller in volts
    private String type; // type of the controller
    private double efficiency; // efficiency of the controller in percentage

    /**
     * Constructs a Controller object with specified parameters.
     *
     * @param id The unique identifier for the controller
     * @param name The name of the controller
     * @param ratedPower The rated power of the controller in watts
     * @param currentRating The maximum current rating of the controller in amperes
     * @param maxVoltage The maximum input voltage of the controller in volts
     * @param minVoltage The minimum input voltage of the controller in volts
     * @param type The type of the controller (e.g., PWM, MPPT)
     * @param efficiency The efficiency of the controller as a percentage
     */
    public Controller(String id, String name, double ratedPower, double currentRating,
                      double maxVoltage, double minVoltage, String type, double efficiency) {
        this.id = id;
        this.name = name;
        this.ratedPower = ratedPower;
        this.currentRating = currentRating;
        this.maxVoltage = maxVoltage;
        this.minVoltage = minVoltage;
        this.type = type;
        this.efficiency = efficiency;
    }

    /**
     * Retrieves the unique identifier of the controller.
     *
     * @return String The unique ID of the controller
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the unique identifier of the controller.
     *
     * @param id The new unique ID of the controller
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the controller.
     *
     * @return String The name of the controller
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the controller.
     *
     * @param name The new name of the controller
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the rated power of the controller in watts.
     *
     * @return double The rated power of the controller
     */
    public double getRatedPower() {
        return ratedPower;
    }

    /**
     * Updates the rated power of the controller in watts.
     *
     * @param ratedPower The new rated power of the controller
     */
    public void setRatedPower(double ratedPower) {
        this.ratedPower = ratedPower;
    }

    /**
     * Retrieves the maximum current rating of the controller in amperes.
     *
     * @return double The maximum current rating of the controller
     */
    public double getCurrentRating() {
        return currentRating;
    }

    /**
     * Updates the maximum current rating of the controller in amperes.
     *
     * @param currentRating The new maximum current rating of the controller
     */
    public void setCurrentRating(double currentRating) {
        this.currentRating = currentRating;
    }

    /**
     * Retrieves the maximum input voltage of the controller in volts.
     *
     * @return double The maximum input voltage of the controller
     */
    public double getMaxVoltage() {
        return maxVoltage;
    }

    /**
     * Updates the maximum input voltage of the controller in volts.
     *
     * @param maxVoltage The new maximum input voltage of the controller
     */
    public void setMaxVoltage(double maxVoltage) {
        this.maxVoltage = maxVoltage;
    }

    /**
     * Retrieves the minimum input voltage of the controller in volts.
     *
     * @return double The minimum input voltage of the controller
     */
    public double getMinVoltage() {
        return minVoltage;
    }

    /**
     * Updates the minimum input voltage of the controller in volts.
     *
     * @param minVoltage The new minimum input voltage of the controller
     */
    public void setMinVoltage(double minVoltage) {
        this.minVoltage = minVoltage;
    }

    /**
     * Retrieves the type of the controller
     *
     * @return String The type of the controller
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type of the controller
     *
     * @param type The new type of the controller
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the efficiency of the controller as a percentage.
     *
     * @return double The efficiency of the controller
     */
    public double getEfficiency() {
        return efficiency;
    }

    /**
     * Updates the efficiency of the controller as a percentage.
     *
     * @param efficiency The new efficiency of the controller
     */
    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}
