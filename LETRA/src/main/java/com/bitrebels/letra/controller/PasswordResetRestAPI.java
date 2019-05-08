package com.bitrebels.letra.controller;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bitrebels.letra.message.request.RecoveryEmailForm;
import com.bitrebels.letra.message.request.ResetForm;
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

	// Process form submission from forgotPassword page
	@PostMapping("/forgot")
//	public ResponseEntity<?> processForgotPasswordForm(@RequestParam("email") String userEmail,
	public ResponseEntity<?> processForgotPasswordForm(@Valid @RequestBody RecoveryEmailForm useremail,
			HttpServletRequest request) {

		// Lookup user in database by e-mail
		Optional<User> optional = userService.findUserByEmail(useremail.getEmail());

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
			passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl + ":4200/reset?token="
					+ user.getResetToken());


			emailService.sendEmail(passwordResetEmail);

			// Add success message to view
			return new ResponseEntity<>(new ResponseMessage("A password reset link has been sent to " + useremail.getEmail()), HttpStatus.OK);
		}

	}


	// Process reset password form
	@RequestMapping(value = "/reset", method = RequestMethod.POST)
//	public ResponseEntity<?> setNewPassword(@RequestParam Map<String, String> requestParams) {
	public ResponseEntity<?> setNewPassword(@Valid @RequestBody ResetForm resetform) {

		// Find the user associated with the reset token
//		Optional<User> user = userService.findUserByResetToken(requestParams.get("token"));
		Optional<User> user = userService.findUserByResetToken(resetform.getToken());

		// This should always be non-null but we check just in case
		if (user.isPresent()) {

			User resetUser = user.get();

			// Set new password
//			resetUser.setPassword(bCryptPasswordEncoder.encode(requestParams.get("password")));
			resetUser.setPassword(bCryptPasswordEncoder.encode(resetform.getPassword()));

			// Set the reset token to null so it cannot be used again
			resetUser.setResetToken(null);

			// Save user
			userService.save(resetUser);

			// In order to set a model attribute on a redirect, we must use
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
