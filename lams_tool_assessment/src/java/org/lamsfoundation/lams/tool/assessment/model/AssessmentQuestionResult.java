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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringEscapeUtils;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbToolAnswer;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;

/**
 * Assessment Question Result
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laasse10_question_result")
//in this entity's table primary key is "uid", but it references "answer_uid" in lams_qb_tool_answer
@PrimaryKeyJoinColumn(name = "uid")
public class AssessmentQuestionResult extends QbToolAnswer {

    @Column(name = "answer_float")
    private float answerFloat;

    @Column(name = "answer_boolean")
    private boolean answerBoolean;

    @Column
    private float mark;

    @Column(name = "max_mark")
    private Float maxMark;

    @Column
    private float penalty;

    @Column(name = "finish_date")
    private Date finishDate;

    @Column(name = "confidence_level")
    private int confidenceLevel;

    @Column
    private String justification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by")
    private AssessmentUser markedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_uid")
    private AssessmentResult assessmentResult;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_result_uid")
    private Set<AssessmentOptionAnswer> optionAnswers = new LinkedHashSet<>();

    // *************** NON Persist Fields ********************

    @Transient
    private AssessmentUser user;
    @Transient
    private String answerEscaped;
    @Transient
    private QuestionDTO questionDto;
    @Transient
    private boolean answerModified;

    public QbQuestion getQbQuestion() {
	return qbToolQuestion.getQbQuestion();
    }

    public AssessmentResult getAssessmentResult() {
	return assessmentResult;
    }

    public void setAssessmentResult(AssessmentResult assessmentResult) {
	this.assessmentResult = assessmentResult;
    }

    /**
     * @return Returns the possible answer.
     */
    public float getAnswerFloat() {
	return answerFloat;
    }

    public void setAnswerFloat(float answerFloat) {
	this.answerFloat = answerFloat;
    }

    /**
     * @return Returns the possible answer.
     */
    public boolean getAnswerBoolean() {
	return answerBoolean;
    }

    public void setAnswerBoolean(boolean answerBoolean) {
	this.answerBoolean = answerBoolean;
    }

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
     * @return Returns the mark.
     */
    public Float getMaxMark() {
	return maxMark;
    }

    public void setMaxMark(Float maxMark) {
	this.maxMark = maxMark;
    }

    /**
     * @return Returns the possible numeric answer.
     */
    public Float getPenalty() {
	return penalty;
    }

    public void setPenalty(Float penalty) {
	this.penalty = penalty;
    }

    /**
     * @return a set of answerOptions to this AssessmentQuestion.
     */
    public Set<AssessmentOptionAnswer> getOptionAnswers() {
	return optionAnswers;
    }

    public void setOptionAnswers(Set<AssessmentOptionAnswer> answers) {
	this.optionAnswers = answers;
    }

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

    public String getJustification() {
	return justification;
    }

    public void setJustification(String justification) {
	this.justification = justification;
    }

    public String getJustificationEscaped() {
	return justification == null ? null : StringEscapeUtils.escapeJavaScript(justification.replace("\r\n", "<br>"));
    }

    public AssessmentUser getMarkedBy() {
	return markedBy;
    }

    public void setMarkedBy(AssessmentUser markedBy) {
	this.markedBy = markedBy;
    }

    public AssessmentUser getUser() {
	return user;
    }

    public void setUser(AssessmentUser user) {
	this.user = user;
    }

    public String getanswerEscaped() {
	return answerEscaped;
    }

    public void setanswerEscaped(String answerEscaped) {
	this.answerEscaped = answerEscaped;
    }

    public QuestionDTO getQuestionDto() {
	return questionDto;
    }

    public void setQuestionDto(QuestionDTO questionDto) {
	this.questionDto = questionDto;
    }

    public boolean isAnswerModified() {
	return answerModified;
    }

    public void setAnswerModified(boolean answerModified) {
	this.answerModified = answerModified;
    }
}