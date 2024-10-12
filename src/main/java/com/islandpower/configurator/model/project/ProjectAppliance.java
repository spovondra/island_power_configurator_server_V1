package com.islandpower.configurator.model.project;

public class ProjectAppliance {
    private double totalAcEnergy; // Total AC energy consumption
    private double totalDcEnergy; // Total DC energy consumption
    private double totalAcPeakPower; // Total peak power for AC
    private double totalDcPeakPower; // Total peak power for DC

    // Getters and Setters for energy and peak power fields
    public double getTotalAcEnergy() {
        return totalAcEnergy;
    }

    public void setTotalAcEnergy(double totalAcEnergy) {
        this.totalAcEnergy = totalAcEnergy;
    }

    public double getTotalDcEnergy() {
        return totalDcEnergy;
    }

    public void setTotalDcEnergy(double totalDcEnergy) {
        this.totalDcEnergy = totalDcEnergy;
    }

    public double getTotalAcPeakPower() {
        return totalAcPeakPower;
    }

    public void setTotalAcPeakPower(double totalAcPeakPower) {
        this.totalAcPeakPower = totalAcPeakPower;
    }

    public double getTotalDcPeakPower() {
        return totalDcPeakPower;
    }

    public void setTotalDcPeakPower(double totalDcPeakPower) {
        this.totalDcPeakPower = totalDcPeakPower;
    }
}
