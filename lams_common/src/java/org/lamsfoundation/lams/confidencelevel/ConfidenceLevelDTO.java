package org.lamsfoundation.lams.confidencelevel;

public class ConfidenceLevelDTO {

    // confidence levels can be of different types:
    public static final int CONFIDENCE_LEVELS_TYPE_0_TO_100 = 1;
    public static final int CONFIDENCE_LEVELS_TYPE_CONFIDENT = 2;
    public static final int CONFIDENCE_LEVELS_TYPE_SURE = 3;

    private Integer userId;

    private String userName;

    private String portraitUuid;

    private int level;

    //confidenceLevel's type: 1)0 to 100, 2)Confident or 3)Sure
    private int type;

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
    public String getPortraitUuid() {
	return portraitUuid;
    }

    public void setPortraitUuid(String portraitUuid) {
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

    public Integer getType() {
	return type;
    }

    public void setType(Integer type) {
	this.type = type;
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
