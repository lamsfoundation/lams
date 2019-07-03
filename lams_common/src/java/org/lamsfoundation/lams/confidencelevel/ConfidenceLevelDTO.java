package org.lamsfoundation.lams.confidencelevel;

public class ConfidenceLevelDTO {

    private Integer userId;
    
    private String userName;
    
    private Long portraitUuid;

    private int level;
    
    private Long qbQuestionUid;
    
    private Long qbOptionUid;
    
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
}
