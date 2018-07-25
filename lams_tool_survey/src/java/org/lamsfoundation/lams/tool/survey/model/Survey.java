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

package org.lamsfoundation.lams.tool.survey.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;

/**
 * Survey
 *
 * @author Dapeng Ni
 *
 *
 *
 */
public class Survey implements Cloneable {

    private static final Logger log = Logger.getLogger(Survey.class);

    // key
    private Long uid;
    // tool contentID
    private Long contentId;
    private String title;
    private String instructions;
    // advance
    private boolean showOnePage;
    private boolean showOtherUsersAnswers;
    private boolean lockWhenFinished;

    private boolean reflectOnActivity;
    private String reflectInstructions;

    private boolean defineLater;
    private boolean contentInUse;

    private boolean notifyTeachersOnAnswerSumbit;

    // general infomation
    private Date created;
    private Date updated;
    private Date submissionDeadline;
    private SurveyUser createdBy;

    // survey Items
    private Set<SurveyQuestion> questions;

    // conditions
    private Set<SurveyCondition> conditions = new TreeSet<SurveyCondition>(new TextSearchConditionComparator());

    /**
     * Default contruction method.
     *
     */
    public Survey() {
	questions = new HashSet<SurveyQuestion>();
    }

    // **********************************************************
    // Function method for Survey
    // **********************************************************
    public static Survey newInstance(Survey defaultContent, Long contentId) {
	Survey toContent = new Survey();
	toContent = (Survey) defaultContent.clone();
	toContent.setContentId(contentId);

	// reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setSurvey(toContent);
	    Set<SurveyQuestion> items = toContent.getQuestions();
	    for (SurveyQuestion item : items) {
		item.setCreateBy(toContent.getCreatedBy());
	    }
	}
	return toContent;
    }

    @Override
    public Object clone() {

	Survey survey = null;
	try {
	    survey = (Survey) super.clone();
	    survey.setUid(null);
	    if (questions != null) {
		Iterator iter = questions.iterator();
		Set<SurveyQuestion> set = new HashSet<SurveyQuestion>();
		while (iter.hasNext()) {
		    SurveyQuestion item = (SurveyQuestion) iter.next();
		    SurveyQuestion newItem = (SurveyQuestion) item.clone();
		    set.add(newItem);
		}
		survey.questions = set;
	    }
	    if (getConditions() != null) {
		Set<SurveyCondition> set = new TreeSet<SurveyCondition>(new TextSearchConditionComparator());
		for (SurveyCondition condition : getConditions()) {
		    set.add(condition.clone(survey));
		}
		survey.setConditions(set);
	    }

	    // clone ReourceUser as well
	    if (createdBy != null) {
		survey.setCreatedBy((SurveyUser) createdBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    Survey.log.error("When clone " + Survey.class + " failed");
	}

	return survey;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Survey)) {
	    return false;
	}

	final Survey genericEntity = (Survey) o;

	return new EqualsBuilder().append(uid, genericEntity.uid).append(title, genericEntity.title)
		.append(instructions, genericEntity.instructions).append(created, genericEntity.created)
		.append(updated, genericEntity.updated).append(createdBy, genericEntity.createdBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(created).append(updated)
		.append(createdBy).toHashCode();
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
     * @return Returns the userid of the user who created the Share surveys.
     *
     *
     *
     */
    public SurveyUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Share surveys.
     */
    public void setCreatedBy(SurveyUser createdBy) {
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
     *
     *
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
     * @return Returns the lockWhenFinish.
     *
     *
     *
     */
    public boolean getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished
     *            Set to true to lock the survey for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
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
     *
     * @return
     */
    public Set<SurveyQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Set questions) {
	this.questions = questions;
    }

    /**
     *
     * @return
     */
    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
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

    /**
     *
     * @return
     */
    public boolean isShowOnePage() {
	return showOnePage;
    }

    public void setShowOnePage(boolean showOnePage) {
	this.showOnePage = showOnePage;
    }

    /**
     *
     * @return
     */
    public boolean isShowOtherUsersAnswers() {
	return showOtherUsersAnswers;
    }

    public void setShowOtherUsersAnswers(boolean showOtherUsersAnswers) {
	this.showOtherUsersAnswers = showOtherUsersAnswers;
    }

    /**
     *
     * @return
     */
    public boolean isNotifyTeachersOnAnswerSumbit() {
	return notifyTeachersOnAnswerSumbit;
    }

    public void setNotifyTeachersOnAnswerSumbit(boolean notifyTeachersOnAnswerSumbit) {
	this.notifyTeachersOnAnswerSumbit = notifyTeachersOnAnswerSumbit;
    }

    /**
     *
     * sort="org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator"
     *
     *
     *
     */
    public Set<SurveyCondition> getConditions() {
	return conditions;
    }

    public void setConditions(Set<SurveyCondition> conditions) {
	this.conditions = conditions;
    }

    /**
     *
     * @return date submissionDeadline
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }
}
