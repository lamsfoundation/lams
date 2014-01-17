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

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McCandidateAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McQuestionContentDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
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
public class AuthoringUtil implements McAppConstants {
    static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());

    /**
     * setRadioboxes(McContent mcContent, McAuthoringForm mcAuthoringForm)
     * 
     * set values of radioboxes for an existing content
     * 
     * @param mcContent
     * @param mcAuthoringForm
     */
    protected static void setRadioboxes(McContent mcContent, McAuthoringForm mcAuthoringForm) {
	mcAuthoringForm.setQuestionsSequenced(mcContent.isQuestionsSequenced() ? "1" : "0");
	mcAuthoringForm.setRandomize(mcContent.isRandomize() ? "1" : "0");
	mcAuthoringForm.setDisplayAnswers(mcContent.isDisplayAnswers() ? "1" : "0");
	mcAuthoringForm.setShowMarks(mcContent.isShowMarks() ? "1" : "0");
	mcAuthoringForm.setUseSelectLeaderToolOuput(mcContent.isUseSelectLeaderToolOuput() ? "1" : "0");
	mcAuthoringForm.setPrefixAnswersWithLetters(mcContent.isPrefixAnswersWithLetters() ? "1" : "0");
	mcAuthoringForm.setRetries(mcContent.isRetries() ? "1" : "0");
	mcAuthoringForm.setSln(mcContent.isShowReport() ? "1" : "0");
	mcAuthoringForm.setReflect(mcContent.isReflect() ? "1" : "0");
    }

    /**
     * verifyMapNoEmptyString(Map map)
     * 
     * makes sure there is data in the map
     * 
     * @param map
     * @return boolean
     */
    public static boolean verifyMapNoEmptyString(Map map) {
	Iterator itMap = map.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();

	    if ((pairs.getValue() != null) && (pairs.getValue().toString().length() == 0)) {
		return false;
	    }

	}
	return true;
    }

    /**
     * removes only unused question entries from the db. It keeps the valid entries since they get updated.
     * 
     * cleanupRedundantQuestions(HttpServletRequest request, List existingQuestions, Map mapQuestionsContent, McContent
     * mcContent)
     * 
     * @param request
     * @param existingQuestions
     * @param mapQuestionsContent
     * @param mcContent
     */
    public static void cleanupRedundantQuestions(HttpServletRequest request, List existingQuestions,
	    Map mapQuestionsContent, McContent mcContent, IMcService mcService) {

	/* remove ununsed question entries from the db */
	boolean questionFound = false;
	Iterator itExistingQuestions = existingQuestions.iterator();
	while (itExistingQuestions.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) itExistingQuestions.next();

	    Iterator itNewQuestionsMap = mapQuestionsContent.entrySet().iterator();
	    questionFound = false;
	    while (itNewQuestionsMap.hasNext()) {
		Map.Entry pairs = (Map.Entry) itNewQuestionsMap.next();

		if (mcQueContent.getQuestion().equals(pairs.getValue().toString())) {
		    questionFound = true;
		    break;
		}
	    }

	    if (questionFound == false) {
		String deletableQuestion = mcQueContent.getQuestion();
		// found is false, delete this question
		mcQueContent = mcService.getQuestionContentByQuestionText(deletableQuestion, mcContent.getUid());

		if (mcQueContent != null) {
		    // first removing from collection
		    mcContent.getMcQueContents().remove(mcQueContent);

		    mcService.removeMcQueContent(mcQueContent);
		    // removed mcQueContent from the db
		}
	    }
	}
    }

    /**
     * 
     * rebuildStartupGeneralOptionsContentMapfromDB(HttpServletRequest request, Map mapQuestionsUidContent)
     * 
     * builds a map to hold all the candidate answers for all the questions by accessing the db
     * 
     * @param request
     * @param mapQuestionsUidContent
     * @return
     */
    public static Map rebuildStartupGeneralOptionsContentMapfromDB(HttpServletRequest request,
	    Map mapQuestionsUidContent, IMcService mcService) {
	Map mapStartupGeneralOptionsContent = new TreeMap(new McComparator());

	Iterator itMap = mapQuestionsUidContent.entrySet().iterator();
	Long mapIndex = new Long(1);
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    String currentQuestionUid = pairs.getValue().toString();
	    List listQuestionOptions = mcService.findMcOptionsContentByQueId(new Long(currentQuestionUid));
	    Map mapQuestionOptions = AuthoringUtil.generateOptionsMap(listQuestionOptions);
	    mapStartupGeneralOptionsContent.put(mapIndex.toString(), mapQuestionOptions);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return mapStartupGeneralOptionsContent;
    }

    protected static List removeBlankEntries(List list) {
	List newList = new LinkedList();

	Iterator listIterator = list.iterator();
	while (listIterator.hasNext()) {
	    McCandidateAnswersDTO mcCandidateAnswersDTO = (McCandidateAnswersDTO) listIterator.next();

	    if (mcCandidateAnswersDTO != null) {
		String ca = mcCandidateAnswersDTO.getCandidateAnswer();

		if ((ca != null) && (ca.length() > 0)) {
		    newList.add(mcCandidateAnswersDTO);
		}
	    }
	}

	return newList;
    }

    /**
     * 
     * generateOptionsMap(List listQuestionOptions)
     * 
     * builds a questions map from questions list
     * 
     * @param listQuestionOptions
     * @return Map
     */
    public static Map generateOptionsMap(List listQuestionOptions) {
	Map mapOptsContent = new TreeMap(new McComparator());

	Iterator listIterator = listQuestionOptions.iterator();
	Long mapIndex = new Long(1);
	while (listIterator.hasNext()) {
	    McOptsContent mcOptsContent = (McOptsContent) listIterator.next();
	    mapOptsContent.put(mapIndex.toString(), mcOptsContent.getMcQueOptionText());
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return mapOptsContent;
    }

    /**
     * builds a map to hold question texts rebuildQuestionMapfromDB(HttpServletRequest request, Long toolContentId)
     * 
     * @param request
     * @param toolContentId
     * @return Map
     */
    public static Map rebuildQuestionMapfromDB(HttpServletRequest request, Long toolContentId, IMcService mcService) {
	Map mapQuestionsContent = new TreeMap(new McComparator());

	McContent mcContent = mcService.retrieveMc(toolContentId);

	List list = mcService.refreshQuestionContent(mcContent.getUid());

	Iterator listIterator = list.iterator();
	Long mapIndex = new Long(1);
	while (listIterator.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) listIterator.next();
	    mapQuestionsContent.put(mapIndex.toString(), mcQueContent.getQuestion());
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}

	return mapQuestionsContent;
    }

    public static Map rebuildFeedbackMapfromDB(HttpServletRequest request, Long toolContentId, IMcService mcService) {
	Map map = new TreeMap(new McComparator());

	McContent mcContent = mcService.retrieveMc(toolContentId);

	List list = mcService.refreshQuestionContent(mcContent.getUid());

	Iterator listIterator = list.iterator();
	Long mapIndex = new Long(1);
	while (listIterator.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) listIterator.next();

	    String feedback = mcQueContent.getFeedback();

	    map.put(mapIndex.toString(), feedback);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}

	return map;
    }

    /**
     * builds a map to hold persisted uid values for questions rebuildQuestionUidMapfromDB(HttpServletRequest request,
     * Long toolContentId)
     * 
     * @param request
     * @param toolContentId
     * @return Map
     */
    public static Map rebuildQuestionUidMapfromDB(HttpServletRequest request, Long toolContentId, IMcService mcService) {
	Map mapQuestionsContent = new TreeMap(new McComparator());

	McContent mcContent = mcService.retrieveMc(toolContentId);

	if (mcContent != null) {
	    List list = mcService.refreshQuestionContent(mcContent.getUid());

	    Iterator listIterator = list.iterator();
	    Long mapIndex = new Long(1);
	    while (listIterator.hasNext()) {
		McQueContent mcQueContent = (McQueContent) listIterator.next();
		mapQuestionsContent.put(mapIndex.toString(), mcQueContent.getUid());
		mapIndex = new Long(mapIndex.longValue() + 1);
	    }
	}

	return mapQuestionsContent;
    }

    /**
     * finds whether a candidate answer is selected or not isOptionSelected(Map mapGeneralSelectedOptionsContent, String
     * optionText, String questionIndex)
     * 
     * @param mapGeneralSelectedOptionsContent
     * @param optionText
     * @param questionIndex
     * @return boolean
     */
    public static boolean isOptionSelected(Map mapGeneralSelectedOptionsContent, String optionText, String questionIndex) {
	Iterator itGSOMap = mapGeneralSelectedOptionsContent.entrySet().iterator();
	while (itGSOMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itGSOMap.next();
	    if (pairs.getKey().toString().equals(questionIndex)) {
		Map currentOptionsMap = (Map) pairs.getValue();
		boolean isOptionSelectedInMap = AuthoringUtil.isOptionSelectedInMap(optionText, currentOptionsMap);
		return isOptionSelectedInMap;
	    }
	}
	return false;
    }

    /**
     * checks the existence of a candidate answer in the options map isOptionSelectedInMap(String optionText, Map
     * currentOptionsMap)
     * 
     * @param optionText
     * @param currentOptionsMap
     * @return
     */
    public static boolean isOptionSelectedInMap(String optionText, Map currentOptionsMap) {
	Iterator itCOMap = currentOptionsMap.entrySet().iterator();
	while (itCOMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itCOMap.next();
	    if (pairs.getValue().toString().equals(optionText)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * List swapNodes(List listQuestionContentDTO, String questionIndex, String direction)
     * 
     * swappes McQuestionContentDTO nodes in the list
     * 
     * @param listQuestionContentDTO
     * @param questionIndex
     * @param direction
     * @return
     */
    protected static List swapNodes(List listQuestionContentDTO, String questionIndex, String direction) {

	int intQuestionIndex = new Integer(questionIndex).intValue();
	int intOriginalQuestionIndex = intQuestionIndex;

	int replacedNodeIndex = 0;
	if (direction.equals("down")) {
	    replacedNodeIndex = ++intQuestionIndex;
	} else {
	    replacedNodeIndex = --intQuestionIndex;

	}

	McQuestionContentDTO mainNode = AuthoringUtil.extractNodeAtDisplayOrder(listQuestionContentDTO,
		intOriginalQuestionIndex);

	McQuestionContentDTO replacedNode = AuthoringUtil.extractNodeAtDisplayOrder(listQuestionContentDTO,
		replacedNodeIndex);

	if ((mainNode == null) || (replacedNode == null)) {
	    return listQuestionContentDTO;
	}

	List listFinalQuestionContentDTO = new LinkedList();

	listFinalQuestionContentDTO = AuthoringUtil.reorderSwappedListQuestionContentDTO(listQuestionContentDTO,
		intOriginalQuestionIndex, replacedNodeIndex, mainNode, replacedNode);

	return listFinalQuestionContentDTO;
    }

    /**
     * List reorderSwappedListQuestionContentDTO(List listQuestionContentDTO, int intOriginalQuestionIndex, int
     * replacedNodeIndex, McQuestionContentDTO mainNode, McQuestionContentDTO replacedNode)
     * 
     * @param listQuestionContentDTO
     * @param intOriginalQuestionIndex
     * @param replacedNodeIndex
     * @param mainNode
     * @param replacedNode
     * @return
     */
    protected static List reorderSwappedListQuestionContentDTO(List listQuestionContentDTO,
	    int intOriginalQuestionIndex, int replacedNodeIndex, McQuestionContentDTO mainNode,
	    McQuestionContentDTO replacedNode) {

	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();
	    queIndex++;
	    McQuestionContentDTO tempNode = new McQuestionContentDTO();

	    if ((!mcQuestionContentDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString()))
		    && !mcQuestionContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) {
		// normal cop
		tempNode.setQuestion(mcQuestionContentDTO.getQuestion());
		tempNode.setDisplayOrder(mcQuestionContentDTO.getDisplayOrder());
		tempNode.setFeedback(mcQuestionContentDTO.getFeedback());
		tempNode.setListCandidateAnswersDTO(mcQuestionContentDTO.getListCandidateAnswersDTO());
		tempNode.setCaCount(mcQuestionContentDTO.getCaCount());
		tempNode.setMark(mcQuestionContentDTO.getMark());

	    } else if (mcQuestionContentDTO.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())) {
		// move type 1
		tempNode.setQuestion(replacedNode.getQuestion());
		tempNode.setDisplayOrder(replacedNode.getDisplayOrder());
		tempNode.setFeedback(replacedNode.getFeedback());
		tempNode.setListCandidateAnswersDTO(replacedNode.getListCandidateAnswersDTO());
		tempNode.setCaCount(replacedNode.getCaCount());
		tempNode.setMark(replacedNode.getMark());
	    } else if (mcQuestionContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) {
		// move type 2
		tempNode.setQuestion(mainNode.getQuestion());
		tempNode.setDisplayOrder(mainNode.getDisplayOrder());
		tempNode.setFeedback(mainNode.getFeedback());
		tempNode.setListCandidateAnswersDTO(mainNode.getListCandidateAnswersDTO());
		tempNode.setCaCount(mainNode.getCaCount());
		tempNode.setMark(mainNode.getMark());
	    }

	    listFinalQuestionContentDTO.add(tempNode);
	}

	return listFinalQuestionContentDTO;
    }

    /**
     * List swapCandidateNodes(List listCandidates, String candidateIndex, String direction)
     * 
     * swaps McCandidateAnswersDTO in the list
     * 
     * 
     * @param listCandidates
     * @param candidateIndex
     * @param direction
     * @return
     */
    protected static List swapCandidateNodes(List listCandidates, String candidateIndex, String direction) {

	int intCandidateIndex = new Integer(candidateIndex).intValue();
	int intOriginalCandidateIndex = intCandidateIndex;

	int replacedNodeIndex = 0;
	if (direction.equals("down")) {
	    replacedNodeIndex = ++intCandidateIndex;
	} else {
	    replacedNodeIndex = --intCandidateIndex;

	}

	McCandidateAnswersDTO mainNode = AuthoringUtil.extractCandidateAtOrder(listCandidates,
		intOriginalCandidateIndex);

	McCandidateAnswersDTO replacedNode = AuthoringUtil.extractCandidateAtOrder(listCandidates, replacedNodeIndex);

	if ((mainNode == null) || (replacedNode == null)) {
	    return listCandidates;
	}

	List listFinalCandidateDTO = new LinkedList();

	listFinalCandidateDTO = AuthoringUtil.reorderSwappedListCandidateDTO(listCandidates, intOriginalCandidateIndex,
		replacedNodeIndex, mainNode, replacedNode);

	return listFinalCandidateDTO;
    }

    /**
     * reorderSwappedListCandidateDTO(List listCandidates, int intOriginalCandidateIndex, int replacedNodeIndex,
     * McCandidateAnswersDTO mainNode, McCandidateAnswersDTO replacedNode)
     * 
     * 
     * @param listCandidates
     * @param intOriginalCandidateIndex
     * @param replacedNodeIndex
     * @param mainNode
     * @param replacedNode
     * @return
     */
    protected static List reorderSwappedListCandidateDTO(List listCandidates, int intOriginalCandidateIndex,
	    int replacedNodeIndex, McCandidateAnswersDTO mainNode, McCandidateAnswersDTO replacedNode) {

	List listFinalCandidatesDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listCandidates.iterator();
	while (listIterator.hasNext()) {
	    McCandidateAnswersDTO mcCandidateAnswersDTO = (McCandidateAnswersDTO) listIterator.next();
	    queIndex++;

	    McCandidateAnswersDTO tempNode = new McCandidateAnswersDTO();

	    if ((!new Integer(queIndex).toString().equals(new Integer(intOriginalCandidateIndex).toString()))
		    && !new Integer(queIndex).toString().equals(new Integer(replacedNodeIndex).toString())) {
		// normal copy
		tempNode.setCandidateAnswer(mcCandidateAnswersDTO.getCandidateAnswer());
		tempNode.setCorrect(mcCandidateAnswersDTO.getCorrect());
	    } else if (new Integer(queIndex).toString().equals(new Integer(intOriginalCandidateIndex).toString())) {
		// move type 1
		tempNode.setCandidateAnswer(replacedNode.getCandidateAnswer());
		tempNode.setCorrect(replacedNode.getCorrect());
	    } else if (new Integer(queIndex).toString().equals(new Integer(replacedNodeIndex).toString())) {
		// move type 2
		tempNode.setCandidateAnswer(mainNode.getCandidateAnswer());
		tempNode.setCorrect(mainNode.getCorrect());
	    }

	    listFinalCandidatesDTO.add(tempNode);
	}

	return listFinalCandidatesDTO;
    }

    /**
     * McQuestionContentDTO extractNodeAtDisplayOrder(List listQuestionContentDTO, int intOriginalQuestionIndex)
     * 
     * returns McQuestionContentDTO in the specified order of the list
     * 
     * @param listQuestionContentDTO
     * @param intOriginalQuestionIndex
     * @return
     */
    protected static McQuestionContentDTO extractNodeAtDisplayOrder(List listQuestionContentDTO,
	    int intOriginalQuestionIndex) {

	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    if (new Integer(intOriginalQuestionIndex).toString().equals(mcQuestionContentDTO.getDisplayOrder())) {
		return mcQuestionContentDTO;
	    }
	}
	return null;
    }

    /**
     * McCandidateAnswersDTO extractCandidateAtOrder(List listCandidates, int intOriginalCandidateIndex)
     * 
     * @param listCandidates
     * @param intOriginalCandidateIndex
     * @return
     */
    protected static McCandidateAnswersDTO extractCandidateAtOrder(List listCandidates, int intOriginalCandidateIndex) {

	int counter = 0;
	Iterator listIterator = listCandidates.iterator();
	while (listIterator.hasNext()) {
	    ++counter;
	    McCandidateAnswersDTO mcCandidateAnswerDTO = (McCandidateAnswersDTO) listIterator.next();

	    if (new Integer(intOriginalCandidateIndex).toString().equals(new Integer(counter).toString())) {
		return mcCandidateAnswerDTO;
	    }
	}
	return null;
    }

    protected static String getTotalMark(List listQuestionContentDTO) {

	Map mapMarks = AuthoringUtil.extractMapMarks(listQuestionContentDTO);

	int intTotalMark = 0;
	Iterator itMap = mapMarks.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();

	    String mark = (String) pairs.getValue();

	    if (mark != null) {
		int intMark = new Integer(mark).intValue();
		intTotalMark += intMark;
	    }
	}

	String strFinalTotalMark = new Integer(intTotalMark).toString();
	return strFinalTotalMark;
    }

    /**
     * extractMapQuestionContent(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static Map extractMapQuestionContent(List listQuestionContentDTO) {
	Map mapQuestionContent = new TreeMap(new McComparator());

	Iterator listIterator = listQuestionContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    queIndex++;
	    mapQuestionContent.put(new Integer(queIndex).toString(), mcQuestionContentDTO.getQuestion());
	}
	return mapQuestionContent;
    }

    /**
     * List reorderListCandidatesDTO(List candidates)
     * 
     * @param candidates
     * @return
     */
    protected static List reorderListCandidatesDTO(List candidates) {

	List listFinalCandidatesDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = candidates.iterator();
	while (listIterator.hasNext()) {
	    McCandidateAnswersDTO mcCandidateAnswersDTO = (McCandidateAnswersDTO) listIterator.next();

	    String answer = mcCandidateAnswersDTO.getCandidateAnswer();

	    String correct = mcCandidateAnswersDTO.getCorrect();

	    if ((answer != null) && (!answer.equals(""))) {
		mcCandidateAnswersDTO.setCandidateAnswer(answer);
		mcCandidateAnswersDTO.setCorrect(correct);

		listFinalCandidatesDTO.add(mcCandidateAnswersDTO);

	    }
	}

	return listFinalCandidatesDTO;
    }

    /**
     * reorderListQuestionContentDTO(List listQuestionContentDTO, String excludeQuestionIndex)
     * 
     * @param listQuestionContentDTO
     * @param excludeQuestionIndex
     * @return
     */
    protected static List reorderListQuestionContentDTO(List listQuestionContentDTO, String excludeQuestionIndex) {

	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    String question = mcQuestionContentDTO.getQuestion();

	    String displayOrder = mcQuestionContentDTO.getDisplayOrder();

	    String feedback = mcQuestionContentDTO.getFeedback();

	    String caCount = mcQuestionContentDTO.getCaCount();

	    List caList = mcQuestionContentDTO.getListCandidateAnswersDTO();

	    if ((question != null) && (!question.equals(""))) {
		if (!displayOrder.equals(excludeQuestionIndex)) {
		    ++queIndex;

		    mcQuestionContentDTO.setQuestion(question);
		    mcQuestionContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		    mcQuestionContentDTO.setFeedback(feedback);
		    mcQuestionContentDTO.setListCandidateAnswersDTO(caList);

		    mcQuestionContentDTO.setCaCount(new Integer(mcQuestionContentDTO.getListCandidateAnswersDTO()
			    .size()).toString());

		    listFinalQuestionContentDTO.add(mcQuestionContentDTO);
		}
	    }
	}

	return listFinalQuestionContentDTO;
    }

    /**
     * List reorderSimpleListQuestionContentDTO(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static List reorderSimpleListQuestionContentDTO(List listQuestionContentDTO) {
	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    String question = mcQuestionContentDTO.getQuestion();

	    String displayOrder = mcQuestionContentDTO.getDisplayOrder();

	    String feedback = mcQuestionContentDTO.getFeedback();

	    List caList = mcQuestionContentDTO.getListCandidateAnswersDTO();

	    String caCount = mcQuestionContentDTO.getCaCount();

	    String mark = mcQuestionContentDTO.getMark();

	    if ((question != null) && (!question.equals(""))) {
		++queIndex;

		mcQuestionContentDTO.setQuestion(question);
		mcQuestionContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		mcQuestionContentDTO.setFeedback(feedback);
		mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
		mcQuestionContentDTO.setCaCount(caCount);
		mcQuestionContentDTO.setMark(mark);
		listFinalQuestionContentDTO.add(mcQuestionContentDTO);
	    }
	}

	return listFinalQuestionContentDTO;
    }

    /**
     * reorderUpdateListQuestionContentDTO(List listQuestionContentDTO, McQuestionContentDTO mcQuestionContentDTONew,
     * String editableQuestionIndex)
     * 
     * @param listQuestionContentDTO
     * @param mcQuestionContentDTONew
     * @param editableQuestionIndex
     * @return
     */
    protected static List reorderUpdateListQuestionContentDTO(List listQuestionContentDTO,
	    McQuestionContentDTO mcQuestionContentDTONew, String editableQuestionIndex) {

	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listQuestionContentDTO.iterator();
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    ++queIndex;
	    String question = mcQuestionContentDTO.getQuestion();

	    String displayOrder = mcQuestionContentDTO.getDisplayOrder();

	    String feedback = mcQuestionContentDTO.getFeedback();

	    String mark = mcQuestionContentDTO.getMark();

	    List caList = mcQuestionContentDTO.getListCandidateAnswersDTO();

	    String caCount = mcQuestionContentDTO.getCaCount();

	    if (displayOrder.equals(editableQuestionIndex)) {
		mcQuestionContentDTO.setQuestion(mcQuestionContentDTONew.getQuestion());
		mcQuestionContentDTO.setDisplayOrder(mcQuestionContentDTONew.getDisplayOrder());
		mcQuestionContentDTO.setFeedback(mcQuestionContentDTONew.getFeedback());
		mcQuestionContentDTO.setMark(mcQuestionContentDTONew.getMark());
		mcQuestionContentDTO.setCaCount(mcQuestionContentDTONew.getCaCount());
		mcQuestionContentDTO.setListCandidateAnswersDTO(mcQuestionContentDTONew.getListCandidateAnswersDTO());

		listFinalQuestionContentDTO.add(mcQuestionContentDTO);
	    } else {
		mcQuestionContentDTO.setQuestion(question);
		mcQuestionContentDTO.setDisplayOrder(displayOrder);
		mcQuestionContentDTO.setFeedback(feedback);
		mcQuestionContentDTO.setMark(mark);
		mcQuestionContentDTO.setCaCount(caCount);
		mcQuestionContentDTO.setListCandidateAnswersDTO(caList);
		listFinalQuestionContentDTO.add(mcQuestionContentDTO);

	    }

	}

	return listFinalQuestionContentDTO;
    }

    /**
     * sorts the questions by the display order
     * 
     * reOrganizeDisplayOrder(Map mapQuestionContent, IMcService mcService, McAuthoringForm mcAuthoringForm, McContent
     * mcContent)
     * 
     * @param mapQuestionContent
     * @param mcService
     * @param mcAuthoringForm
     * @param mcContent
     */
    public void reOrganizeDisplayOrder(Map mapQuestionContent, IMcService mcService, McAuthoringForm mcAuthoringForm,
	    McContent mcContent) {
	if (mcContent != null) {
	    List sortedQuestions = mcService.getAllQuestionEntriesSorted(mcContent.getUid().longValue());

	    Iterator listIterator = sortedQuestions.iterator();
	    int displayOrder = 1;
	    while (listIterator.hasNext()) {
		McQueContent queContent = (McQueContent) listIterator.next();

		McQueContent existingMcQueContent = mcService.getQuestionContentByQuestionText(
			queContent.getQuestion(), mcContent.getUid());
		existingMcQueContent.setDisplayOrder(new Integer(displayOrder));
		mcService.updateMcQueContent(existingMcQueContent);
		displayOrder++;
	    }
	}
    }

    /**
     * boolean checkDuplicateQuestions(List listQuestionContentDTO, String newQuestion)
     * 
     * verifies that there are no duplicate questions
     * 
     * @param listQuestionContentDTO
     * @param newQuestion
     * @return
     */
    public static boolean checkDuplicateQuestions(List listQuestionContentDTO, String newQuestion) {

	Map mapQuestionContent = AuthoringUtil.extractMapQuestionContent(listQuestionContentDTO);

	Iterator itMap = mapQuestionContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    if ((pairs.getValue() != null) && (!pairs.getValue().equals(""))) {

		if (pairs.getValue().equals(newQuestion)) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * removes unused question entries from db removeRedundantQuestions (Map mapQuestionContent, IMcService mcService,
     * McAuthoringForm mcAuthoringForm)
     * 
     * @param mapQuestionContent
     * @param mcService
     * @param mcAuthoringForm
     */
    public void removeRedundantQuestions(Map mapQuestionContent, IMcService mcService, McAuthoringForm mcAuthoringForm,
	    HttpServletRequest request, String toolContentID) {

	McContent mcContent = mcService.retrieveMc(new Long(toolContentID));

	if (mcContent != null) {
	    List allQuestions = mcService.getAllQuestionEntries(mcContent.getUid());

	    Iterator listIterator = allQuestions.iterator();
	    int mapIndex = 0;
	    boolean entryUsed = false;
	    while (listIterator.hasNext()) {
		++mapIndex;

		McQueContent queContent = (McQueContent) listIterator.next();

		entryUsed = false;
		Iterator itMap = mapQuestionContent.entrySet().iterator();
		int displayOrder = 0;
		while (itMap.hasNext()) {
		    ++displayOrder;
		    entryUsed = false;
		    Map.Entry pairs = (Map.Entry) itMap.next();

		    if (pairs.getValue().toString().length() != 0) {

			if (mapIndex == displayOrder) {
			    entryUsed = true;
			    break;
			}

		    }
		}

		if (entryUsed == false) {
		    // removing unused entry in db

		    McQueContent removeableMcQueContent = mcService.getQuestionContentByQuestionText(
			    queContent.getQuestion(), mcContent.getUid());
		    if (removeableMcQueContent != null) {
			// mcContent.getMcQueContents().remove(removeableMcQueContent);
			mcService.removeMcQueContent(removeableMcQueContent);
		    }

		}
	    }
	}

    }

    /**
     * Map extractMapFeedback(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static Map extractMapFeedback(List listQuestionContentDTO) {
	Map mapFeedbackContent = new TreeMap(new McComparator());

	Iterator listIterator = listQuestionContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    queIndex++;
	    mapFeedbackContent.put(new Integer(queIndex).toString(), mcQuestionContentDTO.getFeedback());
	}
	return mapFeedbackContent;
    }

    /**
     * Map extractMapWeights(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static Map extractMapWeights(List listQuestionContentDTO) {
	Map mapWeights = new TreeMap(new McComparator());

	Iterator listIterator = listQuestionContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    queIndex++;
	    mapWeights.put(new Integer(queIndex).toString(), mcQuestionContentDTO.getWeight());
	}
	return mapWeights;
    }

    /**
     * Map extractMapMarks(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static Map extractMapMarks(List listQuestionContentDTO) {
	Map mapMarks = new TreeMap(new McComparator());

	Iterator listIterator = listQuestionContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    queIndex++;
	    mapMarks.put(new Integer(queIndex).toString(), mcQuestionContentDTO.getMark());
	}
	return mapMarks;
    }

    /**
     * Map extractMapCandidatesList(List listQuestionContentDTO)
     * 
     * @param listQuestionContentDTO
     * @return
     */
    protected static Map extractMapCandidatesList(List listQuestionContentDTO) {
	Map mapCandidatesList = new TreeMap(new McComparator());

	Iterator listIterator = listQuestionContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = (McQuestionContentDTO) listIterator.next();

	    queIndex++;
	    mapCandidatesList.put(new Integer(queIndex).toString(), mcQuestionContentDTO.getListCandidateAnswersDTO());
	}
	return mapCandidatesList;
    }

    /**
     * saveOrUpdateMcContent(Map mapQuestionContent, Map mapFeedback, Map mapWeights, Map mapMarks, Map
     * mapCandidatesList, IMcService mcService, McAuthoringForm mcAuthoringForm, HttpServletRequest request, McContent
     * mcContent, String strToolContentID)
     * 
     * enables persisting content
     * 
     * @param mapQuestionContent
     * @param mapFeedback
     * @param mapWeights
     * @param mapMarks
     * @param mapCandidatesList
     * @param mcService
     * @param mcAuthoringForm
     * @param request
     * @param mcContent
     * @param strToolContentID
     * @return
     */
    public McContent saveOrUpdateMcContent(Map mapQuestionContent, Map mapFeedback, Map mapWeights, Map mapMarks,
	    Map mapCandidatesList, IMcService mcService, McAuthoringForm mcAuthoringForm, HttpServletRequest request,
	    McContent mcContent, String strToolContentID) {
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);

	String sln = request.getParameter("sln");
	
	String useSelectLeaderToolOuput = request.getParameter("useSelectLeaderToolOuput");
	
	String prefixAnswersWithLetters = request.getParameter("prefixAnswersWithLetters");

	String questionsSequenced = request.getParameter("questionsSequenced");

	String randomize = request.getParameter("randomize");

	String displayAnswers = request.getParameter("displayAnswers");

	String showMarks = request.getParameter("showMarks");

	String retries = request.getParameter("retries");

	String reflect = request.getParameter(McAppConstants.REFLECT);

	String reflectionSubject = request.getParameter(McAppConstants.REFLECTION_SUBJECT);

	String activeModule = request.getParameter(McAppConstants.ACTIVE_MODULE);

	boolean setCommonContent = true;
	if ((sln == null) || (questionsSequenced == null) || (retries == null) || (reflect == null)
		|| (showMarks == null) || (randomize == null)) {
	    setCommonContent = false;
	}

	boolean questionsSequencedBoolean = false;
	boolean randomizeBoolean = false;
	boolean displayAnswersBoolean = false;
	boolean showMarksBoolean = false;
	boolean slnBoolean = false;
	boolean useSelectLeaderToolOuputBoolean = false;
	boolean prefixAnswersWithLettersBoolean = false;
	boolean retriesBoolean = false;
	boolean reflectBoolean = false;

	if ((questionsSequenced != null) && (questionsSequenced.equalsIgnoreCase("1"))) {
	    questionsSequencedBoolean = true;
	}

	if ((randomize != null) && (randomize.equalsIgnoreCase("1"))) {
	    randomizeBoolean = true;
	}

	if ((displayAnswers != null) && (displayAnswers.equalsIgnoreCase("1"))) {
	    displayAnswersBoolean = true;
	}

	if ((showMarks != null) && (showMarks.equalsIgnoreCase("1"))) {
	    showMarksBoolean = true;
	}

	if ((sln != null) && (sln.equalsIgnoreCase("1"))) {
	    slnBoolean = true;
	}
	
	if ((useSelectLeaderToolOuput != null) && (useSelectLeaderToolOuput.equalsIgnoreCase("1"))) {
	    useSelectLeaderToolOuputBoolean = true;
	}
	
	if ((prefixAnswersWithLetters != null) && (prefixAnswersWithLetters.equalsIgnoreCase("1"))) {
	    prefixAnswersWithLettersBoolean = true;
	}

	if ((retries != null) && (retries.equalsIgnoreCase("1"))) {
	    retriesBoolean = true;
	}

	if ((reflect != null) && (reflect.equalsIgnoreCase("1"))) {
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
		// should not reach here
		userId = 0;
	    }
	}

	boolean newContent = false;
	if (mcContent == null) {
	    mcContent = new McContent();
	    newContent = true;
	}

	mcContent.setMcContentId(new Long(strToolContentID));
	mcContent.setTitle(richTextTitle);
	mcContent.setInstructions(richTextInstructions);
	mcContent.setUpdateDate(new Date(System.currentTimeMillis()));
	/** keep updating this one */
	mcContent.setCreatedBy(userId);
	/** make sure we are setting the userId from the User object above */

	String passmark = request.getParameter("passmark");

	if (passmark == null) {
	    passmark = "0";
	}

	if ((passmark != null) && (passmark.equals(" "))) {
	    passmark = "0";
	} else if ((passmark != null) && (passmark.length() == 0)) {
	    passmark = "0";
	}

	AuthoringUtil.logger.debug("activeModule:" + activeModule);
	if (activeModule.equals(McAppConstants.AUTHORING)) {
	    // setting other content values
	    mcContent.setQuestionsSequenced(questionsSequencedBoolean);
	    mcContent.setRandomize(randomizeBoolean);
	    mcContent.setDisplayAnswers(displayAnswersBoolean);
	    mcContent.setShowMarks(showMarksBoolean);
	    mcContent.setRetries(retriesBoolean);
	    mcContent.setShowReport(slnBoolean);
	    mcContent.setUseSelectLeaderToolOuput(useSelectLeaderToolOuputBoolean);
	    mcContent.setPrefixAnswersWithLetters(prefixAnswersWithLettersBoolean);
	    
	    mcContent.setReflect(reflectBoolean);
	    mcContent.setReflectionSubject(reflectionSubject);

	    mcContent.setPassMark(new Integer(passmark));
	}

	if (newContent) {
	    mcService.createMc(mcContent);
	} else {
	    mcService.updateMc(mcContent);
	}

	mcContent = mcService.retrieveMc(new Long(strToolContentID));

	mcContent = createQuestionContent(mapQuestionContent, mapFeedback, mapWeights, mapMarks, mapCandidatesList,
		mcService, mcContent);

	return mcContent;
    }

    /**
     * 
     * McContent createQuestionContent(Map mapQuestionContent, Map mapFeedback, Map mapWeights, Map mapMarks, Map
     * mapCandidatesList, IMcService mcService, McContent mcContent)
     * 
     * persists the questions in the Map the user has submitted
     * 
     * @param mapQuestionContent
     * @param mapFeedback
     * @param mapWeights
     * @param mapMarks
     * @param mapCandidatesList
     * @param mcService
     * @param mcContent
     * @return
     */
    protected McContent createQuestionContent(Map mapQuestionContent, Map mapFeedback, Map mapWeights, Map mapMarks,
	    Map mapCandidatesList, IMcService mcService, McContent mcContent) {
	List questions = mcService.retrieveMcQueContentsByToolContentId(mcContent.getUid().longValue());

	Iterator itMap = mapQuestionContent.entrySet().iterator();
	int displayOrder = 0;
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();

	    if (pairs.getValue().toString().length() != 0) {

		++displayOrder;
		String currentFeedback = (String) mapFeedback.get(new Integer(displayOrder).toString());

		String currentMark = (String) mapMarks.get(new Integer(displayOrder).toString());
		/* set the default mark in case it is not provided */
		if (currentMark == null) {
		    currentMark = "1";
		}

		List caList = (List) mapCandidatesList.get(new Integer(displayOrder).toString());

		McQueContent queContent = new McQueContent(pairs.getValue().toString(), new Integer(displayOrder),
			new Integer(currentMark), currentFeedback, mcContent, null, null);

		/* checks if the question is already recorded */
		McQueContent existingMcQueContent = mcService.getQuestionByDisplayOrder(new Long(displayOrder),
			mcContent.getUid());

		if (existingMcQueContent == null) {
		    /* make sure a question with the same question text is not already saved */
		    McQueContent duplicateMcQueContent = mcService.getQuestionContentByQuestionText(pairs.getValue()
			    .toString(), mcContent.getUid());
		    // adding a new question to content
		    mcContent.getMcQueContents().add(queContent);
		    queContent.setMcContent(mcContent);

		    mcService.createMcQue(queContent);

		    // start persisting candidate answers
		    persistCandidates(caList, queContent, mcService);
		} else {

		    String existingQuestion = existingMcQueContent.getQuestion();

		    existingMcQueContent.setQuestion(pairs.getValue().toString());
		    existingMcQueContent.setFeedback(currentFeedback);
		    existingMcQueContent.setDisplayOrder(new Integer(displayOrder));
		    existingMcQueContent.setMark(new Integer(currentMark));

		    // updating the existing question content
		    mcService.updateMcQueContent(existingMcQueContent);

		    // will be removing redundant candidates
		    mcService.removeMcOptionsContentByQueId(existingMcQueContent.getUid());
		    // start persisting candidates

		    persistCandidates(caList, existingMcQueContent, mcService);
		}
	    }
	}
	return mcContent;
    }

    protected Map buildDynamicPassMarkMap(List listQuestionContentDTO, boolean initialScreen) {

	Map map = new TreeMap(new McComparator());

	String totalMark = AuthoringUtil.getTotalMark(listQuestionContentDTO);

	int intTotalMark = 0;
	if ((totalMark != null) && (totalMark.length() > 0)) {
	    intTotalMark = new Integer(totalMark).intValue();
	}

	Map passMarksMap = buildPassMarkMap(intTotalMark, false);
	return passMarksMap;
    }

    protected Map buildPassMarkMap(int intTotalMark, boolean initialScreen) {

	Map map = new TreeMap(new McComparator());

	if (initialScreen) {
	    return map;
	}

	for (int i = 1; i <= intTotalMark; i++) {
	    map.put(new Integer(i).toString(), new Integer(i).toString());
	}
	return map;
    }

    /**
     * Map buildMarksMap()
     * 
     * @return
     */
    protected Map buildMarksMap() {
	Map map = new TreeMap(new McComparator());

	for (int i = 1; i <= 10; i++) {
	    map.put(new Integer(i).toString(), new Integer(i).toString());
	}
	return map;
    }

    /**
     * Map buildCorrectMap()
     * 
     * @return
     */
    protected Map buildCorrectMap() {
	Map map = new TreeMap(new McComparator());
	map.put(new Integer(2).toString(), "Correct");
	return map;
    }

    /**
     * List repopulateCandidateAnswersBox(HttpServletRequest request, boolean addBlankCa)
     * 
     * @param request
     * @param addBlankCa
     * @return
     */
    protected List repopulateCandidateAnswersBox(HttpServletRequest request, boolean addBlankCa) {

	String correct = request.getParameter("correct");

	/* check this logic again */
	int intCorrect = 0;
	if (correct != null) {
	    intCorrect = new Integer(correct).intValue();
	}

	List listFinalCandidatesDTO = new LinkedList();

	for (int i = 0; i < McAppConstants.MAX_OPTION_COUNT; i++) {
	    String candidate = request.getParameter("ca" + i);

	    String isCorrect = "Incorrect";

	    if (i == intCorrect) {
		isCorrect = "Correct";
	    }

	    if (candidate != null) {
		McCandidateAnswersDTO mcCandidateAnswersDTO = new McCandidateAnswersDTO();
		mcCandidateAnswersDTO.setCandidateAnswer(candidate);
		mcCandidateAnswersDTO.setCorrect(isCorrect);
		listFinalCandidatesDTO.add(mcCandidateAnswersDTO);
	    }
	}

	if (addBlankCa == true) {
	    McCandidateAnswersDTO mcCandidateAnswersDTO = new McCandidateAnswersDTO();
	    mcCandidateAnswersDTO.setCandidateAnswer("");
	    mcCandidateAnswersDTO.setCorrect("Incorrect");
	    listFinalCandidatesDTO.add(mcCandidateAnswersDTO);
	}

	return listFinalCandidatesDTO;
    }

    protected boolean validateCandidateAnswersNotBlank(HttpServletRequest request) {

	for (int i = 0; i < McAppConstants.MAX_OPTION_COUNT; i++) {
	    String candidate = request.getParameter("ca" + i);

	    if ((candidate != null) && (candidate.length() == 0)) {
		// there is at least 1 blank candidate
		return false;
	    }
	}
	return true;
    }

    /**
     * boolean validateSingleCorrectCandidate(List caList)
     * 
     * verifies that there is at least one Correct entry selected
     * 
     * @param caList
     * @return
     */
    protected boolean validateSingleCorrectCandidate(List caList) {
	Iterator itCaList = caList.iterator();
	while (itCaList.hasNext()) {
	    McCandidateAnswersDTO mcCandidateAnswersDTO = (McCandidateAnswersDTO) itCaList.next();

	    String candidateAnswer = mcCandidateAnswersDTO.getCandidateAnswer();
	    String correct = mcCandidateAnswersDTO.getCorrect();

	    if (correct.equals("Correct")) {
		// there is at least one Correct candidate, it is good.
		return true;
	    }
	}

	return false;
    }

    /**
     * buildDefaultQuestionContent(McContent mcContent, IMcService mcService)
     * 
     * generates a list for holding default questions and their candidate answers
     * 
     * @param mcContent
     * @param mcService
     * @return
     */
    protected List buildDefaultQuestionContent(McContent mcContent, IMcService mcService) {
	List listQuestionContentDTO = new LinkedList();

	/*
	 * get the existing question content
	 */
	Iterator queIterator = mcContent.getMcQueContents().iterator();
	Long mapIndex = new Long(1);

	while (queIterator.hasNext()) {
	    McQuestionContentDTO mcQuestionContentDTO = new McQuestionContentDTO();

	    McQueContent mcQueContent = (McQueContent) queIterator.next();
	    if (mcQueContent != null) {

		String feedback = "";
		if (mcQueContent.getFeedback() != null) {
		    feedback = mcQueContent.getFeedback();
		}

		String question = mcQueContent.getQuestion();

		mcQuestionContentDTO.setQuestion(question);
		mcQuestionContentDTO.setDisplayOrder(mcQueContent.getDisplayOrder().toString());
		mcQuestionContentDTO.setFeedback(feedback);
		mcQuestionContentDTO.setMark(mcQueContent.getMark().toString());

		/* get candidates, from here.. */
		List list = mcService.findMcOptionsContentByQueId(mcQueContent.getUid());

		int caCount = list.size();
		mcQuestionContentDTO.setCaCount(new Integer(caCount).toString());

		List listCandidates = new LinkedList();
		Iterator listIterator = list.iterator();
		while (listIterator.hasNext()) {
		    McOptsContent mcOptsContent = (McOptsContent) listIterator.next();
		    McCandidateAnswersDTO mcCandidateAnswersDTO = new McCandidateAnswersDTO();

		    mcCandidateAnswersDTO.setCandidateAnswer(mcOptsContent.getMcQueOptionText());

		    if (mcOptsContent.isCorrectOption()) {
			mcCandidateAnswersDTO.setCorrect("Correct");
		    } else {
			mcCandidateAnswersDTO.setCorrect("Incorrect");
		    }
		    listCandidates.add(mcCandidateAnswersDTO);
		}
		/* get candidates, till here.. */

		mcQuestionContentDTO.setListCandidateAnswersDTO(listCandidates);

		listQuestionContentDTO.add(mcQuestionContentDTO);

		mapIndex = new Long(mapIndex.longValue() + 1);
	    }
	}

	return listQuestionContentDTO;
    }

    /**
     * persistCandidates(List caList, McQueContent mcQueContent, IMcService mcService)
     * 
     * @param caList
     * @param mcQueContent
     * @param mcService
     */
    protected void persistCandidates(List caList, McQueContent mcQueContent, IMcService mcService) {

	int displayOrder = 0;
	Iterator itCaList = caList.iterator();
	while (itCaList.hasNext()) {
	    McCandidateAnswersDTO mcCandidateAnswersDTO = (McCandidateAnswersDTO) itCaList.next();

	    String candidateAnswer = mcCandidateAnswersDTO.getCandidateAnswer();
	    String correct = mcCandidateAnswersDTO.getCorrect();

	    boolean correctOption = false;
	    if (correct.equals("Correct")) {
		correctOption = true;
	    } else {
		correctOption = false;
	    }

	    ++displayOrder;

	    // McOptsContent mcOptsContent = new McOptsContent(correctOption, candidateAnswer, mcQueContent, new
	    // TreeSet());
	    McOptsContent mcOptsContent = new McOptsContent(new Integer(displayOrder), correctOption, candidateAnswer,
		    mcQueContent);

	    mcService.saveMcOptionsContent(mcOptsContent);
	}
    }
}
