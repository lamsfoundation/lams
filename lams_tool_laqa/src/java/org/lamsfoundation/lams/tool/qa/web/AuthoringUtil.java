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
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.util.QaComparator;
import org.lamsfoundation.lams.tool.qa.util.QaQueContentComparator;
import org.lamsfoundation.lams.tool.qa.util.QaQuestionContentDTOComparator;
import org.lamsfoundation.lams.tool.qa.web.form.QaAuthoringForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * Keeps all operations needed for Authoring mode.
 * 
 * @author Ozgur Demirtas
 * 
 */
public class AuthoringUtil implements QaAppConstants {

    protected static List swapNodes(List listQuestionContentDTO, String questionIndex, String direction,
	    Set<QaCondition> conditions) {

	int intQuestionIndex = new Integer(questionIndex).intValue();
	int intOriginalQuestionIndex = intQuestionIndex;

	int replacedNodeIndex = 0;
	if (direction.equals("down")) {
	    //direction down
	    replacedNodeIndex = ++intQuestionIndex;
	} else {
	    //direction up
	    replacedNodeIndex = --intQuestionIndex;

	}

	QaQuestionDTO mainNode = extractNodeAtDisplayOrder(listQuestionContentDTO, intOriginalQuestionIndex);

	QaQuestionDTO replacedNode = extractNodeAtDisplayOrder(listQuestionContentDTO, replacedNodeIndex);

	List listFinalQuestionContentDTO = new LinkedList();

	listFinalQuestionContentDTO = reorderSwappedListQuestionContentDTO(listQuestionContentDTO,
		intOriginalQuestionIndex, replacedNodeIndex, mainNode, replacedNode, conditions);

	return listFinalQuestionContentDTO;
    }

    protected static List reorderSwappedListQuestionContentDTO(List listQuestionContentDTO,
	    int intOriginalQuestionIndex, int replacedNodeIndex, QaQuestionDTO mainNode,
	    QaQuestionDTO replacedNode, Set<QaCondition> conditions) {

	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = (QaQuestionDTO) listIterator.next();
	    queIndex++;
	    QaQuestionDTO tempNode = null;
	   
	    if (!qaQuestionDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())
		    && !qaQuestionDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) {
		//normal copy
		tempNode = new QaQuestionDTO(qaQuestionDTO.getQuestion(),
			qaQuestionDTO.getDisplayOrder(),	qaQuestionDTO.getFeedback(), qaQuestionDTO.isRequired());
	    } else if (qaQuestionDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())) {
		//move type 1
		tempNode = new QaQuestionDTO(replacedNode.getQuestion(),
			replacedNode.getDisplayOrder(),replacedNode.getFeedback(), replacedNode.isRequired());
	    } else if (qaQuestionDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) {
		//move type 1
		tempNode = new QaQuestionDTO(mainNode.getQuestion(),
			mainNode.getDisplayOrder(), mainNode.getFeedback(), mainNode.isRequired());
	    }

	    listFinalQuestionContentDTO.add(tempNode);
	}
	// references in conditions also need to be changed
	if (conditions != null) {
	    for (QaCondition condition : conditions) {
		SortedSet<QaQuestionDTO> newQuestionDTOSet = new TreeSet<QaQuestionDTO>(
			new QaQuestionContentDTOComparator());
		for (QaQuestionDTO dto : (List<QaQuestionDTO>) listFinalQuestionContentDTO) {
		    if (condition.temporaryQuestionDTOSet.contains(dto)) {
			newQuestionDTOSet.add(dto);
		    }
		}
		condition.temporaryQuestionDTOSet = newQuestionDTOSet;
	    }
	}
	return listFinalQuestionContentDTO;
    }

    protected static QaQuestionDTO extractNodeAtDisplayOrder(List listQuestionContentDTO,
	    int intOriginalQuestionIndex) {

	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = (QaQuestionDTO) listIterator.next();
	    if (new Integer(intOriginalQuestionIndex).toString().equals(qaQuestionDTO.getDisplayOrder())) {
		return qaQuestionDTO;
	    }
	}
	return null;
    }
    
    protected static Map reorderQuestionContentMap(Map mapQuestionContent) {
	Map mapFinalQuestionContent = new TreeMap(new QaComparator());

	int queIndex = 0;
	Iterator itMap = mapQuestionContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    if (pairs.getValue() != null && !pairs.getValue().equals("")) {
		++queIndex;
		mapFinalQuestionContent.put(new Integer(queIndex).toString(), pairs.getValue());

	    }
	}
	return mapFinalQuestionContent;
    }

    protected static List reorderListQuestionContentDTO(List listQuestionContentDTO, String excludeQuestionIndex) {

	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = (QaQuestionDTO) listIterator.next();
	    
	    String question = qaQuestionDTO.getQuestion();

	    String displayOrder = qaQuestionDTO.getDisplayOrder();

	    String feedback = qaQuestionDTO.getFeedback();
	    boolean required = qaQuestionDTO.isRequired();

	    if (question != null && !question.equals("")) {
		if (!displayOrder.equals(excludeQuestionIndex)) {
		    ++queIndex;

		    qaQuestionDTO.setQuestion(question);
		    qaQuestionDTO.setDisplayOrder(new Integer(queIndex).toString());
		    qaQuestionDTO.setFeedback(feedback);
		    qaQuestionDTO.setRequired(required);

		    listFinalQuestionContentDTO.add(qaQuestionDTO);
		}
	    }
	}
	return listFinalQuestionContentDTO;
    }

    protected static List reorderSimpleListQuestionContentDTO(List listQuestionContentDTO) {
	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = (QaQuestionDTO) listIterator.next();
	    
	    String question = qaQuestionDTO.getQuestion();
	    String displayOrder = qaQuestionDTO.getDisplayOrder();
	    String feedback = qaQuestionDTO.getFeedback();   
	    boolean required = qaQuestionDTO.isRequired();

	    if (question != null && !question.equals("")) {
		++queIndex;

		qaQuestionDTO.setQuestion(question);
		qaQuestionDTO.setDisplayOrder(new Integer(queIndex).toString());
		qaQuestionDTO.setFeedback(feedback);
		qaQuestionDTO.setRequired(required);

		listFinalQuestionContentDTO.add(qaQuestionDTO);
	    }
	}
	return listFinalQuestionContentDTO;
    }

    protected static List reorderUpdateListQuestionContentDTO(List listQuestionContentDTO,
	    QaQuestionDTO qaQuestionContentDTONew, String editableQuestionIndex) {

	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionDTO qaQuestionDTO = (QaQuestionDTO) listIterator.next();
	   
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

		listFinalQuestionContentDTO.add(qaQuestionDTO);
	    } else {
		qaQuestionDTO.setQuestion(question);
		qaQuestionDTO.setDisplayOrder(displayOrder);
		qaQuestionDTO.setFeedback(feedback);
		qaQuestionDTO.setRequired(required);

		listFinalQuestionContentDTO.add(qaQuestionDTO);

	    }
	}
	return listFinalQuestionContentDTO;
    }

    /**
     * repopulateMap(TreeMap mapQuestionContent, HttpServletRequest request)
     * return void repopulates the user entries into the Map
     */
    protected void repopulateMap(Map mapQuestionContent, HttpServletRequest request) {
	int intQuestionIndex = mapQuestionContent.size();

	/*
	 * if there is data in the Map remaining from previous session remove those
	 */
	mapQuestionContent.clear();

	for (long i = 0; i < intQuestionIndex; i++) {
	    String candidateQuestionEntry = request.getParameter("questionContent" + i);
	    if (i == 0) {
	    }
	    if (candidateQuestionEntry != null && candidateQuestionEntry.length() > 0) {
		mapQuestionContent.put(new Long(i + 1).toString(), candidateQuestionEntry);
	    }
	}
    }

    public QaContent saveOrUpdateQaContent(List<QaQuestionDTO> listQuestionContentDTO, IQaService qaService,
	    QaAuthoringForm qaAuthoringForm, HttpServletRequest request, QaContent qaContent, String strToolContentID,
	    Set<QaCondition> conditions) {
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	String synchInMonitor = request.getParameter(QaAppConstants.SYNC_IN_MONITOR);

	String usernameVisible = request.getParameter(QaAppConstants.USERNAME_VISIBLE);
	
	String allowRateQuestions = request.getParameter(QaAppConstants.ALLOW_RATE_ANSWERS);

	String showOtherAnswers = request.getParameter("showOtherAnswers");

	String questionsSequenced = request.getParameter(QaAppConstants.QUESTIONS_SEQUENCED);

	String lockWhenFinished = request.getParameter("lockWhenFinished");
	
	String allowRichEditor = request.getParameter("allowRichEditor");
	
	String useSelectLeaderToolOuput = request.getParameter("useSelectLeaderToolOuput");

	String richTextOfflineInstructions = request.getParameter(QaAppConstants.OFFLINE_INSTRUCTIONS);
	String richTextOnlineInstructions = request.getParameter(QaAppConstants.ONLINE_INSTRUCTIONS);
	String reflect = request.getParameter(QaAppConstants.REFLECT);

	String reflectionSubject = request.getParameter(QaAppConstants.REFLECTION_SUBJECT);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);

	boolean questionsSequencedBoolean = false;
	boolean synchInMonitorBoolean = false;
	boolean lockWhenFinishedBoolean = false;
	boolean usernameVisibleBoolean = false;
	boolean allowRateQuestionsBoolean = false;
	boolean showOtherAnswersBoolean = false;
	boolean reflectBoolean = false;
	boolean allowRichEditorBoolean = false;
	boolean useSelectLeaderToolOuputBoolean = false;

	if (questionsSequenced != null && questionsSequenced.equalsIgnoreCase("1")) {
	    questionsSequencedBoolean = true;
	}

	if (synchInMonitor != null && synchInMonitor.equalsIgnoreCase("1")) {
	    synchInMonitorBoolean = true;
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

	if (activeModule.equals(QaAppConstants.AUTHORING)) {
	    qaContent.setOnlineInstructions(richTextOnlineInstructions);
	    qaContent.setOfflineInstructions(richTextOfflineInstructions);
	    qaContent.setUsernameVisible(usernameVisibleBoolean);
	    qaContent.setAllowRateAnswers(allowRateQuestionsBoolean);
	    qaContent.setShowOtherAnswers(showOtherAnswersBoolean);
	    qaContent.setQuestionsSequenced(questionsSequencedBoolean);
	    qaContent.setLockWhenFinished(lockWhenFinishedBoolean);
	    qaContent.setSynchInMonitor(synchInMonitorBoolean);
	    qaContent.setReflect(reflectBoolean);
	    qaContent.setReflectionSubject(reflectionSubject);
	    qaContent.setAllowRichEditor(allowRichEditorBoolean);
	    qaContent.setUseSelectLeaderToolOuput(useSelectLeaderToolOuputBoolean);

	}

	qaContent.setConditions(new TreeSet<QaCondition>(new TextSearchConditionComparator()));
	if (newContent) {
	    qaService.createQa(qaContent);
	} else {
	    qaService.updateQa(qaContent);
	}

	qaContent = qaService.getQa(new Long(strToolContentID).longValue());
	qaContent = createQuestionContent(listQuestionContentDTO, qaService, qaContent);

	qaContent = qaService.getQa(new Long(strToolContentID).longValue());

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
	qaService.updateQa(qaContent);
	return qaContent;
    }

    /**
     * removes unused question entries from db
     * 
     * @param mapQuestionContent
     * @param qaService
     * @param qaAuthoringForm
     */
    public void removeRedundantQuestions(List<QaQuestionDTO>listQuestionContentDTO, IQaService qaService, QaAuthoringForm qaAuthoringForm,
	    HttpServletRequest request, String toolContentID) {

	QaContent qaContent = qaService.getQa(new Long(toolContentID).longValue());
	if (qaContent != null) {
	    List allQuestions = qaService.getAllQuestionEntries(qaContent.getUid());

	    Iterator listIterator = allQuestions.iterator();
	    boolean entryUsed = false;
	    while (listIterator.hasNext()) {

		QaQueContent queContent = (QaQueContent) listIterator.next();

		//Checking whether to remove question with id  queContent.getUid()
		entryUsed = false;
		for ( QaQuestionDTO questionDTO : listQuestionContentDTO ) {
		    if (StringUtils.equals(queContent.getQuestion(), questionDTO.getQuestion())) {
			entryUsed = true;
			break;
		    }
		}

		if (entryUsed == false) {

		    QaQueContent removeableQaQueContent = qaService.getQuestionContentByQuestionText(queContent
			    .getQuestion(), qaContent.getUid());
		    if (removeableQaQueContent != null) {
			//Removing question with id removeableQaQueContent.getUid() 
			qaService.removeQaQueContent(removeableQaQueContent);
		    }

		}
	    }
	}

    }

    /**
     * persist the questions in the Map the user has submitted
     * 
     * LDEV-2526 note that questions have already been removed before this method is called, but
     * their displayOrder fields haven't been updated yet.  Note also that the given
     * mapQuestionContent maps question numbers to question strings.
     */
    protected QaContent createQuestionContent(List<QaQuestionDTO> listQuestionContentDTO, IQaService qaService,
	    QaContent qaContent) {
	int displayOrder = 0;
	for ( QaQuestionDTO questionContentDTO : listQuestionContentDTO ) {
	    
	    // LDEV-2526 Assuming here that removed questions exist in mapQuestionContent, but that the value is empty
	    // (this whole thing needs a rewrite).  If empty, do not attempt to persist it.
	    // LDEV-2524 Partial rewrite - removed the old question and feedback maps and just use the original list.
	    // Overriding the displayOrder with a new displayOrder.
	    String questionText = questionContentDTO.getQuestion();
	    if (StringUtils.isNotBlank(questionText)) {
	    
		++displayOrder;
		
		QaQueContent existingQaQueContent = qaService.getQuestionContentByQuestionText(questionText, qaContent.getUid());
		if (existingQaQueContent == null) {
		    QaQueContent queContent = new QaQueContent(questionText, displayOrder, questionContentDTO.getFeedback(),
			    	questionContentDTO.isRequired(), qaContent, null);
		    qaContent.getQaQueContents().add(queContent);
		    queContent.setQaContent(qaContent);

		    qaService.createQaQue(queContent);
		} else {

		    existingQaQueContent.setQuestion(questionText);
		    existingQaQueContent.setFeedback(questionContentDTO.getFeedback());
		    existingQaQueContent.setDisplayOrder(displayOrder);
		    existingQaQueContent.setRequired(questionContentDTO.isRequired());
		    qaService.saveOrUpdateQaQueContent(existingQaQueContent);
		}
	    }
	}

	return qaContent;
    }

    public static boolean checkDuplicateQuestions(List<QaQuestionDTO> listQuestionContentDTO, String newQuestion) {
	for (QaQuestionDTO questionDTO : listQuestionContentDTO ) {
	    if ( questionDTO.getQuestion() != null && questionDTO.getQuestion().equals(newQuestion) ) {
		return true;
	    }
	}
	return false;
    }

    /**
     * sorts the questions by the display order 
     * 
     * @param mapQuestionContent
     * @param qaService
     * @param qaAuthoringForm
     * @param qaContent
     */
    public void reOrganizeDisplayOrder(IQaService qaService, QaAuthoringForm qaAuthoringForm,
	    QaContent qaContent) {
	if (qaContent != null) {
	    List sortedQuestions = qaService.getAllQuestionEntriesSorted(qaContent.getUid().longValue());

	    Iterator listIterator = sortedQuestions.iterator();
	    int displayOrder = 1;
	    while (listIterator.hasNext()) {
		QaQueContent queContent = (QaQueContent) listIterator.next();

		QaQueContent existingQaQueContent = qaService.getQuestionContentByQuestionText(
			queContent.getQuestion(), qaContent.getUid());
		existingQaQueContent.setDisplayOrder(displayOrder);
		qaService.saveOrUpdateQaQueContent(existingQaQueContent);
		displayOrder++;
	    }
	}
    }

    /**
     * checks if any entry is duplicate verifyDuplicatesOptionsMap(Map
     * mapQuestions)
     * 
     * @param mapQuestions
     * @return
     */
    public static boolean verifyDuplicatesOptionsMap(Map mapQuestions) {
	Map originalMap = mapQuestions;
	Map backupMap = mapQuestions;

	int optionCount = 0;
	for (long i = 1; i <= QaAppConstants.MAX_QUESTION_COUNT.longValue(); i++) {
	    String currentOption = (String) originalMap.get(new Long(i).toString());

	    optionCount = 0;
	    for (long j = 1; j <= QaAppConstants.MAX_QUESTION_COUNT.longValue(); j++) {
		String backedOption = (String) backupMap.get(new Long(j).toString());

		if (currentOption != null && backedOption != null) {
		    if (currentOption.equals(backedOption)) {
			optionCount++;
		    }

		    if (optionCount > 1) {
			return false;
		    }
		}
	    }
	}
	return true;
    }
}