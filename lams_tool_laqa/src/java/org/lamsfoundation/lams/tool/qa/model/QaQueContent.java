/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.qa.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.tool.qa.Nullable;

/**
 *
 * @author Ozgur Demirtas
 *
 *         The value object that maps to our model database table: tl_laqa11_que_content The relevant hibernate mapping
 *         resides in: QaQueContent.hbm.xml
 *
 *         Holds question content within a particular content
 */
@Entity
@Table(name = "tl_laqa11_que_content")
public class QaQueContent implements Serializable, Comparable, Nullable {

    private static final long serialVersionUID = -4028785701106936621L;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String question;

    @Column(name = "display_order")
    private int displayOrder;

    @Column
    private String feedback;

    @Column(name = "answer_required")
    private boolean required;

    @Column(name = "min_words_limit")
    private int minWordsLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qa_content_id")
    private QaContent qaContent;
    
    // *************** NON Persist Fields used in learning ********************
    @Transient
    private QaUsrResp userResponse;

    /** default constructor */
    public QaQueContent() {
    }

    public QaQueContent(String question, int displayOrder, String feedback, boolean required, int minWordsLimit,
	    QaContent qaContent) {
	this.question = question;
	this.displayOrder = displayOrder;
	this.feedback = feedback;
	this.required = required;
	this.minWordsLimit = minWordsLimit;
	this.qaContent = qaContent;
    }

    public static QaQueContent newInstance(QaQueContent queContent, QaContent newQaContent) {
	QaQueContent newQueContent = new QaQueContent(queContent.getQuestion(), queContent.getDisplayOrder(),
		queContent.getFeedback(), queContent.isRequired(), queContent.minWordsLimit, newQaContent);
	return newQueContent;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("qaQueContentId: ", getUid()).append("question: ", getQuestion())
		.append("displayOrder: ", getDisplayOrder()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof QaQueContent)) {
	    return false;
	}
	QaQueContent castOther = (QaQueContent) other;
	return new EqualsBuilder().append(this.getUid(), castOther.getUid()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getUid()).toHashCode();
    }

    /**
     * @return Returns the displayOrder.
     */
    public int getDisplayOrder() {
	return displayOrder;
    }

    /**
     * @param required
     *            Does this question have to be answered.
     */
    public void setRequired(boolean required) {
	this.required = required;
    }

    /**
     * @return Does this question have to be answered.
     */
    public boolean isRequired() {
	return required;
    }

    /**
     * @param minWordsLimit
     *            minWordsLimit
     */
    public void setMinWordsLimit(int minWordsLimit) {
	this.minWordsLimit = minWordsLimit;
    }

    /**
     * @return minWordsLimit
     */
    public int getMinWordsLimit() {
	return minWordsLimit;
    }

    /**
     * @param displayOrder
     *            The displayOrder to set.
     */
    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }

    /**
     * @return Returns the qaContent.
     */
    public org.lamsfoundation.lams.tool.qa.model.QaContent getQaContent() {
	return qaContent;
    }

    /**
     * @param qaContent
     *            The qaContent to set.
     */
    public void setQaContent(org.lamsfoundation.lams.tool.qa.model.QaContent qaContent) {
	this.qaContent = qaContent;
    }

    /**
     * @return Returns the question.
     */
    public String getQuestion() {
	return question;
    }

    /**
     * @param question
     *            The question to set.
     */
    public void setQuestion(String question) {
	this.question = question;
    }

    @Override
    public boolean isNull() {
	return false;
    }

    @Override
    public int compareTo(Object o) {
	//QaQueContent queContent = (QaQueContent) o;

	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	/*
	 * if (uid == null) return 1; else return (int) (uid.longValue() - queContent.uid.longValue());
	 */
	return 1;
    }

    /**
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the feedback.
     */
    public String getFeedback() {
	return feedback;
    }

    /**
     * @param feedback
     *            The feedback to set.
     */
    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }
    
    // *************** NON Persist Fields used in monitoring ********************

    public QaUsrResp getUserResponse() {
	return userResponse;
    }

    public void setUserResponse(QaUsrResp userResponse) {
	this.userResponse = userResponse;
    }
}
