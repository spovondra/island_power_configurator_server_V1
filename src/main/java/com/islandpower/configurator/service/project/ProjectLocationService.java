package com.islandpower.configurator.service.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.islandpower.configurator.model.project.Site;
import com.islandpower.configurator.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectLocationService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectLocationService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // API URLs
    private static final String PVGIS_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/PVcalc?lat=%s&lon=%s&outputformat=json&peakpower=1&loss=1&angle=%s&aspect=%s";
    private static final String OPTIMAL_VALUES_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/PVcalc?lat=%s&lon=%s&raddatabase=PVGIS-SARAH3&usehorizon=1&outputformat=json&js=1&select_database_grid=PVGIS-SARAH2&pvtechchoice=crystSi&peakpower=1&loss=21&mountingplace=free&optimalangles=1";
    private static final String MIN_MAX_TEMP_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/seriescalc?lat=%s&lon=%s&raddatabase=PVGIS-SARAH3&outputformat=json&&startyear=2023&endyear=2023";
    private static final String AVERAGE_TEMP_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/MRcalc?lat=%s&lon=%s&startyear=2023&endyear=2023&raddatabase=PVGIS-SARAH3&outputformat=json&userhorizon=&usehorizon=1&js=1&select_database_month=PVGIS-SARAH3&mstartyear=2023&mendyear=2023&avtemp=1";

    @Autowired
    private ProjectService projectService; // Assuming you have a service to handle projects

    public void processLocationData(String projectId, String userId, double latitude, double longitude, int angle, int aspect, boolean useOptimalValues) {
        double[] minMaxTemperatures = getMinMaxTemperatures(latitude, longitude);

        if (useOptimalValues) {
            OptimalValues optimalValues = fetchOptimalValues(latitude, longitude);
            List<Site.MonthlyData> monthlyHI_dList = calculatePVGISData(latitude, longitude, optimalValues.optimalAngle(), optimalValues.optimalAspect());
            projectService.updateSiteWithLocationData(projectId, userId, latitude, longitude, minMaxTemperatures, optimalValues.optimalAngle(), optimalValues.optimalAspect(), useOptimalValues, monthlyHI_dList);
        } else {
            List<Site.MonthlyData> monthlyHI_dList = calculatePVGISData(latitude, longitude, angle, aspect);
            projectService.updateSiteWithLocationData(projectId, userId, latitude, longitude, minMaxTemperatures, angle, aspect, false, monthlyHI_dList);
        }
    }

    public List<Site.MonthlyData> calculatePVGISData(double latitude, double longitude, double angle, double aspect) {
        List<Site.MonthlyData> monthlyDataList = new ArrayList<>();

        // Initialize the monthly data for all 12 months
        for (int month = 1; month <= 12; month++) {
            Site.MonthlyData monthlyData = new Site.MonthlyData();
            monthlyData.setMonth(month); // Set month (1 to 12)
            monthlyData.setIrradiance(0.0); // Default irradiance
            monthlyData.setAmbientTemperature(0.0); // Default temperature
            monthlyDataList.add(monthlyData);
        }

        // Fetch average ambient temperature
        try {
            String AvgTempApiUrl = String.format(AVERAGE_TEMP_API_URL, latitude, longitude);
            String AvgTempResponse = restTemplate.getForObject(AvgTempApiUrl, String.class);
            JsonNode rootNode = objectMapper.readTree(AvgTempResponse);
            JsonNode temperatureNode = rootNode.path("outputs").path("monthly");

            if (!temperatureNode.isArray()) {
                throw new RuntimeException("Expected an array of monthly average temperatures in the response");
            }

            // Update the ambient temperatures in the monthlyDataList
            for (JsonNode monthNode : temperatureNode) {
                int monthIndex = monthNode.path("month").asInt(); // Assuming month is 1-indexed
                if (monthIndex >= 1 && monthIndex <= 12) {
                    double temp = monthNode.path("T2m").asDouble();
                    monthlyDataList.get(monthIndex - 1).setAmbientTemperature(temp); // Update the correct month
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching average ambient temperature", e);
        }

        // Fetch monthly irradiance data
        try {
            String apiUrl = String.format(PVGIS_API_URL, latitude, longitude, angle, aspect);
            String response = restTemplate.getForObject(apiUrl, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode monthlyDataNode = rootNode.path("outputs").path("monthly").path("fixed");

            if (!monthlyDataNode.isArray()) {
                throw new RuntimeException("Expected an array of monthly data in the response");
            }

            // Update the irradiance values in the monthlyDataList
            for (JsonNode monthNode : monthlyDataNode) {
                int monthIndex = monthNode.path("month").asInt(); // Assuming month is 1-indexed
                if (monthIndex >= 1 && monthIndex <= 12) {
                    double HI_d = monthNode.path("H(i)_d").asDouble();
                    monthlyDataList.get(monthIndex - 1).setIrradiance(HI_d); // Update the correct month
                }
            }
        } catch (Exception e) {
            logger.error("Error processing PVGIS data", e);
        }

        return monthlyDataList;
    }

    public OptimalValues fetchOptimalValues(double latitude, double longitude) {
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
            logger.error("Error parsing optimal values from JSON response", e);
            return new OptimalValues(null, null);
        }
    }

    public double[] getMinMaxTemperatures(double latitude, double longitude) {
        double[] minMaxTemperatures = new double[2];
        try {
            String apiUrl = String.format(MIN_MAX_TEMP_API_URL, latitude, longitude);
            String response = restTemplate.getForObject(apiUrl, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode hourlyDataNode = rootNode.path("outputs").path("hourly");

            double maxTemp = Double.MIN_VALUE;
            double minTemp = Double.MAX_VALUE;

            // Find min and max temperatures based on hourly data
            for (JsonNode hourNode : hourlyDataNode) {
                double temp = hourNode.path("T2m").asDouble();
                double globalRadiation = hourNode.path("G(i)").asDouble();
                if (globalRadiation > 50.0) {
                    maxTemp = Math.max(maxTemp, temp);
                    minTemp = Math.min(minTemp, temp);
                }
            }

            minMaxTemperatures[0] = minTemp;
            minMaxTemperatures[1] = maxTemp;
        } catch (Exception e) {
            logger.error("Error fetching weather data", e);
        }
        return minMaxTemperatures;
    }

    public record OptimalValues(Integer optimalAngle, Integer optimalAspect) {
    }

    public Site getSitesByProjectId(String projectId, String userId) {
        try {
            return projectService.getProjectById(projectId, userId).getSite(); // Replace with actual repository method
        } catch (Exception e) {
            logger.error("Error fetching sites for project ID {}: {}", projectId, e.getMessage());
            return null;
        }
    }
}
