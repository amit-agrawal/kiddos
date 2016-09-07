package com.hmi.kiddos.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

	/*
	 * private @Value("${email.host}") String host;
	 * private @Value("${email.protocol}") String protocol;
	 * private @Value("${email.port}") String port;
	 */ 
	private String username;
	private String password;
	private String notify;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNotify() {
		return notify;
	}

	public void setNotify(String notify) {
		this.notify = notify;
	}

	public void sendGmail(String className, String methodName, String args) {
		Logger.getLogger(MailUtil.class)
				.info(String.format("Sending mail for %s %s with user %s ", className, methodName, username));

		className = className.replaceAll("Controller", "");
		Properties props = new Properties();

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notify));
			message.setSubject(String.format("Kiddos: %s %sd", className, methodName));
			message.setText(String.format("%s %sd: %s", className, methodName, args));

			Transport.send(message);

			System.out.println("Sent Mail");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
