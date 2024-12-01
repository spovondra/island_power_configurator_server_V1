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

    /**
     * Constructs a SolarPanelService with the specified repository.
     *
     * @param solarPanelRepository The repository for managing solar panel entities
     */
    @Autowired
    public SolarPanelService(SolarPanelRepository solarPanelRepository) {
        this.solarPanelRepository = solarPanelRepository;
    }

    /**
     * Creates and saves a new solar panel.
     *
     * @param solarPanel The solar panel object to save
     * @return {@link SolarPanel} The saved solar panel object
     */
    public SolarPanel addSolarPanel(SolarPanel solarPanel) {
        return solarPanelRepository.save(solarPanel);
    }

    /**
     * Retrieves all solar panels.
     *
     * @return {@link List} of {@link SolarPanel} objects in the repository
     */
    public List<SolarPanel> getAllSolarPanels() {
        return solarPanelRepository.findAll();
    }

    /**
     * Retrieves a solar panel by its ID.
     *
     * @param id The ID of the solar panel to retrieve
     * @return {@link Optional} containing the {@link SolarPanel} if found, or empty if not found
     */
    public Optional<SolarPanel> getSolarPanelById(String id) {
        return solarPanelRepository.findById(id);
    }

    /**
     * Updates the details of an existing solar panel.
     *
     * @param id The ID of the solar panel to update
     * @param solarPanelDetails The new details for the solar panel
     * @return {@link SolarPanel} The updated solar panel object, or null if not found
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
        return null;
    }

    /**
     * Deletes a solar panel by its ID.
     *
     * @param id The ID of the solar panel to delete
     */
    public void deleteSolarPanel(String id) {
        solarPanelRepository.deleteById(id);
    }
}
