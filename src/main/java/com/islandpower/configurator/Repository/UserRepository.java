package com.islandpower.configurator.Repository;

import com.islandpower.configurator.Model.OneUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository <OneUser, String> {

    Optional<OneUser> findByUsername(String username);
}
