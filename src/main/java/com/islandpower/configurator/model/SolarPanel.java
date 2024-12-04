package com.islandpower.configurator.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a solar panel used in the system.
 * This class defines the panel's specifications, including power ratings, voltage, current, and efficiency attributes.
 *
 * @version 1.0
 */
@Document(collection = "solarPanels")
public class SolarPanel {
    private String id; // panel identifier
    private String manufacturer; // manufacturer of the panel
    private String name; // name of the panel
    private double pRated; // rated power in watts
    private double voc; // open-circuit voltage
    private double isc; // short-circuit current
    private double vmp; // voltage at maximum power
    private double imp; // current at maximum power
    private double tempCoefficientPMax; // temperature coefficient for maximum power
    private double tempCoefficientVoc; // temperature coefficient for open-circuit voltage
    private double tolerance; // panel tolerance percentage
    private double degradationFirstYear; // degradation in the first year
    private double degradationYears; // degradation per year after the first year
    private double price; // price of the solar panel

    /**
     * Constructs a SolarPanel object with the specified attributes.
     *
     * @param id The identifier of the panel
     * @param manufacturer The manufacturer of the panel
     * @param name The name of the panel
     * @param pRated The rated power in watts
     * @param voc The open-circuit voltage
     * @param isc The short-circuit current
     * @param vmp The voltage at maximum power
     * @param imp The current at maximum power
     * @param tempCoefficientPMax The temperature coefficient for maximum power
     * @param tempCoefficientVoc The temperature coefficient for open-circuit voltage
     * @param tolerance The panel tolerance percentage
     * @param degradationFirstYear The degradation in the first year
     * @param degradationYears The degradation per year after the first year
     * @param price The price of the solar panel
     */
    public SolarPanel(String id, String manufacturer, String name, double pRated, double voc, double isc,
                      double vmp, double imp, double tempCoefficientPMax, double tempCoefficientVoc, double tolerance,
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
        this.tempCoefficientVoc = tempCoefficientVoc;
        this.tolerance = tolerance;
        this.degradationFirstYear = degradationFirstYear;
        this.degradationYears = degradationYears;
        this.price = price;
    }

    /**
     * Retrieves the identifier of the panel.
     *
     * @return String The identifier of the panel
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the identifier of the panel.
     *
     * @param id The new identifier of the panel
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the manufacturer of the panel.
     *
     * @return String The manufacturer of the panel
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Updates the manufacturer of the panel.
     *
     * @param manufacturer The new manufacturer of the panel
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Retrieves the name of the panel.
     *
     * @return String The name of the panel
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the panel.
     *
     * @param name The new name of the panel
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the rated power of the panel in watts.
     *
     * @return double The rated power in watts
     */
    public double getpRated() {
        return pRated;
    }

    /**
     * Updates the rated power of the panel in watts.
     *
     * @param pRated The new rated power in watts
     */
    public void setpRated(double pRated) {
        this.pRated = pRated;
    }

    /**
     * Retrieves the open-circuit voltage of the panel.
     *
     * @return double The open-circuit voltage
     */
    public double getVoc() {
        return voc;
    }

    /**
     * Updates the open-circuit voltage of the panel.
     *
     * @param voc The new open-circuit voltage
     */
    public void setVoc(double voc) {
        this.voc = voc;
    }

    /**
     * Retrieves the short-circuit current of the panel.
     *
     * @return double The short-circuit current
     */
    public double getIsc() {
        return isc;
    }

    /**
     * Updates the short-circuit current of the panel.
     *
     * @param isc The new short-circuit current
     */
    public void setIsc(double isc) {
        this.isc = isc;
    }

    /**
     * Retrieves the voltage at maximum power of the panel.
     *
     * @return double The voltage at maximum power
     */
    public double getVmp() {
        return vmp;
    }

    /**
     * Updates the voltage at maximum power of the panel.
     *
     * @param vmp The new voltage at maximum power
     */
    public void setVmp(double vmp) {
        this.vmp = vmp;
    }

    /**
     * Retrieves the current at maximum power of the panel.
     *
     * @return double The current at maximum power
     */
    public double getImp() {
        return imp;
    }

    /**
     * Updates the current at maximum power of the panel.
     *
     * @param imp The new current at maximum power
     */
    public void setImp(double imp) {
        this.imp = imp;
    }

    /**
     * Retrieves the temperature coefficient for maximum power.
     *
     * @return double The temperature coefficient for maximum power
     */
    public double getTempCoefficientPMax() {
        return tempCoefficientPMax;
    }

    /**
     * Updates the temperature coefficient for maximum power.
     *
     * @param tempCoefficientPMax The new temperature coefficient for maximum power
     */
    public void setTempCoefficientPMax(double tempCoefficientPMax) {
        this.tempCoefficientPMax = tempCoefficientPMax;
    }

    /**
     * Retrieves the temperature coefficient for open-circuit voltage.
     *
     * @return double The temperature coefficient for open-circuit voltage
     */
    public double getTempCoefficientVoc() {
        return tempCoefficientVoc;
    }

    /**
     * Updates the temperature coefficient for open-circuit voltage.
     *
     * @param tempCoefficientVoc The new temperature coefficient for open-circuit voltage
     */
    public void setTempCoefficientVoc(double tempCoefficientVoc) {
        this.tempCoefficientVoc = tempCoefficientVoc;
    }

    /**
     * Retrieves the panel tolerance percentage.
     *
     * @return double The panel tolerance percentage
     */
    public double getTolerance() {
        return tolerance;
    }

    /**
     * Updates the panel tolerance percentage.
     *
     * @param tolerance The new panel tolerance percentage
     */
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * Retrieves the degradation of the panel in the first year.
     *
     * @return double The degradation in the first year
     */
    public double getDegradationFirstYear() {
        return degradationFirstYear;
    }

    /**
     * Updates the degradation of the panel in the first year.
     *
     * @param degradationFirstYear The new degradation in the first year
     */
    public void setDegradationFirstYear(double degradationFirstYear) {
        this.degradationFirstYear = degradationFirstYear;
    }

    /**
     * Retrieves the annual degradation of the panel after the first year.
     *
     * @return double The degradation per year after the first year
     */
    public double getDegradationYears() {
        return degradationYears;
    }

    /**
     * Updates the annual degradation of the panel after the first year.
     *
     * @param degradationYears The new degradation per year after the first year
     */
    public void setDegradationYears(double degradationYears) {
        this.degradationYears = degradationYears;
    }

    /**
     * Retrieves the price of the panel.
     *
     * @return double The price of the panel
     */
    public double getPrice() {
        return price;
    }

    /**
     * Updates the price of the panel.
     *
     * @param price The new price of the panel
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
