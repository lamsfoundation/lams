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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.chat.model.Chat;
import org.lamsfoundation.lams.tool.chat.model.ChatAttachment;
import org.lamsfoundation.lams.tool.chat.model.ChatSession;

public class MonitoringDTO {

	private static Logger logger = Logger.getLogger(MonitoringDTO.class);

	public MonitoringDTO(Chat chat) {
		toolContentId = chat.getToolContentId();
		title = chat.getTitle();
		instructions = chat.getInstructions();
		onlineInstructions = chat.getOnlineInstructions();
		offlineInstructions = chat.getOfflineInstructions();

		// TODO create comparators for TreeSets.
		onlineInstructionsFiles = new HashSet<ChatAttachmentDTO>();
		offlineInstructionsFiles = new HashSet<ChatAttachmentDTO>();

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

		chatSessions = new TreeSet<ChatSessionDTO>();
		for (Iterator i = chat.getChatSessions().iterator(); i.hasNext();) {
			chatSessions.add(new ChatSessionDTO((ChatSession) i.next()));
		}
	}

	public Long toolContentId;

	public String title;

	public String instructions;

	public String onlineInstructions;

	public String offlineInstructions;

	public Set<ChatAttachmentDTO> onlineInstructionsFiles;

	public Set<ChatAttachmentDTO> offlineInstructionsFiles;

	public Set<ChatSessionDTO> chatSessions;
	
	public boolean chatEditable;

	public Set<ChatSessionDTO> getChatSessions() {
		return chatSessions;
	}

	public void setChatSessions(Set<ChatSessionDTO> chatSessions) {
		this.chatSessions = chatSessions;
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

	public boolean isChatEditable() {
		return chatEditable;
	}

	public void setChatEditable(boolean chatEditable) {
		this.chatEditable = chatEditable;
	}
}