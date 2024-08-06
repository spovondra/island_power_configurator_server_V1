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
 * @version 1.0
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OneUserDetailService userServices; // Service for user-related operations

    @Autowired
    private PasswordEncoder passwordEncoder; // Password encoder for hashing passwords

    @Autowired
    private JwtUtil jwtUtil; // Utility class for JWT token operations

    /**
     * Endpoint to register a new user.
     *
     * @param user - The user object to be registered
     * @return OneUser - The registered user object
     */
    @PostMapping("/register")
    public OneUser registerUser(@RequestBody OneUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode the user's password
        return userServices.saveUser(user); // Save and return the user
    }

    /**
     * Endpoint to get all users.
     *
     * @return Iterable<OneUser> - An iterable of all users
     */
    @GetMapping("/getAll")
    public Iterable<OneUser> getAllUsers() {
        return userServices.getAll(); // Return all users
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
        OneUser existingUser = userServices.getUserById(userId); // Get the existing user by ID
        if (existingUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"); // Throw error if user not found
        }

        // Update user details
        existingUser.setUsername(updatedOneUser.getUsername());
        existingUser.setRole(updatedOneUser.getRole());
        existingUser.setFirstName(updatedOneUser.getFirstName());
        existingUser.setLastName(updatedOneUser.getLastName());
        existingUser.setEmail(updatedOneUser.getEmail());

        if (updatedOneUser.getPassword() != null && !updatedOneUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedOneUser.getPassword())); // Encode and set new password if provided
        }

        userServices.saveUser(existingUser); // Save the updated user
        return userId; // Return the user ID
    }

    /**
     * Endpoint to delete a user by ID.
     *
     * @param userId - The ID of the user to delete
     */
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable("id") String userId) {
        userServices.deleteById(userId); // Delete the user by ID
    }

    /**
     * Endpoint to get a user by ID.
     *
     * @param userId - The ID of the user to retrieve
     * @return OneUser - The retrieved user object
     */
    @GetMapping("/user/{id}")
    public OneUser getUserById(@PathVariable("id") String userId) {
        return userServices.getUserById(userId); // Return the user by ID
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
            UserDetails userDetails = userServices.loadUserByUsername(username); // Load user details by username
            String jwt = jwtUtil.generateToken(userDetails); // Generate JWT token

            // Prepare response with JWT token and user details
            Map<String, Object> response = new HashMap<>();
            response.put("jwt", jwt);

            Optional<OneUser> optionalUser = userServices.getUserByUsername(username);
            if (optionalUser.isPresent()) {
                OneUser user = optionalUser.get();
                response.put("userId", user.getId());
                response.put("role", user.getRole());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found after successful authentication"); // Handle unexpected case
            }

            return ResponseEntity.ok(response); // Return response entity with token and user details
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"); // Throw error if authentication fails
        }
    }
}
