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

import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;

/**
 * DTO object which is used in a task summary page in monitoring. Contains
 * partial information for <code>TaskSummary</code> object specific for
 * current user and <code>TaskLIstItem</code> object.
 * 
 * @author Andrey Balan
 */
public class TaskSummaryItem {
	
	private TaskListUser user;
	private boolean completed;
	private Date date;
	
	private List<TaskListItemComment> comments;
	private List<TaskListItemAttachment> attachments;
	
	public TaskSummaryItem() {
		comments = new ArrayList<TaskListItemComment>();
		attachments = new ArrayList<TaskListItemAttachment>();
	}
	
	//  **********************************************************
  	//		Get/Set methods
	//  **********************************************************
	
	public TaskListUser getUser() {
		return user;
	}
	public void setUser(TaskListUser user) {
		this.user = user;
	}
	
	public boolean getCompleted() {
		return completed;
	}
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public List<TaskListItemComment> getComments() {
		return comments;
	}
	public List<TaskListItemAttachment> getAttachments() {
		return attachments;
	}

}
 