package com.islandpower.configurator.dto;

import com.islandpower.configurator.model.*;

/**
 * Data Transfer Object (DTO) for summarizing project configurations.
 * This DTO aggregates project-related data, including the project itself,
 * and its associated inverter, battery, solar panel, and controller.
 *
 * @version 1.0
 */
public class SummaryDto {

    private Project project; // the project details
    private Inverter inverter; // the associated inverter details
    private Battery battery; // the associated battery details
    private SolarPanel solarPanel; // the associated solar panel details
    private Controller controller; // the associated controller details

    /**
     * Constructs a new SummaryDto with the specified project and component details.
     *
     * @param project The project details
     * @param inverter The associated inverter details
     * @param battery The associated battery details
     * @param solarPanel The associated solar panel details
     * @param controller The associated controller details
     */
    public SummaryDto(Project project, Inverter inverter, Battery battery, SolarPanel solarPanel, Controller controller) {
        this.project = project;
        this.inverter = inverter;
        this.battery = battery;
        this.solarPanel = solarPanel;
        this.controller = controller;
    }

    /**
     * Retrieves the project details.
     *
     * @return Project the project details
     */
    public Project getProject() {
        return project;
    }

    /**
     * Updates the project details.
     *
     * @param project The updated project details
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Retrieves the inverter details.
     *
     * @return Inverter the inverter details
     */
    public Inverter getInverter() {
        return inverter;
    }

    /**
     * Updates the inverter details.
     *
     * @param inverter The updated inverter details
     */
    public void setInverter(Inverter inverter) {
        this.inverter = inverter;
    }

    /**
     * Retrieves the battery details.
     *
     * @return Battery the battery details
     */
    public Battery getBattery() {
        return battery;
    }

    /**
     * Updates the battery details.
     *
     * @param battery The updated battery details
     */
    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    /**
     * Retrieves the solar panel details.
     *
     * @return SolarPanel the solar panel details
     */
    public SolarPanel getSolarPanel() {
        return solarPanel;
    }

    /**
     * Updates the solar panel details.
     *
     * @param solarPanel The updated solar panel details
     */
    public void setSolarPanel(SolarPanel solarPanel) {
        this.solarPanel = solarPanel;
    }

    /**
     * Retrieves the controller details.
     *
     * @return Controller the controller details
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Updates the controller details.
     *
     * @param controller The updated controller details
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
