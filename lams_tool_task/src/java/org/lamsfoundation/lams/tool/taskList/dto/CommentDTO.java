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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;

/**
 * DTO class used for export of TaskListItem's comments. 
 * 
 * @author Andrey Balan
 * 
 */
public class CommentDTO {

	private String comment;
	private String createdBy;
	private Date createDate;
	
	public CommentDTO(){}
	
	/**
	 * Contruction method for monitoring summary function. 
	 * 
	 * <B>Don't not set isInitGroup and viewNumber fields</B>
	 * @param sessionName
	 * @param item
	 * @param isInitGroup
	 * @param userLogin
	 */
	public CommentDTO(TaskListItemComment comment){
		this.comment = comment.getComment();
		this.createdBy = comment.getCreateBy().getLoginName();
		this.createDate = comment.getCreateDate();
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
 