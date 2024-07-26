package com.islandpower.configurator.Service;

import com.islandpower.configurator.Model.MyUser;
import com.islandpower.configurator.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    public MyUser saveUser(MyUser myUser) {
        String encodedPassword =(myUser.getPassword());
        myUser.setPassword(encodedPassword);
        userRepository.save(myUser);
        return myUser;
    }

    public Iterable<MyUser> getAll() {
        return userRepository.findAll();
    }

    public void deleteById(String userId) {
        userRepository.deleteById(userId);
    }

    public MyUser getUserById(String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean authenticateUser(String userId, String rawPassword) {
        MyUser myUser = userRepository.findById(userId).orElse(null);
        if (myUser != null) {
            //return passwordEncoder.matches(rawPassword, myUser.getPassword());
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = userRepository.findByUsername(username);
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

    private String[] getRoles(MyUser user) {
        if (user.getRole() == null) {
            return new String[]{"USER"};
        }
        return user.getRole().split(",");
    }
}
