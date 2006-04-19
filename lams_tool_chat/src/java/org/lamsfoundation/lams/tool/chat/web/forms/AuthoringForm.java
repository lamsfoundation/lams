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

package org.lamsfoundation.lams.tool.chat.web.forms;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.chat.beans.AuthoringSessionBean;

public class AuthoringForm extends ActionForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3950453134542135495L;

	// Properties

	String title;

	String instructions;

	String offlineInstruction;

	String onlineInstruction;

	Long toolContentID;

	String lockOnFinished;

	FormFile onlineFile; 

	FormFile offlineFile;

	String currentTab;

	String dispatch;
	
	String authSessionId;
	
	Long deleteFileUuid;

	AuthoringSessionBean authSession;

	public String getAuthSessionId() {
		return authSessionId;
	}

	public void setAuthSessionId(String authSessionId) {
		this.authSessionId = authSessionId;
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

	public String getLockOnFinished() {
		return lockOnFinished;
	}

	public void setLockOnFinished(String lockOnFinished) {
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

	public void setAuthSession(AuthoringSessionBean authSession) {
		this.authSession = authSession;
		
	}

	public AuthoringSessionBean getAuthSession() {
		return authSession;
	}

	public Long getDeleteFileUuid() {
		return deleteFileUuid;
	}

	public void setDeleteFileUuid(Long deleteFile) {
		this.deleteFileUuid = deleteFile;
	}
}
