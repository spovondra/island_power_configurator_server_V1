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

    @Autowired
    private AccessoryRepository accessoryRepository;

    @GetMapping("/{type}")
    public ResponseEntity<List<?>> getAllComponents(@PathVariable String type) {
        switch (type) {
            case "solar-panels":
                return new ResponseEntity<>(solarPanelRepository.findAll(), HttpStatus.OK);
            case "controllers":
                return new ResponseEntity<>(controllerRepository.findAll(), HttpStatus.OK);
            case "batteries":
                return new ResponseEntity<>(batteryRepository.findAll(), HttpStatus.OK);
            case "inverters":
                return new ResponseEntity<>(inverterRepository.findAll(), HttpStatus.OK);
            case "accessories":
                return new ResponseEntity<>(accessoryRepository.findAll(), HttpStatus.OK);
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<?> getComponentById(@PathVariable String type, @PathVariable String id) {
        switch (type) {
            case "solar-panels":
                return getComponent(solarPanelRepository.findById(id), SolarPanel.class);
            case "controllers":
                return getComponent(controllerRepository.findById(id), Controller.class);
            case "batteries":
                return getComponent(batteryRepository.findById(id), Battery.class);
            case "inverters":
                return getComponent(inverterRepository.findById(id), Inverter.class);
            case "accessories":
                return getComponent(accessoryRepository.findById(id), Accessory.class);
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{type}")
    public ResponseEntity<?> createComponent(@PathVariable String type, @RequestBody Object componentData) {
        switch (type) {
            case "solar-panels":
                SolarPanel solarPanel = (SolarPanel) componentData;
                return new ResponseEntity<>(solarPanelRepository.save(solarPanel), HttpStatus.CREATED);
            case "controllers":
                Controller controller = (Controller) componentData;
                return new ResponseEntity<>(controllerRepository.save(controller), HttpStatus.CREATED);
            case "batteries":
                Battery battery = (Battery) componentData;
                return new ResponseEntity<>(batteryRepository.save(battery), HttpStatus.CREATED);
            case "inverters":
                Inverter inverter = (Inverter) componentData;
                return new ResponseEntity<>(inverterRepository.save(inverter), HttpStatus.CREATED);
            case "accessories":
                Accessory accessory = (Accessory) componentData;
                return new ResponseEntity<>(accessoryRepository.save(accessory), HttpStatus.CREATED);
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{type}/{id}")
    public ResponseEntity<?> updateComponent(@PathVariable String type, @PathVariable String id, @RequestBody Object componentData) {
        switch (type) {
            case "solar-panels":
                return updateComponent(id, (SolarPanel) componentData, solarPanelRepository);
            case "controllers":
                return updateComponent(id, (Controller) componentData, controllerRepository);
            case "batteries":
                return updateComponent(id, (Battery) componentData, batteryRepository);
            case "inverters":
                return updateComponent(id, (Inverter) componentData, inverterRepository);
            case "accessories":
                return updateComponent(id, (Accessory) componentData, accessoryRepository);
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<Void> deleteComponent(@PathVariable String type, @PathVariable String id) {
        switch (type) {
            case "solar-panels":
                return deleteComponent(id, solarPanelRepository);
            case "controllers":
                return deleteComponent(id, controllerRepository);
            case "batteries":
                return deleteComponent(id, batteryRepository);
            case "inverters":
                return deleteComponent(id, inverterRepository);
            case "accessories":
                return deleteComponent(id, accessoryRepository);
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Utility methods
    private <T> ResponseEntity<?> getComponent(Optional<T> componentOptional, Class<T> componentClass) {
        return componentOptional
                .map(component -> new ResponseEntity<>(component, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private <T> ResponseEntity<?> updateComponent(String id, T componentData, MongoRepository<T, String> repository) {
        return repository.findById(id)
                .map(existingComponent -> {
                    // Assuming components have an `update` method or you manually copy properties
                    // In this case, we assume the incoming `componentData` has the updated properties
                    ((UpdatableComponent) existingComponent).update(componentData);
                    return new ResponseEntity<>(repository.save(existingComponent), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private <T> ResponseEntity<Void> deleteComponent(String id, MongoRepository<T, String> repository) {
        return repository.findById(id)
                .map(component -> {
                    repository.deleteById(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Interface for updatable components
    private interface UpdatableComponent {
        void update(Object data);
    }
}
