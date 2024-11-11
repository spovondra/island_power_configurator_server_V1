package com.islandpower.configurator.controller.project;

import com.islandpower.configurator.dto.SummaryDto;
import com.islandpower.configurator.service.JwtUtilService;
import com.islandpower.configurator.service.project.SummaryService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class SummaryController {

    private static final Logger logger = LoggerFactory.getLogger(SummaryController.class);

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @GetMapping("/{projectId}/summary")
    public ResponseEntity<SummaryDto> getProjectSummary(@PathVariable String projectId, HttpServletRequest request) {
        String username = jwtUtilService.extractUsernameFromToken(request);
        String userId = jwtUtilService.retrieveUserIdByUsername(username);
        logger.info("User {} (ID: {}) is retrieving summary data for project ID: {}", username, userId, projectId);
        SummaryDto summaryDto = summaryService.getProjectSummary(projectId, userId);
        return ResponseEntity.ok(summaryDto);
    }
}
