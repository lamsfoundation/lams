package org.lamsfoundation.lams.tool.assessment.dto;

import java.util.List;

/**
 * TBL modification of Assessment tool's QuestionSummary.
 */
public class TblAssessmentQuestionDTO {
    private String title;
    private String questionTypeLabel;
    private String correctAnswer;
    private List<TblAssessmentQuestionResultDTO> sessionQuestionResults;
    
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
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

    public List<TblAssessmentQuestionResultDTO> getSessionQuestionResults() {
	return sessionQuestionResults;
    }

    public void setSessionQuestionResults(List<TblAssessmentQuestionResultDTO> sessionQuestionResults) {
	this.sessionQuestionResults = sessionQuestionResults;
    }
}
