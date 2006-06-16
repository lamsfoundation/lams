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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.admin.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.web.util.HttpSessionManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Jun-Dir Liew
 *
 * Created at 12:35:38 on 14/06/2006
 */

/**
 * struts doclets
 * 
 * @struts:action path="/usersave"
 *                name="UserForm"
 *                input=".user"
 *                scope="request"
 *                validate="false"
 *
 * @struts:action-forward name="user"
 *                        path=".user"                
 * @struts:action-forward name="userlist"
 *                        path="/usermanage.do"
 */
public class UserSaveAction extends Action {

	private static Logger log = Logger.getLogger(UserSaveAction.class);
	private static WebApplicationContext ctx = WebApplicationContextUtils
			.getWebApplicationContext(HttpSessionManager.getInstance()
					.getServletContext());
	private static IUserManagementService service = (IUserManagementService) ctx
			.getBean("userManagementServiceTarget");
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
		DynaActionForm userForm = (DynaActionForm)form;
		
		if(isCancelled(request)){
			request.setAttribute("org",userForm.get("orgId"));
			return mapping.findForward("userlist");
		}
		
		ActionMessages errors = new ActionMessages();
		if((userForm.get("login")==null)||(((String)userForm.getString("login").trim()).length()==0)){
			errors.add("login",new ActionMessage("error.login.required"));
		}
		if((userForm.get("password")==null)||(((String)userForm.getString("password").trim()).length()==0)){
			errors.add("password",new ActionMessage("error.password.required"));
		}
		
		if(errors.isEmpty()){
			Integer userId = (Integer)userForm.get("userId");
			Integer orgId = (Integer)userForm.get("orgId");
			User user;
			if(userId!=0){    // edit user
				log.debug("editing userId: "+userId);
				user = service.getUserById(userId);
				BeanUtils.copyProperties(user,userForm);
				service.updateUser(user);
				UserOrganisation userOrganisation = service.getUserOrganisation(userId, orgId);
				List<Role> roles = service.getRolesForUserByOrganisation(user, orgId);
                
				Role currentRole = service.getRoleByName("LEARNER");
				if(userForm.get("learner").equals("on") && roles.indexOf(currentRole)<0) {
					UserOrganisationRole userOrganisationRole = new UserOrganisationRole(userOrganisation, 
							currentRole);
					service.saveOrUpdateUserOrganisationRole(userOrganisationRole);
				} else if(userForm.get("learner").equals("off")){
					//service.deleteUserOrganisationRole();
				}
				currentRole = service.getRoleByName("AUTHOR");
				if(userForm.get("author").equals("on") && roles.indexOf(currentRole)<0) {
					UserOrganisationRole userOrganisationRole = new UserOrganisationRole(userOrganisation, 
							currentRole);
					service.saveOrUpdateUserOrganisationRole(userOrganisationRole);
				} else if(userForm.get("learner").equals("off")) {
					//service.deleteUserOrganisationRole();
				}
				currentRole = service.getRoleByName("STAFF");
				if(userForm.get("staff").equals("on") && roles.indexOf(currentRole)<0) {
					UserOrganisationRole userOrganisationRole = new UserOrganisationRole(userOrganisation, 
							currentRole);
					service.saveOrUpdateUserOrganisationRole(userOrganisationRole);
				} else if(userForm.get("learner").equals("off")) {
					//service.deleteUserOrganisationRole();
				}
				currentRole = service.getRoleByName("COURSE ADMIN");
				if(userForm.get("admin").equals("on") && roles.indexOf(currentRole)<0) {
					UserOrganisationRole userOrganisationRole = new UserOrganisationRole(userOrganisation, 
							currentRole);
					service.saveOrUpdateUserOrganisationRole(userOrganisationRole);
				} else if(userForm.get("learner").equals("off")) {
					//service.deleteUserOrganisationRole();
				}
				currentRole = service.getRoleByName("COURSE MANAGER");
				if(userForm.get("manager").equals("on") && roles.indexOf(currentRole)<0) {
					UserOrganisationRole userOrganisationRole = new UserOrganisationRole(userOrganisation, 
							currentRole);
					service.saveOrUpdateUserOrganisationRole(userOrganisationRole);
				} else if(userForm.get("learner").equals("off")) {
					//service.deleteUserOrganisationRole();
				}
			}else{    // create user
				log.debug("creating user...");
				user = new User();
				BeanUtils.copyProperties(user,userForm);
				if(service.getUserByLogin(user.getLogin())!=null) {
					errors.add("loginUnique",new ActionMessage("error.login.unique"));
				}
				user.setDisabledFlag(false);
				user.setCreateDate(new Date());
				user.setAuthenticationMethod(service.getAuthenticationMethodByName("LAMS-Database"));
				log.debug(user.toString());
				service.createUser(user);
				user = service.getUserByLogin((String)userForm.get("login"));
				UserOrganisation userOrganisation = new UserOrganisation(user, service.getOrganisationById(orgId));
				service.saveOrUpdateUserOrganisation(userOrganisation);
				// set default role to learner
				Role role = service.getRoleByName("LEARNER");
				UserOrganisationRole userOrganisationRole = new UserOrganisationRole(userOrganisation, role);
				service.saveOrUpdateUserOrganisationRole(userOrganisationRole);
			}
			request.setAttribute("org",orgId);
			log.debug("orgId: "+orgId);
			return mapping.findForward("userlist");
		}else{
			saveErrors(request,errors);
			return mapping.findForward("user");
		}
	}
}
