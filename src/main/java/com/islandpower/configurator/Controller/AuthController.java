package com.islandpower.configurator.Controller;

import com.islandpower.configurator.Model.OneUser;
import com.islandpower.configurator.Service.OneUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OneUserDetailService userServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public OneUser registerUser (@RequestBody OneUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userServices.saveUser(user);
    }

    @GetMapping("/getAll")
    public Iterable<OneUser> getAllUsers() {
        return userServices.getAll();
    }

    @PutMapping(value="/update/{id}")
    public String updateUser(@PathVariable("id") String userId, @RequestBody OneUser updatedOneUser) {
        updatedOneUser.setId(userId);
        userServices.saveUser(updatedOneUser);
        return userId;
    }

    @DeleteMapping(value="/delete/{id}")
    public void deleteUser(@PathVariable("id") String userId) {
        userServices.deleteById(userId);
    }

    @RequestMapping(value="/user/{id}")
    public OneUser getUserById(@PathVariable("id") String userId) {
        return userServices.getUserById(userId);
    }

    @PostMapping("/authenticate")
    public OneUser authenticateUser(@RequestParam String username, @RequestParam String password) {
        if (userServices.authenticateUser(username, password)) {
            Optional<OneUser> userOptional = userServices.getUserByUsername(username);
            return userOptional.orElse(null);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }


}
