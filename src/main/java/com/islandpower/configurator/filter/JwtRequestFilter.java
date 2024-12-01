package com.islandpower.configurator.filter;

import com.islandpower.configurator.exceptions.TokenExpiredException;
import com.islandpower.configurator.service.OneUserDetailService;
import com.islandpower.configurator.util.JwtUtil;
import jakarta.servlet.FilterChain;
import org.springframework.lang.NonNull;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter to validate JWT tokens and set the authentication in the security context.
 * This filter intercepts incoming HTTP requests, extracts the JWT token, validates it,
 * and sets the authentication in the security context if valid. It ensures that the
 * application is protected by verifying the user's identity using the JWT.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private OneUserDetailService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Filters incoming HTTP requests, validates the JWT token if present, and sets the authentication in the security context.
     *
     * @param request The incoming HTTP request
     * @param response The outgoing HTTP response
     * @param chain The filter chain
     * @throws ServletException If an error occurs during request processing
     * @throws IOException If an I/O error occurs
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        /* Extract JWT from the Authorization header */
        String jwt = extractJwtFromRequest(request);

        if (jwt != null) {
            try {
                String username = jwtUtil.extractUsername(jwt); // extract username from JWT
                processAuthentication(request, jwt, username); // process authentication
            } catch (TokenExpiredException e) {
                handleJwtError(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired. Please log in again.");
                return; //sop further processing
            } catch (RuntimeException e) {
                handleJwtError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token.");
                return; // stop further processing
            }
        }

        chain.doFilter(request, response); // continue with the next filter in the chain
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request The HTTP request
     * @return The JWT token if present, otherwise null
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); //extract JWT token
        }
        return null;
    }

    /**
     * Processes authentication by validating the token and setting the security context if valid.
     *
     * @param request The HTTP request
     * @param jwt The JWT token
     * @param username The username extracted from the token
     */
    private void processAuthentication(HttpServletRequest request, String jwt, String username) {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            /*  validate the token and set the authentication in the context of security if it is valid */
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
    }

    /**
     * Handles JWT-related errors by setting the appropriate HTTP status and response message.
     *
     * @param response The HTTP response
     * @param httpStatus The HTTP status code
     * @param message The error message
     * @throws IOException If an error occurs while writing the response
     */
    private void handleJwtError(HttpServletResponse response, int httpStatus, String message) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
