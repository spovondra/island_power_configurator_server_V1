package com.islandpower.configurator.service.project;

import com.islandpower.configurator.dto.SummaryDto;
import com.islandpower.configurator.model.*;
import com.islandpower.configurator.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for providing a summary of the project's configuration.
 * This class aggregates the project's key components such as inverter, battery, solar panel, and controller.
 */
@Service
public class SummaryService {

    @Autowired
    private ProjectService projectService; // Service for project-related operations

    @Autowired
    private InverterService inverterService; // Service for inverter-related operations

    @Autowired
    private BatteryService batteryService; // Service for battery-related operations

    @Autowired
    private SolarPanelService solarPanelService; // Service for solar panel-related operations

    @Autowired
    private ControllerService controllerService; // Service for  controller-related operations

    /**
     * Retrieves the project summary by fetching the project's key components.
     * The summary includes the project, inverter, battery, solar panel, and controller configurations.
     *
     * @param projectId The ID of the project
     * @param userId The ID of the user requesting the summary
     * @return SummaryDto Object containing the project's components summary
     * @throws RuntimeException If any component (inverter, battery, solar panel, controller) is not found
     */
    public SummaryDto getProjectSummary(String projectId, String userId) {
        /* Fetch project details */
        Project project = projectService.getProjectById(projectId, userId);

        /* Retrieve the inverter details */
        Inverter inverter = inverterService.getInverterById(project.getConfigurationModel().getProjectInverter().getInverterId())
                .orElseThrow(() -> new RuntimeException("Inverter not found"));

        /* Retrieve the battery details */
        Battery battery = batteryService.getBatteryById(project.getConfigurationModel().getProjectBattery().getBatteryId())
                .orElseThrow(() -> new RuntimeException("Battery not found"));

        /* Retrieve the solar panel details */
        SolarPanel solarPanel = solarPanelService.getSolarPanelById(project.getConfigurationModel().getProjectSolarPanel().getSolarPanelId())
                .orElseThrow(() -> new RuntimeException("Solar panel not found"));

        /* retrieve the controller details */
        Controller controller = controllerService.getControllerById(project.getConfigurationModel().getProjectController().getControllerId())
                .orElseThrow(() -> new RuntimeException("Controller not found"));

        /* return the project summary as a SummaryDto (which is containing all components) */
        return new SummaryDto(project, inverter, battery, solarPanel, controller);
    }
}
