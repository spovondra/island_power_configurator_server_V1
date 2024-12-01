package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.dto.SummaryDto;
import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.project.SummaryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for retrieving project summaries.
 * <p>
 * Provides an endpoint for fetching summarized data of a specific project,
 * including relevant details processed by the summary service.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/projects")
public class SummaryController {

    private final SummaryService summaryService;
    private final JwtUtilService jwtUtilService;

    /**
     * Constructs a SummaryController with the required dependencies.
     *
     * @param summaryService The service handling project summary data
     * @param jwtUtilService The service for handling JWT token operations
     */
    @Autowired
    public SummaryController(SummaryService summaryService, JwtUtilService jwtUtilService) {
        this.summaryService = summaryService;
        this.jwtUtilService = jwtUtilService;
    }

    /**
     * Retrieves the summary data for a specified project.
     *
     * @param projectId The ID of the project for which summary data is requested
     * @param request The HTTP request containing the JWT token
     * @return {@link ResponseEntity} containing the {@link SummaryDto} for the project, or an error response if unauthorized
     */
    @GetMapping("/{projectId}/summary")
    public ResponseEntity<SummaryDto> getProjectSummary(@PathVariable String projectId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request); // extract username from the JWT token (from the request)
        String userId = jwtUtilService.retrieveUserIdByUsername(username); //retrieve the user ID
        SummaryDto summaryDto = summaryService.getProjectSummary(projectId, userId); // Retrieve the project summary data
        return ResponseEntity.ok(summaryDto);
    }
}
