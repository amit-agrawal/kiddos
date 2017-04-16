package com.hmi.kiddos.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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

		try {
			Session session = getMailSession();

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(notify));
			message.setSubject(String.format("Kiddos: %s %sd", className, methodName));

			message.setText(String.format("%s %sd: %s", className, methodName, args));

			Transport.send(message);

			Logger.getLogger(MailUtil.class).info("Sent Mail");
		} catch (MessagingException e) {
			Logger.getLogger(MailUtil.class).error("Error while sending mail", e);

			throw new RuntimeException(e);
		}
	}

	public void sendReceipt(String docPath, String[] mailIds) {
		Logger.getLogger(MailUtil.class).info(String.format("Sending receipt with user %s ", username));

		try {
			Session session = getMailSession();

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			StringBuilder recepients = new StringBuilder();
			if (notify != null && !notify.isEmpty())
				recepients.append(notify);
			for (String mailId : mailIds)
				if (mailId != null && !mailId.isEmpty())
					recepients.append(",").append(mailId);

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recepients.toString(), false));
			message.setSubject(String.format("Receipt for HMI payment"));

			if (docPath != null) {
				Logger.getLogger(MailUtil.class).info("Sending Mail with attachment");

				// Create the message part
				BodyPart messageBodyPart = new MimeBodyPart();

				// Now set the actual message
				messageBodyPart.setText(String.format("Receipt is attached."));

				// Create a multipar message
				Multipart multipart = new MimeMultipart();

				// Set text message part
				multipart.addBodyPart(messageBodyPart);

				// Part two is attachment
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(docPath);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(docPath.substring(2, docPath.length()));
				multipart.addBodyPart(messageBodyPart);

				// Send the complete message parts
				message.setContent(multipart);
				Transport.send(message);
			}

			Logger.getLogger(MailUtil.class).info("Sent Mail");
		} catch (MessagingException e) {
			Logger.getLogger(MailUtil.class).error("Error while sending mail", e);

			throw new RuntimeException(e);
		}
	}

	private Session getMailSession() {
		Properties props = gmailProperties();

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		return session;
	}

	private Properties gmailProperties() {
		Properties props = new Properties();

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		return props;
	}
}