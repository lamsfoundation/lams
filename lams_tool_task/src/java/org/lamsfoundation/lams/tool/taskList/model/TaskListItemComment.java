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


package org.lamsfoundation.lams.tool.taskList.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * TaskList
 * 
 * @author Andrey Balan
 *
 *
 *
 */
public class TaskListItemComment implements Cloneable {

    private static final Logger log = Logger.getLogger(TaskListItemComment.class);

    private Long uid;
    private String comment;
    private TaskListUser createBy;
    private Date createDate;

    //  **********************************************************
    //				Function method for TaskListItemComment
    //  **********************************************************

    @Override
    public Object clone() {
	TaskListItemComment taskListItemComment = null;
	try {
	    taskListItemComment = (TaskListItemComment) super.clone();
	    taskListItemComment.setUid(null);

	    //clone ReourceUser as well
	    if (this.createBy != null) {
		taskListItemComment.setCreateBy((TaskListUser) this.createBy.clone());
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + TaskListItemComment.class + " failed");
	}

	return taskListItemComment;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof TaskListItemComment)) {
	    return false;
	}

	final TaskListItemComment genericEntity = (TaskListItemComment) o;

	return new EqualsBuilder().append(this.uid, genericEntity.uid).append(this.comment, genericEntity.comment)
		.append(this.createBy, genericEntity.createBy).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(comment).append(createBy).toHashCode();
    }

    //  **********************************************************
    //					Get/Set methods
    //  **********************************************************

    /**
     *
     * @return
     */
    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

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
     * @return
     */
    public TaskListUser getCreateBy() {
	return createBy;
    }

    public void setCreateBy(TaskListUser createBy) {
	this.createBy = createBy;
    }

    /**
     *
     *
     * @return
     */
    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }
}
