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

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.web.form.UserForm;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Jun-Dir Liew
 */

@Controller
@RequestMapping("/usersave")
public class UserSaveController {
    private static Logger log = Logger.getLogger(UserSaveController.class);

    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;

    @RequestMapping(path = "/saveUserDetails", method = RequestMethod.POST)
    public String saveUserDetails(@ModelAttribute UserForm userForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// action input
	Integer orgId = userForm.getOrgId();
	Integer userId = userForm.getUserId();

	boolean canEditRole = false;

	// appadmin, global course admins can add/change users and their roles.
	// course manager can add/change users and their roles iff CourseAdminCanAddNewUsers
	// course admin can add/change users but only set role to learner iff CourseAdminCanAddNewUsers
	Integer rootOrgId = userManagementService.getRootOrganisation().getOrganisationId();
	if (request.isUserInRole(Role.APPADMIN) || userManagementService.isUserGlobalGroupManager()) {
	    canEditRole = true;
	} else {

	    Integer loggeduserId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER))
		    .getUserID();
	    Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, orgId);
	    if (organisation == null) {
		String message = "No permission to access organisation " + orgId;
		logErrorMessage(userId, message);
		response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
		return null;
	    }
	    if (organisation.getOrganisationType().getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)) {
		organisation = organisation.getParentOrganisation();
	    }
	    if (userManagementService.isUserInRole(loggeduserId, organisation.getOrganisationId(), Role.GROUP_MANAGER)
		    && !orgId.equals(rootOrgId)) {
		canEditRole = true;
	    } else {
		String message = "No permission to edit user in organisation " + orgId;
		logErrorMessage(userId, message);
		response.sendError(HttpServletResponse.SC_FORBIDDEN, message);
		return null;
	    }
	}

	UserDTO appadmin = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	log.debug("orgId: " + orgId);
	boolean edit = false;
	SupportedLocale locale = (SupportedLocale) userManagementService.findById(SupportedLocale.class,
		userForm.getLocaleId());
	AuthenticationMethod authenticationMethod = (AuthenticationMethod) userManagementService
		.findById(AuthenticationMethod.class, userForm.getAuthenticationMethodId());
	log.debug("locale: " + locale);
	log.debug("authenticationMethod:" + authenticationMethod);

	if (request.getAttribute("CANCEL") != null) {
	    if ((orgId == null) || (orgId == 0)) {
		return "redirect:../usersearch.do";
	    }
	    return "redirect:../usermanage.do?org" + orgId;
	}

	User user = null;
	if (userId != null) {
	    edit = true;
	    user = (User) userManagementService.findById(User.class, userId);
	}

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	// login validation
	String login = userForm.getLogin() == null ? null : userForm.getLogin().trim();
	if (StringUtils.isBlank(login)) {
	    errorMap.add("login", messageService.getMessage("error.login.required"));
	} else if (!ValidationUtil.isUserNameValid(login)) {
	    errorMap.add("login", messageService.getMessage("error.username.invalid.characters"));
	} else {
	    userForm.setLogin(login);
	    User existingUser = userManagementService.getUserByLogin(login);
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
		log.debug("editing userId: " + userId);
		// hash the new password if necessary, and audit the fact
		userForm.setPassword(user.getPassword());
		// retain original create date
		userForm.setCreateDate(user.getCreateDate());
		BeanUtils.copyProperties(user, userForm);
		user.setLocale(locale);
		user.setAuthenticationMethod(authenticationMethod);

		if (userManagementService.hasRoleInOrganisation(user, Role.ROLE_APPADMIN)
			&& !request.isUserInRole(Role.SYSADMIN)) {
		    // appadmins need to have two factor auths always on, unless sysadmin says otherwise
		    user.setTwoFactorAuthenticationEnabled(true);
		}

		Theme cssTheme = (Theme) userManagementService.findById(Theme.class, userForm.getUserTheme());
		user.setTheme(cssTheme);

		userManagementService.saveUser(user);
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
		    errorMap.add("password", messageService.getMessage("label.password.restrictions"));
		}

		if (errorMap.isEmpty()) {
		    user = new User();
		    BeanUtils.copyProperties(user, userForm);
		    if (!ValidationUtil.isPasswordNotUserDetails(password, user)) {
			errorMap.add("password", messageService.getMessage("label.password.restrictions"));
		    }

		    log.debug("creating user... new login: " + user.getLogin());

		    user.setDisabledFlag(false);
		    user.setCreateDate(new Date());
		    user.setAuthenticationMethod((AuthenticationMethod) userManagementService
			    .findByProperty(AuthenticationMethod.class, "authenticationMethodName", "LAMS-Database")
			    .get(0));
		    user.setUserId(null);
		    user.setLocale(locale);

		    Theme theme = null;
		    if (userForm.getUserTheme() != null) {
			theme = (Theme) userManagementService.findById(Theme.class, userForm.getUserTheme());
		    }
		    if (theme == null) {
			theme = userManagementService.getDefaultTheme();
		    }
		    user.setTheme(theme);

		    userManagementService.saveUser(user);
		    userManagementService.updatePassword(user, password);

		    // make 'create user' audit log entry
		    userManagementService.logUserCreated(user, appadmin);

		    log.debug("user: " + user.toString());
		}
	    }
	}

	if (errorMap.isEmpty()) {
	    if ((orgId == null) || (orgId == 1)) {
		return "redirect:/usersearch.do";
	    }
	    if (!edit && !canEditRole) {
		// Course Admin created new learner
		userManagementService.setRolesForUserOrganisation(user, orgId,
			Arrays.asList(Role.ROLE_LEARNER.toString()));
		return "redirect:/usermanage.do?org=" + orgId;
	    } else if (edit) {
		return "redirect:/usermanage.do?org=" + orgId;
	    } else {
		return "redirect:/userroles.do?orgId=" + orgId + "&userId=" + user.getUserId();
	    }
	} else {
	    request.setAttribute("orgId", orgId);
	    request.setAttribute("errorMap", errorMap);
	    return "forward:/user/edit.do";
	}
    }

    private void logErrorMessage(Integer userId, String message) {
	String fullError = null;
	if (userId != null) {
	    fullError = new StringBuilder("Updating user ").append(userId).append(": ").append(message).toString();
	} else {
	    fullError = new StringBuilder("Creating new user:  ").append(message).toString();
	}
	log.error(fullError);
    }

    @RequestMapping(path = "/changePass")
    public String changePass(@ModelAttribute UserForm userForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer userId = WebUtil.readIntParam(request, "userId", true);
	userForm.setUserId(userId);
	Integer loggeduserId = ((UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER)).getUserID();

	// check if logged in User is Appadmin
	if (!securityService.isAppadmin(loggeduserId, "Change Password of User " + userId, true)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only Appadmin has edit permisions");
	    return null;
	}

	String password = WebUtil.readStrParam(request, "password");
	String password2 = WebUtil.readStrParam(request, "password2");

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	//password validation
	if (StringUtils.isBlank(password)) {
	    errorMap.add("password", messageService.getMessage("error.password.required"));
	}

	if (!StringUtils.equals(password, password2)) {
	    errorMap.add("password", messageService.getMessage("error.newpassword.mismatch"));
	}

	User user = (User) userManagementService.findById(User.class, userId);
	if (!ValidationUtil.isPasswordValueValid(password, password2, user)) {
	    errorMap.add("password", messageService.getMessage("label.password.restrictions"));
	}

	if (errorMap.isEmpty()) {

	    userManagementService.updatePassword(user, password);
	    return "forward:/user/edit.do";
	}
	request.setAttribute("errorMap", errorMap);
	return "userChangePass";

    }

}
