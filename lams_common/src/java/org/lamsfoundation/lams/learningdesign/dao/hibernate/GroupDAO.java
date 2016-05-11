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

package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import org.hibernate.Query;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.dao.IGroupDAO;
import org.springframework.stereotype.Repository;

/**
 * @author Manpreet Minhas
 */
@Repository
public class GroupDAO extends LAMSBaseDAO implements IGroupDAO {

    private final static String NUM_GROUPS = "select count(*) from " + Group.class.getName() + " g join g.users u"
	    + " where u.userId = :userID";

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IGroupDAO#getGroupById(java.lang.Long)
     */
    @Override
    public Group getGroupById(Long groupID) {
	return (Group) super.find(Group.class, groupID);
    }

    @Override
    public void saveGroup(Group group) {
	getSession().save(group);
    }

    @Override
    public void deleteGroup(Group group) {
	getSession().delete(group);
    }

    /**
     * Number of users groups for a particular user. Used to determine
     * if we can delete a user.
     * 
     * @param userID
     */
    @Override
    public Integer getCountGroupsForUser(final Integer userID) {
	Query query = getSession().createQuery(NUM_GROUPS);
	query.setInteger("userID", userID);
	Object value = query.uniqueResult();
	return new Integer(((Number) value).intValue());
    }
}
