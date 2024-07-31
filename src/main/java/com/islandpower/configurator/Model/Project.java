package com.islandpower.configurator.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "projects")
public class Project {
    @Id
    private String id;
    private String name;
    private Location location;
    private Temperature temperature;
    private SolarComponents solarComponents;

    public Project(String id, String name, Location location, Temperature temperature, SolarComponents solarComponents) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.temperature = temperature;
        this.solarComponents = solarComponents;
    }

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public SolarComponents getSolarComponents() {
        return solarComponents;
    }

    public void setSolarComponents(SolarComponents solarComponents) {
        this.solarComponents = solarComponents;
    }

    public static class Location {
        private double latitude;
        private double longitude;

        public Location() {}

        public Location(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public static class Temperature {
        private double min;
        private double max;

        public Temperature() {}

        public Temperature(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }
    }

    public static class SolarComponents {
        private Map<String, Component> appliances;
        private Map<String, Component> solarPanels;
        private Map<String, Component> controllers;
        private Map<String, Component> batteries;
        private Map<String, Component> inverters;
        private Map<String, Component> accessories;

        public SolarComponents() {}

        public SolarComponents(Map<String, Component> appliances, Map<String, Component> solarPanels, Map<String, Component> controllers, Map<String, Component> batteries, Map<String, Component> inverters, Map<String, Component> accessories) {
            this.appliances = appliances;
            this.solarPanels = solarPanels;
            this.controllers = controllers;
            this.batteries = batteries;
            this.inverters = inverters;
            this.accessories = accessories;
        }

        // Getters and Setters
        public Map<String, Component> getAppliances() {
            return appliances;
        }

        public void setAppliances(Map<String, Component> appliances) {
            this.appliances = appliances;
        }

        public Map<String, Component> getSolarPanels() {
            return solarPanels;
        }

        public void setSolarPanels(Map<String, Component> solarPanels) {
            this.solarPanels = solarPanels;
        }

        public Map<String, Component> getControllers() {
            return controllers;
        }

        public void setControllers(Map<String, Component> controllers) {
            this.controllers = controllers;
        }

        public Map<String, Component> getBatteries() {
            return batteries;
        }

        public void setBatteries(Map<String, Component> batteries) {
            this.batteries = batteries;
        }

        public Map<String, Component> getInverters() {
            return inverters;
        }

        public void setInverters(Map<String, Component> inverters) {
            this.inverters = inverters;
        }

        public Map<String, Component> getAccessories() {
            return accessories;
        }

        public void setAccessories(Map<String, Component> accessories) {
            this.accessories = accessories;
        }

        public static class Component {
            private String id;
            private int quantity;

            public Component() {}

            public Component(String id, int quantity) {
                this.id = id;
                this.quantity = quantity;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }
        }
    }
}
