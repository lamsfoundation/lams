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

/* $Id$ */
package org.lamsfoundation.lams.tool.assessment.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;

/**
 * Assessment Question Result
 * 
 * @author Andrey Balan
 * 
 * @hibernate.class table="tl_laasse10_question_result"
 * 
 */
public class AssessmentQuestionResult {

    private Long uid;
    private AssessmentUser user;
    private AssessmentQuestion assessmentQuestion;
    private String answerString;
    private float answerFloat;
    private boolean answerBoolean;
    private float mark;
    private float penalty;
    private Date startDate;
    private Date finishDate;
    private Long sessionId;
    private Set<AssessmentAnswer> answers;

    // DTO fields:
    private boolean processed;
    
    public AssessmentQuestionResult() {
	answers = new TreeSet(new SequencableComparator());
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the result Uid.
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
    public AssessmentUser getUser() {
	return user;
    }

    public void setUser(AssessmentUser user) {
	this.user = user;
    }
    
    /**
     * @hibernate.many-to-one column="assessment_question_uid" cascade="none"
     * @return
     */
    public AssessmentQuestion getAssessmentQuestion() {
	return assessmentQuestion;
    }

    public void setAssessmentQuestion(AssessmentQuestion question) {
	this.assessmentQuestion = question;
    }
    
    /**
     * @hibernate.property column="answer_string" type="text"
     * 
     * @return Returns the possible answer.
     */
    public String getAnswerString() {
	return answerString;
    }

    public void setAnswerString(String answerString) {
	this.answerString = answerString;
    }
    
    /**
     * @hibernate.property column="answer_float"
     * 
     * @return Returns the possible answer.
     */
    public float getAnswerFloat() {
	return answerFloat;
    }

    public void setAnswerFloat(float answerFloat) {
	this.answerFloat = answerFloat;
    }
    
    /**
     * @hibernate.property column="answer_boolean"
     * 
     * @return Returns the possible answer.
     */
    public boolean getAnswerBoolean() {
	return answerBoolean;
    }

    public void setAnswerBoolean(boolean answerBoolean) {
	this.answerBoolean = answerBoolean;
    }
    
    /**
     * @hibernate.property column="mark"
     * 
     * @return Returns the mark.
     */
    public Float getMark() {
	return mark;
    }

    public void setMark(Float mark) {
	this.mark = mark;
    }
    
    /**
     * @hibernate.property column="penalty"
     * 
     * @return Returns the possible numeric answer.
     */
    public Float getPenalty() {
	return penalty;
    }

    public void setPenalty(Float penalty) {
	this.penalty = penalty;
    }

    /**
     * @hibernate.property column="start_date"
     * @return
     */
    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }
    
    /**
     * @hibernate.property column="finish_date"
     * @return
     */
    public Date getFinishDate() {
	return finishDate;
    }

    public void setFinishDate(Date finishDate) {
	this.finishDate = finishDate;
    }    

    /**
     * @hibernate.property column="session_id"
     * @return
     */
    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }
    
    /**
     * 
     * @hibernate.set cascade="all" order-by="sequence_id asc"
     * @hibernate.collection-key column="result_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.assessment.model.AssessmentAnswer"
     * 
     * @return a set of answerOptions to this AssessmentQuestion.
     */
    public Set<AssessmentAnswer> getAnswers() {
	return answers;
    }

    /**
     * @param answerOptions answerOptions to set.
     */
    public void setAnswers(Set<AssessmentAnswer> answers) {
	this.answers = answers;
    }
    
    public boolean isProcessed() {
	return processed;
    }
    public void setProcessed(boolean processed) {
	this.processed = processed;
    }

}
