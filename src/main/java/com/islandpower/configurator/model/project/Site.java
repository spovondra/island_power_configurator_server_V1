package com.islandpower.configurator.model.project;

import java.util.List;

/**
 * Represents the geographical and environmental details of a site in a solar energy project.
 * This class contains information about the site's location, temperature ranges, panel orientation,
 * and monthly solar irradiance and temperature data.
 *
 * @version 1.0
 */
public class Site {

    private double latitude;
    private double longitude;
    private double minTemperature;
    private double maxTemperature;
    private int panelAngle;
    private int panelAspect;
    private boolean usedOptimalValues;

    /**
     * List of monthly data for the site, which contains both solar irradiance and ambient temperature.
     */
    private List<MonthlyData> monthlyDataList;

    // Getters and Setters for site data (latitude, longitude, etc.) are unchanged.

    public List<MonthlyData> getMonthlyDataList() {
        return monthlyDataList;
    }

    public void setMonthlyDataList(List<MonthlyData> monthlyDataList) {
        this.monthlyDataList = monthlyDataList;
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

    /**
     * Represents the solar irradiance and ambient temperature data for a specific month.
     */
    public static class MonthlyData {

        private int month;
        private double irradiance;  // Solar irradiance (Wh/m²/day)
        private double ambientTemperature;  // Ambient temperature (°C)

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

        public double getAmbientTemperature() {
            return ambientTemperature;
        }

        public void setAmbientTemperature(double ambientTemperature) {
            this.ambientTemperature = ambientTemperature;
        }
    }
}
