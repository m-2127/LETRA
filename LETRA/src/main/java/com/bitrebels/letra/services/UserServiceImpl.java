package com.bitrebels.letra.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bitrebels.letra.model.User;
import com.bitrebels.letra.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Optional findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public Optional findUserByResetToken(String resetToken) {
		return userRepository.findByResetToken(resetToken);
	}

	@Override
	public void save(User user) {
		userRepository.save(user);
	}
}


