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



package org.lamsfoundation.lams.tool.forum.dao.hibernate;

import java.util.List;

import org.hibernate.LockOptions;
import org.hibernate.query.NativeQuery;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.forum.dao.IMessageDAO;
import org.lamsfoundation.lams.tool.forum.model.Message;
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

    private static final String SQL_QUERY_DATES_BY_USER_SESSION = "SELECT MIN(create_date) start_date, MAX(create_date) end_date "
	    + " FROM tl_lafrum11_message WHERE create_by = :userUid";

    @Override
    public void saveOrUpdate(Message message) {
	this.getSession().saveOrUpdate(message);
    }

    @Override
    public void update(Message message) {
	this.getSession().saveOrUpdate(message);
    }

    @Override
    public Message getByIdForUpdate(Long messageId) {
	return (Message) getSession().get(Message.class, messageId, LockOptions.UPGRADE);
    }

    @Override
    public Message getById(Long messageId) {
	return (Message) getSession().get(Message.class, messageId);
    }

    @Override
    public List<Message> getRootTopics(Long sessionId) {
	return (List<Message>) this.doFind(SQL_QUERY_FIND_ROOT_TOPICS, sessionId);
    }

    @Override
    public List<Message> getTopicsFromAuthor(Long forumUid) {
	return (List<Message>) this.doFind(SQL_QUERY_FIND_TOPICS_FROM_AUTHOR, forumUid);
    }

    @Override
    public void delete(Long uid) {
	Message msg = getById(uid);
	if (msg != null) {
	    this.getSession().delete(msg);
	}
    }

    @Override
    public List<Message> getChildrenTopics(Long parentId) {
	return (List<Message>) this.doFind(SQL_QUERY_FIND_CHILDREN, parentId);
    }

    @Override
    public List<Message> getByUserAndSession(Long userUid, Long sessionId) {
	return (List<Message>) this.doFind(SQL_QUERY_BY_USER_SESSION, new Object[] { userUid, sessionId });
    }

    @Override
    public List<Message> getBySession(Long sessionId) {
	return (List<Message>) this.doFind(SQL_QUERY_BY_SESSION, sessionId);
    }

    @Override
    public int getTopicsNum(Long userID, Long sessionId) {
	List list = this.doFind(SQL_QUERY_TOPICS_NUMBER_BY_USER_SESSION, new Object[] { userID, sessionId });
	if (list != null && list.size() > 0) {
	    return ((Number) list.get(0)).intValue();
	} else {
	    return 0;
	}
    }
    
    @Override
    public Object[] getDateRangeOfMessages(Long userUid) {
	NativeQuery<?> query =  getSession().createNativeQuery(SQL_QUERY_DATES_BY_USER_SESSION.toString())
		.setParameter("userUid", userUid);
	Object[] values = (Object[]) query.list().get(0);
	return values;
    }
}
