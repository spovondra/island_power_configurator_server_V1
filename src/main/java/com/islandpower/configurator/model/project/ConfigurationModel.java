package com.islandpower.configurator.model.project;

public class ConfigurationModel {
    private ProjectAppliance projectAppliance; // Appliance energy and power fields
    private double systemVoltage; // Current system voltage
    private double recommendedSystemVoltage; // Recommended system voltage
    private ProjectInverter projectInverter; // Inverter configuration object
    private ProjectBattery projectBattery; // Battery configuration object
    private ProjectSolarPanel projectSolarPanel; // Solar panel configuration object

    // Getters and Setters for ProjectAppliance
    public ProjectAppliance getProjectAppliance() {
        return projectAppliance;
    }

    public void setProjectAppliance(ProjectAppliance projectAppliance) {
        this.projectAppliance = projectAppliance;
    }

    // Getters and Setters for systemVoltage and recommendedSystemVoltage
    public double getSystemVoltage() {
        return systemVoltage;
    }

    public void setSystemVoltage(double systemVoltage) {
        this.systemVoltage = systemVoltage;
    }

    public double getRecommendedSystemVoltage() {
        return recommendedSystemVoltage;
    }

    public void setRecommendedSystemVoltage(double recommendedSystemVoltage) {
        this.recommendedSystemVoltage = recommendedSystemVoltage;
    }

    // Getters and Setters for ProjectInverter
    public ProjectInverter getProjectInverter() {
        return projectInverter;
    }

    public void setProjectInverter(ProjectInverter projectInverter) {
        this.projectInverter = projectInverter;
    }

    // Getters and Setters for ProjectBattery
    public ProjectBattery getProjectBattery() {
        return projectBattery;
    }

    public void setProjectBattery(ProjectBattery projectBattery) {
        this.projectBattery = projectBattery;
    }

    // Getters and Setters for ProjectSolarPanel
    public ProjectSolarPanel getProjectSolarPanel() {
        return projectSolarPanel;
    }

    public void setProjectSolarPanel(ProjectSolarPanel projectSolarPanel) {
        this.projectSolarPanel = projectSolarPanel;
    }
}
