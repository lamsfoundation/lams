package org.lamsfoundation.lams.qb.model;

import java.util.Comparator;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

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
public abstract class QbToolQuestion implements Comparable<QbToolQuestion> {
    public static final Comparator<QbToolQuestion> COMPARATOR = Comparator.comparing(QbToolQuestion::getDisplayOrder);

    // it makes sense to put comparator here as an internal class, so we do not need to look for it in other classes
    public static class QbToolQuestionComparator implements Comparator<QbToolQuestion> {
	@Override
	public int compare(QbToolQuestion o1, QbToolQuestion o2) {
	    return COMPARATOR.compare(o1, o2);
	}
    }

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

    @Column(name = "tool_content_id")
    protected Long toolContentId;

    @Column(name = "display_order")
    protected int displayOrder = 1;

    @Override
    public int compareTo(QbToolQuestion other) {
	return COMPARATOR.compare(this, other);
    }

    public Long getUid() {
	return this.uid;
    }

    public QbQuestion getQbQuestion() {
	return qbQuestion;
    }

    public void setQbQuestion(QbQuestion qbQuestion) {
	this.qbQuestion = qbQuestion;
    }

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    public int getDisplayOrder() {
	return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof QbToolQuestion)) {
	    return false;
	}
	QbToolQuestion castOther = (QbToolQuestion) other;
	return new EqualsBuilder().append(this.getUid(), castOther.getUid()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getUid()).toHashCode();
    }

}