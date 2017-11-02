package org.lamsfoundation.lams.confidencelevel;

public class ConfidenceLevelDTO {

    private Integer userId;
    
    private Long portraitUuid;

    private int level;
    
    private String questionHash;
    
    private String answerHash;
    
    /**
     */
    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    /**
     */
    public Long getPortraitUuid() {
	return portraitUuid;
    }

    public void setPortraitUuid(Long portraitUuid) {
	this.portraitUuid = portraitUuid;
    }

    /**
     */
    public void setLevel(int level) {
	this.level = level;
    }

    public int getLevel() {
	return this.level;
    }
    
    /**
     */
    public void setQuestionHash(String questionHash) {
	this.questionHash = questionHash;
    }

    public String getQuestionHash() {
	return this.questionHash;
    }
    
    /**
     */
    public void setAnswerHash(String answerHash) {
	this.answerHash = answerHash;
    }

    public String getAnswerHash() {
	return this.answerHash;
    }
}
