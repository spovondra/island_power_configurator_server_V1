package com.islandpower.configurator.model.project;

import java.util.Map;

/**
 * Represents the various solar components used in a solar energy project.
 * This class organizes components into categories, including solar panels, controllers,
 * batteries, inverters, and accessories.
 *
 * @version 1.0
 */
public class SolarComponents {

    /**
     * Map of solar panels used in the project.
     */
    private Map<String, Component> solarPanels;

    /**
     * Map of controllers used in the project.
     */
    private Map<String, Component> controllers;

    /**
     * Map of batteries used in the project.
     */
    private Map<String, Component> batteries;

    /**
     * Map of inverters used in the project.
     */
    private Map<String, Component> inverters;

    /**
     * Map of accessories used in the project.
     */
    private Map<String, Component> accessories;

    // Getters and Setters

    /**
     * Gets the map of solar panels.
     *
     * @return The map of solar panels.
     */
    public Map<String, Component> getSolarPanels() {
        return solarPanels;
    }

    /**
     * Sets the map of solar panels.
     *
     * @param solarPanels The map of solar panels.
     */
    public void setSolarPanels(Map<String, Component> solarPanels) {
        this.solarPanels = solarPanels;
    }

    /**
     * Gets the map of controllers.
     *
     * @return The map of controllers.
     */
    public Map<String, Component> getControllers() {
        return controllers;
    }

    /**
     * Sets the map of controllers.
     *
     * @param controllers The map of controllers.
     */
    public void setControllers(Map<String, Component> controllers) {
        this.controllers = controllers;
    }

    /**
     * Gets the map of batteries.
     *
     * @return The map of batteries.
     */
    public Map<String, Component> getBatteries() {
        return batteries;
    }

    /**
     * Sets the map of batteries.
     *
     * @param batteries The map of batteries.
     */
    public void setBatteries(Map<String, Component> batteries) {
        this.batteries = batteries;
    }

    /**
     * Gets the map of inverters.
     *
     * @return The map of inverters.
     */
    public Map<String, Component> getInverters() {
        return inverters;
    }

    /**
     * Sets the map of inverters.
     *
     * @param inverters The map of inverters.
     */
    public void setInverters(Map<String, Component> inverters) {
        this.inverters = inverters;
    }

    /**
     * Gets the map of accessories.
     *
     * @return The map of accessories.
     */
    public Map<String, Component> getAccessories() {
        return accessories;
    }

    /**
     * Sets the map of accessories.
     *
     * @param accessories The map of accessories.
     */
    public void setAccessories(Map<String, Component> accessories) {
        this.accessories = accessories;
    }

    /**
     * Represents a component within the solar system.
     * This class holds the unique identifier and quantity of a specific component.
     */
    public static class Component {

        /**
         * Unique identifier for the component.
         * This ID is used to uniquely identify the component within the system.
         */
        private String id;

        /**
         * Quantity of the component.
         * Specifies how many units of the component are used in the system.
         */
        private int quantity;

        // Getters and Setters

        /**
         * Gets the unique identifier for the component.
         *
         * @return The unique ID of the component.
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the unique identifier for the component.
         *
         * @param id The unique ID of the component.
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Gets the quantity of the component.
         *
         * @return The quantity of the component.
         */
        public int getQuantity() {
            return quantity;
        }

        /**
         * Sets the quantity of the component.
         *
         * @param quantity The quantity of the component.
         */
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
