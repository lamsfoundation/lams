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

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.util.QaQueContentComparator;
import org.lamsfoundation.lams.tool.qa.util.QaQuestionContentDTOComparator;
import org.lamsfoundation.lams.tool.qa.web.form.QaAuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Keeps all operations needed for Authoring mode.
 * 
 * @author Ozgur Demirtas
 */
public class AuthoringUtil implements QaAppConstants {

    protected static List<QaQuestionDTO> swapQuestions(List<QaQuestionDTO> questionDTOs, String questionIndex,
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

	QaQuestionDTO mainQuestion = getQuestionAtDisplayOrder(questionDTOs, intOriginalQuestionIndex);

	QaQuestionDTO replacedQuestion = getQuestionAtDisplayOrder(questionDTOs, replacedQuestionIndex);

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
		for (QaQuestionDTO dto : (List<QaQuestionDTO>) newQuestionDtos) {
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

    protected static List<QaQuestionDTO> reorderQuestionDTOs(List<QaQuestionDTO> questionDTOs) {
	List<QaQuestionDTO> listFinalQuestionDTO = new LinkedList<QaQuestionDTO>();

	int queIndex = 0;
	Iterator<QaQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = iter.next();

	    String question = qaQuestionDTO.getQuestion();
	    String feedback = qaQuestionDTO.getFeedback();
	    boolean required = qaQuestionDTO.isRequired();

	    if (question != null && !question.equals("")) {
		++queIndex;

		qaQuestionDTO.setQuestion(question);
		qaQuestionDTO.setDisplayOrder(new Integer(queIndex).toString());
		qaQuestionDTO.setFeedback(feedback);
		qaQuestionDTO.setRequired(required);

		listFinalQuestionDTO.add(qaQuestionDTO);
	    }
	}
	return listFinalQuestionDTO;
    }

    protected static List<QaQuestionDTO> reorderUpdateQuestionDTOs(List<QaQuestionDTO> questionDTOs,
	    QaQuestionDTO qaQuestionContentDTONew, String editableQuestionIndex) {

	List<QaQuestionDTO> listFinalQuestionDTO = new LinkedList<QaQuestionDTO>();

	int queIndex = 0;
	Iterator iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = (QaQuestionDTO) iter.next();

	    ++queIndex;

	    String question = qaQuestionDTO.getQuestion();
	    String displayOrder = qaQuestionDTO.getDisplayOrder();
	    String feedback = qaQuestionDTO.getFeedback();
	    boolean required = qaQuestionDTO.isRequired();

	    if (displayOrder.equals(editableQuestionIndex)) {
		qaQuestionDTO.setQuestion(qaQuestionContentDTONew.getQuestion());
		qaQuestionDTO.setDisplayOrder(qaQuestionContentDTONew.getDisplayOrder());
		qaQuestionDTO.setFeedback(qaQuestionContentDTONew.getFeedback());
		qaQuestionDTO.setRequired(required);

		listFinalQuestionDTO.add(qaQuestionDTO);
	    } else {
		qaQuestionDTO.setQuestion(question);
		qaQuestionDTO.setDisplayOrder(displayOrder);
		qaQuestionDTO.setFeedback(feedback);
		qaQuestionDTO.setRequired(required);

		listFinalQuestionDTO.add(qaQuestionDTO);

	    }
	}
	return listFinalQuestionDTO;
    }

    public static QaContent saveOrUpdateQaContent(List<QaQuestionDTO> questionDTOs, IQaService qaService,
	    HttpServletRequest request, QaContent qaContent, String strToolContentID,
	    Set<QaCondition> conditions) {
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);
	String usernameVisible = request.getParameter(QaAppConstants.USERNAME_VISIBLE);
	String allowRateQuestions = request.getParameter(QaAppConstants.ALLOW_RATE_ANSWERS);
	String notifyTeachersOnResponseSubmit = request.getParameter(QaAppConstants.NOTIFY_TEACHERS_ON_RESPONSE_SUBMIT);
	String showOtherAnswers = request.getParameter("showOtherAnswers");
	String questionsSequenced = request.getParameter(QaAppConstants.QUESTIONS_SEQUENCED);
	String lockWhenFinished = request.getParameter("lockWhenFinished");
	String allowRichEditor = request.getParameter("allowRichEditor");
	String useSelectLeaderToolOuput = request.getParameter("useSelectLeaderToolOuput");
	String reflect = request.getParameter(QaAppConstants.REFLECT);
	String reflectionSubject = request.getParameter(QaAppConstants.REFLECTION_SUBJECT);

	boolean questionsSequencedBoolean = false;
	boolean lockWhenFinishedBoolean = false;
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

	if (usernameVisible != null && usernameVisible.equalsIgnoreCase("1")) {
	    usernameVisibleBoolean = true;
	}

	if (allowRateQuestions != null && allowRateQuestions.equalsIgnoreCase("1")) {
	    allowRateQuestionsBoolean = true;
	}

	if (notifyTeachersOnResponseSubmit != null && notifyTeachersOnResponseSubmit.equalsIgnoreCase("1")) {
	    notifyTeachersOnResponseSubmitBoolean = true;
	}

	if (showOtherAnswers != null && showOtherAnswers.equalsIgnoreCase("1")) {
	    showOtherAnswersBoolean = true;
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

	qaContent.setQaContentId(new Long(strToolContentID));
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
	qaContent.setReflect(reflectBoolean);
	qaContent.setReflectionSubject(reflectionSubject);
	qaContent.setAllowRichEditor(allowRichEditorBoolean);
	qaContent.setUseSelectLeaderToolOuput(useSelectLeaderToolOuputBoolean);

	qaContent.setConditions(new TreeSet<QaCondition>(new TextSearchConditionComparator()));
	if (newContent) {
	    qaService.createQaContent(qaContent);
	} else {
	    qaService.updateQaContent(qaContent);
	}

	qaContent = qaService.getQaContent(new Long(strToolContentID).longValue());

	for (QaCondition condition : conditions) {
	    condition.setQuestions(new TreeSet<QaQueContent>(new QaQueContentComparator()));
	    for (QaQuestionDTO dto : condition.temporaryQuestionDTOSet) {
		for (QaQueContent queContent : (Set<QaQueContent>) qaContent.getQaQueContents()) {
		    if (dto.getDisplayOrder().equals(String.valueOf(queContent.getDisplayOrder()))) {
			condition.getQuestions().add(queContent);
		    }
		}
	    }
	}
	qaContent.setConditions(conditions);
	qaService.updateQaContent(qaContent);
	
	//persist questions
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
			questionDTO.isRequired(), qaContent);
		qaContent.getQaQueContents().add(question);
		question.setQaContent(qaContent);
		
	    // in case question exists already		
	    } else {

		question.setQuestion(questionText);
		question.setFeedback(questionDTO.getFeedback());
		question.setDisplayOrder(displayOrder);
		question.setRequired(questionDTO.isRequired());
	    }
	    
	    qaService.saveOrUpdateQuestion(question);
	}
	
	return qaContent;
    }

    public static boolean checkDuplicateQuestions(List<QaQuestionDTO> questionDTOs, String newQuestion) {
	for (QaQuestionDTO questionDTO : questionDTOs) {
	    if (questionDTO.getQuestion() != null && questionDTO.getQuestion().equals(newQuestion)) {
		return true;
	    }
	}
	return false;
    }
}