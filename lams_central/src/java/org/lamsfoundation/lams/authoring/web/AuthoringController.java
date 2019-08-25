/***************************************************************************
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
 * ************************************************************************
 */

package org.lamsfoundation.lams.authoring.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.ObjectExtractorException;
import org.lamsfoundation.lams.authoring.service.IAuthoringFullService;
import org.lamsfoundation.lams.integration.ExtCourseClassMap;
import org.lamsfoundation.lams.integration.ExtServer;
import org.lamsfoundation.lams.integration.service.IIntegrationService;
import org.lamsfoundation.lams.integration.util.LoginRequestDispatcher;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.BranchingActivity;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignAccess;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.LearningLibraryGroup;
import org.lamsfoundation.lams.learningdesign.dto.AuthoringActivityDTO;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.dto.ToolOutputDefinitionDTO;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.web.WorkspaceController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Manpreet Minhas
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController {
    private static Logger log = Logger.getLogger(AuthoringController.class);

    @Autowired
    private IMonitoringService monitoringService;
    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILamsToolService toolService;
    @Autowired
    private IAuthoringFullService authoringService;
    @Autowired
    private ILearningDesignService learningDesignService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IIntegrationService integrationService;
    @Autowired
    WebApplicationContext applicationContext;

    private Integer getUserId() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    private String getUserLanguage() {
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getLocaleLanguage() : "";
    }

    @RequestMapping("/openAuthoring")
    public String openAuthoring(HttpServletRequest request) throws IOException {
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, FileUtil.generateUniqueContentFolderID());

	request.setAttribute("tools", learningDesignService.getToolDTOs(true, true, request.getRemoteUser()));
	// build list of existing learning library groups
	List<LearningLibraryGroup> groups = learningDesignService.getLearningLibraryGroups();
	ArrayNode groupsJSON = JsonNodeFactory.instance.arrayNode();
	for (LearningLibraryGroup group : groups) {
	    ObjectNode groupJSON = JsonNodeFactory.instance.objectNode();
	    groupJSON.put("name", group.getName());
	    for (LearningLibrary learningLibrary : group.getLearningLibraries()) {
		groupJSON.withArray("learningLibraries").add(learningLibrary.getLearningLibraryId());
	    }
	    groupsJSON.add(groupJSON);
	}
	request.setAttribute("learningLibraryGroups", groupsJSON.toString());

	List<LearningDesignAccess> accessList = authoringService.updateLearningDesignAccessByUser(getUserId());
	request.setAttribute("access", JsonUtil.toString(accessList));
	request.setAttribute("licenses", authoringService.getAvailableLicenses());

	boolean canSetReadOnly = userManagementService.isUserSysAdmin()
		|| userManagementService.isUserGlobalGroupManager();
	request.setAttribute("canSetReadOnly", canSetReadOnly);

	return "authoring/authoring";
    }

    @RequestMapping("/generateSVG")
    public String generateSVG(HttpServletRequest request) throws IOException {
	request.setAttribute("tools", learningDesignService.getToolDTOs(true, true, request.getRemoteUser()));

	return "authoring/svgGenerator";
    }

    @RequestMapping("/getToolOutputDefinitions")
    @ResponseBody
    public String getToolOutputDefinitions(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	Long toolContentID = WebUtil.readLongParam(request, "toolContentID");
	Integer definitionType = ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION;

	List<ToolOutputDefinitionDTO> defnDTOList = authoringService.getToolOutputDefinitions(toolContentID,
		definitionType);

	response.setContentType("application/json;charset=utf-8");
	return JsonUtil.toString(defnDTOList);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/openLearningDesign")
    @ResponseBody
    public String openLearningDesign(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException, ImportToolContentException {
	long learningDesignID = WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNINGDESIGN_ID);
	LearningDesignDTO learningDesignDTO = learningDesignService.getLearningDesignDTO(learningDesignID,
		getUserLanguage());

	// some old LDs may not have learning library IDs filled in, try to find them
	for (AuthoringActivityDTO activity : (List<AuthoringActivityDTO>) learningDesignDTO.getActivities()) {
	    if (activity.getLearningLibraryID() == null) {
		learningDesignService.fillLearningLibraryID(activity, learningDesignDTO.getActivities());
	    }
	}

	Integer userId = getUserId();
	if (learningDesignDTO.getWorkspaceFolderID() != null) {
	    authoringService.storeLearningDesignAccess(learningDesignID, userId);
	}

	response.setContentType("application/json;charset=utf-8");
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	ObjectNode ldJSON = JsonUtil.readObject(learningDesignDTO);
	// get all parent folders of the LD
	List<Integer> folderPath = new LinkedList<>();
	WorkspaceFolder folder = (WorkspaceFolder) userManagementService.findById(WorkspaceFolder.class,
		learningDesignDTO.getWorkspaceFolderID());
	while (folder != null) {
	    Integer folderID = folder.getWorkspaceFolderId();
	    if (folderID.equals(WorkspaceController.ROOT_ORG_FOLDER_ID)) {
		// we reached the top folder, finish
		folder = null;
	    } else {
		folderPath.add(folderID);
		folder = folder.getParentWorkspaceFolder();
	    }
	}
	// we'll go from top to bottom
	Collections.reverse(folderPath);
	ldJSON.set("folderPath", JsonUtil.readArray(folderPath));
	responseJSON.set("ld", ldJSON);

	List<LearningDesignAccess> accessList = authoringService.updateLearningDesignAccessByUser(userId);
	responseJSON.set("access", JsonUtil.readArray(accessList));

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    @RequestMapping("/finishLearningDesignEdit")
    @ResponseBody
    public void finishLearningDesignEdit(HttpServletRequest request) throws ServletException, IOException {
	Long learningDesignID = WebUtil.readLongParam(request, "learningDesignID", false);
	boolean cancelled = WebUtil.readBooleanParam(request, "cancelled", false);

	try {
	    authoringService.finishEditOnFly(learningDesignID, getUserId(), cancelled);
	} catch (Exception e) {
	    String errorMsg = "Error occured ending EditOnFly" + e.getMessage() + " learning design id "
		    + learningDesignID;
	    log.error(errorMsg, e);
	    throw new IOException(e);
	}

    }

    /**
     * Copy some existing content. Used when the user copies an activity in authoring. Expects one parameters -
     * toolContentId (the content to be copied)
     */
    @RequestMapping("/copyToolContent")
    @ResponseBody
    public String copyToolContent(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	try {
	    String customCSV = WebUtil.readStrParam(request, AttributeNames.PARAM_CUSTOM_CSV, true);
	    long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, false);
	    Long newToolContentID = authoringService.copyToolContent(toolContentID, customCSV);
	    response.setContentType("text/plain;charset=utf-8");
	    return newToolContentID.toString();
	} catch (Exception e) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

	return null;
    }

    /**
     * Creates a copy of default tool content.
     */
    @RequestMapping("/createToolContent")
    @ResponseBody
    public String createToolContent(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	Long toolID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_ID);
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, true);
	if (toolContentID == null) {
	    // if the tool content ID was not provided, generate the next unique content ID for the tool
	    toolContentID = authoringService.insertToolContentID(toolID);
	}

	if (toolContentID != null) {
	    String contentFolderID = request.getParameter(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	    if (StringUtils.isBlank(contentFolderID)) {
		contentFolderID = FileUtil.generateUniqueContentFolderID();
	    }

	    String authorUrl = authoringService.getToolAuthorUrl(toolID, toolContentID, contentFolderID);
	    if (authorUrl != null) {
		ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
		responseJSON.put("authorURL", authorUrl);
		// return the generated values
		responseJSON.put(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentID);
		responseJSON.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
		response.setContentType("application/json;charset=utf-8");
		return responseJSON.toString();
	    }
	}
	return null;
    }

    /**
     * Creates a LD with the given activity and starts a lesson with default class and settings.
     */
    @ResponseBody
    @RequestMapping("/createSingleActivityLesson")
    @SuppressWarnings("unchecked")
    public String createSingleActivityLesson(HttpServletRequest request, HttpServletResponse response)
	    throws IOException {
	Long toolID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_ID);
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Long learningLibraryID = WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNING_LIBRARY_ID, true);
	String contentFolderID = request.getParameter(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID, true);
	if (organisationID == null) {
	    // if organisation ID is not set explicitly, derived it from external course
	    String serverID = request.getParameter(LoginRequestDispatcher.PARAM_SERVER_ID);
	    String courseID = request.getParameter(LoginRequestDispatcher.PARAM_COURSE_ID);
	    ExtServer extServer = integrationService.getExtServer(serverID);
	    ExtCourseClassMap orgMap = integrationService.getExtCourseClassMap(extServer.getSid(), courseID);
	    organisationID = orgMap.getOrganisation().getOrganisationId();
	}
	Integer userID = getUserId();

	if (!securityService.isGroupMonitor(organisationID, userID, "create single activity lesson", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	// get title from tool content
	Tool tool = toolService.getToolByID(toolID);
	if (learningLibraryID == null) {
	    // if learning library ID is not set explicitly, derive it from tool
	    learningLibraryID = tool.getLearningLibraryId();
	}
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(applicationContext.getServletContext());
	ToolContentManager toolManager = (ToolContentManager) wac.getBean(tool.getServiceName());
	String title = toolManager.getToolContentTitle(toolContentID);
	if (title == null || title.trim().length() == 0) {
	    title = learningDesignService.internationaliseActivityTitle(learningLibraryID);
	}
	// create the LD and put it in Run Sequences folder in the given organisation
	Long learningDesignID = authoringService.insertSingleActivityLearningDesign(title, toolID, toolContentID,
		learningLibraryID, contentFolderID, organisationID);
	if (learningDesignID != null) {
	    User user = (User) userManagementService.findById(User.class, userID);
	    Lesson lesson = monitoringService.initializeLessonWithoutLDcopy(title, "", learningDesignID, user, null,
		    false, false, false, false, true, true, false, false, true, null, null);
	    Organisation organisation = monitoringService.getOrganisation(organisationID);

	    List<User> staffList = new LinkedList<>();
	    staffList.add(user);

	    // add organisation's learners as lesson participants
	    List<User> learnerList = new LinkedList<>();
	    Vector<User> learnerVector = userManagementService.getUsersFromOrganisationByRole(organisationID,
		    Role.LEARNER, true);
	    learnerList.addAll(learnerVector);
	    monitoringService.createLessonClassForLesson(lesson.getLessonId(), organisation,
		    organisation.getName() + " learners", learnerList, organisation.getName() + " staff", staffList,
		    userID);

	    monitoringService.startLesson(lesson.getLessonId(), userID);

	    if (AuthoringController.log.isDebugEnabled()) {
		AuthoringController.log.debug("Created a single activity lesson with ID: " + lesson.getLessonId());
	    }

	    response.setContentType("text/plain;charset=utf-8");
	    return learningDesignID.toString();
	}

	return null;
    }

    /**
     * Stores Learning Desing created in Authoring.
     */
    @ResponseBody
    @RequestMapping(path = "/saveLearningDesign", method = RequestMethod.POST)
    public String saveLearningDesign(HttpServletRequest request, HttpServletResponse response)
	    throws UserException, WorkspaceFolderException, IOException, ObjectExtractorException, ParseException {
	ObjectNode ldJSON = JsonUtil.readObject(request.getParameter("ld"));

	LearningDesign learningDesign = authoringService.saveLearningDesignDetails(ldJSON);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	if (learningDesign != null) {
	    Long learningDesignID = learningDesign.getLearningDesignId();
	    if (learningDesignID != null) {
		responseJSON.put("learningDesignID", learningDesignID);

		Vector<ValidationErrorDTO> validationDTOs = authoringService.validateLearningDesign(learningDesignID);
		responseJSON.set("validation", JsonUtil.readArray(validationDTOs));
	    }
	}

	response.setContentType("application/json;charset=utf-8");
	return responseJSON.toString();
    }

    /**
     * Gets a list of recently used Learning Designs for currently logged in user.
     */
    @ResponseBody
    @RequestMapping("/getLearningDesignAccess")
    public String getLearningDesignAccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Integer userId = getUserId();

	List<LearningDesignAccess> accessList = authoringService.updateLearningDesignAccessByUser(userId);
	response.setContentType("application/json;charset=utf-8");
	return JsonUtil.toString(accessList);
    }

    /**
     * Stores the binary code of an Learning Design thumbnail, created in Authoring.
     *
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(path = "/saveLearningDesignImage", method = RequestMethod.POST)
    public void saveLearningDesignImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long learningDesignID = WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNINGDESIGN_ID);
	String image = request.getParameter("image");
	boolean saveSuccesful = AuthoringController.saveLearningDesignImage(learningDesignID, image);
	if (!saveSuccesful) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}
    }

    /**
     * Updates an existing activity coordinates.
     * It is run when SVG gets recreated in Monitoring or Add Lesson dialog
     * and activities need to be rearranged as one of them is a branching designed in the old Flash Authoring.
     */
    @ResponseBody
    @RequestMapping(path = "/saveActivityCoordinates", method = RequestMethod.POST)
    public void saveActivityCoordinates(HttpServletRequest request, HttpServletResponse response) throws IOException {
	try {
	    ObjectNode activityJSON = JsonUtil.readObject(request.getParameter("activity"));
	    Activity activity = monitoringService.getActivityById(JsonUtil.optLong(activityJSON, "activityID"));
	    activity.setXcoord(JsonUtil.optInt(activityJSON, "xCoord"));
	    activity.setYcoord(JsonUtil.optInt(activityJSON, "yCoord"));
	    if (activity.isBranchingActivity()) {
		BranchingActivity branchingActivity = (BranchingActivity) activity;
		branchingActivity.setStartXcoord(JsonUtil.optInt(activityJSON, "startXCoord"));
		branchingActivity.setEndXcoord(JsonUtil.optInt(activityJSON, "endXCoord"));
		branchingActivity.setStartYcoord(JsonUtil.optInt(activityJSON, "startYCoord"));
		branchingActivity.setEndYcoord(JsonUtil.optInt(activityJSON, "endYCoord"));
	    }
	    userManagementService.save(activity);
	} catch (Exception e) {
	    log.error("Exception while saving activity coordinates", e);
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}
    }

    /**
     * Stores the binary code of an Learning Design thumbnail, created in Authoring.
     */
    private static boolean saveLearningDesignImage(long learningDesignID, String image) {
	if (StringUtils.isBlank(image)) {
	    AuthoringController.log.error("No SVG code to save for LD: " + learningDesignID);
	    return false;
	}

	// prepare the dir and the file
	File thumbnailDir = new File(ILearningDesignService.LD_SVG_TOP_DIR);
	String thumbnailSubdir = String.valueOf(learningDesignID);
	if (thumbnailSubdir.length() % 2 == 1) {
	    thumbnailSubdir = "0" + thumbnailSubdir;
	}
	for (int charIndex = 0; charIndex < thumbnailSubdir.length(); charIndex += 2) {
	    thumbnailDir = new File(thumbnailDir,
		    "" + thumbnailSubdir.charAt(charIndex) + thumbnailSubdir.charAt(charIndex + 1));
	}
	thumbnailDir.mkdirs();

	String absoluteFilePath = FileUtil.getFullPath(thumbnailDir.getAbsolutePath(), learningDesignID + ".svg");

	// write out the content
	try (FileOutputStream fos = new FileOutputStream(absoluteFilePath);
		Writer writer = new OutputStreamWriter(fos, "UTF8")) {
	    // encoding is important, especially for Raphael-generated SVGs
	    writer.write(image);
	} catch (IOException e) {
	    AuthoringController.log.error("Error while writing SVG thumbnail of LD " + learningDesignID + ".", e);
	    return false;
	}
	return true;
    }

}