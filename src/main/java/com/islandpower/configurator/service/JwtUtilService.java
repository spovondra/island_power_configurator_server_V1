package com.islandpower.configurator.service;

import com.islandpower.configurator.model.OneUser;
import com.islandpower.configurator.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for handling operations related to JWT (JSON Web Token) and user details.
 * Provides methods for extracting the username from the token, retrieving the user ID, and token validation.
 */
@Service
public class JwtUtilService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OneUserDetailService userDetailsService;

    /**
     * Extracts the username from the JWT token present in the HTTP request's Authorization header.
     *
     * @param request the HTTP servlet request containing the Authorization header
     * @return String the extracted username from the token
     * @throws RuntimeException if no token is found in the request header
     */
    public String extractUsernameFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7); //extract token by removing "Bearer" prefix
            return jwtUtil.extractUsername(token); //extract the username from the token
        }
        throw new RuntimeException("No token found in request");
    }

    /**
     * Retrieves the user ID associated with the given username.
     *
     * @param username The username for which to retrieve the user ID
     * @return String The user ID associated with the username
     * @throws RuntimeException If no user is found for the given username
     */
    public String retrieveUserIdByUsername(String username) {
        /* fetch user details by username */
        Optional<OneUser> user = userDetailsService.getUserByUsername(username);
        if (user.isPresent()) {
            return user.get().getId(); //return the user ID (if the user exists)
        }
        throw new RuntimeException("User not found: " + username);
    }
}
