package com.islandpower.configurator.repository;

import com.islandpower.configurator.model.Battery;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for performing CRUD operations on Battery documents in MongoDB.
 * This interface extends MongoRepository to provide built-in methods for data access.
 *
 * @version 1.0
 */
public interface BatteryRepository extends MongoRepository<Battery, String> {
}
