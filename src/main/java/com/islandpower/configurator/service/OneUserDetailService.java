package com.islandpower.configurator.service;

import com.islandpower.configurator.model.OneUser;
import com.islandpower.configurator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for handling user-related operations and security details.
 * This class implements the UserDetailsService interface and provides methods
 * for managing user information and authentication.
 *
 * @version 1.0
 */
@Service
public class OneUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; // Repository for CRUD operations on OneUser entities

    private PasswordEncoder passwordEncoder; // Password encoder for encoding and validating passwords

    /**
     * Saves a new user to the repository with an encoded password.
     *
     * @param oneUser - The user object to be saved
     * @return OneUser - The saved user object with encoded password
     */
    public OneUser saveUser(OneUser oneUser) {
        userRepository.save(oneUser);
        return oneUser;
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return Iterable<OneUser> - An iterable collection of all users
     */
    public Iterable<OneUser> getAll() {
        return userRepository.findAll();
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId - The ID of the user to delete
     */
    public void deleteById(String userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId - The ID of the user to retrieve
     * @return OneUser - The user object corresponding to the provided ID
     * @throws RuntimeException if the user is not found
     */
    public OneUser getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId)); // Throw exception if user not found
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username - The username of the user to retrieve
     * @return Optional<OneUser> - An optional user object, which may be empty if the user is not found
     */
    public Optional<OneUser> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Sets the PasswordEncoder used for encoding passwords.
     *
     * @param passwordEncoder - The PasswordEncoder to set
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authenticates a user by comparing the provided password with the stored password.
     *
     * @param username - The username of the user to authenticate
     * @param rawPassword - The raw password provided for authentication
     * @return boolean - True if authentication is successful, false otherwise
     */
    public boolean authenticateUser(String username, String rawPassword) {
        Optional<OneUser> optionalUser = userRepository.findByUsername(username); // Find user by username
        if (optionalUser.isPresent()) {
            OneUser user = optionalUser.get();
            return passwordEncoder.matches(rawPassword, user.getPassword()); // Validate the provided password
        }
        return false; // Authentication fails if user not found
    }

    /**
     * Loads user details by their username for authentication.
     *
     * @param username - The username of the user to load
     * @return UserDetails - The user details object for the specified username
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<OneUser> user = userRepository.findByUsername(username); // Find user by username
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj)) // Set user roles
                    .build(); // Build and return UserDetails object
        } else {
            throw new UsernameNotFoundException(username); // Throw exception if user is not found
        }
    }

    /**
     * Extracts roles from the user object and converts them into an array of role strings.
     *
     * @param user - The user object from which roles are extracted
     * @return String[] - An array of role strings
     */
    private String[] getRoles(OneUser user) {
        if (user.getRole() == null) {
            return new String[]{"USER"}; // Default role if none is set
        }
        return user.getRole().split(","); // Split roles by comma if multiple roles are present
    }
}
