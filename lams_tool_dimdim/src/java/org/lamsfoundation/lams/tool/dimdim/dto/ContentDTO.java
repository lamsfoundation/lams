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

package org.lamsfoundation.lams.tool.dimdim.dto;

import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.dimdim.model.Dimdim;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimAttachment;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimSession;

public class ContentDTO {

	private static final Logger logger = Logger.getLogger(ContentDTO.class);

	public Long toolContentId;

	public String title;

	public String instructions;

	public String onlineInstructions;

	public String offlineInstructions;

	public boolean defineLater;

	public boolean contentInUse;

	public boolean allowRichEditor;

	public boolean lockOnFinish;

	public Set<AttachmentDTO> onlineInstructionsFiles;

	public Set<AttachmentDTO> offlineInstructionsFiles;

	public Set<SessionDTO> sessionDTOs = new TreeSet<SessionDTO>();

	public Long currentTab;
	
	public Integer maxParticipants;

	public  Integer meetingDurationInHours;

	public  boolean allowVideo;

	public  Integer attendeeMikes;

	/* Constructors */
	public ContentDTO() {
	}

	public ContentDTO(Dimdim dimdim) {
		this.toolContentId = dimdim.getToolContentId();
		this.title = dimdim.getTitle();
		this.instructions = dimdim.getInstructions();
		this.onlineInstructions = dimdim.getOnlineInstructions();
		this.offlineInstructions = dimdim.getOfflineInstructions();
		this.contentInUse = dimdim.isContentInUse();
		this.allowRichEditor = dimdim.isAllowRichEditor();
		this.lockOnFinish = dimdim.isLockOnFinished();

		this.onlineInstructionsFiles = new TreeSet<AttachmentDTO>();
		this.offlineInstructionsFiles = new TreeSet<AttachmentDTO>();

		for (DimdimAttachment att : dimdim.getDimdimAttachments()) {
			Set<AttachmentDTO> attSet = null;
			if (att.getFileType().equals(IToolContentHandler.TYPE_OFFLINE)) {
				attSet = offlineInstructionsFiles;
			} else if (att.getFileType()
					.equals(IToolContentHandler.TYPE_ONLINE)) {
				attSet = onlineInstructionsFiles;
			} else {
				// something is wrong. Ignore file, log error
				logger.error("File with uid " + att.getFileUuid()
						+ " contains invalid fileType: " + att.getFileType());
			}

			if (attSet != null)
				attSet.add(new AttachmentDTO(att));
		}

		for (DimdimSession dimdimSession : dimdim.getDimdimSessions()) {
			sessionDTOs.add(new SessionDTO(dimdimSession));
		}
	}

	/* Getters / Setters */
	public Set<SessionDTO> getSessionDTOs() {
		return sessionDTOs;
	}

	public void setSessionDTOs(Set<SessionDTO> sessionDTOs) {
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

	public Set<AttachmentDTO> getOfflineInstructionsFiles() {
		return offlineInstructionsFiles;
	}

	public void setOfflineInstructionsFiles(
			Set<AttachmentDTO> offlineInstructionsFiles) {
		this.offlineInstructionsFiles = offlineInstructionsFiles;
	}

	public String getOnlineInstructions() {
		return onlineInstructions;
	}

	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}

	public Set<AttachmentDTO> getOnlineInstructionsFiles() {
		return onlineInstructionsFiles;
	}

	public void setOnlineInstructionsFiles(
			Set<AttachmentDTO> onlineInstructionsFiles) {
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

	public boolean isAllowRichEditor() {
		return allowRichEditor;
	}

	public void setAllowRichEditor(boolean allowRichEditor) {
		this.allowRichEditor = allowRichEditor;
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

	public Integer getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public Integer getMeetingDurationInHours() {
		return meetingDurationInHours;
	}

	public void setMeetingDurationInHours(Integer meetingDurationInHours) {
		this.meetingDurationInHours = meetingDurationInHours;
	}

	public boolean isAllowVideo() {
		return allowVideo;
	}

	public void setAllowVideo(boolean allowVideo) {
		this.allowVideo = allowVideo;
	}

	public Integer getAttendeeMikes() {
		return attendeeMikes;
	}

	public void setAttendeeMikes(Integer attendeeMikes) {
		this.attendeeMikes = attendeeMikes;
	}
}
