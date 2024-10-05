package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.Inverter;
import com.islandpower.configurator.service.InverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/components/inverters") // Updated path
public class InverterController {

    private final InverterService inverterService;

    @Autowired
    public InverterController(InverterService inverterService) {
        this.inverterService = inverterService;
    }

    // Create a new inverter
    @PostMapping
    public ResponseEntity<Inverter> createInverter(@RequestBody Inverter inverter) {
        Inverter createdInverter = inverterService.addInverter(inverter);
        return new ResponseEntity<>(createdInverter, HttpStatus.CREATED);
    }

    // Get all inverters
    @GetMapping
    public ResponseEntity<List<Inverter>> getAllInverters() {
        List<Inverter> inverters = inverterService.getAllInverters();
        return new ResponseEntity<>(inverters, HttpStatus.OK);
    }

    // Get an inverter by ID
    @GetMapping("/{id}")
    public ResponseEntity<Inverter> getInverterById(@PathVariable String id) {
        Optional<Inverter> inverter = inverterService.getInverterById(id);
        return inverter.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update an inverter
    @PutMapping("/{id}")
    public ResponseEntity<Inverter> updateInverter(@PathVariable String id, @RequestBody Inverter inverter) {
        Inverter updatedInverter = inverterService.updateInverter(id, inverter);
        return updatedInverter != null ? ResponseEntity.ok(updatedInverter) : ResponseEntity.notFound().build();
    }

    // Delete an inverter
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInverter(@PathVariable String id) {
        inverterService.deleteInverter(id);
        return ResponseEntity.noContent().build();
    }
}
