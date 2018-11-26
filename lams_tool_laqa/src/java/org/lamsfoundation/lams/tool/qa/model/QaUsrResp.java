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
import java.util.Date;

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
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;

/**
 * Holds user responses to questions
 *
 * QaUsrResp Value Object The value object that maps to our model database
 * table: tl_laqa11_usr_resp The relevant hibernate mapping resides in:
 * QaQueResp.hbm.xml
 *
 * @author Ozgur Demirtas
 */

@Entity
@Table(name = "tl_laqa11_usr_resp")
public class QaUsrResp implements Serializable, Comparable {

    @Id
    @Column(name = "response_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;

    @Column
    private String answer;

    @Column(name = "answer_autosaved")
    private String answerAutosaved;

    @Column(name = "attempt_time")
    private Date attemptTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qa_que_content_id")
    private QaQueContent qaQuestion;

    @Column
    private boolean visible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "que_usr_id")
    private QaQueUsr qaQueUser;

    @Column(name = "time_zone")
    private String timezone;

    @Transient
    private ItemRatingDTO itemRatingDto;

    @Transient
    private Long portraitId;

    /** full constructor */
    public QaUsrResp(Long responseId, String answer, String answerAutosaved, Date attemptTime, String timezone,
	    QaQueContent qaQuestion, QaQueUsr qaQueUser) {
	this.responseId = responseId;
	this.answer = answer;
	this.answerAutosaved = answerAutosaved;
	this.attemptTime = attemptTime;
	this.timezone = timezone;
	this.qaQuestion = qaQuestion;
	this.qaQueUser = qaQueUser;
    }

    public QaUsrResp(String answer, String answerAutosaved, Date attemptTime, String timezone, QaQueContent qaQuestion,
	    QaQueUsr qaQueUser, boolean visible) {
	this.answer = answer;
	this.answerAutosaved = answerAutosaved;
	this.attemptTime = attemptTime;
	this.timezone = timezone;
	this.qaQuestion = qaQuestion;
	this.qaQueUser = qaQueUser;
	this.visible = visible;
    }

    /** default constructor */
    public QaUsrResp() {
    }

    /**
     * Copy construtor. Delegate to full construtor to achieve the object
     * creation.
     *
     * @param response
     *            the original survey user response
     * @return the new qa user response cloned from original object
     */
    public static QaUsrResp newInstance(QaUsrResp response) {
	return new QaUsrResp(response.getResponseId(), response.getAnswer(), response.getAnswerAutosaved(),
		response.getAttemptTime(), response.getTimezone(), response.getQaQuestion(), response.qaQueUser);
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("responseId: ", getResponseId()).append("answer:", getAnswer())
		.append("attempt time: ", getAttemptTime()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof QaUsrResp)) {
	    return false;
	}
	QaUsrResp castOther = (QaUsrResp) other;
	return new EqualsBuilder().append(this.getResponseId(), castOther.getResponseId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getResponseId()).toHashCode();
    }

    /**
     * @return Returns the answer.
     */
    public String getAnswer() {
	return answer;
    }

    /**
     * @param answer
     *            The answer to set.
     */
    public void setAnswer(String answer) {
	this.answer = answer;
    }

    /**
     * @return Returns the answerAutosaved.
     */
    public String getAnswerAutosaved() {
	return answerAutosaved;
    }

    /**
     * @param answerAutosaved
     *            The answerAutosaved to set.
     */
    public void setAnswerAutosaved(String answerAutosaved) {
	this.answerAutosaved = answerAutosaved;
    }

    /**
     * @return Returns the attemptTime.
     */
    public Date getAttemptTime() {
	return attemptTime;
    }

    /**
     * @param attemptTime
     *            The attemptTime to set.
     */
    public void setAttemptTime(Date attemptTime) {
	this.attemptTime = attemptTime;
    }

    /**
     * @return Returns the qaQuestion.
     */
    public QaQueContent getQaQuestion() {
	return qaQuestion;
    }

    /**
     * @param qaQuestion
     *            The qaQuestion to set.
     */
    public void setQaQuestion(QaQueContent qaQuestion) {
	this.qaQuestion = qaQuestion;
    }

    /**
     * @return Returns the qaQueUsr.
     */
    public QaQueUsr getQaQueUser() {
	return qaQueUser;
    }

    /**
     * @param qaQueUsr
     *            The qaQueUsr to set.
     */
    public void setQaQueUser(QaQueUsr qaQueUser) {
	this.qaQueUser = qaQueUser;
    }

    /**
     * @return Returns the responseId.
     */
    public Long getResponseId() {
	return responseId;
    }

    /**
     * @param responseId
     *            The responseId to set.
     */
    public void setResponseId(Long responseId) {
	this.responseId = responseId;
    }

    @Override
    public int compareTo(Object o) {
	QaUsrResp response = (QaUsrResp) o;

	if (responseId == null) {
	    return -1;
	}
	if (response.responseId == null) {
	    return 1;
	}

	return (int) (responseId.longValue() - response.responseId.longValue());
    }

    /**
     * @return Returns the timezone.
     */
    public String getTimezone() {
	return timezone;
    }

    /**
     * @param timezone
     *            The timezone to set.
     */
    public void setTimezone(String timezone) {
	this.timezone = timezone;
    }

    /**
     * @return Returns the visible.
     */
    public boolean isVisible() {
	return visible;
    }

    /**
     * @param visible
     *            The visible to set.
     */
    public void setVisible(boolean visible) {
	this.visible = visible;
    }

    public ItemRatingDTO getItemRatingDto() {
	return itemRatingDto;
    }

    public void setItemRatingDto(ItemRatingDTO itemRatingDto) {
	this.itemRatingDto = itemRatingDto;
    }

    public Long getPortraitId() {
	return portraitId;
    }

    public void setPortraitId(Long portraitId) {
	this.portraitId = portraitId;
    }

}
