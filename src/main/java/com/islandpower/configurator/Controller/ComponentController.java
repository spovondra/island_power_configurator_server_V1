package com.islandpower.configurator.Controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/components")

public class ComponentController {
    @GetMapping("/solar-panel")
    public String getAllUsers() {
        return "solar";
    }
}
