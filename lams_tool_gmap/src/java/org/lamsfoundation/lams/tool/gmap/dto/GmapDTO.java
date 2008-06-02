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

package org.lamsfoundation.lams.tool.gmap.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapAttachment;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;

public class GmapDTO {

	private static Logger logger = Logger.getLogger(GmapDTO.class);

	public Long toolContentId;

	public String title;

	public String instructions;

	public String onlineInstructions;

	public String offlineInstructions;
	
	public boolean defineLater;
	
	public boolean contentInUse;
	
	//public boolean allowRichEditor;
	
	public Set<GmapAttachmentDTO> onlineInstructionsFiles;

	public Set<GmapAttachmentDTO> offlineInstructionsFiles;

	public Set<GmapSessionDTO> sessionDTOs = new TreeSet<GmapSessionDTO>();
	
	/* Constructors */
	public GmapDTO(){}
	
	public GmapDTO(Gmap gmap) {
		toolContentId = gmap.getToolContentId();
		title = gmap.getTitle();
		instructions = gmap.getInstructions();
		onlineInstructions = gmap.getOnlineInstructions();
		offlineInstructions = gmap.getOfflineInstructions();
		contentInUse = gmap.isContentInUse();
		//allowRichEditor = gmap.isAllowRichEditor();

		onlineInstructionsFiles = new TreeSet<GmapAttachmentDTO>();
		offlineInstructionsFiles = new TreeSet<GmapAttachmentDTO>();

		for (Iterator i = gmap.getGmapAttachments().iterator(); i.hasNext();) {
			GmapAttachment att = (GmapAttachment) i.next();
			if (att.getFileType().equals(IToolContentHandler.TYPE_OFFLINE)) {
				GmapAttachmentDTO attDTO = new GmapAttachmentDTO(att);
				offlineInstructionsFiles.add(attDTO);
			} else if (att.getFileType()
					.equals(IToolContentHandler.TYPE_ONLINE)) {
				GmapAttachmentDTO attDTO = new GmapAttachmentDTO(att);
				onlineInstructionsFiles.add(attDTO);
			} else {
				// something is wrong. Ignore file, log error
				logger.error("File with uid " + att.getFileUuid()
						+ " contains invalid fileType: " + att.getFileType());
			}
		}
		
		for (Iterator iter = gmap.getGmapSessions().iterator(); iter.hasNext();) {
			GmapSession session = (GmapSession) iter.next();
			GmapSessionDTO sessionDTO = new GmapSessionDTO(session);
			
			sessionDTOs.add(sessionDTO);
		}
	}

	/* Getters / Setters */
	public Set<GmapSessionDTO> getSessionDTOs() {
		return sessionDTOs;
	}
	
	public void setSessionDTOs(Set<GmapSessionDTO> sessionDTOs) {
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

	public Set<GmapAttachmentDTO> getOfflineInstructionsFiles() {
		return offlineInstructionsFiles;
	}

	public void setOfflineInstructionsFiles(
			Set<GmapAttachmentDTO> offlineInstructionsFiles) {
		this.offlineInstructionsFiles = offlineInstructionsFiles;
	}

	public String getOnlineInstructions() {
		return onlineInstructions;
	}

	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}

	public Set<GmapAttachmentDTO> getOnlineInstructionsFiles() {
		return onlineInstructionsFiles;
	}

	public void setOnlineInstructionsFiles(
			Set<GmapAttachmentDTO> onlineInstructionsFiles) {
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

	//public boolean isAllowRichEditor() {
	//	return allowRichEditor;
	//}

	//public void setAllowRichEditor(boolean allowRichEditor) {
	//	this.allowRichEditor = allowRichEditor;
	//}
}
