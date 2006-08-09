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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */

package org.lamsfoundation.lams.tool.chat.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatAttachment;

public class ChatDTO {

	private static Logger logger = Logger.getLogger(ChatDTO.class);

	public Long toolContentId;

	public String title;

	public String instructions;

	public String onlineInstructions;

	public String offlineInstructions;
	
	public boolean defineLater;
	
	public boolean contentInUse;
	
	public boolean reflectOnActivity;
	
	public boolean lockOnFinish;
	
	public String reflectInstructions;
	
	public Set<ChatAttachmentDTO> onlineInstructionsFiles = new TreeSet<ChatAttachmentDTO>();

	public Set<ChatAttachmentDTO> offlineInstructionsFiles = new TreeSet<ChatAttachmentDTO>();

	public Set<ChatSessionDTO> sessionDTOs = new TreeSet<ChatSessionDTO>();;
	
	public ChatDTO(Chat chat) {
		toolContentId = chat.getToolContentId();
		title = chat.getTitle();
		instructions = chat.getInstructions();
		onlineInstructions = chat.getOnlineInstructions();
		offlineInstructions = chat.getOfflineInstructions();
		contentInUse = chat.getContentInUse();
		reflectInstructions = chat.getReflectInstructions();
		reflectOnActivity = chat.getReflectOnActivity();
		lockOnFinish = chat.getLockOnFinished();

		for (Iterator i = chat.getChatAttachments().iterator(); i.hasNext();) {
			ChatAttachment att = (ChatAttachment) i.next();
			if (att.getFileType().equals(IToolContentHandler.TYPE_OFFLINE)) {
				ChatAttachmentDTO attDTO = new ChatAttachmentDTO(att);
				offlineInstructionsFiles.add(attDTO);
			} else if (att.getFileType()
					.equals(IToolContentHandler.TYPE_ONLINE)) {
				ChatAttachmentDTO attDTO = new ChatAttachmentDTO(att);
				onlineInstructionsFiles.add(attDTO);
			} else {
				// something is wrong. Ignore file, log error
				logger.error("File with uid " + att.getFileUuid()
						+ " contains invalid fileType: " + att.getFileType());
			}
		} 
	}

	public Set<ChatSessionDTO> getSessionDTOs() {
		return sessionDTOs;
	}

	public void setSessionDTOs(Set<ChatSessionDTO> sessionDTOs) {
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

	public Set<ChatAttachmentDTO> getOfflineInstructionsFiles() {
		return offlineInstructionsFiles;
	}

	public void setOfflineInstructionsFiles(
			Set<ChatAttachmentDTO> offlineInstructionsFiles) {
		this.offlineInstructionsFiles = offlineInstructionsFiles;
	}

	public String getOnlineInstructions() {
		return onlineInstructions;
	}

	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}

	public Set<ChatAttachmentDTO> getOnlineInstructionsFiles() {
		return onlineInstructionsFiles;
	}

	public void setOnlineInstructionsFiles(
			Set<ChatAttachmentDTO> onlineInstructionsFiles) {
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

	public String getReflectInstructions() {
		return reflectInstructions;
	}

	public void setReflectInstructions(String reflectInstructions) {
		this.reflectInstructions = reflectInstructions;
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

	public boolean isReflectOnActivity() {
		return reflectOnActivity;
	}

	public void setReflectOnActivity(boolean reflectOnActivity) {
		this.reflectOnActivity = reflectOnActivity;
	}

	public boolean isLockOnFinish() {
		return lockOnFinish;
	}

	public void setLockOnFinish(boolean lockOnFinish) {
		this.lockOnFinish = lockOnFinish;
	}
}