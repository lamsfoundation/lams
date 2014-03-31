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

/* $$Id$$ */

package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

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
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.dto.EditActivityDTO;
import org.lamsfoundation.lams.tool.qa.dto.GeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.tool.qa.web.form.QaAuthoringForm;
import org.lamsfoundation.lams.tool.qa.web.form.QaMonitoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * @author Ozgur Demirtas
 */
public class QaMonitoringAction extends LamsDispatchAction implements QaAppConstants {
    private static Logger logger = Logger.getLogger(QaMonitoringAction.class.getName());

    public static String SELECTBOX_SELECTED_TOOL_SESSION = "selectBoxSelectedToolSession";
    public static Integer READABLE_TOOL_SESSION_COUNT = new Integer(1);

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	return null;
    }

    /**
     * activates editActivity screen 
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
	
	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	if (qaService == null) {
	    qaService = qaMonitoringForm.getQaService();
	}

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	qaMonitoringForm.setContentFolderID(contentFolderID);

	request.setAttribute(QaAppConstants.SOURCE_MC_STARTER, "monitoring");
	

	/*
	 * it is possible that the content is being used by some learners. In this situation, the content is marked as
	 * "in use" and content in use is not modifiable
	 */
	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());

	GeneralMonitoringDTO generalMonitoringDTO = new GeneralMonitoringDTO();
	if (qaService.isStudentActivityOccurredGlobal(qaContent)) {
	    generalMonitoringDTO.setUserExceptionContentInUse(new Boolean(true).toString());
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
	boolean isContentInUse = qaContent.isContentLocked();
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	qaGeneralAuthoringDTO.setActivityTitle(qaContent.getTitle());
	qaGeneralAuthoringDTO.setActivityInstructions(qaContent.getInstructions());
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	/* note that we are casting monitoring form subclass into Authoring form */
	QaStarterAction qaStarterAction = new QaStarterAction();	
	return qaStarterAction.executeDefineLater(mapping, qaMonitoringForm, request, response, qaService);
    }

    public ActionForward editActivityQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
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
	qaMonitoringForm.setToolContentID(strToolContentID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	qaMonitoringForm.setContentFolderID(contentFolderID);

	String httpSessionID = request.getParameter("httpSessionID");
	
	qaMonitoringForm.setHttpSessionID(httpSessionID);

	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());
	

	qaMonitoringForm.setTitle(qaContent.getTitle());

	QaUtils.setDefineLater(request, true, strToolContentID, qaService);

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, generalMonitoringDTO);

	List listQuestionContentDTO = new LinkedList();

	Iterator queIterator = qaContent.getQaQueContents().iterator();
	while (queIterator.hasNext()) {
	    QaQueContent qaQuestion = (QaQueContent) queIterator.next();
	    if (qaQuestion != null) {
		QaQuestionDTO qaQuestionDTO = new QaQuestionDTO(qaQuestion);
		listQuestionContentDTO.add(qaQuestionDTO);
	    }
	}
	
	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	qaGeneralAuthoringDTO.setActivityTitle(qaContent.getTitle());
	qaGeneralAuthoringDTO.setActivityInstructions(qaContent.getInstructions());	
	qaGeneralAuthoringDTO.setActiveModule(QaAppConstants.MONITORING);
	qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);
	qaGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward updateResponse(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	Long responseUid = WebUtil.readLongParam(request, QaAppConstants.RESPONSE_UID);
	String updatedResponse = request.getParameter("updatedResponse");
	QaUsrResp qaUsrResp = qaService.getResponseById(responseUid);

	/*
	 * write out the audit log entry. If you move this after the update of the response, then make sure you update
	 * the audit call to use a copy of the original answer
	 */
	qaService.getAuditService().logChange(QaAppConstants.MY_SIGNATURE, qaUsrResp.getQaQueUser().getQueUsrId(),
		qaUsrResp.getQaQueUser().getUsername(), qaUsrResp.getAnswer(), updatedResponse);

	qaUsrResp.setAnswer(updatedResponse);
	qaService.updateUserResponse(qaUsrResp);

	return null;
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

    public ActionForward updateResponseVisibility(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	
	Long responseUid = WebUtil.readLongParam(request, QaAppConstants.RESPONSE_UID);
	boolean isHideItem = WebUtil.readBooleanParam(request, QaAppConstants.IS_HIDE_ITEM);
	qaService.updateResponseVisibility(responseUid, isHideItem);
	
	return null;
    }

    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
	

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);

	ActionMessages errors = new ActionMessages();

	if (listQuestionContentDTO.size() == 0) {
	    ActionMessage error = new ActionMessage("questions.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

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
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	QaContent qaContentTest = qaService.getQa(new Long(strToolContentID).longValue());

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
	    defaultContentID = qaService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);

	    if (qaContent != null) {
		qaGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	    }

	    authoringUtil.reOrganizeDisplayOrder(qaService, qaAuthoringForm, qaContent);

	    QaUtils.setDefineLater(request, false, strToolContentID, qaService);

	    QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		    defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	    qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	} else {
	    //errors is not empty

	    if (qaContent != null) {
		long defaultContentID = 0;
		defaultContentID = qaService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
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
	qaAuthoringForm.setCurrentTab("2");
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward saveSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");
	

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());
	if (qaContent == null) {
	    qaContent = qaService.getQa(new Long(defaultContentIdStr).longValue());
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

	if (newQuestion != null && newQuestion.length() > 0) {
	    if (editQuestionBoxRequest != null && editQuestionBoxRequest.equals("false")) {
		boolean duplicates = AuthoringUtil.checkDuplicateQuestions(listQuestionContentDTO, newQuestion);

		if (!duplicates) {
		    QaQuestionDTO qaQuestionDTO = null;
		    Iterator listIterator = listQuestionContentDTO.iterator();
		    while (listIterator.hasNext()) {
			qaQuestionDTO = (QaQuestionDTO) listIterator.next();

			String question = qaQuestionDTO.getQuestion();
			String displayOrder = qaQuestionDTO.getDisplayOrder();

			if (displayOrder != null && !displayOrder.equals("")) {
			    if (displayOrder.equals(editableQuestionIndex)) {
				break;
			    }

			}
		    }
		    

		    qaQuestionDTO.setQuestion(newQuestion);
		    qaQuestionDTO.setFeedback(feedback);
		    qaQuestionDTO.setDisplayOrder(editableQuestionIndex);
		    qaQuestionDTO.setRequired(requiredBoolean);

		    listQuestionContentDTO = AuthoringUtil.reorderUpdateListQuestionContentDTO(listQuestionContentDTO,
			    qaQuestionDTO, editableQuestionIndex);
		} else {
		    //duplicate question entry, not adding
		}
	    } else {
		//request for edit and save
		QaQuestionDTO qaQuestionDTO = null;
		Iterator listIterator = listQuestionContentDTO.iterator();
		while (listIterator.hasNext()) {
		    qaQuestionDTO = (QaQuestionDTO) listIterator.next();

		    String question = qaQuestionDTO.getQuestion();
		    String displayOrder = qaQuestionDTO.getDisplayOrder();
		    

		    if (displayOrder != null && !displayOrder.equals("")) {
			if (displayOrder.equals(editableQuestionIndex)) {
			    break;
			}

		    }
		}
		

		qaQuestionDTO.setQuestion(newQuestion);
		qaQuestionDTO.setFeedback(feedback);
		qaQuestionDTO.setDisplayOrder(editableQuestionIndex);
		qaQuestionDTO.setRequired(requiredBoolean);

		listQuestionContentDTO = AuthoringUtil.reorderUpdateListQuestionContentDTO(listQuestionContentDTO,
			qaQuestionDTO, editableQuestionIndex);
	    }
	} else {
	    //entry blank, not adding
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
	qaAuthoringForm.setCurrentTab("2");

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	

	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward addSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);

	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    qaContent = qaService.getQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

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

	    if (!duplicates) {
		QaQuestionDTO qaQuestionDTO = new QaQuestionDTO(newQuestion, 
			new Long(listSize + 1).toString(), feedback, requiredBoolean);
		listQuestionContentDTO.add(qaQuestionDTO);
		
	    } else {
		//entry duplicate, not adding
	    }
	} else {
	   //entry blank, not adding
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
	qaAuthoringForm.setCurrentTab("2");

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	

	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());

	if (qaContent == null) {
	    qaContent = qaService.getQa(new Long(defaultContentIdStr).longValue());
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
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return mapping.findForward("newQuestionBox");
    }

    public ActionForward newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	

	String questionIndex = request.getParameter("questionIndex");
	qaAuthoringForm.setEditableQuestionIndex(questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	String editableQuestion = "";
	String editableFeedback = "";
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = (QaQuestionDTO) listIterator.next();
	    
	    
	    String displayOrder = qaQuestionDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    editableFeedback = qaQuestionDTO.getFeedback();
		    editableQuestion = qaQuestionDTO.getQuestion();
		    
		    break;
		}

	    }
	}
	
	

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);
	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());
	if (qaContent == null) {
	    qaContent = qaService.getQa(new Long(defaultContentIdStr).longValue());
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
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return mapping.findForward("editQuestionBox");
    }

    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;
	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	

	String questionIndex = request.getParameter("questionIndex");
	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	QaQuestionDTO qaQuestionDTO = null;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    qaQuestionDTO = (QaQuestionDTO) listIterator.next();

	    String displayOrder = qaQuestionDTO.getDisplayOrder();
	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}
	
	qaQuestionDTO.setQuestion("");

	listQuestionContentDTO = AuthoringUtil.reorderListQuestionContentDTO(listQuestionContentDTO, questionIndex);

	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	

	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);
	

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);

	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    qaContent = qaService.getQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

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
	qaAuthoringForm.setCurrentTab("2");

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	
	

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
    }

    public ActionForward moveQuestionDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	

	String questionIndex = request.getParameter("questionIndex");

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	listQuestionContentDTO = AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "down", null);
	

	listQuestionContentDTO = AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	

	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	

	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);
	

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);

	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    qaContent = qaService.getQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

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
	qaAuthoringForm.setCurrentTab("2");

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);

    }

    public ActionForward moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	/* start authoring code */
	QaAuthoringForm qaAuthoringForm = (QaMonitoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	

	String httpSessionID = request.getParameter("httpSessionID");

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(httpSessionID);
	

	String questionIndex = request.getParameter("questionIndex");

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);
	

	listQuestionContentDTO = AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "up", null);
	

	listQuestionContentDTO = AuthoringUtil.reorderSimpleListQuestionContentDTO(listQuestionContentDTO);
	

	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	

	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);
	

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);

	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());
	

	if (qaContent == null) {
	    qaContent = qaService.getQa(new Long(defaultContentIdStr).longValue());
	}
	

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

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
	qaAuthoringForm.setCurrentTab("2");

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	

	
	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	/* start monitoring code */
	GeneralMonitoringDTO qaGeneralMonitoringDTO = new GeneralMonitoringDTO();
	qaGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	
	request.setAttribute(QaAppConstants.QA_GENERAL_MONITORING_DTO, qaGeneralMonitoringDTO);

	MonitoringUtil.setUpMonitoring(request, qaService, qaContent);

	return mapping.findForward(QaAppConstants.LOAD_MONITORING);
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
	IQaService qaService = getQAService();
	
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaContent content = qaService.getQa(contentID);
	
	Long dateParameter = WebUtil.readLongParam(request, QaAppConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	}
	content.setSubmissionDeadline(tzSubmissionDeadline);
	qaService.saveOrUpdateQa(content);

	return null;
    }

    private IQaService getQAService() {
    	return QaServiceProxy.getQaService(getServlet().getServletContext());
    }
    
}