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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * Not used at present - creates a separate ToolSession for each learner. When we have a user interface that allows the
 * author to select the whole of the class vs an individual learner for the tool session, then it will be used.
 *
 * @author daveg
 */
@Entity
@DiscriminatorValue("2")
public class NonGroupedToolSession extends ToolSession {
    private static final long serialVersionUID = 8063910455683242612L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public NonGroupedToolSession(ToolActivity toolActivity, Date createDateTime, int toolSessionStateId, User user,
	    Lesson lesson) {
	super(null, toolActivity, createDateTime, toolSessionStateId, lesson);
	super.setUniqueKey(ToolSession.UNIQUE_KEY_PREFIX + "_" + toolActivity.getActivityId().toString() + "_"
		+ user.getUserId().toString());
	this.user = user;
	//set toolSession name as same as login name of relatived user.
	this.setToolSessionName(user.getLogin());

    }

    /** default constructor */
    public NonGroupedToolSession() {
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    /** Get all the learners who may be part of this tool session. */
    @Override
    public Set<User> getLearners() {
	HashSet<User> users = new HashSet<User>();
	if (user != null) {
	    users.add(user);
	}
	return users;
    }
}