/***************************************************************************
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2 as
published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
USA

http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

/**
 * @author Ozgur Demirtas
 * 
 <action
 path="/monitoring"
 type="org.lamsfoundation.lams.tool.qa.web.QaMonitoringAction"
 name="QaMonitoringForm"
 scope="request"
 parameter="dispatch"
 unknown="false"
 validate="true">

 <forward
 name="loadMonitoring"
 path="/monitoring/MonitoringMaincontent.jsp"
 redirect="false"
 />

 <forward
 name="refreshMonitoring"
 path="/monitoring/MonitoringMaincontent.jsp"
 redirect="false"
 />

 <forward
 name="load"
 path="/monitoring/MonitoringMaincontent.jsp"
 redirect="false"
 />

 <forward
 name="loadViewOnly"
 path="/authoring/AuthoringTabsHolder.jsp"
 redirect="false"
 />

 <forward
 name="newQuestionBox"
 path="/monitoring/newQuestionBox.jsp"
 redirect="false"
 />

 <forward
 name="editQuestionBox"
 path="/monitoring/editQuestionBox.jsp"
 redirect="false"
 />

 <forward
 name="starter"
 path="/index.jsp"
 redirect="false"
 />


 <forward
 name="learnerNotebook"
 path="/monitoring/LearnerNotebook.jsp"
 redirect="false"
 />

 <forward
 name="errorList"
 path="/QaErrorBox.jsp"
 redirect="false"
 />
 </action>


 */

/* $$Id$$ */

package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.lamsfoundation.lams.tool.qa.EditActivityDTO;
import org.lamsfoundation.lams.tool.qa.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.GeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaQuestionContentDTO;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.ReflectionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

public class QaMonitoringAction extends LamsDispatchAction implements QaAppConstants {
    static Logger logger = Logger.getLogger(QaMonitoringAction.class.getName());

    public static String SELECTBOX_SELECTED_TOOL_SESSION = "selectBoxSelectedToolSession";
    public static Integer READABLE_TOOL_SESSION_COUNT = new Integer(1);

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	QaMonitoringAction.logger.debug("dispatching unspecified...");
	return null;
    }

    public void initStatsContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response, GeneralMonitoringDTO generalMonitoringDTO) throws IOException,
	    ServletException {
	QaMonitoringAction.logger.debug("starting  initStatsContent...");

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	IQaService qaService = null;
	if (getServlet() != null) {
	    qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	} else {
	    qaService = qaMonitoringForm.getQaService();
	}

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	} else {
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	}

	refreshStatsData(request, qaMonitoringForm, qaService, generalMonitoringDTO);
	generalMonitoringDTO.setEditResponse(new Boolean(false).toString());

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	prepareEditActivityScreenData(request, qaContent);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	/** getting instructions screen content from here... */
	generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	generalMonitoringDTO.setAttachmentList(attachmentList);
	generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);
	QaMonitoringAction.logger.debug("ending  initStatsContent...");

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

    }

    /**
     * switches to instructions tab of the monitoring url.
     * getInstructions(ActionMapping mapping, ActionForm form,
     * HttpServletRequest request, HttpServletResponse response) throws
     * IOException, ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispatching getInstructions..." + request);
	initInstructionsContent(mapping, form, request, response);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public void initInstructionsContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("starting initInstructionsContent...");
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
	

	GeneralMonitoringDTO generalMonitoringDTO = new GeneralMonitoringDTO();

	IQaService qaService = null;
	if (getServlet() != null) {
	    qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	} else {
	    qaService = qaMonitoringForm.getQaService();
	}

	

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	generalMonitoringDTO.setEditResponse(new Boolean(false).toString());

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	/** getting instructions screen content from here... */
	generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	generalMonitoringDTO.setAttachmentList(attachmentList);
	generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);

	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);
	
    }

    /**
     * activates editActivity screen ActionForward editActivity(ActionMapping
     * mapping, ActionForm form, HttpServletRequest request, HttpServletResponse
     * response) throws IOException, ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward editActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispatching editActivity...");
	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
	

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	if (qaService == null) {
	    qaService = qaMonitoringForm.getQaService();
	}

	QaStarterAction qaStarterAction = new QaStarterAction();

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	request.setAttribute(QaAppConstants.SOURCE_MC_STARTER, "monitoring");
	

	/*
	 * it is possible that the content is being used by some learners. In this situation, the content is marked as
	 * "in use" and content in use is not modifiable
	 */
	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	QaMonitoringAction.logger.debug("qaContent:" + qaContent.getUid());

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	GeneralMonitoringDTO generalMonitoringDTO = new GeneralMonitoringDTO();
	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("student activity occurred on this content:" + qaContent);
	    generalMonitoringDTO.setUserExceptionContentInUse(new Boolean(true).toString());
	    QaMonitoringAction.logger.debug("forwarding to: " + QaAppConstants.LOAD_MONITORING);
	    return mapping.findForward(QaAppConstants.LOAD_MONITORING);
	}

	if (qaContent.getTitle() == null) {
	    generalMonitoringDTO.setActivityTitle("Questions and Answers");
	    generalMonitoringDTO.setActivityInstructions("Please answer the questions.");
	} else {
	    generalMonitoringDTO.setActivityTitle(qaContent.getTitle());
	    generalMonitoringDTO.setActivityInstructions(qaContent.getInstructions());
	}

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/** getting instructions screen content from here... */
	generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	generalMonitoringDTO.setAttachmentList(attachmentList);
	generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	prepareEditActivityScreenData(request, qaContent);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);

	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	/* note that we are casting monitoring form subclass into Authoring form */
	QaMonitoringAction.logger
		.debug("watch here: note that we are casting monitoring form subclass into Authoring form");
	return qaStarterAction.executeDefineLater(mapping, qaMonitoringForm, request, response, qaService);
    }

    /**
     * switches to summary tab of the monitoring url
     * 
     * getSummary(ActionMapping mapping, ActionForm form, HttpServletRequest
     * request, HttpServletResponse response) throws IOException,
     * ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward getSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("start getSummary...");
	initSummaryContent(mapping, form, request, response);
	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public void initSummaryContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("initSummaryContent...");
	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
	

	GeneralMonitoringDTO generalMonitoringDTO = new GeneralMonitoringDTO();

	IQaService qaService = null;
	if (getServlet() != null) {
	    qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	} else {
	    qaService = qaMonitoringForm.getQaService();
	}



	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	/* this section is related to summary tab. Starts here. */
	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	

	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);


	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	/* true means there is at least 1 response */
	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	generalMonitoringDTO.setEditResponse(new Boolean(false).toString());

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	prepareEditActivityScreenData(request, qaContent);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	/* getting instructions screen content from here... */
	generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	generalMonitoringDTO.setAttachmentList(attachmentList);
	generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/* ...till here */

	// setting up the advanced summary for LDEV-1662
	request.setAttribute("showOtherAnswers", qaContent.isShowOtherAnswers());
	request.setAttribute("usernameVisible", qaContent.isUsernameVisible());
	request.setAttribute("questionsSequenced", qaContent.isQuestionsSequenced());
	request.setAttribute("lockWhenFinished", qaContent.isLockWhenFinished());
	request.setAttribute("reflect", qaContent.isReflect());
	request.setAttribute("reflectionSubject", qaContent.getReflectionSubject());

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	QaMonitoringAction.logger.debug("end  initSummaryContent...");
    }

    public ActionForward editActivityQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	QaMonitoringAction.logger.debug("dispatching editActivityQuestions...");

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
	

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	GeneralMonitoringDTO generalMonitoringDTO = new GeneralMonitoringDTO();

	generalMonitoringDTO.setMonitoredContentInUse(new Boolean(false).toString());

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	editActivityDTO.setMonitoredContentInUse(new Boolean(false).toString());
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	generalMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String httpSessionID = request.getParameter("httpSessionID");
	
	qaMonitoringForm.setHttpSessionID(httpSessionID);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	qaMonitoringForm.setTitle(qaContent.getTitle());

	QaUtils.setDefineLater(request, true, strToolContentID, qaService);

	prepareEditActivityScreenData(request, qaContent);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	/** getting instructions screen content from here... */
	generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	generalMonitoringDTO.setAttachmentList(attachmentList);
	generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/** ...till here * */

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	List listQuestionContentDTO = new LinkedList();

	Iterator queIterator = qaContent.getQaQueContents().iterator();
	while (queIterator.hasNext()) {
	    QaQueContent qaQueContent = (QaQueContent) queIterator.next();
	    if (qaQueContent != null) {
		QaQuestionContentDTO qaQuestionContentDTO = new QaQuestionContentDTO(qaQueContent);
		listQuestionContentDTO.add(qaQuestionContentDTO);
	    }
	}
	
	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = (QaGeneralAuthoringDTO) request
		.getAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO);
	qaGeneralAuthoringDTO.setActiveModule(QaAppConstants.MONITORING);

	qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);
	qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = generalMonitoringDTO.getUserExceptionNoToolSessions();
	    QaMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		QaMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(QaAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    /**
     * gets called when the user selects a group from dropdown box in the
     * summary tab
     * 
     * submitSession(ActionMapping mapping, ActionForm form, HttpServletRequest
     * request, HttpServletResponse response) throws IOException,
     * ServletException
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
	QaMonitoringAction.logger.debug("dispatching submitSession...");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	String currentMonitoredToolSession = qaMonitoringForm.getSelectedToolSessionId();
	QaMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(1));

	    QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(currentMonitoredToolSession)
		    .longValue());
	    QaMonitoringAction.logger.debug("retrieving qaSession name: " + qaSession.getSession_name());
	    request.setAttribute(QaAppConstants.CURRENT_SESSION_NAME, qaSession.getSession_name());
	}

	QaMonitoringAction.logger.debug("SELECTION_CASE: " + request.getAttribute(QaAppConstants.SELECTION_CASE));
	request.setAttribute(QaAppConstants.CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	QaMonitoringAction.logger.debug("editResponse: " + editResponse);
	qaMonitoringForm.setEditResponse(editResponse);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false,
		currentMonitoredToolSession);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    /**
     * enables the user to edit responses ActionForward
     * editResponse(ActionMapping mapping, ActionForm form, HttpServletRequest
     * request, HttpServletResponse response) throws IOException,
     * ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward editResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispatching editResponse...");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	QaMonitoringAction.logger.debug("editResponse: " + editResponse);
	qaMonitoringForm.setEditResponse(editResponse);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String responseId = qaMonitoringForm.getResponseId();
	QaMonitoringAction.logger.debug("responseId: " + responseId);
	request.getSession().setAttribute(QaAppConstants.EDITABLE_RESPONSE_ID, responseId);

	QaUsrResp qaUsrResp = qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	

	refreshUserInput(request, qaMonitoringForm);

	QaContent qaContent = qaUsrResp.getQaQueContent().getQaContent();
	

	String currentMonitoredToolSession = qaMonitoringForm.getSelectedToolSessionId();
	

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(1));

	    QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(currentMonitoredToolSession)
		    .longValue());
	    QaMonitoringAction.logger.debug("retrieving qaSession name: " + qaSession.getSession_name());
	    request.setAttribute(QaAppConstants.CURRENT_SESSION_NAME, qaSession.getSession_name());
	}

	QaMonitoringAction.logger.debug("SELECTION_CASE: " + request.getAttribute(QaAppConstants.SELECTION_CASE));
	request.setAttribute(QaAppConstants.CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	

	refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, true,
		currentMonitoredToolSession);

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward editGroupResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispatching editGroupResponse...");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	String editableSessionId = request.getParameter("sessionId");
	QaMonitoringAction.logger.debug("editableSessionId: " + editableSessionId);
	request.setAttribute("editableSessionId", editableSessionId);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String responseId = qaMonitoringForm.getResponseId();
	QaMonitoringAction.logger.debug("responseId: " + responseId);
	request.getSession().setAttribute(QaAppConstants.EDITABLE_RESPONSE_ID, responseId);

	QaUsrResp qaUsrResp = qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	

	refreshUserInput(request, qaMonitoringForm);

	QaContent qaContent = qaUsrResp.getQaQueContent().getQaContent();
	

	String currentMonitoredToolSession = qaMonitoringForm.getSelectedToolSessionId();
	QaMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(1));
	}

	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	

	refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, true,
		currentMonitoredToolSession);

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);
	request.setAttribute("currentMonitoredToolSession", "All");

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    /**
     * enables the user to update responses ActionForward
     * updateResponse(ActionMapping mapping, ActionForm form, HttpServletRequest
     * request, HttpServletResponse response) throws IOException,
     * ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward updateResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispatching updateResponse...");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	String responseId = qaMonitoringForm.getResponseId();
	QaMonitoringAction.logger.debug("responseId: " + responseId);

	String updatedResponse = request.getParameter("updatedResponse");
	
	QaUsrResp qaUsrResp = qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	

	/*
	 * write out the audit log entry. If you move this after the update of the response, then make sure you update
	 * the audit call to use a copy of the original answer
	 */
	qaService.getAuditService().logChange(QaAppConstants.MY_SIGNATURE, qaUsrResp.getQaQueUser().getQueUsrId(),
		qaUsrResp.getQaQueUser().getUsername(), qaUsrResp.getAnswer(), updatedResponse);

	qaUsrResp.setAnswer(updatedResponse);
	qaService.updateQaUsrResp(qaUsrResp);
	QaMonitoringAction.logger.debug("response updated.");

	refreshUserInput(request, qaMonitoringForm);

	String currentMonitoredToolSession = qaMonitoringForm.getSelectedToolSessionId();
	QaMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(2));

	    QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(currentMonitoredToolSession)
		    .longValue());
	    QaMonitoringAction.logger.debug("retrieving qaSession name: " + qaSession.getSession_name());
	    request.setAttribute(QaAppConstants.CURRENT_SESSION_NAME, qaSession.getSession_name());
	} else {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(1));
	}
	
	request.setAttribute(QaAppConstants.CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);

	QaContent qaContent = qaUsrResp.getQaQueContent().getQaContent();
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	

	refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false,
		currentMonitoredToolSession);

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward updateGroupResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispatching updateGroupResponse...");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	String responseId = qaMonitoringForm.getResponseId();
	QaMonitoringAction.logger.debug("responseId: " + responseId);

	String updatedResponse = request.getParameter("updatedResponse");
	
	QaUsrResp qaUsrResp = qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	

	/*
	 * write out the audit log entry. If you move this after the update of the response, then make sure you update
	 * the audit call to use a copy of the original answer
	 */
	qaService.getAuditService().logChange(QaAppConstants.MY_SIGNATURE, qaUsrResp.getQaQueUser().getQueUsrId(),
		qaUsrResp.getQaQueUser().getUsername(), qaUsrResp.getAnswer(), updatedResponse);

	qaUsrResp.setAnswer(updatedResponse);
	qaService.updateQaUsrResp(qaUsrResp);
	QaMonitoringAction.logger.debug("response updated.");

	refreshUserInput(request, qaMonitoringForm);

	String currentMonitoredToolSession = qaMonitoringForm.getSelectedToolSessionId();
	QaMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(1));
	}
	

	QaContent qaContent = qaUsrResp.getQaQueContent().getQaContent();
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessions: " + summaryToolSessions);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	

	refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false,
		currentMonitoredToolSession);

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);
	request.setAttribute("currentMonitoredToolSession", "All");

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    /**
     * enables the user to delete responses ActionForward
     * deleteResponse(ActionMapping mapping, ActionForm form, HttpServletRequest
     * request, HttpServletResponse response) throws IOException,
     * ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispatching deleteResponse...");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	String currentMonitoredToolSession = qaMonitoringForm.getSelectedToolSessionId();
	QaMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(1));
	}

	

	String responseId = qaMonitoringForm.getResponseId();
	QaMonitoringAction.logger.debug("responseId: " + responseId);

	QaUsrResp qaUsrResp = qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	

	qaService.removeUserResponse(qaUsrResp);
	QaMonitoringAction.logger.debug("response deleted.");

	refreshUserInput(request, qaMonitoringForm);

	QaContent qaContent = qaUsrResp.getQaQueContent().getQaContent();
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessions: " + summaryToolSessions);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	

	refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false,
		currentMonitoredToolSession);

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    /**
     * refreshUserInput(HttpServletRequest request)
     * 
     * @param request
     */
    public void refreshUserInput(HttpServletRequest request, QaMonitoringForm qaMonitoringForm) {
	QaMonitoringAction.logger.debug("starting refreshUserInput: " + qaMonitoringForm);

	IQaService qaService = null;
	if (getServlet() != null) {
	    qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	} else {
	    qaService = qaMonitoringForm.getQaService();
	}


	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessions: " + summaryToolSessions);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);

	// prepareReflectionData(request, qaContent, qaService,null, false);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);
	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
    }

    /**
     * persists error messages to request scope persistError(HttpServletRequest
     * request, String message)
     * 
     * @param request
     * @param message
     */
    public void persistError(HttpServletRequest request, String message) {
	ActionMessages errors = new ActionMessages();
	errors.add(Globals.ERROR_KEY, new ActionMessage(message));
	QaMonitoringAction.logger.debug("add " + message + "  to ActionMessages:");
	saveErrors(request, errors);
    }

    /**
     * populates data for summary screen, view all results screen and export
     * portfolio.
     * 
     * User id is needed if isUserNamesVisible is false && learnerRequest is
     * true, as it is required to work out if the data being analysed is the
     * current user.
     * 
     * @param request
     * @param qaContent
     * @param qaService
     * @param isUserNamesVisible
     * @param isLearnerRequest
     * @param currentSessionId
     * @param userId
     */
    public void refreshSummaryData(HttpServletRequest request, QaContent qaContent, IQaService qaService,
	    boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId,
	    GeneralLearnerFlowDTO generalLearnerFlowDTO, boolean setEditResponse, String currentMonitoredToolSession) {
	QaMonitoringAction.logger.debug("starting refreshSummaryData");
	QaMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);
	QaMonitoringAction.logger.debug("isUserNamesVisible: " + isUserNamesVisible);
	QaMonitoringAction.logger.debug("isLearnerRequest: " + isLearnerRequest);

	GeneralMonitoringDTO generalMonitoringDTO = new GeneralMonitoringDTO();

	/* this section is related to summary tab. Starts here. */
	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	if (qaContent.getTitle() == null) {
	    generalMonitoringDTO.setActivityTitle("Questions and Answers");
	    generalMonitoringDTO.setActivityInstructions("Please answer the questions.");
	} else {
	    generalMonitoringDTO.setActivityTitle(qaContent.getTitle());
	    generalMonitoringDTO.setActivityInstructions(qaContent.getInstructions());
	}

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);

	generalMonitoringDTO.setMonitoredContentInUse(new Boolean(false).toString());
	if (isContentInUse == true) {
	    
	    persistError(request, "error.content.inUse");
	    generalMonitoringDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	currentSessionId = currentMonitoredToolSession;

	QaMonitoringAction.logger.debug("using allUsersData to retrieve data: " + isUserNamesVisible);
	List listMonitoredAnswersContainerDTO = MonitoringUtil.buildGroupsQuestionData(request, qaContent, qaService,
		isUserNamesVisible, isLearnerRequest, currentSessionId, userId);

	

	/* getting stats screen content from here... */
	int countAllUsers = qaService.getTotalNumberOfUsers(qaContent);
	QaMonitoringAction.logger.debug("countAllUsers: " + countAllUsers);

	if (countAllUsers == 0) {
	    QaMonitoringAction.logger.debug("error: countAllUsers is 0");
	    generalMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
	}

	generalMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());

	int countSessionComplete = qaService.countSessionComplete(qaContent);
	QaMonitoringAction.logger.debug("countSessionComplete: " + countSessionComplete);

	generalMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
	QaMonitoringAction.logger.debug("ending refreshStatsData with generalMonitoringDTO: " + generalMonitoringDTO);
	/* till here */

	generalMonitoringDTO.setEditResponse(new Boolean(setEditResponse).toString());

	/* getting instructions screen content from here... */
	generalMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	generalMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	generalMonitoringDTO.setAttachmentList(attachmentList);
	generalMonitoringDTO.setDeletedAttachmentList(new ArrayList());
	/* ...till here * */

	if (generalLearnerFlowDTO != null) {
	    
	    QaMonitoringAction.logger
		    .debug("placing LIST_MONITORED_ANSWERS_CONTAINER_DTO within generalLearnerFlowDTO");
	    generalLearnerFlowDTO.setListMonitoredAnswersContainerDTO(listMonitoredAnswersContainerDTO);

	    if (isLearnerRequest) {
		QaMonitoringAction.logger.debug("isLearnerRequest is true.");
		generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(true).toString());
	    }
	    request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
	}

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);
    }

    /**
     * populates data for stats screen refreshStatsData(HttpServletRequest
     * request)
     * 
     * @param request
     */
    public void refreshStatsData(HttpServletRequest request, QaMonitoringForm qaMonitoringForm, IQaService qaService,
	    GeneralMonitoringDTO generalMonitoringDTO) {
	
	
	/* it is possible that no users has ever logged in for the activity yet */

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	qaMonitoringForm.setToolContentID(strToolContentID);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	int countAllUsers = qaService.getTotalNumberOfUsers(qaContent);
	QaMonitoringAction.logger.debug("countAllUsers: " + countAllUsers);

	if (countAllUsers == 0) {
	    QaMonitoringAction.logger.debug("error: countAllUsers is 0");
	    generalMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
	}

	generalMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());

	int countSessionComplete = qaService.countSessionComplete(qaContent);
	QaMonitoringAction.logger.debug("countSessionComplete: " + countSessionComplete);

	generalMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	generalMonitoringDTO.setAttachmentList(attachmentList);
	

	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);
	QaMonitoringAction.logger.debug("ending refreshStatsData");
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);
    }

    public ActionForward showResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	QaMonitoringAction.logger.debug("dispatching showResponse...");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	String currentUid = qaMonitoringForm.getCurrentUid();
	QaMonitoringAction.logger.debug("currentUid: " + currentUid);
	QaUsrResp qaUsrResp = qaService.getAttemptByUID(new Long(currentUid));
	
	qaUsrResp.setVisible(true);
	qaService.updateUserResponse(qaUsrResp);
	qaService.showResponse(qaUsrResp);
	

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	String currentMonitoredToolSession = qaMonitoringForm.getSelectedToolSessionId();
	QaMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	

	refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false,
		currentMonitoredToolSession);

	
	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(1));

	    QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(currentMonitoredToolSession)
		    .longValue());
	    QaMonitoringAction.logger.debug("retrieving qaSession name: " + qaSession.getSession_name());
	    request.setAttribute(QaAppConstants.CURRENT_SESSION_NAME, qaSession.getSession_name());
	}

	
	request.setAttribute(QaAppConstants.CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	QaMonitoringAction.logger.debug("submitting session to refresh the data from the database: ");
	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward showGroupResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	QaMonitoringAction.logger.debug("dispatching showGroupResponse...");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	String currentUid = qaMonitoringForm.getCurrentUid();
	QaMonitoringAction.logger.debug("currentUid: " + currentUid);
	QaUsrResp qaUsrResp = qaService.getAttemptByUID(new Long(currentUid));
	
	qaUsrResp.setVisible(true);
	qaService.updateUserResponse(qaUsrResp);
	qaService.showResponse(qaUsrResp);
	

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	String currentMonitoredToolSession = qaMonitoringForm.getSelectedToolSessionId();
	QaMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	

	refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false,
		currentMonitoredToolSession);

	
	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(1));
	}

	

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);
	request.setAttribute("currentMonitoredToolSession", "All");

	QaMonitoringAction.logger.debug("submitting session to refresh the data from the database: ");
	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward hideResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	QaMonitoringAction.logger.debug("dispatching hideResponse...");
	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String currentUid = qaMonitoringForm.getCurrentUid();
	QaMonitoringAction.logger.debug("currentUid: " + currentUid);
	QaUsrResp qaUsrResp = qaService.getAttemptByUID(new Long(currentUid));
	
	qaUsrResp.setVisible(false);
	qaService.updateUserResponse(qaUsrResp);
	qaService.hideResponse(qaUsrResp);
	

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	qaMonitoringForm.setToolContentID(strToolContentID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	String currentMonitoredToolSession = qaMonitoringForm.getSelectedToolSessionId();
	QaMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(2));

	    QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(currentMonitoredToolSession)
		    .longValue());
	    QaMonitoringAction.logger.debug("retrieving qaSession name: " + qaSession.getSession_name());
	    request.setAttribute(QaAppConstants.CURRENT_SESSION_NAME, qaSession.getSession_name());
	} else {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(1));
	}

	
	request.setAttribute(QaAppConstants.CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	

	refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false,
		currentMonitoredToolSession);

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	QaMonitoringAction.logger.debug("submitting session to refresh the data from the database: ");
	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward hideGroupResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	QaMonitoringAction.logger.debug("dispatching hideGroupResponse...");
	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String currentUid = qaMonitoringForm.getCurrentUid();
	QaMonitoringAction.logger.debug("currentUid: " + currentUid);
	QaUsrResp qaUsrResp = qaService.getAttemptByUID(new Long(currentUid));
	
	qaUsrResp.setVisible(false);
	qaService.updateUserResponse(qaUsrResp);
	qaService.hideResponse(qaUsrResp);
	

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	qaMonitoringForm.setToolContentID(strToolContentID);

	String editResponse = request.getParameter(QaAppConstants.EDIT_RESPONSE);
	
	qaMonitoringForm.setEditResponse(editResponse);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	Map summaryToolSessions = MonitoringUtil.populateToolSessions(request, qaContent, qaService);
	
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS, summaryToolSessions);

	Map summaryToolSessionsId = MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
	QaMonitoringAction.logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	request.setAttribute(QaAppConstants.SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);

	String currentMonitoredToolSession = qaMonitoringForm.getSelectedToolSessionId();
	QaMonitoringAction.logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	if (currentMonitoredToolSession.equals("")) {
	    currentMonitoredToolSession = "All";
	}

	if (currentMonitoredToolSession.equals("All")) {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(2));
	} else {
	    request.setAttribute(QaAppConstants.SELECTION_CASE, new Long(1));
	}

	

	GeneralLearnerFlowDTO generalLearnerFlowDTO = LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	

	refreshSummaryData(request, qaContent, qaService, true, false, null, null, generalLearnerFlowDTO, false,
		currentMonitoredToolSession);

	prepareReflectionData(request, qaContent, qaService, null, false, currentMonitoredToolSession);

	prepareEditActivityScreenData(request, qaContent);

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	boolean isContentInUse = QaUtils.isContentInUse(qaContent);
	QaMonitoringAction.logger.debug("isContentInUse:" + isContentInUse);
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);
	request.setAttribute("currentMonitoredToolSession", "All");

	QaMonitoringAction.logger.debug("submitting session to refresh the data from the database: ");
	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	QaMonitoringAction.logger.debug("dispatching openNotebook...");
	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String uid = request.getParameter("uid");
	QaMonitoringAction.logger.debug("uid: " + uid);

	String userId = request.getParameter("userId");
	QaMonitoringAction.logger.debug("userId: " + userId);

	String userName = request.getParameter("userName");
	QaMonitoringAction.logger.debug("userName: " + userName);

	String sessionId = request.getParameter("sessionId");
	QaMonitoringAction.logger.debug("sessionId: " + sessionId);

	NotebookEntry notebookEntry = qaService.getEntry(new Long(sessionId), CoreNotebookConstants.NOTEBOOK_TOOL,
		QaAppConstants.MY_SIGNATURE, new Integer(userId));

	

	GeneralLearnerFlowDTO generalLearnerFlowDTO = new GeneralLearnerFlowDTO();
	if (notebookEntry != null) {
	    String notebookEntryPresentable = QaUtils.replaceNewLines(notebookEntry.getEntry());
	    generalLearnerFlowDTO.setNotebookEntry(notebookEntryPresentable);
	    generalLearnerFlowDTO.setUserName(userName);
	}

	
	request.setAttribute(QaAppConstants.GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);

	QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(sessionId).longValue());
	

	QaContent qaContent = qaSession.getQaContent();
	

	prepareEditActivityScreenData(request, qaContent);

	GeneralMonitoringDTO generalMonitoringDTO = new GeneralMonitoringDTO();
	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	generalMonitoringDTO.setAttachmentList(attachmentList);

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    generalMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = generalMonitoringDTO.getUserExceptionNoToolSessions();
	    

	    if (userExceptionNoToolSessions.equals("true")) {
		QaMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(QaAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LEARNER_NOTEBOOK);
    }

    public void prepareEditActivityScreenData(HttpServletRequest request, QaContent qaContent) {
	
	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();

	qaGeneralAuthoringDTO.setActivityTitle(qaContent.getTitle());
	qaGeneralAuthoringDTO.setActivityInstructions(qaContent.getInstructions());

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
    }

    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispatching proxy submitAllContent...");

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
	

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	QaMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);

	ActionMessages errors = new ActionMessages();
	QaMonitoringAction.logger.debug("listQuestionContentDTO size: " + listQuestionContentDTO.size());

	if (listQuestionContentDTO.size() == 0) {
	    ActionMessage error = new ActionMessage("questions.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}
	QaMonitoringAction.logger.debug("errors: " + errors);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	QaMonitoringAction.logger.debug("qaGeneralAuthoringDTO: " + qaGeneralAuthoringDTO);

	QaMonitoringAction.logger.debug("qaGeneralAuthoringDTO now: " + qaGeneralAuthoringDTO);
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	QaContent qaContentTest = qaService.loadQa(new Long(strToolContentID).longValue());

	QaMonitoringAction.logger.debug("errors: " + errors);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    QaMonitoringAction.logger.debug("errors saved: " + errors);
	}

	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();

	QaContent qaContent = qaContentTest;
	if (errors.isEmpty()) {
	    /*
	     * removes deleted entries in the questions table based on mapQuestionContent
	     */
	    authoringUtil.removeRedundantQuestions(listQuestionContentDTO, qaService, qaAuthoringForm, request,
		    strToolContentID);
	    
	    Set<QaCondition> conditionSet = (Set<QaCondition>) sessionMap.get(QaAppConstants.ATTR_CONDITION_SET);

	    qaContent = authoringUtil.saveOrUpdateQaContent(listQuestionContentDTO, qaService,
		    qaAuthoringForm, request, qaContentTest, strToolContentID, conditionSet);
	    

	    long defaultContentID = 0;
	    QaMonitoringAction.logger.debug("attempt retrieving tool with signatute : " + QaAppConstants.MY_SIGNATURE);
	    defaultContentID = qaService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
	    QaMonitoringAction.logger.debug("retrieved tool default contentId: " + defaultContentID);

	    if (qaContent != null) {
		qaGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	    }

	    authoringUtil.reOrganizeDisplayOrder(qaService, qaAuthoringForm, qaContent);

	    QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);
	    QaUtils.setDefineLater(request, false, strToolContentID, qaService);
	    QaMonitoringAction.logger.debug("define later set to false");

	    QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		    defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	    qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("errors is not empty: " + errors);

	    if (qaContent != null) {
		long defaultContentID = 0;
		QaMonitoringAction.logger.debug("attempt retrieving tool with signatute : "
			+ QaAppConstants.MY_SIGNATURE);
		defaultContentID = qaService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
		QaMonitoringAction.logger.debug("retrieved tool default contentId: " + defaultContentID);

		if (qaContent != null) {
		    qaGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		}

		QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
			defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	    }
	    qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	}

	qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(1).toString());

	qaAuthoringForm.resetUserAction();
	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
	qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	qaGeneralAuthoringDTO.setActiveModule(activeModule);
	qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setActiveModule(activeModule);
	qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setCurrentTab("3");

	/* start monitoring code */

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
	

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = qaGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    

	    if (userExceptionNoToolSessions.equals("true")) {
		QaMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(QaAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward saveSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	QaMonitoringAction.logger.debug("dispathcing proxy saveSingleQuestion");

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
	QaMonitoringAction.logger.debug("dispathcing saveSingleQuestion");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	QaMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	QaMonitoringAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    QaMonitoringAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    qaContent = qaService.loadQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();

	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	String newQuestion = request.getParameter("newQuestion");
	String feedback = request.getParameter("feedback");
	String required = request.getParameter("required");
	boolean requiredBoolean = false;
	if (required != null && required.equalsIgnoreCase("1")) {
	    requiredBoolean = true;
	}

	String editableQuestionIndex = request.getParameter("editableQuestionIndex");
	QaMonitoringAction.logger.debug("editableQuestionIndex: " + editableQuestionIndex);

	if (newQuestion != null && newQuestion.length() > 0) {
	    if (editQuestionBoxRequest != null && editQuestionBoxRequest.equals("false")) {
		QaMonitoringAction.logger.debug("request for add and save");
		boolean duplicates = AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);
		QaMonitoringAction.logger.debug("duplicates: " + duplicates);

		if (!duplicates) {
		    QaQuestionContentDTO qaQuestionContentDTO = null;
		    Iterator listIterator = listQuestionContentDTO.iterator();
		    while (listIterator.hasNext()) {
			qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();

			String question = qaQuestionContentDTO.getQuestion();
			String displayOrder = qaQuestionContentDTO.getDisplayOrder();

			if (displayOrder != null && !displayOrder.equals("")) {
			    if (displayOrder.equals(editableQuestionIndex)) {
				break;
			    }

			}
		    }
		    

		    qaQuestionContentDTO.setQuestion(newQuestion);
		    qaQuestionContentDTO.setFeedback(feedback);
		    qaQuestionContentDTO.setDisplayOrder(editableQuestionIndex);
		    qaQuestionContentDTO.setRequired(requiredBoolean);

		    listQuestionContentDTO = AuthoringUtil.reorderUpdateListQuestionContentDTO(listQuestionContentDTO,
			    qaQuestionContentDTO, editableQuestionIndex);
		} else {
		    QaMonitoringAction.logger.debug("duplicate question entry, not adding");
		}
	    } else {
		QaMonitoringAction.logger.debug("request for edit and save.");
		QaQuestionContentDTO qaQuestionContentDTO = null;
		Iterator listIterator = listQuestionContentDTO.iterator();
		while (listIterator.hasNext()) {
		    qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();

		    String question = qaQuestionContentDTO.getQuestion();
		    String displayOrder = qaQuestionContentDTO.getDisplayOrder();
		    

		    if (displayOrder != null && !displayOrder.equals("")) {
			if (displayOrder.equals(editableQuestionIndex)) {
			    break;
			}

		    }
		}
		

		qaQuestionContentDTO.setQuestion(newQuestion);
		qaQuestionContentDTO.setFeedback(feedback);
		qaQuestionContentDTO.setDisplayOrder(editableQuestionIndex);
		qaQuestionContentDTO.setRequired(requiredBoolean);

		listQuestionContentDTO = AuthoringUtil.reorderUpdateListQuestionContentDTO(listQuestionContentDTO,
			qaQuestionContentDTO, editableQuestionIndex);
	    }
	} else {
	    QaMonitoringAction.logger.debug("entry blank, not adding");
	}

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
	

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	
	
	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);
	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
	qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	qaGeneralAuthoringDTO.setActiveModule(activeModule);
	qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setActiveModule(activeModule);
	qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setCurrentTab("3");

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	QaMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);
	

	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
	

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = qaGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    

	    if (userExceptionNoToolSessions.equals("true")) {
		QaMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(QaAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward addSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispathcing proxy addSingleQuestion");

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
	QaMonitoringAction.logger.debug("dispathcing  addSingleQuestion");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	QaMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	QaMonitoringAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    QaMonitoringAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    qaContent = qaService.loadQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	AuthoringUtil authoringUtil = new AuthoringUtil();

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	String newQuestion = request.getParameter("newQuestion");
	String feedback = request.getParameter("feedback");
	String required = request.getParameter("required");
	boolean requiredBoolean = false;
	if (required != null && required.equalsIgnoreCase("1")) {
	    requiredBoolean = true;
	}

	int listSize = listQuestionContentDTO.size();
	

	if (newQuestion != null && newQuestion.length() > 0) {
	    boolean duplicates = AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);
	    QaMonitoringAction.logger.debug("duplicates: " + duplicates);

	    if (!duplicates) {
		QaQuestionContentDTO qaQuestionContentDTO = new QaQuestionContentDTO(newQuestion, 
			new Long(listSize + 1).toString(), feedback, requiredBoolean);
		listQuestionContentDTO.add(qaQuestionContentDTO);
		
	    } else {
		QaMonitoringAction.logger.debug("entry duplicate, not adding");
	    }
	} else {
	    QaMonitoringAction.logger.debug("entry blank, not adding");
	}

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	
	
	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	request.getSession().setAttribute(httpSessionID, sessionMap);

	QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
	qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	qaGeneralAuthoringDTO.setActiveModule(activeModule);
	qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);

	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setActiveModule(activeModule);
	qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setCurrentTab("3");

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	QaMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);
	

	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
	

	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = qaGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    QaMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		QaMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(QaAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispathcing proxy newQuestionBox");

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
	QaMonitoringAction.logger.debug("dispathcing  newQuestionBox");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	QaMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	QaMonitoringAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    QaMonitoringAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    qaContent = qaService.loadQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	
	
	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
	

	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = qaGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    QaMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		QaMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(QaAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	QaMonitoringAction.logger.debug("fwd ing to newQuestionBox: ");
	return mapping.findForward("newQuestionBox");
    }

    public ActionForward newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispathcing proxy newEditableQuestionBox");

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
	QaMonitoringAction.logger.debug("dispathcing  newEditableQuestionBox");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	QaMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	

	String questionIndex = request.getParameter("questionIndex");
	QaMonitoringAction.logger.debug("questionIndex: " + questionIndex);

	qaAuthoringForm.setEditableQuestionIndex(questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	String editableQuestion = "";
	String editableFeedback = "";
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionContentDTO qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();
	    
	    
	    String question = qaQuestionContentDTO.getQuestion();
	    String displayOrder = qaQuestionContentDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    editableFeedback = qaQuestionContentDTO.getFeedback();
		    editableQuestion = qaQuestionContentDTO.getQuestion();
		    
		    break;
		}

	    }
	}
	
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	QaMonitoringAction.logger.debug("activeModule: " + activeModule);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    QaMonitoringAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    qaContent = qaService.loadQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	
	
	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	qaGeneralAuthoringDTO.setEditableQuestionText(editableQuestion);
	qaGeneralAuthoringDTO.setEditableQuestionFeedback(editableFeedback);
	qaAuthoringForm.setFeedback(editableFeedback);

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
	

	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = qaGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    QaMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		QaMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(QaAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }

	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	QaMonitoringAction.logger.debug("fwd ing to editQuestionBox: ");
	return mapping.findForward("editQuestionBox");
    }

    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispathcing proxy removeQuestion");

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
	QaMonitoringAction.logger.debug("dispathcing  removeQuestion");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	QaMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	

	String questionIndex = request.getParameter("questionIndex");
	QaMonitoringAction.logger.debug("questionIndex: " + questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	QaQuestionContentDTO qaQuestionContentDTO = null;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();
	    
	    

	    String question = qaQuestionContentDTO.getQuestion();
	    String displayOrder = qaQuestionContentDTO.getDisplayOrder();
	    QaMonitoringAction.logger.debug("displayOrder:" + displayOrder);

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	
	qaQuestionContentDTO.setQuestion("");
	

	listQuestionContentDTO = AuthoringUtil.reorderListQuestionContentDTO(listQuestionContentDTO, questionIndex);
	

	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	QaMonitoringAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	

	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);
	

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    QaMonitoringAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    qaContent = qaService.loadQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
	qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	qaGeneralAuthoringDTO.setActiveModule(activeModule);
	qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setActiveModule(activeModule);
	qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setCurrentTab("3");

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	
	

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
	

	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = qaGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    QaMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		QaMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(QaAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }

	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward moveQuestionDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispathcing proxy moveQuestionDown");

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
	QaMonitoringAction.logger.debug("dispathcing  moveQuestionDown");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	QaMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	

	String questionIndex = request.getParameter("questionIndex");
	QaMonitoringAction.logger.debug("questionIndex: " + questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	listQuestionContentDTO = AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "down", null);
	

	listQuestionContentDTO = AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	

	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	QaMonitoringAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	

	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);
	

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    QaMonitoringAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    qaContent = qaService.loadQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	request.getSession().setAttribute(httpSessionID, sessionMap);

	QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
	qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	qaGeneralAuthoringDTO.setActiveModule(activeModule);
	qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setActiveModule(activeModule);
	qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setCurrentTab("3");

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
	

	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = qaGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    QaMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		QaMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(QaAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }

	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);

    }

    public ActionForward moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction.logger.debug("dispathcing proxy moveQuestionUp");

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
	QaMonitoringAction.logger.debug("dispathcing  moveQuestionDown");

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	QaMonitoringAction.logger.debug("httpSessionID: " + httpSessionID);

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	

	String questionIndex = request.getParameter("questionIndex");
	QaMonitoringAction.logger.debug("questionIndex: " + questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	listQuestionContentDTO = AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "up", null);
	

	listQuestionContentDTO = AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	

	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	QaMonitoringAction.logger.debug("contentFolderID: " + contentFolderID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	QaMonitoringAction.logger.debug("activeModule: " + activeModule);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	

	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);
	

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaMonitoringAction.logger.debug("strToolContentID: " + strToolContentID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaMonitoringAction.logger.debug("defaultContentIdStr: " + defaultContentIdStr);

	QaContent qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    QaMonitoringAction.logger.debug("using defaultContentIdStr: " + defaultContentIdStr);
	    qaContent = qaService.loadQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	AuthoringUtil authoringUtil = new AuthoringUtil();

	qaGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());

	request.getSession().setAttribute(httpSessionID, sessionMap);

	QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
	qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	qaGeneralAuthoringDTO.setActiveModule(activeModule);
	qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setActiveModule(activeModule);
	qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setCurrentTab("3");

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();

	if (qaService.studentActivityOccurredGlobal(qaContent)) {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    QaMonitoringAction.logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
	    qaGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	qaGeneralMonitoringDTO.setOnlineInstructions(qaContent.getOnlineInstructions());
	qaGeneralMonitoringDTO.setOfflineInstructions(qaContent.getOfflineInstructions());

	List attachmentList = qaService.retrieveQaUploadedFiles(qaContent);
	
	qaGeneralMonitoringDTO.setAttachmentList(attachmentList);
	

	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	prepareReflectionData(request, qaContent, qaService, null, false, "All");

	/* find out if there are any reflection entries, from here */
	boolean notebookEntriesExist = MonitoringUtil.notebookEntriesExist(qaService, qaContent);
	QaMonitoringAction.logger.debug("notebookEntriesExist : " + notebookEntriesExist);

	if (notebookEntriesExist) {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = qaGeneralMonitoringDTO.getUserExceptionNoToolSessions();
	    QaMonitoringAction.logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);

	    if (userExceptionNoToolSessions.equals("true")) {
		QaMonitoringAction.logger.debug("there are no online student activity but there are reflections : ");
		request.setAttribute(QaAppConstants.NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(QaAppConstants.NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.buildQaStatsDTO(request, qaService, qaContent);
	MonitoringUtil.generateGroupsSessionData(request, qaService, qaContent, false);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public void prepareReflectionData(HttpServletRequest request, QaContent qaContent, IQaService qaService,
	    String userID, boolean exportMode, String currentSessionId) {
	
	QaMonitoringAction.logger.debug("currentSessionId: " + currentSessionId);
	QaMonitoringAction.logger.debug("userID: " + userID);
	QaMonitoringAction.logger.debug("exportMode: " + exportMode);

	List reflectionsContainerDTO = new LinkedList();
	/*
	 * if (currentSessionId.equals("All")) { reflectionsContainerDTO=getReflectionList(qaContent, userID,
	 * qaService); } else { reflectionsContainerDTO=getReflectionListForSession(qaContent, userID, qaService,
	 * currentSessionId); }
	 */

	reflectionsContainerDTO = getReflectionList(qaContent, userID, qaService);

	
	request.setAttribute(QaAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

	if (exportMode) {
	    request.getSession().setAttribute(QaAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	}
    }

    /**
     * returns reflection data for all sessions
     * 
     * getReflectionList
     * 
     * @param qaContent
     * @param userID
     * @param qaService
     * @return
     */
    public List getReflectionList(QaContent qaContent, String userID, IQaService qaService) {
	QaMonitoringAction.logger.debug("getting reflections for all sessions");
	List reflectionsContainerDTO = new LinkedList();
	if (userID == null) {
	    QaMonitoringAction.logger.debug("all users mode");
	    for (Iterator sessionIter = qaContent.getQaSessions().iterator(); sessionIter.hasNext();) {
		QaSession qaSession = (QaSession) sessionIter.next();
		
		QaMonitoringAction.logger.debug("qaSession sessionId: " + qaSession.getQaSessionId());

		for (Iterator userIter = qaSession.getQaQueUsers().iterator(); userIter.hasNext();) {
		    QaQueUsr user = (QaQueUsr) userIter.next();
		    QaMonitoringAction.logger.debug("user: " + user.getUsername());

		    NotebookEntry notebookEntry = qaService.getEntry(qaSession.getQaSessionId(),
			    CoreNotebookConstants.NOTEBOOK_TOOL, QaAppConstants.MY_SIGNATURE, new Integer(user
				    .getQueUsrId().toString()));

		    

		    if (notebookEntry != null) {
			ReflectionDTO reflectionDTO = new ReflectionDTO();
			reflectionDTO.setUserId(user.getQueUsrId().toString());
			reflectionDTO.setSessionId(qaSession.getQaSessionId().toString());
			reflectionDTO.setUserName(user.getFullname());
			reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			String notebookEntryPresentable = QaUtils.replaceNewLines(notebookEntry.getEntry());
			reflectionDTO.setEntry(notebookEntryPresentable);
			reflectionsContainerDTO.add(reflectionDTO);
		    }
		}
	    }
	} else {
	    QaMonitoringAction.logger.debug("single user mode");
	    for (Iterator sessionIter = qaContent.getQaSessions().iterator(); sessionIter.hasNext();) {
		QaSession qaSession = (QaSession) sessionIter.next();
		
		for (Iterator userIter = qaSession.getQaQueUsers().iterator(); userIter.hasNext();) {
		    QaQueUsr user = (QaQueUsr) userIter.next();
		    QaMonitoringAction.logger.debug("user: " + user.getUsername());

		    if (user.getQueUsrId().toString().equals(userID)) {
			QaMonitoringAction.logger.debug("getting reflection for user with  userID: " + userID);
			NotebookEntry notebookEntry = qaService.getEntry(qaSession.getQaSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, QaAppConstants.MY_SIGNATURE, new Integer(user
					.getQueUsrId().toString()));

			

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(qaSession.getQaSessionId().toString());
			    reflectionDTO.setUserName(user.getFullname());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    String notebookEntryPresentable = QaUtils.replaceNewLines(notebookEntry.getEntry());
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
     * getReflectionListForSession(QaContent qaContent, String userID,
     * IQaService qaService, String currentSessionId)
     * 
     * @param qaContent
     * @param userID
     * @param qaService
     * @param currentSessionId
     * @return
     */
    public List getReflectionListForSession(QaContent qaContent, String userID, IQaService qaService,
	    String currentSessionId) {
	QaMonitoringAction.logger.debug("getting reflections for a specific session");
	QaMonitoringAction.logger.debug("currentSessionId: " + currentSessionId);

	List reflectionsContainerDTO = new LinkedList();
	if (userID == null) {
	    QaMonitoringAction.logger.debug("all users mode");
	    for (Iterator sessionIter = qaContent.getQaSessions().iterator(); sessionIter.hasNext();) {
		QaSession qaSession = (QaSession) sessionIter.next();
		
		QaMonitoringAction.logger.debug("qaSession sessionId: " + qaSession.getQaSessionId());

		if (currentSessionId.equals(qaSession.getQaSessionId())) {

		    for (Iterator userIter = qaSession.getQaQueUsers().iterator(); userIter.hasNext();) {
			QaQueUsr user = (QaQueUsr) userIter.next();
			QaMonitoringAction.logger.debug("user: " + user.getUsername());

			NotebookEntry notebookEntry = qaService.getEntry(qaSession.getQaSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, QaAppConstants.MY_SIGNATURE, new Integer(user
					.getQueUsrId().toString()));

			

			if (notebookEntry != null) {
			    ReflectionDTO reflectionDTO = new ReflectionDTO();
			    reflectionDTO.setUserId(user.getQueUsrId().toString());
			    reflectionDTO.setSessionId(qaSession.getQaSessionId().toString());
			    reflectionDTO.setUserName(user.getFullname());
			    reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
			    String notebookEntryPresentable = QaUtils.replaceNewLines(notebookEntry.getEntry());
			    reflectionDTO.setEntry(notebookEntryPresentable);
			    reflectionsContainerDTO.add(reflectionDTO);
			}
		    }
		}
	    }
	} else {
	    QaMonitoringAction.logger.debug("single user mode");
	    for (Iterator sessionIter = qaContent.getQaSessions().iterator(); sessionIter.hasNext();) {
		QaSession qaSession = (QaSession) sessionIter.next();
		

		if (currentSessionId.equals(qaSession.getQaSessionId())) {
		    for (Iterator userIter = qaSession.getQaQueUsers().iterator(); userIter.hasNext();) {
			QaQueUsr user = (QaQueUsr) userIter.next();
			QaMonitoringAction.logger.debug("user: " + user.getUsername());

			if (user.getQueUsrId().toString().equals(userID)) {
			    QaMonitoringAction.logger.debug("getting reflection for user with  userID: " + userID);
			    NotebookEntry notebookEntry = qaService.getEntry(qaSession.getQaSessionId(),
				    CoreNotebookConstants.NOTEBOOK_TOOL, QaAppConstants.MY_SIGNATURE, new Integer(user
					    .getQueUsrId().toString()));

			    

			    if (notebookEntry != null) {
				ReflectionDTO reflectionDTO = new ReflectionDTO();
				reflectionDTO.setUserId(user.getQueUsrId().toString());
				reflectionDTO.setSessionId(qaSession.getQaSessionId().toString());
				reflectionDTO.setUserName(user.getFullname());
				reflectionDTO.setReflectionUid(notebookEntry.getUid().toString());
				String notebookEntryPresentable = QaUtils.replaceNewLines(notebookEntry.getEntry());
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

}