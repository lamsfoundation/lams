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
package org.lamsfoundation.lams.tool.qa.service;

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
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaCondition;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;

/**
 * Output factory for Q&A tool. Currently it provides only one type of output - a user answers represented by an array
 * of strings (output type "OUTPUT_COMPLEX").
 * 
 * @author Marcin Cieslak
 */
public class QaOutputFactory extends OutputFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject)
	    throws ToolException {
	SortedMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	if (toolContentObject != null) {
	    ToolOutputDefinition allAnswersDefinition = buildComplexOutputDefinition(QaAppConstants.TEXT_SEARCH_DEFINITION_NAME);
	    QaContent qaContent = (QaContent) toolContentObject;
	    // adding all existing conditions
	    allAnswersDefinition.setDefaultConditions(new ArrayList<BranchCondition>(qaContent.getConditions()));
	    // if no conditions were created in the tool instance, a default condition is added; the condition is
	    // persisted in QaService.
	    if (allAnswersDefinition.getDefaultConditions().isEmpty() && !qaContent.getQaQueContents().isEmpty()) {
		Set<QaQueContent> questions = new HashSet<QaQueContent>();
		questions.add((QaQueContent) qaContent.getQaQueContents().iterator().next());
		String name = buildConditionName(QaAppConstants.TEXT_SEARCH_DEFINITION_NAME, qaContent.getQaContentId()
			.toString());
		// Default condition checks if the first answer contains word "LAMS"
		QaCondition defaultCondition = new QaCondition(null, null, 1, name, getI18NText(
			QaAppConstants.TEXT_SEARCH_DEFAULT_CONDITION_DISPLAY_NAME_KEY, false), "LAMS", null, null,
			null, questions);
		qaContent.getConditions().add(defaultCondition);

		allAnswersDefinition.getDefaultConditions().add(defaultCondition);
	    }
	    allAnswersDefinition.setShowConditionNameOnly(true);
	    definitionMap.put(QaAppConstants.TEXT_SEARCH_DEFINITION_NAME, allAnswersDefinition);
	}

	return definitionMap;
    }

    /**
     * Follows {@link QaServicePOJO#getToolOutput(List, Long, Long)}.
     * 
     */
    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IQaService qaService, Long toolSessionId,
	    Long learnerId) {

	TreeMap<String, ToolOutput> outputs = new TreeMap<String, ToolOutput>();
	// cached tool output for all text search conditions
	ToolOutput allAnswersOutput = null;
	if (names == null) {
	    // output will be set for all the existing conditions
	    QaContent qaContent = qaService.getQaContentBySessionId(toolSessionId);
	    Set<QaCondition> conditions = qaContent.getConditions();
	    for (QaCondition condition : conditions) {
		String name = condition.getName();
		if (isTextSearchConditionName(name) && allAnswersOutput != null) {
		    outputs.put(name, allAnswersOutput);
		} else {
		    ToolOutput output = getToolOutput(name, qaService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			if (isTextSearchConditionName(name)) {
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
		    ToolOutput output = getToolOutput(name, qaService, toolSessionId, learnerId);
		    if (output != null) {
			outputs.put(name, output);
			if (isTextSearchConditionName(name)) {
			    allAnswersOutput = output;
			}
		    }
		}
	    }
	}
	return outputs;

    }

    public ToolOutput getToolOutput(String name, IQaService qaService, Long toolSessionId, Long learnerId) {
	if (isTextSearchConditionName(name)) {
	    // user answers are loaded from the DB and array of strings is created

	    QaSession session = qaService.retrieveQaSession(toolSessionId);
	    QaContent qaContent = session.getQaContent();
	    Set<QaQueContent> questions = qaContent.getQaQueContents();
	    String[] answers = new String[questions.size()];
	    QaQueUsr user = qaService.getQaUserBySession(learnerId, session.getUid());
	    for (QaQueContent question : questions) {
		List<QaUsrResp> attempts = null;
		if (user != null) {
		    attempts = qaService.getAttemptsForUserAndQuestionContent(user.getUid(), question.getUid());
		}
		if (attempts != null && !attempts.isEmpty()) {
		    // only the last attempt is taken into consideration
		    String answer = attempts.get(attempts.size() - 1).getAnswer();
		    answers[question.getDisplayOrder() - 1] = answer;
		}
	    }
	    return new ToolOutput(name, getI18NText(QaAppConstants.TEXT_SEARCH_DEFINITION_NAME, true), answers, false);
	}
	return null;
    }

    @Override
    protected String[] splitConditionName(String conditionName) {
	return super.splitConditionName(conditionName);
    }

    protected String buildConditionName(String uniquePart) {
	return super.buildConditionName(QaAppConstants.TEXT_SEARCH_DEFINITION_NAME, uniquePart);
    }

    private boolean isTextSearchConditionName(String name) {
	return name != null && name.startsWith(QaAppConstants.TEXT_SEARCH_DEFINITION_NAME);
    }
}