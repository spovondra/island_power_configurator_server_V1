package com.islandpower.configurator.repository;

import com.islandpower.configurator.Model.Battery;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BatteryRepository extends MongoRepository<Battery, String> {
}
