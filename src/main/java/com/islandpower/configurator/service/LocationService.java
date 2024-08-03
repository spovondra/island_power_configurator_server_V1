package com.islandpower.configurator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final String PVGIS_API_URL = "https://re.jrc.ec.europa.eu/api/v5_2/PVcalc?lat=%s&lon=%s&peakpower=1&loss=1&angle=%s&aspect=%s";
    public static final String OPTIMAL_VALUES_API_URL = "https://re.jrc.ec.europa.eu/api/v5_2/PVcalc?lat=%s&lon=%s&raddatabase=PVGIS-SARAH2&usehorizon=1&outputformat=json&js=1&select_database_grid=PVGIS-SARAH2&pvtechchoice=crystSi&peakpower=1&loss=21&mountingplace=free&optimalangles=1";
    public static final String WEATHER_API_URL = "https://re.jrc.ec.europa.eu/api/v5_2/seriescalc?lat=%s&lon=%s&raddatabase=PVGIS-SARAH2&outputformat=json&startyear=2019&endyear=2020";

    public String calculatePVGISData(String latitude, String longitude, String angle, String aspect) {
        String apiUrl = String.format(PVGIS_API_URL, latitude, longitude, angle, aspect);
        return restTemplate.getForObject(apiUrl, String.class);
    }

    public OptimalValues fetchOptimalValues(String latitude, String longitude) {
        String apiUrl = String.format(OPTIMAL_VALUES_API_URL, latitude, longitude);
        String response = restTemplate.getForObject(apiUrl, String.class);
        return parseOptimalValues(response);
    }

    private OptimalValues parseOptimalValues(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode inputsNode = rootNode.path("inputs");
            JsonNode mountingSystemNode = inputsNode.path("mounting_system").path("fixed");

            Integer optimalAngle = mountingSystemNode.path("slope").path("value").asInt();
            Integer optimalAspect = mountingSystemNode.path("azimuth").path("value").asInt();

            return new OptimalValues(optimalAngle, optimalAspect);
        } catch (Exception e) {
            e.printStackTrace();
            return new OptimalValues(null, null);
        }
    }

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
            e.printStackTrace();
            throw new RuntimeException("Error fetching weather data");
        }
    }
}
