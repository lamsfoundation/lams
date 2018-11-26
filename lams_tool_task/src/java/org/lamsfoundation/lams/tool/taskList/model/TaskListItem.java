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

package org.lamsfoundation.lams.tool.taskList.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * TaskList
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_latask10_tasklist_item")
public class TaskListItem implements Cloneable {
    private static final Logger log = Logger.getLogger(TaskListItem.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "sequence_id")
    private int sequenceId;

    @Column(name = "init_item")
    private String initialItem;

    @Column(name = "organization_xml")
    private String organizationXml;

    @Column(name = "create_by_author")
    private boolean isCreateByAuthor;

    @Column(name = "create_date")
    private Date createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private TaskListUser createBy;

    //************ advanced tab ***************
    @Column(name = "is_required")
    private boolean isRequired;

    @Column(name = "is_comments_allowed")
    private boolean isCommentsAllowed;

    @Column(name = "is_comments_required")
    private boolean isCommentsRequired;

    @Column(name = "is_files_allowed")
    private boolean isFilesAllowed;

    @Column(name = "is_files_required")
    private boolean isFilesRequired;

    @Column(name = "is_child_task")
    private boolean isChildTask;

    @Column(name = "parent_task_name")
    private String parentTaskName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "taskList_item_uid")
    @OrderBy("create_date ASC")
    private Set<TaskListItemAttachment> attachments = new HashSet<TaskListItemAttachment>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "taskList_item_uid")
    @OrderBy("create_date ASC")
    private Set<TaskListItemComment> comments = new HashSet<TaskListItemComment>();

    // *************** NON Persist Fields ********************
    @Transient
    private boolean complete;

    @Override
    public Object clone() {

	TaskListItem taskListItem = null;
	try {
	    taskListItem = (TaskListItem) super.clone();
	    taskListItem.setUid(null);

	    // clone set of taskListItemsAttachment
	    if (attachments != null) {
		Iterator<TaskListItemAttachment> iter = attachments.iterator();
		Set<TaskListItemAttachment> set = new HashSet<TaskListItemAttachment>();
		while (iter.hasNext()) {
		    TaskListItemAttachment file = iter.next();
		    TaskListItemAttachment newFile = (TaskListItemAttachment) file.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newFile);
		}
		taskListItem.attachments = set;
	    }

	    // clone set of taskListItemsComment
	    if (comments != null) {
		Iterator<TaskListItemComment> iter = comments.iterator();
		Set<TaskListItemComment> set = new HashSet<TaskListItemComment>();
		while (iter.hasNext()) {
		    TaskListItemComment comment = iter.next();
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
     *
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

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getInitialItem() {
	return initialItem;
    }

    public void setInitialItem(String initialItem) {
	this.initialItem = initialItem;
    }

    public String getOrganizationXml() {
	return organizationXml;
    }

    public void setOrganizationXml(String organizationXml) {
	this.organizationXml = organizationXml;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public TaskListUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(TaskListUser createBy) {
	this.createBy = createBy;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

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

    public boolean isCommentsRequired() {
	return isCommentsRequired;
    }

    public void setCommentsRequired(boolean isCommentsRequired) {
	this.isCommentsRequired = isCommentsRequired;
    }

    public boolean isFilesAllowed() {
	return isFilesAllowed;
    }

    public void setFilesAllowed(boolean isFilesAllowed) {
	this.isFilesAllowed = isFilesAllowed;
    }

    public boolean isFilesRequired() {
	return isFilesRequired;
    }

    public void setFilesRequired(boolean isFilesRequired) {
	this.isFilesRequired = isFilesRequired;
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

    /**
     * @return a set of Attachments to this TaskListItem.
     */
    public Set<TaskListItemAttachment> getAttachments() {
	return attachments;
    }

    public void setAttachments(Set<TaskListItemAttachment> attachments) {
	this.attachments = attachments;
    }

    /**
     * @return a set of Comments to this TaskListItem.
     */
    public Set<TaskListItemComment> getComments() {
	return comments;
    }

    public void setComments(Set<TaskListItemComment> comments) {
	this.comments = comments;
    }
}
