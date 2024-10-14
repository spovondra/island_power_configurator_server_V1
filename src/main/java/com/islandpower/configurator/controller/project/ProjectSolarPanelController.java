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

@RestController
@RequestMapping("/api/projects/{projectId}/solar-panels")
public class ProjectSolarPanelController {

    @Autowired
    private ProjectSolarPanelService projectSolarPanelService;

    // Endpoint to fetch suitable solar panels for a project
    @GetMapping("/suitable")
    public ResponseEntity<List<SolarPanel>> getSuitableSolarPanels(@PathVariable String projectId) {
        List<SolarPanel> suitablePanels = projectSolarPanelService.getSuitableSolarPanels(projectId);
        return ResponseEntity.ok(suitablePanels);
    }

    // Endpoint to select a solar panel and calculate its configuration
    @PostMapping("/select")
    public ResponseEntity<ProjectSolarPanel> selectSolarPanel(
            @PathVariable String projectId,
            @RequestBody Map<String, Object> requestBody) {

        String solarPanelId = (String) requestBody.get("solarPanelId");
        double panelOversizeCoefficient = ((Number) requestBody.get("panelOversizeCoefficient")).doubleValue();
        double batteryEfficiency = ((Number) requestBody.get("batteryEfficiency")).doubleValue();
        double cableEfficiency = ((Number) requestBody.get("cableEfficiency")).doubleValue();
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
        double manufacturerTolerance = ((Number) requestBody.get("manufacturerTolerance")).doubleValue();
        double agingLoss = ((Number) requestBody.get("agingLoss")).doubleValue();
        double dirtLoss = ((Number) requestBody.get("dirtLoss")).doubleValue();

        ProjectSolarPanel projectSolarPanel = projectSolarPanelService.calculateSolarPanelConfiguration(
                projectId, solarPanelId, panelOversizeCoefficient, batteryEfficiency, cableEfficiency,
                selectedMonths, installationType, manufacturerTolerance, agingLoss, dirtLoss);

        return ResponseEntity.ok(projectSolarPanel);
    }

    // Endpoint to get the current solar panel configuration for a project
    @GetMapping
    public ResponseEntity<ProjectSolarPanel> getProjectSolarPanel(@PathVariable String projectId) {
        ProjectSolarPanel projectSolarPanel = projectSolarPanelService.getProjectSolarPanel(projectId);
        return ResponseEntity.ok(projectSolarPanel);
    }
}
