package org.lamsfoundation.lams.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;

/**
 * A class that handles emails
 *
 * @author lfoxton
 */
public class Emailer {
    /**
     * Sends an email sourced from support email
     *
     * @param subject
     *            the subject of the email
     * @param to
     *            the email of the recipient
     * @param body
     *            the body of the email
     * @param isHtmlFormat
     *            whether the message is of HTML content-type or plain text
     */
    public static void sendFromSupportEmail(String subject, String to, String body, boolean isHtmlFormat)
	    throws AddressException, MessagingException, UnsupportedEncodingException {
	Emailer.sendFromSupportEmail(subject, to, body, isHtmlFormat, null);
    }

    public static void sendFromSupportEmail(String subject, String to, String body, boolean isHtmlFormat,
	    String attachmentFilename) throws AddressException, MessagingException, UnsupportedEncodingException {
	String supportEmail = Configuration.get(ConfigurationKeys.LAMS_ADMIN_EMAIL);
	Emailer.send(subject, to, "", supportEmail, "", body, isHtmlFormat, attachmentFilename);
    }

    /**
     * Creates a mail session with authentication if it is required, ie if it has been set up with SMTP authentication
     * in the config page
     *
     * @param properties
     * @return
     */
    public static Session getMailSession() {
	String smtpServer = Configuration.get(ConfigurationKeys.SMTP_SERVER);
	String smtpPort = Configuration.get(ConfigurationKeys.SMTP_PORT);
	Properties properties = new Properties();
	properties.put("mail.smtp.host", smtpServer);
	properties.put("mail.smtp.port", smtpPort);

	String smtpAuthUser = Configuration.get(ConfigurationKeys.SMTP_AUTH_USER);
	String smtpAuthPass = Configuration.get(ConfigurationKeys.SMTP_AUTH_PASSWORD);
	String smtpAuthSecurity = Configuration.get(ConfigurationKeys.SMTP_AUTH_SECURITY);
	Session session = null;
	if (StringUtils.isBlank(smtpAuthUser)) {
	    session = Session.getInstance(properties);
	} else {
	    properties.setProperty("mail.smtp.submitter", smtpAuthUser);
	    properties.setProperty("mail.smtp.auth", "true");

	    if (smtpAuthSecurity.equals("starttls")) {
		properties.put("mail.smtp.starttls.enable", "true");
	    } else if (smtpAuthSecurity.equals("ssl")) {
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    }
	    SMTPAuthenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPass);
	    session = Session.getInstance(properties, auth);
	}
	return session;
    }

    /**
     * Send email to recipients
     *
     * @param subject
     *            the subject of the email
     * @param to
     *            the email of the recipient
     * @param toPerson
     *            receiver's name
     * @param from
     *            the email to source the email from
     * @param fromPerson
     *            sender's name
     * @param body
     *            the body of the email
     * @param isHtmlFormat
     *            whether the message is of HTML content-type or plain text
     */
    public static void send(String subject, String to, String toPerson, String from, String fromPerson, String body,
	    boolean isHtmlFormat) throws AddressException, MessagingException, UnsupportedEncodingException {

	Emailer.send(subject, to, toPerson, from, fromPerson, body, isHtmlFormat, null);
    }

    /**
     * Send email to recipients
     *
     * @param subject
     *            the subject of the email
     * @param to
     *            the email of the recipient
     * @param toPerson
     *            receiver's name
     * @param from
     *            the email to source the email from
     * @param fromPerson
     *            sender's name
     * @param body
     *            the body of the email
     * @param isHtmlFormat
     *            whether the message is of HTML content-type or plain text
     * @param file
     *            file to attach
     */
    public static void send(String subject, String to, String toPerson, String from, String fromPerson, String body,
	    boolean isHtmlFormat, String filename)
	    throws AddressException, MessagingException, UnsupportedEncodingException {

	Session session = Emailer.getMailSession();

	MimeMessage message = new MimeMessage(session);
	message.setFrom(new InternetAddress(from, fromPerson));
	message.addRecipient(RecipientType.TO, new InternetAddress(to, toPerson));
	message.setSubject(subject, "UTF-8");
	message.setSentDate(new Date());

	if (filename == null) {
	    message.setText(body, "UTF-8");
	    String contentType = (isHtmlFormat) ? "text/html;charset=UTF-8" : "text/plain;charset=UTF-8";
	    message.addHeader("Content-Type", contentType);
	} else {
	    MimeBodyPart msgPart = new MimeBodyPart();
	    msgPart.setText(body, "UTF-8");
	    String contentType = (isHtmlFormat) ? "text/html;charset=UTF-8" : "text/plain;charset=UTF-8";
	    msgPart.addHeader("Content-Type", contentType);

	    MimeBodyPart filePart = new MimeBodyPart();
	    FileDataSource fds = new FileDataSource(filename);
	    filePart.setDataHandler(new DataHandler(fds));
	    String encodedFilename = MimeUtility.encodeText(fds.getName());
	    filePart.setFileName(encodedFilename);

	    Multipart mp = new MimeMultipart();
	    mp.addBodyPart(msgPart);
	    mp.addBodyPart(filePart);
	    message.setContent(mp);

	}
	Transport.send(message);
    }
}