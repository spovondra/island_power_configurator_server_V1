package com.islandpower.configurator.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the system.
 * This class is used to store user information, including authentication details and user-specific data.
 * It is mapped to a MongoDB collection named "users".
 *
 * @version 1.0
 */
@Document(collection = "users")
public class OneUser {

    /**
     * Unique identifier for the user, used as the primary key in the database.
     */
    @Id
    private String id;

    /**
     * Username of the user used for authentication.
     */
    private String username;

    /**
     * Password of the user, stored in an encoded format.
     */
    private String password;

    /**
     * Role assigned to the user, defining their permissions within the system.
     */
    private String role;

    /**
     * First name of the user.
     */
    private String firstName;

    /**
     * Last name of the user.
     */
    private String lastName;

    /**
     * Email address of the user.
     */
    private String email;

    /**
     * List of project IDs or names associated with the user.
     * This can be used to track the user's involvement in various projects.
     */
    private List<String> projects = new ArrayList<>();

    /**
     * Default constructor for the OneUser class.
     */
    public OneUser() {}

    /**
     * Parameterized constructor for creating a new OneUser instance.
     * Initializes the user with the given details and sets default values for optional fields.
     *
     * @param id - The unique identifier for the user
     * @param username - The username for authentication
     * @param password - The encoded password for the user
     * @param role - The role of the user in the system
     * @param firstName - The first name of the user
     * @param lastName - The last name of the user
     * @param email - The email address of the user
     */
    public OneUser(String id, String username, String password, String role, String firstName, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstName != null ? firstName : "";
        this.lastName = lastName != null ? lastName : "";
        this.email = email != null ? email : "";
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the user.
     *
     * @return The unique ID of the user.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the user.
     *
     * @param id The unique ID of the user.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username used for authentication.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username used for authentication.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return The encoded password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The encoded password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role assigned to the user.
     *
     * @return The role of the user.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role assigned to the user.
     *
     * @param role The role of the user.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the first name of the user.
     *
     * @return The first name of the user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName The first name of the user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the user.
     *
     * @return The last name of the user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName The last name of the user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the list of projects associated with the user.
     *
     * @return The list of project IDs or names associated with the user.
     */
    public List<String> getProjects() {
        return projects;
    }

    /**
     * Sets the list of projects associated with the user.
     *
     * @param projects The list of project IDs or names associated with the user.
     */
    public void setProjects(List<String> projects) {
        this.projects = projects;
    }
}
