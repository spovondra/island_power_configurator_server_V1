package com.islandpower.configurator.dto;

import com.islandpower.configurator.model.*;

public class SummaryDto {
    private Project project;
    private Inverter inverter;
    private Battery battery;
    private SolarPanel solarPanel;
    private Controller controller;

    // Constructors, getters, and setters
    public SummaryDto(Project project, Inverter inverter, Battery battery, SolarPanel solarPanel, Controller controller) {
        this.project = project;
        this.inverter = inverter;
        this.battery = battery;
        this.solarPanel = solarPanel;
        this.controller = controller;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Inverter getInverter() {
        return inverter;
    }

    public void setInverter(Inverter inverter) {
        this.inverter = inverter;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public SolarPanel getSolarPanel() {
        return solarPanel;
    }

    public void setSolarPanel(SolarPanel solarPanel) {
        this.solarPanel = solarPanel;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
