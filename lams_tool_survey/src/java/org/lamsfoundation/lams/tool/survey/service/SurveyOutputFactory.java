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

/* $Id$ */
package org.lamsfoundation.lams.tool.survey.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyAnswer;
import org.lamsfoundation.lams.tool.survey.model.SurveyCondition;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;

/**
 * Output factory for Survey tool. Currently it provides only one type of output - a user answers represented by an
 * array of strings (output type "OUTPUT_COMPLEX").
 * 
 * @author Marcin Cieslak
 */
public class SurveyOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject)
	    throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	if (toolContentObject != null) {
	    ToolOutputDefinition allAnswersDefinition = buildComplexOutputDefinition(SurveyConstants.TEXT_SEARCH_DEFINITION_NAME);
	    Survey survey = (Survey) toolContentObject;
	    // adding all existing conditions
	    allAnswersDefinition.setDefaultConditions(new ArrayList<BranchCondition>(survey.getConditions()));
	    // if no conditions were created in the tool instance, a default condition is added; the condition is
	    // persisted in SurveyService.
	    if (allAnswersDefinition.getDefaultConditions().isEmpty() && !survey.getQuestions().isEmpty()) {
		Set<SurveyQuestion> questions = new HashSet<SurveyQuestion>();
		questions.add(survey.getQuestions().iterator().next());
		String name = buildConditionName(SurveyConstants.TEXT_SEARCH_DEFINITION_NAME, survey.getContentId()
			.toString());
		// Default condition checks if the first answer contains word "LAMS"
		SurveyCondition defaultCondition = new SurveyCondition(null, null, 1, name, getI18NText(
			SurveyConstants.TEXT_SEARCH_DEFAULT_CONDITION_DISPLAY_NAME_KEY, false), "LAMS", null, null,
			null, questions);
		survey.getConditions().add(defaultCondition);

		allAnswersDefinition.getDefaultConditions().add(defaultCondition);
	    }
	    allAnswersDefinition.setShowConditionNameOnly(true);
	    definitionMap.put(SurveyConstants.TEXT_SEARCH_DEFINITION_NAME, allAnswersDefinition);
	}

	return definitionMap;
    }

    /**
     * Follows {@link QaServicePOJO#getToolOutput(List, Long, Long)}.
     * 
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, ISurveyService surveyService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// cached tool output for all text search conditions
	ToolOutput allAnswersOutput = null;
	if (names == null) {
	    // output will be set for all the existing conditions
	    Survey qaContent = surveyService.getSurveyBySessionId(toolSessionId);
	    Set<SurveyCondition> conditions = qaContent.getConditions();
	    for (SurveyCondition condition : conditions) {
		String name = condition.getName();
		if (isTextSearchConditionName(name) && allAnswersOutput != null) {
		    outputs.put(name, allAnswersOutput);
		} else {
		    ToolOutput output = getToolOutput(name, surveyService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			if (isTextSearchConditionName(SurveyConstants.TEXT_SEARCH_DEFINITION_NAME)) {
			    allAnswersOutput = output;
			}
		    }
		}
	    }
	} else {
	    for (String name : names) {
		if (isTextSearchConditionName(name) && allAnswersOutput != null) {
		    outputs.put(name, allAnswersOutput);
		} else {
		    ToolOutput output = getToolOutput(name, surveyService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			if (isTextSearchConditionName(SurveyConstants.TEXT_SEARCH_DEFINITION_NAME)) {
			    allAnswersOutput = output;
			}
		    }
		}
	    }
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, ISurveyService surveyService, Long toolSessionId, Long learnerId) {
	if (isTextSearchConditionName(SurveyConstants.TEXT_SEARCH_DEFINITION_NAME)) {
	    // user answers are loaded from the DB and array of strings is created

	    Survey survey = surveyService.getSurveyBySessionId(toolSessionId);
	    Set<SurveyQuestion> questions = survey.getQuestions();
	    String[] textAnswers = new String[questions.size()];
	    SurveyUser user = surveyService.getUserByIDAndSession(learnerId, toolSessionId);
	    List<AnswerDTO> answerDTOs = surveyService.getQuestionAnswers(toolSessionId, user.getUid());
	    if (answerDTOs != null && !answerDTOs.isEmpty()) {
		for (AnswerDTO answerDTO : answerDTOs) {
		    SurveyAnswer surveyAnswer = answerDTO.getAnswer();
		    SurveyQuestion question = surveyAnswer.getSurveyQuestion();
		    if (question.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
			textAnswers[question.getSequenceId() - 1] = surveyAnswer.getAnswerText();
		    }
		}
	    }
	    return new ToolOutput(name, getI18NText(SurveyConstants.TEXT_SEARCH_DEFINITION_NAME, true), textAnswers,
		    false);
	}
	return null;
    }

    @Override
    protected String[] splitConditionName(String conditionName) {
	return super.splitConditionName(conditionName);
    }

    protected String buildConditionName(String uniquePart) {
	return super.buildConditionName(SurveyConstants.TEXT_SEARCH_DEFINITION_NAME, uniquePart);
    }

    private boolean isTextSearchConditionName(String name) {
	return name != null && name.startsWith(SurveyConstants.TEXT_SEARCH_DEFINITION_NAME);
    }
}