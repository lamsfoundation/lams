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
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;

/**
 * @author jliew
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class UserRolesAction extends Action {

    private static Logger log = Logger.getLogger(UserRolesAction.class);
    private static IUserManagementService service;
    private static MessageService messageService;
    private static List<Role> rolelist;

    @Override
    @SuppressWarnings("unchecked")
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	service = AdminServiceProxy.getService(getServlet().getServletContext());
	messageService = AdminServiceProxy.getMessageService(getServlet().getServletContext());
	if (rolelist == null) {
	    rolelist = service.findAll(Role.class);
	    Collections.sort(rolelist);
	}

	ActionMessages errors = new ActionMessages();
	DynaActionForm userRolesForm = (DynaActionForm) form;
	Integer orgId = WebUtil.readIntParam(request, "orgId", true);
	Integer userId = WebUtil.readIntParam(request, "userId", true);

	// user and org ids passed as attributes by UserSaveAction
	if (orgId == null) {
	    orgId = (Integer) request.getAttribute("orgId");
	}
	if (orgId == null) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.org.invalid"));
	    saveErrors(request, errors);
	    return mapping.findForward("userrole");
	}
	if (userId == null || userId == 0) {
	    userId = (Integer) request.getAttribute("userId");
	}
	if (userId == null) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.userid.invalid"));
	    saveErrors(request, errors);
	    return mapping.findForward("userrole");
	}
	log.debug("editing roles for userId: " + userId + " and orgId: " + orgId);

	// test requestor's permission
	Organisation org = (Organisation) service.findById(Organisation.class, orgId);
	User user = (User) service.findById(User.class, userId);
	OrganisationType orgType = org.getOrganisationType();
	Integer orgIdOfCourse = (orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE))
		? org.getParentOrganisation().getOrganisationId() : orgId;
	Boolean isSysadmin = request.isUserInRole(Role.SYSADMIN);
	User requestor = service.getUserByLogin(request.getRemoteUser());
	Integer rootOrgId = service.getRootOrganisation().getOrganisationId();
	Boolean requestorHasRole = service.isUserInRole(requestor.getUserId(), orgIdOfCourse, Role.GROUP_MANAGER)
		|| (service.isUserInRole(requestor.getUserId(), orgIdOfCourse, Role.GROUP_ADMIN)
			&& !rootOrgId.equals(orgId))
		|| (service.isUserGlobalGroupAdmin() && !rootOrgId.equals(orgId));

	if (!(requestorHasRole || isSysadmin)) {
	    request.setAttribute("errorName", "UserRolesAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return mapping.findForward("error");
	}

	userRolesForm.set("userId", userId);
	userRolesForm.set("orgId", org.getOrganisationId());
	// screen display vars
	request.setAttribute("rolelist", service.filterRoles(rolelist, isSysadmin, orgType));
	request.setAttribute("login", user.getLogin());
	request.setAttribute("fullName", user.getFullName());
	request.setAttribute("orgName", org.getName());
	Organisation parentOrg = org.getParentOrganisation();
	if (parentOrg != null && !parentOrg.equals(service.getRootOrganisation())) {
	    request.setAttribute("pOrgId", parentOrg.getOrganisationId());
	    request.setAttribute("parentName", parentOrg.getName());
	}

	String[] roles = null;
	UserOrganisation uo = service.getUserOrganisation(userId, orgId);
	if (uo != null) {
	    Iterator iter2 = uo.getUserOrganisationRoles().iterator();
	    roles = new String[uo.getUserOrganisationRoles().size()];
	    int i = 0;
	    while (iter2.hasNext()) {
		UserOrganisationRole uor = (UserOrganisationRole) iter2.next();
		roles[i] = uor.getRole().getRoleId().toString();
		log.debug("got roleid: " + roles[i]);
		i++;
	    }
	} else {
	    ActionMessages messages = new ActionMessages();
	    messages.add("roles", new ActionMessage("msg.add.to.org", org.getName()));
	    saveMessages(request, messages);
	}
	userRolesForm.set("roles", roles);

	return mapping.findForward("userrole");
    }

}
