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

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQuestionContentDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.util.QaQueContentComparator;
import org.lamsfoundation.lams.tool.qa.util.QaQuestionContentDTOComparator;
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
    static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());

    protected static List swapNodes(List listQuestionContentDTO, String questionIndex, String direction,
	    Set<QaCondition> conditions) {
	AuthoringUtil.logger.debug("swapNodes:");
	AuthoringUtil.logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
	AuthoringUtil.logger.debug("questionIndex:" + questionIndex);
	AuthoringUtil.logger.debug("direction:" + direction);

	int intQuestionIndex = new Integer(questionIndex).intValue();
	int intOriginalQuestionIndex = intQuestionIndex;
	AuthoringUtil.logger.debug("intQuestionIndex:" + intQuestionIndex);

	int replacedNodeIndex = 0;
	if (direction.equals("down")) {
	    AuthoringUtil.logger.debug("direction down:");
	    replacedNodeIndex = ++intQuestionIndex;
	} else {
	    AuthoringUtil.logger.debug("direction up:");
	    replacedNodeIndex = --intQuestionIndex;

	}
	AuthoringUtil.logger.debug("replacedNodeIndex:" + replacedNodeIndex);
	AuthoringUtil.logger.debug("replacing nodes:" + intOriginalQuestionIndex + " and " + replacedNodeIndex);

	QaQuestionContentDTO mainNode = extractNodeAtDisplayOrder(listQuestionContentDTO, intOriginalQuestionIndex);
	AuthoringUtil.logger.debug("mainNode:" + mainNode);

	QaQuestionContentDTO replacedNode = extractNodeAtDisplayOrder(listQuestionContentDTO, replacedNodeIndex);
	AuthoringUtil.logger.debug("replacedNode:" + replacedNode);

	List listFinalQuestionContentDTO = new LinkedList();

	listFinalQuestionContentDTO = reorderSwappedListQuestionContentDTO(listQuestionContentDTO,
		intOriginalQuestionIndex, replacedNodeIndex, mainNode, replacedNode, conditions);

	AuthoringUtil.logger.debug("listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
	return listFinalQuestionContentDTO;
    }

    protected static List reorderSwappedListQuestionContentDTO(List listQuestionContentDTO,
	    int intOriginalQuestionIndex, int replacedNodeIndex, QaQuestionContentDTO mainNode,
	    QaQuestionContentDTO replacedNode, Set<QaCondition> conditions) {
	AuthoringUtil.logger.debug("reorderSwappedListQuestionContentDTO: intOriginalQuestionIndex:"
		+ intOriginalQuestionIndex);
	AuthoringUtil.logger.debug("reorderSwappedListQuestionContentDTO: replacedNodeIndex:" + replacedNodeIndex);
	AuthoringUtil.logger.debug("mainNode: " + mainNode);
	AuthoringUtil.logger.debug("replacedNode: " + replacedNode);

	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionContentDTO qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	    AuthoringUtil.logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());
	    queIndex++;
	    QaQuestionContentDTO tempNode = new QaQuestionContentDTO();

	    if (!qaQuestionContentDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())
		    && !qaQuestionContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) {
		AuthoringUtil.logger.debug("normal copy ");
		tempNode.setQuestion(qaQuestionContentDTO.getQuestion());
		tempNode.setDisplayOrder(qaQuestionContentDTO.getDisplayOrder());
		tempNode.setFeedback(qaQuestionContentDTO.getFeedback());
	    } else if (qaQuestionContentDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())) {
		AuthoringUtil.logger.debug("move type 1 ");
		tempNode.setQuestion(replacedNode.getQuestion());
		tempNode.setDisplayOrder(replacedNode.getDisplayOrder());
		tempNode.setFeedback(replacedNode.getFeedback());
	    } else if (qaQuestionContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) {
		AuthoringUtil.logger.debug("move type 1 ");
		tempNode.setQuestion(mainNode.getQuestion());
		tempNode.setDisplayOrder(mainNode.getDisplayOrder());
		tempNode.setFeedback(mainNode.getFeedback());
	    }

	    listFinalQuestionContentDTO.add(tempNode);
	}
	// references in conditions also need to be changed
	if (conditions != null) {
	    for (QaCondition condition : conditions) {
		SortedSet<QaQuestionContentDTO> newQuestionDTOSet = new TreeSet<QaQuestionContentDTO>(
			new QaQuestionContentDTOComparator());
		for (QaQuestionContentDTO dto : (List<QaQuestionContentDTO>) listFinalQuestionContentDTO) {
		    if (condition.temporaryQuestionDTOSet.contains(dto)) {
			newQuestionDTOSet.add(dto);
		    }
		}
		condition.temporaryQuestionDTOSet = newQuestionDTOSet;
	    }
	}
	AuthoringUtil.logger.debug("final listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
	return listFinalQuestionContentDTO;
    }

    protected static QaQuestionContentDTO extractNodeAtDisplayOrder(List listQuestionContentDTO,
	    int intOriginalQuestionIndex) {
	AuthoringUtil.logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
	AuthoringUtil.logger.debug("intOriginalQuestionIndex:" + intOriginalQuestionIndex);

	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionContentDTO qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	    AuthoringUtil.logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());

	    AuthoringUtil.logger.debug("intOriginalQuestionIndex versus displayOrder:"
		    + new Integer(intOriginalQuestionIndex).toString() + " versus "
		    + qaQuestionContentDTO.getDisplayOrder());
	    if (new Integer(intOriginalQuestionIndex).toString().equals(qaQuestionContentDTO.getDisplayOrder())) {
		AuthoringUtil.logger.debug("node found:" + qaQuestionContentDTO);
		return qaQuestionContentDTO;
	    }
	}
	return null;
    }

    protected static Map extractMapQuestionContent(List listQuestionContentDTO) {
	AuthoringUtil.logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
	Map mapQuestionContent = new TreeMap(new QaComparator());

	Iterator listIterator = listQuestionContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    QaQuestionContentDTO qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	    AuthoringUtil.logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());

	    queIndex++;
	    AuthoringUtil.logger.debug("queIndex:" + queIndex);
	    mapQuestionContent.put(new Integer(queIndex).toString(), qaQuestionContentDTO.getQuestion());
	}
	AuthoringUtil.logger.debug("mapQuestionContent:" + mapQuestionContent);
	return mapQuestionContent;
    }

    protected static Map extractMapFeedback(List listQuestionContentDTO) {
	AuthoringUtil.logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
	Map mapFeedbackContent = new TreeMap(new QaComparator());

	Iterator listIterator = listQuestionContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    QaQuestionContentDTO qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	    AuthoringUtil.logger.debug("qaQuestionContentDTO feedback:" + qaQuestionContentDTO.getFeedback());

	    queIndex++;
	    AuthoringUtil.logger.debug("queIndex:" + queIndex);
	    mapFeedbackContent.put(new Integer(queIndex).toString(), qaQuestionContentDTO.getFeedback());
	}
	AuthoringUtil.logger.debug("mapFeedbackContent:" + mapFeedbackContent);
	return mapFeedbackContent;
    }

    protected static Map reorderQuestionContentMap(Map mapQuestionContent) {
	AuthoringUtil.logger.debug("reorderQuestionContentMap:" + mapQuestionContent);

	Map mapFinalQuestionContent = new TreeMap(new QaComparator());

	int queIndex = 0;
	Iterator itMap = mapQuestionContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    if (pairs.getValue() != null && !pairs.getValue().equals("")) {
		++queIndex;
		AuthoringUtil.logger.debug("using queIndex:" + queIndex);
		mapFinalQuestionContent.put(new Integer(queIndex).toString(), pairs.getValue());

	    }
	}

	AuthoringUtil.logger.debug("final mapFinalQuestionContent:" + mapFinalQuestionContent);
	return mapFinalQuestionContent;
    }

    protected static List reorderListQuestionContentDTO(List listQuestionContentDTO, String excludeQuestionIndex) {
	AuthoringUtil.logger.debug("reorderListQuestionContentDTO");
	AuthoringUtil.logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
	AuthoringUtil.logger.debug("excludeQuestionIndex:" + excludeQuestionIndex);

	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionContentDTO qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	    AuthoringUtil.logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());

	    String question = qaQuestionContentDTO.getQuestion();
	    AuthoringUtil.logger.debug("question:" + question);

	    String displayOrder = qaQuestionContentDTO.getDisplayOrder();
	    AuthoringUtil.logger.debug("displayOrder:" + displayOrder);

	    String feedback = qaQuestionContentDTO.getFeedback();
	    AuthoringUtil.logger.debug("feedback:" + feedback);

	    AuthoringUtil.logger.debug("displayOrder versus excludeQuestionIndex :" + displayOrder + " versus "
		    + excludeQuestionIndex);

	    if (question != null && !question.equals("")) {
		if (!displayOrder.equals(excludeQuestionIndex)) {
		    ++queIndex;
		    AuthoringUtil.logger.debug("using queIndex:" + queIndex);

		    qaQuestionContentDTO.setQuestion(question);
		    qaQuestionContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		    qaQuestionContentDTO.setFeedback(feedback);

		    listFinalQuestionContentDTO.add(qaQuestionContentDTO);
		}
	    }
	}

	AuthoringUtil.logger.debug("final listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
	return listFinalQuestionContentDTO;
    }

    protected static List reorderSimpleListQuestionContentDTO(List listQuestionContentDTO) {
	AuthoringUtil.logger.debug("reorderListQuestionContentDTO");
	AuthoringUtil.logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionContentDTO qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	    AuthoringUtil.logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());

	    String question = qaQuestionContentDTO.getQuestion();
	    AuthoringUtil.logger.debug("question:" + question);

	    String displayOrder = qaQuestionContentDTO.getDisplayOrder();
	    AuthoringUtil.logger.debug("displayOrder:" + displayOrder);

	    String feedback = qaQuestionContentDTO.getFeedback();
	    AuthoringUtil.logger.debug("feedback:" + feedback);

	    if (question != null && !question.equals("")) {
		++queIndex;
		AuthoringUtil.logger.debug("using queIndex:" + queIndex);

		qaQuestionContentDTO.setQuestion(question);
		qaQuestionContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		qaQuestionContentDTO.setFeedback(feedback);

		listFinalQuestionContentDTO.add(qaQuestionContentDTO);
	    }
	}

	AuthoringUtil.logger.debug("final listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
	return listFinalQuestionContentDTO;
    }

    protected static List reorderUpdateListQuestionContentDTO(List listQuestionContentDTO,
	    QaQuestionContentDTO qaQuestionContentDTONew, String editableQuestionIndex) {
	AuthoringUtil.logger.debug("reorderUpdateListQuestionContentDTO");
	AuthoringUtil.logger.debug("listQuestionContentDTO:" + listQuestionContentDTO);
	AuthoringUtil.logger.debug("qaQuestionContentDTONew:" + qaQuestionContentDTONew);
	AuthoringUtil.logger.debug("editableQuestionIndex:" + editableQuestionIndex);

	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    QaQuestionContentDTO qaQuestionContentDTO = (QaQuestionContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("qaQuestionContentDTO:" + qaQuestionContentDTO);
	    AuthoringUtil.logger.debug("qaQuestionContentDTO question:" + qaQuestionContentDTO.getQuestion());

	    ++queIndex;
	    AuthoringUtil.logger.debug("using queIndex:" + queIndex);
	    String question = qaQuestionContentDTO.getQuestion();
	    AuthoringUtil.logger.debug("question:" + question);

	    String displayOrder = qaQuestionContentDTO.getDisplayOrder();
	    AuthoringUtil.logger.debug("displayOrder:" + displayOrder);

	    String feedback = qaQuestionContentDTO.getFeedback();
	    AuthoringUtil.logger.debug("feedback:" + feedback);

	    if (displayOrder.equals(editableQuestionIndex)) {
		AuthoringUtil.logger.debug("displayOrder equals editableQuestionIndex:" + editableQuestionIndex);
		qaQuestionContentDTO.setQuestion(qaQuestionContentDTONew.getQuestion());
		qaQuestionContentDTO.setDisplayOrder(qaQuestionContentDTONew.getDisplayOrder());
		qaQuestionContentDTO.setFeedback(qaQuestionContentDTONew.getFeedback());

		listFinalQuestionContentDTO.add(qaQuestionContentDTO);
	    } else {
		AuthoringUtil.logger
			.debug("displayOrder does not equal editableQuestionIndex:" + editableQuestionIndex);
		qaQuestionContentDTO.setQuestion(question);
		qaQuestionContentDTO.setDisplayOrder(displayOrder);
		qaQuestionContentDTO.setFeedback(feedback);

		listFinalQuestionContentDTO.add(qaQuestionContentDTO);

	    }

	}

	AuthoringUtil.logger.debug("listFinalQuestionContentDTO:" + listFinalQuestionContentDTO);
	return listFinalQuestionContentDTO;
    }

    /**
     * repopulateMap(TreeMap mapQuestionContent, HttpServletRequest request) return void repopulates the user entries
     * into the Map
     */
    protected void repopulateMap(Map mapQuestionContent, HttpServletRequest request) {
	AuthoringUtil.logger.debug("starting repopulateMap");
	int intQuestionIndex = mapQuestionContent.size();
	AuthoringUtil.logger.debug("intQuestionIndex: " + intQuestionIndex);

	/*
	 * if there is data in the Map remaining from previous session remove those
	 */
	mapQuestionContent.clear();
	AuthoringUtil.logger.debug("Map got initialized: " + mapQuestionContent);

	for (long i = 0; i < intQuestionIndex; i++) {
	    String candidateQuestionEntry = request.getParameter("questionContent" + i);
	    if (i == 0) {
		AuthoringUtil.logger.debug("defaultQuestionContent set to: " + candidateQuestionEntry);
	    }
	    if (candidateQuestionEntry != null && candidateQuestionEntry.length() > 0) {
		AuthoringUtil.logger.debug("using key: " + i);
		mapQuestionContent.put(new Long(i + 1).toString(), candidateQuestionEntry);
		AuthoringUtil.logger.debug("added new entry.");
	    }
	}
    }

    public QaContent saveOrUpdateQaContent(Map mapQuestionContent, Map mapFeedback, IQaService qaService,
	    QaAuthoringForm qaAuthoringForm, HttpServletRequest request, QaContent qaContent, String strToolContentID,
	    Set<QaCondition> conditions) {
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	String richTextTitle = request.getParameter(QaAppConstants.TITLE);
	String richTextInstructions = request.getParameter(QaAppConstants.INSTRUCTIONS);

	AuthoringUtil.logger.debug("richTextTitle: " + richTextTitle);
	AuthoringUtil.logger.debug("richTextInstructions: " + richTextInstructions);

	String synchInMonitor = request.getParameter(QaAppConstants.SYNC_IN_MONITOR);
	AuthoringUtil.logger.debug("synchInMonitor: " + synchInMonitor);

	String usernameVisible = request.getParameter(QaAppConstants.USERNAME_VISIBLE);
	AuthoringUtil.logger.debug("usernameVisible: " + usernameVisible);

	String showOtherAnswers = request.getParameter("showOtherAnswers");
	AuthoringUtil.logger.debug("showOtherAnswers: " + showOtherAnswers);

	String questionsSequenced = request.getParameter(QaAppConstants.QUESTIONS_SEQUENCED);
	AuthoringUtil.logger.debug("questionsSequenced: " + questionsSequenced);

	String lockWhenFinished = request.getParameter("lockWhenFinished");
	AuthoringUtil.logger.debug("lockWhenFinished: " + lockWhenFinished);

	String richTextOfflineInstructions = request.getParameter(QaAppConstants.OFFLINE_INSTRUCTIONS);
	String richTextOnlineInstructions = request.getParameter(QaAppConstants.ONLINE_INSTRUCTIONS);
	String reflect = request.getParameter(QaAppConstants.REFLECT);
	AuthoringUtil.logger.debug("reflect: " + reflect);

	String reflectionSubject = request.getParameter(QaAppConstants.REFLECTION_SUBJECT);
	AuthoringUtil.logger.debug("reflectionSubject: " + reflectionSubject);

	String activeModule = request.getParameter(QaAppConstants.ACTIVE_MODULE);
	AuthoringUtil.logger.debug("activeModule: " + activeModule);

	boolean setCommonContent = true;
	if (questionsSequenced == null || synchInMonitor == null || lockWhenFinished == null || usernameVisible == null
		|| reflect == null || showOtherAnswers == null) {
	    setCommonContent = false;
	}
	AuthoringUtil.logger.debug("setCommonContent: " + setCommonContent);

	boolean questionsSequencedBoolean = false;
	boolean synchInMonitorBoolean = false;
	boolean lockWhenFinishedBoolean = false;
	boolean usernameVisibleBoolean = false;
	boolean showOtherAnswersBoolean = false;
	boolean reflectBoolean = false;

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

	if (showOtherAnswers != null && showOtherAnswers.equalsIgnoreCase("1")) {
	    showOtherAnswersBoolean = true;
	}

	if (reflect != null && reflect.equalsIgnoreCase("1")) {
	    reflectBoolean = true;
	}

	AuthoringUtil.logger.debug("questionsSequencedBoolean: " + questionsSequencedBoolean);
	AuthoringUtil.logger.debug("synchInMonitorBoolean: " + synchInMonitorBoolean);
	AuthoringUtil.logger.debug("lockWhenFinishedBoolean: " + lockWhenFinishedBoolean);
	AuthoringUtil.logger.debug("usernameVisibleBoolean: " + usernameVisibleBoolean);
	AuthoringUtil.logger.debug("showOtherAnswersBoolean: " + showOtherAnswersBoolean);
	AuthoringUtil.logger.debug("reflectBoolean: " + reflectBoolean);

	long userId = 0;
	if (toolUser != null) {
	    userId = toolUser.getUserID().longValue();
	} else {
	    HttpSession ss = SessionManager.getSession();
	    AuthoringUtil.logger.debug("ss: " + ss);
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    AuthoringUtil.logger.debug("user" + user);
	    if (user != null) {
		userId = user.getUserID().longValue();
	    } else {
		AuthoringUtil.logger.debug("should not reach here");
		userId = 0;
	    }
	}
	AuthoringUtil.logger.debug("userId: " + userId);
	AuthoringUtil.logger.debug("qaContent: " + qaContent);

	boolean newContent = false;
	if (qaContent == null) {
	    qaContent = new QaContent();
	    newContent = true;
	}

	AuthoringUtil.logger.debug("setting common content values..." + richTextTitle + " " + richTextInstructions);
	qaContent.setQaContentId(new Long(strToolContentID));
	qaContent.setTitle(richTextTitle);
	qaContent.setInstructions(richTextInstructions);
	qaContent.setUpdateDate(new Date(System.currentTimeMillis()));
	/** keep updating this one */
	AuthoringUtil.logger.debug("userId: " + userId);
	qaContent.setCreatedBy(userId);
	/** make sure we are setting the userId from the User object above */
	AuthoringUtil.logger.debug("end of setting common content values...");

	AuthoringUtil.logger.debug("activeModule:" + activeModule);
	if (activeModule.equals(QaAppConstants.AUTHORING)) {
	    AuthoringUtil.logger.debug("setting other content values...");
	    qaContent.setOnlineInstructions(richTextOnlineInstructions);
	    qaContent.setOfflineInstructions(richTextOfflineInstructions);
	    qaContent.setUsernameVisible(usernameVisibleBoolean);
	    qaContent.setShowOtherAnswers(showOtherAnswersBoolean);
	    qaContent.setQuestionsSequenced(questionsSequencedBoolean);
	    qaContent.setLockWhenFinished(lockWhenFinishedBoolean);
	    qaContent.setSynchInMonitor(synchInMonitorBoolean);
	    qaContent.setReflect(reflectBoolean);
	    qaContent.setReflectionSubject(reflectionSubject);

	}

	qaContent.setConditions(new TreeSet<QaCondition>(new TextSearchConditionComparator()));
	if (newContent) {
	    AuthoringUtil.logger.debug("will create: " + qaContent);
	    qaService.createQa(qaContent);
	} else {
	    AuthoringUtil.logger.debug("will update: " + qaContent);
	    qaService.updateQa(qaContent);
	}

	qaContent = qaService.loadQa(new Long(strToolContentID).longValue());
	AuthoringUtil.logger.debug("qaContent: " + qaContent);
	qaContent = createQuestionContent(mapQuestionContent, mapFeedback, qaService, qaContent, conditions);

	qaContent = qaService.loadQa(new Long(strToolContentID).longValue());

	for (QaCondition condition : conditions) {
	    condition.setQuestions(new TreeSet<QaQueContent>(new QaQueContentComparator()));
	    for (QaQuestionContentDTO dto : condition.temporaryQuestionDTOSet) {
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
     * removes unused question entries from db removeRedundantQuestions (Map mapQuestionContent, IQaService qaService,
     * QaAuthoringForm qaAuthoringForm)
     * 
     * @param mapQuestionContent
     * @param qaService
     * @param qaAuthoringForm
     */
    public void removeRedundantQuestions(Map mapQuestionContent, IQaService qaService, QaAuthoringForm qaAuthoringForm,
	    HttpServletRequest request, String toolContentID) {
	AuthoringUtil.logger.debug("removing unused entries... ");
	AuthoringUtil.logger.debug("mapQuestionContent:  " + mapQuestionContent);
	AuthoringUtil.logger.debug("toolContentID:  " + toolContentID);

	QaContent qaContent = qaService.loadQa(new Long(toolContentID).longValue());
	AuthoringUtil.logger.debug("qaContent:  " + qaContent);

	if (qaContent != null) {
	    AuthoringUtil.logger.debug("qaContent uid: " + qaContent.getUid());
	    List allQuestions = qaService.getAllQuestionEntries(qaContent.getUid());
	    AuthoringUtil.logger.debug("allQuestions: " + allQuestions);

	    Iterator listIterator = allQuestions.iterator();
	    int mapIndex = 0;
	    boolean entryUsed = false;
	    while (listIterator.hasNext()) {
		++mapIndex;
		AuthoringUtil.logger.debug("current mapIndex: " + mapIndex);

		QaQueContent queContent = (QaQueContent) listIterator.next();
		AuthoringUtil.logger.debug("queContent data: " + queContent);
		AuthoringUtil.logger.debug("queContent: " + queContent.getQuestion() + " "
			+ queContent.getDisplayOrder());

		entryUsed = false;
		Iterator itMap = mapQuestionContent.entrySet().iterator();
		int displayOrder = 0;
		while (itMap.hasNext()) {
		    ++displayOrder;
		    AuthoringUtil.logger.debug("current displayOrder: " + displayOrder);
		    entryUsed = false;
		    Map.Entry pairs = (Map.Entry) itMap.next();
		    AuthoringUtil.logger.debug("using the pair: " + pairs.getKey() + " = " + pairs.getValue());

		    if (pairs.getValue().toString().length() != 0) {
			AuthoringUtil.logger.debug("text from map:" + pairs.getValue().toString());
			AuthoringUtil.logger.debug("text from db:" + queContent.getQuestion());

			AuthoringUtil.logger.debug("mapIndex versus displayOrder:" + mapIndex + " versus "
				+ displayOrder);
			if (mapIndex == displayOrder) {
			    // logger.debug("used entry in db:" +
			    // queContent.getQuestion());
			    AuthoringUtil.logger.debug("used displayOrder position:" + displayOrder);
			    entryUsed = true;
			    break;
			}

		    }
		}

		if (entryUsed == false) {
		    AuthoringUtil.logger.debug("removing unused entry in db:" + queContent.getQuestion());

		    QaQueContent removeableQaQueContent = qaService.getQuestionContentByQuestionText(queContent
			    .getQuestion(), qaContent.getUid());
		    AuthoringUtil.logger.debug("removeableQaQueContent" + removeableQaQueContent);
		    if (removeableQaQueContent != null) {
			// qaContent.getQaQueContents().remove(removeableQaQueContent);
			qaService.removeQaQueContent(removeableQaQueContent);
			AuthoringUtil.logger.debug("removed removeableQaQueContent from the db: "
				+ removeableQaQueContent);
		    }

		}
	    }
	}

    }

    /**
     * createQuestionContent(TreeMap mapQuestionContent, HttpServletRequest request) return void
     * 
     * persist the questions in the Map the user has submitted
     */
    protected QaContent createQuestionContent(Map mapQuestionContent, Map mapFeedback, IQaService qaService,
	    QaContent qaContent, Set<QaCondition> conditions) {
	AuthoringUtil.logger.debug("createQuestionContent: ");
	AuthoringUtil.logger.debug("content uid is: " + qaContent.getUid());
	List questions = qaService.retrieveQaQueContentsByToolContentId(qaContent.getUid().longValue());
	AuthoringUtil.logger.debug("questions: " + questions);

	AuthoringUtil.logger.debug("mapQuestionContent: " + mapQuestionContent);
	AuthoringUtil.logger.debug("mapFeedback: " + mapFeedback);

	Iterator itMap = mapQuestionContent.entrySet().iterator();
	int displayOrder = 0;
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    AuthoringUtil.logger.debug("using the pair: " + pairs.getKey() + " = " + pairs.getValue());

	    if (pairs.getValue().toString().length() != 0) {
		AuthoringUtil.logger.debug("starting createQuestionContent: pairs.getValue().toString():"
			+ pairs.getValue().toString());
		AuthoringUtil.logger.debug("starting createQuestionContent: qaContent: " + qaContent);

		++displayOrder;
		AuthoringUtil.logger.debug("starting createQuestionContent: displayOrder: " + displayOrder);
		String currentFeedback = (String) mapFeedback.get(new Integer(displayOrder).toString());
		AuthoringUtil.logger.debug("currentFeedback: " + currentFeedback);

		QaQueContent queContent = new QaQueContent(pairs.getValue().toString(), displayOrder, currentFeedback,
			qaContent, null, null);

		AuthoringUtil.logger.debug("queContent: " + queContent);

		/* checks if the question is already recorded */
		AuthoringUtil.logger.debug("question text is: " + pairs.getValue().toString());
		AuthoringUtil.logger.debug("content uid is: " + qaContent.getUid());
		AuthoringUtil.logger.debug("question display order is: " + displayOrder);
		QaQueContent existingQaQueContent = qaService.getQuestionContentByDisplayOrder(new Long(displayOrder),
			qaContent.getUid());
		AuthoringUtil.logger.debug("existingQaQueContent: " + existingQaQueContent);

		if (existingQaQueContent == null) {
		    /*
		     * make sure a question with the same question text is not already saved
		     */
		    QaQueContent duplicateQaQueContent = qaService.getQuestionContentByQuestionText(pairs.getValue()
			    .toString(), qaContent.getUid());
		    AuthoringUtil.logger.debug("duplicateQaQueContent: " + duplicateQaQueContent);
		    // if (duplicateQaQueContent == null)
		    // {
		    AuthoringUtil.logger.debug("adding a new question to content: " + queContent);
		    qaContent.getQaQueContents().add(queContent);
		    queContent.setQaContent(qaContent);

		    qaService.createQaQue(queContent);

		    // }
		} else {

		    String existingQuestion = existingQaQueContent.getQuestion();
		    AuthoringUtil.logger.debug("existingQuestion: " + existingQuestion);

		    AuthoringUtil.logger.debug("map question versus existingQuestion: " + pairs.getValue().toString()
			    + " versus db question value: " + existingQuestion);

		    existingQaQueContent.setQuestion(pairs.getValue().toString());
		    existingQaQueContent.setFeedback(currentFeedback);
		    existingQaQueContent.setDisplayOrder(displayOrder);

		    AuthoringUtil.logger.debug("updating the existing question content: " + existingQaQueContent);
		    qaService.saveOrUpdateQaQueContent(existingQaQueContent);
		}
	    }
	}

	return qaContent;
    }

    public static boolean checkDuplicateQuestions(List listQuestionContentDTO, String newQuestion) {
	AuthoringUtil.logger.debug("checkDuplicateQuestions: " + listQuestionContentDTO);
	AuthoringUtil.logger.debug("newQuestion: " + newQuestion);

	Map mapQuestionContent = extractMapQuestionContent(listQuestionContentDTO);
	AuthoringUtil.logger.debug("mapQuestionContent: " + mapQuestionContent);

	Iterator itMap = mapQuestionContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    if (pairs.getValue() != null && !pairs.getValue().equals("")) {
		AuthoringUtil.logger.debug("checking the  pair: " + pairs.getKey() + " = " + pairs.getValue());

		if (pairs.getValue().equals(newQuestion)) {
		    AuthoringUtil.logger.debug("entry found: " + newQuestion);
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * sorts the questions by the display order reOrganizeDisplayOrder(Map mapQuestionContent, IQaService qaService,
     * QaAuthoringForm qaAuthoringForm, QaContent qaContent)
     * 
     * @param mapQuestionContent
     * @param qaService
     * @param qaAuthoringForm
     * @param qaContent
     */
    public void reOrganizeDisplayOrder(Map mapQuestionContent, IQaService qaService, QaAuthoringForm qaAuthoringForm,
	    QaContent qaContent) {
	AuthoringUtil.logger.debug("qaContent: " + qaContent);
	if (qaContent != null) {
	    AuthoringUtil.logger.debug("content uid: " + qaContent.getUid());
	    List sortedQuestions = qaService.getAllQuestionEntriesSorted(qaContent.getUid().longValue());
	    AuthoringUtil.logger.debug("sortedQuestions: " + sortedQuestions);

	    Iterator listIterator = sortedQuestions.iterator();
	    int displayOrder = 1;
	    while (listIterator.hasNext()) {
		QaQueContent queContent = (QaQueContent) listIterator.next();
		AuthoringUtil.logger.debug("queContent data: " + queContent);
		AuthoringUtil.logger.debug("queContent: " + queContent.getQuestion() + " "
			+ queContent.getDisplayOrder());

		QaQueContent existingQaQueContent = qaService.getQuestionContentByQuestionText(
			queContent.getQuestion(), qaContent.getUid());
		AuthoringUtil.logger.debug("existingQaQueContent: " + existingQaQueContent);
		existingQaQueContent.setDisplayOrder(displayOrder);
		AuthoringUtil.logger.debug("updating the existing question content for displayOrder: "
			+ existingQaQueContent);
		qaService.saveOrUpdateQaQueContent(existingQaQueContent);
		displayOrder++;
	    }
	}
	AuthoringUtil.logger.debug("done with reOrganizeDisplayOrder...");
    }

    /**
     * checks if any entry is duplicate verifyDuplicatesOptionsMap(Map mapQuestions)
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