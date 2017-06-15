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
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.util.McComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Keeps all operations needed for Authoring mode.
 *
 * @author Ozgur Demirtas
 */
public class AuthoringUtil {
    private static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());

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
	
	//calculate total mark
	int totalMark = 0;
	for (McQuestionDTO questionDto: questionDTOs) {
	    String mark = questionDto.getMark();
	    if (StringUtils.isNotBlank(mark)) {
		int intMark = new Integer(mark).intValue();
		totalMark += intMark;
	    }
	}

	String passmarkStr = request.getParameter("passmark");
	//nullify passmark in case 'retries' option is OFF
	if (StringUtils.isBlank(passmarkStr) || !retriesBoolean) {
	    passmarkStr = "0";
	}
	//passmark can't be more than total mark
	Integer passmark = new Integer(passmarkStr);
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
	List<McQuestionDTO> questionDtos = new LinkedList<McQuestionDTO>();

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
	    questionDto.setDisplayOrder(question.getDisplayOrder());
	    questionDto.setFeedback(feedback);
	    questionDto.setMark(question.getMark().toString());

	    // build candidate dtos
	    List<McOptionDTO> optionDtos = new LinkedList<McOptionDTO>();
	    for (McOptsContent option : (Set<McOptsContent>) question.getMcOptionsContents()) {
		McOptionDTO optionDTO = new McOptionDTO(option);
		optionDtos.add(optionDTO);
	    }

	    questionDto.setOptionDtos(optionDtos);
	    questionDtos.add(questionDto);

	    mapIndex = new Long(mapIndex.longValue() + 1);
	}

	return questionDtos;
    }
}
