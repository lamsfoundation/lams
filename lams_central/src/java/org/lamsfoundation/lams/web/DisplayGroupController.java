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

import org.lamsfoundation.lams.index.IndexLessonBean;
import org.lamsfoundation.lams.index.IndexLinkBean;
import org.lamsfoundation.lams.index.IndexOrgBean;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.dto.LearnerProgressDTO;
import org.lamsfoundation.lams.lesson.service.LessonService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisationCollapsed;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.IndexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author jliew
 */
@Controller
@RequestMapping("/displayGroup")
public class DisplayGroupController {
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ILearningDesignService learningDesignService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private ILearnerService learnerService;
    @Autowired
    private IMonitoringService monitoringService;

    @RequestMapping("")
    public String execute(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer orgId)
	    throws Exception {
	Organisation org = null;
	if (orgId != null) {
	    org = (Organisation) userManagementService.findById(Organisation.class, orgId);
	}

	if (org != null) {
	    User user = getUser(request.getRemoteUser());
	    if (!securityService.hasOrgRole(orgId, user.getUserId(),
		    new String[] { Role.GROUP_MANAGER, Role.LEARNER, Role.MONITOR, Role.AUTHOR }, "display group",
		    false)) {
		response.sendError(HttpServletResponse.SC_FORBIDDEN, "The user is not a part of the organisation");
		return null;
	    }

	    List<Integer> roles = new ArrayList<>();
	    List<UserOrganisationRole> userOrganisationRoles = userManagementService.getUserOrganisationRoles(orgId,
		    request.getRemoteUser());
	    for (UserOrganisationRole userOrganisationRole : userOrganisationRoles) {
		Integer roleId = userOrganisationRole.getRole().getRoleId();
		roles.add(roleId);
	    }

	    IndexOrgBean iob = createOrgBean(org, roles, request.getRemoteUser(), request.isUserInRole(Role.SYSADMIN));

	    request.setAttribute("orgBean", iob);
	    if (org.getEnableSingleActivityLessons()
		    && (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR))) {
		// if single activity lessons are enabled, put sorted list of tools
		request.setAttribute("tools", learningDesignService.getToolDTOs(false, false, request.getRemoteUser()));
	    }

	    //star lesson feature is available to only organisation's monitors and authors
	    boolean isFavouriteLessonEnabled = request.isUserInRole(Role.AUTHOR) || request.isUserInRole(Role.MONITOR)
		    || request.isUserInRole(Role.GROUP_MANAGER);
	    request.setAttribute("isFavouriteLessonEnabled", isFavouriteLessonEnabled);
	    
	    //lesson reordering is available to only organisation's monitors and authors
	    boolean isUserMonitor = roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR);
	    request.setAttribute("isRowReorderingEnabled", isUserMonitor);
	    request.setAttribute("isUserMonitor", isUserMonitor);

	    //set whether organisation is favorite
	    boolean isFavorite = userManagementService.isOrganisationFavorite(orgId, user.getUserId());
	    iob.setFavorite(isFavorite);
	}

	return "group";
    }

    private IndexOrgBean createOrgBean(Organisation org, List<Integer> roles, String username, boolean isSysAdmin)
	    throws SQLException, NamingException {
	Integer organisationId = org.getOrganisationId();
	IndexOrgBean orgBean = new IndexOrgBean(organisationId, org.getName(),
		org.getOrganisationType().getOrganisationTypeId());

	// populate group contents
	orgBean = populateContentsOrgBean(orgBean, org, roles, username, isSysAdmin);

	// First, populate header part
	List<IndexLinkBean> links = new ArrayList<>();
	List<IndexLinkBean> moreLinks = new ArrayList<>();
	if (isSysAdmin) {
	    if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
		moreLinks.add(new IndexLinkBean("index.classman",
			"javascript:openOrgManagement(" + organisationId + ")", "fa fa-fw fa-users", null));
	    }
	}

	if (org.getEnableGradebookForLearners() && roles.contains(Role.ROLE_LEARNER)) {
	    String link = "javascript:showGradebookLearnerDialog(" + organisationId + ")";
	    links.add(new IndexLinkBean("index.coursegradebook.learner", link, "fa fa-fw fa-list-ol", null));
	}

	if (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR)) {
	    if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
		if ((!isSysAdmin) && (roles.contains(Role.ROLE_GROUP_MANAGER))) {
		    moreLinks.add(new IndexLinkBean("index.classman",
			    "javascript:openOrgManagement(" + organisationId + ")", "fa fa-fw fa-ellipsis-v", null));
		}
		if ((roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_AUTHOR)
			|| roles.contains(Role.ROLE_MONITOR))) {
		    moreLinks.add(new IndexLinkBean("index.orggroup",
			    "javascript:showOrgGroupingDialog(" + organisationId + ")", "fa fa-fw fa-users", null));
		}

		if (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR)) {
		    String name = org.getEnableSingleActivityLessons() ? "index.addlesson.single" : "index.addlesson";
		    links.add(new IndexLinkBean(name, "javascript:showAddLessonDialog(" + organisationId + ")",
			    "fa fa-fw fa-plus", null));
		}
		moreLinks.add(new IndexLinkBean("index.searchlesson",
			"javascript:showSearchLessonDialog(" + organisationId + ")", "fa fa-fw fa-search",
			"index.searchlesson.tooltip"));

		// Adding course notifications links if enabled
		if (org.getEnableCourseNotifications()
			&& (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR))) {
		    moreLinks.add(new IndexLinkBean("index.emailnotifications",
			    "javascript:showNotificationsDialog(" + organisationId + ",null)", "fa fa-fw fa-bullhorn",
			    "index.emailnotifications.tooltip"));
		}

		// Adding lesson sorting link
		if (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR)) {

		    // make sure the group or any of its subgroups has at least one lesson
		    boolean hasLesson = !orgBean.getLessons().isEmpty();
		    for (IndexOrgBean childOrgBean : orgBean.getChildIndexOrgBeans()) {
			hasLesson |= (childOrgBean.getLessons() != null) && !childOrgBean.getLessons().isEmpty();
		    }

		    String link = "javascript:showGradebookCourseDialog(" + organisationId + ")";
		    moreLinks.add(new IndexLinkBean("index.coursegradebook", link, "fa fa-fw fa-list-ol",
			    "index.coursegradebook.tooltip"));
		}
	    } else {// CLASS_TYPE
		if (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR)) {
		    String name = org.getParentOrganisation().getEnableSingleActivityLessons()
			    ? "index.addlesson.single"
			    : "index.addlesson";
		    links.add(new IndexLinkBean(name, "javascript:showAddLessonDialog(" + organisationId + ")",
			    "fa fa-fw fa-plus", null));

		    String link = "javascript:showGradebookCourseDialog(" + organisationId + ")";
		    moreLinks.add(
			    new IndexLinkBean("index.coursegradebook.subgroup", link, "fa fa-fw fa-list-ol", null));
		}
	    }
	}

	if (Configuration.getAsBoolean(ConfigurationKeys.ALLOW_KUMALIVE) && org.getEnableKumalive()
		&& (roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR)
			|| roles.contains(Role.ROLE_LEARNER))) {
	    boolean isKumaliveDisabledForOrganisation = learnerService
		    .isKumaliveDisabledForOrganisation(organisationId);
	    boolean isMonitor = roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR);
	    boolean disabled = !isMonitor && isKumaliveDisabledForOrganisation;
	    links.add(new IndexLinkBean(isMonitor ? "index.kumalive.teacher" : "index.kumalive",
		    "javascript:openKumalive(" + organisationId + ")",
		    "fa fa-fw fa-bolt" + (disabled ? " disabled" : ""), "index.kumalive.tooltip"));
	}

	orgBean.setLinks(links);
	orgBean.setMoreLinks(moreLinks);

	return orgBean;
    }

    @SuppressWarnings("unchecked")
    private IndexOrgBean populateContentsOrgBean(IndexOrgBean orgBean, Organisation org, List<Integer> roles,
	    String username, boolean isSysAdmin) throws SQLException, NamingException {
	Integer userId = getUser(username).getUserId();
	
	// set lesson beans
	Map<Long, IndexLessonBean> map = populateLessonBeans(userId, org.getOrganisationId(),
		roles);
	List<IndexLessonBean> lessonBeans = IndexUtils.sortLessonBeans(org.getOrderedLessonIds(), map);
	orgBean.setLessons(lessonBeans);

	// create subgroup beans
	if (orgBean.getType().equals(OrganisationType.COURSE_TYPE)) {
	    Set<Organisation> childOrganisations = org.getChildOrganisations();
	    boolean isCollapsingSubcoursesEnabled = Configuration
		    .getAsBoolean(ConfigurationKeys.ENABLE_COLLAPSING_SUBCOURSES);
	    List<UserOrganisationCollapsed> userOrganisationsCollapsed = isCollapsingSubcoursesEnabled
		    ? userManagementService.getChildOrganisationsCollapsedByUser(org.getOrganisationId(), userId)
		    : null;

	    List<IndexOrgBean> childOrgBeans = new ArrayList<>();
	    for (Organisation childOrganisation : childOrganisations) {
		if (OrganisationState.ACTIVE.equals(childOrganisation.getOrganisationState().getOrganisationStateId())) {
		    List<Integer> classRoles = new ArrayList<>();
		    List<UserOrganisationRole> userOrganisationRoles = userManagementService
			    .getUserOrganisationRoles(childOrganisation.getOrganisationId(), username);
		    // don't list the subgroup if user is not a member, and not a group admin/manager
		    if (((userOrganisationRoles == null) || userOrganisationRoles.isEmpty()) && !isSysAdmin
			    && !roles.contains(Role.ROLE_GROUP_MANAGER)) {
			continue;
		    }

		    for (UserOrganisationRole userOrganisationRole : userOrganisationRoles) {
			classRoles.add(userOrganisationRole.getRole().getRoleId());
		    }
		    if (roles.contains(Role.ROLE_GROUP_MANAGER)) {
			classRoles.add(Role.ROLE_GROUP_MANAGER);
		    }
		    IndexOrgBean childOrgBean = createOrgBean(childOrganisation, classRoles, username, isSysAdmin);
		    
		    //check whether organisation was collapsed by the user
		    if (isCollapsingSubcoursesEnabled) {
			for (UserOrganisationCollapsed userOrganisationCollapsed : userOrganisationsCollapsed) {
			    if (userOrganisationCollapsed.getOrganisation().getOrganisationId()
				    .equals(childOrganisation.getOrganisationId())) {
				childOrgBean.setCollapsed(userOrganisationCollapsed.getCollapsed());
				break;
			    }
			}
		    }
		    
		    childOrgBeans.add(childOrgBean);
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
	Map<Long, IndexLessonBean> map = lessonService.getLessonsByOrgAndUserWithCompletedFlag(userId, orgId,
		Role.ROLE_LEARNER);
	// remove lessons which do not have preceding lessons completed
	Iterator<Entry<Long, IndexLessonBean>> lessonIter = map.entrySet().iterator();
	while (lessonIter.hasNext()) {
	    Entry<Long, IndexLessonBean> entry = lessonIter.next();
	    if (entry.getValue().isDependent() && !lessonService.checkLessonReleaseConditions(entry.getKey(), userId)) {
		lessonIter.remove();
	    }
	}

	for (IndexLessonBean bean : map.values()) {
	    LinkedList<IndexLinkBean> lessonLinks = new LinkedList<>();
	    String url = null;
	    Integer lessonStateId = bean.getState();
	    Long lessonId = bean.getId();
	    
	    if (roles.contains(Role.ROLE_LEARNER)
		    && (lessonStateId.equals(Lesson.STARTED_STATE) || lessonStateId.equals(Lesson.FINISHED_STATE))) {
		url = "javascript:openLearner(" + lessonId + ")";
		
		//calculate learner progress. for all other lesson states, progress will be displayed as 0
		LearnerProgress learnerProgress = lessonService.getUserProgressForLesson(userId, lessonId);
		if (learnerProgress != null) {
		    int progressAsLearner = learnerProgress.isComplete() ? 100
			    : learnerProgress.getCompletedActivities().size() * 100
				    / learnerProgress.getLesson().getLearningDesign().getActivities().size();
		    bean.setProgressAsLearner(progressAsLearner);
		}
	    }
	    
//	    public sdtatic final Integer CREATED = 1;
//	    /** The state for lessons that have been scheduled. */
//	    public static final Integer NOT_STARTED_STATE = 2;
//	    /** The state for started lesson */
//	    public static final Integer STARTED_STATE = 3;
//	    /**
//	     * The state for lessons that have been suspended by the teacher. The lesson can be seen on the staff interface but
//	     * not on the learning interface
//	     */
//	    public static final Integer SUSPENDED_STATE = 4;
//	    /**
//	     * The state for lessons that have been finished. A finished lesson is shown as inactive on the staff interface, and
//	     * is shown on the learner interface but the learner is to only see the overall progress and be able to export data
//	     * - they should not be able to iteract with the tools
//	     */
//	    public static final Integer FINISHED_STATE = 5;
//	    /**
//	     * The state for lesssons that are shown as inactive on the staff interface but no longer visible to the learners.
//	     */
//	    public static final Integer ARCHIVED_STATE = 6;
//	    /** The state for lesssons that are removed and never can be accessed again */
//	    public static final Integer REMOVED_STATE = 7;
	    

	    
	    if (roles.contains(Role.ROLE_LEARNER)) {
		int progressAsLearner = 0;
		if (Lesson.STARTED_STATE.equals(lessonStateId)) {
		    
		}
		
	    }

	    if ((lessonLinks.size() > 0) || (url != null)) {
		bean.setUrl(url);
		bean.setLinks(lessonLinks);
	    }
	}

	// iterate through user's lessons where they are staff (or simply through all lessons in case of Group_Manager),
	// and add staff links to the beans in the map.
	Integer userRole = (roles.contains(Role.ROLE_GROUP_MANAGER)) ? Role.ROLE_GROUP_MANAGER : Role.ROLE_MONITOR;
	Map<Long, IndexLessonBean> staffMap = lessonService.getLessonsByOrgAndUserWithCompletedFlag(userId, orgId,
		userRole);
	boolean isGroupManagerOrMonitor = roles.contains(Role.ROLE_GROUP_MANAGER) || roles.contains(Role.ROLE_MONITOR);
	    
	Map<Long, Integer> countTotalLearnersMap = isGroupManagerOrMonitor
		? lessonService.getCountLearnersByOrganisationLessons(orgId)
		: null;
	Map<Long, Integer> countAttemptedLearnersMap = isGroupManagerOrMonitor
		? monitoringService.getCountAttemptedUsersByOrganisationLessons(orgId)
		: null;
	Map<Long, Integer> countCompletedLearnersMap = isGroupManagerOrMonitor
		? monitoringService.getCountCompletedUsersByOrganisationLessons(orgId)
		: null;

	for (IndexLessonBean bean : staffMap.values()) {
	    Long lessonId = bean.getId();
	    if (map.containsKey(lessonId)) {
		bean = map.get(lessonId);
	    }
	    
	    //set nunmber of not started/started/completed users
	    if (isGroupManagerOrMonitor) {
		int countTotalLearners = countTotalLearnersMap.containsKey(lessonId)
			? countTotalLearnersMap.get(lessonId)
			: 0;
		bean.setCountTotalLearners(countTotalLearners);
		int countAttemptedLearners = countAttemptedLearnersMap.containsKey(lessonId)
			? countAttemptedLearnersMap.get(lessonId)
			: 0;
		bean.setCountAttemptedLearners(countAttemptedLearners);
		int countCompletedLearners = countCompletedLearnersMap.containsKey(lessonId)
			? countCompletedLearnersMap.get(lessonId)
			: 0;
		bean.setCountCompletedLearners(countCompletedLearners);
	    }
	    
	    LinkedList<IndexLinkBean> lessonLinks = (LinkedList<IndexLinkBean>) bean.getLinks();
	    if (lessonLinks == null) {
		lessonLinks = new LinkedList<>();
		bean.setLinks(lessonLinks);
	    }
	    
	    LinkedList<IndexLinkBean> auxiliaryLessonLinks = (LinkedList<IndexLinkBean>) bean.getAuxiliaryLinks();
	    if (auxiliaryLessonLinks == null) {
		auxiliaryLessonLinks = new LinkedList<>();
		bean.setAuxiliaryLinks(auxiliaryLessonLinks);
	    }

	    if (isGroupManagerOrMonitor) {
		String showMonitor = "javascript:showMonitorLessonDialog(" + lessonId + ")";
		
		lessonLinks.addFirst(new IndexLinkBean("index.monitor",
			showMonitor, "fa fa-fw fa-heartbeat", null));

		//check whether there are any required tasks for this lesson
		int contributeActivitiesCounter = monitoringService.getCountContributeActivities(lessonId);
		if (contributeActivitiesCounter != 0) {
		    bean.setRequiredTasksUrl(showMonitor);    
		}
	    }

	    // Adding lesson notifications links if enabled
	    if (isGroupManagerOrMonitor && bean.isEnableLessonNotifications()) {
		lessonLinks.addFirst(new IndexLinkBean("index.emailnotifications",
			"javascript:showNotificationsDialog(null," + lessonId + ")", "fa fa-fw fa-bullhorn",
			"index.emailnotifications.tooltip"));
	    }

	    // Add lesson conditions
	    if (isGroupManagerOrMonitor) {
		String conditionsLink = "javascript:showConditionsDialog(" + lessonId + ")";
		auxiliaryLessonLinks.addFirst(new IndexLinkBean("index.conditions", conditionsLink, "fa fa-fw fa-code-fork",
			"index.conditions.tooltip"));
	    }

	    // Add delete lesson option
	    if (isGroupManagerOrMonitor) {
		String removeLessonLink = "javascript:removeLesson(" + lessonId + ")";
		auxiliaryLessonLinks.addFirst(new IndexLinkBean("index.remove.lesson", removeLessonLink, "fa fa-fw fa-trash-o",
			"index.remove.lesson.tooltip"));
	    }

	    map.put(lessonId, bean);
	}
	
	boolean isFavouriteLessonEnabled = isGroupManagerOrMonitor || roles.contains(Role.ROLE_AUTHOR);
	List<Long> favoriteLessonIds = isFavouriteLessonEnabled
		? userManagementService.getFavoriteLessonsByOrgAndUser(orgId, userId)
		: new ArrayList<>();
	
	//required learning design id, number of learners (started/total), required actions (from monitorLesson.js:updateContributeActivities)
	for (IndexLessonBean bean : map.values()) { 
	    bean.setFavorite(favoriteLessonIds.contains(bean.getId()));
	}

	return map;
    }

    private User getUser(String login) {
	return (User) userManagementService.findByProperty(User.class, "login", login).get(0);
    }
}
