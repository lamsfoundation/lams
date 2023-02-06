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

package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.service.LessonServiceException;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationGroup;
import org.lamsfoundation.lams.usermanagement.OrganisationGrouping;
import org.lamsfoundation.lams.usermanagement.OrganisationType;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.usermanagement.util.FirstNameAlphabeticComparator;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The action servlet that provides the support for the
 * <UL>
 * <LI>AJAX based Chosen Grouping screen</LI>
 * <LI>forwards to the learner's view grouping screen for Random Grouping.</LI>
 * </UL>
 *
 * @author Fiona Malikoff
 */
@Controller
@RequestMapping("/grouping")
public class GroupingController {
    private static Logger log = Logger.getLogger(GroupingController.class);

    @Autowired
    private IMonitoringFullService monitoringService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IUserManagementService userManagementService;

    private static final String PARAM_ACTIVITY_TITLE = "title";
    private static final String PARAM_ACTIVITY_DESCRIPTION = "description";
    public static final String PARAM_MAX_NUM_GROUPS = "maxNumberOfGroups";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_MEMBERS = "members";
    public static final String PARAM_MAY_DELETE = "mayDelete";
    public static final String PARAM_USED_FOR_BRANCHING = "usedForBranching";
    public static final String PARAM_VIEW_MODE = "viewMode";
    public static final String GROUPS = "groups";

    /**
     * Start the process of doing the chosen grouping
     *
     * Input parameters: activityID
     */
    @RequestMapping("/startGrouping")
    public String startGrouping(HttpServletRequest request) throws IOException, ServletException {
	return startGrouping(request, false);
    }

    private String startGrouping(HttpServletRequest request, boolean forcePrintView)
	    throws IOException, ServletException {

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Activity activity = monitoringService.getActivityById(activityID);

	Grouping grouping = null;
	if (activity.isChosenBranchingActivity()) {
	    grouping = activity.getGrouping();
	    monitoringService.createChosenBranchingGroups(activityID);
	} else {
	    grouping = ((GroupingActivity) activity).getCreateGrouping();
	}

	if (!forcePrintView) {
	    return "redirect:" + Configuration.get(ConfigurationKeys.SERVER_URL) + "grouping/viewGroupings.do?lessonID="
		    + lessonId + "&activityID=" + activityID;
	}

	request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activityID);
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
	request.setAttribute(GroupingController.PARAM_ACTIVITY_TITLE, activity.getTitle());
	request.setAttribute(GroupingController.PARAM_ACTIVITY_DESCRIPTION, activity.getDescription());

	SortedSet<Group> groups = new TreeSet<>();
	groups.addAll(grouping.getGroups());

	// sort users with first, then last name, then login
	Comparator<User> userComparator = new FirstNameAlphabeticComparator();
	for (Group group : groups) {
	    Set<User> sortedUsers = new TreeSet<>(userComparator);
	    sortedUsers.addAll(group.getUsers());
	    group.setUsers(sortedUsers);
	}

	request.setAttribute(GroupingController.GROUPS, groups);
	// go to a view only screen for random grouping
	return "grouping/viewGroups";
    }

    /**
     * Called by the chosen grouping / course grouping screen to show a print version of the grouping.
     */
    @RequestMapping("/printGrouping")
    public String printGrouping(HttpServletRequest request) throws IOException, ServletException {

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID, true);
	if (activityID != null) {
	    // normal activity based processing, startGrouping can handle it as it supports the normal view screen in monitoring
	    return startGrouping(request, true);
	}

	// Not activity? Then it must be the course grouping print view request
	Long orgGroupingId = WebUtil.readLongParam(request, "groupingId", true);
	OrganisationGrouping orgGrouping = null;
	if (orgGroupingId != null) {
	    orgGrouping = (OrganisationGrouping) userManagementService.findById(OrganisationGrouping.class,
		    orgGroupingId);
	}

	SortedSet<OrganisationGroup> groups = new TreeSet<>();
	if (orgGrouping != null) {
	    groups.addAll(orgGrouping.getGroups());

	    // sort users with first, then last name, then login
	    Comparator<User> userComparator = new FirstNameAlphabeticComparator();
	    for (OrganisationGroup group : groups) {
		Set<User> sortedUsers = new TreeSet<>(userComparator);
		sortedUsers.addAll(group.getUsers());
		group.setUsers(sortedUsers);
	    }
	}

	request.setAttribute(GroupingController.GROUPS, groups);
	request.setAttribute("isCourseGrouping", true); // flag to page it is a course grouping so use the field names for OrganisationGroup
	return "grouping/viewGroups";
    }

    /**
     * Moves users between groups, removing them from previous group and creating a new one, if needed.
     */
    @RequestMapping(path = "/addMembers", method = RequestMethod.POST)
    @ResponseBody
    public String addMembers(HttpServletRequest request, HttpServletResponse response) throws IOException {
	response.setContentType("application/json;charset=utf-8");
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	boolean result = true;

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	String membersParam = WebUtil.readStrParam(request, GroupingController.PARAM_MEMBERS, true);
	String[] members = StringUtils.isBlank(membersParam) ? null : membersParam.split(",");

	// remove users from current group
	if (members != null) {
	    Activity activity = monitoringService.getActivityById(activityID);
	    Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		    : ((GroupingActivity) activity).getCreateGrouping();
	    User exampleUser = (User) userManagementService.findById(User.class, Integer.valueOf(members[0]));
	    Group group = grouping.getGroupBy(exampleUser);
	    // null group means that user is not assigned anywhere in this grouping
	    if (!group.isNull()) {
		// check if user can be moved outside of this group
		result = group.mayBeDeleted();

		if (result) {
		    if (log.isDebugEnabled()) {
			log.debug("Removing users " + membersParam.toString() + " from group " + group.getGroupId()
				+ " in activity " + activityID);
		    }

		    try {
			monitoringService.removeUsersFromGroup(activityID, group.getGroupId(), members);
		    } catch (LessonServiceException e) {
			log.error(e);
			result = false;
		    }
		}

		if (!result) {
		    // let JSP page know that this group became immutable
		    responseJSON.put("locked", true);
		}
	    }
	}

	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID, true);
	// no group ID means that it has to be created
	// group ID = -1 means that user is not being assigned to any new group, i.e. becomse unassigned
	if (result && ((groupID == null) || (groupID > 0))) {
	    if (groupID == null) {
		String name = WebUtil.readStrParam(request, GroupingController.PARAM_NAME);
		if (log.isDebugEnabled()) {
		    log.debug("Creating group with name \"" + name + "\" in activity " + activityID);
		}
		Group group = monitoringService.addGroup(activityID, name, true);
		if (group == null) {
		    // group creation failed
		    result = false;
		} else {
		    groupID = group.getGroupId();
		    // let JSP page know that the group was given this ID
		    responseJSON.put("groupId", groupID);
		}
	    }

	    if (result && (members != null)) {
		if (log.isDebugEnabled()) {
		    log.debug("Adding users " + membersParam.toString() + " to group " + groupID + " in activity "
			    + activityID);
		}

		// add users to the given group
		try {
		    monitoringService.addUsersToGroup(activityID, groupID, members);
		} catch (LessonServiceException e) {
		    log.error(e);
		    result = false;
		}
	    }
	}

	responseJSON.put("result", result);
	return responseJSON.toString();
    }

    /**
     * Stores lesson grouping as a course grouping.
     */
    @RequestMapping(path = "/saveAsCourseGrouping", method = RequestMethod.POST)
    @ResponseBody
    public String saveAsCourseGrouping(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	Organisation organisation = (Organisation) userManagementService.findById(Organisation.class, organisationId);
	// get course groupings from top-leve course
	if (OrganisationType.CLASS_TYPE.equals(organisation.getOrganisationType().getOrganisationTypeId())) {
	    organisation = organisation.getParentOrganisation();
	    organisationId = organisation.getOrganisationId();
	}
	HttpSession ss = SessionManager.getSession();
	Integer userId = ((UserDTO) ss.getAttribute(AttributeNames.USER)).getUserID();
	String newGroupingName = request.getParameter("name");

	// check if user is allowed to view and edit groupings
	if (!securityService.hasOrgRole(organisationId, userId,
		new String[] { Role.GROUP_MANAGER, Role.MONITOR, Role.AUTHOR }, "view organisation groupings")) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a participant in the organisation");
	    return null;
	}

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Activity activity = monitoringService.getActivityById(activityID);
	Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		: ((GroupingActivity) activity).getCreateGrouping();

	// iterate over groups
	List<OrganisationGroup> orgGroups = new LinkedList<>();
	for (Group group : grouping.getGroups()) {
	    OrganisationGroup orgGroup = new OrganisationGroup();
	    //groupId and GroupingId will be set during  userManagementService.saveOrganisationGrouping() call
	    orgGroup.setName(group.getGroupName());
	    HashSet<User> users = new HashSet<>();
	    users.addAll(group.getUsers());
	    orgGroup.setUsers(users);

	    orgGroups.add(orgGroup);
	}

	OrganisationGrouping orgGrouping = new OrganisationGrouping();
	orgGrouping.setOrganisationId(organisationId);
	orgGrouping.setName(newGroupingName);

	userManagementService.saveOrganisationGrouping(orgGrouping, orgGroups);

	response.setContentType("application/json;charset=utf-8");
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("result", true);
	return responseJSON.toString();
    }

    /**
     * Renames the group.
     */
    @RequestMapping(path = "/changeGroupName", method = RequestMethod.POST)
    public String changeGroupName(HttpServletRequest request) {
	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
	String name = WebUtil.readStrParam(request, GroupingController.PARAM_NAME);
	if (name != null) {
	    if (log.isDebugEnabled()) {
		log.debug("Renaming group  " + groupID + " to \"" + name + "\"");
	    }
	    monitoringService.setGroupName(groupID, name);
	}
	return null;
    }

    /**
     * Checks if a course grouping name is unique inside of this organisation and thus whether the new group can be
     * named using it
     */
    @RequestMapping("/checkGroupingNameUnique")
    @ResponseBody
    public String checkGroupingNameUnique(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Integer organisationId = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	String newGroupingName = request.getParameter("name");

	// Checks if a course grouping name is unique inside of this group and thus new group can have it
	HashMap<String, Object> properties = new HashMap<>();
	properties.put("organisationId", organisationId);
	properties.put("name", newGroupingName);
	List<OrganisationGrouping> orgGroupings = userManagementService.findByProperties(OrganisationGrouping.class,
		properties);
	boolean isGroupingNameUnique = orgGroupings.isEmpty();

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("isGroupingNameUnique", isGroupingNameUnique);
	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Checks if a group can be removed and performs it.
     */
    @RequestMapping(path = "/removeGroup", method = RequestMethod.POST)
    @ResponseBody
    public String removeGroup(HttpServletRequest request, HttpServletResponse response) throws IOException {
	response.setContentType("application/json;charset=utf-8");
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
	boolean result = true;

	// check if the group can be removed
	Group group = (Group) userManagementService.findById(Group.class, groupID);
	result = group.mayBeDeleted();

	if (result) {
	    try {
		if (log.isDebugEnabled()) {
		    log.debug("Removing group  " + groupID + " from activity " + activityID);
		}
		monitoringService.removeGroup(activityID, groupID);
	    } catch (LessonServiceException e) {
		log.error(e);
		result = false;
	    }
	}
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("result", result);
	return responseJSON.toString();
    }
}
