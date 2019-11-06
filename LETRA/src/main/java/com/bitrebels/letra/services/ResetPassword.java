package com.bitrebels.letra.services;

import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.User;
import com.bitrebels.letra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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



    public ResponseEntity<?> setNewPassword(String password, User user) {

        boolean passmatch = passwordEncoder().matches(password,user.getPassword());

        if(passmatch){
            user.setPassword(passwordEncoder().encode(password));
            userService.save(user);

            return new ResponseEntity<>(new ResponseMessage("Password changed successfully"), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ResponseMessage("The old password did not match"), HttpStatus.OK);
        }

    }
}
