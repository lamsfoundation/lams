package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.Set;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionResult;

/**
 * DTO for AssessmentResult.java.
 *
 * @author Andrey Balan
 */
public class AssessmentResultDTO {

    private Long sessionId;
    private Set<AssessmentQuestionResult> questionResults;

    /**
     *
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    /**
     * @return a set of answerOptions to this AssessmentQuestion.
     */
    public Set<AssessmentQuestionResult> getQuestionResults() {
	return questionResults;
    }

    /**
     * @param answerOptions
     *            answerOptions to set.
     */
    public void setQuestionResults(Set<AssessmentQuestionResult> questionResults) {
	this.questionResults = questionResults;
    }

}
