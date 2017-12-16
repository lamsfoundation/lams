/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.admin.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.OrgManageForm;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.service.UserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * <p>
 * <a href="OrgManageAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class OrgManageAction extends LamsDispatchAction {

    private static IUserManagementService userManagementService;

    @SuppressWarnings("unchecked")
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	initServices();

	// Get organisation whose child organisations we will populate the OrgManageForm with
	Integer orgId = WebUtil.readIntParam(request, "org", true);
	if (orgId == null) {
	    orgId = (Integer) request.getAttribute("org");
	}
	if ((orgId == null) || (orgId == 0)) {
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing organisation ID");
	    return null;
	}

	// get logged in user's id
	Integer userId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
	ISecurityService securityService = AdminServiceProxy.getSecurityService(getServlet().getServletContext());

	Organisation org = null;
	boolean isRootOrganisation = false;
	Organisation rootOrganisation = userManagementService.getRootOrganisation();
	if (orgId.equals(rootOrganisation.getOrganisationId())) {
	    org = rootOrganisation;
	    isRootOrganisation = true;
	} else {
	    org = (Organisation) userManagementService.findById(Organisation.class, orgId);
	}

	// check if user is allowed to view and edit groups
	if (!request.isUserInRole(Role.SYSADMIN) && !(isRootOrganisation
		? request.isUserInRole(Role.GROUP_ADMIN) || request.isUserInRole(Role.GROUP_MANAGER)
		: securityService.hasOrgRole(orgId, userId, new String[] { Role.GROUP_ADMIN, Role.GROUP_MANAGER },
			"manage courses", false))) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a manager or admin in the organisation");
	    return null;
	}

	// get number of users figure
	// TODO use hql that does a count instead of getting whole objects
	int numUsers = org == rootOrganisation ? userManagementService.getCountUsers()
		: userManagementService.getUsersFromOrganisation(orgId).size();
	String key = org == rootOrganisation ? "label.users.in.system" : "label.users.in.group";
	MessageService messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
	request.setAttribute("numUsers", messageService.getMessage(key, new String[] { String.valueOf(numUsers) }));

	// Set OrgManageForm
	OrgManageForm orgManageForm = (OrgManageForm) form;
	if (orgManageForm == null) {
	    orgManageForm = new OrgManageForm();
	    orgManageForm.setStateId(OrganisationState.ACTIVE);
	} else if (orgManageForm.getStateId() == null) {
	    orgManageForm.setStateId(OrganisationState.ACTIVE);
	}
	orgManageForm.setParentId(orgId);
	orgManageForm.setParentName(org.getName());
	orgManageForm.setType(org.getOrganisationType().getOrganisationTypeId());

	// Get list of child organisations depending on requestor's role and the organisation's type
	if (orgManageForm.getType().equals(OrganisationType.CLASS_TYPE)) {
	    // display class info, with parent group's 'courseAdminCan...' permissions.
	    // note the org is not saved, properties set only for passing to view component.
	    Organisation pOrg = org.getParentOrganisation();
	    org.setCourseAdminCanAddNewUsers(pOrg.getCourseAdminCanAddNewUsers());
	    org.setCourseAdminCanBrowseAllUsers(pOrg.getCourseAdminCanBrowseAllUsers());
	    org.setCourseAdminCanChangeStatusOfCourse(pOrg.getCourseAdminCanChangeStatusOfCourse());
	    request.setAttribute("org", org);

	    // display parent org breadcrumb link
	    request.setAttribute("parentGroupName", pOrg.getName());
	    request.setAttribute("parentGroupId", pOrg.getOrganisationId());
	} else {
	    request.setAttribute("OrgManageForm", orgManageForm);

	    // display org info
	    request.setAttribute("org", org);
	}

	// let the jsp know whether to display links
	request.setAttribute("createGroup",
		request.isUserInRole(Role.SYSADMIN) || userManagementService.isUserGlobalGroupAdmin());
	request.setAttribute("editGroup", true);
	request.setAttribute("manageGlobalRoles", request.isUserInRole(Role.SYSADMIN));
	return mapping.findForward("orglist");
    }

    /**
     * Returns list of organisations for .
     */
    public ActionForward getOrgs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	initServices();

	Integer parentOrgId = WebUtil.readIntParam(request, "parentOrgId");
	Integer stateId = WebUtil.readIntParam(request, "stateId");
	Integer typeIdParam = WebUtil.readIntParam(request, "type");
	// the organisation type of the children
	Integer typeId = (typeIdParam.equals(OrganisationType.ROOT_TYPE) ? OrganisationType.COURSE_TYPE
		: OrganisationType.CLASS_TYPE);
	String searchString = WebUtil.readStrParam(request, "fcol[1]", true);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSort1 = WebUtil.readIntParam(request, "column[0]", true);
	Integer isSort2 = WebUtil.readIntParam(request, "column[1]", true);
	Integer isSort3 = WebUtil.readIntParam(request, "column[2]", true);
	Integer isSort4 = WebUtil.readIntParam(request, "column[3]", true);

	String sortBy = "";
	String sortOrder = "";
	if (isSort1 != null) {
	    sortBy = "id";
	    sortOrder = isSort1.equals(0) ? "ASC" : "DESC";

	} else if (isSort2 != null) {
	    sortBy = "name";
	    sortOrder = isSort2.equals(0) ? "ASC" : "DESC";

	} else if (isSort3 != null) {
	    sortBy = "code";
	    sortOrder = isSort3.equals(0) ? "ASC" : "DESC";

	} else if (isSort4 != null) {
	    sortBy = "createDate";
	    sortOrder = isSort4.equals(0) ? "ASC" : "DESC";

	}

	List<Organisation> organisations = userManagementService.getPagedCourses(parentOrgId, typeId, stateId, page,
		size, sortBy, sortOrder, searchString);

	JSONObject responcedata = new JSONObject();
	responcedata.put("total_rows", userManagementService.getCountCoursesByParentCourseAndTypeAndState(parentOrgId,
		typeId, stateId, searchString));

	JSONArray rows = new JSONArray();
	for (Organisation organisation : organisations) {

	    JSONObject responseRow = new JSONObject();
	    responseRow.put("id", organisation.getOrganisationId());
	    String orgName = organisation.getName() == null ? "" : organisation.getName();
	    responseRow.put("name", StringEscapeUtils.escapeHtml(orgName));
	    String orgCode = organisation.getCode() == null ? "" : organisation.getCode();
	    responseRow.put("code", StringEscapeUtils.escapeHtml(orgCode));
	    String orgCreateDate = organisation.getCreateDate() == null ? ""
		    : FileUtil.EXPORT_TO_SPREADSHEET_TITLE_DATE_FORMAT.format(organisation.getCreateDate());
	    responseRow.put("createDate", orgCreateDate);

	    rows.put(responseRow);
	}
	responcedata.put("rows", rows);
	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responcedata.toString()));
	return null;
    }

    private void initServices() {
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getWebApplicationContext(getServlet().getServletContext());
	    userManagementService = (UserManagementService) ctx.getBean("userManagementService");
	}
    }
}