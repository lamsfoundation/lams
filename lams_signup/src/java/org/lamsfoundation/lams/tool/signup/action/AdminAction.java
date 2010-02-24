package org.lamsfoundation.lams.tool.signup.action;

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
import org.lamsfoundation.lams.tool.signup.model.SignupOrganisation;
import org.lamsfoundation.lams.tool.signup.service.SignupService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @struts:action path="/admin" name="AdminForm" scope="request"
 *                validate="false" parameter="method"
 * 
 * @struts:action-forward name="admin-list" path=".admin-list"
 * @struts:action-forward name="admin-add" path=".admin-add"
 * @struts:action-forward name="admin" path="/admin.do?method=list"
 */
public class AdminAction extends Action {

    private static Logger log = Logger.getLogger(AdminAction.class);
    private static SignupService signupService = null;

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    if (signupService == null) {
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
			.getServletContext());
		signupService = (SignupService) wac.getBean("signupService");
	    }

	    String method = WebUtil.readStrParam(request, "method", true);

	    if (StringUtils.equals(method, "edit")) {
		return edit(mapping, form, request, response);
	    } else if (StringUtils.equals(method, "add")) {
		return add(mapping, form, request, response);
	    } else if (StringUtils.equals(method, "delete")) {
		return delete(mapping, form, request, response);
	    } else if (StringUtils.equals(method, "list")) {
		// do nothing
	    }

	    List signupOrganisations = signupService.getSignupDAO().getSignupOrganisations();
	    request.setAttribute("signupOrganisations", signupOrganisations);
	} catch (Exception e) {
	    log.error(e.getMessage(), e);
	    request.setAttribute("error", e.getMessage());
	}

	return mapping.findForward("admin-list");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer soid = WebUtil.readIntParam(request, "soid", false);

	if (soid != null && soid > 0) {
	    SignupOrganisation signup = (SignupOrganisation) signupService.getSignupDAO().find(
		    SignupOrganisation.class, soid);
	    if (signup != null) {
		DynaActionForm adminForm = (DynaActionForm) form;
		adminForm.set("signupOrganisationId", signup.getSignupOrganisationId());
		adminForm.set("organisationId", signup.getOrganisation().getOrganisationId());
		adminForm.set("addToLessons", signup.getAddToLessons());
		adminForm.set("addAsStaff", signup.getAddAsStaff());
		adminForm.set("addWithAuthor", signup.getAddWithAuthor());
		adminForm.set("addWithMonitor", signup.getAddWithMonitor());
		adminForm.set("courseKey", signup.getCourseKey());
		adminForm.set("blurb", signup.getBlurb());
		adminForm.set("disabled", signup.getDisabled());
		adminForm.set("context", signup.getContext());

		List organisations = signupService.getSignupDAO().getOrganisationCandidates();
		request.setAttribute("organisations", organisations);

		return mapping.findForward("admin-add");
	    }
	}
	return null;
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	DynaActionForm adminForm = (DynaActionForm) form;

	// check if form submitted
	if (adminForm.get("organisationId") != null && (Integer) adminForm.get("organisationId") > 0) {
	    ActionMessages errors = new ActionMessages();

	    // validate
	    if (!StringUtils.equals(adminForm.getString("courseKey"), adminForm.getString("confirmCourseKey"))) {
		errors.add("courseKey", new ActionMessage("error.course.keys.unequal"));
	    }
	    if (signupService.getSignupDAO().contextExists((Integer) adminForm.get("signupOrganisationId"),
		    adminForm.getString("context"))) {
		errors.add("context", new ActionMessage("error.context.exists"));
	    }

	    if (!errors.isEmpty()) {
		saveErrors(request, errors);
	    } else {
		// proceed
		SignupOrganisation signup;
		if (adminForm.get("signupOrganisationId") != null
			&& (Integer) adminForm.get("signupOrganisationId") > 0) {
		    // form was editing existing
		    signup = (SignupOrganisation) signupService.getSignupDAO().find(SignupOrganisation.class,
			    (Integer) adminForm.get("signupOrganisationId"));
		} else {
		    signup = new SignupOrganisation();
		    signup.setCreateDate(new Date());
		}
		signup.setAddToLessons((Boolean) adminForm.get("addToLessons"));
		signup.setAddAsStaff((Boolean) adminForm.get("addAsStaff"));
		signup.setAddWithAuthor((Boolean)adminForm.get("addWithAuthor"));
		signup.setAddWithMonitor((Boolean)adminForm.get("addWithMonitor"));
		signup.setDisabled((Boolean) adminForm.get("disabled"));
		signup.setOrganisation((Organisation) signupService.getSignupDAO().find(Organisation.class,
			(Integer) adminForm.get("organisationId")));
		signup.setCourseKey(adminForm.getString("courseKey"));
		signup.setBlurb(adminForm.getString("blurb"));
		signup.setContext(adminForm.getString("context"));
		signupService.getSignupDAO().insertOrUpdate(signup);

		return mapping.findForward("admin");
	    }
	} else {
	    // form not submitted, default values
	    adminForm.set("blurb", "Register your LAMS account for this group using the form below.");
	}

	List organisations = signupService.getSignupDAO().getOrganisationCandidates();
	request.setAttribute("organisations", organisations);

	return mapping.findForward("admin-add");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer soid = WebUtil.readIntParam(request, "soid");

	if (soid != null && soid > 0) {
	    signupService.getSignupDAO().deleteById(SignupOrganisation.class, soid);
	}

	return mapping.findForward("admin");
    }
}
