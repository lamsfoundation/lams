package org.lamsfoundation.lams.qb;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * One of possible answers for a question in Question Bank.
 *
 * @author Marcin Cieslak
 */
@Entity
@Table(name = "lams_qb_option")
public class QbOption implements Serializable, Cloneable {
    private static final long serialVersionUID = -2354311780882736829L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String name;

    @Column
    private boolean correct = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "qb_question_uid")
    private QbQuestion question;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public boolean isCorrect() {
	return correct;
    }

    public void setCorrect(boolean correct) {
	this.correct = correct;
    }

    public QbQuestion getQuestion() {
	return question;
    }

    public void setQuestion(QbQuestion question) {
	this.question = question;
    }

    public Long getUid() {
	return uid;
    }
}