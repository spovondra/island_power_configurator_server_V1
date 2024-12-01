package com.islandpower.configurator.model.project;

/**
 * Model representing the configuration of a project.
 * This class aggregates various project-related components, such as appliances,
 * system voltage, and selected hardware (inverter, battery, solar panel, and controller).
 *
 * @version 1.0
 */
public class ConfigurationModel {

    private ProjectAppliance projectAppliance; // aggregated appliance data for the project
    private double systemVoltage; // system voltage of the project
    private double recommendedSystemVoltage; // recommended system voltage based on configuration
    private ProjectInverter projectInverter; // selected inverter configuration
    private ProjectBattery projectBattery; // selected battery configuration
    private ProjectSolarPanel projectSolarPanel; // selected solar panel configuration
    private ProjectController projectController; // selected controller configuration

    /**
     * Retrieves the project appliance configuration.
     *
     * @return ProjectAppliance The configuration of appliances in the project
     */
    public ProjectAppliance getProjectAppliance() {
        return projectAppliance;
    }

    /**
     * Updates the project appliance configuration.
     *
     * @param projectAppliance The new appliance configuration
     */
    public void setProjectAppliance(ProjectAppliance projectAppliance) {
        this.projectAppliance = projectAppliance;
    }

    /**
     * Retrieves the system voltage of the project.
     *
     * @return double The system voltage
     */
    public double getSystemVoltage() {
        return systemVoltage;
    }

    /**
     * Updates the system voltage of the project.
     *
     * @param systemVoltage The new system voltage
     */
    public void setSystemVoltage(double systemVoltage) {
        this.systemVoltage = systemVoltage;
    }

    /**
     * Retrieves the recommended system voltage for the project.
     *
     * @return double The recommended system voltage
     */
    public double getRecommendedSystemVoltage() {
        return recommendedSystemVoltage;
    }

    /**
     * Updates the recommended system voltage for the project.
     *
     * @param recommendedSystemVoltage The new recommended system voltage
     */
    public void setRecommendedSystemVoltage(double recommendedSystemVoltage) {
        this.recommendedSystemVoltage = recommendedSystemVoltage;
    }

    /**
     * Retrieves the inverter configuration for the project.
     *
     * @return ProjectInverter The inverter configuration
     */
    public ProjectInverter getProjectInverter() {
        return projectInverter;
    }

    /**
     * Updates the inverter configuration for the project.
     *
     * @param projectInverter The new inverter configuration
     */
    public void setProjectInverter(ProjectInverter projectInverter) {
        this.projectInverter = projectInverter;
    }

    /**
     * Retrieves the battery configuration for the project.
     *
     * @return ProjectBattery The battery configuration
     */
    public ProjectBattery getProjectBattery() {
        return projectBattery;
    }

    /**
     * Updates the battery configuration for the project.
     *
     * @param projectBattery The new battery configuration
     */
    public void setProjectBattery(ProjectBattery projectBattery) {
        this.projectBattery = projectBattery;
    }

    /**
     * Retrieves the solar panel configuration for the project.
     *
     * @return ProjectSolarPanel The solar panel configuration
     */
    public ProjectSolarPanel getProjectSolarPanel() {
        return projectSolarPanel;
    }

    /**
     * Updates the solar panel configuration for the project.
     *
     * @param projectSolarPanel The new solar panel configuration
     */
    public void setProjectSolarPanel(ProjectSolarPanel projectSolarPanel) {
        this.projectSolarPanel = projectSolarPanel;
    }

    /**
     * Retrieves the controller configuration for the project.
     *
     * @return ProjectController The controller configuration
     */
    public ProjectController getProjectController() {
        return projectController;
    }

    /**
     * Updates the controller configuration for the project.
     *
     * @param projectController The new controller configuration
     */
    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
    }
}
