package com.bitrebels.letra.services;

import java.util.Optional;
import com.bitrebels.letra.model.User;

public interface UserService {
	Optional<User> findUserByEmail(String email);
	Optional<User> findUserByResetToken(String resetToken);
    void save(User user);
    Long authenticatedUser();
}
