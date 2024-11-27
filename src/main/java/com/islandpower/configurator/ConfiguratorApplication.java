package com.islandpower.configurator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Configurator application.
 *
 * @version 1.0
 */
@SpringBootApplication
public class ConfiguratorApplication {

	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ConfiguratorApplication.class, args);
	}
}
