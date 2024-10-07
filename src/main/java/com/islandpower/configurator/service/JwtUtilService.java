package com.islandpower.configurator.service;

import com.islandpower.configurator.model.OneUser;
import com.islandpower.configurator.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUtilService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OneUserDetailService userDetailsService;

    public String extractUsernameFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUsername(token);
        }
        throw new RuntimeException("No token found in request");
    }

    public String retrieveUserIdByUsername(String username) {
        Optional<OneUser> user = userDetailsService.getUserByUsername(username);
        if (user.isPresent()) {
            return user.get().getId();
        }
        throw new RuntimeException("User not found: " + username);
    }
}
