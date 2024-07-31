package com.islandpower.configurator.repository;

import com.islandpower.configurator.model.Accessory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccessoryRepository extends MongoRepository<Accessory, String> {
}
