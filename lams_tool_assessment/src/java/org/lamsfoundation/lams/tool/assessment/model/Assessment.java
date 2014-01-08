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
/* $Id$ */
package org.lamsfoundation.lams.tool.assessment.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.assessment.util.AssessmentToolContentHandler;
import org.lamsfoundation.lams.tool.assessment.util.SequencableComparator;

/**
 * Assessment
 * 
 * @author Andrey Balan
 * 
 * @hibernate.class table="tl_laasse10_assessment"
 * 
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
    
    private boolean runOffline;

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

    private boolean contentInUse;

    private boolean notifyTeachersOnAttemptCompletion;
    
    private boolean reflectOnActivity;

    private String reflectInstructions;

    // instructions
    private String onlineInstructions;

    private String offlineInstructions;

    private Set attachments;

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

    // *************** NON Persist Fields ********************
    private IToolContentHandler toolContentHandler;

    private List<AssessmentAttachment> onlineFileList;

    private List<AssessmentAttachment> offlineFileList;

    /**
     * Default contruction method.
     * 
     */
    public Assessment() {
	attachments = new TreeSet();
	questions = new TreeSet(new SequencableComparator());
	questionReferences = new TreeSet(new SequencableComparator());
	overallFeedbacks = new TreeSet(new SequencableComparator());
    }

    // **********************************************************
    // Function method for Assessment
    // **********************************************************
    public static Assessment newInstance(Assessment defaultContent, Long contentId,
	    AssessmentToolContentHandler assessmentToolContentHandler) {
	Assessment toContent = new Assessment();
	defaultContent.toolContentHandler = assessmentToolContentHandler;
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
		Set<AssessmentOverallFeedback> set = new TreeSet<AssessmentOverallFeedback>(new SequencableComparator());
		while (iter.hasNext()) {
		    AssessmentOverallFeedback overallFeedback = (AssessmentOverallFeedback) iter.next();
		    AssessmentOverallFeedback newOverallFeedback = (AssessmentOverallFeedback) overallFeedback.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newOverallFeedback);
		}
		assessment.overallFeedbacks = set;
	    }	    
	    // clone attachment
	    if (attachments != null) {
		Iterator iter = attachments.iterator();
		Set set = new TreeSet();
		while (iter.hasNext()) {
		    AssessmentAttachment file = (AssessmentAttachment) iter.next();
		    AssessmentAttachment newFile = (AssessmentAttachment) file.clone();
		    // just clone old file without duplicate it in repository

		    set.add(newFile);
		}
		assessment.attachments = set;
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

	return new EqualsBuilder().append(uid, genericEntity.uid).append(title, genericEntity.title).append(
		instructions, genericEntity.instructions).append(onlineInstructions, genericEntity.onlineInstructions)
		.append(offlineInstructions, genericEntity.offlineInstructions).append(created, genericEntity.created)
		.append(updated, genericEntity.updated).append(createdBy, genericEntity.createdBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(onlineInstructions).append(
		offlineInstructions).append(created).append(updated).append(createdBy).toHashCode();
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

    public void toDTO() {
	onlineFileList = new ArrayList<AssessmentAttachment>();
	offlineFileList = new ArrayList<AssessmentAttachment>();
	Set<AssessmentAttachment> fileSet = this.getAttachments();
	if (fileSet != null) {
	    for (AssessmentAttachment file : fileSet) {
		if (StringUtils.equalsIgnoreCase(file.getFileType(), IToolContentHandler.TYPE_OFFLINE)) {
		    offlineFileList.add(file);
		} else {
		    onlineFileList.add(file);
		}
	    }
	}
    }

    // **********************************************************
    // get/set methods
    // **********************************************************
    /**
     * Returns the object's creation date
     * 
     * @return date
     * @hibernate.property column="create_date"
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
     * @hibernate.property column="update_date"
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
     * @hibernate.property column="submission_deadline"
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
     * 
     * @hibernate.many-to-one cascade="save-update" column="create_by"
     * 
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
     * @hibernate.id column="uid" generator-class="native"
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the title.
     * 
     * @hibernate.property column="title"
     * 
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
     * @return Returns the runOffline.
     * 
     * @hibernate.property column="run_offline"
     * 
     */
    public boolean getRunOffline() {
	return runOffline;
    }

    /**
     * @param runOffline
     *            The forceOffLine to set.
     * 
     * 
     */
    public void setRunOffline(boolean forceOffline) {
	runOffline = forceOffline;
    }
    
    /**
     * If the tool utilizes leaders from Select Leader tool.
     * 
     * @return
     * @hibernate.property column="use_select_leader_tool_ouput"
     */
    public boolean isUseSelectLeaderToolOuput() {
        return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
        this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    /**
     * @return Returns the time limitation, that students have to complete an attempt.
     * 
     * @hibernate.property column="time_limit"
     * 
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
     * @hibernate.property column="instructions" type="text"
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @return Returns the onlineInstructions set by the teacher.
     * 
     * @hibernate.property column="online_instructions" type="text"
     */
    public String getOnlineInstructions() {
	return onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
	this.onlineInstructions = onlineInstructions;
    }

    /**
     * @return Returns the onlineInstructions set by the teacher.
     * 
     * @hibernate.property column="offline_instructions" type="text"
     */
    public String getOfflineInstructions() {
	return offlineInstructions;
    }

    public void setOfflineInstructions(String offlineInstructions) {
	this.offlineInstructions = offlineInstructions;
    }

    /**
     * 
     * @hibernate.set lazy="true" cascade="all" inverse="false" order-by="create_date desc"
     * @hibernate.collection-key column="assessment_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.assessment.model.AssessmentAttachment"
     * 
     * @return a set of Attachments to this Message.
     */
    public Set getAttachments() {
	return attachments;
    }

    /*
     * @param attachments The attachments to set.
     */
    public void setAttachments(Set attachments) {
	this.attachments = attachments;
    }

    /**
     * 
     * @hibernate.set lazy="true" inverse="false" cascade="all" order-by="sequence_id asc"
     * @hibernate.collection-key column="assessment_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.assessment.model.AssessmentQuestion"
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
     * @hibernate.set lazy="true" inverse="false" cascade="all" order-by="sequence_id asc"
     * @hibernate.collection-key column="assessment_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.assessment.model.QuestionReference"
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
     * @hibernate.set cascade="all" order-by="sequence_id asc"
     * @hibernate.collection-key column="assessment_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.assessment.model.AssessmentOverallFeedback"
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
     * @hibernate.property column="content_in_use"
     * @return
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * @hibernate.property column="define_later"
     * @return
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @hibernate.property column="content_id" unique="true"
     * @return
     */
    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
    }

    /**
     * @hibernate.property column="allow_question_feedback"
     * @return
     */
    public boolean isAllowQuestionFeedback() {
	return allowQuestionFeedback;
    }

    public void setAllowQuestionFeedback(boolean allowQuestionFeedback) {
	this.allowQuestionFeedback = allowQuestionFeedback;
    }

    /**
     * @hibernate.property column="allow_overall_feedback"
     * @return
     */
    public boolean isAllowOverallFeedbackAfterQuestion() {
	return allowOverallFeedbackAfterQuestion;
    }

    public void setAllowOverallFeedbackAfterQuestion(boolean allowOverallFeedbackAfterQuestion) {
	this.allowOverallFeedbackAfterQuestion = allowOverallFeedbackAfterQuestion;
    }
    
    /**
     * @hibernate.property column="allow_right_answers"
     * @return
     */
    public boolean isAllowRightAnswersAfterQuestion() {
	return allowRightAnswersAfterQuestion;
    }

    public void setAllowRightAnswersAfterQuestion(boolean allowRightAnswersAfterQuestion) {
	this.allowRightAnswersAfterQuestion = allowRightAnswersAfterQuestion;
    }
    
    /**
     * @hibernate.property column="allow_wrong_answers"
     * @return
     */
    public boolean isAllowWrongAnswersAfterQuestion() {
	return allowWrongAnswersAfterQuestion;
    }

    public void setAllowWrongAnswersAfterQuestion(boolean allowWrongAnswersAfterQuestion) {
	this.allowWrongAnswersAfterQuestion = allowWrongAnswersAfterQuestion;
    }
    
    
    
    /**
     * @hibernate.property column="allow_grades_after_attempt"
     * @return
     */
    public boolean isAllowGradesAfterAttempt() {
	return allowGradesAfterAttempt;
    }

    public void setAllowGradesAfterAttempt(boolean allowGradesAfterAttempt) {
	this.allowGradesAfterAttempt = allowGradesAfterAttempt;
    }
    
    /**
     * @hibernate.property column="allow_history_responses"
     * @return
     */
    public boolean isAllowHistoryResponses() {
	return allowHistoryResponses;
    }

    public void setAllowHistoryResponses(boolean allowHistoryResponses) {
	this.allowHistoryResponses = allowHistoryResponses;
    }
    
    /**
     * @hibernate.property column="display_summary"
     * @return
     */
	public boolean isDisplaySummary() {
		return displaySummary;
	}

	public void setDisplaySummary(boolean displaySummary) {
		this.displaySummary = displaySummary;
	}

	

	/**
     * @hibernate.property column="questions_per_page"
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
     * @hibernate.property column="attempts_allowed"
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
     * @hibernate.property column="passing_mark"
     * @return
     */
    public int getPassingMark() {
	return passingMark;
    }
    
    public void setPassingMark(int passingMark) {
	this.passingMark = passingMark;
    }

    /**
     * @hibernate.property column="shuffled"
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
     * @hibernate.property column="numbered"
     * @return
     */
    public boolean isNumbered() {
	return numbered;
    }

    public void setNumbered(boolean numbered) {
	this.numbered = numbered;
    }

    public List<AssessmentAttachment> getOfflineFileList() {
	return offlineFileList;
    }

    public void setOfflineFileList(List<AssessmentAttachment> offlineFileList) {
	this.offlineFileList = offlineFileList;
    }

    public List<AssessmentAttachment> getOnlineFileList() {
	return onlineFileList;
    }

    public void setOnlineFileList(List<AssessmentAttachment> onlineFileList) {
	this.onlineFileList = onlineFileList;
    }

    public void setToolContentHandler(IToolContentHandler toolContentHandler) {
	this.toolContentHandler = toolContentHandler;
    }

    /**
     * @hibernate.property column="attempt_completion_notify"
     * @return
     */
    public boolean isNotifyTeachersOnAttemptCompletion() {
	return notifyTeachersOnAttemptCompletion;
    }

    public void setNotifyTeachersOnAttemptCompletion(boolean notifyTeachersOnAttemptCompletion) {
	this.notifyTeachersOnAttemptCompletion = notifyTeachersOnAttemptCompletion;
    }
    
    /**
     * @hibernate.property column="reflect_instructions"
     * @return
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     * @hibernate.property column="reflect_on_activity"
     * @return
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }
}
