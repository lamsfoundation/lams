package org.lamsfoundation.lams.tool.assessment.dto;

import org.lamsfoundation.lams.qb.model.QbOption;

public class OptionDTO implements Comparable<OptionDTO> {
    
    // ============= immutable properties copied from AssessmentQuestion =============
    
    private Long uid;

    private Integer displayOrder;

    private String matchingPair;

    private String name;

    private float numericalOption;

    private float acceptedError;

    private float maxMark;

    private boolean correct;

    private String feedback;
    
    // ============= variable properties =============
    
    private int answerInt = -1;

    private boolean answerBoolean;
    
    private float percentage;
    
    private String matchingPairEscaped;

    private String nameEscaped;
    
    public OptionDTO(QbOption qbOption) {
	this.uid = qbOption.getUid();
	this.displayOrder = qbOption.getDisplayOrder();
	this.matchingPair = qbOption.getMatchingPair();
	this.name = qbOption.getName();
	this.numericalOption = qbOption.getNumericalOption();
	this.acceptedError = qbOption.getAcceptedError();
	this.maxMark = qbOption.getMaxMark();
	this.correct = qbOption.isCorrect();
	this.feedback = qbOption.getFeedback();
    }
    
    @Override
    public int compareTo(OptionDTO o) {
	return Integer.compare(this.displayOrder, o.displayOrder);
    }
    
    public Long getUid() {
	return uid;
    }
    public void setUid(Long uuid) {
	uid = uuid;
    }

    public int getDisplayOrder() {
	return displayOrder;
    }
    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }

    public String getMatchingPair() {
	return matchingPair;
    }
    public void setMatchingPair(String matchingPair) {
	this.matchingPair = matchingPair;
    }

    public String getName() {
	return name;
    }
    public void setName(String name) {
	this.name = name;
    }

    public float getNumericalOption() {
	return numericalOption;
    }
    public void setNumericalOption(float numericalOption) {
	this.numericalOption = numericalOption;
    }

    public float getAcceptedError() {
	return acceptedError;
    }
    public void setAcceptedError(float acceptedError) {
	this.acceptedError = acceptedError;
    }

    public float getMaxMark() {
	return maxMark;
    }
    public void setMaxMark(float maxMark) {
	this.maxMark = maxMark;
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
    
    public float getPercentage() {
	return percentage;
    }
    
    public void setPercentage(float percentage) {
	this.percentage = percentage;
    }
    
    public String getMatchingPairEscaped() {
	return matchingPairEscaped;
    }

    public void setMatchingPairEscaped(String matchingPairEscaped) {
	this.matchingPairEscaped = matchingPairEscaped;
    }

    public String getNameEscaped() {
	return nameEscaped;
    }

    public void setNameEscaped(String nameEscaped) {
	this.nameEscaped = nameEscaped;
    }
}
