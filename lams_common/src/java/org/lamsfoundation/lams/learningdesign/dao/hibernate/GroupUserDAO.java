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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.GroupUser;
import org.lamsfoundation.lams.learningdesign.dao.IGroupUserDAO;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.stereotype.Repository;

/**
 * @author Andrey Balan
 */
@Repository
public class GroupUserDAO extends LAMSBaseDAO implements IGroupUserDAO {

    private static final String GET_USERS_BY_LESSON_AND_TIME = "SELECT DISTINCT gu.user FROM "
	    + GroupUser.class.getName()
	    + " AS gu WHERE gu.group.grouping.groupingId=? AND (gu.scheduledLessonEndDate IS NOT NULL) AND (NOW() < gu.scheduledLessonEndDate) AND (? > gu.scheduledLessonEndDate)";

    @Override
    public GroupUser getGroupUser(Lesson lesson, Integer userId) {
	HashMap<String, Object> properties = new HashMap<String, Object>();
	properties.put("group.grouping.groupingId", lesson.getLessonClass().getGroupingId());
	properties.put("user.userId", userId);
	List<GroupUser> list = this.findByProperties(GroupUser.class, properties);
	if (list == null || list.size() == 0) {
	    return null;
	} else {
	    return list.get(0);
	}

    }

    /**
     * Returns users with deadline sooner than specified date
     *
     * @param lesson
     * @param timeToScheduledLessonEnd
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<User> getUsersWithLessonEndingSoonerThan(Lesson lesson, Date timeToScheduledLessonEnd) {
	return (List<User>) doFind(GET_USERS_BY_LESSON_AND_TIME,
		new Object[] { lesson.getLessonClass().getGroupingId(), timeToScheduledLessonEnd });
    }

    @Override
    public void saveGroupUser(GroupUser groupUser) {
	getSession().save(groupUser);
    }

}