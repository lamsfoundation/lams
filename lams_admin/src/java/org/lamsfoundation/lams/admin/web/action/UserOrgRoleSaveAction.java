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

import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.dto.UserBean;
import org.lamsfoundation.lams.admin.web.form.UserOrgRoleForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 * @author jliew
 *
 * Saves roles for users that were just added.
 * Uses session scope because using request scope doesn't copy the form data
 * into UserOrgRoleForm's userBeans ArrayList (the list becomes empty).
 *
 */

/**
 * struts doclets
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
public class UserOrgRoleSaveAction extends Action {

    private static Logger log = Logger.getLogger(UserOrgRoleSaveAction.class);
    private static IUserManagementService service;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	service = AdminServiceProxy.getService(getServlet().getServletContext());
	UserOrgRoleForm userOrgRoleForm = (UserOrgRoleForm) form;

	ArrayList userBeans = userOrgRoleForm.getUserBeans();
	log.debug("userBeans is null? " + userBeans == null);
	Integer orgId = userOrgRoleForm.getOrgId();
	log.debug("orgId: " + orgId);

	request.setAttribute("org", orgId);
	request.getSession().removeAttribute("UserOrgRoleForm");

	if (isCancelled(request)) {
	    return mapping.findForward("userlist");
	}

	// save UserOrganisation memberships, and the associated roles;
	// for subgroups, if user is not a member of the parent group then add to that as well.
	for (int i = 0; i < userBeans.size(); i++) {
	    UserBean bean = (UserBean) userBeans.get(i);
	    User user = (User) service.findById(User.class, bean.getUserId());
	    log.debug("userId: " + bean.getUserId());
	    String[] roleIds = bean.getRoleIds();
	    if (roleIds.length == 0) {
		// TODO forward to userorgrole.do, not userorg.do
		ActionMessages errors = new ActionMessages();
		errors.add("roles", new ActionMessage("error.roles.empty"));
		saveErrors(request, errors);
		request.setAttribute("orgId", orgId);
		return mapping.findForward("userorg");
	    }
	    service.setRolesForUserOrganisation(user, orgId, Arrays.asList(roleIds));
	    // FMALIKOFF 5/7/7 Commented out the following code that set the roles in the course if the current org is a class, as the logic 
	    // is done in service.setRolesForUserOrganisation()
	    //if (organisation.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
	    //	if (service.getUserOrganisation(bean.getUserId(), organisation.getParentOrganisation().getOrganisationId())==null) {
	    //		service.setRolesForUserOrganisation(user, organisation.getParentOrganisation(), (List<String>)Arrays.asList(roleIds));
	    //	}
	    //}
	}
	return mapping.findForward("userlist");
    }

}
