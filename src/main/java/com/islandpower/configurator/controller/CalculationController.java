package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.CalculationParams;
import com.islandpower.configurator.model.CalculationResult;
import com.islandpower.configurator.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling calculation-related requests - ONLY FOR TESTING PURPOSES !!!!.
 * Provides endpoints for performing calculations based on input parameters.
 *
 *  @version 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/api/calculations")
public class CalculationController {

    /**
     * Service for performing calculations.
     */
    @Autowired
    private CalculationService calculationService;

    /**
     * Endpoint to calculate load based on provided parameters.
     *
     * @param params - Parameters required for the calculation
     * @return CalculationResult - The result of the calculation
     */
    @PostMapping("/load")
    public CalculationResult calculateLoad(@RequestBody CalculationParams params) {
        // Call the service method to perform the calculation and return the result
        return calculationService.calculate(params);
    }
}
