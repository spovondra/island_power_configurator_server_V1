package com.islandpower.configurator.model.project;

import java.util.List;

/**
 * Represents the geographical and environmental details of a site in a solar energy project.
 * <p>
 * This class includes information about the site's coordinates, temperature ranges,
 * panel orientation, and monthly solar irradiance and temperature data.
 * </p>
 *
 * @version 1.0
 */
public class Site {

    private double latitude; // Latitude coordinate of the site
    private double longitude; // Longitude coordinate of the site
    private double minTemperature; // Minimum temperature at the site
    private double maxTemperature; // Maximum temperature at the site
    private int panelAngle; // Angle of the solar panels
    private int panelAspect; // Aspect (orientation) of the solar panels
    private boolean usedOptimalValues; // Indicates if optimal values are used for calculations

    /**
     * List of monthly solar data, including irradiance and ambient temperature.
     */
    private List<MonthlyData> monthlyDataList;

    /**
     * Retrieves the latitude of the site.
     *
     * @return double - the latitude of the site
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Updates the latitude of the site.
     *
     * @param latitude - the new latitude of the site
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Retrieves the longitude of the site.
     *
     * @return double - the longitude of the site
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Updates the longitude of the site.
     *
     * @param longitude - the new longitude of the site
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Retrieves the minimum temperature at the site.
     *
     * @return double - the minimum temperature
     */
    public double getMinTemperature() {
        return minTemperature;
    }

    /**
     * Updates the minimum temperature at the site.
     *
     * @param minTemperature - the new minimum temperature
     */
    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    /**
     * Retrieves the maximum temperature at the site.
     *
     * @return double - the maximum temperature
     */
    public double getMaxTemperature() {
        return maxTemperature;
    }

    /**
     * Updates the maximum temperature at the site.
     *
     * @param maxTemperature - the new maximum temperature
     */
    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    /**
     * Retrieves the angle of the solar panels.
     *
     * @return int - the panel angle
     */
    public int getPanelAngle() {
        return panelAngle;
    }

    /**
     * Updates the angle of the solar panels.
     *
     * @param panelAngle - the new panel angle
     */
    public void setPanelAngle(int panelAngle) {
        this.panelAngle = panelAngle;
    }

    /**
     * Retrieves the aspect (orientation) of the solar panels.
     *
     * @return int - the panel aspect
     */
    public int getPanelAspect() {
        return panelAspect;
    }

    /**
     * Updates the aspect (orientation) of the solar panels.
     *
     * @param panelAspect - the new panel aspect
     */
    public void setPanelAspect(int panelAspect) {
        this.panelAspect = panelAspect;
    }

    /**
     * Indicates whether optimal values are used for the site configuration.
     *
     * @return boolean - true if optimal values are used; false otherwise
     */
    public boolean isUsedOptimalValues() {
        return usedOptimalValues;
    }

    /**
     * Sets whether optimal values are used for the site configuration.
     *
     * @param usedOptimalValues - true to use optimal values; false otherwise
     */
    public void setUsedOptimalValues(boolean usedOptimalValues) {
        this.usedOptimalValues = usedOptimalValues;
    }

    /**
     * Retrieves the list of monthly solar data for the site.
     *
     * @return List<MonthlyData> - the monthly solar data
     */
    public List<MonthlyData> getMonthlyDataList() {
        return monthlyDataList;
    }

    /**
     * Updates the list of monthly solar data for the site.
     *
     * @param monthlyDataList - the new list of monthly data
     */
    public void setMonthlyDataList(List<MonthlyData> monthlyDataList) {
        this.monthlyDataList = monthlyDataList;
    }

    /**
     * Represents the solar irradiance and ambient temperature data for a specific month.
     */
    public static class MonthlyData {

        private int month; // Month number (1-12)
        private double irradiance; // Solar irradiance (Wh/m²/day)
        private double ambientTemperature; // Ambient temperature (°C)

        /**
         * Retrieves the month number.
         *
         * @return int - the month number (1-12)
         */
        public int getMonth() {
            return month;
        }

        /**
         * Updates the month number.
         *
         * @param month - the new month number (1-12)
         */
        public void setMonth(int month) {
            this.month = month;
        }

        /**
         * Retrieves the solar irradiance for the month.
         *
         * @return double - the solar irradiance in Wh/m²/day
         */
        public double getIrradiance() {
            return irradiance;
        }

        /**
         * Updates the solar irradiance for the month.
         *
         * @param irradiance - the new solar irradiance in Wh/m²/day
         */
        public void setIrradiance(double irradiance) {
            this.irradiance = irradiance;
        }

        /**
         * Retrieves the ambient temperature for the month.
         *
         * @return double - the ambient temperature in °C
         */
        public double getAmbientTemperature() {
            return ambientTemperature;
        }

        /**
         * Updates the ambient temperature for the month.
         *
         * @param ambientTemperature - the new ambient temperature in °C
         */
        public void setAmbientTemperature(double ambientTemperature) {
            this.ambientTemperature = ambientTemperature;
        }
    }
}
