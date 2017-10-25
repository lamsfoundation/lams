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

package org.lamsfoundation.lams.tool.assessment.model;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.assessment.dto.QuestionDTO;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;

/**
 * Assessment Question
 *
 * @author Andrey Balan
 */
public class AssessmentQuestion implements Cloneable, Sequencable, Comparable {
    private static final Logger log = Logger.getLogger(AssessmentQuestion.class);

    private Long uid;

    private short type;

    private String title;

    private String question;
    
    /**
     * It stores sha1(title + description) value that allows us to search for the AssessmentQuestions with the same title and question
     */
    private String questionHash;

    private int sequenceId;

    /**
     * Default grade set in author.
     */
    private int defaultGrade;

    private float penaltyFactor;

    private boolean answerRequired;

    private String generalFeedback;

    private String feedback;

    private boolean multipleAnswersAllowed;

    private boolean incorrectAnswerNullifiesMark;

    private String feedbackOnCorrect;

    private String feedbackOnPartiallyCorrect;

    private String feedbackOnIncorrect;

    private boolean shuffle;

    private boolean caseSensitive;

    private boolean correctAnswer;

    private boolean allowRichEditor;

    private Set<AssessmentQuestionOption> options;

    private Set<AssessmentUnit> units;

    // only for essay type of question
    private int maxWordsLimit;
    // only for essay type of question
    private int minWordsLimit;

    // only for hedging mark type of question
    private boolean hedgingJustificationEnabled;

    // *************** NON Persist Fields used in monitoring ********************
    
    private String titleEscaped;

    public AssessmentQuestion() {
	options = new TreeSet<AssessmentQuestionOption>(new SequencableComparator());
	units = new TreeSet<AssessmentUnit>(new SequencableComparator());
    }

    @Override
    public Object clone() {
	AssessmentQuestion obj = null;
	try {
	    obj = (AssessmentQuestion) super.clone();
	    obj.setUid(null);

	    // clone options
	    if (options != null) {
		Iterator<AssessmentQuestionOption> iter = options.iterator();
		Set<AssessmentQuestionOption> set = new TreeSet<AssessmentQuestionOption>(new SequencableComparator());
		while (iter.hasNext()) {
		    AssessmentQuestionOption answerOption = iter.next();
		    AssessmentQuestionOption newAnswerOption = (AssessmentQuestionOption) answerOption.clone();
		    set.add(newAnswerOption);
		}
		obj.options = set;
	    }

	    // clone units
	    if (units != null) {
		Iterator<AssessmentUnit> iter = units.iterator();
		Set<AssessmentUnit> set = new TreeSet<AssessmentUnit>(new SequencableComparator());
		while (iter.hasNext()) {
		    AssessmentUnit unit = iter.next();
		    AssessmentUnit newUnit = (AssessmentUnit) unit.clone();
		    set.add(newUnit);
		}
		obj.units = set;
	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + AssessmentQuestion.class + " failed");
	}

	return obj;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof AssessmentQuestion)) {
	    return false;
	}

	final AssessmentQuestion genericEntity = (AssessmentQuestion) o;

	return new EqualsBuilder().append(this.getUid(), genericEntity.getUid())
		.append(this.getSequenceId(), genericEntity.getSequenceId()).isEquals();
    }

    @Override
    public int compareTo(Object o) {
	if ((o != null) && o instanceof AssessmentQuestion) {
	    AssessmentQuestion anotherQuestion = (AssessmentQuestion) o;
	    return sequenceId - anotherQuestion.getSequenceId();
	} else {
	    return 1;
	}
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getUid()).append(getSequenceId()).toHashCode();
    }
    
    public QuestionDTO getQuestionDTO() {
	QuestionDTO questionDTO = new QuestionDTO(this);

	return questionDTO;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     *
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    public short getType() {
	return type;
    }
    public void setType(short type) {
	this.type = type;
    }

    public String getTitle() {
	return title;
    }
    public void setTitle(String title) {
	this.title = title;
    }

    public String getQuestion() {
	return question;
    }
    public void setQuestion(String question) {
	this.question = question;
    }
    
    /**
     * Returns sha1(title + description) value that allows us to search for the AssessmentQuestions with the same title and question
     */
    public String getQuestionHash() {
	return questionHash;
    }
    public void setQuestionHash(String questionHash) {
	this.questionHash = questionHash;
    }

    /**
     * Returns image sequence number.
     *
     * @return image sequence number
     */
    @Override
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets image sequence number.
     *
     * @param sequenceId
     *            image sequence number
     */
    @Override
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     * Default grade set in author. To be used only in author
     */
    public int getDefaultGrade() {
	return defaultGrade;
    }

    /**
     * @param defaultGrade
     *            Default grade set in author. To be used only in author
     */
    public void setDefaultGrade(int defaultGrade) {
	this.defaultGrade = defaultGrade;
    }

    public float getPenaltyFactor() {
	return penaltyFactor;
    }
    public void setPenaltyFactor(float penaltyFactor) {
	this.penaltyFactor = penaltyFactor;
    }

    public boolean isAnswerRequired() {
	return answerRequired;
    }
    public void setAnswerRequired(boolean answerRequired) {
	this.answerRequired = answerRequired;
    }

    public String getGeneralFeedback() {
	return generalFeedback;
    }
    public void setGeneralFeedback(String generalFeedback) {
	this.generalFeedback = generalFeedback;
    }

    public String getFeedback() {
	return feedback;
    }
    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }

    public boolean isMultipleAnswersAllowed() {
	return multipleAnswersAllowed;
    }
    public void setMultipleAnswersAllowed(boolean multipleAnswersAllowed) {
	this.multipleAnswersAllowed = multipleAnswersAllowed;
    }

    public boolean isIncorrectAnswerNullifiesMark() {
	return incorrectAnswerNullifiesMark;
    }
    public void setIncorrectAnswerNullifiesMark(boolean incorrectAnswerNullifiesMark) {
	this.incorrectAnswerNullifiesMark = incorrectAnswerNullifiesMark;
    }

    public String getFeedbackOnCorrect() {
	return feedbackOnCorrect;
    }
    public void setFeedbackOnCorrect(String feedbackOnCorrect) {
	this.feedbackOnCorrect = feedbackOnCorrect;
    }

    public String getFeedbackOnPartiallyCorrect() {
	return feedbackOnPartiallyCorrect;
    }
    public void setFeedbackOnPartiallyCorrect(String feedbackOnPartiallyCorrect) {
	this.feedbackOnPartiallyCorrect = feedbackOnPartiallyCorrect;
    }

    public String getFeedbackOnIncorrect() {
	return feedbackOnIncorrect;
    }
    public void setFeedbackOnIncorrect(String feedbackOnIncorrect) {
	this.feedbackOnIncorrect = feedbackOnIncorrect;
    }

    public boolean isShuffle() {
	return shuffle;
    }
    public void setShuffle(boolean shuffle) {
	this.shuffle = shuffle;
    }

    public boolean isCaseSensitive() {
	return caseSensitive;
    }
    public void setCaseSensitive(boolean caseSensitive) {
	this.caseSensitive = caseSensitive;
    }

    public boolean getCorrectAnswer() {
	return correctAnswer;
    }
    public void setCorrectAnswer(boolean correctAnswer) {
	this.correctAnswer = correctAnswer;
    }

    public boolean isAllowRichEditor() {
	return allowRichEditor;
    }
    public void setAllowRichEditor(boolean allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    /**
     * @return a set of options to this AssessmentQuestion.
     */
    public Set<AssessmentQuestionOption> getOptions() {
	return options;
    }

    /**
     * @param options
     *            options to set.
     */
    public void setOptions(Set<AssessmentQuestionOption> options) {
	this.options = options;
    }

    /**
     * @return a set of units to this AssessmentQuestion.
     */
    public Set<AssessmentUnit> getUnits() {
	return units;
    }

    /**
     * @param options
     *            units to set.
     */
    public void setUnits(Set<AssessmentUnit> units) {
	this.units = units;
    }

    /**
     * maxWordsLimit set in author. Used only for essay type of questions
     */
    public int getMaxWordsLimit() {
	return maxWordsLimit;
    }

    /**
     * @param maxWordsLimit
     *            set in author. Used only for essay type of questions
     */
    public void setMaxWordsLimit(int maxWordsLimit) {
	this.maxWordsLimit = maxWordsLimit;
    }

    /**
     * minWordsLimit set in author. Used only for essay type of questions
     */
    public int getMinWordsLimit() {
	return minWordsLimit;
    }

    /**
     * @param minWordsLimit
     *            set in author. Used only for essay type of questions
     */
    public void setMinWordsLimit(int minWordsLimit) {
	this.minWordsLimit = minWordsLimit;
    }

    public boolean isHedgingJustificationEnabled() {
	return hedgingJustificationEnabled;
    }
    public void setHedgingJustificationEnabled(boolean hedgingJustificationEnabled) {
	this.hedgingJustificationEnabled = hedgingJustificationEnabled;
    }
    
    // *************** NON Persist Fields used in monitoring ********************

    public String getTitleEscaped() {
	return titleEscaped;
    }
    public void setTitleEscaped(String titleEscaped) {
	this.titleEscaped = titleEscaped;
    }
}
