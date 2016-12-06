package org.lamsfoundation.lams.tool.scratchie.model;

/**
 * @author Andrey Balan
 */
public class BurningQuestionLike {

    private Long uid;
    private ScratchieBurningQuestion burningQuestion;
    private Long sessionId;

    /**
     *
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     * @return
     */
    public ScratchieBurningQuestion getBurningQuestion() {
	return burningQuestion;
    }

    public void setBurningQuestion(ScratchieBurningQuestion burningQuestion) {
	this.burningQuestion = burningQuestion;
    }

    /**
     *
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

}
