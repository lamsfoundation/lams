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

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.index.IndexLinkBean;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.policies.service.IPolicyService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    private static Logger log = Logger.getLogger(IndexController.class);

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private IIntegrationService integrationService;
    @Autowired
    private IPolicyService policyService;

    @RequestMapping("")
    public String unspecified(HttpServletRequest request, HttpServletResponse response) throws Exception {

	IndexController.setHeaderLinks(request);
	setAdminLinks(request);
	if (request.isUserInRole(Role.AUTHOR)) {
	    request.setAttribute("showQbCollectionsLink", true);
	}

	// check if this is user's first login; some action (like displaying a dialog for disabling tutorials) can be
	// taken based on that parameter; immediatelly, the value in DB is updated
	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	User user = userManagementService.getUserByLogin(userDTO.getLogin());
	if (userDTO.isFirstLogin()) {
	    request.setAttribute("firstLogin", true);
	    user.setFirstLogin(false);
	    userManagementService.saveUser(user);
	    userDTO.setFirstLogin(false);
	}

	if (user.getPasswordChangeDate() != null) {
	    int expirationPeriod = Configuration.getAsInt(ConfigurationKeys.PASSWORD_EXPIRATION_MONTHS);
	    if (expirationPeriod > 0) {
		LocalDateTime expirationDate = user.getPasswordChangeDate().plusMonths(expirationPeriod);
		if (LocalDateTime.now().isAfter(expirationDate)) {
		    user.setChangePassword(true);
		    userManagementService.save(user);
		    return "forward:/password.do?passwordExpired=true";
		}
	    }
	}

	if (user.getChangePassword() != null && user.getChangePassword()) {
	    return "forward:/password.do";
	}

	// check if user needs to get his shared two-factor authorization secret
	if (user.isTwoFactorAuthenticationEnabled() && user.getTwoFactorAuthenticationSecret() == null) {
	    return "forward:/twoFactorAuthentication.do";
	}

	// check if user needs to get his shared two-factor authorization secret
	if (policyService.isPolicyConsentRequiredForUser(user.getUserId())) {
	    return "forward:/policyConsents.do";
	}

	request.setAttribute("portraitUuid", user.getPortraitUuid());

	String redirectParam = WebUtil.readStrParam(request, "redirect", true);
	if (StringUtils.equals(redirectParam, "profile")) {
	    return "forward:/profile/view.do";
	} else if (StringUtils.equals(redirectParam, "editprofile")) {
	    return "forward:/profile/edit.do";
	} else if (StringUtils.equals(redirectParam, "password")) {
	    String passwordUrl = "forward:/password.do";
	    String redirectUrlParam = WebUtil.readStrParam(request, "redirectURL", true);
	    if (StringUtils.isNotBlank(redirectUrlParam)) {
		passwordUrl = WebUtil.appendParameterToURL(passwordUrl, "redirectURL",
			URLEncoder.encode(redirectUrlParam, "UTF-8"));
	    }
	    return passwordUrl;
	} else if (StringUtils.equals(redirectParam, "portrait")) {
	    return "forward:/portrait.do";
	} else if (StringUtils.equals(redirectParam, "lessons")) {
	    return "forward:/profile/lessons.do";
	}

	// This test also appears in LoginAsAction
	Boolean allowDirectAccessIntegrationLearner = Configuration
		.getAsBoolean(ConfigurationKeys.ALLOW_DIRECT_ACCESS_FOR_INTEGRATION_LEARNERS);
	if (!allowDirectAccessIntegrationLearner) {
	    boolean isIntegrationUser = integrationService.isIntegrationUser(userDTO.getUserID());
	    //prevent integration users with mere learner rights from accessing index.do
	    if (isIntegrationUser && !request.isUserInRole(Role.AUTHOR) && !request.isUserInRole(Role.MONITOR)
		    && !request.isUserInRole(Role.GROUP_MANAGER) && !request.isUserInRole(Role.SYSADMIN)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN,
			"Integration users with learner right are not allowed to access this page");
		return null;
	    }
	}

	// only show the growl warning the first time after a user has logged in & if turned on in configuration
	Boolean tzWarning = Configuration.getAsBoolean(ConfigurationKeys.SHOW_TIMEZONE_WARNING);
	request.setAttribute("showTimezoneWarning", tzWarning);
	request.setAttribute("showTimezoneWarningPopup", false);
	if (tzWarning) {
	    Boolean ssWarningShown = (Boolean) ss.getAttribute("timezoneWarningShown");
	    if (!Boolean.TRUE.equals(ssWarningShown)) {
		ss.setAttribute("timezoneWarningShown", Boolean.TRUE);
		request.setAttribute("showTimezoneWarningPopup", true);
	    }
	}

	List<Organisation> favoriteOrganisations = userManagementService
		.getFavoriteOrganisationsByUser(userDTO.getUserID());
	request.setAttribute("favoriteOrganisations", favoriteOrganisations);
	request.setAttribute("activeOrgId", user.getLastVisitedOrganisationId());

	boolean isSysadmin = request.isUserInRole(Role.SYSADMIN);
	int userCoursesCount = userManagementService.getCountActiveCoursesByUser(userDTO.getUserID(), isSysadmin, null);
	request.setAttribute("isCourseSearchOn", userCoursesCount > 10);

	return "main";
    }

    private static void setHeaderLinks(HttpServletRequest request) {
	List<IndexLinkBean> headerLinks = new ArrayList<>();
	if (request.isUserInRole(Role.AUTHOR)) {
	    headerLinks.add(new IndexLinkBean("index.author", "javascript:showAuthoringDialog()"));
	}

	String customTabText = Configuration.get(ConfigurationKeys.CUSTOM_TAB_TITLE);
	String customTabLink = Configuration.get(ConfigurationKeys.CUSTOM_TAB_LINK);
	if (customTabLink != null && customTabLink.trim().length() > 0) {
	    headerLinks.add(new IndexLinkBean(customTabText, "javascript:openCustom(\"" + customTabLink + "\")"));
	}

	request.setAttribute("headerLinks", headerLinks);
    }

    private void setAdminLinks(HttpServletRequest request) {
	List<IndexLinkBean> adminLinks = new ArrayList<>();
	if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.GROUP_MANAGER)) {
	    adminLinks.add(new IndexLinkBean("index.courseman", "javascript:openOrgManagement("
		    + userManagementService.getRootOrganisation().getOrganisationId() + ')'));
	}
	if (request.isUserInRole(Role.SYSADMIN) || userManagementService.isUserGlobalGroupManager()) {
	    adminLinks.add(new IndexLinkBean("index.sysadmin", "javascript:openSysadmin()"));
	}

	request.setAttribute("adminLinks", adminLinks);
    }

    /**
     * Returns list of organisations for user. Used by offcanvas tablesorter on main.jsp.
     */
    @ResponseBody
    @RequestMapping("/getOrgs")
    public void getOrgs(HttpServletRequest request, HttpServletResponse res) throws IOException, ServletException {
	User loggedInUser = userManagementService.getUserByLogin(request.getRemoteUser());

	Integer userId = loggedInUser.getUserId();
	boolean isSysadmin = request.isUserInRole(Role.SYSADMIN);
	String searchString = WebUtil.readStrParam(request, "fcol[1]", true);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
//	Integer isSort1 = WebUtil.readIntParam(request, "column[0]", true);
//	Integer isSort2 = WebUtil.readIntParam(request, "column[1]", true);
//	Integer isSort3 = WebUtil.readIntParam(request, "column[2]", true);
//	Integer isSort4 = WebUtil.readIntParam(request, "column[3]", true);
//
//	String sortBy = "";
//	String sortOrder = "";
//	if (isSort2 != null) {
//	    sortBy = "name";
//	    sortOrder = isSort2.equals(0) ? "ASC" : "DESC";
//
//	}

	List<OrganisationDTO> orgDtos = userManagementService.getActiveCoursesByUser(userId, isSysadmin, page, size,
		searchString);

	ObjectNode responcedata = JsonNodeFactory.instance.objectNode();
	responcedata.put("total_rows",
		userManagementService.getCountActiveCoursesByUser(userId, isSysadmin, searchString));

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	for (OrganisationDTO orgDto : orgDtos) {
	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();
	    responseRow.put("id", orgDto.getOrganisationID());
	    String orgName = orgDto.getName() == null ? "" : orgDto.getName();
	    responseRow.put("name", HtmlUtils.htmlEscape(orgName));

	    rows.add(responseRow);
	}
	responcedata.set("rows", rows);
	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responcedata.toString()));
    }

    /**
     * Toggles whether organisation is marked as favorite.
     */
    @RequestMapping("/toggleFavoriteOrganisation")
    public String toggleFavoriteOrganisation(HttpServletRequest request) throws IOException, ServletException {
	Integer orgId = WebUtil.readIntParam(request, "orgId", false);
	Integer userId = getUserId();

	if (orgId != null) {
	    userManagementService.toggleOrganisationFavorite(orgId, userId);
	}

	List<Organisation> favoriteOrganisations = userManagementService.getFavoriteOrganisationsByUser(userId);
	request.setAttribute("favoriteOrganisations", favoriteOrganisations);

	String activeOrgId = request.getParameter("activeOrgId");
	request.setAttribute("activeOrgId", activeOrgId);

	return "favoriteOrganisations";
    }

    /**
     * Saves to DB last visited organisation. It's required for displaying some org on main.jsp next time user logs in.
     */
    @ResponseBody
    @RequestMapping("/storeLastVisitedOrganisation")
    public void storeLastVisitedOrganisation(HttpServletRequest request) throws IOException, ServletException {
	Integer lastVisitedOrganisationId = WebUtil.readIntParam(request, "orgId", false);

	//saves to DB last visited organisation
	if (lastVisitedOrganisationId != null) {
	    User user = userManagementService.getUserByLogin(request.getRemoteUser());
	    user.setLastVisitedOrganisationId(lastVisitedOrganisationId);
	    userManagementService.saveUser(user);
	}

    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO learner = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return learner != null ? learner.getUserID() : null;
    }
}