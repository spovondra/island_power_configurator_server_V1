package com.islandpower.configurator.model;

import com.islandpower.configurator.model.project.Appliance;
import com.islandpower.configurator.model.project.Site;
import com.islandpower.configurator.model.project.SolarComponents;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a solar energy project.
 * This class is used to store information about a specific project, including its name, user details,
 * associated appliances, site details, and solar components.
 * It is mapped to a MongoDB collection named "projects".
 *
 * @version 1.0
 */
@Document(collection = "projects")
public class Project {

    /**
     * Unique identifier for the project.
     */
    @Id
    private String id;

    /**
     * Name of the project.
     */
    private String name;

    /**
     * ID of the user who owns or is associated with the project.
     */
    private String userId;

    /**
     * List of appliances used in the project.
     * Each appliance has details such as power rating, quantity, and daily usage.
     */
    private List<Appliance> appliances;

    /**
     * Site-specific details including location, environmental conditions, and panel orientation.
     */
    private Site site;

    /**
     * Components related to the solar energy system, including solar panels, controllers, batteries, inverters, and accessories.
     */
    private SolarComponents solarComponents;

    // Getters and Setters

    /**
     * Gets the unique identifier for the project.
     *
     * @return The unique ID of the project.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the project.
     *
     * @param id The unique ID of the project.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the project.
     *
     * @return The name of the project.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project.
     *
     * @param name The name of the project.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the ID of the user associated with the project.
     *
     * @return The ID of the user.
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the ID of the user associated with the project.
     *
     * @param userId The ID of the user.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the list of appliances used in the project.
     *
     * @return The list of appliances.
     */
    public List<Appliance> getAppliances() {
        return appliances;
    }

    /**
     * Sets the list of appliances used in the project.
     *
     * @param appliances The list of appliances.
     */
    public void setAppliances(List<Appliance> appliances) {
        this.appliances = appliances;
    }

    /**
     * Gets the site-specific details for the project.
     *
     * @return The site details.
     */
    public Site getSite() {
        return site;
    }

    /**
     * Sets the site-specific details for the project.
     *
     * @param site The site details.
     */
    public void setSite(Site site) {
        this.site = site;
    }

    /**
     * Gets the solar components related to the project.
     *
     * @return The solar components.
     */
    public SolarComponents getSolarComponents() {
        return solarComponents;
    }

    /**
     * Sets the solar components related to the project.
     *
     * @param solarComponents The solar components.
     */
    public void setSolarComponents(SolarComponents solarComponents) {
        this.solarComponents = solarComponents;
    }
}
