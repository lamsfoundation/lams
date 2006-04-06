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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author conradb
 */
public class MessageDao extends HibernateDaoSupport {
	private static final String SQL_QUERY_FIND_ROOT_TOPICS = "from " + Message.class.getName() +" m "
					+ " where parent_uid is null and m.toolSession.sessionId=?";
	
	private static final String SQL_QUERY_FIND_TOPICS_FROM_AUTHOR = "from " + Message.class.getName()
					+ " where is_authored = true and forum_uid=? order by create_date";
	
	private static final String SQL_QUERY_FIND_CHILDREN = "from " + Message.class.getName()
					+ " where parent=?";
	
	private static final String SQL_QUERY_BY_USER_SESSION = "from " + Message.class.getName() + " m "
					+ " where m.createdBy.uid = ? and  m.toolSession.sessionId=?";
	
	private static final String SQL_QUERY_BY_SESSION = "from " + Message.class.getName() + " m "
					+ " where m.toolSession.sessionId=?";
	
	public void saveOrUpdate(Message message) {
		message.updateModificationData();
		this.getHibernateTemplate().saveOrUpdate(message);
		this.getHibernateTemplate().flush();
	}
	
	public void update(Message message) {
		this.getHibernateTemplate().saveOrUpdate(message);
		this.getHibernateTemplate().flush();
	}

	public Message getById(Long messageId) {
		return (Message) getHibernateTemplate().get(Message.class,messageId);
	}
	/**
	 * Get all root (first level) topics in a special Session.
	 * @param sessionId
	 * @return
	 */
	public List getRootTopics(Long sessionId) {
		return this.getHibernateTemplate().find(SQL_QUERY_FIND_ROOT_TOPICS, sessionId);
	}
	/**
	 * Get all message posted by author role in a special forum.
	 * @param forumUid
	 * @return
	 */
	public List getTopicsFromAuthor(Long forumUid) {
		return this.getHibernateTemplate().find(SQL_QUERY_FIND_TOPICS_FROM_AUTHOR, forumUid);
	}

	public void delete(Long uid) {
		Message msg = getById(uid);
		if(msg != null){
			this.getHibernateTemplate().delete(msg);
			this.getHibernateTemplate().flush();
		}
	}
	/**
	 * Get all children message from the given parent topic ID. 
	 * @param parentId
	 * @return
	 */
	public List getChildrenTopics(Long parentId) {
		return this.getHibernateTemplate().find(SQL_QUERY_FIND_CHILDREN, parentId);
	}
	/**
	 * Get all messages according to special user and session.
	 * @param userId
	 * @param sessionId
	 * @return
	 */
	public List getByUserAndSession(Long userId, Long sessionId) {
		return this.getHibernateTemplate().find(SQL_QUERY_BY_USER_SESSION, new Object[]{userId,sessionId});
	}
	/**
	 * Get all messages according to special session.
	 * @param sessionId
	 * @return
	 */
	public List getBySession(Long sessionId) {
		return this.getHibernateTemplate().find(SQL_QUERY_BY_SESSION, sessionId);
	}
	


}
