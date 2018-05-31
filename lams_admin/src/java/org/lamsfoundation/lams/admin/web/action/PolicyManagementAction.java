package org.lamsfoundation.lams.admin.web.action;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Handles Policy management requests.
 *
 *@author Andrey Balan
 */
public class PolicyManagementAction extends Action {

    private static Logger log = Logger.getLogger(PolicyManagementAction.class);
    private static IPolicyService policyService = null;
    private static IUserManagementService userManagementService = null;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

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
	    return list(mapping, form, request, response);
	} else if (StringUtils.equals(method, "edit")) {
	    return edit(mapping, form, request, response);
	} else if (StringUtils.equals(method, "save")) {
	    return save(mapping, form, request, response);
	} else if (StringUtils.equals(method, "delete")) {
	    return delete(mapping, form, request, response);
	} else if (StringUtils.equals(method, "viewPreviousVersions")) {
	    return viewPreviousVersions(mapping, form, request, response);
	} else if (StringUtils.equals(method, "changeStatus")) {
	    return changeStatus(mapping, form, request, response);
	}

	return mapping.findForward("error");
    }
    
    private ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	List<Policy> policies = policyService.getPolicies();
	
	//calculate which policies have previous version instanses
	HashMap<Long, Integer> policyCount = new HashMap<Long, Integer>();
	for (Policy policy : policies) {
	    Long policyId = policy.getPolicyId();
	    int previousVersionsCount = policyCount.get(policyId) == null ? 0 : policyCount.get(policyId);
	    policyCount.put(policy.getPolicyId(), previousVersionsCount + 1);
	}
	
	//filter out older versioned policies
	HashMap<Long, Policy> filteredPolicies = new HashMap<Long, Policy>();
	for (Policy policy : policies) {
	    Long policyId = policy.getPolicyId();
	    boolean hasPreviousVersions = policyCount.get(policyId) != null && policyCount.get(policyId) > 1; 
	    policy.setPreviousVersions(hasPreviousVersions);
	    
	    if (filteredPolicies.containsKey(policyId)) {
		Policy alreadyAddedPolicy = filteredPolicies.get(policyId);
		Integer policyStateId = policy.getPolicyStateId();

		//active policy has priority
		if (Policy.STATUS_ACTIVE.equals(policyStateId)) {
		    filteredPolicies.put(policyId, policy);

		    //if both are inactive - newer has priority
		} else if (Policy.STATUS_INACTIVE.equals(policyStateId)
			&& Policy.STATUS_INACTIVE.equals(alreadyAddedPolicy.getPolicyStateId())
			&& policy.getLastModified().after(alreadyAddedPolicy.getLastModified())) {
		    filteredPolicies.put(policyId, policy);
		}

	    } else {
		filteredPolicies.put(policyId, policy);
	    }
	    
	}
	request.setAttribute("policies", filteredPolicies.values());

	int userCount = userManagementService.getCountUsers();
	request.setAttribute("userCount", userCount);
	return mapping.findForward("policyList");
    }

    private ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long policyUid = WebUtil.readLongParam(request, "policyUid", true);
	if (policyUid != null && policyUid > 0) {
	    Policy policy = policyService.getPolicyByUid(policyUid);
	    if (policy != null) {
		DynaActionForm policyForm = (DynaActionForm) form;
		policyForm.set("policyUid", policy.getUid());
		policyForm.set("policyId", policy.getPolicyId());
		policyForm.set("policyName", policy.getPolicyName());
		policyForm.set("summary", policy.getSummary());
		policyForm.set("fullPolicy", policy.getFullPolicy());
		policyForm.set("policyTypeId", policy.getPolicyTypeId());
		policyForm.set("version", policy.getVersion());
		policyForm.set("policyStateId", policy.getPolicyStateId());
		request.setAttribute("policyForm", policyForm);
	    }
	}
	return mapping.findForward("editPolicy");
    }

    private ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DynaActionForm policyForm = (DynaActionForm) form;
	String currentDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

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
	if (policyUid != null && (Long) policyUid > 0) {
	    policy = policyService.getPolicyByUid((Long) policyUid);
	    
	   //if it's not a minor change - then instantiate a new child policy
	    if (!isMinorChange) {
		Policy oldPolicy = policy;

		//if the new policy has Active status then set the old one to Inactive
		if (policyStateId.equals(Policy.STATUS_ACTIVE)) {
		    oldPolicy.setPolicyStateId(Policy.STATUS_INACTIVE);
		    oldPolicy.setLastModified(new Date());
		    userManagementService.save(oldPolicy);
		}

		//if version was not changed by the user - append current date
		if (oldPolicy.getVersion().equals(version)) {
		    version += " " + currentDate;
		}

		policy = new Policy();
		policy.setPolicyId(oldPolicy.getPolicyId());
	    }
	    
	//create a new policy
	} else {
	    policy = new Policy();
	    // generate Unique long ID
	    Long policyId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
	    policy.setPolicyId(policyId);
	}

	//set default version if it's empty
	if (StringUtils.isEmpty(version)) {
	    version = currentDate;
	}
	policy.setVersion(version);
	policy.setSummary(policyForm.getString("summary"));
	policy.setFullPolicy(policyForm.getString("fullPolicy"));
	policy.setPolicyTypeId((Integer) policyForm.get("policyTypeId"));
	policy.setPolicyStateId(policyStateId);
	policy.setPolicyName(policyForm.getString("policyName"));
	//set created user
	Integer loggeduserId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
	User createdBy = (User) userManagementService.findById(User.class, loggeduserId);
	policy.setCreatedBy(createdBy);
	policy.setLastModified(new Date());
	userManagementService.save(policy);

	return mapping.findForward("policyListMethod");
//	    }

//	return mapping.findForward("editPolicy");
    }

    private ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long policyUid = WebUtil.readLongParam(request, "policyUid");
	if (policyUid != null && policyUid > 0) {
	    userManagementService.deleteById(Policy.class, policyUid);
	}

	return mapping.findForward("policyListMethod");
    }
    
    private ActionForward viewPreviousVersions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	long policyId = WebUtil.readLongParam(request, "policyId");
	List<Policy> policies = policyService.getPreviousVersionsPolicies(policyId);
	request.setAttribute("policies", policies);

	int userCount = userManagementService.getCountUsers();
	request.setAttribute("userCount", userCount);
	request.setAttribute("viewPreviousVersions", "true");
	return mapping.findForward("policyList");
    }
    
    private ActionForward changeStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	long policyUid = WebUtil.readLongParam(request, "policyUid");
	int newStatus = WebUtil.readIntParam(request, "newStatus");
	policyService.changePolicyStatus(policyUid, newStatus);

	return list(mapping, form, request, response);
    }
}
