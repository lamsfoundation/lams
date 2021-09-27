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

/**
 * Daco
 *
 * @author Marcin Cieslak
 *
 *
 *
 */
@Entity
@Table(name = "tl_ladaco10_contents")
public class Daco implements Cloneable {

    private static final Logger log = Logger.getLogger(Daco.class);

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

    @Column(name = "lock_on_finished")
    private boolean lockOnFinished;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "min_records")
    private Short minRecords;

    @Column(name = "max_records")
    private Short maxRecords;

    @Column(name = "create_date")
    private Date created;

    @Column(name = "update_date")
    private Date updated;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "create_by")
    private DacoUser createdBy;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "daco", cascade = CascadeType.ALL)
    @OrderBy("uid ASC")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<DacoQuestion> dacoQuestions = new LinkedHashSet<>();

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column(name = "learner_entry_notify")
    private boolean notifyTeachersOnLearnerEntry;

    @Column(name = "record_submit_notify")
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

    public Date getCreated() {
	return created;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    public Date getUpdated() {
	return updated;
    }

    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    public DacoUser getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(DacoUser createdBy) {
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

    public boolean getLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public Set<DacoQuestion> getDacoQuestions() {
	return dacoQuestions;
    }

    public void setDacoQuestions(Set<DacoQuestion> dacoQuestions) {
	this.dacoQuestions = dacoQuestions;
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

    public Short getMinRecords() {
	return minRecords;
    }

    public void setMinRecords(Short minRecords) {
	this.minRecords = minRecords;
    }

    public Short getMaxRecords() {
	return maxRecords;
    }

    public void setMaxRecords(Short maxRecords) {
	this.maxRecords = maxRecords;
    }

    public boolean isNotifyTeachersOnLearnerEntry() {
	return notifyTeachersOnLearnerEntry;
    }

    public void setNotifyTeachersOnLearnerEntry(boolean notifyTeachersOnLearnerEntry) {
	this.notifyTeachersOnLearnerEntry = notifyTeachersOnLearnerEntry;
    }

    public boolean isNotifyTeachersOnRecordSumbit() {
	return notifyTeachersOnRecordSumbit;
    }

    public void setNotifyTeachersOnRecordSumbit(boolean notifyTeachersOnRecordSumbit) {
	this.notifyTeachersOnRecordSumbit = notifyTeachersOnRecordSumbit;
    }
}