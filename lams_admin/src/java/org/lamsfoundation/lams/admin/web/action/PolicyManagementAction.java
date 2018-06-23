package org.lamsfoundation.lams.admin.web.action;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
import org.lamsfoundation.lams.admin.web.dto.UserPolicyConsentDTO;
import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.PolicyConsent;
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
	} else if (StringUtils.equals(method, "displayUserConsents")) {
	    return displayUserConsents(mapping, form, request, response);
	} else if (StringUtils.equals(method, "viewPreviousVersions")) {
	    return viewPreviousVersions(mapping, form, request, response);
	} else if (StringUtils.equals(method, "changeStatus")) {
	    return changeStatus(mapping, form, request, response);
	}

	return mapping.findForward("error");
    }
    
    private ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	List<Policy> policies = policyService.getAllPoliciesWithUserConsentsCount();
	
	//calculate which policies have previous version instances
	HashMap<Long, Integer> policyCount = new LinkedHashMap<Long, Integer>();
	for (Policy policy : policies) {
	    Long policyId = policy.getPolicyId();
	    int previousVersionsCount = policyCount.get(policyId) == null ? 0 : policyCount.get(policyId);
	    policyCount.put(policy.getPolicyId(), previousVersionsCount + 1);
	}
	
	//filter out older versioned policies
	HashMap<Long, Policy> filteredPolicies = new LinkedHashMap<Long, Policy>();
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

		    //if neither are active - newer has priority
		} else if (!Policy.STATUS_ACTIVE.equals(alreadyAddedPolicy.getPolicyStateId())
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
	boolean isEditingPreviousVersion = WebUtil.readBooleanParam(request, "isEditingPreviousVersion", false);
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
		policyForm.set("editingPreviousVersion", isEditingPreviousVersion);
		request.setAttribute("policyForm", policyForm);
	    }
	}
	return mapping.findForward("editPolicy");
    }

    private ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	DynaActionForm policyForm = (DynaActionForm) form;
	String currentDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

	Object policyUid = policyForm.get("policyUid");
	String version = policyForm.getString("version");
	Integer policyStateId = (Integer) policyForm.get("policyStateId");
	Boolean isMinorChange = (Boolean) policyForm.get("minorChange");
	Boolean isEditingPreviousVersion = (Boolean) policyForm.get("editingPreviousVersion");

	Policy policy;
	// edit existing policy only in case of minor change
	if (isMinorChange) {
	    Policy oldPolicy = policyService.getPolicyByUid((Long) policyUid);
	    policy = oldPolicy;

	} else {
	    //if it's not a minor change - then instantiate a new child policy
	    policy = new Policy();

	    //set policy's policyId
	    Long policyId;
	    if (policyUid != null && (Long) policyUid > 0) {
		Policy oldPolicy = policyService.getPolicyByUid((Long) policyUid);
		policyId = oldPolicy.getPolicyId();
	    } else {
		// generate Unique long ID
		policyId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
	    }
	    policy.setPolicyId(policyId);
	    
	    //set policy's version
	    if (policyUid != null && (Long) policyUid > 0) {
		Policy oldPolicy = policyService.getPolicyByUid((Long) policyUid);
		//if version was not changed by the user - append current date
		if (oldPolicy.getVersion().equals(version)) {
		    version += " " + currentDate;
		}
	    }

	    //take care about old policy/policies: if the new policy has Active status then set the old one(s) to Inactive
	    if (policyUid != null && (Long) policyUid > 0 && policyStateId.equals(Policy.STATUS_ACTIVE)) {

		if (isEditingPreviousVersion) {
		    List<Policy> policyFamily = policyService.getPreviousVersionsPolicies(policyId);
		    for (Policy policyFromFamily : policyFamily) {
			if (!policyFromFamily.getUid().equals(policyUid)
				&& Policy.STATUS_ACTIVE.equals(policyFromFamily.getPolicyStateId())) {
			    policyFromFamily.setPolicyStateId(Policy.STATUS_INACTIVE);
			    policyFromFamily.setLastModified(new Date());
			    userManagementService.save(policyFromFamily);
			}
		    }
		    
		} else {
		    Policy oldPolicy = policyService.getPolicyByUid((Long) policyUid);
		    if (Policy.STATUS_ACTIVE.equals(oldPolicy.getPolicyStateId())) {
			oldPolicy.setPolicyStateId(Policy.STATUS_INACTIVE);
			oldPolicy.setLastModified(new Date());
			userManagementService.save(oldPolicy);
		    }
		}
	    }

	}

	//set default version, if it's empty
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
    }

    private ActionForward displayUserConsents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long policyUid = WebUtil.readLongParam(request, "policyUid");
	Policy policy = policyService.getPolicyByUid(policyUid);
	Set<PolicyConsent> consents = policy.getConsents();
	request.setAttribute("policy", policy);
	
	List<User> users = userManagementService.getAllUsers();
	LinkedList<UserPolicyConsentDTO> userPolicyConsents = new LinkedList<UserPolicyConsentDTO>();
	for (User user : users) {
	    UserPolicyConsentDTO userPolicyConsent = new UserPolicyConsentDTO(user.getUserId(), user.getFirstName(), user.getLastName(), user.getLogin());
	    
	    for (PolicyConsent consent : consents) {
		if (consent.getUser().getUserId().equals(user.getUserId())) {
		    userPolicyConsent.setConsentGivenByUser(true);
		    userPolicyConsent.setDateAgreedOn(consent.getDateAgreedOn());
		}
	    }
	    userPolicyConsents.add(userPolicyConsent);
	}
	request.setAttribute("userPolicyConsents", userPolicyConsents);

	return mapping.findForward("userConsents");
    }
    
    private ActionForward viewPreviousVersions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	long policyId = WebUtil.readLongParam(request, "policyId");
	List<Policy> policyFamily = policyService.getPreviousVersionsPolicies(policyId);
	//remove the first one from the list
	if (policyFamily.size() > 1) {
//	    policyFamily.remove(policyFamily.size() - 1);
	}
	request.setAttribute("policies", policyFamily);

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

	return mapping.findForward("policyListMethod");
    }
}
