/****************************************************************
 * Copyright (C) 2006 LAMS Foundation (http://lamsfoundation.org)
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * ConfigAction
 *
 * @author Mitchell Seaton
 */
/**
 * struts doclets
 * 
 * @struts.action path="/register" parameter="method" name="RegisterForm" input=".register" scope="request" validate="false"
 * @struts.action-forward name="register" path=".register"
 * @struts.action-forward name="sysadmin" path=".sysadmin"
 * @struts.action-forward name="success" path="http://lamscommunity.org/registration"
 * @struts.action-forward name="error" path=".error"
 */
public class RegisterAction extends LamsDispatchAction {


	private static final Logger log = Logger.getLogger(RegisterAction.class);
	private static IUserManagementService service;
	
	public ActionForward unspecified(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{
		
		// check permission
		if(!request.isUserInRole(Role.SYSADMIN)){
			request.setAttribute("errorName","RegisterAction");
			request.setAttribute("errorMessage",AdminServiceProxy.getMessageService(getServlet().getServletContext())
				.getMessage("error.authorisation"));
			return mapping.findForward("error");
		}
		
		service = AdminServiceProxy.getService(getServlet().getServletContext());
		
		DynaActionForm registerForm = (DynaActionForm) form;
		
		/** Set Server Configuration details in dyna form */
		registerForm.set("serverurl", Configuration.get(ConfigurationKeys.SERVER_URL));
		registerForm.set("serverversion", Configuration.get(ConfigurationKeys.VERSION));
		registerForm.set("serverbuild", Configuration.get(ConfigurationKeys.SERVER_VERSION_NUMBER));
		registerForm.set("serverlocale", Configuration.get(ConfigurationKeys.SERVER_LANGUAGE));
		registerForm.set("langdate", Configuration.get(ConfigurationKeys.DICTIONARY_DATE_CREATED));
		
		/** Set user details for registration */
		UserDTO sysadmin = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
		
		registerForm.set("rname", sysadmin.getFirstName() + " " + sysadmin.getLastName());
		registerForm.set("remail", sysadmin.getEmail());
		
		/** Get Server statistics for registration */
		List groups = service.findByProperty(Organisation.class,"organisationType.organisationTypeId",OrganisationType.COURSE_TYPE);
		List subgroups = service.findByProperty(Organisation.class,"organisationType.organisationTypeId",OrganisationType.CLASS_TYPE);
		List sysadmins = service.findByProperty(UserOrganisationRole.class, "role.roleId", Role.ROLE_SYSADMIN);
		List admins = service.findByProperty(UserOrganisationRole.class, "role.roleId", Role.ROLE_COURSE_ADMIN);
		List authors = service.findByProperty(UserOrganisationRole.class, "role.roleId", Role.ROLE_AUTHOR);
		List monitors = service.findByProperty(UserOrganisationRole.class, "role.roleId", Role.ROLE_MONITOR);
		List managers = service.findByProperty(UserOrganisationRole.class, "role.roleId", Role.ROLE_COURSE_MANAGER);
		List learners = service.findByProperty(UserOrganisationRole.class, "role.roleId", Role.ROLE_LEARNER);
		
		/** Set statistics in dyna form */
		registerForm.set("groupno", Integer.valueOf(groups.size()));
		registerForm.set("subgroupno", Integer.valueOf(subgroups.size()));
		registerForm.set("sysadminno", Integer.valueOf(sysadmins.size()));
		registerForm.set("adminno", Integer.valueOf(admins.size()));
		registerForm.set("authorno", Integer.valueOf(authors.size()));
		registerForm.set("monitorno", Integer.valueOf(monitors.size()));
		registerForm.set("managerno", Integer.valueOf(managers.size()));
		registerForm.set("learnerno", Integer.valueOf(learners.size()));
		
		return mapping.findForward("register");
	}
	
}
