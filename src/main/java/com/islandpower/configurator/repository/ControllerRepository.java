package com.islandpower.configurator.repository;

import com.islandpower.configurator.model.Controller;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ControllerRepository extends MongoRepository<Controller, String> {
}
