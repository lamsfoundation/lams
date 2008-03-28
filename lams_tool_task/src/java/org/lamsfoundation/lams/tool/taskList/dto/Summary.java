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
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;

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
public class Summary {

	private boolean isMonitorVerificationRequired;
	
	private List<TaskListUser> userNames;
	private List<TaskListItem> taskListItems;
	
	private boolean[][] completeMap; 
	private int[] visitNumbers;
	
	public Summary(){}
	
	/**
	 * Contruction method for monitoring summary function. 
	 * 
	 * <B>Don't not set isInitGroup and viewNumber fields</B>
	 * @param sessionName
	 * @param item
	 * @param isInitGroup
	 */
	public Summary(List<TaskListItem> taskListItems, List<TaskListUser> userNames, boolean[][] completeMap, int[] visitNumbers, boolean isMonitorVerificationRequired){
		this.userNames = userNames;
		this.taskListItems = taskListItems;
		this.completeMap = completeMap;
		this.visitNumbers = visitNumbers;
		this.isMonitorVerificationRequired = isMonitorVerificationRequired;
	}
	
	public List<TaskListUser> getUserNames() {
		return userNames;
	}
	public void setUserNames(List<TaskListUser> userNames) {
		this.userNames = userNames;
	}
	
	public List<TaskListItem> getTaskListItems() {
		return taskListItems;
	}
	public void setTaskListItems(List<TaskListItem> taskListItems) {
		this.taskListItems = taskListItems;
	}
	
	public boolean[][] getCompleteMap() {
		return completeMap;
	}
	public void setCompleteMap(boolean[][] completeMap) {
		this.completeMap = completeMap;
	}
	
	public int[] getVisitNumbers() {
		return visitNumbers;
	}
	public void setVisitNumbers(int[] visitNumbers) {
		this.visitNumbers = visitNumbers;
	}
	
	public boolean isMonitorVerificationRequired() {
		return isMonitorVerificationRequired;
	}
	public void setIsMonitorVerificationRequired(boolean isMonitorVerificationRequired) {
		this.isMonitorVerificationRequired = isMonitorVerificationRequired;
	}
	
}
