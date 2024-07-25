package com.islandpower.configurator.Controller;

import com.islandpower.configurator.Model.User;
import com.islandpower.configurator.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

     @Autowired
     private UserServices userServices;

    @PostMapping("/register")
    public String registerUser (@RequestBody User users) {
        userServices.saveUser(users);
        return users.getId();
    }

    @GetMapping("/getAll")
    public Iterable<User> getAllUsers() {
        return userServices.getAll();
    }

    @PutMapping(value="/edit/{id}")
    public String updateUser(@PathVariable("id") String userId, @RequestBody User updatedUser) {
        updatedUser.setId(userId);
        userServices.saveUser(updatedUser);
        return userId;
    }

    @DeleteMapping(value="/delete/{id}")
    public void deleteUser(@PathVariable("id") String userId) {
        userServices.deleteById(userId);
    }

    @RequestMapping(value="user/{id}")
    public User getUserById(@PathVariable("id") String userId) {
        return userServices.getUserById(userId);
    }
}
