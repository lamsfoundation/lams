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

/* $Id$ */
package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupComparator;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.lesson.service.LessonServiceException;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceException;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.util.LastNameAlphabeticComparator;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * The action servlet that provides the support for the
 * <UL>
 * <LI>AJAX based Chosen Grouping screen</LI>
 * <LI>forwards to the learner's view grouping screen for Random Grouping.</LI>
 * </UL>
 *
 * @author Fiona Malikoff
 *
 *         ----------------XDoclet Tags--------------------
 *
 * @struts:action path="/grouping" parameter="method" validate="false"
 * @struts.action-forward name = "chosenGrouping" path = "/grouping/chosenGrouping.jsp"
 * @struts.action-forward name = "viewGroups" path = ".viewGroups"
 *
 *                        ----------------XDoclet Tags--------------------
 */
public class GroupingAJAXAction extends LamsDispatchAction {

    // ---------------------------------------------------------------------

    private static final String CHOSEN_GROUPING_SCREEN = "chosenGrouping";
    private static final String VIEW_GROUPS_SCREEN = "viewGroups";
    private static final String PARAM_ACTIVITY_TITLE = "title";
    private static final String PARAM_ACTIVITY_DESCRIPTION = "description";
    public static final String PARAM_MAX_NUM_GROUPS = "maxNumberOfGroups";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_GROUPS = "groups";
    public static final String PARAM_MEMBERS = "members";
    public static final String PARAM_MAY_DELETE = "mayDelete";
    public static final String PARAM_USED_FOR_BRANCHING = "usedForBranching";
    public static final String PARAM_MODULE_LANGUAGE_XML = "languageXML";
    public static final String PARAM_VIEW_MODE = "viewMode";

    private Integer getUserId(HttpServletRequest request) {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    private Grouping getGrouping(GroupingActivity activity) {
	Grouping grouping = activity.getCreateGrouping();
	if (grouping == null) {
	    String error = "Grouping activity missing grouping. Activity was " + activity + " Grouping was " + grouping;
	    LamsDispatchAction.log.error(error);
	    throw new MonitoringServiceException(error);
	}
	return grouping;
    }

    /**
     * Start the process of doing the chosen grouping
     *
     * Input parameters: activityID
     */
    public ActionForward startGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	GroupingActivity activity = monitoringService.getGroupingActivityById(activityID);
	Grouping grouping = activity.getCreateGrouping();
	if (grouping == null) {
	    String error = "Grouping activity missing grouping. Activity was " + activity;
	    LamsDispatchAction.log.error(error);
	    throw new MonitoringServiceException(error);
	}

	request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activityID);
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
	request.setAttribute(GroupingAJAXAction.PARAM_ACTIVITY_TITLE, activity.getTitle());
	request.setAttribute(GroupingAJAXAction.PARAM_ACTIVITY_DESCRIPTION, activity.getDescription());
	request.setAttribute(GroupingAJAXAction.PARAM_MODULE_LANGUAGE_XML, getLanguageXML());

	if (grouping.isChosenGrouping()) {
	    // can I remove groups/users - can't if tool sessions have been created
	    Set groups = grouping.getGroups();
	    Iterator iter = groups.iterator();
	    boolean mayDelete = true;
	    while (mayDelete && iter.hasNext()) {
		Group group = (Group) iter.next();
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

	} else {

	    // go to a view only screen for random grouping
	    request.setAttribute(GroupingAJAXAction.PARAM_MAY_DELETE, Boolean.FALSE);
	    request.setAttribute(GroupingAJAXAction.PARAM_VIEW_MODE, Boolean.TRUE);
	    return mapping.findForward(GroupingAJAXAction.VIEW_GROUPS_SCREEN);
	}

    }

    /**
     * Get a list of group names and the number of users in each group. Designed to respond to an AJAX call.
     *
     * Input parameters: activityID
     *
     * Output format: "groupid,name,num users;groupid,name,num users"
     */
    public ActionForward getGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	// get the grouping data and sort it.
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	GroupingActivity activity = monitoringService.getGroupingActivityById(activityID);
	Grouping grouping = getGrouping(activity);
	Set<Group> sortedGroups = new TreeSet<Group>(new GroupComparator());
	sortedGroups.addAll(grouping.getGroups());

	String groupOutput = buildGroupsStringXML(sortedGroups);
	writeAJAXResponse(response, groupOutput);
	return null;
    }

    /**
     * Get a list of all the class members who aren't grouped yet. Designed to respond to an AJAX call.
     *
     * Input parameters: activityID
     *
     * Output format: "groupid,name,num users;groupid,name,num users"
     */
    public ActionForward getClassMembersNotGrouped(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	// get the grouping data and sort it.
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	SortedSet<User> users = monitoringService.getClassMembersNotGrouped(lessonID, activityID, true);
	String groupOutput = buildUserStringXML(-1, users);
	writeAJAXResponse(response, groupOutput);
	return null;

    }

    /**
     * Get a list of group names and the number of users in each group. Designed to respond to an AJAX call.
     *
     * Input parameters: activityID, groupID
     *
     * Output format: "userid,lastname,firstname;"
     */
    public ActionForward getGroupMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	// TODO optimise this call - we don't really need the activity and the grouping - go straight to the group in
	// the db
	// get the group, and from there the user data and sort the user data.
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	GroupingActivity activity = monitoringService.getGroupingActivityById(activityID);
	Grouping grouping = getGrouping(activity);
	Set groups = grouping.getGroups();
	Iterator iter = groups.iterator();
	Group group = null;
	while ((group == null) && iter.hasNext()) {
	    Group candidateGroup = (Group) iter.next();
	    if (groupID.equals(candidateGroup.getGroupId())) {
		group = candidateGroup;
	    }
	}
	if (group == null) {
	    String error = "Group cannot be found. Activity was " + activity + " Grouping was " + grouping
		    + " Grouping ID was " + groupID;
	    LamsDispatchAction.log.error(error);
	    throw new MonitoringServiceException(error);
	}

	Set users = group.getUsers();
	SortedSet<User> sortedUsers = new TreeSet<User>(new LastNameAlphabeticComparator());
	sortedUsers.addAll(users);
	String userOutput = buildUserStringXML(groupID, sortedUsers);
	writeAJAXResponse(response, userOutput);
	return null;
    }

    /**
     * Output format: "userid,lastname,firstname;"
     *
     * @param sortedUsers
     * @return String of users
     */
    private String buildUserString(SortedSet<User> sortedUsers) {
	String userOutput = "";
	boolean first = true;
	for (User user : sortedUsers) {
	    Integer userID = user.getUserId();
	    String lastName = user.getLastName();
	    String firstName = user.getFirstName();
	    if (!first) {
		userOutput = userOutput + ";";
	    } else {
		first = false;
	    }
	    userOutput = userOutput + userID + "," + lastName + "," + firstName;
	}
	return userOutput;
    }

    /**
     * @param sortedUsers
     * @return String of users
     */
    private String buildUserStringXML(long groupId, SortedSet<User> sortedUsers) {
	String userOutput = "<xml>";
	userOutput += "<groupID>" + groupId + "</groupID>";
	userOutput += "<users>";
	for (User user : sortedUsers) {
	    Integer userID = user.getUserId();
	    String lastName = user.getLastName();
	    String firstName = user.getFirstName();

	    userOutput += "<user>";
	    userOutput += "<id>" + userID + "</id>";
	    userOutput += "<firstName>" + firstName + "</firstName>";
	    userOutput += "<lastName>" + lastName + "</lastName>";
	    userOutput += "<displayName>" + firstName + " " + lastName + "</displayName>";
	    userOutput += "</user>";
	}
	userOutput += "</users></xml>";
	return userOutput;
    }

    /**
     * @param sortedGroups
     * @return String of groups
     */
    private String buildGroupsString(Set<Group> sortedGroups) {
	String groupOutput = "";

	boolean first = true;
	for (Group group : sortedGroups) {
	    Long groupId = group.getGroupId();
	    String name = group.getGroupName();
	    Integer numberOfMembers = group.getUsers().size();
	    if (!first) {
		groupOutput = groupOutput + ";";
	    } else {
		first = false;
	    }
	    groupOutput = groupOutput + groupId + "," + name + "," + numberOfMembers;
	}

	return groupOutput;
    }

    /**
     * @param sortedGroups
     * @return String of groups
     */
    private String buildGroupsStringXML(Set<Group> sortedGroups) {
	String groupOutput = "<xml><groups>";
	for (Group group : sortedGroups) {
	    Long groupId = group.getGroupId();
	    String name = group.getGroupName();
	    Integer numberOfMembers = group.getUsers().size();

	    groupOutput += "<group>";
	    groupOutput += "<id>" + groupId + "</id>";
	    groupOutput += "<name>" + name + "</name>";
	    groupOutput += "<numberOfMembers>" + numberOfMembers + "</numberOfMembers>";
	    groupOutput += "</group>";
	}
	groupOutput += "</groups></xml>";

	return groupOutput;
    }

    /**
     * @param group
     * @return String of xml with group added
     */
    private String buildAddGroupStringXML(Group group) {
	String groupOutput = "<xml><group>";
	groupOutput += "<id>" + group.getGroupId() + "</id>";
	groupOutput += "<name>" + group.getGroupName() + "</name>";
	groupOutput += "</group></xml>";
	return groupOutput;
    }

    /**
     * @param group
     * @return String of xml with group added
     */
    private String buildRemoveGroupStringXML(Long groupID) {
	String groupOutput = "<xml><group>";
	groupOutput += "<id>" + groupID + "</id>";
	groupOutput += "</group></xml>";
	return groupOutput;
    }

    /**
     * @param group
     * @return String of xml with group added
     */
    private String buildChangeGroupNameStringXML(Long groupID, String name) {
	String groupOutput = "<xml><group>";
	groupOutput += "<id>" + groupID + "</id>";
	groupOutput += "<newName>" + name + "</newName>";
	groupOutput += "</group></xml>";
	return groupOutput;
    }

    /**
     * @return String of xml with all needed language elements
     */
    private String getLanguageXML() {
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	MessageService messageService = monitoringService.getMessageService();
	ArrayList<String> languageCollection = new ArrayList<String>();
	languageCollection.add(new String("button.finished"));
	languageCollection.add(new String("label.grouping.non.grouped.users.heading"));
	languageCollection.add(new String("label.grouping.status"));
	languageCollection.add(new String("label.grouping.functions"));
	languageCollection.add(new String("label.grouping.popup.change.group.name"));
	languageCollection.add(new String("label.grouping.popup.change.group.name.field"));
	languageCollection.add(new String("label.grouping.learners"));
	languageCollection.add(new String("label.grouping.popup.delete.group"));
	languageCollection.add(new String("label.grouping.popup.delete.group.message"));
	languageCollection.add(new String("button.ok"));
	languageCollection.add(new String("button.cancel"));
	languageCollection.add(new String("button.yes"));
	languageCollection.add(new String("button.no"));
	languageCollection.add(new String("error.title"));
	languageCollection.add(new String("label.grouping.max.num.in.group.heading"));
	languageCollection.add(new String("label.grouping.popup.drag.selection.message"));
	languageCollection.add(new String("label.grouping.general.instructions.branching"));
	languageCollection.add(new String("label.grouping.popup.viewmode.message"));

	String languageOutput = "<xml><language>";

	for (int i = 0; i < languageCollection.size(); i++) {
	    languageOutput += "<entry key='" + languageCollection.get(i) + "'><name>"
		    + messageService.getMessage(languageCollection.get(i)) + "</name></entry>";
	}

	languageOutput += "</language></xml>";

	return languageOutput;
    }

    /**
     * Add a new group. Designed to respond to an AJAX call. If the teacher wants to add more groups than the number of
     * groups set in authoring, and this grouping isn't used for branching then reset the max number of groups to avoid
     * that validation.
     *
     * Input parameters: activityID, name (group name)
     *
     * Output format: no data returned - just the header
     */
    public ActionForward addGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	String name = WebUtil.readStrParam(request, GroupingAJAXAction.PARAM_NAME);
	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	Group group = monitoringService.addGroup(activityID, name, true);
	String groupResponse = buildAddGroupStringXML(group);
	writeAJAXResponse(response, groupResponse);
	return null;
    }

    /**
     * Remove a group. Cannot remove the group if it is in use (tool session ids exist). Designed to respond to an AJAX
     * call.
     *
     * Input parameters: activityID, name: group name
     *
     * Output format: no data returned - just the header
     */
    public ActionForward removeGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);

	IMonitoringService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	monitoringService.removeGroup(activityID, groupID);
	String responseString = buildRemoveGroupStringXML(groupID);
	writeAJAXResponse(response, responseString);
	return null;
    }

    /**
     * Add learners to a group. Designed to respond to an AJAX call.
     *
     * Input parameters: activityID, name: group name, members: comma separated list of users
     *
     * Output format: no data returned - just the header
     */
    public ActionForward addMembers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
	String members = WebUtil.readStrParam(request, GroupingAJAXAction.PARAM_MEMBERS, true);
	if (members != null) {
	    String[] membersSplit = members.split(",");
	    IMonitoringService monitoringService = MonitoringServiceProxy
		    .getMonitoringService(getServlet().getServletContext());
	    monitoringService.addUsersToGroup(activityID, groupID, membersSplit);
	}
	writeAJAXOKResponse(response);
	return null;
    }

    /**
     * Add learners to a group. Designed to respond to an AJAX call.
     *
     * Input parameters: activityID, name: group name, members: comma separated list of users
     *
     * Output format: no data returned - just the header
     */
    public ActionForward changeGroupName(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
	String name = WebUtil.readStrParam(request, GroupingAJAXAction.PARAM_NAME, true);
	if (name != null) {
	    IMonitoringService monitoringService = MonitoringServiceProxy
		    .getMonitoringService(getServlet().getServletContext());
	    monitoringService.setGroupName(groupID, name);
	}
	String reponseSting = buildChangeGroupNameStringXML(groupID, name);
	writeAJAXResponse(response, reponseSting);
	return null;
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
    public ActionForward addMembersJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
	    GroupingActivity groupingActivity = monitoringService.getGroupingActivityById(activityID);
	    Grouping grouping = groupingActivity.getCreateGrouping();
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

	    if (result && members != null) {
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
    public ActionForward changeGroupNameJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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
    public ActionForward removeGroupJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
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