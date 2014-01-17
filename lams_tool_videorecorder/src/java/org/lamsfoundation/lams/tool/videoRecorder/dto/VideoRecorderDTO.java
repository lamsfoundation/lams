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

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;

public class VideoRecorderDTO {

	private static Logger logger = Logger.getLogger(VideoRecorderDTO.class);

	public Long toolContentId;

	public String title;

	public String instructions;
	
	public boolean reflectOnActivity;
	
	public String reflectInstructions;
	
	public boolean defineLater;
	
	public boolean contentInUse;
	
	public boolean lockOnFinish;
	
    public boolean allowUseVoice;
    
    public boolean allowUseCamera;
    
    public boolean allowLearnerVideoVisibility;
    
    public boolean allowComments;
    
    public boolean allowRatings;
    
    public boolean exportOffline;
    
    public boolean exportAll;

	public Set<VideoRecorderSessionDTO> sessionDTOs = new TreeSet<VideoRecorderSessionDTO>();
	
	public VideoRecorderRecordingDTO authorRecording;
	
	public Long currentTab;
	
	/* Constructors */
	public VideoRecorderDTO(){}
	
	public VideoRecorderDTO(VideoRecorder videoRecorder) {
		toolContentId = videoRecorder.getToolContentId();
		title = videoRecorder.getTitle();
		instructions = videoRecorder.getInstructions();
		contentInUse = videoRecorder.isContentInUse();
		lockOnFinish = videoRecorder.isLockOnFinished();
		allowUseVoice = videoRecorder.isAllowUseVoice();
		allowUseCamera = videoRecorder.isAllowUseCamera();
		allowLearnerVideoVisibility = videoRecorder.isAllowLearnerVideoVisibility();
		allowComments = videoRecorder.isAllowComments();
		allowRatings = videoRecorder.isAllowRatings();
		exportAll = videoRecorder.isExportAll();
		exportOffline = videoRecorder.isExportOffline();
		reflectOnActivity = videoRecorder.isReflectOnActivity();
		reflectInstructions = videoRecorder.getReflectInstructions();

		if(videoRecorder.getAuthorRecording() != null){
			authorRecording = new VideoRecorderRecordingDTO();
		}
	}

	/* Getters / Setters */
	public Set<VideoRecorderSessionDTO> getSessionDTOs() {
		return sessionDTOs;
	}
	
	public void setSessionDTOs(Set<VideoRecorderSessionDTO> sessionDTOs) {
		this.sessionDTOs = sessionDTOs;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public VideoRecorderRecordingDTO getAuthorRecording() {
		return authorRecording;
	}

	public void setAuthorRecording(VideoRecorderRecordingDTO authorRecording) {
		this.authorRecording = authorRecording;
	}

	public boolean isReflectOnActivity() {
		return reflectOnActivity;
	}

	public void setReflectOnActivity(boolean reflectOnActivity) {
		this.reflectOnActivity = reflectOnActivity;
	}

	public String getReflectInstructions() {
		return reflectInstructions;
	}

	public void setReflectInstructions(String reflectInstructions) {
		this.reflectInstructions = reflectInstructions;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getToolContentId() {
		return toolContentId;
	}

	public void setToolContentId(Long toolContentID) {
		this.toolContentId = toolContentID;
	}

	public Boolean getContentInUse() {
		return contentInUse;
	}

	public void setContentInUse(Boolean contentInUse) {
		this.contentInUse = contentInUse;
	}
	
	public boolean isAllowUseVoice() {
		return allowUseVoice;
	}

	public void setAllowUseVoice(boolean allowUseVoice) {
		this.allowUseVoice = allowUseVoice;
	}

	public boolean isAllowUseCamera() {
		return allowUseCamera;
	}

	public void setAllowUseCamera(boolean allowUseCamera) {
		this.allowUseCamera = allowUseCamera;
	}

	public boolean isAllowLearnerVideoVisibility() {
		return allowLearnerVideoVisibility;
	}

	public void setLearnerVideoVisibility(boolean allowLearnerVideoVisibility) {
		this.allowLearnerVideoVisibility = allowLearnerVideoVisibility;
	}
		
	public boolean isAllowComments() {
		return allowComments;
	}

	public void setAllowComments(boolean allowComments) {
		this.allowComments = allowComments;
	}
	
	public boolean isAllowRatings() {
		return allowRatings;
	}

	public void setAllowRatings(boolean allowRatings) {
		this.allowRatings = allowRatings;
	}
	
	public boolean isExportOffline() {
		return exportOffline;
	}

	public void setExportOffline(boolean exportOffline) {
		this.exportOffline = exportOffline;
	}
	
	public boolean isExportAll() {
		return exportAll;
	}

	public void setExportAll(boolean exportAll) {
		this.exportAll = exportAll;
	}
	
	public boolean isLockOnFinish() {
		return lockOnFinish;
	}

	public void setLockOnFinish(boolean lockOnFinish) {
		this.lockOnFinish = lockOnFinish;
	}

	public Long getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(Long currentTab) {
		this.currentTab = currentTab;
	}
}
