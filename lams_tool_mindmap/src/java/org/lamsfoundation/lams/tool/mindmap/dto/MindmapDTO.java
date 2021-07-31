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

package org.lamsfoundation.lams.tool.mindmap.dto;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.mindmap.model.Mindmap;
import org.lamsfoundation.lams.tool.mindmap.model.MindmapSession;

public class MindmapDTO {

    public Long toolContentId;
    public String title;
    public String instructions;
    public boolean defineLater;
    public boolean contentInUse;
    public boolean lockOnFinish;
    public boolean multiUserMode;
    public boolean galleryWalkEnabled;
    public boolean galleryWalkReadOnly;
    public boolean galleryWalkStarted;
    public boolean galleryWalkFinished;
    public boolean galleryWalkEditEnabled;
    public String galleryWalkInstructions;
    public Set<MindmapSessionDTO> sessionDTOs = new TreeSet<>();
    public Long currentTab;
    // reflection
    public boolean reflectOnActivity;
    public String reflectInstructions;

    /* Constructors */
    public MindmapDTO() {
    }

    public MindmapDTO(Mindmap mindmap) {
	toolContentId = mindmap.getToolContentId();
	title = mindmap.getTitle();
	instructions = mindmap.getInstructions();
	contentInUse = mindmap.isContentInUse();
	lockOnFinish = mindmap.isLockOnFinished();
	multiUserMode = mindmap.isMultiUserMode();
	galleryWalkEnabled = mindmap.isGalleryWalkEnabled();
	galleryWalkReadOnly = mindmap.isGalleryWalkReadOnly();
	galleryWalkStarted = mindmap.isGalleryWalkStarted();
	galleryWalkFinished = mindmap.isGalleryWalkFinished();
	galleryWalkEditEnabled = mindmap.isGalleryWalkEditEnabled();
	galleryWalkInstructions = mindmap.getGalleryWalkInstructions();
	reflectOnActivity = mindmap.isReflectOnActivity();
	reflectInstructions = mindmap.getReflectInstructions();

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

    public boolean isGalleryWalkEnabled() {
	return galleryWalkEnabled;
    }

    public void setGalleryWalkEnabled(boolean galleryWalkEnabled) {
	this.galleryWalkEnabled = galleryWalkEnabled;
    }

    public boolean isGalleryWalkReadOnly() {
	return galleryWalkReadOnly;
    }

    public void setGalleryWalkReadOnly(boolean galleryWalkReadOnly) {
	this.galleryWalkReadOnly = galleryWalkReadOnly;
    }

    public boolean isGalleryWalkStarted() {
	return galleryWalkStarted;
    }

    public void setGalleryWalkStarted(boolean galleryWalkStarted) {
	this.galleryWalkStarted = galleryWalkStarted;
    }

    public boolean isGalleryWalkFinished() {
	return galleryWalkFinished;
    }

    public void setGalleryWalkFinished(boolean galleryWalkFinished) {
	this.galleryWalkFinished = galleryWalkFinished;
    }

    public boolean isGalleryWalkEditEnabled() {
	return galleryWalkEditEnabled;
    }

    public void setGalleryWalkEditEnabled(boolean galleryWalkEditEnabled) {
	this.galleryWalkEditEnabled = galleryWalkEditEnabled;
    }

    public String getGalleryWalkInstructions() {
	return galleryWalkInstructions;
    }

    public void setGalleryWalkInstructions(String galleryWalkInstructions) {
	this.galleryWalkInstructions = galleryWalkInstructions;
    }

    public Long getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(Long currentTab) {
	this.currentTab = currentTab;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }
}
