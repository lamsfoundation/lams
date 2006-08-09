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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.HashUtil;
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
 * @struts:action path="/usersave" name="UserForm" input=".user" scope="request"
 *                validate="false"
 * 
 * @struts:action-forward name="user" path=".user"
 * @struts:action-forward name="userlist" path="/usermanage.do"
 */
public class UserSaveAction extends Action {

	private static Logger log = Logger.getLogger(UserSaveAction.class);

	private static IUserManagementService service;

	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaActionForm userForm = (DynaActionForm) form;
		Boolean edit = false;
		Boolean passwordChanged = true;

		Integer orgId = (Integer) userForm.get("orgId");
		
		if (isCancelled(request)) {
			request.setAttribute("org", orgId);
			return mapping.findForward("userlist");
		}

		Integer userId = (Integer) userForm.get("userId");
		log.debug("got userId: " + userId);

		if (userId != 0)
			edit = true;

		ActionMessages errors = new ActionMessages();
		if ((userForm.get("login") == null)
				|| (userForm.getString("login").trim().length() == 0)) {
			errors.add("login", new ActionMessage("error.login.required"));
		}
		if (!userForm.get("password").equals(userForm.get("password2"))) {
			errors.add("password", new ActionMessage(
					"error.newpassword.mismatch"));
		}
		if ((userForm.get("password") == null)
				|| (((String) userForm.getString("password").trim()).length() == 0)) {
			passwordChanged = false;
			if (!edit)
				errors.add("password", new ActionMessage(
						"error.password.required"));
		}

		SupportedLocale locale = (SupportedLocale) request.getSession()
				.getAttribute("locale");
		log.debug("locale: " + locale);

		if (errors.isEmpty()) {
			List<Role> allRoles = (List<Role>)request.getSession().getAttribute("rolelist");
			User user;
			String[] roles = (String[]) userForm.get("roles");
			if (edit) { // edit user
				log.debug("editing userId: " + userId);
				user = (User) request.getSession().getAttribute("user");
				if (passwordChanged) {
					userForm.set("password", HashUtil.sha1((String) userForm.get("password")));
				} else {
					userForm.set("password", user.getPassword());
				}
				BeanUtils.copyProperties(user, userForm);
				user.setLocaleCountry(locale.getCountryIsoCode());
				user.setLocaleLanguage(locale.getLanguageIsoCode());
				log.debug("country: " + user.getLocaleCountry());
				log.debug("language: " + user.getLocaleLanguage());
				List<String> rolesList = Arrays.asList(roles);
				List<String> rolesCopy = new ArrayList<String>();
				rolesCopy.addAll(rolesList);
				log.debug("rolesList.size: " + rolesList.size());
				UserOrganisation uo = (UserOrganisation) request.getSession().getAttribute("uo");
				Set<UserOrganisationRole> uors = (Set<UserOrganisationRole>) request.getSession().getAttribute("uors");
				Set<UserOrganisationRole> uorsCopy = new HashSet<UserOrganisationRole>();
				uorsCopy.addAll(uors);
				//remove the common part from the rolesList and uors
				//to get the uors to remove and the roles to add 
				for(String roleId : rolesList) { 
					for(UserOrganisationRole uor : uors) {
						if (uor.getRole().getRoleId().toString().equals(roleId)) {
							rolesCopy.remove(roleId);
							uorsCopy.remove(uor);
						}
					}
				}
				uors.removeAll(uorsCopy);
				for(String roleId : rolesCopy){
					UserOrganisationRole uor = new UserOrganisationRole(uo, findRole(allRoles, roleId));
					getService().save(uor);
					uors.add(uor);
				}
				uo.setUserOrganisationRoles(uors);
				getService().save(user);
			} else { // create user
				log.debug("creating user...");
				user = new User();
				userForm.set("password", HashUtil.sha1((String) userForm
						.get("password")));
				BeanUtils.copyProperties(user, userForm);
				log.debug("new login: " + user.getLogin());
				if (getService().getUserByLogin(user.getLogin()) != null) {
					errors.add("loginUnique", new ActionMessage("error.login.unique"));
				}
				if (errors.isEmpty()) {
					user.setDisabledFlag(false);
					user.setCreateDate(new Date());
					user.setAuthenticationMethod((AuthenticationMethod) getService().findByProperty(AuthenticationMethod.class,
							"authenticationMethodName","LAMS-Database").get(0));
					user.setUserId(null);
					user.setLocaleCountry(locale.getCountryIsoCode());
					user.setLocaleLanguage(locale.getLanguageIsoCode());
					getService().save(user);
					log.debug("user: " + user.toString());
					List<Organisation> orgs = new ArrayList<Organisation>();
					// if user is to be added to a class, make user a member of
					// parent course also
					Organisation org = (Organisation)request.getSession().getAttribute("org");
					orgs.add(org);
					OrganisationType orgType = (OrganisationType)request.getSession().getAttribute("orgType");
					if (orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
						Organisation parentOrg = (Organisation)request.getSession().getAttribute("parentOrg");
						orgs.add(parentOrg);
					}
					for (Organisation o : orgs) {
						UserOrganisation uo = new UserOrganisation(user,o);
						getService().save(uo);
						log.debug("created UserOrganisation: " + uo.getUserOrganisationId());
						for (String roleId : roles) { 
							UserOrganisationRole uor = new UserOrganisationRole(uo, findRole(allRoles,roleId));
							getService().save(uor);
							uo.addUserOrganisationRole(uor);
						}
						user.addUserOrganisation(uo);
					}
				}
			}
		}

		if (errors.isEmpty()) {
			request.setAttribute("org", orgId);
			log.debug("orgId: " + orgId);
			clearSessionAttributes(request.getSession());
			return mapping.findForward("userlist");
		} else {
			if (!edit) { // error screen on create user shouldn't show empty roles
				userForm.set("userId", null);
			}
			saveErrors(request, errors);
			return mapping.findForward("user");
		}
	}

	
	private void clearSessionAttributes(HttpSession session) {
		String[] attributes = {"locales","org","parentOrg","user","orgType","rolelist","uo","uors"};
		for(String attr : attributes){
			session.removeAttribute(attr);
		}
	}


	private Role findRole(List<Role> allRoles, String roleId){
		for(Role role: allRoles){
			if(role.getRoleId().toString().equals(roleId))
				return role;
		}
		return null;
	}
	
	private IUserManagementService getService() {
		if (service == null) {
			WebApplicationContext ctx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(getServlet()
							.getServletContext());
			service = (IUserManagementService) ctx
					.getBean("userManagementServiceTarget");
		}
		return service;
	}
}
