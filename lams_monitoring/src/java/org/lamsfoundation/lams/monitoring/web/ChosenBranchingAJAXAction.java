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
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityTitleComparator;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.GroupBranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.lesson.service.LessonServiceException;
import org.lamsfoundation.lams.monitoring.BranchingDTO;
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
* <LI>AJAX based Chosen Branching screen</LI>
* <LI>View only screen for Group Based and Tool Based branching</LI>
* <LI>Class export portfolio page for all branching types
* </UL>
* 
* @author Fiona Malikoff

* ----------------XDoclet Tags--------------------
* 
* @struts:action path="/branching" 
*                parameter="method" 
*                validate="false"
* @struts.action-forward name = "chosenSelection" path = "/branching/chosenSelection.jsp"
* @struts.action-forward name = "viewBranches" path = ".viewBranches"
* 
* ----------------XDoclet Tags--------------------
*/
public class ChosenBranchingAJAXAction extends LamsDispatchAction {

    //---------------------------------------------------------------------

	private static final String CHOSEN_SELECTION_SCREEN = "chosenSelection";
	private static final String VIEW_BRANCHES_SCREEN = "viewBranches";
	public static final String PARAM_TYPE = "type";
	public static final String PARAM_BRANCHING_DTO = "branching";
	public static final String PARAM_BRANCH_ID = "branchID";
	public static final String PARAM_MAY_DELETE = "mayDelete";
	public static final String PARAM_MEMBERS = "members";
	public static final String PARAM_SHOW_GROUP_NAME = "showGroupName";
	/** If localFiles = true will be written to a local file for export portfolio */
	public static final String PARAM_LOCAL_FILES= "localFiles"; 
	
	private void writeAJAXResponse(HttpServletResponse response, String output) throws IOException {
		response.setContentType("text/html");
	    PrintWriter writer = response.getWriter();
	    // set it to unicode
		if (output != null && output.length()>0) {
	        writer.println(output);
		}
	}

	private void writeAJAXOKResponse(HttpServletResponse response) throws IOException {
		writeAJAXResponse(response, "OK");
	}

	/** 
	 * Start the process of doing the chosen grouping
     *
	 * Input parameters: activityID
	 */
	public ActionForward assignBranch(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
        Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

        IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Activity activity = monitoringService.getActivityById(activityID);
    	if ( activity == null ||  !activity.isBranchingActivity() ) {
			String error = "Activity is not a branching activity. Activity was "+activity; 
			log.error(error);
			throw new MonitoringServiceException(error);
    	}

	
		if ( activity.isChosenBranchingActivity() ) {
			
	    	// in general the progress engine expects the activity and lesson id to be in the request,
	    	// so follow that standard.
			request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activityID);
			request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
			request.setAttribute(AttributeNames.PARAM_TITLE, activity.getTitle());

			// can we still move users? check each group for tool sessions.
			Iterator iter = ((BranchingActivity)activity).getActivities().iterator();
			boolean mayMoveUser = true;
			while (iter.hasNext()) {
				SequenceActivity branch = (SequenceActivity) iter.next();
				Set<GroupBranchActivityEntry> mappingEntries = branch.getBranchEntries();
				for ( GroupBranchActivityEntry entry : mappingEntries ) {
					mayMoveUser = mayMoveUser && entry.getGroup().mayBeDeleted();
				}
			}
			request.setAttribute(PARAM_MAY_DELETE, mayMoveUser);

			return mapping.findForward(CHOSEN_SELECTION_SCREEN);
			
		} else {
			// go to a view only screen for group based and tool based grouping
			return viewBranching((BranchingActivity) activity, lessonId, false, mapping, request);
		}
    	
	}
    
   /**
     * Export Portfolio Page
     */
    public ActionForward exportPortfolio(ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws IOException,
                                                                          ServletException
    {
        long lessonId = WebUtil.readLongParam(request,AttributeNames.PARAM_LESSON_ID);
        long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

        IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
    	Activity activity = monitoringService.getActivityById(activityId);
    	if ( activity == null ||  !activity.isBranchingActivity() ) {
			String error = "Activity is not a branching activity. Activity was "+activity; 
			log.error(error);
			throw new MonitoringServiceException(error);
    	}

    	return viewBranching((BranchingActivity) activity, lessonId, true, mapping, request);
    }


	/** 
	 * Display the view screen, irrespective of the branching type.
     *
	 * Input parameters: activityID
	 */
	private ActionForward viewBranching(BranchingActivity activity, Long lessonId, boolean useLocalFiles, 
			ActionMapping mapping, HttpServletRequest request) throws IOException, ServletException {

    	// in general the progress engine expects the activity and lesson id to be in the request,
    	// so follow that standard.
		request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activity.getActivityId());
		request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
		request.setAttribute(AttributeNames.PARAM_TITLE, activity.getTitle());
		request.setAttribute(PARAM_LOCAL_FILES, useLocalFiles);
	    	
		// only show the group names if this is a group based branching activity - the names
		// are meaningless for chosen and tool based branching
		BranchingDTO dto = new BranchingDTO((BranchingActivity) activity);
		request.setAttribute(PARAM_BRANCHING_DTO, dto);
		request.setAttribute(PARAM_SHOW_GROUP_NAME, activity.isGroupBranchingActivity());
		if ( log.isDebugEnabled() ) {
			log.debug("viewBranching: Branching activity "+dto);
		}
		return mapping.findForward(VIEW_BRANCHES_SCREEN);
	}
    	
	/** 
	 * Get a list of branch names, their associated group id and the number of users in the group. Designed to respond to an AJAX call.
     *
	 * Input parameters: activityID (which is the branching activity id)
	 * 
	 * Output format: "branchid,name,num users;branchid,groupid,name,num users" 
	 */
	public ActionForward getBranches(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		// get the branching data and sort it.
    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
		IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
		BranchingActivity activity = (BranchingActivity) monitoringService.getActivityById(activityID);
		
		TreeSet<SequenceActivity> sortedBranches = new TreeSet<SequenceActivity>(new ActivityTitleComparator());
		sortedBranches.addAll(activity.getActivities());

		// build the output string to return to the chosen branching page.
		// there should only ever be one group for each branch in chosen branching
		String branchesOutput = "";

		boolean first = true;
		for ( SequenceActivity branch : sortedBranches ) {
			Long branchId = branch.getActivityId();
			String name = branch.getTitle();
			int numberOfMembers = 0;

			Group group = branch.getSoleGroupForBranch();
			if ( group != null )
				numberOfMembers = group.getUsers().size();

			if ( ! first ) {
				branchesOutput=branchesOutput+";";
			} else {
				first = false;
			}

			branchesOutput=branchesOutput+branchId+","+name+","+numberOfMembers;
		}

		if ( log.isDebugEnabled() ) {
			log.debug("getBranches activity id "+activityID+" returning "+branchesOutput);
		}
		
		writeAJAXResponse(response, branchesOutput);
		return null;
	}

	/** 
	 * Get a list of all the class members who aren't grouped yet. Designed to respond to an AJAX call.
     *
	 * Input parameters: activityID (which is the branching activity id) 
	 * 
	 * Output format: "userid,lastname,firstname;userid,lastname,firstname;" 
	 */
	public ActionForward getClassMembersNotGrouped(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		// get the grouping data and sort it.
    	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
		Long lessonID = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
		IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
		SortedSet<User> users = monitoringService.getClassMembersNotGrouped(lessonID, activityID, false);
		String groupOutput = buildUserString(users);
		writeAJAXResponse(response, groupOutput);
		return null;
		
	}
	
	/** 
	 * Get a list of group names and the number of users in each group. Designed to respond to an AJAX call.
     *
	 * Input parameters: branchID which is sequence activity id
	 * 
	 * Output format: "userid,lastname,firstname;" 
	 */
	public ActionForward getBranchMembers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

    	Long branchID = WebUtil.readLongParam(request, PARAM_BRANCH_ID);
		IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
		SequenceActivity branch = (SequenceActivity) monitoringService.getActivityById(branchID);

		Group group = branch.getSoleGroupForBranch();

		String userOutput = null;
		if ( group != null ) {
			SortedSet<User> sortedUsers = new TreeSet<User>(new LastNameAlphabeticComparator());
			sortedUsers.addAll(group.getUsers());
			userOutput = buildUserString(sortedUsers);
		}
		
		if ( log.isDebugEnabled() ) {
			log.debug("getBranchMembers branch id "+branchID+" returning "+userOutput);
		}

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
	 * Add learners to a group. Designed to respond to an AJAX call.
     *
	 * Input parameters: branchID, name: group name, members: comma separated list of users 
	 * 
	 * Output format: no data returned - just the header 
	 */
	public ActionForward addMembers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

    	Long branchID = WebUtil.readLongParam(request, PARAM_BRANCH_ID);

    	String members = WebUtil.readStrParam(request, PARAM_MEMBERS, true);
    	if ( members != null ) {
        	String[] membersSplit = members.split(","); 

        	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
			monitoringService.addUsersToBranch(branchID, membersSplit);
    	}

    	writeAJAXOKResponse(response);
		return null;
	}

	/** 
	 * Remove a list of users from a group. Designed to respond to an AJAX call.
     *
	 * Input parameters: branchID, members: comma separated list of users
	 * 
	 * Output format: no data returned - just the header 
	 */
	public ActionForward removeMembers(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

    	Long branchID = WebUtil.readLongParam(request, PARAM_BRANCH_ID);

    	String members = WebUtil.readStrParam(request, PARAM_MEMBERS, true);
    	if ( members != null ) {
        	String[] membersSplit = members.split(","); 

        	IMonitoringService monitoringService = MonitoringServiceProxy.getMonitoringService(getServlet().getServletContext());
			monitoringService.removeUsersFromBranch(branchID, membersSplit);
    	}

		writeAJAXOKResponse(response);
		return null;
	}

 }
