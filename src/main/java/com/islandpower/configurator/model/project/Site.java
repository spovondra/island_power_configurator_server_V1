package com.islandpower.configurator.model.project;

import java.util.List;

/**
 * Represents the geographical and environmental details of a site in a solar energy project.
 * This class includes information about the site's coordinates, temperature ranges,
 * panel orientation, and monthly solar irradiance and temperature data.
 *
 * @version 1.0
 */
public class Site {

    private double latitude; // latitude coordinate of the site
    private double longitude; // longitude coordinate of the site
    private double minTemperature; // minimum temperature at the site
    private double maxTemperature; // maximum temperature at the site
    private int panelAngle; // angle of the solar panels
    private int panelAspect; // aspect (orientation) of the solar panels
    private boolean usedOptimalValues; // indicates if optimal values are used for calculations

    private List<MonthlyData> monthlyDataList; // list of monthly solar data, including irradiance and ambient temperature

    /**
     * Retrieves the latitude of the site.
     *
     * @return double The latitude of the site
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Updates the latitude of the site.
     *
     * @param latitude The new latitude of the site
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Retrieves the longitude of the site.
     *
     * @return double The longitude of the site
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Updates the longitude of the site.
     *
     * @param longitude The new longitude of the site
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Retrieves the minimum temperature at the site.
     *
     * @return double The minimum temperature
     */
    public double getMinTemperature() {
        return minTemperature;
    }

    /**
     * Updates the minimum temperature at the site.
     *
     * @param minTemperature The new minimum temperature
     */
    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    /**
     * Retrieves the maximum temperature at the site.
     *
     * @return double The maximum temperature
     */
    public double getMaxTemperature() {
        return maxTemperature;
    }

    /**
     * Updates the maximum temperature at the site.
     *
     * @param maxTemperature The new maximum temperature
     */
    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    /**
     * Retrieves the angle of the solar panels.
     *
     * @return int The panel angle
     */
    public int getPanelAngle() {
        return panelAngle;
    }

    /**
     * Updates the angle of the solar panels.
     *
     * @param panelAngle The new panel angle
     */
    public void setPanelAngle(int panelAngle) {
        this.panelAngle = panelAngle;
    }

    /**
     * Retrieves the aspect (orientation) of the solar panels.
     *
     * @return int The panel aspect
     */
    public int getPanelAspect() {
        return panelAspect;
    }

    /**
     * Updates the aspect (orientation) of the solar panels.
     *
     * @param panelAspect The new panel aspect
     */
    public void setPanelAspect(int panelAspect) {
        this.panelAspect = panelAspect;
    }

    /**
     * Indicates whether optimal values are used for the site configuration.
     *
     * @return boolean True if optimal values are used; false otherwise
     */
    public boolean isUsedOptimalValues() {
        return usedOptimalValues;
    }

    /**
     * Sets whether optimal values are used for the site configuration.
     *
     * @param usedOptimalValues True to use optimal values; false otherwise
     */
    public void setUsedOptimalValues(boolean usedOptimalValues) {
        this.usedOptimalValues = usedOptimalValues;
    }

    /**
     * Retrieves the list of monthly solar data for the site.
     *
     * @return {@link List} of {@link MonthlyData} The monthly solar data
     */
    public List<MonthlyData> getMonthlyDataList() {
        return monthlyDataList;
    }

    /**
     * Updates the list of monthly solar data for the site.
     *
     * @param monthlyDataList The new list of monthly data
     */
    public void setMonthlyDataList(List<MonthlyData> monthlyDataList) {
        this.monthlyDataList = monthlyDataList;
    }

    /**
     * Represents the solar irradiance and ambient temperature data for a specific month.
     */
    public static class MonthlyData {

        private int month; // month number (1-12)
        private double irradiance; // solar irradiance (Wh/m²/day)
        private double ambientTemperature; // ambient temperature (degrees Celsius)

        /**
         * Retrieves the month number.
         *
         * @return int The month number (1-12)
         */
        public int getMonth() {
            return month;
        }

        /**
         * Updates the month number.
         *
         * @param month The new month number (1-12)
         */
        public void setMonth(int month) {
            this.month = month;
        }

        /**
         * Retrieves the solar irradiance for the month.
         *
         * @return double The solar irradiance in Wh/m²/day
         */
        public double getIrradiance() {
            return irradiance;
        }

        /**
         * Updates the solar irradiance for the month.
         *
         * @param irradiance The new solar irradiance in Wh/m²/day
         */
        public void setIrradiance(double irradiance) {
            this.irradiance = irradiance;
        }

        /**
         * Retrieves the ambient temperature for the month.
         *
         * @return double The ambient temperature in degrees Celsius
         */
        public double getAmbientTemperature() {
            return ambientTemperature;
        }

        /**
         * Updates the ambient temperature for the month.
         *
         * @param ambientTemperature The new ambient temperature in degrees Celsius
         */
        public void setAmbientTemperature(double ambientTemperature) {
            this.ambientTemperature = ambientTemperature;
        }
    }
}
