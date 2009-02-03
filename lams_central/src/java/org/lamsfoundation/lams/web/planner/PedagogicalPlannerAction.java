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
/* $$Id$$ */
package org.lamsfoundation.lams.web.planner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.authoring.dto.PedagogicalPlannerActivityDTO;
import org.lamsfoundation.lams.authoring.dto.PedagogicalPlannerDTO;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearnerChoiceGrouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Action that manages Pedagogical Planner base page.
 * 
 * @author Marcin Cieslak
 * 
 * @struts:action path="/pedagogicalPlanner" scope="request" parameter="method"
 * @struts:action-forward name="success" path="/pedagogical_planner/base.jsp"
 * 
 * @struts:action path="/pedagogicalPlanner/grouping" scope="request" name="GroupingPedagogicalPlannerForm"
 *                validate="false" parameter="method"
 * @struts:action-forward name="grouping" path="/pedagogical_planner/grouping.jsp"
 * @struts:action-forward name="preview" path="/home.do?method=learner"
 */
public class PedagogicalPlannerAction extends LamsDispatchAction {

    private static final String CHAR_QUESTION_MARK = "?";
    private static final char CHAR_AMPERSAND = '&';
    private static final char CHAR_EQUALS = '=';
    private static IUserManagementService userManagementService;
    private static IExportToolContentService exportService;
    private static IAuthoringService authoringService;
    private static IMonitoringService monitoringService;
    private static MessageService messageService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return start(mapping, form, request, response);
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// Get the learning design template zip file
	String designFilePath = "pedagogical_planner" + File.separator + "1" + File.separator + "1" + File.separator
		+ "1";
	String contentRepositoryPath = Configuration.get("ContentRepositoryPath");
	File templateDirectory = new File(contentRepositoryPath, designFilePath);
	File designFile = new File(templateDirectory, CentralConstants.TEMPLATE_FILE_NAME);
	if (!designFile.exists() || designFile.isDirectory()) {
	    throw new IOException("The sequence template does not exist or is not a proper file.");
	}
	HttpSession session = SessionManager.getSession();
	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	User user = (User) getUserManagementService().findById(User.class, userDto.getUserID());
	if (user == null) {
	    throw new ServletException("User not found.");
	}
	List<String> toolsErrorMsgs = new ArrayList<String>();
	Long learningDesignID = null;
	LearningDesign learningDesign = null;
	List<String> learningDesignErrorMsgs = new ArrayList<String>();
	// Extract the template
	try {
	    Object[] ldResults = getExportService().importLearningDesign(designFile, user, null, toolsErrorMsgs, "");
	    learningDesignID = (Long) ldResults[0];
	    learningDesignErrorMsgs = (List<String>) ldResults[1];
	    toolsErrorMsgs = (List<String>) ldResults[2];
	    learningDesign = getAuthoringService().getLearningDesign(learningDesignID);
	} catch (ImportToolContentException e) {
	    throw new ServletException(e.getMessage());
	}
	if ((learningDesignID == null || learningDesignID.longValue() == -1) && learningDesignErrorMsgs.size() == 0) {
	    throw new ServletException("Learning design could not be retrieved.");
	}
	if (learningDesignErrorMsgs.size() > 0) {
	    throw new ServletException("There were learning design errors.");
	}
	if (toolsErrorMsgs.size() > 0) {
	    throw new ServletException("There were tool errors.");
	}
	List<PedagogicalPlannerActivityDTO> activities = new ArrayList<PedagogicalPlannerActivityDTO>();
	// create DTO that holds all the necessary information of the activities

	Activity activity = learningDesign.getFirstActivity();
	while (activity != null) {
	    addActivityToPlanner(learningDesign, activities, activity);
	    Transition transitionTo = activity.getTransitionTo();
	    if (transitionTo == null) {
		activity = null;
	    } else {
		activity = transitionTo.getToActivity();
	    }
	}
	int activitySupportingPlannerCount = 0;
	for (PedagogicalPlannerActivityDTO activityDTO : activities) {
	    if (activityDTO.getSupportsPlanner()) {
		activitySupportingPlannerCount++;
	    }
	}
	// Set other properties
	PedagogicalPlannerDTO planner = new PedagogicalPlannerDTO();
	planner.setActivitySupportingPlannerCount(activitySupportingPlannerCount);
	planner.setSequenceTitle(learningDesign.getTitle());
	planner.setActivities(activities);
	planner.setSendInPortions(false);
	planner.setSubmitDelay(5000L);
	planner.setActivitiesInPortion(2);
	planner.setLearningDesignID(learningDesignID);

	request.setAttribute(CentralConstants.ATTR_PLANNER, planner);
	return mapping.findForward("success");
    }

    private IExportToolContentService getExportService() {
	if (PedagogicalPlannerAction.exportService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.exportService = (IExportToolContentService) ctx
		    .getBean("exportToolContentService");
	}
	return PedagogicalPlannerAction.exportService;
    }

    private IAuthoringService getAuthoringService() {
	if (PedagogicalPlannerAction.authoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.authoringService = (IAuthoringService) ctx.getBean("authoringService");
	}
	return PedagogicalPlannerAction.authoringService;
    }

    private IMonitoringService getMonitoringService() {
	if (PedagogicalPlannerAction.monitoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.monitoringService = (IMonitoringService) ctx.getBean("monitoringService");
	}
	return PedagogicalPlannerAction.monitoringService;
    }

    private IUserManagementService getUserManagementService() {
	if (PedagogicalPlannerAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.userManagementService = (IUserManagementService) ctx
		    .getBean("userManagementService");
	}
	return PedagogicalPlannerAction.userManagementService;
    }

    private MessageService getMessageService() {
	if (PedagogicalPlannerAction.messageService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.messageService = (MessageService) ctx.getBean("centralMessageService");
	}
	return PedagogicalPlannerAction.messageService;
    }

    public ActionForward saveSequenceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String sequenceTitle = WebUtil.readStrParam(request, CentralConstants.PARAM_SEQUENCE_TITLE, true);
	Long learningDesignID = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	Integer callAttemptedID = WebUtil.readIntParam(request, CentralConstants.PARAM_CALL_ATTEMPTED_ID);
	String responseSuffix = "&" + callAttemptedID;

	if (StringUtils.isEmpty(sequenceTitle)) {
	    String blankTitleError = getMessageService().getMessage(CentralConstants.ERROR_PLANNER_TITLE_BLANK);
	    writeAJAXResponse(response, blankTitleError + responseSuffix);
	} else {
	    LearningDesign learningDesign = getAuthoringService().getLearningDesign(learningDesignID);
	    learningDesign.setTitle(sequenceTitle);
	    getAuthoringService().saveLearningDesign(learningDesign);
	    writeAJAXResponse(response, "OK" + responseSuffix);
	}
	return null;
    }

    public ActionForward initGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	GroupingPedagogicalPlannerForm plannerForm = (GroupingPedagogicalPlannerForm) form;
	Long groupingId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Grouping grouping = getAuthoringService().getGroupingById(groupingId);
	plannerForm.fillForm(grouping);
	return mapping.findForward("grouping");
    }

    public ActionForward saveOrUpdateGroupingForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	GroupingPedagogicalPlannerForm plannerForm = (GroupingPedagogicalPlannerForm) form;
	ActionMessages errors = plannerForm.validate();
	if (errors.isEmpty()) {
	    Grouping grouping = getAuthoringService().getGroupingById(plannerForm.getToolContentID());
	    if (grouping.isRandomGrouping()) {
		RandomGrouping randomGrouping = (RandomGrouping) grouping;
		Integer number = StringUtils.isEmpty(plannerForm.getNumberOfGroups()) ? null : Integer
			.parseInt(plannerForm.getNumberOfGroups());
		randomGrouping.setNumberOfGroups(number);

		number = StringUtils.isEmpty(plannerForm.getLearnersPerGroup()) ? null : Integer.parseInt(plannerForm
			.getLearnersPerGroup());
		randomGrouping.setLearnersPerGroup(number);
	    } else if (grouping.isLearnerChoiceGrouping()) {
		LearnerChoiceGrouping learnerChoiceGrouping = (LearnerChoiceGrouping) grouping;
		Integer number = StringUtils.isEmpty(plannerForm.getNumberOfGroups()) ? null : Integer
			.parseInt(plannerForm.getNumberOfGroups());
		learnerChoiceGrouping.setNumberOfGroups(number);

		number = StringUtils.isEmpty(plannerForm.getLearnersPerGroup()) ? null : Integer.parseInt(plannerForm
			.getLearnersPerGroup());
		learnerChoiceGrouping.setLearnersPerGroup(number);
		learnerChoiceGrouping.setEqualNumberOfLearnersPerGroup(plannerForm.getEqualGroupSizes());
	    } else {
		Integer number = StringUtils.isEmpty(plannerForm.getNumberOfGroups()) ? null : Integer
			.parseInt(plannerForm.getNumberOfGroups());
		grouping.setMaxNumberOfGroups(number);
	    }
	} else {
	    saveMessages(request, errors);
	}
	return mapping.findForward("grouping");
    }

    public ActionForward startPreview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long learningDesignID = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	HttpSession session = SessionManager.getSession();
	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	Lesson lesson = getMonitoringService().initializeLessonForPreview("Preview", null, learningDesignID,
		userDto.getUserID(), null, false, false, false);
	getMonitoringService().createPreviewClassForLesson(userDto.getUserID(), lesson.getLessonId());

	getMonitoringService().startLesson(lesson.getLessonId(), userDto.getUserID());
	String newPath = mapping.findForward("preview").getPath();
	newPath = newPath + PedagogicalPlannerAction.CHAR_AMPERSAND + AttributeNames.PARAM_LESSON_ID
		+ PedagogicalPlannerAction.CHAR_EQUALS + lesson.getLessonId();
	return new ActionForward(newPath, true);
    }

    private PedagogicalPlannerActivityDTO addActivityToPlanner(LearningDesign learningDesign,
	    List<PedagogicalPlannerActivityDTO> activities, Activity activity) throws ServletException {
	boolean isNested = activity.getParentActivity() != null
		&& (activity.getParentActivity().isBranchingActivity() || activity.isOptionsActivity());

	PedagogicalPlannerActivityDTO addedDTO = null;
	if (activity.isToolActivity()) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    String pedagogicalPlannerUrl = toolActivity.getTool().getPedagogicalPlannerUrl();
	    if (pedagogicalPlannerUrl == null) {
		addedDTO = new PedagogicalPlannerActivityDTO(toolActivity.getTool().getToolDisplayName(), activity
			.getTitle(), false, CentralConstants.PATH_ACTIVITY_NO_PLANNER_SUPPORT, activity
			.getLibraryActivityUiImage(), null, null);
	    } else {
		pedagogicalPlannerUrl += pedagogicalPlannerUrl.contains(PedagogicalPlannerAction.CHAR_QUESTION_MARK) ? PedagogicalPlannerAction.CHAR_AMPERSAND
			: PedagogicalPlannerAction.CHAR_QUESTION_MARK;
		pedagogicalPlannerUrl += AttributeNames.PARAM_TOOL_CONTENT_ID + PedagogicalPlannerAction.CHAR_EQUALS
			+ toolActivity.getToolContentId();
		addedDTO = new PedagogicalPlannerActivityDTO(toolActivity.getTool().getToolDisplayName(), activity
			.getTitle(), true, pedagogicalPlannerUrl + PedagogicalPlannerAction.CHAR_AMPERSAND
			+ AttributeNames.PARAM_CONTENT_FOLDER_ID + PedagogicalPlannerAction.CHAR_EQUALS
			+ learningDesign.getContentFolderID(), activity.getLibraryActivityUiImage(),
			pedagogicalPlannerUrl + PedagogicalPlannerAction.CHAR_AMPERSAND + AttributeNames.PARAM_COMMAND
				+ PedagogicalPlannerAction.CHAR_EQUALS + AttributeNames.COMMAND_CHECK_EDITING_ADVICE
				+ PedagogicalPlannerAction.CHAR_AMPERSAND + AttributeNames.PARAM_ACTIVITY_INDEX
				+ PedagogicalPlannerAction.CHAR_EQUALS + (activities.size() + 1), pedagogicalPlannerUrl
				+ PedagogicalPlannerAction.CHAR_AMPERSAND + AttributeNames.PARAM_COMMAND
				+ PedagogicalPlannerAction.CHAR_EQUALS + AttributeNames.COMMAND_GET_EDITING_ADVICE);
	    }
	    activities.add(addedDTO);
	} else if (activity.isGroupingActivity()) {
	    GroupingActivity groupingActivity = (GroupingActivity) activity;
	    addedDTO = new PedagogicalPlannerActivityDTO(null, activity.getTitle(), true, groupingActivity
		    .getSystemTool().getPedagogicalPlannerUrl()
		    + PedagogicalPlannerAction.CHAR_AMPERSAND
		    + AttributeNames.PARAM_TOOL_CONTENT_ID
		    + PedagogicalPlannerAction.CHAR_EQUALS + groupingActivity.getCreateGrouping().getGroupingId(),
		    CentralConstants.IMAGE_PATH_GROUPING, null, null);
	    activities.add(addedDTO);
	} else if (activity.isGateActivity()) {
	    addedDTO = new PedagogicalPlannerActivityDTO(null, activity.getTitle(), false,
		    CentralConstants.PATH_ACTIVITY_NO_PLANNER_SUPPORT, CentralConstants.IMAGE_PATH_GATE, null, null);
	    activities.add(addedDTO);
	} else if (activity.isBranchingActivity()) {
	    if (isNested) {
		throw new ServletException(
			"Nested branching or optional activities are not allowed in Pedagogical Planner.");
	    }
	    BranchingActivity branchingActivity = (BranchingActivity) activity;
	    SequenceActivity defaultSequence = (SequenceActivity) branchingActivity.getDefaultActivity();
	    Set<SequenceActivity> sequenceActivities = branchingActivity.getActivities();
	    short branch = 1;
	    for (SequenceActivity sequenceActivity : sequenceActivities) {
		if (branch > 4) {
		    throw new ServletException(
			    "Number of branches in branching activity is limited to 4 in Pedagogical Planner.");
		}
		Activity nestedActivity = sequenceActivity.getDefaultActivity();
		boolean defaultBranch = sequenceActivity.equals(defaultSequence);
		if (nestedActivity == null) {
		    String path = CentralConstants.PATH_ACTIVITY_NO_PLANNER_SUPPORT
			    + PedagogicalPlannerAction.CHAR_QUESTION_MARK + CentralConstants.PARAM_FORM_MESSAGE
			    + PedagogicalPlannerAction.CHAR_EQUALS
			    + getMessageService().getMessage(CentralConstants.RESOURCE_KEY_BRANCH_EMPTY);
		    addedDTO = new PedagogicalPlannerActivityDTO(null, null, false, path, null, null, null);
		    addedDTO.setParentActivityTitle(activity.getTitle());
		    addedDTO.setBranch(branch);
		    addedDTO.setDefaultBranch(defaultBranch);
		    activities.add(addedDTO);
		} else {
		    do {
			addedDTO = addActivityToPlanner(learningDesign, activities, nestedActivity);
			Transition transitionTo = nestedActivity.getTransitionTo();
			if (transitionTo == null) {
			    nestedActivity = null;
			} else {
			    nestedActivity = transitionTo.getToActivity();
			}
			addedDTO.setParentActivityTitle(activity.getTitle());
			addedDTO.setBranch(branch);
			addedDTO.setDefaultBranch(defaultBranch);
		    } while (nestedActivity != null);
		}
		branch++;
	    }
	    addedDTO.setLastNestedActivity(true);
	} else {
	    addedDTO = new PedagogicalPlannerActivityDTO(null, activity.getTitle(), false,
		    CentralConstants.PATH_ACTIVITY_NO_PLANNER_SUPPORT, activity.getLibraryActivityUiImage(), null, null);
	    activities.add(addedDTO);
	}
	return addedDTO;
    }
}