package com.islandpower.configurator.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "batteries")
public class Battery {
    private String id;
    private String name;
    private String type; // Battery type (Li-ion, LiFePO4, Lead Acid)
    private int capacity; // Capacity in Ah
    private int voltage; // Voltage in V
    private double dod; // Depth of Discharge
    private double price; // Price of the battery

    // New attributes
    private double maxDischargeRate; // Max discharge rate in A
    private double efficiency; // Efficiency in percentage

    // No-argument constructor
    public Battery() {
        // Initialize default values if needed
        this.maxDischargeRate = 0.0; // or any default value
        this.efficiency = 100; // or any default value
    }

    // Parameterized constructor
    public Battery(String id, String name, String type, int capacity, int voltage, double dod, double price, double maxDischargeRate, double efficiency) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.voltage = voltage;
        this.dod = dod;
        this.price = price;
        this.maxDischargeRate = maxDischargeRate; // Initialize max discharge rate
        this.efficiency = efficiency; // Initialize efficiency
    }

    // Getters and setters for new attributes
    public double getMaxDischargeRate() {
        return maxDischargeRate;
    }

    public void setMaxDischargeRate(double maxDischargeRate) {
        this.maxDischargeRate = maxDischargeRate;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    // Other existing getters and setters
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
}
