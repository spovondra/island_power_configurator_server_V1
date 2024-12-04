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

/**
 * Service class for managing location data for projects, including integration with external APIs
 * for solar irradiance, temperature data, and optimal PV system configurations.
 */
@Service
public class ProjectLocationService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectLocationService.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /* API urls for retrieving location-based data */
    private static final String PVGIS_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/PVcalc?lat=%s&lon=%s&outputformat=json&peakpower=1&loss=1&angle=%s&aspect=%s";
    private static final String OPTIMAL_VALUES_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/PVcalc?lat=%s&lon=%s&raddatabase=PVGIS-SARAH3&usehorizon=1&outputformat=json&js=1&select_database_grid=PVGIS-SARAH2&pvtechchoice=crystSi&peakpower=1&loss=21&mountingplace=free&optimalangles=1";
    private static final String MIN_MAX_TEMP_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/seriescalc?lat=%s&lon=%s&raddatabase=PVGIS-SARAH3&outputformat=json&&startyear=2023&endyear=2023";
    private static final String AVERAGE_TEMP_API_URL = "https://re.jrc.ec.europa.eu/api/v5_3/MRcalc?lat=%s&lon=%s&startyear=2023&endyear=2023&raddatabase=PVGIS-SARAH3&outputformat=json&userhorizon=&usehorizon=1&js=1&select_database_month=PVGIS-SARAH3&mstartyear=2023&mendyear=2023&avtemp=1";

    @Autowired
    private ProjectService projectService;

    /**
     * Processes location data for a project, fetching temperature and irradiance details and storing them in the project.
     *
     * @param projectId The ID of the project
     * @param userId The ID of the user
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     * @param angle The panel angle for PV system
     * @param aspect The panel aspect (azimuth)
     * @param useOptimalValues Indicates whether to use optimal values for angle and aspect
     */
    public void processLocationData(String projectId, String userId, double latitude, double longitude, int angle, int aspect, boolean useOptimalValues) {
        // retrieve min and max temperatures for the selected location
        double[] minMaxTemperatures = getMinMaxTemperatures(latitude, longitude);

        if (useOptimalValues) {
            /*fetch optimal panel angle and aspect if user selected that options */
            OptimalValues optimalValues = fetchOptimalValues(latitude, longitude);
            List<Site.MonthlyData> monthlyHI_dList = calculatePVGISData(latitude, longitude, optimalValues.optimalAngle(), optimalValues.optimalAspect());
            projectService.updateSiteWithLocationData(projectId, userId, latitude, longitude, minMaxTemperatures, optimalValues.optimalAngle(), optimalValues.optimalAspect(), useOptimalValues, monthlyHI_dList);
        } else {
            /* use user-provided angle and aspect for calculations */
            List<Site.MonthlyData> monthlyHI_dList = calculatePVGISData(latitude, longitude, angle, aspect);
            projectService.updateSiteWithLocationData(projectId, userId, latitude, longitude, minMaxTemperatures, angle, aspect, false, monthlyHI_dList);
        }
    }

    /**
     * Calculates PVGIS data for the specified location, angle, and aspect.
     *
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     * @param angle The panel angle for PV system
     * @param aspect The panel aspect (azimuth)
     * @return List of monthly solar data including irradiance and temperature
     */
    public List<Site.MonthlyData> calculatePVGISData(double latitude, double longitude, double angle, double aspect) {
        List<Site.MonthlyData> monthlyDataList = initializeMonthlyData();

        /* fetch and update average temperatures and iraddiance data */
        fetchAverageTemperatures(latitude, longitude, monthlyDataList);
        fetchIrradianceData(latitude, longitude, angle, aspect, monthlyDataList);

        return monthlyDataList;
    }

    /**
     * Fetches optimal angle and aspect values for PV system installation.
     *
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     * @return OptimalValues containing optimal angle and aspect
     */
    public OptimalValues fetchOptimalValues(double latitude, double longitude) {
        String apiUrl = String.format(OPTIMAL_VALUES_API_URL, latitude, longitude);
        String response = restTemplate.getForObject(apiUrl, String.class);
        return parseOptimalValues(response);
    }

    /**
     * Retrieves minimum and maximum temperatures for a location based on PVGIS hourly data.
     *
     * @param latitude  The latitude of the location
     * @param longitude The longitude of the location
     * @return Array containing minimum and maximum temperatures
     */
    public double[] getMinMaxTemperatures(double latitude, double longitude) {
        double[] minMaxTemperatures = new double[2];
        try {
            String apiUrl = String.format(MIN_MAX_TEMP_API_URL, latitude, longitude);
            String response = restTemplate.getForObject(apiUrl, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode hourlyDataNode = rootNode.path("outputs").path("hourly");

            double maxTemp = Double.MIN_VALUE;
            double minTemp = Double.MAX_VALUE;

            /* find min and max temperatures from obtained hourly data */
            for (JsonNode hourNode : hourlyDataNode) {
                double temp = hourNode.path("T2m").asDouble();
                double globalRadiation = hourNode.path("G(i)").asDouble();
                if (globalRadiation > 50.0) { //filter only relevant data where radiation is sufficient
                    maxTemp = Math.max(maxTemp, temp);
                    minTemp = Math.min(minTemp, temp);
                }
            }

            minMaxTemperatures[0] = minTemp;
            minMaxTemperatures[1] = maxTemp;
        } catch (Exception e) {
            logger.error("Error while fetching weather data", e);
        }
        return minMaxTemperatures;
    }

    /**
     * Parses JSON response to extract optimal angle and aspect values.
     *
     * @param jsonResponse JSON response from the optimal values API
     * @return OptimalValues Containing optimal angle and aspect
     */
    private OptimalValues parseOptimalValues(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode mountingSystemNode = rootNode.path("inputs").path("mounting_system").path("fixed");

            /* extract optimal slope (angle) and azimuth (aspect) values */
            Integer optimalAngle = mountingSystemNode.path("slope").path("value").asInt();
            Integer optimalAspect = mountingSystemNode.path("azimuth").path("value").asInt();

            return new OptimalValues(optimalAngle, optimalAspect);
        } catch (Exception e) {
            return new OptimalValues(null, null);
        }
    }

    /**
     * Initializes a list of monthly data objects with default values.
     *
     * @return List of initialized monthly data
     */
    private List<Site.MonthlyData> initializeMonthlyData() {
        List<Site.MonthlyData> monthlyDataList = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Site.MonthlyData monthlyData = new Site.MonthlyData();
            monthlyData.setMonth(month);
            monthlyData.setIrradiance(0.0);
            monthlyData.setAmbientTemperature(0.0);
            monthlyDataList.add(monthlyData);
        }
        return monthlyDataList;
    }

    /**
     * Fetches average ambient temperatures for a location and updates monthly data.
     *
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     * @param monthlyDataList List of monthly data to update
     */
    private void fetchAverageTemperatures(double latitude, double longitude, List<Site.MonthlyData> monthlyDataList) {
        try {
            String apiUrl = String.format(AVERAGE_TEMP_API_URL, latitude, longitude);
            String response = restTemplate.getForObject(apiUrl, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode temperatureNode = rootNode.path("outputs").path("monthly");

            /* iterate through monthly temperature data */
            for (JsonNode monthNode : temperatureNode) {
                int monthIndex = monthNode.path("month").asInt();
                if (monthIndex >= 1 && monthIndex <= 12) {
                    double temp = monthNode.path("T2m").asDouble();
                    monthlyDataList.get(monthIndex - 1).setAmbientTemperature(temp);
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching average ambient temperature", e);
        }
    }

    /**
     * Fetches monthly irradiance data for a location and updates monthly data.
     *
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     * @param angle The panel angle
     * @param aspect The panel aspect
     * @param monthlyDataList List of monthly data to update
     */
    private void fetchIrradianceData(double latitude, double longitude, double angle, double aspect, List<Site.MonthlyData> monthlyDataList) {
        try {
            String apiUrl = String.format(PVGIS_API_URL, latitude, longitude, angle, aspect);
            String response = restTemplate.getForObject(apiUrl, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode monthlyDataNode = rootNode.path("outputs").path("monthly").path("fixed");

            /* iterate through monthly temperature data */
            for (JsonNode monthNode : monthlyDataNode) {
                int monthIndex = monthNode.path("month").asInt();
                if (monthIndex >= 1 && monthIndex <= 12) {
                    double HI_d = monthNode.path("H(i)_d").asDouble();
                    monthlyDataList.get(monthIndex - 1).setIrradiance(HI_d);
                }
            }
        } catch (Exception e) {
            logger.error("Error processing PVGIS data", e);
        }
    }

    /**
     * Fetches site details by project ID.
     *
     * @param projectId The ID of the project
     * @param userId The ID of the user
     * @return Site object containing location data
     */
    public Site getSitesByProjectId(String projectId, String userId) {
        try {
            return projectService.getProjectById(projectId, userId).getSite();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Record to store optimal angle and aspect values for PV systems (simplified storage).
     */
    public record OptimalValues(Integer optimalAngle, Integer optimalAspect) {
    }
}
