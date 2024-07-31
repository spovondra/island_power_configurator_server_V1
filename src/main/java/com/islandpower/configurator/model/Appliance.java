package com.islandpower.configurator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "appliances")
public class Appliance {
    @Id
    private String id;
    private String name;
    private double power;
    private int quantity;
    private int dailyUsageHours;
    private String image; //del
    private String dimensions; //del
    private String weight; //del

    public Appliance() {}

    public Appliance(String id, String name, double power, int quantity, int dailyUsageHours, String image, String dimensions, String weight) {
        this.id = id;
        this.name = name;
        this.power = power;
        this.quantity = quantity;
        this.dailyUsageHours = dailyUsageHours;
        this.image = image;
        this.dimensions = dimensions;
        this.weight = weight;
    }

    // Getters and Setters
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

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDailyUsageHours() {
        return dailyUsageHours;
    }

    public void setDailyUsageHours(int dailyUsageHours) {
        this.dailyUsageHours = dailyUsageHours;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
