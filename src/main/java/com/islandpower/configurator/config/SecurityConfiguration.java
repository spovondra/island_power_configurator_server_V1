package com.islandpower.configurator.config;

import com.islandpower.configurator.Service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private MyUserDetailService userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/api/map/**", "/api/auth/register/**").permitAll();
                    registry.requestMatchers("/api/components/**").hasRole("USER");
                    registry.requestMatchers("/api/**").hasRole("ADMIN");
        })
            .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
            .build();
    }

    /*
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails normalUser = User.builder()
                .username("user")
                .password("$2a$12$3Z387odK.i4GTPADgSOxduMZzEKdMSxPBmeFsKF4mii4YCKe1n5z6") //$2a$12$3Z387odK.i4GTPADgSOxduMZzEKdMSxPBmeFsKF4mii4YCKe1n5z6
                .roles("USER")
                .build();

        UserDetails adminUser = User.builder()

                .username("admin")
                .password("$2a$12$3Z387odK.i4GTPADgSOxduMZzEKdMSxPBmeFsKF4mii4YCKe1n5z6") //$2a$12$3Z387odK.i4GTPADgSOxduMZzEKdMSxPBmeFsKF4mii4YCKe1n5z6
                .roles("ADMIN", "USER")
                .build();

        return new InMemoryUserDetailsManager(normalUser, adminUser);
    }
    */
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailService;
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}