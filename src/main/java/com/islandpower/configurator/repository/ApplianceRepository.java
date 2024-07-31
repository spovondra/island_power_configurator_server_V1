package com.islandpower.configurator.repository;

import com.islandpower.configurator.model.Appliance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplianceRepository extends MongoRepository<Appliance, String> {
}