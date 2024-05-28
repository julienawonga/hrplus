package com.example.hrplus.service;


import com.example.hrplus.model.User;
import com.example.hrplus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public boolean isValidUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()){
            return user.get().getPassword().equals(password);
        } else {
            return false;
        }
    }

}
