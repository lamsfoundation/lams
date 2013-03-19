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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.index.IndexLinkBean;
import org.lamsfoundation.lams.index.IndexOrgBean;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.LessonService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.IndexUtils;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 * 
 * @struts.action path="/displayGroup" validate="false"
 * @struts.action-forward name="groupHeader" path="/groupHeader.jsp"
 * @struts.action-forward name="groupContents" path="/groupContents.jsp"
 * @struts.action-forward name="group" path="/group.jsp"
 */
public class DisplayGroupAction extends Action {

    private static Logger log = Logger.getLogger(DisplayGroupAction.class);
    private static IUserManagementService service;
    private static LessonService lessonService;
    private Integer stateId = OrganisationState.ACTIVE;

    @SuppressWarnings( { "unchecked" })
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String display = WebUtil.readStrParam(request, "display", false);
	stateId = WebUtil.readIntParam(request, "stateId", false);
	Integer orgId = WebUtil.readIntParam(request, "orgId", false);

	Organisation org = null;
	if (orgId != null) {
	    org = (Organisation) getService().findById(Organisation.class, orgId);
	}

	String forwardPath = "group";
	if (org != null) {
	    boolean allowSorting = false;
	    List<Integer> roles = new ArrayList<Integer>();
	    List<UserOrganisationRole> userOrganisationRoles = getService().getUserOrganisationRoles(orgId,
		    request.getRemoteUser());
	    for (UserOrganisationRole userOrganisationRole : userOrganisationRoles) {
		Integer roleId = userOrganisationRole.getRole().getRoleId();
		roles.add(roleId);
		if (roleId.equals(Role.ROLE_GROUP_MANAGER) || roleId.equals(Role.ROLE_MONITOR)) {
		    allowSorting = true;
		}
	    }

	    IndexOrgBean iob;
	    if (StringUtils.equals(display, "contents")) {
		iob = new IndexOrgBean(org.getOrganisationId(), org.getName(), org.getOrganisationType()
			.getOrganisationTypeId());
		iob = populateContentsOrgBean(iob, org, roles, request.getRemoteUser(), request
			.isUserInRole(Role.SYSADMIN));
		forwardPath = "groupContents";
	    } else if (StringUtils.equals(display, "header")) {
		iob = createHeaderOrgBean(org, roles, request.getRemoteUser(), request.isUserInRole(Role.SYSADMIN),
			false);
		forwardPath = "groupHeader";
	    } else {
		iob = createHeaderOrgBean(org, roles, request.getRemoteUser(), request.isUserInRole(Role.SYSADMIN),
			true);
	    }

	    request.setAttribute("orgBean", iob);
	    request.setAttribute("allowSorting", allowSorting);
	}

	return mapping.findForward(forwardPath);
    }

    @SuppressWarnings( { "unchecked" })
    private IndexOrgBean createHeaderOrgBean(Organisation org, List<Integer> roles, String username,
	    boolean isSysAdmin, boolean includeContents) throws SQLException, NamingException {
	IndexOrgBean orgBean = new IndexOrgBean(org.getOrganisationId(), org.getName(), org.getOrganisationType()
		.getOrganisationTypeId());

	// set org links
	List<IndexLinkBean> links = new ArrayList<IndexLinkBean>();
	List<IndexLinkBean> moreLinks = new ArrayList<IndexLinkBean>();
	if (isSysAdmin && stateId.equals(OrganisationState.ACTIVE)) {
	    if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
		moreLinks.add(new IndexLinkBean("index.classman", "javascript:openOrgManagement("
			+ org.getOrganisationId() + ")", "manage-group-button", null, null));
	    }
	}

	if (org.getEnableGradebookForLearners() && contains(roles, Role.ROLE_LEARNER)) {
		try {
			// for some reason the name needs to be encoded twice so it's encoded on JSP page as well
			String encodedOrgName = URLEncoder.encode(URLEncoder.encode(org.getName(), "UTF8"), "UTF8");
			String link = "javascript:openGradebookLearnerPopup(" + "'" + encodedOrgName + "','"
			    + Configuration.get(ConfigurationKeys.SERVER_URL)
			    + "/gradebook/gradebookLearning.do?dispatch=courseLearner&organisationID="
			    + org.getOrganisationId() + "'," + "750,400,0,0);";
	
		    links.add(new IndexLinkBean("index.coursegradebook.learner", link, "my-grades-button", null, null));
		} catch (UnsupportedEncodingException e) {
			log.error("Error while encoding course name, skipping gradebook course monitor link", e);
		}
	}
	
	if ((contains(roles, Role.ROLE_GROUP_ADMIN) || contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles,
		Role.ROLE_MONITOR))
		&& stateId.equals(OrganisationState.ACTIVE)) {
	    if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
		if ((!isSysAdmin)
			&& (contains(roles, Role.ROLE_GROUP_ADMIN) || contains(roles, Role.ROLE_GROUP_MANAGER))) {
		    moreLinks.add(new IndexLinkBean("index.classman", "javascript:openOrgManagement("
			    + org.getOrganisationId() + ")", "manage-group-button", null, null));
		}
		if (contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles, Role.ROLE_MONITOR))
		    links.add(new IndexLinkBean("index.addlesson", "javascript:showAddLessonDialog("
			    + org.getOrganisationId() + ")", "add-lesson-button", null, null));
		moreLinks.add(new IndexLinkBean("index.searchlesson", Configuration.get(ConfigurationKeys.SERVER_URL)
			+ "/findUserLessons.do?dispatch=getResults&courseID=" + org.getOrganisationId()
			+ "&KeepThis=true&TB_iframe=true&height=400&width=600", "search-lesson thickbox"
			+ org.getOrganisationId(), null, "index.searchlesson.tooltip"));
		
		// Adding course notifications links if enabled
		if (org.getEnableCourseNotifications() && (contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles, Role.ROLE_MONITOR))) {
		    moreLinks.add(new IndexLinkBean("index.emailnotifications", Configuration.get(ConfigurationKeys.SERVER_URL)
			    + "/monitoring/emailNotifications.do?method=getCourseView&organisationID="
			    + org.getOrganisationId() + "&KeepThis=true&TB_iframe=true&height=500&width=800",
			    "course-notifications thickbox" + org.getOrganisationId(), null,
			    "index.emailnotifications.tooltip"));
		}

		// Adding gradebook course monitor links if enabled
		if (org.getEnableGradebookForMonitors() && (contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles, Role.ROLE_MONITOR))) {
		    try {
			// for some reason the name needs to be encoded twice so it's encoded on JSP page as well
			String encodedOrgName = URLEncoder.encode(URLEncoder.encode(org.getName(), "UTF8"), "UTF8");
			String link = "javascript:openGradebookCourseMonitorPopup(" + "'" + encodedOrgName + "','"
				+ Configuration.get(ConfigurationKeys.SERVER_URL)
				+ "/gradebook/gradebookMonitoring.do?dispatch=courseMonitor&organisationID="
				+ org.getOrganisationId() + "'," + "850,400,0,0);";
			moreLinks.add(new IndexLinkBean("index.coursegradebook", link, "course-gradebook-button", null,
				"index.coursegradebook.tooltip"));
		    } catch (UnsupportedEncodingException e) {
			log.error("Error while encoding course name, skipping gradebook course monitor link", e);
		    }
		}

	    } else {//CLASS_TYPE
		if (contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles, Role.ROLE_MONITOR))
		    links.add(new IndexLinkBean("index.addlesson", "javascript:showAddLessonDialog("
			    + org.getOrganisationId() + ")", "add-lesson-button", null, null));

		// Adding gradebook course monitor links if enabled
		if (org.getParentOrganisation().getEnableGradebookForMonitors()
			&& (contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles, Role.ROLE_MONITOR))) {
		    try {
			// for some reason the name needs to be encoded twice so it's encoded on JSP page as well
			String encodedOrgName = URLEncoder.encode(URLEncoder.encode(org.getName(), "UTF8"), "UTF8");
			String link = "javascript:openGradebookCourseMonitorPopup(" + "'" + encodedOrgName + "','"
				+ Configuration.get(ConfigurationKeys.SERVER_URL)
				+ "/gradebook/gradebookMonitoring.do?dispatch=courseMonitor&organisationID="
				+ org.getOrganisationId() + "'," + "850,400,0,0);";
			moreLinks.add(new IndexLinkBean("index.coursegradebook.subgroup", link, "mycourses-mark-img",
				null, null));
		    } catch (UnsupportedEncodingException e) {
			log.error("Error while encoding course name, skipping gradebook course monitor link", e);
		    }
		}
	    }
	}
	orgBean.setLinks(links);
	orgBean.setMoreLinks(moreLinks);

	// set archived date if archived
	if (stateId.equals(OrganisationState.ARCHIVED)
		&& org.getOrganisationState().getOrganisationStateId().equals(OrganisationState.ARCHIVED)) {
	    orgBean.setArchivedDate(org.getArchivedDate());
	}

	if (includeContents) {
	    orgBean = populateContentsOrgBean(orgBean, org, roles, username, isSysAdmin);
	}

	return orgBean;
    }

    private IndexOrgBean populateContentsOrgBean(IndexOrgBean orgBean, Organisation org, List<Integer> roles,
	    String username, boolean isSysAdmin) throws SQLException, NamingException {
	User user = (User) getService().findByProperty(User.class, "login", username).get(0);

	//	set lesson beans
	List<IndexLessonBean> lessonBeans = null;
	try {
	    Map<Long, IndexLessonBean> map = populateLessonBeans(user.getUserId(), org.getOrganisationId(), roles);
	    lessonBeans = IndexUtils.sortLessonBeans(org.getOrderedLessonIds(), map);
	} catch (Exception e) {
	    log.error("Failed retrieving user's lessons from database: " + e, e);
	}
	orgBean.setLessons(lessonBeans);

	// create subgroup beans
	if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
	    Set<Organisation> children = org.getChildOrganisations();

	    List<IndexOrgBean> childOrgBeans = new ArrayList<IndexOrgBean>();
	    for (Organisation organisation : children) {
		if (organisation.getOrganisationState().getOrganisationStateId().equals(stateId)) {
		    List<Integer> classRoles = new ArrayList<Integer>();
		    List<UserOrganisationRole> userOrganisationRoles = getService().getUserOrganisationRoles(
			    organisation.getOrganisationId(), username);
		    // don't list the subgroup if user is not a member, and not a group admin/manager
		    if (userOrganisationRoles == null || userOrganisationRoles.isEmpty()) {
			if (!contains(roles, Role.ROLE_GROUP_ADMIN) && !contains(roles, Role.ROLE_GROUP_MANAGER)
				&& !isSysAdmin) {
			    continue;
			}
		    }
		    for (UserOrganisationRole userOrganisationRole : userOrganisationRoles) {
			classRoles.add(userOrganisationRole.getRole().getRoleId());
		    }
		    if (contains(roles, Role.ROLE_GROUP_MANAGER))
			classRoles.add(Role.ROLE_GROUP_MANAGER);
		    childOrgBeans.add(createHeaderOrgBean(organisation, classRoles, username, isSysAdmin, true));
		}
	    }
	    Collections.sort(childOrgBeans);
	    orgBean.setChildIndexOrgBeans(childOrgBeans);
	}
	return orgBean;
    }

    // create lesson beans
    private Map<Long, IndexLessonBean> populateLessonBeans(Integer userId, Integer orgId, List<Integer> roles)
	    throws SQLException, NamingException {

	// iterate through user's lessons where they are learner
	Map<Long, IndexLessonBean> map = getLessonService().getLessonsByOrgAndUserWithCompletedFlag(userId, orgId,
		Role.ROLE_LEARNER);
	// remove lessons which do not have preceding lessons completed
	Iterator<Entry<Long, IndexLessonBean>> lessonIter = map.entrySet().iterator();
	while (lessonIter.hasNext()) {
	    Entry<Long,IndexLessonBean> entry = lessonIter.next();
	    if (entry.getValue().isDependent() && !lessonService.checkLessonReleaseConditions(entry.getKey(), userId)) {
		lessonIter.remove();
	    }
	}
	
	for (IndexLessonBean bean : map.values()) {
	    List<IndexLinkBean> lessonLinks = new ArrayList<IndexLinkBean>();
	    String url = null;
	    Integer lessonStateId = bean.getState();
	    if (stateId.equals(OrganisationState.ACTIVE)) {
		if (contains(roles, Role.ROLE_LEARNER)) {
		    if (lessonStateId.equals(Lesson.STARTED_STATE) || lessonStateId.equals(Lesson.FINISHED_STATE)) {
			url = "javascript:openLearner(" + bean.getId() + ")";
		    }
		}
	    } else if (stateId.equals(OrganisationState.ARCHIVED)) {
		if (contains(roles, Role.ROLE_LEARNER)) {
		    if (lessonStateId.equals(Lesson.STARTED_STATE) || lessonStateId.equals(Lesson.FINISHED_STATE)) {
			lessonLinks.add(new IndexLinkBean("label.export.portfolio", "javascript:openExportPortfolio("
				+ bean.getId() + ")"));
		    }
		}
	    }
	    if (lessonLinks.size() > 0 || url != null) {
		bean.setUrl(url);
		bean.setLinks(lessonLinks);
	    }
	}

	// getting the organisation
	Organisation org = (Organisation) service.findById(Organisation.class, orgId);

	// Getting the parent organisation if applicable
	Organisation parent = org.getParentOrganisation();

	// iterate through user's lessons where they are staff (or simply through all lessons in case of Group_Manager),
	// and add staff links to the beans in the map.
	Integer userRole = (contains(roles, Role.ROLE_GROUP_MANAGER)) ? Role.ROLE_GROUP_MANAGER : Role.ROLE_MONITOR;
	Map<Long, IndexLessonBean> staffMap = getLessonService().getLessonsByOrgAndUserWithCompletedFlag(userId, orgId,
		userRole);
	boolean isGroupManagerOrMonitor = contains(roles, Role.ROLE_GROUP_MANAGER) || contains(roles, Role.ROLE_MONITOR);
	for (IndexLessonBean bean : staffMap.values()) {
	    if (map.containsKey(bean.getId())) {
		bean = map.get(bean.getId());
	    }
	    List<IndexLinkBean> lessonLinks = bean.getLinks();
	    if (lessonLinks == null) {
		lessonLinks = new ArrayList<IndexLinkBean>();
	    }
	    
	    if ((isGroupManagerOrMonitor && stateId.equals(OrganisationState.ACTIVE))
		    || (stateId.equals(OrganisationState.ARCHIVED) && contains(roles, Role.ROLE_GROUP_MANAGER))) {
		lessonLinks.add(new IndexLinkBean("index.monitor",
			"javascript:openMonitorLesson(" + bean.getId() + ")", null, "mycourses-monitor-img", null));
	    }

	    // Adding lesson notifications links if enabled
	    if (isGroupManagerOrMonitor && bean.isEnableLessonNotifications()) {
		String emailnotificationsUrl = Configuration.get(ConfigurationKeys.SERVER_URL)
			+ "/monitoring/emailNotifications.do?method=getLessonView&lessonID=" + bean.getId()
			+ "&KeepThis=true&TB_iframe=true&height=560&width=800";
		lessonLinks.add(new IndexLinkBean("index.emailnotifications", emailnotificationsUrl,
			"thickbox" + orgId, "mycourses-notifications-img", "index.emailnotifications.tooltip"));
	    }

	    // Adding gradebook course monitor links if enabled
	    if (isGroupManagerOrMonitor
		    && (org.getEnableGradebookForMonitors() || (parent != null && parent
			    .getEnableGradebookForMonitors()))) {
		try {
		    String encodedOrgName = URLEncoder.encode(URLEncoder.encode(org.getName(), "UTF8"), "UTF8");
		    String link = "javascript:openGradebookLessonMonitorPopup(" + "'" + encodedOrgName + "','"
			    + Configuration.get(ConfigurationKeys.SERVER_URL)
			    + "/gradebook/gradebookMonitoring.do?lessonID=" + bean.getId() + "'," + "850,700,0,0);";
		    lessonLinks.add(new IndexLinkBean("index.coursegradebookmonitor", link, null, "mycourses-mark-img",
			    null));
		} catch (UnsupportedEncodingException e) {
		    log.error("Error while encoding course name, skipping gradebook lesson monitor link", e);
		}
	    }

	    // Add lesson conditions thickbox
	    if (isGroupManagerOrMonitor) {
		String conditionsLink = Configuration.get(ConfigurationKeys.SERVER_URL)
			+ "/lessonConditions.do?method=getIndexLessonConditions&" + CentralConstants.PARAM_LESSON_ID
			+ "=" + bean.getId() + "&KeepThis=true&TB_iframe=true&height=480&width=610";
		lessonLinks.add(new IndexLinkBean("index.conditions", conditionsLink, "thickbox" + orgId,
			"mycourses-conditions-img", "index.conditions.tooltip"));
	    }
	    
	    // Add delete lesson option
	    if (isGroupManagerOrMonitor) {
		String removeLessonLink = "javascript:removeLesson(" + bean.getId() + ")";
		lessonLinks.add(new IndexLinkBean("index.remove.lesson", removeLessonLink, null,
			"mycourses-removelesson-img", "index.remove.lesson.tooltip"));
	    }
	    
	    if ((isGroupManagerOrMonitor && stateId.equals(OrganisationState.ACTIVE))
		    || (stateId.equals(OrganisationState.ARCHIVED) && contains(roles, Role.ROLE_GROUP_MANAGER))) {
		lessonLinks.add(new IndexLinkBean("index.monitor",
			"javascript:showMonitorLessonDialog(" + bean.getId() + ")", null, "mycourses-monitor-img", null));
	    }

	    if (lessonLinks.size() > 0) {
		bean.setLinks(lessonLinks);
	    }
	    map.put(bean.getId(), bean);
	}

	return map;
    }

    private boolean contains(List<Integer> roles, Integer roleId) {
	for (int i = 0; i < roles.size(); i++) {
	    if (roleId.equals(roles.get(i)))
		return true;
	}
	return false;
    }

    private IUserManagementService getService() {
	if (service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return service;
    }

    private LessonService getLessonService() {
	if (lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    lessonService = (LessonService) ctx.getBean("lessonService");
	}
	return lessonService;
    }
}
