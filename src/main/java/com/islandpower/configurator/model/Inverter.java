package com.islandpower.configurator.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents an inverter used in the system, including its power ratings, efficiency, and pricing.
 * This model defines the inverter's attributes, including continuous power ratings for different temperatures,
 * maximum power, voltage, and efficiency.
 *
 * @version 1.0
 */
@Document(collection = "inverters")
public class Inverter {

    private String id; // unique identifier for the inverter
    private String name; // name of the inverter
    private double continuousPower25C; // continuous power rating at 25°C (Watts)
    private double continuousPower40C; // continuous power rating at 40°C (Watts)
    private double continuousPower65C; // continuous power rating at 65°C (Watts)
    private double maxPower; // maximum power rating of the inverter (Watts)
    private double efficiency; // efficiency of the inverter as a percentage (e.g., 95 for 95%)
    private int voltage; // operating voltage of the inverter (Volts)
    private double price; // price of the inverter in the local currency

    /**
     * Constructs an Inverter object with the specified parameters.
     *
     * @param id The unique identifier for the inverter
     * @param name The name of the inverter
     * @param continuousPower25C The continuous power rating at 25 degrees Celsius in Watts
     * @param continuousPower40C The continuous power rating at 40 degrees Celsius in Watts
     * @param continuousPower65C The continuous power rating at 65 degrees Celsius in Watts
     * @param maxPower The maximum power rating of the inverter in Watts
     * @param efficiency The efficiency of the inverter as a percentage
     * @param voltage The operating voltage of the inverter in Volts
     * @param price The price of the inverter
     */
    public Inverter(String id, String name, double continuousPower25C, double continuousPower40C,
                    double continuousPower65C, double maxPower, double efficiency, int voltage, double price) {
        this.id = id;
        this.name = name;
        this.continuousPower25C = continuousPower25C;
        this.continuousPower40C = continuousPower40C;
        this.continuousPower65C = continuousPower65C;
        this.maxPower = maxPower;
        this.efficiency = efficiency;
        this.voltage = voltage;
        this.price = price;
    }

    /**
     * Retrieves the unique identifier of the inverter.
     *
     * @return String The unique ID of the inverter
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the unique identifier of the inverter.
     *
     * @param id The new unique ID of the inverter
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the inverter.
     *
     * @return String The name of the inverter
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the inverter.
     *
     * @param name The new name of the inverter
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the continuous power rating at 25 degrees Celsius in Watts.
     *
     * @return double The continuous power at 25 degrees Celsius
     */
    public double getContinuousPower25C() {
        return continuousPower25C;
    }

    /**
     * Updates the continuous power rating at 25 degrees Celsius in Watts.
     *
     * @param continuousPower25C The new continuous power rating at 25 degrees Celsius
     */
    public void setContinuousPower25C(double continuousPower25C) {
        this.continuousPower25C = continuousPower25C;
    }

    /**
     * Retrieves the continuous power rating at 40 degrees Celsius in Watts.
     *
     * @return double The continuous power at 40 degrees Celsius
     */
    public double getContinuousPower40C() {
        return continuousPower40C;
    }

    /**
     * Updates the continuous power rating at 40 degrees Celsius in Watts.
     *
     * @param continuousPower40C The new continuous power rating at 40 degrees Celsius
     */
    public void setContinuousPower40C(double continuousPower40C) {
        this.continuousPower40C = continuousPower40C;
    }

    /**
     * Retrieves the continuous power rating at 65 degrees Celsius in Watts.
     *
     * @return double The continuous power at 65 degrees Celsius
     */
    public double getContinuousPower65C() {
        return continuousPower65C;
    }

    /**
     * Updates the continuous power rating at 65 degrees Celsius in Watts.
     *
     * @param continuousPower65C The new continuous power rating at 65 degrees Celsius
     */
    public void setContinuousPower65C(double continuousPower65C) {
        this.continuousPower65C = continuousPower65C;
    }

    /**
     * Retrieves the maximum power rating of the inverter in Watts.
     *
     * @return double The maximum power rating
     */
    public double getMaxPower() {
        return maxPower;
    }

    /**
     * Updates the maximum power rating of the inverter in Watts.
     *
     * @param maxPower The new maximum power rating
     */
    public void setMaxPower(double maxPower) {
        this.maxPower = maxPower;
    }

    /**
     * Retrieves the efficiency of the inverter as a percentage.
     *
     * @return double The efficiency of the inverter
     */
    public double getEfficiency() {
        return efficiency;
    }

    /**
     * Updates the efficiency of the inverter as a percentage.
     *
     * @param efficiency The new efficiency of the inverter
     */
    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    /**
     * Retrieves the operating voltage of the inverter in Volts.
     *
     * @return int The operating voltage
     */
    public int getVoltage() {
        return voltage;
    }

    /**
     * Updates the operating voltage of the inverter in Volts.
     *
     * @param voltage The new operating voltage
     */
    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    /**
     * Retrieves the price of the inverter.
     *
     * @return double The price of the inverter
     */
    public double getPrice() {
        return price;
    }

    /**
     * Updates the price of the inverter.
     *
     * @param price The new price of the inverter
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
