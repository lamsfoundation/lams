package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.Emailer;

/**
 * Allows sending mail from the configured mail server.
 * 
 * @author Marcin Cieslak
 * 
 */
public class DeliveryMethodMail extends AbstractDeliveryMethod {

    private static DeliveryMethodMail instance;

    protected DeliveryMethodMail() {
	super((short) 1, "MAIL", "Messages will be send by Mail");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String send(Long userId, String subject, String message) throws InvalidParameterException {
	if (userId == null) {
	    return "User ID should not be null.";
	}
	try {
	    User user = (User) EventNotificationService.getInstance().getUserManagementService().findById(User.class,
		    userId.intValue());
	    if (user == null) {
		return "User with the provided ID was not found.";
	    }
	    String email = user.getEmail();
	    if (StringUtils.isBlank(email)) {
		return "User's e-mail address is blank.";
	    }
	    sendFromSupportEmail(subject, email, message);
	    return null;
	} catch (Exception e) {
	    return e.getMessage();
	}
    }

    public static DeliveryMethodMail getInstance() {
	if (DeliveryMethodMail.instance == null) {
	    DeliveryMethodMail.instance = new DeliveryMethodMail();
	}
	return DeliveryMethodMail.instance;
    }

    /**
     * Sends an email sourced from admin. Copied from Emailer class.
     * 
     * @param subject
     *                subject of the message
     * @param to
     *                address to send
     * @param body
     *                text of the message
     * @throws AddressException
     *                 if address was incorrect
     * @throws MessagingException
     *                 if the operation failed
     */
    public void sendFromSupportEmail(String subject, String to, String body) throws AddressException,
	    MessagingException {
	Emailer.sendFromSupportEmail(subject, to, body);
    }

    /**
     * Sends an email to the address provided by the admin.
     * 
     * @param subject
     *                subject of the message
     * @param body
     *                text of the message
     * @throws AddressException
     *                 if address was incorrect
     * @throws MessagingException
     *                 if the operation failed
     */
    void notifyAdmin(String subject, String body) throws AddressException, MessagingException {
	String adminEmail = Configuration.get("LamsSupportEmail");
	if (!StringUtils.isEmpty(adminEmail)) {
	    sendFromSupportEmail(subject, adminEmail, body);
	}
    }
}