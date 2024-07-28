package com.islandpower.configurator.Controller;

import com.islandpower.configurator.Model.OneUser;
import com.islandpower.configurator.Service.OneUserDetailService;
import com.islandpower.configurator.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private OneUserDetailService userServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public OneUser registerUser (@RequestBody OneUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userServices.saveUser(user);
    }

    @GetMapping("/getAll")
    public Iterable<OneUser> getAllUsers() {
        return userServices.getAll();
    }

    @PutMapping(value="/update/{id}")
    public String updateUser(@PathVariable("id") String userId, @RequestBody OneUser updatedOneUser) {
        updatedOneUser.setId(userId);
        userServices.saveUser(updatedOneUser);
        return userId;
    }

    @DeleteMapping(value="/delete/{id}")
    public void deleteUser(@PathVariable("id") String userId) {
        userServices.deleteById(userId);
    }

    @RequestMapping(value="/user/{id}")
    public OneUser getUserById(@PathVariable("id") String userId) {
        return userServices.getUserById(userId);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String username, @RequestParam String password) {
        if (userServices.authenticateUser(username, password)) {
            UserDetails userDetails = userServices.loadUserByUsername(username);
            String jwt = jwtUtil.generateToken(userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("jwt", jwt);
            response.put("userId", userServices.getUserByUsername(username).get().getId());
            response.put("role", userServices.getUserByUsername(username).get().getRole());

            return ResponseEntity.ok(response);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }
}
