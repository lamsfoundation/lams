package org.lamsfoundation.lams.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.lamsfoundation.lams.integration.service.IntegrationService;
import org.lamsfoundation.lams.util.Configuration;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;

/**
 * A class that handles emails, used for forgot password task
 * @author lfoxton
 */
public class Emailer {

	private static Logger log = Logger.getLogger(Emailer.class);
	
	public Emailer() {}
	

	/**
	 * Sends an email sourced from admin
	 * @param subject the subject of the email
	 * @param to the email of the recipient
	 * @param body the body of the email
	 */
	public static void sendFromSupportEmail(String subject, String to, String body)
							throws AddressException, MessagingException
	{
		String supportEmail = Configuration.get("LamsSupportEmail");
		String smtpServer = Configuration.get("SMTPServer");
		Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpServer);
		send(subject, to, supportEmail, body, properties);
	}
	
	
	/**
	 * Send email to recipients
	 * @param subject the subject of the email
	 * @param to the email of the recipient
	 * @param from the email to source the email from
	 * @param body the body of the email
	 */
	public static void send(String subject, String to, String from, String body, Properties mailServerConfig) 
							throws AddressException, MessagingException
	{
		Session session = Session.getDefaultInstance(mailServerConfig, null);
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.addRecipient(RecipientType.TO, new InternetAddress(to));
		message.setSubject(subject);
		message.setText(body);
		Transport.send(message);
	}
	
	
}
