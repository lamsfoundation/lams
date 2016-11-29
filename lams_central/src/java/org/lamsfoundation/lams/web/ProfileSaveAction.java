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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.lamsfoundation.lams.themes.Theme;
import org.lamsfoundation.lams.usermanagement.SupportedLocale;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.ValidationUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 */
public class ProfileSaveAction extends Action {

    private static Logger log = Logger.getLogger(ProfileSaveAction.class);
    private static IUserManagementService service;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (isCancelled(request)) {
	    return mapping.findForward("profile");
	}

	ActionMessages errors = new ActionMessages();

	if (!Configuration.getAsBoolean(ConfigurationKeys.PROFILE_EDIT_ENABLE)) {
	    if (!Configuration.getAsBoolean(ConfigurationKeys.PROFILE_PARTIAL_EDIT_ENABLE)) {
		return mapping.findForward("editprofile");
	    }
	}

	User requestor = getService().getUserByLogin(request.getRemoteUser());
	DynaActionForm userForm = (DynaActionForm) form;

	// check requestor is same as user being edited
	if (!requestor.getLogin().equals(userForm.get("login"))) {
	    ProfileSaveAction.log
		    .warn(requestor.getLogin() + " tried to edit profile of user " + userForm.get("login"));
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.authorisation"));
	    saveErrors(request, errors);
	    return mapping.findForward("editprofile");
	}

	// (dyna)form validation
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

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    return (mapping.findForward("editprofile"));
	}

	if (!Configuration.getAsBoolean(ConfigurationKeys.PROFILE_EDIT_ENABLE)
		&& Configuration.getAsBoolean(ConfigurationKeys.PROFILE_PARTIAL_EDIT_ENABLE)) {
	    // update only contact fields
	    requestor.setEmail(userForm.getString("email"));
	    requestor.setDayPhone(userForm.getString("dayPhone"));
	    requestor.setEveningPhone(userForm.getString("eveningPhone"));
	    requestor.setMobilePhone(userForm.getString("mobilePhone"));
	    requestor.setFax(userForm.getString("fax"));
	} else {
	    // update all fields
	    BeanUtils.copyProperties(requestor, userForm);
	    SupportedLocale locale = (SupportedLocale) getService().findById(SupportedLocale.class,
		    (Integer) userForm.get("localeId"));
	    requestor.setLocale(locale);

	    Theme cssTheme = (Theme) getService().findById(Theme.class, (Long) userForm.get("userTheme"));
	    requestor.setTheme(cssTheme);

	    if ((userForm.get("disableLamsCommunityUsername") != null)
		    && (Boolean) userForm.get("disableLamsCommunityUsername")) {
		requestor.setLamsCommunityToken(null);
		requestor.setLamsCommunityUsername(null);
	    }
	}

	getService().saveUser(requestor);

	// replace UserDTO in the shared session
	HttpSession ss = SessionManager.getSession();
	ss.setAttribute(AttributeNames.USER, requestor.getUserDTO());

	return mapping.findForward("profile");
    }

    private IUserManagementService getService() {
	if (ProfileSaveAction.service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    ProfileSaveAction.service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return ProfileSaveAction.service;
    }

}
