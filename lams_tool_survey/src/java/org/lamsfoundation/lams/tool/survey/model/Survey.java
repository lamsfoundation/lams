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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;

/**
 * @author Dapeng Ni
 */

@Entity
@Table(name = "tl_lasurv11_survey")
public class Survey implements Cloneable {

    private static final Logger log = Logger.getLogger(Survey.class);

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

    @Column(name = "show_questions_on_one_page")
    private boolean showOnePage;

    @Column(name = "show_other_users_answers")
    private boolean showOtherUsersAnswers;

    @Column(name = "lock_on_finished")
    private boolean lockWhenFinished;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "answer_submit_notify")
    private boolean notifyTeachersOnAnswerSumbit;

    @Column(name = "create_date")
    private Date created;

    @Column(name = "update_date")
    private Date updated;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private SurveyUser createdBy;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "survey_uid")
    @OrderBy("sequence_id")
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<SurveyQuestion> questions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "content_uid")
    @SortComparator(TextSearchConditionComparator.class)
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<SurveyCondition> conditions = new TreeSet<>(new TextSearchConditionComparator());

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
		Iterator<SurveyQuestion> iter = questions.iterator();
		Set<SurveyQuestion> set = new HashSet<>();
		while (iter.hasNext()) {
		    SurveyQuestion item = iter.next();
		    SurveyQuestion newItem = (SurveyQuestion) item.clone();
		    set.add(newItem);
		}
		survey.questions = set;
	    }
	    if (getConditions() != null) {
		Set<SurveyCondition> set = new TreeSet<>(new TextSearchConditionComparator());
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

    /**
     * Returns the object's creation date
     */
    public Date getCreated() {
	return created;
    }

    /**
     * Sets the object's creation date
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * Returns the object's date of last update
     */
    public Date getUpdated() {
	return updated;
    }

    /**
     * Sets the object's date of last update
     */
    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    /**
     * @return Returns the userid of the user who created the Share surveys.
     */
    public SurveyUser getCreatedBy() {
	return createdBy;
    }

    /**
     * The userid of the user who created this Share surveys.
     */
    public void setCreatedBy(SurveyUser createdBy) {
	this.createdBy = createdBy;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public boolean getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * Set to true to lock the survey for finished users.
     */
    public void setLockWhenFinished(boolean lockWhenFinished) {
	this.lockWhenFinished = lockWhenFinished;
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

    public Set<SurveyQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Set<SurveyQuestion> questions) {
	this.questions = questions;
    }

    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
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
    }

    public boolean isShowOnePage() {
	return showOnePage;
    }

    public void setShowOnePage(boolean showOnePage) {
	this.showOnePage = showOnePage;
    }

    public boolean isShowOtherUsersAnswers() {
	return showOtherUsersAnswers;
    }

    public void setShowOtherUsersAnswers(boolean showOtherUsersAnswers) {
	this.showOtherUsersAnswers = showOtherUsersAnswers;
    }

    public boolean isNotifyTeachersOnAnswerSumbit() {
	return notifyTeachersOnAnswerSumbit;
    }

    public void setNotifyTeachersOnAnswerSumbit(boolean notifyTeachersOnAnswerSumbit) {
	this.notifyTeachersOnAnswerSumbit = notifyTeachersOnAnswerSumbit;
    }

    public Set<SurveyCondition> getConditions() {
	return conditions;
    }

    public void setConditions(Set<SurveyCondition> conditions) {
	this.conditions = conditions;
    }

    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }
}