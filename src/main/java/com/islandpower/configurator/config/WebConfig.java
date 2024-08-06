package com.islandpower.configurator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up CORS (Cross-Origin Resource Sharing) in the application.
 * This class configures CORS settings to allow or restrict cross-origin requests.
 *
 * @version 1.0
 */
@Configuration
public class WebConfig {

    /**
     * Configures CORS settings.
     *
     * @return WebMvcConfigurer - The configured CORS settings
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Configure CORS for the /api/** endpoints
                registry.addMapping("/api/**")
                        .allowedOrigins("*") // Allow all origins - FOR TESTING PURPOSES ONLY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specified HTTP methods
                        .allowedHeaders("*"); // Allow all headers
                        //.allowCredentials(true); // FOR TESTING PURPOSES ONLY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        };
    }
}
