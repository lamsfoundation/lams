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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.videoRecorder.model;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRatingDTO;

/**
 *
 * Caches recording details.
 *
 * @hibernate.class table="tl_lavidr10_rating"
 */

public class VideoRecorderRating implements java.io.Serializable {

    private static final long serialVersionUID = -3701664859818409197L;

    private Long uid;

    private float rating;

    private VideoRecorderRecording recording;

    private VideoRecorderUser createBy;

    private Date createDate;

    private VideoRecorderSession videoRecorderSession;

    // Constructors

    /** default constructor */
    public VideoRecorderRating() {
    }

    public VideoRecorderRating(VideoRecorderRatingDTO rating) {
	this.createDate = rating.createDate;
	this.createBy = rating.createBy;
	this.rating = rating.rating;
	this.recording = rating.recording;
	this.videoRecorderSession = rating.videoRecorderSession;
    }

    /** full constructor */
    public VideoRecorderRating(float rating, VideoRecorderRecording recording, VideoRecorderUser createBy,
	    Date createDate, VideoRecorderSession videoRecorderSession) {

	this.createDate = createDate;
	this.rating = rating;
	this.createBy = createBy;
	this.recording = recording;
	this.videoRecorderSession = videoRecorderSession;
    }

    public static Set<VideoRecorderRating> getVideoRecorderRatings(Set<VideoRecorderRatingDTO> list) {
	Set<VideoRecorderRating> retSet = new TreeSet<VideoRecorderRating>();
	if (list == null || list.isEmpty()) {
	    return retSet;
	}

	Iterator iter = list.iterator();
	while (iter.hasNext()) {
	    VideoRecorderRatingDTO cDto = (VideoRecorderRatingDTO) iter.next();
	    VideoRecorderRating c = new VideoRecorderRating(cDto);
	    retSet.add(c);
	}
	return retSet;
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
     * @return Returns the VideoRecorderUser of the person who created the recording
     *
     * @hibernate.many-to-one
     * 		       column="videoRecorder_recording_uid"
     *                        cascade="none"
     *
     */

    public VideoRecorderRecording getRecording() {
	return recording;
    }

    public void setRecording(VideoRecorderRecording recording) {
	this.recording = recording;
    }

    /**
     * @hibernate.property column="rating"
     *
     */

    public float getRating() {
	return rating;
    }

    public void setRating(float rating) {
	this.rating = rating;
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
	buffer.append("createBy").append("='").append(getCreateBy()).append("', ");
	buffer.append("rating").append("='").append(getRating()).append("', ");
	buffer.append("recording").append("='").append(getRecording()).append("', ");
	buffer.append("videoRecorderSession").append("='").append(getVideoRecorderSession()).append("', ");
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
	if (!(other instanceof VideoRecorderRating)) {
	    return false;
	}
	VideoRecorderRating castOther = (VideoRecorderRating) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }
}
