package com.islandpower.configurator.model.project;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model representing an appliance in a project.
 * <p>
 * This class defines the attributes of an appliance, including its type,
 * power consumption, usage details, and calculated energy requirements.
 * </p>
 *
 * @version 1.0
 */
@Document(collection = "appliances")
public class Appliance {
    private String id;
    private String name;
    private String type; // AC or DC
    private int quantity;
    private double power; // in Watts
    private double hours; // Hours per day
    private double days; // Days per week
    private double peakPower; // in Watts
    private double energy; // Energy consumption calculated

    /**
     * Retrieves the ID of the appliance.
     *
     * @return String - the ID of the appliance
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the ID of the appliance.
     *
     * @param id - the new ID of the appliance
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the appliance.
     *
     * @return String - the name of the appliance
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the appliance.
     *
     * @param name - the new name of the appliance
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the type of the appliance (AC or DC).
     *
     * @return String - the type of the appliance
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type of the appliance (AC or DC).
     *
     * @param type - the new type of the appliance
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Retrieves the quantity of the appliance.
     *
     * @return int - the quantity of the appliance
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Updates the quantity of the appliance.
     *
     * @param quantity - the new quantity of the appliance
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Retrieves the power consumption of the appliance in Watts.
     *
     * @return double - the power consumption in Watts
     */
    public double getPower() {
        return power;
    }

    /**
     * Updates the power consumption of the appliance in Watts.
     *
     * @param power - the new power consumption in Watts
     */
    public void setPower(double power) {
        this.power = power;
    }

    /**
     * Retrieves the number of hours the appliance is used per day.
     *
     * @return double - the usage hours per day
     */
    public double getHours() {
        return hours;
    }

    /**
     * Updates the number of hours the appliance is used per day.
     *
     * @param hours - the new usage hours per day
     */
    public void setHours(double hours) {
        this.hours = hours;
    }

    /**
     * Retrieves the number of days the appliance is used per week.
     *
     * @return double - the usage days per week
     */
    public double getDays() {
        return days;
    }

    /**
     * Updates the number of days the appliance is used per week.
     *
     * @param days - the new usage days per week
     */
    public void setDays(double days) {
        this.days = days;
    }

    /**
     * Retrieves the peak power consumption of the appliance in Watts.
     *
     * @return double - the peak power in Watts
     */
    public double getPeakPower() {
        return peakPower;
    }

    /**
     * Updates the peak power consumption of the appliance in Watts.
     *
     * @param peakPower - the new peak power in Watts
     */
    public void setPeakPower(double peakPower) {
        this.peakPower = peakPower;
    }

    /**
     * Retrieves the calculated energy consumption of the appliance.
     *
     * @return double - the energy consumption
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * Updates the calculated energy consumption of the appliance.
     *
     * @param energy - the new energy consumption
     */
    public void setEnergy(double energy) {
        this.energy = energy;
    }
}
