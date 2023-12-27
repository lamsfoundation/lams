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

package org.lamsfoundation.lams.tool.mindmap.web.forms;

import org.lamsfoundation.lams.web.util.SessionMap;

public class AuthoringForm {

    private String title;
    private String instructions;
    private boolean lockOnFinished;
    private boolean multiUserMode;
    private boolean galleryWalkEnabled;
    private boolean galleryWalkReadOnly;
    private String galleryWalkInstructions;
    private String currentTab;
    private String dispatch;
    private String sessionMapID;
    private SessionMap sessionMap;
    private String mindmapContent;

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(String currentTab) {
	this.currentTab = currentTab;
    }

    public String getDispatch() {
	return dispatch;
    }

    public void setDispatch(String dispatch) {
	this.dispatch = dispatch;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setSessionMap(SessionMap sessionMap) {
	this.sessionMap = sessionMap;
    }

    public SessionMap getSessionMap() {
	return sessionMap;
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

    public String getGalleryWalkInstructions() {
	return galleryWalkInstructions;
    }

    public void setGalleryWalkInstructions(String galleryWalkInstructions) {
	this.galleryWalkInstructions = galleryWalkInstructions;
    }

    public String getMindmapContent() {
	return mindmapContent;
    }

    public void setMindmapContent(String mindmapContent) {
	this.mindmapContent = mindmapContent;
    }
}
