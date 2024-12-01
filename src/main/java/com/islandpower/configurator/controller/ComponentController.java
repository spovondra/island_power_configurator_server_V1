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
 * Controller for managing various components.
 * <p>
 * Provides endpoints for retrieving, creating, updating, and deleting different component types such as
 * solar panels, controllers, batteries, and inverters.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/components")
public class ComponentController {

    @Autowired
    private SolarPanelRepository solarPanelRepository;

    @Autowired
    private ControllerRepository controllerRepository;

    @Autowired
    private BatteryRepository batteryRepository;

    @Autowired
    private InverterRepository inverterRepository;

    /**
     * Retrieves all components of a specified type.
     *
     * @param type - the type of component (e.g., "solar-panels", "controllers", etc.)
     * @return ResponseEntity<List<?>> - a list of all components of the specified type
     */
    @GetMapping("/{type}")
    public ResponseEntity<List<?>> getAllComponents(@PathVariable String type) {
        return switch (type) {
            case "solar-panels" -> new ResponseEntity<>(solarPanelRepository.findAll(), HttpStatus.OK);
            case "controllers" -> new ResponseEntity<>(controllerRepository.findAll(), HttpStatus.OK);
            case "batteries" -> new ResponseEntity<>(batteryRepository.findAll(), HttpStatus.OK);
            case "inverters" -> new ResponseEntity<>(inverterRepository.findAll(), HttpStatus.OK);
            default -> new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        };
    }

    /**
     * Retrieves a specific component by its ID.
     *
     * @param type - the type of component
     * @param id - the ID of the component
     * @return ResponseEntity<?> - the component or a NOT_FOUND status if it does not exist
     */
    @GetMapping("/{type}/{id}")
    public ResponseEntity<?> getComponentById(@PathVariable String type, @PathVariable String id) {
        return switch (type) {
            case "solar-panels" -> getComponent(solarPanelRepository.findById(id));
            case "controllers" -> getComponent(controllerRepository.findById(id));
            case "batteries" -> getComponent(batteryRepository.findById(id));
            case "inverters" -> getComponent(inverterRepository.findById(id));
            default -> new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        };
    }

    /**
     * Creates a new component of a specified type.
     *
     * @param type - the type of component
     * @param componentData - the data for the new component
     * @return ResponseEntity<?> - the created component or a BAD_REQUEST status if the type is invalid
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
            default -> new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        };
    }

    /**
     * Updates an existing component by its ID.
     *
     * @param type - the type of component
     * @param id - the ID of the component
     * @param componentData - the new data for the component
     * @return ResponseEntity<?> - the updated component or a NOT_FOUND status if it does not exist
     */
    @PutMapping("/{type}/{id}")
    public ResponseEntity<?> updateComponent(@PathVariable String type, @PathVariable String id, @RequestBody Object componentData) {
        return switch (type) {
            case "solar-panels" -> updateComponent(id, (SolarPanel) componentData, solarPanelRepository);
            case "controllers" -> updateComponent(id, (Controller) componentData, controllerRepository);
            case "batteries" -> updateComponent(id, (Battery) componentData, batteryRepository);
            case "inverters" -> updateComponent(id, (Inverter) componentData, inverterRepository);
            default -> new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        };
    }

    /**
     * Deletes a component by its ID.
     *
     * @param type - the type of component
     * @param id - the ID of the component
     * @return ResponseEntity<Void> - a NO_CONTENT status if successful or NOT_FOUND if it does not exist
     */
    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<Void> deleteComponent(@PathVariable String type, @PathVariable String id) {
        return switch (type) {
            case "solar-panels" -> deleteComponent(id, solarPanelRepository);
            case "controllers" -> deleteComponent(id, controllerRepository);
            case "batteries" -> deleteComponent(id, batteryRepository);
            case "inverters" -> deleteComponent(id, inverterRepository);
            default -> new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        };
    }

    /**
     * Utility method to retrieve a component by its ID.
     *
     * @param componentOptional - optional containing the component if found
     * @param <T> - the type of the component
     * @return ResponseEntity<?> - the component with HTTP OK status, or NOT_FOUND if not present
     */
    private <T> ResponseEntity<?> getComponent(Optional<T> componentOptional) {
        return componentOptional
                .map(component -> new ResponseEntity<>(component, HttpStatus.OK)) // Return OK if component is found
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Return NOT_FOUND if component is absent
    }

    /**
     * Utility method to update an existing component.
     *
     * @param id - the ID of the component to update
     * @param componentData - the updated data for the component
     * @param repository - the repository managing the component type
     * @param <T> - the type of the component
     * @return ResponseEntity<?> - the updated component with HTTP OK status, or NOT_FOUND if not found
     */
    private <T> ResponseEntity<?> updateComponent(String id, T componentData, MongoRepository<T, String> repository) {
        return repository.findById(id)
                .map(existingComponent -> {
                    // Update existing component using the provided data
                    ((UpdatableComponent) existingComponent).update(componentData);
                    return new ResponseEntity<>(repository.save(existingComponent), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Return NOT_FOUND if component is not found
    }

    /**
     * Utility method to delete a component by its ID.
     *
     * @param id - the ID of the component to delete
     * @param repository - the repository managing the component type
     * @param <T> - the type of the component
     * @return ResponseEntity<Void> - HTTP NO_CONTENT status if deletion is successful, or NOT_FOUND if not found
     */
    private <T> ResponseEntity<Void> deleteComponent(String id, MongoRepository<T, String> repository) {
        return repository.findById(id)
                .map(component -> {
                    repository.deleteById(id); // Delete the component by ID
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT); // Return NO_CONTENT if deletion is successful
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Return NOT_FOUND if component is not found
    }

    /**
     * Interface for making components updatable.
     */
    private interface UpdatableComponent {
        /**
         * Updates the component's data with the provided object.
         *
         * @param data - the new data to update the component
         */
        void update(Object data);
    }
}