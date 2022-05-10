package org.lamsfoundation.lams.qb.model;

import java.util.Comparator;

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
 * @author Marcin Cieslak
 */
@Entity
@Table(name = "lams_qb_tool_answer")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class QbToolAnswer implements Comparable<QbToolAnswer> {
    public static final Comparator<QbToolAnswer> COMPARATOR = Comparator
	    .comparing(a -> a.getQbToolQuestion().getDisplayOrder());

    // it makes sense to put comparator here as an internal class, so we do not need to look for it in other classes
    public static class QbToolAnswerComparator implements Comparator<QbToolAnswer> {
	@Override
	public int compare(QbToolAnswer o1, QbToolAnswer o2) {
	    return COMPARATOR.compare(o1, o2);
	}
    }

    @Id
    @Column(name = "answer_uid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long uid;

    @Column
    protected String answer;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_question_uid")
    protected QbToolQuestion qbToolQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qb_option_uid")
    protected QbOption qbOption;

    @Override
    public int compareTo(QbToolAnswer other) {
	return COMPARATOR.compare(this, other);
    }

    public Long getUid() {
	return this.uid;
    }

    public String getAnswer() {
	return answer;
    }

    public void setAnswer(String answer) {
	this.answer = answer;
    }

    public QbToolQuestion getQbToolQuestion() {
	return qbToolQuestion;
    }

    public void setQbToolQuestion(QbToolQuestion qbToolQuestion) {
	this.qbToolQuestion = qbToolQuestion;
    }

    public QbOption getQbOption() {
	return qbOption;
    }

    public void setQbOption(QbOption qbOption) {
	this.qbOption = qbOption;
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getUid()).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof QbToolAnswer)) {
	    return false;
	}

	QbToolAnswer other = (QbToolAnswer) obj;
	return new EqualsBuilder().append(this.getUid(), other.getUid()).isEquals();
    }

}