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
/* $$Id$$ */
package org.lamsfoundation.lams.tool;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

public abstract class ToolSession implements Serializable {

    /** Tool session type id for grouped */
    public static final int GROUPED_TYPE = 1;
    /** Tool session type id for non-grouped - all learners in a separate session */
    public static final int NON_GROUPED_TYPE = 2;

    /** Tool session state id for started tool session */
    public static final int STARTED_STATE = 1;
    /** Tool session state id for completed tool session */
    public static final int ENDED_STATE = 2;

    public static final String UNIQUE_KEY_PREFIX = "uq";
    /** identifier field */
    private Long toolSessionId;

    /** Persistent field * */
    private String toolSessionName;

    /** persistent field */
    private ToolActivity toolActivity;

    /** persistent field */
    private Date createDateTime;

    /** persistent field */
    private int toolSessionStateId;

    private String uniqueKey;

    private Lesson lesson;

    /** Get all the learners who may be part of this tool session. */
    public abstract Set<User> getLearners();

    /** full constructor */
    public ToolSession(Long toolSessionId, ToolActivity toolActivity, Date createDateTime, int toolSessionStateId,
	    Lesson lesson) {
	this.toolSessionId = toolSessionId;
	this.toolActivity = toolActivity;
	this.createDateTime = createDateTime;
	this.toolSessionStateId = toolSessionStateId;
	this.lesson = lesson;
    }

    /** default constructor */
    public ToolSession() {
    }

    public Long getToolSessionId() {
	return this.toolSessionId;
    }

    public void setToolSessionId(Long toolSessionId) {
	this.toolSessionId = toolSessionId;
    }

    public ToolActivity getToolActivity() {
	return this.toolActivity;
    }

    public void setToolActivity(ToolActivity toolActivity) {
	this.toolActivity = toolActivity;
    }

    public Date getCreateDateTime() {
	return this.createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
	this.createDateTime = createDateTime;
    }

    /**
     * @return Returns the uniqueKey.
     */
    public String getUniqueKey() {
	return uniqueKey;
    }

    /**
     * @param uniqueKey
     *            The uniqueKey to set.
     */
    public void setUniqueKey(String uniqueKey) {
	this.uniqueKey = uniqueKey;
    }

    public String getToolSessionName() {
	return toolSessionName;
    }

    public void setToolSessionName(String toolSessionName) {
	this.toolSessionName = toolSessionName;
    }

    public int getToolSessionStateId() {
	return this.toolSessionStateId;
    }

    public void setToolSessionStateId(int toolSessionStateId) {
	this.toolSessionStateId = toolSessionStateId;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("toolSessionId", getToolSessionId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof ToolSession)) {
	    return false;
	}
	ToolSession castOther = (ToolSession) other;
	return new EqualsBuilder().append(this.getToolSessionId(), castOther.getToolSessionId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getToolSessionId()).toHashCode();
    }

    public int getToolSessionTypeId() {
	if (this instanceof NonGroupedToolSession) {
	    return NON_GROUPED_TYPE;
	} else {
	    return GROUPED_TYPE;
	}
    }

    public Lesson getLesson() {
	return lesson;
    }

    public void setLesson(Lesson lesson) {
	this.lesson = lesson;
    }
}
