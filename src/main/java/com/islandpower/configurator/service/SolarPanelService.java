package com.islandpower.configurator.service;

import com.islandpower.configurator.model.SolarPanel;
import com.islandpower.configurator.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SolarPanelService {

    private final SolarPanelRepository solarPanelRepository;

    @Autowired
    public SolarPanelService(SolarPanelRepository solarPanelRepository) {
        this.solarPanelRepository = solarPanelRepository;
    }

    // Create a new solar panel
    public SolarPanel addSolarPanel(SolarPanel solarPanel) {
        return solarPanelRepository.save(solarPanel);
    }

    // Get all solar panels
    public List<SolarPanel> getAllSolarPanels() {
        return solarPanelRepository.findAll();
    }

    // Get a solar panel by ID
    public Optional<SolarPanel> getSolarPanelById(String id) {
        return solarPanelRepository.findById(id);
    }

    // Update a solar panel
    public SolarPanel updateSolarPanel(String id, SolarPanel solarPanelDetails) {
        Optional<SolarPanel> optionalSolarPanel = solarPanelRepository.findById(id);
        if (optionalSolarPanel.isPresent()) {
            SolarPanel solarPanel = optionalSolarPanel.get();
            solarPanel.setManufacturer(solarPanelDetails.getManufacturer());
            solarPanel.setName(solarPanelDetails.getName());
            solarPanel.setpRated(solarPanelDetails.getpRated());
            solarPanel.setVoc(solarPanelDetails.getVoc());
            solarPanel.setIsc(solarPanelDetails.getIsc());
            solarPanel.setVmp(solarPanelDetails.getVmp());
            solarPanel.setImp(solarPanelDetails.getImp());
            solarPanel.setTempCoefficientPMax(solarPanelDetails.getTempCoefficientPMax());
            solarPanel.setTolerance(solarPanelDetails.getTolerance());
            solarPanel.setDegradationFirstYear(solarPanelDetails.getDegradationFirstYear());
            solarPanel.setDegradationYears(solarPanelDetails.getDegradationYears());
            solarPanel.setPrice(solarPanelDetails.getPrice());
            return solarPanelRepository.save(solarPanel);
        }
        return null; // or throw an exception
    }

    // Delete a solar panel
    public void deleteSolarPanel(String id) {
        solarPanelRepository.deleteById(id);
    }
}
