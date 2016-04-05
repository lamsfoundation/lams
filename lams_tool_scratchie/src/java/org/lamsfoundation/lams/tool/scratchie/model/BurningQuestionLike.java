package org.lamsfoundation.lams.tool.scratchie.model;

/**
 * @author Andrey Balan
 * 
 * @hibernate.class table="tl_lascrt11_burning_que_like"
 */
public class BurningQuestionLike {

    private Long uid;
    private ScratchieBurningQuestion burningQuestion;
    private Long sessionId;

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }
	
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.many-to-one column="burning_question_uid" cascade="none"
     * @return
     */
    public ScratchieBurningQuestion getBurningQuestion() {
	return burningQuestion;
    }

    public void setBurningQuestion(ScratchieBurningQuestion burningQuestion) {
	this.burningQuestion = burningQuestion;
    }

    /**
     * @hibernate.property column="session_id"
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

}
