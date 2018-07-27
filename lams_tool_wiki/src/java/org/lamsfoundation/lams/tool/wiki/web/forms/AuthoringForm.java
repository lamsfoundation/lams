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



package org.lamsfoundation.lams.tool.wiki.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 *
 */
public class AuthoringForm extends WikiPageForm {

    private static final long serialVersionUID = 353256767734345767L;

    // Properties

    String offlineInstruction;

    String onlineInstruction;

    boolean lockOnFinished;

    boolean allowLearnerCreatePages;

    boolean allowLearnerInsertLinks;

    boolean allowLearnerAttachImages;

    boolean notifyUpdates;

    boolean reflectOnActivity;

    String reflectInstructions;

    Integer minimumEdits;

    Integer maximumEdits;

    FormFile onlineFile;

    FormFile offlineFile;

    String currentTab;

    String dispatch;

    String sessionMapID;

    Long deleteFileUuid;

    Long toolContentID;

    String contentFolderID;

    String mode;

    SessionMap sessionMap;

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
	ActionErrors ac = new ActionErrors();
	ac.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("this is an error"));

	return ac;
    }

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

    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public FormFile getOfflineFile() {
	return offlineFile;
    }

    public void setOfflineFile(FormFile offlineFile) {
	this.offlineFile = offlineFile;
    }

    public String getOfflineInstruction() {
	return offlineInstruction;
    }

    public void setOfflineInstruction(String offlineInstruction) {
	this.offlineInstruction = offlineInstruction;
    }

    public FormFile getOnlineFile() {
	return onlineFile;
    }

    public void setOnlineFile(FormFile onlineFile) {
	this.onlineFile = onlineFile;
    }

    public String getOnlineInstruction() {
	return onlineInstruction;
    }

    public void setOnlineInstruction(String onlineInstruction) {
	this.onlineInstruction = onlineInstruction;
    }

    public void setSessionMap(SessionMap sessionMap) {
	this.sessionMap = sessionMap;
    }

    public SessionMap getSessionMap() {
	return sessionMap;
    }

    public Long getDeleteFileUuid() {
	return deleteFileUuid;
    }

    public void setDeleteFileUuid(Long deleteFile) {
	this.deleteFileUuid = deleteFile;
    }

    public boolean isAllowLearnerCreatePages() {
	return allowLearnerCreatePages;
    }

    public void setAllowLearnerCreatePages(boolean allowLearnerCreatePages) {
	this.allowLearnerCreatePages = allowLearnerCreatePages;
    }

    public boolean isAllowLearnerInsertLinks() {
	return allowLearnerInsertLinks;
    }

    public void setAllowLearnerInsertLinks(boolean allowLearnerInsertLinks) {
	this.allowLearnerInsertLinks = allowLearnerInsertLinks;
    }

    public boolean isAllowLearnerAttachImages() {
	return allowLearnerAttachImages;
    }

    public void setAllowLearnerAttachImages(boolean allowLearnerAttachImages) {
	this.allowLearnerAttachImages = allowLearnerAttachImages;
    }

    public boolean isNotifyUpdates() {
	return notifyUpdates;
    }

    public void setNotifyUpdates(boolean notifyUpdates) {
	this.notifyUpdates = notifyUpdates;
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

    public Integer getMinimumEdits() {
	return minimumEdits;
    }

    public void setMinimumEdits(Integer minimumEdits) {
	this.minimumEdits = minimumEdits;
    }

    public Integer getMaximumEdits() {
	return maximumEdits;
    }

    public void setMaximumEdits(Integer maximumEdits) {
	this.maximumEdits = maximumEdits;
    }

    public Long getToolContentID() {
	return toolContentID;
    }

    public void setToolContentID(Long toolContentID) {
	this.toolContentID = toolContentID;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    public String getMode() {
	return mode;
    }

    public void setMode(String mode) {
	this.mode = mode;
    }
}
