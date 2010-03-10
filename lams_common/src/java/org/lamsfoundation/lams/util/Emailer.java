package org.lamsfoundation.lams.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.masukomi.aspirin.core.MailQue;

/**
 * A class that handles emails
 * 
 * @author lfoxton
 */
public class Emailer {

	public Emailer() {
	}

	/**
	 * Sends an email sourced from support email
	 * 
	 * @param subject
	 *                the subject of the email
	 * @param to
	 *                the email of the recipient
	 * @param body
	 *                the body of the email
	 */
	public static void sendFromSupportEmail(String subject, String to, String body) throws AddressException,
	MessagingException, UnsupportedEncodingException {
		String supportEmail = Configuration.get(ConfigurationKeys.LAMS_ADMIN_EMAIL);
		Properties properties = new Properties();
		send(subject, to, supportEmail, body, properties);
	}

	/**
	 * Creates a mail session with authentication if it is required, ie if it
	 * has been set up with SMTP authentication in the config page
	 * 
	 * @param properties
	 * @return
	 */
	public static Session getMailSession(Properties properties) {
		Session session;
		boolean useInternalSMTPServer = Boolean.parseBoolean(Configuration.get(ConfigurationKeys.USE_INTERNAL_SMTP_SERVER));
		if (! useInternalSMTPServer) {
			String smtpServer = Configuration.get(ConfigurationKeys.SMTP_SERVER);
			properties.put("mail.smtp.host", smtpServer);
		}

		String smtpAuthUser = Configuration.get(ConfigurationKeys.SMTP_AUTH_USER);
		String smtpAuthPass = Configuration.get(ConfigurationKeys.SMTP_AUTH_PASSWORD);
		if (!useInternalSMTPServer && (smtpAuthUser != null) && !smtpAuthUser.trim().equals("")) {
			properties.setProperty("mail.smtp.submitter", smtpAuthUser);
			properties.setProperty("mail.smtp.auth", "true");
			SMTPAuthenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPass);
			session = Session.getInstance(properties, auth);
		} else {
			session = Session.getInstance(properties);
		}
		return session;
	}

	/**
	 * Send email to recipients
	 * 
	 * @param subject
	 *                the subject of the email
	 * @param to
	 *                the email of the recipient
	 * @param from
	 *                the email to source the email from
	 * @param body
	 *                the body of the email
	 */
	public static void send(String subject, String to, String from, String body, Properties mailServerConfig)
	throws AddressException, MessagingException, UnsupportedEncodingException {

		send(subject, to, "", from, "", body, mailServerConfig);
		

	}

	/**
	 * Send email to recipients
	 * 
	 * @param subject
	 *                the subject of the email
	 * @param to
	 *                the email of the recipient
	 * @param toPerson
	 *                receiver's name
	 * @param from
	 *                the email to source the email from
	 * @param fromPerson
	 *                sender's name
	 * @param body
	 *                the body of the email
	 */
	public static void send(String subject, String to, String toPerson, String from, String fromPerson, String body, Properties mailServerConfig)
	throws AddressException, MessagingException, UnsupportedEncodingException {

		Session session = getMailSession(mailServerConfig);
		MimeMessage message = new MimeMessage(session);
		
		message.setFrom(new InternetAddress(from, fromPerson));
		message.addRecipient(RecipientType.TO, new InternetAddress(to, toPerson));
		message.setSubject(subject);
		message.setText(body, "text/plain");

		boolean useInternalSMTPServer = Boolean.parseBoolean(Configuration.get(ConfigurationKeys.USE_INTERNAL_SMTP_SERVER));
		if (useInternalSMTPServer) {
			MailQue myMailQue = new MailQue();
			myMailQue.queMail(message);	    
		} else {
			Transport.send(message);    
		}

	} 
	


}
