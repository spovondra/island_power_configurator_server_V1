package com.islandpower.configurator.repository;

import com.islandpower.configurator.model.SolarPanel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SolarPanelRepository extends MongoRepository<SolarPanel, String> {
}
