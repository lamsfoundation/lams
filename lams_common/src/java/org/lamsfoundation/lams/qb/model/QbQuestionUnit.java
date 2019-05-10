package org.lamsfoundation.lams.qb.model;

import java.io.Serializable;

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
 * Unit for a numerical type of questions in Question Bank.
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "lams_qb_question_unit")
public class QbQuestionUnit implements Serializable, Cloneable, Comparable<QbQuestionUnit> {
    private static final long serialVersionUID = -6772525485898794744L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long uid;

    @Column
    private String name;
    
    @Column
    private float multiplier = 0;

    @Column(name = "display_order")
    private int displayOrder = 1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "qb_question_uid")
    private QbQuestion qbQuestion;

    @Override
    public QbQuestionUnit clone() {
	QbQuestionUnit clone = null;
	try {
	    clone = (QbQuestionUnit) super.clone();
	    clone.qbQuestion = null;
	} catch (CloneNotSupportedException e) {
	    // it should never happen
	    e.printStackTrace();
	}
	return clone;
    }

    @Override
    public boolean equals(Object o) {
	QbQuestionUnit other = (QbQuestionUnit) o;
	return new EqualsBuilder().append(this.name, other.name).append(this.multiplier, other.multiplier)
		.append(this.displayOrder, other.displayOrder).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(this.name).append(this.multiplier).toHashCode();
    }

    @Override
    public int compareTo(QbQuestionUnit o) {
	return Integer.compare(this.displayOrder, o.displayOrder);
    }

    public Long getUid() {
	return uid;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public float getMultiplier() {
	return multiplier;
    }

    public void setMultiplier(float multiplier) {
	this.multiplier = multiplier;
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

}