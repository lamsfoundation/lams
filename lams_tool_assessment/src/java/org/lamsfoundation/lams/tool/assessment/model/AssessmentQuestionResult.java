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


package org.lamsfoundation.lams.tool.assessment.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Assessment Question Result
 *
 * @author Andrey Balan
 */
public class AssessmentQuestionResult {

    private Long uid;
    private AssessmentQuestion assessmentQuestion;
    private AssessmentResult assessmentResult;
    private String answerString;
    private float answerFloat;
    private boolean answerBoolean;
    private Long submittedOptionUid;
    private float mark;
    private Float maxMark;
    private float penalty;
    private Set<AssessmentOptionAnswer> optionAnswers;
    private Date finishDate;
    private int confidenceLevel;

    // DTO fields:
    private AssessmentUser user;
    private String answerStringEscaped;

    public AssessmentQuestionResult() {
	optionAnswers = new LinkedHashSet<AssessmentOptionAnswer>();
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
	if (!(obj instanceof AssessmentQuestionResult)) {
	    return false;
	}

	final AssessmentQuestionResult genericEntity = (AssessmentQuestionResult) obj;
	return new EqualsBuilder().append(this.getUid(), genericEntity.getUid()).isEquals();
    }

    /**
     *
     * @return Returns the result Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     * @return
     */
    public AssessmentQuestion getAssessmentQuestion() {
	return assessmentQuestion;
    }

    public void setAssessmentQuestion(AssessmentQuestion question) {
	this.assessmentQuestion = question;
    }

    /**
     *
     * @return
     */
    public AssessmentResult getAssessmentResult() {
	return assessmentResult;
    }

    public void setAssessmentResult(AssessmentResult assessmentResult) {
	this.assessmentResult = assessmentResult;
    }

    /**
     *
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
     *
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
     *
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
     *
     *
     * @return Returns submittedOptionUid.
     */
    public Long getSubmittedOptionUid() {
	return submittedOptionUid;
    }

    public void setSubmittedOptionUid(Long submittedOptionUid) {
	this.submittedOptionUid = submittedOptionUid;
    }

    /**
     *
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
     * Maximum mark user could have scored for this question. (It is stored in AssessmentQuestionResult class due to
     * existence of random questions which makes it's impossible to obtain this info from question)
     *
     *
     *
     * @return Returns the mark.
     */
    public Float getMaxMark() {
	return maxMark;
    }

    public void setMaxMark(Float maxMark) {
	this.maxMark = maxMark;
    }

    /**
     *
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
     *
     *
     *
     *
     *
     * @return a set of answerOptions to this AssessmentQuestion.
     */
    public Set<AssessmentOptionAnswer> getOptionAnswers() {
	return optionAnswers;
    }

    /**
     * @param answerOptions
     *            answerOptions to set.
     */
    public void setOptionAnswers(Set<AssessmentOptionAnswer> answers) {
	this.optionAnswers = answers;
    }

    /**
     *
     *
     * @return Returns submittedOptionUid.
     */
    public Date getFinishDate() {
	return finishDate;
    }

    public void setFinishDate(Date finishDate) {
	this.finishDate = finishDate;
    }
    
    public int getConfidenceLevel() {
	return confidenceLevel;
    }

    public void setConfidenceLevel(int confidenceLevel) {
	this.confidenceLevel = confidenceLevel;
    }

    public AssessmentUser getUser() {
	return user;
    }

    public void setUser(AssessmentUser user) {
	this.user = user;
    }

    public String getAnswerStringEscaped() {
	return answerStringEscaped;
    }

    public void setAnswerStringEscaped(String answerStringEscaped) {
	this.answerStringEscaped = answerStringEscaped;
    }

}
