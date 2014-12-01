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

package org.lamsfoundation.lams.tool.forum.persistence.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.forum.persistence.IMessageDAO;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.springframework.stereotype.Repository;

/**
 * @author conradb
 */
@Repository
public class MessageDao extends LAMSBaseDAO implements IMessageDAO {
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

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageDAO#saveOrUpdate(org.lamsfoundation.lams.tool.forum.persistence.Message)
	 */
    @Override
	public void saveOrUpdate(Message message) {
	this.getSession().saveOrUpdate(message);
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageDAO#update(org.lamsfoundation.lams.tool.forum.persistence.Message)
	 */
    @Override
	public void update(Message message) {
	this.getSession().saveOrUpdate(message);
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageDAO#getById(java.lang.Long)
	 */
    @Override
	public Message getById(Long messageId) {
	return (Message) getSession().get(Message.class, messageId);
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageDAO#getRootTopics(java.lang.Long)
	 */
    @Override
	public List getRootTopics(Long sessionId) {
	return this.doFind(SQL_QUERY_FIND_ROOT_TOPICS, sessionId);
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageDAO#getTopicsFromAuthor(java.lang.Long)
	 */
    @Override
	public List getTopicsFromAuthor(Long forumUid) {
	return this.doFind(SQL_QUERY_FIND_TOPICS_FROM_AUTHOR, forumUid);
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageDAO#delete(java.lang.Long)
	 */
    @Override
	public void delete(Long uid) {
	Message msg = getById(uid);
	if (msg != null) {
	    this.getSession().delete(msg);
	}
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageDAO#getChildrenTopics(java.lang.Long)
	 */
    @Override
	public List getChildrenTopics(Long parentId) {
	return this.doFind(SQL_QUERY_FIND_CHILDREN, parentId);
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageDAO#getByUserAndSession(java.lang.Long, java.lang.Long)
	 */
    @Override
	public List getByUserAndSession(Long userUid, Long sessionId) {
	return this.doFind(SQL_QUERY_BY_USER_SESSION, new Object[] { userUid, sessionId });
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageDAO#getBySession(java.lang.Long)
	 */
    @Override
	public List getBySession(Long sessionId) {
	return this.doFind(SQL_QUERY_BY_SESSION, sessionId);
    }

    /* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.forum.persistence.hibernate.IMessageDAO#getTopicsNum(java.lang.Long, java.lang.Long)
	 */
    @Override
	public int getTopicsNum(Long userID, Long sessionId) {
	List list = this.doFind(SQL_QUERY_TOPICS_NUMBER_BY_USER_SESSION,
		new Object[] { userID, sessionId });
	if (list != null && list.size() > 0)
	    return ((Number) list.get(0)).intValue();
	else
	    return 0;
    }
}
