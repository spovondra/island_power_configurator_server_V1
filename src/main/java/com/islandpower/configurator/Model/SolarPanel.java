package com.islandpower.configurator.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "solarPanels")
public class SolarPanel {
    @Id
    private String id;
    private String model;
    private double power;
    private double efficiency;
    private String manufacturer;
    private String image;
    private double voltage;
    private double current;
    private String dimensions;

    public SolarPanel(String id, String model, double power, double efficiency, String manufacturer, String image, double voltage, double current, String dimensions) {
        this.id = id;
        this.model = model;
        this.power = power;
        this.efficiency = efficiency;
        this.manufacturer = manufacturer;
        this.image = image;
        this.voltage = voltage;
        this.current = current;
        this.dimensions = dimensions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
}
