package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.*;
import com.islandpower.configurator.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling component-related requests - Currently under development !!!! .
 * This class provides endpoints for retrieving, creating, updating, and deleting various types of components.
 *
 * @version 0.0
 */
@RestController
@RequestMapping("/api/components") // Base URL for all endpoints in this controller
public class ComponentController {

    /**
     * Repository for managing SolarPanel entities.
     */
    @Autowired
    private SolarPanelRepository solarPanelRepository;

    /**
     * Repository for managing Controller entities.
     */
    @Autowired
    private ControllerRepository controllerRepository;

    /**
     * Repository for managing Battery entities.
     */
    @Autowired
    private BatteryRepository batteryRepository;

    /**
     * Repository for managing Inverter entities.
     */
    @Autowired
    private InverterRepository inverterRepository;


    /**
     * Endpoint to retrieve all components of a specified type.
     *
     * @param type - The type of component to retrieve (e.g., "solar-panels", "controllers", etc.)
     * @return ResponseEntity<List<?>> - A response entity containing a list of components + HTTP status
     */
    @GetMapping("/{type}")
    public ResponseEntity<List<?>> getAllComponents(@PathVariable String type) {
        return switch (type) {
            case "solar-panels" -> new ResponseEntity<>(solarPanelRepository.findAll(), HttpStatus.OK);
            case "controllers" -> new ResponseEntity<>(controllerRepository.findAll(), HttpStatus.OK);
            case "batteries" -> new ResponseEntity<>(batteryRepository.findAll(), HttpStatus.OK);
            case "inverters" -> new ResponseEntity<>(inverterRepository.findAll(), HttpStatus.OK);
            default ->
                    new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return a BAD_REQUEST status for invalid component type
        };
    }

    /**
     * Endpoint to retrieve a specific component by ID.
     *
     * @param type - The type of component to retrieve
     * @param id - The ID of the component to retrieve
     * @return ResponseEntity<?> - A response entity containing the component + HTTP status
     */
    @GetMapping("/{type}/{id}")
    public ResponseEntity<?> getComponentById(@PathVariable String type, @PathVariable String id) {
        return switch (type) {
            case "solar-panels" -> getComponent(solarPanelRepository.findById(id));
            case "controllers" -> getComponent(controllerRepository.findById(id));
            case "batteries" -> getComponent(batteryRepository.findById(id));
            case "inverters" -> getComponent(inverterRepository.findById(id));
            default ->
                    new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return a BAD_REQUEST status for invalid component type
        };
    }

    /**
     * Endpoint to create a new component of a specified type.
     *
     * @param type - The type of component to create
     * @param componentData - The data for the new component
     * @return ResponseEntity<?> - A response entity containing the created component + HTTP status
     */
    @PostMapping("/{type}")
    public ResponseEntity<?> createComponent(@PathVariable String type, @RequestBody Object componentData) {
        return switch (type) {
            case "solar-panels" -> {
                SolarPanel solarPanel = (SolarPanel) componentData;
                yield new ResponseEntity<>(solarPanelRepository.save(solarPanel), HttpStatus.CREATED);
            }
            case "controllers" -> {
                Controller controller = (Controller) componentData;
                yield new ResponseEntity<>(controllerRepository.save(controller), HttpStatus.CREATED);
            }
            case "batteries" -> {
                Battery battery = (Battery) componentData;
                yield new ResponseEntity<>(batteryRepository.save(battery), HttpStatus.CREATED);
            }
            case "inverters" -> {
                Inverter inverter = (Inverter) componentData;
                yield new ResponseEntity<>(inverterRepository.save(inverter), HttpStatus.CREATED);
            }
            default ->
                    new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return a BAD_REQUEST status for invalid component type
        };
    }

    /**
     * Endpoint to update an existing component by ID.
     *
     * @param type - The type of component to update
     * @param id - The ID of the component to update
     * @param componentData - The updated data for the component
     * @return ResponseEntity<?> - A response entity containing the updated component + HTTP status
     */
    @PutMapping("/{type}/{id}")
    public ResponseEntity<?> updateComponent(@PathVariable String type, @PathVariable String id, @RequestBody Object componentData) {
        return switch (type) {
            case "solar-panels" -> updateComponent(id, (SolarPanel) componentData, solarPanelRepository);
            case "controllers" -> updateComponent(id, (Controller) componentData, controllerRepository);
            case "batteries" -> updateComponent(id, (Battery) componentData, batteryRepository);
            case "inverters" -> updateComponent(id, (Inverter) componentData, inverterRepository);
            default ->
                    new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return a BAD_REQUEST status for invalid component type
        };
    }

    /**
     * Endpoint to delete a component by ID.
     *
     * @param type - The type of component to delete
     * @param id - The ID of the component to delete
     * @return ResponseEntity<Void> - A response entity with NO CONTENT status
     */
    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<Void> deleteComponent(@PathVariable String type, @PathVariable String id) {
        return switch (type) {
            case "solar-panels" -> deleteComponent(id, solarPanelRepository);
            case "controllers" -> deleteComponent(id, controllerRepository);
            case "batteries" -> deleteComponent(id, batteryRepository);
            case "inverters" -> deleteComponent(id, inverterRepository);
            default ->
                    new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return a BAD_REQUEST status for invalid component type
        };
    }

    // Utility methods

    /**
     * Utility method to handle retrieval of a component.
     *
     * @param componentOptional - Optional containing the component data
     * @param <T> - Type of the component
     * @return ResponseEntity<?> - A response entity containing the component + HTTP status
     */
    private <T> ResponseEntity<?> getComponent(Optional<T> componentOptional) {
        return componentOptional
                .map(component -> new ResponseEntity<>(component, HttpStatus.OK)) // Return OK status if component is present
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Return NOT_FOUND status if component is not present
    }

    /**
     * Utility method to handle updating a component.
     *
     * @param id - The ID of the component to update
     * @param componentData - The new data for the component
     * @param repository - The repository for the component type
     * @param <T> - Type of the component
     * @return ResponseEntity<?> - A response entity containing the updated component + HTTP status
     */
    private <T> ResponseEntity<?> updateComponent(String id, T componentData, MongoRepository<T, String> repository) {
        return repository.findById(id)
                .map(existingComponent -> {
                    // Assuming components have an `update` method or properties are copied manually
                    ((UpdatableComponent) existingComponent).update(componentData);
                    return new ResponseEntity<>(repository.save(existingComponent), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Return NOT_FOUND status if component is not present
    }

    /**
     * Utility method to handle deletion of a component.
     *
     * @param id - The ID of the component to delete
     * @param repository - The repository for the component type
     * @param <T> - Type of the component
     * @return ResponseEntity<Void> - A response entity with NO CONTENT status
     */
    private <T> ResponseEntity<Void> deleteComponent(String id, MongoRepository<T, String> repository) {
        return repository.findById(id)
                .map(component -> {
                    repository.deleteById(id); // Delete the component by ID
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT); // Return NO_CONTENT status if deletion is successful
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Return NOT_FOUND status if component does not exist
    }

    /**
     * Interface for components update.
     */
    private interface UpdatableComponent {
        void update(Object data);
    }
}
