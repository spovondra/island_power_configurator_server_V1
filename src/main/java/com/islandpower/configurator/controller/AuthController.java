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
 * Provides endpoints for user registration, authentication, and management,
 * including login, logout, user retrieval, and token refresh.
 *
 * @version 1.0
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

    /**
     * Registers a new user in the system.
     *
     * @param user The user object containing registration details
     * @return ResponseEntity<?> The created user or an error message if the username already exists
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody OneUser user) {
        Optional<OneUser> existingUser = userServices.getUserByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        OneUser savedUser = userServices.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Retrieves all registered users.
     *
     * @return Iterable<OneUser> A list of all users
     */
    @GetMapping("/getAll")
    public Iterable<OneUser> getAllUsers() {
        return userServices.getAll();
    }

    /**
     * Updates an existing user.
     *
     * @param userId The ID of the user to update
     * @param updatedOneUser The updated user details
     * @return String The ID of the updated user
     */
    @PutMapping("/update/{id}")
    public String updateUser(@PathVariable("id") String userId, @RequestBody OneUser updatedOneUser) {
        OneUser existingUser = userServices.getUserById(userId);
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (updatedOneUser.getUsername() != null && !updatedOneUser.getUsername().isEmpty()) {
            existingUser.setUsername(updatedOneUser.getUsername());
        }
        if (updatedOneUser.getRole() != null && !updatedOneUser.getRole().isEmpty()) {
            existingUser.setRole(updatedOneUser.getRole());
        }
        if (updatedOneUser.getFirstName() != null && !updatedOneUser.getFirstName().isEmpty()) {
            existingUser.setFirstName(updatedOneUser.getFirstName());
        }
        if (updatedOneUser.getLastName() != null && !updatedOneUser.getLastName().isEmpty()) {
            existingUser.setLastName(updatedOneUser.getLastName());
        }
        if (updatedOneUser.getEmail() != null && !updatedOneUser.getEmail().isEmpty()) {
            existingUser.setEmail(updatedOneUser.getEmail());
        }
        if (updatedOneUser.getPassword() != null && !updatedOneUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedOneUser.getPassword()));
        }

        userServices.saveUser(existingUser);
        return userId;
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId The ID of the user to delete
     */
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") String userId) {
        userServices.deleteById(userId);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId The ID of the user to retrieve
     * @return OneUser The user object
     */
    @GetMapping("/user/{id}")
    public OneUser getUserById(@PathVariable("id") String userId) {
        return userServices.getUserById(userId);
    }

    /**
     * Authenticates a user and generates JWT tokens.
     *
     * @param username The username of the user
     * @param password The password of the user
     * @return ResponseEntity<?> JWT tokens and user details if authentication succeeds
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String username, @RequestParam String password) {
        if (userServices.authenticateUser(username, password)) {
            UserDetails userDetails = userServices.loadUserByUsername(username);
            String jwt = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

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
     * Refreshes JWT token using a refresh token.
     *
     * @param refreshToken The refresh token
     * @return ResponseEntity<?> A new JWT token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshJwtToken(@RequestParam String refreshToken) {
        String newToken = jwtUtil.refreshToken(refreshToken);
        if (newToken != null) {
            Map<String, String> response = new HashMap<>();
            response.put("jwt", newToken);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
