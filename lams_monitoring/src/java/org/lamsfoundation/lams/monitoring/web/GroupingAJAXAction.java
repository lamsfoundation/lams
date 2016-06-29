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
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.web.action.GroupingAction;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupComparator;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.service.LessonServiceException;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.util.FirstNameAlphabeticComparator;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * The action servlet that provides the support for the
 * <UL>
 * <LI>AJAX based Chosen Grouping screen</LI>
 * <LI>forwards to the learner's view grouping screen for Random Grouping.</LI>
 * </UL>
 *
 * @author Fiona Malikoff
 */
public class GroupingAJAXAction extends LamsDispatchAction {

    // ---------------------------------------------------------------------

    private static final String CHOSEN_GROUPING_SCREEN = "chosenGrouping";
    private static final String VIEW_GROUPS_SCREEN = "viewGroups";
    private static final String PARAM_ACTIVITY_TITLE = "title";
    private static final String PARAM_ACTIVITY_DESCRIPTION = "description";
    public static final String PARAM_MAX_NUM_GROUPS = "maxNumberOfGroups";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_MEMBERS = "members";
    public static final String PARAM_MAY_DELETE = "mayDelete";
    public static final String PARAM_USED_FOR_BRANCHING = "usedForBranching";
    public static final String PARAM_VIEW_MODE = "viewMode";

    /**
     * Start the process of doing the chosen grouping
     *
     * Input parameters: activityID
     */
    @SuppressWarnings("unchecked")
    public ActionForward startGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	Activity activity = monitoringService.getActivityById(activityID);

	Grouping grouping = null;
	if (activity.isChosenBranchingActivity()) {
	    grouping = activity.getGrouping();
	    monitoringService.createChosenBranchingGroups(activityID);
	} else {
	    grouping = ((GroupingActivity) activity).getCreateGrouping();
	}

	request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activityID);
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
	request.setAttribute(GroupingAJAXAction.PARAM_ACTIVITY_TITLE, activity.getTitle());
	request.setAttribute(GroupingAJAXAction.PARAM_ACTIVITY_DESCRIPTION, activity.getDescription());

	if (grouping.isChosenGrouping()) {
	    // can I remove groups/users - can't if tool sessions have been created
	    Set<Group> groups = grouping.getGroups();
	    Iterator<Group> iter = groups.iterator();
	    boolean mayDelete = true;
	    while (mayDelete && iter.hasNext()) {
		Group group = iter.next();
		mayDelete = group.mayBeDeleted();
	    }

	    // is this grouping used for branching. If it is, must honour the groups
	    // set in authoring or some groups won't have a branch. mayDelete can still
	    // be true or false as you can remove users from groups, you just can't remove
	    // groups due to the branching relationship.
	    boolean usedForBranching = grouping.isUsedForBranching();

	    request.setAttribute(GroupingAJAXAction.PARAM_MAY_DELETE, mayDelete);
	    request.setAttribute(GroupingAJAXAction.PARAM_USED_FOR_BRANCHING, usedForBranching);
	    request.setAttribute(GroupingAJAXAction.PARAM_MAX_NUM_GROUPS, grouping.getMaxNumberOfGroups());
	    request.setAttribute(GroupingAJAXAction.PARAM_VIEW_MODE, Boolean.FALSE);

	    return mapping.findForward(GroupingAJAXAction.CHOSEN_GROUPING_SCREEN);
	}

	SortedSet<Group> groups = new TreeSet<Group>(new GroupComparator());
	groups.addAll(grouping.getGroups());

	// sort users with first, then last name, then login
	Comparator<User> userComparator = new FirstNameAlphabeticComparator();
	for (Group group : groups) {
	    Set<User> sortedUsers = new TreeSet<User>(userComparator);
	    sortedUsers.addAll(group.getUsers());
	    group.setUsers(sortedUsers);
	}

	request.setAttribute(GroupingAction.GROUPS, groups);
	// go to a view only screen for random grouping
	return mapping.findForward(GroupingAJAXAction.VIEW_GROUPS_SCREEN);
    }

    /**
     * Remove a list of users from a group. Designed to respond to an AJAX call.
     *
     * Input parameters: activityID, name: group name, members: comma separated list of users
     *
     * Output format: no data returned - just the header
     */
    public ActionForward removeMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
	String members = WebUtil.readStrParam(request, GroupingAJAXAction.PARAM_MEMBERS, true);
	if (members != null) {
	    String[] membersSplit = members.split(",");
	    IMonitoringService monitoringService = MonitoringServiceProxy
		    .getMonitoringService(getServlet().getServletContext());
	    monitoringService.removeUsersFromGroup(activityID, groupID, membersSplit);
	}
	writeAJAXOKResponse(response);
	return null;
    }

    /**
     * Moves users between groups, removing them from previous group and creating a new one, if needed.
     */
    public ActionForward addMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	response.setContentType("application/json;charset=utf-8");
	JSONObject responseJSON = new JSONObject();
	boolean result = true;

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	String membersParam = WebUtil.readStrParam(request, GroupingAJAXAction.PARAM_MEMBERS, true);
	String[] members = StringUtils.isBlank(membersParam) ? null : membersParam.split(",");

	// remove users from current group
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	if (members != null) {
	    Activity activity = monitoringService.getActivityById(activityID);
	    Grouping grouping = activity.isChosenBranchingActivity() ? activity.getGrouping()
		    : ((GroupingActivity) activity).getCreateGrouping();
	    User exampleUser = (User) MonitoringServiceProxy.getUserManagementService(getServlet().getServletContext())
		    .findById(User.class, Integer.valueOf(members[0]));
	    Group group = grouping.getGroupBy(exampleUser);
	    // null group means that user is not assigned anywhere in this grouping
	    if (!group.isNull()) {
		// check if user can be moved outside of this group
		result = group.mayBeDeleted();

		if (result) {
		    if (LamsDispatchAction.log.isDebugEnabled()) {
			LamsDispatchAction.log.debug("Removing users " + membersParam.toString() + " from group "
				+ group.getGroupId() + " in activity " + activityID);
		    }

		    try {
			monitoringService.removeUsersFromGroup(activityID, group.getGroupId(), members);
		    } catch (LessonServiceException e) {
			LamsDispatchAction.log.error(e);
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
		String name = WebUtil.readStrParam(request, GroupingAJAXAction.PARAM_NAME);
		if (LamsDispatchAction.log.isDebugEnabled()) {
		    LamsDispatchAction.log.debug("Creating group with name \"" + name + "\" in activity " + activityID);
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
		if (LamsDispatchAction.log.isDebugEnabled()) {
		    LamsDispatchAction.log.debug("Adding users " + membersParam.toString() + " to group " + groupID
			    + " in activity " + activityID);
		}

		// add users to the given group
		try {
		    monitoringService.addUsersToGroup(activityID, groupID, members);
		} catch (LessonServiceException e) {
		    LamsDispatchAction.log.error(e);
		    result = false;
		}
	    }
	}

	responseJSON.put("result", result);
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    /**
     * Renames the group.
     */
    public ActionForward changeGroupName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
	String name = WebUtil.readStrParam(request, GroupingAJAXAction.PARAM_NAME);
	if (name != null) {
	    if (LamsDispatchAction.log.isDebugEnabled()) {
		LamsDispatchAction.log.debug("Renaming group  " + groupID + " to \"" + name + "\"");
	    }
	    IMonitoringService monitoringService = MonitoringServiceProxy
		    .getMonitoringService(getServlet().getServletContext());
	    monitoringService.setGroupName(groupID, name);
	}
	return null;
    }

    /**
     * Checks if a group can be removed and performs it.
     */
    public ActionForward removeGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	response.setContentType("application/json;charset=utf-8");
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	boolean result = true;

	// check if the group can be removed
	Group group = (Group) MonitoringServiceProxy.getUserManagementService(getServlet().getServletContext())
		.findById(Group.class, groupID);
	result = group.mayBeDeleted();

	if (result) {
	    try {
		if (LamsDispatchAction.log.isDebugEnabled()) {
		    LamsDispatchAction.log.debug("Removing group  " + groupID + " from activity " + activityID);
		}
		monitoringService.removeGroup(activityID, groupID);
	    } catch (LessonServiceException e) {
		LamsDispatchAction.log.error(e);
		result = false;
	    }
	}
	JSONObject responseJSON = new JSONObject();
	responseJSON.put("result", result);
	response.getWriter().write(responseJSON.toString());
	return null;
    }
}
