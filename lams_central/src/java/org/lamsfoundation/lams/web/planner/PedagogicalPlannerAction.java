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

package org.lamsfoundation.lams.web.planner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
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
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
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
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.ActivityDAO;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.planner.PedagogicalPlannerActivityMetadata;
import org.lamsfoundation.lams.planner.PedagogicalPlannerSequenceNode;
import org.lamsfoundation.lams.planner.dao.PedagogicalPlannerDAO;
import org.lamsfoundation.lams.planner.dto.PedagogicalPlannerActivityDTO;
import org.lamsfoundation.lams.planner.dto.PedagogicalPlannerSequenceNodeDTO;
import org.lamsfoundation.lams.planner.dto.PedagogicalPlannerTemplateDTO;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
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
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * Action managing Pedagogical Planner base page and non-tool activities.
 *
 * @author Marcin Cieslak
 */
public class PedagogicalPlannerAction extends LamsDispatchAction {
    private static Logger log = Logger.getLogger(PedagogicalPlannerAction.class);

    // Services used in the class, injected by Spring
    private static IUserManagementService userManagementService;
    private static IExportToolContentService exportService;
    private static IAuthoringService authoringService;
    private static IMonitoringService monitoringService;
    private static MessageService messageService;
    private static PedagogicalPlannerDAO pedagogicalPlannerDAO;
    private static IActivityDAO activityDAO;

    private static final String FILE_EXTENSION_ZIP = ".zip";
    private static final String FILE_EXTENSION_LAS = ".las";

    // ActionForwards
    private static final String FORWARD_TEMPLATE = "template";
    private static final String FORWARD_PREVIEW = "preview";
    private static final String FORWARD_SEQUENCE_CHOOSER = "sequenceChooser";
    public static final String FORWARD_GROUPING = "grouping";

    // for building HTML requests
    private static final String ACTIVITY_METADATA_PREFIX = "activity";
    private static final String ACTIVITY_METADATA_HIDDEN = "Hidden";
    private static final String ACTIVITY_METADATA_EXPANDED = "Expanded";
    private static final String ACTIVITY_METADATA_COLLAPSED = "Collapsed";
    private static final String ACTIVITY_METADATA_EDITING_ADVICE = "EditingAdvice";

    // Keys of error messages used in this class. They are meant to be displayed for user.
    private static final String ERROR_KEY_TOOL_ERRORS = "error.planner.tools.";
    private static final String ERROR_KEY_NODE_TITLE_BLANK = "error.planner.node.title.blank";
    private static final String ERROR_KEY_FILE_BAD_EXTENSION = "error.planner.file.bad.extension";
    private static final String ERROR_KEY_FILE_EMPTY = "error.planner.file.empty";
    private static final String ERROR_KEY_FILE_OPEN = "error.planner.file.open";
    private static final String ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED = "error.planner.learning.design.retrieve";
    private static final String ERROR_KEY_EXPORT = "error.planner.export";
    private static final String ERROR_KEY_IMPORT = "error.planner.import";
    private static final String ERROR_KEY_FILTER_PARSE = "error.planner.filter.parse";
    private static final String ERROR_KEY_EXPORT_TEMPLATE = "error.planner.export.template";
    private static final String ERROR_KEY_REMOVE_NODE_TREE = "error.planner.remove.node.tree";

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
    private static final String ENCODING_UTF_8 = "UTF-8";
    private static final String DIR_UPLOADED_NODE_SUFFIX = "_uploaded_node";
    private static final String EXPORT_NODE_ZIP_PREFIX = "lams_planner_node_";
    private static final String PLANNER_FOLDER_NAME = "Preview Planner";

    // Filter constants
    private static final String FIELD_NAME_TITLE = "title";
    private static final String FIELD_NAME_BRIEF_DESCRIPTION = "briefDescription";
    private static final String FIELD_NAME_FULL_DESCRIPTION = "fullDescription";
    private static final String FIELD_NAME_ANCESTOR_UID = "ancestorUid";

    // Tutorial video page string for recognising which page the video was started from
    private static final String PAGE_STRING_START_PLANNER = "StPed";

    // Parameters
    public static final String PARAM_REQUEST_SRC = "requestSrc";
    public static final String PARAM_FORBID_BUTTONS = "forbidButtons";
    public static final String PARAM_RETURN_TO_PARENT = "returnToParent";

    // Template copy mode values
    public static final String COPY_MODE_EDIT_CURRENT = "editCurrent";
    public static final String COPY_MODE_MOVE_CURRRENT = "moveCurrent";
    public static final String COPY_MODE_MAKE_COPY = "makeCopy";

    @Override
    /**
     * Go straight to open sequence node.
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// First we check if a tutorial video should be displayed
	HttpSession session = SessionManager.getSession();
	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	boolean doNotShowAgain = (userDto.getPagesWithDisabledTutorials() != null)
		&& userDto.getPagesWithDisabledTutorials().contains(PedagogicalPlannerAction.PAGE_STRING_START_PLANNER);
	boolean showTutorial = !(userDto.getTutorialsDisabled() || doNotShowAgain);
	request.setAttribute(AttributeNames.ATTR_SHOW_TUTORIAL, showTutorial);

	// process requestSrc and notifyCloseURL parameters (if any)
	String requestSrc = request.getParameter(PedagogicalPlannerAction.PARAM_REQUEST_SRC);
	if (StringUtils.isNotBlank(requestSrc)) {
	    request.getSession().setAttribute(PedagogicalPlannerAction.PARAM_REQUEST_SRC, requestSrc);
	}
	String notifyCloseURL = request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL);
	if (StringUtils.isNotBlank(notifyCloseURL)) {
	    request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL, notifyCloseURL);
	}

	return openSequenceNode(mapping, form, request, response);
    }

    /*----------------------- TEMPLATE CHOOSER METHODS --------------------*/

    /**
     * Opens an existing learning design.
     */
    public ActionForward openExistingTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	ActionMessages errors = new ActionMessages();

	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID, true);

	LearningDesign learningDesign = null;
	// either get the design ID or we don't know it and need to extract it from the node
	Long learningDesignId = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID, true);
	boolean isEditor = true;
	PedagogicalPlannerSequenceNode node = null;
	if ((learningDesignId == null) && (nodeUid != null)) {
	    // we are opening a LD from a certain node, so check is we are allowed to do it
	    isEditor = canEditNode(request, nodeUid);
	    node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	    learningDesignId = node.getLearningDesignId();
	}

	if (learningDesignId != null) {
	    String copyMode = WebUtil.readStrParam(request, CentralConstants.PARAM_COPY_MODE, true);
	    if (StringUtils.isBlank(copyMode)
		    || PedagogicalPlannerAction.COPY_MODE_EDIT_CURRENT.equalsIgnoreCase(copyMode)) {
		// make sure user has priviledges
		if (!isEditor || ((node != null) && (node.getPermissions() != null)
			&& ((node.getPermissions() & PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_MODIFY) == 0))) {
		    PedagogicalPlannerAction.log.debug("Unauthorised attempt to openExistingTemplate (original)");
		    throw new UserAccessDeniedException();
		}
		// modify the original design (a hard copy in user's folder)
		learningDesign = getAuthoringService().getLearningDesign(learningDesignId);
		copyMode = PedagogicalPlannerAction.COPY_MODE_EDIT_CURRENT;
	    } else if (PedagogicalPlannerAction.COPY_MODE_MAKE_COPY.equalsIgnoreCase(copyMode)) {
		if (isEditor
			? (node != null) && (node.getPermissions() != null)
				&& ((node.getPermissions()
					& PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_VIEW) == 0)
			: (node != null) && (node.getPermissions() != null) && ((node.getPermissions()
				& PedagogicalPlannerSequenceNode.PERMISSION_TEACHER_COPY) == 0)) {
		    PedagogicalPlannerAction.log.debug("Unauthorised attempt to openExistingTemplate (copy)");
		    throw new UserAccessDeniedException();
		}
		// make a temporary copy in common folder
		learningDesign = copyLearningDesign(learningDesignId, errors);
		copyMode = PedagogicalPlannerAction.COPY_MODE_MOVE_CURRRENT;
	    }
	    request.setAttribute(CentralConstants.PARAM_COPY_MODE, copyMode);
	}
	if (learningDesign == null) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage("no.such.learningdesign.exist", new Object[] { learningDesignId }));
	} else {
	    errors = openTemplate(request, learningDesign);
	}
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    // If anything goes wrong, errors will be displayed at top. This approach is used widely in this action.
	    return openSequenceNode(mapping, form, request, nodeUid);
	}

	// process requestSrc and notifyCloseURL parameters (if any)
	String requestSrc = request.getParameter(PedagogicalPlannerAction.PARAM_REQUEST_SRC);
	if (StringUtils.isNotBlank(requestSrc)) {
	    request.getSession().setAttribute(PedagogicalPlannerAction.PARAM_REQUEST_SRC, requestSrc);
	}
	String notifyCloseURL = request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL);
	if (StringUtils.isNotBlank(notifyCloseURL)) {
	    request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL, notifyCloseURL);
	}

	return mapping.findForward(PedagogicalPlannerAction.FORWARD_TEMPLATE);
    }

    /**
     * The main method for opening and parsing template (chosen learning desing).
     *
     * @param request
     * @param learningDesign
     * @return
     * @throws ServletException
     */
    private ActionMessages openTemplate(HttpServletRequest request, LearningDesign learningDesign)
	    throws ServletException {
	ActionMessages errors = new ActionMessages();

	List<PedagogicalPlannerActivityDTO> activities = new ArrayList<>();

	// Create DTOs that hold all the necessary information of the activities
	Activity activity = learningDesign.getFirstActivity();
	PedagogicalPlannerAction.log.debug("Parsing learning design activities");
	try {
	    while (activity != null) {
		// Iterate through all the activities, detecting type of each one
		addActivityToPlanner(learningDesign, activities, activity);
		Transition transitionFrom = activity.getTransitionFrom();
		if (transitionFrom == null) {
		    activity = null;
		} else {
		    activity = transitionFrom.getToActivity();
		}
	    }
	} catch (ServletException e) {
	    PedagogicalPlannerAction.log.error(e, e);
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED));
	    return errors;
	}

	// Recalculate how many activities actually support the planner
	int activitySupportingPlannerCount = 0;
	for (PedagogicalPlannerActivityDTO activityDTO : activities) {
	    if (activityDTO.getSupportsPlanner()) {
		activitySupportingPlannerCount++;
	    }
	}
	// create DTO for the whole design
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID, true);

	PedagogicalPlannerTemplateDTO planner = new PedagogicalPlannerTemplateDTO();
	planner.setActivitySupportingPlannerCount(activitySupportingPlannerCount);
	planner.setSequenceTitle(learningDesign.getTitle());
	planner.setActivities(activities);
	planner.setLearningDesignID(learningDesign.getLearningDesignId());

	if (nodeUid != null) {
	    PedagogicalPlannerSequenceNode node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	    if (node != null) {
		boolean isEditor = canEditNode(request, nodeUid);
		Integer nodePermissions = node.getPermissions();
		planner.setPermissions(nodePermissions, isEditor);

		boolean returnToParent = WebUtil.readBooleanParam(request,
			PedagogicalPlannerAction.PARAM_RETURN_TO_PARENT, false);
		// after editing the node, should user return to parent or to current node
		if (returnToParent) {
		    // if parent is null, do not set anything and user will return to root node
		    if (node.getParent() != null) {
			planner.setNodeUid(node.getParent().getUid());
		    }
		} else {
		    planner.setNodeUid(node.getUid());
		}
	    }
	}

	String forbidButtonsString = request.getParameter(PedagogicalPlannerAction.PARAM_FORBID_BUTTONS);
	planner.overridePermissions(forbidButtonsString);

	// Some additional options for submitting activity forms; should be moved to configuration file in the future
	planner.setSendInPortions(false);
	planner.setSubmitDelay(5000);
	planner.setActivitiesPerPortion(2);

	request.setAttribute(CentralConstants.ATTR_PLANNER, planner);

	return errors;
    }

    /**
     * Copies LearningDesign to common folder.
     *
     * @throws ServletException
     */
    private LearningDesign copyLearningDesign(Long originalDesignId, ActionMessages errors) throws ServletException {
	PedagogicalPlannerAction.log.debug("Copying LearningDesign to common folder");
	LearningDesign originalDesign = null;
	if (originalDesignId != null) {
	    originalDesign = getAuthoringService().getLearningDesign(originalDesignId);
	}
	if (originalDesign == null) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage("no.such.learningdesign.exist", new Object[] { originalDesignId }));
	    return null;
	}
	User user = getUser();
	WorkspaceFolder targetFolder = getCommonWorkspaceFolderId(user.getUserId());
	Integer copyType = LearningDesign.COPY_TYPE_NONE;
	boolean setOriginalDesign = false;
	String newDesignName = originalDesign.getTitle();
	String customCSV = null;

	return getAuthoringService().copyLearningDesign(originalDesign, copyType, user, targetFolder, setOriginalDesign,
		newDesignName, customCSV);
    }

    /**
     * Recognises activitiy type and creates proper DTO for web pages use. For branching and options it can be called
     * recursevely.
     *
     * @param learningDesign
     *            learning design from which activity was taken
     * @param activities
     *            set of DTOs
     * @param activity
     *            currently parsed activity
     * @return created DTO
     * @throws ServletException
     */
    @SuppressWarnings("unchecked")
    private PedagogicalPlannerActivityDTO addActivityToPlanner(LearningDesign learningDesign,
	    List<PedagogicalPlannerActivityDTO> activities, Activity activity) throws ServletException {
	PedagogicalPlannerAction.log.debug("Parsing activity: " + activity.getTitle());

	// Check if the activity is contained in some complex activity: branching or options
	boolean isNested = (activity.getParentActivity() != null)
		&& (activity.getParentActivity().isBranchingActivity() || activity.isOptionsActivity());

	PedagogicalPlannerActivityDTO addedDTO = null;

	// Load the real object, otherwise we get an error when casting to concrete Activity classes
	activity = getActivityDAO().getActivityByActivityId(activity.getActivityId());

	// Simple tool activity
	if (activity.isToolActivity()) {
	    ToolActivity toolActivity = (ToolActivity) activity;
	    Long toolContentID = toolActivity.getToolContentId();

	    // Every tool has an URL that leads to Action that returns proper tool form to use
	    String pedagogicalPlannerUrl = toolActivity.getTool().getPedagogicalPlannerUrl();
	    String authorUrl = WebUtil.appendParameterToURL(toolActivity.getTool().getAuthorUrl(),
		    AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentID.toString());
	    authorUrl = WebUtil.appendParameterToURL(authorUrl, AttributeNames.PARAM_CONTENT_FOLDER_ID,
		    learningDesign.getContentFolderID());

	    if (pedagogicalPlannerUrl == null) {
		// if there is no URL, the tool does not support the planner
		addedDTO = new PedagogicalPlannerActivityDTO(false,
			PedagogicalPlannerAction.PATH_ACTIVITY_NO_PLANNER_SUPPORT);
		addedDTO.setType(toolActivity.getTool().getToolDisplayName());
		addedDTO.setTitle(activity.getTitle());
		addedDTO.setAuthorUrl(authorUrl);
		addedDTO.setToolIconUrl(activity.getLibraryActivityUiImage());

		PedagogicalPlannerActivityMetadata plannerMetadata = toolActivity.getPlannerMetadata();
		if (plannerMetadata != null) {
		    addedDTO.setCollapsed(plannerMetadata.getCollapsed());
		    addedDTO.setExpanded(plannerMetadata.getExpanded());
		    addedDTO.setHidden(plannerMetadata.getHidden());
		}
	    } else {
		pedagogicalPlannerUrl = WebUtil.appendParameterToURL(pedagogicalPlannerUrl,
			AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentID.toString());
		pedagogicalPlannerUrl = WebUtil.appendParameterToURL(pedagogicalPlannerUrl,
			AttributeNames.PARAM_CONTENT_FOLDER_ID, learningDesign.getContentFolderID());

		// Looks heavy, but we just build URLs for DTO - see that class the meaning of constructor parameters
		addedDTO = new PedagogicalPlannerActivityDTO(true, pedagogicalPlannerUrl);
		addedDTO.setType(toolActivity.getTool().getToolDisplayName());
		addedDTO.setTitle(activity.getTitle());
		addedDTO.setAuthorUrl(authorUrl);
		addedDTO.setToolIconUrl(activity.getLibraryActivityUiImage());

		PedagogicalPlannerActivityMetadata plannerMetadata = toolActivity.getPlannerMetadata();
		if (plannerMetadata != null) {
		    addedDTO.setCollapsed(plannerMetadata.getCollapsed());
		    addedDTO.setExpanded(plannerMetadata.getExpanded());
		    addedDTO.setHidden(plannerMetadata.getHidden());
		    addedDTO.setEditingAdvice(plannerMetadata.getEditingAdvice());
		}
	    }
	    activities.add(addedDTO);
	} else if (activity.isGroupingActivity()) {
	    // grouping is managed by this action class;
	    GroupingActivity groupingActivity = (GroupingActivity) activity;
	    String pedagogicalPlannerUrl = WebUtil.appendParameterToURL(
		    groupingActivity.getSystemTool().getPedagogicalPlannerUrl(), AttributeNames.PARAM_TOOL_CONTENT_ID,
		    groupingActivity.getCreateGrouping().getGroupingId().toString());
	    addedDTO = new PedagogicalPlannerActivityDTO(true, pedagogicalPlannerUrl);
	    addedDTO.setTitle(activity.getTitle());
	    addedDTO.setToolIconUrl(PedagogicalPlannerAction.IMAGE_PATH_GROUPING);
	    activities.add(addedDTO);
	} else if (activity.isGateActivity()) {
	    // gate is not supported, but takes its image from a differen spot
	    addedDTO = new PedagogicalPlannerActivityDTO(false,
		    PedagogicalPlannerAction.PATH_ACTIVITY_NO_PLANNER_SUPPORT);
	    addedDTO.setTitle(activity.getTitle());
	    addedDTO.setToolIconUrl(PedagogicalPlannerAction.IMAGE_PATH_GATE);
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
		    String path = WebUtil.appendParameterToURL(
			    PedagogicalPlannerAction.PATH_ACTIVITY_NO_PLANNER_SUPPORT,
			    CentralConstants.PARAM_FORM_MESSAGE,
			    getMessageService().getMessage(CentralConstants.RESOURCE_KEY_BRANCH_EMPTY));
		    addedDTO = new PedagogicalPlannerActivityDTO(false, path);
		    addedDTO.setParentActivityTitle(activity.getTitle());
		    addedDTO.setGroup(branch);
		    addedDTO.setDefaultBranch(defaultBranch);
		    addedDTO.setComplexActivityType(PedagogicalPlannerActivityDTO.TYPE_BRANCHING_ACTIVITY);
		    activities.add(addedDTO);
		} else {
		    // Iterate through all the activities from the sequence, adding them to Planner activity set
		    do {
			addedDTO = addActivityToPlanner(learningDesign, activities, nestedActivity);
			Transition transitionFrom = nestedActivity.getTransitionFrom();
			if (transitionFrom == null) {
			    nestedActivity = null;
			} else {
			    nestedActivity = transitionFrom.getToActivity();
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
	    addedDTO = new PedagogicalPlannerActivityDTO(false,
		    PedagogicalPlannerAction.PATH_ACTIVITY_NO_PLANNER_SUPPORT);
	    addedDTO.setTitle(activity.getTitle());
	    addedDTO.setToolIconUrl(activity.getLibraryActivityUiImage());
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
	// TODO: shall we make a copy for preview???
	// either get the design ID or we don't know it and need to extract it from the node
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID, true);
	Long learningDesignId = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID, true);
	if ((learningDesignId == null) && (nodeUid != null)) {
	    PedagogicalPlannerSequenceNode node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	    learningDesignId = node.getLearningDesignId();
	}

	HttpSession session = SessionManager.getSession();
	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);

	// Start preview the same way as in authoring
	PedagogicalPlannerAction.log.debug("Opening preview for learnind design id: " + learningDesignId);
	Lesson lesson = getMonitoringService().initializeLessonForPreview("Preview", null, learningDesignId,
		userDto.getUserID(), null, false, false, false);
	getMonitoringService().createPreviewClassForLesson(userDto.getUserID(), lesson.getLessonId());

	getMonitoringService().startLesson(lesson.getLessonId(), userDto.getUserID());
	String newPath = WebUtil.appendParameterToURL(
		mapping.findForward(PedagogicalPlannerAction.FORWARD_PREVIEW).getPath(), AttributeNames.PARAM_LESSON_ID,
		lesson.getLessonId().toString());
	newPath = WebUtil.appendParameterToURL(newPath, AttributeNames.PARAM_MODE, "preview");
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
	String filterText = WebUtil.readStrParam(request, CentralConstants.PARAM_FILTER_TEXT, true);
	// Do we display the root (top) node or an existing one
	PedagogicalPlannerSequenceNode node = null;
	if (nodeUid == null) {
	    node = getPedagogicalPlannerDAO().getRootNode();
	} else {
	    node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	}
	PedagogicalPlannerAction.log.debug("Opening sequence node with UID: " + nodeUid);

	// Only certain roles can open the editor
	boolean isSysAdmin = request.isUserInRole(Role.SYSADMIN);
	boolean canEdit = canEditNode(request, nodeUid);
	Boolean edit = WebUtil.readBooleanParam(request, CentralConstants.PARAM_EDIT, false);
	edit &= canEdit;

	// No filtering or something went wrong in filtering
	PedagogicalPlannerSequenceNodeDTO dto = new PedagogicalPlannerSequenceNodeDTO(node, node.getSubnodes(),
		isSysAdmin, getPedagogicalPlannerDAO());
	if (nodeUid == null) {
	    dto.setRecentlyModifiedNodes(getRecentlyModifiedLearnindDesignsAsNodes());
	}

	// Additional DTO parameters
	List<String[]> titlePath = getPedagogicalPlannerDAO().getTitlePath(nodeUid);
	Boolean createSubnode = WebUtil.readBooleanParam(request, CentralConstants.PARAM_CREATE_SUBNODE, false);
	Boolean importNode = WebUtil.readBooleanParam(request, CentralConstants.PARAM_IMPORT_NODE, false);
	dto.setCreateSubnode(createSubnode);
	dto.setEdit(edit);
	dto.setIsEditor(canEdit);
	dto.setImportNode(importNode);
	dto.setTitlePath(titlePath);
	request.setAttribute(CentralConstants.ATTR_NODE, dto);

	// Set doNotShowAgain parameter
	HttpSession session = SessionManager.getSession();
	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	boolean doNotShowAgain = (userDto.getPagesWithDisabledTutorials() != null)
		&& userDto.getPagesWithDisabledTutorials().contains(PedagogicalPlannerAction.PAGE_STRING_START_PLANNER);
	request.setAttribute(AttributeNames.ATTR_DO_NOT_SHOW_AGAIN, doNotShowAgain);
	request.setAttribute(AttributeNames.ATTR_PAGE_STR, PedagogicalPlannerAction.PAGE_STRING_START_PLANNER);

	if (edit) {
	    // If we are in edit mode, the node form is displayed, requiring additional parameters
	    PedagogicalPlannerSequenceNodeForm nodeForm = (PedagogicalPlannerSequenceNodeForm) form;

	    if (createSubnode) {
		// We create a new node, rather than edit the existing one
		if (node.getContentFolderId() == null) {
		    // If it's a new top level node, we create an uniuqe ID
		    String contentFolderId = FileUtil.generateUniqueContentFolderID();
		    nodeForm.setContentFolderId(contentFolderId);
		} else {
		    // Whole node tree share the same content folder ID
		    nodeForm.setContentFolderId(node.getContentFolderId());
		}
		// set the default permissions
		nodeForm.setPermissions(null);
	    } else if (!importNode) {
		// We fill the form with necessary data
		nodeForm.setNodeType(
			node.getLearningDesignId() == null ? PedagogicalPlannerSequenceNodeForm.NODE_TYPE_SUBNODES
				: PedagogicalPlannerSequenceNodeForm.NODE_TYPE_TEMPLATE);
		nodeForm.setRemoveTemplate(false);
		nodeForm.setTitle(dto.getTitle());
		nodeForm.setBriefDescription(dto.getBriefDescription());
		nodeForm.setFullDescription(dto.getFullDescription());
		nodeForm.setPermissions(node.getPermissions());
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

	boolean newRootNode = false;
	Long parentUid = null;
	if (nodeUid == null) {
	    // It's a new subnode
	    node = new PedagogicalPlannerSequenceNode();
	    parentUid = nodeForm.getParentUid() == 0 ? null : nodeForm.getParentUid();
	    if (parentUid != null) {
		PedagogicalPlannerSequenceNode parent = getPedagogicalPlannerDAO().getByUid(parentUid);
		node.setParent(parent);
	    } else {
		newRootNode = true;
	    }
	    node.setOrder(getPedagogicalPlannerDAO().getNextOrderId(parentUid));
	    node.setUser(getUser());
	} else {
	    // It's an existing node
	    node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	    nodeUid = node.getUid();
	}

	// either user can edit current node or he is creating a subnode,
	// so he needs permissions for the parent node
	if (nodeUid == null ? (parentUid == null ? !canEditNode(request, nodeUid) : !canEditNode(request, parentUid))
		: !canEditNode(request, nodeUid)) {
	    PedagogicalPlannerAction.log.debug("Unauthorised attempt to saveSequenceNode");
	    throw new UserAccessDeniedException();
	}
	PedagogicalPlannerAction.log.debug("Saving sequence node with UID: " + nodeUid);

	// If anything goes wrong, we need to put back these values
	String title = nodeForm.getTitle();
	String briefDescription = nodeForm.getBriefDescription();
	String fullDescription = nodeForm.getFullDescription();
	String nodeType = nodeForm.getNodeType();
	int nodePermissions = nodeForm.getPermissions();

	ActionMessages errors = validateNodeForm(node, nodeForm);
	if (errors.isEmpty()) {

	    node.setTitle(title);
	    node.setBriefDescription(briefDescription);
	    node.setFullDescription(fullDescription);
	    node.setContentFolderId(nodeForm.getContentFolderId());
	    node.setPermissions(nodePermissions);

	    // Different properties are set, depending on node type: with subnodes or template
	    if (PedagogicalPlannerSequenceNodeForm.NODE_TYPE_SUBNODES.equals(nodeForm.getNodeType())
		    || Boolean.TRUE.equals(nodeForm.getRemoveTemplate())) {
		/*
		 * LearningDesign learningDesign = getAuthoringService().getLearningDesign(node.getLearningDesignId());
		 * getAuthoringService().deleteLearningDesign(learningDesign);
		 */
		node.setLearningDesignId(null);
		node.setLearningDesignTitle(null);
		node.setPermissions(PedagogicalPlannerSequenceNode.PERMISSION_DEFAULT_SETTING);
	    } else if ((nodeForm.getFile() != null) && (nodeForm.getFile().getFileSize() > 0)) {
		// file has been uploaded, so copy it to file system and import LD
		FormFile file = nodeForm.getFile();
		InputStream inputStream = file.getInputStream();
		File sourceFile = new File(FileUtil.getTempDir(), file.getFileName());

		try {
		    FileUtils.copyInputStreamToFile(inputStream, sourceFile);
		} catch (Exception e) {
		    PedagogicalPlannerAction.log.error(e, e);
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_FILE_OPEN));
		}
		if (!sourceFile.exists() || sourceFile.isDirectory()) {
		    PedagogicalPlannerAction.log.error(PedagogicalPlannerAction.ERROR_NOT_PROPER_FILE);
		    errors.add(ActionMessages.GLOBAL_MESSAGE,
			    new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_FILE_OPEN));
		}
		if (errors.isEmpty()) {
		    LearningDesign learningDesign = importLearningDesign(sourceFile, errors);
		    if (errors.isEmpty()) {
			updateRecentLearningDesignList(learningDesign.getLearningDesignId());

			node.setLearningDesignId(learningDesign.getLearningDesignId());
			node.setLearningDesignTitle(learningDesign.getTitle());

			// If there were subnodes, we delete them now
			Iterator<PedagogicalPlannerSequenceNode> subnodeIter = node.getSubnodes().iterator();
			while (subnodeIter.hasNext()) {
			    PedagogicalPlannerSequenceNode subnode = subnodeIter.next();
			    subnodeIter.remove();
			    getPedagogicalPlannerDAO().removeNode(subnode);
			}
		    }
		}
	    }

	    getPedagogicalPlannerDAO().saveOrUpdateNode(node);
	    // If it was a new subnode, we need to retrieved the assigned UID
	    nodeUid = node.getUid();
	    // If it was a new root node, add creator's role
	    if (newRootNode) {
		try {
		    HttpSession s = SessionManager.getSession();
		    UserDTO u = (UserDTO) s.getAttribute(AttributeNames.USER);
		    getPedagogicalPlannerDAO().saveNodeRole(u.getUserID(), nodeUid, Role.ROLE_SYSADMIN);
		} catch (Exception e) {
		    PedagogicalPlannerAction.log
			    .error("Error saving role for newly created root node: " + e.getMessage());
		    e.printStackTrace();
		}
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
	    nodeForm.setPermissions(nodePermissions);
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
    private ActionMessages validateNodeForm(PedagogicalPlannerSequenceNode node,
	    PedagogicalPlannerSequenceNodeForm form) {
	ActionMessages errors = new ActionMessages();
	// Title must not be blank
	if (StringUtils.isEmpty(form.getTitle())) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_NODE_TITLE_BLANK));
	}
	// Template must a proper file
	if (PedagogicalPlannerSequenceNodeForm.NODE_TYPE_TEMPLATE.equals(form.getNodeType())
		&& (node.getLearningDesignTitle() == null)) {
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
	if ((form.getFile() == null) || (form.getFile().getFileSize() == 0)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_FILE_EMPTY));
	} else {
	    String fileName = form.getFile().getFileName();
	    boolean badExtension = false;
	    if (fileName.length() >= 4) {
		String extension = fileName.substring(fileName.length() - 4);
		if (!(extension.equalsIgnoreCase(PedagogicalPlannerAction.FILE_EXTENSION_LAS)
			|| extension.equalsIgnoreCase(PedagogicalPlannerAction.FILE_EXTENSION_ZIP))) {
		    badExtension = true;
		}
	    } else {
		badExtension = true;
	    }
	    if (badExtension) {
		errors.add(ActionMessages.GLOBAL_MESSAGE,
			new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_FILE_BAD_EXTENSION));
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
     * @throws UserAccessDeniedException
     */
    public ActionForward removeSequenceNode(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, UserAccessDeniedException {
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID);
	PedagogicalPlannerSequenceNode node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	Long parentUid = node.getParent() == null ? null : node.getParent().getUid();
	if (canRemoveSubtree(request, nodeUid)) {
	    PedagogicalPlannerAction.log.debug("Removing sequence node with UID" + nodeUid);
	    getPedagogicalPlannerDAO().removeNode(node);

	} else {
	    ActionMessages errors = new ActionMessages();
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_REMOVE_NODE_TREE));
	    saveErrors(request, errors);
	    PedagogicalPlannerAction.log.debug("Unauthorised attempt to removeSequenceNode");
	}
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

	    response.setContentType(CentralConstants.RESPONSE_CONTENT_TYPE_DOWNLOAD);
	    // response.setContentType("application/zip");
	    response.setHeader(CentralConstants.HEADER_CONTENT_DISPOSITION, "attachment;filename=" + filename);
	} catch (Exception e) {
	    PedagogicalPlannerAction.log.error(e, e);
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_EXPORT));
	    saveErrors(request, errors);
	    return openSequenceNode(mapping, form, request, nodeUid);
	}
	FileUtils.copyFile(new File(zipFilePath), response.getOutputStream());
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
     * @throws ExportToolContentException
     */
    private String exportNode(Long nodeUid) throws ZipFileUtilException, FileUtilException, IOException,
	    RepositoryCheckedException, ExportToolContentException {
	if (nodeUid != null) {

	    String rootDir = FileUtil.createTempDirectory(PedagogicalPlannerAction.EXPORT_NODE_FOLDER_SUFFIX);
	    String contentDir = FileUtil.getFullPath(rootDir, PedagogicalPlannerAction.DIR_CONTENT);
	    FileUtil.createDirectory(contentDir);

	    String nodeFileName = FileUtil.getFullPath(contentDir, PedagogicalPlannerAction.NODE_FILE_NAME);
	    Writer nodeFile = new OutputStreamWriter(new FileOutputStream(nodeFileName),
		    PedagogicalPlannerAction.ENCODING_UTF_8);

	    PedagogicalPlannerSequenceNode node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	    // exporting XML
	    XStream designXml = new XStream(new StaxDriver());
	    designXml.addPermission(AnyTypePermission.ANY);
	    // do not serialize node's owner
	    designXml.omitField(PedagogicalPlannerSequenceNode.class, "user");
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
		PedagogicalPlannerAction.log
			.debug("Create export node content target zip file. File name is " + targetZipFileName);
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
     * Imports a zipped node. This method is based on a similar one for importing learning designs.
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

	if (!canEditNode(request, null)) {
	    PedagogicalPlannerAction.log.debug("Unauthorised access to importNode");
	    throw new UserAccessDeniedException();
	}

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
		FileUtils.copyInputStreamToFile(inputStream, importFile);

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
		importSubnodeTemplates(node, templateDir, errors);

		// The imported node is added as the last one
		Integer order = getPedagogicalPlannerDAO().getNextOrderId(null);
		node.setOrder(order);
		getPedagogicalPlannerDAO().saveOrUpdateNode(node);
	    } catch (Exception e) {
		PedagogicalPlannerAction.log.error(e, e);
		errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_IMPORT));
	    }
	}
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	}
	return openSequenceNode(mapping, form, request, (Long) null);
    }

    /**
     * Imports a learning design to bind it with a certain node.
     *
     * @param fileUuid
     * @param fileName
     * @param errors
     * @return
     * @throws ServletException
     */
    @SuppressWarnings("unchecked")
    private LearningDesign importLearningDesign(File sourceFile, ActionMessages errors) throws ServletException {
	User user = getUser();
	List<String> toolsErrorMsgs = new ArrayList<>();
	Long learningDesignID = null;
	LearningDesign learningDesign = null;
	List<String> learningDesignErrorMsgs = new ArrayList<>();

	Integer workspaceFolderId = null;

	// getWorkspaceFolderId(user.getUserId());

	// Extract the template
	try {
	    Object[] ldResults = getExportService().importLearningDesign(sourceFile, user, workspaceFolderId,
		    toolsErrorMsgs, "");
	    sourceFile.delete();
	    learningDesignID = (Long) ldResults[0];
	    learningDesignErrorMsgs = (List<String>) ldResults[1];
	    toolsErrorMsgs = (List<String>) ldResults[2];
	    learningDesign = getAuthoringService().getLearningDesign(learningDesignID);
	} catch (ImportToolContentException e) {
	    PedagogicalPlannerAction.log.error(e, e);
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED));

	}

	if (((learningDesignID == null) || (learningDesignID.longValue() == -1))
		&& (learningDesignErrorMsgs.size() == 0)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED));
	    return null;
	}
	if (learningDesignErrorMsgs.size() > 0) {
	    for (String error : learningDesignErrorMsgs) {
		PedagogicalPlannerAction.log.error(error);
	    }
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED));
	    return null;
	}
	if (toolsErrorMsgs.size() > 0) {
	    for (String error : toolsErrorMsgs) {
		PedagogicalPlannerAction.log.error(error);
	    }
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_TOOL_ERRORS));
	    return null;
	}
	return learningDesign;
    }

    /**
     * Returns WorkspaceFolder where imported learning designs can be stored.
     */
    @SuppressWarnings("unchecked")
    private WorkspaceFolder getCommonWorkspaceFolderId(Integer userID) {
	String name = PedagogicalPlannerAction.PLANNER_FOLDER_NAME;

	WorkspaceFolder parentFolder = getUserManagementService().getRootOrganisation().getNormalFolder();
	Integer workspaceFolderType = WorkspaceFolder.PUBLIC_SEQUENCES;

	Map<String, Object> properties = new HashMap<>();
	properties.put("name", name);
	properties.put("parentWorkspaceFolder.workspaceFolderId", parentFolder.getWorkspaceFolderId());
	properties.put("workspaceFolderType", workspaceFolderType);
	List<WorkspaceFolder> workspaceFolderList = PedagogicalPlannerAction.userManagementService
		.findByProperties(WorkspaceFolder.class, properties);

	WorkspaceFolder workspaceFolder = null;
	if ((workspaceFolderList != null) && (workspaceFolderList.size() > 0)) {
	    workspaceFolder = workspaceFolderList.get(0);
	} else {
	    workspaceFolder = new WorkspaceFolder(name, parentFolder, userID, new Date(), new Date(),
		    WorkspaceFolder.PUBLIC_SEQUENCES);
	    PedagogicalPlannerAction.userManagementService.save(workspaceFolder);
	}
	return workspaceFolder;
    }

    /**
     * Returns current user stored in session.
     *
     * @throws ServletException
     */
    private User getUser() throws ServletException {
	HttpSession session = SessionManager.getSession();
	UserDTO userDto = (UserDTO) session.getAttribute(AttributeNames.USER);
	User user = (User) getUserManagementService().findById(User.class, userDto.getUserID());
	if (user == null) {
	    throw new ServletException(PedagogicalPlannerAction.ERROR_USER_NOT_FOUND);
	}
	return user;
    }

    /**
     * Export the subnodes' templates into the selected dir.
     *
     * @param node
     * @param outputDir
     * @throws ExportToolContentException
     * @throws RepositoryCheckedException
     * @throws IOException
     */
    private void exportSubnodeTemplates(PedagogicalPlannerSequenceNode node, File outputDir)
	    throws IOException, RepositoryCheckedException, ExportToolContentException {
	if (node != null) {
	    if (node.getLearningDesignId() == null) {
		if (node.getSubnodes() != null) {
		    for (PedagogicalPlannerSequenceNode subnode : node.getSubnodes()) {
			exportSubnodeTemplates(subnode, outputDir);
		    }
		}
	    } else {

		List<String> toolsErrorMsgs = new ArrayList<>();
		String exportedLdFilePath = getExportService().exportLearningDesign(node.getLearningDesignId(),
			toolsErrorMsgs);
		if (!toolsErrorMsgs.isEmpty()) {
		    StringBuffer errorMessages = new StringBuffer();
		    for (String error : toolsErrorMsgs) {
			errorMessages.append(error);
		    }
		    throw new ExportToolContentException(errorMessages.toString());
		}
		FileInputStream inputStream = new FileInputStream(exportedLdFilePath);

		File ldIdDir = new File(outputDir, node.getLearningDesignId().toString());
		ldIdDir.mkdirs();
		File targetFile = new File(ldIdDir,
			node.getLearningDesignTitle() + PedagogicalPlannerAction.FILE_EXTENSION_ZIP);

		PedagogicalPlannerAction.log
			.debug("Preparing for zipping the template file: " + node.getLearningDesignTitle());
		FileUtils.copyInputStreamToFile(inputStream, targetFile);
	    }
	}
    }

    /**
     * Imports back the subnodes' templates. Also sets all the nodes' UIDs to NULL.
     *
     * @param node
     * @param inputDir
     * @throws ServletException
     */
    private void importSubnodeTemplates(PedagogicalPlannerSequenceNode node, File inputDir, ActionMessages errors)
	    throws ServletException {
	if (node != null) {
	    node.setUid(null);
	    User user = getUser();
	    node.setUser(user);

	    if (node.getLearningDesignId() == null) {
		if (node.getSubnodes() != null) {
		    for (PedagogicalPlannerSequenceNode subnode : node.getSubnodes()) {
			importSubnodeTemplates(subnode, inputDir, errors);
			subnode.setParent(node);
		    }
		}
	    } else {
		File ldIdDir = new File(inputDir, node.getLearningDesignId().toString());
		String fileName = node.getLearningDesignTitle() + PedagogicalPlannerAction.FILE_EXTENSION_ZIP;
		File sourceFile = new File(ldIdDir, fileName);
		PedagogicalPlannerAction.log.debug("Importing a template file: " + fileName);
		LearningDesign learningDesign = importLearningDesign(sourceFile, errors);

		node.setLearningDesignId(learningDesign.getLearningDesignId());
		node.setLearningDesignTitle(learningDesign.getTitle());
	    }
	}
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
	return mapping.findForward(PedagogicalPlannerAction.FORWARD_GROUPING);
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
		Integer number = StringUtils.isEmpty(plannerForm.getNumberOfGroups()) ? null
			: Integer.parseInt(plannerForm.getNumberOfGroups());
		randomGrouping.setNumberOfGroups(number);

		number = StringUtils.isEmpty(plannerForm.getLearnersPerGroup()) ? null
			: Integer.parseInt(plannerForm.getLearnersPerGroup());
		randomGrouping.setLearnersPerGroup(number);
	    } else if (grouping.isLearnerChoiceGrouping()) {
		LearnerChoiceGrouping learnerChoiceGrouping = (LearnerChoiceGrouping) grouping;
		Integer number = StringUtils.isEmpty(plannerForm.getNumberOfGroups()) ? null
			: Integer.parseInt(plannerForm.getNumberOfGroups());
		learnerChoiceGrouping.setNumberOfGroups(number);

		number = StringUtils.isEmpty(plannerForm.getLearnersPerGroup()) ? null
			: Integer.parseInt(plannerForm.getLearnersPerGroup());
		learnerChoiceGrouping.setLearnersPerGroup(number);
		learnerChoiceGrouping.setEqualNumberOfLearnersPerGroup(plannerForm.getEqualGroupSizes());
		learnerChoiceGrouping.setViewStudentsBeforeSelection(plannerForm.getViewStudentsBeforeSelection());
	    } else {
		Integer number = StringUtils.isEmpty(plannerForm.getNumberOfGroups()) ? null
			: Integer.parseInt(plannerForm.getNumberOfGroups());
		grouping.setMaxNumberOfGroups(number);
	    }
	} else {
	    saveMessages(request, errors);
	}
	return mapping.findForward(PedagogicalPlannerAction.FORWARD_GROUPING);
    }

    private List<PedagogicalPlannerSequenceNodeDTO> getRecentlyModifiedLearnindDesignsAsNodes()
	    throws ServletException {
	// Add the recently modified learning design list, if it's the root node with no filtering
	User user = getUser();
	// the list is sorted most-recently-edited-on-top (so by the timestamp descending)
	Set<Long> recentLDs = user.getRecentlyModifiedLearningDesigns();
	List<PedagogicalPlannerSequenceNodeDTO> recentNodes = new LinkedList<>();
	// create "dummy", almost empty nodes
	for (Long learningDesignId : recentLDs) {
	    LearningDesign learningDesign = getAuthoringService().getLearningDesign(learningDesignId);

	    PedagogicalPlannerSequenceNodeDTO node = new PedagogicalPlannerSequenceNodeDTO();
	    node.setTitle(learningDesign.getTitle());
	    node.setLearningDesignId(learningDesignId);

	    recentNodes.add(node);
	}
	return recentNodes;
    }

    /**
     * Adds the Learning Design to the list of recenlty edited sequences. It puts the selected LD on the top of the
     * list.
     *
     * @param learningDesignId
     */
    private void updateRecentLearningDesignList(Long learningDesignId) throws ServletException {
	User user = getUser();
	// the list is sorted most-recently-edited-on-top (so by the timestamp descending)
	Set<Long> recentLDs = user.getRecentlyModifiedLearningDesigns();
	boolean ldFound = false;
	// if LD is already in the list, remove it and add it again - it a way to refresh the timestamp in DB
	Iterator<Long> iter = recentLDs.iterator();
	while (iter.hasNext()) {
	    Long recentLD = iter.next();
	    if (recentLD.equals(learningDesignId)) {
		iter.remove();
		getUserManagementService().saveUser(user);
		ldFound = true;
		break;
	    }
	}
	// if LD was not in the list, but the list is full, remove the last entry
	if (!ldFound && (recentLDs.size() >= CentralConstants.PLANNER_RECENT_LD_MAX_COUNT)) {
	    iter.remove();
	}
	recentLDs.add(learningDesignId);
	getUserManagementService().save(user);
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
     * @throws ServletException
     */
    @SuppressWarnings("unchecked")
    public ActionForward saveSequenceDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	PedagogicalPlannerAction.log.debug("Saving sequence title");
	String sequenceTitle = WebUtil.readStrParam(request, CentralConstants.PARAM_SEQUENCE_TITLE, true);
	Long learningDesignID = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID);
	Integer callAttemptedID = WebUtil.readIntParam(request, CentralConstants.PARAM_CALL_ATTEMPTED_ID);
	// We send a message in format "<response>&<call ID>"; it is then parsed in JavaScript
	String responseSuffix = "&" + callAttemptedID;

	if (StringUtils.isEmpty(sequenceTitle)) {
	    String blankTitleError = getMessageService().getMessage(CentralConstants.ERROR_PLANNER_TITLE_BLANK);
	    writeAJAXResponse(response, blankTitleError + responseSuffix);
	} else {
	    LearningDesign learningDesign = getAuthoringService().getLearningDesign(learningDesignID);
	    learningDesign.setTitle(sequenceTitle);

	    // parse activity metadata, which is in form "activity<tool_content_id>.<field_name>=<value)&..."
	    String activityMetadataString = WebUtil.readStrParam(request, CentralConstants.PARAM_ACTIVITY_METADATA,
		    true);
	    if (!StringUtils.isEmpty(activityMetadataString)) {
		String[] activityMetadataEntries = activityMetadataString.split("&");
		// creata a map of metadata objects, because we are filling them multiple times during this iteration
		Map<Long, PedagogicalPlannerActivityMetadata> activitiesMetadata = new TreeMap<>();
		for (String activityMetadataEntry : activityMetadataEntries) {
		    String[] keyAndValue = activityMetadataEntry.split("=");
		    String[] keyParts = keyAndValue[0].split("\\.");
		    String toolContentIdString = keyParts[0]
			    .substring(PedagogicalPlannerAction.ACTIVITY_METADATA_PREFIX.length());
		    Long toolContentId = Long.parseLong(toolContentIdString);

		    PedagogicalPlannerActivityMetadata plannerMetadata = activitiesMetadata.get(toolContentId);
		    if (plannerMetadata == null) {
			plannerMetadata = new PedagogicalPlannerActivityMetadata();
			activitiesMetadata.put(toolContentId, plannerMetadata);
		    }

		    String fieldName = keyParts[1];
		    String value = keyAndValue[1];

		    // recognise fields and set properties
		    if (PedagogicalPlannerAction.ACTIVITY_METADATA_COLLAPSED.equalsIgnoreCase(fieldName)) {
			plannerMetadata.setCollapsed(Boolean.parseBoolean(value));
		    } else if (PedagogicalPlannerAction.ACTIVITY_METADATA_EXPANDED.equalsIgnoreCase(fieldName)) {
			plannerMetadata.setExpanded(Boolean.parseBoolean(value));
		    } else if (PedagogicalPlannerAction.ACTIVITY_METADATA_HIDDEN.equalsIgnoreCase(fieldName)) {
			plannerMetadata.setHidden(Boolean.parseBoolean(value));
		    } else if (PedagogicalPlannerAction.ACTIVITY_METADATA_EDITING_ADVICE.equalsIgnoreCase(fieldName)) {
			plannerMetadata.setEditingAdvice(value);
		    }
		}

		// assign metadata to activities
		if (!activitiesMetadata.isEmpty()) {
		    for (Activity activity : (Set<Activity>) learningDesign.getActivities()) {
			if (activity.isToolActivity()) {
			    activity = getActivityDAO().getActivityByActivityId(activity.getActivityId());
			    ToolActivity toolActivity = (ToolActivity) activity;
			    PedagogicalPlannerActivityMetadata plannerMetadata = activitiesMetadata
				    .get(toolActivity.getToolContentId());
			    if (plannerMetadata != null) {
				PedagogicalPlannerActivityMetadata storedMetadata = toolActivity.getPlannerMetadata();
				if (storedMetadata == null) {
				    plannerMetadata.setActivity(toolActivity);
				    toolActivity.setPlannerMetadata(plannerMetadata);
				} else {
				    storedMetadata.copyProperties(plannerMetadata);
				}
			    }
			}
		    }
		}
	    }

	    learningDesign.setLastModifiedDateTime(new Date());
	    String copyMode = WebUtil.readStrParam(request, CentralConstants.PARAM_COPY_MODE, true);
	    if (PedagogicalPlannerAction.COPY_MODE_MOVE_CURRRENT.equalsIgnoreCase(copyMode)) {
		// if temporary copy (only for browsing) was edited and now saved,
		// we move it to user's folder as a hard copy
		User user = getUser();
		WorkspaceFolder userFolder = user.getWorkspaceFolder();
		learningDesign.setWorkspaceFolder(userFolder);
	    }
	    getAuthoringService().saveLearningDesign(learningDesign);

	    updateRecentLearningDesignList(learningDesignID);
	    writeAJAXResponse(response, "OK" + responseSuffix);
	}
	return null;
    }

    public ActionForward exportTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	Long learningDesignId = WebUtil.readLongParam(request, CentralConstants.PARAM_LEARNING_DESIGN_ID);

	List<String> toolsErrorMsgs = new ArrayList<>();
	ActionMessages errors = new ActionMessages();
	String zipFilePath = null;
	boolean valid = false;
	try {
	    zipFilePath = getExportService().exportLearningDesign(learningDesignId, toolsErrorMsgs);
	    if (toolsErrorMsgs.isEmpty()) {

		// get only filename
		String zipfile = FileUtil.getFileName(zipFilePath);

		// replace spaces (" ") with underscores ("_")

		zipfile = zipfile.replaceAll(" ", "_");

		// write zip file as response stream.

		// Different browsers handle stream downloads differently LDEV-1243
		String filename = FileUtil.encodeFilenameForDownload(request, zipfile);
		PedagogicalPlannerAction.log.debug("Final filename to export: " + filename);

		response.setContentType(CentralConstants.RESPONSE_CONTENT_TYPE_DOWNLOAD);
		// response.setContentType("application/zip");
		response.setHeader(CentralConstants.HEADER_CONTENT_DISPOSITION, "attachment;filename=" + filename);
		valid = true;
	    }

	} catch (Exception e) {
	    PedagogicalPlannerAction.log.error(e, e);
	}
	if (!valid) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE,
		    new ActionMessage(PedagogicalPlannerAction.ERROR_KEY_EXPORT_TEMPLATE));
	    saveErrors(request, errors);
	    for (String error : toolsErrorMsgs) {
		PedagogicalPlannerAction.log.error(error);
	    }
	    LearningDesign learningDesign = getAuthoringService().getLearningDesign(learningDesignId);
	    errors = openTemplate(request, learningDesign);
	    if (!errors.isEmpty()) {
		throw new ServletException(getMessageService()
			.getMessage(PedagogicalPlannerAction.ERROR_KEY_LEARNING_DESIGN_COULD_NOT_BE_RETRIEVED));
	    }
	    return mapping.findForward(PedagogicalPlannerAction.FORWARD_TEMPLATE);
	}

	FileUtils.copyFile(new File(zipFilePath), response.getOutputStream());
	return null;
    }

    @SuppressWarnings("unchecked")
    public ActionForward addRemoveEditors(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID, true);

	if (canEditNode(request, nodeUid)) {
	    List<User> existingUsers = getPedagogicalPlannerDAO().getNodeUsers(nodeUid, Role.ROLE_SYSADMIN);

	    Integer orgId = getUserManagementService().getRootOrganisation().getOrganisationId();
	    Vector<User> potentialUsersVector = getUserManagementService().getUsersFromOrganisationByRole(orgId,
		    Role.SYSADMIN, true);

	    // list existing users (inherited role from parent nodes)
	    Set<User> allInheritedUsers = getPedagogicalPlannerDAO().getInheritedNodeUsers(nodeUid, Role.ROLE_SYSADMIN);
	    ArrayList<User> filteredInheritedUsers = new ArrayList<>();
	    for (Object o : allInheritedUsers) {
		User u = (User) o;
		// filter existing users of the actual node
		if (existingUsers.contains(u)) {
		    continue;
		}
		filteredInheritedUsers.add(u);
	    }

	    // filter existing users from list of potential users
	    ArrayList<User> potentialUsers = new ArrayList<>();
	    for (Object o : potentialUsersVector) {
		User u = (User) o;
		if (existingUsers.contains(u) || allInheritedUsers.contains(u)) {
		    continue;
		}
		// filter self
		if (StringUtils.equals(u.getLogin(), request.getRemoteUser())) {
		    continue;
		}
		potentialUsers.add(u);
	    }

	    request.setAttribute("existingUsers", existingUsers);
	    request.setAttribute("potentialUsers", potentialUsers);
	    request.setAttribute("inheritedUsers", filteredInheritedUsers);

	    return mapping.findForward("editAuthors");
	} else {
	    PedagogicalPlannerAction.log.debug("Unauthorised attempt to access add/remove editors page.");
	    throw new UserAccessDeniedException();
	}
    }

    public ActionForward addEditor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer userId = WebUtil.readIntParam(request, CentralConstants.PARAM_USER_ID, false);
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID, true);

	if (canEditNode(request, nodeUid)) {
	    getPedagogicalPlannerDAO().saveNodeRole(userId, nodeUid, Role.ROLE_SYSADMIN);
	} else {
	    PedagogicalPlannerAction.log.debug("Unauthorised attempt to add editor to node.");
	}

	return null;
    }

    public ActionForward removeEditor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer userId = WebUtil.readIntParam(request, CentralConstants.PARAM_USER_ID, false);
	Long nodeUid = WebUtil.readLongParam(request, CentralConstants.PARAM_UID, false);

	if (canEditNode(request, nodeUid)) {
	    getPedagogicalPlannerDAO().removeNodeRole(userId, nodeUid, Role.ROLE_SYSADMIN);
	} else {
	    PedagogicalPlannerAction.log.debug("Unauthorised attempt to remove editor from node.");
	}

	return null;
    }

    /*------------------------ COMMON METHODS --------------------*/

    // only these roles can edit nodes and give this role on this node to others
    private boolean canEditNode(HttpServletRequest request, Long nodeUid) {
	return isNodeOwnerOrSuperuser(request, nodeUid) || isEditor(request, nodeUid);
    }

    private boolean isNodeOwnerOrSuperuser(HttpServletRequest request, Long nodeUid) {
	if (request.isUserInRole(Role.SYSADMIN)) {
	    // sysadmins have all permission
	    return true;
	} else {
	    if (nodeUid == null) {
		// all global author admins (GAA) can create and edit at the root level
		return getUserManagementService().isUserSysAdmin();
	    } else {
		// at any other node, the user needs to be the node's owner
		// or linked to that node or one of its parents
		User user = getUserManagementService().getUserByLogin(request.getRemoteUser());
		PedagogicalPlannerSequenceNode node = getPedagogicalPlannerDAO().getByUid(nodeUid);
		User nodeOwner = node.getUser();
		return (nodeOwner != null) && user.equals(nodeOwner);
	    }
	}
    }

    private boolean isEditor(HttpServletRequest request, Long nodeUid) {
	User user = getUserManagementService().getUserByLogin(request.getRemoteUser());
	return getPedagogicalPlannerDAO().isEditor(user.getUserId(), nodeUid, Role.ROLE_SYSADMIN);
    }

    private boolean canRemoveSubtree(HttpServletRequest request, Long nodeUid) {
	if ((nodeUid == null) || request.isUserInRole(Role.SYSADMIN)) {
	    return true;
	}
	boolean isOwner = isNodeOwnerOrSuperuser(request, nodeUid);
	boolean isPlainEditor = isEditor(request, nodeUid);
	if (isOwner || isPlainEditor) {
	    PedagogicalPlannerSequenceNode node = getPedagogicalPlannerDAO().getByUid(nodeUid);
	    Integer nodePermissions = node.getPermissions();
	    if (isOwner || (nodePermissions == null)
		    || ((nodePermissions & PedagogicalPlannerSequenceNode.PERMISSION_EDITOR_REMOVE) != 0)) {
		Set<PedagogicalPlannerSequenceNode> subnodes = node.getSubnodes();
		if ((subnodes != null) && !subnodes.isEmpty()) {
		    for (PedagogicalPlannerSequenceNode subnode : subnodes) {
			if (!canRemoveSubtree(request, subnode.getUid())) {
			    return false;
			}
		    }
		}
		return true;
	    }
	}
	return false;
    }

    private IExportToolContentService getExportService() {
	if (exportService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    exportService = (IExportToolContentService) ctx
		    .getBean(CentralConstants.EXPORT_TOOLCONTENT_SERVICE_BEAN_NAME);
	}
	return exportService;
    }

    private IAuthoringService getAuthoringService() {
	if (authoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    authoringService = (IAuthoringService) ctx.getBean(AuthoringConstants.AUTHORING_SERVICE_BEAN_NAME);
	}
	return authoringService;
    }

    private IMonitoringService getMonitoringService() {
	if (monitoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    monitoringService = (IMonitoringService) ctx.getBean(CentralConstants.MONITORING_SERVICE_BEAN_NAME);
	}
	return monitoringService;
    }

    private IUserManagementService getUserManagementService() {
	if (userManagementService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    userManagementService = (IUserManagementService) ctx
		    .getBean(CentralConstants.USER_MANAGEMENT_SERVICE_BEAN_NAME);
	}
	return userManagementService;
    }

    private MessageService getMessageService() {
	if (messageService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    messageService = (MessageService) ctx.getBean(CentralConstants.CENTRAL_MESSAGE_SERVICE_BEAN_NAME);
	}
	return messageService;
    }

    private PedagogicalPlannerDAO getPedagogicalPlannerDAO() {
	if (pedagogicalPlannerDAO == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    pedagogicalPlannerDAO = (PedagogicalPlannerDAO) wac.getBean("pedagogicalPlannerDAO");
	}
	return pedagogicalPlannerDAO;
    }

    private IActivityDAO getActivityDAO() {
	if (activityDAO == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    activityDAO = (IActivityDAO) wac.getBean("activityDAO");
	}
	return activityDAO;
    }
}