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

package org.lamsfoundation.lams.tool.notebook.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookAttachment;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;

public class NotebookDTO {

	private static Logger logger = Logger.getLogger(NotebookDTO.class);

	public Long toolContentId;

	public String title;

	public String instructions;

	public String onlineInstructions;

	public String offlineInstructions;
	
	public boolean defineLater;
	
	public boolean contentInUse;
	
	public boolean allowRichEditor;
	
	public Set<NotebookAttachmentDTO> onlineInstructionsFiles;

	public Set<NotebookAttachmentDTO> offlineInstructionsFiles;

	public Set<NotebookSessionDTO> sessionDTOs = new TreeSet<NotebookSessionDTO>();
	
	/* Constructors */
	public NotebookDTO(){}
	
	public NotebookDTO(Notebook notebook) {
		toolContentId = notebook.getToolContentId();
		title = notebook.getTitle();
		instructions = notebook.getInstructions();
		onlineInstructions = notebook.getOnlineInstructions();
		offlineInstructions = notebook.getOfflineInstructions();
		contentInUse = notebook.getContentInUse();
		allowRichEditor = notebook.getAllowRichEditor();

		onlineInstructionsFiles = new TreeSet<NotebookAttachmentDTO>();
		offlineInstructionsFiles = new TreeSet<NotebookAttachmentDTO>();

		for (Iterator i = notebook.getNotebookAttachments().iterator(); i.hasNext();) {
			NotebookAttachment att = (NotebookAttachment) i.next();
			if (att.getFileType().equals(IToolContentHandler.TYPE_OFFLINE)) {
				NotebookAttachmentDTO attDTO = new NotebookAttachmentDTO(att);
				offlineInstructionsFiles.add(attDTO);
			} else if (att.getFileType()
					.equals(IToolContentHandler.TYPE_ONLINE)) {
				NotebookAttachmentDTO attDTO = new NotebookAttachmentDTO(att);
				onlineInstructionsFiles.add(attDTO);
			} else {
				// something is wrong. Ignore file, log error
				logger.error("File with uid " + att.getFileUuid()
						+ " contains invalid fileType: " + att.getFileType());
			}
		}
		
		for (Iterator iter = notebook.getNotebookSessions().iterator(); iter.hasNext();) {
			NotebookSession session = (NotebookSession) iter.next();
			NotebookSessionDTO sessionDTO = new NotebookSessionDTO(session);
			
			sessionDTOs.add(sessionDTO);
		}
	}

	/* Getters / Setters */
	public Set<NotebookSessionDTO> getSessionDTOs() {
		return sessionDTOs;
	}
	
	public void setSessionDTOs(Set<NotebookSessionDTO> sessionDTOs) {
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

	public Set<NotebookAttachmentDTO> getOfflineInstructionsFiles() {
		return offlineInstructionsFiles;
	}

	public void setOfflineInstructionsFiles(
			Set<NotebookAttachmentDTO> offlineInstructionsFiles) {
		this.offlineInstructionsFiles = offlineInstructionsFiles;
	}

	public String getOnlineInstructions() {
		return onlineInstructions;
	}

	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}

	public Set<NotebookAttachmentDTO> getOnlineInstructionsFiles() {
		return onlineInstructionsFiles;
	}

	public void setOnlineInstructionsFiles(
			Set<NotebookAttachmentDTO> onlineInstructionsFiles) {
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
}