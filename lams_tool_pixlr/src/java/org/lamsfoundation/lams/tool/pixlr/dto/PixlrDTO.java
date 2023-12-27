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



package org.lamsfoundation.lams.tool.pixlr.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.pixlr.model.Pixlr;
import org.lamsfoundation.lams.tool.pixlr.model.PixlrSession;

public class PixlrDTO {

    private static Logger logger = Logger.getLogger(PixlrDTO.class);

    public Long toolContentId;

    public String title;

    public String instructions;

    public boolean defineLater;

    public boolean contentInUse;;

    public boolean lockOnFinish;

    public Set<PixlrSessionDTO> sessionDTOs = new TreeSet<PixlrSessionDTO>();

    public Long currentTab;

    private String imageFileName;

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
	this.contentInUse = pixlr.isContentInUse();
	this.lockOnFinish = pixlr.isLockOnFinished();
	this.imageFileName = pixlr.getImageFileName();
	this.allowViewOthersImages = pixlr.isAllowViewOthersImages();

	for (Iterator<PixlrSession> iter = pixlr.getPixlrSessions().iterator(); iter.hasNext();) {
	    PixlrSession session = iter.next();
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
