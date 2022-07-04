package org.lamsfoundation.lams.admin.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.web.form.SignupManagementForm;
import org.lamsfoundation.lams.signup.model.SignupOrganisation;
import org.lamsfoundation.lams.signup.service.ISignupService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/signupManagement")
public class SignupManagementController {
    private static Logger log = Logger.getLogger(SignupManagementController.class);

    @Autowired
    private ISignupService signupService = null;
    @Autowired
    private IUserManagementService userManagementService = null;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping("/start")
    public String execute(HttpServletRequest request) {
	List<SignupOrganisation> signupOrganisations = signupService.getSignupOrganisations();
	request.setAttribute("signupOrganisations", signupOrganisations);

	return "signupmanagement/list";
    }

    @RequestMapping(path = "/edit")
    public String edit(@ModelAttribute("signupForm") SignupManagementForm signupForm, HttpServletRequest request)
	    throws Exception {

	Integer soid = WebUtil.readIntParam(request, "soid", false);

	if (soid != null && soid > 0) {
	    SignupOrganisation signup = (SignupOrganisation) userManagementService.findById(SignupOrganisation.class,
		    soid);
	    if (signup != null) {
		signupForm.setSignupOrganisationId(signup.getSignupOrganisationId());
		signupForm.setOrganisationId(signup.getOrganisation().getOrganisationId());
		signupForm.setAddToLessons(signup.getAddToLessons());
		signupForm.setAddAsStaff(signup.getAddAsStaff());
		signupForm.setAddWithAuthor(signup.getAddWithAuthor());
		signupForm.setAddWithMonitor(signup.getAddWithMonitor());
		signupForm.setEmailVerify(signup.getEmailVerify());
		signupForm.setCourseKey(signup.getCourseKey());
		signupForm.setBlurb(signup.getBlurb());
		signupForm.setDisabled(signup.getDisabled());
		signupForm.setLoginTabActive(signup.getLoginTabActive());
		signupForm.setContext(signup.getContext());
		request.setAttribute("signupForm", signupForm);

		List<Organisation> organisations = signupService.getOrganisationCandidates();
		request.setAttribute("organisations", organisations);

		return "signupmanagement/add";
	    }
	}
	return null;
    }

    @RequestMapping(path = "/add")
    public String add(@ModelAttribute("signupForm") SignupManagementForm signupForm, HttpServletRequest request)
	    throws Exception {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	// check if form submitted
	if (signupForm.getOrganisationId() != null && signupForm.getOrganisationId() > 0) {

	    // validate
	    if (!StringUtils.equals(signupForm.getCourseKey(), signupForm.getConfirmCourseKey())) {
		errorMap.add("courseKey", messageService.getMessage("error.course.keys.unequal"));
	    }
	    if (signupService.contextExists(signupForm.getSignupOrganisationId(), signupForm.getContext())) {
		errorMap.add("context", messageService.getMessage("error.context.exists"));
	    }

	    if (!errorMap.isEmpty()) {
		request.setAttribute("errorMap", errorMap);
	    } else {
		// proceed
		SignupOrganisation signup;
		if (signupForm.getSignupOrganisationId() != null && signupForm.getSignupOrganisationId() > 0) {
		    // form was editing existing
		    signup = (SignupOrganisation) userManagementService.findById(SignupOrganisation.class,
			    signupForm.getSignupOrganisationId());
		} else {
		    signup = new SignupOrganisation();
		    signup.setCreateDate(new Date());
		}
		signup.setAddToLessons(signupForm.isAddToLessons());
		signup.setAddAsStaff(signupForm.isAddAsStaff());
		signup.setAddWithAuthor(signupForm.isAddWithAuthor());
		signup.setAddWithMonitor(signupForm.isAddWithMonitor());
		signup.setEmailVerify(signupForm.getEmailVerify());
		signup.setDisabled(signupForm.isDisabled());
		signup.setLoginTabActive(signupForm.isLoginTabActive());
		signup.setOrganisation((Organisation) userManagementService.findById(Organisation.class,
			signupForm.getOrganisationId()));
		signup.setCourseKey(signupForm.getCourseKey());
		signup.setBlurb(signupForm.getBlurb());
		signup.setContext(signupForm.getContext());
		userManagementService.save(signup);
		SignupManagementController.log.warn("Signup page added/updated! Coursekey: " + signupForm.getCourseKey());

		return "forward:/signupManagement/start.do";
	    }
	} else {
	    // form not submitted, default values
	    signupForm.setBlurb("Register your LAMS account for this group using the form below.");
	}

	List<Organisation> organisations = signupService.getOrganisationCandidates();
	request.setAttribute("organisations", organisations);

	return "signupmanagement/add";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(HttpServletRequest request) throws Exception {
	Integer soid = WebUtil.readIntParam(request, "soid");

	if (soid != null && soid > 0) {
		SignupManagementController.log.warn("Signup page deleted! soid: " + soid);
	    userManagementService.deleteById(SignupOrganisation.class, soid);
	}

	return "redirect:/signupManagement/start.do";
    }
}
