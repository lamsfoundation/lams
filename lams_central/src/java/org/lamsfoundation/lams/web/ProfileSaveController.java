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

package org.lamsfoundation.lams.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author jliew
 */
@Controller
public class ProfileSaveController {

    private static Logger log = Logger.getLogger(ProfileSaveController.class);
    @Autowired
    @Qualifier("userManagementService")
    private IUserManagementService service;
    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;

    @RequestMapping(path = "/saveprofile")
    public String execute(@ModelAttribute("newForm") UserForm userForm, HttpServletRequest request) throws Exception {

	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	if (!Configuration.getAsBoolean(ConfigurationKeys.PROFILE_EDIT_ENABLE)) {
	    if (!Configuration.getAsBoolean(ConfigurationKeys.PROFILE_PARTIAL_EDIT_ENABLE)) {
		request.setAttribute("redirect", "editprofile");
		return "redirect:/index.do";
	    }
	}

	User requestor = service.getUserByLogin(request.getRemoteUser());

	// check requestor is same as user being edited
	if (!requestor.getLogin().equals(userForm.getLogin())) {
	    ProfileSaveController.log
		    .warn(requestor.getLogin() + " tried to edit profile of user " + userForm.getLogin());
	    errorMap.add("GLOBAL", messageService.getMessage("error.authorisation"));
	    request.setAttribute("errorMap", errorMap);
	    request.setAttribute("redirect", "editprofile");
	    return "redirect:/index.do";
	}

	// (dyna)form validation
	//first name validation
	String firstName = (userForm.getFirstName() == null) ? null : (String) userForm.getFirstName();
	if (StringUtils.isBlank(firstName)) {
	    errorMap.add("firstName", messageService.getMessage("error.firstname.required"));
	} else if (!ValidationUtil.isFirstLastNameValid(firstName)) {
	    errorMap.add("firstName", messageService.getMessage("error.firstname.invalid.characters"));
	}

	//last name validation
	String lastName = (userForm.getLastName() == null) ? null : (String) userForm.getLastName();
	if (StringUtils.isBlank(lastName)) {
	    errorMap.add("lastName", messageService.getMessage("error.lastname.required"));
	} else if (!ValidationUtil.isFirstLastNameValid(lastName)) {
	    errorMap.add("lastName", messageService.getMessage("error.lastname.invalid.characters"));
	}

	//user email validation
	String userEmail = (userForm.getEmail() == null) ? null : (String) userForm.getEmail();
	if (StringUtils.isBlank(userEmail)) {
	    errorMap.add("email", messageService.getMessage("error.email.required"));
	} else if (!ValidationUtil.isEmailValid(userEmail)) {
	    errorMap.add("email", messageService.getMessage("error.valid.email.required"));
	}

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    request.setAttribute("redirect", "editprofile");
	    return "redirect:/index.do";
	}

	if (!Configuration.getAsBoolean(ConfigurationKeys.PROFILE_EDIT_ENABLE)
		&& Configuration.getAsBoolean(ConfigurationKeys.PROFILE_PARTIAL_EDIT_ENABLE)) {
	    // update only contact fields
	    requestor.setEmail(userForm.getEmail());
	    requestor.setDayPhone(userForm.getDayPhone());
	    requestor.setEveningPhone(userForm.getEveningPhone());
	    requestor.setMobilePhone(userForm.getMobilePhone());
	    requestor.setFax(userForm.getFax());
	} else {
	    // update all fields
	    BeanUtils.copyProperties(requestor, userForm);
	    SupportedLocale locale = (SupportedLocale) service.findById(SupportedLocale.class, userForm.getLocaleId());
	    requestor.setLocale(locale);

	    Theme cssTheme = (Theme) service.findById(Theme.class, userForm.getUserTheme());
	    requestor.setTheme(cssTheme);
	}

	service.saveUser(requestor);

	// replace UserDTO in the shared session
	HttpSession ss = SessionManager.getSession();
	ss.setAttribute(AttributeNames.USER, requestor.getUserDTO());

	request.setAttribute("redirect", "profile");
	return "redirect:/index.do";
    }

}
