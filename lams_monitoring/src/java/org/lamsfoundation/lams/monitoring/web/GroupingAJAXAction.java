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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.monitoring.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ChosenGrouping;
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

* ----------------XDoclet Tags--------------------
* 
* @struts:action path="/grouping" 
*                parameter="method" 
*                validate="false"
* @struts.action-exception key="error.system.monitor" scope="request"
*                          type="org.lamsfoundation.lams.monitoring.service.MonitoringServiceException"
*                          path=".systemError"
* 							handler="org.lamsfoundation.lams.web.util.CustomStrutsExceptionHandler"
* @struts.action-exception key="error.system.monitor" scope="request"
*                          type="org.lamsfoundation.lams.lesson.service.LessonServiceException"
*                          path=".systemError"
* 							handler="org.lamsfoundation.lams.web.util.CustomStrutsExceptionHandler"
* @struts.action-forward name = "chosenGrouping" path = "/grouping/chosenGrouping.jsp"
* @struts.action-forward name = "viewGroups" path = ".viewGroups"
* 
* ----------------XDoclet Tags--------------------
*/
public class GroupingAJAXAction extends LamsDispatchAction {

    //---------------------------------------------------------------------

	private static final String CHOSEN_GROUPING_SCREEN = "chosenGrouping";
	private static final String VIEW_GROUPS_SCREEN = "viewGroups";
	private static final String PARAM_ACTIVITY_TITLE = "title";
	private static final String PARAM_ACTIVITY_DESCRIPTION = "description";
	public static final String PARAM_MAX_NUM_GROUPS = "maxNumberOfGroups";
	public static final String PARAM_NAME = "name";
	public static final String PARAM_GROUPS = "groups";
	public static final String PARAM_MEMBERS = "members";
	public static final String PARAM_MAY_DELETE = "mayDelete";
	
	private Integer getUserId(HttpServletRequest request) {
		HttpSession ss = SessionManager.getSession();
		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
		return user != null ? user.getUserID() : null;
	}
	

	private void writeAJAXResponse(HttpServletResponse response, String output) throws IOException {
		response.setContentType("text/html");
	    PrintWriter writer = response.getWriter();
	    // set it to unicode
		if (output.length()>0) {
	        writer.println(output);
		}
	}

	private void writeAJAXOKResponse(HttpServletResponse response) throws IOException {
		writeAJAXResponse(response, "OK");
	}

	private Grouping getGrouping(GroupingActivity activity) {
		Grouping grouping = activity.getCreateGrouping();
		if ( grouping == null ) {
			String error = "Grouping activity missing grouping. Activity was "+activity+" Grouping was "+grouping; 
			log.error(error);
			throw new MonitoringServiceException(error);
		}
		return grouping;
	}
	
	/** 
	 * Start the process of doing the chosen grouping
     *
	 * Input parameters: activityID
	 */
	public ActionForward startGrouping(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		
    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
        Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
		IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	GroupingActivity activity = monitoringService.getGroupingActivityById(activityID);
		Grouping grouping = activity.getCreateGrouping();
		if ( grouping == null  ) {
			String error = "Grouping activity missing grouping. Activity was "+activity; 
			log.error(error);
			throw new MonitoringServiceException(error);
		}

		request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activityID);
		request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
		request.setAttribute(PARAM_ACTIVITY_TITLE, activity.getTitle());
		request.setAttribute(PARAM_ACTIVITY_DESCRIPTION, activity.getDescription());
		
		if ( grouping.isChosenGrouping() ) {
			request.setAttribute(PARAM_MAX_NUM_GROUPS, grouping.getMaxNumberOfGroups());
			// can I remove groups/users - can't if tool sessions have been created
			Set groups = grouping.getGroups();
			Iterator iter = groups.iterator();
			boolean mayDelete = true;
			while (mayDelete && iter.hasNext()) {
				Group group = (Group) iter.next();
				mayDelete = group.mayBeDeleted();
			}
			request.setAttribute(PARAM_MAY_DELETE, mayDelete);
			return mapping.findForward(CHOSEN_GROUPING_SCREEN);
			
		} else {
			
			// go to a view only screen for random grouping
	        SortedSet groups = new TreeSet(new GroupComparator());
	        groups.addAll(grouping.getGroups());
	        request.getSession().setAttribute(PARAM_GROUPS,groups);
			return mapping.findForward(VIEW_GROUPS_SCREEN);
		}
    	
	}
    
	/** 
	 * Get a list of group names and the number of users in each group. Designed to respond to an AJAX call.
     *
	 * Input parameters: activityID
	 * 
	 * Output format: "groupid,name,num users;groupid,name,num users" 
	 */
	public ActionForward getGroups(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		// get the grouping data and sort it.
    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
		IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	GroupingActivity activity = monitoringService.getGroupingActivityById(activityID);
    	Grouping grouping = getGrouping(activity);
		Set<Group> sortedGroups = new TreeSet<Group>(new GroupComparator());
		sortedGroups.addAll(grouping.getGroups());

		// build the output string to return to the chosen grouping page.
		String groupOutput = "";
		boolean first = true;
		for ( Group group: sortedGroups ) {
			Long groupId = group.getGroupId();
			String name = group.getGroupName();
			Integer numberOfMembers = group.getUsers().size();
			if ( ! first ) {
				groupOutput=groupOutput+";";
			} else {
				first = false;
			}
			groupOutput=groupOutput+groupId+","+name+","+numberOfMembers;
		}
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
	public ActionForward getClassMembersNotGrouped(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		// get the grouping data and sort it.
    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
		Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
		IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
		SortedSet<User> users = monitoringService.getClassMembersNotGrouped(lessonID, activityID);
		String groupOutput = buildUserString(users);
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
	public ActionForward getGroupMembers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		// TODO optimise this call - we don't really need the activity and the grouping - go straight to the group in the db
		// get the group, and from there the user data and sort the user data.
    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
    	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
		IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	GroupingActivity activity = monitoringService.getGroupingActivityById(activityID);
    	Grouping grouping = getGrouping(activity);
    	Set groups = grouping.getGroups();
    	Iterator iter = groups.iterator();
    	Group group = null;
    	while (group==null && iter.hasNext()) {
			Group candidateGroup = (Group) iter.next();
			if ( groupID.equals(candidateGroup.getGroupId()) )
				group = candidateGroup;
		}
		if ( group == null ) {
			String error = "Group cannot be found. Activity was "+activity+" Grouping was "+grouping+" Grouping ID was "+groupID; 
			log.error(error);
			throw new MonitoringServiceException(error);
		}

		Set users = group.getUsers();
		SortedSet<User> sortedUsers = new TreeSet<User>(new LastNameAlphabeticComparator());
		sortedUsers.addAll(users);
		String userOutput = buildUserString(sortedUsers);
		writeAJAXResponse(response, userOutput);
		return null;
	}


	/**
	 *  Output format: "userid,lastname,firstname;"
	 * @param sortedUsers
	 * @return String of users
	 */ 
	private String buildUserString(SortedSet<User> sortedUsers) {
		String userOutput = "";
		boolean first = true;
		for ( User user : sortedUsers ) {
			Integer userID = user.getUserId();
			String lastName = user.getLastName();
			String firstName = user.getFirstName();
			if ( ! first ) {
				userOutput=userOutput+";";
			} else {
				first = false;
			}
			userOutput=userOutput+userID+","+lastName+","+firstName;
		}
		return userOutput;
	}
	
	/** 
	 * Add a new group. Designed to respond to an AJAX call.
     *
	 * Input parameters: activityID, name (group name)
	 * 
	 * Output format: no data returned - just the header 
	 */
	public ActionForward addGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
    	String name = WebUtil.readStrParam(request, PARAM_NAME);
		IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
		monitoringService.addGroup(activityID, name);
		writeAJAXResponse(response,"");
		return null;
	}

	/** 
	 * Remove a group. Cannot remove the group if it is in use (tool session ids exist). Designed to respond to an AJAX call.
     *
	 * Input parameters: activityID, name: group name
	 * 
	 * Output format: no data returned - just the header 
	 */
	public ActionForward removeGroup(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
    	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);

    	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
		monitoringService.removeGroup(activityID, groupID);
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
	public ActionForward addMembers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
    	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
    	String members = WebUtil.readStrParam(request, PARAM_MEMBERS, true);
    	if ( members != null ) {
        	String[] membersSplit = members.split(","); 
			IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
			monitoringService.addUsersToGroup(activityID,  groupID, membersSplit);
    	}
		writeAJAXOKResponse(response);
		return null;
	}

	/** 
	 * Remove a list of users from a group. Designed to respond to an AJAX call.
     *
	 * Input parameters: activityID, name: group name, members: comma separated list of users
	 * 
	 * Output format: no data returned - just the header 
	 */
	public ActionForward removeMembers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
    	Long groupID = WebUtil.readLongParam(request, AttributeNames.PARAM_GROUP_ID);
    	String members = WebUtil.readStrParam(request, PARAM_MEMBERS, true);
    	if ( members != null ) {
        	String[] membersSplit = members.split(","); 
	    	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
			monitoringService.removeUsersFromGroup(activityID, groupID, membersSplit);
    	}
		writeAJAXOKResponse(response);
		return null;
	}

}
