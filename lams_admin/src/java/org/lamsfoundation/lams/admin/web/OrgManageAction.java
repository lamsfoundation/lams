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
package org.lamsfoundation.lams.admin.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.OrgManageForm;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @version
 *
 * <p>
 * <a href="OrgManageAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 20:29:13 on 2006-6-5
 */

/**
 * struts doclets
 * 
 * @struts:action path="/orgmanage" name="OrgManageForm" input=".orglist" scope="request" validate="false"
 *
 * @struts:action-forward name="orglist" path=".orglist"
 */
public class OrgManageAction extends Action {

    @SuppressWarnings("unchecked")
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
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
	IUserManagementService userManagementService = AdminServiceProxy.getService(getServlet().getServletContext());

	Organisation org = null;
	boolean isRootOrganisation = false;
	Organisation rootOrganisation = userManagementService.getRootOrganisation();
	if (orgId.equals(rootOrganisation.getOrganisationId())) {
	    org = rootOrganisation;
	    isRootOrganisation = true;
	} else {
	    org = (Organisation) userManagementService.findById(Organisation.class, orgId);
	}

	boolean isGlobalManager = request.isUserInRole(Role.SYSADMIN)
		&& !userManagementService.isUserGlobalGroupAdmin();

	// check if user is allowed to view and edit groups
	if (!isGlobalManager
		&& !(isRootOrganisation ? request.isUserInRole(Role.GROUP_ADMIN)
			|| request.isUserInRole(Role.GROUP_MANAGER) : securityService.hasOrgRole(orgId, userId,
			new String[] { Role.GROUP_ADMIN, Role.GROUP_MANAGER }, "manage courses", false))) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a manager or admin in the organisation");
	    return null;
	}

	// get number of users figure
	// TODO use hql that does a count instead of getting whole objects
	int numUsers = org == rootOrganisation ? userManagementService.getCountUsers() : userManagementService
		.getUsersFromOrganisation(orgId).size();
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
	    List<OrgManageBean> orgManageBeans = new ArrayList<OrgManageBean>();
	    // the organisation type of the children
	    Integer typeId = (orgManageForm.getType().equals(OrganisationType.ROOT_TYPE) ? OrganisationType.COURSE_TYPE
		    : OrganisationType.CLASS_TYPE);

	    HashMap<String, Object> properties = new HashMap<String, Object>();
	    properties.put("organisationType.organisationTypeId", typeId);
	    properties.put("organisationState.organisationStateId", orgManageForm.getStateId());
	    List<Organisation> organisations = userManagementService.findByProperties(Organisation.class, properties);

	    for (Organisation organisation : organisations) {
		Organisation parentOrg = (typeId.equals(OrganisationType.CLASS_TYPE)) ? organisation
			.getParentOrganisation() : organisation;
		// do not list this org if it is not a child of the requested parent
		if (typeId.equals(OrganisationType.CLASS_TYPE) && !parentOrg.getOrganisationId().equals(orgId)) {
		    continue;
		}

		OrgManageBean orgManageBean = new OrgManageBean();
		BeanUtils.copyProperties(orgManageBean, organisation);
		orgManageBean.setStatus(organisation.getOrganisationState().getDescription());
		orgManageBean.setEditable(true);
		orgManageBeans.add(orgManageBean);
	    }
	    Collections.sort(orgManageBeans);
	    orgManageForm.setOrgManageBeans(orgManageBeans);
	    request.setAttribute("OrgManageForm", orgManageForm);

	    // display org info
	    request.setAttribute("org", org);
	}

	// let the jsp know whether to display links
	request.setAttribute("createGroup", isGlobalManager);
	request.setAttribute("editGroup", true);
	request.setAttribute("manageGlobalRoles", request.isUserInRole(Role.SYSADMIN));
	return mapping.findForward("orglist");
    }
}