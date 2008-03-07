/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.taskList.web.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;
/**
 *  TaskList Item  Form.
 *	@struts.form name="taskListItemForm"
 * @author Steve.Ni
 * 
 * @version $Revision$
 */
public class TaskListItemForm extends ActionForm {
	private String itemIndex;
	private String sessionMapID;
	
	//tool access mode;
	private String mode;
	
	private String title;
	private String description;
	
	private boolean isRequired;
	private boolean isCommentsAllowed;
	private boolean showCommentsToAll;
	private boolean isChildTask;
	
	private String parentTaskName;
	
    private FormFile uploadedFile;
    private TaskListItemComment comment;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getItemIndex() {
		return itemIndex;
	}
	public void setItemIndex(String itemIndex) {
		this.itemIndex = itemIndex;
	}
	public String getSessionMapID() {
		return sessionMapID;
	}
	public void setSessionMapID(String sessionMapID) {
		this.sessionMapID = sessionMapID;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public boolean isRequired() {
		return isRequired;
	}
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	
	public boolean isCommentsAllowed() {
		return isCommentsAllowed;
	}
	public void setCommentsAllowed(boolean isCommentsAllowed) {
		this.isCommentsAllowed = isCommentsAllowed;
	}

	public boolean getShowCommentsToAll() {
		return showCommentsToAll;
	}
	public void setShowCommentsToAll(boolean showCommentsToAll) {
		this.showCommentsToAll = showCommentsToAll;
	}
	
	public boolean isChildTask() {
		return isChildTask;
	}
	public void setChildTask(boolean isChildTask) {
		this.isChildTask = isChildTask;
	}
	
	public String getParentTaskName() {
		return parentTaskName;
	}
	public void setParentTaskName(String parentTaskName) {
		this.parentTaskName = parentTaskName;
	}
	
	public FormFile getUploadedFile() {
		return uploadedFile;
	}
	public void setUploadedFile(FormFile uploadedFile) {
		this.uploadedFile = uploadedFile;
	}
	
	public TaskListItemComment getComment() {
		return comment;
	}
	public void setComment(TaskListItemComment comment) {
		this.comment = comment;
	}
}
