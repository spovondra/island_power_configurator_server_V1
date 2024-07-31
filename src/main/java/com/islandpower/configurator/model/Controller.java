package com.islandpower.configurator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "controllers")
public class Controller {
    @Id
    private String id;
    private String model;
    private String type;
    private double voltage;
    private double current;
    private String image;
    private double efficiency;
    private String dimensions;

    public Controller(String id, String model, String type, double voltage, double current, String image, double efficiency, String dimensions) {
        this.id = id;
        this.model = model;
        this.type = type;
        this.voltage = voltage;
        this.current = current;
        this.image = image;
        this.efficiency = efficiency;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
}
