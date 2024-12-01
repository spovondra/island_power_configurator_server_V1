package com.islandpower.configurator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the system.
 * This class stores user information, including authentication details, roles, and project associations.
 * It is mapped to a MongoDB collection named "users".
 *
 * @version 1.0
 */
@Document(collection = "users")
public class OneUser {
    @Id
    private String id; // unique identifier for the user
    private String username; // username used for authentication
    private String password; // encoded password for secure authentication
    private String role; // role of the user within the system (e.g., admin, user)
    private String firstName; // first name of the user
    private String lastName; // last name of the user
    private String email; // email address of the user
    private List<String> projects = new ArrayList<>(); // list of project IDs associated with the user

    /**
     * Retrieves the unique identifier for the user.
     *
     * @return {@code String} The unique ID of the user
     */
    public String getId() {
        return id;
    }

    /**
     * Updates the unique identifier for the user.
     *
     * @param id The new unique ID of the user
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the username used for authentication.
     *
     * @return {@code String} The username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Updates the username used for authentication.
     *
     * @param username The new username of the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the encoded password for the user.
     *
     * @return {@code String} The encoded password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the encoded password for the user.
     *
     * @param password The new encoded password of the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the role assigned to the user.
     *
     * @return {@code String} The role of the user
     */
    public String getRole() {
        return role;
    }

    /**
     * Updates the role assigned to the user.
     *
     * @param role The new role of the user
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Retrieves the first name of the user.
     *
     * @return {@code String} The first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Updates the first name of the user.
     *
     * @param firstName The new first name of the user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return {@code String} The last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Updates the last name of the user.
     *
     * @param lastName The new last name of the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Retrieves the email address of the user.
     *
     * @return {@code String} The email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the email address of the user.
     *
     * @param email The new email address of the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the list of project IDs associated with the user.
     *
     * @return {@code List<String>} The list of project IDs
     */
    public List<String> getProjects() {
        return projects;
    }

    /**
     * Updates the list of project IDs associated with the user.
     *
     * @param projects The new list of project IDs
     */
    public void setProjects(List<String> projects) {
        this.projects = projects;
    }
}
