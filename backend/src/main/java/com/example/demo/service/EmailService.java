package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.utils.Constants;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {

	public static final String ACTIVATION_TITLE = "Bank account activation";
	public static final String ACTIVATION_TEXT = "Dear %s %s, \n\n"
			+ "In order to activate your bank account, please click the following link: \n"
			+ Constants.BACKEND + "/auth/activate/%s\n"
			+ "We advise you to change your password the first time you sign in.\n"
			+ "Initial password: %s\n\n"
			+ "Best regards, \n"
			+ "Your banking system.";

	private final JavaMailSenderImpl sender;
	
	@Async
	public void sendEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		this.sender.send(message);
	}
	
}
