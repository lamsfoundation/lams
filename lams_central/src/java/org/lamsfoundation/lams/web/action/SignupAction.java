package org.lamsfoundation.lams.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.signup.model.SignupOrganisation;
import org.lamsfoundation.lams.signup.service.ISignupService;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.Emailer;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SignupAction extends Action {

    private static Logger log = Logger.getLogger(SignupAction.class);
    private static ISignupService signupService = null;
    private static ITimezoneService timezoneService = null;
    private static MessageService messageService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String method = WebUtil.readStrParam(request, "method", true);
	if (StringUtils.equals(method, "emailVerify")) {
	    return emailVerify(mapping, form, request, response);
	}

	if (signupService == null || timezoneService == null || messageService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    signupService = (ISignupService) wac.getBean("signupService");
	    timezoneService = (ITimezoneService) wac.getBean("timezoneService");
	    messageService = (MessageService) wac.getBean("centralMessageService");
	}

	request.setAttribute("countryCodes", LanguageUtil.getCountryCodes(true));

	DynaActionForm signupForm = (DynaActionForm) form;

	String context = WebUtil.readStrParam(request, "context", true);
	SignupOrganisation signupOrganisation = null;
	if (StringUtils.isNotBlank(context)) {
	    signupOrganisation = signupService.getSignupOrganisation(context);
	    request.setAttribute("signupOrganisation", signupOrganisation);
	}
	if ((signupForm.get("submitted") == null) || !((Boolean) signupForm.get("submitted"))) {
	    if (signupOrganisation == null) {
		request.setAttribute("messageKey", "no.such.signup.page.exist");
		return mapping.findForward("message");
	    }

	    // no context and unsubmitted form means it's the initial request
	    return mapping.findForward("signup");
	} else if (StringUtils.equals(method, "register")) {
	    return signUp(mapping, form, request, response);
	} else {
	    return signIn(mapping, form, request, response);
	}
    }

    private ActionForward signUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    DynaActionForm signupForm = (DynaActionForm) form;

	    // validation
	    ActionMessages errors = validateSignup(signupForm);
	    if (!errors.isEmpty()) {
		saveErrors(request, errors);
		return mapping.findForward("signup");
	    } else {
		boolean emailVerify = false;
		String context = WebUtil.readStrParam(request, "context", true);
		if (StringUtils.isNotBlank(context)) {
		    SignupOrganisation signupOrganisation = signupService.getSignupOrganisation(context);
		    emailVerify = signupOrganisation.getEmailVerify();
		}
		// proceed with signup
		User user = new User();
		user.setLogin(signupForm.getString("username"));
		user.setFirstName(signupForm.getString("firstName"));
		user.setLastName(signupForm.getString("lastName"));
		user.setEmail(signupForm.getString("email"));
		user.setCountry(signupForm.getString("country"));
		user.setTimeZone(timezoneService.getServerTimezone().getTimezoneId());
		String salt = HashUtil.salt();
		user.setSalt(salt);
		user.setPassword(HashUtil.sha256(signupForm.getString("password"), salt));
		if (emailVerify) {
		    user.setEmailVerified(false);
		    user.setDisabledFlag(true);
		    signupService.signupUser(user, signupForm.getString("context"));
		    try {
			sendVerificationEmail(user);
		    } catch (Exception e) {
			log.error(e.getMessage(), e);
			request.setAttribute("error", e.getMessage());
		    }
		    request.setAttribute("email", user.getEmail());
		    return mapping.findForward("emailVerify");
		} else {
		    user.setDisabledFlag(false);
		    signupService.signupUser(user, signupForm.getString("context"));
		    try {
			sendWelcomeEmail(user);
		    } catch (Exception e) {
			log.error(e.getMessage(), e);
			request.setAttribute("error", e.getMessage());
		    }
		    return mapping.findForward("success");
		}
	    }
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}

	return mapping.findForward("index");
    }

    private void sendWelcomeEmail(User user) throws AddressException, MessagingException, UnsupportedEncodingException {
	String subject = messageService.getMessage("signup.email.welcome.subject");
	String body = new StringBuilder(messageService.getMessage("signup.email.welcome.body.1")).append("<br /><br />")
		.append(messageService.getMessage("signup.email.welcome.body.2",
			new Object[] { user.getLogin(), Configuration.get(ConfigurationKeys.SERVER_URL) }))
		.append("<br />").append(messageService.getMessage("signup.email.welcome.body.3"))
		.append("<br /><a href=\"").append(Configuration.get(ConfigurationKeys.SERVER_URL))
		.append("forgotPassword.jsp\">").append(Configuration.get(ConfigurationKeys.SERVER_URL))
		.append("forgotPassword.jsp</a><br /><br />")
		.append(messageService.getMessage("signup.email.welcome.body.4")).append("<br />")
		.append(messageService.getMessage("signup.email.welcome.body.5")).toString();
	boolean isHtmlFormat = true;

	Emailer.sendFromSupportEmail(subject, user.getEmail(), body, isHtmlFormat);
    }

    private void sendVerificationEmail(User user)
	    throws AddressException, MessagingException, UnsupportedEncodingException {
	String hash = HashUtil.sha256(user.getEmail(), user.getSalt());
	StringBuilder stringBuilder = new StringBuilder().append(Configuration.get(ConfigurationKeys.SERVER_URL))
		.append("signup/signup.do?method=emailVerify&login=")
		.append(URLEncoder.encode(user.getLogin(), "UTF-8")).append("&hash=").append(hash);
	String link = stringBuilder.toString();

	String subject = messageService.getMessage("signup.email.verify.subject");
	stringBuilder = new StringBuilder(messageService.getMessage("signup.email.verify.body.1"))
		.append("<br /><br />").append(messageService.getMessage("signup.email.verify.body.2"))
		.append("<br /><a href=\"").append(link).append("\">").append(link).append("</a><br /><br />")
		.append(messageService.getMessage("signup.email.verify.body.3")).append("<br />")
		.append(messageService.getMessage("signup.email.verify.body.4"));
	String body = stringBuilder.toString();
	boolean isHtmlFormat = true;

	Emailer.sendFromSupportEmail(subject, user.getEmail(), body, isHtmlFormat);
    }

    private ActionForward signIn(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	try {
	    DynaActionForm signupForm = (DynaActionForm) form;

	    // validation
	    ActionMessages errors = validateSignin(signupForm);
	    if (!errors.isEmpty()) {
		saveErrors(request, errors);
		return mapping.findForward("signup");
	    } else {
		String login = signupForm.getString("usernameTab2");
		String password = signupForm.getString("passwordTab2");
		String context = signupForm.getString("context");
		signupService.signinUser(login, context);

		HttpSession hses = request.getSession();
		hses.setAttribute("login", login);
		hses.setAttribute("password", password);
		response.sendRedirect("/lams/login.jsp?redirectURL=/lams");
		return null;
	    }
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}

	return mapping.findForward("index");
    }

    private ActionMessages validateSignup(DynaActionForm signupForm) {
	ActionMessages errors = new ActionMessages();

	// user name validation
	String userName = (signupForm.get("username") == null) ? null : (String) signupForm.get("username");
	if (StringUtils.isBlank(userName)) {
	    errors.add("username", new ActionMessage("error.username.blank"));
	} else if (!ValidationUtil.isUserNameValid(userName)) {
	    errors.add("username", new ActionMessage("error.username.invalid.characters"));
	    log.info("username has invalid characters: " + userName);
	} else if (signupService.usernameExists(userName)) {
	    errors.add("username", new ActionMessage("error.username.exists"));
	}

	// first name validation
	String firstName = (signupForm.get("firstName") == null) ? null : (String) signupForm.get("firstName");
	if (StringUtils.isBlank(firstName)) {
	    errors.add("firstName", new ActionMessage("error.first.name.blank"));
	} else if (!ValidationUtil.isFirstLastNameValid(firstName)) {
	    errors.add("firstName", new ActionMessage("error.firstname.invalid.characters"));
	    log.info("firstname has invalid characters: " + firstName);
	}

	//last name validation
	String lastName = (signupForm.get("lastName") == null) ? null : (String) signupForm.get("lastName");
	if (StringUtils.isBlank(lastName)) {
	    errors.add("lastName", new ActionMessage("error.last.name.blank"));
	} else if (!ValidationUtil.isFirstLastNameValid(lastName)) {
	    errors.add("lastName", new ActionMessage("error.lastname.invalid.characters"));
	    log.info("lastName has invalid characters: " + lastName);
	}

	//password validation
	if (StringUtils.isBlank(signupForm.getString("password"))) {
	    errors.add("password", new ActionMessage("error.password.blank"));
	} else if (!StringUtils.equals(signupForm.getString("password"), signupForm.getString("confirmPassword"))) {
	    errors.add("password", new ActionMessage("error.passwords.unequal"));
	} else if (!ValidationUtil.isPasswordValueValid(signupForm.getString("password"),
		signupForm.getString("confirmPassword"))) {
	    errors.add("password", new ActionMessage("label.password.restrictions"));
	}

	//user email validation
	String userEmail = (signupForm.get("email") == null) ? null : (String) signupForm.get("email");
	if (StringUtils.isBlank(userEmail)) {
	    errors.add("email", new ActionMessage("error.email.blank"));
	} else if (!ValidationUtil.isEmailValid(userEmail)) {
	    errors.add("email", new ActionMessage("error.email.invalid.format"));
	} else if (!StringUtils.equals(userEmail, signupForm.getString("confirmEmail"))) {
	    errors.add("email", new ActionMessage("error.emails.unequal"));
	}

	//country validation
	String country = (signupForm.get("country") == null) ? null : (String) signupForm.get("country");
	if (StringUtils.isBlank(country) || "0".equals(country)) {
	    errors.add("email", new ActionMessage("error.country.required"));
	}

	// courseKey validation
	if (!signupService.courseKeyIsValid(signupForm.getString("context"), signupForm.getString("courseKey"))) {
	    errors.add("courseKey", new ActionMessage("error.course.key.invalid"));
	}
	return errors;
    }

    private ActionMessages validateSignin(DynaActionForm signupForm) {
	ActionMessages errors = new ActionMessages();
	if (StringUtils.isBlank(signupForm.getString("usernameTab2"))) {
	    errors.add("usernameTab2", new ActionMessage("error.username.blank"));
	}
	if (StringUtils.isBlank(signupForm.getString("passwordTab2"))) {
	    errors.add("passwordTab2", new ActionMessage("error.password.blank"));
	}
	if (!signupService.courseKeyIsValid(signupForm.getString("context"), signupForm.getString("courseKeyTab2"))) {
	    errors.add("courseKeyTab2", new ActionMessage("error.course.key.invalid"));
	}

	if (errors.isEmpty()) {
	    String login = signupForm.getString("usernameTab2");
	    String password = signupForm.getString("passwordTab2");
	    User user = signupService.getUserByLogin(login);
	    if (user == null) {
		errors.add("usernameTab2", new ActionMessage("error.login.or.password.incorrect",
			"<a onclick='selectSignupTab();' id='selectLoginTabA'>", "</a>"));
	    } else {
		String passwordHash = user.getPassword().length() == HashUtil.SHA1_HEX_LENGTH ? HashUtil.sha1(password)
			: HashUtil.sha256(password, user.getSalt());
		if (!user.getPassword().equals(passwordHash)) {
		    errors.add("usernameTab2", new ActionMessage("error.login.or.password.incorrect",
			    "<a onclick='selectSignupTab();' id='selectLoginTabA'>", "</a>"));
		}
	    }
	}

	return errors;
    }

    /**
     * Checks whether incoming hash is the same as the expected hash for email verification
     */
    public ActionForward emailVerify(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String login = WebUtil.readStrParam(request, "login", false);
	String hash = WebUtil.readStrParam(request, "hash", false);

	boolean verified = signupService.emailVerify(login, hash);
	if (verified) {
	    request.setAttribute("emailVerified", verified);
	    User user = signupService.getUserByLogin(login);
	    try {
		sendWelcomeEmail(user);
	    } catch (Exception e) {
		log.error(e.getMessage(), e);
		request.setAttribute("error", e.getMessage());
	    }
	}
	return mapping.findForward("emailVerifyResult");
    }
}
