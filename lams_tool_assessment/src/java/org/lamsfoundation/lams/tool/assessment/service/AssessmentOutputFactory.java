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
package org.lamsfoundation.lams.tool.assessment.service;  

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentResult;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;

public class AssessmentOutputFactory extends OutputFactory {

    protected final static String OUTPUT_NAME_LEARNER_TOTAL_SCORE = "learner.total.score";
    protected final static String OUTPUT_NAME_LEARNER_TIME_TAKEN = "learner.time.taken";
    protected final static String OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS = "learner.number.of.attempts";

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject, int definitionType) {

	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();
	
	ToolOutputDefinition definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS, new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS, definition);
	
	definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_TIME_TAKEN, new Long(0), null);
	definitionMap.put(OUTPUT_NAME_LEARNER_TIME_TAKEN, definition);
	
	if (toolContentObject != null) {
	    Assessment assessment = (Assessment) toolContentObject;
	    Set<AssessmentQuestion> questions = assessment.getQuestions();

	    Long totalMarksPossible = new Long(0);
	    for(AssessmentQuestion question : questions) {
		totalMarksPossible += question.getDefaultGrade();
	    };	    
	    definition = buildRangeDefinition(OUTPUT_NAME_LEARNER_TOTAL_SCORE, new Long(0), totalMarksPossible, true);
	    definitionMap.put(OUTPUT_NAME_LEARNER_TOTAL_SCORE, definition);

	    for(AssessmentQuestion question : questions) {
		Long markAvailable = null;
		if (question.getDefaultGrade() != 0) {
		    markAvailable = new Long(question.getDefaultGrade());
		}
		
		definition = buildRangeDefinition(String.valueOf(question.getSequenceId()), new Long(0), markAvailable);
		definition.setDescription(getI18NText("output.user.score.for.question", false) + question.getTitle());
		definitionMap.put(String.valueOf(question.getSequenceId()), definition);		
	    };
	}

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IAssessmentService assessmentService, Long toolSessionId,
	    Long learnerId) {

	TreeMap<String, ToolOutput> output = new TreeMap<String, ToolOutput>();

	AssessmentSession session = assessmentService.getAssessmentSessionBySessionId(toolSessionId);
	if ((session != null) && (session.getAssessment() != null)) {
	    Assessment assessment = session.getAssessment();

	    if (names == null || names.contains(OUTPUT_NAME_LEARNER_TOTAL_SCORE)) {
		output.put(OUTPUT_NAME_LEARNER_TOTAL_SCORE, getTotalScore(assessmentService, learnerId, assessment));
	    }
	    if (names == null || names.contains(OUTPUT_NAME_LEARNER_TIME_TAKEN)) {
		output.put(OUTPUT_NAME_LEARNER_TIME_TAKEN, getTimeTaken(assessmentService, learnerId, assessment));
	    }
	    if (names == null || names.contains(OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS)) {
		output.put(OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS, getNumberAttempts(assessmentService, learnerId, assessment));
	    }	
	    Set<AssessmentQuestion> questions = assessment.getQuestions();
	    for(AssessmentQuestion question : questions) {
		if (names == null || names.contains(String.valueOf(question.getSequenceId()))) {
		    output.put(OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS, getQuestionScore(assessmentService, learnerId, assessment, question.getSequenceId()));
		}
	    }
	}

	return output;
    }

    public ToolOutput getToolOutput(String name, IAssessmentService assessmentService, Long toolSessionId, Long learnerId) {
	if (name != null) {
	    AssessmentSession session = assessmentService.getAssessmentSessionBySessionId(toolSessionId);
	    
	    if ((session != null) && (session.getAssessment() != null)) {
		Assessment assessment = session.getAssessment();

		if (name.equals(OUTPUT_NAME_LEARNER_TOTAL_SCORE)) {
		    return getTotalScore(assessmentService, learnerId, assessment);
		} else if (name.equals(OUTPUT_NAME_LEARNER_TIME_TAKEN)) {
		    return getTimeTaken(assessmentService, learnerId, assessment);
		} else if (name.equals(OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS)) {
		    return getNumberAttempts(assessmentService, learnerId, assessment);
		} else {
		    Set<AssessmentQuestion> questions = assessment.getQuestions();
		    for(AssessmentQuestion question : questions) {
			if (name.equals(String.valueOf(question.getSequenceId()))) {
			    return getQuestionScore(assessmentService, learnerId, assessment, question.getSequenceId());
			}
		    }
		}
	    }
	}
	return null;
    }

    /**
     * Get total score for a user. Will always return a ToolOutput object.
     */
    private ToolOutput getTotalScore(IAssessmentService assessmentService, Long learnerId, Assessment assessment) {
	AssessmentResult assessmentResult = assessmentService.getLastFinishedAssessmentResult(assessment.getUid(), learnerId);
	
	float totalScore = (assessmentResult == null) ? 0 : assessmentResult.getGrade();
	
	return new ToolOutput(AssessmentOutputFactory.OUTPUT_NAME_LEARNER_TOTAL_SCORE, getI18NText(
		AssessmentOutputFactory.OUTPUT_NAME_LEARNER_TOTAL_SCORE, true), totalScore);
    }

    /**
     * Get time taken for a specific user to accomplish this assessment. Will always return a ToolOutput object.
     */
    private ToolOutput getTimeTaken(IAssessmentService assessmentService, Long learnerId, Assessment assessment) {
	AssessmentResult assessmentResult = assessmentService.getLastFinishedAssessmentResult(assessment.getUid(), learnerId);
	
	long timeTaken = 0;
	if ((assessmentResult != null) && (assessmentResult.getFinishDate() != null)) {
	    timeTaken = (assessmentResult.getFinishDate().getTime() - assessmentResult.getStartDate().getTime()) / 1000;
	}
	
	return new ToolOutput(AssessmentOutputFactory.OUTPUT_NAME_LEARNER_TIME_TAKEN, getI18NText(
		AssessmentOutputFactory.OUTPUT_NAME_LEARNER_TIME_TAKEN, true), timeTaken);
    }
    
    /**
     * Get the number of attempts done by user. Will always return a ToolOutput object.
     */
    private ToolOutput getNumberAttempts(IAssessmentService assessmentService, Long learnerId, Assessment assessment) {
	int numberAttempts = assessmentService.getAssessmentResultCount(assessment.getUid(), learnerId);
	
	return new ToolOutput(AssessmentOutputFactory.OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS, getI18NText(
		AssessmentOutputFactory.OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS, true), numberAttempts);
    }
    
    /**
     * Get user's score for the question. Will always return a ToolOutput object.
     */
    private ToolOutput getQuestionScore(IAssessmentService assessmentService, Long learnerId, Assessment assessment,
	    int questionSequenceId) {
	AssessmentResult assessmentResult = assessmentService.getLastFinishedAssessmentResult(assessment.getUid(), learnerId);

	float questionScore = 0;
	for (AssessmentQuestionResult questionResult : assessmentResult.getQuestionResults()) {
	    if (questionResult.getAssessmentQuestion().getSequenceId() == questionSequenceId) {
		questionScore = questionResult.getMark();
		break;
	    }
	}

	return new ToolOutput(String.valueOf(questionSequenceId), "description", questionScore);
    }

}
