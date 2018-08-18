package org.lamsfoundation.lams.tool.assessment.dto;

public class TblAssessmentQuestionResultDTO {

    private boolean correct;
    private String answer;

    public boolean isCorrect() {
	return correct;
    }

    public void setCorrect(boolean correct) {
	this.correct = correct;
    }

    public String getAnswer() {
	return answer;
    }

    public void setAnswer(String answer) {
	this.answer = answer;
    }

}
