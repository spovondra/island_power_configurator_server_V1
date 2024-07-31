package com.islandpower.configurator.repository;

import com.islandpower.configurator.Model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProjectRepository extends MongoRepository<Project, String> {
}
