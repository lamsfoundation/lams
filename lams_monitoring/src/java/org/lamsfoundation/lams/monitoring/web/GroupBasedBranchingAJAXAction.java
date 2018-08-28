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
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ActivityTitleComparator;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.lesson.service.LessonServiceException;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * The action servlet that provides the support for the
 * <UL>
 * <LI>AJAX based Groups Based Branching screen</LI>
 * </UL>
 * 
 * @author Fiona Malikoff
 */
public class GroupBasedBranchingAJAXAction extends BranchingAction {

    private static final String GROUPED_SELECTION_SCREEN = "groupedSelection";
    public static final String PARAM_BRANCH_ID = "branchID";
    public static final String PARAM_MAY_DELETE = "mayDelete";
    public static final String PARAM_GROUPS = "groups";

    /**
     * Start the process of doing the group to branch mapping. Used for define later.
     *
     * Input parameters: activityID
     */
    public ActionForward assignBranch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	Long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	IMonitoringFullService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	BranchingActivity activity = (BranchingActivity) monitoringService.getActivityById(activityID,
		BranchingActivity.class);

	if (activity.isGroupBranchingActivity()) {

	    // in general the progress engine expects the activity and lesson id to be in the request,
	    // so follow that standard.
	    request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activityID);
	    request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
	    request.setAttribute(AttributeNames.PARAM_TITLE, activity.getTitle());

	    // can we still move groups? Only allowed if no users have started the branching activity
	    boolean usersStartedBranching = monitoringService.isActivityAttempted(activity);
	    request.setAttribute(PARAM_MAY_DELETE, !usersStartedBranching);

	    return mapping.findForward(GROUPED_SELECTION_SCREEN);

	} else {
	    // something gone wrong - shouldn't be here so revert to standard screen for viewing branching.
	    return viewBranching(activity, lessonId, false, mapping, request, monitoringService);
	}

    }

    /**
     * Get a list of branch names, their associated group id and the number of groups for this branch. Designed to
     * respond to an AJAX call.
     *
     * Input parameters: activityID (which is the branching activity id)
     * 
     * Output format: "branchid,name,num groups;branchid,groupid,name,num groups"
     */
    public ActionForward getBranches(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	// get the branching data and sort it.
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	IMonitoringFullService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	BranchingActivity activity = (BranchingActivity) monitoringService.getActivityById(activityID);

	TreeSet<Activity> sortedBranches = new TreeSet<Activity>(new ActivityTitleComparator());
	sortedBranches.addAll(activity.getActivities());

	// build the output string to return to the chosen branching page.
	// there should only ever be one group for each branch in chosen branching
	String branchesOutput = "";

	boolean first = true;
	for (Activity childActivity : sortedBranches) {

	    SequenceActivity branch = (SequenceActivity) monitoringService
		    .getActivityById(childActivity.getActivityId(), SequenceActivity.class);

	    Long branchId = branch.getActivityId();
	    String name = branch.getTitle();

	    SortedSet<Group> groups = branch.getGroupsForBranch();
	    int numberOfGroups = groups != null ? groups.size() : 0;

	    if (!first) {
		branchesOutput = branchesOutput + ";";
	    } else {
		first = false;
	    }

	    branchesOutput = branchesOutput + branchId + "," + name + "," + numberOfGroups;
	}

	if (log.isDebugEnabled()) {
	    log.debug("getBranches activity id " + activityID + " returning " + branchesOutput);
	}

	writeAJAXResponse(response, branchesOutput);
	return null;
    }

    /**
     * Get a list of all the class members who aren't grouped yet. Designed to respond to an AJAX call.
     *
     * Input parameters: activityID (which is the branching activity id)
     * 
     * Output format: "groupid,groupname;groupid,groupname"
     */
    public ActionForward getGroupsNotAssignedToBranch(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	// get the grouping data and sort it.
	Long activityID = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	IMonitoringFullService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	SortedSet<Group> groups = monitoringService.getGroupsNotAssignedToBranch(activityID);
	String groupOutput = buildGroupString(groups);
	writeAJAXResponse(response, groupOutput);
	return null;

    }

    /**
     * Get a list of groups associated with this branch. Designed to respond to an AJAX call.
     *
     * Input parameters: branchID which is sequence activity id
     * 
     * Output format: "groupid,groupname;"
     */
    public ActionForward getBranchGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	Long branchID = WebUtil.readLongParam(request, PARAM_BRANCH_ID);
	IMonitoringFullService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	SequenceActivity branch = (SequenceActivity) monitoringService.getActivityById(branchID);

	SortedSet<Group> groups = branch.getGroupsForBranch();
	String groupOutput = buildGroupString(groups);

	if (log.isDebugEnabled()) {
	    log.debug("getBranchGroups branch id " + branchID + " returning " + groupOutput);
	}

	writeAJAXResponse(response, groupOutput);
	return null;
    }

    /**
     * @param groups
     * @return
     */
    private String buildGroupString(SortedSet<Group> groups) {
	String groupOutput = "";
	boolean first = true;

	for (Group group : groups) {
	    if (!first) {
		groupOutput = groupOutput + ";";
	    } else {
		first = false;
	    }
	    groupOutput = groupOutput + group.getGroupId() + "," + group.getGroupName();
	}
	return groupOutput;
    }

    /**
     * Add groups to a branch. Designed to respond to an AJAX call.
     *
     * Input parameters: branchID, groups: comma separated list of group ids
     * 
     * Output format: no data returned - just the header
     */
    public ActionForward addGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

	Long branchID = WebUtil.readLongParam(request, PARAM_BRANCH_ID);

	String groups = WebUtil.readStrParam(request, PARAM_GROUPS, true);
	if (groups != null) {
	    String[] groupsSplit = groups.split(",");

	    IMonitoringFullService monitoringService = MonitoringServiceProxy
		    .getMonitoringService(getServlet().getServletContext());
	    monitoringService.addGroupToBranch(branchID, groupsSplit);
	}

	writeAJAXOKResponse(response);
	return null;
    }

    /**
     * Remove a list of users from a group. Designed to respond to an AJAX call.
     *
     * Input parameters: branchID, groups: comma separated list of group ids
     * 
     * Output format: no data returned - just the header
     */
    public ActionForward removeGroups(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, LessonServiceException {

	Long branchID = WebUtil.readLongParam(request, PARAM_BRANCH_ID);

	String groups = WebUtil.readStrParam(request, PARAM_GROUPS, true);
	if (groups != null) {
	    String[] groupsSplit = groups.split(",");

	    IMonitoringFullService monitoringService = MonitoringServiceProxy
		    .getMonitoringService(getServlet().getServletContext());
	    monitoringService.removeGroupFromBranch(branchID, groupsSplit);
	}

	writeAJAXOKResponse(response);
	return null;
    }

}
