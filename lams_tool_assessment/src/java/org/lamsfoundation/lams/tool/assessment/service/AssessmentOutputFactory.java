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


package org.lamsfoundation.lams.tool.assessment.service;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.OutputFactory;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolOutputDefinition;
import org.lamsfoundation.lams.tool.assessment.AssessmentConstants;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;
import org.lamsfoundation.lams.tool.assessment.model.AssessmentSession;
import org.lamsfoundation.lams.tool.assessment.model.QuestionReference;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;

public class AssessmentOutputFactory extends OutputFactory {

    /**
     * @see org.lamsfoundation.lams.tool.OutputDefinitionFactory#getToolOutputDefinitions(java.lang.Object)
     */
    @Override
    public SortedMap<String, ToolOutputDefinition> getToolOutputDefinitions(Object toolContentObject,
	    int definitionType) {

	TreeMap<String, ToolOutputDefinition> definitionMap = new TreeMap<String, ToolOutputDefinition>();

	ToolOutputDefinition definition = buildRangeDefinition(AssessmentConstants.OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS,
		new Long(0), null);
	definitionMap.put(AssessmentConstants.OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS, definition);

	definition = buildRangeDefinition(AssessmentConstants.OUTPUT_NAME_LEARNER_TIME_TAKEN, new Long(0), null);
	definitionMap.put(AssessmentConstants.OUTPUT_NAME_LEARNER_TIME_TAKEN, definition);

	if (toolContentObject != null) {
	    Assessment assessment = (Assessment) toolContentObject;
	    Set<QuestionReference> questionReferences = new TreeSet<QuestionReference>(new SequencableComparator());
	    questionReferences.addAll(assessment.getQuestionReferences());

	    Long totalMarksPossible = new Long(0);
	    for (QuestionReference questionReference : questionReferences) {
		totalMarksPossible += questionReference.getDefaultGrade();
	    }
	    ;
	    definition = buildRangeDefinition(AssessmentConstants.OUTPUT_NAME_LEARNER_TOTAL_SCORE, new Long(0),
		    totalMarksPossible, true);
	    definitionMap.put(AssessmentConstants.OUTPUT_NAME_LEARNER_TOTAL_SCORE, definition);

	    definition = buildRangeDefinition(AssessmentConstants.OUTPUT_NAME_BEST_SCORE, new Long(0),
		    totalMarksPossible, false);
	    definitionMap.put(AssessmentConstants.OUTPUT_NAME_BEST_SCORE, definition);
	    definition = buildRangeDefinition(AssessmentConstants.OUTPUT_NAME_FIRST_SCORE, new Long(0),
		    totalMarksPossible, false);
	    definitionMap.put(AssessmentConstants.OUTPUT_NAME_FIRST_SCORE, definition);
	    definition = buildRangeDefinition(AssessmentConstants.OUTPUT_NAME_AVERAGE_SCORE, new Long(0),
		    totalMarksPossible, false);
	    definitionMap.put(AssessmentConstants.OUTPUT_NAME_AVERAGE_SCORE, definition);

	    int randomQuestionsCount = 1;
	    for (QuestionReference questionReference : questionReferences) {
		Long markAvailable = null;
		if (questionReference.getDefaultGrade() != 0) {
		    markAvailable = new Long(questionReference.getDefaultGrade());
		}

		String description = getI18NText("output.user.score.for.question", false);
		if (questionReference.isRandomQuestion()) {
		    description += getI18NText("label.authoring.basic.type.random.question", false) + " "
			    + randomQuestionsCount++;
		} else {
		    description += questionReference.getQuestion().getTitle();
		}

		definition = buildRangeDefinition(String.valueOf(questionReference.getSequenceId()), new Long(0),
			markAvailable);
		definition.setDescription(description);
		definitionMap.put(String.valueOf(questionReference.getSequenceId()), definition);
	    }
	    ;
	}

	return definitionMap;
    }

    public SortedMap<String, ToolOutput> getToolOutput(List<String> names, IAssessmentService assessmentService,
	    Long toolSessionId, Long learnerId) {

	TreeMap<String, ToolOutput> output = new TreeMap<String, ToolOutput>();

	AssessmentSession session = assessmentService.getAssessmentSessionBySessionId(toolSessionId);
	if ((session != null) && (session.getAssessment() != null)) {
	    Assessment assessment = session.getAssessment();

	    if (names == null || names.contains(AssessmentConstants.OUTPUT_NAME_LEARNER_TOTAL_SCORE)) {
		output.put(AssessmentConstants.OUTPUT_NAME_LEARNER_TOTAL_SCORE,
			getLastTotalScore(assessmentService, learnerId, assessment));
	    }
	    if (names == null || names.contains(AssessmentConstants.OUTPUT_NAME_BEST_SCORE)) {
		output.put(AssessmentConstants.OUTPUT_NAME_BEST_SCORE,
			getBestTotalScore(assessmentService, toolSessionId, learnerId));
	    }
	    if (names == null || names.contains(AssessmentConstants.OUTPUT_NAME_FIRST_SCORE)) {
		output.put(AssessmentConstants.OUTPUT_NAME_FIRST_SCORE,
			getFirstTotalScore(assessmentService, toolSessionId, learnerId));
	    }
	    if (names == null || names.contains(AssessmentConstants.OUTPUT_NAME_AVERAGE_SCORE)) {
		output.put(AssessmentConstants.OUTPUT_NAME_AVERAGE_SCORE,
			getAverageTotalScore(assessmentService, toolSessionId, learnerId));
	    }
	    if (names == null || names.contains(AssessmentConstants.OUTPUT_NAME_LEARNER_TIME_TAKEN)) {
		output.put(AssessmentConstants.OUTPUT_NAME_LEARNER_TIME_TAKEN,
			getTimeTaken(assessmentService, learnerId, assessment));
	    }
	    if (names == null || names.contains(AssessmentConstants.OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS)) {
		output.put(AssessmentConstants.OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS,
			getNumberAttempts(assessmentService, learnerId, assessment));
	    }
	    Set<AssessmentQuestion> questions = assessment.getQuestions();
	    for (AssessmentQuestion question : questions) {
		if (names == null || names.contains(String.valueOf(question.getSequenceId()))) {
		    output.put(AssessmentConstants.OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS,
			    getQuestionScore(assessmentService, learnerId, assessment, question.getSequenceId()));
		}
	    }
	}

	return output;
    }

    public ToolOutput getToolOutput(String name, IAssessmentService assessmentService, Long toolSessionId,
	    Long learnerId) {
	if (name != null) {
	    AssessmentSession session = assessmentService.getAssessmentSessionBySessionId(toolSessionId);

	    if ((session != null) && (session.getAssessment() != null)) {
		Assessment assessment = session.getAssessment();

		if (name.equals(AssessmentConstants.OUTPUT_NAME_LEARNER_TOTAL_SCORE)) {
		    return getLastTotalScore(assessmentService, learnerId, assessment);
		} else if (name.equals(AssessmentConstants.OUTPUT_NAME_BEST_SCORE)) {
		    return getBestTotalScore(assessmentService, toolSessionId, learnerId);
		} else if (name.equals(AssessmentConstants.OUTPUT_NAME_FIRST_SCORE)) {
		    return getFirstTotalScore(assessmentService, toolSessionId, learnerId);
		} else if (name.equals(AssessmentConstants.OUTPUT_NAME_AVERAGE_SCORE)) {
		    return getAverageTotalScore(assessmentService, toolSessionId, learnerId);
		} else if (name.equals(AssessmentConstants.OUTPUT_NAME_LEARNER_TIME_TAKEN)) {
		    return getTimeTaken(assessmentService, learnerId, assessment);
		} else if (name.equals(AssessmentConstants.OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS)) {
		    return getNumberAttempts(assessmentService, learnerId, assessment);
		} else {
		    Set<AssessmentQuestion> questions = assessment.getQuestions();
		    for (AssessmentQuestion question : questions) {
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
    private ToolOutput getLastTotalScore(IAssessmentService assessmentService, Long learnerId, Assessment assessment) {
	Float assessmentResultGrade = assessmentService.getLastTotalScoreByUser(assessment.getUid(), learnerId);

	float totalScore = (assessmentResultGrade == null) ? 0 : assessmentResultGrade;

	return new ToolOutput(AssessmentConstants.OUTPUT_NAME_LEARNER_TOTAL_SCORE,
		getI18NText(AssessmentConstants.OUTPUT_NAME_LEARNER_TOTAL_SCORE, true), totalScore);
    }

    /**
     * Get the best score for a user. Will always return a ToolOutput object.
     */
    private ToolOutput getBestTotalScore(IAssessmentService assessmentService, Long sessionId, Long userId) {
	Float bestTotalScore = assessmentService.getBestTotalScoreByUser(sessionId, userId);
	float bestTotalScoreFloat = (bestTotalScore == null) ? 0 : bestTotalScore;

	return new ToolOutput(AssessmentConstants.OUTPUT_NAME_BEST_SCORE,
		getI18NText(AssessmentConstants.OUTPUT_NAME_BEST_SCORE, true), bestTotalScoreFloat);
    }

    /**
     * Get the first score for a user. Will always return a ToolOutput object.
     */
    private ToolOutput getFirstTotalScore(IAssessmentService assessmentService, Long sessionId, Long userId) {
	Float firstTotalScore = assessmentService.getFirstTotalScoreByUser(sessionId, userId);
	float firstTotalScoreFloat = (firstTotalScore == null) ? 0 : firstTotalScore;

	return new ToolOutput(AssessmentConstants.OUTPUT_NAME_FIRST_SCORE,
		getI18NText(AssessmentConstants.OUTPUT_NAME_FIRST_SCORE, true), firstTotalScoreFloat);
    }

    /**
     * Get the average score for a user. Will always return a ToolOutput object.
     */
    private ToolOutput getAverageTotalScore(IAssessmentService assessmentService, Long sessionId, Long userId) {
	Float averageTotalScore = assessmentService.getAvergeTotalScoreByUser(sessionId, userId);
	float averageTotalScoreFloat = (averageTotalScore == null) ? 0 : averageTotalScore;

	return new ToolOutput(AssessmentConstants.OUTPUT_NAME_AVERAGE_SCORE,
		getI18NText(AssessmentConstants.OUTPUT_NAME_AVERAGE_SCORE, true), averageTotalScoreFloat);
    }

    /**
     * Get time taken for a specific user to accomplish this assessment. Will always return a ToolOutput object.
     */
    private ToolOutput getTimeTaken(IAssessmentService assessmentService, Long learnerId, Assessment assessment) {
	Integer assessmentResultTimeTaken = assessmentService
		.getLastFinishedAssessmentResultTimeTaken(assessment.getUid(), learnerId);

	long timeTaken = (assessmentResultTimeTaken == null) ? 0 : assessmentResultTimeTaken;

	return new ToolOutput(AssessmentConstants.OUTPUT_NAME_LEARNER_TIME_TAKEN,
		getI18NText(AssessmentConstants.OUTPUT_NAME_LEARNER_TIME_TAKEN, true), timeTaken);
    }

    /**
     * Get the number of attempts done by user. Will always return a ToolOutput object.
     */
    private ToolOutput getNumberAttempts(IAssessmentService assessmentService, Long learnerId, Assessment assessment) {
	int numberAttempts = assessmentService.getAssessmentResultCount(assessment.getUid(), learnerId);

	return new ToolOutput(AssessmentConstants.OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS,
		getI18NText(AssessmentConstants.OUTPUT_NAME_LEARNER_NUMBER_ATTEMPTS, true), numberAttempts);
    }

    /**
     * Get user's score for the question. Will always return a ToolOutput object.
     */
    private ToolOutput getQuestionScore(IAssessmentService assessmentService, Long learnerId, Assessment assessment,
	    int questionSequenceId) {
	Float questionResultMarkDB = assessmentService.getQuestionResultMark(assessment.getUid(), learnerId,
		questionSequenceId);

	float questionResultMark = (questionResultMarkDB == null) ? 0 : questionResultMarkDB;
	return new ToolOutput(String.valueOf(questionSequenceId), "description", questionResultMark);
    }

}
