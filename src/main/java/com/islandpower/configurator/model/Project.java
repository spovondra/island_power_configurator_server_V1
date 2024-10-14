package com.islandpower.configurator.model;

import com.islandpower.configurator.model.project.Appliance;
import com.islandpower.configurator.model.project.Site;
import com.islandpower.configurator.model.project.ConfigurationModel; // Import your new class
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "projects")
public class Project {

    @Id
    private String id;
    private String name;
    private String userId;
    private Site site;
    private List<Appliance> appliances; // List of appliances
    private ConfigurationModel configurationModel; // New field for configuration details

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public List<Appliance> getAppliances() {
        return appliances;
    }

    public void setAppliances(List<Appliance> appliances) {
        this.appliances = appliances;
    }

    public ConfigurationModel getConfigurationModel() {
        return configurationModel;
    }

    public void setConfigurationModel(ConfigurationModel configurationModel) {
        this.configurationModel = configurationModel;
    }
}
