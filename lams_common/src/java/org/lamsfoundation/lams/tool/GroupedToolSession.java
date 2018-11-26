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

package org.lamsfoundation.lams.tool;

import java.util.Date;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * This is the tool session shared within a learner group. It is meant to be unique against learner group and a tool
 * activity instance.
 *
 * @author daveg, Jacky Fang
 *
 */
@Entity
@DiscriminatorValue("1")
public class GroupedToolSession extends ToolSession {
    private static final long serialVersionUID = 8638128083435243375L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group sessionGroup;

    /** default constructor */
    public GroupedToolSession() {
    }

    /**
     * Grouped tool session initialization constructor.
     *
     * @param toolActivity
     *            the tool activity for that group
     * @param createDateTime
     *            the time this tool session is created.
     * @param toolSessionStateId
     *            the tool session status.
     * @param group
     *            the target group
     */
    public GroupedToolSession(ToolActivity toolActivity, Date createDateTime, int toolSessionStateId,
	    Group sessionGroup, Lesson lesson) {
	super(null, toolActivity, createDateTime, toolSessionStateId, lesson);
	super.setUniqueKey(ToolSession.UNIQUE_KEY_PREFIX + "_" + toolActivity.getActivityId().toString() + "_"
		+ sessionGroup.getGroupId().toString());
	this.sessionGroup = sessionGroup;
	//set toolSession name as same as name of relatived group.
	this.setToolSessionName(sessionGroup.getGroupName());
    }

    public Group getSessionGroup() {
	return sessionGroup;
    }

    public void setSessionGroup(Group sessionGroup) {
	this.sessionGroup = sessionGroup;
    }

    /** Get all the learners who may be part of this tool session. */
    @Override
    public Set<User> getLearners() {
	// the collection is extra lazy and possibly very large so avoid initialisation if possible
	return sessionGroup.getUsers();
    }
}