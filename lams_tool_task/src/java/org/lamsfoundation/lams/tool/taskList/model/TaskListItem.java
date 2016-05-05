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
package org.lamsfoundation.lams.tool.taskList.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * TaskList
 *
 * @author Andrey Balan
 *
 * @hibernate.class table="tl_latask10_tasklist_item"
 *
 */
public class TaskListItem implements Cloneable {

    private static final Logger log = Logger.getLogger(TaskListItem.class);

    // key
    private Long uid;

    private String title;
    private String description;
    private int sequenceId;

    private String initialItem;
    private String organizationXml;
    private boolean isCreateByAuthor;

    // general infomation
    private Date createDate;
    private TaskListUser createBy;
    // advanced
    private boolean isRequired;
    private boolean isCommentsAllowed;
    private boolean isCommentsRequired;
    private boolean isFilesAllowed;
    private boolean isFilesRequired;
    private boolean isChildTask;
    private String parentTaskName;
    // advanced options that are not used now
    private boolean isCommentsFilesAllowed;
    private boolean showCommentsToAll;

    // Set of uploaded files
    private Set attachments;
    // Set of user comments
    private Set comments;

    // *************** NON Persist Fields ********************
    private boolean complete;

    /**
     * Default contruction method.
     *
     */
    public TaskListItem() {
	attachments = new HashSet();
	comments = new HashSet();
    }

    // **********************************************************
    // Function method for TaskList
    // **********************************************************
    @Override
    public Object clone() {

	TaskListItem taskListItem = null;
	try {
	    taskListItem = (TaskListItem) super.clone();
	    taskListItem.setUid(null);

	    // clone set of taskListItemsAttachment
	    if (attachments != null) {
		Iterator iter = attachments.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    TaskListItemAttachment file = (TaskListItemAttachment) iter.next();
		    TaskListItemAttachment newFile = (TaskListItemAttachment) file.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newFile);
		}
		taskListItem.attachments = set;
	    }

	    // clone set of taskListItemsComment
	    if (comments != null) {
		Iterator iter = comments.iterator();
		Set set = new HashSet();
		while (iter.hasNext()) {
		    TaskListItemComment comment = (TaskListItemComment) iter.next();
		    TaskListItemComment newComment = (TaskListItemComment) comment.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newComment);
		}
		taskListItem.comments = set;
	    }

	    // clone ReourceUser as well
	    if (this.createBy != null) {
		taskListItem.setCreateBy((TaskListUser) this.createBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + TaskListItem.class + " failed");
	}

	return taskListItem;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof TaskListItem)) {
	    return false;
	}

	final TaskListItem genericEntity = (TaskListItem) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid).append(this.title, genericEntity.title)
		.append(this.description, genericEntity.description).append(this.createDate, genericEntity.createDate)
		.append(this.createBy, genericEntity.createBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(description).append(createDate).append(createBy)
		.toHashCode();
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     * @hibernate.property column="description" type="text"
     * @return
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * @hibernate.property column="init_item"
     * @return
     */
    public String getInitialItem() {
	return initialItem;
    }

    public void setInitialItem(String initialItem) {
	this.initialItem = initialItem;
    }

    /**
     * @hibernate.property column="organization_xml" length="65535"
     * @return
     */
    public String getOrganizationXml() {
	return organizationXml;
    }

    public void setOrganizationXml(String organizationXml) {
	this.organizationXml = organizationXml;
    }

    /**
     * @hibernate.property column="title" length="255"
     * @return
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.many-to-one cascade="none" column="create_by"
     *
     * @return
     */
    public TaskListUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(TaskListUser createBy) {
	this.createBy = createBy;
    }

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
     * @hibernate.property column="create_by_author"
     * @return
     */
    public boolean isCreateByAuthor() {
	return isCreateByAuthor;
    }

    public void setCreateByAuthor(boolean isCreateByAuthor) {
	this.isCreateByAuthor = isCreateByAuthor;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    public boolean isComplete() {
	return complete;
    }

    /**
     * Returns taskListItem sequence number.
     *
     * @return taskListItem sequence number
     *
     * @hibernate.property column="sequence_id"
     */
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets taskListItem sequence number.
     *
     * @param sequenceId
     *            taskListItem sequence number
     */
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     * @hibernate.property column="is_required"
     * @return
     */
    public boolean isRequired() {
	return isRequired;
    }

    public void setRequired(boolean isRequired) {
	this.isRequired = isRequired;
    }

    /**
     * @hibernate.property column="is_comments_allowed"
     * @return
     */
    public boolean isCommentsAllowed() {
	return isCommentsAllowed;
    }

    public void setCommentsAllowed(boolean isCommentsAllowed) {
	this.isCommentsAllowed = isCommentsAllowed;
    }

    /**
     * @hibernate.property column="is_comments_required"
     * @return
     */
    public boolean isCommentsRequired() {
	return isCommentsRequired;
    }

    public void setCommentsRequired(boolean isCommentsRequired) {
	this.isCommentsRequired = isCommentsRequired;
    }

    /**
     * @hibernate.property column="is_files_allowed"
     * @return
     */
    public boolean isFilesAllowed() {
	return isFilesAllowed;
    }

    public void setFilesAllowed(boolean isFilesAllowed) {
	this.isFilesAllowed = isFilesAllowed;
    }

    /**
     * @hibernate.property column="is_files_required"
     * @return
     */
    public boolean isFilesRequired() {
	return isFilesRequired;
    }

    public void setFilesRequired(boolean isFilesRequired) {
	this.isFilesRequired = isFilesRequired;
    }

    /**
     * @hibernate.property column="is_child_task"
     * @return
     */
    public boolean isChildTask() {
	return isChildTask;
    }

    public void setChildTask(boolean isChildTask) {
	this.isChildTask = isChildTask;
    }

    /**
     * @hibernate.property column="parent_task_name"
     * @return
     */
    public String getParentTaskName() {
	return parentTaskName;
    }

    public void setParentTaskName(String parentTaskName) {
	this.parentTaskName = parentTaskName;
    }

    /**
     * @hibernate.property column="show_comments_to_all"
     * @return
     */
    public boolean getShowCommentsToAll() {
	return showCommentsToAll;
    }

    public void setShowCommentsToAll(boolean showCommentsToAll) {
	this.showCommentsToAll = showCommentsToAll;
    }

    /**
     * @hibernate.property column="is_comments_files_allowed"
     * @return
     */
    public boolean isCommentsFilesAllowed() {
	// true bacause we can not set it from the UI
	// TODO get rid of this later
	return true;
    }

    public void setCommentsFilesAllowed(boolean isCommentsFilesAllowed) {
	this.isCommentsFilesAllowed = isCommentsFilesAllowed;
    }

    /**
     * @hibernate.set lazy="true" cascade="all" inverse="false" order-by="create_date asc"
     * @hibernate.collection-key column="taskList_item_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.taskList.model.TaskListItemAttachment"
     *
     * @return a set of Attachments to this TaskListItem.
     */
    public Set getAttachments() {
	return attachments;
    }

    public void setAttachments(Set attachments) {
	this.attachments = attachments;
    }

    /**
     * @hibernate.set lazy="true" cascade="all" inverse="false" order-by="create_date asc"
     * @hibernate.collection-key column="taskList_item_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.taskList.model.TaskListItemComment"
     *
     * @return a set of Comments to this TaskListItem.
     */
    public Set getComments() {
	return comments;
    }

    public void setComments(Set comments) {
	this.comments = comments;
    }
}
