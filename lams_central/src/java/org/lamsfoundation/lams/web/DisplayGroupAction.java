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

package org.lamsfoundation.lams.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.index.IndexLinkBean;
import org.lamsfoundation.lams.index.IndexOrgBean;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.LessonService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.IndexUtils;
import org.lamsfoundation.lams.util.WebUtil;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 *
 *
 *
 */
public class DisplayGroupAction extends Action {
    private static IUserManagementService service;
    private static LessonService lessonService;
    private static ILearningDesignService learningDesignService;
    private static ISecurityService securityService;

    private Integer stateId = OrganisationState.ACTIVE;

    @Override
    @SuppressWarnings({ "unchecked" })
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	stateId = WebUtil.readIntParam(request, "stateId", false);
	Integer orgId = WebUtil.readIntParam(request, "orgId", false);

	Organisation org = null;
	if (orgId != null) {
	    org = (Organisation) getService().findById(Organisation.class, orgId);
	}

	if (org != null) {
	    User user = getUser(request.getRemoteUser());
	    if (!getSecurityService().hasOrgRole(orgId, user.getUserId(),
		    new String[] { Role.GROUP_MANAGER, Role.LEARNER, Role.MONITOR, Role.AUTHOR }, "display group",
		    false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a part of the organisation");
		return null;
	    }

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

	    IndexOrgBean iob = createOrgBean(org, roles, request.getRemoteUser(), request.isUserInRole(Role.SYSADMIN));

	    request.setAttribute("orgBean", iob);
	    request.setAttribute("allowSorting", allowSorting);
	    iob.setAllowSorting(allowSorting && iob.getAllowSorting());
	    if (org.getEnableSingleActivityLessons()
		    && (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR))) {
		// if single activity lessons are enabled, put sorted list of tools
		request.setAttribute("tools",
			getLearningDesignService().getToolDTOs(false, false, request.getRemoteUser()));
	    }
	}

	return mapping.findForward("group");
    }

    private IndexOrgBean createOrgBean(Organisation org, List<Integer> roles, String username, boolean isSysAdmin)
	    throws SQLException, NamingException {
	IndexOrgBean orgBean = new IndexOrgBean(org.getOrganisationId(), org.getName(),
		org.getOrganisationType().getOrganisationTypeId());

	// First, populate header part
	List<IndexLinkBean> links = new ArrayList<IndexLinkBean>();
	List<IndexLinkBean> moreLinks = new ArrayList<IndexLinkBean>();
	if (isSysAdmin && stateId.equals(OrganisationState.ACTIVE)) {
	    if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
		moreLinks.add(new IndexLinkBean("index.classman",
			"javascript:openOrgManagement(" + org.getOrganisationId() + ")", "fa fa-fw fa-users", null));
	    }
	}

	if (org.getEnableGradebookForLearners() && roles.contains(Role.ROLE_LEARNER)) {
	    String link = "javascript:showGradebookLearnerDialog(" + org.getOrganisationId() + ")";
	    links.add(new IndexLinkBean("index.coursegradebook.learner", link, "fa fa-fw fa-list-ol", null));
	}

	if ((roles.contains(Role.ROLE_GROUP_ADMIN) || roles.contains(Role.ROLE_GROUP_MANAGER)
		|| roles.contains(Role.ROLE_MONITOR)) && stateId.equals(OrganisationState.ACTIVE)) {
	    if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
		if ((!isSysAdmin)
			&& (roles.contains(Role.ROLE_GROUP_ADMIN) || roles.contains(Role.ROLE_GROUP_MANAGER))) {
		    moreLinks.add(new IndexLinkBean("index.classman",
			    "javascript:openOrgManagement(" + org.getOrganisationId() + ")", "fa fa-fw fa-users",
			    null));
		}
		if ((roles.contains(Role.ROLE_GROUP_ADMIN) || roles.contains(Role.ROLE_GROUP_MANAGER)
			|| roles.contains(Role.ROLE_AUTHOR) || roles.contains(Role.ROLE_MONITOR))) {
		    moreLinks.add(new IndexLinkBean("index.orggroup",
			    "javascript:showOrgGroupDialog(" + org.getOrganisationId() + ")", "fa fa-fw fa-users",
			    null));
		}

		if (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR)) {
		    String name = org.getEnableSingleActivityLessons() ? "index.addlesson.single" : "index.addlesson";
		    links.add(new IndexLinkBean(name, "javascript:showAddLessonDialog(" + org.getOrganisationId() + ")",
			    "fa fa-fw fa-plus", null));
		}
		moreLinks.add(new IndexLinkBean("index.searchlesson",
			"javascript:showSearchLessonDialog(" + org.getOrganisationId() + ")", "fa fa-fw fa-search",
			"index.searchlesson.tooltip"));

		// Adding course notifications links if enabled
		if (org.getEnableCourseNotifications()
			&& (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR))) {
		    moreLinks.add(new IndexLinkBean("index.emailnotifications",
			    "javascript:showNotificationsDialog(" + org.getOrganisationId() + ",null)",
			    "fa fa-fw fa-bullhorn", "index.emailnotifications.tooltip"));
		}

		// Adding gradebook course monitor links if enabled
		if (org.getEnableGradebookForMonitors()
			&& (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_GROUP_ADMIN))) {
		    String link = "javascript:showGradebookCourseDialog(" + org.getOrganisationId() + ")";
		    moreLinks.add(new IndexLinkBean("index.coursegradebook", link, "course-gradebook-button",
			    "index.coursegradebook.tooltip"));
		}

	    } else {// CLASS_TYPE
		if (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR)) {
		    String name = org.getParentOrganisation().getEnableSingleActivityLessons()
			    ? "index.addlesson.single" : "index.addlesson";
		    links.add(new IndexLinkBean(name, "javascript:showAddLessonDialog(" + org.getOrganisationId() + ")",
			    "fa fa-fw fa-plus", null));
		}

		// Adding gradebook course monitor links if enabled
		if (org.getParentOrganisation().getEnableGradebookForMonitors()
			&& (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_GROUP_ADMIN))) {
		    String link = "javascript:showGradebookCourseDialog(" + org.getOrganisationId() + ")";
		    moreLinks.add(new IndexLinkBean("index.coursegradebook.subgroup", link, "fa fa-fw fa-list-ol", null));
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

	// now populate group contents
	orgBean = populateContentsOrgBean(orgBean, org, roles, username, isSysAdmin);

	return orgBean;
    }

    @SuppressWarnings("unchecked")
    private IndexOrgBean populateContentsOrgBean(IndexOrgBean orgBean, Organisation org, List<Integer> roles,
	    String username, boolean isSysAdmin) throws SQLException, NamingException {
	// set lesson beans
	Map<Long, IndexLessonBean> map = populateLessonBeans(getUser(username).getUserId(), org.getOrganisationId(),
		roles);
	List<IndexLessonBean> lessonBeans = IndexUtils.sortLessonBeans(org.getOrderedLessonIds(), map);

	orgBean.setLessons(lessonBeans);
	if (!lessonBeans.isEmpty()) {
	    orgBean.setAllowSorting(true);
	}

	// create subgroup beans
	if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
	    Set<Organisation> children = org.getChildOrganisations();

	    List<IndexOrgBean> childOrgBeans = new ArrayList<IndexOrgBean>();
	    for (Organisation organisation : children) {
		if (organisation.getOrganisationState().getOrganisationStateId().equals(stateId)) {
		    List<Integer> classRoles = new ArrayList<Integer>();
		    List<UserOrganisationRole> userOrganisationRoles = getService()
			    .getUserOrganisationRoles(organisation.getOrganisationId(), username);
		    // don't list the subgroup if user is not a member, and not a group admin/manager
		    if (((userOrganisationRoles == null) || userOrganisationRoles.isEmpty()) && !isSysAdmin
			    && !roles.contains(Role.ROLE_GROUP_ADMIN) && !roles.contains(Role.ROLE_GROUP_MANAGER)) {
			continue;
		    }

		    for (UserOrganisationRole userOrganisationRole : userOrganisationRoles) {
			classRoles.add(userOrganisationRole.getRole().getRoleId());
		    }
		    if (roles.contains(Role.ROLE_GROUP_MANAGER)) {
			classRoles.add(Role.ROLE_GROUP_MANAGER);
		    }
		    IndexOrgBean childOrgBean = createOrgBean(organisation, classRoles, username, isSysAdmin);
		    childOrgBeans.add(childOrgBean);
		    if ((childOrgBean.getLessons() != null) && !childOrgBean.getLessons().isEmpty()) {
			orgBean.setAllowSorting(true);
		    }
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
	    Entry<Long, IndexLessonBean> entry = lessonIter.next();
	    if (entry.getValue().isDependent()
		    && !DisplayGroupAction.lessonService.checkLessonReleaseConditions(entry.getKey(), userId)) {
		lessonIter.remove();
	    }
	}

	for (IndexLessonBean bean : map.values()) {
	    LinkedList<IndexLinkBean> lessonLinks = new LinkedList<IndexLinkBean>();
	    String url = null;
	    Integer lessonStateId = bean.getState();
	    if (stateId.equals(OrganisationState.ACTIVE) && roles.contains(Role.ROLE_LEARNER)
		    && (lessonStateId.equals(Lesson.STARTED_STATE) || lessonStateId.equals(Lesson.FINISHED_STATE))) {
		url = "javascript:openLearner(" + bean.getId() + ")";
	    }

	    if ((lessonLinks.size() > 0) || (url != null)) {
		bean.setUrl(url);
		bean.setLinks(lessonLinks);
	    }
	}

	// getting the organisation
	Organisation org = (Organisation) DisplayGroupAction.service.findById(Organisation.class, orgId);

	// Getting the parent organisation if applicable
	Organisation parent = org.getParentOrganisation();

	// iterate through user's lessons where they are staff (or simply through all lessons in case of Group_Manager),
	// and add staff links to the beans in the map.
	Integer userRole = (roles.contains(Role.ROLE_GROUP_MANAGER)) ? Role.ROLE_GROUP_MANAGER : Role.ROLE_MONITOR;
	Map<Long, IndexLessonBean> staffMap = getLessonService().getLessonsByOrgAndUserWithCompletedFlag(userId, orgId,
		userRole);
	boolean isGroupManagerOrMonitor = roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR);
	for (IndexLessonBean bean : staffMap.values()) {
	    if (map.containsKey(bean.getId())) {
		bean = map.get(bean.getId());
	    }
	    LinkedList<IndexLinkBean> lessonLinks = (LinkedList<IndexLinkBean>) bean.getLinks();
	    if (lessonLinks == null) {
		lessonLinks = new LinkedList<IndexLinkBean>();
	    }

	    if ((isGroupManagerOrMonitor && stateId.equals(OrganisationState.ACTIVE))
		    || (stateId.equals(OrganisationState.ARCHIVED) && roles.contains(Role.ROLE_GROUP_MANAGER))) {
		lessonLinks.addFirst(new IndexLinkBean("index.monitor",
			"javascript:showMonitorLessonDialog(" + bean.getId() + ")", "fa fa-fw fa-heartbeat", null));
	    }

	    // Adding lesson notifications links if enabled
	    if (isGroupManagerOrMonitor && bean.isEnableLessonNotifications()) {
		lessonLinks.addFirst(new IndexLinkBean("index.emailnotifications",
			"javascript:showNotificationsDialog(null," + bean.getId() + ")", "fa fa-fw fa-bullhorn",
			"index.emailnotifications.tooltip"));
	    }

	    // Adding gradebook course monitor links if enabled
	    if (isGroupManagerOrMonitor && (org.getEnableGradebookForMonitors()
		    || ((parent != null) && parent.getEnableGradebookForMonitors()))) {
		String link = "javascript:showGradebookLessonDialog(" + bean.getId() + ")";
		lessonLinks
			.addFirst(new IndexLinkBean("index.coursegradebookmonitor", link, "fa fa-fw fa-check-square-o", null));
	    }

	    // Add lesson conditions
	    if (isGroupManagerOrMonitor) {
		String conditionsLink = "javascript:showConditionsDialog(" + bean.getId() + ")";
		lessonLinks.addFirst(new IndexLinkBean("index.conditions", conditionsLink, "fa fa-fw fa-code-fork",
			"index.conditions.tooltip"));
	    }

	    // Add delete lesson option
	    if (isGroupManagerOrMonitor) {
		String removeLessonLink = "javascript:removeLesson(" + bean.getId() + ")";
		lessonLinks.addFirst(new IndexLinkBean("index.remove.lesson", removeLessonLink,
			"fa fa-fw fa-trash-o", "index.remove.lesson.tooltip"));
	    }

	    if (lessonLinks.size() > 0) {
		bean.setLinks(lessonLinks);
	    }
	    map.put(bean.getId(), bean);
	}

	return map;
    }

    private User getUser(String login) {
	return (User) getService().findByProperty(User.class, "login", login).get(0);
    }

    private IUserManagementService getService() {
	if (DisplayGroupAction.service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    DisplayGroupAction.service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return DisplayGroupAction.service;
    }

    private LessonService getLessonService() {
	if (DisplayGroupAction.lessonService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    DisplayGroupAction.lessonService = (LessonService) ctx.getBean("lessonService");
	}
	return DisplayGroupAction.lessonService;
    }

    private ILearningDesignService getLearningDesignService() {
	if (DisplayGroupAction.learningDesignService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    DisplayGroupAction.learningDesignService = (ILearningDesignService) ctx.getBean("learningDesignService");
	}
	return DisplayGroupAction.learningDesignService;
    }

    private ISecurityService getSecurityService() {
	if (DisplayGroupAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    DisplayGroupAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return DisplayGroupAction.securityService;
    }
}
