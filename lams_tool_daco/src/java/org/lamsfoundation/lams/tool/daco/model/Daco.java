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

package org.lamsfoundation.lams.tool.daco.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * Daco
 *
 * @author Marcin Cieslak
 *
 * @hibernate.class table="tl_ladaco10_contents"
 *
 */
public class Daco implements Cloneable {

    private static final Logger log = Logger.getLogger(Daco.class);

    // key
    private Long uid;

    // tool contentID
    private Long contentId;

    private String title;

    private String instructions;

    // advance

    private boolean lockOnFinished;

    private boolean defineLater;

    private boolean contentInUse;

    private Short minRecords;

    private Short maxRecords;

    // general infomation
    private Date created;

    private Date updated;

    private DacoUser createdBy;

    // daco Questions
    private Set<DacoQuestion> dacoQuestions = new LinkedHashSet();

    private boolean reflectOnActivity;

    private String reflectInstructions;

    private boolean notifyTeachersOnLearnerEntry;

    private boolean notifyTeachersOnRecordSumbit;

    // **********************************************************
    // Function method for Daco
    // **********************************************************
    public static Daco newInstance(Daco defaultContent, Long contentId) {
	Daco toContent = new Daco();
	toContent = (Daco) defaultContent.clone();
	toContent.setContentId(contentId);

	// reset user info as well
	if (toContent.getCreatedBy() != null) {
	    toContent.getCreatedBy().setDaco(toContent);
	    Set<DacoQuestion> questions = toContent.getDacoQuestions();
	    for (DacoQuestion question : questions) {
		question.setCreateBy(toContent.getCreatedBy());
	    }
	}
	return toContent;
    }

    @Override
    public Object clone() {

	Daco daco = null;
	try {
	    daco = (Daco) super.clone();
	    daco.setUid(null);
	    daco.setDacoQuestions(new LinkedHashSet<DacoQuestion>(dacoQuestions.size()));
	    for (DacoQuestion question : dacoQuestions) {
		DacoQuestion clonedQuestion = (DacoQuestion) question.clone();
		clonedQuestion.setDaco(daco);
		daco.getDacoQuestions().add(clonedQuestion);
	    }

	    if (createdBy != null) {
		daco.setCreatedBy((DacoUser) createdBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    Daco.log.error("When clone " + Daco.class + " failed");
	}

	return daco;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Daco)) {
	    return false;
	}

	final Daco genericEntity = (Daco) o;

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
     * @hibernate.many-to-one cascade="all" column="create_by" foreign-key="DacoToUser"
     * @return Returns the userid of the user who created the Share daco.
     * 
     */
    public DacoUser getCreatedBy() {
	return createdBy;
    }

    /**
     * @param createdBy
     *            The userid of the user who created this Share daco.
     */
    public void setCreatedBy(DacoUser createdBy) {
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
     * @return Returns the lockOnFinish.
     * 
     * @hibernate.property column="lock_on_finished"
     * 
     */
    public boolean getLockOnFinished() {
	return lockOnFinished;
    }

    /**
     * @param lockOnFinished
     *            Set to true to lock the daco for finished users.
     */
    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
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
     * 
     * 
     * @hibernate.set cascade="all" order-by="uid asc" outer-join="true"
     * @hibernate.collection-key column="content_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.daco.model.DacoQuestion"
     * 
     * @return
     */
    public Set<DacoQuestion> getDacoQuestions() {
	return dacoQuestions;
    }

    public void setDacoQuestions(Set<DacoQuestion> dacoQuestions) {
	this.dacoQuestions = dacoQuestions;
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

    /**
     * @return Returns the instructions set by the teacher.
     * 
     * @hibernate.property column="min_records"
     */
    public Short getMinRecords() {
	return minRecords;
    }

    public void setMinRecords(Short minRecords) {
	this.minRecords = minRecords;
    }

    /**
     * @return Returns the instructions set by the teacher.
     * 
     * @hibernate.property column="max_records"
     */
    public Short getMaxRecords() {
	return maxRecords;
    }

    public void setMaxRecords(Short maxRecords) {
	this.maxRecords = maxRecords;
    }

    /**
     * @hibernate.property column="learner_entry_notify"
     * @return
     */
    public boolean isNotifyTeachersOnLearnerEntry() {
	return notifyTeachersOnLearnerEntry;
    }

    public void setNotifyTeachersOnLearnerEntry(boolean notifyTeachersOnLearnerEntry) {
	this.notifyTeachersOnLearnerEntry = notifyTeachersOnLearnerEntry;
    }

    /**
     * @hibernate.property column="record_submit_notify"
     * @return
     */
    public boolean isNotifyTeachersOnRecordSumbit() {
	return notifyTeachersOnRecordSumbit;
    }

    public void setNotifyTeachersOnRecordSumbit(boolean notifyTeachersOnRecordSumbit) {
	this.notifyTeachersOnRecordSumbit = notifyTeachersOnRecordSumbit;
    }
}