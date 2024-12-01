package com.islandpower.configurator.model;

import com.islandpower.configurator.model.project.Appliance;
import com.islandpower.configurator.model.project.Site;
import com.islandpower.configurator.model.project.ConfigurationModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a project in the system.
 * <p>
 * This class contains information about the project, including its name, associated user, site details,
 * appliances, configuration, and tracking of the last completed step in the setup process.
 * <p>
 *
 * @version 1.0
 */
@Document(collection = "projects")
public class Project {
    @Id
    private String id; // unique identifier for the project
    private String name; // name of the project
    private String userId; // identifier of the user associated with the project
    private Site site; // details of the project's geographical and environmental site
    private List<Appliance> appliances; // list of appliances included in the project
    private ConfigurationModel configurationModel; // configuration details of the project
    private int lastCompletedStep; // tracks the last completed step in the project workflow

    /**
     * Retrieves the unique identifier of the project.
     *
     * @return String The unique ID of the project
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the unique identifier of the project.
     *
     * @param id The new unique ID of the project
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the project.
     *
     * @return String The name of the project
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the project.
     *
     * @param name The new name of the project
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the identifier of the user associated with the project.
     *
     * @return String The user ID associated with the project
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Updates the identifier of the user associated with the project.
     *
     * @param userId The new user ID associated with the project
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Retrieves the details of the project's geographical and environmental site.
     *
     * @return Site The site details of the project
     */
    public Site getSite() {
        return site;
    }

    /**
     * Updates the details of the project's geographical and environmental site.
     *
     * @param site The new site details of the project
     */
    public void setSite(Site site) {
        this.site = site;
    }

    /**
     * Retrieves the list of appliances included in the project.
     *
     * @return List<Appliance> The list of appliances in the project
     */
    public List<Appliance> getAppliances() {
        return appliances;
    }

    /**
     * Updates the list of appliances included in the project.
     *
     * @param appliances The new list of appliances for the project
     */
    public void setAppliances(List<Appliance> appliances) {
        this.appliances = appliances;
    }

    /**
     * Retrieves the configuration details of the project.
     *
     * @return ConfigurationModel The configuration model of the project
     */
    public ConfigurationModel getConfigurationModel() {
        return configurationModel;
    }

    /**
     * Updates the configuration details of the project.
     *
     * @param configurationModel The new configuration model of the project
     */
    public void setConfigurationModel(ConfigurationModel configurationModel) {
        this.configurationModel = configurationModel;
    }

    /**
     * Retrieves the last completed step in the project workflow.
     *
     * @return int The last completed step in the project
     */
    public int getLastCompletedStep() {
        return lastCompletedStep;
    }

    /**
     * Updates the last completed step in the project workflow.
     *
     * @param lastCompletedStep The new last completed step in the project
     */
    public void setLastCompletedStep(int lastCompletedStep) {
        this.lastCompletedStep = lastCompletedStep;
    }
}
