package com.islandpower.configurator.service;

import com.islandpower.configurator.model.Battery;
import com.islandpower.configurator.repository.BatteryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing batteries in the system.
 * Provides CRUD operations for battery configurations.
 *
 * @version 1.0
 */
@Service
public class BatteryService {

    private final BatteryRepository batteryRepository;

    /**
     * Constructs a BatteryService instance.
     *
     * @param batteryRepository {@link BatteryRepository} for managing batteries
     */
    @Autowired
    public BatteryService(BatteryRepository batteryRepository) {
        this.batteryRepository = batteryRepository;
    }

    /**
     * Adds a new battery to the system.
     *
     * @param battery {@link Battery} The battery to add
     * @return {@link Battery} The added battery
     */
    public Battery addBattery(Battery battery) {
        return batteryRepository.save(battery);
    }

    /**
     * Retrieves all batteries in the system.
     *
     * @return {@code List<Battery>} A list of all batteries
     */
    public List<Battery> getAllBatteries() {
        return batteryRepository.findAll();
    }

    /**
     * Retrieves a battery by its ID.
     *
     * @param id The ID of the battery
     * @return {@code Optional<Battery>} The battery wrapped in an {@code Optional}
     */
    public Optional<Battery> getBatteryById(String id) {
        return batteryRepository.findById(id);
    }

    /**
     * Updates the details of an existing battery.
     *
     * @param id The ID of the battery to update
     * @param batteryDetails {@link Battery} The updated battery details
     * @return {@link Battery} The updated battery, or {@code null} if not found
     */
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
        return null;
    }

    /**
     * Deletes a battery from the system by its ID.
     *
     * @param id The ID of the battery to delete
     */
    public void deleteBattery(String id) {
        batteryRepository.deleteById(id);
    }
}
