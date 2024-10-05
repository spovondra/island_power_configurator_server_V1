package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.Battery;
import com.islandpower.configurator.service.BatteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/components/batteries") // Updated path
public class BatteryController {

    private final BatteryService batteryService;

    @Autowired
    public BatteryController(BatteryService batteryService) {
        this.batteryService = batteryService;
    }

    // Create a new battery
    @PostMapping
    public ResponseEntity<Battery> createBattery(@RequestBody Battery battery) {
        Battery createdBattery = batteryService.addBattery(battery);
        return new ResponseEntity<>(createdBattery, HttpStatus.CREATED);
    }

    // Get all batteries
    @GetMapping
    public ResponseEntity<List<Battery>> getAllBatteries() {
        List<Battery> batteries = batteryService.getAllBatteries();
        return new ResponseEntity<>(batteries, HttpStatus.OK);
    }

    // Get a battery by ID
    @GetMapping("/{id}")
    public ResponseEntity<Battery> getBatteryById(@PathVariable String id) {
        Optional<Battery> battery = batteryService.getBatteryById(id);
        return battery.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a battery
    @PutMapping("/{id}")
    public ResponseEntity<Battery> updateBattery(@PathVariable String id, @RequestBody Battery battery) {
        Battery updatedBattery = batteryService.updateBattery(id, battery);
        return updatedBattery != null ? ResponseEntity.ok(updatedBattery) : ResponseEntity.notFound().build();
    }

    // Delete a battery
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBattery(@PathVariable String id) {
        batteryService.deleteBattery(id);
        return ResponseEntity.noContent().build();
    }
}
