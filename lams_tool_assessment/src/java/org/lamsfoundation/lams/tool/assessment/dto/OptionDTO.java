package org.lamsfoundation.lams.tool.assessment.dto;

import org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption;
import org.lamsfoundation.lams.tool.assessment.model.Sequencable;

public class OptionDTO implements Sequencable{
    
    // ============= immutable properties copied from AssessmentQuestion question =============
    
    private Long uid;

    private Integer sequenceId;

    private String question;

    private String optionString;

    private float optionFloat;

    private float acceptedError;

    private float grade;

    private boolean correct;

    private String feedback;
    
    // ============= variable properties =============
    
    private int answerInt = -1;

    private boolean answerBoolean;
    
    public OptionDTO(AssessmentQuestionOption option) {
	this.uid = option.getUid();
	this.sequenceId = option.getSequenceId();
	this.question = option.getQuestion();
	this.optionString = option.getOptionString();
	this.optionFloat = option.getOptionFloat();
	this.acceptedError = option.getAcceptedError();
	this.grade = option.getGrade();
	this.correct = option.isCorrect();
	this.feedback = option.getFeedback();
    }
    
    public Long getUid() {
	return uid;
    }
    public void setUid(Long uuid) {
	uid = uuid;
    }

    @Override
    public int getSequenceId() {
	return sequenceId;
    }
    @Override
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    public String getQuestion() {
	return question;
    }
    public void setQuestion(String question) {
	this.question = question;
    }

    public String getOptionString() {
	return optionString;
    }
    public void setOptionString(String optionString) {
	this.optionString = optionString;
    }

    public float getOptionFloat() {
	return optionFloat;
    }
    public void setOptionFloat(float optionFloat) {
	this.optionFloat = optionFloat;
    }

    public float getAcceptedError() {
	return acceptedError;
    }
    public void setAcceptedError(float acceptedError) {
	this.acceptedError = acceptedError;
    }

    public float getGrade() {
	return grade;
    }
    public void setGrade(float grade) {
	this.grade = grade;
    }

    public boolean isCorrect() {
	return correct;
    }
    public void setCorrect(boolean correct) {
	this.correct = correct;
    }

    public String getFeedback() {
	return feedback;
    }
    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }
    
    // ============= variable properties =============

    public int getAnswerInt() {
	return answerInt;
    }

    public void setAnswerInt(int answerInt) {
	this.answerInt = answerInt;
    }

    public boolean getAnswerBoolean() {
	return answerBoolean;
    }

    public void setAnswerBoolean(boolean answerBoolean) {
	this.answerBoolean = answerBoolean;
    }

    public String formatPrefixLetter(int index) {
	return new String(Character.toChars(97 + index)) + ")";
    }
}
