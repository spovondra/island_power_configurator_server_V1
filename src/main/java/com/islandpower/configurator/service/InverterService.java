package com.islandpower.configurator.service;

import com.islandpower.configurator.model.Inverter;
import com.islandpower.configurator.repository.InverterRepository; // Ensure you have this repository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InverterService {

    private final InverterRepository inverterRepository;

    @Autowired
    public InverterService(InverterRepository inverterRepository) {
        this.inverterRepository = inverterRepository;
    }

    // Create a new inverter
    public Inverter addInverter(Inverter inverter) {
        return inverterRepository.save(inverter);
    }

    // Get all inverters
    public List<Inverter> getAllInverters() {
        return inverterRepository.findAll();
    }

    // Get an inverter by ID
    public Optional<Inverter> getInverterById(String id) {
        return inverterRepository.findById(id);
    }

    // Update an inverter
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
        return null; // or throw an exception
    }

    // Delete an inverter
    public void deleteInverter(String id) {
        inverterRepository.deleteById(id);
    }
}
