package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.usermanagement.ForgotPasswordRequest;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.Emailer;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Servlet to handle forgot password requests
 *
 * This servlet handles two type of requests, one to save the request and send an email to the user, and one to save the
 * new password
 *
 * @author lfoxton
 */
public class ForgotPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = -4833236166181290760L;
    private static Logger log = Logger.getLogger(ForgotPasswordServlet.class);

    @Autowired
    protected MessageService centralMessageService;
    @Autowired
    protected IUserManagementService userManagementService;

    // states
    private static String SMTP_SERVER_NOT_SET = "error.support.email.not.set";
    private static String PASSWORD_REQUEST_EXPIRED = "error.password.request.expired";
    private static String REQUEST_PROCESSED = "forgot.password.request.processed";
    private static String SUCCESS_CHANGE_PASS = "heading.password.changed.screen";
    private static String INTERNAL_ERROR = "error.email.internal";
    private static String EMAIL_FAILED = "error.email.not.sent";
    private static String REQUEST_KEY_NOT_FOUND = "error.forgot.password.incorrect.key";

    private static int MILLISECONDS_IN_A_DAY = 86400000;

    private static String LANGUAGE_KEY = "languageKey";

    /*
     * Request Spring to lookup the applicationContext tied to the current ServletContext and inject service beans
     * available in that applicationContext.
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
	super.init(config);
	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String method = request.getParameter("method");

	if (method.equals("showForgotYourPasswordPage")) {
	    if (Configuration.getAsBoolean(ConfigurationKeys.FORGOT_YOUR_PASSWORD_LINK_ENABLE)) {
		request.getRequestDispatcher("/forgotPassword.jsp").forward(request, response);
	    } else {
		//if people try to get to the forgot your password page by going to the URL directly, we display a 404 error message
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	    }

	} else if (method.equals("requestEmail")) {
	    String selectType = request.getParameter("selectType");
	    Boolean findByEmail = false;
	    String param = "";
	    if (selectType.equals("radioEmail")) {
		findByEmail = true;
		param = request.getParameter("email");
	    } else {
		param = request.getParameter("login");
	    }
	    handleEmailRequest(findByEmail, param.trim(), request, response);

	} else if (method.equals("requestPasswordChange")) {
	    String newPassword = request.getParameter("newPassword");
	    String key = request.getParameter("key");
	    handlePasswordChange(newPassword, key, request, response);

	} else {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	}
    }

    /**
     * Handles the first step of the forgot login process, sending the email to the user. An email is sent with a link
     * and key attached to identify the forgot login request
     *
     * @param findByEmail
     *            true if the forgot login request was for an email, false if it was for a login
     * @param param
     *            the param for which the user password will be searched
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void handleEmailRequest(Boolean findByEmail, String param, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	if ((param == null) || param.equals("")) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    return;
	}

	String SMPTServer = Configuration.get("SMTPServer");
	String supportEmail = Configuration.get("LamsSupportEmail");
	User user = null;

	String languageKey = null;
	boolean skipSendingEmail = false;
	if (StringUtils.isBlank(SMPTServer) || StringUtils.isBlank(supportEmail)) {
	    // Validate SMTP not set up
	    languageKey = ForgotPasswordServlet.SMTP_SERVER_NOT_SET;

	} else {
	    // get the user by email or login
	    if (!findByEmail) {
		if (userManagementService.getUserByLogin(param) != null) {
		    user = userManagementService.getUserByLogin(param);

		} else {
		    // validate user is not found
		    skipSendingEmail = true;
		}

	    } else {
		try {
		    List<User> users = userManagementService.getAllUsersWithEmail(param);
		    if (users.size() == 1) {
			user = users.get(0);

		    } else if (users.size() == 0) {
			// validate no user with email found
			skipSendingEmail = true;

		    } else {
			// validate multiple users with email found
			languageKey = ForgotPasswordServlet.INTERNAL_ERROR;
			log.info("Password recovery: The email is assigned to multiple users: " + param);
			skipSendingEmail = true;

		    }
		} catch (Exception e) {
		    languageKey = ForgotPasswordServlet.INTERNAL_ERROR;
		    log.error("Error while recovering password.", e);
		    skipSendingEmail = true;
		}
	    }

	    if (!skipSendingEmail) {
		boolean isHtmlFormat = false;
		// generate a key for the request
		String key = RandomPasswordGenerator.generateForgotPasswordKey();

		// all good, save the request in the db
		ForgotPasswordRequest fp = new ForgotPasswordRequest();
		fp.setRequestDate(new Date());
		fp.setUserId(user.getUserId());
		fp.setRequestKey(key);
		userManagementService.save(fp);

		// Constructing the body of the email
		String body = centralMessageService.getMessage("forgot.password.email.body") + "\n\n"
			+ Configuration.get("ServerURL") + "forgotPasswordChange.jsp?key=" + key;

		// send the email
		try {
		    Emailer.sendFromSupportEmail(centralMessageService.getMessage("forgot.password.email.subject"),
			    user.getEmail(), body, isHtmlFormat);
		} catch (AddressException e) {
		    // failure handling
		    log.error("Problem sending email to: " + user.getLogin() + " with email: " + user.getEmail(), e);
		    languageKey = ForgotPasswordServlet.EMAIL_FAILED;
		} catch (MessagingException e) {
		    // failure handling
		    log.error("Problem sending email to: " + user.getLogin() + " with email: " + user.getEmail(), e);
		    languageKey = ForgotPasswordServlet.EMAIL_FAILED;
		} catch (Exception e) {
		    // failure handling
		    log.error("Problem sending email to: " + user.getLogin() + " with email: " + user.getEmail(), e);
		    languageKey = ForgotPasswordServlet.EMAIL_FAILED;
		}
	    }
	}

	//show message as an error only in case message differs from the default one
	boolean showErrorMessage = languageKey != null;
	//show default message if there is no error message
	languageKey = languageKey == null ? ForgotPasswordServlet.REQUEST_PROCESSED : languageKey;

	request.setAttribute(ForgotPasswordServlet.LANGUAGE_KEY, languageKey);
	request.setAttribute("showErrorMessage", showErrorMessage);
	request.getRequestDispatcher("/forgotPasswordProc.jsp").forward(request, response);

    }

    /**
     * Updates the user's password
     *
     * @param key
     *            the key of the forgot password request
     */
    public void handlePasswordChange(String newPassword, String key, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	if ((key == null) || key.equals("") || (newPassword == null) || newPassword.equals("")) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    return;
	}

	String languageKey = "";
	boolean showErrorMessage = false;
	ForgotPasswordRequest fp = userManagementService.getForgotPasswordRequest(key);
	if (fp == null) {
	    languageKey = ForgotPasswordServlet.REQUEST_KEY_NOT_FOUND;
	    showErrorMessage = true;

	} else {
	    long cutoffTime = fp.getRequestDate().getTime() + ForgotPasswordServlet.MILLISECONDS_IN_A_DAY;
	    long now = new Date().getTime();

	    if (now < cutoffTime) {
		User user = (User) userManagementService.findById(User.class, fp.getUserId());
		userManagementService.updatePassword(user, newPassword);

		languageKey = ForgotPasswordServlet.SUCCESS_CHANGE_PASS;

	    } else {
		// validate password request expired
		languageKey = ForgotPasswordServlet.PASSWORD_REQUEST_EXPIRED;
		showErrorMessage = true;
	    }
	    userManagementService.delete(fp);
	}

	request.setAttribute(ForgotPasswordServlet.LANGUAGE_KEY, languageKey);
	request.setAttribute("showErrorMessage", showErrorMessage);
	request.getRequestDispatcher("/forgotPasswordProc.jsp").forward(request, response);
    }
}