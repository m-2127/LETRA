package com.bitrebels.letra.services;

import java.util.Optional;
import com.bitrebels.letra.model.User;

public interface UserService {
	public Optional<User> findUserByEmail(String email);
    public Optional<User> findUserByResetToken(String resetToken);
    public void save(User user);
}
