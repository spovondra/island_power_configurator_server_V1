package com.islandpower.configurator.model.project;

import java.util.List;

public class ProjectSolarPanel {
    private String solarPanelId; // Reference to the selected Solar Panel
    private int numberOfPanels; // Maximum number of panels needed in any month
    private double totalPowerGenerated; // Total power generated by the panels (Watt-hours)
    private double totalCost; // Total cost of the selected panels
    private double efficiencyLoss; // Efficiency loss due to temperature or other factors
    private double estimatedDailyEnergyProduction; // Estimated daily energy production (Wh)

    // Efficiency and factors for solar panel calculations
    private double panelOversizeCoefficient; // Oversize factor for the panels
    private double batteryEfficiency; // Efficiency of the battery system
    private double cableEfficiency; // Efficiency of the cabling
    private double manufacturerTolerance; // Manufacturer's tolerance value
    private double agingLoss; // Loss due to aging of the panels
    private double dirtLoss; // Loss due to dirt accumulation on the panels
    private String installationType; // Installation type (ground, roof_angle, parallel_greater_150mm, etc.)

    private List<MonthlySolarData> monthlyData; // Monthly data including PSH and ambientTemperature

    public ProjectSolarPanel() {
    }

    public ProjectSolarPanel(String solarPanelId, int numberOfPanels, double totalPowerGenerated, double totalCost,
                             double efficiencyLoss, double estimatedDailyEnergyProduction, double panelOversizeCoefficient,
                             double batteryEfficiency, double cableEfficiency, double manufacturerTolerance,
                             double agingLoss, double dirtLoss, String installationType, List<MonthlySolarData> monthlyData) {
        this.solarPanelId = solarPanelId;
        this.numberOfPanels = numberOfPanels;
        this.totalPowerGenerated = totalPowerGenerated;
        this.totalCost = totalCost;
        this.efficiencyLoss = efficiencyLoss;
        this.estimatedDailyEnergyProduction = estimatedDailyEnergyProduction;
        this.panelOversizeCoefficient = panelOversizeCoefficient;
        this.batteryEfficiency = batteryEfficiency;
        this.cableEfficiency = cableEfficiency;
        this.manufacturerTolerance = manufacturerTolerance;
        this.agingLoss = agingLoss;
        this.dirtLoss = dirtLoss;
        this.installationType = installationType;
        this.monthlyData = monthlyData;
    }

    // Getters and Setters
    public String getSolarPanelId() {
        return solarPanelId;
    }

    public void setSolarPanelId(String solarPanelId) {
        this.solarPanelId = solarPanelId;
    }

    public int getNumberOfPanels() {
        return numberOfPanels;
    }

    public void setNumberOfPanels(int numberOfPanels) {
        this.numberOfPanels = numberOfPanels;
    }

    public double getTotalPowerGenerated() {
        return totalPowerGenerated;
    }

    public void setTotalPowerGenerated(double totalPowerGenerated) {
        this.totalPowerGenerated = totalPowerGenerated;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getEfficiencyLoss() {
        return efficiencyLoss;
    }

    public void setEfficiencyLoss(double efficiencyLoss) {
        this.efficiencyLoss = efficiencyLoss;
    }

    public double getEstimatedDailyEnergyProduction() {
        return estimatedDailyEnergyProduction;
    }

    public void setEstimatedDailyEnergyProduction(double estimatedDailyEnergyProduction) {
        this.estimatedDailyEnergyProduction = estimatedDailyEnergyProduction;
    }

    public double getPanelOversizeCoefficient() {
        return panelOversizeCoefficient;
    }

    public void setPanelOversizeCoefficient(double panelOversizeCoefficient) {
        this.panelOversizeCoefficient = panelOversizeCoefficient;
    }

    public double getBatteryEfficiency() {
        return batteryEfficiency;
    }

    public void setBatteryEfficiency(double batteryEfficiency) {
        this.batteryEfficiency = batteryEfficiency;
    }

    public double getCableEfficiency() {
        return cableEfficiency;
    }

    public void setCableEfficiency(double cableEfficiency) {
        this.cableEfficiency = cableEfficiency;
    }

    public double getManufacturerTolerance() {
        return manufacturerTolerance;
    }

    public void setManufacturerTolerance(double manufacturerTolerance) {
        this.manufacturerTolerance = manufacturerTolerance;
    }

    public double getAgingLoss() {
        return agingLoss;
    }

    public void setAgingLoss(double agingLoss) {
        this.agingLoss = agingLoss;
    }

    public double getDirtLoss() {
        return dirtLoss;
    }

    public void setDirtLoss(double dirtLoss) {
        this.dirtLoss = dirtLoss;
    }

    public String getInstallationType() {
        return installationType;
    }

    public void setInstallationType(String installationType) {
        this.installationType = installationType;
    }

    public List<MonthlySolarData> getMonthlyData() {
        return monthlyData;
    }

    public void setMonthlyData(List<MonthlySolarData> monthlyData) {
        this.monthlyData = monthlyData;
    }

    // Inner class to store data for each month, including PSH and ambient temperature
    public static class MonthlySolarData {
        private int month;
        private Double psh;
        private Double ambientTemperature;
        private double totalDailyEnergy;
        private double requiredEnergy;
        private double requiredPower;
        private double efficiency;
        private double deratedPower;
        private int numPanels;
        private double estimatedDailySolarEnergy;

        public MonthlySolarData(int month, Double psh, Double ambientTemperature, double totalDailyEnergy,
                                double requiredEnergy, double requiredPower, double efficiency, double deratedPower,
                                int numPanels, double estimatedDailySolarEnergy) {
            this.month = month;
            this.psh = (psh != null) ? psh : 0.0;
            this.ambientTemperature = (ambientTemperature != null) ? ambientTemperature : 0.0;
            this.totalDailyEnergy = totalDailyEnergy;
            this.requiredEnergy = requiredEnergy;
            this.requiredPower = requiredPower;
            this.efficiency = efficiency;
            this.deratedPower = deratedPower;
            this.numPanels = numPanels;
            this.estimatedDailySolarEnergy = estimatedDailySolarEnergy;
        }

        // Getters and Setters
        public int getMonth() {
            return month;
        }

        public void setMonth(int month) {
            this.month = month;
        }

        public Double getPsh() {
            return psh;
        }

        public void setPsh(Double psh) {
            this.psh = psh;
        }

        public Double getAmbientTemperature() {
            return ambientTemperature;
        }

        public void setAmbientTemperature(Double ambientTemperature) {
            this.ambientTemperature = ambientTemperature;
        }

        public double getTotalDailyEnergy() {
            return totalDailyEnergy;
        }

        public void setTotalDailyEnergy(double totalDailyEnergy) {
            this.totalDailyEnergy = totalDailyEnergy;
        }

        public double getRequiredEnergy() {
            return requiredEnergy;
        }

        public void setRequiredEnergy(double requiredEnergy) {
            this.requiredEnergy = requiredEnergy;
        }

        public double getRequiredPower() {
            return requiredPower;
        }

        public void setRequiredPower(double requiredPower) {
            this.requiredPower = requiredPower;
        }

        public double getEfficiency() {
            return efficiency;
        }

        public void setEfficiency(double efficiency) {
            this.efficiency = efficiency;
        }

        public double getDeratedPower() {
            return deratedPower;
        }

        public void setDeratedPower(double deratedPower) {
            this.deratedPower = deratedPower;
        }

        public int getNumPanels() {
            return numPanels;
        }

        public void setNumPanels(int numPanels) {
            this.numPanels = numPanels;
        }

        public double getEstimatedDailySolarEnergy() {
            return estimatedDailySolarEnergy;
        }

        public void setEstimatedDailySolarEnergy(double estimatedDailySolarEnergy) {
            this.estimatedDailySolarEnergy = estimatedDailySolarEnergy;
        }
    }
}
