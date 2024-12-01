package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.Battery;
import com.islandpower.configurator.service.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing battery components.
 * <p>
 * Provides endpoints for creating, retrieving, updating, and deleting battery components.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/components/batteries")
public class BatteryController {

    private final BatteryService batteryService;

    @Autowired
    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    /**
     * Creates a new battery.
     *
     * @param battery - the battery object to be created
     * @return ResponseEntity<Battery> - the created battery
     */
    @PostMapping
    public ResponseEntity<Battery> createBattery(@RequestBody Battery battery) {
        Battery createdBattery = batteryService.addBattery(battery);
        return new ResponseEntity<>(createdBattery, HttpStatus.CREATED);
    }

    /**
     * Retrieves all batteries.
     *
     * @return ResponseEntity<List<Battery>> - a list of all batteries
     */
    @GetMapping
    public ResponseEntity<List<Battery>> getAllBatteries() {
        List<Battery> batteries = batteryService.getAllBatteries();
        return new ResponseEntity<>(batteries, HttpStatus.OK);
    }

    /**
     * Retrieves a battery by its ID.
     *
     * @param id - the ID of the battery to retrieve
     * @return ResponseEntity<Battery> - the battery object, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Battery> getBatteryById(@PathVariable String id) {
        Optional<Battery> battery = batteryService.getBatteryById(id);
        return battery.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing battery.
     *
     * @param id - the ID of the battery to update
     * @param battery - the updated battery object
     * @return ResponseEntity<Battery> - the updated battery object, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Battery> updateBattery(@PathVariable String id, @RequestBody Battery battery) {
        Battery updatedBattery = batteryService.updateBattery(id, battery);
        return updatedBattery != null ? ResponseEntity.ok(updatedBattery) : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a battery by its ID.
     *
     * @param id - the ID of the battery to delete
     * @return ResponseEntity<Void> - no content response
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBattery(@PathVariable String id) {
        batteryService.deleteBattery(id);
        return ResponseEntity.noContent().build();
    }
}
