package com.islandpower.configurator.controller;

import com.islandpower.configurator.model.OneUser;
import com.islandpower.configurator.service.OneUserDetailService;
import com.islandpower.configurator.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Controller class for handling authentication and user management endpoints.
 * This class provides endpoints for user registration, authentication, and user management operations.
 *
 * @version 1.1
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OneUserDetailService userServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody OneUser user) {
        // Check if the username already exists
        Optional<OneUser> existingUser = userServices.getUserByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        OneUser savedUser = userServices.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Endpoint to get all users.
     *
     * @return Iterable<OneUser> - An iterable of all users
     */
    @GetMapping("/getAll")
    public Iterable<OneUser> getAllUsers() {
        return userServices.getAll();
    }

    /**
     * Endpoint to update an existing user.
     *
     * @param userId - The ID of the user to update
     * @param updatedOneUser - The updated user object
     * @return String - The ID of the updated user
     */
    @PutMapping("/update/{id}")
    public String updateUser(@PathVariable("id") String userId, @RequestBody OneUser updatedOneUser) {
        OneUser existingUser = userServices.getUserById(userId);
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        existingUser.setUsername(updatedOneUser.getUsername());
        existingUser.setRole(updatedOneUser.getRole());
        existingUser.setFirstName(updatedOneUser.getFirstName());
        existingUser.setLastName(updatedOneUser.getLastName());
        existingUser.setEmail(updatedOneUser.getEmail());

        if (updatedOneUser.getPassword() != null && !updatedOneUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedOneUser.getPassword()));
        }

        userServices.saveUser(existingUser);
        return userId;
    }

    /**
     * Endpoint to delete a user by ID.
     *
     * @param userId - The ID of the user to delete
     */
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") String userId) {
        userServices.deleteById(userId);
    }

    /**
     * Endpoint to get a user by ID.
     *
     * @param userId - The ID of the user to retrieve
     * @return OneUser - The retrieved user object
     */
    @GetMapping("/user/{id}")
    public OneUser getUserById(@PathVariable("id") String userId) {
        return userServices.getUserById(userId);
    }

    /**
     * Endpoint to authenticate a user.
     *
     * @param username - The username of the user
     * @param password - The password of the user
     * @return ResponseEntity<?> - Response entity containing JWT token and user details
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String username, @RequestParam String password) {
        if (userServices.authenticateUser(username, password)) {
            UserDetails userDetails = userServices.loadUserByUsername(username);
            String jwt = jwtUtil.generateToken(userDetails); // Generate access token
            String refreshToken = jwtUtil.generateRefreshToken(userDetails); // Generate refresh token

            Map<String, Object> response = new HashMap<>();
            response.put("jwt", jwt);
            response.put("refreshToken", refreshToken);

            Optional<OneUser> optionalUser = userServices.getUserByUsername(username);
            if (optionalUser.isPresent()) {
                OneUser user = optionalUser.get();
                response.put("userId", user.getId());
                response.put("role", user.getRole());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found after successful authentication");
            }

            return ResponseEntity.ok(response);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    /**
     * Endpoint to refresh JWT token using a refresh token.
     *
     * @param refreshToken - The refresh token used to generate a new JWT token
     * @return ResponseEntity<?> - Response entity containing the new JWT token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshJwtToken(@RequestParam String refreshToken) {
        String newToken = jwtUtil.refreshToken(refreshToken); // Generate a new access token

        if (newToken != null) {
            Map<String, String> response = new HashMap<>();
            response.put("jwt", newToken);
            return ResponseEntity.ok(response); // Return the new token in the response
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
