package com.islandpower.configurator.controller;

import com.islandpower.configurator.Model.Appliance;
import com.islandpower.configurator.repository.ApplianceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appliances")
public class ApplianceController {

    @Autowired
    private ApplianceRepository applianceRepository;

    @GetMapping
    public ResponseEntity<List<Appliance>> getAllAppliances() {
        return new ResponseEntity<>(applianceRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appliance> getApplianceById(@PathVariable String id) {
        return applianceRepository.findById(id)
                .map(appliance -> new ResponseEntity<>(appliance, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Appliance> createAppliance(@RequestBody Appliance appliance) {
        return new ResponseEntity<>(applianceRepository.save(appliance), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appliance> updateAppliance(@PathVariable String id, @RequestBody Appliance appliance) {
        return applianceRepository.findById(id)
                .map(existingAppliance -> {
                    appliance.setId(id);
                    return new ResponseEntity<>(applianceRepository.save(appliance), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppliance(@PathVariable String id) {
        return applianceRepository.findById(id)
                .map(appliance -> {
                    applianceRepository.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
