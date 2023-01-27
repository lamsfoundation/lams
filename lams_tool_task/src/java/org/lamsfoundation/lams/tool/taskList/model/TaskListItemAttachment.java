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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * A Wrapper class for uploaded files. An Attachment cannot exist independently and must belong to a
 * TaskListItem.
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_latask10_item_attachment")
public class TaskListItemAttachment implements Cloneable {
    private static final Logger log = Logger.getLogger(TaskListItemAttachment.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "file_uuid")
    private Long fileUuid;

    @Column(name = "file_version_id")
    private Long fileVersionId;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "create_date")
    private Date created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    private TaskListUser createBy;

    @Transient
    private String fileDisplayUuid;

    @Override
    public Object clone() {
	TaskListItemAttachment taskListItemAttachment = null;
	try {
	    taskListItemAttachment = (TaskListItemAttachment) super.clone();
	    taskListItemAttachment.setUid(null);

	    //clone ReourceUser as well
	    if (this.createBy != null) {
		taskListItemAttachment.setCreateBy((TaskListUser) this.createBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + TaskListItemAttachment.class + " failed");
	}

	return taskListItemAttachment;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof TaskListItemAttachment)) {
	    return false;
	}

	final TaskListItemAttachment genericEntity = (TaskListItemAttachment) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid)
		.append(this.fileVersionId, genericEntity.fileVersionId).append(this.fileName, genericEntity.fileName)
		.append(this.fileType, genericEntity.fileType).append(this.created, genericEntity.created)
		.append(this.createBy, genericEntity.createBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(fileVersionId).append(fileName).append(fileType).append(created)
		.append(createBy).toHashCode();
    }

    //  **********************************************************
    //		Get/Set methods
    //  **********************************************************

    /**
     *
     * @return Returns the log Uid.
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long version) {
	this.fileVersionId = version;
    }

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String type) {
	this.fileType = type;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	this.fileName = name;
    }

    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long uuid) {
	this.fileUuid = uuid;
    }

    public Date getCreated() {
	return created;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    public TaskListUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(TaskListUser createBy) {
	this.createBy = createBy;
    }

    public String getFileDisplayUuid() {
	return fileDisplayUuid;
    }

    public void setFileDisplayUuid(String fileDisplayUuid) {
	this.fileDisplayUuid = fileDisplayUuid;
    }

}