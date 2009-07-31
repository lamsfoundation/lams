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

package org.lamsfoundation.lams.tool.wookie.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.wookie.model.Wookie;
import org.lamsfoundation.lams.tool.wookie.model.WookieAttachment;
import org.lamsfoundation.lams.tool.wookie.model.WookieSession;

public class WookieDTO {

    private static Logger logger = Logger.getLogger(WookieDTO.class);

    public Long toolContentId;

    public String title;

    public String instructions;

    public String onlineInstructions;

    public String offlineInstructions;

    public boolean defineLater;

    public boolean contentInUse;

    public boolean reflectOnActivity;

    public boolean lockOnFinish;

    public Set<WookieAttachmentDTO> onlineInstructionsFiles;

    public Set<WookieAttachmentDTO> offlineInstructionsFiles;

    public Set<WookieSessionDTO> sessionDTOs = new TreeSet<WookieSessionDTO>();

    public Long currentTab;

    private String imageFileName;

    private String reflectInstructions;

    /* Constructors */
    public WookieDTO() {
    }

    public WookieDTO(Wookie wookie) {
	this.toolContentId = wookie.getToolContentId();
	this.title = wookie.getTitle();
	this.instructions = wookie.getInstructions();
	this.onlineInstructions = wookie.getOnlineInstructions();
	this.offlineInstructions = wookie.getOfflineInstructions();
	this.contentInUse = wookie.isContentInUse();
	this.reflectOnActivity = wookie.isReflectOnActivity();
	this.lockOnFinish = wookie.isLockOnFinished();
	this.reflectInstructions = wookie.getReflectInstructions();
	this.onlineInstructionsFiles = new TreeSet<WookieAttachmentDTO>();
	this.offlineInstructionsFiles = new TreeSet<WookieAttachmentDTO>();

	for (Iterator<WookieAttachment> i = wookie.getWookieAttachments().iterator(); i.hasNext();) {
	    WookieAttachment att = (WookieAttachment) i.next();
	    if (att.getFileType().equals(IToolContentHandler.TYPE_OFFLINE)) {
		WookieAttachmentDTO attDTO = new WookieAttachmentDTO(att);
		offlineInstructionsFiles.add(attDTO);
	    } else if (att.getFileType().equals(IToolContentHandler.TYPE_ONLINE)) {
		WookieAttachmentDTO attDTO = new WookieAttachmentDTO(att);
		onlineInstructionsFiles.add(attDTO);
	    } else {
		// something is wrong. Ignore file, log error
		logger.error("File with uid " + att.getFileUuid() + " contains invalid fileType: " + att.getFileType());
	    }
	}

	for (Iterator<WookieSession> iter = wookie.getWookieSessions().iterator(); iter.hasNext();) {
	    WookieSession session = (WookieSession) iter.next();
	    WookieSessionDTO sessionDTO = new WookieSessionDTO(session);

	    sessionDTOs.add(sessionDTO);
	}
    }

    /* Getters / Setters */
    public Set<WookieSessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<WookieSessionDTO> sessionDTOs) {
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

    public Set<WookieAttachmentDTO> getOfflineInstructionsFiles() {
	return offlineInstructionsFiles;
    }

    public void setOfflineInstructionsFiles(Set<WookieAttachmentDTO> offlineInstructionsFiles) {
	this.offlineInstructionsFiles = offlineInstructionsFiles;
    }

    public String getOnlineInstructions() {
	return onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
	this.onlineInstructions = onlineInstructions;
    }

    public Set<WookieAttachmentDTO> getOnlineInstructionsFiles() {
	return onlineInstructionsFiles;
    }

    public void setOnlineInstructionsFiles(Set<WookieAttachmentDTO> onlineInstructionsFiles) {
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

    public Long getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(Long currentTab) {
	this.currentTab = currentTab;
    }

    public String getImageFileName() {
	return imageFileName;
    }

    public void getImageFileName(String imageFileName) {
	this.imageFileName = imageFileName;
    }

    public void setImageFileName(String imageFileName) {
	this.imageFileName = imageFileName;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }
}
