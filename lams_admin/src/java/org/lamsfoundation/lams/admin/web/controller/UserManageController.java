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

package org.lamsfoundation.lams.admin.web.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.dto.UserListDTO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserManageBean;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Jun-Dir Liew
 *
 * Created at 13:51:51 on 9/06/2006
 */

/**
 *
 *
 *
 *
 */
@Controller
public class UserManageController {

    private static final Logger log = Logger.getLogger(UserManageController.class);
    private static IUserManagementService service;
    private static MessageService messageService;

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/usermanage")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

	service = AdminServiceProxy.getService(applicationContext.getServletContext());
	messageService = AdminServiceProxy.getMessageService(applicationContext.getServletContext());

	// get id of org to list users for
	Integer orgId = WebUtil.readIntParam(request, "org", true);
	if (orgId == null) {
	    orgId = (Integer) request.getAttribute("org");
	}
	if ((orgId == null) || (orgId <= 0)) {
	    return forwardError(request, "error.org.invalid");
	}
	log.debug("orgId: " + orgId);

	// get org name
	Organisation organisation = (Organisation) service.findById(Organisation.class, orgId);
	if (organisation == null) {
	    return forwardError(request, "error.org.invalid");
	}
	String orgName = organisation.getName();
	log.debug("orgName: " + orgName);

	Organisation pOrg = organisation.getParentOrganisation();
	if (pOrg != null) {
	    request.setAttribute("pOrgId", pOrg.getOrganisationId());
	    request.setAttribute("pOrgName", pOrg.getName());
	}
	OrganisationType orgType = organisation.getOrganisationType();
	request.setAttribute("orgType", orgType.getOrganisationTypeId());

	// create form object
	UserListDTO userManageForm = new UserListDTO();

	Integer userId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();
	Organisation orgOfCourseAdmin = (orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) ? pOrg
		: organisation;
	// check permission
	Integer rootOrgId = service.getRootOrganisation().getOrganisationId();
	if (request.isUserInRole(Role.SYSADMIN) || (service.isUserGlobalGroupAdmin() && !orgId.equals(rootOrgId))) {
	    userManageForm.setCourseAdminCanAddNewUsers(true);
	    userManageForm.setCourseAdminCanBrowseAllUsers(true);
	    request.setAttribute("canDeleteUser", true);
	} else if ((service.isUserInRole(userId, orgOfCourseAdmin.getOrganisationId(), Role.GROUP_ADMIN)
		|| service.isUserInRole(userId, orgOfCourseAdmin.getOrganisationId(), Role.GROUP_MANAGER))
		&& !orgId.equals(rootOrgId)) {
	    userManageForm.setCourseAdminCanAddNewUsers(orgOfCourseAdmin.getCourseAdminCanAddNewUsers());
	    userManageForm.setCourseAdminCanBrowseAllUsers(orgOfCourseAdmin.getCourseAdminCanBrowseAllUsers());
	} else {
	    return forwardError(request, "error.authorisation");
	}
	userManageForm.setCanResetOrgPassword(request.isUserInRole(Role.SYSADMIN));

	userManageForm.setOrgId(orgId);
	userManageForm.setOrgName(orgName);
	List<UserManageBean> userManageBeans = service.getUserManageBeans(orgId);
	Collections.sort(userManageBeans);
	userManageForm.setUserManageBeans(userManageBeans);
	request.setAttribute("userManageForm", userManageForm);

	// heading
	String[] args = { orgName };
	request.setAttribute("heading", messageService.getMessage("heading.manage.group.users", args));

	// count roles in the org
	HashMap<String, Integer> roleCount = new HashMap<>();
	if (orgId.equals(rootOrgId)) {
	    roleCount.put(Role.SYSADMIN, Role.ROLE_SYSADMIN);
	    roleCount.put(Role.GROUP_ADMIN, Role.ROLE_GROUP_ADMIN);
	} else {
	    roleCount.put(Role.LEARNER, Role.ROLE_LEARNER);
	    roleCount.put(Role.MONITOR, Role.ROLE_MONITOR);
	    roleCount.put(Role.AUTHOR, Role.ROLE_AUTHOR);
	    roleCount.put(Role.GROUP_MANAGER, Role.ROLE_GROUP_MANAGER);
	    roleCount.put(Role.GROUP_ADMIN, Role.ROLE_GROUP_ADMIN);
	}
	for (String role : roleCount.keySet()) {
	    Integer count = service.getCountRoleForOrg(orgId, roleCount.get(role), null);
	    request.setAttribute(role.replace(' ', '_'), count);
	}

	// count users in the org
	// TODO use hql that does a count instead of getting whole objects
	Integer numUsers = Integer.valueOf(service.getUsersFromOrganisation(orgId).size());
	args[0] = numUsers.toString();
	request.setAttribute("numUsers", messageService.getMessage("label.users.in.group", args));

	return "userlist";
    }

    @RequestMapping(path = "/usermanage/forwardError", method = RequestMethod.POST)
    private String forwardError(HttpServletRequest request, String key) {
	request.setAttribute("errorName", "UserManageController");
	request.setAttribute("errorMessage", messageService.getMessage(key));
	return "error";
    }

}
