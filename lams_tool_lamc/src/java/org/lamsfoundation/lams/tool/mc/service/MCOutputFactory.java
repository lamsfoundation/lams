/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.mc.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.dto.ToolOutputDTO;
import org.lamsfoundation.lams.tool.mc.model.McContent;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.lamsfoundation.lams.tool.mc.model.McQueUsr;
import org.lamsfoundation.lams.tool.mc.model.McSession;
import org.lamsfoundation.lams.tool.mc.model.McUsrAttempt;

public class MCOutputFactory extends OutputFactory {

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) {

	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<>();
	ToolOutputDefinition definition = buildBooleanOutputDefinition(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT);
	definitionMap.put(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT, definition);

	if (toolContentObject != null) {
	    McContent content = (McContent) toolContentObject;
	    definition = buildRangeDefinition(McAppConstants.OUTPUT_NAME_LEARNER_MARK, new Long(0),
		    new Long(content.getTotalMarksPossible().longValue()), true);
	    definition.setWeightable(true);
	    definitionMap.put(McAppConstants.OUTPUT_NAME_LEARNER_MARK, definition);
	} else {
	    log.error(
		    "Unable to build content based output definitions for Multiple Choice as no tool content object supplied. Only including the definitions that do not need any content.");
	}

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IMcService mcService, Long toolSessionId,
	    Long learnerId) {

	TreeMap<String, ToolOutput> output = new TreeMap<>();

	McSession session = mcService.getMcSessionById(toolSessionId);
	if (session != null) {

	    McQueUsr queUser = mcService.getMcUserBySession(learnerId, session.getUid());
	    if (queUser != null) {

		if (names == null || names.contains(McAppConstants.OUTPUT_NAME_LEARNER_MARK)) {
		    output.put(McAppConstants.OUTPUT_NAME_LEARNER_MARK, getLearnerMark(queUser));
		}
		if (names == null || names.contains(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT)) {
		    output.put(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT,
			    getLearnerAllCorrect(mcService, queUser));
		}
	    }
	}

	return output;
    }

    public ToolOutput getToolOutput(String name, IMcService mcService, Long toolSessionId, Long learnerId) {
	if (name != null) {
	    McSession session = mcService.getMcSessionById(toolSessionId);
	    if (session != null) {
		McQueUsr queUser = mcService.getMcUserBySession(learnerId, session.getUid());

		if (queUser != null) {
		    if (name.equals(McAppConstants.OUTPUT_NAME_LEARNER_MARK)) {
			return getLearnerMark(queUser);
		    } else if (name.equals(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT)) {
			return getLearnerAllCorrect(mcService, queUser);
		    }
		}
	    }
	}
	return null;
    }

    public List<ToolOutput> getToolOutputs(String name, IMcService assessmentService, Long toolContentId) {
	if ((name != null) && (toolContentId != null)) {

	    if (name.equals(McAppConstants.OUTPUT_NAME_LEARNER_MARK)) {
		List<ToolOutputDTO> toolOutputDtos = assessmentService.getLearnerMarksByContentId(toolContentId);

		//convert toolOutputDtos to toolOutputs
		List<ToolOutput> toolOutputs = new ArrayList<>();
		for (ToolOutputDTO toolOutputDto : toolOutputDtos) {
		    float totalMark = toolOutputDto.getMark() == null ? 0 : toolOutputDto.getMark().floatValue();

		    ToolOutput toolOutput = new ToolOutput(McAppConstants.OUTPUT_NAME_LEARNER_MARK,
			    getI18NText(McAppConstants.OUTPUT_NAME_LEARNER_MARK, true), totalMark);
		    toolOutput.setUserId(toolOutputDto.getUserId().intValue());
		    toolOutputs.add(toolOutput);
		}

		return toolOutputs;

	    } else if (name.equals(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT)) {
		List<ToolOutputDTO> toolOutputDtos = assessmentService.getLearnerMarksByContentId(toolContentId);

		//calculate max possible total mark
		int maxMark = 0;
		McContent mcContent = assessmentService.getMcContent(toolContentId);
		for (McQueContent question : mcContent.getMcQueContents()) {
		    maxMark += question.getMark();
		}

		//convert toolOutputDtos to toolOutputs
		List<ToolOutput> toolOutputs = new ArrayList<>();
		for (ToolOutputDTO toolOutputDto : toolOutputDtos) {
		    float totalMark = toolOutputDto.getMark() == null ? 0 : toolOutputDto.getMark().floatValue();
		    boolean isAllQuestionAnswersCorrect = totalMark == maxMark;

		    ToolOutput toolOutput = new ToolOutput(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT,
			    getI18NText(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT, true),
			    isAllQuestionAnswersCorrect);
		    toolOutput.setUserId(toolOutputDto.getUserId().intValue());
		    toolOutputs.add(toolOutput);
		}

		return toolOutputs;
	    }
	}
	return null;
    }

    /**
     * Get the mark for a specific user. This gets the mark associated with the last attempt. Will always return a
     * ToolOutput object.
     */
    private ToolOutput getLearnerMark(McQueUsr queUser) {
	Long mark;
	if (queUser != null && queUser.getLastAttemptTotalMark() != null) {
	    mark = queUser.getLastAttemptTotalMark().longValue();
	} else {
	    mark = new Long(0);
	}
	return new ToolOutput(McAppConstants.OUTPUT_NAME_LEARNER_MARK,
		getI18NText(McAppConstants.OUTPUT_NAME_LEARNER_MARK, true), mark);
    }

    /**
     * Did the user get the questions all correct. This checks the answers associated with the last attempt. Assumes all
     * correct if the mark is equal to the maximum possible mark. Will always return a ToolOutput object.
     */
    private ToolOutput getLearnerAllCorrect(IMcService mcService, McQueUsr queUser) {
	boolean allCorrect = allQuestionsCorrect(mcService, queUser);
	return new ToolOutput(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT,
		getI18NText(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT, true), allCorrect);
    }

    // written to cope with more than one correct option for each questionDescription but only tested with
    // one correct option for a questionDescription.
    private boolean allQuestionsCorrect(IMcService mcService, McQueUsr user) {

	// Build a list of all the correct answers. If we hit any options that are not a correct option
	// we can abort as we know there is a wrong answer.
	// Otherwise count the number of correct options overall (for comparison later).
	long correctlearnerOptions = 0;
	List<McUsrAttempt> userAttempts = mcService.getFinalizedUserAttempts(user);
	for (McUsrAttempt userAttempt : userAttempts) {
	    QbOption option = userAttempt.getQbOption();
	    if (!option.isCorrect()) {
		// wrong answer so no point going any further
		return false;
	    } else {
		correctlearnerOptions++;
	    }
	}

	// now count the overall number of correct options
	long correctOptions = 0;
	McContent mcContent = user.getMcSession().getMcContent();
	Iterator questionIterator = mcContent.getMcQueContents().iterator();
	while (questionIterator.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) questionIterator.next();
	    Iterator<QbOption> optionIterator = mcQueContent.getQbQuestion().getQbOptions().iterator();
	    while (optionIterator.hasNext()) {
		if (optionIterator.next().isCorrect()) {
		    correctOptions++;
		}
	    }
	}

	// We know the user didn't get everything wrong, but did they answer enough options correctly?
	// This case is used when there is more than one correct option for each answer. Simple way, compare counts!
	return correctOptions == correctlearnerOptions;
    }

}
