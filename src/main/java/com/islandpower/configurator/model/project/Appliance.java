package com.islandpower.configurator.model.project;

/**
 * Represents an appliance used in a solar energy project.
 * This class stores information about an individual appliance, including its power rating,
 * quantity, and daily usage. It is used to track and manage appliances within a project.
 *
 * @version 1.0
 */
public class Appliance {

    /**
     * Unique identifier for the appliance.
     */
    private String id;

    /**
     * Name of the appliance.
     * This is a descriptive name for the appliance (e.g., "Refrigerator", "Washing Machine").
     */
    private String name;

    /**
     * Power rating of the appliance in watts (W).
     * This value indicates the amount of power the appliance consumes during operation.
     */
    private double power;

    /**
     * Quantity of the appliance.
     * This specifies how many units of the appliance are used in the project.
     */
    private int quantity;

    /**
     * Daily usage hours of the appliance.
     * This represents the number of hours the appliance is used per day.
     */
    private int dailyUsageHours;

    // Getters and Setters

    /**
     * Gets the unique identifier for the appliance.
     *
     * @return The unique ID of the appliance.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the appliance.
     *
     * @param id The unique ID of the appliance.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the appliance.
     *
     * @return The name of the appliance.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the appliance.
     *
     * @param name The name of the appliance.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the power rating of the appliance.
     *
     * @return The power rating in watts.
     */
    public double getPower() {
        return power;
    }

    /**
     * Sets the power rating of the appliance.
     *
     * @param power The power rating in watts.
     */
    public void setPower(double power) {
        this.power = power;
    }

    /**
     * Gets the quantity of the appliance.
     *
     * @return The quantity of the appliance.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the appliance.
     *
     * @param quantity The quantity of the appliance.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the daily usage hours of the appliance.
     *
     * @return The number of hours the appliance is used per day.
     */
    public int getDailyUsageHours() {
        return dailyUsageHours;
    }

    /**
     * Sets the daily usage hours of the appliance.
     *
     * @param dailyUsageHours The number of hours the appliance is used per day.
     */
    public void setDailyUsageHours(int dailyUsageHours) {
        this.dailyUsageHours = dailyUsageHours;
    }
}
