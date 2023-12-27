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

package org.lamsfoundation.lams.tool.mc.util;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.dto.McQuestionDTO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Keeps all operations needed for Authoring mode.
 *
 * @author Ozgur Demirtas
 */
public class AuthoringUtil {

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
     * persisting content.
     */
    public static McContent saveOrUpdateMcContent(IMcService mcService, HttpServletRequest request, ToolAccessMode mode,
	    McContent mcContent, Long toolContentID, List<McQuestionDTO> questionDTOs) {
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	String richTextTitle = request.getParameter(McAppConstants.TITLE);
	String richTextInstructions = request.getParameter(McAppConstants.INSTRUCTIONS);
	String sln = request.getParameter("sln");
	String useSelectLeaderToolOuput = request.getParameter("useSelectLeaderToolOuput");
	String prefixAnswersWithLetters = request.getParameter("prefixAnswersWithLetters");
	String questionsSequenced = request.getParameter("questionsSequenced");
	String randomize = request.getParameter("randomize");
	String displayAnswersFeedback = request.getParameter("displayAnswersFeedback");
	String showMarks = request.getParameter("showMarks");
	String retries = request.getParameter("retries");

	boolean questionsSequencedBoolean = false;
	boolean randomizeBoolean = false;
	boolean displayAnswersBoolean = false;
	boolean displayFeedbackOnlyBoolean = false;
	boolean showMarksBoolean = false;
	boolean slnBoolean = false;
	boolean useSelectLeaderToolOuputBoolean = false;
	boolean prefixAnswersWithLettersBoolean = false;
	boolean retriesBoolean = false;
	boolean enableConfidenceLevels = WebUtil.readBooleanParam(request, "enableConfidenceLevels", false);

	if ((questionsSequenced != null) && (questionsSequenced.equalsIgnoreCase("1"))) {
	    questionsSequencedBoolean = true;
	}

	if ((randomize != null) && (randomize.equalsIgnoreCase("1"))) {
	    randomizeBoolean = true;
	}

	if (displayAnswersFeedback != null) {
	    if (displayAnswersFeedback.equalsIgnoreCase("answers")) {
		displayAnswersBoolean = true;
		displayFeedbackOnlyBoolean = false;
	    } else if (displayAnswersFeedback.equalsIgnoreCase("feedback")) {
		displayAnswersBoolean = false;
		displayFeedbackOnlyBoolean = true;
	    }
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

	} else {
	    // if it is a Teacher mode (i.e. request comes from monitor) - change define later status
	    if (mode.isTeacher()) {
		mcContent.setDefineLater(false);
	    }
	}

	mcContent.setMcContentId(toolContentID);
	mcContent.setTitle(richTextTitle);
	mcContent.setInstructions(richTextInstructions);
	mcContent.setUpdateDate(new Date(System.currentTimeMillis()));
	/** keep updating this one */
	mcContent.setCreatedBy(userId);
	/** make sure we are setting the userId from the User object above */

	mcContent.setQuestionsSequenced(questionsSequencedBoolean);
	mcContent.setRandomize(randomizeBoolean);
	mcContent.setDisplayAnswers(displayAnswersBoolean);
	mcContent.setDisplayFeedbackOnly(displayFeedbackOnlyBoolean);
	mcContent.setShowMarks(showMarksBoolean);
	mcContent.setRetries(retriesBoolean);
	mcContent.setShowReport(slnBoolean);
	mcContent.setUseSelectLeaderToolOuput(useSelectLeaderToolOuputBoolean);
	mcContent.setPrefixAnswersWithLetters(prefixAnswersWithLettersBoolean);
	mcContent.setEnableConfidenceLevels(enableConfidenceLevels);

	//calculate total mark
	int totalMark = 0;
	for (McQuestionDTO questionDto : questionDTOs) {
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

	mcContent = mcService.getMcContent(new Long(toolContentID));

	return mcContent;
    }

    /**
     * generates a list for holding default questions and their candidate answers
     */
    public static List<McQuestionDTO> buildDefaultQuestions(McContent mcContent) {
	List<McQuestionDTO> questionDtos = new LinkedList<>();

	for (McQueContent question : mcContent.getMcQueContents()) {
	    McQuestionDTO questionDto = new McQuestionDTO();
	    questionDto.setUid(question.getUid());
	    questionDto.setQbQuestionUid(question.getQbQuestion().getUid());
	    questionDto.setName(question.getName());
	    questionDto.setDescription(question.getDescription());
	    questionDto.setDisplayOrder(question.getDisplayOrder());
	    String feedback = question.getFeedback() == null ? "" : question.getFeedback();
	    questionDto.setFeedback(feedback);
	    String mark = question.getMark() == null ? "1" : question.getMark().toString();
	    questionDto.setMark(mark);
	    questionDto.setContentFolderId(question.getQbQuestion().getContentFolderId());

	    // build candidate dtos
	    List<McOptionDTO> optionDtos = new LinkedList<>();
	    for (QbOption option : question.getQbQuestion().getQbOptions()) {
		McOptionDTO optionDTO = new McOptionDTO(option);
		optionDtos.add(optionDTO);
	    }

	    questionDto.setOptionDtos(optionDtos);
	    questionDtos.add(questionDto);
	}

	return questionDtos;
    }
}
