package com.islandpower.configurator.Repository;

import com.islandpower.configurator.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository <User, String> {
}
