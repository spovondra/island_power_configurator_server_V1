package com.islandpower.configurator.service;

import com.islandpower.configurator.model.SolarPanel;
import com.islandpower.configurator.repository.SolarPanelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing solar panels.
 * This class provides methods to add, update, retrieve, and delete solar panel entities.
 *
 * @version 1.0
 */
@Service
public class SolarPanelService {

    private final SolarPanelRepository solarPanelRepository;

    @Autowired
    public SolarPanelService(SolarPanelRepository solarPanelRepository) {
        this.solarPanelRepository = solarPanelRepository;
    }

    /**
     * Creates a new solar panel in the repository.
     *
     * @param solarPanel The solar panel object to be saved
     * @return SolarPanel The saved solar panel object
     */
    public SolarPanel addSolarPanel(SolarPanel solarPanel) {
        return solarPanelRepository.save(solarPanel);
    }

    /**
     * Retrieves all solar panels from the repository.
     *
     * @return List<SolarPanel> A list of all solar panels
     */
    public List<SolarPanel> getAllSolarPanels() {
        return solarPanelRepository.findAll();
    }

    /**
     * Retrieves a solar panel by its ID.
     *
     * @param id The ID of the solar panel to retrieve
     * @return Optional<SolarPanel> An optional solar panel object, which may be empty if not found
     */
    public Optional<SolarPanel> getSolarPanelById(String id) {
        return solarPanelRepository.findById(id);
    }

    /**
     * Updates an existing solar panel.
     * If the solar panel with the specified ID is found, its details are updated.
     *
     * @param id The ID of the solar panel to update
     * @param solarPanelDetails The updated solar panel details
     * @return SolarPanel The updated solar panel object
     */
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

    /**
     * Deletes a solar panel from the repository.
     *
     * @param id The ID of the solar panel to delete
     */
    public void deleteSolarPanel(String id) {
        solarPanelRepository.deleteById(id);
    }
}
