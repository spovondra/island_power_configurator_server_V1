package com.islandpower.configurator.repository;

import com.islandpower.configurator.model.SolarPanel;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for managing SolarPanel entities in the database.
 * Extends MongoRepository to provide basic CRUD operations and custom queries.
 *
 * @version 1.0
 */
public interface SolarPanelRepository extends MongoRepository<SolarPanel, String> {
}
