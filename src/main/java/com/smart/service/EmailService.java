package com.smart.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

	// This is responsible to send email....
	public  static boolean sendEmail(String message, String subject, String to) {

		boolean status = false;
		// Variable for Gmail Host
		String host = "smtp.gmail.com";

		Properties properties = System.getProperties();
		System.out.println("PROPERTIES " + properties);

		// Setting important Information to properties object

		// host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Step 1: to get the session object...
		Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("Pratikkayte96@gmail.com", "wasv cffy jefc zhal");
			}
		});

		session.setDebug(true);

		// Step 2: Compose the message [text/multi media]

		MimeMessage mimeMessage = new MimeMessage(session);

		try {
			// From email
			mimeMessage.setFrom("Pratikkayte96@gmail.com");

			// adding recipient to message
			mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// adding subject to message
			mimeMessage.setSubject(subject);

			// adding text to message
			//mimeMessage.setText(message);
			mimeMessage.setContent(message, "text/html; charset=utf-8");


			// send message

			// Step 3: Send the message using Transport class
			Transport.send(mimeMessage);

			System.out.println("Sent success");
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;

	}

}
