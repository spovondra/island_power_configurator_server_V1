package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.model.SolarPanel;
import com.islandpower.configurator.model.project.ProjectSolarPanel;
import com.islandpower.configurator.service.project.ProjectSolarPanelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing solar panel configurations within a specific project.
 * Provides endpoints for fetching suitable solar panels, selecting a solar panel,
 * calculating its configuration, and retrieving the current solar panel configuration.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projects/{projectId}/solar-panels")
public class ProjectSolarPanelController {

    @Autowired
    private ProjectSolarPanelService projectSolarPanelService;

    /**
     * Fetches a list of suitable solar panels for a specific project.
     *
     * @param projectId The ID of the project
     * @return ResponseEntity<List<SolarPanel>> The list of suitable solar panels
     */
    @GetMapping("/suitable")
    public ResponseEntity<List<SolarPanel>> getSuitableSolarPanels(@PathVariable String projectId) {
        List<SolarPanel> suitablePanels = projectSolarPanelService.getSuitableSolarPanels(projectId);
        return ResponseEntity.ok(suitablePanels);
    }

    /**
     * Selects a solar panel and calculates its configuration for the project.
     *
     * @param projectId   The ID of the project
     * @param requestBody A map containing solar panel details and efficiency parameters
     * @return ResponseEntity<ProjectSolarPanel> The updated solar panel configuration
     */
    @PostMapping("/select")
    public ResponseEntity<ProjectSolarPanel> selectSolarPanel(
            @PathVariable String projectId,
            @RequestBody Map<String, Object> requestBody) {

        /* Extract solar panel ID */
        String solarPanelId = (String) requestBody.get("solarPanelId");

        /* Extract efficiency and loss parameters with defaults */
        double panelOversizeCoefficient = extractDoubleFromRequest(requestBody, "panelOversizeCoefficient");
        double batteryEfficiency = extractDoubleFromRequest(requestBody, "batteryEfficiency");
        double cableEfficiency = extractDoubleFromRequest(requestBody, "cableEfficiency");

        /* Extract selected months */
        List<Integer> selectedMonths = extractIntegerListFromRequest(requestBody);

        /* Extract other configuration parameters */
        String installationType = (String) requestBody.get("installationType");
        double manufacturerTolerance = extractDoubleFromRequest(requestBody, "manufacturerTolerance");
        double agingLoss = extractDoubleFromRequest(requestBody, "agingLoss");
        double dirtLoss = extractDoubleFromRequest(requestBody, "dirtLoss");

        /* Calculate and save the solar panel configuration */
        ProjectSolarPanel projectSolarPanel = projectSolarPanelService.calculateSolarPanelConfiguration(
                projectId, solarPanelId, panelOversizeCoefficient, batteryEfficiency, cableEfficiency,
                selectedMonths, installationType, manufacturerTolerance, agingLoss, dirtLoss);

        return ResponseEntity.ok(projectSolarPanel);
    }

    /**
     * Retrieves the current solar panel configuration for a specific project.
     *
     * @param projectId The ID of the project
     * @return ResponseEntity<ProjectSolarPanel> The current solar panel configuration
     */
    @GetMapping
    public ResponseEntity<ProjectSolarPanel> getProjectSolarPanel(@PathVariable String projectId) {
        ProjectSolarPanel projectSolarPanel = projectSolarPanelService.getProjectSolarPanel(projectId);
        return ResponseEntity.ok(projectSolarPanel);
    }

    /**
     * Utility method to extract a double value from the request body.
     *
     * @param requestBody The request body as a map
     * @param key The key to extract the value
     * @return double The extracted or default value
     */
    private double extractDoubleFromRequest(Map<String, Object> requestBody, String key) {
        return requestBody.get(key) != null ? ((Number) requestBody.get(key)).doubleValue() : 0.0;
    }

    /**
     * Utility method to extract a list of integers from the request body.
     *
     * @param requestBody The request body as a map
     * @return List<Integer> The list of integers or an empty list if the key is absent
     */
    private List<Integer> extractIntegerListFromRequest(Map<String, Object> requestBody) {
        List<?> rawList = (List<?>) requestBody.get("selectedMonths");
        List<Integer> intList = new ArrayList<>();
        if (rawList != null) {
            for (Object item : rawList) {
                if (item instanceof Number) {
                    intList.add(((Number) item).intValue());
                }
            }
        }
        return intList;
    }
}
