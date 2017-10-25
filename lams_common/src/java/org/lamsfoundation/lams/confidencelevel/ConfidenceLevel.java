package org.lamsfoundation.lams.confidencelevel;

import org.lamsfoundation.lams.usermanagement.User;

/**
 * Object for storing confidence levels left by learners in MCQ and Assessment tools.
 */
public class ConfidenceLevel implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = -6417303272123399980L;

    private Long uid;

    private Long questionUid;

    private User learner;

    private int confidenceLevel;
    
    private Long toolSessionId;

    public ConfidenceLevel() {
    }

    public ConfidenceLevel(Long itemId, User learner, int confidenceLevel) {
	this.questionUid = itemId;
	this.learner = learner;
	this.confidenceLevel = confidenceLevel;
    }

    public ConfidenceLevel(Long itemId, User learner, Long toolSessionId, int confidenceLevel) {
	this.questionUid = itemId;
	this.learner = learner;
	this.toolSessionId = toolSessionId;
	this.confidenceLevel = confidenceLevel;
    }

    /**
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     */
    public Long getQuestionUid() {
	return questionUid;
    }

    public void setQuestionUid(Long itemId) {
	this.questionUid = itemId;
    }

    /**
     */
    public User getLearner() {
	return learner;
    }

    public void setLearner(User learner) {
	this.learner = learner;
    }

    /**
     */
    public void setConfidenceLevel(int confidenceLevel) {
	this.confidenceLevel = confidenceLevel;
    }

    public int getConfidenceLevel() {
	return this.confidenceLevel;
    }

    public Long getToolSessionId() {
	return toolSessionId;
    }

    public void setToolSessionId(Long toolSessionId) {
	this.toolSessionId = toolSessionId;
    }
}
