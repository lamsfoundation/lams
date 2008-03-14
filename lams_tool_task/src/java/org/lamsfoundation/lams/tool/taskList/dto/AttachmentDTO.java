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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */  
 
/* $Id$ */  
package org.lamsfoundation.lams.tool.taskList.dto;  

import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;

/**
 * DTO class used for export of TaskListItem's attachments. Usefull for
 * <code>TaskListAttachment</code> and <code>TaskListItemAttachment</code>
 * both. <br>
 * 
 * @author Andrey Balan
 * 
 */
public class AttachmentDTO {
	
	private Long fileUuid;
	private Long fileVersionId;
	private String fileName;
	private String attachmentLocalUrl;
	//following is used for TaskListItemAttachment only
	private String createdBy;
	
	public AttachmentDTO(){}
	
	/**
	 * Contruction method for export summary function. 
	 * 
	 * <B>Don't not set isInitGroup and viewNumber fields</B>
	 * @param sessionName
	 * @param item
	 * @param isInitGroup
	 */
	public AttachmentDTO(TaskListItemAttachment attachment){
		this.fileName = attachment.getFileName();
		this.fileUuid = attachment.getFileUuid();
		this.fileVersionId = attachment.getFileVersionId();
		this.createdBy = attachment.getCreateBy().getLoginName();
	}

	public Long getFileUuid() {
		return fileUuid;
	}
	public void setFileUuid(Long fileUuid) {
		this.fileUuid = fileUuid;
	}
	
	public Long getFileVersionId() {
		return fileVersionId;
	}
	public void setFileVersionId(Long fileVersionId) {
		this.fileVersionId = fileVersionId;
	}

	public String getAttachmentLocalUrl() {
		return attachmentLocalUrl;
	}
	public void setAttachmentLocalUrl(String attachmentLocalUrl) {
		this.attachmentLocalUrl = attachmentLocalUrl;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
 