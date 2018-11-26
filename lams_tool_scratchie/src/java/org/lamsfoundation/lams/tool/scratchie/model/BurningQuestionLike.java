package org.lamsfoundation.lams.tool.scratchie.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_lascrt11_burning_que_like")
public class BurningQuestionLike {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "burning_question_uid")
    private ScratchieBurningQuestion burningQuestion;

    @Column(name = "session_id")
    private Long sessionId;

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public ScratchieBurningQuestion getBurningQuestion() {
	return burningQuestion;
    }

    public void setBurningQuestion(ScratchieBurningQuestion burningQuestion) {
	this.burningQuestion = burningQuestion;
    }

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

}
