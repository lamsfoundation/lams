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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.lamsfoundation.lams.qb.model.QbToolAnswer;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;

/**
 * Holds user responses to questions
 *
 * @author Ozgur Demirtas
 */
@Entity
@Table(name = "tl_laqa11_usr_resp")
//in this entity's table primary key is "uid", but it references "answer_uid" in lams_qb_tool_answer
@PrimaryKeyJoinColumn(name = "uid")
public class QaUsrResp extends QbToolAnswer implements Serializable {

    private static final long serialVersionUID = 3446870699674533029L;

    @Column(name = "answer_autosaved")
    private String answerAutosaved;

    @Column(name = "attempt_time")
    private Date attemptTime;

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
    private String portraitId;

    /** full constructor */
    public QaUsrResp(Long uid, String answer, String answerAutosaved, Date attemptTime, String timezone,
	    QaQueContent qaQuestion, QaQueUsr qaQueUser) {
	this.uid = uid;
	this.answer = answer;
	this.answerAutosaved = answerAutosaved;
	this.attemptTime = attemptTime;
	this.timezone = timezone;
	this.qbToolQuestion = qaQuestion;
	this.qaQueUser = qaQueUser;
    }

    public QaUsrResp(String answer, String answerAutosaved, Date attemptTime, String timezone, QaQueContent qaQuestion,
	    QaQueUsr qaQueUser, boolean visible) {
	this(null, answer, answerAutosaved, attemptTime, timezone, qaQuestion, qaQueUser);
	this.visible = visible;
    }

    /** default constructor */
    public QaUsrResp() {
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid: ", getUid()).append("answer:", getAnswer())
		.append("attempt time: ", getAttemptTime()).toString();
    }

    public String getAnswerAutosaved() {
	return answerAutosaved;
    }

    public void setAnswerAutosaved(String answerAutosaved) {
	this.answerAutosaved = answerAutosaved;
    }

    public Date getAttemptTime() {
	return attemptTime;
    }

    public void setAttemptTime(Date attemptTime) {
	this.attemptTime = attemptTime;
    }

    public QaQueContent getQaQuestion() {
	return (QaQueContent) Hibernate.unproxy(qbToolQuestion);
    }

    public void setQaQuestion(QaQueContent qaQuestion) {
	this.qbToolQuestion = qaQuestion;
    }

    public QaQueUsr getQaQueUser() {
	return qaQueUser;
    }

    public void setQaQueUser(QaQueUsr qaQueUser) {
	this.qaQueUser = qaQueUser;
    }

    public String getTimezone() {
	return timezone;
    }

    public void setTimezone(String timezone) {
	this.timezone = timezone;
    }

    public boolean isVisible() {
	return visible;
    }

    public void setVisible(boolean visible) {
	this.visible = visible;
    }

    public ItemRatingDTO getItemRatingDto() {
	return itemRatingDto;
    }

    public void setItemRatingDto(ItemRatingDTO itemRatingDto) {
	this.itemRatingDto = itemRatingDto;
    }

    public String getPortraitId() {
	return portraitId;
    }

    public void setPortraitId(String portraitId) {
	this.portraitId = portraitId;
    }
}