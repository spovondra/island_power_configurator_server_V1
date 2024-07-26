package com.islandpower.configurator.Repository;

import com.islandpower.configurator.Model.MyUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository <MyUser, String> {

    Optional<MyUser> findByUsername(String username);
}
