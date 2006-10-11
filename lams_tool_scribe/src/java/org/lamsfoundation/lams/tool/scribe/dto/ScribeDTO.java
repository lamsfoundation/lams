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

package org.lamsfoundation.lams.tool.scribe.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeAttachment;

public class ScribeDTO {

	private static Logger logger = Logger.getLogger(ScribeDTO.class);

	public Long toolContentID;

	public String title;

	public String instructions;

	public String onlineInstructions;

	public String offlineInstructions;
	
	public boolean defineLater;
	
	public boolean contentInUse;
	
	public boolean reflectOnActivity;
	
	public String reflectInstructions;
	
	public Set<ScribeAttachmentDTO> onlineInstructionsFiles = new TreeSet<ScribeAttachmentDTO>();

	public Set<ScribeAttachmentDTO> offlineInstructionsFiles = new TreeSet<ScribeAttachmentDTO>();

	public Set<ScribeSessionDTO> sessionDTOs = new TreeSet<ScribeSessionDTO>();
	
	public Set<ScribeHeadingDTO> headingDTOs = new TreeSet<ScribeHeadingDTO>();
	
	public boolean autoSelectScribe;
	
	
	
	public ScribeDTO(Scribe scribe) {
		toolContentID = scribe.getToolContentId();
		title = scribe.getTitle();
		instructions = scribe.getInstructions();
		onlineInstructions = scribe.getOnlineInstructions();
		offlineInstructions = scribe.getOfflineInstructions();
		contentInUse = scribe.isContentInUse();
		reflectInstructions = scribe.getReflectInstructions();
		reflectOnActivity = scribe.isReflectOnActivity();

		// Adding attachments
		for (Iterator i = scribe.getScribeAttachments().iterator(); i.hasNext();) {
			ScribeAttachment att = (ScribeAttachment) i.next();
			if (att.getFileType().equals(IToolContentHandler.TYPE_OFFLINE)) {
				ScribeAttachmentDTO attDTO = new ScribeAttachmentDTO(att);
				offlineInstructionsFiles.add(attDTO);
			} else if (att.getFileType()
					.equals(IToolContentHandler.TYPE_ONLINE)) {
				ScribeAttachmentDTO attDTO = new ScribeAttachmentDTO(att);
				onlineInstructionsFiles.add(attDTO);
			} else {
				// something is wrong. Ignore file, log error
				logger.error("File with uid " + att.getFileUuid()
						+ " contains invalid fileType: " + att.getFileType());
			}
		}
		
	}

	public Set<ScribeSessionDTO> getSessionDTOs() {
		return sessionDTOs;
	}

	public void setSessionDTOs(Set<ScribeSessionDTO> sessionDTOs) {
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

	public Set<ScribeAttachmentDTO> getOfflineInstructionsFiles() {
		return offlineInstructionsFiles;
	}

	public void setOfflineInstructionsFiles(
			Set<ScribeAttachmentDTO> offlineInstructionsFiles) {
		this.offlineInstructionsFiles = offlineInstructionsFiles;
	}

	public String getOnlineInstructions() {
		return onlineInstructions;
	}

	public void setOnlineInstructions(String onlineInstructions) {
		this.onlineInstructions = onlineInstructions;
	}

	public Set<ScribeAttachmentDTO> getOnlineInstructionsFiles() {
		return onlineInstructionsFiles;
	}

	public void setOnlineInstructionsFiles(
			Set<ScribeAttachmentDTO> onlineInstructionsFiles) {
		this.onlineInstructionsFiles = onlineInstructionsFiles;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getToolContentID() {
		return toolContentID;
	}

	public void setToolContentID(Long toolContentID) {
		this.toolContentID = toolContentID;
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

	public boolean isAutoSelectScribe() {
		return autoSelectScribe;
	}

	public void setAutoSelectScribe(boolean autoSelectScribe) {
		this.autoSelectScribe = autoSelectScribe;
	}
}
