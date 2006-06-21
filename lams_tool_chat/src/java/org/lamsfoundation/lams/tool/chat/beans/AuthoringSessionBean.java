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
 
package org.lamsfoundation.lams.tool.chat.beans;

import java.util.LinkedList;
import java.util.List;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.chat.model.ChatAttachment;

public class AuthoringSessionBean {
	String authSessionId;
		
	List<ChatAttachment> onlineFilesList  = new LinkedList<ChatAttachment>();
	
	List<ChatAttachment> offlineFilesList  = new LinkedList<ChatAttachment>();
	
	List<ChatAttachment> unsavedOnlineFilesList = new LinkedList<ChatAttachment>();
	
	List<ChatAttachment> unsavedOfflineFilesList = new LinkedList<ChatAttachment>();
	
	List<ChatAttachment> deletedFilesList = new LinkedList<ChatAttachment>();
	
	ToolAccessMode mode;
	
	public String getAuthSessionId() {
		return authSessionId;
	}

	public void setAuthSessionId(String authSessionId) {
		this.authSessionId = authSessionId;
	}

	public List<ChatAttachment> getDeletedFilesList() {
		return deletedFilesList;
	}

	public void setDeletedFilesList(List<ChatAttachment> deletedFilesList) {
		this.deletedFilesList = deletedFilesList;
	}

	public List<ChatAttachment> getOfflineFilesList() {
		return offlineFilesList;
	}

	public void setOfflineFilesList(List<ChatAttachment> offlineFilesList) {
		this.offlineFilesList = offlineFilesList;
	}

	public List<ChatAttachment> getOnlineFilesList() {
		return onlineFilesList;
	}

	public void setOnlineFilesList(List<ChatAttachment> onlineFilesList) {
		this.onlineFilesList = onlineFilesList;
	}

	public List<ChatAttachment> getUnsavedOfflineFilesList() {
		return unsavedOfflineFilesList;
	}

	public void setUnsavedOfflineFilesList(
			List<ChatAttachment> unsavedOfflineFilesList) {
		this.unsavedOfflineFilesList = unsavedOfflineFilesList;
	}

	public List<ChatAttachment> getUnsavedOnlineFilesList() {
		return unsavedOnlineFilesList;
	}

	public void setUnsavedOnlineFilesList(
			List<ChatAttachment> unsavedOnlineFilesList) {
		this.unsavedOnlineFilesList = unsavedOnlineFilesList;
	}

	public ToolAccessMode getMode() {
		return mode;
	}

	public void setMode(ToolAccessMode mode) {
		this.mode = mode;
	}
}
