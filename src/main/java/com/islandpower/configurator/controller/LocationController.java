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
 * Controller for managing location-based calculations and data retrieval.
 * <p>
 * Provides endpoints for calculating PVGIS data, retrieving optimal installation parameters,
 * and fetching minimum and maximum temperatures for a given location.
 * </p>
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/location")
public class LocationController {

    /**
     * Service for performing location-related calculations and retrieving PVGIS data.
     */
    @Autowired
    private LocationService locationService;

    /**
     * Utility for converting objects to JSON format.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Calculates PVGIS data based on location and panel configuration.
     *
     * @param latitude - Latitude coordinate of the location
     * @param longitude - Longitude coordinate of the location
     * @param angle - Installation angle of the solar panels
     * @param aspect - Orientation aspect of the solar panels
     * @return ResponseEntity<String> - JSON containing PVGIS data or an error message
     */
    @GetMapping("/calculatePVGISData")
    public ResponseEntity<String> calculatePVGISData(
            @RequestParam String latitude,
            @RequestParam String longitude,
            @RequestParam String angle,
            @RequestParam String aspect) {
        try {
            List<LocationService.MonthlyHI_d> monthlyHI_dList = locationService.calculatePVGISData(latitude, longitude, angle, aspect);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonResult = objectMapper.writeValueAsString(monthlyHI_dList);

            return new ResponseEntity<>(jsonResult, headers, HttpStatus.OK);
        } catch (JsonProcessingException e) { // error processing JSON response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\":\"Error processing JSON\"}");
        } catch (RuntimeException e) { // general error while processing API request
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("{\"error\":\"Service unavailable\"}");
        }
    }

    /**
     * Retrieves the optimal angle and aspect for solar panel installation based on location.
     *
     * @param latitude - Latitude coordinate of the location
     * @param longitude - Longitude coordinate of the location
     * @return ResponseEntity<String> - JSON containing the optimal angle and aspect or an error message
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
     * Retrieves minimum and maximum temperatures for a given location.
     *
     * @param latitude - Latitude coordinate of the location
     * @param longitude - Longitude coordinate of the location
     * @return ResponseEntity<String> - JSON containing the min and max temperatures or an error message
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
     * Formats an array of temperatures into a JSON string.
     *
     * @param minMaxTemperatures - Array containing minimum and maximum temperatures
     * @return String - JSON string with formatted temperature values
     */
    private static String getString(double[] minMaxTemperatures) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#.00", symbols);

        String minTempFormatted = decimalFormat.format(minMaxTemperatures[0]);
        String maxTempFormatted = decimalFormat.format(minMaxTemperatures[1]);

        return String.format("{\"minTemp\": %s, \"maxTemp\": %s}", minTempFormatted, maxTempFormatted);
    }
}
