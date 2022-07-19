package org.lamsfoundation.lams.events;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
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
    private static final Logger log = Logger.getLogger(DeliveryMethodMail.class);

    private static DeliveryMethodMail instance = null;
    private static final EmailValidator emailValidator = EmailValidator.getInstance();

    private static IUserManagementService userManagementService = null;

    protected static DeliveryMethodMail getInstance() {
	if (DeliveryMethodMail.instance == null) {
	    DeliveryMethodMail.instance = new DeliveryMethodMail();
	}
	return DeliveryMethodMail.instance;
    }

    private DeliveryMethodMail() {
	super((short) 1);
    }

    @Override
    protected String send(Integer fromUserId, Integer toUserId, String subject, String message, boolean isHtmlFormat)
	    throws InvalidParameterException {
	return send(fromUserId, toUserId, subject, message, isHtmlFormat, null);
    }

    @Override
    protected String send(Integer fromUserId, Integer toUserId, String subject, String message, boolean isHtmlFormat,
	    String attachmentFilename) throws InvalidParameterException {
	try {
	    User toUser = (User) DeliveryMethodMail.userManagementService.findById(User.class, toUserId);
	    if (toUser == null) {
		return "Target user with ID " + toUserId + " was not found.";
	    }
	    String toEmail = toUser.getEmail();
	    if (!DeliveryMethodMail.emailValidator.isValid(toEmail)) {
		return "Target user's e-mail address is invalid.";
	    }

	    // keep fromUserId parameter for consistency with other delivery method signatures
	    // but ignore it as all emails are sent using the system account

	    Emailer.sendFromSupportEmail(subject, toEmail, message, isHtmlFormat, attachmentFilename);
	    return null;
	} catch (Exception e) {
	    String error = e.toString();
	    logError(error);
	    return error;
	}
    }

    @Override
    protected boolean lastOperationFailed(Subscription subscription) {
	return subscription.getLastOperationMessage() != null;
    }

    static void setUserManagementService(IUserManagementService userManagementService) {
	DeliveryMethodMail.userManagementService = userManagementService;
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
    void notifyAdmin(String subject, String body, boolean isHtmlFormat)
	    throws AddressException, UnsupportedEncodingException, MessagingException {
	String adminEmail = Configuration.get(ConfigurationKeys.LAMS_ADMIN_EMAIL);
	if (StringUtils.isBlank(adminEmail)) {
	    DeliveryMethodMail.log.warn(
		    "Could not notify admin as his email is blank. The subject: " + subject + ". The message: " + body);
	} else {
	    Emailer.sendFromSupportEmail(subject, adminEmail, body, isHtmlFormat, null);
	}
    }

    @Override
    protected Logger getLog() {
	return DeliveryMethodMail.log;
    }
}