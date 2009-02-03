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
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderAttachment;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;

public class VideoRecorderDTO {

	private static Logger logger = Logger.getLogger(VideoRecorderDTO.class);

	public Long toolContentId;

	public String title;

	public String instructions;

	public String onlineInstructions;

	public String offlineInstructions;
	
	public boolean defineLater;
	
	public boolean contentInUse;
	
	public boolean lockOnFinish;
	
    public boolean allowUseVoice;
    
    public boolean allowUseCamera;
    
    public boolean allowLearnerVideoExport;
    
    public boolean allowLearnerVideoVisibility;
    
    public boolean allowComments;
    
    public boolean allowRatings;
	
	public Set<VideoRecorderAttachmentDTO> onlineInstructionsFiles;

	public Set<VideoRecorderAttachmentDTO> offlineInstructionsFiles;

	public Set<VideoRecorderSessionDTO> sessionDTOs = new TreeSet<VideoRecorderSessionDTO>();
	
	public Long currentTab;
	
	/* Constructors */
	public VideoRecorderDTO(){}
	
	public VideoRecorderDTO(VideoRecorder videoRecorder) {
		toolContentId = videoRecorder.getToolContentId();
		title = videoRecorder.getTitle();
		instructions = videoRecorder.getInstructions();
		onlineInstructions = videoRecorder.getOnlineInstructions();
		offlineInstructions = videoRecorder.getOfflineInstructions();
		contentInUse = videoRecorder.isContentInUse();
		lockOnFinish = videoRecorder.isLockOnFinished();
		allowUseVoice = videoRecorder.isAllowUseVoice();
		allowUseCamera = videoRecorder.isAllowUseCamera();
		allowLearnerVideoExport = videoRecorder.isAllowLearnerVideoExport();
		allowLearnerVideoVisibility = videoRecorder.isAllowLearnerVideoVisibility();
		allowComments = videoRecorder.isAllowComments();
		allowRatings = videoRecorder.isAllowRatings();

		onlineInstructionsFiles = new TreeSet<VideoRecorderAttachmentDTO>();
		offlineInstructionsFiles = new TreeSet<VideoRecorderAttachmentDTO>();

		for (Iterator i = videoRecorder.getVideoRecorderAttachments().iterator(); i.hasNext();) {
			VideoRecorderAttachment att = (VideoRecorderAttachment) i.next();
			if (att.getFileType().equals(IToolContentHandler.TYPE_OFFLINE)) {
				VideoRecorderAttachmentDTO attDTO = new VideoRecorderAttachmentDTO(att);
				offlineInstructionsFiles.add(attDTO);
			} else if (att.getFileType()
					.equals(IToolContentHandler.TYPE_ONLINE)) {
				VideoRecorderAttachmentDTO attDTO = new VideoRecorderAttachmentDTO(att);
				onlineInstructionsFiles.add(attDTO);
			} else {
				// something is wrong. Ignore file, log error
				logger.error("File with uid " + att.getFileUuid()
						+ " contains invalid fileType: " + att.getFileType());
			}
		}
		
		for (Iterator iter = videoRecorder.getVideoRecorderSessions().iterator(); iter.hasNext();) {
			VideoRecorderSession session = (VideoRecorderSession) iter.next();
			VideoRecorderSessionDTO sessionDTO = new VideoRecorderSessionDTO(session);
			
			sessionDTOs.add(sessionDTO);
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

	public String getOfflineInstructions() {
		return offlineInstructions;
	}

	public void setOfflineInstructions(String offlineInstructions) {
		this.offlineInstructions = offlineInstructions;
	}

	public Set<VideoRecorderAttachmentDTO> getOfflineInstructionsFiles() {
		return offlineInstructionsFiles;
	}

	public void setOfflineInstructionsFiles(
			Set<VideoRecorderAttachmentDTO> offlineInstructionsFiles) {
		this.offlineInstructionsFiles = offlineInstructionsFiles;
	}

	public String getOnlineInstructions() {
		return onlineInstructions;
	}

	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}

	public Set<VideoRecorderAttachmentDTO> getOnlineInstructionsFiles() {
		return onlineInstructionsFiles;
	}

	public void setOnlineInstructionsFiles(
			Set<VideoRecorderAttachmentDTO> onlineInstructionsFiles) {
		this.onlineInstructionsFiles = onlineInstructionsFiles;
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
	
	public boolean isAllowLearnerVideoExport() {
		return allowLearnerVideoExport;
	}

	public void setAllowLearnerVideoExport(boolean allowLearnerVideoExport) {
		this.allowLearnerVideoExport = allowLearnerVideoExport;
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
