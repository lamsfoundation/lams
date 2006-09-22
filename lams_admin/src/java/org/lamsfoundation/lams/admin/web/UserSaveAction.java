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

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
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
 * @struts:action-forward name="userroles" path="/userroles.do"
 * @struts:action-forward name="usersearch" path="/usersearch.do"
 */
public class UserSaveAction extends Action {

	private static Logger log = Logger.getLogger(UserSaveAction.class);
	private static IUserManagementService service;

	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// action input
		ActionMessages errors = new ActionMessages();
		DynaActionForm userForm = (DynaActionForm) form;
		Integer orgId = (Integer) userForm.get("orgId");
		Integer userId = (Integer) userForm.get("userId");
		
		log.debug("orgId: " + orgId);
		Boolean edit = false;
		Boolean passwordChanged = true;
		SupportedLocale locale = (SupportedLocale)getService().findById(SupportedLocale.class, (Byte)userForm.get("localeId"));
		log.debug("locale: " + locale);
		
		if (isCancelled(request)) {
			if (orgId==null || orgId==0) {
				return mapping.findForward("usersearch");
			}
			request.setAttribute("org", orgId);
			return mapping.findForward("userlist");
		}

		if (userId != 0) edit = true;

		// (dyna)form validation
		if ((userForm.get("login") == null) || (userForm.getString("login").trim().length() == 0)) {
			errors.add("login", new ActionMessage("error.login.required"));
		}
		if (!StringUtils.equals((String)userForm.get("password"),((String)userForm.get("password2")))) {
			errors.add("password", new ActionMessage("error.newpassword.mismatch"));
		}
		if ((userForm.get("password") == null) || (userForm.getString("password").trim().length() == 0)) {
			passwordChanged = false;
			if (!edit) errors.add("password", new ActionMessage("error.password.required"));
		}
		if ((userForm.get("firstName") == null) || (userForm.getString("firstName").trim().length() == 0)) {
			errors.add("firstName", new ActionMessage("error.firstname.required"));
		}
		if ((userForm.get("lastName") == null) || (userForm.getString("lastName").trim().length() == 0)) {
			errors.add("lastName", new ActionMessage("error.lastname.required"));
		}
		if ((userForm.get("email") == null) || (userForm.getString("email").trim().length() == 0)) {
			errors.add("email", new ActionMessage("error.email.required"));
		} else {
			Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
			Matcher m = p.matcher(userForm.getString("email"));
			if (!m.matches()) {
				errors.add("email", new ActionMessage("error.valid.email.required"));
			}
		}
		
		User user = null;
		if (errors.isEmpty()) {
			if (edit) { // edit user
				log.debug("editing userId: " + userId);
				user = (User)getService().findById(User.class, userId);
				// hash the new password if necessary, and audit the fact
				if (passwordChanged) {
					writeAuditLog(user, new String[1]);
					userForm.set("password", HashUtil.sha1((String)userForm.get("password")));
				} else {
					userForm.set("password", user.getPassword());
				}
				BeanUtils.copyProperties(user, userForm);
				user.setLocale(locale);
			} else { // create user
				user = new User();
				userForm.set("password", HashUtil.sha1((String)userForm.get("password")));
				BeanUtils.copyProperties(user, userForm);
				log.debug("creating user... new login: " + user.getLogin());
				if (getService().getUserByLogin(user.getLogin()) != null) {
					errors.add("login", new ActionMessage("error.login.unique"));
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
					user.setAuthenticationMethod((AuthenticationMethod)getService().findByProperty(AuthenticationMethod.class,
							"authenticationMethodName","LAMS-Database").get(0));
					user.setUserId(null);
					user.setLocale(locale);
					getService().save(user);
					
					// make 'create user' audit log entry
					writeAuditLog(user, new String[2]);
					
					log.debug("user: " + user.toString());
				}
			}
		}

		
		if (errors.isEmpty()) {
			if (orgId==null || orgId==0) {
				return mapping.findForward("usersearch");
			}
			if (edit) {
				request.setAttribute("org", orgId);
				return mapping.findForward("userlist");
			} else {
				request.setAttribute("orgId", orgId);
				request.setAttribute("userId", user.getUserId());
				return mapping.findForward("userroles");
			}
		} else {
			saveErrors(request, errors);
			request.setAttribute("orgId", orgId);
			return mapping.findForward("user");
		}
	}

	private void writeAuditLog(User user, String[] args) {
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
		IAuditService auditService = (IAuditService) ctx.getBean("auditService");
		MessageService messageService = (MessageService)ctx.getBean("adminMessageService");
		
		if (args.length==1) {  // password changed
			args[0] = user.getLogin()+"("+user.getUserId()+")";
			String message = messageService.getMessage("audit.user.password.change",args);
			auditService.log(AdminConstants.MODULE_NAME, message);
		} else if (args.length==2) {  // user created
			args[0] = user.getLogin()+"("+user.getUserId()+")";
			args[1] = user.getFullName();
			String message = messageService.getMessage("audit.user.create", args);
			auditService.log(AdminConstants.MODULE_NAME, message);
		}
	}
	
	@SuppressWarnings("unchecked")
	private IUserManagementService getService() {
		if (service == null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
			service = (IUserManagementService) ctx.getBean("userManagementServiceTarget");
		}
		return service;
	}
	
}
