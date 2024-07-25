package com.islandpower.configurator.Service;

import com.islandpower.configurator.Model.User;
import com.islandpower.configurator.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User users) {
        userRepository.save(users);
    }

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    public void deleteById(String userId) {
        userRepository.deleteById(userId);
    }

    public User getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
