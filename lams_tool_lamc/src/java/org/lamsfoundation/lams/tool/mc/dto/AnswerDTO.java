/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.dto;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.tool.mc.model.McOptsContent;

/**
 * <p>
 * DTO that holds question and candidate answers for the learner environment
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class AnswerDTO implements Comparable {
    protected String question;

    protected String displayOrder;

    protected Long questionUid;

    protected Integer mark;

    protected McOptsContent answerOption;

    protected List<McOptsContent> options;

    protected boolean attemptCorrect;

    protected String feedbackIncorrect;

    protected String feedbackCorrect;

    protected String feedback;
    
    private int confidenceLevel;

    /**
     * @return Returns the answerOption.
     */
    public McOptsContent getAnswerOption() {
	return answerOption;
    }

    /**
     * @param answerOption
     *            The answerOption to set.
     */
    public void setAnswerOption(McOptsContent answerOption) {
	this.answerOption = answerOption;
    }

    /**
     * @return Returns the options.
     */
    public List<McOptsContent> getOptions() {
	return options;
    }

    /**
     * @param options
     *            The options to set.
     */
    public void setOptions(List<McOptsContent> options) {
	this.options = options;
    }

    /**
     * @return Returns the question.
     */
    public String getQuestion() {
	return question;
    }

    /**
     * @param question
     *            The question to set.
     */
    public void setQuestion(String question) {
	this.question = question;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("question: ", getQuestion()).append("displayOrder: ", getDisplayOrder())
		.append("answerOption: ", getAnswerOption()).toString();
    }

    @Override
    public int compareTo(Object o) {
	AnswerDTO answerDto = (AnswerDTO) o;

	if (answerDto == null) {
	    return 1;
	} else {
	    return 0;
	}
    }

    /**
     * @return Returns the displayOrder.
     */
    public String getDisplayOrder() {
	return displayOrder;
    }

    /**
     * @param displayOrder
     *            The displayOrder to set.
     */
    public void setDisplayOrder(String displayOrder) {
	this.displayOrder = displayOrder;
    }

    /**
     * @return Returns the questionUid.
     */
    public Long getQuestionUid() {
	return questionUid;
    }

    /**
     * @param questionUid
     *            The questionUid to set.
     */
    public void setQuestionUid(Long questionUid) {
	this.questionUid = questionUid;
    }

    /**
     * @return Returns the feedbackCorrect.
     */
    public String getFeedbackCorrect() {
	return feedbackCorrect;
    }

    /**
     * @param feedbackCorrect
     *            The feedbackCorrect to set.
     */
    public void setFeedbackCorrect(String feedbackCorrect) {
	this.feedbackCorrect = feedbackCorrect;
    }

    /**
     * @return Returns the attemptCorrect.
     */
    public boolean isAttemptCorrect() {
	return attemptCorrect;
    }

    /**
     * @param attemptCorrect
     *            The attemptCorrect to set.
     */
    public void setAttemptCorrect(boolean attemptCorrect) {
	this.attemptCorrect = attemptCorrect;
    }

    /**
     * @return Returns the feedbackIncorrect.
     */
    public String getFeedbackIncorrect() {
	return feedbackIncorrect;
    }

    /**
     * @param feedbackIncorrect
     *            The feedbackIncorrect to set.
     */
    public void setFeedbackIncorrect(String feedbackIncorrect) {
	this.feedbackIncorrect = feedbackIncorrect;
    }

    /**
     * @return Returns the mark.
     */
    public Integer getMark() {
	return mark;
    }

    /**
     * @param mark
     *            The mark to set.
     */
    public void setMark(Integer mark) {
	this.mark = mark;
    }

    /**
     * @return Returns the feedback.
     */
    public String getFeedback() {
	return feedback;
    }

    /**
     * @param feedback
     *            The feedback to set.
     */
    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }
    
    public int getConfidenceLevel() {
	return confidenceLevel;
    }

    public void setConfidenceLevel(int confidenceLevel) {
	this.confidenceLevel = confidenceLevel;
    }
}
