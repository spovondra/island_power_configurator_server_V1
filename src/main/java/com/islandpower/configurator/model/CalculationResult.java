package com.islandpower.configurator.model;

/**
 * Represents the result of energy system calculations - ONLY FOR TESTING PURPOSES.
 *
 */
public class CalculationResult {

    /**
     * The amount of energy required per day in watt-hours (Wh).
     */
    private double dailyEnergy;

    /**
     * The amount of energy required per week in watt-hours (Wh).
     */
    private double weeklyEnergy;

    /**
     * The recommended power of solar panels in watts.
     */
    private double recommendedPanelPower;

    /**
     * The number of batteries needed to meet the energy requirements.
     */
    private int numberOfBatteries;

    // Getters and Setters

    /**
     * Gets the daily energy requirement.
     *
     * @return The daily energy requirement in watt-hours.
     */
    public double getDailyEnergy() {
        return dailyEnergy;
    }

    /**
     * Sets the daily energy requirement.
     *
     * @param dailyEnergy The daily energy requirement in watt-hours.
     */
    public void setDailyEnergy(double dailyEnergy) {
        this.dailyEnergy = dailyEnergy;
    }

    /**
     * Gets the weekly energy requirement.
     *
     * @return The weekly energy requirement in watt-hours.
     */
    public double getWeeklyEnergy() {
        return weeklyEnergy;
    }

    /**
     * Sets the weekly energy requirement.
     *
     * @param weeklyEnergy The weekly energy requirement in watt-hours.
     */
    public void setWeeklyEnergy(double weeklyEnergy) {
        this.weeklyEnergy = weeklyEnergy;
    }

    /**
     * Gets the recommended power for solar panels.
     *
     * @return The recommended solar panel power in watts.
     */
    public double getRecommendedPanelPower() {
        return recommendedPanelPower;
    }

    /**
     * Sets the recommended power for solar panels.
     *
     * @param recommendedPanelPower The recommended solar panel power in watts.
     */
    public void setRecommendedPanelPower(double recommendedPanelPower) {
        this.recommendedPanelPower = recommendedPanelPower;
    }

    /**
     * Gets the number of batteries required.
     *
     * @return The number of batteries needed.
     */
    public int getNumberOfBatteries() {
        return numberOfBatteries;
    }

    /**
     * Sets the number of batteries required.
     *
     * @param numberOfBatteries The number of batteries needed.
     */
    public void setNumberOfBatteries(int numberOfBatteries) {
        this.numberOfBatteries = numberOfBatteries;
    }
}
