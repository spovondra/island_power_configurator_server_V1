package com.islandpower.configurator.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a battery used in the system, including its technical specifications and performance parameters.
 * This model defines the attributes of a battery, such as its capacity, voltage, depth of discharge (DOD),
 * and efficiency, along with pricing and charging parameters.
 *
 * @version 1.0
 */
@Document(collection = "batteries")
public class Battery {

    private String id; // unique identifier for the battery
    private String name; // name of the battery
    private String type; // battery type
    private int capacity; // capacity of the battery in ampere-hours
    private int voltage; // voltage of the battery in volts (V)
    private double dod; // depth of discharge (DOD) as a fraction
    private double price; // price of the battery in the local currency
    private double optimalChargingCurrent; // optimal charging current in amperes
    private double maxChargingCurrent; // maximum charging current in amperes
    private double efficiency; // efficiency of the battery as a percentage

    /**
     * Retrieves the unique identifier of the battery.
     *
     * @return String The unique ID of the battery
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the unique identifier of the battery.
     *
     * @param id The new unique ID of the battery
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the battery.
     *
     * @return String The name of the battery
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the battery.
     *
     * @param name The new name of the battery
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the type of the battery.
     *
     * @return String The type of the battery
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type of the battery.
     *
     * @param type The new type of the battery
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the capacity of the battery in ampere-hours.
     *
     * @return int The capacity of the battery
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Updates the capacity of the battery in ampere-hours.
     *
     * @param capacity The new capacity of the battery
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Retrieves the voltage of the battery in volts.
     *
     * @return int The voltage of the battery
     */
    public int getVoltage() {
        return voltage;
    }

    /**
     * Updates the voltage of the battery in volts.
     *
     * @param voltage The new voltage of the battery
     */
    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    /**
     * Retrieves the depth of discharge (DOD) of the battery as a fraction.
     *
     * @return double The DOD of the battery
     */
    public double getDod() {
        return dod;
    }

    /**
     * Updates the depth of discharge (DOD) of the battery as a fraction.
     *
     * @param dod The new DOD of the battery
     */
    public void setDod(double dod) {
        this.dod = dod;
    }

    /**
     * Retrieves the price of the battery in the local currency.
     *
     * @return double The price of the battery
     */
    public double getPrice() {
        return price;
    }

    /**
     * Updates the price of the battery in the local currency.
     *
     * @param price The new price of the battery
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Retrieves the optimal charging current of the battery in amperes.
     *
     * @return double The optimal charging current
     */
    public double getOptimalChargingCurrent() {
        return optimalChargingCurrent;
    }

    /**
     * Updates the optimal charging current of the battery in amperes.
     *
     * @param optimalChargingCurrent The new optimal charging current
     */
    public void setOptimalChargingCurrent(double optimalChargingCurrent) {
        this.optimalChargingCurrent = optimalChargingCurrent;
    }

    /**
     * Retrieves the maximum charging current of the battery in amperes.
     *
     * @return double The maximum charging current
     */
    public double getMaxChargingCurrent() {
        return maxChargingCurrent;
    }

    /**
     * Updates the maximum charging current of the battery in amperes.
     *
     * @param maxChargingCurrent The new maximum charging current
     */
    public void setMaxChargingCurrent(double maxChargingCurrent) {
        this.maxChargingCurrent = maxChargingCurrent;
    }

    /**
     * Retrieves the efficiency of the battery as a percentage.
     *
     * @return double The efficiency of the battery
     */
    public double getEfficiency() {
        return efficiency;
    }

    /**
     * Updates the efficiency of the battery as a percentage.
     *
     * @param efficiency The new efficiency of the battery
     */
    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}
