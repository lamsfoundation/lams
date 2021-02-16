package org.lamsfoundation.lams.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.web.form.SignupForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private static Logger log = Logger.getLogger(SignupController.class);
    
    @Autowired
    private ISignupService signupService;
    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;
    @Autowired
    private ITimezoneService timezoneService ;

    @RequestMapping("init")
    public String execute(@ModelAttribute("SignupForm") SignupForm signupForm, HttpServletRequest request,
	    HttpServletResponse response) {

	String context = WebUtil.readStrParam(request, "context", true);
	SignupOrganisation signupOrganisation = StringUtils.isNotBlank(context)
		? signupService.getSignupOrganisation(context)
		: null;
	if (signupOrganisation == null) {
	    request.setAttribute("messageKey", "no.such.signup.page.exist");
	    return "msgContent";
	}
	
	request.setAttribute("countryCodes", LanguageUtil.getCountryCodes(true));
	
	request.setAttribute("signupOrganisation", signupOrganisation);
	return "signup/signup";
    }

    @RequestMapping("signup")
    private String signup(@ModelAttribute("SignupForm") SignupForm signupForm, HttpServletRequest request) {
	try {
	    // validation
	    MultiValueMap<String, String> errorMap = validateSignup(signupForm);
	    if (!errorMap.isEmpty()) {
		request.setAttribute("countryCodes", LanguageUtil.getCountryCodes(true));
		request.setAttribute("errorMap", errorMap);
		return "signup/signup";
	    } else {
		boolean emailVerify = false;
		String context = WebUtil.readStrParam(request, "context", true);
		if (StringUtils.isNotBlank(context)) {
		    SignupOrganisation signupOrganisation = signupService.getSignupOrganisation(context);
		    emailVerify = signupOrganisation.getEmailVerify();
		}
		// proceed with signup
		User user = new User();
		user.setLogin(signupForm.getUsername());
		user.setFirstName(signupForm.getFirstName());
		user.setLastName(signupForm.getLastName());
		user.setEmail(signupForm.getEmail());
		user.setCountry(signupForm.getCountry());
		user.setTimeZone(timezoneService.getServerTimezone().getTimezoneId());
		String salt = HashUtil.salt();
		user.setSalt(salt);
		user.setPassword(HashUtil.sha256(signupForm.getPassword(), salt));
		user.setPasswordChangeDate(LocalDateTime.now());
		if (emailVerify) {
		    user.setEmailVerified(false);
		    user.setDisabledFlag(true);
		    signupService.signupUser(user, signupForm.getContext());
		    try {
			sendVerificationEmail(user);
		    } catch (Exception e) {
			log.error(e.getMessage(), e);
			request.setAttribute("error", e.getMessage());
		    }
		    request.setAttribute("email", user.getEmail());
		    return "/signup/emailVerifyResult";
		} else {
		    user.setDisabledFlag(false);
		    signupService.signupUser(user, signupForm.getContext());
		    try {
			sendWelcomeEmail(user);
		    } catch (Exception e) {
			log.error(e.getMessage(), e);
			request.setAttribute("error", e.getMessage());
		    }
		    return "signup/successfulSignup";
		}
	    }
	} catch (Exception e) {
	    SignupController.log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}

	return "/";
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
		.append("signup/emailVerify.do?login=").append(URLEncoder.encode(user.getLogin(), "UTF-8"))
		.append("&hash=").append(hash);
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

    @RequestMapping("login")
    public String login(@ModelAttribute("SignupForm") SignupForm signupForm, HttpServletRequest request,
	    HttpServletResponse response) {
	try {

	    // validation
	    MultiValueMap<String, String> errorMap = validateSignin(signupForm);
	    if (!errorMap.isEmpty()) {
		request.setAttribute("errorMap", errorMap);
		return "signup/signup";
	    } else {
		String login = signupForm.getUsernameTab2();
		String password = signupForm.getPasswordTab2();
		String context = signupForm.getContext();
		signupService.signinUser(login, context);

		HttpSession hses = request.getSession();
		hses.setAttribute("login", login);
		hses.setAttribute("password", password);
		response.sendRedirect("/lams/login.jsp?redirectURL=/lams");
		return null;
	    }
	} catch (Exception e) {
	    SignupController.log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}

	return "/";
    }

    private MultiValueMap<String, String> validateSignup(SignupForm signupForm) {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	// user name validation
	String userName = (signupForm.getUsername() == null) ? null : signupForm.getUsername();
	if (StringUtils.isBlank(userName)) {
	    errorMap.add("username", messageService.getMessage("error.username.blank"));
	} else if (!ValidationUtil.isUserNameValid(userName)) {
	    errorMap.add("username", messageService.getMessage("error.username.invalid.characters"));
	    SignupController.log.info("username has invalid characters: " + userName);
	} else if (signupService.usernameExists(userName)) {
	    errorMap.add("username", messageService.getMessage("error.username.exists"));
	}

	// first name validation
	String firstName = (signupForm.getFirstName() == null) ? null : signupForm.getFirstName();
	if (StringUtils.isBlank(firstName)) {
	    errorMap.add("firstName", messageService.getMessage("error.first.name.blank"));
	} else if (!ValidationUtil.isFirstLastNameValid(firstName)) {
	    errorMap.add("firstName", messageService.getMessage("error.firstname.invalid.characters"));
	    SignupController.log.info("firstname has invalid characters: " + firstName);
	}

	//last name validation
	String lastName = (signupForm.getLastName() == null) ? null : signupForm.getLastName();
	if (StringUtils.isBlank(lastName)) {
	    errorMap.add("lastName", messageService.getMessage("error.last.name.blank"));
	} else if (!ValidationUtil.isFirstLastNameValid(lastName)) {
	    errorMap.add("lastName", messageService.getMessage("error.lastname.invalid.characters"));
	    SignupController.log.info("lastName has invalid characters: " + lastName);
	}

	//password validation
	if (StringUtils.isBlank(signupForm.getPassword())) {
	    errorMap.add("password", messageService.getMessage("error.password.blank"));
	} else if (!StringUtils.equals(signupForm.getPassword(), signupForm.getConfirmPassword())) {
	    errorMap.add("password", messageService.getMessage("error.passwords.unequal"));
	} else if (!ValidationUtil.isPasswordValueValid(signupForm.getPassword(), signupForm.getConfirmPassword())) {
	    errorMap.add("password", messageService.getMessage("label.password.restrictions"));
	}

	//user email validation
	String userEmail = (signupForm.getEmail() == null) ? null : signupForm.getEmail();
	if (StringUtils.isBlank(userEmail)) {
	    errorMap.add("email", messageService.getMessage("error.email.blank"));
	} else if (!ValidationUtil.isEmailValid(userEmail)) {
	    errorMap.add("email", messageService.getMessage("error.email.invalid.format"));
	} else if (!StringUtils.equals(userEmail, signupForm.getConfirmEmail())) {
	    errorMap.add("email", messageService.getMessage("error.emails.unequal"));
	}

	// courseKey validation
	if (!signupService.courseKeyIsValid(signupForm.getContext(), signupForm.getCourseKey())) {
	    errorMap.add("courseKey", messageService.getMessage("error.course.key.invalid"));
	}
	return errorMap;
    }

    private MultiValueMap<String, String> validateSignin(SignupForm signupForm) {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	if (StringUtils.isBlank(signupForm.getUsernameTab2())) {
	    errorMap.add("usernameTab2", messageService.getMessage("error.username.blank"));
	}
	if (StringUtils.isBlank(signupForm.getPasswordTab2())) {
	    errorMap.add("passwordTab2", messageService.getMessage("error.password.blank"));
	}
	if (!signupService.courseKeyIsValid(signupForm.getContext(), signupForm.getCourseKeyTab2())) {
	    errorMap.add("courseKeyTab2", messageService.getMessage("error.course.key.invalid"));
	}

	if (errorMap.isEmpty()) {
	    String login = signupForm.getUsernameTab2();
	    String password = signupForm.getPasswordTab2();
	    User user = signupService.getUserByLogin(login);
	    if (user == null) {
		errorMap.add("usernameTab2", messageService.getMessage("error.login.or.password.incorrect",
			new Object[] { "<a onclick='selectSignupTab();' id='selectLoginTabA'>", "</a>" }));
	    } else {
		String passwordHash = user.getPassword().length() == HashUtil.SHA1_HEX_LENGTH ? HashUtil.sha1(password)
			: HashUtil.sha256(password, user.getSalt());
		if (!user.getPassword().equals(passwordHash)) {
		    errorMap.add("usernameTab2", messageService.getMessage("error.login.or.password.incorrect",
			    new Object[] { "<a onclick='selectSignupTab();' id='selectLoginTabA'>", "</a>" }));
		}
	    }
	}

	return errorMap;
    }

    /**
     * Checks whether incoming hash is the same as the expected hash for email verification
     */
    @RequestMapping("emailVerify")
    public String emailVerify(HttpServletRequest request) {
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
	return "/signup/emailVerify";
    }
}
