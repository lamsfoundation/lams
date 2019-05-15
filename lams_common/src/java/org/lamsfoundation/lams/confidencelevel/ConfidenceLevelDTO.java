package org.lamsfoundation.lams.confidencelevel;

public class ConfidenceLevelDTO {

    private Integer userId;
    
    private String userName;
    
    private Long portraitUuid;

    private int level;
    
    private String question;
    
    private String answer;
    
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
    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getUserName() {
	return this.userName;
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
    public void setQuestion(String question) {
	this.question = question;
    }

    public String getQuestion() {
	return this.question;
    }
    
    /**
     */
    public void setAnswer(String answer) {
	this.answer = answer;
    }

    public String getAnswer() {
	return this.answer;
    }
}
