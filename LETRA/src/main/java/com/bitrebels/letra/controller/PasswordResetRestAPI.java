package com.bitrebels.letra.controller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bitrebels.letra.message.response.ResponseMessage;
import com.bitrebels.letra.model.User;
import com.bitrebels.letra.services.EmailService;
import com.bitrebels.letra.services.UserService;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetRestAPI {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// Display forgotPassword page
	@GetMapping("/forgot")
	public ResponseEntity<?> displayForgotPasswordPage() {
	//	return new ModelAndView("forgotPassword");
		return new ResponseEntity<>(new ResponseMessage("Forgot Password"), HttpStatus.OK);
	}

	// Process form submission from forgotPassword page
	@PostMapping("/forgot")
	public ResponseEntity<?> processForgotPasswordForm(@RequestParam("email") String userEmail,
			HttpServletRequest request) {

		// Lookup user in database by e-mail
		Optional<User> optional = userService.findUserByEmail(userEmail);

		if (!optional.isPresent()) {
			return new ResponseEntity<>(new ResponseMessage("We didn't find an account for that e-mail address."), HttpStatus.BAD_REQUEST);
		} else {

			// Generate random 36-character string token for reset password
			User user = optional.get();
			user.setResetToken(UUID.randomUUID().toString());

			// Save token to database
			userService.save(user);

			String appUrl = request.getScheme() + "://" + request.getServerName();

			// Email message
			SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
			passwordResetEmail.setFrom("letramazarin@gmail.com");
			passwordResetEmail.setTo(user.getEmail());
			passwordResetEmail.setSubject("Password Reset Request");
			passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl + "/api/auth/reset?token="
					+ user.getResetToken());

			emailService.sendEmail(passwordResetEmail);

			// Add success message to view
			return new ResponseEntity<>(new ResponseMessage("A password reset link has been sent to " + userEmail), HttpStatus.OK);
		}

	}

	// Display form to reset password
	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public ResponseEntity<?> displayResetPasswordPage(@RequestParam("token") String token) {

		Optional<User> user = userService.findUserByResetToken(token);

		if (user.isPresent()) { // Token found in DB
			return new ResponseEntity<>(new ResponseMessage(token), HttpStatus.OK);
		} else { // Token not found in DB
			return new ResponseEntity<>(new ResponseMessage("Oops!  This is an invalid password reset link."), HttpStatus.BAD_REQUEST);
		}
	}

	// Process reset password form
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public ResponseEntity<?> setNewPassword(@RequestParam Map<String, String> requestParams) {

		// Find the user associated with the reset token
		Optional<User> user = userService.findUserByResetToken(requestParams.get("token"));

		// This should always be non-null but we check just in case
		if (user.isPresent()) {

			User resetUser = user.get();

			// Set new password
			resetUser.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));

			// Set the reset token to null so it cannot be used again
			resetUser.setResetToken(null);

			// Save user
			userService.save(resetUser);

			// In order to set a model attribute on a redirect, we must use
			// RedirectAttributes
			return new ResponseEntity<>(new ResponseMessage("You have successfully reset your password.  You may now login"), HttpStatus.OK);

		} else {
			return new ResponseEntity<>(new ResponseMessage("Oops!  This is an invalid password reset link."), HttpStatus.BAD_REQUEST);
		}
	}

	// Going to reset page without a token redirects to login page
//	@ExceptionHandler(MissingServletRequestParameterException.class)
//	public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {
//		return new ModelAndView("redirect:login");
//	}
}
