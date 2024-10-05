package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.SolarPanel;
import com.islandpower.configurator.service.SolarPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/components/solar-panels") // Updated path
public class SolarPanelController {

    private final SolarPanelService solarPanelService;

    @Autowired
    public SolarPanelController(SolarPanelService solarPanelService) {
        this.solarPanelService = solarPanelService;
    }

    // Create a new solar panel
    @PostMapping
    public ResponseEntity<SolarPanel> createSolarPanel(@RequestBody SolarPanel solarPanel) {
        SolarPanel createdPanel = solarPanelService.addSolarPanel(solarPanel);
        return new ResponseEntity<>(createdPanel, HttpStatus.CREATED);
    }

    // Get all solar panels
    @GetMapping
    public ResponseEntity<List<SolarPanel>> getAllSolarPanels() {
        List<SolarPanel> panels = solarPanelService.getAllSolarPanels();
        return new ResponseEntity<>(panels, HttpStatus.OK);
    }

    // Get a solar panel by ID
    @GetMapping("/{id}")
    public ResponseEntity<SolarPanel> getSolarPanelById(@PathVariable String id) {
        Optional<SolarPanel> panel = solarPanelService.getSolarPanelById(id);
        return panel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a solar panel
    @PutMapping("/{id}")
    public ResponseEntity<SolarPanel> updateSolarPanel(@PathVariable String id, @RequestBody SolarPanel solarPanel) {
        SolarPanel updatedPanel = solarPanelService.updateSolarPanel(id, solarPanel);
        return updatedPanel != null ? ResponseEntity.ok(updatedPanel) : ResponseEntity.notFound().build();
    }

    // Delete a solar panel
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSolarPanel(@PathVariable String id) {
        solarPanelService.deleteSolarPanel(id);
        return ResponseEntity.noContent().build();
    }
}
