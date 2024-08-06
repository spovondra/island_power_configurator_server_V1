package com.islandpower.configurator.model.project;

import java.util.List;

/**
 * Represents the geographical and environmental details of a site in a solar energy project.
 * This class contains information about the site's location, temperature ranges, panel orientation,
 * and monthly solar irradiance data.
 *
 * @version 1.0
 */
public class Site {

    /**
     * Latitude of the site.
     */
    private double latitude;

    /**
     * Longitude of the site.
     */
    private double longitude;

    /**
     * Minimum temperature recorded at the site.
     * Represents the lowest temperature observed, which can affect solar panel efficiency.
     */
    private double minTemperature;

    /**
     * Maximum temperature recorded at the site.
     * Represents the highest temperature observed, which can affect solar panel efficiency.
     */
    private double maxTemperature;

    /**
     * Angle of the solar panels relative to the horizontal plane.
     * This angle is used to optimize the solar panel's exposure to sunlight.
     */
    private int panelAngle;

    /**
     * Aspect of the solar panels relative to the north.
     * This represents the direction the panels face to maximize sunlight exposure.
     */
    private int panelAspect;

    /**
     * Flag indicating whether optimal values for site parameters are used.
     * This flag determines if the site configuration uses optimal settings.
     */
    private boolean usedOptimalValues;

    /**
     * List of monthly solar irradiance data for the site.
     * Contains the average solar irradiance values for each month of the year.
     */
    private List<MonthlyIrradiance> monthlyIrradianceList;

    // Getters and Setters

    /**
     * Gets the latitude of the site.
     *
     * @return The latitude of the site.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the site.
     *
     * @param latitude The latitude of the site.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the site.
     *
     * @return The longitude of the site.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the site.
     *
     * @param longitude The longitude of the site.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the minimum temperature recorded at the site.
     *
     * @return The minimum temperature.
     */
    public double getMinTemperature() {
        return minTemperature;
    }

    /**
     * Sets the minimum temperature recorded at the site.
     *
     * @param minTemperature The minimum temperature.
     */
    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    /**
     * Gets the maximum temperature recorded at the site.
     *
     * @return The maximum temperature.
     */
    public double getMaxTemperature() {
        return maxTemperature;
    }

    /**
     * Sets the maximum temperature recorded at the site.
     *
     * @param maxTemperature The maximum temperature.
     */
    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    /**
     * Gets the angle of the solar panels.
     *
     * @return The panel angle in degrees.
     */
    public int getPanelAngle() {
        return panelAngle;
    }

    /**
     * Sets the angle of the solar panels.
     *
     * @param panelAngle The panel angle in degrees.
     */
    public void setPanelAngle(int panelAngle) {
        this.panelAngle = panelAngle;
    }

    /**
     * Gets the aspect of the solar panels.
     *
     * @return The panel aspect in degrees relative to the north.
     */
    public int getPanelAspect() {
        return panelAspect;
    }

    /**
     * Sets the aspect of the solar panels.
     *
     * @param panelAspect The panel aspect in degrees relative to the north.
     */
    public void setPanelAspect(int panelAspect) {
        this.panelAspect = panelAspect;
    }

    /**
     * Checks if optimal values for site parameters are used.
     *
     * @return True if optimal values are used, otherwise false.
     */
    public boolean isUsedOptimalValues() {
        return usedOptimalValues;
    }

    /**
     * Sets the flag for using optimal values for site parameters.
     *
     * @param usedOptimalValues True if optimal values are used, otherwise false.
     */
    public void setUsedOptimalValues(boolean usedOptimalValues) {
        this.usedOptimalValues = usedOptimalValues;
    }

    /**
     * Gets the list of monthly solar irradiance data.
     *
     * @return The list of monthly irradiance data.
     */
    public List<MonthlyIrradiance> getMonthlyIrradianceList() {
        return monthlyIrradianceList;
    }

    /**
     * Sets the list of monthly solar irradiance data.
     *
     * @param monthlyIrradianceList The list of monthly irradiance data.
     */
    public void setMonthlyIrradianceList(List<MonthlyIrradiance> monthlyIrradianceList) {
        this.monthlyIrradianceList = monthlyIrradianceList;
    }

    /**
     * Represents the solar irradiance data for a specific month.
     * This nested class stores the irradiance values for a given month.
     */
    public static class MonthlyIrradiance {

        /**
         * Month of the year (1 for January, 2 for February, etc.).
         */
        private int month;

        /**
         * Average solar irradiance for the month in watt-hours per square meter per day (Wh/m²/day).
         */
        private double irradiance;

        // Getters and Setters

        /**
         * Gets the month for which the irradiance data is recorded.
         *
         * @return The month of the year.
         */
        public int getMonth() {
            return month;
        }

        /**
         * Sets the month for which the irradiance data is recorded.
         *
         * @param month The month of the year.
         */
        public void setMonth(int month) {
            this.month = month;
        }

        /**
         * Gets the solar irradiance value for the month.
         *
         * @return The solar irradiance in Wh/m²/day.
         */
        public double getIrradiance() {
            return irradiance;
        }

        /**
         * Sets the solar irradiance value for the month.
         *
         * @param irradiance The solar irradiance in Wh/m²/day.
         */
        public void setIrradiance(double irradiance) {
            this.irradiance = irradiance;
        }
    }
}
