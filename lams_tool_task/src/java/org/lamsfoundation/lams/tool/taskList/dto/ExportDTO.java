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
package org.lamsfoundation.lams.tool.taskList.dto;

import java.util.ArrayList;
import java.util.List;

import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;

/**
 * List contains following element: <br>
 * 
 * <li>session_id</li>
 * <li>session_name</li>
 * <li>TaskListItem.uid</li>
 * <li>TaskListItem.create_by_author</li>
 * <li>TaskListItem.is_hide</li>
 * <li>TaskListItem.title</li>
 * <li>User.login_name</li>
 * <li>count(taskList_item_uid)</li>
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class ExportDTO {

	private String sessionName;
	private Long itemUid;
	private boolean itemCreateByAuthor;
	private String itemTitle;
	private String username;
	
	//true: initial group item, false, belong to some group.
	private boolean isInitGroup;
	
	private List<CommentDTO> commentDTOs;
	private List<AttachmentDTO> attachmentDTOs;
	
	public ExportDTO(){}
	
	/**
	 * Contruction method for export profolio function. learner
	 * 
	 * <B>Don't not set sessionId and viewNumber fields</B>
	 * @param sessionName
	 * @param item
	 * @param isInitGroup
	 */
	public ExportDTO(String sessionName, TaskListItem item,boolean isInitGroup, String userLogin){
		commentDTOs = new ArrayList<CommentDTO>();
		attachmentDTOs = new ArrayList<AttachmentDTO>();
		this.sessionName = sessionName;
		if(item != null){
			this.itemUid = item.getUid();
			this.itemCreateByAuthor = item.isCreateByAuthor();
			this.itemTitle = item.getTitle();
			this.username = item.getCreateBy() == null?"":item.getCreateBy().getLoginName();
			
			if (item.isCommentsAllowed()) {
				for (Object objectComment:item.getComments()) {
					TaskListItemComment comment = (TaskListItemComment) objectComment;
						
					if (item.getShowCommentsToAll() || comment.getCreateBy().getLoginName().equals(userLogin)) {
						CommentDTO commentDTO = new CommentDTO(comment);
						commentDTOs.add(commentDTO);
					}
				}
					
				for (Object objectAttachment:item.getUploadedFileList()) {
					TaskListItemAttachment attachment = (TaskListItemAttachment) objectAttachment;
						
					if (item.getShowCommentsToAll() || attachment.getCreateBy().getLoginName().equals(userLogin)) {
						AttachmentDTO attachmentDTO = new AttachmentDTO(attachment);
						attachmentDTOs.add(attachmentDTO);
					}
				}
			}

		}else {
			this.itemUid = new Long(-1);
		}
			
		this.isInitGroup = isInitGroup;
	}
	
	/**
	 * Contruction method for export profolio function. teacher
	 * 
	 * <B>Don't not set sessionId and viewNumber fields</B>
	 * @param sessionName
	 * @param item
	 * @param isInitGroup
	 */
	public ExportDTO(String sessionName, TaskListItem item, boolean isInitGroup){
		commentDTOs = new ArrayList<CommentDTO>();
		attachmentDTOs = new ArrayList<AttachmentDTO>();
		this.sessionName = sessionName;
		if(item != null){
			this.itemUid = item.getUid();
			this.itemCreateByAuthor = item.isCreateByAuthor();
			this.itemTitle = item.getTitle();
			this.username = item.getCreateBy() == null?"":item.getCreateBy().getLoginName();
			
			if (item.isCommentsAllowed()) {
				for (Object objectComment:item.getComments()) {
					TaskListItemComment comment = (TaskListItemComment) objectComment;
					CommentDTO commentDTO = new CommentDTO(comment);
					commentDTOs.add(commentDTO);
				}
					
				for (Object objectAttachment:item.getUploadedFileList()) {
					TaskListItemAttachment attachment = (TaskListItemAttachment) objectAttachment;
					AttachmentDTO attachmentDTO = new AttachmentDTO(attachment);
					attachmentDTOs.add(attachmentDTO);
				}
			}

		}else {
			this.itemUid = new Long(-1);
		}
			
		this.isInitGroup = isInitGroup;
	}
	
	public boolean isItemCreateByAuthor() {
		return itemCreateByAuthor;
	}
	public void setItemCreateByAuthor(boolean itemCreateByAuthor) {
		this.itemCreateByAuthor = itemCreateByAuthor;
	}

	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public Long getItemUid() {
		return itemUid;
	}
	public void setItemUid(Long itemUid) {
		this.itemUid = itemUid;
	}

	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isInitGroup() {
		return isInitGroup;
	}

	public void setInitGroup(boolean isInitGroup) {
		this.isInitGroup = isInitGroup;
	}
	
	public List<CommentDTO> getCommentDTOs() {
		return commentDTOs;
	}
	public List<AttachmentDTO> getAttachmentDTOs() {
		return attachmentDTOs;
	}
	
}
