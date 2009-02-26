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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.client.ToolContentHandler;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.LearnerChoiceGrouping;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.OptionsActivity;
import org.lamsfoundation.lams.learningdesign.ParallelActivity;
import org.lamsfoundation.lams.learningdesign.RandomGrouping;
import org.lamsfoundation.lams.learningdesign.SequenceActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.Transition;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.planner.PedagogicalPlannerSequenceNode;
import org.lamsfoundation.lams.planner.dao.PedagogicalPlannerDAO;
import org.lamsfoundation.lams.planner.dto.PedagogicalPlannerActivityDTO;
import org.lamsfoundation.lams.planner.dto.PedagogicalPlannerSequenceNodeDTO;
import org.lamsfoundation.lams.planner.dto.PedagogicalPlannerTemplateDTO;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.CentralToolContentHandler;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtil;
import org.lamsfoundation.lams.util.zipfile.ZipFileUtilException;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.thoughtworks.xstream.XStream;

/**
 * Action managing Pedagogical Planner base page and non-tool activities.
 * 
 * @author Marcin Cieslak
 * 
 * @struts:action path="/pedagogicalPlanner" scope="request" parameter="method"
 *                name="PedagogicalPlannerSequenceNodeForm"
 * @struts:action-forward name="template" path="/pedagogical_planner/templateBase.jsp"
 * @struts:action-forward name="preview" path="/home.do?method=learner"
 * @struts:action-forward name="sequenceChooser" path="/pedagogical_planner/sequenceChooser.jsp"
 * 
 * @struts:action path="/pedagogicalPlanner/grouping" scope="request" name="PedagogicalPlannerGroupingForm"
 *                validate="false" parameter="method"
 * @struts:action-forward name="grouping" path="/pedagogical_planner/grouping.jsp"
 */
public class PedagogicalPlannerAction extends LamsDispatchAction {

    private static final String FILE_EXTENSION_ZIP = ".zip";
    private static final String FILE_EXTENSION_LAS = ".las";

    // ActionForwards
    private static final String FORWARD_TEMPLATE = "template";
    private static final String FORWARD_PREVIEW = "preview";
    private static final String FORWARD_SEQUENCE_CHOOSER = "sequenceChooser";

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
    private static PedagogicalPlannerDAO pedagogicalPlannerDAO;
    private static ToolContentHandler contentHandler;
    private static final String PEDAGOGICAL_PLANNER_DAO_BEAN_NAME = "pedagogicalPlannerDAO";

    // Keys of error messages used in this class. They are ment to be displayed for user.
    private static final String ERROR_KEY_TOOL_ERRORS = "error.planner.tools.";
    private static final String ERROR_KEY_NODE_TITLE_BLANK = "error.planner.node.title.blank";
    private static final String ERROR_KEY_REPOSITORY = "error.planner.repository";
    private static final String ERROR_KEY_FILE_BAD_EXTENSION = "error.planner.file.bad.extension";
    private static final String ERROR_KEY_FILE_EMPTY = "error.planner.file.empty";
    private static final String ERROR_KEY_FILE_OPEN = "error.planner.file.open";
    private static final String ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED = "error.planner.learning.design.retrieve";
    private static final String ERROR_KEY_EDITOR = "error.planner.editor";
    private static final String ERROR_KEY_EXPORT = "error.planner.export";
    private static final String ERROR_KEY_IMPORT = "error.planner.import";

    // Error messages used in this class. They are ment to be thrown.
    private static final String ERROR_USER_NOT_FOUND = "User not found.";
    private static final String ERROR_NOT_PROPER_FILE = "The sequence template does not exist or is not a proper file.";
    private static final String ERROR_TOO_MANY_OPTIONS = "Number of options in options activity is limited to "
	    + CentralConstants.PLANNER_MAX_OPTIONS + " in Pedagogical Planner.";
    private static final String ERROR_NESTED_OPTIONS = "Nested optional activities are not allowed in Pedagogical Planner.";
    private static final String ERROR_NESTED_BRANCHING = "Nested branching activities are not allowed in Pedagogical Planner.";
    private static final String ERROR_NESTED_PARALLEL = "Nested parallel activities are not allowed in Pedagogical Planner.";
    private static final String ERROR_TOO_MANY_BRANCHES = "Number of branches in branching activity is limited to "
	    + CentralConstants.PLANNER_MAX_BRANCHES + " in Pedagogical Planner.";
    private static final String ERROR_TOO_MANY_PARALLEL_ACTIVITIES = "Number of parallel activities is limited to "
	    + CentralConstants.PLANNER_MAX_PARALLEL_ACTIVITIES + " in Pedagogical Planner.";

    private static Logger log = Logger.getLogger(PedagogicalPlannerAction.class);

    // Paths used in templateBase.jsp
    private static final String IMAGE_PATH_GATE = "images/stop.gif";
    private static final String PATH_ACTIVITY_NO_PLANNER_SUPPORT = "/pedagogical_planner/defaultActivityForm.jsp";
    private static final String IMAGE_PATH_GROUPING = "images/grouping.gif";

    // Parts of paths used in importing/exporting nodes

    private static final String NODE_FILE_NAME = "node.xml";
    private static final String DIR_CONTENT = "content";
    private static final String DIR_TEMPLATES = "template";

    private static final String EXPORT_NODE_FOLDER_SUFFIX = "export_node";
    private static final String EXPORT_NODE_CONTENT_ZIP_PREFIX = "content_";
    private static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    private static final String RESPONSE_CONTENT_TYPE_DOWNLOAD = "application/x-download";
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final String DIR_UPLOADED_NODE_SUFFIX = "_uploaded_node";
    private static final String EXPORT_NODE_ZIP_PREFIX = "lams_planner_node_";

    private static final int FILE_COPY_BUFFER_SIZE = 1024;

    @Override
    /**
     * Go straight to open sequence node.
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	return openSequenceNode(mapping, form, request, response);
    }

    /*----------------------- TEMPLATE CHOOSER METHODS --------------------*/

    /**
     * The main method for opening and parsing template (chosen learning desing).
     * 
     * @param mapping
     * @param form
     * @param request
     * @param fileUuid
     * @param fileName
     * @return
     * @throws ServletException
     */
    private ActionForward openTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    Long fileUuid, String fileName) throws ServletException {

	ActionMessages errors = new ActionMessages();

	// Open the learning design stored in the repository.
	LearningDesign learningDesign = importLearningDesign(fileUuid, fileName, errors);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    // If anything goes wrong, open the root node. Errors will be displayed at top.
	    // This approach is used widely in this action.
	    return openSequenceNode(mapping, form, request, (Long) null);
	}

	List<PedagogicalPlannerActivityDTO> activities = new ArrayList<PedagogicalPlannerActivityDTO>();

	// Create DTOs that hold all the necessary information of the activities
	Activity activity = learningDesign.getFirstActivity();
	PedagogicalPlannerAction.log.debug("Parsing learning design activities");
	try {
	    while (activity != null) {
		// Iterate through all the activities, detecting type of each one
		addActivityToPlanner(learningDesign, activities, activity);
		Transition transitionTo = activity.getTransitionTo();
		if (transitionTo == null) {
		    activity = null;
		} else {
		    activity = transitionTo.getToActivity();
		}
	    }
	} catch (ServletException e) {
	    PedagogicalPlannerAction.log.error(e);
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
		    PedagogicalPlannerAction.ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED));
	    saveErrors(request, errors);
	    return openSequenceNode(mapping, form, request, (Long) null);
	}

	// Recalculate how many activities actually support the planner
	int activitySupportingPlannerCount = 0;
	for (PedagogicalPlannerActivityDTO activityDTO : activities) {
	    if (activityDTO.getSupportsPlanner()) {
		activitySupportingPlannerCount++;
	    }
	}
	// create DTO for the whole design
	PedagogicalPlannerTemplateDTO planner = new PedagogicalPlannerTemplateDTO();
	planner.setActivitySupportingPlannerCount(activitySupportingPlannerCount);
	planner.setSequenceTitle(learningDesign.getTitle());
	planner.setActivities(activities);
	planner.setLearningDesignID(learningDesign.getLearningDesignId());

	// Some additional options for submitting activity forms; should be moved to configuration file in the future
	planner.setSendInPortions(false);
	planner.setSubmitDelay(5000);
	planner.setActivitiesPerPortion(2);

	request.setAttribute(CentralConstants.ATTR_PLANNER, planner);
	return mapping.findForward(PedagogicalPlannerAction.FORWARD_TEMPLATE);
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
	PedagogicalPlannerAction.log.debug("Parsing activity: " + activity.getTitle());

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
			.getTitle(), false, PedagogicalPlannerAction.PATH_ACTIVITY_NO_PLANNER_SUPPORT, activity
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
		    PedagogicalPlannerAction.IMAGE_PATH_GROUPING, null, null);
	    activities.add(addedDTO);
	} else if (activity.isGateActivity()) {
	    // gate is not supported, but takes its image from a differen spot
	    addedDTO = new PedagogicalPlannerActivityDTO(null, activity.getTitle(), false,
		    PedagogicalPlannerAction.PATH_ACTIVITY_NO_PLANNER_SUPPORT,
		    PedagogicalPlannerAction.IMAGE_PATH_GATE, null, null);
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
		    String path = PedagogicalPlannerAction.PATH_ACTIVITY_NO_PLANNER_SUPPORT
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
	} else if (activity.isParallelActivity()) {
	    if (isNested) {
		throw new ServletException(PedagogicalPlannerAction.ERROR_NESTED_PARALLEL);
	    }
	    ParallelActivity parallelActivity = (ParallelActivity) activity;
	    Set<Activity> nestedActivities = parallelActivity.getActivities();
	    short option = 1;
	    for (Activity nestedActivity : nestedActivities) {
		// Currently Planner supports only parallel activities, but there is no logical reason for that;
		// just add colours in CSS and change this value for additional options
		if (option > CentralConstants.PLANNER_MAX_PARALLEL_ACTIVITIES) {
		    throw new ServletException(PedagogicalPlannerAction.ERROR_TOO_MANY_PARALLEL_ACTIVITIES);
		}

		addedDTO = addActivityToPlanner(learningDesign, activities, nestedActivity);
		addedDTO.setParentActivityTitle(activity.getTitle());
		addedDTO.setGroup(option);
		addedDTO.setComplexActivityType(PedagogicalPlannerActivityDTO.TYPE_PARALLEL_ACTIVITY);
		option++;
	    }
	    addedDTO.setLastNestedActivity(true);
	} else {
	    // If unknown/unsupported activity
	    addedDTO = new PedagogicalPlannerActivityDTO(null, activity.getTitle(), false,
		    PedagogicalPlannerAction.PATH_ACTIVITY_NO_PLANNER_SUPPORT, activity.getLibraryActivityUiImage(),
		    null, null);
	    activities.add(addedDTO);
	}
	return addedDTO;
    }

    /**
     * Starts a lesson preview, both in sequence chooser and in template base.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward startPreview(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	Long learningDesignID = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID, true);
	if (learningDesignID == null) {
	    Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID);
	    PedagogicalPlannerSequenceNode node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	    ActionMessages errors = new ActionMessages();
	    LearningDesign learningDesign = importLearningDesign(node.getFileUuid(), node.getFileName(), errors);
	    if (!errors.isEmpty()) {
		ActionMessage error = (ActionMessage) errors.get().next();
		String errorMessage = getMessageService().getMessage(error.getKey());
		throw new ServletException(errorMessage);
	    }
	    learningDesignID = learningDesign.getLearningDesignId();
	}
	HttpSession session = SessionManager.getSession();
	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);

	// Start preview the same way as in authoring
	PedagogicalPlannerAction.log.debug("Opening preview for learnind design id: " + learningDesignID);
	Lesson lesson = getMonitoringService().initializeLessonForPreview("Preview", null, learningDesignID,
		userDto.getUserID(), null, false, false, false);
	getMonitoringService().createPreviewClassForLesson(userDto.getUserID(), lesson.getLessonId());

	getMonitoringService().startLesson(lesson.getLessonId(), userDto.getUserID());
	String newPath = mapping.findForward(PedagogicalPlannerAction.FORWARD_PREVIEW).getPath();
	newPath = newPath + PedagogicalPlannerAction.CHAR_AMPERSAND + AttributeNames.PARAM_LESSON_ID
		+ PedagogicalPlannerAction.CHAR_EQUALS + lesson.getLessonId();
	return new ActionForward(newPath, true);
    }

    /**
     * Reads UID of the node and goes straight to
     * {@link #openSequenceNode(ActionMapping, ActionForm, HttpServletRequest, Long)}
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward openSequenceNode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID, true);
	return openSequenceNode(mapping, form, request, nodeUid);
    }

    /**
     * Opens a sequence node and fill the necessary data into DTO and form.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param nodeUid
     * @return
     * @throws ServletException
     */
    public ActionForward openSequenceNode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    Long nodeUid) throws ServletException {
	// Only SysAdmin can open the editor
	Boolean isSysAdmin = request.isUserInRole(Role.SYSADMIN);
	Boolean edit = WebUtil.readBooleanParam(request, CentralConstants.PARAM_EDIT, false);
	edit &= isSysAdmin;
	// Do we display the root (top) node or an existing one
	PedagogicalPlannerSequenceNode node = null;
	if (nodeUid == null) {
	    node = getPedagogicalPlannerDAO().getRootNode();
	} else {
	    node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	    if (!edit && node.getFileUuid() != null) {
		// If we are not in the edit mode and we open a node containing a template, then we open the template
		return openTemplate(mapping, form, request, node.getFileUuid(), node.getFileName());
	    }
	}
	PedagogicalPlannerAction.log.debug("Opening sequence node with UID: " + nodeUid);

	// Fill the DTO
	List<String[]> titlePath = getPedagogicalPlannerDAO().getTitlePath(node);
	PedagogicalPlannerSequenceNodeDTO dto = new PedagogicalPlannerSequenceNodeDTO(node, titlePath);

	// Additional DTO parameters
	Boolean createSubnode = WebUtil.readBooleanParam(request, CentralConstants.PARAM_CREATE_SUBNODE, false);
	Boolean importNode = WebUtil.readBooleanParam(request, CentralConstants.PARAM_IMPORT_NODE, false);
	dto.setCreateSubnode(createSubnode);
	dto.setEdit(edit);
	dto.setIsSysAdmin(isSysAdmin);
	dto.setImportNode(importNode);

	request.setAttribute(CentralConstants.ATTR_NODE, dto);

	if (edit) {
	    // If we are in edit mode, the node form is displayed, requiring additional parameters
	    PedagogicalPlannerSequenceNodeForm nodeForm = (PedagogicalPlannerSequenceNodeForm) form;

	    if (createSubnode) {
		// We create a new node, rather than edit the existing one
		if (node.getContentFolderId() == null) {
		    try {
			// If it's a new top level node, we create an uniuqe ID
			String contentFolderId = FileUtil.generateUniqueContentFolderID();
			nodeForm.setContentFolderId(contentFolderId);
		    } catch (Exception e) {
			PedagogicalPlannerAction.log.error(e);
			dto.setEdit(false);
			dto.setCreateSubnode(false);
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				PedagogicalPlannerAction.ERROR_KEY_EDITOR));
			saveErrors(request, errors);
			return openSequenceNode(mapping, form, request, (Long) null);
		    }
		} else {
		    // Whole node tree share the same content folder ID
		    nodeForm.setContentFolderId(node.getContentFolderId());
		}
	    } else if (!importNode) {
		// We fill the form with necessary data
		nodeForm.setNodeType(node.getFileName() == null ? PedagogicalPlannerSequenceNodeForm.NODE_TYPE_SUBNODES
			: PedagogicalPlannerSequenceNodeForm.NODE_TYPE_TEMPLATE);
		nodeForm.setRemoveFile(false);
		nodeForm.setTitle(dto.getTitle());
		nodeForm.setBriefDescription(dto.getBriefDescription());
		nodeForm.setFullDescription(dto.getFullDescription());
		nodeForm.setFileUuid(node.getFileUuid());
	    }
	}
	return mapping.findForward(PedagogicalPlannerAction.FORWARD_SEQUENCE_CHOOSER);
    }

    /**
     * Saves the created/edited sequence node.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward saveSequenceNode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID, true);
	PedagogicalPlannerSequenceNode node = null;
	PedagogicalPlannerSequenceNodeForm nodeForm = (PedagogicalPlannerSequenceNodeForm) form;

	if (nodeUid == null) {
	    // It's a new subnode
	    node = new PedagogicalPlannerSequenceNode();
	    Long parentUid = nodeForm.getParentUid() == 0 ? null : nodeForm.getParentUid();
	    if (parentUid != null) {
		PedagogicalPlannerSequenceNode parent = getPedagogicalPlannerDAO().getByUid(parentUid);
		node.setParent(parent);
	    }
	    node.setOrder(getPedagogicalPlannerDAO().getNextOrderId(parentUid));
	} else {
	    // It's an existing node
	    node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	    nodeUid = node.getUid();
	}
	PedagogicalPlannerAction.log.debug("Saving sequence node with UID: " + nodeUid);

	// If anything goes wrong, we need to put back these values
	String title = nodeForm.getTitle();
	String briefDescription = nodeForm.getBriefDescription();
	String fullDescription = nodeForm.getFullDescription();
	String nodeType = nodeForm.getNodeType();

	ActionMessages errors = validateNodeForm(node, nodeForm);
	if (errors.isEmpty()) {
	    try {
		node.setTitle(title);
		node.setBriefDescription(briefDescription);
		node.setContentFolderId(nodeForm.getContentFolderId());

		// Different properties are set, depending on node type: with subnodes or template
		if (PedagogicalPlannerSequenceNodeForm.NODE_TYPE_SUBNODES.equals(nodeForm.getNodeType())) {
		    if (node.getFileUuid() != null) {
			getContentHandler().deleteFile(node.getFileUuid());
			node.setFileName(null);
			node.setFileUuid(null);
		    }
		    node.setFullDescription(fullDescription);
		} else if (Boolean.TRUE.equals(nodeForm.getRemoveFile())) {
		    getContentHandler().deleteFile(node.getFileUuid());
		    node.setFileName(null);
		    node.setFileUuid(null);
		    node.setFullDescription(null);
		} else if (nodeForm.getFile() != null && nodeForm.getFile().getFileSize() > 0) {
		    FormFile file = nodeForm.getFile();
		    InputStream inputStream = file.getInputStream();
		    String fileName = file.getFileName();
		    String type = file.getContentType();

		    PedagogicalPlannerAction.log.debug("Uploading to repository file: " + fileName);
		    // Upload to repository
		    NodeKey nodeKey = getContentHandler().uploadFile(inputStream, fileName, type,
			    IToolContentHandler.TYPE_OFFLINE);
		    if (node.getFileUuid() != null) {
			getContentHandler().deleteFile(node.getFileUuid());
		    }

		    // If there were subnodes, we delete them now
		    Iterator<PedagogicalPlannerSequenceNode> subnodeIter = node.getSubnodes().iterator();
		    while (subnodeIter.hasNext()) {
			PedagogicalPlannerSequenceNode subnode = subnodeIter.next();
			subnodeIter.remove();
			getPedagogicalPlannerDAO().removeNode(subnode);
		    }

		    node.setFileUuid(nodeKey.getUuid());
		    node.setFileName(fileName);
		    node.setFullDescription(null);
		}

		getPedagogicalPlannerDAO().saveOrUpdateNode(node);
		// If it was a new subnode, we need to retrieved the assigned UID
		nodeUid = node.getUid();

	    } catch (RepositoryCheckedException e) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			PedagogicalPlannerAction.ERROR_KEY_REPOSITORY));
		PedagogicalPlannerAction.log.error(e);
	    }
	}

	// If something went wrong and the new subnode was not saved,
	// we need to inform the following method of that fact
	boolean createSubnode = false;
	if (nodeUid == null) {
	    nodeUid = node.getParent() == null ? null : node.getParent().getUid();
	    createSubnode = true;
	}
	// Set the parameters, but do not return yet
	openSequenceNode(mapping, form, request, nodeUid);

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    nodeForm.setTitle(title);
	    nodeForm.setBriefDescription(briefDescription);
	    nodeForm.setFullDescription(fullDescription);
	    nodeForm.setNodeType(nodeType);
	    if (createSubnode) {
		PedagogicalPlannerSequenceNodeDTO dto = (PedagogicalPlannerSequenceNodeDTO) request
			.getAttribute(CentralConstants.ATTR_NODE);
		dto.setCreateSubnode(true);
	    }
	}
	return mapping.findForward(PedagogicalPlannerAction.FORWARD_SEQUENCE_CHOOSER);
    }

    /**
     * Validates node form fields.
     * 
     * @param node
     * @param form
     * @return
     */
    private ActionMessages validateNodeForm(PedagogicalPlannerSequenceNode node, PedagogicalPlannerSequenceNodeForm form) {
	ActionMessages errors = new ActionMessages();
	// Title must not be blank
	if (StringUtils.isEmpty(form.getTitle())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
		    PedagogicalPlannerAction.ERROR_KEY_NODE_TITLE_BLANK));
	}
	// Template must a proper file
	if (PedagogicalPlannerSequenceNodeForm.NODE_TYPE_TEMPLATE.equals(form.getNodeType())
		&& node.getFileName() == null) {
	    errors.add(validateFormFile(form));
	}
	return errors;
    }

    /**
     * Validates form file. Used both for templates and exported nodes.
     * 
     * @param form
     * @return
     */
    private ActionMessages validateFormFile(PedagogicalPlannerSequenceNodeForm form) {
	ActionMessages errors = new ActionMessages();
	if (form.getFile() == null || form.getFile().getFileSize() == 0) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_FILE_EMPTY));
	} else {
	    String fileName = form.getFile().getFileName();
	    boolean badExtension = false;
	    if (fileName.length() >= 4) {
		String extension = fileName.substring(fileName.length() - 4);
		if (!(extension.equalsIgnoreCase(PedagogicalPlannerAction.FILE_EXTENSION_LAS) || extension
			.equalsIgnoreCase(PedagogicalPlannerAction.FILE_EXTENSION_ZIP))) {
		    badExtension = true;
		}
	    } else {
		badExtension = true;
	    }
	    if (badExtension) {
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			PedagogicalPlannerAction.ERROR_KEY_FILE_BAD_EXTENSION));
	    }
	}
	return errors;
    }

    /**
     * Removes the selected node and all of its subnodes
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeSequenceNode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID);
	PedagogicalPlannerSequenceNode node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	Long parentUid = node.getParent() == null ? null : node.getParent().getUid();
	PedagogicalPlannerAction.log.debug("Removing sequence node with UID" + nodeUid);
	getPedagogicalPlannerDAO().removeNode(node);
	return openSequenceNode(mapping, form, request, parentUid);
    }

    public ActionForward moveNodeUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	return moveNode(mapping, form, request, -1);
    }

    public ActionForward moveNodeDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	return moveNode(mapping, form, request, 1);
    }

    private ActionForward moveNode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    Integer orderDelta) throws IOException, ServletException {
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID);
	PedagogicalPlannerSequenceNode node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	Long parentUid = node.getParent() == null ? null : node.getParent().getUid();
	PedagogicalPlannerSequenceNode neighbourNode = getPedagogicalPlannerDAO().getNeighbourNode(node, orderDelta);

	neighbourNode.setOrder(0);
	getPedagogicalPlannerDAO().saveOrUpdateNode(neighbourNode);
	node.setOrder(node.getOrder() + orderDelta);
	getPedagogicalPlannerDAO().saveOrUpdateNode(node);
	neighbourNode.setOrder(node.getOrder() - orderDelta);
	getPedagogicalPlannerDAO().saveOrUpdateNode(neighbourNode);

	return openSequenceNode(mapping, form, request, parentUid);
    }

    /**
     * Exports the selected node to a ZIP file. Method is based on a similar one for exporting learning designs.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ActionForward exportNode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID);
	ActionMessages errors = new ActionMessages();

	PedagogicalPlannerAction.log.debug("Exporting sequence node with UID" + nodeUid);
	String zipFilePath = null;
	// Different browsers handle stream downloads differently LDEV-1243
	String filename = null;
	try {
	    zipFilePath = exportNode(nodeUid);

	    // get only filename
	    String zipfile = FileUtil.getFileName(zipFilePath);

	    // replace spaces (" ") with underscores ("_")

	    zipfile = zipfile.replaceAll(" ", "_");

	    // write zip file as response stream.

	    filename = FileUtil.encodeFilenameForDownload(request, zipfile);

	    PedagogicalPlannerAction.log.debug("Final filename to export: " + filename);

	    response.setContentType(PedagogicalPlannerAction.RESPONSE_CONTENT_TYPE_DOWNLOAD);
	    // response.setContentType("application/zip");
	    response.setHeader(PedagogicalPlannerAction.HEADER_CONTENT_DISPOSITION, "attachment;filename=" + filename);
	} catch (Exception e) {
	    PedagogicalPlannerAction.log.error(e);
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_EXPORT));
	    saveErrors(request, errors);
	    return openSequenceNode(mapping, form, request, nodeUid);
	}
	InputStream in = null;
	ServletOutputStream out = null;
	try {
	    in = new BufferedInputStream(new FileInputStream(zipFilePath));
	    out = response.getOutputStream();
	    int count = 0;

	    int ch;
	    while ((ch = in.read()) != -1) {
		out.write((char) ch);
		count++;
	    }
	    PedagogicalPlannerAction.log.debug("Wrote out " + count + " bytes");
	    response.setContentLength(count);
	    out.flush();
	} finally {
	    /*
	     * If anything goes wrong, we can not display it nicely for the user. Once response.getOutputStream() was
	     * called to write the file data, we can not forward to a JSP page anymore. Maybe there is a way to avoid
	     * it, but currently we are simply throwing an error. So no "catch" clause.
	     */
	    if (in != null) {
		in.close(); // very important
	    }
	}
	return null;
    }

    /**
     * The proper method for exporting nodes.
     * 
     * @param nodeUid
     * @return
     * @throws ZipFileUtilException
     * @throws FileUtilException
     * @throws IOException
     * @throws RepositoryCheckedException
     */
    private String exportNode(Long nodeUid) throws ZipFileUtilException, FileUtilException, IOException,
	    RepositoryCheckedException {
	if (nodeUid != null) {

	    String rootDir = FileUtil.createTempDirectory(PedagogicalPlannerAction.EXPORT_NODE_FOLDER_SUFFIX);
	    String contentDir = FileUtil.getFullPath(rootDir, PedagogicalPlannerAction.DIR_CONTENT);
	    FileUtil.createDirectory(contentDir);

	    String nodeFileName = FileUtil.getFullPath(contentDir, PedagogicalPlannerAction.NODE_FILE_NAME);
	    Writer nodeFile = new OutputStreamWriter(new FileOutputStream(nodeFileName),
		    PedagogicalPlannerAction.ENCODING_UTF_8);

	    PedagogicalPlannerSequenceNode node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	    // exporting XML
	    XStream designXml = new XStream();
	    designXml.toXML(node, nodeFile);
	    nodeFile.close();

	    PedagogicalPlannerAction.log.debug("Node xml export success");

	    // Copy templates' ZIP files from repository
	    File templateDir = new File(contentDir, PedagogicalPlannerAction.DIR_TEMPLATES);
	    exportSubnodeTemplates(node, templateDir);

	    // create zip file for fckeditor unique content folder
	    String targetZipFileName = PedagogicalPlannerAction.EXPORT_NODE_CONTENT_ZIP_PREFIX
		    + node.getContentFolderId() + PedagogicalPlannerAction.FILE_EXTENSION_ZIP;
	    String secureDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator
		    + FileUtil.LAMS_WWW_DIR + File.separator + FileUtil.LAMS_WWW_SECURE_DIR;
	    String nodeContentDir = FileUtil.getFullPath(secureDir, node.getContentFolderId());

	    if (!FileUtil.isEmptyDirectory(nodeContentDir, true)) {
		PedagogicalPlannerAction.log.debug("Create export node content target zip file. File name is "
			+ targetZipFileName);
		ZipFileUtil.createZipFile(targetZipFileName, nodeContentDir, contentDir);
	    } else {
		PedagogicalPlannerAction.log.debug("No such directory (or empty directory): " + nodeContentDir);
	    }

	    // zip file name with full path
	    targetZipFileName = PedagogicalPlannerAction.EXPORT_NODE_ZIP_PREFIX + node.getTitle()
		    + PedagogicalPlannerAction.FILE_EXTENSION_ZIP;
	    ;
	    PedagogicalPlannerAction.log
		    .debug("Create export node content zip file. File name is " + targetZipFileName);
	    // create zip file and return zip full file name
	    return ZipFileUtil.createZipFile(targetZipFileName, contentDir, rootDir);
	}
	return null;
    }

    /**
     * Imports a zipped node. Thi method is based on a similar one for importing learning designs.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward importNode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	PedagogicalPlannerSequenceNodeForm nodeForm = (PedagogicalPlannerSequenceNodeForm) form;
	ActionMessages errors = validateFormFile(nodeForm);

	if (errors.isEmpty()) {
	    try {
		String uploadPath = FileUtil.createTempDirectory(PedagogicalPlannerAction.DIR_UPLOADED_NODE_SUFFIX);
		String fileName = nodeForm.getFile().getFileName();
		File importFile = new File(uploadPath + fileName);
		PedagogicalPlannerAction.log.debug("Importing a node from file: " + fileName);

		// Copy the submitted file to the hard drive
		InputStream inputStream = nodeForm.getFile().getInputStream();
		copyFileFromRepository(inputStream, importFile);

		nodeForm.setFile(null);

		String rootPath = ZipFileUtil.expandZip(new FileInputStream(importFile), fileName);
		String nodeFilePath = FileUtil.getFullPath(rootPath, PedagogicalPlannerAction.NODE_FILE_NAME);

		PedagogicalPlannerSequenceNode node = (PedagogicalPlannerSequenceNode) FileUtil.getObjectFromXML(null,
			nodeFilePath);

		// begin fckeditor content folder import
		String contentZipFileName = PedagogicalPlannerAction.EXPORT_NODE_CONTENT_ZIP_PREFIX
			+ node.getContentFolderId() + PedagogicalPlannerAction.FILE_EXTENSION_ZIP;
		String secureDir = Configuration.get(ConfigurationKeys.LAMS_EAR_DIR) + File.separator
			+ FileUtil.LAMS_WWW_DIR + File.separator + FileUtil.LAMS_WWW_SECURE_DIR + File.separator
			+ node.getContentFolderId();
		File contentZipFile = new File(FileUtil.getFullPath(rootPath, contentZipFileName));

		// unzip file to target secure dir if exists
		if (contentZipFile.exists()) {
		    InputStream is = new FileInputStream(contentZipFile);
		    ZipFileUtil.expandZipToFolder(is, secureDir);
		}

		// Upload the template files back into the repository
		File templateDir = new File(rootPath, PedagogicalPlannerAction.DIR_TEMPLATES);
		importSubnodeTemplates(node, templateDir);

		// The imported node is added as the last one
		Integer order = getPedagogicalPlannerDAO().getNextOrderId(null);
		node.setOrder(order);
		getPedagogicalPlannerDAO().saveOrUpdateNode(node);
	    } catch (Exception e) {
		PedagogicalPlannerAction.log.error(e);
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_IMPORT));
	    }
	}
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	}
	return openSequenceNode(mapping, form, request, (Long) null);
    }

    /**
     * Imports a learning design for edit/preview template purposes.
     * 
     * @param fileUuid
     * @param fileName
     * @param errors
     * @return
     * @throws ServletException
     */
    private LearningDesign importLearningDesign(Long fileUuid, String fileName, ActionMessages errors)
	    throws ServletException {
	File designFile = null;
	try {
	    designFile = new File(FileUtil.TEMP_DIR, fileName);
	    InputStream inputStream = getContentHandler().getFileNode(fileUuid).getFile();
	    copyFileFromRepository(inputStream, designFile);
	} catch (Exception e) {
	    PedagogicalPlannerAction.log.error(e);
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_FILE_OPEN));
	    return null;
	}
	if (!designFile.exists() || designFile.isDirectory()) {
	    PedagogicalPlannerAction.log.error(PedagogicalPlannerAction.ERROR_NOT_PROPER_FILE);
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_FILE_OPEN));
	    return null;
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
	    designFile.delete();
	    learningDesignID = (Long) ldResults[0];
	    learningDesignErrorMsgs = (List<String>) ldResults[1];
	    toolsErrorMsgs = (List<String>) ldResults[2];
	    learningDesign = getAuthoringService().getLearningDesign(learningDesignID);
	} catch (ImportToolContentException e) {
	    PedagogicalPlannerAction.log.error(e);
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
		    PedagogicalPlannerAction.ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED));

	}

	if ((learningDesignID == null || learningDesignID.longValue() == -1) && learningDesignErrorMsgs.size() == 0) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
		    PedagogicalPlannerAction.ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED));
	    return null;
	}
	if (learningDesignErrorMsgs.size() > 0) {
	    for (String error : learningDesignErrorMsgs) {
		PedagogicalPlannerAction.log.error(error);
	    }
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
		    PedagogicalPlannerAction.ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED));
	    return null;
	}
	if (toolsErrorMsgs.size() > 0) {
	    for (String error : toolsErrorMsgs) {
		PedagogicalPlannerAction.log.error(error);
	    }
	    errors
		    .add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
			    PedagogicalPlannerAction.ERROR_KEY_TOOL_ERRORS));
	    return null;
	}
	return learningDesign;
    }

    /**
     * Copies the template files from repository into the selected dir.
     * 
     * @param node
     * @param outputDir
     * @throws RepositoryCheckedException
     * @throws IOException
     */
    private void exportSubnodeTemplates(PedagogicalPlannerSequenceNode node, File outputDir)
	    throws RepositoryCheckedException, IOException {
	if (node != null) {
	    if (node.getFileUuid() == null) {
		if (node.getSubnodes() != null) {
		    for (PedagogicalPlannerSequenceNode subnode : node.getSubnodes()) {
			exportSubnodeTemplates(subnode, outputDir);
		    }
		}
	    } else {
		File uuidDir = new File(outputDir, node.getFileUuid().toString());
		uuidDir.mkdirs();
		File targetFile = new File(uuidDir, node.getFileName());
		InputStream inputStream = getContentHandler().getFileNode(node.getFileUuid()).getFile();
		PedagogicalPlannerAction.log.debug("Preparing for zipping the template file: " + node.getFileName());
		copyFileFromRepository(inputStream, targetFile);
	    }
	}
    }

    /**
     * Uploads the templates back into repository. Also sets all the nodes' UIDs to NULL.
     * 
     * @param node
     * @param inputDir
     * @throws RepositoryCheckedException
     * @throws IOException
     */
    private void importSubnodeTemplates(PedagogicalPlannerSequenceNode node, File inputDir)
	    throws RepositoryCheckedException, IOException {
	if (node != null) {
	    node.setUid(null);

	    if (node.getFileUuid() == null) {
		if (node.getSubnodes() != null) {
		    for (PedagogicalPlannerSequenceNode subnode : node.getSubnodes()) {
			importSubnodeTemplates(subnode, inputDir);
		    }
		}
	    } else {
		File uuidDir = new File(inputDir, node.getFileUuid().toString());
		File file = new File(uuidDir, node.getFileName());
		InputStream inputStream = new FileInputStream(file);
		String fileName = node.getFileName();
		PedagogicalPlannerAction.log.debug("Uploading into repository a template file: " + fileName);
		NodeKey nodeKey = getContentHandler().uploadFile(inputStream, fileName,
			PedagogicalPlannerAction.RESPONSE_CONTENT_TYPE_DOWNLOAD, IToolContentHandler.TYPE_OFFLINE);
		node.setFileUuid(nodeKey.getUuid());
	    }
	}
    }

    /**
     * Copies a file using the provided input stream.
     * 
     * @param inputStream
     * @param targetFile
     * @throws RepositoryCheckedException
     * @throws IOException
     */
    private void copyFileFromRepository(InputStream inputStream, File targetFile) throws RepositoryCheckedException,
	    IOException {

	FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
	byte[] data = new byte[PedagogicalPlannerAction.FILE_COPY_BUFFER_SIZE];
	int read = 0;
	do {
	    read = inputStream.read(data);
	    if (read > 0) {
		fileOutputStream.write(data, 0, read);
	    }
	} while (read > 0);
	fileOutputStream.close();
	inputStream.close();
    }

    /*----------------------- GROUPING METHODS -------------------------*/

    /**
     * Fill the grouping with initial data
     */
    public ActionForward initGrouping(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PedagogicalPlannerGroupingForm plannerForm = (PedagogicalPlannerGroupingForm) form;
	Long groupingId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Grouping grouping = getAuthoringService().getGroupingById(groupingId);
	plannerForm.fillForm(grouping);
	return mapping.findForward(CentralConstants.FORWARD_GROUPING);
    }

    /**
     * Saves parameters of the grouping form, depending on the grouping type.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward saveOrUpdateGroupingForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PedagogicalPlannerAction.log.debug("Saving grouping activity");
	PedagogicalPlannerGroupingForm plannerForm = (PedagogicalPlannerGroupingForm) form;
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
		learnerChoiceGrouping.setViewStudentsBeforeSelection(plannerForm.getViewStudentsBeforeSelection());
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

    /*-------------------------- TEMPLATE BASE METHODS -----------------*/

    /**
     * Saves additional, non tool-bound template details - currently only the title.
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
	PedagogicalPlannerAction.log.debug("Saving sequence title");
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

    /*------------------------ SERVICE ACCESS METHODS --------------------*/

    private IExportToolContentService getExportService() {
	if (PedagogicalPlannerAction.exportService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.exportService = (IExportToolContentService) ctx
		    .getBean(CentralConstants.EXPORT_TOOLCONTENT_SERVICE_BEAN_NAME);
	}
	return PedagogicalPlannerAction.exportService;
    }

    private IAuthoringService getAuthoringService() {
	if (PedagogicalPlannerAction.authoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.authoringService = (IAuthoringService) ctx
		    .getBean(AuthoringConstants.AUTHORING_SERVICE_BEAN_NAME);
	}
	return PedagogicalPlannerAction.authoringService;
    }

    private IMonitoringService getMonitoringService() {
	if (PedagogicalPlannerAction.monitoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.monitoringService = (IMonitoringService) ctx
		    .getBean(CentralConstants.MONITORING_SERVICE_BEAN_NAME);
	}
	return PedagogicalPlannerAction.monitoringService;
    }

    private IUserManagementService getUserManagementService() {
	if (PedagogicalPlannerAction.userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.userManagementService = (IUserManagementService) ctx
		    .getBean(CentralConstants.USER_MANAGEMENT_SERVICE_BEAN_NAME);
	}
	return PedagogicalPlannerAction.userManagementService;
    }

    private MessageService getMessageService() {
	if (PedagogicalPlannerAction.messageService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.messageService = (MessageService) ctx
		    .getBean(CentralConstants.CENTRAL_MESSAGE_SERVICE_BEAN_NAME);
	}
	return PedagogicalPlannerAction.messageService;
    }

    private PedagogicalPlannerDAO getPedagogicalPlannerDAO() {
	if (PedagogicalPlannerAction.pedagogicalPlannerDAO == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.pedagogicalPlannerDAO = (PedagogicalPlannerDAO) wac
		    .getBean(PedagogicalPlannerAction.PEDAGOGICAL_PLANNER_DAO_BEAN_NAME);
	}
	return PedagogicalPlannerAction.pedagogicalPlannerDAO;
    }

    private ToolContentHandler getContentHandler() {
	if (PedagogicalPlannerAction.contentHandler == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PedagogicalPlannerAction.contentHandler = (CentralToolContentHandler) wac
		    .getBean(CentralConstants.CENTRAL_TOOL_CONTENT_HANDLER_BEAN_NAME);
	}
	return PedagogicalPlannerAction.contentHandler;
    }
}