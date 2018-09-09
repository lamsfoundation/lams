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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.index.IndexLinkBean;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.integration.service.IntegrationService;
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
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 */
public class IndexAction extends LamsDispatchAction {

    private static final String PATH_LAMS_PLANNER_WAR = "lams-planner.war";

    private static Logger log = Logger.getLogger(IndexAction.class);
    private static IUserManagementService userManagementService;
    private static IIntegrationService integrationService;
    private static IPolicyService policyService;

    @Override
    @SuppressWarnings("unchecked")
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IndexAction.setHeaderLinks(request);
	setAdminLinks(request);

	// check if this is user's first login; some action (like displaying a tour) can be
	// taken based on that parameter; immediatelly, the value in DB is updated
	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if (userDTO.isFirstLogin()) {
	    request.setAttribute("firstLogin", true);
	    User user = getUserManagementService().getUserByLogin(userDTO.getLogin());
	    user.setFirstLogin(false);
	    getUserManagementService().saveUser(user);
	    userDTO.setFirstLogin(false);
	}

	// check if user is flagged as needing to change their password
	User loggedInUser = getUserManagementService().getUserByLogin(request.getRemoteUser());
	if (loggedInUser.getChangePassword() != null && loggedInUser.getChangePassword()) {
	    return mapping.findForward("password");
	}

	// check if user needs to get his shared two-factor authorization secret
	if (loggedInUser.isTwoFactorAuthenticationEnabled()
		&& loggedInUser.getTwoFactorAuthenticationSecret() == null) {
	    return mapping.findForward("twoFactorAuthentication");
	}
	
	// check if user needs to get his shared two-factor authorization secret
	if (getPolicyService().isPolicyConsentRequiredForUser(loggedInUser.getUserId())) {
	    return mapping.findForward("policyConsents");
	}

	User user = getUserManagementService().getUserByLogin(userDTO.getLogin());
	request.setAttribute("portraitUuid", user.getPortraitUuid());

	String method = WebUtil.readStrParam(request, "method", true);
	if (StringUtils.equals(method, "profile")) {
	    return mapping.findForward("profile");
	} else if (StringUtils.equals(method, "editprofile")) {
	    return mapping.findForward("editprofile");
	} else if (StringUtils.equals(method, "password")) {
	    return mapping.findForward("password");
	} else if (StringUtils.equals(method, "portrait")) {
	    return mapping.findForward("portrait");
	} else if (StringUtils.equals(method, "lessons")) {
	    return mapping.findForward("lessons");
	}

	
	// This test also appears in LoginAsAction
	Boolean allowDirectAccessIntegrationLearner = Configuration
		.getAsBoolean(ConfigurationKeys.ALLOW_DIRECT_ACCESS_FOR_INTEGRATION_LEARNERS);
	if (!allowDirectAccessIntegrationLearner) {
	    boolean isIntegrationUser = getIntegrationService().isIntegrationUser(userDTO.getUserID());
	    //prevent integration users with mere learner rights from accessing index.do
	    if (isIntegrationUser && !request.isUserInRole(Role.AUTHOR) && !request.isUserInRole(Role.MONITOR)
		    && !request.isUserInRole(Role.GROUP_MANAGER) && !request.isUserInRole(Role.GROUP_ADMIN)
		    && !request.isUserInRole(Role.SYSADMIN)) {
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

	return mapping.findForward("main");
    }

    private static void setHeaderLinks(HttpServletRequest request) {
	List<IndexLinkBean> headerLinks = new ArrayList<>();
	if (request.isUserInRole(Role.AUTHOR)) {
	    if (IndexAction.isPedagogicalPlannerAvailable()) {
		headerLinks.add(new IndexLinkBean("index.planner", "javascript:openPedagogicalPlanner()"));
	    }
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
	if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.GROUP_ADMIN)
		|| request.isUserInRole(Role.GROUP_MANAGER)) {
	    adminLinks.add(new IndexLinkBean("index.courseman", "javascript:openOrgManagement("
		    + getUserManagementService().getRootOrganisation().getOrganisationId() + ')'));
	}
	if (request.isUserInRole(Role.SYSADMIN) || getUserManagementService().isUserGlobalGroupAdmin()) {
	    adminLinks.add(new IndexLinkBean("index.sysadmin", "javascript:openSysadmin()"));
	}
	request.setAttribute("adminLinks", adminLinks);
    }

    /**
     * Returns list of organisations for user. Used by offcanvas tablesorter on main.jsp.
     */
    public ActionForward getOrgs(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException {
	getUserManagementService();
	User loggedInUser = getUserManagementService().getUserByLogin(request.getRemoteUser());

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
	return null;
    }

    /**
     * Toggles whether organisation is marked as favorite.
     */
    public ActionForward toggleFavoriteOrganisation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException {
	getUserManagementService();
	Integer orgId = WebUtil.readIntParam(request, "orgId", false);
	Integer userId = getUserId();

	if (orgId != null) {
	    userManagementService.toggleOrganisationFavorite(orgId, userId);
	}

	List<Organisation> favoriteOrganisations = userManagementService.getFavoriteOrganisationsByUser(userId);
	request.setAttribute("favoriteOrganisations", favoriteOrganisations);

	String activeOrgId = request.getParameter("activeOrgId");
	request.setAttribute("activeOrgId", activeOrgId);

	return mapping.findForward("favoriteOrganisations");
    }

    /**
     * Saves to DB last visited organisation. It's required for displaying some org on main.jsp next time user logs in.
     */
    public ActionForward storeLastVisitedOrganisation(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse res) throws IOException, ServletException {
	getUserManagementService();
	Integer lastVisitedOrganisationId = WebUtil.readIntParam(request, "orgId", false);

	//saves to DB last visited organisation
	if (lastVisitedOrganisationId != null) {
	    User user = userManagementService.getUserByLogin(request.getRemoteUser());
	    user.setLastVisitedOrganisationId(lastVisitedOrganisationId);
	    userManagementService.saveUser(user);
	}

	return null;
    }

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO learner = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return learner != null ? learner.getUserID() : null;
    }

    private IIntegrationService getIntegrationService() {
	if (integrationService == null) {
	    integrationService = (IntegrationService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext()).getBean("integrationService");
	}
	return integrationService;
    }

    private IUserManagementService getUserManagementService() {
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return userManagementService;
    }
    
    private IPolicyService getPolicyService() {
	if (policyService == null) {
	    policyService = (IPolicyService) WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext()).getBean("policyService");
	}
	return policyService;
    }

    private static boolean isPedagogicalPlannerAvailable() {
	String lamsEarPath = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR);
	String plannerPath = lamsEarPath + File.separator + PATH_LAMS_PLANNER_WAR;
	File plannerDir = new File(plannerPath);
	return plannerDir.exists();
    }
}