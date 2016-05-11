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


package org.lamsfoundation.lams.tool.videoRecorder.model;

import java.util.Date;
import java.util.Set;

import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;

/**
 *
 * Caches recording details.
 *
 * @hibernate.class table="tl_lavidr10_recording"
 */

public class VideoRecorderRecording implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = -3701664859818409197L;

    // Fields
    private Long uid;

    private Long toolContentId;

    private Date createDate;

    private Date updateDate;

    private String title;

    private String description;

    private Float rating;

    private String filename;

    private Boolean isLocal;

    private Boolean isJustSound;

    private VideoRecorderUser createBy;

    private VideoRecorderSession videoRecorderSession;

    private Set ratings;

    private Set comments;

    // Constructors

    /** default constructor */
    public VideoRecorderRecording() {
    }

    public VideoRecorderRecording(VideoRecorderRecordingDTO recording, VideoRecorderSession videoRecorderSession) {
	this.uid = recording.uid;
	this.createDate = recording.createDate;
	this.updateDate = recording.updateDate;
	this.createBy = recording.createBy;
	this.title = recording.title;
	this.description = recording.description;
	this.rating = recording.rating;
	this.isLocal = recording.isLocal;
	this.isJustSound = recording.isJustSound;
	this.ratings = VideoRecorderRating.getVideoRecorderRatings(recording.ratings);
	this.comments = VideoRecorderComment.getVideoRecorderComments(recording.comments);
	this.videoRecorderSession = videoRecorderSession;
	this.filename = recording.filename;
	this.toolContentId = recording.toolContentId;
    }

    public VideoRecorderRecording(VideoRecorderRecording recording) {
	this.uid = recording.uid;
	this.createDate = new Date();
	this.updateDate = new Date();
	this.createBy = recording.createBy;
	this.title = recording.title;
	this.description = recording.description;
	this.rating = recording.rating;
	this.isLocal = recording.isLocal;
	this.isJustSound = recording.isJustSound;
	this.ratings = null;
	this.comments = null;
	this.filename = recording.filename;
	this.toolContentId = recording.toolContentId;
    }

    /** full constructor */
    public VideoRecorderRecording(Date createDate, Date updateDate, VideoRecorderUser createBy, String title,
	    String description, Float rating, Boolean isJustSound, Boolean isLocal, Long videoRecorderUid,
	    VideoRecorderSession videoRecorderSession, String filename, Set ratings, Set comments, Long toolContentId) {

	this.createDate = createDate;
	this.updateDate = updateDate;
	this.createBy = createBy;
	this.title = title;
	this.description = description;
	this.rating = rating;
	this.isJustSound = isJustSound;
	this.isLocal = isLocal;
	this.ratings = ratings;
	this.comments = comments;
	this.videoRecorderSession = videoRecorderSession;
	this.filename = filename;
	this.toolContentId = toolContentId;
    }

    // Property accessors
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     *
     */

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="tool_content_id"
     *
     */

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     * @hibernate.property column="create_date"
     *
     */

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     * @hibernate.property column="update_date"
     *
     */

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     * @return Returns the VideoRecorderUser of the person who created the recording
     *
     * @hibernate.many-to-one
     * 		       column="create_by"
     *                        cascade="none"
     *
     */

    public VideoRecorderUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(VideoRecorderUser createBy) {
	this.createBy = createBy;
    }

    /**
     * @hibernate.property column="title" length="255"
     *
     */

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.property column="description" length="1027"
     *
     */

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @hibernate.property column="rating"
     *
     */

    public Float getRating() {
	return rating;
    }

    public void setRating(Float rating) {
	this.rating = rating;
    }

    /**
     * @hibernate.property column="is_just_sound"
     *
     */

    public Boolean getIsJustSound() {
	return isJustSound;
    }

    public void setIsJustSound(Boolean isJustSound) {
	this.isJustSound = isJustSound;
    }

    /**
     * @hibernate.property column="is_local"
     *
     */

    public Boolean getIsLocal() {
	return isLocal;
    }

    public void setIsLocal(Boolean isLocal) {
	this.isLocal = isLocal;
    }

    /**
     * Gets the toolSession
     *
     * @hibernate.many-to-one cascade="none"
     *                        class="org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession"
     *                        column="videoRecorder_session_uid"
     *
     */

    public VideoRecorderSession getVideoRecorderSession() {
	return this.videoRecorderSession;
    }

    public void setVideoRecorderSession(VideoRecorderSession videoRecorderSession) {
	this.videoRecorderSession = videoRecorderSession;
    }

    /**
     * @hibernate.property column="filename" length="255"
     *
     */

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    /**
     * @hibernate.set lazy="false"
     *                cascade="all-delete-orphan"
     * @hibernate.collection-key column="videoRecorder_recording_uid"
     * 
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRating"
     * 
     * @return
     */

    public Set getRatings() {
	return ratings;
    }

    public void setRatings(Set ratings) {
	this.ratings = ratings;
    }

    /**
     * @hibernate.set lazy="false"
     *                cascade="all-delete-orphan"
     * @hibernate.collection-key column="videoRecorder_recording_uid"
     * 
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderComment"
     * 
     * @return
     */

    public Set getComments() {
	return comments;
    }

    public void setComments(Set comments) {
	this.comments = comments;
    }

    /**
     * toString
     * 
     * @return String
     */
    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("uid").append("='").append(getUid()).append("', ");
	buffer.append("createDate").append("='").append(getCreateDate()).append("', ");
	buffer.append("updateDate").append("='").append(getUpdateDate()).append("', ");
	buffer.append("createBy").append("='").append(getCreateBy()).append("', ");
	buffer.append("createTitle").append("='").append(getTitle()).append("', ");
	buffer.append("createDescription").append("='").append(getDescription()).append("', ");
	buffer.append("videoRecorderSessionUid").append("='").append(getVideoRecorderSession().getSessionId());
	buffer.append("filename").append("='").append(getFilename());
	buffer.append("comments").append("='").append(getComments());
	buffer.append("ratings").append("='").append(getRatings());
	buffer.append("']");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if ((other == null)) {
	    return false;
	}
	if (!(other instanceof VideoRecorderRecording)) {
	    return false;
	}
	VideoRecorderRecording castOther = (VideoRecorderRecording) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    @Override
    public Object clone() {
	Object obj = null;
	try {
	    obj = super.clone();
	    ((VideoRecorderRecording) obj).setUid(null);
	} catch (CloneNotSupportedException e) {
	    return null;
	}

	return obj;
    }
}
