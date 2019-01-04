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


package org.lamsfoundation.lams.tool.taskList.dto;

import java.util.HashSet;
import java.util.Set;

import org.lamsfoundation.lams.tool.taskList.model.TaskListItem;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;

/**
 * DTO object which contains data for displaying learning stuff.
 *
 * @author Andrey Balan
 */
public class TasListItemDTO {

    private TaskListItem taskListItem;

    private boolean isCommentRequirementsMet;
    private boolean isAttachmentRequirementsMet;
    private boolean isAllowedByParent;
    private boolean isPreviousTaskCompleted;
    //shows if this TaskListItem shoud be displayed open or close
    private boolean isDisplayedOpen;

    //Set of comments posted by the members of the group to which user belong to
    private Set<TaskListItemComment> comments;
    //Set of files uploaded by the members of the group to which user belong to
    private Set<TaskListItemAttachment> attachments;

    public TasListItemDTO(TaskListItem taskListItem) {
	this.taskListItem = taskListItem;
	this.comments = new HashSet<>();
	this.attachments = new HashSet<>();
    }

    //  **********************************************************
    //		Get/Set methods
    //  **********************************************************

    /**
     * Returns taskListItem.
     * 
     * @return taskListItem
     */
    public TaskListItem getTaskListItem() {
	return taskListItem;
    }

    /**
     * Returns taskListItem.
     * 
     * @param taskListItem
     */
    public void setTaskListItem(TaskListItem taskListItem) {
	this.taskListItem = taskListItem;
    }

    /**
     * Returns whether this TaskListItem meets comment requirements.
     * 
     * @return boolean showing whether this TaskListItem meets comment requirements or not
     */
    public boolean isCommentRequirementsMet() {
	return isCommentRequirementsMet;
    }

    /**
     * Sets whether this TaskListItem meets comment requirements.
     * 
     * @param completed
     *            true if this TaskListItem meets comment requirements, false otherwise
     */
    public void setCommentRequirementsMet(boolean isCommentRequirementsMet) {
	this.isCommentRequirementsMet = isCommentRequirementsMet;
    }

    /**
     * Returns whether this TaskListItem meets attachment requirements.
     * 
     * @return boolean showing whether this TaskListItem meets attachment requirements or not
     */
    public boolean isAttachmentRequirementsMet() {
	return isAttachmentRequirementsMet;
    }

    /**
     * Sets whether this TaskListItem meets attachment requirements.
     * 
     * @param completed
     *            true if this TaskListItem meets attachment requirements, false otherwise
     */
    public void setAttachmentRequirementsMet(boolean isAttachmentRequirementsMet) {
	this.isAttachmentRequirementsMet = isAttachmentRequirementsMet;
    }

    /**
     * Returns whether this TaskListItem is allowed by parent.
     * 
     * @return boolean showing whether this TaskListItem is allowed by parent or not
     */
    public boolean isAllowedByParent() {
	return isAllowedByParent;
    }

    /**
     * Sets whether this TaskListItem is allowed by parent.
     * 
     * @param completed
     *            true if this TaskListItem is allowed by parent, false otherwise
     */
    public void setAllowedByParent(boolean isAllowedByParent) {
	this.isAllowedByParent = isAllowedByParent;
    }

    /**
     * Returns whether the previous TaskListItem was completed.
     * 
     * @return boolean showing whether the previous TaskListItem was completed or not
     */
    public boolean isPreviousTaskCompleted() {
	return isPreviousTaskCompleted;
    }

    /**
     * Sets whether the previous TaskListItem was completed.
     * 
     * @param completed
     *            true if the previous TaskListItem was completed, false otherwise
     */
    public void setPreviousTaskCompleted(boolean isPreviousTaskCompleted) {
	this.isPreviousTaskCompleted = isPreviousTaskCompleted;
    }

    /**
     * Returns whether this TaskListItem shoud be displayed open or close.
     * 
     * @return boolean showing whether this TaskListItem shoud be displayed open or close
     */
    public boolean isDisplayedOpen() {
	return isDisplayedOpen;
    }

    /**
     * Sets whether this TaskListItem shoud be displayed open or close.
     * 
     * @param completed
     *            true if this TaskListItem shoud be displayed open, false otherwise
     */
    public void setDisplayedOpen(boolean isDisplayedOpen) {
	this.isDisplayedOpen = isDisplayedOpen;
    }

    /**
     * Returns set of comments posted by the members of the group to which user belong to.
     * 
     * @return set of comments posted by the members of the group to which user belong to
     */
    public Set<TaskListItemComment> getComments() {
	return comments;
    }

    /**
     * Sets set of comments posted by the members of the group to which user belong to.
     * 
     * @param comments
     *            set of comments posted by the members of the group to which user belong to
     */
    public void setComments(Set<TaskListItemComment> comments) {
	this.comments = comments;
    }

    /**
     * Returns set of files uploaded by the members of the group to which user belong to.
     * 
     * @return set of files uploaded by the members of the group to which user belong to
     */
    public Set<TaskListItemAttachment> getAttachments() {
	return attachments;
    }

    /**
     * Sets set of files uploaded by the members of the group to which user belong to.
     * 
     * @param attachments
     *            set of files uploaded by the members of the group to which user belong to
     */
    public void setAttachments(Set<TaskListItemAttachment> attachments) {
	this.attachments = attachments;
    }

}
