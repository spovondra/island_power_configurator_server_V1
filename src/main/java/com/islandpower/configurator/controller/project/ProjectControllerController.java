package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.model.Controller;
import com.islandpower.configurator.model.project.ProjectController;
import com.islandpower.configurator.service.project.ProjectControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing controller configurations within a project.
 * Provides endpoints for fetching suitable controllers, selecting a controller,
 * and retrieving the current controller configuration.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projects/{projectId}/controller")
public class ProjectControllerController {

    @Autowired
    private ProjectControllerService projectControllerService;

    /**
     * Retrieves a list of suitable controllers for a specific project based on the regulator type.
     *
     * @param projectId The ID of the project for which suitable controllers are fetched
     * @param regulatorType The type of regulator (PWM or MPPT)
     * @return {@link ResponseEntity} containing a list of {@link Controller} objects
     */
    @GetMapping("/suitable")
    public ResponseEntity<List<Controller>> getSuitableControllers(
            @PathVariable String projectId,
            @RequestParam String regulatorType) {

        List<Controller> suitableControllers = projectControllerService.getSuitableControllers(projectId, regulatorType);
        return ResponseEntity.ok(suitableControllers);
    }

    /**
     * Selects a controller for a specific project and performs calculations based on the selection.
     *
     * @param projectId The ID of the project for which the controller is selected
     * @param controllerId The ID of the selected controller
     * @return {@link ResponseEntity} containing the updated {@link ProjectController} configuration
     */
    @PostMapping("/select")
    public ResponseEntity<ProjectController> selectControllerForProject(
            @PathVariable String projectId,
            @RequestParam String controllerId) {

        ProjectController projectController = projectControllerService.selectControllerForProject(projectId, controllerId);
        return ResponseEntity.ok(projectController);
    }

    /**
     * Retrieves the current controller configuration for a specific project.
     *
     * @param projectId The ID of the project for which the controller configuration is retrieved
     * @return {@link ResponseEntity} containing the current {@link ProjectController} configuration,
     *         or an error message if not found
     */
    @GetMapping
    public ResponseEntity<?> getProjectController(@PathVariable String projectId) {
        try {
            ProjectController projectController = projectControllerService.getProjectController(projectId);
            return ResponseEntity.ok(projectController);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
