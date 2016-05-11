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

package org.lamsfoundation.lams.tool.taskList.web.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

/**
 * Form responsible for representing <code>TaskListItem</code> objects on a view layer.
 *
 * @author Steve.Ni
 * @author Andrey Balan
 *
 * @struts.form name="taskListItemForm"
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
    private boolean isCommentsRequired;
    private boolean isFilesAllowed;
    private boolean isFilesRequired;
    private boolean isChildTask;

    // The next 2 options always will be true (as it set in jsp). Waiting
    // for the final decision -- if this options will be needed later.
    private boolean isCommentsFilesAllowed;
    private boolean showCommentsToAll;

    private String parentTaskName;

    private FormFile uploadedFile;
    private String comment;

    /**
     * Returns TaskListItem title.
     * 
     * @return TaskListItem title
     */
    public String getTitle() {
	return title;
    }

    /**
     * Sets TaskListItem title.
     * 
     * @param title
     *            TaskListItem title
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * Returns TaskListItem description.
     * 
     * @return TaskListItem description
     */
    public String getDescription() {
	return description;
    }

    /**
     * Sets TaskListItem description.
     * 
     * @param description
     *            TaskListItem description
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Returns TaskListItem order index.
     * 
     * @return TaskListItem order index
     */
    public String getItemIndex() {
	return itemIndex;
    }

    /**
     * Sets TaskListItem order index.
     * 
     * @param itemIndex
     *            TaskListItem order index
     */
    public void setItemIndex(String itemIndex) {
	this.itemIndex = itemIndex;
    }

    /**
     * Returns current SessionMapID.
     * 
     * @return current SessionMapID
     */
    public String getSessionMapID() {
	return sessionMapID;
    }

    /**
     * Sets current SessionMapID.
     * 
     * @param sessionMapID
     *            current SessionMapID
     */
    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    /**
     * Returns working mode.
     * 
     * @return working mode
     */
    public String getMode() {
	return mode;
    }

    /**
     * Returns working mode.
     * 
     * @param mode
     *            working mode
     */
    public void setMode(String mode) {
	this.mode = mode;
    }

    /**
     * Returns whether this <code>TaskLiskItem</code> is required to finish activity.
     * 
     * @return true if the <code>TaskLiskItem</code> is required to finish activity, false otherwise.
     */
    public boolean isRequired() {
	return isRequired;
    }

    /**
     * Sets whether this <code>TaskLiskItem</code> is required to finish activity.
     * 
     * @param isRequired
     *            true if the <code>TaskLiskItem</code> is required to finish activity, false otherwise.
     */
    public void setRequired(boolean isRequired) {
	this.isRequired = isRequired;
    }

    /**
     * Returns whether comments are allowed in this <code>TaskLiskItem</code>.
     * 
     * @return true if comments are allowed in this <code>TaskLiskItem</code>, false otherwise.
     */
    public boolean isCommentsAllowed() {
	return isCommentsAllowed;
    }

    /**
     * Sets whether comments are allowed in this <code>TaskLiskItem</code>.
     * 
     * @param isCommentsAllowed
     *            true if comments are allowed in this <code>TaskLiskItem</code>, false otherwise.
     */
    public void setCommentsAllowed(boolean isCommentsAllowed) {
	this.isCommentsAllowed = isCommentsAllowed;
    }

    /**
     * Returns whether comments are required to complete this <code>TaskLiskItem</code>.
     * 
     * @return true if comments are required to complete this <code>TaskLiskItem</code>, false otherwise.
     */
    public boolean isCommentsRequired() {
	return isCommentsRequired;
    }

    /**
     * Sets whether comments are required to complete this <code>TaskLiskItem</code>.
     * 
     * @param isCommentsAllowed
     *            true if comments are required to complete this <code>TaskLiskItem</code>, false otherwise.
     */
    public void setCommentsRequired(boolean isCommentsRequired) {
	this.isCommentsRequired = isCommentsRequired;
    }

    /**
     * Returns whether files are allowed in this <code>TaskLiskItem</code>.
     * 
     * @return true if files are allowed in this <code>TaskLiskItem</code>, false otherwise.
     */
    public boolean isFilesAllowed() {
	return isFilesAllowed;
    }

    /**
     * Sets whether files are allowed in this <code>TaskLiskItem</code>.
     * 
     * @param isCommentsAllowed
     *            true if files are allowed in this <code>TaskLiskItem</code>, false otherwise.
     */
    public void setFilesAllowed(boolean isFilesAllowed) {
	this.isFilesAllowed = isFilesAllowed;
    }

    /**
     * Returns whether files are required to complete this <code>TaskLiskItem</code>.
     * 
     * @return true if files are required to complete this <code>TaskLiskItem</code>, false otherwise.
     */
    public boolean isFilesRequired() {
	return isFilesRequired;
    }

    /**
     * Sets whether files are required to complete this <code>TaskLiskItem</code>.
     * 
     * @param isCommentsAllowed
     *            true if files are required to complete this <code>TaskLiskItem</code>, false otherwise.
     */
    public void setFilesRequired(boolean isFilesRequired) {
	this.isFilesRequired = isFilesRequired;
    }

    // The next 2 options always will be true (as it set in jsp). Waiting
    // for the final decision -- if this options will be needed later.
    /**
     * Returns whether comments are allowed in this <code>TaskLiskItem</code>.
     * 
     * @return true if comments are allowed in this <code>TaskLiskItem</code>, false otherwise.
     */
    public boolean isCommentsFilesAllowed() {
	return isCommentsFilesAllowed;
    }

    /**
     * Sets whether comments are allowed in this <code>TaskLiskItem</code>.
     * 
     * @param isCommentsAllowed
     *            true if comments are allowed in this <code>TaskLiskItem</code>, false otherwise.
     */
    public void setCommentsFilesAllowed(boolean isCommentsFilesAllowed) {
	this.isCommentsFilesAllowed = isCommentsFilesAllowed;
    }

    /**
     * Returns whether comments are allowed to be shown to everybody for this <code>TaskLiskItem</code>.
     * 
     * @return true if comments are allowed to be shown to everybody for this <code>TaskLiskItem</code>, false
     *         otherwise.
     */
    public boolean getShowCommentsToAll() {
	return showCommentsToAll;
    }

    /**
     * Sets whether comments are allowed to be shown to everybody for this <code>TaskLiskItem</code> or not.
     * 
     * @param showCommentsToAll
     *            true if comments are allowed to be shown to everybody for this <code>TaskLiskItem</code>, false
     *            otherwise.
     */
    public void setShowCommentsToAll(boolean showCommentsToAll) {
	this.showCommentsToAll = showCommentsToAll;
    }

    /**
     * Returns whether this <code>TaskLiskItem</code> is a child task.
     * 
     * @return true if this <code>TaskLiskItem</code> is a child task, false otherwise.
     */
    public boolean isChildTask() {
	return isChildTask;
    }

    /**
     * Sets whether this <code>TaskLiskItem</code> is a child task or not.
     * 
     * @param isChildTask
     *            true if this <code>TaskLiskItem</code> is a child task, false otherwise.
     */
    public void setChildTask(boolean isChildTask) {
	this.isChildTask = isChildTask;
    }

    /**
     * If the <code>TaskLiskItem</code> is a child task then it has a parent. So this method returns its title.
     * 
     * @return parent's title
     */
    public String getParentTaskName() {
	return parentTaskName;
    }

    /**
     * If the <code>TaskLiskItem</code> is a child task then it has a parent. So this method sets its title.
     * 
     * @param parentTaskName
     *            parent's title
     */
    public void setParentTaskName(String parentTaskName) {
	this.parentTaskName = parentTaskName;
    }

    /**
     * Returns attachment for this <code>TaskLiskItem</code>.
     * 
     * @return attachment for this <code>TaskLiskItem</code>
     */
    public FormFile getUploadedFile() {
	return uploadedFile;
    }

    /**
     * Sets attachment for this <code>TaskLiskItem</code>.
     * 
     * @param uploadedFile
     *            attachment for this <code>TaskLiskItem</code>
     */
    public void setUploadedFile(FormFile uploadedFile) {
	this.uploadedFile = uploadedFile;
    }

    /**
     * Returns comment for this <code>TaskLiskItem</code>.
     * 
     * @return comment for this <code>TaskLiskItem</code>
     */
    public String getComment() {
	return comment;
    }

    /**
     * Sets comment for this <code>TaskLiskItem</code>.
     * 
     * @param comment
     *            comment for this <code>TaskLiskItem</code>
     */
    public void setComment(String comment) {
	this.comment = comment;
    }
}
