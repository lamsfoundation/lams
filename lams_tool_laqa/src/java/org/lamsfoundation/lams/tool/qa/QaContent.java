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


package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;

/**
 * QaContent Value Object The value object that maps to our model database table: tl_laqa11_content The relevant
 * hibernate mapping resides in: QaContent.hbm.xml
 *
 * Holds content representation for the tool. Default content is made available to the tool by the database.
 *
 * @author Ozgur Demirtas
 */
public class QaContent implements Serializable {

    /** identifier field */
    private Long uid;

    /** identifier field */
    private Long qaContentId;

    /** nullable persistent field */
    private String title;

    /** nullable persistent field */
    private String instructions;

    /** nullable persistent field */
    private String reportTitle;

    /** nullable persistent field */
    private String monitoringReportTitle;

    /** nullable persistent field */
    private long createdBy;

    /** nullable persistent field */
    private boolean defineLater;

    private boolean reflect;

    private String reflectionSubject;

    /** nullable persistent field */

    private boolean questionsSequenced;

    private boolean lockWhenFinished;

    private boolean noReeditAllowed;

    private boolean showOtherAnswers;

    private boolean allowRichEditor;

    private boolean useSelectLeaderToolOuput;

    /** nullable persistent field */
    private boolean usernameVisible;

    /** nullable persistent field */
    private boolean allowRateAnswers;

    private boolean notifyTeachersOnResponseSubmit;

    /** nullable persistent field */
    private Date creationDate;

    /** nullable persistent field */
    private Date updateDate;

    private Date submissionDeadline;

    private boolean showOtherAnswersAfterDeadline;

    private int maximumRates;

    private int minimumRates;

    private Set<LearnerItemRatingCriteria> ratingCriterias;

    /** persistent field */
    private Set<QaQueContent> qaQueContents;

    /** persistent field */
    private Set qaSessions;

    /** persistent field */
    private Set<QaCondition> conditions = new TreeSet<QaCondition>(new TextSearchConditionComparator());

    public QaContent() {
    };

    /** full constructor */
    public QaContent(Long qaContentId, String title, String instructions, String reportTitle,
	    String monitoringReportTitle, long createdBy, boolean questionsSequenced, boolean usernameVisible,
	    boolean allowRateAnswers, boolean notifyTeachersOnResponseSubmit, boolean lockWhenFinished,
	    boolean noReeditAllowed, boolean showOtherAnswers, boolean reflect, String reflectionSubject,
	    Date creationDate, Date updateDate, Set<QaQueContent> qaQueContents, Set qaSessions,
	    Set<QaCondition> conditions, boolean allowRichEditor, boolean useSelectLeaderToolOuput, int maximumRates,
	    int minimumRates, Set<LearnerItemRatingCriteria> ratingCriterias) {
	this.qaContentId = qaContentId;
	this.title = title;
	this.instructions = instructions;
	this.reportTitle = reportTitle;
	this.monitoringReportTitle = monitoringReportTitle;
	this.createdBy = createdBy;
	this.questionsSequenced = questionsSequenced;
	this.usernameVisible = usernameVisible;
	this.allowRateAnswers = allowRateAnswers;
	this.notifyTeachersOnResponseSubmit = notifyTeachersOnResponseSubmit;
	this.lockWhenFinished = lockWhenFinished;
	this.noReeditAllowed = noReeditAllowed;
	this.showOtherAnswers = showOtherAnswers;
	this.reflect = reflect;
	this.reflectionSubject = reflectionSubject;
	this.creationDate = creationDate;
	this.updateDate = updateDate;
	this.qaQueContents = qaQueContents;
	this.qaSessions = qaSessions;
	this.conditions = conditions;
	this.allowRichEditor = allowRichEditor;
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
	this.maximumRates = maximumRates;
	this.minimumRates = minimumRates;
	this.ratingCriterias = ratingCriterias;
    }

    /**
     * Copy Construtor to create a new qa content instance. Note that we don't copy the qa session data here because the
     * qa session will be created after we copied tool content.
     *
     * @param qa
     *            the original qa content.
     * @param newContentId
     *            the new qa content id.
     * @return the new qa content object.
     */
    public static QaContent newInstance(QaContent qa, Long newContentId) {
	QaContent newContent = new QaContent(newContentId, qa.getTitle(), qa.getInstructions(), qa.getReportTitle(),
		qa.getMonitoringReportTitle(), qa.getCreatedBy(), qa.isQuestionsSequenced(), qa.isUsernameVisible(),
		qa.isAllowRateAnswers(), qa.isNotifyTeachersOnResponseSubmit(), qa.isLockWhenFinished(),
		qa.isNoReeditAllowed(), qa.isShowOtherAnswers(), qa.isReflect(), qa.getReflectionSubject(),
		qa.getCreationDate(), qa.getUpdateDate(), new TreeSet(), new TreeSet(),
		new TreeSet<QaCondition>(new TextSearchConditionComparator()), qa.isAllowRichEditor(),
		qa.isUseSelectLeaderToolOuput(), qa.maximumRates, qa.minimumRates, new TreeSet());

	newContent.setQaQueContents(qa.deepCopyQaQueContent(newContent));

	newContent.setRatingCriterias(qa.deepCopyRatingCriterias(newContent));

	newContent.setConditions(qa.deepCopyConditions(newContent));
	return newContent;
    }

    public Set<LearnerItemRatingCriteria> deepCopyRatingCriterias(QaContent newQaContent) {

	Set<LearnerItemRatingCriteria> newCriterias = new TreeSet<LearnerItemRatingCriteria>();
	for (Iterator<LearnerItemRatingCriteria> i = ratingCriterias.iterator(); i.hasNext();) {
	    LearnerItemRatingCriteria criteria = i.next();
	    LearnerItemRatingCriteria newCriteria = (LearnerItemRatingCriteria) criteria.clone();
	    newCriteria.setToolContentId(newQaContent.qaContentId);
	    newCriterias.add(newCriteria);
	}
	return newCriterias;
    }

    public Set<QaQueContent> deepCopyQaQueContent(QaContent newQaContent) {
	Set<QaQueContent> newQaQueContent = new TreeSet<QaQueContent>();
	for (Iterator<QaQueContent> i = this.getQaQueContents().iterator(); i.hasNext();) {
	    QaQueContent queContent = i.next();
	    newQaQueContent.add(QaQueContent.newInstance(queContent, newQaContent));
	}
	return newQaQueContent;
    }

    public Set<QaCondition> deepCopyConditions(QaContent newQaContent) {

	Set<QaCondition> newConditions = new TreeSet<QaCondition>(new TextSearchConditionComparator());
	if (getConditions() != null) {
	    for (QaCondition condition : getConditions()) {
		newConditions.add(condition.clone(newQaContent));
	    }
	}

	return newConditions;
    }

    public Set deepCopyQaSession(QaContent newQaSession) {
	return new TreeSet();
    }

    public Set<QaQueContent> getQaQueContents() {
	if (qaQueContents == null) {
	    setQaQueContents(new TreeSet<QaQueContent>());
	}
	return qaQueContents;
    }

    public void setQaQueContents(Set<QaQueContent> qaQueContents) {
	this.qaQueContents = qaQueContents;
    }

    public Set getQaSessions() {
	if (qaSessions == null) {
	    setQaSessions(new TreeSet());
	}
	return qaSessions;
    }

    public void setQaSessions(Set qaSessions) {
	this.qaSessions = qaSessions;
    }

    /**
     * @return Returns the qaContentId.
     */
    public Long getQaContentId() {
	return qaContentId;
    }

    /**
     * @param qaContentId
     *            The qaContentId to set.
     */
    public void setQaContentId(Long qaContentId) {
	this.qaContentId = qaContentId;
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

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("qaContentId:", getQaContentId()).append("qa title:", getTitle())
		.append("qa instructions:", getInstructions()).append("creator user id", getCreatedBy())
		.append("username_visible:", isUsernameVisible()).append("allow to rate answers:", isAllowRateAnswers())
		.append("defineLater", isDefineLater()).append("report_title: ", getReportTitle())
		.append("reflection subject: ", getReflectionSubject()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if (!(other instanceof QaContent)) {
	    return false;
	}
	QaContent castOther = (QaContent) other;
	return new EqualsBuilder().append(this.getQaContentId(), castOther.getQaContentId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getQaContentId()).toHashCode();
    }

    /**
     * @return Returns the createdBy.
     */
    public long getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The createdBy to set.
     */
    public void setCreatedBy(long createdBy) {
	this.createdBy = createdBy;
    }

    /**
     * @return Returns the defineLater.
     */
    public boolean isDefineLater() {
	return defineLater;
    }

    /**
     * @param defineLater
     *            The defineLater to set.
     */
    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @return Returns the instructions.
     */
    public String getInstructions() {
	return instructions;
    }

    /**
     * @param instructions
     *            The instructions to set.
     */
    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @return Returns the reportTitle.
     */
    public String getReportTitle() {
	return reportTitle;
    }

    /**
     * @param reportTitle
     *            The reportTitle to set.
     */
    public void setReportTitle(String reportTitle) {
	this.reportTitle = reportTitle;
    }

    /**
     * @return Returns the updateDate.
     */
    public Date getUpdateDate() {
	return updateDate;
    }

    /**
     * @param updateDate
     *            The updateDate to set.
     */
    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     * @return Returns the submissionDeadline.
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    /**
     * @param submissionDeadline
     *            The submissionDeadline to set.
     */
    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    /**
     * @return Returns the showOtherAnswersAfterDeadline.
     */
    public boolean isShowOtherAnswersAfterDeadline() {
	return showOtherAnswersAfterDeadline;
    }

    /**
     * @param showOtherAnswersAfterDeadline
     *            The showOtherAnswersAfterDeadline to set.
     */
    public void setShowOtherAnswersAfterDeadline(boolean showOtherAnswersAfterDeadline) {
	this.showOtherAnswersAfterDeadline = showOtherAnswersAfterDeadline;
    }

    /**
     * @return Returns the usernameVisible.
     */
    public boolean isUsernameVisible() {
	return usernameVisible;
    }

    /**
     * @param usernameVisible
     *            The usernameVisible to set.
     */
    public void setUsernameVisible(boolean usernameVisible) {
	this.usernameVisible = usernameVisible;
    }

    /**
     * @return Returns the allowRateAnswers.
     */
    public boolean isAllowRateAnswers() {
	return allowRateAnswers;
    }

    /**
     * @param allowRateAnswers
     *            The allowRateAnswers to set.
     */
    public void setAllowRateAnswers(boolean allowRateAnswers) {
	this.allowRateAnswers = allowRateAnswers;
    }

    /**
     * @return
     */
    public boolean isNotifyTeachersOnResponseSubmit() {
	return notifyTeachersOnResponseSubmit;
    }

    public void setNotifyTeachersOnResponseSubmit(boolean notifyTeachersOnResponseSubmit) {
	this.notifyTeachersOnResponseSubmit = notifyTeachersOnResponseSubmit;
    }

    /**
     * @return Returns the questionsSequenced.
     */
    public boolean isQuestionsSequenced() {
	return questionsSequenced;
    }

    /**
     * @param questionsSequenced
     *            The questionsSequenced to set.
     */
    public void setQuestionsSequenced(boolean questionsSequenced) {
	this.questionsSequenced = questionsSequenced;
    }

    /**
     * @return Returns the monitoringReportTitle.
     */
    public String getMonitoringReportTitle() {
	return monitoringReportTitle;
    }

    /**
     * @param monitoringReportTitle
     *            The monitoringReportTitle to set.
     */
    public void setMonitoringReportTitle(String monitoringReportTitle) {
	this.monitoringReportTitle = monitoringReportTitle;
    }

    /**
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
	return creationDate;
    }

    /**
     * @param creationDate
     *            The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

    /**
     * @return Returns the reflect.
     */
    public boolean isReflect() {
	return reflect;
    }

    /**
     * @param reflect
     *            The reflect to set.
     */
    public void setReflect(boolean reflect) {
	this.reflect = reflect;
    }

    /**
     * @return Returns the reflectionSubject.
     */
    public String getReflectionSubject() {
	return reflectionSubject;
    }

    /**
     * @param reflectionSubject
     *            The reflectionSubject to set.
     */
    public void setReflectionSubject(String reflectionSubject) {
	this.reflectionSubject = reflectionSubject;
    }

    /**
     * @return Returns the lockWhenFinished.
     */
    public boolean isLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished
     *            The lockWhenFinished to set.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
    }

    /**
     * @return Returns the noReeditAllowed.
     */
    public boolean isNoReeditAllowed() {
	return noReeditAllowed;
    }

    /**
     * @param noReeditAllowed
     *            The noReeditAllowed to set.
     */
    public void setNoReeditAllowed(boolean noReeditAllowed) {
	this.noReeditAllowed = noReeditAllowed;
    }

    public boolean isAllowRichEditor() {
	return allowRichEditor;
    }

    public void setAllowRichEditor(boolean allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    public boolean isUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
    }

    /**
     * @return Returns the showOtherAnswers.
     */
    public boolean isShowOtherAnswers() {
	return showOtherAnswers;
    }

    /**
     * @param showOtherAnswers
     *            The showOtherAnswers to set.
     */
    public void setShowOtherAnswers(boolean showOtherAnswers) {
	this.showOtherAnswers = showOtherAnswers;
    }

    /**
     * @return
     */
    public int getMaximumRates() {
	return maximumRates;
    }

    public void setMaximumRates(int maximumRate) {
	this.maximumRates = maximumRate;
    }

    /**
     * @return
     */
    public int getMinimumRates() {
	return minimumRates;
    }

    public void setMinimumRates(int minimumRates) {
	this.minimumRates = minimumRates;
    }

    /**
     *
     * @return
     */
    public Set<LearnerItemRatingCriteria> getRatingCriterias() {
	return ratingCriterias;
    }

    public void setRatingCriterias(Set<LearnerItemRatingCriteria> ratingCriterias) {
	this.ratingCriterias = ratingCriterias;
    }

    public Set<QaCondition> getConditions() {
	return conditions;
    }

    public void setConditions(Set<QaCondition> conditions) {
	this.conditions = conditions;
    }
}