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
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.videoRecorder.service.VideoRecorderService;

/**
 * @hibernate.class table="tl_lavidr10_videorecorder"
 */

public class VideoRecorder implements java.io.Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 579733009969321015L;

    static Logger log = Logger.getLogger(VideoRecorderService.class.getName());

    // Fields
    /**
     *
     */
    private Long uid;

    private Date createDate;

    private Date updateDate;

    private Long createBy;

    private String title;

    private String instructions;

    boolean reflectOnActivity;

    String reflectInstructions;

    private boolean lockOnFinished;

    private boolean allowUseVoice;

    private boolean allowUseCamera;

    private boolean allowLearnerVideoVisibility;

    private boolean allowComments;

    private boolean allowRatings;

    private boolean exportOffline;

    private boolean exportAll;

    private boolean contentInUse;

    private boolean defineLater;

    private Long toolContentId;

    private Set videoRecorderSessions;

    private VideoRecorderRecording authorRecording;

    private Set<VideoRecorderCondition> conditions = new TreeSet<VideoRecorderCondition>(
	    new TextSearchConditionComparator());

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
     * @hibernate.property column="create_by" length="20"
     *
     */

    public Long getCreateBy() {
	return createBy;
    }

    public void setCreateBy(Long createBy) {
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
     * @hibernate.property column="instructions" length="65535"
     *
     */

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @hibernate.property column="reflect_on_activity" length="1"
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     * @hibernate.property column="reflect_instructions" length="65535"
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     * @hibernate.property column="lock_on_finished" length="1"
     *
     */

    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    /**
     * @hibernate.property column="allow_use_voice" length="1"
     * @return
     */
    public boolean isAllowUseVoice() {
	return allowUseVoice;
    }

    public void setAllowUseVoice(boolean allowUseVoice) {
	this.allowUseVoice = allowUseVoice;
    }

    /**
     * @hibernate.property column="allow_use_camera" length="1"
     * @return
     */
    public boolean isAllowUseCamera() {
	return allowUseCamera;
    }

    public void setAllowUseCamera(boolean allowUseCamera) {
	this.allowUseCamera = allowUseCamera;
    }

    /**
     * @hibernate.property column="allow_learner_video_visibility" length="1"
     * @return
     */
    public boolean isAllowLearnerVideoVisibility() {
	return allowLearnerVideoVisibility;
    }

    public void setAllowLearnerVideoVisibility(boolean allowLearnerVideoVisibility) {
	this.allowLearnerVideoVisibility = allowLearnerVideoVisibility;
    }

    /**
     * @hibernate.property column="allow_comments" length="1"
     * @return
     */
    public boolean isAllowComments() {
	return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
	this.allowComments = allowComments;
    }

    /**
     * @hibernate.property column="allow_ratings" length="1"
     * @return
     */
    public boolean isAllowRatings() {
	return allowRatings;
    }

    public void setAllowRatings(boolean allowRatings) {
	this.allowRatings = allowRatings;
    }

    /**
     * @hibernate.property column="export_offline" length="1"
     * @return
     */
    public boolean isExportOffline() {
	return exportOffline;
    }

    public void setExportOffline(boolean exportOffline) {
	this.exportOffline = exportOffline;
    }

    /**
     * @hibernate.property column="export_all" length="1"
     * @return
     */
    public boolean isExportAll() {
	return exportAll;
    }

    public void setExportAll(boolean exportAll) {
	this.exportAll = exportAll;
    }

    /**
     * @hibernate.property column="content_in_use" length="1"
     *
     */

    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * @hibernate.property column="define_later" length="1"
     *
     */

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @hibernate.property column="tool_content_id" length="20"
     *
     */

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="videoRecorder_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession"
     *
     */

    public Set getVideoRecorderSessions() {
	return videoRecorderSessions;
    }

    public void setVideoRecorderSessions(Set videoRecorderSessions) {
	this.videoRecorderSessions = videoRecorderSessions;
    }

    /**
     * @return Returns the first recording made by author
     *
     * @hibernate.one-to-one
     * 		      column="author_recording_id"
     *                       cascade="none"
     *
     */

    public VideoRecorderRecording getAuthorRecording() {
	return authorRecording;
    }

    public void setAuthorRecording(VideoRecorderRecording authorRecording) {
	this.authorRecording = authorRecording;
    }

    /**
     * @hibernate.set lazy="true" cascade="all"
     *                sort="org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator"
     * @hibernate.collection-key column="content_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderCondition"
     *
     */
    public Set<VideoRecorderCondition> getConditions() {
	return conditions;
    }

    public void setConditions(Set<VideoRecorderCondition> conditions) {
	this.conditions = conditions;
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
	buffer.append("title").append("='").append(getTitle()).append("' ");
	buffer.append("instructions").append("='").append(getInstructions()).append("' ");
	buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");
	buffer.append("allowUseVoice").append("='").append(isAllowUseVoice()).append("' ");
	buffer.append("allowUseCamera").append("='").append(isAllowUseCamera()).append("' ");
	buffer.append("allowLearnerVideoVisibility").append("='").append(isAllowLearnerVideoVisibility()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if (this == other) {
	    return true;
	}
	if (other == null) {
	    return false;
	}
	if (!(other instanceof VideoRecorder)) {
	    return false;
	}
	VideoRecorder castOther = (VideoRecorder) other;

	return this.getUid() == castOther.getUid()
		|| this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid());
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static VideoRecorder newInstance(VideoRecorder fromContent, Long toContentId) {
	VideoRecorder toContent = new VideoRecorder();
	toContent = (VideoRecorder) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    @Override
    protected Object clone() {

	VideoRecorder videoRecorder = null;
	try {
	    videoRecorder = (VideoRecorder) super.clone();
	    videoRecorder.setUid(null);

	    // create an empty set for the videoRecorderSession
	    videoRecorder.videoRecorderSessions = new HashSet();

	    if (conditions != null) {
		Set<VideoRecorderCondition> set = new TreeSet<VideoRecorderCondition>(
			new TextSearchConditionComparator());
		for (VideoRecorderCondition condition : conditions) {
		    set.add((VideoRecorderCondition) condition.clone());
		}
		videoRecorder.setConditions(set);
	    }

	} catch (CloneNotSupportedException cnse) {
	    VideoRecorder.log.error("Error cloning " + VideoRecorder.class);
	}
	return videoRecorder;
    }

}
