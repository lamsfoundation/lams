package org.lamsfoundation.lams.qb.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * One of possible answers for a question in Question Bank.
 *
 * @author Marcin Cieslak
 */
@Entity
@Table(name = "lams_qb_option")
public class QbOption implements Serializable, Cloneable, Comparable<QbOption> {
    private static final long serialVersionUID = -2354311780882736829L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uid;

    @Column
    private String name;

    @Column(name = "display_order")
    private int displayOrder = 1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "qb_question_uid")
    private QbQuestion qbQuestion;

    // **********************************************************
    // Properties used only in Assessment
    // **********************************************************

    @Column(name = "matching_pair")
    private String matchingPair;

    @Column(name = "numerical_option")
    private float numericalOption;

    @Column(name = "max_mark")
    private float maxMark;

    @Column(name = "accepted_error")
    private float acceptedError;

    @Column
    private String feedback;

    @Override
    public QbOption clone() {
	QbOption clone = null;
	try {
	    clone = (QbOption) super.clone();
	    clone.qbQuestion = null;
	} catch (CloneNotSupportedException e) {
	    // it should never happen
	    e.printStackTrace();
	}
	return clone;
    }

    @Override
    public boolean equals(Object o) {
	QbOption other = (QbOption) o;
	return new EqualsBuilder().append(this.name, other.name).append(this.maxMark, other.maxMark)
		.append(this.displayOrder, other.displayOrder).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(this.name).append(this.maxMark).toHashCode();
    }

    @Override
    public int compareTo(QbOption o) {
	return Integer.compare(this.displayOrder, o.displayOrder);
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public boolean isCorrect() {
	return this.maxMark == 1f;
    }

    public void setCorrect(boolean correct) {
	this.maxMark = correct ? 1 : 0;
    }

    public int getDisplayOrder() {
	return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }

    public QbQuestion getQbQuestion() {
	return qbQuestion;
    }

    public void setQbQuestion(QbQuestion question) {
	this.qbQuestion = question;
    }

    public String getMatchingPair() {
	return matchingPair;
    }

    public void setMatchingPair(String matchingPair) {
	this.matchingPair = matchingPair;
    }

    public float getNumericalOption() {
	return numericalOption;
    }

    public void setNumericalOption(float numericalOption) {
	this.numericalOption = numericalOption;
    }

    public float getAcceptedError() {
	return acceptedError;
    }

    public void setAcceptedError(float acceptedError) {
	this.acceptedError = acceptedError;
    }

    public float getMaxMark() {
	return maxMark;
    }

    public void setMaxMark(float maxMark) {
	this.maxMark = maxMark;
    }

    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }
}