package com.islandpower.configurator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for fetching and processing location-based data.
 * This class interacts with various external PVGIS APIs to obtain solar panel data, optimal values, and weather information.
 *
 * @version 1.0
 */
@Service
public class LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // API URLs
    public static final String PVGIS_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/PVcalc?lat=%s&lon=%s&outputformat=json&peakpower=1&loss=1&angle=%s&aspect=%s";
    public static final String OPTIMAL_VALUES_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/PVcalc?lat=%s&lon=%s&raddatabase=PVGIS-SARAH3&usehorizon=1&outputformat=json&js=1&select_database_grid=PVGIS-SARAH2&pvtechchoice=crystSi&peakpower=1&loss=21&mountingplace=free&optimalangles=1";
    public static final String WEATHER_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/seriescalc?lat=%s&lon=%s&raddatabase=PVGIS-SARAH3&outputformat=json&&startyear=2023&endyear=2023";

    /**
     * Fetches and processes photovoltaic (PV) data from the PVGIS API.
     * The data includes monthly solar irradiance values.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param angle The angle of the solar panels.
     * @param aspect The aspect (orientation) of the solar panels.
     * @return List of MonthlyHI_d objects containing monthly solar irradiance data.
     */
    public List<MonthlyHI_d> calculatePVGISData(String latitude, String longitude, String angle, String aspect) {
        List<MonthlyHI_d> monthlyHI_dList = new ArrayList<>();

        try {
            String apiUrl = String.format(PVGIS_API_URL, latitude, longitude, angle, aspect);
            String response = restTemplate.getForObject(apiUrl, String.class);

            if (response == null || response.trim().isEmpty()) {
                throw new RuntimeException("Received empty response from PVGIS API");
            }

            // Parse the JSON response
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode monthlyDataNode = rootNode.path("outputs").path("monthly").path("fixed");

            if (!monthlyDataNode.isArray()) {
                throw new RuntimeException("Expected an array of monthly data in the response");
            }

            // Loop through the monthly data array
            for (JsonNode monthNode : monthlyDataNode) {
                int month = monthNode.path("month").asInt();
                double HI_d = monthNode.path("H(i)_d").asDouble(); // Daily solar irradiance

                monthlyHI_dList.add(new MonthlyHI_d(month, HI_d));
            }

        } catch (Exception e) {
            logger.error("Error processing PVGIS data", e);
            throw new RuntimeException("Error processing PVGIS data", e);
        }

        return monthlyHI_dList; // Return the list of valid monthlyHI_d data
    }


    /**
     * Fetches optimal values for solar panel angles and aspects from the PVGIS API.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return OptimalValues object containing the optimal angle and aspect for solar panels.
     */
    public OptimalValues fetchOptimalValues(String latitude, String longitude) {
        String apiUrl = String.format(OPTIMAL_VALUES_API_URL, latitude, longitude);
        String response = restTemplate.getForObject(apiUrl, String.class);
        return parseOptimalValues(response);
    }

    /**
     * Parses the JSON response from the PVGIS API to extract optimal values for solar panel angles and aspects.
     *
     * @param jsonResponse The JSON response from the API.
     * @return OptimalValues object containing the optimal angle and aspect for solar panels.
     */
    private OptimalValues parseOptimalValues(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode inputsNode = rootNode.path("inputs");
            JsonNode mountingSystemNode = inputsNode.path("mounting_system").path("fixed");

            Integer optimalAngle = mountingSystemNode.path("slope").path("value").asInt();
            Integer optimalAspect = mountingSystemNode.path("azimuth").path("value").asInt();

            return new OptimalValues(optimalAngle, optimalAspect);
        } catch (Exception e) {
            logger.error("Error parsing optimal values from JSON response", e);
            return new OptimalValues(null, null);
        }
    }

    /**
     * Fetches minimum and maximum temperatures from the weather data API.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return Array containing minimum and maximum temperatures.
     */
    public double[] getMinMaxTemperatures(String latitude, String longitude) {
        try {
            String apiUrl = String.format(WEATHER_API_URL, latitude, longitude);
            String response = restTemplate.getForObject(apiUrl, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode hourlyDataNode = rootNode.path("outputs").path("hourly");

            double maxTemp = Double.MIN_VALUE;
            double minTemp = Double.MAX_VALUE;

            for (JsonNode hourNode : hourlyDataNode) {
                double temp = hourNode.path("T2m").asDouble();
                double globalRadiation = hourNode.path("G(i)").asDouble();

                // Remove nighttime hours (e.g., when G(h) is low, indicating no light)
                if (globalRadiation > 50.0) {  // Set a suitable threshold value as needed
                    maxTemp = Math.max(maxTemp, temp);
                    minTemp = Math.min(minTemp, temp);
                }
            }

            return new double[]{minTemp, maxTemp};
        } catch (Exception e) {
            logger.error("Error fetching weather data", e);
            throw new RuntimeException("Error fetching weather data", e);
        }
    }

    /**
     * Inner class representing monthly solar irradiance data.
     */
    public record MonthlyHI_d(int month, double HI_d) { }

    /**
     * Inner class representing optimal values for solar panel angles and aspects.
     */
    public static class OptimalValues {
        private Integer optimalAngle;
        private Integer optimalAspect;

        public OptimalValues(Integer optimalAngle, Integer optimalAspect) {
            this.optimalAngle = optimalAngle;
            this.optimalAspect = optimalAspect;
        }

        public Integer getOptimalAngle() {
            return optimalAngle;
        }

        public void setOptimalAngle(Integer optimalAngle) {
            this.optimalAngle = optimalAngle;
        }

        public Integer getOptimalAspect() {
            return optimalAspect;
        }

        public void setOptimalAspect(Integer optimalAspect) {
            this.optimalAspect = optimalAspect;
        }
    }
}
