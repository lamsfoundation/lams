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

/* $Id$ */

package org.lamsfoundation.lams.tool.videoRecorder.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
	
public class VideoRecorderRecordingDTO {

	private static Logger logger = Logger.getLogger(VideoRecorderRecordingDTO.class);

	public Long uid;
	
	public Long toolContentId;

	public Date createDate;

	public Date updateDate;

	public VideoRecorderUser createBy;

	public String title;

	public String description;
	
	public Float rating;
	
	public Boolean isJustSound;
	
	public Boolean isLocal;
    
	public VideoRecorderSession videoRecorderSession;
	
	public String filename;
	
	public Set<VideoRecorderRatingDTO> ratings;
	
	public Set<VideoRecorderCommentDTO> comments;
	
	/* Constructors */
	public VideoRecorderRecordingDTO(){}
	
	public VideoRecorderRecordingDTO(VideoRecorderRecording videoRecorderRecording) {
		this.uid = videoRecorderRecording.getUid();
		this.toolContentId = videoRecorderRecording.getToolContentId();
		this.createDate = videoRecorderRecording.getCreateDate();
		this.updateDate = videoRecorderRecording.getUpdateDate();
		this.createBy = videoRecorderRecording.getCreateBy();
		this.title = videoRecorderRecording.getTitle();
		this.description = videoRecorderRecording.getDescription();
		this.rating = videoRecorderRecording.getRating();
		this.isJustSound = videoRecorderRecording.getIsJustSound();
		this.isLocal = videoRecorderRecording.getIsLocal();
		this.videoRecorderSession = videoRecorderRecording.getVideoRecorderSession();
		this.filename = videoRecorderRecording.getFilename();
		this.ratings = VideoRecorderRatingDTO.getVideoRecorderRatingDTOs(videoRecorderRecording.getRatings());
		this.comments = VideoRecorderCommentDTO.getVideoRecorderCommentDTOs(videoRecorderRecording.getComments());
	}

	public static List<VideoRecorderRecordingDTO> getVideoRecorderRecordingDTOs(List vrrList){
		List<VideoRecorderRecordingDTO> retSet = new ArrayList<VideoRecorderRecordingDTO>();
		if(vrrList == null || vrrList.isEmpty())
			return retSet;
		
		Iterator iter = vrrList.iterator();
		while(iter.hasNext()){
			VideoRecorderRecording vrr = (VideoRecorderRecording) iter.next();
			VideoRecorderRecordingDTO vrrDto = new VideoRecorderRecordingDTO(vrr);
			retSet.add(vrrDto);
		}
		return retSet;
	}
	
	/* Getters / Setters */

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }    
   
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public VideoRecorderUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(VideoRecorderUser createBy) {
	this.createBy = createBy;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }
    
    public Float getRating() {
	return rating;
    }

    public void setRating(Float rating) {
	this.rating = rating;
    }
        
	public Boolean getIsJustSound() {
		return isJustSound;
	}

	public void setIsJustSound(Boolean isJustSound) {
		this.isJustSound = isJustSound;
	}
	
	public Boolean getIsLocal() {
		return isLocal;
	}

	public void setIsLocal(Boolean isLocal) {
		this.isLocal = isLocal;
	}

	public VideoRecorderSession getVideoRecorderSession() {
		return this.videoRecorderSession;
	}

	public void setVideoRecorderSession(VideoRecorderSession videoRecorderSession) {
		this.videoRecorderSession = videoRecorderSession;
	}
	
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public Set<VideoRecorderCommentDTO> getComments() {
		return this.comments;
	}
	
	public void setComments(Set<VideoRecorderCommentDTO> comments) {
		this.comments = comments;
	}
	
	public Set<VideoRecorderRatingDTO> getRatings() {
		return this.ratings;
	}
	
	public void setRatings(Set<VideoRecorderRatingDTO> ratings) {
		this.ratings = ratings;
	}
}
