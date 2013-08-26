/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.EditActivityDTO;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McCandidateAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.mc.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.McGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionContentDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUploadedFile;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * * @author Ozgur Demirtas
 * 
 * <!--Monitoring Main Action: interacts with the Monitoring module user --> <action path="/monitoring"
 * type="org.lamsfoundation.lams.tool.mc.web.McMonitoringAction" name="McMonitoringForm" scope="request"
 * input="/monitoring/MonitoringMaincontent.jsp" parameter="dispatch" unknown="false" validate="false">
 * 
 * <forward name="loadMonitoring" path="/monitoring/MonitoringMaincontent.jsp" redirect="false" />
 * 
 * <forward name="loadMonitoringEditActivity" path="/monitoring/MonitoringMaincontent.jsp" redirect="false" />
 * 
 * <forward name="refreshMonitoring" path="/monitoring/MonitoringMaincontent.jsp" redirect="false" />
 * 
 * 
 * <forward name="learnerNotebook" path="/monitoring/LearnerNotebook.jsp" redirect="false" />
 * 
 * 
 * <forward name="newQuestionBox" path="/monitoring/newQuestionBox.jsp" redirect="false" />
 * 
 * <forward name="editQuestionBox" path="/monitoring/editQuestionBox.jsp" redirect="false" /> </action>
 * 
 */
public class McMonitoringAction extends LamsDispatchAction implements McAppConstants {
    static Logger logger = Logger.getLogger(McMonitoringAction.class.getName());

    /**
     * main content/question content management and workflow logic
     * 
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	return null;
    }

    /**
     * @param form
     * @param request
     * @param mcService
     * @param mcGeneralMonitoringDTO
     * @throws IOException
     * @throws ServletException
     */
    protected ActionForward commonSubmitSessionCode(McMonitoringForm mcMonitoringForm, HttpServletRequest request,
	    ActionMapping mapping, IMcService mcService, McGeneralMonitoringDTO mcGeneralMonitoringDTO)
	    throws IOException, ServletException {

	repopulateRequestParameters(request, mcMonitoringForm, mcGeneralMonitoringDTO);

	String currentMonitoredToolSession = request.getParameter("monitoredToolSessionId");

	if (currentMonitoredToolSession == null) {
	    // default to All
	    currentMonitoredToolSession = "All";
	}

	String toolContentID = mcMonitoringForm.getToolContentID();

	McContent mcContent = mcService.retrieveMc(new Long(toolContentID));

	if (currentMonitoredToolSession.equals("All")) {
	    // generate DTO for All sessions
	    MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	    mcGeneralMonitoringDTO.setSelectionCase(new Long(2));
	    request.setAttribute(SELECTION_CASE, new Long(2));
	} else {
	    McSession mcSession = mcService.getMcSessionById(new Long(currentMonitoredToolSession));

	    MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	    mcGeneralMonitoringDTO.setGroupName(mcSession.getSession_name());
	    mcGeneralMonitoringDTO.setSelectionCase(new Long(1));
	    request.setAttribute(SELECTION_CASE, new Long(1));
	}

	request.setAttribute(CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);

	mcGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
	mcGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
	mcGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());

	mcGeneralMonitoringDTO.setSummaryToolSessions(populateToolSessions(mcContent));
	mcGeneralMonitoringDTO.setDisplayAnswers(new Boolean(mcContent.isDisplayAnswers()).toString());

	boolean isGroupedActivity = mcService.isGroupedActivity(new Long(toolContentID));
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	/* setting editable screen properties */
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setActivityTitle(mcContent.getTitle());
	mcGeneralAuthoringDTO.setActivityInstructions(mcContent.getInstructions());

	Map mapOptionsContent = new TreeMap(new McComparator());
	Iterator queIterator = mcContent.getMcQueContents().iterator();
	Long mapIndex = new Long(1);
	while (queIterator.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) queIterator.next();
	    if (mcQueContent != null) {
		mapOptionsContent.put(mapIndex.toString(), mcQueContent.getQuestion());

		mapIndex = new Long(mapIndex.longValue() + 1);
	    }
	}

	boolean isContentInUse = McUtils.isContentInUse(mcContent);
	mcGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
	if (isContentInUse == true) {
	    // monitoring url does not allow editActivity since the content is in use
	    mcGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	}

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	prepareReflectionData(request, mcContent, mcService, null, false, "All");

	if (mcService.studentActivityOccurredGlobal(mcContent)) {
	    // USER_EXCEPTION_NO_TOOL_SESSIONS is set to false
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    // USER_EXCEPTION_NO_TOOL_SESSIONS is set to true
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
	mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());

	List<McUploadedFile> attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
	mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
	mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here **/

	request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(mcService, mcContent);

	if (notebookEntriesExist) {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = (String) mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		// there are no online student activity but there are reflections
		request.setAttribute(NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.setSessionUserCount(mcContent, mcGeneralMonitoringDTO);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	return (mapping.findForward(LOAD_MONITORING_CONTENT));

    }

    /**
     * Populates a sorted map of the tool session where the key is the mcSessionId and the value is name of the session.
     * If no sessions exists, there will be a single entry "None", otherwise on the end of the list will be the entry
     * "All"
     */
    public static Map<String, String> populateToolSessions(McContent mcContent) {
	Map<String, String> sessionsMap = new TreeMap<String, String>();
	Iterator iter = mcContent.getMcSessions().iterator();
	while (iter.hasNext()) {
	    McSession elem = (McSession) iter.next();
	    sessionsMap.put(elem.getMcSessionId().toString(), elem.getSession_name());
	}

	if (sessionsMap.isEmpty()) {
	    sessionsMap.put("None", "None");
	} else {
	    sessionsMap.put("All", "All");
	}

	return sessionsMap;

    }

    /**
     * 
     * submitSession
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward submitSession(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	mcContent.setDisplayAnswers(new Boolean(true));
	return commonSubmitSessionCode((McMonitoringForm) form, request, mapping, mcService,
		new McGeneralMonitoringDTO());
    }

    /**
     * displayAnswers
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward displayAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	mcContent.setDisplayAnswers(new Boolean(true));
	return commonSubmitSessionCode((McMonitoringForm) form, request, mapping, mcService,
		new McGeneralMonitoringDTO());
    }

    /**
     * editActivityQuestions
     * 
     * enables swiching to editable mode in the Edit Activity tab
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward editActivityQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {

	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	// String defaultContentIdStr=request.getParameter(DEFAULT_CONTENT_ID_STR);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(mcContent.getTitle());
	mcAuthoringForm.setTitle(mcContent.getTitle());

	mcGeneralAuthoringDTO.setActivityInstructions(mcContent.getInstructions());

	mcAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	boolean isContentInUse = McUtils.isContentInUse(mcContent);

	mcGeneralAuthoringDTO.setMonitoredContentInUse(new Boolean(false).toString());
	if (isContentInUse == true) {
	    // monitoring url does not allow editActivity since the content is in use
	    persistError(request, "error.content.inUse");
	    mcGeneralAuthoringDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

	McUtils.setDefineLater(request, true, strToolContentID, mcService);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	AuthoringUtil authoringUtil = new AuthoringUtil();
	List listQuestionContentDTO = authoringUtil.buildDefaultQuestionContent(mcContent, mcService);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	request.getSession().setAttribute(httpSessionID, sessionMap);

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	/* setting up USER_EXCEPTION_NO_TOOL_SESSIONS, from here */
	McGeneralMonitoringDTO mcGeneralMonitoringDTO = new McGeneralMonitoringDTO();
	mcGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (mcService.studentActivityOccurredGlobal(mcContent)) {
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
	mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());

	List<McUploadedFile> attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
	mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
	mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());

	request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
	/* .. till here */

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(mcService, mcContent);

	if (notebookEntriesExist) {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = (String) mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		// there are no online student activity but there are reflections
		request.setAttribute(NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */
	MonitoringUtil.setSessionUserCount(mcContent, mcGeneralMonitoringDTO);
	
	List listAllGroupsDTO = MonitoringUtil.buildGroupBasedSessionData(mcContent, mcService);
	request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	return mapping.findForward(LOAD_MONITORING_CONTENT);
    }

    /**
     * persists error messages to request scope
     * 
     * @param request
     * @param message
     */
    public void persistError(HttpServletRequest request, String message) {
	ActionMessages errors = new ActionMessages();
	errors.add(Globals.ERROR_KEY, new ActionMessage(message));
	saveErrors(request, errors);
    }

    /**
     * 
     submits content into the tool database
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);

	Map mapQuestionContent = AuthoringUtil.extractMapQuestionContent(listQuestionContentDTO);

	Map mapFeedback = AuthoringUtil.extractMapFeedback(listQuestionContentDTO);

	Map mapWeights = new TreeMap(new McComparator());

	Map mapMarks = AuthoringUtil.extractMapMarks(listQuestionContentDTO);

	Map mapCandidatesList = AuthoringUtil.extractMapCandidatesList(listQuestionContentDTO);

	ActionMessages errors = new ActionMessages();

	if (mapQuestionContent.size() == 0) {
	    ActionMessage error = new ActionMessage("questions.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	AuthoringUtil authoringUtil = new AuthoringUtil();

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();

	if (activeModule.equals(AUTHORING)) {
	    List attachmentList = (List) sessionMap.get(ATTACHMENT_LIST_KEY);
	    List deletedAttachmentList = (List) sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);

	    String onlineInstructions = (String) sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
	    mcGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	    String offlineInstructions = (String) sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
	    mcGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

	    mcGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    mcGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

	    String strOnlineInstructions = request.getParameter("onlineInstructions");
	    String strOfflineInstructions = request.getParameter("offlineInstructions");
	    mcAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	    mcAuthoringForm.setOnlineInstructions(strOnlineInstructions);

	}

	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	mcGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);
	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	McContent mcContentTest = mcService.retrieveMc(new Long(strToolContentID));
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    logger.debug("errors saved: " + errors);
	}

	McGeneralMonitoringDTO mcGeneralMonitoringDTO = new McGeneralMonitoringDTO();

	McContent mcContent = mcContentTest;
	if (errors.isEmpty()) {
	    authoringUtil.removeRedundantQuestions(mapQuestionContent, mcService, mcAuthoringForm, request,
		    strToolContentID);

	    mcContent = authoringUtil.saveOrUpdateMcContent(mapQuestionContent, mapFeedback, mapWeights, mapMarks,
		    mapCandidatesList, mcService, mcAuthoringForm, request, mcContentTest, strToolContentID);

	    long defaultContentID = 0;
	    defaultContentID = mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);

	    if (mcContent != null) {
		mcGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	    }

	    authoringUtil.reOrganizeDisplayOrder(mapQuestionContent, mcService, mcAuthoringForm, mcContent);

	    McUtils.setDefineLater(request, false, strToolContentID, mcService);
	    // define later set to false

	    McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		    defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	    // go back to view only screen
	    mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	    mcGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	} else {
	    // errors is not empty

	    if (mcContent != null) {
		long defaultContentID = 0;
		defaultContentID = mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);

		if (mcContent != null) {
		    mcGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		}

		McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
			defaultContentIdStr, activeModule, sessionMap, httpSessionID);

		mcGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	    }
	}
	request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);

	mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(1).toString());

	mcAuthoringForm.resetUserAction();
	mcGeneralAuthoringDTO.setMapQuestionContent(mapQuestionContent);

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	/* common screen data */
	if (mcService.studentActivityOccurredGlobal(mcContent)) {
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
	mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());

	List<McUploadedFile> attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
	mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
	mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(mcService, mcContent);

	if (notebookEntriesExist) {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = (String) mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		// there are no online student activity but there are reflections
		request.setAttribute(NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */
	MonitoringUtil.setSessionUserCount(mcContent, mcGeneralMonitoringDTO);
	List listAllGroupsDTO = MonitoringUtil.buildGroupBasedSessionData(mcContent, mcService);
	request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	return mapping.findForward(LOAD_MONITORING);
    }

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward saveSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;
	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	String totalMarks = request.getParameter("totalMarks");
	String mark = request.getParameter("mark");
	String passmark = request.getParameter("passmark");

	AuthoringUtil authoringUtil = new AuthoringUtil();
	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);

	caList = AuthoringUtil.removeBlankEntries(caList);

	boolean validateSingleCorrectCandidate = authoringUtil.validateSingleCorrectCandidate(caList);

	ActionMessages errors = new ActionMessages();

	if (!validateSingleCorrectCandidate) {
	    ActionMessage error = new ActionMessage("candidates.none.correct");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    logger.debug("errors saved: " + errors);
	}

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();

	mcGeneralAuthoringDTO.setMarkValue(mark);
	mcGeneralAuthoringDTO.setPassMarkValue(passmark);

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));

	if (errors.isEmpty()) {
	    // errors is empty
	    mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	    mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	    String newQuestion = request.getParameter("newQuestion");
	    String feedback = request.getParameter("feedback");

	    String editableQuestionIndex = request.getParameter("editableQuestionIndex");
	    mcAuthoringForm.setQuestionIndex(editableQuestionIndex);

	    if ((newQuestion != null) && (newQuestion.length() > 0)) {
		if ((editQuestionBoxRequest != null) && (editQuestionBoxRequest.equals("false"))) {
		    boolean duplicates = AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);
		    if (!duplicates) {
			McQuestionContentDTO mcQuestionContentDTO = null;
			Iterator listIterator = listQuestionContentDTO.iterator();
			while (listIterator.hasNext()) {
			    mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

			    String question = mcQuestionContentDTO.getQuestion();
			    String displayOrder = mcQuestionContentDTO.getDisplayOrder();

			    if ((displayOrder != null) && (!displayOrder.equals(""))) {
				if (displayOrder.equals(editableQuestionIndex)) {
				    break;
				}

			    }
			}

			mcQuestionContentDTO.setQuestion(newQuestion);
			mcQuestionContentDTO.setFeedback(feedback);
			mcQuestionContentDTO.setDisplayOrder(editableQuestionIndex);
			mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
			mcQuestionContentDTO.setMark(mark);

			mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO()
				.size()).toString());

			listQuestionContentDTO = AuthoringUtil.reorderUpdateListQuestionContentDTO(
				listQuestionContentDTO, mcQuestionContentDTO, editableQuestionIndex);
		    } else {
			// duplicate question entry, not adding
		    }
		} else {
		    // request for edit and save
		    McQuestionContentDTO mcQuestionContentDTO = null;
		    Iterator listIterator = listQuestionContentDTO.iterator();
		    while (listIterator.hasNext()) {
			mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

			String question = mcQuestionContentDTO.getQuestion();
			String displayOrder = mcQuestionContentDTO.getDisplayOrder();

			if ((displayOrder != null) && (!displayOrder.equals(""))) {
			    if (displayOrder.equals(editableQuestionIndex)) {
				break;
			    }

			}
		    }

		    mcQuestionContentDTO.setQuestion(newQuestion);
		    mcQuestionContentDTO.setFeedback(feedback);
		    mcQuestionContentDTO.setDisplayOrder(editableQuestionIndex);
		    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
		    mcQuestionContentDTO.setMark(mark);

		    mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO()
			    .size()).toString());

		    listQuestionContentDTO = AuthoringUtil.reorderUpdateListQuestionContentDTO(listQuestionContentDTO,
			    mcQuestionContentDTO, editableQuestionIndex);
		}
	    } else {
		// entry blank, not adding
	    }

	    mcGeneralAuthoringDTO.setMarkValue(mark);

	    request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	    commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, activeModule, strToolContentID,
		    defaultContentIdStr, mcService, httpSessionID, listQuestionContentDTO);

	    request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	    setupCommonScreenData(mcContent, mcService, request);
	    MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	    return (mapping.findForward(LOAD_MONITORING));
	} else {
	    // errors is not empty
	    commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, activeModule, strToolContentID,
		    defaultContentIdStr, mcService, httpSessionID, listQuestionContentDTO);

	    Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	    String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	    mcAuthoringForm.setTotalMarks(totalMark);
	    mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	    request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	    setupCommonScreenData(mcContent, mcService, request);
	    MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	    request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());

	    return newEditableQuestionBox(mapping, form, request, response);
	}

    }

    /**
     * 
     * @param request
     * @param mcGeneralAuthoringDTO
     * @param mcAuthoringForm
     * @param sessionMap
     * @param activeModule
     * @param strToolContentID
     * @param defaultContentIdStr
     * @param mcService
     * @param httpSessionID
     * @param listQuestionContentDTO
     */
    protected void commonSaveCode(HttpServletRequest request, McGeneralAuthoringDTO mcGeneralAuthoringDTO,
	    McAuthoringForm mcAuthoringForm, SessionMap<String, Object> sessionMap, String activeModule, String strToolContentID,
	    String defaultContentIdStr, IMcService mcService, String httpSessionID, List listQuestionContentDTO) {
	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();
	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);
    }

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	List listAddableQuestionContentDTO = (List) sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);

	int listSize = listQuestionContentDTO.size();
	request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);

	String newQuestion = request.getParameter("newQuestion");
	String feedback = request.getParameter("feedback");
	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	String passmark = request.getParameter("passmark");
	mcGeneralAuthoringDTO.setPassMarkValue(passmark);

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	caList = AuthoringUtil.removeBlankEntries(caList);

	boolean validateSingleCorrectCandidate = authoringUtil.validateSingleCorrectCandidate(caList);

	ActionMessages errors = new ActionMessages();

	if (!validateSingleCorrectCandidate) {
	    ActionMessage error = new ActionMessage("candidates.none.correct");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    logger.debug("errors saved: " + errors);
	}

	if (errors.isEmpty()) {
	    if ((newQuestion != null) && (newQuestion.length() > 0)) {
		boolean duplicates = AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);

		if (!duplicates) {
		    McQuestionContentDTO mcQuestionContentDTO = new McQuestionContentDTO();
		    mcQuestionContentDTO.setDisplayOrder(new Long(listSize + 1).toString());
		    mcQuestionContentDTO.setFeedback(feedback);
		    mcQuestionContentDTO.setQuestion(newQuestion);
		    mcQuestionContentDTO.setMark(mark);

		    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
		    mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO()
			    .size()).toString());

		    listQuestionContentDTO.add(mcQuestionContentDTO);
		} else {
		    // entry duplicate, not adding
		}
	    } else {
		// entry blank, not adding
	    }
	} else {
	    // errors, not adding

	    commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, activeModule, strToolContentID,
		    defaultContentIdStr, mcService, httpSessionID, listQuestionContentDTO);

	    Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	    mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	    String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	    mcAuthoringForm.setTotalMarks(totalMark);
	    mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	    request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	    request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	    sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	    return newQuestionBox(mapping, form, request, response);
	}
	mcGeneralAuthoringDTO.setMarkValue(mark);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	mcAuthoringForm.setFeedback(feedback);

	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	commonSaveCode(request, mcGeneralAuthoringDTO, mcAuthoringForm, sessionMap, activeModule, strToolContentID,
		defaultContentIdStr, mcService, httpSessionID, listQuestionContentDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	return (mapping.findForward(LOAD_MONITORING));
    }

    /**
     * newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * opens up an new screen within the current page for adding a new question
     * 
     * newQuestionBox
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();

	/* create default mcContent object */
	McContent mcContent = mcService.retrieveMc(new Long(defaultContentIdStr));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();
	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	String requestType = request.getParameter("requestType");

	List listAddableQuestionContentDTO = (List) sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);
	if ((requestType != null) && (requestType.equals("direct"))) {
	    // requestType is direct
	    listAddableQuestionContentDTO = authoringUtil.buildDefaultQuestionContent(mcContent, mcService);
	}

	request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
	sessionMap.put(NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);
	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
	return (mapping.findForward("newQuestionBox"));
    }

    /**
     * opens up an new screen within the current page for editing a question
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	request.setAttribute(CURRENT_EDITABLE_QUESTION_INDEX, questionIndex);

	mcAuthoringForm.setEditableQuestionIndex(questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);

	String editableQuestion = "";
	String editableFeedback = "";
	String editableMark = "";
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
	    String question = mcQuestionContentDTO.getQuestion();
	    String displayOrder = mcQuestionContentDTO.getDisplayOrder();

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    editableFeedback = mcQuestionContentDTO.getFeedback();
		    editableQuestion = mcQuestionContentDTO.getQuestion();
		    editableMark = mcQuestionContentDTO.getMark();

		    List candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();

		    break;
		}

	    }
	}

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = (McGeneralAuthoringDTO) request
		.getAttribute(MC_GENERAL_AUTHORING_DTO);

	if (mcGeneralAuthoringDTO == null)
	    mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();

	mcGeneralAuthoringDTO.setMarkValue(editableMark);

	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setEditableQuestionText(editableQuestion);
	mcGeneralAuthoringDTO.setEditableQuestionFeedback(editableFeedback);
	mcAuthoringForm.setFeedback(editableFeedback);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();
	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String requestNewEditableQuestionBox = (String) request.getAttribute("requestNewEditableQuestionBox");

	String newQuestion = request.getParameter("newQuestion");

	if ((requestNewEditableQuestionBox != null) && requestNewEditableQuestionBox.equals("true")) {
	    mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	    String feedback = request.getParameter("feedback");
	    mcAuthoringForm.setFeedback(feedback);
	}

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
	return (mapping.findForward("editQuestionBox"));
    }

    /**
     * removes a question from the questions map
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);

	McQuestionContentDTO mcQuestionContentDTO = null;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    String question = mcQuestionContentDTO.getQuestion();
	    String displayOrder = mcQuestionContentDTO.getDisplayOrder();

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	mcQuestionContentDTO.setQuestion("");

	listQuestionContentDTO = AuthoringUtil.reorderListQuestionContentDTO(listQuestionContentDTO, questionIndex);

	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);
	String activeModule = request.getParameter(ACTIVE_MODULE);

	String richTextTitle = request.getParameter(TITLE);

	String richTextInstructions = request.getParameter(INSTRUCTIONS);

	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));

	if (mcContent == null) {
	    mcContent = mcService.retrieveMc(new Long(defaultContentIdStr));
	}

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
	return (mapping.findForward(LOAD_MONITORING));
    }

    /**
     * moves a question down in the list
     * 
     * moveQuestionDown
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveQuestionDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);

	listQuestionContentDTO = AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "down");

	listQuestionContentDTO = AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);
	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
	return (mapping.findForward(LOAD_MONITORING));
    }

    /**
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	listQuestionContentDTO = AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "up");
	listQuestionContentDTO = AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);
	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);
	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	return (mapping.findForward(LOAD_MONITORING));
    }

    /**
     * moves a candidate dwn in the list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String candidateIndex = request.getParameter("candidateIndex");
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	AuthoringUtil authoringUtil = new AuthoringUtil();
	boolean validateCandidateAnswersNotBlank = authoringUtil.validateCandidateAnswersNotBlank(request);
	ActionMessages errors = new ActionMessages();

	if (!validateCandidateAnswersNotBlank) {
	    ActionMessage error = new ActionMessage("candidates.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    logger.debug("errors saved: " + errors);
	}

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);

	if (errors.isEmpty()) {
	    List candidates = new LinkedList();
	    List listCandidates = new LinkedList();
	    String editableQuestion = "";
	    Iterator listIterator = listQuestionContentDTO.iterator();
	    while (listIterator.hasNext()) {
		McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

		String question = mcQuestionContentDTO.getQuestion();
		String displayOrder = mcQuestionContentDTO.getDisplayOrder();

		if ((displayOrder != null) && (!displayOrder.equals(""))) {
		    if (displayOrder.equals(questionIndex)) {
			editableQuestion = mcQuestionContentDTO.getQuestion();

			candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();

			listCandidates = AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "down");

			mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);

			break;
		    }

		}
	    }
	}

	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);

	String richTextTitle = request.getParameter(TITLE);

	String richTextInstructions = request.getParameter(INSTRUCTIONS);

	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());

	return newEditableQuestionBox(mapping, form, request, response);
    }

    /**
     * 
     * moves a candidate up in the list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String totalMarks = request.getParameter("totalMarks");
	String candidateIndex = request.getParameter("candidateIndex");
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	boolean validateCandidateAnswersNotBlank = authoringUtil.validateCandidateAnswersNotBlank(request);

	ActionMessages errors = new ActionMessages();

	if (!validateCandidateAnswersNotBlank) {
	    ActionMessage error = new ActionMessage("candidates.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    logger.debug("errors saved: " + errors);
	}

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);

	if (errors.isEmpty()) {
	    List candidates = new LinkedList();
	    List listCandidates = new LinkedList();
	    String editableQuestion = "";
	    Iterator listIterator = listQuestionContentDTO.iterator();
	    while (listIterator.hasNext()) {
		McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
		String question = mcQuestionContentDTO.getQuestion();
		String displayOrder = mcQuestionContentDTO.getDisplayOrder();

		if ((displayOrder != null) && (!displayOrder.equals(""))) {
		    if (displayOrder.equals(questionIndex)) {
			editableQuestion = mcQuestionContentDTO.getQuestion();

			candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();
			listCandidates = AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "up");
			mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
			mcQuestionContentDTO.setCaCount(new Integer(listCandidates.size()).toString());

			break;
		    }

		}
	    }
	}

	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);
	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());

	return newEditableQuestionBox(mapping, form, request, response);
    }

    /**
     * removes a candidate from the list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String candidateIndex = request.getParameter("candidateIndex");
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	String totalMarks = request.getParameter("totalMarks");
	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	AuthoringUtil authoringUtil = new AuthoringUtil();
	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);

	McQuestionContentDTO mcQuestionContentDTO = null;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    String question = mcQuestionContentDTO.getQuestion();
	    String displayOrder = mcQuestionContentDTO.getDisplayOrder();

	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	mcQuestionContentDTO.setListCandidateAnswersDTO(caList);

	List candidateAnswers = mcQuestionContentDTO.getListCandidateAnswersDTO();

	McCandidateAnswersDTO mcCandidateAnswersDTO = null;
	Iterator listCaIterator = candidateAnswers.iterator();
	int caIndex = 0;
	while (listCaIterator.hasNext()) {
	    caIndex++;
	    mcCandidateAnswersDTO = (McCandidateAnswersDTO) listCaIterator.next();

	    if (caIndex == new Integer(candidateIndex).intValue()) {
		mcCandidateAnswersDTO.setCandidateAnswer("");

		break;
	    }
	}

	candidateAnswers = AuthoringUtil.reorderListCandidatesDTO(candidateAnswers);
	mcQuestionContentDTO.setListCandidateAnswersDTO(candidateAnswers);
	mcQuestionContentDTO.setCaCount(new Integer(candidateAnswers.size()).toString());
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);
	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	if (mcContent == null) {
	    mcContent = mcService.retrieveMc(new Long(defaultContentIdStr));
	}
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);
	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);
	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());

	return newEditableQuestionBox(mapping, form, request, response);

    }

    /**
     * 
     * enables adding a new candidate answer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");
	mcAuthoringForm.setQuestionIndex(questionIndex);

	String candidateIndex = request.getParameter("candidateIndex");
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	String totalMarks = request.getParameter("totalMarks");

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);

	AuthoringUtil authoringUtil = new AuthoringUtil();
	List caList = authoringUtil.repopulateCandidateAnswersBox(request, true);

	int caCount = caList.size();
	String newQuestion = request.getParameter("newQuestion");
	String mark = request.getParameter("mark");
	String passmark = request.getParameter("passmark");
	String feedback = request.getParameter("feedback");
	int currentQuestionCount = listQuestionContentDTO.size();
	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	McQuestionContentDTO mcQuestionContentDTOLocal = null;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    mcQuestionContentDTOLocal = (McQuestionContentDTO) listIterator.next();

	    String question = mcQuestionContentDTOLocal.getQuestion();
	    String displayOrder = mcQuestionContentDTOLocal.getDisplayOrder();
	    if ((displayOrder != null) && (!displayOrder.equals(""))) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	if (mcQuestionContentDTOLocal != null) {
	    mcQuestionContentDTOLocal.setListCandidateAnswersDTO(caList);
	    mcQuestionContentDTOLocal.setCaCount(new Integer(caList.size()).toString());
	}

	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);
	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	mcAuthoringForm.setFeedback(feedback);

	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
	request.setAttribute("requestNewEditableQuestionBox", new Boolean(true).toString());

	return newEditableQuestionBox(mapping, form, request, response);
    }

    public ActionForward moveAddedCandidateUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String totalMarks = request.getParameter("totalMarks");

	String candidateIndex = request.getParameter("candidateIndex");
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	boolean validateCandidateAnswersNotBlank = authoringUtil.validateCandidateAnswersNotBlank(request);
	ActionMessages errors = new ActionMessages();

	if (!validateCandidateAnswersNotBlank) {
	    ActionMessage error = new ActionMessage("candidates.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    logger.debug("errors saved: " + errors);
	}

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);
	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	List listAddableQuestionContentDTO = (List) sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);
	if (errors.isEmpty()) {
	    List candidates = new LinkedList();
	    List listCandidates = new LinkedList();

	    Iterator listIterator = listAddableQuestionContentDTO.iterator();
	    /* there is only 1 question dto */
	    while (listIterator.hasNext()) {
		McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

		candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();
		listCandidates = AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "up");

		mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
	    }
	}

	request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
	sessionMap.put(NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);

	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);
	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	return newQuestionBox(mapping, form, request, response);

    }

    public ActionForward moveAddedCandidateDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	String candidateIndex = request.getParameter("candidateIndex");
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	String totalMarks = request.getParameter("totalMarks");
	AuthoringUtil authoringUtil = new AuthoringUtil();
	boolean validateCandidateAnswersNotBlank = authoringUtil.validateCandidateAnswersNotBlank(request);
	ActionMessages errors = new ActionMessages();

	if (!validateCandidateAnswersNotBlank) {
	    ActionMessage error = new ActionMessage("candidates.blank");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    logger.debug("errors saved: " + errors);
	}

	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	List listAddableQuestionContentDTO = (List) sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);

	if (errors.isEmpty()) {
	    List candidates = new LinkedList();
	    List listCandidates = new LinkedList();

	    Iterator listIterator = listAddableQuestionContentDTO.iterator();
	    /* there is only 1 question dto */
	    while (listIterator.hasNext()) {
		McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

		candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();
		listCandidates = AuthoringUtil.swapCandidateNodes(caList, candidateIndex, "down");
		mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);
	    }
	}

	request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
	sessionMap.put(NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);
	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	return newQuestionBox(mapping, form, request, response);
    }

    public ActionForward removeAddedCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	String httpSessionID = mcAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String candidateIndex = request.getParameter("candidateIndex");
	mcAuthoringForm.setCandidateIndex(candidateIndex);

	String totalMarks = request.getParameter("totalMarks");

	AuthoringUtil authoringUtil = new AuthoringUtil();
	List caList = authoringUtil.repopulateCandidateAnswersBox(request, false);

	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	List listAddableQuestionContentDTO = (List) sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);

	List candidates = new LinkedList();
	List listCandidates = new LinkedList();

	Iterator listIterator = listAddableQuestionContentDTO.iterator();
	/* there is only 1 question dto */
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    candidates = mcQuestionContentDTO.getListCandidateAnswersDTO();
	    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);

	    List candidateAnswers = mcQuestionContentDTO.getListCandidateAnswersDTO();
	    McCandidateAnswersDTO mcCandidateAnswersDTO = null;
	    Iterator listCaIterator = candidateAnswers.iterator();
	    int caIndex = 0;
	    while (listCaIterator.hasNext()) {
		caIndex++;
		mcCandidateAnswersDTO = (McCandidateAnswersDTO) listCaIterator.next();
		if (caIndex == new Integer(candidateIndex).intValue()) {
		    mcCandidateAnswersDTO.setCandidateAnswer("");
		    break;
		}
	    }

	    candidateAnswers = AuthoringUtil.reorderListCandidatesDTO(candidateAnswers);
	    mcQuestionContentDTO.setListCandidateAnswersDTO(candidateAnswers);
	    mcQuestionContentDTO.setCaCount(new Integer(candidateAnswers.size()).toString());
	}

	request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
	sessionMap.put(NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);
	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	if (mcContent == null) {
	    mcContent = mcService.retrieveMc(new Long(defaultContentIdStr));
	}

	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	String newQuestion = request.getParameter("newQuestion");
	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);

	String feedback = request.getParameter("feedback");
	mcAuthoringForm.setFeedback(feedback);

	String mark = request.getParameter("mark");
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
	return newQuestionBox(mapping, form, request, response);

    }

    public ActionForward newAddedCandidateBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	McAuthoringForm mcAuthoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String httpSessionID = mcAuthoringForm.getHttpSessionID();
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	String candidateIndex = request.getParameter("candidateIndex");
	mcAuthoringForm.setCandidateIndex(candidateIndex);
	String totalMarks = request.getParameter("totalMarks");
	List listQuestionContentDTO = (List) sessionMap.get(LIST_QUESTION_CONTENT_DTO_KEY);
	AuthoringUtil authoringUtil = new AuthoringUtil();
	List caList = authoringUtil.repopulateCandidateAnswersBox(request, true);
	int caCount = caList.size();
	String newQuestion = request.getParameter("newQuestion");
	String mark = request.getParameter("mark");
	String passmark = request.getParameter("passmark");

	String feedback = request.getParameter("feedback");
	int currentQuestionCount = listQuestionContentDTO.size();
	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	List listAddableQuestionContentDTO = (List) sessionMap.get(NEW_ADDABLE_QUESTION_CONTENT_KEY);
	List candidates = new LinkedList();
	List listCandidates = new LinkedList();

	Iterator listIterator = listAddableQuestionContentDTO.iterator();
	/* there is only 1 question dto */
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
	    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
	    mcQuestionContentDTO.setCaCount(new Integer(caList.size()).toString());
	}

	request.setAttribute(NEW_ADDABLE_QUESTION_CONTENT_LIST, listAddableQuestionContentDTO);
	sessionMap.put(NEW_ADDABLE_QUESTION_CONTENT_KEY, listAddableQuestionContentDTO);

	sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	mcAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	String richTextTitle = request.getParameter(TITLE);
	String richTextInstructions = request.getParameter(INSTRUCTIONS);
	sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = new Long(mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE)).toString();
	;
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	mcGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	mcAuthoringForm.setTitle(richTextTitle);

	mcGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	mcGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	McUtils.setFormProperties(request, mcService, mcAuthoringForm, mcGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	mcGeneralAuthoringDTO.setToolContentID(strToolContentID);
	mcGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	mcGeneralAuthoringDTO.setActiveModule(activeModule);
	mcGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setToolContentID(strToolContentID);
	mcAuthoringForm.setHttpSessionID(httpSessionID);
	mcAuthoringForm.setActiveModule(activeModule);
	mcAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	mcAuthoringForm.setCurrentTab("3");

	request.setAttribute(LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	mcGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	Map marksMap = authoringUtil.buildMarksMap();
	mcGeneralAuthoringDTO.setMarksMap(marksMap);

	Map passMarksMap = authoringUtil.buildDynamicPassMarkMap(listQuestionContentDTO, false);
	mcGeneralAuthoringDTO.setPassMarksMap(passMarksMap);

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);
	mcAuthoringForm.setTotalMarks(totalMark);
	mcGeneralAuthoringDTO.setTotalMarks(totalMark);

	Map correctMap = authoringUtil.buildCorrectMap();
	mcGeneralAuthoringDTO.setCorrectMap(correctMap);

	mcGeneralAuthoringDTO.setEditableQuestionText(newQuestion);
	mcAuthoringForm.setFeedback(feedback);
	mcGeneralAuthoringDTO.setMarkValue(mark);

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	McContent mcContent = mcService.retrieveMc(new Long(strToolContentID));
	setupCommonScreenData(mcContent, mcService, request);

	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
	return newQuestionBox(mapping, form, request, response);
    }

    /**
     * prepares reflection data
     * 
     * @param request
     * @param mcContent
     * @param mcService
     * @param userID
     * @param exportMode
     */
    public void prepareReflectionData(HttpServletRequest request, McContent mcContent, IMcService mcService,
	    String userID, boolean exportMode) {
	List reflectionsContainerDTO = new LinkedList();

	if (userID == null) {
	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) {
		McSession mcSession = (McSession) sessionIter.next();
		for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) {
		    McQueUsr user = (McQueUsr) userIter.next();

		    NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(user.getQueUsrId()
				    .toString()));

		    if (notebookEntry != null) {
			ReflectionDTO reflectionDTO = new ReflectionDTO();
			reflectionDTO.setUserId(user.getQueUsrId().toString());
			reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
			reflectionDTO.setUserName(user.getUsername());
			reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
			reflectionDTO.setEntry(notebookEntryPresentable);
			reflectionsContainerDTO.add(reflectionDTO);
		    }

		}
	    }
	} else {
	    // single user mode
	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) {
		McSession mcSession = (McSession) sessionIter.next();
		for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) {
		    McQueUsr user = (McQueUsr) userIter.next();
		    if (user.getQueUsrId().toString().equals(userID)) {
			NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(user.getQueUsrId()
					.toString()));

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
			    reflectionDTO.setUserName(user.getUsername());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
			    reflectionDTO.setEntry(notebookEntryPresentable);
			    reflectionsContainerDTO.add(reflectionDTO);
			}

		    }
		}
	    }

	}

	request.getSession().setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

	if (exportMode) {
	    request.getSession().setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	}
    }

    /**
     * allows viewing users reflection data
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String uid = request.getParameter("uid");
	String userId = request.getParameter("userId");
	String userName = request.getParameter("userName");
	String sessionId = request.getParameter("sessionId");
	NotebookEntry notebookEntry = mcService.getEntry(new Long(sessionId), CoreNotebookConstants.NOTEBOOK_TOOL,
		MY_SIGNATURE, new Integer(userId));

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = new McGeneralLearnerFlowDTO();
	if (notebookEntry != null) {
	    String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
	    mcGeneralLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
	    mcGeneralLearnerFlowDTO.setUserName(userName);
	}

	request.setAttribute(MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	return mapping.findForward(LEARNER_NOTEBOOK);
    }

    /**
     * 
     * @param request
     * @param mcContent
     * @param mcService
     * @param userID
     * @param exportMode
     * @param currentSessionId
     */
    public void prepareReflectionData(HttpServletRequest request, McContent mcContent, IMcService mcService,
	    String userID, boolean exportMode, String currentSessionId) {

	List reflectionsContainerDTO = new LinkedList();

	reflectionsContainerDTO = getReflectionList(mcContent, userID, mcService);

	request.setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

	if (exportMode) {
	    request.getSession().setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	}
    }

    /**
     * 
     * returns reflection data for all sessions
     * 
     * @param mcContent
     * @param userID
     * @param mcService
     * @return
     */
    public List getReflectionList(McContent mcContent, String userID, IMcService mcService) {
	List reflectionsContainerDTO = new LinkedList();
	if (userID == null) {
	    // all users mode
	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) {
		McSession mcSession = (McSession) sessionIter.next();

		for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) {
		    McQueUsr user = (McQueUsr) userIter.next();

		    NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(user.getQueUsrId()
				    .toString()));

		    if (notebookEntry != null) {
			ReflectionDTO reflectionDTO = new ReflectionDTO();
			reflectionDTO.setUserId(user.getQueUsrId().toString());
			reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
			reflectionDTO.setUserName(user.getFullname());
			reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
			reflectionDTO.setEntry(notebookEntryPresentable);
			reflectionsContainerDTO.add(reflectionDTO);
		    }
		}
	    }
	} else {
	    // single user mode
	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) {
		McSession mcSession = (McSession) sessionIter.next();
		for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) {
		    McQueUsr user = (McQueUsr) userIter.next();
		    if (user.getQueUsrId().toString().equals(userID)) {
			NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(user.getQueUsrId()
					.toString()));

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
			    reflectionDTO.setUserName(user.getFullname());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
			    reflectionDTO.setEntry(notebookEntryPresentable);
			    reflectionsContainerDTO.add(reflectionDTO);
			}
		    }
		}
	    }
	}

	return reflectionsContainerDTO;
    }

    /**
     * returns reflection data for a specific session
     * 
     * @param mcContent
     * @param userID
     * @param mcService
     * @param currentSessionId
     * @return
     */
    public List getReflectionListForSession(McContent mcContent, String userID, IMcService mcService,
	    String currentSessionId) {
	List reflectionsContainerDTO = new LinkedList();
	if (userID == null) {
	    // all users mode
	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) {
		McSession mcSession = (McSession) sessionIter.next();

		if (currentSessionId.equals(mcSession.getMcSessionId())) {

		    for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) {
			McQueUsr user = (McQueUsr) userIter.next();

			NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(user.getQueUsrId()
					.toString()));

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
			    reflectionDTO.setUserName(user.getFullname());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
			    reflectionDTO.setEntry(notebookEntryPresentable);
			    reflectionsContainerDTO.add(reflectionDTO);
			}
		    }
		}
	    }
	} else {
	    // single user mode
	    for (Iterator sessionIter = mcContent.getMcSessions().iterator(); sessionIter.hasNext();) {
		McSession mcSession = (McSession) sessionIter.next();

		if (currentSessionId.equals(mcSession.getMcSessionId())) {
		    for (Iterator userIter = mcSession.getMcQueUsers().iterator(); userIter.hasNext();) {
			McQueUsr user = (McQueUsr) userIter.next();
			if (user.getQueUsrId().toString().equals(userID)) {
			    NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
				    CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(user.getQueUsrId()
					    .toString()));
			    if (notebookEntry != null) {
				ReflectionDTO reflectionDTO = new ReflectionDTO();
				reflectionDTO.setUserId(user.getQueUsrId().toString());
				reflectionDTO.setSessionId(mcSession.getMcSessionId().toString());
				reflectionDTO.setUserName(user.getFullname());
				reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
				String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
				reflectionDTO.setEntry(notebookEntryPresentable);
				reflectionsContainerDTO.add(reflectionDTO);
			    }
			}
		    }

		}
	    }
	}

	return reflectionsContainerDTO;
    }

    /**
     * prepareEditActivityScreenData
     * 
     * @param request
     * @param mcContent
     */
    public void prepareEditActivityScreenData(HttpServletRequest request, McContent mcContent) {
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();

	mcGeneralAuthoringDTO.setActivityTitle(mcContent.getTitle());
	mcGeneralAuthoringDTO.setActivityInstructions(mcContent.getInstructions());
	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
    }

    /**
     * 
     * @param request
     * @param mcMonitoringForm
     * @param mcGeneralMonitoringDTO
     */
    protected void repopulateRequestParameters(HttpServletRequest request, McMonitoringForm mcMonitoringForm,
	    McGeneralMonitoringDTO mcGeneralMonitoringDTO) {

	String toolContentID = request.getParameter(TOOL_CONTENT_ID);
	mcMonitoringForm.setToolContentID(toolContentID);
	mcGeneralMonitoringDTO.setToolContentID(toolContentID);

	String activeModule = request.getParameter(ACTIVE_MODULE);
	mcMonitoringForm.setActiveModule(activeModule);
	mcGeneralMonitoringDTO.setActiveModule(activeModule);

	String defineLaterInEditMode = request.getParameter(DEFINE_LATER_IN_EDIT_MODE);
	mcMonitoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
	mcGeneralMonitoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);

	String isToolSessionChanged = request.getParameter(IS_TOOL_SESSION_CHANGED);
	mcMonitoringForm.setIsToolSessionChanged(isToolSessionChanged);
	mcGeneralMonitoringDTO.setIsToolSessionChanged(isToolSessionChanged);

	String responseId = request.getParameter(RESPONSE_ID);
	mcMonitoringForm.setResponseId(responseId);
	mcGeneralMonitoringDTO.setResponseId(responseId);

	String currentUid = request.getParameter(CURRENT_UID);
	mcMonitoringForm.setCurrentUid(currentUid);
	mcGeneralMonitoringDTO.setCurrentUid(currentUid);
    }

    /**
     * 
     * @param mcContent
     * @param mcService
     * @param request
     */
    protected void setupCommonScreenData(McContent mcContent, IMcService mcService, HttpServletRequest request) {
	/* setting up USER_EXCEPTION_NO_TOOL_SESSIONS, from here */
	McGeneralMonitoringDTO mcGeneralMonitoringDTO = new McGeneralMonitoringDTO();
	mcGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	if (mcService.studentActivityOccurredGlobal(mcContent)) {
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
	mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());

	List attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
	mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
	mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());

	request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
	/* .. till here */

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(mcService, mcContent);

	if (notebookEntriesExist) {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = (String) mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		// there are no online student activity but there are reflections
		request.setAttribute(NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.setSessionUserCount(mcContent, mcGeneralMonitoringDTO);
	List listAllGroupsDTO = MonitoringUtil.buildGroupBasedSessionData(mcContent, mcService);
	request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);
	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);
    }

    /**
     * downloadMarks
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward downloadMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	MessageService messageService = getMessageService();

	String currentMonitoredToolSession = request.getParameter("monitoredToolSessionId");
	McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String toolContentID = mcMonitoringForm.getToolContentID();

	McContent mcContent = mcService.retrieveMc(new Long(toolContentID));

	byte[] spreadsheet = null;

	try {
	    spreadsheet = mcService.prepareSessionDataSpreadsheet(request, mcContent, currentMonitoredToolSession);
	} catch (Exception e) {
	    log.error("Error preparing spreadsheet: ", e);
	    request.setAttribute("errorName", messageService.getMessage("error.monitoring.spreadsheet.download"));
	    request.setAttribute("errorMessage", e);
	    return mapping.findForward("error");
	}

	// construct download file response header
	OutputStream out = response.getOutputStream();
	String fileName = "lams_mcq_" + currentMonitoredToolSession + ".xls";
	String mineType = "application/vnd.ms-excel";
	String header = "attachment; filename=\"" + fileName + "\";";
	response.setContentType(mineType);
	response.setHeader("Content-Disposition", header);

	// write response
	try {
	    out.write(spreadsheet);
	    out.flush();
	} finally {
	    try {
		if (out != null)
		    out.close();
	    } catch (IOException e) {
	    }
	}

	return null;
    }

    /**
     * Set Submission Deadline
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.retrieveMc(contentID);

	Long dateParameter = WebUtil.readLongParam(request, McAppConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		    .getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	}
	mcContent.setSubmissionDeadline(tzSubmissionDeadline);
	mcService.updateMc(mcContent);

	return null;
    }

    /**
     * Return ResourceService bean.
     */
    private MessageService getMessageService() {
	return (MessageService) McServiceProxy.getMessageService(getServlet().getServletContext());
    }
}
