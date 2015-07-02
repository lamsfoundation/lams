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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.assessment.model;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;

/**
 * Assessment Question
 * 
 * @author Andrey Balan
 * 
 * @hibernate.class table="tl_laasse10_assessment_question"
 */
public class AssessmentQuestion implements Cloneable, Sequencable, Comparable {
    private static final Logger log = Logger.getLogger(AssessmentQuestion.class);

    private Long uid;

    private short type;

    private String title;

    private String question;
    
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
    
    private String feedbackOnCorrect;
    
    private String feedbackOnPartiallyCorrect;
    
    private String feedbackOnIncorrect;

    private boolean shuffle;
    
    private boolean caseSensitive;
    
    private boolean correctAnswer;
    
    private boolean allowRichEditor;

    private Date createDate;
    
    private AssessmentUser createBy;
    
    private Set<AssessmentQuestionOption> options;
    
    private Set<AssessmentUnit> units;
    
    //only for essay type of question
    private int maxWordsLimit;
    //only for essay type of question
    private int minWordsLimit;
    
    // DTO fields:
    private String answerString;
    
    private float answerFloat;
    
    private boolean answerBoolean;
    
    private String questionFeedback;
    
    private String titleEscaped;
    
    private boolean responseSubmitted;
    
    /**
     * Grade acquired from QUestionReference in learner.
     */
    private int grade;
    
    private float mark;
    
    private float penalty;
    
    private Set<AssessmentQuestionOption> matchingPairOptions;
    
    private List<Object[]> questionResults;
    
    public AssessmentQuestion() {
	options = new TreeSet<AssessmentQuestionOption>(new SequencableComparator());
	units = new TreeSet<AssessmentUnit>(new SequencableComparator());
    }

    public Object clone() {
	AssessmentQuestion obj = null;
	try {
	    obj = (AssessmentQuestion) super.clone();
	    ((AssessmentQuestion) obj).setUid(null);
	    
	    // clone options
	    if (options != null) {
		Iterator<AssessmentQuestionOption> iter = options.iterator();
		Set<AssessmentQuestionOption> set = new TreeSet<AssessmentQuestionOption>(new SequencableComparator());
		while (iter.hasNext()) {
		    AssessmentQuestionOption answerOption = (AssessmentQuestionOption) iter.next();
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
		    AssessmentUnit unit = (AssessmentUnit) iter.next();
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
	if (this == o)
	    return true;
	if (!(o instanceof AssessmentQuestion))
	    return false;

	final AssessmentQuestion genericEntity = (AssessmentQuestion) o;

	return new EqualsBuilder().append(this.getUid(), genericEntity.getUid()).append(this.getSequenceId(), genericEntity.getSequenceId()).isEquals();
    }
    
    @Override
    public int compareTo(Object o) {
	if ((o != null) && o instanceof AssessmentQuestion) {
	    AssessmentQuestion anotherQuestion = (AssessmentQuestion) o;
	    return (int) (sequenceId - anotherQuestion.getSequenceId());
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

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
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
    
    /**
     * @hibernate.property column="question_type"
     * @return
     */
    public short getType() {
	return type;
    }

    public void setType(short type) {
	this.type = type;
    }
    
    /**
     * @hibernate.property column="title" length="255"
     * @return
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }
    
    /**
     * @hibernate.property column="question" type="text"
     * @return
     */
    public String getQuestion() {
	return question;
    }

    public void setQuestion(String question) {
	this.question = question;
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
     * Default grade set in author. To be used only in author
     * 
     * @hibernate.property column="default_grade"
     * 
     * @return
     */
    public int getDefaultGrade() {
	return defaultGrade;
    }

    /**
     * @param defaultGrade Default grade set in author. To be used only in author
     */
    public void setDefaultGrade(int defaultGrade) {
	this.defaultGrade = defaultGrade;
    }

    /**
     * @hibernate.property column="penalty_factor"
     * @return
     */
    public float getPenaltyFactor() {
	return penaltyFactor;
    }

    public void setPenaltyFactor(float penaltyFactor) {
	this.penaltyFactor = penaltyFactor;
    }
    
    /**
     * @hibernate.property column="answer_required"
     * @return
     */
    public boolean isAnswerRequired() {
	return answerRequired;
    }

    public void setAnswerRequired(boolean answerRequired) {
	this.answerRequired = answerRequired;
    }

    /**
     * @hibernate.property column="general_feedback" type="text"
     * @return
     */
    public String getGeneralFeedback() {
	return generalFeedback;
    }

    public void setGeneralFeedback(String generalFeedback) {
	this.generalFeedback = generalFeedback;
    }
    
    /**
     * @hibernate.property column="feedback" type="text"
     * @return
     */
    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }
    
    /**
     * @hibernate.property column="multiple_answers_allowed"
     * @return
     */
    public boolean isMultipleAnswersAllowed() {
	return multipleAnswersAllowed;
    }

    public void setMultipleAnswersAllowed(boolean multipleAnswersAllowed) {
	this.multipleAnswersAllowed = multipleAnswersAllowed;
    }

    /**
     * @hibernate.property column="feedback_on_correct"  type="text"
     * @return
     */
    public String getFeedbackOnCorrect() {
	return feedbackOnCorrect;
    }

    public void setFeedbackOnCorrect(String feedbackOnCorrect) {
	this.feedbackOnCorrect = feedbackOnCorrect;
    }
    
    /**
     * @hibernate.property column="feedback_on_partially_correct"  type="text"
     * @return
     */
    public String getFeedbackOnPartiallyCorrect() {
	return feedbackOnPartiallyCorrect;
    }

    public void setFeedbackOnPartiallyCorrect(String feedbackOnPartiallyCorrect) {
	this.feedbackOnPartiallyCorrect = feedbackOnPartiallyCorrect;
    }
    
    /**
     * @hibernate.property column="feedback_on_incorrect"  type="text"
     * @return
     */
    public String getFeedbackOnIncorrect() {
	return feedbackOnIncorrect;
    }

    public void setFeedbackOnIncorrect(String feedbackOnIncorrect) {
	this.feedbackOnIncorrect = feedbackOnIncorrect;
    }
    
    /**
     * @hibernate.property column="shuffle"
     * @return
     */
    public boolean isShuffle() {
	return shuffle;
    }

    public void setShuffle(boolean shuffle) {
	this.shuffle = shuffle;
    }
    
    /**
     * @hibernate.property column="case_sensitive"
     * @return
     */
    public boolean isCaseSensitive() {
	return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
	this.caseSensitive = caseSensitive;
    }
    
    /**
     * @hibernate.property column="correct_answer"
     * @return
     */
    public boolean getCorrectAnswer() {
	return correctAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
	this.correctAnswer = correctAnswer;
    }
    
    /**
     * @hibernate.property column="allow_rich_editor"
     * @return
     */
    public boolean isAllowRichEditor() {
	return allowRichEditor;
    }

    public void setAllowRichEditor(boolean allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    /**
     * @hibernate.property column="create_date"
     * @return
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }
    
    /**
     * @hibernate.many-to-one cascade="none" column="create_by"
     * 
     * @return
     */
    public AssessmentUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(AssessmentUser createBy) {
	this.createBy = createBy;
    }
    
    /**
     * 
     * @hibernate.set cascade="all" order-by="sequence_id asc"
     * @hibernate.collection-key column="question_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestionOption"
     * 
     * @return a set of options to this AssessmentQuestion.
     */
    public Set<AssessmentQuestionOption> getOptions() {
	return options;
    }

    /**
     * @param options options to set.
     */
    public void setOptions(Set<AssessmentQuestionOption> options) {
	this.options = options;
    }
    
    /**
     * 
     * @hibernate.set cascade="all" order-by="sequence_id asc"
     * @hibernate.collection-key column="question_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.assessment.model.AssessmentUnit"
     * 
     * @return a set of units to this AssessmentQuestion.
     */
    public Set<AssessmentUnit> getUnits() {
	return units;
    }

    /**
     * @param options units to set.
     */
    public void setUnits(Set<AssessmentUnit> units) {
	this.units = units;
    }
    
    /**
     * maxWordsLimit set in author. Used only for essay type of questions
     * 
     * @hibernate.property column="max_words_limit"
     * 
     * @return
     */
    public int getMaxWordsLimit() {
	return maxWordsLimit;
    }

    /**
     * @param maxWordsLimit set in author. Used only for essay type of questions
     */
    public void setMaxWordsLimit(int maxWordsLimit) {
	this.maxWordsLimit = maxWordsLimit;
    }
    
    /**
     * minWordsLimit set in author. Used only for essay type of questions
     * 
     * @hibernate.property column="min_words_limit"
     * 
     * @return
     */
    public int getMinWordsLimit() {
	return minWordsLimit;
    }

    /**
     * @param minWordsLimit set in author. Used only for essay type of questions
     */
    public void setMinWordsLimit(int minWordsLimit) {
	this.minWordsLimit = minWordsLimit;
    }
    
    public String getAnswerString() {
	return answerString;
    }
    public void setAnswerString(String answerString) {
	this.answerString = answerString;
    }
    
    public float getAnswerFloat() {
	return answerFloat;
    }
    public void setAnswerFloat(float answerFloat) {
	this.answerFloat = answerFloat;
    }
    
    public boolean getAnswerBoolean() {
	return answerBoolean;
    }
    public void setAnswerBoolean(boolean answerBoolean) {
	this.answerBoolean = answerBoolean;
    }   
    
    public void setQuestionFeedback(String questionFeedback) {
	this.questionFeedback = questionFeedback;
    }
    public String getQuestionFeedback() {
	return questionFeedback;
    }
    
    public String getTitleEscaped() {
	return titleEscaped;
    }
    public void setTitleEscaped(String titleEscaped) {
	this.titleEscaped = titleEscaped;
    }
    
    /**
     * @return Grade acquired from QUestionReference in learner.
     */
    public int getGrade() {
	return grade;
    }
    
    /**
     * @param grade Grade acquired from QuestionReference in learner.
     */
    public void setGrade(int grade) {
	this.grade = grade;
    }
    
    public Float getMark() {
	return mark;
    }
    public void setMark(Float mark) {
	this.mark = mark;
    }
    
    public Float getPenalty() {
	return penalty;
    }
    public void setPenalty(Float penalty) {
	this.penalty = penalty;
    }
    
    public Set<AssessmentQuestionOption> getMatchingPairOptions() {
	return matchingPairOptions;
    }
    public void setMatchingPairOptions(Set<AssessmentQuestionOption> matchingPairOptions) {
	this.matchingPairOptions = matchingPairOptions;
    }
    
    public List<Object[]> getQuestionResults() {
	return questionResults;
    }
    public void setQuestionResults(List<Object[]> questionResults2) {
	this.questionResults = questionResults2;
    }
    
    public boolean isResponseSubmitted() {
	return responseSubmitted;
    }
    public void setResponseSubmitted(boolean responseSubmitted) {
	this.responseSubmitted = responseSubmitted;
    }
}
