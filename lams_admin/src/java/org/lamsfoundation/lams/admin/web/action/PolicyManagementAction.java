package org.lamsfoundation.lams.admin.web.action;

import java.io.IOException;
import java.text.DateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.policies.Policy;
import org.lamsfoundation.lams.policies.dto.UserPolicyConsentDTO;
import org.lamsfoundation.lams.policies.service.IPolicyService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.HtmlUtils;

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
	    HttpServletResponse response) throws JSONException, IOException {

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
	    handleListPage(request);
	    return mapping.findForward("policyList");
	} else if (StringUtils.equals(method, "edit")) {
	    return edit(mapping, form, request, response);
	} else if (StringUtils.equals(method, "save")) {
	    return save(mapping, form, request, response);
	} else if (StringUtils.equals(method, "displayUserConsents")) {
	    return displayUserConsents(mapping, form, request, response);
	} else if (StringUtils.equals(method, "getConsentsGridData")) {
	    return getConsentsGridData(mapping, form, request, response);
	} else if (StringUtils.equals(method, "viewPreviousVersions")) {
	    handleViewPreviousVersionsPage(request);
	    return mapping.findForward("policyList");
	} else if (StringUtils.equals(method, "togglePolicyStatus")) {
	    return togglePolicyStatus(mapping, form, request, response);
	} 

	return mapping.findForward("error");
    }
    
    /**
     * Prepare all data for the "list" page.
     */
    private void handleListPage(HttpServletRequest request) {
	Collection<Policy> policies = policyService.getPoliciesOfDistinctVersions();
	request.setAttribute("policies", policies);

	int userCount = userManagementService.getCountUsers();
	request.setAttribute("userCount", userCount);
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
			    userManagementService.save(policyFromFamily);
			}
		    }
		    
		} else {
		    Policy oldPolicy = policyService.getPolicyByUid((Long) policyUid);
		    if (Policy.STATUS_ACTIVE.equals(oldPolicy.getPolicyStateId())) {
			oldPolicy.setPolicyStateId(Policy.STATUS_INACTIVE);
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

	return mapping.findForward("policyListRedirect");
    }

    private ActionForward displayUserConsents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long policyUid = WebUtil.readLongParam(request, "policyUid");
	Policy policy = policyService.getPolicyByUid(policyUid);
	request.setAttribute("policy", policy);

	return mapping.findForward("userConsents");
    }
    
    /**
     * Prepares all data for "vewPreviousVersions" page.
     */
    private void handleViewPreviousVersionsPage(HttpServletRequest request) {
	long policyId = WebUtil.readLongParam(request, "policyId");
	List<Policy> previousVersions = policyService.getPreviousVersionsPolicies(policyId);
//	remove the first one from the list
//	if (previousVersion.size() > 1) {
//	    policyFamily.remove(policyFamily.size() - 1);
//	}
	request.setAttribute("policies", previousVersions);

	int userCount = userManagementService.getCountUsers();
	request.setAttribute("userCount", userCount);
	
	request.setAttribute("viewPreviousVersions", true);
    }
    
    private ActionForward togglePolicyStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	long policyUid = WebUtil.readLongParam(request, "policyUid");
	policyService.togglePolicyStatus(policyUid);

	Boolean isViewPreviousVersions = (Boolean) WebUtil.readBooleanParam(request, "viewPreviousVersions", false);
	if (isViewPreviousVersions) {
	    handleViewPreviousVersionsPage(request);
	} else {
	    handleListPage(request);
	}
	return mapping.findForward("policyTable");
    }
    
    /**
     */
    @SuppressWarnings("unchecked")
    public ActionForward getConsentsGridData(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	Long policyUid = WebUtil.readLongParam(request, "policyUid");
	Policy policy = policyService.getPolicyByUid(policyUid);
	
	// Getting the params passed in from the jqGrid
	int page = WebUtil.readIntParam(request, "page");
	int rowLimit = WebUtil.readIntParam(request, "rows");
	String sortOrder = WebUtil.readStrParam(request, "sord");
	String sortBy = WebUtil.readStrParam(request, "sidx", true);
	if (StringUtils.isEmpty(sortBy)) {
	    sortBy = "fullName";
	}
	String searchString = WebUtil.readStrParam(request, "fullName", true);
	
	List<UserPolicyConsentDTO> consentDtos = policyService.getConsentDtosByPolicy(policyUid, page-1, rowLimit, sortBy, sortOrder, searchString);
	int countUsers = userManagementService.getCountUsers(searchString);
	int totalPages = new Double(
		Math.ceil(new Integer(countUsers).doubleValue() / new Integer(rowLimit).doubleValue()))
			.intValue();
	
	JSONObject responcedata = new JSONObject();
	responcedata.put("total", "" + totalPages);
	responcedata.put("page", "" + page);
	responcedata.put("records", "" + countUsers);

	JSONArray rows = new JSONArray();
	for (UserPolicyConsentDTO consentDto : consentDtos) {
	    JSONArray userData = new JSONArray();
	    userData.put(consentDto.getUserID());
	    String firstName = consentDto.getFirstName() == null ? "" : consentDto.getFirstName();
	    String lastName = consentDto.getLastName() == null ? "" : consentDto.getLastName();
	    String fullName = HtmlUtils.htmlEscape(lastName) + " " + HtmlUtils.htmlEscape(firstName);
	    userData.put(fullName);
	    String consentedIcon = consentDto.isConsentGivenByUser()
		    ? "<i class=\"icon fa fa-check-circle text-success fa-fw\" title=\"Consent given\"></i>"
		    : "-";
	    userData.put(consentedIcon);
	    String dateAgreedOn = consentDto.getDateAgreedOn() == null ? ""
		    : DateUtil.convertToStringForJSON(consentDto.getDateAgreedOn(), request.getLocale());
	    userData.put(HtmlUtils.htmlEscape(dateAgreedOn));
	    
	    JSONObject cellobj = new JSONObject();
	    cellobj.put("id", "" + consentDto.getUserID());
	    cellobj.put("cell", userData);
	    rows.put(cellobj);
	}
	responcedata.put("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responcedata.toString()));
	return null;
    }
}
