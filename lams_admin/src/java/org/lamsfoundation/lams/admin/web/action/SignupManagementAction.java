package org.lamsfoundation.lams.admin.web.action;

import java.util.Date;
import java.util.List;

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
import org.lamsfoundation.lams.signup.model.SignupOrganisation;
import org.lamsfoundation.lams.signup.service.ISignupService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 *
 *
 *
 *
 *
 */
public class SignupManagementAction extends Action {

    private static Logger log = Logger.getLogger(SignupManagementAction.class);
    private static ISignupService signupService = null;
    private static IUserManagementService userManagementService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    if (signupService == null) {
		WebApplicationContext wac = WebApplicationContextUtils
			.getRequiredWebApplicationContext(getServlet().getServletContext());
		signupService = (ISignupService) wac.getBean("signupService");
	    }
	    if (userManagementService == null) {
		WebApplicationContext wac = WebApplicationContextUtils
			.getRequiredWebApplicationContext(getServlet().getServletContext());
		userManagementService = (IUserManagementService) wac.getBean("userManagementService");
	    }

	    String method = WebUtil.readStrParam(request, "method", true);

	    if (StringUtils.equals(method, "list") || isCancelled(request)) {
		// do nothing
	    } else if (StringUtils.equals(method, "edit")) {
		return edit(mapping, form, request, response);
	    } else if (StringUtils.equals(method, "add")) {
		return add(mapping, form, request, response);
	    } else if (StringUtils.equals(method, "delete")) {
		return delete(mapping, form, request, response);
	    }

	    List signupOrganisations = signupService.getSignupOrganisations();
	    request.setAttribute("signupOrganisations", signupOrganisations);
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}

	return mapping.findForward("signupPageList");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer soid = WebUtil.readIntParam(request, "soid", false);

	if (soid != null && soid > 0) {
	    SignupOrganisation signup = (SignupOrganisation) userManagementService.findById(SignupOrganisation.class,
		    soid);
	    if (signup != null) {
		DynaActionForm signupForm = (DynaActionForm) form;
		signupForm.set("signupOrganisationId", signup.getSignupOrganisationId());
		signupForm.set("organisationId", signup.getOrganisation().getOrganisationId());
		signupForm.set("addToLessons", signup.getAddToLessons());
		signupForm.set("addAsStaff", signup.getAddAsStaff());
		signupForm.set("addWithAuthor", signup.getAddWithAuthor());
		signupForm.set("addWithMonitor", signup.getAddWithMonitor());
		signupForm.set("courseKey", signup.getCourseKey());
		signupForm.set("blurb", signup.getBlurb());
		signupForm.set("disabled", signup.getDisabled());
		signupForm.set("loginTabActive", signup.getLoginTabActive());
		signupForm.set("context", signup.getContext());
		request.setAttribute("signupForm", signupForm);

		List organisations = signupService.getOrganisationCandidates();
		request.setAttribute("organisations", organisations);

		return mapping.findForward("addSignupPage");
	    }
	}
	return null;
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm signupForm = (DynaActionForm) form;

	// check if form submitted
	if (signupForm.get("organisationId") != null && (Integer) signupForm.get("organisationId") > 0) {
	    ActionMessages errors = new ActionMessages();

	    // validate
	    if (!StringUtils.equals(signupForm.getString("courseKey"), signupForm.getString("confirmCourseKey"))) {
		errors.add("courseKey", new ActionMessage("error.course.keys.unequal"));
	    }
	    if (signupService.contextExists((Integer) signupForm.get("signupOrganisationId"),
		    signupForm.getString("context"))) {
		errors.add("context", new ActionMessage("error.context.exists"));
	    }

	    if (!errors.isEmpty()) {
		saveErrors(request, errors);
	    } else {
		// proceed
		SignupOrganisation signup;
		if (signupForm.get("signupOrganisationId") != null
			&& (Integer) signupForm.get("signupOrganisationId") > 0) {
		    // form was editing existing
		    signup = (SignupOrganisation) userManagementService.findById(SignupOrganisation.class,
			    (Integer) signupForm.get("signupOrganisationId"));
		} else {
		    signup = new SignupOrganisation();
		    signup.setCreateDate(new Date());
		}
		signup.setAddToLessons((Boolean) signupForm.get("addToLessons"));
		signup.setAddAsStaff((Boolean) signupForm.get("addAsStaff"));
		signup.setAddWithAuthor((Boolean) signupForm.get("addWithAuthor"));
		signup.setAddWithMonitor((Boolean) signupForm.get("addWithMonitor"));
		signup.setDisabled((Boolean) signupForm.get("disabled"));
		signup.setLoginTabActive((Boolean) signupForm.get("loginTabActive"));
		signup.setOrganisation((Organisation) userManagementService.findById(Organisation.class,
			(Integer) signupForm.get("organisationId")));
		signup.setCourseKey(signupForm.getString("courseKey"));
		signup.setBlurb(signupForm.getString("blurb"));
		signup.setContext(signupForm.getString("context"));
		userManagementService.save(signup);

		return mapping.findForward("signupPageListMethod");
	    }
	} else {
	    // form not submitted, default values
	    signupForm.set("blurb", "Register your LAMS account for this group using the form below.");
	}

	List organisations = signupService.getOrganisationCandidates();
	request.setAttribute("organisations", organisations);

	return mapping.findForward("addSignupPage");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer soid = WebUtil.readIntParam(request, "soid");

	if (soid != null && soid > 0) {
	    userManagementService.deleteById(SignupOrganisation.class, soid);
	}

	return mapping.findForward("signupPageListMethod");
    }
}
