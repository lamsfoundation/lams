package org.lamsfoundation.lams.util;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

public class XMPPUtil {

    private static final Logger logger = Logger.getLogger(XMPPUtil.class);

    /**
     * Creates a new id on the xmpp server.
     * 
     * @param user
     * @return id if user is successfully created, otherwise null.
     */
    public static String createId(UserDTO user) {
	try {
	    XMPPConnection con = new XMPPConnection(Configuration.get(ConfigurationKeys.XMPP_DOMAIN));

	    AccountManager manager = con.getAccountManager();
	    if (manager.supportsAccountCreation()) {
		// using the lams userId as xmpp username and password.
		manager.createAccount(user.getUserID().toString(), user.getUserID().toString());
	    }
	    

	} catch (XMPPException e) {
	    if (e.getXMPPError().getCode() == 409) { // log and continue
		if (logger.isDebugEnabled()) {
	    	    logger.debug(e + " : XMPP account with id = " + user.getUserID() + " already exists");
		}
	    }else{
	    	logger.error(e);
	    	return null;
	    }
	}
	return user.getUserID() + "@" + Configuration.get(ConfigurationKeys.XMPP_DOMAIN);	
    }

    /**
     * Creates a MUC room called on the xmpp server.
     * 
     * @param room
     * @return Returns true if MUC room successfully created.
     */
    public static boolean createMultiUserChat(String room) {
	try {
	    XMPPConnection.DEBUG_ENABLED = false;
	    XMPPConnection con = new XMPPConnection(Configuration.get(ConfigurationKeys.XMPP_DOMAIN));

	    con.login(Configuration.get(ConfigurationKeys.XMPP_ADMIN), Configuration
		    .get(ConfigurationKeys.XMPP_PASSWORD));

	    MultiUserChat muc = new MultiUserChat(con, room);

	    // Create the room
	    muc.create("nick");

	    // Get the the room's configuration form
	    Form form = muc.getConfigurationForm();

	    // Create a new form to submit based on the original form
	    Form submitForm = form.createAnswerForm();

	    // Add default answers to the form to submit
	    for (Iterator fields = form.getFields(); fields.hasNext();) {
		FormField field = (FormField) fields.next();
		if (!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
		    // Sets the default value as the answer
		    submitForm.setDefaultAnswer(field.getVariable());
		}
	    }

	    // Sets the new owner of the room
	    submitForm.setAnswer("muc#roomconfig_persistentroom", true);
	    // Send the completed form (with default values) to the server to
	    // configure the room
	    muc.sendConfigurationForm(submitForm);

	    con.close();

	    return true;

	} catch (XMPPException e) {
		// if we get a connection refused exception but the server is not configured correctly, just debug print
		if (e.getXMPPError() != null && e.getXMPPError().getCode() == 502 && (
				Configuration.get(ConfigurationKeys.XMPP_DOMAIN).compareTo("") == 0 ||
				Configuration.get(ConfigurationKeys.XMPP_ADMIN).compareTo("") == 0 ||
				Configuration.get(ConfigurationKeys.XMPP_PASSWORD).compareTo("") == 0)) {
			logger.debug(e);
		}
		// if we are recreating the same room (no error code)
		else if(e.getMessage().compareTo("Creation failed - Missing acknowledge of room creation.") == 0){
			logger.debug(e);
		}
		// otherwise, we've got a problem
		else{
			logger.error(e);
		}

	    return false;
	}catch (Exception e) {
		logger.error(e);
		return false;
	}
    }

}
