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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.assessment.model;

import org.apache.log4j.Logger;

/**
 * AssessmentAnswerOption
 * 
 * @author Andrey Balan
 * 
 * @hibernate.class table="tl_laasse10_answer_options"
 */
public class AssessmentAnswerOption implements Cloneable {
    private static final Logger log = Logger.getLogger(AssessmentAnswerOption.class);

    private Long uid;

    private Integer sequenceId;
    
    private String question;

    private String answerString;
    
    private Long answerLong;
    
    private Long acceptedError;
    
    private float grade;
    
    private String feedback;

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    /**
     * @hibernate.id generator-class="native" column="uid"
     * @return Returns the answer ID.
     */
    public Long getUid() {
	return uid;
    }

    private void setUid(Long uuid) {
	uid = uuid;
    }

    /**
     * Returns image sequence number.
     * 
     * @return image sequence number
     * 
     * @hibernate.property column="sequence_id"
     */
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets image sequence number.
     * 
     * @param sequenceId
     *                image sequence number
     */
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     * @hibernate.property column="question" type="text"
     * 
     * @return Returns the possible answer.
     */
    public String getQuestion() {
	return question;
    }

    public void setQuestion(String question) {
	this.question = question;
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
     * @hibernate.property column="answer_long"
     * 
     * @return Returns the possible numeric answer.
     */
    public Long getAnswerLong() {
	return answerLong;
    }

    public void setAnswerLong(Long answerLong) {
	this.answerLong = answerLong;
    }

    /**
     * @hibernate.property column="accepted_error"
     * 
     * @return Returns the possible  answer.
     */
    public Long getAcceptedError() {
	return acceptedError;
    }

    public void setAcceptedError(Long acceptedError) {
	this.acceptedError = acceptedError;
    }
    
    /**
     * Returns image grade.
     * 
     * @return image grade
     * 
     * @hibernate.property column="grade"
     */
    public float getGrade() {
	return grade;
    }

    /**
     * Sets image grade.
     * 
     * @param grade
     *            image grade
     */
    public void setGrade(float grade) {
	this.grade = grade;
    }
    
    /**
     * @hibernate.property column="feedback" type="text"
     * 
     * @return Returns feedback on this answer option.
     */
    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    @Override
    public Object clone() {
	AssessmentAnswerOption obj = null;
	try {
	    obj = (AssessmentAnswerOption) super.clone();
	    obj.setUid(null);
	} catch (CloneNotSupportedException e) {
	    AssessmentAnswerOption.log.error("When clone " + AssessmentAnswerOption.class + " failed");
	}

	return obj;
    }
}
