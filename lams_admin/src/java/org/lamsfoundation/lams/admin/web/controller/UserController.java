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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.admin.web.dto.UserOrgRoleDTO;
import org.lamsfoundation.lams.admin.web.form.UserForm;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.themes.service.IThemeService;
import org.lamsfoundation.lams.timezone.Timezone;
import org.lamsfoundation.lams.timezone.dto.TimezoneDTO;
import org.lamsfoundation.lams.timezone.service.ITimezoneService;
import org.lamsfoundation.lams.timezone.util.TimezoneDTOComparator;
import org.lamsfoundation.lams.usermanagement.AuthenticationMethod;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.LanguageUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Jun-Dir Liew
 */
@Controller
@RequestMapping(path = "/user", method = RequestMethod.POST)
public class UserController {
    private static Logger log = Logger.getLogger(UserController.class);

    @Autowired
    private ILogEventService logEventService;
    @Autowired
    @Qualifier("adminMessageService")
    private MessageService messageService;
    @Autowired
    private IThemeService themeService;
    @Autowired
    private ITimezoneService timezoneService;
    @Autowired
    private IUserManagementService userManagementService;

    private static List<SupportedLocale> locales;
    private static List<AuthenticationMethod> authenticationMethods;

    @RequestMapping(path = "/edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute UserForm userForm, HttpServletRequest request) throws Exception {
	if (locales == null) {
	    locales = userManagementService.findAll(SupportedLocale.class);
	    Collections.sort(locales);
	}
	if (authenticationMethods == null) {
	    authenticationMethods = userManagementService.findAll(AuthenticationMethod.class);
	}

	Integer orgId = WebUtil.readIntParam(request, "orgId", true);
	Integer userId = WebUtil.readIntParam(request, "userId", true);

	userForm.setOrgId(orgId);
	userForm.setUserId(userId);

	// Get all the css themess
	List<Theme> themes = themeService.getAllThemes();
	request.setAttribute("themes", themes);

	// Select the default themes by default
	Theme defaultTheme = themeService.getDefaultTheme();
	for (Theme theme : themes) {
	    if (theme.getThemeId().equals(defaultTheme.getThemeId())) {
		userForm.setUserTheme(theme.getThemeId());
		break;
	    }
	}

	// test requestor's permission
	Organisation org = null;
	Boolean canEdit = userManagementService.isUserGlobalGroupManager();
	if (orgId != null) {
	    org = (Organisation) userManagementService.findById(Organisation.class, orgId);
	    if (!canEdit) {
		OrganisationType orgType = org.getOrganisationType();
		Integer orgIdOfCourse = orgType.getOrganisationTypeId().equals(OrganisationType.CLASS_TYPE)
			? org.getParentOrganisation().getOrganisationId()
			: orgId;
		User requestor = userManagementService.getUserByLogin(request.getRemoteUser());
		if (userManagementService.isUserInRole(requestor.getUserId(), orgIdOfCourse, Role.GROUP_MANAGER)) {
		    Organisation course = (Organisation) userManagementService.findById(Organisation.class,
			    orgIdOfCourse);
		    canEdit = course.getCourseAdminCanAddNewUsers();
		}
	    }
	}

	if (!(canEdit || request.isUserInRole(Role.SYSADMIN))) {
	    request.setAttribute("errorName", "UserController");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}

	// editing a user
	if ((userId != null) && (userId != 0)) {
	    User user = (User) userManagementService.findById(User.class, userId);
	    log.debug("got userid to edit: " + userId);
	    BeanUtils.copyProperties(userForm, user);
	    userForm.setPassword(null);
	    SupportedLocale locale = user.getLocale();
	    userForm.setLocaleId(locale.getLocaleId());

	    AuthenticationMethod authenticationMethod = user.getAuthenticationMethod();
	    userForm.setAuthenticationMethodId(authenticationMethod.getAuthenticationMethodId());
	    // set user's organisations to display
	    request.setAttribute("userOrgRoles", getUserOrgRoles(user));
	    request.setAttribute("globalRoles", getGlobalRoles(user));

	    // Check the user css theme is still installed
	    Long userSelectedTheme = null;
	    if (user.getTheme() != null) {
		for (Theme theme : themes) {
		    if (theme.getThemeId() == user.getTheme().getThemeId()) {
			userSelectedTheme = theme.getThemeId();
			break;
		    }
		}
	    }
	    // if still null, use the default
	    if (userSelectedTheme == null) {
		userSelectedTheme = themeService.getDefaultTheme().getThemeId();
	    }
	    userForm.setUserTheme(userSelectedTheme);
	    userForm.setInitialPortraitId(user.getPortraitUuid());

	    //property available for modification only to sysadmins
	    userForm.setTwoFactorAuthenticationEnabled(user.isTwoFactorAuthenticationEnabled());
	} else { // create a user
	    try {
		SupportedLocale locale = LanguageUtil.getDefaultLocale();
		userForm.setLocaleId(locale.getLocaleId());
		String country = LanguageUtil.getDefaultCountry();
		userForm.setCountry(country);
	    } catch (Exception e) {
		log.debug(e);
	    }

	    Timezone serverTimezone = timezoneService.getServerTimezone();
	    userForm.setTimeZone(serverTimezone.getTimezoneId());
	}
	userForm.setOrgId(org == null ? null : org.getOrganisationId());

	// sysadmins can mark users as required to use two-factor authentication
	if (request.isUserInRole(Role.SYSADMIN)) {
	    request.setAttribute("isSysadmin", true);
	}

	// Get all available time zones
	List<Timezone> availableTimeZones = timezoneService.getDefaultTimezones();
	TreeSet<TimezoneDTO> timezoneDtos = new TreeSet<>(new TimezoneDTOComparator());
	for (Timezone availableTimeZone : availableTimeZones) {
	    String timezoneId = availableTimeZone.getTimezoneId();
	    TimezoneDTO timezoneDto = new TimezoneDTO();
	    timezoneDto.setTimeZoneId(timezoneId);
	    timezoneDto.setDisplayName(TimeZone.getTimeZone(timezoneId).getDisplayName());
	    timezoneDtos.add(timezoneDto);
	}
	request.setAttribute("timezoneDtos", timezoneDtos);

	// for breadcrumb links
	if (org != null) {
	    request.setAttribute("orgName", org.getName());
	    Organisation parentOrg = org.getParentOrganisation();
	    if ((parentOrg != null) && !parentOrg.equals(userManagementService.getRootOrganisation())) {
		request.setAttribute("pOrgId", parentOrg.getOrganisationId());
		request.setAttribute("parentName", parentOrg.getName());
	    }
	}

	request.setAttribute("locales", locales);
	request.setAttribute("countryCodes", LanguageUtil.getCountryCodes(false));
	request.setAttribute("authenticationMethods", authenticationMethods);

	return "user";
    }

    // display user's global roles, if any
    private UserOrgRoleDTO getGlobalRoles(User user) {
	UserOrganisation uo = userManagementService.getUserOrganisation(user.getUserId(),
		userManagementService.getRootOrganisation().getOrganisationId());
	if (uo == null) {
	    return null;
	}
	UserOrgRoleDTO uorDTO = new UserOrgRoleDTO();
	List<String> roles = new ArrayList<>();
	for (Object uor : uo.getUserOrganisationRoles()) {
	    roles.add(((UserOrganisationRole) uor).getRole().getName());
	}
	Collections.sort(roles);
	uorDTO.setOrgName(uo.getOrganisation().getName());
	uorDTO.setRoles(roles);
	return uorDTO;
    }

    // display user's organisations and roles in them
    @SuppressWarnings("unchecked")
    private List<UserOrgRoleDTO> getUserOrgRoles(User user) {
	List<UserOrgRoleDTO> uorDTOs = new ArrayList<>();
	List<UserOrganisation> uos = userManagementService.getUserOrganisationsForUserByTypeAndStatus(user.getLogin(),
		OrganisationType.COURSE_TYPE, OrganisationState.ACTIVE);
	for (UserOrganisation uo : uos) {
	    UserOrgRoleDTO uorDTO = new UserOrgRoleDTO();
	    List<String> roles = new ArrayList<>();
	    for (Object uor : uo.getUserOrganisationRoles()) {
		roles.add(((UserOrganisationRole) uor).getRole().getName());
	    }
	    Collections.sort(roles);
	    uorDTO.setOrgName(uo.getOrganisation().getName());
	    uorDTO.setRoles(roles);
	    List<UserOrgRoleDTO> childDTOs = new ArrayList<>();
	    List<UserOrganisation> childuos = userManagementService.getUserOrganisationsForUserByTypeAndStatusAndParent(
		    user.getLogin(), OrganisationType.CLASS_TYPE, OrganisationState.ACTIVE,
		    uo.getOrganisation().getOrganisationId());
	    for (UserOrganisation childuo : childuos) {
		UserOrgRoleDTO childDTO = new UserOrgRoleDTO();
		List<String> childroles = new ArrayList<>();
		for (Object uor : childuo.getUserOrganisationRoles()) {
		    childroles.add(((UserOrganisationRole) uor).getRole().getName());
		}
		Collections.sort(childroles);
		childDTO.setOrgName(childuo.getOrganisation().getName());
		childDTO.setRoles(childroles);
		childDTOs.add(childDTO);
	    }
	    uorDTO.setChildDTOs(childDTOs);
	    uorDTOs.add(uorDTO);
	}

	return uorDTOs;
    }

    // determine whether to disable or delete user based on their lams data
    @RequestMapping(path = "/remove", method = RequestMethod.POST)
    public String remove(HttpServletRequest request) throws Exception {
	if (!(request.isUserInRole(Role.SYSADMIN) || userManagementService.isUserGlobalGroupManager())) {
	    request.setAttribute("errorName", "UserAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}

	Integer orgId = WebUtil.readIntParam(request, "orgId", true);
	Integer userId = WebUtil.readIntParam(request, "userId");
	User user = (User) userManagementService.findById(User.class, userId);

	Boolean hasData = userManagementService.userHasData(user);

	request.setAttribute("method", (hasData ? "disable" : "delete"));
	request.setAttribute("orgId", orgId);
	request.setAttribute("userId", userId);
	return "remove";
    }

    @RequestMapping(path = "/disable", method = RequestMethod.POST)
    public String disable(HttpServletRequest request) throws Exception {
	if (!(request.isUserInRole(Role.SYSADMIN) || userManagementService.isUserGlobalGroupManager())) {
	    request.setAttribute("errorName", "UserController");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}
	UserDTO sysadmin = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	Integer orgId = WebUtil.readIntParam(request, "orgId", true);
	Integer userId = WebUtil.readIntParam(request, "userId");
	userManagementService.disableUser(userId);
	String[] args = new String[1];
	args[0] = userId.toString();
	String message = messageService.getMessage("audit.user.disable", args);
	logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, sysadmin != null ? sysadmin.getUserID() : null, userId,
		null, null, message);
	if ((orgId == null) || (orgId == 0)) {
	    return "forward:../usersearch.do";
	} else {
	    request.setAttribute("org", orgId);
	    return "forward:../usermanage.do";
	}
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public String delete(HttpServletRequest request) throws Exception {
	if (!(request.isUserInRole(Role.SYSADMIN) || userManagementService.isUserGlobalGroupManager())) {
	    request.setAttribute("errorName", "UserAction");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}
	UserDTO sysadmin = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	Integer orgId = WebUtil.readIntParam(request, "orgId", true);
	Integer userId = WebUtil.readIntParam(request, "userId");
	try {
	    userManagementService.removeUser(userId);
	} catch (Exception e) {
	    request.setAttribute("errorName", "UserController");
	    request.setAttribute("errorMessage", e.getMessage());
	    return "error";
	}
	String[] args = new String[1];
	args[0] = userId.toString();
	String message = messageService.getMessage("audit.user.delete", args);
	logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, sysadmin != null ? sysadmin.getUserID() : null, userId,
		null, null, message);
	if ((orgId == null) || (orgId == 0)) {
	    return "forward:/usersearch.do";
	} else {
	    request.setAttribute("org", orgId);
	    return "forward:/usermanage.do";
	}
    }

    // called from disabled users screen
    @RequestMapping(path = "/enable", method = RequestMethod.POST)
    public String enable(HttpServletRequest request) throws Exception {
	if (!(request.isUserInRole(Role.SYSADMIN) || userManagementService.isUserGlobalGroupManager())) {
	    request.setAttribute("errorName", "UserController");
	    request.setAttribute("errorMessage", messageService.getMessage("error.authorisation"));
	    return "error";
	}

	Integer userId = WebUtil.readIntParam(request, "userId", true);
	User user = (User) userManagementService.findById(User.class, userId);

	log.debug("enabling user: " + userId);
	user.setDisabledFlag(false);
	userManagementService.saveUser(user);

	return "forward:/disabledmanage.do";
    }

}
