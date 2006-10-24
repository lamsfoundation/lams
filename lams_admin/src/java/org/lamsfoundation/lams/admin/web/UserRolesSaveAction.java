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

/* $Id$ */
package org.lamsfoundation.lams.admin.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

/**
 * @author jliew
 *
 * @struts:action path="/userrolessave"
 *              name="UserRolesForm"
 *              input=".userroles"
 *              scope="request"
 *              parameter="method"
 * 				validate="false"
 * 
 * @struts:action-forward name="userroles" path="/userroles.do"
 * @struts:action-forward name="userlist" path="/usermanage.do"
 */
public class UserRolesSaveAction extends Action {
	
	private static Logger log = Logger.getLogger(UserRolesSaveAction.class);
	private static IUserManagementService service;
	private static List<Role> rolelist;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		service = AdminServiceProxy.getService(getServlet().getServletContext());
		if (rolelist==null) {
			rolelist = service.findAll(Role.class);
			Collections.sort(rolelist);
		}
		
		ActionMessages errors = new ActionMessages();
		DynaActionForm userRolesForm = (DynaActionForm) form;
		Integer orgId = (Integer) userRolesForm.get("orgId");
		Integer userId = (Integer) userRolesForm.get("userId");
		String[] roles = (String[]) userRolesForm.get("roles");
		
		request.setAttribute("org", orgId);
				
		if (isCancelled(request)) {
			return mapping.findForward("userlist");
		}
		
		log.debug("userId: "+userId+", orgId: "+orgId+" will have "+roles.length+" roles");
		Organisation org = (Organisation)service.findById(Organisation.class, orgId);
		User user = (User)service.findById(User.class, userId);
		
		// user must have at least 1 role
		if (roles.length < 1) {
			errors.add("roles", new ActionMessage("error.roles.empty"));
			saveErrors(request,errors);
			request.setAttribute("rolelist",filterRoles(rolelist,request.isUserInRole(Role.SYSADMIN),org.getOrganisationType()));
			request.setAttribute("login", user.getLogin());
			request.setAttribute("fullName", user.getFullName());
			return mapping.findForward("userroles");
		}
		
		// if user is to be added to a class, make user a member of parent course also if not already
		List<Organisation> orgs = new ArrayList<Organisation>();
		orgs.add(org);
		OrganisationType orgType = org.getOrganisationType();
		Organisation parentOrg = org.getParentOrganisation();
		if (orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE) 
				&& service.getUserOrganisation(userId, parentOrg.getOrganisationId())==null) {
			orgs.add(parentOrg);
		}
		for (Organisation o : orgs) {
			service.setRolesForUserOrganisation(user, o, (List<String>)Arrays.asList(roles));
		}
		
		return mapping.findForward("userlist");
	}
	
	private List<Role> filterRoles(List<Role> rolelist, Boolean isSysadmin, OrganisationType orgType){
		List<Role> allRoles = new ArrayList<Role>();
		allRoles.addAll(rolelist);
		Role role = new Role();
		if(!isSysadmin) {
			role.setRoleId(Role.ROLE_SYSADMIN);
			allRoles.remove(role);
		}
		if(orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
			role.setRoleId(Role.ROLE_COURSE_ADMIN);
			allRoles.remove(role);
			role.setRoleId(Role.ROLE_COURSE_MANAGER);
			allRoles.remove(role);
		}
		return allRoles;
	}

}
