package com.islandpower.configurator.service;

import com.islandpower.configurator.model.Battery;
import com.islandpower.configurator.repository.BatteryRepository; // Ensure you have this repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BatteryService {

    private final BatteryRepository batteryRepository;

    @Autowired
    public BatteryService(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    // Create a new battery
    public Battery addBattery(Battery battery) {
        return batteryRepository.save(battery);
    }

    // Get all batteries
    public List<Battery> getAllBatteries() {
        return batteryRepository.findAll();
    }

    // Get a battery by ID
    public Optional<Battery> getBatteryById(String id) {
        return batteryRepository.findById(id);
    }

    // Update a battery
    public Battery updateBattery(String id, Battery batteryDetails) {
        Optional<Battery> optionalBattery = batteryRepository.findById(id);
        if (optionalBattery.isPresent()) {
            Battery battery = optionalBattery.get();
            battery.setName(batteryDetails.getName());
            battery.setType(batteryDetails.getType());
            battery.setCapacity(batteryDetails.getCapacity());
            battery.setVoltage(batteryDetails.getVoltage());
            battery.setDod(batteryDetails.getDod());
            battery.setPrice(batteryDetails.getPrice());
            return batteryRepository.save(battery);
        }
        return null; // or throw an exception
    }

    // Delete a battery
    public void deleteBattery(String id) {
        batteryRepository.deleteById(id);
    }
}
