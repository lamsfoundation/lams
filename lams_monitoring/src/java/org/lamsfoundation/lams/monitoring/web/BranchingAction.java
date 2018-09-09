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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchActivityEntry;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.monitoring.dto.BranchDTO;
import org.lamsfoundation.lams.monitoring.dto.BranchingDTO;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.monitoring.service.MonitoringServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * The action servlet that provides the support for the
 *
 * <UL>
 * <LI>View only screen for Group Based and Tool Based branching</LI>
 * </UL>
 *
 * Due to how XDoclet works, we have to have a separate subclass of BranchingAction for each branching type, and include
 *
 *
 * @author Fiona Malikoff
 */
public class BranchingAction extends LamsDispatchAction {

    //---------------------------------------------------------------------

    protected static final String VIEW_BRANCHES_SCREEN = "viewBranches";
    protected static final String CHOSEN_SELECTION_SCREEN = "chosenSelection";
    public static final String PARAM_BRANCHING_DTO = "branching";
    public static final String PARAM_SHOW_GROUP_NAME = "showGroupName";
    public static final String PARAM_LOCAL_FILES = "localFiles";
    public static final String PARAM_MAY_DELETE = "mayDelete";
    public static final String PARAM_MODULE_LANGUAGE_XML = "languageXML";
    protected static final String PARAM_VIEW_MODE = "viewMode";

    /**
     * Display the view screen, irrespective of the branching type.
     */
    public ActionForward viewBranching(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);

	IMonitoringFullService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	BranchingActivity activity = (BranchingActivity) monitoringService.getActivityById(activityId,
		BranchingActivity.class);
	return viewBranching(activity, lessonId, false, mapping, request, monitoringService);
    }

    protected ActionForward viewBranching(BranchingActivity activity, Long lessonId, boolean useLocalFiles,
	    ActionMapping mapping, HttpServletRequest request, IMonitoringService monitoringService)
	    throws IOException, ServletException {

	// in general the progress engine expects the activity and lesson id to be in the request,
	// so follow that standard.
	request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activity.getActivityId());
	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
	request.setAttribute(AttributeNames.PARAM_TITLE, activity.getTitle());
	request.setAttribute(PARAM_LOCAL_FILES, useLocalFiles);
	request.setAttribute(PARAM_MODULE_LANGUAGE_XML, getLanguageXML());
	request.setAttribute(PARAM_MAY_DELETE, Boolean.FALSE);
	request.setAttribute(PARAM_VIEW_MODE, Boolean.TRUE);

	// only show the group names if this is a group based branching activity - the names
	// are meaningless for chosen and tool based branching
	BranchingDTO dto = getBranchingDTO(activity, monitoringService);
	request.setAttribute(PARAM_BRANCHING_DTO, dto);
	request.setAttribute(PARAM_SHOW_GROUP_NAME, activity.isGroupBranchingActivity());
	if (log.isDebugEnabled()) {
	    log.debug("viewBranching: Branching activity " + dto);
	}
	return mapping.findForward(CHOSEN_SELECTION_SCREEN);
    }

    // Can't do this in BranchingDTO (although that's where it should be) as we have
    // to get the SequenceActivities via the getActivityById to get around Hibernate
    // not allowing us to cast the cglib classes.
    private BranchingDTO getBranchingDTO(BranchingActivity activity, IMonitoringService monitoringService) {
	BranchingDTO dto = new BranchingDTO();

	dto.setBranchActivityId(activity.getActivityId());
	dto.setBranchActivityName(activity.getTitle());

	TreeSet<BranchDTO> branches = new TreeSet<BranchDTO>();
	Iterator<Activity> iter = activity.getActivities().iterator();
	while (iter.hasNext()) {
	    Activity childActivity = iter.next();
	    SequenceActivity branch = (SequenceActivity) monitoringService
		    .getActivityById(childActivity.getActivityId(), SequenceActivity.class);
	    Set<BranchActivityEntry> mappingEntries = branch.getBranchEntries();

	    // If it is a grouped based or teacher chosen branching, the users will be in groups.
	    // If not get the user based on the progress engine and create a dummy group.
	    // Can't use tool session as sequence activities don't have a tool session!
	    SortedSet<Group> groups = new TreeSet<Group>();
	    if (activity.isChosenBranchingActivity() || activity.isGroupBranchingActivity()) {
		for (BranchActivityEntry entry : mappingEntries) {
		    Group group = entry.getGroup();
		    groups.add(group);
		}
	    } else {
		Group group = new Group();
		if (group.getUsers() == null) {
		    group.setUsers(new HashSet());
		}
		List<User> learners = monitoringService.getLearnersAttemptedOrCompletedActivity(branch);
		group.getUsers().addAll(learners);
		groups.add(group);
	    }
	    branches.add(new BranchDTO(branch, groups));
	}
	dto.setBranches(branches);
	return dto;
    }

    /**
     * @return String of xml with all needed language elements
     */
    protected String getLanguageXML() {
	IMonitoringFullService monitoringService = MonitoringServiceProxy
		.getMonitoringService(getServlet().getServletContext());
	MessageService messageService = monitoringService.getMessageService();
	ArrayList<String> languageCollection = new ArrayList<String>();
	languageCollection.add(new String("button.finished"));
	languageCollection.add(new String("label.branching.non.allocated.users.heading"));
	languageCollection.add(new String("label.grouping.status"));
	languageCollection.add(new String("label.grouping.learners"));
	languageCollection.add(new String("error.title"));
	languageCollection.add(new String("label.branching.popup.drag.selection.message"));

	String languageOutput = "<xml><language>";

	for (int i = 0; i < languageCollection.size(); i++) {
	    languageOutput += "<entry key='" + languageCollection.get(i) + "'><name>"
		    + messageService.getMessage(languageCollection.get(i)) + "</name></entry>";
	}

	languageOutput += "</language></xml>";

	return languageOutput;
    }
}