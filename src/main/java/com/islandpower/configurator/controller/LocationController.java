package com.islandpower.configurator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/calculatePVGISData")
    public ResponseEntity<String> calculatePVGISData(@RequestParam String latitude, @RequestParam String longitude, @RequestParam String angle, @RequestParam String aspect) {
        try {
            List<LocationService.MonthlyHI_d> monthlyHI_dList = locationService.calculatePVGISData(latitude, longitude, angle, aspect);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonResult = objectMapper.writeValueAsString(monthlyHI_dList);

            return new ResponseEntity<>(jsonResult, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            // Chyba při zpracování JSON odpovědi
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error processing JSON\"}");
        } catch (RuntimeException e) {
            // Obecná chyba při zpracování API
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("{\"error\":\"Service unavailable\"}");
        }
    }

    @GetMapping("/fetchOptimalValues")
    public ResponseEntity<String> fetchOptimalValues(@RequestParam String latitude, @RequestParam String longitude) {
        try {
            OptimalValues optimalValues = locationService.fetchOptimalValues(latitude, longitude);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String jsonResult = String.format("{\"optimalAngle\": %d, \"optimalAspect\": %d}", optimalValues.getOptimalAngle(), optimalValues.getOptimalAspect());
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

            String jsonResult = getString(minMaxTemperatures);

            return new ResponseEntity<>(jsonResult, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(503).body("{\"error\":\"Service unavailable\"}");
        }
    }

    private static String getString(double[] minMaxTemperatures) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.00", symbols);

        // Format temperatures to two decimal places
        String minTempFormatted = decimalFormat.format(minMaxTemperatures[0]);
        String maxTempFormatted = decimalFormat.format(minMaxTemperatures[1]);

        return String.format("{\"minTemp\": %s, \"maxTemp\": %s}", minTempFormatted, maxTempFormatted);
    }

}
