/*
 * Created on Jun 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
