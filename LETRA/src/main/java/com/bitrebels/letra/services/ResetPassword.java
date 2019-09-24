package com.bitrebels.letra.services;

import com.bitrebels.letra.model.User;
import com.bitrebels.letra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResetPassword {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepo;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    public void setNewPassword(String password, User user) {
        user.setPassword(passwordEncoder().encode(password));
            userService.save(user);
    }
}
