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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Entity
@Table(name = "tl_laasse10_assessment_question")
public class AssessmentQuestion implements Cloneable, Sequencable, Comparable<AssessmentQuestion> {
    private static final Logger log = Logger.getLogger(AssessmentQuestion.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "question_type")
    private short type;

    @Column
    private String title;

    @Column
    private String question;

    /**
     * It stores sha1(question) value that allows us to search for the AssessmentQuestions with the same question
     */
    @Column(name = "question_hash")
    private String questionHash;

    @Column(name = "sequence_id")
    private int sequenceId;

    /**
     * Default grade set in author.
     */
    @Column(name = "default_grade")
    private int defaultGrade;

    @Column(name = "penalty_factor")
    private float penaltyFactor;

    @Column(name = "answer_required")
    private boolean answerRequired;

    @Column(name = "general_feedback")
    private String generalFeedback;

    @Column
    private String feedback;

    @Column(name = "multiple_answers_allowed")
    private boolean multipleAnswersAllowed;

    @Column(name = "incorrect_answer_nullifies_mark")
    private boolean incorrectAnswerNullifiesMark;

    @Column(name = "feedback_on_correct")
    private String feedbackOnCorrect;

    @Column(name = "feedback_on_partially_correct")
    private String feedbackOnPartiallyCorrect;

    @Column(name = "feedback_on_incorrect")
    private String feedbackOnIncorrect;

    // only one of shuffle and prefixAnswersWithLetters should be on. Both may be off
    @Column
    private boolean shuffle;
    
    @Column(name = "prefix_answers_with_letters")
    private boolean prefixAnswersWithLetters;

    @Column(name = "case_sensitive")
    private boolean caseSensitive;

    @Column(name = "correct_answer")
    private boolean correctAnswer;

    @Column(name = "allow_rich_editor")
    private boolean allowRichEditor;

    // only for essay type of question
    @Column(name = "max_words_limit")
    private int maxWordsLimit;
    
    // only for essay type of question
    @Column(name = "min_words_limit")
    private int minWordsLimit;

    // only for hedging mark type of question
    @Column(name = "hedging_justification_enabled")
    private boolean hedgingJustificationEnabled;

    @Column(name = "correct_answers_disclosed")
    private boolean correctAnswersDisclosed;

    @Column(name = "groups_answers_disclosed")
    private boolean groupsAnswersDisclosed;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_uid")
    @OrderBy("sequence_id ASC")
    private Set<AssessmentQuestionOption> options = new TreeSet<>(new SequencableComparator());

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_uid")
    @OrderBy("sequence_id ASC")
    private Set<AssessmentUnit> units = new TreeSet<>(new SequencableComparator());

    // *************** NON Persist Fields used in monitoring ********************
    @Transient
    private String titleEscaped;

    @Override
    public Object clone() {
	AssessmentQuestion obj = null;
	try {
	    obj = (AssessmentQuestion) super.clone();
	    obj.setUid(null);

	    // clone options
	    if (options != null) {
		Iterator<AssessmentQuestionOption> iter = options.iterator();
		Set<AssessmentQuestionOption> set = new TreeSet<>(new SequencableComparator());
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
		Set<AssessmentUnit> set = new TreeSet<>(new SequencableComparator());
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
    public int compareTo(AssessmentQuestion anotherQuestion) {
	return sequenceId - anotherQuestion.getSequenceId();
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
    public Long getUid() {
	return uid;
    }

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
     * Returns sha1(question) value that allows us to search for the AssessmentQuestions with the same question
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

    public boolean isCorrectAnswersDisclosed() {
	return correctAnswersDisclosed;
    }

    public void setCorrectAnswersDisclosed(boolean correctAnswersDisclosed) {
	this.correctAnswersDisclosed = correctAnswersDisclosed;
    }

    public boolean isGroupsAnswersDisclosed() {
	return groupsAnswersDisclosed;
    }

    public void setGroupsAnswersDisclosed(boolean groupsAnswersDisclosed) {
	this.groupsAnswersDisclosed = groupsAnswersDisclosed;
    }

    public boolean isPrefixAnswersWithLetters() {
        return prefixAnswersWithLetters;
    }

    public void setPrefixAnswersWithLetters(boolean prefixAnswersWithLetters) {
        this.prefixAnswersWithLetters = prefixAnswersWithLetters;
    }

    // *************** NON Persist Fields used in monitoring ********************

    public String getTitleEscaped() {
	return titleEscaped;
    }

    public void setTitleEscaped(String titleEscaped) {
	this.titleEscaped = titleEscaped;
    }
}
