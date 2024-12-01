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
 * <p>
 * Provides endpoints for creating, retrieving, updating, and deleting solar panels.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/components/solar-panels")
public class SolarPanelController {

    private final SolarPanelService solarPanelService;

    @Autowired
    public SolarPanelController(SolarPanelService solarPanelService) {
        this.solarPanelService = solarPanelService;
    }

    /**
     * Creates a new solar panel.
     *
     * @param solarPanel - the solar panel object to be created
     * @return ResponseEntity<SolarPanel> - the created solar panel with HTTP CREATED status
     */
    @PostMapping
    public ResponseEntity<SolarPanel> createSolarPanel(@RequestBody SolarPanel solarPanel) {
        SolarPanel createdPanel = solarPanelService.addSolarPanel(solarPanel);
        return new ResponseEntity<>(createdPanel, HttpStatus.CREATED);
    }

    /**
     * Retrieves all solar panels.
     *
     * @return ResponseEntity<List<SolarPanel>> - a list of all solar panels with HTTP OK status
     */
    @GetMapping
    public ResponseEntity<List<SolarPanel>> getAllSolarPanels() {
        List<SolarPanel> panels = solarPanelService.getAllSolarPanels();
        return new ResponseEntity<>(panels, HttpStatus.OK);
    }

    /**
     * Retrieves a solar panel by its ID.
     *
     * @param id - the ID of the solar panel to retrieve
     * @return ResponseEntity<SolarPanel> - the solar panel object with HTTP OK status, or NOT_FOUND if not present
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolarPanel> getSolarPanelById(@PathVariable String id) {
        Optional<SolarPanel> panel = solarPanelService.getSolarPanelById(id);
        return panel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing solar panel.
     *
     * @param id - the ID of the solar panel to update
     * @param solarPanel - the updated solar panel object
     * @return ResponseEntity<SolarPanel> - the updated solar panel with HTTP OK status, or NOT_FOUND if not present
     */
    @PutMapping("/{id}")
    public ResponseEntity<SolarPanel> updateSolarPanel(@PathVariable String id, @RequestBody SolarPanel solarPanel) {
        SolarPanel updatedPanel = solarPanelService.updateSolarPanel(id, solarPanel);
        return updatedPanel != null ? ResponseEntity.ok(updatedPanel) : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a solar panel by its ID.
     *
     * @param id - the ID of the solar panel to delete
     * @return ResponseEntity<Void> - HTTP NO_CONTENT status if deletion is successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolarPanel(@PathVariable String id) {
        solarPanelService.deleteSolarPanel(id);
        return ResponseEntity.noContent().build();
    }
}
