package org.lamsfoundation.lams.web.action;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.PolicyConsent;
import org.lamsfoundation.lams.policies.PolicyDTO;
import org.lamsfoundation.lams.policies.service.IPolicyService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

/**
 * Shows policies user has to consent to. Stores PolicyConsents once user agrees to them.
 * 
 * @author Andrey Balan
 */
public class PolicyConsentsAction extends Action {
    private static IPolicyService policyService = null;
    private static IUserManagementService userManagementService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (policyService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    policyService = (IPolicyService) wac.getBean("policyService");
	}
	if (userManagementService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    userManagementService = (IUserManagementService) wac.getBean("userManagementService");
	}

	String method = WebUtil.readStrParam(request, "method", true);
	if (StringUtils.equals(method, "consent") || isCancelled(request)) {
	    return consent(mapping, form, request, response);
	}
	
	Integer loggedUserId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
	
	List<PolicyDTO> policyDtos = policyService.getPolicyDtosByUser(loggedUserId);
	request.setAttribute("policies", policyDtos);
	
	return mapping.findForward("policyConsents");
    }
    
    private ActionForward consent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	
	UserDTO userDto = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	User user = userManagementService.getUserByLogin(userDto.getLogin());
	
	List<PolicyDTO> policyDtos = policyService.getPolicyDtosByUser(user.getUserId());
	//create all missing policy consents
	for (PolicyDTO policyDto : policyDtos) {
	    if (!policyDto.isConsentedByUser()) {
		PolicyConsent consent = new PolicyConsent();
		Policy policy = policyService.getPolicyByUid(policyDto.getUid());
		consent.setPolicy(policy);
		consent.setUser(user);
		userManagementService.save(consent);
	    }
	}

//	    ActionMessages errors = new ActionMessages();
//
//	    // validate
//	    if (!StringUtils.equals(policyForm.getString("courseKey"), policyForm.getString("confirmCourseKey"))) {
//		errors.add("courseKey", new ActionMessage("error.course.keys.unequal"));
//	    }
//	    if (policyService.contextExists((Integer) policyForm.get("signupOrganisationId"),
//		    policyForm.getString("context"))) {
//		errors.add("context", new ActionMessage("error.context.exists"));
//	    }
//
//	    if (!errors.isEmpty()) {
//		saveErrors(request, errors);
//	    } else {
	



	return mapping.findForward("index");
    }
}
