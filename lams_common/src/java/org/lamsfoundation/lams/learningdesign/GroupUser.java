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
 * @hibernate.class table="lams_user_group"
 */
public class GroupUser implements Serializable {

    private static final long serialVersionUID = 4680781848791310422L;

    private Group group;
    private User user;
    private Date scheduledLessonEndDate;

    public GroupUser() {
    }

    /**
     * @hibernate.many-to-one not-null="true" lazy="false"
     * @hibernate.column name="group_id"
     *
     */
    public Group getGroup() {
	return group;
    }

    public void setGroup(Group group) {
	this.group = group;
    }

    /**
     * @hibernate.many-to-one not-null="true" lazy="false"
     * @hibernate.column name="user_id"
     *
     */
    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    /**
     * @hibernate.property type="java.sql.Timestamp" column="scheduled_lesson_end_date" length="19"
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
}
