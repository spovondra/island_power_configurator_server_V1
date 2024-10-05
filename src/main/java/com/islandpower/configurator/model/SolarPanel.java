package com.islandpower.configurator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "solarPanels")
public class SolarPanel {
    private String id; // Panel identifier (e.g., "panel1")
    private String manufacturer; // Manufacturer of the panel
    private String name; // Name of the panel
    private double pRated; // Rated power in Watts
    private double voc; // Open-circuit voltage
    private double isc; // Short-circuit current
    private double vmp; // Voltage at maximum power
    private double imp; // Current at maximum power
    private double tempCoefficientPMax; // Temperature coefficient for maximum power
    private double tolerance; // Panel tolerance percentage
    private double degradationFirstYear; // Degradation in the first year
    private double degradationYears; // Degradation per year after the first year
    private double price; // Price of the solar panel

    // Constructor
    public SolarPanel(String id, String manufacturer, String name, double pRated, double voc, double isc,
                      double vmp, double imp, double tempCoefficientPMax, double tolerance,
                      double degradationFirstYear, double degradationYears, double price) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.name = name;
        this.pRated = pRated;
        this.voc = voc;
        this.isc = isc;
        this.vmp = vmp;
        this.imp = imp;
        this.tempCoefficientPMax = tempCoefficientPMax;
        this.tolerance = tolerance;
        this.degradationFirstYear = degradationFirstYear;
        this.degradationYears = degradationYears;
        this.price = price;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getpRated() {
        return pRated;
    }

    public void setpRated(double pRated) {
        this.pRated = pRated;
    }

    public double getVoc() {
        return voc;
    }

    public void setVoc(double voc) {
        this.voc = voc;
    }

    public double getIsc() {
        return isc;
    }

    public void setIsc(double isc) {
        this.isc = isc;
    }

    public double getVmp() {
        return vmp;
    }

    public void setVmp(double vmp) {
        this.vmp = vmp;
    }

    public double getImp() {
        return imp;
    }

    public void setImp(double imp) {
        this.imp = imp;
    }

    public double getTempCoefficientPMax() {
        return tempCoefficientPMax;
    }

    public void setTempCoefficientPMax(double tempCoefficientPMax) {
        this.tempCoefficientPMax = tempCoefficientPMax;
    }

    public double getTolerance() {
        return tolerance;
    }

    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    public double getDegradationFirstYear() {
        return degradationFirstYear;
    }

    public void setDegradationFirstYear(double degradationFirstYear) {
        this.degradationFirstYear = degradationFirstYear;
    }

    public double getDegradationYears() {
        return degradationYears;
    }

    public void setDegradationYears(double degradationYears) {
        this.degradationYears = degradationYears;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
