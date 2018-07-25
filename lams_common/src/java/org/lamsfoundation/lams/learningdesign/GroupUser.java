/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.learningdesign;

import java.io.Serializable;
import java.util.Date;

import org.lamsfoundation.lams.usermanagement.User;

/**
 * A persistence bean for group/user mappings
 *
 * @author lfoxton
 *
 *
 */
public class GroupUser implements Serializable {

    private static final long serialVersionUID = 4680781848791310422L;

    private Group group;
    private User user;
    private Date scheduledLessonEndDate;

    public GroupUser() {
    }

    /**
     *
     *
     *
     */
    public Group getGroup() {
	return group;
    }

    public void setGroup(Group group) {
	this.group = group;
    }

    /**
     *
     *
     *
     */
    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    /**
     *
     *
     * @return Returns the scheduledLessonEndDate.
     */
    public Date getScheduledLessonEndDate() {
	return scheduledLessonEndDate;
    }

    /**
     * @param scheduledLessonEndDate
     *            The scheduledLessonEndDate to set.
     */
    public void setScheduledLessonEndDate(Date scheduledLessonEndDate) {
	this.scheduledLessonEndDate = scheduledLessonEndDate;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((group == null) ? 0 : group.hashCode());
	result = prime * result + ((scheduledLessonEndDate == null) ? 0 : scheduledLessonEndDate.hashCode());
	result = prime * result + ((user == null) ? 0 : user.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	GroupUser other = (GroupUser) obj;
	if (group == null) {
	    if (other.group != null) {
		return false;
	    }
	} else if (!group.equals(other.group)) {
	    return false;
	}
	if (scheduledLessonEndDate == null) {
	    if (other.scheduledLessonEndDate != null) {
		return false;
	    }
	} else if (!scheduledLessonEndDate.equals(other.scheduledLessonEndDate)) {
	    return false;
	}
	if (user == null) {
	    if (other.user != null) {
		return false;
	    }
	} else if (!user.equals(other.user)) {
	    return false;
	}
	return true;
    }

}
