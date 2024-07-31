package com.islandpower.configurator.Model;

public class CalculationParams {
    private double power;
    private double hoursPerDay;
    private double daysPerWeek;
    private double panelEfficiency;
    private double batteryCapacity;
    private double autonomyDays;

    // Getters and Setters
    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(double hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public double getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(double daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public double getPanelEfficiency() {
        return panelEfficiency;
    }

    public void setPanelEfficiency(double panelEfficiency) {
        this.panelEfficiency = panelEfficiency;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public double getAutonomyDays() {
        return autonomyDays;
    }

    public void setAutonomyDays(double autonomyDays) {
        this.autonomyDays = autonomyDays;
    }
}
