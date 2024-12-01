package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.SolarPanel;
import com.islandpower.configurator.service.SolarPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing solar panel components.
 * Provides endpoints for creating, retrieving, updating, and deleting solar panels.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/components/solar-panels")
public class SolarPanelController {

    private final SolarPanelService solarPanelService;

    /**
     * Constructs a SolarPanelController with the specified SolarPanelService.
     *
     * @param solarPanelService The service handling solar panel operations
     */
    @Autowired
    public SolarPanelController(SolarPanelService solarPanelService) {
        this.solarPanelService = solarPanelService;
    }

    /**
     * Creates a new solar panel.
     *
     * @param solarPanel The solar panel object to be created
     * @return {@link ResponseEntity} containing the created {@link SolarPanel} with HTTP CREATED status
     */
    @PostMapping
    public ResponseEntity<SolarPanel> createSolarPanel(@RequestBody SolarPanel solarPanel) {
        SolarPanel createdPanel = solarPanelService.addSolarPanel(solarPanel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPanel);
    }

    /**
     * Retrieves all solar panels.
     *
     * @return {@link ResponseEntity} containing a {@link List} of {@link SolarPanel} objects with HTTP OK status
     */
    @GetMapping
    public ResponseEntity<List<SolarPanel>> getAllSolarPanels() {
        List<SolarPanel> panels = solarPanelService.getAllSolarPanels();
        return ResponseEntity.ok(panels);
    }

    /**
     * Retrieves a solar panel by its ID.
     *
     * @param id The ID of the solar panel to retrieve
     * @return {@link ResponseEntity} containing the {@link SolarPanel} with HTTP OK status, or NOT_FOUND if not present
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolarPanel> getSolarPanelById(@PathVariable String id) {
        Optional<SolarPanel> panel = solarPanelService.getSolarPanelById(id);
        return panel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * Updates an existing solar panel.
     *
     * @param id The ID of the solar panel to update
     * @param solarPanel The updated solar panel object
     * @return {@link ResponseEntity} containing the updated {@link SolarPanel} with HTTP OK status, or NOT_FOUND if not present
     */
    @PutMapping("/{id}")
    public ResponseEntity<SolarPanel> updateSolarPanel(@PathVariable String id, @RequestBody SolarPanel solarPanel) {
        SolarPanel updatedPanel = solarPanelService.updateSolarPanel(id, solarPanel);
        return updatedPanel != null ? ResponseEntity.ok(updatedPanel) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * Deletes a solar panel by its ID.
     *
     * @param id The ID of the solar panel to delete
     * @return {@link ResponseEntity} with HTTP NO_CONTENT status if deletion is successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolarPanel(@PathVariable String id) {
        solarPanelService.deleteSolarPanel(id);
        return ResponseEntity.noContent().build();
    }
}
