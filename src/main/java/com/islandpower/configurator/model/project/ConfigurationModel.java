package com.islandpower.configurator.model.project;

public class ConfigurationModel {
    private ProjectAppliance projectAppliance;
    private double systemVoltage;
    private double recommendedSystemVoltage;
    private ProjectInverter projectInverter;
    private ProjectBattery projectBattery;
    private ProjectSolarPanel projectSolarPanel;
    private ProjectController projectController; // Přidání ProjectController

    // Gettery a Settery
    public ProjectAppliance getProjectAppliance() {
        return projectAppliance;
    }

    public void setProjectAppliance(ProjectAppliance projectAppliance) {
        this.projectAppliance = projectAppliance;
    }

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

    public ProjectInverter getProjectInverter() {
        return projectInverter;
    }

    public void setProjectInverter(ProjectInverter projectInverter) {
        this.projectInverter = projectInverter;
    }

    public ProjectBattery getProjectBattery() {
        return projectBattery;
    }

    public void setProjectBattery(ProjectBattery projectBattery) {
        this.projectBattery = projectBattery;
    }

    public ProjectSolarPanel getProjectSolarPanel() {
        return projectSolarPanel;
    }

    public void setProjectSolarPanel(ProjectSolarPanel projectSolarPanel) {
        this.projectSolarPanel = projectSolarPanel;
    }

    public ProjectController getProjectController() {
        return projectController;
    }

    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
    }
}
