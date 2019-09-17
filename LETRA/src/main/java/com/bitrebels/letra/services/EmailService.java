package com.bitrebels.letra.services;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
	 void sendEmail(SimpleMailMessage email);
}
