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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * @author Andrey Balan
 *
 *         A Wrapper class for uploaded files. An Attachment cannot exist independently and must belong to a
 *         TaskListItem.
 *
 *
 *
 *
 */
public class TaskListItemAttachment implements Cloneable {
    private static final Logger log = Logger.getLogger(TaskListItemAttachment.class);

    private Long uid;
    private Long fileUuid;
    private Long fileVersionId;
    private String fileType;
    private String fileName;
    private Date created;
    private TaskListUser createBy;

    //  **********************************************************
    //		Function method for TaskListItemAttachment
    //  **********************************************************
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
	    TaskListItemAttachment.log.error("When clone " + TaskListItemAttachment.class + " failed");
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

    /**
     *
     *
     */
    public Long getFileVersionId() {
	return fileVersionId;
    }

    public void setFileVersionId(Long version) {
	this.fileVersionId = version;
    }

    /**
     *
     */
    public String getFileType() {
	return fileType;
    }

    public void setFileType(String type) {
	this.fileType = type;
    }

    /**
     *
     */
    public String getFileName() {
	return fileName;
    }

    public void setFileName(String name) {
	this.fileName = name;
    }

    /**
     *
     * @return
     */
    public Long getFileUuid() {
	return fileUuid;
    }

    public void setFileUuid(Long uuid) {
	this.fileUuid = uuid;
    }

    /**
     *
     * @return
     */
    public Date getCreated() {
	return created;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     *
     * @return
     */
    public TaskListUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(TaskListUser createBy) {
	this.createBy = createBy;
    }
}