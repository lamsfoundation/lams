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

/**
 * Survey
 *
 * @author Dapeng Ni
 *
 * @hibernate.class table="tl_lasurv11_answer"
 *
 */
public class SurveyAnswer {

    private Long uid;

    private SurveyUser user;

    private SurveyQuestion surveyQuestion;
    // options choice string: option UIDs are conjunctioned by &. Such as 2&5&3
    private String answerChoices;
    private String answerText;
    private Date updateDate;

    // ************************************************
    // DTO fields
    // ************************************************
    // it is list of optional UIDs. Uid is long type, but here just save them by String format
    private String[] choices;

    /**
     * @hibernate.many-to-one column="question_uid" cascade="none"
     * @return
     */
    public SurveyQuestion getSurveyQuestion() {
	return surveyQuestion;
    }

    public void setSurveyQuestion(SurveyQuestion item) {
	this.surveyQuestion = item;
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.many-to-one column="user_uid" cascade="none"
     * @return
     */
    public SurveyUser getUser() {
	return user;
    }

    public void setUser(SurveyUser user) {
	this.user = user;
    }

    /**
     * @hibernate.property column="answer_choices"
     * @return
     */
    public String getAnswerChoices() {
	return answerChoices;
    }

    public void setAnswerChoices(String answers) {
	this.answerChoices = answers;
    }

    /**
     * @hibernate.property column="udpate_date"
     * @return
     */
    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     * @hibernate.property column="answer_text"
     * @return
     */
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
