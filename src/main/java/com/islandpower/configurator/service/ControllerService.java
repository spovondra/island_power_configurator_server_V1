package com.islandpower.configurator.service;

import com.islandpower.configurator.model.Controller;
import com.islandpower.configurator.repository.ControllerRepository; // Ensure you have this repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ControllerService {

    private final ControllerRepository controllerRepository;

    @Autowired
    public ControllerService(ControllerRepository controllerRepository) {
        this.controllerRepository = controllerRepository;
    }

    // Create a new controller
    public Controller addController(Controller controller) {
        return controllerRepository.save(controller);
    }

    // Get all controllers
    public List<Controller> getAllControllers() {
        return controllerRepository.findAll();
    }

    // Get a controller by ID
    public Optional<Controller> getControllerById(String id) {
        return controllerRepository.findById(id);
    }

    // Update a controller
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
        return null; // or throw an exception
    }

    // Delete a controller
    public void deleteController(String id) {
        controllerRepository.deleteById(id);
    }
}
