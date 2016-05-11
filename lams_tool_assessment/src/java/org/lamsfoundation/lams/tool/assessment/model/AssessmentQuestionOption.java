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

import org.apache.log4j.Logger;

/**
 * AssessmentQuestionOption
 *
 * @author Andrey Balan
 *
 * @hibernate.class table="tl_laasse10_question_option"
 */
public class AssessmentQuestionOption implements Cloneable, Sequencable {
    private static final Logger log = Logger.getLogger(AssessmentQuestionOption.class);

    private Long uid;

    private Integer sequenceId;

    private String question;

    private String optionString;

    private float optionFloat;

    private float acceptedError;

    private float grade;

    private boolean correct;

    private String feedback;

    // *************** NON Persist Fields ********************
    private int answerInt = -1;

    private boolean answerBoolean;

    private String questionEscaped;

    private String optionStringEscaped;

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

    public void setUid(Long uuid) {
	uid = uuid;
    }

    /**
     * Returns option's sequence number.
     *
     * @return option's sequence number
     *
     * @hibernate.property column="sequence_id"
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
     * @hibernate.property column="option_string" type="text"
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
     * @hibernate.property column="option_float"
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
     * @hibernate.property column="accepted_error"
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
     * @hibernate.property column="correct"
     * @return
     */
    public boolean isCorrect() {
	return correct;
    }

    public void setCorrect(boolean correct) {
	this.correct = correct;
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

    public int getAnswerInt() {
	return answerInt;
    }

    public void setAnswerInt(int answerInt) {
	this.answerInt = answerInt;
    }

    public boolean getAnswerBoolean() {
	return answerBoolean;
    }

    public void setAnswerBoolean(boolean answerBoolean) {
	this.answerBoolean = answerBoolean;
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
}
