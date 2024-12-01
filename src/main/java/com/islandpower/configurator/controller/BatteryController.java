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
 * Provides endpoints for creating, retrieving, updating, and deleting battery components.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/components/batteries")
public class BatteryController {

    private final BatteryService batteryService;

    /**
     * Constructs a BatteryController with the required BatteryService dependency.
     *
     * @param batteryService The service handling battery-related operations
     */
    @Autowired
    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    /**
     * Creates a new battery.
     *
     * @param battery The {@link Battery} object to be created
     * @return {@code ResponseEntity<Battery>} The created battery with HTTP CREATED status
     */
    @PostMapping
    public ResponseEntity<Battery> createBattery(@RequestBody Battery battery) {
        Battery createdBattery = batteryService.addBattery(battery);
        return new ResponseEntity<>(createdBattery, HttpStatus.CREATED);
    }

    /**
     * Retrieves all batteries.
     *
     * @return {@code ResponseEntity<List<Battery>>} A list of all batteries with HTTP OK status
     */
    @GetMapping
    public ResponseEntity<List<Battery>> getAllBatteries() {
        List<Battery> batteries = batteryService.getAllBatteries();
        return new ResponseEntity<>(batteries, HttpStatus.OK);
    }

    /**
     * Retrieves a battery by its ID.
     *
     * @param id The ID of the battery to retrieve
     * @return {@code ResponseEntity<Battery>} The battery object with HTTP OK status, or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Battery> getBatteryById(@PathVariable String id) {
        Optional<Battery> battery = batteryService.getBatteryById(id);
        return battery.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing battery.
     *
     * @param id The ID of the battery to update
     * @param battery The updated {@link Battery} object
     * @return {@code ResponseEntity<Battery>} The updated battery with HTTP OK status, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Battery> updateBattery(@PathVariable String id, @RequestBody Battery battery) {
        Battery updatedBattery = batteryService.updateBattery(id, battery);
        return updatedBattery != null ? ResponseEntity.ok(updatedBattery) : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a battery by its ID.
     *
     * @param id The ID of the battery to delete
     * @return {@code ResponseEntity<Void>} A no-content response with HTTP NO_CONTENT status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBattery(@PathVariable String id) {
        batteryService.deleteBattery(id);
        return ResponseEntity.noContent().build();
    }
}
