package com.islandpower.configurator.service;

import com.islandpower.configurator.Model.OneUser;
import com.islandpower.configurator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OneUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    public OneUser saveUser(OneUser oneUser) {
        String encodedPassword =(oneUser.getPassword());
        oneUser.setPassword(encodedPassword);
        userRepository.save(oneUser);
        return oneUser;
    }

    public Iterable<OneUser> getAll() {
        return userRepository.findAll();
    }

    public void deleteById(String userId) {
        userRepository.deleteById(userId);
    }

    public OneUser getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public Optional<OneUser> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticateUser(String username, String rawPassword) {
        Optional<OneUser> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            OneUser user = optionalUser.get();
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<OneUser> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    private String[] getRoles(OneUser user) {
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
}
