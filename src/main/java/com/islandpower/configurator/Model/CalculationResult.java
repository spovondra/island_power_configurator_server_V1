package com.islandpower.configurator.Model;

public class CalculationResult {
    private double dailyEnergy;
    private double weeklyEnergy;
    private double recommendedPanelPower;
    private int numberOfBatteries;

    // Getters and Setters
    public double getDailyEnergy() {
        return dailyEnergy;
    }

    public void setDailyEnergy(double dailyEnergy) {
        this.dailyEnergy = dailyEnergy;
    }

    public double getWeeklyEnergy() {
        return weeklyEnergy;
    }

    public void setWeeklyEnergy(double weeklyEnergy) {
        this.weeklyEnergy = weeklyEnergy;
    }

    public double getRecommendedPanelPower() {
        return recommendedPanelPower;
    }

    public void setRecommendedPanelPower(double recommendedPanelPower) {
        this.recommendedPanelPower = recommendedPanelPower;
    }

    public int getNumberOfBatteries() {
        return numberOfBatteries;
    }

    public void setNumberOfBatteries(int numberOfBatteries) {
        this.numberOfBatteries = numberOfBatteries;
    }
}
