package com.islandpower.configurator.service;

import com.islandpower.configurator.model.CalculationParams;
import com.islandpower.configurator.model.CalculationResult;
import org.springframework.stereotype.Service;

@Service
public class CalculationService {

    public CalculationResult calculate(CalculationParams params) {
        double dailyEnergy = params.getPower() * params.getHoursPerDay();
        double weeklyEnergy = dailyEnergy * params.getDaysPerWeek();
        double recommendedPanelPower = dailyEnergy / (params.getPanelEfficiency() / 100);
        int numberOfBatteries = (int) Math.ceil((dailyEnergy * params.getAutonomyDays()) / params.getBatteryCapacity());

        CalculationResult result = new CalculationResult();
        result.setDailyEnergy(dailyEnergy);
        result.setWeeklyEnergy(weeklyEnergy);
        result.setRecommendedPanelPower(recommendedPanelPower);
        result.setNumberOfBatteries(numberOfBatteries);

        return result;
    }
}
