package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.model.Controller;
import com.islandpower.configurator.model.project.ProjectController;
import com.islandpower.configurator.service.project.ProjectControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects/{projectId}/controller")
public class ProjectControllerController {

    @Autowired
    private ProjectControllerService projectControllerService;

    // Get suitable controllers for a specific project based on regulator type (PWM or MPPT)
    @GetMapping("/suitable")
    public ResponseEntity<List<Controller>> getSuitableControllers(
            @PathVariable String projectId,
            @RequestParam String regulatorType) {

        List<Controller> suitableControllers = projectControllerService.getSuitableControllers(projectId, regulatorType);
        return ResponseEntity.ok(suitableControllers);
    }

    // Select a controller by its ID and perform calculations
    @PostMapping("/select")
    public ResponseEntity<ProjectController> selectControllerForProject(
            @PathVariable String projectId,
            @RequestParam String controllerId) {

        // Call the service to select the controller and perform calculations
        ProjectController projectController = projectControllerService.selectControllerForProject(projectId, controllerId);
        return ResponseEntity.ok(projectController);
    }

    // Get the current controller configuration for a specific project
    @GetMapping
    public ResponseEntity<ProjectController> getProjectController(@PathVariable String projectId) {
        ProjectController projectController = projectControllerService.getProjectController(projectId);
        return ResponseEntity.ok(projectController);
    }
}
