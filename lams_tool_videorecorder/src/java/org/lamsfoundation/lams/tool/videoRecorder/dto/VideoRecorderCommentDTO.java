/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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



package org.lamsfoundation.lams.tool.videoRecorder.dto;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderComment;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;

public class VideoRecorderCommentDTO implements Comparable<VideoRecorderCommentDTO> {

    private static Logger logger = Logger.getLogger(VideoRecorderRecordingDTO.class);

    public Long uid;

    public String text;

    public VideoRecorderRecording recording;

    public VideoRecorderUser createBy;

    public Date createDate;

    public VideoRecorderSession videoRecorderSession;

    /* Constructors */
    public VideoRecorderCommentDTO() {
    }

    public VideoRecorderCommentDTO(VideoRecorderComment videoRecorderComment) {
	this.uid = videoRecorderComment.getUid();
	this.createDate = videoRecorderComment.getCreateDate();
	this.createBy = videoRecorderComment.getCreateBy();
	this.text = videoRecorderComment.getText();
	this.recording = videoRecorderComment.getRecording();
	this.videoRecorderSession = videoRecorderComment.getVideoRecorderSession();
    }

    public static Set<VideoRecorderCommentDTO> getVideoRecorderCommentDTOs(Collection list) {
	Set<VideoRecorderCommentDTO> retSet = new TreeSet<VideoRecorderCommentDTO>();
	if (list == null || list.isEmpty()) {
	    return retSet;
	}

	Iterator iter = list.iterator();
	while (iter.hasNext()) {
	    VideoRecorderComment c = (VideoRecorderComment) iter.next();
	    VideoRecorderCommentDTO cDto = new VideoRecorderCommentDTO(c);
	    retSet.add(cDto);
	}
	return retSet;
    }

    @Override
    public int compareTo(VideoRecorderCommentDTO o) {
	if (this.createDate.after(o.createDate)) {
	    return -1;
	} else if (this.createDate == o.createDate) {
	    return 0;
	} else {
	    return 1;
	}
    }

    /* Getters / Setters */

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public VideoRecorderUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(VideoRecorderUser createBy) {
	this.createBy = createBy;
    }

    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public VideoRecorderRecording getRecording() {
	return this.recording;
    }

    public void setRecording(VideoRecorderRecording recording) {
	this.recording = recording;
    }

    public VideoRecorderSession getVideoRecorderSession() {
	return this.videoRecorderSession;
    }

    public void setVideoRecorderSession(VideoRecorderSession videoRecorderSession) {
	this.videoRecorderSession = videoRecorderSession;
    }
}
