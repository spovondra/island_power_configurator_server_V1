package com.islandpower.configurator.repository;

import com.islandpower.configurator.model.OneUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository interface for managing OneUser entities in the database.
 * Extends MongoRepository to provide basic CRUD operations and includes a custom method
 * for finding a user by their username.
 *
 * @version 1.0
 */
public interface UserRepository extends MongoRepository<OneUser, String> {

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to be retrieved
     * @return Optional containing the OneUser entity if found, or empty if no user with the specified username exists
     */
    Optional<OneUser> findByUsername(String username);
}
