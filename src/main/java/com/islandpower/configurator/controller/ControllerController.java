package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.Controller;
import com.islandpower.configurator.service.ControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller for managing controller components.
 * Provides endpoints for creating, retrieving, updating, and deleting controller components.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/components/controllers")
public class ControllerController {

    private final ControllerService controllerService;

    /**
     * Constructs a ControllerController with the specified service.
     *
     * @param controllerService The service handling controller operations
     */
    @Autowired
    public ControllerController(ControllerService controllerService) {
        this.controllerService = controllerService;
    }

    /**
     * Creates a new controller.
     *
     * @param controller The controller object to be created
     * @return {@code ResponseEntity<Controller>} The created controller with HTTP CREATED status
     */
    @PostMapping
    public ResponseEntity<Controller> createController(@RequestBody Controller controller) {
        Controller createdController = controllerService.addController(controller);
        return new ResponseEntity<>(createdController, HttpStatus.CREATED);
    }

    /**
     * Retrieves all controllers.
     *
     * @return {@code ResponseEntity<List<Controller>>} A list of all controllers with HTTP OK status
     */
    @GetMapping
    public ResponseEntity<List<Controller>> getAllControllers() {
        List<Controller> controllers = controllerService.getAllControllers();
        return new ResponseEntity<>(controllers, HttpStatus.OK);
    }

    /**
     * Retrieves a controller by its ID.
     *
     * @param id The ID of the controller to retrieve
     * @return {@code ResponseEntity<Controller>} The controller object with HTTP OK status, or NOT_FOUND
     */
    @GetMapping("/{id}")
    public ResponseEntity<Controller> getControllerById(@PathVariable String id) {
        Optional<Controller> controller = controllerService.getControllerById(id);
        return controller.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates an existing controller.
     *
     * @param id The ID of the controller to update
     * @param controller The updated controller object
     * @return {@code ResponseEntity<Controller>} The updated controller with HTTP OK status, or NOT_FOUND
     */
    @PutMapping("/{id}")
    public ResponseEntity<Controller> updateController(@PathVariable String id, @RequestBody Controller controller) {
        Controller updatedController = controllerService.updateController(id, controller);
        return updatedController != null ? ResponseEntity.ok(updatedController) : ResponseEntity.notFound().build();
    }

    /**
     * Deletes a controller by its ID.
     *
     * @param id The ID of the controller to delete
     * @return {@code ResponseEntity<Void>} HTTP NO_CONTENT status if deletion is successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteController(@PathVariable String id) {
        controllerService.deleteController(id);
        return ResponseEntity.noContent().build();
    }
}
