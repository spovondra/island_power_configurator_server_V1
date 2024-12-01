package com.islandpower.configurator.service;

import com.islandpower.configurator.model.Inverter;
import com.islandpower.configurator.repository.InverterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing inverters in the system.
 * Provides CRUD operations for inverter configurations.
 */
@Service
public class InverterService {

    private final InverterRepository inverterRepository;

    /**
     * Constructs an InverterService instance.
     *
     * @param inverterRepository Repository for managing inverters
     */
    @Autowired
    public InverterService(InverterRepository inverterRepository) {
        this.inverterRepository = inverterRepository;
    }

    /**
     * Adds a new inverter to the system.
     *
     * @param inverter The inverter to add
     * @return Inverter The added inverter
     */
    public Inverter addInverter(Inverter inverter) {
        return inverterRepository.save(inverter);
    }

    /**
     * Retrieves all inverters in the system.
     *
     * @return List<Inverter> List of all inverters
     */
    public List<Inverter> getAllInverters() {
        return inverterRepository.findAll();
    }

    /**
     * Retrieves an inverter by its ID.
     *
     * @param id The ID of the inverter
     * @return Optional<Inverter> The inverter wrapped in an Optional
     */
    public Optional<Inverter> getInverterById(String id) {
        return inverterRepository.findById(id);
    }

    /**
     * Updates the details of an existing inverter.
     *
     * @param id               The ID of the inverter to update
     * @param inverterDetails The updated inverter details
     * @return Inverter The updated inverter, or null if not found
     */
    public Inverter updateInverter(String id, Inverter inverterDetails) {
        Optional<Inverter> optionalInverter = inverterRepository.findById(id);
        if (optionalInverter.isPresent()) {
            Inverter inverter = optionalInverter.get();
            inverter.setName(inverterDetails.getName());
            inverter.setContinuousPower25C(inverterDetails.getContinuousPower25C());
            inverter.setContinuousPower40C(inverterDetails.getContinuousPower40C());
            inverter.setContinuousPower65C(inverterDetails.getContinuousPower65C());
            inverter.setMaxPower(inverterDetails.getMaxPower());
            inverter.setEfficiency(inverterDetails.getEfficiency());
            inverter.setVoltage(inverterDetails.getVoltage());
            inverter.setPrice(inverterDetails.getPrice());
            return inverterRepository.save(inverter);
        }
        return null;
    }

    /**
     * Deletes an inverter from the system by its ID.
     *
     * @param id The ID of the inverter to delete
     */
    public void deleteInverter(String id) {
        inverterRepository.deleteById(id);
    }
}
