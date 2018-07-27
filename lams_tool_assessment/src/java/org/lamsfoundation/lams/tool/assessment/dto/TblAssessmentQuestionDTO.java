package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.List;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion;

/**
 * TBL modification of Assessment tool's QuestionSummary.
 */
public class TblAssessmentQuestionDTO {
    private AssessmentQuestion question;
    private List<TblAssessmentQuestionResultDTO> sessionQuestionResults;

    private String questionTypeLabel;
    private String correctAnswer;

    public AssessmentQuestion getQuestion() {
	return question;
    }

    public void setQuestion(AssessmentQuestion question) {
	this.question = question;
    }

    public List<TblAssessmentQuestionResultDTO> getSessionQuestionResults() {
	return sessionQuestionResults;
    }

    public void setSessionQuestionResults(List<TblAssessmentQuestionResultDTO> sessionQuestionResults) {
	this.sessionQuestionResults = sessionQuestionResults;
    }

    public String getQuestionTypeLabel() {
	return questionTypeLabel;
    }

    public void setQuestionTypeLabel(String questionTypeLabel) {
	this.questionTypeLabel = questionTypeLabel;
    }

    public String getCorrectAnswer() {
	return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
	this.correctAnswer = correctAnswer;
    }
}
