package com.islandpower.configurator.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inverters")
public class Inverter {
    private int id;
    private String name;
    private double continuousPower25C;
    private double continuousPower40C;
    private double continuousPower65C;
    private double maxPower;
    private double efficiency;
    private int voltage;
    private double price; // Price of the inverter

    public Inverter(int id, String name, double continuousPower25C, double continuousPower40C,
                    double continuousPower65C, double maxPower, double efficiency, int voltage, double price) {
        this.id = id;
        this.name = name;
        this.continuousPower25C = continuousPower25C;
        this.continuousPower40C = continuousPower40C;
        this.continuousPower65C = continuousPower65C;
        this.maxPower = maxPower;
        this.efficiency = efficiency;
        this.voltage = voltage;
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

    public double getContinuousPower25C() {
        return continuousPower25C;
    }

    public void setContinuousPower25C(double continuousPower25C) {
        this.continuousPower25C = continuousPower25C;
    }

    public double getContinuousPower40C() {
        return continuousPower40C;
    }

    public void setContinuousPower40C(double continuousPower40C) {
        this.continuousPower40C = continuousPower40C;
    }

    public double getContinuousPower65C() {
        return continuousPower65C;
    }

    public void setContinuousPower65C(double continuousPower65C) {
        this.continuousPower65C = continuousPower65C;
    }

    public double getMaxPower() {
        return maxPower;
    }

    public void setMaxPower(double maxPower) {
        this.maxPower = maxPower;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public double getPrice() {
        return price; // Getter for price
    }

    public void setPrice(double price) {
        this.price = price; // Setter for price
    }
}
