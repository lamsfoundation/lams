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

package org.lamsfoundation.lams.tool.qa.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.learningdesign.BranchCondition;
import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.model.QaCondition;
import org.lamsfoundation.lams.tool.qa.model.QaContent;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.lamsfoundation.lams.tool.qa.model.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.model.QaSession;
import org.lamsfoundation.lams.tool.qa.model.QaUsrResp;

/**
 * Output factory for Q&A tool. For conditions it provides only one type of output - a user answers represented by an
 * array of strings (output type "OUTPUT_COMPLEX"). For data flow between tools it provides all users' answers (from
 * current group) and questions asked.
 *
 * @author Marcin Cieslak
 */
public class QaOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<>();
	if (toolContentObject != null) {
	    QaContent qaContent = (QaContent) toolContentObject;
	    // Different definitions are provided, depending how the output will be used
	    Class stringArrayClass = String[].class;
	    Class arrayofStringArraysClass = String[][].class;
	    switch (definitionType) {
		case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_CONDITION:
		    ToolOutputDefinition userAnswersDefinition = buildComplexOutputDefinition(
			    QaAppConstants.USER_ANSWERS_DEFINITION_NAME, stringArrayClass);

		    // adding all existing conditions
		    userAnswersDefinition.setConditions(new ArrayList<BranchCondition>(qaContent.getConditions()));
		    definitionMap.put(QaAppConstants.USER_ANSWERS_DEFINITION_NAME, userAnswersDefinition);

		    break;
		case ToolOutputDefinition.DATA_OUTPUT_DEFINITION_TYPE_DATA_FLOW:
		    ToolOutputDefinition groupAnswersDefinition = buildComplexOutputDefinition(
			    QaAppConstants.GROUP_ANSWERS_DEFINITION_NAME, arrayofStringArraysClass);
		    definitionMap.put(QaAppConstants.GROUP_ANSWERS_DEFINITION_NAME, groupAnswersDefinition);

		    ToolOutputDefinition questionsDefinition = buildComplexOutputDefinition(
			    QaAppConstants.QUESTIONS_DEFINITION_NAME, stringArrayClass);
		    definitionMap.put(QaAppConstants.QUESTIONS_DEFINITION_NAME, questionsDefinition);

		    break;
	    }
	}

	return definitionMap;
    }

    /**
     * Follows {@link QaService#getToolOutput(List, Long, Long)}.
     *
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IQaService qaService, Long toolSessionId,
	    Long learnerId) {
	// result
	TreeMap<String, ToolOutput> outputs = new TreeMap<>();
	// tool output cache
	TreeMap<String, ToolOutput> baseOutputs = new TreeMap<>();

	if (names == null) {
	    // output will be set for all the existing conditions
	    QaContent qaContent = qaService.getQaContentBySessionId(toolSessionId);
	    Set<QaCondition> conditions = qaContent.getConditions();
	    for (QaCondition condition : conditions) {
		String name = condition.getName();
		String[] nameParts = splitConditionName(name);

		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, qaService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	} else {
	    for (String name : names) {
		String[] nameParts = splitConditionName(name);
		if (baseOutputs.get(nameParts[0]) != null) {
		    outputs.put(name, baseOutputs.get(nameParts[0]));
		} else {
		    ToolOutput output = getToolOutput(name, qaService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			baseOutputs.put(nameParts[0], output);
		    }
		}
	    }
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, IQaService qaService, Long toolSessionId, Long learnerId) {
	String[] nameParts = splitConditionName(name);
	if (QaAppConstants.USER_ANSWERS_DEFINITION_NAME.equals(nameParts[0])) {
	    // user answers are loaded from the DB and array of strings is created

	    QaSession session = qaService.getSessionById(toolSessionId);
	    QaContent qaContent = session.getQaContent();
	    Set<QaQueContent> questions = qaContent.getQaQueContents();
	    String[] answers = new String[questions.size()];
	    QaQueUsr user = qaService.getUserByIdAndSession(learnerId, session.getQaSessionId());
	    for (QaQueContent question : questions) {
		QaUsrResp response = qaService.getResponseByUserAndQuestion(user.getQueUsrId(), question.getUid());
		if (response != null) {
		    answers[question.getDisplayOrder() - 1] = response.getAnswer();
		}
	    }
	    return new ToolOutput(name, getI18NText(QaAppConstants.USER_ANSWERS_DEFINITION_NAME, true), answers, false);
	} else if (QaAppConstants.GROUP_ANSWERS_DEFINITION_NAME.equals(nameParts[0])) {
	    // all users' answers are loaded from the DB and array of strings is created

	    QaSession session = qaService.getSessionById(toolSessionId);
	    QaContent qaContent = session.getQaContent();
	    Set<QaQueContent> questions = qaContent.getQaQueContents();
	    Set<QaQueUsr> users = session.getQaQueUsers();
	    String[] dummyStringArray = new String[] {};

	    // answers sorted by time of adding, so "usersAndAnswers" has the newest answers at the beginning
	    Map<Long, String[]> timeAndAnswers = new TreeMap<>();
	    for (QaQueUsr user : users) {
		if (user != null) {
		    List<String> answers = new LinkedList<>();
		    long lastAttemptTime = Long.MAX_VALUE;
		    for (QaQueContent question : questions) {

			QaUsrResp response = qaService.getResponseByUserAndQuestion(user.getQueUsrId(),
				question.getUid());

			if (response != null) {
			    // we get the time of the attempt - the "lastAttemptTime" will the time of the whole answer
			    // set given
			    long timeOfAttempt = response.getAttemptTime().getTime();
			    if (timeOfAttempt < lastAttemptTime) {
				lastAttemptTime = timeOfAttempt;
			    }

			    String answer = response.getAnswer();
			    if (!StringUtils.isBlank(answer)) {
				// check for duplicate answers
				boolean duplicate = false;
				int questionIndex = question.getDisplayOrder() - 1;
				for (String[] previousAnswers : timeAndAnswers.values()) {
				    for (String previousAnswer : previousAnswers) {
					if (answer.equalsIgnoreCase(previousAnswer)) {
					    duplicate = true;
					    break;
					}
				    }
				}

				if (!duplicate) {
				    answers.add(answer);
				}
			    }
			}
		    }
		    if (!answers.isEmpty()) {
			while (timeAndAnswers.containsKey(lastAttemptTime)) {
			    lastAttemptTime--;
			}
			timeAndAnswers.put(lastAttemptTime, answers.toArray(dummyStringArray));
		    }
		}
	    }
	    String[][] usersAndAnswers = new String[timeAndAnswers.size()][];
	    int userIndex = 0;
	    for (Long key : timeAndAnswers.keySet()) {
		usersAndAnswers[userIndex] = timeAndAnswers.get(key);
		userIndex++;
	    }

	    return new ToolOutput(name, getI18NText(QaAppConstants.GROUP_ANSWERS_DEFINITION_NAME, true),
		    usersAndAnswers, false);
	} else if (QaAppConstants.QUESTIONS_DEFINITION_NAME.equals(nameParts[0])) {
	    // Questions asked in this Q&A activity
	    QaSession session = qaService.getSessionById(toolSessionId);
	    QaContent qaContent = session.getQaContent();
	    Set<QaQueContent> questions = qaContent.getQaQueContents();
	    String[] questionArray = new String[questions.size()];
	    int questionIndex = 0;
	    for (QaQueContent question : questions) {
		questionArray[questionIndex++] = question.getQbQuestion().getName();
	    }
	    return new ToolOutput(name, getI18NText(QaAppConstants.QUESTIONS_DEFINITION_NAME, true), questionArray,
		    false);
	}
	return null;
    }

    @Override
    protected String[] splitConditionName(String conditionName) {
	return super.splitConditionName(conditionName);
    }

    protected String buildUserAnswersConditionName(String uniquePart) {
	return super.buildConditionName(QaAppConstants.USER_ANSWERS_DEFINITION_NAME, uniquePart);
    }
}