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


package org.lamsfoundation.lams.tool.qa.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
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
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaConfigItem;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.AuthoringUtil;
import org.lamsfoundation.lams.tool.qa.util.QaQueContentComparator;
import org.lamsfoundation.lams.tool.qa.util.QaQuestionContentDTOComparator;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.tool.qa.web.form.QaAuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * Q&A Tool's authoring methods. Additionally, there is one more method that initializes authoring and it's located in
 * QaStarterAction.java.
 *
 * @author Ozgur Demirtas
 */
public class QaAction extends LamsDispatchAction implements QaAppConstants {
    private static Logger logger = Logger.getLogger(QaAction.class.getName());

    private static IQaService qaService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * submits content into the tool database
     */
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
	qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	String httpSessionID = qaAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	Long toolContentID = new Long(strToolContentID);

	List<QaQuestionDTO> questionDTOs = (List<QaQuestionDTO>) sessionMap.get(QaAppConstants.LIST_QUESTION_DTOS);

	ActionMessages errors = new ActionMessages();
	if (questionDTOs.size() == 0) {
	    ActionMessage error = new ActionMessage("questions.none.submitted");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	}

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	qaAuthoringForm.setTitle(richTextTitle);
	qaAuthoringForm.setInstructions(richTextInstructions);

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    QaAction.logger.debug("errors saved: " + errors);
	}

	QaContent qaContent = qaService.getQaContent(toolContentID);
	if (errors.isEmpty()) {
	    ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	    request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	    List<QaQuestionDTO> deletedQuestionDTOs = (List<QaQuestionDTO>) sessionMap.get(LIST_DELETED_QUESTION_DTOS);

	    // in case request is from monitoring module - recalculate User Answers
	    if (mode.isTeacher()) {
		Set<QaQueContent> oldQuestions = qaContent.getQaQueContents();
		qaService.removeQuestionsFromCache(qaContent);
		qaService.setDefineLater(strToolContentID, false);

		// audit log the teacher has started editing activity in monitor
		qaService.auditLogStartEditingActivityInMonitor(toolContentID);

		// recalculate User Answers
		qaService.recalculateUserAnswers(qaContent, oldQuestions, questionDTOs, deletedQuestionDTOs);
	    }

	    // remove deleted questions
	    for (QaQuestionDTO deletedQuestionDTO : deletedQuestionDTOs) {
		QaQueContent removeableQuestion = qaService.getQuestionByUid(deletedQuestionDTO.getUid());
		if (removeableQuestion != null) {
		    qaContent.getQaQueContents().remove(removeableQuestion);
		    qaService.removeQuestion(removeableQuestion);
		}
	    }

	    // store content
	    SortedSet<QaCondition> conditionSet = (SortedSet<QaCondition>) sessionMap
		    .get(QaAppConstants.ATTR_CONDITION_SET);
	    qaContent = saveOrUpdateQaContent(questionDTOs, request, qaContent, toolContentID, conditionSet);

	    //reOrganizeDisplayOrder
	    List<QaQueContent> sortedQuestions = qaService.getAllQuestionEntriesSorted(qaContent.getUid().longValue());
	    Iterator<QaQueContent> iter = sortedQuestions.iterator();
	    int displayOrder = 1;
	    while (iter.hasNext()) {
		QaQueContent question = iter.next();

		QaQueContent existingQaQueContent = qaService.getQuestionByUid(question.getUid());
		existingQaQueContent.setDisplayOrder(displayOrder);
		qaService.saveOrUpdateQuestion(existingQaQueContent);
		displayOrder++;
	    }

	    // ************************* Handle rating criterias *******************
	    List<RatingCriteria> oldCriterias = (List<RatingCriteria>) sessionMap
		    .get(AttributeNames.ATTR_RATING_CRITERIAS);
	    qaService.saveRatingCriterias(request, oldCriterias, toolContentID);

	    QaUtils.setFormProperties(request, qaAuthoringForm, strToolContentID, httpSessionID);

	    request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);

	} else {
	    if (qaContent != null) {
		QaUtils.setFormProperties(request, qaAuthoringForm, strToolContentID, httpSessionID);
	    }
	}

	List<QaCondition> delConditionList = getDeletedQaConditionList(sessionMap);
	Iterator<QaCondition> iter = delConditionList.iterator();
	while (iter.hasNext()) {
	    QaCondition condition = iter.next();
	    iter.remove();
	    qaService.deleteCondition(condition);
	}

	qaAuthoringForm.resetUserAction();
	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setCurrentTab("1");

	request.setAttribute(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);
	request.getSession().setAttribute(httpSessionID, sessionMap);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));
	sessionMap.put(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    private QaContent saveOrUpdateQaContent(List<QaQuestionDTO> questionDTOs, HttpServletRequest request,
	    QaContent qaContent, Long toolContentId, Set<QaCondition> conditions) {
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);
	String usernameVisible = request.getParameter(QaAppConstants.USERNAME_VISIBLE);
	String allowRateQuestions = request.getParameter(QaAppConstants.ALLOW_RATE_ANSWERS);
	String notifyTeachersOnResponseSubmit = request.getParameter(QaAppConstants.NOTIFY_TEACHERS_ON_RESPONSE_SUBMIT);
	String showOtherAnswers = request.getParameter("showOtherAnswers");
	String questionsSequenced = request.getParameter(QaAppConstants.QUESTIONS_SEQUENCED);
	String lockWhenFinished = request.getParameter("lockWhenFinished");
	String noReeditAllowed = request.getParameter("noReeditAllowed");
	String allowRichEditor = request.getParameter("allowRichEditor");
	String useSelectLeaderToolOuput = request.getParameter("useSelectLeaderToolOuput");
	String reflect = request.getParameter(QaAppConstants.REFLECT);
	String reflectionSubject = request.getParameter(QaAppConstants.REFLECTION_SUBJECT);
	int minimumRates = WebUtil.readIntParam(request, "minimumRates");
	int maximumRates = WebUtil.readIntParam(request, "maximumRates");

	boolean questionsSequencedBoolean = false;
	boolean lockWhenFinishedBoolean = false;
	boolean noReeditAllowedBoolean = false;
	boolean usernameVisibleBoolean = false;
	boolean allowRateQuestionsBoolean = false;
	boolean notifyTeachersOnResponseSubmitBoolean = false;
	boolean showOtherAnswersBoolean = false;
	boolean reflectBoolean = false;
	boolean allowRichEditorBoolean = false;
	boolean useSelectLeaderToolOuputBoolean = false;

	if (questionsSequenced != null && questionsSequenced.equalsIgnoreCase("1")) {
	    questionsSequencedBoolean = true;
	}

	if (lockWhenFinished != null && lockWhenFinished.equalsIgnoreCase("1")) {
	    lockWhenFinishedBoolean = true;
	}

	if (noReeditAllowed != null && noReeditAllowed.equalsIgnoreCase("1")) {
	    noReeditAllowedBoolean = true;
	    lockWhenFinishedBoolean = true;
	}

	if (usernameVisible != null && usernameVisible.equalsIgnoreCase("1")) {
	    usernameVisibleBoolean = true;
	}

	if (showOtherAnswers != null && showOtherAnswers.equalsIgnoreCase("1")) {
	    showOtherAnswersBoolean = true;
	}

	if (allowRateQuestions != null && allowRateQuestions.equalsIgnoreCase("1") && showOtherAnswersBoolean) {
	    allowRateQuestionsBoolean = true;
	}

	if (notifyTeachersOnResponseSubmit != null && notifyTeachersOnResponseSubmit.equalsIgnoreCase("1")) {
	    notifyTeachersOnResponseSubmitBoolean = true;
	}

	if (allowRichEditor != null && allowRichEditor.equalsIgnoreCase("1")) {
	    allowRichEditorBoolean = true;
	}

	if (useSelectLeaderToolOuput != null && useSelectLeaderToolOuput.equalsIgnoreCase("1")) {
	    useSelectLeaderToolOuputBoolean = true;
	}

	if (reflect != null && reflect.equalsIgnoreCase("1")) {
	    reflectBoolean = true;
	}
	long userId = 0;
	if (toolUser != null) {
	    userId = toolUser.getUserID().longValue();
	} else {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null) {
		userId = user.getUserID().longValue();
	    } else {
		userId = 0;
	    }
	}

	boolean newContent = false;
	if (qaContent == null) {
	    qaContent = new QaContent();
	    newContent = true;
	}

	qaContent.setQaContentId(toolContentId);
	qaContent.setTitle(richTextTitle);
	qaContent.setInstructions(richTextInstructions);
	qaContent.setUpdateDate(new Date(System.currentTimeMillis()));
	/** keep updating this one */
	qaContent.setCreatedBy(userId);
	/** make sure we are setting the userId from the User object above */

	qaContent.setUsernameVisible(usernameVisibleBoolean);
	qaContent.setAllowRateAnswers(allowRateQuestionsBoolean);
	qaContent.setNotifyTeachersOnResponseSubmit(notifyTeachersOnResponseSubmitBoolean);
	qaContent.setShowOtherAnswers(showOtherAnswersBoolean);
	qaContent.setQuestionsSequenced(questionsSequencedBoolean);
	qaContent.setLockWhenFinished(lockWhenFinishedBoolean);
	qaContent.setNoReeditAllowed(noReeditAllowedBoolean);
	qaContent.setReflect(reflectBoolean);
	qaContent.setReflectionSubject(reflectionSubject);
	qaContent.setAllowRichEditor(allowRichEditorBoolean);
	qaContent.setUseSelectLeaderToolOuput(useSelectLeaderToolOuputBoolean);
	qaContent.setMinimumRates(minimumRates);
	qaContent.setMaximumRates(maximumRates);

	qaContent.setConditions(new TreeSet<QaCondition>(new TextSearchConditionComparator()));
	if (newContent) {
	    qaService.createQaContent(qaContent);
	} else {
	    qaService.updateQaContent(qaContent);
	}

	qaContent = qaService.getQaContent(toolContentId);

	for (QaCondition condition : conditions) {
	    condition.setQuestions(new TreeSet<QaQueContent>(new QaQueContentComparator()));
	    for (QaQuestionDTO dto : condition.temporaryQuestionDTOSet) {
		for (QaQueContent queContent : qaContent.getQaQueContents()) {
		    if (dto.getDisplayOrder().equals(String.valueOf(queContent.getDisplayOrder()))) {
			condition.getQuestions().add(queContent);
		    }
		}
	    }
	}
	qaContent.setConditions(conditions);
	qaService.updateQaContent(qaContent);

	// persist questions
	int displayOrder = 0;
	for (QaQuestionDTO questionDTO : questionDTOs) {

	    String questionText = questionDTO.getQuestion();

	    // skip empty questions
	    if (questionText.isEmpty()) {
		continue;
	    }

	    ++displayOrder;

	    QaQueContent question = qaService.getQuestionByUid(questionDTO.getUid());

	    // in case question doesn't exist
	    if (question == null) {
		question = new QaQueContent(questionText, displayOrder, questionDTO.getFeedback(),
			questionDTO.isRequired(), questionDTO.getMinWordsLimit(), qaContent);
		qaContent.getQaQueContents().add(question);
		question.setQaContent(qaContent);

		// in case question exists already
	    } else {

		question.setQuestion(questionText);
		question.setFeedback(questionDTO.getFeedback());
		question.setDisplayOrder(displayOrder);
		question.setRequired(questionDTO.isRequired());
		question.setMinWordsLimit(questionDTO.getMinWordsLimit());
	    }

	    qaService.saveOrUpdateQuestion(question);
	}

	return qaContent;
    }

    /**
     * saveSingleQuestion
     */
    public ActionForward saveSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String editQuestionBoxRequest = request.getParameter("editQuestionBoxRequest");

	List<QaQuestionDTO> questionDTOs = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_DTOS);

	String newQuestion = request.getParameter("newQuestion");

	String feedback = request.getParameter("feedback");

	String editableQuestionIndex = request.getParameter("editableQuestionIndex");

	String required = request.getParameter("required");
	boolean requiredBoolean = false;
	if (required != null && required.equalsIgnoreCase("1")) {
	    requiredBoolean = true;
	}
	int minWordsLimit = WebUtil.readIntParam(request, "minWordsLimit");

	if (newQuestion != null && newQuestion.length() > 0) {
	    if (editQuestionBoxRequest != null && editQuestionBoxRequest.equals("false")) {
		//request for add and save
		boolean duplicates = AuthoringUtil.checkDuplicateQuestions(questionDTOs, newQuestion);

		if (!duplicates) {
		    QaQuestionDTO qaQuestionDTO = null;
		    Iterator<QaQuestionDTO> iter = questionDTOs.iterator();
		    while (iter.hasNext()) {
			qaQuestionDTO = iter.next();

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
		    qaQuestionDTO.setMinWordsLimit(minWordsLimit);

		    questionDTOs = AuthoringUtil.reorderUpdateQuestionDTOs(questionDTOs, qaQuestionDTO,
			    editableQuestionIndex);
		} else {
		    //duplicate question entry, not adding
		}
	    } else {
		//request for edit and save
		QaQuestionDTO qaQuestionDTO = null;
		Iterator<QaQuestionDTO> iter = questionDTOs.iterator();
		while (iter.hasNext()) {
		    qaQuestionDTO = iter.next();

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
		qaQuestionDTO.setMinWordsLimit(minWordsLimit);

		questionDTOs = AuthoringUtil.reorderUpdateQuestionDTOs(questionDTOs, qaQuestionDTO,
			editableQuestionIndex);
	    }
	} else {
	    //entry blank, not adding
	}

	request.setAttribute(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);
	sessionMap.put(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	qaAuthoringForm.setTitle(richTextTitle);
	qaAuthoringForm.setInstructions(richTextInstructions);

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	QaUtils.setFormProperties(request, qaAuthoringForm, strToolContentID, httpSessionID);

	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setCurrentTab("1");

	request.getSession().setAttribute(httpSessionID, sessionMap);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * addSingleQuestion
     */
    public ActionForward addSingleQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	List<QaQuestionDTO> questionDTOs = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_DTOS);

	String newQuestion = request.getParameter("newQuestion");
	String feedback = request.getParameter("feedback");
	String required = request.getParameter("required");
	boolean requiredBoolean = false;
	if (required != null && required.equalsIgnoreCase("1")) {
	    requiredBoolean = true;
	}
	int minWordsLimit = WebUtil.readIntParam(request, "minWordsLimit");

	int listSize = questionDTOs.size();

	if (newQuestion != null && newQuestion.length() > 0) {
	    boolean duplicates = AuthoringUtil.checkDuplicateQuestions(questionDTOs, newQuestion);

	    if (!duplicates) {
		QaQuestionDTO qaQuestionDTO = new QaQuestionDTO(newQuestion, new Long(listSize + 1).toString(),
			feedback, requiredBoolean, minWordsLimit);
		questionDTOs.add(qaQuestionDTO);
	    } else {
		//entry duplicate, not adding
	    }
	} else {
	    //entry blank, not adding
	}

	request.setAttribute(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);
	sessionMap.put(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	qaAuthoringForm.setTitle(richTextTitle);
	qaAuthoringForm.setInstructions(richTextInstructions);

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	QaUtils.setFormProperties(request, qaAuthoringForm, strToolContentID, httpSessionID);

	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setCurrentTab("1");

	request.getSession().setAttribute(httpSessionID, sessionMap);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * opens up an new screen within the current page for adding a new question
     */
    public ActionForward newQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	qaAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);

	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	qaAuthoringForm.setTitle(richTextTitle);
	qaAuthoringForm.setInstructions(richTextInstructions);

	QaUtils.setFormProperties(request, qaAuthoringForm, strToolContentID, httpSessionID);

	Collection<QaQuestionDTO> questionDTOs = (Collection<QaQuestionDTO>) sessionMap
		.get(QaAppConstants.LIST_QUESTION_DTOS);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

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
     */
    public ActionForward newEditableQuestionBox(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	qaAuthoringForm.setEditableQuestionIndex(questionIndex);

	List<QaQuestionDTO> questionDTOs = (List<QaQuestionDTO>) sessionMap.get(QaAppConstants.LIST_QUESTION_DTOS);

	String editableQuestion = "";
	String editableFeedback = "";
	boolean requiredBoolean = false;
	int minWordsLimit = 0;
	Iterator<QaQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = iter.next();
	    String displayOrder = qaQuestionDTO.getDisplayOrder();

	    if (displayOrder != null && !displayOrder.equals("")) {
		if (displayOrder.equals(questionIndex)) {
		    editableFeedback = qaQuestionDTO.getFeedback();
		    editableQuestion = qaQuestionDTO.getQuestion();
		    requiredBoolean = qaQuestionDTO.isRequired();
		    minWordsLimit = qaQuestionDTO.getMinWordsLimit();
		    break;
		}

	    }
	}

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	qaAuthoringForm.setTitle(richTextTitle);
	qaAuthoringForm.setInstructions(richTextInstructions);

	QaUtils.setFormProperties(request, qaAuthoringForm, strToolContentID, httpSessionID);

	qaAuthoringForm.setRequired(requiredBoolean);
	qaAuthoringForm.setMinWordsLimit(minWordsLimit);
	qaAuthoringForm.setEditableQuestionText(editableQuestion);
	qaAuthoringForm.setFeedback(editableFeedback);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	return mapping.findForward("editQuestionBox");
    }

    /**
     * removes a question from the questions map
     */
    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	String httpSessionID = qaAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndexToDelete = request.getParameter("questionIndex");
	QaQuestionDTO questionToDelete = null;
	List<QaQuestionDTO> questionDTOs = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_DTOS);

	List<QaQuestionDTO> listFinalQuestionDTO = new LinkedList<QaQuestionDTO>();
	int queIndex = 0;
	for (QaQuestionDTO questionDTO : questionDTOs) {

	    String questionText = questionDTO.getQuestion();
	    String displayOrder = questionDTO.getDisplayOrder();

	    if (questionText != null && !questionText.equals("") && (!displayOrder.equals(questionIndexToDelete))) {

		++queIndex;
		questionDTO.setDisplayOrder(new Integer(queIndex).toString());
		listFinalQuestionDTO.add(questionDTO);
	    }
	    if ((questionText != null) && (!questionText.isEmpty()) && displayOrder.equals(questionIndexToDelete)) {
		List<QaQuestionDTO> deletedQuestionDTOs = (List<QaQuestionDTO>) sessionMap
			.get(LIST_DELETED_QUESTION_DTOS);
		;
		deletedQuestionDTOs.add(questionDTO);
		sessionMap.put(LIST_DELETED_QUESTION_DTOS, deletedQuestionDTOs);
		questionToDelete = questionDTO;
	    }
	}
	request.setAttribute(QaAppConstants.LIST_QUESTION_DTOS, listFinalQuestionDTO);
	sessionMap.put(QaAppConstants.LIST_QUESTION_DTOS, listFinalQuestionDTO);
	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(listFinalQuestionDTO.size()));

	SortedSet<QaCondition> conditions = (SortedSet<QaCondition>) sessionMap.get(QaAppConstants.ATTR_CONDITION_SET);
	Iterator<QaCondition> conditionIter = conditions.iterator();
	while (conditionIter.hasNext()) {
	    QaCondition condition = conditionIter.next();
	    Iterator<QaQuestionDTO> dtoIter = condition.temporaryQuestionDTOSet.iterator();
	    while (dtoIter.hasNext()) {
		if (dtoIter.next() == questionToDelete) {
		    dtoIter.remove();
		}
	    }
	    if (condition.temporaryQuestionDTOSet.isEmpty()) {
		conditionIter.remove();
	    }
	}

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);
	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);
	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	qaAuthoringForm.setTitle(richTextTitle);
	qaAuthoringForm.setInstructions(richTextInstructions);
	request.getSession().setAttribute(httpSessionID, sessionMap);
	QaUtils.setFormProperties(request, qaAuthoringForm, strToolContentID, httpSessionID);
	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setCurrentTab("1");

	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * moves a question down in the list
     */
    public ActionForward moveQuestionDown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	String httpSessionID = qaAuthoringForm.getHttpSessionID();
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List<QaQuestionDTO> questionDTOs = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_DTOS);

	SortedSet<QaCondition> conditionSet = (SortedSet<QaCondition>) sessionMap
		.get(QaAppConstants.ATTR_CONDITION_SET);

	questionDTOs = QaAction.swapQuestions(questionDTOs, questionIndex, "down", conditionSet);

	questionDTOs = QaAction.reorderQuestionDTOs(questionDTOs);

	sessionMap.put(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	qaAuthoringForm.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);

	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	qaAuthoringForm.setTitle(richTextTitle);
	qaAuthoringForm.setInstructions(richTextInstructions);
	request.getSession().setAttribute(httpSessionID, sessionMap);

	QaUtils.setFormProperties(request, qaAuthoringForm, strToolContentID, httpSessionID);

	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setCurrentTab("1");

	request.setAttribute(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    /**
     * moves a question up in the list
     */
    public ActionForward moveQuestionUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;

	String httpSessionID = qaAuthoringForm.getHttpSessionID();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(httpSessionID);

	String questionIndex = request.getParameter("questionIndex");

	List<QaQuestionDTO> questionDTOs = (List) sessionMap.get(QaAppConstants.LIST_QUESTION_DTOS);

	SortedSet<QaCondition> conditionSet = (SortedSet<QaCondition>) sessionMap
		.get(QaAppConstants.ATTR_CONDITION_SET);
	questionDTOs = QaAction.swapQuestions(questionDTOs, questionIndex, "up", conditionSet);

	questionDTOs = QaAction.reorderQuestionDTOs(questionDTOs);

	sessionMap.put(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	qaAuthoringForm.setContentFolderID(contentFolderID);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);

	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	sessionMap.put(QaAppConstants.ACTIVITY_TITLE_KEY, richTextTitle);
	sessionMap.put(QaAppConstants.ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);

	qaAuthoringForm.setTitle(richTextTitle);
	qaAuthoringForm.setInstructions(richTextInstructions);

	request.getSession().setAttribute(httpSessionID, sessionMap);

	QaUtils.setFormProperties(request, qaAuthoringForm, strToolContentID, httpSessionID);

	qaAuthoringForm.setToolContentID(strToolContentID);
	qaAuthoringForm.setHttpSessionID(httpSessionID);
	qaAuthoringForm.setCurrentTab("1");

	request.setAttribute(QaAppConstants.LIST_QUESTION_DTOS, questionDTOs);

	request.setAttribute(QaAppConstants.TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));
	return mapping.findForward(QaAppConstants.LOAD_QUESTIONS);
    }

    private static List<QaQuestionDTO> swapQuestions(List<QaQuestionDTO> questionDTOs, String questionIndex,
	    String direction, Set<QaCondition> conditions) {

	int intQuestionIndex = new Integer(questionIndex).intValue();
	int intOriginalQuestionIndex = intQuestionIndex;

	int replacedQuestionIndex = 0;
	if (direction.equals("down")) {
	    // direction down
	    replacedQuestionIndex = ++intQuestionIndex;
	} else {
	    // direction up
	    replacedQuestionIndex = --intQuestionIndex;
	}

	QaQuestionDTO mainQuestion = QaAction.getQuestionAtDisplayOrder(questionDTOs, intOriginalQuestionIndex);

	QaQuestionDTO replacedQuestion = QaAction.getQuestionAtDisplayOrder(questionDTOs, replacedQuestionIndex);

	List<QaQuestionDTO> newQuestionDtos = new LinkedList<QaQuestionDTO>();

	Iterator<QaQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    QaQuestionDTO questionDTO = iter.next();
	    QaQuestionDTO tempQuestion = null;

	    if (!questionDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())
		    && !questionDTO.getDisplayOrder().equals(new Integer(replacedQuestionIndex).toString())) {
		// normal copy
		tempQuestion = questionDTO;

	    } else if (questionDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())) {
		// move type 1
		tempQuestion = replacedQuestion;

	    } else if (questionDTO.getDisplayOrder().equals(new Integer(replacedQuestionIndex).toString())) {
		// move type 1
		tempQuestion = mainQuestion;
	    }

	    newQuestionDtos.add(tempQuestion);
	}

	// references in conditions also need to be changed
	if (conditions != null) {
	    for (QaCondition condition : conditions) {
		SortedSet<QaQuestionDTO> newQuestionDTOSet = new TreeSet<QaQuestionDTO>(
			new QaQuestionContentDTOComparator());
		for (QaQuestionDTO dto : newQuestionDtos) {
		    if (condition.temporaryQuestionDTOSet.contains(dto)) {
			newQuestionDTOSet.add(dto);
		    }
		}
		condition.temporaryQuestionDTOSet = newQuestionDTOSet;
	    }
	}

	return newQuestionDtos;
    }

    private static QaQuestionDTO getQuestionAtDisplayOrder(List<QaQuestionDTO> questionDTOs,
	    int intOriginalQuestionIndex) {

	Iterator<QaQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = iter.next();
	    if (new Integer(intOriginalQuestionIndex).toString().equals(qaQuestionDTO.getDisplayOrder())) {
		return qaQuestionDTO;
	    }
	}
	return null;
    }

    private static List<QaQuestionDTO> reorderQuestionDTOs(List<QaQuestionDTO> questionDTOs) {
	List<QaQuestionDTO> listFinalQuestionDTO = new LinkedList<QaQuestionDTO>();

	int queIndex = 0;
	Iterator<QaQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = iter.next();

	    String question = qaQuestionDTO.getQuestion();
	    String feedback = qaQuestionDTO.getFeedback();
	    boolean required = qaQuestionDTO.isRequired();
	    int minWordsLimit = qaQuestionDTO.getMinWordsLimit();

	    if (question != null && !question.equals("")) {
		++queIndex;

		qaQuestionDTO.setQuestion(question);
		qaQuestionDTO.setDisplayOrder(new Integer(queIndex).toString());
		qaQuestionDTO.setFeedback(feedback);
		qaQuestionDTO.setRequired(required);
		qaQuestionDTO.setMinWordsLimit(minWordsLimit);

		listFinalQuestionDTO.add(qaQuestionDTO);
	    }
	}
	return listFinalQuestionDTO;
    }

    /**
     * Get the deleted condition list, which could be persisted or non-persisted
     * items.
     *
     * @param request
     * @return
     */
    private List<QaCondition> getDeletedQaConditionList(SessionMap<String, Object> sessionMap) {
	List<QaCondition> list = (List<QaCondition>) sessionMap.get(QaAppConstants.ATTR_DELETED_CONDITION_LIST);
	if (list == null) {
	    list = new ArrayList<QaCondition>();
	    sessionMap.put(QaAppConstants.ATTR_DELETED_CONDITION_LIST, list);
	}
	return list;
    }
}
