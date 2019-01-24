package org.lamsfoundation.lams.qb.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * Serves as a super class for all tools' questions.
 * Storing references to QB questions in a single table allows easy tracking of a question's usage thorough LAMS.
 *
 * @author Marcin Cieslak
 */
@Entity
@Table(name = "lams_qb_tool_question")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class QbToolQuestion {
    @Id
    @Column(name = "tool_question_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long uid;

    // part of question's data is stored in Question Bank's DB tables
    // getters and setters of this data (question, mark, feedback) are mapped to QbQuestion
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE,
	    CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "qb_question_uid")
    protected QbQuestion qbQuestion;

    public Long getUid() {
	return this.uid;
    }

    public QbQuestion getQbQuestion() {
	return qbQuestion;
    }

    public void setQbQuestion(QbQuestion qbQuestion) {
	this.qbQuestion = qbQuestion;
    }
}
