package org.lamsfoundation.lams.confidencelevel;

import java.util.LinkedList;
import java.util.List;

public class VsaAnswerDTO {

    private Long qbQuestionUid;
    private Long qbOptionUid;
    private String answer;
    private boolean correct;
    private Long userId;
    private List<ConfidenceLevelDTO> confidenceLevels;
    
    public VsaAnswerDTO() {
	this.confidenceLevels = new LinkedList<>();
    }
    
    /**
     */
    public void setQbQuestionUid(Long qbQuestionUid) {
	this.qbQuestionUid = qbQuestionUid;
    }

    public Long getQbQuestionUid() {
	return this.qbQuestionUid;
    }
    
    /**
     */
    public void setQbOptionUid(Long qbOptionUid) {
	this.qbOptionUid = qbOptionUid;
    }

    public Long getQbOptionUid() {
	return this.qbOptionUid;
    }
    
    /**
     */
    public void setAnswer(String answer) {
	this.answer = answer;
    }

    public String getAnswer() {
	return this.answer;
    }
    
    public void setCorrect(boolean correct) {
	this.correct = correct;
    }

    public boolean isCorrect() {
	return this.correct;
    }
    
    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public Long getUserId() {
	return this.userId;
    }
    
    /**
     */
    public void setConfidenceLevels(List<ConfidenceLevelDTO> confidenceLevels) {
	this.confidenceLevels = confidenceLevels;
    }

    /**
     * Each ConfidenceLevelDTO contains userId, userName, portraitUuid, confidence level (if this option was enabled in
     * Assessment)
     * 
     * @return
     */
    public List<ConfidenceLevelDTO> getConfidenceLevels() {
	return this.confidenceLevels;
    }
}
