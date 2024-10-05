package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.Controller;
import com.islandpower.configurator.service.ControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/components/controllers") // Updated path
public class ControllerController {

    private final ControllerService controllerService;

    @Autowired
    public ControllerController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    // Create a new controller
    @PostMapping
    public ResponseEntity<Controller> createController(@RequestBody Controller controller) {
        Controller createdController = controllerService.addController(controller);
        return new ResponseEntity<>(createdController, HttpStatus.CREATED);
    }

    // Get all controllers
    @GetMapping
    public ResponseEntity<List<Controller>> getAllControllers() {
        List<Controller> controllers = controllerService.getAllControllers();
        return new ResponseEntity<>(controllers, HttpStatus.OK);
    }

    // Get a controller by ID
    @GetMapping("/{id}")
    public ResponseEntity<Controller> getControllerById(@PathVariable String id) {
        Optional<Controller> controller = controllerService.getControllerById(id);
        return controller.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a controller
    @PutMapping("/{id}")
    public ResponseEntity<Controller> updateController(@PathVariable String id, @RequestBody Controller controller) {
        Controller updatedController = controllerService.updateController(id, controller);
        return updatedController != null ? ResponseEntity.ok(updatedController) : ResponseEntity.notFound().build();
    }

    // Delete a controller
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteController(@PathVariable String id) {
        controllerService.deleteController(id);
        return ResponseEntity.noContent().build();
    }
}
