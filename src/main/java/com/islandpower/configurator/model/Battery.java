package com.islandpower.configurator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "batteries")
public class Battery {
    @Id
    private String id;
    private String model;
    private double capacity;
    private double voltage;
    private String chemistry;
    private String image;
    private String dischargeRate;
    private String weight;
    private String dimensions;

    public Battery(String id, String model, double capacity, double voltage, String chemistry, String image, String dischargeRate, String weight, String dimensions) {
        this.id = id;
        this.model = model;
        this.capacity = capacity;
        this.voltage = voltage;
        this.chemistry = chemistry;
        this.image = image;
        this.dischargeRate = dischargeRate;
        this.weight = weight;
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

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public String getChemistry() {
        return chemistry;
    }

    public void setChemistry(String chemistry) {
        this.chemistry = chemistry;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDischargeRate() {
        return dischargeRate;
    }

    public void setDischargeRate(String dischargeRate) {
        this.dischargeRate = dischargeRate;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }
}
