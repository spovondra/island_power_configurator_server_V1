package com.islandpower.configurator.Controller;

import com.islandpower.configurator.Model.MyUser;
import com.islandpower.configurator.Service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private MyUserDetailService userServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public MyUser registerUser (@RequestBody MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userServices.saveUser(user);
    }

    @GetMapping("/getAll")
    public Iterable<MyUser> getAllUsers() {
        return userServices.getAll();
    }

    @PutMapping(value="/edit/{id}")
    public String updateUser(@PathVariable("id") String userId, @RequestBody MyUser updatedMyUser) {
        updatedMyUser.setId(userId);
        userServices.saveUser(updatedMyUser);
        return userId;
    }

    @DeleteMapping(value="/delete/{id}")
    public void deleteUser(@PathVariable("id") String userId) {
        userServices.deleteById(userId);
    }

    @RequestMapping(value="user/{id}")
    public MyUser getUserById(@PathVariable("id") String userId) {
        return userServices.getUserById(userId);
    }

    @PostMapping("/authenticate")
    public boolean authenticateUser(@RequestParam String userId, @RequestParam String password) {
        return userServices.authenticateUser(userId, password);
    }
}
