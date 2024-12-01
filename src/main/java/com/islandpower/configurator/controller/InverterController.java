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
 * <p>
 * Provides endpoints for creating, retrieving, updating, and deleting inverter components.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/components/inverters")
public class InverterController {

    private final InverterService inverterService;

    @Autowired
    public InverterController(InverterService inverterService) {
        this.inverterService = inverterService;
    }

    /**
     * Creates a new inverter.
     *
     * @param inverter - the inverter object to be created
     * @return ResponseEntity<Inverter> - the created inverter with HTTP CREATED status
     */
    @PostMapping
    public ResponseEntity<Inverter> createInverter(@RequestBody Inverter inverter) {
        Inverter createdInverter = inverterService.addInverter(inverter);
        return new ResponseEntity<>(createdInverter, HttpStatus.CREATED);
    }

    /**
     * Retrieves all inverters.
     *
     * @return ResponseEntity<List<Inverter>> - a list of all inverters with HTTP OK status
     */
    @GetMapping
    public ResponseEntity<List<Inverter>> getAllInverters() {
        List<Inverter> inverters = inverterService.getAllInverters();
        return new ResponseEntity<>(inverters, HttpStatus.OK);
    }

    /**
     * Retrieves an inverter by its ID.
     *
     * @param id - the ID of the inverter to retrieve
     * @return ResponseEntity<Inverter> - the inverter object with HTTP OK status, or NOT_FOUND if not present
     */
    @GetMapping("/{id}")
    public ResponseEntity<Inverter> getInverterById(@PathVariable String id) {
        Optional<Inverter> inverter = inverterService.getInverterById(id);
        return inverter.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing inverter.
     *
     * @param id - the ID of the inverter to update
     * @param inverter - the updated inverter object
     * @return ResponseEntity<Inverter> - the updated inverter with HTTP OK status, or NOT_FOUND if not present
     */
    @PutMapping("/{id}")
    public ResponseEntity<Inverter> updateInverter(@PathVariable String id, @RequestBody Inverter inverter) {
        Inverter updatedInverter = inverterService.updateInverter(id, inverter);
        return updatedInverter != null ? ResponseEntity.ok(updatedInverter) : ResponseEntity.notFound().build();
    }

    /**
     * Deletes an inverter by its ID.
     *
     * @param id - the ID of the inverter to delete
     * @return ResponseEntity<Void> - HTTP NO_CONTENT status if deletion is successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInverter(@PathVariable String id) {
        inverterService.deleteInverter(id);
        return ResponseEntity.noContent().build();
    }
}
