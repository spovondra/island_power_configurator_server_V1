package com.islandpower.configurator.dto;

public class InverterDTO {
    private String id;
    private String name;
    private double continuousPower;
    private double maxPower;
    private double efficiency;
    private double voltage;

    // Constructor
    public InverterDTO(String id, String name, double continuousPower, double maxPower, double efficiency, double voltage) {
        this.id = id;
        this.name = name;
        this.continuousPower = continuousPower;
        this.maxPower = maxPower;
        this.efficiency = efficiency;
        this.voltage = voltage;
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

    public double getContinuousPower() {
        return continuousPower;
    }

    public void setContinuousPower(double continuousPower) {
        this.continuousPower = continuousPower;
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

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }
}
