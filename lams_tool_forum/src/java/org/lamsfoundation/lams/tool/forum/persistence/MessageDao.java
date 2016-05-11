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



package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author conradb
 */
public class MessageDao extends HibernateDaoSupport {
    private static final String SQL_QUERY_FIND_ROOT_TOPICS = "from " + Message.class.getName() + " m "
	    + " where parent_uid is null and m.toolSession.sessionId=?";

    private static final String SQL_QUERY_FIND_TOPICS_FROM_AUTHOR = "from " + Message.class.getName()
	    + " where is_authored = true and forum_uid=? order by create_date";

    private static final String SQL_QUERY_FIND_CHILDREN = "from " + Message.class.getName() + " m where m.parent.uid=?";

    private static final String SQL_QUERY_BY_USER_SESSION = "from " + Message.class.getName() + " m "
	    + " where m.createdBy.uid = ? and  m.toolSession.sessionId=?";

    private static final String SQL_QUERY_BY_SESSION = "from " + Message.class.getName() + " m "
	    + " where m.toolSession.sessionId=?";

    private static final String SQL_QUERY_TOPICS_NUMBER_BY_USER_SESSION = "select count(*) from "
	    + Message.class.getName() + " m "
	    + " where m.createdBy.userId=? and m.toolSession.sessionId=? and m.isAuthored = false";

    public void saveOrUpdate(Message message) {
	this.getHibernateTemplate().saveOrUpdate(message);
    }

    public void update(Message message) {
	this.getHibernateTemplate().saveOrUpdate(message);
    }

    public Message getById(Long messageId) {
	return (Message) getHibernateTemplate().get(Message.class, messageId);
    }

    /**
     * Get all root (first level) topics in a special Session.
     *
     * @param sessionId
     * @return
     */
    public List<Message> getRootTopics(Long sessionId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_ROOT_TOPICS, sessionId);
    }

    /**
     * Get all message posted by author role in a special forum.
     *
     * @param forumUid
     * @return
     */
    public List<Message> getTopicsFromAuthor(Long forumUid) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_TOPICS_FROM_AUTHOR, forumUid);
    }

    public void delete(Long uid) {
	Message msg = getById(uid);
	if (msg != null) {
	    this.getHibernateTemplate().delete(msg);
	}
    }

    /**
     * Get all children message from the given parent topic ID.
     *
     * @param parentId
     * @return
     */
    public List<Message> getChildrenTopics(Long parentId) {
	return this.getHibernateTemplate().find(SQL_QUERY_FIND_CHILDREN, parentId);
    }

    /**
     * Get all messages according to special user and session.
     *
     * @param userUid
     * @param sessionId
     * @return
     */
    public List<Message> getByUserAndSession(Long userUid, Long sessionId) {
	return this.getHibernateTemplate().find(SQL_QUERY_BY_USER_SESSION, new Object[] { userUid, sessionId });
    }

    /**
     * Get all messages according to special session.
     *
     * @param sessionId
     * @return
     */
    public List<Message> getBySession(Long sessionId) {
	return this.getHibernateTemplate().find(SQL_QUERY_BY_SESSION, sessionId);
    }

    /**
     * Return how many post from this user and session. DOES NOT include posts from author.
     *
     * @param userID
     * @param sessionId
     * @return
     */
    public int getTopicsNum(Long userID, Long sessionId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_TOPICS_NUMBER_BY_USER_SESSION,
		new Object[] { userID, sessionId });
	if (list != null && list.size() > 0) {
	    return ((Number) list.get(0)).intValue();
	} else {
	    return 0;
	}
    }
}
