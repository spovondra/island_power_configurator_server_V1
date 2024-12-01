package com.islandpower.configurator.model.project;

import java.util.List;

/**
 * Model representing the solar panel configuration in a project.
 * This class contains attributes and methods to handle solar panel data, including efficiency losses,
 * installation type, total cost, and monthly data calculations.
 *
 * @version 1.0
 */
public class ProjectSolarPanel {

    private String solarPanelId; // ID of the selected solar panel
    private int numberOfPanels; // Maximum number of panels needed in any month
    private double totalPowerGenerated; // Total power generated by the panels (Watt-hours)
    private double totalCost; // Total cost of the selected panels
    private double efficiencyLoss; // Efficiency loss due to temperature or other factors
    private double estimatedDailyEnergyProduction; // Estimated daily energy production (Wh)
    private double panelOversizeCoefficient; // Oversize factor for the panels
    private double batteryEfficiency; // Efficiency of the battery system
    private double cableEfficiency; // Efficiency of the cabling
    private double manufacturerTolerance; // Manufacturer's tolerance value
    private double agingLoss; // Loss due to aging of the panels
    private double dirtLoss; // Loss due to dirt accumulation on the panels
    private String installationType; // Installation type (e.g., ground, roof_angle)
    private List<MonthlySolarData> monthlyData; // Monthly data for solar performance
    private String statusMessage; // Status message for the solar panel configuration

    /**
     * Default constructor for ProjectSolarPanel.
     */
    public ProjectSolarPanel() {}

    /**
     * Retrieves the ID of the selected solar panel.
     *
     * @return String The ID of the solar panel
     */
    public String getSolarPanelId() {
        return solarPanelId;
    }

    /**
     * Updates the ID of the selected solar panel.
     *
     * @param solarPanelId The new ID of the solar panel
     */
    public void setSolarPanelId(String solarPanelId) {
        this.solarPanelId = solarPanelId;
    }

    /**
     * Retrieves the maximum number of panels needed in any month.
     *
     * @return int The number of panels
     */
    public int getNumberOfPanels() {
        return numberOfPanels;
    }

    /**
     * Updates the maximum number of panels needed in any month.
     *
     * @param numberOfPanels The new number of panels
     */
    public void setNumberOfPanels(int numberOfPanels) {
        this.numberOfPanels = numberOfPanels;
    }

    /**
     * Retrieves the total power generated by the panels.
     *
     * @return double Total power in Watt-hours
     */
    public double getTotalPowerGenerated() {
        return totalPowerGenerated;
    }

    /**
     * Updates the total power generated by the panels.
     *
     * @param totalPowerGenerated The new total power in Watt-hours
     */
    public void setTotalPowerGenerated(double totalPowerGenerated) {
        this.totalPowerGenerated = totalPowerGenerated;
    }

    /**
     * Retrieves the total cost of the selected panels.
     *
     * @return double Total cost
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Updates the total cost of the selected panels.
     *
     * @param totalCost The new total cost
     */
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * Retrieves the efficiency loss due to factors like temperature.
     *
     * @return double Efficiency loss
     */
    public double getEfficiencyLoss() {
        return efficiencyLoss;
    }

    /**
     * Updates the efficiency loss.
     *
     * @param efficiencyLoss The new efficiency loss
     */
    public void setEfficiencyLoss(double efficiencyLoss) {
        this.efficiencyLoss = efficiencyLoss;
    }

    /**
     * Retrieves the estimated daily energy production.
     *
     * @return double Estimated daily energy production in Wh
     */
    public double getEstimatedDailyEnergyProduction() {
        return estimatedDailyEnergyProduction;
    }

    /**
     * Updates the estimated daily energy production.
     *
     * @param estimatedDailyEnergyProduction The new estimated daily energy production in Wh
     */
    public void setEstimatedDailyEnergyProduction(double estimatedDailyEnergyProduction) {
        this.estimatedDailyEnergyProduction = estimatedDailyEnergyProduction;
    }

    /**
     * Retrieves the oversize coefficient for the panels.
     *
     * @return double Panel oversize coefficient
     */
    public double getPanelOversizeCoefficient() {
        return panelOversizeCoefficient;
    }

    /**
     * Updates the oversize coefficient for the panels.
     *
     * @param panelOversizeCoefficient The new panel oversize coefficient
     */
    public void setPanelOversizeCoefficient(double panelOversizeCoefficient) {
        this.panelOversizeCoefficient = panelOversizeCoefficient;
    }

    /**
     * Retrieves the efficiency of the battery system.
     *
     * @return double Battery efficiency
     */
    public double getBatteryEfficiency() {
        return batteryEfficiency;
    }

    /**
     * Updates the efficiency of the battery system.
     *
     * @param batteryEfficiency The new battery efficiency
     */
    public void setBatteryEfficiency(double batteryEfficiency) {
        this.batteryEfficiency = batteryEfficiency;
    }

    /**
     * Retrieves the efficiency of the cabling.
     *
     * @return double Cable efficiency
     */
    public double getCableEfficiency() {
        return cableEfficiency;
    }

    /**
     * Updates the efficiency of the cabling.
     *
     * @param cableEfficiency The new cable efficiency
     */
    public void setCableEfficiency(double cableEfficiency) {
        this.cableEfficiency = cableEfficiency;
    }

    /**
     * Retrieves the manufacturer's tolerance value.
     *
     * @return double Manufacturer's tolerance
     */
    public double getManufacturerTolerance() {
        return manufacturerTolerance;
    }

    /**
     * Updates the manufacturer's tolerance value.
     *
     * @param manufacturerTolerance The new manufacturer's tolerance
     */
    public void setManufacturerTolerance(double manufacturerTolerance) {
        this.manufacturerTolerance = manufacturerTolerance;
    }

    /**
     * Retrieves the aging loss of the panels.
     *
     * @return double Aging loss
     */
    public double getAgingLoss() {
        return agingLoss;
    }

    /**
     * Updates the aging loss of the panels.
     *
     * @param agingLoss The new aging loss
     */
    public void setAgingLoss(double agingLoss) {
        this.agingLoss = agingLoss;
    }

    /**
     * Retrieves the dirt loss on the panels.
     *
     * @return double Dirt loss
     */
    public double getDirtLoss() {
        return dirtLoss;
    }

    /**
     * Updates the dirt loss on the panels.
     *
     * @param dirtLoss The new dirt loss
     */
    public void setDirtLoss(double dirtLoss) {
        this.dirtLoss = dirtLoss;
    }

    /**
     * Retrieves the installation type of the panels.
     *
     * @return String Installation type
     */
    public String getInstallationType() {
        return installationType;
    }

    /**
     * Updates the installation type of the panels.
     *
     * @param installationType The new installation type
     */
    public void setInstallationType(String installationType) {
        this.installationType = installationType;
    }

    /**
     * Retrieves the monthly solar panel data.
     *
     * @return List<@link ProjectSolarPanel.MonthlySolarData> List of monthly solar data
     */
    public List<MonthlySolarData> getMonthlyData() {
        return monthlyData;
    }

    /**
     * Updates the monthly solar panel data.
     *
     * @param monthlyData The new monthly data
     */
    public void setMonthlyData(List<MonthlySolarData> monthlyData) {
        this.monthlyData = monthlyData;
    }

    /**
     * Retrieves the status message for the solar panel configuration.
     *
     * @return String Status message
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Updates the status message for the solar panel configuration.
     *
     * @param statusMessage The new status message
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * Inner class to represent solar data for each month.
     */
    public static class MonthlySolarData {

        private int month; // Month number (1-12)
        private Double psh; // Peak Sun Hours (PSH)
        private Double ambientTemperature; // Ambient temperature in degrees Celsius
        private double totalDailyEnergy; // Total daily energy production in Wh
        private double requiredEnergy; // Required energy for the month in Wh
        private double requiredPower; // Required power for the month in W
        private double efficiency; // Efficiency factor
        private double deratedPower; // Derated power due to losses
        private int numPanels; // Number of panels needed
        private double estimatedDailySolarEnergy; // Estimated daily solar energy production in Wh

        /**
         * Constructor for MonthlySolarData.
         *
         * @param month The month number (1-12)
         * @param psh The Peak Sun Hours
         * @param ambientTemperature The ambient temperature in degrees Celsius
         * @param totalDailyEnergy The total daily energy in Wh
         * @param requiredEnergy The required energy in Wh
         * @param requiredPower The required power in W
         * @param efficiency The efficiency factor
         * @param deratedPower The derated power in W
         * @param numPanels The number of panels needed
         * @param estimatedDailySolarEnergy The estimated daily solar energy in Wh
         */
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
         * Retrieves the Peak Sun Hours (PSH).
         *
         * @return Double The Peak Sun Hours
         */
        public Double getPsh() {
            return psh;
        }

        /**
         * Updates the Peak Sun Hours (PSH).
         *
         * @param psh The new Peak Sun Hours
         */
        public void setPsh(Double psh) {
            this.psh = psh;
        }

        /**
         * Retrieves the ambient temperature.
         *
         * @return Double The ambient temperature in degrees Celsius
         */
        public Double getAmbientTemperature() {
            return ambientTemperature;
        }

        /**
         * Updates the ambient temperature.
         *
         * @param ambientTemperature The new ambient temperature in degrees Celsius
         */
        public void setAmbientTemperature(Double ambientTemperature) {
            this.ambientTemperature = ambientTemperature;
        }

        /**
         * Retrieves the total daily energy production.
         *
         * @return double The total daily energy in Wh
         */
        public double getTotalDailyEnergy() {
            return totalDailyEnergy;
        }

        /**
         * Updates the total daily energy production.
         *
         * @param totalDailyEnergy The new total daily energy in Wh
         */
        public void setTotalDailyEnergy(double totalDailyEnergy) {
            this.totalDailyEnergy = totalDailyEnergy;
        }

        /**
         * Retrieves the required energy for the month.
         *
         * @return double The required energy in Wh
         */
        public double getRequiredEnergy() {
            return requiredEnergy;
        }

        /**
         * Updates the required energy for the month.
         *
         * @param requiredEnergy The new required energy in Wh
         */
        public void setRequiredEnergy(double requiredEnergy) {
            this.requiredEnergy = requiredEnergy;
        }

        /**
         * Retrieves the required power for the month.
         *
         * @return double The required power in W
         */
        public double getRequiredPower() {
            return requiredPower;
        }

        /**
         * Updates the required power for the month.
         *
         * @param requiredPower The new required power in W
         */
        public void setRequiredPower(double requiredPower) {
            this.requiredPower = requiredPower;
        }

        /**
         * Retrieves the efficiency factor.
         *
         * @return double The efficiency factor
         */
        public double getEfficiency() {
            return efficiency;
        }

        /**
         * Updates the efficiency factor.
         *
         * @param efficiency The new efficiency factor
         */
        public void setEfficiency(double efficiency) {
            this.efficiency = efficiency;
        }

        /**
         * Retrieves the derated power due to losses.
         *
         * @return double The derated power in W
         */
        public double getDeratedPower() {
            return deratedPower;
        }

        /**
         * Updates the derated power due to losses.
         *
         * @param deratedPower The new derated power in W
         */
        public void setDeratedPower(double deratedPower) {
            this.deratedPower = deratedPower;
        }

        /**
         * Retrieves the number of panels needed for the month.
         *
         * @return int The number of panels
         */
        public int getNumPanels() {
            return numPanels;
        }

        /**
         * Updates the number of panels needed for the month.
         *
         * @param numPanels The new number of panels
         */
        public void setNumPanels(int numPanels) {
            this.numPanels = numPanels;
        }

        /**
         * Retrieves the estimated daily solar energy production.
         *
         * @return double The estimated daily solar energy in Wh
         */
        public double getEstimatedDailySolarEnergy() {
            return estimatedDailySolarEnergy;
        }

        /**
         * Updates the estimated daily solar energy production.
         *
         * @param estimatedDailySolarEnergy The new estimated daily solar energy in Wh
         */
        public void setEstimatedDailySolarEnergy(double estimatedDailySolarEnergy) {
            this.estimatedDailySolarEnergy = estimatedDailySolarEnergy;
        }
    }
}
