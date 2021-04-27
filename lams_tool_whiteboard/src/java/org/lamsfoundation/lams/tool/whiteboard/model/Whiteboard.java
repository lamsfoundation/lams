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

package org.lamsfoundation.lams.tool.whiteboard.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "tl_lawhiteboard11_whiteboard")
public class Whiteboard implements Cloneable {
    private static final Logger log = Logger.getLogger(Whiteboard.class);

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

    // advance
    @Column(name = "use_select_leader_tool_ouput")
    private boolean useSelectLeaderToolOuput;

    @Column(name = "relative_time_limit")
    private int relativeTimeLimit;

    @Column(name = "absolute_time_limit")
    private LocalDateTime absoluteTimeLimit;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tl_lawhiteboard11_time_limit", joinColumns = @JoinColumn(name = "whiteboard_uid"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "adjustment")
    private Map<Integer, Integer> timeLimitAdjustments = new HashMap<>();

    @Column(name = "lock_on_finished")
    private boolean lockWhenFinished;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column(name = "gallery_walk_enabled")
    private boolean galleryWalkEnabled;

    @Column(name = "gallery_walk_read_only")
    private boolean galleryWalkReadOnly;

    @Column(name = "gallery_walk_started")
    private boolean galleryWalkStarted;

    @Column(name = "gallery_walk_finished")
    private boolean galleryWalkFinished;

    @Column(name = "gallery_walk_instructions")
    private String galleryWalkInstructions;

    // general information
    @Column(name = "create_date")
    private Date created;

    @Column(name = "update_date")
    private Date updated;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "create_by")
    private WhiteboardUser createdBy;

    // **********************************************************
    // Function method for Whiteboard
    // **********************************************************
    public static Whiteboard newInstance(Whiteboard defaultContent, Long contentId) {
	Whiteboard toContent = new Whiteboard();
	toContent = (Whiteboard) defaultContent.clone();
	toContent.setContentId(contentId);

	return toContent;
    }

    @Override
    public Object clone() {
	Whiteboard whiteboard = null;
	try {
	    whiteboard = (Whiteboard) super.clone();
	    whiteboard.setUid(null);
	    // clone ReourceUser as well
	    if (createdBy != null) {
		whiteboard.setCreatedBy((WhiteboardUser) createdBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    Whiteboard.log.error("When clone " + Whiteboard.class + " failed");
	}

	whiteboard.setTimeLimitAdjustments(new HashMap<>(this.getTimeLimitAdjustments()));

	return whiteboard;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Whiteboard)) {
	    return false;
	}

	final Whiteboard genericEntity = (Whiteboard) o;

	return new EqualsBuilder().append(uid, genericEntity.uid).append(title, genericEntity.title)
		.append(instructions, genericEntity.instructions).append(created, genericEntity.created)
		.append(updated, genericEntity.updated).append(createdBy, genericEntity.createdBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(created).append(updated)
		.append(createdBy).toHashCode();
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

    public WhiteboardUser getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(WhiteboardUser createdBy) {
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
     * @return Returns the lockWhenFinish.
     */
    public boolean getLockWhenFinished() {
	return lockWhenFinished;
    }

    /**
     * @param lockWhenFinished
     *            Set to true to lock the whiteboard for finished users.
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

    public void setAbsoluteTimeLimit(LocalDateTime absoluteTimeLimit) {
	this.absoluteTimeLimit = absoluteTimeLimit;
    }

    public Long getAbsoluteTimeLimitSeconds() {
	return absoluteTimeLimit == null ? null : absoluteTimeLimit.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public Map<Integer, Integer> getTimeLimitAdjustments() {
	return timeLimitAdjustments;
    }

    public void setTimeLimitAdjustments(Map<Integer, Integer> timeLimitAdjustments) {
	this.timeLimitAdjustments = timeLimitAdjustments;
    }

    public boolean isUseSelectLeaderToolOuput() {
	return useSelectLeaderToolOuput;
    }

    public void setUseSelectLeaderToolOuput(boolean useSelectLeaderToolOuput) {
	this.useSelectLeaderToolOuput = useSelectLeaderToolOuput;
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

    public boolean isGalleryWalkEnabled() {
	return galleryWalkEnabled;
    }

    public void setGalleryWalkEnabled(boolean galleryWalkEnabled) {
	this.galleryWalkEnabled = galleryWalkEnabled;
    }

    public boolean isGalleryWalkReadOnly() {
	return galleryWalkReadOnly;
    }

    public void setGalleryWalkReadOnly(boolean galleryWalkReadOnly) {
	this.galleryWalkReadOnly = galleryWalkReadOnly;
    }

    public boolean isGalleryWalkStarted() {
	return galleryWalkStarted;
    }

    public void setGalleryWalkStarted(boolean galleryWalkStarted) {
	this.galleryWalkStarted = galleryWalkStarted;
    }

    public boolean isGalleryWalkFinished() {
	return galleryWalkFinished;
    }

    public void setGalleryWalkFinished(boolean galleryWalkFinished) {
	this.galleryWalkFinished = galleryWalkFinished;
    }

    public String getGalleryWalkInstructions() {
	return galleryWalkInstructions;
    }

    public void setGalleryWalkInstructions(String galleryWalkInstructions) {
	this.galleryWalkInstructions = galleryWalkInstructions;
    }
}