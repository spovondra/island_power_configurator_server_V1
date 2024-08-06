package com.islandpower.configurator.repository;

import com.islandpower.configurator.model.OneUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on OneUser entities.
 * This interface extends MongoRepository to provide built-in methods for accessing
 * and managing OneUser documents in a MongoDB database. It also includes a custom method
 * to find a user by their username.
 */
public interface UserRepository extends MongoRepository<OneUser, String> {

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return An Optional containing the user if found, or empty if no user with the given username exists.
     */
    Optional<OneUser> findByUsername(String username);
}
