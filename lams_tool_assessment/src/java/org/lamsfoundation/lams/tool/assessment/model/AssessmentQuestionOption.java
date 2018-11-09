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


package org.lamsfoundation.lams.tool.assessment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * AssessmentQuestionOption
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laasse10_question_option")
public class AssessmentQuestionOption implements Cloneable, Sequencable {
    private static final Logger log = Logger.getLogger(AssessmentQuestionOption.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "sequence_id")
    private Integer sequenceId;

    @Column
    private String question;

    @Column(name = "option_string")
    private String optionString;

    @Column(name = "option_float")
    private float optionFloat;

    @Column(name = "accepted_error")
    private float acceptedError;

    @Column
    private float grade;

    @Column
    private boolean correct;

    @Column
    private String feedback;

    // *************** DTO fields (used in monitoring) ********************
    @Transient
    private String questionEscaped;
    @Transient
    private String optionStringEscaped;
    @Transient
    private float percentage;

    @Override
    public Object clone() {
	AssessmentQuestionOption obj = null;
	try {
	    obj = (AssessmentQuestionOption) super.clone();
	    obj.setUid(null);
	} catch (CloneNotSupportedException e) {
	    AssessmentQuestionOption.log.error("When clone " + AssessmentQuestionOption.class + " failed");
	}

	return obj;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uuid) {
	uid = uuid;
    }

    /**
     * @return option's sequence number
     */
    @Override
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets option's sequence number.
     *
     * @param sequenceId
     *            option's sequence number
     */
    @Override
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     *
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
     *
     *
     * @return Returns the possible answer.
     */
    public String getOptionString() {
	return optionString;
    }

    public void setOptionString(String optionString) {
	this.optionString = optionString;
    }

    /**
     *
     *
     * @return Returns the possible numeric answer.
     */
    public float getOptionFloat() {
	return optionFloat;
    }

    public void setOptionFloat(float optionFloat) {
	this.optionFloat = optionFloat;
    }

    /**
     *
     *
     * @return Returns the possible answer.
     */
    public float getAcceptedError() {
	return acceptedError;
    }

    public void setAcceptedError(float acceptedError) {
	this.acceptedError = acceptedError;
    }

    /**
     * Returns image grade.
     *
     * @return image grade
     *
     *
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
     *
     * @return
     */
    public boolean isCorrect() {
	return correct;
    }

    public void setCorrect(boolean correct) {
	this.correct = correct;
    }

    /**
     *
     *
     * @return Returns feedback on this answer option.
     */
    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    public String getQuestionEscaped() {
	return questionEscaped;
    }

    public void setQuestionEscaped(String questionEscaped) {
	this.questionEscaped = questionEscaped;
    }

    public String getOptionStringEscaped() {
	return optionStringEscaped;
    }

    public void setOptionStringEscaped(String optionStringEscaped) {
	this.optionStringEscaped = optionStringEscaped;
    }
    
    public float getPercentage() {
	return percentage;
    }
    
    public void setPercentage(float percentage) {
	this.percentage = percentage;
    }
}
