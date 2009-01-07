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

package org.lamsfoundation.lams.tool.pixlr.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.pixlr.model.Pixlr;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrAttachment;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrSession;

public class PixlrDTO {

    private static Logger logger = Logger.getLogger(PixlrDTO.class);

    public Long toolContentId;

    public String title;

    public String instructions;

    public String onlineInstructions;

    public String offlineInstructions;

    public boolean defineLater;

    public boolean contentInUse;

    public boolean reflectOnActivity;

    public boolean lockOnFinish;

    public Set<PixlrAttachmentDTO> onlineInstructionsFiles;

    public Set<PixlrAttachmentDTO> offlineInstructionsFiles;

    public Set<PixlrSessionDTO> sessionDTOs = new TreeSet<PixlrSessionDTO>();

    public Long currentTab;

    private String imageFileName;
    
    private String reflectInstructions;
    
    boolean allowViewOthersImages;
    
    private Long imageWidth;
    
    private Long imageHeight;

    /* Constructors */
    public PixlrDTO() {
    }

    public PixlrDTO(Pixlr pixlr) {
	this.toolContentId = pixlr.getToolContentId();
	this.title = pixlr.getTitle();
	this.instructions = pixlr.getInstructions();
	this.onlineInstructions = pixlr.getOnlineInstructions();
	this.offlineInstructions = pixlr.getOfflineInstructions();
	this.contentInUse = pixlr.isContentInUse();
	this.reflectOnActivity = pixlr.isReflectOnActivity();
	this.lockOnFinish = pixlr.isLockOnFinished();
	this.imageFileName = pixlr.getImageFileName();
	this.reflectInstructions = pixlr.getReflectInstructions();
	this.allowViewOthersImages = pixlr.isAllowViewOthersImages();

	this.onlineInstructionsFiles = new TreeSet<PixlrAttachmentDTO>();
	this.offlineInstructionsFiles = new TreeSet<PixlrAttachmentDTO>();

	for (Iterator<PixlrAttachment> i = pixlr.getPixlrAttachments().iterator(); i.hasNext();) {
	    PixlrAttachment att = (PixlrAttachment) i.next();
	    if (att.getFileType().equals(IToolContentHandler.TYPE_OFFLINE)) {
		PixlrAttachmentDTO attDTO = new PixlrAttachmentDTO(att);
		offlineInstructionsFiles.add(attDTO);
	    } else if (att.getFileType().equals(IToolContentHandler.TYPE_ONLINE)) {
		PixlrAttachmentDTO attDTO = new PixlrAttachmentDTO(att);
		onlineInstructionsFiles.add(attDTO);
	    } else {
		// something is wrong. Ignore file, log error
		logger.error("File with uid " + att.getFileUuid() + " contains invalid fileType: " + att.getFileType());
	    }
	}

	for (Iterator<PixlrSession> iter = pixlr.getPixlrSessions().iterator(); iter.hasNext();) {
	    PixlrSession session = (PixlrSession) iter.next();
	    PixlrSessionDTO sessionDTO = new PixlrSessionDTO(session);

	    sessionDTOs.add(sessionDTO);
	}
    }

    /* Getters / Setters */
    public Set<PixlrSessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<PixlrSessionDTO> sessionDTOs) {
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

    public Set<PixlrAttachmentDTO> getOfflineInstructionsFiles() {
	return offlineInstructionsFiles;
    }

    public void setOfflineInstructionsFiles(Set<PixlrAttachmentDTO> offlineInstructionsFiles) {
	this.offlineInstructionsFiles = offlineInstructionsFiles;
    }

    public String getOnlineInstructions() {
	return onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
	this.onlineInstructions = onlineInstructions;
    }

    public Set<PixlrAttachmentDTO> getOnlineInstructionsFiles() {
	return onlineInstructionsFiles;
    }

    public void setOnlineInstructionsFiles(Set<PixlrAttachmentDTO> onlineInstructionsFiles) {
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

    public boolean isAllowViewOthersImages() {
        return allowViewOthersImages;
    }

    public void setAllowViewOthersImages(boolean allowViewOthersImages) {
        this.allowViewOthersImages = allowViewOthersImages;
    }

    public Long getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Long imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Long getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Long imageHeight) {
        this.imageHeight = imageHeight;
    }
}
