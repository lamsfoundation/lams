package org.lamsfoundation.lams.events;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.Emailer;

/**
 * Allows sending mail from the configured mail server.
 * 
 * @author Marcin Cieslak
 * 
 */
public class DeliveryMethodMail extends AbstractDeliveryMethod {

    private static DeliveryMethodMail instance;
    protected static final EmailValidator emailValidator = EmailValidator.getInstance();
    private static final Logger log = Logger.getLogger(DeliveryMethodMail.class);

    protected DeliveryMethodMail() {
	super((short) 1, "MAIL", "Messages will be send by Mail");
    }

    @Override
    public String send(Integer fromUserId, Integer toUserId, String subject, String message, boolean isHtmlFormat)
	    throws InvalidParameterException {
	if (toUserId == null) {
	    log.error("Target user ID must not be null.");
	}
	try {
	    User toUser = (User) EventNotificationService.getInstance().getUserManagementService()
		    .findById(User.class, toUserId);
	    if (toUser == null) {
		log.error("Target user with ID " + toUserId + " was not found.");
	    }
	    String toEmail = toUser.getEmail();
	    if (!DeliveryMethodMail.emailValidator.isValid(toEmail)) {
		return "Target user's e-mail address is invalid.";
	    }

	    if (fromUserId == null) {
		Emailer.sendFromSupportEmail(subject, toEmail, message, isHtmlFormat);
	    } else {
		User fromUser = (User) EventNotificationService.getInstance().getUserManagementService()
			.findById(User.class, fromUserId);
		if (fromUser == null) {
		    log.error("Source user with ID " + fromUserId + " was not found.");
		}
		String fromEmail = fromUser.getEmail();
		if (!DeliveryMethodMail.emailValidator.isValid(fromEmail)) {
		    return "Source user's e-mail address is invalid.";
		}

		Emailer.send(subject, toEmail, fromEmail, message, isHtmlFormat, new Properties());
	    }
	    return null;
	} catch (Exception e) {
	    return e.toString();
	}
    }

    public static DeliveryMethodMail getInstance() {
	if (DeliveryMethodMail.instance == null) {
	    DeliveryMethodMail.instance = new DeliveryMethodMail();
	}
	return DeliveryMethodMail.instance;
    }

    /**
     * Sends an email to the address provided by the admin.
     * 
     * @param subject
     *            subject of the message
     * @param body
     *            text of the message
     * 
     * @param isHtmlFormat
     *            whether the message is of HTML content-type or plain text
     * @throws UnsupportedEncodingException
     * @throws AddressException
     *             if address was incorrect
     * @throws MessagingException
     *             if the operation failed
     */
    void notifyAdmin(String subject, String body, boolean isHtmlFormat) throws AddressException,
	    UnsupportedEncodingException, MessagingException {
	String adminEmail = Configuration.get(ConfigurationKeys.LAMS_ADMIN_EMAIL);
	if (StringUtils.isEmpty(adminEmail)) {
	    DeliveryMethodMail.log.warn("Could not notify admin as his email is blank. The subject: " + subject
		    + ". The message: " + body);
	} else {
	    Emailer.sendFromSupportEmail(subject, adminEmail, body, isHtmlFormat);
	}
    }
}