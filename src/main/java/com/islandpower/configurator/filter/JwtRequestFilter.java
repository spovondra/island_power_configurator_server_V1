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
 * Filter to validate the JWT token and set the authentication in the security context.
 * <p>
 * This filter is executed once per request to ensure the security context is populated
 * with authentication details if the JWT token is valid.
 * </p>
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
     * @param request - the incoming HTTP request
     * @param response- the outgoing HTTP response
     * @param chain - the filter chain
     * @throws ServletException - if any error occurs during request processing
     * @throws IOException - if any I/O error occurs
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        String jwt = extractJwtFromRequest(request);

        if (jwt != null) {
            try {
                String username = jwtUtil.extractUsername(jwt); // Extract username from JWT
                processAuthentication(request, jwt, username);
            } catch (TokenExpiredException e) {
                handleJwtError(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired. Please log in again.");
                return; // stop further processing
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
     * @param request - the HTTP request
     * @return - the JWT token if present, otherwise null
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Extract JWT token
        }
        return null;
    }

    /**
     * Processes authentication by validating the token and setting the security context if valid.
     *
     * @param request - the HTTP request
     * @param jwt - the JWT token
     * @param username - the username extracted from the token
     */
    private void processAuthentication(HttpServletRequest request, String jwt, String username) {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Load user details

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
     * @param response - the HTTP response
     * @param httpStatus - the HTTP status code
     * @param message - the error message
     * @throws IOException - if an error occurs while writing the response
     */
    private void handleJwtError(HttpServletResponse response, int httpStatus, String message) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
