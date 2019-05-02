package com.bitrebels.letra.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bitrebels.letra.message.request.RegistrationForm;
import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.Role;
import com.bitrebels.letra.model.RoleName;
import com.bitrebels.letra.model.User;
import com.bitrebels.letra.repository.RoleRepository;
import com.bitrebels.letra.repository.UserRepository;

@RestController
@RequestMapping("/api/hrm")
public class HRMRestAPI {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;
	
	@PostMapping("/registration")
	@PreAuthorize("hasRole('HRM')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationForm registrationRequest) {

		if (userRepository.existsByEmail(registrationRequest.getEmail())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Email is already in use!"),
					HttpStatus.BAD_REQUEST);
		}
		if (userRepository.existsByMobilenumber(registrationRequest.getMobilenumber())) {
			return new ResponseEntity<>(new ResponseMessage("Fail -> Mobile number is already in use!"),
					HttpStatus.BAD_REQUEST);
		}

		// Creating user's account
		User user = new User(registrationRequest.getName(), registrationRequest.getEmail(),
				encoder.encode(registrationRequest.getPassword()), registrationRequest.getMobilenumber(), 
				registrationRequest.getGender());

		Set<Role> roles = new HashSet<>();
		Role employeeRole = roleRepository.findByName(RoleName.ROLE_EMPLOYEE)
				.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
		roles.add(employeeRole);
		
		user.setRoles(roles);
		userRepository.save(user);

		return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
	}
}
