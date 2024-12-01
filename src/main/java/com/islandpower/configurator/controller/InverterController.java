package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.Inverter;
import com.islandpower.configurator.service.InverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing inverter components.
 * Provides endpoints for creating, retrieving, updating, and deleting inverter components.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/components/inverters")
public class InverterController {

    private final InverterService inverterService;

    /**
     * Constructs an InverterController with the specified service.
     *
     * @param inverterService The service handling inverter operations
     */
    @Autowired
    public InverterController(InverterService inverterService) {
        this.inverterService = inverterService;
    }

    /**
     * Creates a new inverter.
     *
     * @param inverter The inverter object to be created
     * @return {@code ResponseEntity<Inverter>} The created inverter with HTTP CREATED status
     */
    @PostMapping
    public ResponseEntity<Inverter> createInverter(@RequestBody Inverter inverter) {
        Inverter createdInverter = inverterService.addInverter(inverter);
        return new ResponseEntity<>(createdInverter, HttpStatus.CREATED);
    }

    /**
     * Retrieves all inverters.
     *
     * @return {@code ResponseEntity<List<Inverter>>} A list of all inverters with HTTP OK status
     */
    @GetMapping
    public ResponseEntity<List<Inverter>> getAllInverters() {
        List<Inverter> inverters = inverterService.getAllInverters();
        return new ResponseEntity<>(inverters, HttpStatus.OK);
    }

    /**
     * Retrieves an inverter by its ID.
     *
     * @param id The ID of the inverter to retrieve
     * @return {@code ResponseEntity<Inverter>} The inverter object with HTTP OK status, or NOT_FOUND if not present
     */
    @GetMapping("/{id}")
    public ResponseEntity<Inverter> getInverterById(@PathVariable String id) {
        Optional<Inverter> inverter = inverterService.getInverterById(id);
        return inverter.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing inverter.
     *
     * @param id The ID of the inverter to update
     * @param inverter The updated inverter object
     * @return {@code ResponseEntity<Inverter>} The updated inverter with HTTP OK status, or NOT_FOUND if not present
     */
    @PutMapping("/{id}")
    public ResponseEntity<Inverter> updateInverter(@PathVariable String id, @RequestBody Inverter inverter) {
        Inverter updatedInverter = inverterService.updateInverter(id, inverter);
        return updatedInverter != null ? ResponseEntity.ok(updatedInverter) : ResponseEntity.notFound().build();
    }

    /**
     * Deletes an inverter by its ID.
     *
     * @param id The ID of the inverter to delete
     * @return {@code ResponseEntity<Void>} HTTP NO_CONTENT status if deletion is successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInverter(@PathVariable String id) {
        inverterService.deleteInverter(id);
        return ResponseEntity.noContent().build();
    }
}
