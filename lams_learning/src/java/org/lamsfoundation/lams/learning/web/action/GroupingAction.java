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

package org.lamsfoundation.lams.learning.web.action;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.RedirectingActionForward;
import org.lamsfoundation.lams.learning.service.ILearnerFullService;
import org.lamsfoundation.lams.learning.service.LearnerServiceProxy;
import org.lamsfoundation.lams.learning.web.util.ActivityMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearnerChoiceGrouping;
import org.lamsfoundation.lams.learningdesign.dto.GroupDTO;
import org.lamsfoundation.lams.lesson.LearnerProgress;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.usermanagement.dto.UserBasicDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 *
 * <p>
 * The action servlet that triggers the system driven grouping (random grouping) and allows the learner to view the
 * result of the grouping.
 * </p>
 *
 * <p>
 * Has a special override key - if the parameter force is set and the lesson is a preview lesson, any chosen grouping
 * will be overridden.
 * </p>
 *
 * @author Jacky Fang
 */
public class GroupingAction extends LamsDispatchAction {

    /** Input parameter. Boolean value */
    public static final String PARAM_FORCE_GROUPING = "force";

    // ---------------------------------------------------------------------
    // Instance variables
    // ---------------------------------------------------------------------
    private static Logger log = Logger.getLogger(GroupingAction.class);
    // ---------------------------------------------------------------------
    // Class level constants - Session Attributes
    // ---------------------------------------------------------------------
    public static final String GROUPS = "groups";
    public static final String FINISHED_BUTTON = "finishedButton";
    public static final String LOCAL_FILES = "localFiles";
    public static final String TITLE = "title";
    public static final String MAX_LEARNERS_PER_GROUP = "maxLearnersPerGroup";
    public static final String VIEW_STUDENTS_BEFORE_SELECTION = "viewStudentsBeforeSelection";

    // ---------------------------------------------------------------------
    // Class level constants - Struts forward
    // ---------------------------------------------------------------------
    public static final String VIEW_GROUP = "viewGroup";
    public static final String WAIT_GROUP = "waitGroup";
    public static final String SHOW_GROUP = "showGroup";
    public static final String CHOOSE_GROUP = "chooseGroup";

    // ---------------------------------------------------------------------
    // Struts Dispatch Method
    // ---------------------------------------------------------------------
    /**
     * Perform the grouping for the users who are currently running the lesson. If force is set to true, then we should
     * be in preview mode, and we want to override the chosen grouping to make it group straight away.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward performGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	boolean forceGroup = WebUtil.readBooleanParam(request, GroupingAction.PARAM_FORCE_GROUPING, false);

	// initialize service object
	ILearnerFullService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);
	if (!(activity instanceof GroupingActivity)) {
	    GroupingAction.log.error(LamsDispatchAction.className + ": activity not GroupingActivity");
	    return mapping.findForward(ActivityMapping.ERROR);
	}
	Long lessonId = learnerProgress.getLesson().getLessonId();
	boolean groupingDone = learnerService.performGrouping(lessonId, activity.getActivityId(),
		LearningWebUtil.getUserId(), forceGroup);

	DynaActionForm groupForm = (DynaActionForm) form;
	groupForm.set("previewLesson", learnerProgress.getLesson().isPreviewLesson());
	groupForm.set("title", activity.getTitle());
	groupForm.set(AttributeNames.PARAM_ACTIVITY_ID, activity.getActivityId());

	request.setAttribute(AttributeNames.PARAM_LESSON_ID, lessonId);
	if (groupingDone) {
	    request.setAttribute(GroupingAction.FINISHED_BUTTON, Boolean.TRUE);
	    return mapping.findForward(GroupingAction.VIEW_GROUP);
	}
	// forward to group choosing page
	if (((GroupingActivity) activity).getCreateGrouping().isLearnerChoiceGrouping()) {
	    Long groupingId = ((GroupingActivity) activity).getCreateGrouping().getGroupingId();
	    Integer maxNumberOfLeaernersPerGroup = learnerService.calculateMaxNumberOfLearnersPerGroup(lessonId,
		    groupingId);

	    LearnerChoiceGrouping grouping = (LearnerChoiceGrouping) learnerService.getGrouping(groupingId);
	    prepareGroupData(request);
	    request.setAttribute(GroupingAction.MAX_LEARNERS_PER_GROUP, maxNumberOfLeaernersPerGroup);
	    request.setAttribute(GroupingAction.LOCAL_FILES, Boolean.FALSE);
	    request.setAttribute(GroupingAction.VIEW_STUDENTS_BEFORE_SELECTION,
		    grouping.getViewStudentsBeforeSelection());
	    return mapping.findForward(GroupingAction.CHOOSE_GROUP);
	}
	return mapping.findForward(GroupingAction.WAIT_GROUP);
    }

    /**
     * Load up the grouping information and forward to the jsp page to display all the groups and members.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward viewGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	return viewGrouping(mapping, form, request, response, null);
    }

    public ActionForward viewGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, LearnerProgress learnerProgress) throws IOException, ServletException {
	prepareGroupData(request);
	request.setAttribute(GroupingAction.LOCAL_FILES, Boolean.FALSE);
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	request.setAttribute(GroupingAction.FINISHED_BUTTON, new Boolean((mode == null) || !mode.isTeacher()));

	long activityId = WebUtil.readLongParam(request, AttributeNames.PARAM_ACTIVITY_ID);
	LearningWebUtil.putActivityPositionInRequest(activityId, request, getServlet().getServletContext());

	// make sure the lesson id is always in the request for the progress bar.
	if (request.getAttribute(AttributeNames.PARAM_LESSON_ID) == null) {
	    if (learnerProgress == null) {
		ILearnerFullService learnerService = LearnerServiceProxy
			.getLearnerService(getServlet().getServletContext());
		learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	    }
	    request.setAttribute(AttributeNames.PARAM_LESSON_ID, learnerProgress.getLesson().getLessonId());
	}
	return mapping.findForward(GroupingAction.SHOW_GROUP);
    }

    /**
     * Complete the current tool activity and forward to the url of next activity in the learning design.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward completeActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// initialize service object
	ILearnerFullService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	LearnerProgress progress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Activity groupingActivity = LearningWebUtil.getActivityFromRequest(request, learnerService);
	Integer learnerId = LearningWebUtil.getUserId();

	// so manually resume the progress. The completeActivity code can cope with a missing activity.
	return LearningWebUtil.completeActivity(request, response,
		LearningWebUtil.getActivityMapping(this.getServlet().getServletContext()), progress, groupingActivity,
		learnerId, learnerService, true);
    }

    /**
     * Inserts into the request most of the data required by JSP page. This method is common for several pages.
     *
     * @param request
     */
    @SuppressWarnings("unchecked")
    private void prepareGroupData(HttpServletRequest request) {

	ILearnerFullService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());

	SortedSet<GroupDTO> groups = new TreeSet<GroupDTO>(GroupDTO.GROUP_NAME_COMPARATOR);
	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);

	Grouping grouping = ((GroupingActivity) activity).getCreateGrouping();
	if (grouping != null) {
	    for (Group group : grouping.getGroups()) {
		GroupDTO groupDTO = new GroupDTO(group, true);
		groupDTO.getUserList().sort(UserBasicDTO.USER_BASIC_DTO_COMPARATOR);
		groups.add(groupDTO);
	    }
	}

	request.setAttribute(GroupingAction.GROUPS, groups);
	request.setAttribute(GroupingAction.TITLE, activity.getTitle());
	request.setAttribute(AttributeNames.PARAM_ACTIVITY_ID, activity.getActivityId());
    }

    /**
     * Responds to a learner's group choice. Might forward back to group choice page if the chosen group was full.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward learnerChooseGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	ILearnerFullService learnerService = LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
	Activity activity = LearningWebUtil.getActivityFromRequest(request, learnerService);
	Long groupId = WebUtil.readLongParam(request, "groupId");
	LearnerProgress learnerProgress = LearningWebUtil.getLearnerProgress(request, learnerService);
	Long lessonId = learnerProgress.getLesson().getLessonId();
	learnerService.learnerChooseGroup(lessonId, activity.getActivityId(), groupId, LearningWebUtil.getUserId());

	String redirectURL = "/grouping.do";
	redirectURL = WebUtil.appendParameterToURL(redirectURL, "method", "performGrouping");
	redirectURL = WebUtil.appendParameterToURL(redirectURL, "activityID", activity.getActivityId().toString());

	return new RedirectingActionForward(redirectURL);
    }

}