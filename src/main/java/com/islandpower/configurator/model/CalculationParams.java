package com.islandpower.configurator.model;

/**
 * Represents the parameters used for performing calculations related to energy systems - FOR TESTING PURPOSES.
 * This class includes attributes such as power, hours of operation, panel efficiency, and more.
 *
 */
public class CalculationParams {

    /**
     * The power requirement or output in watts.
     */
    private double power;

    /**
     * The number of hours per day the system is expected to operate.
     */
    private double hoursPerDay;

    /**
     * The number of days per week the system is expected to operate.
     */
    private double daysPerWeek;

    /**
     * The efficiency of the solar panels as a percentage (e.g., 0.18 for 18% efficiency).
     */
    private double panelEfficiency;

    /**
     * The capacity of the battery in ampere-hours (Ah).
     */
    private double batteryCapacity;

    /**
     * The number of autonomy days, representing how many days the system can operate without sunlight or charging.
     */
    private double autonomyDays;

    // Getters and Setters

    /**
     * Gets the power requirement or output.
     *
     * @return The power value in watts.
     */
    public double getPower() {
        return power;
    }

    /**
     * Sets the power requirement or output.
     *
     * @param power The power value in watts.
     */
    public void setPower(double power) {
        this.power = power;
    }

    /**
     * Gets the number of hours per day the system is expected to operate.
     *
     * @return The hours per day.
     */
    public double getHoursPerDay() {
        return hoursPerDay;
    }

    /**
     * Sets the number of hours per day the system is expected to operate.
     *
     * @param hoursPerDay The hours per day.
     */
    public void setHoursPerDay(double hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    /**
     * Gets the number of days per week the system is expected to operate.
     *
     * @return The days per week.
     */
    public double getDaysPerWeek() {
        return daysPerWeek;
    }

    /**
     * Sets the number of days per week the system is expected to operate.
     *
     * @param daysPerWeek The days per week.
     */
    public void setDaysPerWeek(double daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    /**
     * Gets the efficiency of the solar panels.
     *
     * @return The panel efficiency as a percentage.
     */
    public double getPanelEfficiency() {
        return panelEfficiency;
    }

    /**
     * Sets the efficiency of the solar panels.
     *
     * @param panelEfficiency The panel efficiency as a percentage.
     */
    public void setPanelEfficiency(double panelEfficiency) {
        this.panelEfficiency = panelEfficiency;
    }

    /**
     * Gets the battery capacity in ampere-hours.
     *
     * @return The battery capacity.
     */
    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    /**
     * Sets the battery capacity in ampere-hours.
     *
     * @param batteryCapacity The battery capacity.
     */
    public void setBatteryCapacity(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    /**
     * Gets the number of autonomy days for the system.
     *
     * @return The autonomy days.
     */
    public double getAutonomyDays() {
        return autonomyDays;
    }

    /**
     * Sets the number of autonomy days for the system.
     *
     * @param autonomyDays The autonomy days.
     */
    public void setAutonomyDays(double autonomyDays) {
        this.autonomyDays = autonomyDays;
    }
}
