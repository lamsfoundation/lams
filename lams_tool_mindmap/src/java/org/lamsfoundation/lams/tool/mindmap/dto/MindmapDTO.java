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

package org.lamsfoundation.lams.tool.mindmap.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapAttachment;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;

public class MindmapDTO {

    private static Logger logger = Logger.getLogger(MindmapDTO.class);

    public Long toolContentId;
    public String title;
    public String instructions;
    public String onlineInstructions;
    public String offlineInstructions;
    public boolean defineLater;
    public boolean contentInUse;
    public boolean lockOnFinish;
    public boolean multiUserMode;
    public Set<MindmapAttachmentDTO> onlineInstructionsFiles;
    public Set<MindmapAttachmentDTO> offlineInstructionsFiles;
    public Set<MindmapSessionDTO> sessionDTOs = new TreeSet<MindmapSessionDTO>();
    public Long currentTab;

    public String reflectInstructions;

    /* Constructors */
    public MindmapDTO() {
    }

    public MindmapDTO(Mindmap mindmap) {
	toolContentId = mindmap.getToolContentId();
	title = mindmap.getTitle();
	instructions = mindmap.getInstructions();
	onlineInstructions = mindmap.getOnlineInstructions();
	offlineInstructions = mindmap.getOfflineInstructions();
	contentInUse = mindmap.isContentInUse();
	lockOnFinish = mindmap.isLockOnFinished();
	multiUserMode = mindmap.isMultiUserMode();

	onlineInstructionsFiles = new TreeSet<MindmapAttachmentDTO>();
	offlineInstructionsFiles = new TreeSet<MindmapAttachmentDTO>();

	for (Iterator i = mindmap.getMindmapAttachments().iterator(); i.hasNext();) {
	    MindmapAttachment att = (MindmapAttachment) i.next();
	    if (att.getFileType().equals(IToolContentHandler.TYPE_OFFLINE)) {
		MindmapAttachmentDTO attDTO = new MindmapAttachmentDTO(att);
		offlineInstructionsFiles.add(attDTO);
	    } else if (att.getFileType().equals(IToolContentHandler.TYPE_ONLINE)) {
		MindmapAttachmentDTO attDTO = new MindmapAttachmentDTO(att);
		onlineInstructionsFiles.add(attDTO);
	    } else {
		// something is wrong. Ignore file, log error
		logger.error("File with uid " + att.getFileUuid() + " contains invalid fileType: " + att.getFileType());
	    }
	}

	for (Iterator iter = mindmap.getMindmapSessions().iterator(); iter.hasNext();) {
	    MindmapSession session = (MindmapSession) iter.next();
	    MindmapSessionDTO sessionDTO = new MindmapSessionDTO(session);

	    sessionDTOs.add(sessionDTO);
	}
    }

    /* Getters / Setters */
    public Set<MindmapSessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<MindmapSessionDTO> sessionDTOs) {
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

    public Set<MindmapAttachmentDTO> getOfflineInstructionsFiles() {
	return offlineInstructionsFiles;
    }

    public void setOfflineInstructionsFiles(Set<MindmapAttachmentDTO> offlineInstructionsFiles) {
	this.offlineInstructionsFiles = offlineInstructionsFiles;
    }

    public String getOnlineInstructions() {
	return onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
	this.onlineInstructions = onlineInstructions;
    }

    public Set<MindmapAttachmentDTO> getOnlineInstructionsFiles() {
	return onlineInstructionsFiles;
    }

    public void setOnlineInstructionsFiles(Set<MindmapAttachmentDTO> onlineInstructionsFiles) {
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

    public boolean isLockOnFinish() {
	return lockOnFinish;
    }

    public void setLockOnFinish(boolean lockOnFinish) {
	this.lockOnFinish = lockOnFinish;
    }

    public boolean isMultiUserMode() {
	return multiUserMode;
    }

    public void setMultiUserMode(boolean multiUserMode) {
	this.multiUserMode = multiUserMode;
    }

    public Long getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(Long currentTab) {
	this.currentTab = currentTab;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }
}
