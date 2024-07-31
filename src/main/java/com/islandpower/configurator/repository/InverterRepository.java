package com.islandpower.configurator.repository;

import com.islandpower.configurator.model.Inverter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InverterRepository extends MongoRepository<Inverter, String> {
}
