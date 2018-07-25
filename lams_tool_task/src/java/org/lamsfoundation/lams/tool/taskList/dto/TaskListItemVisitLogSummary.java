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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment;
import org.lamsfoundation.lams.tool.taskList.model.TaskListUser;

/**
 * DTO object which is used in a task summary page in monitoring. Contains partial information for
 * <code>TaskSummary</code> object specific for current user and <code>TaskLIstItem</code> object.
 *
 * @author Andrey Balan
 */
public class TaskListItemVisitLogSummary {

    private TaskListUser user;
    private boolean completed;
    private Date date;

    private List<TaskListItemComment> comments;
    private List<TaskListItemAttachment> attachments;

    public TaskListItemVisitLogSummary() {
	comments = new ArrayList<TaskListItemComment>();
	attachments = new ArrayList<TaskListItemAttachment>();
    }

    //  **********************************************************
    //		Get/Set methods
    //  **********************************************************

    /**
     * Returns user whom created this TaskListItem.
     *
     * @return user whom created this TaskListItem
     */
    public TaskListUser getUser() {
	return user;
    }

    /**
     * Returns user whom created this TaskListItem.
     *
     * @param user
     *            user whom created this TaskListItem
     */
    public void setUser(TaskListUser user) {
	this.user = user;
    }

    /**
     * Returns whether this TaskListItem was completed.
     *
     * @return boolean showing whether this TaskListItem was completed or not
     */
    public boolean getCompleted() {
	return completed;
    }

    /**
     * Sets whether this TaskListItem was completed.
     *
     * @param completed
     *            true if this TaskListItem was completed, false otherwise
     */
    public void setCompleted(boolean completed) {
	this.completed = completed;
    }

    /**
     * Returns the date of the completion.
     *
     * @return the date of the completion
     */
    public Date getDate() {
	return date;
    }

    /**
     * Sets the date of the completion.
     *
     * @param date
     *            the date of the completion
     */
    public void setDate(Date date) {
	this.date = date;
    }

    /**
     * Returns list of the comments done by this user for this particular TaskListItem.
     *
     * @return list of the comments done by this user for this particular TaskListItem
     */
    public List<TaskListItemComment> getComments() {
	return comments;
    }

    /**
     * Sets list of the comments done by this user for this particular TaskListItem.
     *
     * @param comments
     *            list of the comments done by this user for this particular TaskListItem
     */
    public void setComments(List<TaskListItemComment> comments) {
	this.comments = comments;
    }

    /**
     * Returns list of the attachments uploaded by this user for this particular TaskListItem.
     *
     * @return list of the attachments uploaded by this user for this particular TaskListItem
     */
    public List<TaskListItemAttachment> getAttachments() {
	return attachments;
    }

    /**
     * Sets list of the attachments uploaded by this user for this particular TaskListItem.
     *
     * @param attachments
     *            list of the attachments uploaded by this user for this particular TaskListItem.
     */
    public void setAttachments(List<TaskListItemAttachment> attachments) {
	this.attachments = attachments;
    }
}