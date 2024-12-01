package com.islandpower.configurator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up CORS (Cross-Origin Resource Sharing) in the application.
 * This class configures CORS settings to allow or restrict cross-origin requests.
 *
 * @version 1.1
 */
@Configuration
public class WebConfig {

    /**
     * Configures CORS settings.
     *
     * @return WebMvcConfigurer - the configured CORS settings
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**") // apply CORS rules to all API endpoints
                        .allowedOrigins("http://localhost:3000", "https://fve.firmisimo.eu") // allow specific origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // specify allowed HTTP methods
                        .allowedHeaders("*") // allow all headers
                        .allowCredentials(true); // enable credentials
            }
        };
    }
}
