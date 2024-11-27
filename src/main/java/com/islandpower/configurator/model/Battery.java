package com.islandpower.configurator.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a battery used in the system, including its technical specifications and performance parameters.
 */
@Document(collection = "batteries")
public class Battery {
    private String id; // Unique identifier for the battery
    private String name; // Name of the battery
    private String type; // Battery type (e.g., Li-ion, LiFePO4, Lead Acid)
    private int capacity; // Capacity of the battery in Ah
    private int voltage; // Voltage of the battery in V
    private double dod; // Depth of Discharge (DOD) as a fraction (e.g., 0.8 for 80%)
    private double price; // Price of the battery in the local currency
    private double optimalChargingCurrent; // Optimal charging current (I_battery_optimal) in A
    private double maxChargingCurrent; // Maximum charging current (I_battery_max) in A
    private double efficiency; // Efficiency of the battery in percentage (e.g., 95 for 95%)

    /**
     * Default constructor.
     */
    public Battery() {
        // Initialize default values if needed
        this.optimalChargingCurrent = 0.0;
        this.maxChargingCurrent = 0.0;
        this.efficiency = 100.0; // Default efficiency is 100%
    }

    /**
     * Parameterized constructor to initialize all fields.
     *
     * @param id the unique identifier
     * @param name the name of the battery
     * @param type the type of the battery (e.g., Li-ion)
     * @param capacity the capacity in Ah
     * @param voltage the voltage in V
     * @param dod the depth of discharge
     * @param price the price of the battery
     * @param optimalChargingCurrent the optimal charging current in A
     * @param maxChargingCurrent the maximum charging current in A
     * @param efficiency the efficiency in percentage
     */
    public Battery(String id, String name, String type, int capacity, int voltage, double dod, double price,
                   double optimalChargingCurrent, double maxChargingCurrent, double efficiency) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.voltage = voltage;
        this.dod = dod;
        this.price = price;
        this.optimalChargingCurrent = optimalChargingCurrent;
        this.maxChargingCurrent = maxChargingCurrent;
        this.efficiency = efficiency;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public double getDod() {
        return dod;
    }

    public void setDod(double dod) {
        this.dod = dod;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOptimalChargingCurrent() {
        return optimalChargingCurrent;
    }

    public void setOptimalChargingCurrent(double optimalChargingCurrent) {
        this.optimalChargingCurrent = optimalChargingCurrent;
    }

    public double getMaxChargingCurrent() {
        return maxChargingCurrent;
    }

    public void setMaxChargingCurrent(double maxChargingCurrent) {
        this.maxChargingCurrent = maxChargingCurrent;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}
