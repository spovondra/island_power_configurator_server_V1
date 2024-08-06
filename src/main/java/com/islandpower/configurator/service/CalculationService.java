package com.islandpower.configurator.service;

import com.islandpower.configurator.model.CalculationParams;
import com.islandpower.configurator.model.CalculationResult;
import org.springframework.stereotype.Service;

/**
 * Service class for performing energy calculations - FOR TESTING PURPOSES!!!!!.
 * This class contains methods for calculating energy requirements and component recommendations based on provided parameters.
 *
 * @version 0.0
 */
@Service
public class CalculationService {

    /**
     * Calculates the energy requirements and recommendations based on the provided parameters.
     *
     * @param params The parameters required for the calculations, including power, usage hours, panel efficiency, and battery capacity.
     * @return CalculationResult The result of the calculations, including daily energy, weekly energy, recommended panel power, and the number of batteries.
     */
    public CalculationResult calculate(CalculationParams params) {
        // Calculate daily energy requirement
        double dailyEnergy = params.getPower() * params.getHoursPerDay();

        // Calculate weekly energy requirement
        double weeklyEnergy = dailyEnergy * params.getDaysPerWeek();

        // Calculate recommended power for solar panels
        double recommendedPanelPower = dailyEnergy / (params.getPanelEfficiency() / 100);

        // Calculate number of batteries required
        int numberOfBatteries = (int) Math.ceil((dailyEnergy * params.getAutonomyDays()) / params.getBatteryCapacity());

        // Create and populate the result object
        CalculationResult result = new CalculationResult();
        result.setDailyEnergy(dailyEnergy);
        result.setWeeklyEnergy(weeklyEnergy);
        result.setRecommendedPanelPower(recommendedPanelPower);
        result.setNumberOfBatteries(numberOfBatteries);

        // Return the calculation result
        return result;
    }
}
