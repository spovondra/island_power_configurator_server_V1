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
 * <p>
 * Provides endpoints for fetching suitable solar panels, selecting a solar panel,
 * calculating its configuration, and retrieving the current solar panel configuration.
 * </p>
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
     * @param projectId - the ID of the project
     * @return ResponseEntity<List<SolarPanel>> - the list of suitable solar panels
     */
    @GetMapping("/suitable")
    public ResponseEntity<List<SolarPanel>> getSuitableSolarPanels(@PathVariable String projectId) {
        List<SolarPanel> suitablePanels = projectSolarPanelService.getSuitableSolarPanels(projectId);
        return ResponseEntity.ok(suitablePanels);
    }

    /**
     * Selects a solar panel and calculates its configuration for the project.
     *
     * @param projectId - the ID of the project
     * @param requestBody - a map containing solar panel details and efficiency parameters
     * @return ResponseEntity<ProjectSolarPanel> - the updated solar panel configuration
     */
    @PostMapping("/select")
    public ResponseEntity<ProjectSolarPanel> selectSolarPanel(
            @PathVariable String projectId,
            @RequestBody Map<String, Object> requestBody) {

        String solarPanelId = (String) requestBody.get("solarPanelId");

        double panelOversizeCoefficient = requestBody.get("panelOversizeCoefficient") != null
                ? ((Number) requestBody.get("panelOversizeCoefficient")).doubleValue()
                : 0.0;
        double batteryEfficiency = requestBody.get("batteryEfficiency") != null
                ? ((Number) requestBody.get("batteryEfficiency")).doubleValue()
                : 0.0;
        double cableEfficiency = requestBody.get("cableEfficiency") != null
                ? ((Number) requestBody.get("cableEfficiency")).doubleValue()
                : 0.0;

        List<?> selectedMonthsRaw = (List<?>) requestBody.get("selectedMonths");
        List<Integer> selectedMonths = new ArrayList<>();
        if (selectedMonthsRaw != null) {
            for (Object item : selectedMonthsRaw) {
                if (item instanceof Number) {
                    selectedMonths.add(((Number) item).intValue());
                }
            }
        }

        String installationType = (String) requestBody.get("installationType");

        double manufacturerTolerance = requestBody.get("manufacturerTolerance") != null
                ? ((Number) requestBody.get("manufacturerTolerance")).doubleValue()
                : 0.0;
        double agingLoss = requestBody.get("agingLoss") != null
                ? ((Number) requestBody.get("agingLoss")).doubleValue()
                : 0.0;
        double dirtLoss = requestBody.get("dirtLoss") != null
                ? ((Number) requestBody.get("dirtLoss")).doubleValue()
                : 0.0;

        ProjectSolarPanel projectSolarPanel = projectSolarPanelService.calculateSolarPanelConfiguration(
                projectId, solarPanelId, panelOversizeCoefficient, batteryEfficiency, cableEfficiency,
                selectedMonths, installationType, manufacturerTolerance, agingLoss, dirtLoss);

        return ResponseEntity.ok(projectSolarPanel);
    }

    /**
     * Retrieves the current solar panel configuration for a specific project.
     *
     * @param projectId - the ID of the project
     * @return ResponseEntity<ProjectSolarPanel> - the current solar panel configuration
     */
    @GetMapping
    public ResponseEntity<ProjectSolarPanel> getProjectSolarPanel(@PathVariable String projectId) {
        ProjectSolarPanel projectSolarPanel = projectSolarPanelService.getProjectSolarPanel(projectId);
        return ResponseEntity.ok(projectSolarPanel);
    }
}
