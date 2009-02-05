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
import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
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
 * Action managing Pedagogical Planner base page and non-tool activities.
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

    // Several chars and strings used for building HTML requests
    private static final String CHAR_QUESTION_MARK = "?";
    private static final String CHAR_AMPERSAND = "&";
    private static final char CHAR_EQUALS = '=';
    private static final String STRING_OK = "OK";

    // Services used in the class, injected by Spring
    private static IUserManagementService userManagementService;
    private static IExportToolContentService exportService;
    private static IAuthoringService authoringService;
    private static IMonitoringService monitoringService;
    private static MessageService messageService;

    // Error messages used in class
    private static final String ERROR_TOOL_ERRORS = "There were tool errors.";
    private static final String ERROR_LEARNING_DESIGN_ERRORS = "There were learning design errors.";
    private static final String ERROR_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED = "Learning design could not be retrieved.";
    private static final String ERROR_USER_NOT_FOUND = "User not found.";
    private static final String ERROR_NOT_PROPER_FILE = "The sequence template does not exist or is not a proper file.";
    private static final String ERROR_TOO_MANY_OPTIONS = "Number of options in options activity is limited to "
	    + CentralConstants.PLANNER_MAX_OPTIONS + " in Pedagogical Planner.";
    private static final String ERROR_NESTED_OPTIONS = "Nested optional activities are not allowed in Pedagogical Planner.";
    private static final String ERROR_NESTED_BRANCHING = "Nested branching activities are not allowed in Pedagogical Planner.";
    private static final String ERROR_TOO_MANY_BRANCHES = "Number of branches in branching activity is limited to "
	    + CentralConstants.PLANNER_MAX_BRANCHES + " in Pedagogical Planner.";
    private static Logger log = Logger.getLogger(PedagogicalPlannerAction.class);

    @Override
    /**
     * Go straight to start().
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return start(mapping, form, request, response);
    }

    /**
     * The main method for opening and parsing template (chosen learning desing).
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	// Get the learning design template zip file
	String designFilePath = "pedagogical_planner" + File.separator + "1" + File.separator + "1" + File.separator
		+ "1";
	String contentRepositoryPath = Configuration.get("ContentRepositoryPath");
	File templateDirectory = new File(contentRepositoryPath, designFilePath);
	File designFile = new File(templateDirectory, CentralConstants.TEMPLATE_FILE_NAME);
	if (!designFile.exists() || designFile.isDirectory()) {
	    throw new IOException(PedagogicalPlannerAction.ERROR_NOT_PROPER_FILE);
	}
	HttpSession session = SessionManager.getSession();
	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	User user = (User) getUserManagementService().findById(User.class, userDto.getUserID());
	if (user == null) {
	    throw new ServletException(PedagogicalPlannerAction.ERROR_USER_NOT_FOUND);
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
	    throw new ServletException(PedagogicalPlannerAction.ERROR_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED);
	}
	if (learningDesignErrorMsgs.size() > 0) {
	    for (String error : learningDesignErrorMsgs) {
		PedagogicalPlannerAction.log.error(error);
	    }
	    throw new ServletException(PedagogicalPlannerAction.ERROR_LEARNING_DESIGN_ERRORS);
	}
	if (toolsErrorMsgs.size() > 0) {
	    for (String error : toolsErrorMsgs) {
		PedagogicalPlannerAction.log.error(error);
	    }
	    throw new ServletException(PedagogicalPlannerAction.ERROR_TOOL_ERRORS);
	}

	List<PedagogicalPlannerActivityDTO> activities = new ArrayList<PedagogicalPlannerActivityDTO>();

	// create DTOs that hold all the necessary information of the activities
	Activity activity = learningDesign.getFirstActivity();
	while (activity != null) {

	    // Iterate through all the activities
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
	// create DTO for the whole design
	PedagogicalPlannerDTO planner = new PedagogicalPlannerDTO();
	planner.setActivitySupportingPlannerCount(activitySupportingPlannerCount);
	planner.setSequenceTitle(learningDesign.getTitle());
	planner.setActivities(activities);
	planner.setLearningDesignID(learningDesignID);

	// Some additional options for submitting activity forms; should be moved to configuration file in the future
	planner.setSendInPortions(false);
	planner.setSubmitDelay(5000L);
	planner.setActivitiesInPortion(2);

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

    /**
     * Saves additional, non tool bound template details - currently only the title.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    public ActionForward saveSequenceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	String sequenceTitle = WebUtil.readStrParam(request, CentralConstants.PARAM_SEQUENCE_TITLE, true);
	Long learningDesignID = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	Integer callAttemptedID = WebUtil.readIntParam(request, CentralConstants.PARAM_CALL_ATTEMPTED_ID);
	// We send a message in format "<response>&<call ID>"; it is then parsed in JavaScript
	String responseSuffix = PedagogicalPlannerAction.CHAR_AMPERSAND + callAttemptedID;

	if (StringUtils.isEmpty(sequenceTitle)) {
	    String blankTitleError = getMessageService().getMessage(CentralConstants.ERROR_PLANNER_TITLE_BLANK);
	    writeAJAXResponse(response, blankTitleError + responseSuffix);
	} else {
	    LearningDesign learningDesign = getAuthoringService().getLearningDesign(learningDesignID);
	    learningDesign.setTitle(sequenceTitle);
	    getAuthoringService().saveLearningDesign(learningDesign);
	    writeAJAXResponse(response, PedagogicalPlannerAction.STRING_OK + responseSuffix);
	}
	return null;
    }

    public ActionForward initGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	GroupingPedagogicalPlannerForm plannerForm = (GroupingPedagogicalPlannerForm) form;
	Long groupingId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Grouping grouping = getAuthoringService().getGroupingById(groupingId);
	plannerForm.fillForm(grouping);
	return mapping.findForward(CentralConstants.FORWARD_GROUPING);
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
	return mapping.findForward(CentralConstants.FORWARD_GROUPING);
    }

    public ActionForward startPreview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long learningDesignID = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	HttpSession session = SessionManager.getSession();
	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);

	// Start preview the same way as in authoring
	Lesson lesson = getMonitoringService().initializeLessonForPreview("Preview", null, learningDesignID,
		userDto.getUserID(), null, false, false, false);
	getMonitoringService().createPreviewClassForLesson(userDto.getUserID(), lesson.getLessonId());

	getMonitoringService().startLesson(lesson.getLessonId(), userDto.getUserID());
	String newPath = mapping.findForward("preview").getPath();
	newPath = newPath + PedagogicalPlannerAction.CHAR_AMPERSAND + AttributeNames.PARAM_LESSON_ID
		+ PedagogicalPlannerAction.CHAR_EQUALS + lesson.getLessonId();
	return new ActionForward(newPath, true);
    }

    /**
     * Recognises activitiy type and creates proper DTO for web pages use. For branching and options it can be called
     * recursevely.
     * 
     * @param learningDesign
     *                learning design from which activity was taken
     * @param activities
     *                set of DTOs
     * @param activity
     *                currently parsed activity
     * @return created DTO
     * @throws ServletException
     */
    private PedagogicalPlannerActivityDTO addActivityToPlanner(LearningDesign learningDesign,
	    List<PedagogicalPlannerActivityDTO> activities, Activity activity) throws ServletException {
	// Check if the activity is contained in some complex activity: branching or options
	boolean isNested = activity.getParentActivity() != null
		&& (activity.getParentActivity().isBranchingActivity() || activity.isOptionsActivity());

	PedagogicalPlannerActivityDTO addedDTO = null;
	// Simple tool activity
	if (activity.isToolActivity()) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    // Every tool has an URL that leads to Action that returns proper tool form to use
	    String pedagogicalPlannerUrl = toolActivity.getTool().getPedagogicalPlannerUrl();
	    if (pedagogicalPlannerUrl == null) {
		// if there is no URL, the tool does not support the planner
		addedDTO = new PedagogicalPlannerActivityDTO(toolActivity.getTool().getToolDisplayName(), activity
			.getTitle(), false, CentralConstants.PATH_ACTIVITY_NO_PLANNER_SUPPORT, activity
			.getLibraryActivityUiImage(), null, null);
	    } else {
		// add some required parameters
		pedagogicalPlannerUrl += pedagogicalPlannerUrl.contains(PedagogicalPlannerAction.CHAR_QUESTION_MARK) ? PedagogicalPlannerAction.CHAR_AMPERSAND
			: PedagogicalPlannerAction.CHAR_QUESTION_MARK;
		pedagogicalPlannerUrl += AttributeNames.PARAM_TOOL_CONTENT_ID + PedagogicalPlannerAction.CHAR_EQUALS
			+ toolActivity.getToolContentId();
		// Looks heavy, but we just build URLs for DTO - see that class the meaning of constructor parameters
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
	    // grouping is managed by this action class;
	    GroupingActivity groupingActivity = (GroupingActivity) activity;
	    addedDTO = new PedagogicalPlannerActivityDTO(null, activity.getTitle(), true, groupingActivity
		    .getSystemTool().getPedagogicalPlannerUrl()
		    + PedagogicalPlannerAction.CHAR_AMPERSAND
		    + AttributeNames.PARAM_TOOL_CONTENT_ID
		    + PedagogicalPlannerAction.CHAR_EQUALS + groupingActivity.getCreateGrouping().getGroupingId(),
		    CentralConstants.IMAGE_PATH_GROUPING, null, null);
	    activities.add(addedDTO);
	} else if (activity.isGateActivity()) {
	    // gate is not supported, but takes its image from a differen spot
	    addedDTO = new PedagogicalPlannerActivityDTO(null, activity.getTitle(), false,
		    CentralConstants.PATH_ACTIVITY_NO_PLANNER_SUPPORT, CentralConstants.IMAGE_PATH_GATE, null, null);
	    activities.add(addedDTO);
	} else if (activity.isBranchingActivity()) {
	    // Planner does not support branching inside branching/options
	    if (isNested) {
		throw new ServletException(PedagogicalPlannerAction.ERROR_NESTED_BRANCHING);
	    }
	    BranchingActivity branchingActivity = (BranchingActivity) activity;
	    SequenceActivity defaultSequence = (SequenceActivity) branchingActivity.getDefaultActivity();
	    Set<SequenceActivity> sequenceActivities = branchingActivity.getActivities();
	    short branch = 1;
	    for (SequenceActivity sequenceActivity : sequenceActivities) {
		// Currently Planner supports only 4 branches, but there is no logical reason for that; just add colours
		// in CSS and change this value for additional branches
		if (branch > CentralConstants.PLANNER_MAX_BRANCHES) {
		    throw new ServletException(PedagogicalPlannerAction.ERROR_TOO_MANY_BRANCHES);
		}
		Activity nestedActivity = sequenceActivity.getDefaultActivity();
		boolean defaultBranch = sequenceActivity.equals(defaultSequence);

		if (nestedActivity == null) {
		    // Empty sequence
		    String path = CentralConstants.PATH_ACTIVITY_NO_PLANNER_SUPPORT
			    + PedagogicalPlannerAction.CHAR_QUESTION_MARK + CentralConstants.PARAM_FORM_MESSAGE
			    + PedagogicalPlannerAction.CHAR_EQUALS
			    + getMessageService().getMessage(CentralConstants.RESOURCE_KEY_BRANCH_EMPTY);
		    addedDTO = new PedagogicalPlannerActivityDTO(null, null, false, path, null, null, null);
		    addedDTO.setParentActivityTitle(activity.getTitle());
		    addedDTO.setGroup(branch);
		    addedDTO.setDefaultBranch(defaultBranch);
		    addedDTO.setComplexActivityType(PedagogicalPlannerActivityDTO.TYPE_BRANCHING_ACTIVITY);
		    activities.add(addedDTO);
		} else {
		    // Iterate through all the activities from the sequence, adding them to Planner activity set
		    do {
			addedDTO = addActivityToPlanner(learningDesign, activities, nestedActivity);
			Transition transitionTo = nestedActivity.getTransitionTo();
			if (transitionTo == null) {
			    nestedActivity = null;
			} else {
			    nestedActivity = transitionTo.getToActivity();
			}
			addedDTO.setParentActivityTitle(activity.getTitle());
			addedDTO.setGroup(branch);
			addedDTO.setDefaultBranch(defaultBranch);
			addedDTO.setComplexActivityType(PedagogicalPlannerActivityDTO.TYPE_BRANCHING_ACTIVITY);
		    } while (nestedActivity != null);
		}
		branch++;
	    }
	    addedDTO.setLastNestedActivity(true);
	} else if (activity.isOptionsActivity()) {
	    // Planner does not support branching inside branching/options
	    if (isNested) {
		throw new ServletException(PedagogicalPlannerAction.ERROR_NESTED_OPTIONS);
	    }
	    OptionsActivity optionsActivity = (OptionsActivity) activity;
	    Set<Activity> nestedActivities = optionsActivity.getActivities();
	    short option = 1;
	    for (Activity nestedActivity : nestedActivities) {
		// Currently Planner supports only 4 options, but there is no logical reason for that; just add
		    // colours
		// in CSS and change this value for additional options
		if (option > CentralConstants.PLANNER_MAX_OPTIONS) {
		    throw new ServletException(PedagogicalPlannerAction.ERROR_TOO_MANY_OPTIONS);
		}
		// There are two types of options: sequence and activity; if sequence, iterate through them to get all
		// the nested activities, same as in branching
		if (nestedActivity.isSequenceActivity()) {
		    nestedActivity = ((SequenceActivity) nestedActivity).getDefaultActivity();
		    do {
			addedDTO = addActivityToPlanner(learningDesign, activities, nestedActivity);
			Transition transitionTo = nestedActivity.getTransitionTo();
			if (transitionTo == null) {
			    nestedActivity = null;
			} else {
			    nestedActivity = transitionTo.getToActivity();
			}
			addedDTO.setParentActivityTitle(activity.getTitle());
			addedDTO.setGroup(option);
			addedDTO.setComplexActivityType(PedagogicalPlannerActivityDTO.TYPE_OPTIONAL_ACTIVITY);
		    } while (nestedActivity != null);
		} else {
		    addedDTO = addActivityToPlanner(learningDesign, activities, nestedActivity);
		    addedDTO.setParentActivityTitle(activity.getTitle());
		    addedDTO.setGroup(option);
		    addedDTO.setComplexActivityType(PedagogicalPlannerActivityDTO.TYPE_OPTIONAL_ACTIVITY);
		}
		option++;
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