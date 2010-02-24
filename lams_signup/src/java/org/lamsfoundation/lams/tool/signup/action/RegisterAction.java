package org.lamsfoundation.lams.tool.signup.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.tool.signup.model.SignupOrganisation;
import org.lamsfoundation.lams.tool.signup.service.SignupService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.Emailer;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @struts:action path="/register" name="RegisterForm" scope="request"
 *                validate="false" parameter="method"
 * 
 * @struts:action-forward name="register" path=".register"
 * @struts:action-forward name="index" path="/"
 * @struts:action-forward name="success" path=".success"
 */
public class RegisterAction extends Action {

    private static Logger log = Logger.getLogger(RegisterAction.class);
    private static SignupService signupService = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	try {
	    if (signupService == null) {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
			.getServletContext());
		signupService = (SignupService) wac.getBean("signupService");
	    }

	    String context = WebUtil.readStrParam(request, "context", true);
	    if (StringUtils.isNotBlank(context)) {
		SignupOrganisation signupOrganisation = signupService.getSignupDAO().getSignupOrganisation(context);
		request.setAttribute("signupOrganisation", signupOrganisation);
	    }

	    DynaActionForm registerForm = (DynaActionForm) form;

	    if (registerForm.get("submitted") != null && (Boolean) registerForm.get("submitted")) {
		// validation
		ActionMessages errors = validate(registerForm);
		if (!errors.isEmpty()) {
		    saveErrors(request, errors);
		    return mapping.findForward("register");
		} else {
		    // proceed with signup
		    User user = new User();
		    user.setLogin(registerForm.getString("username"));
		    user.setFirstName(registerForm.getString("firstName"));
		    user.setLastName(registerForm.getString("lastName"));
		    user.setEmail(registerForm.getString("email"));
		    user.setPassword(HashUtil.sha1(registerForm.getString("password")));
		    signupService.signupUser(user, registerForm.getString("context"));

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

			Emailer.sendFromSupportEmail(subject, user.getEmail(), body);
		    } catch (Exception e) {
			log.error(e.getMessage(), e);
			request.setAttribute("error", e.getMessage());
		    }

		    return mapping.findForward("success");
		}
	    } else {
		// no context and unsubmitted form means it's the initial
		// request
		return mapping.findForward("register");
	    }
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}

	return mapping.findForward("index");
    }

    private ActionMessages validate(DynaActionForm registerForm) {
	ActionMessages errors = new ActionMessages();
	if (StringUtils.isBlank(registerForm.getString("username"))) {
	    errors.add("username", new ActionMessage("error.username.blank"));
	} else if (signupService.getSignupDAO().usernameExists(registerForm.getString("username"))) {
	    errors.add("username", new ActionMessage("error.username.exists"));
	}
	if (StringUtils.isBlank(registerForm.getString("firstName"))) {
	    errors.add("firstName", new ActionMessage("error.first.name.blank"));
	}
	if (StringUtils.isBlank(registerForm.getString("lastName"))) {
	    errors.add("lastName", new ActionMessage("error.last.name.blank"));
	}
	if (StringUtils.isBlank(registerForm.getString("password"))) {
	    errors.add("password", new ActionMessage("error.password.blank"));
	} else if (!StringUtils.equals(registerForm.getString("password"), registerForm.getString("confirmPassword"))) {
	    errors.add("password", new ActionMessage("error.passwords.unequal"));
	}
	if (StringUtils.isBlank(registerForm.getString("email"))) {
	    errors.add("email", new ActionMessage("error.email.blank"));
	} else {
	    if (!StringUtils.equals(registerForm.getString("email"), registerForm.getString("confirmEmail"))) {
		errors.add("email", new ActionMessage("error.emails.unequal"));
	    }
	    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
	    Matcher m = p.matcher(registerForm.getString("email"));
	    if (!m.matches()) {
		errors.add("email", new ActionMessage("error.email.invalid.format"));
	    }
	}
	if (!signupService.getSignupDAO().courseKeyIsValid(registerForm.getString("context"),
		registerForm.getString("courseKey"))) {
	    errors.add("courseKey", new ActionMessage("error.course.key.invalid"));
	}
	return errors;
    }
}
