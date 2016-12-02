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

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;

/**
 * Assessment
 *
 * @author Andrey Balan
 */
public class Assessment implements Cloneable {

    private static final Logger log = Logger.getLogger(Assessment.class);

    // key
    private Long uid;

    // tool contentID
    private Long contentId;

    private String title;

    private String instructions;

    // advance
    private boolean useSelectLeaderToolOuput;

    private int timeLimit;

    private int questionsPerPage;

    private int attemptsAllowed;

    private int passingMark;

    private boolean shuffled;

    private boolean numbered;

    private boolean allowQuestionFeedback;

    private boolean allowOverallFeedbackAfterQuestion;

    private boolean allowRightAnswersAfterQuestion;

    private boolean allowWrongAnswersAfterQuestion;

    private boolean allowGradesAfterAttempt;

    private boolean allowHistoryResponses;

    private boolean displaySummary;

    private boolean defineLater;

    private boolean notifyTeachersOnAttemptCompletion;

    private boolean reflectOnActivity;

    private String reflectInstructions;

    // general infomation
    private Date created;

    private Date updated;

    private Date submissionDeadline;

    private AssessmentUser createdBy;

    // Question bank questions
    private Set questions;

    // assessment questions references that form question list
    private Set questionReferences;

    private Set overallFeedbacks;

    /**
     * Default contruction method.
     *
     */
    public Assessment() {
	questions = new TreeSet(new SequencableComparator());
	questionReferences = new TreeSet(new SequencableComparator());
	overallFeedbacks = new TreeSet(new SequencableComparator());
    }

    // **********************************************************
    // Function method for Assessment
    // **********************************************************
    public static Assessment newInstance(Assessment defaultContent, Long contentId) {
	Assessment toContent = new Assessment();
	toContent = (Assessment) defaultContent.clone();
	toContent.setContentId(contentId);

	// reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setAssessment(toContent);
	    Set<AssessmentQuestion> questions = toContent.getQuestions();
	    for (AssessmentQuestion question : questions) {
		question.setCreateBy(toContent.getCreatedBy());
	    }
	    Set<QuestionReference> questionReferences = toContent.getQuestionReferences();
	    for (QuestionReference questionReference : questionReferences) {
		if (questionReference.getQuestion() != null) {
		    questionReference.getQuestion().setCreateBy(toContent.getCreatedBy());
		}
	    }
	}
	return toContent;
    }

    @Override
    public Object clone() {

	Assessment assessment = null;
	try {
	    assessment = (Assessment) super.clone();
	    assessment.setUid(null);

	    // clone questions
	    if (questions != null) {
		Iterator iter = questions.iterator();
		TreeSet<AssessmentQuestion> set = new TreeSet<AssessmentQuestion>(new SequencableComparator());
		while (iter.hasNext()) {
		    AssessmentQuestion question = (AssessmentQuestion) iter.next();
		    AssessmentQuestion newQuestion = (AssessmentQuestion) question.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newQuestion);
		}
		assessment.questions = set;
	    }

	    // clone questionReferences
	    if (questionReferences != null) {
		Iterator iter = questionReferences.iterator();
		Set<QuestionReference> set = new TreeSet<QuestionReference>(new SequencableComparator());
		while (iter.hasNext()) {
		    QuestionReference questionReference = (QuestionReference) iter.next();
		    QuestionReference newQuestionReference = (QuestionReference) questionReference.clone();

		    // update questionReferences with new cloned question
		    if (newQuestionReference.getQuestion() != null) {
			for (AssessmentQuestion newQuestion : (Set<AssessmentQuestion>) assessment.questions) {
			    if (newQuestion.getSequenceId() == newQuestionReference.getQuestion().getSequenceId()) {
				newQuestionReference.setQuestion(newQuestion);
				break;
			    }
			}
		    }

		    set.add(newQuestionReference);
		}
		assessment.questionReferences = set;
	    }

	    // clone OverallFeedbacks
	    if (overallFeedbacks != null) {
		Iterator iter = overallFeedbacks.iterator();
		Set<AssessmentOverallFeedback> set = new TreeSet<AssessmentOverallFeedback>(
			new SequencableComparator());
		while (iter.hasNext()) {
		    AssessmentOverallFeedback overallFeedback = (AssessmentOverallFeedback) iter.next();
		    AssessmentOverallFeedback newOverallFeedback = (AssessmentOverallFeedback) overallFeedback.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newOverallFeedback);
		}
		assessment.overallFeedbacks = set;
	    }
	    // clone ReourceUser as well
	    if (createdBy != null) {
		assessment.setCreatedBy((AssessmentUser) createdBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    Assessment.log.error("When clone " + Assessment.class + " failed");
	}

	return assessment;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Assessment)) {
	    return false;
	}

	final Assessment genericEntity = (Assessment) o;

	return new EqualsBuilder().append(uid, genericEntity.uid).append(title, genericEntity.title)
		.append(instructions, genericEntity.instructions).append(created, genericEntity.created)
		.append(updated, genericEntity.updated).append(createdBy, genericEntity.createdBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(updated).append(createdBy)
		.toHashCode();
    }

    /**
     * Updates the modification data for this entity.
     */
    public void updateModificationData() {

	long now = System.currentTimeMillis();
	if (created == null) {
	    this.setCreated(new Date(now));
	}
	this.setUpdated(new Date(now));
    }

    // **********************************************************
    // get/set methods
    // **********************************************************
    /**
     * Returns the object's creation date
     *
     * @return date
     *
     */
    public Date getCreated() {
	return created;
    }

    /**
     * Sets the object's creation date
     *
     * @param created
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * Returns the object's date of last update
     *
     * @return date updated
     *
     */
    public Date getUpdated() {
	return updated;
    }

    /**
     * Sets the object's date of last update
     *
     * @param updated
     */
    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    /**
     * Returns deadline for learner's submission
     *
     * @return submissionDeadline
     *
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    /**
     * Sets deadline for learner's submission
     *
     * @param submissionDeadline
     */
    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    /**
     * @return Returns the userid of the user who created the Share assessment.
     */
    public AssessmentUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Share assessment.
     */
    public void setCreatedBy(AssessmentUser createdBy) {
	this.createdBy = createdBy;
    }

    /**
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * If the tool utilizes leaders from Select Leader tool.
     */
    public boolean isUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    /**
     * @return Returns the time limitation, that students have to complete an attempt.
     */
    public int getTimeLimit() {
	return timeLimit;
    }

    /**
     * @param timeLimit
     *            the time limitation, that students have to complete an attempt.
     */
    public void setTimeLimit(int timeLimit) {
	this.timeLimit = timeLimit;
    }

    /**
     * @return Returns the instructions set by the teacher.
     *
     *
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     *
     *
     *
     *
     *
     * @return
     */
    public Set getQuestions() {
	return questions;
    }

    public void setQuestions(Set questions) {
	this.questions = questions;
    }

    /**
     *
     *
     *
     *
     *
     * @return
     */
    public Set getQuestionReferences() {
	return questionReferences;
    }

    public void setQuestionReferences(Set questionReferences) {
	this.questionReferences = questionReferences;
    }

    /**
     *
     *
     *
     *
     *
     * @return a set of OverallFeedbacks for this Assessment.
     */
    public Set getOverallFeedbacks() {
	return overallFeedbacks;
    }

    public void setOverallFeedbacks(Set assessmentOverallFeedbacks) {
	this.overallFeedbacks = assessmentOverallFeedbacks;
    }

    /**
     *
     * @return
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     *
     * @return
     */
    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    /**
     *
     * @return
     */
    public boolean isAllowQuestionFeedback() {
	return allowQuestionFeedback;
    }

    public void setAllowQuestionFeedback(boolean allowQuestionFeedback) {
	this.allowQuestionFeedback = allowQuestionFeedback;
    }

    /**
     *
     * @return
     */
    public boolean isAllowOverallFeedbackAfterQuestion() {
	return allowOverallFeedbackAfterQuestion;
    }

    public void setAllowOverallFeedbackAfterQuestion(boolean allowOverallFeedbackAfterQuestion) {
	this.allowOverallFeedbackAfterQuestion = allowOverallFeedbackAfterQuestion;
    }

    /**
     *
     * @return
     */
    public boolean isAllowRightAnswersAfterQuestion() {
	return allowRightAnswersAfterQuestion;
    }

    public void setAllowRightAnswersAfterQuestion(boolean allowRightAnswersAfterQuestion) {
	this.allowRightAnswersAfterQuestion = allowRightAnswersAfterQuestion;
    }

    /**
     *
     * @return
     */
    public boolean isAllowWrongAnswersAfterQuestion() {
	return allowWrongAnswersAfterQuestion;
    }

    public void setAllowWrongAnswersAfterQuestion(boolean allowWrongAnswersAfterQuestion) {
	this.allowWrongAnswersAfterQuestion = allowWrongAnswersAfterQuestion;
    }

    /**
     *
     * @return
     */
    public boolean isAllowGradesAfterAttempt() {
	return allowGradesAfterAttempt;
    }

    public void setAllowGradesAfterAttempt(boolean allowGradesAfterAttempt) {
	this.allowGradesAfterAttempt = allowGradesAfterAttempt;
    }

    /**
     *
     * @return
     */
    public boolean isAllowHistoryResponses() {
	return allowHistoryResponses;
    }

    public void setAllowHistoryResponses(boolean allowHistoryResponses) {
	this.allowHistoryResponses = allowHistoryResponses;
    }

    /**
     *
     * @return
     */
    public boolean isDisplaySummary() {
	return displaySummary;
    }

    public void setDisplaySummary(boolean displaySummary) {
	this.displaySummary = displaySummary;
    }

    /**
     *
     * @return
     */
    public int getQuestionsPerPage() {
	return questionsPerPage;
    }

    public void setQuestionsPerPage(int questionsPerPage) {
	this.questionsPerPage = questionsPerPage;
    }

    /**
     * number of allow students attempts
     *
     *
     * @return
     */
    public int getAttemptsAllowed() {
	return attemptsAllowed;
    }

    public void setAttemptsAllowed(int attemptsAllowed) {
	this.attemptsAllowed = attemptsAllowed;
    }

    /**
     * passing mark based on which we decide either user has failed or passed
     *
     *
     * @return
     */
    public int getPassingMark() {
	return passingMark;
    }

    public void setPassingMark(int passingMark) {
	this.passingMark = passingMark;
    }

    /**
     *
     * @return
     */
    public boolean isShuffled() {
	return shuffled;
    }

    public void setShuffled(boolean shuffled) {
	this.shuffled = shuffled;
    }

    /**
     * If this is checked, then in learner we display the numbering for learners.
     *
     *
     * @return
     */
    public boolean isNumbered() {
	return numbered;
    }

    public void setNumbered(boolean numbered) {
	this.numbered = numbered;
    }

    /**
     *
     * @return
     */
    public boolean isNotifyTeachersOnAttemptCompletion() {
	return notifyTeachersOnAttemptCompletion;
    }

    public void setNotifyTeachersOnAttemptCompletion(boolean notifyTeachersOnAttemptCompletion) {
	this.notifyTeachersOnAttemptCompletion = notifyTeachersOnAttemptCompletion;
    }

    /**
     *
     * @return
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
     * @return
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }
}
