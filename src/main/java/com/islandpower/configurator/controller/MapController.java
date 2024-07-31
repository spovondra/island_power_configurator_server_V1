package com.islandpower.configurator.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/map")
public class MapController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/calculatePVGISData")
    public ResponseEntity<String> calculatePVGISData(@RequestParam String latitude, @RequestParam String longitude, @RequestParam String angle, @RequestParam String aspect) {
        try {
            String apiUrl = String.format("https://re.jrc.ec.europa.eu/api/v5_2/PVcalc?lat=%s&lon=%s&peakpower=1&loss=1&angle=%s&aspect=%s", latitude, longitude, angle, aspect);
            String response = restTemplate.getForObject(apiUrl, String.class);

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
            String apiUrl = String.format("https://re.jrc.ec.europa.eu/api/v5_2/PVcalc?lat=%s&lon=%s&raddatabase=PVGIS-SARAH2&userhorizon=&usehorizon=1&outputformat=json&js=1&select_database_grid=PVGIS-SARAH2&pvtechchoice=crystSi&peakpower=1.01&loss=21&mountingplace=free&optimalangles=1", latitude, longitude);
            String response = restTemplate.getForObject(apiUrl, String.class);
            OptimalValues optimalValues = parseOptimalValues(response);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String jsonResult = objectMapper.writeValueAsString(optimalValues);

            return new ResponseEntity<>(jsonResult, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(503).body("{\"error\":\"Service unavailable\"}");
        }
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
}
