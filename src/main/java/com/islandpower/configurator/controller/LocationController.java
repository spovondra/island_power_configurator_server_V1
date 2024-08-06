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

/**
 * Controller class for handling location-related requests.
 * This class provides endpoints for calculating PVGIS data, fetching optimal values, and retrieving minimum and maximum temperatures.
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/location")
public class LocationController {

    /**
     * Service for location-related calculations and PVGIS data retrieval.
     */
    @Autowired
    private LocationService locationService;

    /**
     * ObjectMapper for converting Java objects to JSON.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Endpoint to calculate PVGIS data based on latitude, longitude, angle, and aspect.
     *
     * @param latitude - Latitude coordinate of the location
     * @param longitude - Longitude coordinate of the location
     * @param angle - Angle of panel for the calculation
     * @param aspect - Aspect of panel for the calculation
     * @return ResponseEntity<String> - JSON response containing PVGIS data or error message
     */
    @GetMapping("/calculatePVGISData")
    public ResponseEntity<String> calculatePVGISData(@RequestParam String latitude, @RequestParam String longitude, @RequestParam String angle, @RequestParam String aspect) {
        try {
            List<LocationService.MonthlyHI_d> monthlyHI_dList = locationService.calculatePVGISData(latitude, longitude, angle, aspect);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonResult = objectMapper.writeValueAsString(monthlyHI_dList);

            return new ResponseEntity<>(jsonResult, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            // Error processing JSON response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error processing JSON\"}");
        } catch (RuntimeException e) {
            // General error while processing API request
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("{\"error\":\"Service unavailable\"}");
        }
    }

    /**
     * Endpoint to fetch optimal values (angle and aspect) based on latitude and longitude.
     *
     * @param latitude - Latitude coordinate of the location
     * @param longitude - Longitude coordinate of the location
     * @return ResponseEntity<String> - JSON response containing optimal angle and aspect or error message
     */
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

    /**
     * Endpoint to retrieve the minimum and maximum temperatures for a given latitude and longitude.
     *
     * @param latitude - Latitude coordinate of the location
     * @param longitude - Longitude coordinate of the location
     * @return ResponseEntity<String> - JSON response containing min and max temperatures or error message
     */
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

    // Utility methods

    /**
     * Utility method to format minimum and maximum temperatures as a JSON string.
     *
     * @param minMaxTemperatures - Array containing minimum and maximum temperatures
     * @return String - JSON formatted string with temperatures
     */
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
