package com.islandpower.configurator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "accessories")
public class Accessory {
    @Id
    private String id;
    private String model;
    private String manufacturer;
    private double power;
    private String image;
    private Map<String, String> parameters;

    public Accessory() {}

    public Accessory(String id, String model, String manufacturer, double power, String image, Map<String, String> parameters) {
        this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.power = power;
        this.image = image;
        this.parameters = parameters;
    }

    // Getters and Setters
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
