package org.lamsfoundation.lams.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.signup.model.SignupOrganisation;
import org.lamsfoundation.lams.signup.service.ISignupService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.Emailer;
import org.lamsfoundation.lams.util.HashUtil;
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
    MessageService messageService;

    @RequestMapping("")
    public String execute(@ModelAttribute SignupForm signupForm, HttpServletRequest request,
	    HttpServletResponse response) {

	String method = WebUtil.readStrParam(request, "method", true);
	String context = WebUtil.readStrParam(request, "context", true);
	SignupOrganisation signupOrganisation = null;
	if (StringUtils.isNotBlank(context)) {
	    signupOrganisation = signupService.getSignupOrganisation(context);
	    request.setAttribute("signupOrganisation", signupOrganisation);
	}
	if ((signupForm.getSubmitted() == null) || !((Boolean) signupForm.getSubmitted())) {
	    if (signupOrganisation == null) {
		request.setAttribute("messageKey", "no.such.signup.page.exist");
		return "msgContent";
	    }

	    // no context and unsubmitted form means it's the initial request
	    return "signup/signup";
	} else if (StringUtils.equals(method, "register")) {
	    return signUp(signupForm, request);
	} else {
	    return signIn(signupForm, request, response);
	}
    }

    @RequestMapping("/signUp")
    private String signUp(@ModelAttribute SignupForm signupForm, HttpServletRequest request) {

	try {

	    // validation
	    MultiValueMap<String, String> errorMap = validateSignup(signupForm);
	    if (!errorMap.isEmpty()) {
		request.setAttribute("errorMap", errorMap);
		return "signup/signup";
	    } else {
		// proceed with signup
		User user = new User();
		user.setLogin(signupForm.getUsername());
		user.setFirstName(signupForm.getFirstName());
		user.setLastName(signupForm.getLastName());
		user.setEmail(signupForm.getEmail());
		String salt = HashUtil.salt();
		user.setSalt(salt);
		user.setPassword(HashUtil.sha256(signupForm.getPassword(), salt));
		signupService.signupUser(user, signupForm.getContext());

		// send email
		try {
		    String subject = "Your LAMS account details";
		    String body = "Hi there,\n\n";
		    body += "You've successfully registered an account with username " + user.getLogin();
		    body += " on the LAMS server at " + Configuration.get(ConfigurationKeys.SERVER_URL);
		    body += ".  If you ever forget your password, you can reset it via this URL "
			    + Configuration.get(ConfigurationKeys.SERVER_URL) + "/forgotPassword.jsp.";
		    body += "\n\n";
		    body += "Regards,\n";
		    body += "LAMS Signup System";
		    boolean isHtmlFormat = false;

		    Emailer.sendFromSupportEmail(subject, user.getEmail(), body, isHtmlFormat);
		} catch (Exception e) {
		    SignupController.log.error(e.getMessage(), e);
		    request.setAttribute("error", e.getMessage());
		}

		return "signup/successfulSignup";
	    }
	} catch (Exception e) {
	    SignupController.log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}

	return "/";
    }

    @RequestMapping("/signIn")
    private String signIn(@ModelAttribute SignupForm signupForm, HttpServletRequest request,
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
}
