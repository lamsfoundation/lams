/****************************************************************
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
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

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
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaConfigItem;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dto.EditActivityDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaToolContentHandler;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.tool.qa.web.form.QaAuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Ozgur Demirtas
 */
public class QaAction extends LamsDispatchAction implements QaAppConstants {
    static Logger logger = Logger.getLogger(QaAction.class.getName());

    private QaToolContentHandler toolContentHandler;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * submits content into the tool database
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


	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	SortedSet<QaCondition> conditionSet = (SortedSet<QaCondition>) sessionMap
		.get(QaAppConstants.ATTR_CONDITION_SET);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);

	List<QaQuestionDTO> listQuestionContentDTO = (List<QaQuestionDTO>) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);

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
	    QaAction.logger.debug("errors saved: " + errors);
	}

	QaContent qaContent = qaContentTest;

	if (errors.isEmpty()) {
	    /*
	     * to remove deleted entries in the questions table based on mapQuestionContent
	     */
	    authoringUtil.removeRedundantQuestions(listQuestionContentDTO, qaService, qaAuthoringForm, request,
		    strToolContentID);

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

	    if (activeModule.equals(QaAppConstants.AUTHORING)) {
		request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
		qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	    } else {
		qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
	    }

	} else {
	    if (qaContent != null) {
		long defaultContentID = 0;
		defaultContentID = qaService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);

		if (qaContent != null) {
		    qaGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		}

		QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
			defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	    }
	}

	List delConditionList = getDeletedQaConditionList(sessionMap);
	Iterator iter = delConditionList.iterator();
	while (iter.hasNext()) {
	    QaCondition condition = (QaCondition) iter.next();
	    iter.remove();
	    qaService.deleteCondition(condition);
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
	qaAuthoringForm.setCurrentTab("1");

	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * saveSingleQuestion
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

	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();

	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	qaGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);

	String newQuestion = request.getParameter("newQuestion");

	String feedback = request.getParameter("feedback");

	String editableQuestionIndex = request.getParameter("editableQuestionIndex");

	String required = request.getParameter("required");
	boolean requiredBoolean = false;
	if (required != null && required.equalsIgnoreCase("1")) {
	    requiredBoolean = true;
	}
	
	if (newQuestion != null && newQuestion.length() > 0) {
	    if (editQuestionBoxRequest != null && editQuestionBoxRequest.equals("false")) {
		//request for add and save
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
	qaAuthoringForm.setCurrentTab("1");

	qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	request.getSession().setAttribute(httpSessionID, sessionMap);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * addSingleQuestion
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

	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);

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

	    if (!duplicates) {
		QaQuestionDTO qaQuestionDTO = new QaQuestionDTO(newQuestion, new Long(listSize + 1).toString(), 
			feedback, requiredBoolean);
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
	qaAuthoringForm.setCurrentTab("1");

	qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	request.getSession().setAttribute(httpSessionID, sessionMap);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * opens up an new screen within the current page for adding a new question
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
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	qaGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	
	qaAuthoringForm.setTitle(richTextTitle);

	qaGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

	QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	Collection listQuestionContentDTO = (Collection) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	// Adding in the qa wizard data if it is turned on
	if (qaService.getConfigItem(QaConfigItem.KEY_ENABLE_QAWIZARD) != null
		&& qaService.getConfigItem(QaConfigItem.KEY_ENABLE_QAWIZARD).getConfigValue().equals("true")) {
	    request.setAttribute(QaAppConstants.ATTR_WIZARD_ENABLED, true);
	    request.setAttribute(QaAppConstants.ATTR_WIZARD_CATEGORIES, qaService.getWizardCategories());
	}

	return mapping.findForward("newQuestionBox");
    }

    /**
     * opens up an new screen within the current page for editing a question
     * newEditableQuestionBox
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	qaAuthoringForm.setEditableQuestionIndex(questionIndex);

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);

	String editableQuestion = "";
	String editableFeedback = "";
	boolean requiredBoolean = false;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = (QaQuestionDTO) listIterator.next();
	    String question = qaQuestionDTO.getQuestion();
	    String displayOrder = qaQuestionDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    editableFeedback = qaQuestionDTO.getFeedback();
		    editableQuestion = qaQuestionDTO.getQuestion();
		    requiredBoolean = qaQuestionDTO.isRequired();
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
	qaAuthoringForm.setRequired(requiredBoolean);
	qaAuthoringForm.setFeedback(editableFeedback);

	qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));

	return mapping.findForward("editQuestionBox");
    }

    /**
     * removes a question from the questions map ActionForward
     * removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest
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
    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);

	QaQuestionDTO qaQuestionDTO = null;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    qaQuestionDTO = (QaQuestionDTO) listIterator.next();
	    
	    String question = qaQuestionDTO.getQuestion();
	    String displayOrder = qaQuestionDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    break;
		}

	    }
	}

	qaQuestionDTO.setQuestion("");

	SortedSet<QaCondition> list = (SortedSet<QaCondition>) sessionMap.get(QaAppConstants.ATTR_CONDITION_SET);
	Iterator<QaCondition> conditionIter = list.iterator();

	while (conditionIter.hasNext()) {
	    QaCondition condition = conditionIter.next();
	    Iterator<QaQuestionDTO> dtoIter = condition.temporaryQuestionDTOSet.iterator();
	    while (dtoIter.hasNext()) {
		if (dtoIter.next() == qaQuestionDTO) {
		    dtoIter.remove();
		}
	    }
	    if (condition.temporaryQuestionDTOSet.isEmpty()) {
		conditionIter.remove();
	    }
	}


	listQuestionContentDTO = AuthoringUtil.reorderListQuestionContentDTO(listQuestionContentDTO, questionIndex);

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
	qaAuthoringForm.setCurrentTab("1");

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * moves a question down in the list moveQuestionDown
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
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);

	SortedSet<QaCondition> conditionSet = (SortedSet<QaCondition>) sessionMap
		.get(QaAppConstants.ATTR_CONDITION_SET);

	listQuestionContentDTO = AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "down", conditionSet);

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
	qaAuthoringForm.setCurrentTab("1");

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * moves a question up in the list moveQuestionUp
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
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List listQuestionContentDTO = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY);

	SortedSet<QaCondition> conditionSet = (SortedSet<QaCondition>) sessionMap
		.get(QaAppConstants.ATTR_CONDITION_SET);
	listQuestionContentDTO = AuthoringUtil.swapNodes(listQuestionContentDTO, questionIndex, "up", conditionSet);

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
	qaAuthoringForm.setCurrentTab("1");

	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);

	qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }


    /**
     * persists error messages to request scope
     * 
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
     * QaToolContentHandler getToolContentHandler()
     * 
     * @return
     */
    private QaToolContentHandler getToolContentHandler() {
	if (toolContentHandler == null) {
	    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    toolContentHandler = (QaToolContentHandler) wac.getBean("qaToolContentHandler");
	}
	return toolContentHandler;
    }

    public ActionForward editActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	return null;
    }

    /**
     * calls monitoring action stats screen generation
     * 
     * ActionForward getStats(ActionMapping mapping, ActionForm form,
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
    public ActionForward getStats(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaMonitoringAction qaMonitoringAction = new QaMonitoringAction();
	// return qaMonitoringAction.getStats(mapping, form, request, response,
	// "All");
	return null;
    }

    /**
     * generates Edit Activity screen 
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

	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String defaultContentIdStr = request.getParameter(QaAppConstants.DEFAULT_CONTENT_ID_STR);

	QaContent qaContent = qaService.getQa(new Long(strToolContentID).longValue());

	QaGeneralAuthoringDTO qaGeneralAuthoringDTO = new QaGeneralAuthoringDTO();
	qaGeneralAuthoringDTO.setContentFolderID(contentFolderID);


	qaGeneralAuthoringDTO.setActivityTitle(qaContent.getTitle());
	qaAuthoringForm.setTitle(qaContent.getTitle());

	qaGeneralAuthoringDTO.setActivityInstructions(qaContent.getInstructions());

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, qaContent.getTitle());
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, qaContent.getInstructions());

	/* determine whether the request is from Monitoring url Edit Activity */
	String sourceMcStarter = (String) request.getAttribute(QaAppConstants.SOURCE_MC_STARTER);

	qaAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
	qaGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());

	boolean isContentInUse = qaContent.isContentLocked();

	qaGeneralAuthoringDTO.setMonitoredContentInUse(new Boolean(false).toString());
	if (isContentInUse == true) {
	    //monitoring url does not allow editActivity since the content is in use
	    persistError(request, "error.content.inUse");
	    qaGeneralAuthoringDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}

	EditActivityDTO editActivityDTO = new EditActivityDTO();
	if (isContentInUse == true) {
	    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
	}
	request.setAttribute(QaAppConstants.EDIT_ACTIVITY_DTO, editActivityDTO);

	QaUtils.setDefineLater(request, true, strToolContentID, qaService);

	QaUtils.setFormProperties(request, qaService, qaAuthoringForm, qaGeneralAuthoringDTO, strToolContentID,
		defaultContentIdStr, activeModule, sessionMap, httpSessionID);

	qaGeneralAuthoringDTO.setToolContentID(strToolContentID);
	qaGeneralAuthoringDTO.setActiveModule(activeModule);
	qaGeneralAuthoringDTO.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setActiveModule(activeModule);
	qaAuthoringForm.setDefaultContentIdStr(defaultContentIdStr);
	qaAuthoringForm.setCurrentTab("1");

	List listQuestionContentDTO = new LinkedList();

	Iterator queIterator = qaContent.getQaQueContents().iterator();
	while (queIterator.hasNext()) {
	    QaQueContent qaQuestion = (QaQueContent) queIterator.next();

	    if (qaQuestion != null) {
		QaQuestionDTO qaQuestionDTO = new QaQuestionDTO(qaQuestion);
		qaQuestionDTO.setDisplayOrder(new Integer(qaQuestion.getDisplayOrder()).toString());
		listQuestionContentDTO.add(qaQuestionDTO);
	    }
	}
	request.setAttribute(QaAppConstants.LIST_QUESTION_CONTENT_DTO, listQuestionContentDTO);
	sessionMap.put(QaAppConstants.LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
	request.getSession().setAttribute(httpSessionID, sessionMap);

	request.setAttribute(QaAppConstants.QA_GENERAL_AUTHORING_DTO, qaGeneralAuthoringDTO);

	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * existsContent
     * 
     * @param toolContentID
     * @param qaService
     * @return
     */
    protected boolean existsContent(long toolContentID, IQaService qaService) {
	QaContent qaContent = qaService.getQa(toolContentID);
	if (qaContent == null) {
	    return false;
	}

	return true;
    }

    /**
     * Get the deleted condition list, which could be persisted or non-persisted
     * items.
     * 
     * @param request
     * @return
     */
    private List getDeletedQaConditionList(SessionMap sessionMap) {
	List list = (List) sessionMap.get(QaAppConstants.ATTR_DELETED_CONDITION_LIST);
	if (list == null) {
	    list = new ArrayList();
	    sessionMap.put(QaAppConstants.ATTR_DELETED_CONDITION_LIST, list);
	}
	return list;
    }
}
