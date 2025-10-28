package com.travelmanagement.util;

import java.io.InputStream;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

	private static final String USERNAME;
	private static final String PASSWORD;

	static {
		try {
			Properties props = new Properties();
			try (InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
				props.load(is);
			}
			USERNAME = props.getProperty("email.username");
			PASSWORD = props.getProperty("email.password");
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize EmailUtil", e);
		}
	}

	public static boolean sendOTP(String toEmail, String otp) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, PASSWORD);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USERNAME));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject("Your OTP for TravelMate Registration");
			message.setText("Your OTP is: " + otp);
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
}
