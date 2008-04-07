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

import java.util.List;

import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;

/**
 * DTO object intented to be used in a task summary page (monitoring). Contains
 * <code>TaskLIstItem</code>'s overall information and list of
 * <code>TaskSummaryItem</code> with more detailed information specific for each user.
 * 
 * @author Andrey Balan
 */
public class TaskSummary {

	private TaskListItem taskListItem;
	
	private List<TaskSummaryItem> taskSummaryItems;

	public TaskSummary(TaskListItem taskListItem, List<TaskSummaryItem> taskSummaryItems) {
		this.taskListItem = taskListItem;
		
		this.taskSummaryItems = taskSummaryItems;
	}
	
	//  **********************************************************
  	//		Get/Set methods
	//  **********************************************************
	
	/**
	 * Returns TaskListItem described by this current TaskSummary.
	 * 
	 * @return TaskListItem described by this current TaskSummary
	 */
	public TaskListItem getTaskListItem() {
		return taskListItem;
	}
	/**
	 * Sets TaskListItem described by this current TaskSummary.
	 * 
	 * @param taskListItem TaskListItem described by this current TaskSummary
	 */
	public void setTaskListItem(TaskListItem taskListItem) {
		this.taskListItem = taskListItem;
	}
	
	/**
	 * Returns list of TaskSummaryItem, each one for every TaskListItem.
	 * 
	 * @return 
	 */
	public List<TaskSummaryItem> getTaskSummaryItems() {
		return taskSummaryItems;
	}
	public List<TaskSummaryItem> setTaskSummaryItems(List<TaskSummaryItem> taskSummaryItems) {
		return this.taskSummaryItems = taskSummaryItems;
	}
	
}
 