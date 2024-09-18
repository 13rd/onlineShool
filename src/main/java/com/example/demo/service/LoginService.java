package com.example.demo.service;

import com.example.demo.model.UserModel;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean authenticate(String username, String password) {
        System.out.println(username + " " + password);

        Optional<UserModel> user = userRepository.findByUsername(username);
        System.out.println(user);
        if (user.isPresent() && bCryptPasswordEncoder.matches(password, user.get().getPassword())) {
            System.out.println("GOOD");
            return true;
        }
        return false;
    }
}
