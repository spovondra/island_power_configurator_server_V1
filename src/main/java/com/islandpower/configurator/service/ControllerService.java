package com.islandpower.configurator.service;

import com.islandpower.configurator.model.Controller;
import com.islandpower.configurator.repository.ControllerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing controllers in the system.
 * Provides CRUD operations for controller configurations.
 */
@Service
public class ControllerService {

    private final ControllerRepository controllerRepository;

    /**
     * Constructs a ControllerService instance.
     *
     * @param controllerRepository Repository for managing controllers
     */
    @Autowired
    public ControllerService(ControllerRepository controllerRepository) {
        this.controllerRepository = controllerRepository;
    }

    /**
     * Adds a new controller to the system.
     *
     * @param controller The controller to add
     * @return Controller The added controller
     */
    public Controller addController(Controller controller) {
        return controllerRepository.save(controller);
    }

    /**
     * Retrieves all controllers in the system.
     *
     * @return List<Controller> List of all controllers
     */
    public List<Controller> getAllControllers() {
        return controllerRepository.findAll();
    }

    /**
     * Retrieves a controller by its ID.
     *
     * @param id The ID of the controller
     * @return Optional<Controller> The controller wrapped in an Optional
     */
    public Optional<Controller> getControllerById(String id) {
        return controllerRepository.findById(id);
    }

    /**
     * Updates the details of an existing controller.
     *
     * @param id The ID of the controller to update
     * @param controllerDetails The updated controller details
     * @return Controller The updated controller, or null if not found
     */
    public Controller updateController(String id, Controller controllerDetails) {
        Optional<Controller> optionalController = controllerRepository.findById(id);
        if (optionalController.isPresent()) {
            Controller controller = optionalController.get();
            controller.setName(controllerDetails.getName());
            controller.setRatedPower(controllerDetails.getRatedPower());
            controller.setCurrentRating(controllerDetails.getCurrentRating());
            controller.setMaxVoltage(controllerDetails.getMaxVoltage());
            controller.setMinVoltage(controllerDetails.getMinVoltage());
            controller.setType(controllerDetails.getType());
            controller.setEfficiency(controllerDetails.getEfficiency());
            return controllerRepository.save(controller);
        }
        return null;
    }

    /**
     * Deletes a controller from the system by its ID.
     *
     * @param id The ID of the controller to delete
     */
    public void deleteController(String id) {
        controllerRepository.deleteById(id);
    }
}
