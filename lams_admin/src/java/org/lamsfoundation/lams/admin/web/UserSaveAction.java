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
import org.lamsfoundation.lams.admin.AdminConstants;
import org.lamsfoundation.lams.themes.CSSThemeVisualElement;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.audit.IAuditService;
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
 * @struts:action-forward name="user" path="/user.do?method=edit"
 * @struts:action-forward name="userlist" path="/usermanage.do"
 */
public class UserSaveAction extends Action {

	private static Logger log = Logger.getLogger(UserSaveAction.class);

	private static IUserManagementService service;
	private static List<Role> rolelist;

	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DynaActionForm userForm = (DynaActionForm) form;
		Boolean edit = false;
		Boolean passwordChanged = true;

		Integer orgId = (Integer) userForm.get("orgId");
		Organisation org = (Organisation)getService().findById(Organisation.class, orgId);
		
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
				|| (userForm.getString("password").trim().length() == 0)) {
			passwordChanged = false;
			if (!edit)
				errors.add("password", new ActionMessage(
						"error.password.required"));
		}

		SupportedLocale locale = (SupportedLocale) getService().findById(SupportedLocale.class, (Byte)userForm.get("localeId"));
		log.debug("locale: " + locale);

		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
		IAuditService auditService = (IAuditService) ctx.getBean("auditService");
		MessageService messageService = (MessageService)ctx.getBean("adminMessageService");
		
		if (errors.isEmpty()) {
			String[] roles = (String[]) userForm.get("roles");
			if (edit) { // edit user
				log.debug("editing userId: " + userId);
				User user = (User)getService().findById(User.class, userId);
				if (passwordChanged) {
					// make 'password changed' audit log entry
					String[] args = new String[1];
					args[0] = user.getLogin()+"("+userId+")";
					String message = messageService.getMessage("audit.user.password.change",args);
					auditService.log(AdminConstants.MODULE_NAME, message);
					userForm.set("password", HashUtil.sha1((String) userForm.get("password")));
				} else {
					userForm.set("password", user.getPassword());
				}
				BeanUtils.copyProperties(user, userForm);
				user.setLocale(locale);
				log.debug("locale: " + locale);
				getService().setRolesForUserOrganisation(user, org, (List<String>)Arrays.asList(roles));
			} else { // create user
				log.debug("creating user...");
				User user = new User();
				userForm.set("password", HashUtil.sha1((String) userForm.get("password")));
				BeanUtils.copyProperties(user, userForm);
				log.debug("new login: " + user.getLogin());
				if (getService().getUserByLogin(user.getLogin()) != null) {
					errors.add("loginUnique", new ActionMessage("error.login.unique"));
				}
				if (errors.isEmpty()) {
					// TODO set flash/html themes according to user input instead of server default.
					String flashName = Configuration.get(ConfigurationKeys.DEFAULT_FLASH_THEME);
					List list = getService().findByProperty(CSSThemeVisualElement.class, "name", flashName);
					if (list!=null) {
						CSSThemeVisualElement flashTheme = (CSSThemeVisualElement)list.get(0);
						user.setFlashTheme(flashTheme);
					}
					String htmlName = Configuration.get(ConfigurationKeys.DEFAULT_HTML_THEME);
					list = getService().findByProperty(CSSThemeVisualElement.class, "name", htmlName);
					if (list!=null) {
						CSSThemeVisualElement htmlTheme = (CSSThemeVisualElement)list.get(0);
						user.setHtmlTheme(htmlTheme);
					}
					user.setDisabledFlag(false);
					user.setCreateDate(new Date());
					user.setAuthenticationMethod((AuthenticationMethod) getService().findByProperty(AuthenticationMethod.class,
							"authenticationMethodName","LAMS-Database").get(0));
					user.setUserId(null);
					user.setLocale(locale);
					getService().save(user);
					
					// make 'create user' audit log entry
					String[] args = new String[2];
					args[0] = user.getLogin()+"("+user.getUserId()+")";
					args[1] = user.getFullName();
					String message = messageService.getMessage("audit.user.create", args);
					auditService.log(AdminConstants.MODULE_NAME, message);
					
					log.debug("user: " + user.toString());
					List<Organisation> orgs = new ArrayList<Organisation>();
					// if user is to be added to a class, make user a member of
					// parent course also
					orgs.add(org);
					OrganisationType orgType = org.getOrganisationType();
					if (orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
						Organisation parentOrg = org.getParentOrganisation();
						orgs.add(parentOrg);
					}
					for (Organisation o : orgs) {
						getService().setRolesForUserOrganisation(user, o, (List<String>)Arrays.asList(roles));
					}
				}
			}
		}

		if (errors.isEmpty()) {
			request.setAttribute("org", orgId);
			log.debug("orgId: " + orgId);
			return mapping.findForward("userlist");
		} else {
			if (!edit) { // error screen on create user shouldn't show empty roles
				userForm.set("userId", null);
			}
			saveErrors(request, errors);
			request.setAttribute("orgId", orgId);
			return mapping.findForward("user");
		}
	}

	
	@SuppressWarnings("unchecked")
	private IUserManagementService getService() {
		if (service == null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
			rolelist = service.findAll(Role.class);
		}
		return service;
	}
	
}
