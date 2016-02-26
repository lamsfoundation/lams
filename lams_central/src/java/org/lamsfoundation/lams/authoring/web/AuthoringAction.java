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
/* $$Id$$ */
package org.lamsfoundation.lams.authoring.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.authoring.ObjectExtractorException;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningDesignAccess;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.LearningLibraryGroup;
import org.lamsfoundation.lams.learningdesign.dto.LearningDesignDTO;
import org.lamsfoundation.lams.learningdesign.dto.LicenseDTO;
import org.lamsfoundation.lams.learningdesign.dto.ValidationErrorDTO;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.monitoring.service.IMonitoringService;
import org.lamsfoundation.lams.security.ISecurityService;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.dto.ToolOutputDefinitionDTO;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Manpreet Minhas
 * 
 * @struts.action path = "/authoring/author" parameter = "method" validate = "false"
 * @struts:action-forward name="openAutoring" path="/authoring/authoring.jsp"
 * @struts:action-forward name="svgGenerator" path="/authoring/svgGenerator.jsp"
 */
public class AuthoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(AuthoringAction.class);

    private static IAuditService auditService;
    private static IMonitoringService monitoringService;
    private static IUserManagementService userManagementService;
    private static ILamsToolService toolService;
    private static IAuthoringService authoringService;
    private static ILearningDesignService learningDesignService;
    private static ISecurityService securityService;

    private static int LEARNING_DESIGN_ACCESS_ENTRIES_LIMIT = 7;

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

    public ActionForward openAuthoring(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, FileUtil.generateUniqueContentFolderID());

	request.setAttribute("tools", getLearningDesignService().getToolDTOs(true, request.getRemoteUser()));
	// build list of existing learning library groups
	List<LearningLibraryGroup> groups = getLearningDesignService().getLearningLibraryGroups();
	JSONArray groupsJSON = new JSONArray();
	for (LearningLibraryGroup group : groups) {
	    JSONObject groupJSON = new JSONObject();
	    groupJSON.put("name", group.getName());
	    for (LearningLibrary learningLibrary : group.getLearningLibraries()) {
		groupJSON.append("learningLibraries", learningLibrary.getLearningLibraryId());
	    }
	    groupsJSON.put(groupJSON);
	}
	request.setAttribute("learningLibraryGroups", groupsJSON.toString());

	List<LearningDesignAccess> accessList = getAuthoringService().updateLearningDesignAccessByUser(getUserId());
	accessList = accessList.subList(0,
		Math.min(accessList.size(), AuthoringAction.LEARNING_DESIGN_ACCESS_ENTRIES_LIMIT - 1));
	Gson gson = new GsonBuilder().create();
	request.setAttribute("access", gson.toJson(accessList));

	request.setAttribute("licenses", getAuthoringService().getAvailableLicenses());

	return mapping.findForward("openAutoring");
    }

    public ActionForward generateSVG(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	request.setAttribute("tools", getLearningDesignService().getToolDTOs(true, request.getRemoteUser()));

	return mapping.findForward("svgGenerator");
    }

    /**
     * Output the supplied WDDX packet. If the request parameter USE_JSP_OUTPUT is set, then it sets the session
     * attribute "parameterName" to the wddx packet string. If USE_JSP_OUTPUT is not set, then the packet is written out
     * to the request's PrintWriter.
     * 
     * @param mapping
     *            action mapping (for the forward to the success jsp)
     * @param request
     *            needed to check the USE_JSP_OUTPUT parameter
     * @param response
     *            to write out the wddx packet if not using the jsp
     * @param wddxPacket
     *            wddxPacket or message to be sent/displayed
     * @param parameterName
     *            session attribute to set if USE_JSP_OUTPUT is set
     * @throws IOException
     */
    private ActionForward outputPacket(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response,
	    String wddxPacket, String parameterName) throws IOException {
	response.addHeader("Cache-Control", "no-cache");
	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    public ActionForward getToolOutputDefinitions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	String wddxPacket;
	IAuthoringService authoringService = getAuthoringService();
	try {
	    Long toolContentID = WebUtil.readLongParam(request, "toolContentID", false);
	    Integer definitionType = ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION; // WebUtil.readIntParam(request,
												 // "toolOutputDefinitionType");
	    List<ToolOutputDefinitionDTO> defnDTOList = authoringService.getToolOutputDefinitions(toolContentID,
		    definitionType);

	    FlashMessage flashMessage = new FlashMessage("getToolOutputDefinitions", defnDTOList);
	    wddxPacket = flashMessage.serializeMessage();

	} catch (Exception e) {
	    wddxPacket = handleException(e, "getToolOutputDefinitions", authoringService, true).serializeMessage();
	}
	return outputPacket(mapping, request, response, wddxPacket, "definitions");
    }

    public ActionForward getToolOutputDefinitionsJSON(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException, JSONException {
	IAuthoringService authoringService = getAuthoringService();
	Long toolContentID = WebUtil.readLongParam(request, "toolContentID");
	Integer definitionType = ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION;

	List<ToolOutputDefinitionDTO> defnDTOList = authoringService.getToolOutputDefinitions(toolContentID,
		definitionType);

	Gson gson = new GsonBuilder().create();
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(gson.toJson(defnDTOList));
	return null;
    }

    public ActionForward getLearningDesignDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	String wddxPacket;
	IAuthoringService authoringService = getAuthoringService();
	try {
	    Long learningDesignID = WebUtil.readLongParam(request, "learningDesignID", false);
	    wddxPacket = authoringService.getLearningDesignDetails(learningDesignID, getUserLanguage());
	    AuthoringAction.log.debug("LD wddx packet: " + wddxPacket);
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getLearningDesignDetails", authoringService, true).serializeMessage();
	}
	return outputPacket(mapping, request, response, wddxPacket, "details");
    }

    public ActionForward openLearningDesign(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException, JSONException {
	long learningDesignID = WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNINGDESIGN_ID);
	LearningDesignDTO learningDesignDTO = getLearningDesignService().getLearningDesignDTO(learningDesignID,
		getUserLanguage());

	Integer userId = getUserId();
	getAuthoringService().storeLearningDesignAccess(learningDesignID, userId);

	response.setContentType("application/json;charset=utf-8");
	JSONObject responseJSON = new JSONObject();
	Gson gson = new GsonBuilder().create();
	responseJSON.put("ld", new JSONObject(gson.toJson(learningDesignDTO)));

	List<LearningDesignAccess> accessList = getAuthoringService().updateLearningDesignAccessByUser(userId);
	accessList = accessList.subList(0,
		Math.min(accessList.size(), AuthoringAction.LEARNING_DESIGN_ACCESS_ENTRIES_LIMIT - 1));
	responseJSON.put("access", new JSONArray(gson.toJson(accessList)));

	response.getWriter().write(responseJSON.toString());
	return null;
    }

    public ActionForward finishLearningDesignEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {

	String wddxPacket;
	IAuthoringService authoringService = getAuthoringService();
	try {
	    Long learningDesignID = WebUtil.readLongParam(request, "learningDesignID", false);
	    boolean cancelled = WebUtil.readBooleanParam(request, "cancelled", false);

	    wddxPacket = authoringService.finishEditOnFly(learningDesignID, getUserId(), cancelled);

	} catch (Exception e) {
	    wddxPacket = handleException(e, "getLearningDesignDetails", authoringService, true).serializeMessage();
	    return outputPacket(mapping, request, response, wddxPacket, "details");
	}

	return outputPacket(mapping, request, response, wddxPacket, "details");

    }

    public ActionForward getAllLearningLibraryDetails(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String wddxPacket;
	IAuthoringService authoringService = getAuthoringService();
	try {
	    wddxPacket = authoringService.getAllLearningLibraryDetails(getUserLanguage());
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getAllLearningLibraryDetails", authoringService, true).serializeMessage();
	}
	AuthoringAction.log.debug("getAllLearningLibraryDetails: returning " + wddxPacket);
	return outputPacket(mapping, request, response, wddxPacket, "details");
    }

    public ActionForward getToolContentID(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String wddxPacket;
	IAuthoringService authoringService = getAuthoringService();
	try {
	    Long toolID = WebUtil.readLongParam(request, "toolID", false);
	    wddxPacket = authoringService.getToolContentIDFlash(toolID);
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getAllLearningLibraryDetails", authoringService, true).serializeMessage();
	}
	return outputPacket(mapping, request, response, wddxPacket, "details");

    }

    /**
     * Copy some existing content. Used when the user copies an activity in authoring. Expects one parameters -
     * toolContentId (the content to be copied)
     */
    public ActionForward copyToolContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String wddxPacket;
	IAuthoringService authoringService = getAuthoringService();
	String customCSV = WebUtil.readStrParam(request, AttributeNames.PARAM_CUSTOM_CSV, true);
	try {
	    long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, false);
	    Long newToolContentID = authoringService.copyToolContent(toolContentID, customCSV);
	    FlashMessage flashMessage = new FlashMessage("copyToolContent", newToolContentID);
	    wddxPacket = flashMessage.serializeMessage();
	} catch (Exception e) {
	    wddxPacket = handleException(e, "copyToolContent", authoringService, true).serializeMessage();
	}
	return outputPacket(mapping, request, response, wddxPacket, "details");
    }

    /**
     * Copy some existing content. Used when the user copies an activity in authoring. Expects one parameters -
     * toolContentId (the content to be copied)
     */
    public ActionForward copyToolContentPlain(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IAuthoringService authoringService = getAuthoringService();
	try {
	    String customCSV = WebUtil.readStrParam(request, AttributeNames.PARAM_CUSTOM_CSV, true);
	    long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID, false);
	    Long newToolContentID = authoringService.copyToolContent(toolContentID, customCSV);
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().write(newToolContentID.toString());
	} catch (Exception e) {
	    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	}

	return null;
    }

    /**
     * This method returns a list of all available license in WDDX format.
     * 
     * This will include our supported Creative Common licenses and an "OTHER" license which may be used for user
     * entered license details. The picture url supplied should be a full URL i.e. if it was a relative URL in the
     * database, it should have been converted to a complete server URL (starting http://) before sending to the client.
     * 
     * @return String The required information in WDDX format
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public ActionForward getAvailableLicenses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, Exception {

	FlashMessage flashMessage = null;
	try {
	    IAuthoringService authoringService = getAuthoringService();
	    Vector<LicenseDTO> licenses = authoringService.getAvailableLicenses();
	    flashMessage = new FlashMessage("getAvailableLicenses", licenses);
	} catch (Exception e) {
	    AuthoringAction.log.error("getAvailableLicenses: License details unavailable due to system error.", e);
	    flashMessage = new FlashMessage("getAvailableLicenses",
		    "License details unavailable due to system error :" + e.getMessage(), FlashMessage.ERROR);

	    getAuditService().log(AuthoringAction.class.getName(), e.toString());
	}

	PrintWriter writer = response.getWriter();
	writer.println(flashMessage.serializeMessage());
	return null;
    }

    public ActionForward createUniqueContentFolder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, Exception {

	String wddxPacket;
	IAuthoringService authoringService = getAuthoringService();

	try {
	    wddxPacket = authoringService.generateUniqueContentFolder();
	} catch (FileUtilException fue) {
	    // return a normal error, not a critical error as a critical error
	    // interrupts the display
	    // of the toolkit in the Flash client.
	    wddxPacket = handleException(fue, "createUniqueContentFolder", authoringService, false).serializeMessage();
	} catch (Exception e) {
	    wddxPacket = handleException(e, "createUniqueContentFolder", authoringService, false).serializeMessage();
	}

	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    public ActionForward getHelpURL(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, Exception {

	String wddxPacket;
	IAuthoringService authoringService = getAuthoringService();
	try {
	    wddxPacket = authoringService.getHelpURL();
	} catch (Exception e) {
	    wddxPacket = handleException(e, "getHelpURL", authoringService, true).serializeMessage();
	}

	PrintWriter writer = response.getWriter();
	writer.println(wddxPacket);
	return null;
    }

    /**
     * Creates a copy of default tool content.
     */
    public ActionForward createToolContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {
	IAuthoringService authoringService = getAuthoringService();
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
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("authorURL", authorUrl);
		// return the generated values
		responseJSON.put(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentID);
		responseJSON.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(responseJSON.toString());
	    }
	}
	return null;
    }

    /**
     * Creates a LD with the given activity and starts a lesson with default class and settings.
     */
    @SuppressWarnings("unchecked")
    public ActionForward createSingleActivityLesson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	IAuthoringService authoringService = getAuthoringService();
	Long toolID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_ID);
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = request.getParameter(AttributeNames.PARAM_CONTENT_FOLDER_ID);
	Integer organisationID = WebUtil.readIntParam(request, AttributeNames.PARAM_ORGANISATION_ID);
	Integer userID = getUserId();

	if (!getSecurityService().isGroupMonitor(organisationID, userID, "create single activity lesson", false)) {
	    response.sendError(HttpServletResponse.SC_FORBIDDEN, "User is not a monitor in the organisation");
	    return null;
	}

	// get title from tool content
	IToolVO tool = getToolService().getToolByID(toolID);
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	ToolContentManager toolManager = (ToolContentManager) wac.getBean(tool.getServiceName());
	String title = toolManager.getToolContentTitle(toolContentID);
	// create the LD and put it in Run Sequences folder in the given organisation
	Long learningDesignID = authoringService.insertSingleActivityLearningDesign(title, toolID, toolContentID,
		contentFolderID, organisationID);
	if (learningDesignID != null) {
	    User user = (User) getUserManagementService().findById(User.class, userID);
	    Lesson lesson = getMonitoringService().initializeLessonWithoutLDcopy(title, "", learningDesignID, user,
		    null, false, false, false, false, true, true, false, null, null);
	    Organisation organisation = getMonitoringService().getOrganisation(organisationID);

	    List<User> staffList = new LinkedList<User>();
	    staffList.add(user);

	    // add organisation's learners as lesson participants
	    List<User> learnerList = new LinkedList<User>();
	    Vector<User> learnerVector = getUserManagementService().getUsersFromOrganisationByRole(organisationID,
		    Role.LEARNER, false, true);
	    learnerList.addAll(learnerVector);
	    getMonitoringService().createLessonClassForLesson(lesson.getLessonId(), organisation,
		    organisation.getName() + "Learners", learnerList, organisation.getName() + "Staff", staffList,
		    userID);

	    getMonitoringService().startLesson(lesson.getLessonId(), userID);

	    if (AuthoringAction.log.isDebugEnabled()) {
		AuthoringAction.log.debug("Created a single activity lesson with ID: " + lesson.getLessonId());
	    }
	    
	    response.setContentType("text/plain;charset=utf-8");
	    response.getWriter().write(learningDesignID.toString());
	}
	
	return null;
    }

    /**
     * Handle flash error.
     * 
     * @param e
     * @param methodKey
     * @param monitoringService
     * @return
     */
    private FlashMessage handleException(Exception e, String methodKey, IAuthoringService authoringService,
	    boolean useCriticalError) {
	AuthoringAction.log.error("Exception thrown " + methodKey, e);
	getAuditService().log(AuthoringAction.class.getName() + ":" + methodKey, e.toString());

	String[] msg = new String[1];
	msg[0] = e.getMessage();
	return new FlashMessage(methodKey, authoringService.getMessageService().getMessage("error.system.error", msg),
		useCriticalError ? FlashMessage.CRITICAL_ERROR : FlashMessage.ERROR);
    }

    /**
     * Stores Learning Desing created in Flashless Authoring.
     */
    public ActionForward saveLearningDesign(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, UserException, WorkspaceFolderException, IOException,
		    ObjectExtractorException, ParseException {
	JSONObject ldJSON = new JSONObject(request.getParameter("ld"));

	LearningDesign learningDesign = getAuthoringService().saveLearningDesignDetails(ldJSON);

	JSONObject responseJSON = new JSONObject();
	if (learningDesign != null) {
	    Long learningDesignID = learningDesign.getLearningDesignId();
	    if (learningDesignID != null) {
		Gson gson = new GsonBuilder().create();
		Vector<ValidationErrorDTO> validationDTOs = getAuthoringService()
			.validateLearningDesign(learningDesignID);
		String validationJSON = gson.toJson(validationDTOs);
		responseJSON.put("validation", new JSONArray(validationJSON));

		// get DTO with updated IDs
		LearningDesignDTO learningDesignDTO = getLearningDesignService().getLearningDesignDTO(learningDesignID,
			getUserLanguage());
		responseJSON.put("ld", new JSONObject(gson.toJson(learningDesignDTO)));

		Integer userId = getUserId();
		getAuthoringService().storeLearningDesignAccess(learningDesignID, userId);

		List<LearningDesignAccess> accessList = getAuthoringService().updateLearningDesignAccessByUser(userId);
		accessList = accessList.subList(0,
			Math.min(accessList.size(), AuthoringAction.LEARNING_DESIGN_ACCESS_ENTRIES_LIMIT - 1));
		responseJSON.put("access", new JSONArray(gson.toJson(accessList)));
	    }
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(responseJSON.toString());
	return null;
    }

    /**
     * Gets a list of recently used Learning Designs for currently logged in user.
     */
    public ActionForward getLearningDesignAccess(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
	Integer userId = getUserId();

	List<LearningDesignAccess> accessList = getAuthoringService().updateLearningDesignAccessByUser(userId);
	accessList = accessList.subList(0,
		Math.min(accessList.size(), AuthoringAction.LEARNING_DESIGN_ACCESS_ENTRIES_LIMIT - 1));
	Gson gson = new GsonBuilder().create();

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().write(gson.toJson(accessList));
	return null;
    }

    /**
     * Stores the binary code of an Learning Design thumbnail, created in Flashless Authoring.
     */
    public ActionForward saveLearningDesignImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long learningDesignID = WebUtil.readLongParam(request, AttributeNames.PARAM_LEARNINGDESIGN_ID);
	String extension = request.getParameter("extension");
	String image = request.getParameter("image");
	AuthoringAction.saveLearningDesignImage(learningDesignID, extension, image);

	return null;
    }

    /**
     * Stores the binary code of an Learning Design thumbnail, created in Flashless Authoring.
     */
    private static void saveLearningDesignImage(long learningDesignID, String extension, String image) {
	if (StringUtils.isBlank(image) || (!"SVG".equalsIgnoreCase(extension) && !"PNG".equalsIgnoreCase(extension))) {
	    return;
	}

	// prepare the dir and the file
	File thumbnailDirectory = new File(IAuthoringService.LEARNING_DESIGN_IMAGES_FOLDER);
	if (!thumbnailDirectory.exists()) {
	    thumbnailDirectory.mkdirs();
	}

	String absoluteFilePath = FileUtil.getFullPath(IAuthoringService.LEARNING_DESIGN_IMAGES_FOLDER,
		learningDesignID + "." + extension.toLowerCase());

	// write out the content
	FileOutputStream fos = null;
	try {
	    fos = new FileOutputStream(absoluteFilePath);
	    if (extension.equalsIgnoreCase("png")) {
		// if it comes from Flashless Authoring, it can have a prefix we need to remove
		if (image.contains("base64")) {
		    image = image.substring(image.indexOf(",") + 1);
		}
		byte[] bytes = DatatypeConverter.parseBase64Binary(image);
		fos.write(bytes);
	    } else {
		// encoding is important, especially for Raphael-generated SVGs
		Writer writer = new OutputStreamWriter(fos, "UTF8");
		writer.write(image);
		writer.close();
	    }
	} catch (IOException e) {
	    AuthoringAction.log.error(
		    "Error while writing " + extension.toUpperCase() + " thumbnail of LD " + learningDesignID + ".", e);
	} finally {
	    if (fos != null) {
		try {
		    fos.close();
		} catch (IOException e) {
		    AuthoringAction.log.error("Error while closing stream to " + extension.toUpperCase()
			    + " thumbnail of LD " + learningDesignID);
		}
	    }
	}
    }

    /**
     * Get AuditService bean.
     * 
     * @return
     */
    private IAuditService getAuditService() {
	if (AuthoringAction.auditService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    AuthoringAction.auditService = (IAuditService) ctx.getBean("auditService");
	}
	return AuthoringAction.auditService;
    }

    private IMonitoringService getMonitoringService() {
	if (AuthoringAction.monitoringService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    AuthoringAction.monitoringService = (IMonitoringService) ctx.getBean("monitoringService");
	}
	return AuthoringAction.monitoringService;
    }

    private IUserManagementService getUserManagementService() {
	if (AuthoringAction.userManagementService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    AuthoringAction.userManagementService = (IUserManagementService) wac
		    .getBean(CentralConstants.USER_MANAGEMENT_SERVICE_BEAN_NAME);
	}
	return AuthoringAction.userManagementService;
    }

    public IAuthoringService getAuthoringService() {
	if (AuthoringAction.authoringService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    AuthoringAction.authoringService = (IAuthoringService) wac
		    .getBean(AuthoringConstants.AUTHORING_SERVICE_BEAN_NAME);
	}
	return AuthoringAction.authoringService;
    }

    public ILamsToolService getToolService() {
	if (AuthoringAction.toolService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    AuthoringAction.toolService = (ILamsToolService) wac.getBean(AuthoringConstants.TOOL_SERVICE_BEAN_NAME);
	}
	return AuthoringAction.toolService;
    }

    private ILearningDesignService getLearningDesignService() {
	if (AuthoringAction.learningDesignService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    AuthoringAction.learningDesignService = (ILearningDesignService) ctx.getBean("learningDesignService");
	}
	return AuthoringAction.learningDesignService;
    }

    private ISecurityService getSecurityService() {
	if (AuthoringAction.securityService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    AuthoringAction.securityService = (ISecurityService) ctx.getBean("securityService");
	}
	return AuthoringAction.securityService;
    }
}