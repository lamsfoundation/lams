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
package org.lamsfoundation.lams.tool.taskList.model;

import java.util.Date;
/**
 * TaskList
 * @author Andrey Balan
 *
 * @hibernate.class  table="tl_latask10_item_comment"
 *
 */
public class TaskListItemComment {

	private Long uid;
	private String comment;
	private TaskListItem taskListItem;
	private TaskListUser createBy;
	private Date createDate;
	
	/**
	 * @hibernate.property column="create_date"
	 * @return
	 */
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * @hibernate.many-to-one  column="taskList_item_uid"
 	 * cascade="none"
	 * @return
	 */
	public TaskListItem getTaskListItem() {
		return taskListItem;
	}
	public void setTaskListItem(TaskListItem item) {
		this.taskListItem = item;
	}
	
	/**
	 * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
	 * @return Returns the log Uid.
	 */
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	/**
	 * @hibernate.many-to-one  column="create_by"
 	 * cascade="none"
	 * @return
	 */
	public TaskListUser getCreateBy() {
		return createBy;
	}
	public void setCreateBy(TaskListUser createBy) {
		this.createBy = createBy;
	}
	/**
	 * @hibernate.property column="comment"
	 * @return
	 */
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
