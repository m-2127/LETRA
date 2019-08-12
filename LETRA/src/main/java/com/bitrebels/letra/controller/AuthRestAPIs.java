package com.bitrebels.letra.controller;

import com.bitrebels.letra.message.request.GoogleLogin;
import com.bitrebels.letra.message.request.LoginForm;
import com.bitrebels.letra.message.response.JwtResponse;
import com.bitrebels.letra.security.jwt.JwtProvider;
import com.bitrebels.letra.services.UserDetailsServiceImpl;
import com.bitrebels.letra.services.UserPrinciple;
import com.bitrebels.letra.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtProvider jwtProvider;

	@Autowired
	UserService userService;

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtProvider.generateJwtToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
	}

	@PostMapping("/google")
	public ResponseEntity<?> googleLogin(@Valid @RequestBody GoogleLogin googleLogin) {


		UserPrinciple userPrinciple = (UserPrinciple) userDetailsServiceImpl.loadUserByUsername(googleLogin.getEmail());



		String jwt = jwtProvider.generateJwtToken(userPrinciple);

		return ResponseEntity.ok(new JwtResponse(jwt, userPrinciple.getUsername(), userPrinciple.getAuthorities()));
	}
}