package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.model.Project;
import com.islandpower.configurator.model.project.Appliance;
import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.ProjectService;
import com.islandpower.configurator.service.project.ProjectApplianceService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/appliances")
public class ProjectApplianceController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectApplianceController.class);

    @Autowired
    private ProjectApplianceService projectApplianceService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @PostMapping
    public Project addOrUpdateAppliance(@PathVariable String projectId, @RequestBody Appliance appliance, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is adding/updating an appliance in project with ID: {}", username, userId, projectId);
        return projectApplianceService.addOrUpdateAppliance(projectId, appliance);
    }

    @DeleteMapping("/{applianceId}")
    public void removeAppliance(@PathVariable String projectId, @PathVariable String applianceId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is attempting to remove appliance with ID: {} from project with ID: {}", username, userId, applianceId, projectId);
        projectApplianceService.removeAppliance(projectId, applianceId);
        logger.info("Appliance with ID: {} was successfully removed from project with ID: {} by user {} (ID: {})", applianceId, projectId, username, userId);
    }
}
