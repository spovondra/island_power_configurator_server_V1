package com.islandpower.configurator.controller;

import com.islandpower.configurator.Model.CalculationParams;
import com.islandpower.configurator.Model.CalculationResult;
import com.islandpower.configurator.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/calculations")
public class CalculationController {

    @Autowired
    private CalculationService calculationService;

    @PostMapping("/load")
    public CalculationResult calculateLoad(@RequestBody CalculationParams params) {
        return calculationService.calculate(params);
    }
}
