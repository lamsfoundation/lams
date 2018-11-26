/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.survey.model;

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

/**
 * @author Dapeng Ni
 */

@Entity
@Table(name = "tl_lasurv11_answer")
public class SurveyAnswer {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid")
    private SurveyUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_uid")
    private SurveyQuestion surveyQuestion;

    // options choice string: option UIDs are conjunctioned by &. Such as 2&5&3
    @Column(name = "answer_choices")
    private String answerChoices;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "udpate_date")
    private Date updateDate;

    // it is list of optional UIDs. Uid is long type, but here just save them by String format
    @Transient
    private String[] choices;

    public SurveyQuestion getSurveyQuestion() {
	return surveyQuestion;
    }

    public void setSurveyQuestion(SurveyQuestion item) {
	this.surveyQuestion = item;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public SurveyUser getUser() {
	return user;
    }

    public void setUser(SurveyUser user) {
	this.user = user;
    }

    public String getAnswerChoices() {
	return answerChoices;
    }

    public void setAnswerChoices(String answers) {
	this.answerChoices = answers;
    }

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public String getAnswerText() {
	return answerText;
    }

    public void setAnswerText(String textEntry) {
	this.answerText = textEntry;
    }

    public String[] getChoices() {
	return choices;
    }

    public void setChoices(String[] choices) {
	this.choices = choices;
    }
}