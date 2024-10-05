package com.islandpower.configurator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "controllers")
public class Controller {
    private int id; // Unikátní identifikátor regulátoru
    private String name; // Název regulátoru
    private double ratedPower; // Jmenovitý výkon regulátoru (W)
    private double currentRating; // Maximální proud regulátoru (A)
    private double maxVoltage; // Maximální napětí regulátoru (V)
    private double minVoltage; // Minimální napětí regulátoru (V)
    private String type; // Typ regulátoru (např. PWM, MPPT)
    private double efficiency; // Účinnost regulátoru

    // Konstruktory
    public Controller(int id, String name, double ratedPower, double currentRating,
                      double maxVoltage, double minVoltage, String type, double efficiency) {
        this.id = id;
        this.name = name;
        this.ratedPower = ratedPower;
        this.currentRating = currentRating;
        this.maxVoltage = maxVoltage;
        this.minVoltage = minVoltage;
        this.type = type;
        this.efficiency = efficiency; // Inicializace účinnosti
    }

    // Getter a setter metody
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

    public double getRatedPower() {
        return ratedPower;
    }

    public void setRatedPower(double ratedPower) {
        this.ratedPower = ratedPower;
    }

    public double getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(double currentRating) {
        this.currentRating = currentRating;
    }

    public double getMaxVoltage() {
        return maxVoltage;
    }

    public void setMaxVoltage(double maxVoltage) {
        this.maxVoltage = maxVoltage;
    }

    public double getMinVoltage() {
        return minVoltage;
    }

    public void setMinVoltage(double minVoltage) {
        this.minVoltage = minVoltage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
}
