package org.lamsfoundation.lams.web.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.authenticator.Constants;
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
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.Emailer;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @struts:action path="/signup/signup" name="SignupForm" scope="request"
 *                validate="false" parameter="method"
 * 
 * @struts:action-forward name="signup" path=".signup"
 * @struts:action-forward name="index" path="/"
 * @struts:action-forward name="success" path=".successfulSignup"
 */
public class SignupAction extends Action {

    private static Logger log = Logger.getLogger(SignupAction.class);
    private static ISignupService signupService = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if (signupService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    signupService = (ISignupService) wac.getBean("signupService");
	}

	String context = WebUtil.readStrParam(request, "context", true);
	if (StringUtils.isNotBlank(context)) {
	    SignupOrganisation signupOrganisation = signupService.getSignupOrganisation(context);
	    request.setAttribute("signupOrganisation", signupOrganisation);
	}

	DynaActionForm signupForm = (DynaActionForm) form;
	String method = WebUtil.readStrParam(request, "method", true);

	if (signupForm.get("submitted") == null || !((Boolean) signupForm.get("submitted"))) {
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
		// proceed with signup
		User user = new User();
		user.setLogin(signupForm.getString("username"));
		user.setFirstName(signupForm.getString("firstName"));
		user.setLastName(signupForm.getString("lastName"));
		user.setEmail(signupForm.getString("email"));
		user.setPassword(HashUtil.sha1(signupForm.getString("password")));
		signupService.signupUser(user, signupForm.getString("context"));

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
		    log.error(e.getMessage(), e);
		    request.setAttribute("error", e.getMessage());
		}

		return mapping.findForward("success");
	    }
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}

	return mapping.findForward("index");
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
		String password = HashUtil.sha1(signupForm.getString("passwordTab2"));
		String context = signupForm.getString("context");
		signupService.signinUser(login, context);

		String redirectUrl = Configuration.get(ConfigurationKeys.SERVER_URL);
		
		//check if user is logged in already
		if (SessionManager.getSession() == null || SessionManager.getSession().getAttribute(AttributeNames.USER) == null) {
		    redirectUrl += "/j_security_check?" + Constants.FORM_USERNAME + "=" + login + "&"
			    + Constants.FORM_PASSWORD + "=" + password;		    
		}
		
		response.sendRedirect(redirectUrl);
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
	if (StringUtils.isBlank(signupForm.getString("username"))) {
	    errors.add("username", new ActionMessage("error.username.blank"));
	} else if (signupService.usernameExists(signupForm.getString("username"))) {
	    errors.add("username", new ActionMessage("error.username.exists"));
	}
	if (StringUtils.isBlank(signupForm.getString("firstName"))) {
	    errors.add("firstName", new ActionMessage("error.first.name.blank"));
	}
	if (StringUtils.isBlank(signupForm.getString("lastName"))) {
	    errors.add("lastName", new ActionMessage("error.last.name.blank"));
	}
	if (StringUtils.isBlank(signupForm.getString("password"))) {
	    errors.add("password", new ActionMessage("error.password.blank"));
	} else if (!StringUtils.equals(signupForm.getString("password"), signupForm.getString("confirmPassword"))) {
	    errors.add("password", new ActionMessage("error.passwords.unequal"));
	}
	if (StringUtils.isBlank(signupForm.getString("email"))) {
	    errors.add("email", new ActionMessage("error.email.blank"));
	} else {
	    if (!StringUtils.equals(signupForm.getString("email"), signupForm.getString("confirmEmail"))) {
		errors.add("email", new ActionMessage("error.emails.unequal"));
	    }
	    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
	    Matcher m = p.matcher(signupForm.getString("email"));
	    if (!m.matches()) {
		errors.add("email", new ActionMessage("error.email.invalid.format"));
	    }
	}
	if (!signupService.courseKeyIsValid(signupForm.getString("context"),
		signupForm.getString("courseKey"))) {
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
	if (!signupService.courseKeyIsValid(signupForm.getString("context"),
		signupForm.getString("courseKeyTab2"))) {
	    errors.add("courseKeyTab2", new ActionMessage("error.course.key.invalid"));
	}
	
	if (errors.isEmpty()) {
	    String login = signupForm.getString("usernameTab2");
	    String password = HashUtil.sha1(signupForm.getString("passwordTab2"));
	    User user = signupService.getUserByLogin(login);
	    
	    if ((user == null) || !user.getPassword().equals(password)) {
		errors.add("usernameTab2", new ActionMessage("error.login.or.password.incorrect", "<a onclick='selectSignupTab();' id='selectLoginTabA'>", "</a>"));
	    }
	}
	
	return errors;
    }
}
