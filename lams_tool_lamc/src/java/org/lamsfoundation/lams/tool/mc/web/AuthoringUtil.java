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

package org.lamsfoundation.lams.tool.mc.web;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Keeps all operations needed for Authoring mode.
 *
 * @author Ozgur Demirtas
 */
public class AuthoringUtil implements McAppConstants {
    private static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());

    public static List<McOptionDTO> removeBlankOptions(List<McOptionDTO> optionDtos) {
	List<McOptionDTO> newList = new LinkedList<McOptionDTO>();

	for (McOptionDTO optionDTO : optionDtos) {
	    String optionText = optionDTO.getCandidateAnswer();

	    if ((optionText != null) && (optionText.length() > 0)) {
		newList.add(optionDTO);
	    }
	}

	return newList;
    }

    /**
     * swappes McQuestionDTO questions in the list
     */
    public static List<McQuestionDTO> swapQuestions(List<McQuestionDTO> questionDTOs, String questionIndex,
	    String direction) {

	int intQuestionIndex = new Integer(questionIndex).intValue();
	int intOriginalQuestionIndex = intQuestionIndex;

	int replacedQuestionIndex = 0;
	if (direction.equals("down")) {
	    replacedQuestionIndex = ++intQuestionIndex;
	} else {
	    replacedQuestionIndex = --intQuestionIndex;
	}

	McQuestionDTO mainQuestion = AuthoringUtil.getQuestionAtDisplayOrder(questionDTOs, intOriginalQuestionIndex);

	McQuestionDTO replacedQuestion = AuthoringUtil.getQuestionAtDisplayOrder(questionDTOs, replacedQuestionIndex);

	if ((mainQuestion == null) || (replacedQuestion == null)) {
	    return questionDTOs;
	}

	List<McQuestionDTO> newQuestionDtos = new LinkedList<McQuestionDTO>();

	int queIndex = 0;
	Iterator<McQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = iter.next();
	    queIndex++;
	    McQuestionDTO tempQuestion = new McQuestionDTO();

	    if ((!questionDto.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString()))
		    && !questionDto.getDisplayOrder().equals(new Integer(replacedQuestionIndex).toString())) {
		// normal copy
		tempQuestion = questionDto;

	    } else if (questionDto.getDisplayOrder().equals(new Integer(intOriginalQuestionIndex).toString())) {
		// move type 1
		tempQuestion = replacedQuestion;

	    } else if (questionDto.getDisplayOrder().equals(new Integer(replacedQuestionIndex).toString())) {
		// move type 2
		tempQuestion = mainQuestion;
	    }

	    newQuestionDtos.add(tempQuestion);
	}

	return newQuestionDtos;
    }

    /**
     * swaps options in the list
     */
    public static List<McOptionDTO> swapOptions(List<McOptionDTO> optionDtos, String optionIndex, String direction) {

	int intOptionIndex = new Integer(optionIndex).intValue();
	int intOriginalOptionIndex = intOptionIndex;

	int replacedOptionIndex = 0;
	if (direction.equals("down")) {
	    replacedOptionIndex = ++intOptionIndex;
	} else {
	    replacedOptionIndex = --intOptionIndex;
	}

	McOptionDTO mainOption = AuthoringUtil.getOptionAtDisplayOrder(optionDtos, intOriginalOptionIndex);

	McOptionDTO replacedOption = AuthoringUtil.getOptionAtDisplayOrder(optionDtos, replacedOptionIndex);

	if ((mainOption == null) || (replacedOption == null)) {
	    return optionDtos;
	}

	List<McOptionDTO> newOptionDtos = new LinkedList<McOptionDTO>();

	int queIndex = 1;
	for (McOptionDTO option : optionDtos) {

	    McOptionDTO tempOption = new McOptionDTO();
	    if ((!new Integer(queIndex).toString().equals(new Integer(intOriginalOptionIndex).toString()))
		    && !new Integer(queIndex).toString().equals(new Integer(replacedOptionIndex).toString())) {
		// normal copy
		tempOption = option;
	    } else if (new Integer(queIndex).toString().equals(new Integer(intOriginalOptionIndex).toString())) {
		// move type 1
		tempOption = replacedOption;
	    } else if (new Integer(queIndex).toString().equals(new Integer(replacedOptionIndex).toString())) {
		// move type 2
		tempOption = mainOption;
	    }

	    newOptionDtos.add(tempOption);
	    queIndex++;
	}

	return newOptionDtos;
    }

    /**
     * returns McQuestionDTO in the specified order of the list
     */
    public static McQuestionDTO getQuestionAtDisplayOrder(List questionDTOs, int intOriginalQuestionIndex) {

	Iterator iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = (McQuestionDTO) iter.next();

	    if (new Integer(intOriginalQuestionIndex).toString().equals(questionDto.getDisplayOrder())) {
		return questionDto;
	    }
	}
	return null;
    }

    /**
     * extractCandidateAtOrder
     */
    public static McOptionDTO getOptionAtDisplayOrder(List options, int intOriginalCandidateIndex) {

	int counter = 0;
	Iterator iter = options.iterator();
	while (iter.hasNext()) {
	    ++counter;
	    McOptionDTO optionDto = (McOptionDTO) iter.next();

	    if (new Integer(intOriginalCandidateIndex).toString().equals(new Integer(counter).toString())) {
		return optionDto;
	    }
	}
	return null;
    }

    public static int getTotalMark(List<McQuestionDTO> questionDTOs) {

	Iterator<McQuestionDTO> iter = questionDTOs.iterator();
	int totalMark = 0;
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = iter.next();

	    String mark = questionDto.getMark();

	    if (StringUtils.isNotBlank(mark)) {
		int intMark = new Integer(mark).intValue();
		totalMark += intMark;
	    }
	}

	return totalMark;
    }

    /**
     * reorderSimpleListQuestionContentDTO
     */
    public static List<McQuestionDTO> reorderSimpleQuestionDtos(List<McQuestionDTO> questionDTOs) {
	List<McQuestionDTO> listFinalQuestionContentDTO = new LinkedList<McQuestionDTO>();

	int queIndex = 0;
	Iterator<McQuestionDTO> iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = iter.next();

	    String question = questionDto.getQuestion();

	    String feedback = questionDto.getFeedback();

	    List<McOptionDTO> optionDtos = questionDto.getListCandidateAnswersDTO();

	    String mark = questionDto.getMark();

	    if ((question != null) && (!question.equals(""))) {
		++queIndex;

		questionDto.setQuestion(question);
		questionDto.setDisplayOrder(new Integer(queIndex).toString());
		questionDto.setFeedback(feedback);
		questionDto.setListCandidateAnswersDTO(optionDtos);
		questionDto.setMark(mark);
		listFinalQuestionContentDTO.add(questionDto);
	    }
	}

	return listFinalQuestionContentDTO;
    }

    /**
     * reorderUpdateListQuestionContentDTO
     */
    public static List reorderUpdateQuestionDtos(List questionDTOs, McQuestionDTO mcQuestionContentDTONew,
	    String editableQuestionIndex) {

	List listFinalQuestionContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator iter = questionDTOs.iterator();
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = (McQuestionDTO) iter.next();

	    ++queIndex;
	    String question = questionDto.getQuestion();

	    String displayOrder = questionDto.getDisplayOrder();

	    String feedback = questionDto.getFeedback();

	    String mark = questionDto.getMark();

	    List<McOptionDTO> optionDtos = questionDto.getListCandidateAnswersDTO();

	    if (displayOrder.equals(editableQuestionIndex)) {
		questionDto.setQuestion(mcQuestionContentDTONew.getQuestion());
		questionDto.setDisplayOrder(mcQuestionContentDTONew.getDisplayOrder());
		questionDto.setFeedback(mcQuestionContentDTONew.getFeedback());
		questionDto.setMark(mcQuestionContentDTONew.getMark());
		questionDto.setListCandidateAnswersDTO(mcQuestionContentDTONew.getListCandidateAnswersDTO());

		listFinalQuestionContentDTO.add(questionDto);
	    } else {
		questionDto.setQuestion(question);
		questionDto.setDisplayOrder(displayOrder);
		questionDto.setFeedback(feedback);
		questionDto.setMark(mark);
		questionDto.setListCandidateAnswersDTO(optionDtos);
		listFinalQuestionContentDTO.add(questionDto);

	    }

	}

	return listFinalQuestionContentDTO;
    }

    /**
     * verifies that there are no duplicate questions
     */
    public static boolean checkDuplicateQuestions(List<McQuestionDTO> questionDTOs, String newQuestion) {

	Map<String, String> mapQuestionContent = new TreeMap<String, String>(new McComparator());
	Iterator<McQuestionDTO> iter = questionDTOs.iterator();
	int queIndex = 0;
	while (iter.hasNext()) {
	    McQuestionDTO questionDto = iter.next();

	    queIndex++;
	    mapQuestionContent.put(new Integer(queIndex).toString(), questionDto.getQuestion());
	}

	Iterator<Map.Entry<String, String>> itMap = mapQuestionContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Entry<String, String> pairs = itMap.next();
	    if ((pairs.getValue() != null) && (!pairs.getValue().equals(""))) {

		if (pairs.getValue().equals(newQuestion)) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * persisting content
     */
    public static McContent saveOrUpdateMcContent(IMcService mcService, HttpServletRequest request, McContent mcContent,
	    String strToolContentID, List<McQuestionDTO> questionDTOs) {
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

	String passmarkStr = request.getParameter("passmark");
	//nullify passmark in case 'retries' option is OFF
	if (StringUtils.isBlank(passmarkStr) || !retriesBoolean) {
	    passmarkStr = "0";
	}
	//passmark can't be more than total mark
	Integer passmark = new Integer(passmarkStr);
	int totalMark = AuthoringUtil.getTotalMark(questionDTOs);
	if (totalMark < passmark) {
	    passmark = totalMark;
	}
	mcContent.setPassMark(passmark);

	if (newContent) {
	    mcService.createMc(mcContent);
	} else {
	    mcService.updateMc(mcContent);
	}

	mcContent = mcService.getMcContent(new Long(strToolContentID));

	return mcContent;
    }

    /**
     * generates a list for holding default questions and their candidate answers
     */
    public static List<McQuestionDTO> buildDefaultQuestions(McContent mcContent) {
	List<McQuestionDTO> questionDTOs = new LinkedList<McQuestionDTO>();

	Long mapIndex = new Long(1);

	for (McQueContent question : (Set<McQueContent>) mcContent.getMcQueContents()) {
	    McQuestionDTO questionDto = new McQuestionDTO();

	    String feedback = "";
	    if (question.getFeedback() != null) {
		feedback = question.getFeedback();
	    }

	    String questionText = question.getQuestion();

	    questionDto.setUid(question.getUid());
	    questionDto.setQuestion(questionText);
	    questionDto.setDisplayOrder(question.getDisplayOrder().toString());
	    questionDto.setFeedback(feedback);
	    questionDto.setMark(question.getMark().toString());

	    // build candidate dtos
	    List<McOptionDTO> optionDtos = new LinkedList<McOptionDTO>();
	    for (McOptsContent option : (Set<McOptsContent>) question.getMcOptionsContents()) {
		McOptionDTO optionDTO = new McOptionDTO(option);
		optionDtos.add(optionDTO);
	    }

	    questionDto.setListCandidateAnswersDTO(optionDtos);

	    questionDTOs.add(questionDto);

	    mapIndex = new Long(mapIndex.longValue() + 1);
	}

	return questionDTOs;
    }
}
