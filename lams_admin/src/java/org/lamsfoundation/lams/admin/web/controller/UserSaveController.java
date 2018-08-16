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

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.service.AdminServiceProxy;
import org.lamsfoundation.lams.admin.web.form.UserForm;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author Jun-Dir Liew
 *
 * Created at 12:35:38 on 14/06/2006
 */

/**
 *
 *
 *
 *
 *
 *
 *
 */

@Controller
@RequestMapping
public class UserSaveController {

    private static Logger log = Logger.getLogger(UserSaveController.class);
    private static IUserManagementService service;
    private static MessageService messageService;

    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(path = "/saveUserDetails", method = RequestMethod.POST)
    public String saveUserDetails(@ModelAttribute UserForm userForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	messageService = AdminServiceProxy.getMessageService(applicationContext.getServletContext());
	UserSaveController.service = AdminServiceProxy.getService(applicationContext.getServletContext());
	// action input
	Integer orgId = userForm.getOrgId();
	Integer userId = userForm.getUserId();
	ISecurityService securityService = AdminServiceProxy.getSecurityService(applicationContext.getServletContext());
	Integer loggeduserId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();

	// check if logged in User is Sysadmin
	if (!securityService.isSysadmin(loggeduserId, "Edit User Details " + userId, true)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Sysadmin has edit permisions");
	    return null;
	}
	UserDTO sysadmin = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	UserSaveController.log.debug("orgId: " + orgId);
	Boolean edit = false;
	SupportedLocale locale = (SupportedLocale) UserSaveController.service.findById(SupportedLocale.class,
		userForm.getLocaleId());
	AuthenticationMethod authenticationMethod = (AuthenticationMethod) UserSaveController.service
		.findById(AuthenticationMethod.class, userForm.getAuthenticationMethodId());
	UserSaveController.log.debug("locale: " + locale);
	UserSaveController.log.debug("authenticationMethod:" + authenticationMethod);

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	if (request.getAttribute("CANCEL") != null) {
	    if ((orgId == null) || (orgId == 0)) {
		return "forward:/usersearch.do";
	    }
	    request.setAttribute("org", orgId);
	    return "forward:/usermanage.do";
	}

	User user = null;
	if (userId != 0) {
	    edit = true;
	    user = (User) UserSaveController.service.findById(User.class, userId);
	}

	// login validation
	String login = userForm.getLogin() == null ? null : userForm.getLogin().trim();
	if (StringUtils.isBlank(login)) {
	    errorMap.add("login", messageService.getMessage("error.login.required"));
	} else if (!ValidationUtil.isUserNameValid(login)) {
	    errorMap.add("login", messageService.getMessage("error.username.invalid.characters"));
	} else {
	    userForm.setLogin(login);
	    User existingUser = UserSaveController.service.getUserByLogin(login);
	    if (existingUser != null) {
		if ((user != null) && StringUtils.equals(user.getLogin(), login)) {
		    // login exists - it's the user's current login
		} else {
		    errorMap.add("login", messageService.getMessage("error.login.unique",
			    "(" + login + ", ID: " + existingUser.getUserId() + ")"));
		}
	    }
	}

	//first name validation
	String firstName = (userForm.getFirstName() == null) ? null : userForm.getFirstName();
	if (StringUtils.isBlank(firstName)) {
	    errorMap.add("firstName", messageService.getMessage("error.firstname.required"));
	} else if (!ValidationUtil.isFirstLastNameValid(firstName)) {
	    errorMap.add("firstName", messageService.getMessage("error.firstname.invalid.characters"));
	}

	//last name validation
	String lastName = (userForm.getLastName() == null) ? null : userForm.getLastName();
	if (StringUtils.isBlank(lastName)) {
	    errorMap.add("lastName", messageService.getMessage("error.lastname.required"));
	} else if (!ValidationUtil.isFirstLastNameValid(lastName)) {
	    errorMap.add("lastName", messageService.getMessage("error.lastname.invalid.characters"));
	}

	//user email validation
	String userEmail = (userForm.getEmail() == null) ? null : userForm.getEmail();
	if (StringUtils.isBlank(userEmail)) {
	    errorMap.add("email", messageService.getMessage("error.email.required"));
	} else if (!ValidationUtil.isEmailValid(userEmail)) {
	    errorMap.add("email", messageService.getMessage("error.valid.email.required"));
	}

	if (errorMap.isEmpty()) {
	    if (edit) { // edit user
		UserSaveController.log.debug("editing userId: " + userId);
		// hash the new password if necessary, and audit the fact
		userForm.setPassword(user.getPassword());
		BeanUtils.copyProperties(user, userForm);
		user.setLocale(locale);
		user.setAuthenticationMethod(authenticationMethod);

		Theme cssTheme = (Theme) UserSaveController.service.findById(Theme.class, userForm.getUserTheme());
		user.setTheme(cssTheme);

		UserSaveController.service.saveUser(user);
	    } else { // create user

		//password validation
		String password2 = userForm.getPassword2();
		String password = (userForm.getPassword() == null) ? null : userForm.getPassword();
		if (StringUtils.isBlank(password)) {
		    errorMap.add("password", messageService.getMessage("error.password.required"));
		}
		if (!StringUtils.equals(password, (userForm.getPassword2()))) {
		    errorMap.add("password", messageService.getMessage("error.newpassword.mismatch"));
		}
		if (!ValidationUtil.isPasswordValueValid(password, password2)) {
		    errorMap.add("password", messageService.getMessage("error.newpassword.mismatch"));
		}

		if (errorMap.isEmpty()) {
		    user = new User();
		    String salt = HashUtil.salt();
		    String passwordHash = HashUtil.sha256(userForm.getPassword(), salt);
		    BeanUtils.copyProperties(user, userForm);
		    user.setSalt(salt);
		    user.setPassword(passwordHash);
		    UserSaveController.log.debug("creating user... new login: " + user.getLogin());
		    if (errorMap.isEmpty()) {
			// TODO set theme according to user input
			// instead of server default.
			user.setTheme(UserSaveController.service.getDefaultTheme());
			user.setDisabledFlag(false);
			user.setCreateDate(new Date());
			user.setAuthenticationMethod((AuthenticationMethod) UserSaveController.service
				.findByProperty(AuthenticationMethod.class, "authenticationMethodName", "LAMS-Database")
				.get(0));
			user.setUserId(null);
			user.setLocale(locale);

			Theme theme = (Theme) UserSaveController.service.findById(Theme.class, userForm.getUserTheme());
			user.setTheme(theme);

			UserSaveController.service.saveUser(user);

			// make 'create user' audit log entry
			UserSaveController.service.logUserCreated(user, sysadmin);

			UserSaveController.log.debug("user: " + user.toString());
		    }
		}
	    }
	}

	if (errorMap.isEmpty()) {
	    if ((orgId == null) || (orgId == 0)) {
		return "forward:/usersearch.do";
	    }
	    if (edit) {
		request.setAttribute("org", orgId);
		return "forward:/usermanage.do";
	    } else {
		request.setAttribute("orgId", orgId);
		request.setAttribute("userId", user.getUserId());
		return "forward:/userroles.do";
	    }
	} else {
	    request.setAttribute("errorMap", errorMap);
	    request.setAttribute("orgId", orgId);
	    return "/user/edit.do";
	}
    }

    @RequestMapping(path = "/changePass", method = RequestMethod.POST)
    public String changePass(HttpServletRequest request, HttpServletResponse response) throws Exception {

	UserSaveController.service = AdminServiceProxy.getService(applicationContext.getServletContext());
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	Integer userId = WebUtil.readIntParam(request, "userId", true);
	ISecurityService securityService = AdminServiceProxy.getSecurityService(applicationContext.getServletContext());
	Integer loggeduserId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();

	// check if logged in User is Sysadmin
	if (!securityService.isSysadmin(loggeduserId, "Change Password of User " + userId, true)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Sysadmin has edit permisions");
	    return null;
	}

	String password = WebUtil.readStrParam(request, "password");
	String password2 = WebUtil.readStrParam(request, "password2");

	//password validation
	if (StringUtils.isBlank(password)) {
	    errorMap.add("password", messageService.getMessage("error.password.required"));
	}

	if (!StringUtils.equals(password, password2)) {
	    errorMap.add("password", messageService.getMessage("error.newpassword.mismatch"));
	}
	if (!ValidationUtil.isPasswordValueValid(password, password2)) {
	    errorMap.add("password", messageService.getMessage("label.password.restrictions"));
	}

	if (errorMap.isEmpty()) {
	    User user = (User) UserSaveController.service.findById(User.class, userId);
	    String salt = HashUtil.salt();
	    String passwordHash = HashUtil.sha256(password, salt);
	    user.setSalt(salt);
	    user.setPassword(passwordHash);
	    UserSaveController.service.saveUser(user);
	    return "forward:/user/edit.do";
	}
	request.setAttribute("errorMap", errorMap);
	return "userChangePass";

    }

}
