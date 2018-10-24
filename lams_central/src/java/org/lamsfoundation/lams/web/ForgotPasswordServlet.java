package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDGenerator;
import org.hibernate.type.StringType;
import org.lamsfoundation.lams.usermanagement.ForgotPasswordRequest;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.Emailer;
import org.lamsfoundation.lams.util.FileUtilException;
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
    public static String SMTP_SERVER_NOT_SET = "error.support.email.not.set";
    public static String USER_NOT_FOUND = "error.user.not.found";
    public static String PASSWORD_REQUEST_EXPIRED = "error.password.request.expired";
    public static String SUCCESS_REQUEST_EMAIL = "forgot.password.email.sent";
    public static String SUCCESS_CHANGE_PASS = "heading.password.changed.screen";
    public static String EMAIL_NOT_FOUND = "error.email.not.found";
    public static String INTERNAL_ERROR = "error.email.internal";
    public static String EMAIL_FAILED = "error.email.not.sent";
    public static String REQUEST_KEY_NOT_FOUND = "error.forgot.password.incorrect.key";

    private static int MILLISECONDS_IN_A_DAY = 86400000;

    private static String STATE = "&state=";
    private static String LANGUAGE_KEY = "&languageKey=";
    private static String EMAIL_SENT = "&emailSent=";
    
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

	if (method.equals("requestEmail")) {
	    String selectType = request.getParameter("selectType");
	    Boolean findByEmail = false;
	    String param = "";
	    if (selectType.equals("radioEmail")) {
		findByEmail = true;
		param = request.getParameter("email");
	    } else {
		param = request.getParameter("login");
	    }

	    handleEmailRequest(findByEmail, param.trim(), response);
	} else if (method.equals("requestPasswordChange")) {
	    String newPassword = request.getParameter("newPassword");
	    String key = request.getParameter("key");
	    handlePasswordChange(newPassword, key, response);
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
    public void handleEmailRequest(Boolean findByEmail, String param, HttpServletResponse response)
	    throws ServletException, IOException {

	int success = 0;
	String languageKey = "";

	boolean err = false;

	if ((param == null) || param.equals("")) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    return;
	}

	String SMPTServer = Configuration.get("SMTPServer");
	String supportEmail = Configuration.get("LamsSupportEmail");
	User user = null;

	if ((SMPTServer == null) || SMPTServer.equals("") || (supportEmail == null) || supportEmail.equals("")) {
	    // Validate SMTP not set up
	    languageKey = ForgotPasswordServlet.SMTP_SERVER_NOT_SET;
	    
	} else {
	    // get the user by email or login
	    if (!findByEmail) {
		if (userManagementService.getUserByLogin(param) != null) {
		    user = userManagementService.getUserByLogin(param);
		} else {
		    // validate user is not found
		    languageKey = ForgotPasswordServlet.USER_NOT_FOUND;
		    err = true;
		}
		
	    } else {
		try {
		    List<User> users = userManagementService.getAllUsersWithEmail(param);
		    if (users.size() == 1) {
			user = users.get(0);
			
		    } else if (users.size() == 0) {
			// validate no user with email found
			languageKey = ForgotPasswordServlet.EMAIL_NOT_FOUND;
			err = true;
			
		    } else {
			// validate multiple users with email found
			languageKey = ForgotPasswordServlet.INTERNAL_ERROR;
			ForgotPasswordServlet.log
				.info("Password recovery: The email is assigned to multiple users: " + param);
			err = true;
		    }
		} catch (Exception e) {
		    languageKey = ForgotPasswordServlet.INTERNAL_ERROR;
		    ForgotPasswordServlet.log.error("Error while recovering password.", e);
		    err = true;
		}
	    }

	    if (!err) {
		boolean isHtmlFormat = false;
		// generate a key for the request
		String key = ForgotPasswordServlet.generateUniqueKey();

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
		    languageKey = ForgotPasswordServlet.SUCCESS_REQUEST_EMAIL;
		    success = 1;
		} catch (AddressException e) {
		    // failure handling
		    ForgotPasswordServlet.log.error(
			    "Problem sending email to: " + user.getLogin() + " with email: " + user.getEmail(), e);
		    // response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    languageKey = ForgotPasswordServlet.EMAIL_FAILED;
		    success = 0;
		} catch (MessagingException e) {
		    // failure handling
		    ForgotPasswordServlet.log.error(
			    "Problem sending email to: " + user.getLogin() + " with email: " + user.getEmail(), e);
		    // response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    languageKey = ForgotPasswordServlet.EMAIL_FAILED;
		    success = 0;
		} catch (Exception e) {
		    // failure handling
		    ForgotPasswordServlet.log.error(
			    "Problem sending email to: " + user.getLogin() + " with email: " + user.getEmail(), e);
		    languageKey = ForgotPasswordServlet.EMAIL_FAILED;
		    success = 0;
		    // response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

	    }

	}

	String redirectStr = Configuration.get("ServerURL") + "forgotPasswordProc.jsp?" + ForgotPasswordServlet.STATE
		+ success + ForgotPasswordServlet.LANGUAGE_KEY + languageKey;

	if ((success == 1) && (user.getEmail() != null)) {
	    redirectStr += ForgotPasswordServlet.EMAIL_SENT + java.net.URLEncoder.encode(user.getEmail(), "UTF-8");
	}

	response.sendRedirect(redirectStr);
    }

    /**
     * Updates the user's password
     *
     * @param key
     *            the key of the forgot password request
     */
    public void handlePasswordChange(String newPassword, String key, HttpServletResponse response)
	    throws ServletException, IOException {
	int success = 0;
	String languageKey = "";

	if ((key == null) || key.equals("") || (newPassword == null) || newPassword.equals("")) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
	    return;
	}

	ForgotPasswordRequest fp = userManagementService.getForgotPasswordRequest(key);

	if (fp == null) {
	    response.sendRedirect(
		    Configuration.get("ServerURL") + "forgotPasswordProc.jsp?" + ForgotPasswordServlet.STATE + 0
			    + ForgotPasswordServlet.LANGUAGE_KEY + ForgotPasswordServlet.REQUEST_KEY_NOT_FOUND);
	    return;
	}

	long cutoffTime = fp.getRequestDate().getTime() + ForgotPasswordServlet.MILLISECONDS_IN_A_DAY;
	Date now = new Date();
	long nowLong = now.getTime();

	if (nowLong < cutoffTime) {
	    User user = (User) userManagementService.findById(User.class, fp.getUserId());
	    userManagementService.updatePassword(user.getLogin(), newPassword);
	    userManagementService.logPasswordChanged(user, user);
	    languageKey = ForgotPasswordServlet.SUCCESS_CHANGE_PASS;
	    success = 1;
	} else {
	    // validate password request expired
	    languageKey = ForgotPasswordServlet.PASSWORD_REQUEST_EXPIRED;
	}

	userManagementService.delete(fp);

	response.sendRedirect(Configuration.get("ServerURL") + "forgotPasswordProc.jsp?" + ForgotPasswordServlet.STATE
		+ success + ForgotPasswordServlet.LANGUAGE_KEY + languageKey);
    }

    /**
     * Generates the unique key used for the forgot password request
     *
     * @return a unique key
     * @throws HibernateException 
     * @throws FileUtilException
     * @throws IOException
     */
    public static String generateUniqueKey() throws HibernateException {
	Properties props = new Properties();

	IdentifierGenerator uuidGen = new UUIDGenerator();
	((Configurable) uuidGen).configure(StringType.INSTANCE, props, null);

	return ((String) uuidGen.generate(null, null)).toLowerCase();
    }

}
