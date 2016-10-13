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


package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.tool.qa.web.form.QaAuthoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Initializes the tool's authoring mode
 *
 * @author Ozgur Demirtas
 */
public class QaStarterAction extends Action implements QaAppConstants {
    private static Logger logger = Logger.getLogger(QaStarterAction.class.getName());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, QaApplicationException {

	QaUtils.cleanUpSessionAbsolute(request);
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	qaAuthoringForm.resetRadioBoxes();

	IQaService qaService = null;
	if (getServlet() == null || getServlet().getServletContext() == null) {
	    qaService = qaAuthoringForm.getQaService();
	} else {
	    qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	}

	validateDefaultContent(request, mapping, qaService, qaAuthoringForm);

	//no problems getting the default content, will render authoring screen
	String strToolContentID = "";
	/* the authoring url must be passed a tool content id */
	strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	qaAuthoringForm.setToolContentID(strToolContentID);

	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, "");
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, "");
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);
	qaAuthoringForm.setHttpSessionID(sessionMap.getSessionID());

	if (strToolContentID == null || strToolContentID.equals("")) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    throw new ServletException("No Tool Content ID found");
	}

	QaContent qaContent = qaService.getQaContent(new Long(strToolContentID).longValue());
	if (qaContent == null) {
	    /* fetch default content */
	    long defaultContentID = qaService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
	    qaContent = qaService.getQaContent(defaultContentID);
	    qaContent = QaContent.newInstance(qaContent, new Long(strToolContentID));

	    if (qaContent.getConditions().isEmpty()) {
		qaContent.getConditions().add(qaService.createDefaultComplexCondition(qaContent));
	    }
	}

	prepareDTOandForm(request, qaAuthoringForm, qaContent, qaService, sessionMap);

	ToolAccessMode mode = getAccessMode(request);
	// request is from monitoring module
	if (mode.isTeacher()) {
	    qaService.setDefineLater(strToolContentID, true);
	}
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	SortedSet<QaCondition> conditionList = getQaConditionList(sessionMap);
	conditionList.clear();
	conditionList.addAll(qaContent.getConditions());

	qaAuthoringForm.setAllowRichEditor(qaContent.isAllowRichEditor());
	qaAuthoringForm.setUseSelectLeaderToolOuput(qaContent.isUseSelectLeaderToolOuput());

	sessionMap.put(QaAppConstants.ATTR_QA_AUTHORING_FORM, qaAuthoringForm);
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	// get rating criterias from DB
	List<RatingCriteria> ratingCriterias = qaService.getRatingCriterias(qaContent.getQaContentId());
	sessionMap.put(AttributeNames.ATTR_RATING_CRITERIAS, ratingCriterias);

	return mapping.findForward(LOAD_QUESTIONS);
    }

    /**
     * retrives the existing content information from the db and prepares the data for presentation purposes.
     *
     * @param request
     * @param mapping
     * @param qaAuthoringForm
     * @param mapQuestionContent
     * @param toolContentID
     * @return ActionForward
     */
    protected QaContent prepareDTOandForm(HttpServletRequest request, QaAuthoringForm qaAuthoringForm,
	    QaContent qaContent, IQaService qaService, SessionMap<String, Object> sessionMap) {

	qaAuthoringForm.setUsernameVisible(qaContent.isUsernameVisible() ? "1" : "0");
	qaAuthoringForm.setAllowRateAnswers(qaContent.isAllowRateAnswers() ? "1" : "0");
	qaAuthoringForm.setNotifyTeachersOnResponseSubmit(qaContent.isNotifyTeachersOnResponseSubmit() ? "1" : "0");
	qaAuthoringForm.setShowOtherAnswers(qaContent.isShowOtherAnswers() ? "1" : "0");
	qaAuthoringForm.setQuestionsSequenced(qaContent.isQuestionsSequenced() ? "1" : "0");
	qaAuthoringForm.setLockWhenFinished(qaContent.isLockWhenFinished() ? "1" : "0");
	qaAuthoringForm.setNoReeditAllowed(qaContent.isNoReeditAllowed() ? "1" : "0");
	qaAuthoringForm.setMaximumRates(qaContent.getMaximumRates());
	qaAuthoringForm.setMinimumRates(qaContent.getMinimumRates());
	qaAuthoringForm.setReflect(qaContent.isReflect() ? "1" : "0");
	qaAuthoringForm.setReflectionSubject(qaContent.getReflectionSubject());
	qaAuthoringForm.setTitle(qaContent.getTitle());
	qaAuthoringForm.setInstructions(qaContent.getInstructions());
	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, qaContent.getTitle());
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, qaContent.getInstructions());

	List<QaQuestionDTO> questionDTOs = new LinkedList();

	/*
	 * get the existing question content
	 */
	Iterator queIterator = qaContent.getQaQueContents().iterator();
	while (queIterator.hasNext()) {

	    QaQueContent qaQuestion = (QaQueContent) queIterator.next();
	    if (qaQuestion != null) {
		QaQuestionDTO qaQuestionDTO = new QaQuestionDTO(qaQuestion);
		questionDTOs.add(qaQuestionDTO);
	    }
	}

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));
	request.setAttribute(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);
	sessionMap.put(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	SortedSet<QaCondition> conditionSet = new TreeSet<QaCondition>(new TextSearchConditionComparator());
	for (QaCondition condition : qaContent.getConditions()) {
	    conditionSet.add(condition);
	    for (QaQuestionDTO dto : questionDTOs) {
		for (QaQueContent question : condition.getQuestions()) {
		    if (dto.getDisplayOrder().equals(String.valueOf(question.getDisplayOrder()))) {
			condition.temporaryQuestionDTOSet.add(dto);
		    }
		}
	    }
	}
	sessionMap.put(QaAppConstants.ATTR_CONDITION_SET, conditionSet);

	List<QaQuestionDTO> listDeletedQuestionDTOs = new ArrayList<QaQuestionDTO>();
	sessionMap.put(QaAppConstants.LIST_DELETED_QUESTION_DTOS, listDeletedQuestionDTOs);

	qaAuthoringForm.resetUserAction();

	return qaContent;
    }

    /**
     * each tool has a signature. QA tool's signature is stored in MY_SIGNATURE.
     * The default tool content id and other depending content ids are obtained
     * in this method. if all the default content has been setup properly the
     * method persists DEFAULT_CONTENT_ID in the session.
     *
     * @param request
     * @param mapping
     * @return ActionForward
     */
    public boolean validateDefaultContent(HttpServletRequest request, ActionMapping mapping, IQaService qaService,
	    QaAuthoringForm qaAuthoringForm) {

	/*
	 * retrieve the default content id based on tool signature
	 */
	long defaultContentID = 0;
	try {
	    defaultContentID = qaService.getToolDefaultContentIdBySignature(QaAppConstants.MY_SIGNATURE);
	    if (defaultContentID == 0) {
		QaStarterAction.logger.debug("default content id has not been setup");
		return false;
	    }
	} catch (Exception e) {
	    QaStarterAction.logger.error("error getting the default content id: " + e.getMessage());
	    persistError(request, "error.defaultContent.notSetup");
	    return false;
	}

	/*
	 * retrieve uid of the content based on default content id determined above
	 */
	try {
	    //retrieve uid of the content based on default content id determined above
	    QaContent qaContent = qaService.getQaContent(defaultContentID);
	    if (qaContent == null) {
		QaStarterAction.logger.error("Exception occured: No default content");
		persistError(request, "error.defaultContent.notSetup");
		return false;
	    }

	} catch (Exception e) {
	    QaStarterAction.logger.error("Exception occured: No default question content");
	    persistError(request, "error.defaultContent.notSetup");
	    return false;
	}

	return true;
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

    private SortedSet<QaCondition> getQaConditionList(SessionMap<String, Object> sessionMap) {
	SortedSet<QaCondition> list = (SortedSet<QaCondition>) sessionMap.get(QaAppConstants.ATTR_CONDITION_SET);
	if (list == null) {
	    list = new TreeSet<QaCondition>(new TextSearchConditionComparator());
	    sessionMap.put(QaAppConstants.ATTR_CONDITION_SET, list);
	}
	return list;
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     *
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }
}
