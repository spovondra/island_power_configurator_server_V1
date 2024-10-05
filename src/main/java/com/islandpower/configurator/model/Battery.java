package com.islandpower.configurator.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "batteries")
public class Battery {
    private int id;
    private String name;
    private String type; // Battery type (Li-ion, LiFePO4, Lead Acid)
    private int capacity; // Capacity in Ah
    private int voltage; // Voltage in V
    private double dod; // Depth of Discharge
    private double price; // Price of the battery

    public Battery(int id, String name, String type, int capacity, int voltage, double dod, double price) {
        this.id = id;
        this.name = name;
        this.type = type; // Initialize battery type
        this.capacity = capacity;
        this.voltage = voltage;
        this.dod = dod;
        this.price = price; // Initialize price
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type; // Getter for battery type
    }

    public void setType(String type) {
        this.type = type; // Setter for battery type
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
        return dod; // Getter for Depth of Discharge
    }

    public void setDod(double dod) {
        this.dod = dod; // Setter for Depth of Discharge
    }

    public double getPrice() {
        return price; // Getter for price
    }

    public void setPrice(double price) {
        this.price = price; // Setter for price
    }
}
