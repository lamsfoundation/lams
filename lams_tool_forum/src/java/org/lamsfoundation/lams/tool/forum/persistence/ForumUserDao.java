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

package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class ForumUserDao extends HibernateDaoSupport {

    private static final String SQL_QUERY_FIND_BY_USER_ID_SESSION_ID = "from " + ForumUser.class.getName() + " as f"
	    + " where user_id=? and f.session.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_USER_ID = "from " + ForumUser.class.getName() + " as f"
	    + " where user_id=? and session_id is null";

    private static final String SQL_QUERY_FIND_BY_SESSION_ID = "from " + ForumUser.class.getName() + " as f "
	    + " where f.session.sessionId=?";

    private static final String SQL_QUERY_FIND_BY_SESSION_AND_QUESTION_LIMIT = "from user in class ForumUser "
	    + "where user.session.sessionId=:sessionId order by ";
    
    private static final String GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION = "SELECT COUNT(*) from "
	    + ForumUser.class.getName()
	    + " as user where user.session.sessionId=?";

    public List<ForumUser> getBySessionId(Long sessionID) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_SESSION_ID, sessionID);
    }

    public void save(ForumUser forumUser) {
	this.getHibernateTemplate().save(forumUser);
    }

    public ForumUser getByUserIdAndSessionId(Long userId, Long sessionId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID_SESSION_ID,
		new Object[] { userId, sessionId });

	if (list == null || list.isEmpty())
	    return null;

	return (ForumUser) list.get(0);
    }

    public ForumUser getByUserId(Long userId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_USER_ID, userId);

	if (list == null || list.isEmpty())
	    return null;

	return (ForumUser) list.get(0);
    }

    public ForumUser getByUid(Long userUid) {
	return (ForumUser) this.getHibernateTemplate().get(ForumUser.class, userUid);
    }

    public List<ForumUser> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting) {
	String sortingOrder = "";
	switch (sorting) {
	case ForumConstants.SORT_BY_NO:
	    sortingOrder = "user.lastName";
	    break;
	case ForumConstants.SORT_BY_USER_NAME_ASC:
	    sortingOrder = "user.lastName ASC";
	    break;
	case ForumConstants.SORT_BY_USER_NAME_DESC:
	    sortingOrder = "user.lastName DESC";
	    break;
	}

	return getSession().createQuery(SQL_QUERY_FIND_BY_SESSION_AND_QUESTION_LIMIT + sortingOrder)
		.setLong("sessionId", sessionId.longValue()).setFirstResult(page * size).setMaxResults(size).list();
    }
    
    public int getCountUsersBySession(final Long sessionId) {
	List list = getHibernateTemplate().find(GET_COUNT_RESPONSES_FOR_SESSION_AND_QUESTION,
		new Object[] { sessionId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    public void delete(ForumUser user) {
	this.getHibernateTemplate().delete(user);
    }

}
