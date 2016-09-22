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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.config.Registration;
import org.lamsfoundation.lams.index.IndexLinkBean;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @version
 *
 * <p>
 * <a href="IndexAction.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:fyang@melcoe.mq.edu.au">Fei Yang</a>
 *
 * Created at 16:59:28 on 13/06/2006
 */
/**
 * struts doclet
 *
 * @struts.action path="/index" validate="false"
 *
 * @struts.action-forward name="main" path="/main.jsp"
 * @struts.action-forward name="community" path=".community"
 * @struts.action-forward name="profile" path="/profile.do?method=view"
 * @struts.action-forward name="editprofile" path="/profile.do?method=edit"
 * @struts.action-forward name="password" path="/password.do"
 * @struts.action-forward name="passwordChanged" path=".passwordChangeOk"
 * @struts.action-forward name="portrait" path="/portrait.do"
 * @struts.action-forward name="lessons" path="/profile.do?method=lessons"
 */
public class IndexAction extends Action {

    private static final String PATH_PEDAGOGICAL_PLANNER = "pedagogical_planner";
    private static final String PATH_LAMS_CENTRAL = "lams-central.war";

    private static Logger log = Logger.getLogger(IndexAction.class);
    private static IUserManagementService userManagementService;
    private static IExportToolContentService exportService;
    private static IAuthoringService authoringService;
    private static Configuration configurationService;

    @Override
    @SuppressWarnings("unchecked")
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IndexAction.setHeaderLinks(request);
	setAdminLinks(request);

	// check if this is user's first login; some action (like displaying a dialog for disabling tutorials) can be
	// taken based on that parameter; immediatelly, the value in DB is updated
	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if (userDTO.isFirstLogin()) {
	    request.setAttribute("firstLogin", true);
	    User user = getUserManagementService().getUserByLogin(userDTO.getLogin());
	    user.setFirstLogin(false);
	    getUserManagementService().save(user);
	    ss.removeAttribute(AttributeNames.USER);
	    ss.setAttribute(AttributeNames.USER, user.getUserDTO());
	}

	// check if user is flagged as needing to change their password
	User loggedInUser = getUserManagementService().getUserByLogin(request.getRemoteUser());
	if (loggedInUser.getChangePassword() != null) {
	    if (loggedInUser.getChangePassword()) {
		return mapping.findForward("password");
	    }
	}

	User user = getUserManagementService().getUserByLogin(userDTO.getLogin());
	request.setAttribute("portraitUuid", user.getPortraitUuid());

	String tab = WebUtil.readStrParam(request, "tab", true);
	if (StringUtils.equals(tab, "profile")) {
	    List orgDTOs = getUserManagementService().getArchivedCourseIdsByUser(loggedInUser.getUserId(),
		    request.isUserInRole(Role.SYSADMIN));
	    request.setAttribute("orgDTOs", orgDTOs);
	    return mapping.findForward("profile");
	} else if (StringUtils.equals(tab, "editprofile")) {
	    return mapping.findForward("editprofile");
	} else if (StringUtils.equals(tab, "password")) {
	    return mapping.findForward("password");
	} else if (StringUtils.equals(tab, "passwordChanged")) {
	    request.setAttribute("tab", "profile");
	    return mapping.findForward("passwordChanged");
	} else if (StringUtils.equals(tab, "portrait")) {
	    return mapping.findForward("portrait");
	} else if (StringUtils.equals(tab, "lessons")) {
	    return mapping.findForward("lessons");
	} else if (StringUtils.equals(tab, "community")) {

	    String comLoginUrl = Configuration.get(ConfigurationKeys.SERVER_URL) + "/lamsCommunityLogin.do";
	    request.setAttribute("comLoginUrl", comLoginUrl);
	    request.setAttribute("tab", tab);
	    return mapping.findForward("community");
	}

	Registration reg = Configuration.getRegistration();
	if (reg != null) {
	    request.setAttribute("lamsCommunityEnabled", reg.isEnableLamsCommunityIntegration());
	}

	List orgDTOs = getUserManagementService().getActiveCourseIdsByUser(loggedInUser.getUserId(),
		request.isUserInRole(Role.SYSADMIN));
	request.setAttribute("orgDTOs", orgDTOs);
	return mapping.findForward("main");
    }

    private static void setHeaderLinks(HttpServletRequest request) {
	List<IndexLinkBean> headerLinks = new ArrayList<IndexLinkBean>();
	if (request.isUserInRole(Role.AUTHOR)) {
	    if (IndexAction.isPedagogicalPlannerAvailable()) {
		headerLinks.add(new IndexLinkBean("index.planner", "javascript:openPedagogicalPlanner()"));
	    }
	    headerLinks.add(new IndexLinkBean("index.author", "javascript:openAuthor(false)"));
	}
	headerLinks.add(new IndexLinkBean("index.myprofile", "index.do?tab=profile"));

	Registration reg = Configuration.getRegistration();
	if (reg != null) {
	    if (request.isUserInRole(Role.SYSADMIN) || request.isUserInRole(Role.GROUP_ADMIN)
		    || request.isUserInRole(Role.GROUP_MANAGER) || request.isUserInRole(Role.AUTHOR)
		    || request.isUserInRole(Role.MONITOR)) {
		headerLinks.add(new IndexLinkBean("index.community", "index.do?tab=community"));
	    }
	}

	String customTabText = Configuration.get(ConfigurationKeys.CUSTOM_TAB_TITLE);
	String customTabLink = Configuration.get(ConfigurationKeys.CUSTOM_TAB_LINK);
	if (customTabLink != null && customTabLink.trim().length() > 0) {
	    headerLinks.add(new IndexLinkBean(customTabText, "javascript:openCustom(\"" + customTabLink + "\")"));
	}

	request.setAttribute("headerLinks", headerLinks);
    }

    private void setAdminLinks(HttpServletRequest request) {
	List<IndexLinkBean> adminLinks = new ArrayList<IndexLinkBean>();
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

    private IUserManagementService getUserManagementService() {
	if (IndexAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    IndexAction.userManagementService = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return IndexAction.userManagementService;
    }

    private static boolean isPedagogicalPlannerAvailable() {
	String lamsEarPath = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR);
	String plannerPath = lamsEarPath + File.separator + IndexAction.PATH_LAMS_CENTRAL + File.separator
		+ IndexAction.PATH_PEDAGOGICAL_PLANNER;
	File plannerDir = new File(plannerPath);
	return plannerDir.isDirectory();
    }
}