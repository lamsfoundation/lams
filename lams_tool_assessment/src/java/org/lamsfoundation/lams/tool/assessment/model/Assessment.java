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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.SortComparator;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;

/**
 * Assessment
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laasse10_assessment")
public class Assessment implements Cloneable {
    private static final Logger log = Logger.getLogger(Assessment.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "content_id")
    private Long contentId;

    @Column
    private String title;

    @Column
    private String instructions;

    // *** advance tab ***

    @Column(name = "use_select_leader_tool_ouput")
    private boolean useSelectLeaderToolOuput;

    @Column(name = "relative_time_limit")
    private int relativeTimeLimit;

    @Column(name = "absolute_time_limit")
    private LocalDateTime absoluteTimeLimit;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tl_laasse10_time_limit", joinColumns = @JoinColumn(name = "assessment_uid"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "adjustment")
    private Map<Integer, Integer> timeLimitAdjustments = new HashMap<>();

    @Column(name = "questions_per_page")
    private int questionsPerPage;

    @Column(name = "attempts_allowed")
    private int attemptsAllowed;

    @Column(name = "passing_mark")
    private int passingMark;

    @Column
    private boolean shuffled;

    @Column
    private boolean numbered;

    @Column(name = "allow_question_feedback")
    private boolean allowQuestionFeedback;

    @Column(name = "allow_overall_feedback")
    private boolean allowOverallFeedbackAfterQuestion;

    @Column(name = "allow_disclose_answers")
    private boolean allowDiscloseAnswers;

    @Column(name = "allow_right_answers")
    private boolean allowRightAnswersAfterQuestion;

    @Column(name = "allow_wrong_answers")
    private boolean allowWrongAnswersAfterQuestion;

    @Column(name = "allow_grades_after_attempt")
    private boolean allowGradesAfterAttempt;

    @Column(name = "enable_confidence_levels")
    private boolean enableConfidenceLevels;

    @Column(name = "confidence_levels_type")
    private Integer confidenceLevelsType;

    @Column(name = "allow_history_responses")
    private boolean allowHistoryResponses;

    @Column(name = "allow_answer_justification")
    private boolean allowAnswerJustification;

    @Column(name = "allow_discussion_sentiment")
    private boolean allowDiscussionSentiment;

    @Column(name = "display_summary")
    private boolean displaySummary;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "attempt_completion_notify")
    private boolean notifyTeachersOnAttemptCompletion;

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column(name = "question_etherpad_enabled")
    private boolean questionEtherpadEnabled;

    // general information

    @Column(name = "create_date")
    private Date created;

    @Column(name = "update_date")
    private Date updated;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "create_by")
    private AssessmentUser createdBy;

    // Question bank questions
    @OneToMany
    @JoinColumn(name = "assessment_uid")
    @SortComparator(QbToolQuestion.QbToolQuestionComparator.class)
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<AssessmentQuestion> questions = new TreeSet<>();

    // assessment questions references that form question list
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_uid")
    @OrderBy("sequence_id ASC")
    private Set<QuestionReference> questionReferences = new TreeSet<>(new SequencableComparator());

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "assessment_uid")
    @OrderBy("sequence_id ASC")
    private Set<AssessmentOverallFeedback> overallFeedbacks = new TreeSet<>(new SequencableComparator());

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
		Iterator<AssessmentQuestion> iter = questions.iterator();
		TreeSet<AssessmentQuestion> set = new TreeSet<>();
		while (iter.hasNext()) {
		    AssessmentQuestion question = iter.next();
		    AssessmentQuestion newQuestion = (AssessmentQuestion) question.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newQuestion);
		}
		assessment.questions = set;
	    }

	    // clone questionReferences
	    if (questionReferences != null) {
		Iterator<QuestionReference> iter = questionReferences.iterator();
		Set<QuestionReference> set = new TreeSet<>(new SequencableComparator());
		while (iter.hasNext()) {
		    QuestionReference questionReference = iter.next();
		    QuestionReference newQuestionReference = (QuestionReference) questionReference.clone();

		    // update questionReferences with new cloned question
		    if (newQuestionReference.getQuestion() != null) {
			for (AssessmentQuestion newQuestion : assessment.questions) {
			    if (newQuestion.getDisplayOrder() == newQuestionReference.getQuestion().getDisplayOrder()) {
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
		Iterator<AssessmentOverallFeedback> iter = overallFeedbacks.iterator();
		Set<AssessmentOverallFeedback> set = new TreeSet<>(new SequencableComparator());
		while (iter.hasNext()) {
		    AssessmentOverallFeedback overallFeedback = iter.next();
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

	    assessment.setTimeLimitAdjustments(new HashMap<>(this.getTimeLimitAdjustments()));
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + Assessment.class + " failed");
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

    /**
     * Checks whether content was modified in monitor and this has happened after attempt was started (and thus this
     * modification can potentially affect attempt results).
     *
     * @param attemptStartingDate
     * @return
     */
    public boolean isContentModifiedInMonitor(Date attemptStartingDate) {
	return (updated != null) && updated.after(attemptStartingDate);
    }

    /**
     * @return true if there is at least one random question reference, false otherwise
     */
    public boolean hasRandomQuestion() {
	boolean hasRandomQuestion = false;
	for (QuestionReference reference : questionReferences) {
	    hasRandomQuestion |= reference.isRandomQuestion();
	}
	return hasRandomQuestion;
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
    public int getRelativeTimeLimit() {
	return relativeTimeLimit;
    }

    /**
     * @param timeLimit
     *            the time limitation, that students have to complete an attempt.
     */
    public void setRelativeTimeLimit(int timeLimit) {
	this.relativeTimeLimit = timeLimit;
    }

    public LocalDateTime getAbsoluteTimeLimit() {
	return absoluteTimeLimit;
    }

    public Long getAbsoluteTimeLimitSeconds() {
	return absoluteTimeLimit == null ? null : absoluteTimeLimit.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public void setAbsoluteTimeLimit(LocalDateTime absoluteTimeLimit) {
	this.absoluteTimeLimit = absoluteTimeLimit;
    }

    public Map<Integer, Integer> getTimeLimitAdjustments() {
	return timeLimitAdjustments;
    }

    public void setTimeLimitAdjustments(Map<Integer, Integer> timeLimitAdjustment) {
	this.timeLimitAdjustments = timeLimitAdjustment;
    }

    /**
     * @return Returns the instructions set by the teacher.
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public Set<AssessmentQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Set<AssessmentQuestion> questions) {
	this.questions = questions;
    }

    public Set<QuestionReference> getQuestionReferences() {
	return questionReferences;
    }

    public void setQuestionReferences(Set<QuestionReference> questionReferences) {
	this.questionReferences = questionReferences;
    }

    /**
     * @return a set of OverallFeedbacks for this Assessment.
     */
    public Set<AssessmentOverallFeedback> getOverallFeedbacks() {
	return overallFeedbacks;
    }

    public void setOverallFeedbacks(Set<AssessmentOverallFeedback> assessmentOverallFeedbacks) {
	this.overallFeedbacks = assessmentOverallFeedbacks;
    }

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
	for (AssessmentQuestion question : questions) {
	    question.setToolContentId(contentId);
	}
    }

    public boolean isAllowQuestionFeedback() {
	return allowQuestionFeedback;
    }

    public void setAllowQuestionFeedback(boolean allowQuestionFeedback) {
	this.allowQuestionFeedback = allowQuestionFeedback;
    }

    public boolean isAllowOverallFeedbackAfterQuestion() {
	return allowOverallFeedbackAfterQuestion;
    }

    public void setAllowOverallFeedbackAfterQuestion(boolean allowOverallFeedbackAfterQuestion) {
	this.allowOverallFeedbackAfterQuestion = allowOverallFeedbackAfterQuestion;
    }

    public boolean isAllowDiscloseAnswers() {
	return allowDiscloseAnswers;
    }

    public void setAllowDiscloseAnswers(boolean tblReleaseAnswers) {
	this.allowDiscloseAnswers = tblReleaseAnswers;
    }

    public boolean isAllowRightAnswersAfterQuestion() {
	return allowRightAnswersAfterQuestion;
    }

    public void setAllowRightAnswersAfterQuestion(boolean allowRightAnswersAfterQuestion) {
	this.allowRightAnswersAfterQuestion = allowRightAnswersAfterQuestion;
    }

    public boolean isAllowWrongAnswersAfterQuestion() {
	return allowWrongAnswersAfterQuestion;
    }

    public void setAllowWrongAnswersAfterQuestion(boolean allowWrongAnswersAfterQuestion) {
	this.allowWrongAnswersAfterQuestion = allowWrongAnswersAfterQuestion;
    }

    public boolean isAllowGradesAfterAttempt() {
	return allowGradesAfterAttempt;
    }

    public void setAllowGradesAfterAttempt(boolean allowGradesAfterAttempt) {
	this.allowGradesAfterAttempt = allowGradesAfterAttempt;
    }

    public boolean isEnableConfidenceLevels() {
	return enableConfidenceLevels;
    }

    public void setEnableConfidenceLevels(boolean enableConfidenceLevels) {
	this.enableConfidenceLevels = enableConfidenceLevels;
    }

    public Integer getConfidenceLevelsType() {
	return confidenceLevelsType;
    }

    public void setConfidenceLevelsType(Integer confidenceLevelsType) {
	this.confidenceLevelsType = confidenceLevelsType;
    }

    public boolean isAllowHistoryResponses() {
	return allowHistoryResponses;
    }

    public void setAllowHistoryResponses(boolean allowHistoryResponses) {
	this.allowHistoryResponses = allowHistoryResponses;
    }

    public boolean isAllowAnswerJustification() {
	return allowAnswerJustification;
    }

    public void setAllowAnswerJustification(boolean allowAnswerJustification) {
	this.allowAnswerJustification = allowAnswerJustification;
    }

    public boolean isAllowDiscussionSentiment() {
	return allowDiscussionSentiment;
    }

    public void setAllowDiscussionSentiment(boolean allowDiscussionSentiment) {
	this.allowDiscussionSentiment = allowDiscussionSentiment;
    }

    public boolean isDisplaySummary() {
	return displaySummary;
    }

    public void setDisplaySummary(boolean displaySummary) {
	this.displaySummary = displaySummary;
    }

    public int getQuestionsPerPage() {
	return questionsPerPage;
    }

    public void setQuestionsPerPage(int questionsPerPage) {
	this.questionsPerPage = questionsPerPage;
    }

    /**
     * number of allow students attempts
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
     * @return
     */
    public int getPassingMark() {
	return passingMark;
    }

    public void setPassingMark(int passingMark) {
	this.passingMark = passingMark;
    }

    public boolean isShuffled() {
	return shuffled;
    }

    public void setShuffled(boolean shuffled) {
	this.shuffled = shuffled;
    }

    /**
     * If this is checked, then in learner we display the numbering for learners.
     *
     * @return
     */
    public boolean isNumbered() {
	return numbered;
    }

    public void setNumbered(boolean numbered) {
	this.numbered = numbered;
    }

    public boolean isNotifyTeachersOnAttemptCompletion() {
	return notifyTeachersOnAttemptCompletion;
    }

    public void setNotifyTeachersOnAttemptCompletion(boolean notifyTeachersOnAttemptCompletion) {
	this.notifyTeachersOnAttemptCompletion = notifyTeachersOnAttemptCompletion;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public boolean isQuestionEtherpadEnabled() {
	return questionEtherpadEnabled;
    }

    public void setQuestionEtherpadEnabled(boolean questionEtherpadEnabled) {
	this.questionEtherpadEnabled = questionEtherpadEnabled;
    }
}
