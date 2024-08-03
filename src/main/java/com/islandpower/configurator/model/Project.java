package com.islandpower.configurator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "projects")
public class Project {

    @Id
    private String id;
    private String name;
    private String userId;
    private Site site;
    private SolarComponents solarComponents;

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

    public SolarComponents getSolarComponents() {
        return solarComponents;
    }

    public void setSolarComponents(SolarComponents solarComponents) {
        this.solarComponents = solarComponents;
    }

    // Nested Classes
    public static class Site {
        private double latitude;
        private double longitude;
        private double minTemperature;
        private double maxTemperature;
        private int panelAngle;
        private int panelAspect;
        private boolean usedOptimalValues;
        private List<MonthlyIrradiance> monthlyIrradianceList;

        // Getters and Setters
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

        public double getMinTemperature() {
            return minTemperature;
        }

        public void setMinTemperature(double minTemperature) {
            this.minTemperature = minTemperature;
        }

        public double getMaxTemperature() {
            return maxTemperature;
        }

        public void setMaxTemperature(double maxTemperature) {
            this.maxTemperature = maxTemperature;
        }

        public int getPanelAngle() {
            return panelAngle;
        }

        public void setPanelAngle(int panelAngle) {
            this.panelAngle = panelAngle;
        }

        public int getPanelAspect() {
            return panelAspect;
        }

        public void setPanelAspect(int panelAspect) {
            this.panelAspect = panelAspect;
        }

        public boolean isUsedOptimalValues() {
            return usedOptimalValues;
        }

        public void setUsedOptimalValues(boolean usedOptimalValues) {
            this.usedOptimalValues = usedOptimalValues;
        }

        public List<MonthlyIrradiance> getMonthlyIrradianceList() {
            return monthlyIrradianceList;
        }

        public void setMonthlyIrradianceList(List<MonthlyIrradiance> monthlyIrradianceList) {
            this.monthlyIrradianceList = monthlyIrradianceList;
        }

        // Nested Class
        public static class MonthlyIrradiance {
            private int month;
            private double irradiance;

            // Getters and Setters
            public int getMonth() {
                return month;
            }

            public void setMonth(int month) {
                this.month = month;
            }

            public double getIrradiance() {
                return irradiance;
            }

            public void setIrradiance(double irradiance) {
                this.irradiance = irradiance;
            }
        }
    }

    public static class SolarComponents {
        private Map<String, Component> appliances;
        private Map<String, Component> solarPanels;
        private Map<String, Component> controllers;
        private Map<String, Component> batteries;
        private Map<String, Component> inverters;
        private Map<String, Component> accessories;

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

        // Nested Class
        public static class Component {
            private String id;
            private int quantity;

            // Getters and Setters
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
