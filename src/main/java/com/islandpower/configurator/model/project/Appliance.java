package com.islandpower.configurator.model.project;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model representing an appliance in a project.
 * This class defines the attributes of an appliance, including its type,
 * power consumption, usage details, and calculated energy requirements.
 *
 * @version 1.0
 */
@Document(collection = "appliances")
public class Appliance {

    private String id; // unique identifier for the appliance
    private String name; // name of the appliance
    private String type; // type of the appliance (AC or DC)
    private int quantity; // number of units of the appliance
    private double power; // power consumption of the appliance in watts
    private double hours; // number of hours the appliance is used per day
    private double days; // number of days the appliance is used per week
    private double peakPower; // peak power consumption of the appliance in watts
    private double energy; // calculated energy consumption of the appliance

    /**
     * Retrieves the ID of the appliance.
     *
     * @return String The ID of the appliance
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the ID of the appliance.
     *
     * @param id The new ID of the appliance
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the appliance.
     *
     * @return String The name of the appliance
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the appliance.
     *
     * @param name The new name of the appliance
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the type of the appliance (AC or DC).
     *
     * @return String The type of the appliance
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type of the appliance (AC or DC).
     *
     * @param type The new type of the appliance
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the quantity of the appliance.
     *
     * @return int The quantity of the appliance
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Updates the quantity of the appliance.
     *
     * @param quantity The new quantity of the appliance
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Retrieves the power consumption of the appliance in watts.
     *
     * @return double The power consumption in watts
     */
    public double getPower() {
        return power;
    }

    /**
     * Updates the power consumption of the appliance in watts.
     *
     * @param power The new power consumption in watts
     */
    public void setPower(double power) {
        this.power = power;
    }

    /**
     * Retrieves the number of hours the appliance is used per day.
     *
     * @return double The usage hours per day
     */
    public double getHours() {
        return hours;
    }

    /**
     * Updates the number of hours the appliance is used per day.
     *
     * @param hours The new usage hours per day
     */
    public void setHours(double hours) {
        this.hours = hours;
    }

    /**
     * Retrieves the number of days the appliance is used per week.
     *
     * @return double The usage days per week
     */
    public double getDays() {
        return days;
    }

    /**
     * Updates the number of days the appliance is used per week.
     *
     * @param days The new usage days per week
     */
    public void setDays(double days) {
        this.days = days;
    }

    /**
     * Retrieves the peak power consumption of the appliance in watts.
     *
     * @return double The peak power in watts
     */
    public double getPeakPower() {
        return peakPower;
    }

    /**
     * Updates the peak power consumption of the appliance in watts.
     *
     * @param peakPower The new peak power in watts
     */
    public void setPeakPower(double peakPower) {
        this.peakPower = peakPower;
    }

    /**
     * Retrieves the calculated energy consumption of the appliance.
     *
     * @return double The energy consumption
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * Updates the calculated energy consumption of the appliance.
     *
     * @param energy The new energy consumption
     */
    public void setEnergy(double energy) {
        this.energy = energy;
    }
}
