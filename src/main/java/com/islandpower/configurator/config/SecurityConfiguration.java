package com.islandpower.configurator.config;

import com.islandpower.configurator.filter.JwtRequestFilter;
import com.islandpower.configurator.service.OneUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for setting up Spring Security in the application.
 * This class configures authentication, authorization, and security filters.
 *
 * @version 1.0
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Service to load user data
     */
    @Autowired
    private OneUserDetailService userDetailService;

    /**
     * JWT filter for processing JWT tokens
     */
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * @param httpSecurity - The HttpSecurity object to configure
     * @return SecurityFilterChain - The configured security filter chain
     * @throws Exception - If an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
                .authorizeHttpRequests(registry -> {

                    // Public endpoints accessible to everyone
                    registry.requestMatchers(
                            "/api/location/**",
                            "/api/auth/register/**",
                            "/api/auth/login/**"
                    ).permitAll();

                    // Endpoints accessible to users with USER or ADMIN roles
                    registry.requestMatchers(
                            "/api/auth/user/{id}",
                            "/api/auth/update/{id}",
                            "/api/components/**",
                            "/api/calculations/**",
                            "/api/projects/**"
                    ).hasAnyRole("USER", "ADMIN");

                    // Endpoints accessible only to users with ADMIN role
                    registry.requestMatchers(
                            "/api/**"
                    ).hasRole("ADMIN");
                })
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter before the username-password authentication filter
                .httpBasic(Customizer.withDefaults()); // Enable HTTP Basic authentication
        return httpSecurity.build();
    }

    /**
     * Provides the UserDetailsService bean.
     *
     * @return UserDetailsService - The user details service
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailService; // Return the user detail service instance
    }

    /**
     * Provides the AuthenticationProvider bean.
     *
     * @return AuthenticationProvider - The authentication provider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService); // Set the user details service
        provider.setPasswordEncoder(passwordEncoder()); // Set the password encoder
        return provider; // Return the configured authentication provider
    }

    /**
     * Provides the PasswordEncoder bean.
     * <p>
     * This method configures a password encoder using BCrypt for hashing passwords,
     * ensuring that user passwords are securely hashed before storage.
     *
     * @return PasswordEncoder - The password encoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder(); // Create an instance of BCryptPasswordEncoder
        userDetailService.setPasswordEncoder(encoder); // Set the password encoder in the user detail service
        return encoder; // Return the configured password encoder
    }
}
