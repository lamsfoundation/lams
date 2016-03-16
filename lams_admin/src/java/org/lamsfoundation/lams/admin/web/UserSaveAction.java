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
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.ValidationUtil;

/**
 * @author Jun-Dir Liew
 * 
 * Created at 12:35:38 on 14/06/2006
 */

/**
 * struts doclets
 * 
 * @struts:action path="/usersave" name="UserForm" input=".user" scope="request" validate="false"
 * 
 * @struts:action-forward name="user" path="/user.do?method=edit"
 * @struts:action-forward name="userlist" path="/usermanage.do"
 * @struts:action-forward name="userroles" path="/userroles.do"
 * @struts:action-forward name="usersearch" path="/usersearch.do"
 */
public class UserSaveAction extends Action {

    private static Logger log = Logger.getLogger(UserSaveAction.class);
    private static IUserManagementService service;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	UserSaveAction.service = AdminServiceProxy.getService(getServlet().getServletContext());

	// action input
	ActionMessages errors = new ActionMessages();
	DynaActionForm userForm = (DynaActionForm) form;
	Integer orgId = (Integer) userForm.get("orgId");
	Integer userId = (Integer) userForm.get("userId");

	UserSaveAction.log.debug("orgId: " + orgId);
	Boolean edit = false;
	Boolean passwordChanged = true;
	SupportedLocale locale = (SupportedLocale) UserSaveAction.service.findById(SupportedLocale.class,
		(Integer) userForm.get("localeId"));
	AuthenticationMethod authenticationMethod = (AuthenticationMethod) UserSaveAction.service
		.findById(AuthenticationMethod.class, (Integer) userForm.get("authenticationMethodId"));
	UserSaveAction.log.debug("locale: " + locale);
	UserSaveAction.log.debug("authenticationMethod:" + authenticationMethod);

	if (isCancelled(request)) {
	    if ((orgId == null) || (orgId == 0)) {
		return mapping.findForward("usersearch");
	    }
	    request.setAttribute("org", orgId);
	    return mapping.findForward("userlist");
	}

	User user = null;
	if (userId != 0) {
	    edit = true;
	    user = (User) UserSaveAction.service.findById(User.class, userId);
	}

	// login validation
	String login = (userForm.get("login") == null) ? null : userForm.getString("login").trim();
	if (StringUtils.isBlank(login)) {
	    errors.add("login", new ActionMessage("error.login.required"));
	} else if (!ValidationUtil.isUserNameValid(login)) {
	    errors.add("login", new ActionMessage("error.username.invalid.characters"));
	} else {
	    userForm.set("login", login);
	    User existingUser = UserSaveAction.service.getUserByLogin(login);
	    if (existingUser != null) {
		if ((user != null) && StringUtils.equals(user.getLogin(), login)) {
		    // login exists - it's the user's current login
		} else {
		    errors.add("login", new ActionMessage("error.login.unique",
			    "(" + login + ", ID: " + existingUser.getUserId() + ")"));
		}
	    }
	}

	//password validation
	String password = (userForm.get("password") == null) ? null : (String) userForm.get("password");
	if (!StringUtils.equals(password, ((String) userForm.get("password2")))) {
	    errors.add("password", new ActionMessage("error.newpassword.mismatch"));
	}
	if (StringUtils.isBlank(password)) {
	    passwordChanged = false;
	    if (!edit) {
		errors.add("password", new ActionMessage("error.password.required"));
	    }
	}

	//first name validation
	String firstName = (userForm.get("firstName") == null) ? null : (String) userForm.get("firstName");
	if (StringUtils.isBlank(firstName)) {
	    errors.add("firstName", new ActionMessage("error.firstname.required"));
	} else if (!ValidationUtil.isFirstLastNameValid(firstName)) {
	    errors.add("firstName", new ActionMessage("error.firstname.invalid.characters"));
	}

	//last name validation
	String lastName = (userForm.get("lastName") == null) ? null : (String) userForm.get("lastName");
	if (StringUtils.isBlank(lastName)) {
	    errors.add("lastName", new ActionMessage("error.lastname.required"));
	} else if (!ValidationUtil.isFirstLastNameValid(lastName)) {
	    errors.add("lastName", new ActionMessage("error.lastname.invalid.characters"));
	}

	//user email validation
	String userEmail = (userForm.get("email") == null) ? null : (String) userForm.get("email");
	if (StringUtils.isBlank(userEmail)) {
	    errors.add("email", new ActionMessage("error.email.required"));
	} else if (!ValidationUtil.isEmailValid(userEmail)) {
	    errors.add("email", new ActionMessage("error.valid.email.required"));
	}

	if (errors.isEmpty()) {
	    if (edit) { // edit user
		UserSaveAction.log.debug("editing userId: " + userId);
		// hash the new password if necessary, and audit the fact
		if (passwordChanged) {
		    UserSaveAction.service.auditPasswordChanged(user, AdminConstants.MODULE_NAME);
		    String salt = HashUtil.salt();
		    String passwordHash = HashUtil.sha256((String) userForm.get("password"), salt);
		    userForm.set("salt", salt);
		    userForm.set("password", passwordHash);
		} else {
		    userForm.set("password", user.getPassword());
		}
		BeanUtils.copyProperties(user, userForm);
		user.setLocale(locale);
		user.setAuthenticationMethod(authenticationMethod);

		Theme cssTheme = (Theme) UserSaveAction.service.findById(Theme.class,
			(Long) userForm.get("userTheme"));
		user.setTheme(cssTheme);

		UserSaveAction.service.save(user);
	    } else { // create user
		user = new User();
		String salt = HashUtil.salt();
		String passwordHash = HashUtil.sha256((String) userForm.get("password"), salt);
		userForm.set("salt", salt);
		userForm.set("password", passwordHash);
		BeanUtils.copyProperties(user, userForm);
		UserSaveAction.log.debug("creating user... new login: " + user.getLogin());
		if (errors.isEmpty()) {
		    // TODO set theme according to user input
		    // instead of server default.
		    user.setTheme(UserSaveAction.service.getDefaultTheme());
		    user.setDisabledFlag(false);
		    user.setCreateDate(new Date());
		    user.setAuthenticationMethod((AuthenticationMethod) UserSaveAction.service
			    .findByProperty(AuthenticationMethod.class, "authenticationMethodName", "LAMS-Database")
			    .get(0));
		    user.setUserId(null);
		    user.setLocale(locale);

		    Theme theme = (Theme) UserSaveAction.service.findById(Theme.class,
			    (Long) userForm.get("userTheme"));
		    user.setTheme(theme);

		    UserSaveAction.service.save(user);

		    // make 'create user' audit log entry
		    UserSaveAction.service.auditUserCreated(user, AdminConstants.MODULE_NAME);

		    UserSaveAction.log.debug("user: " + user.toString());
		}
	    }
	}

	if (errors.isEmpty()) {
	    if ((orgId == null) || (orgId == 0)) {
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

}
