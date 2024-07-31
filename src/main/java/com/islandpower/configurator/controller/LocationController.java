package com.islandpower.configurator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.islandpower.configurator.service.LocationService;
import com.islandpower.configurator.service.LocationService.OptimalValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/calculatePVGISData")
    public ResponseEntity<String> calculatePVGISData(@RequestParam String latitude, @RequestParam String longitude, @RequestParam String angle, @RequestParam String aspect) {
        try {
            String response = locationService.calculatePVGISData(latitude, longitude, angle, aspect);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(503).body("{\"error\":\"Service unavailable\"}");
        }
    }

    @GetMapping("/fetchOptimalValues")
    public ResponseEntity<String> fetchOptimalValues(@RequestParam String latitude, @RequestParam String longitude) {
        try {
            OptimalValues optimalValues = locationService.fetchOptimalValues(latitude, longitude);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonResult = objectMapper.writeValueAsString(optimalValues);

            return new ResponseEntity<>(jsonResult, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(503).body("{\"error\":\"Service unavailable\"}");
        }
    }

    @GetMapping("/min-max-temperatures")
    public ResponseEntity<String> getMinMaxTemperatures(@RequestParam String latitude, @RequestParam String longitude) {
        try {
            double[] minMaxTemperatures = locationService.getMinMaxTemperatures(latitude, longitude);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonResult = String.format("{\"minTemp\": %f, \"maxTemp\": %f}", minMaxTemperatures[0], minMaxTemperatures[1]);

            return new ResponseEntity<>(jsonResult, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(503).body("{\"error\":\"Service unavailable\"}");
        }
    }
}
