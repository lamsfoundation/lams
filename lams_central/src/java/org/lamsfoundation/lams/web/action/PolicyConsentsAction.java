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
 * Shows policies user has to consent to. Also stores them once user agrees.
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
	if (StringUtils.equals(method, "list") || isCancelled(request)) {
	    return consent(mapping, form, request, response);
	}
	
	List<Policy> policies = policyService.getActivePolicies();
	request.setAttribute("policies", policies);
	
//	User loggedInUser = userManagementService.getUserByLogin(request.getRemoteUser());
	
	return mapping.findForward("policyConsents");
    }
    
    private ActionForward consent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DynaActionForm policyForm = (DynaActionForm) form;

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
	Object policyUid = policyForm.get("policyUid");
	String version = policyForm.getString("version");
	Integer policyStateId = (Integer) policyForm.get("policyStateId");
	Boolean isMinorChange = (Boolean) policyForm.get("minorChange");
	


	Policy policy;
	// edit existing policy
//	if (policyUid != null && (Long) policyUid > 0) {
//	    policy = policyService.getPolicyByUid((Long) policyUid);
//	    
//	   //if it's not a minor change - then instantiate a new child policy
//	    if (!isMinorChange) {
//		Policy oldPolicy = policy;
//
//		//if the new policy has Active status then set the old one to Inactive
//		if (policyStateId.equals(Policy.STATUS_ACTIVE)) {
//		    oldPolicy.setPolicyStateId(Policy.STATUS_INACTIVE);
//		    oldPolicy.setLastModified(new Date());
//		    userManagementService.save(oldPolicy);
//		}
//
//		//if version was not changed by the user - append current date
//		if (oldPolicy.getVersion().equals(version)) {
//		    version += " " + currentDate;
//		}
//
//		policy = new Policy();
//		policy.setPolicyId(oldPolicy.getPolicyId());
//	    }
//	    
//	//create a new policy
//	} else {
//	    policy = new Policy();
//	    // generate Unique long ID
//	    Long policyId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
//	    policy.setPolicyId(policyId);
//	}
//
//	//set default version if it's empty
//	if (StringUtils.isEmpty(version)) {
//	    version = currentDate;
//	}
//	policy.setVersion(version);
//	policy.setSummary(policyForm.getString("summary"));
//	policy.setFullPolicy(policyForm.getString("fullPolicy"));
//	policy.setPolicyTypeId((Integer) policyForm.get("policyTypeId"));
//	policy.setPolicyStateId(policyStateId);
//	policy.setPolicyName(policyForm.getString("policyName"));
//	//set created user
//	Integer loggeduserId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
//	User createdBy = (User) userManagementService.findById(User.class, loggeduserId);
//	policy.setCreatedBy(createdBy);
//	policy.setLastModified(new Date());
//	userManagementService.save(policy);

	return mapping.findForward("policyListMethod");
//	    }

//	return mapping.findForward("editPolicy");
    }
}
