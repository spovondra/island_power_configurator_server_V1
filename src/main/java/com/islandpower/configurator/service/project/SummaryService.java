package com.islandpower.configurator.service.project;

import com.islandpower.configurator.dto.SummaryDto;
import com.islandpower.configurator.model.*;
import com.islandpower.configurator.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private InverterService inverterService;
    @Autowired
    private BatteryService batteryService;
    @Autowired
    private SolarPanelService solarPanelService;
    @Autowired
    private ControllerService controllerService;

    public SummaryDto getProjectSummary(String projectId, String userId) {
        Project project = projectService.getProjectById(projectId, userId);

        Inverter inverter = inverterService.getInverterById(project.getConfigurationModel().getProjectInverter().getInverterId())
                .orElseThrow(() -> new RuntimeException("Inverter not found"));
        Battery battery = batteryService.getBatteryById(project.getConfigurationModel().getProjectBattery().getBatteryId())
                .orElseThrow(() -> new RuntimeException("Battery not found"));
        SolarPanel solarPanel = solarPanelService.getSolarPanelById(project.getConfigurationModel().getProjectSolarPanel().getSolarPanelId())
                .orElseThrow(() -> new RuntimeException("Solar panel not found"));
        Controller controller = controllerService.getControllerById(project.getConfigurationModel().getProjectController().getControllerId())
                .orElseThrow(() -> new RuntimeException("Controller not found"));

        return new SummaryDto(project, inverter, battery, solarPanel, controller);
    }
}
