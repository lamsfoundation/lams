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
	private static final String SQL_QUERY_FIND_ROOT_TOPICS = "from " + Message.class.getName() 
					+ " where parent_uid is null and forum_session_uid=?";
	
	private static final String SQL_QUERY_FIND_TOPICS_FROM_AUTHOR = "from " + Message.class.getName()
					+ " where is_authored = true and forum_uid=?";
	
	private static final String SQL_QUERY_FIND_CHILDREN = "from " + Message.class.getName()
					+ " where parent=?";
	
	private static final String SQL_QUERY_BY_USER_SESSION = "from " + Message.class.getName() + " m "
					+ " where m.createdBy.uid = ? and  m.toolSession.uid=?";
	
	private static final String SQL_QUERY_BY_SESSION = "from " + Message.class.getName() + " m "
					+ " where m.toolSession.sessionId=?";
	
	public void saveOrUpdate(Message message) {
		message.updateModificationData();
		this.getHibernateTemplate().saveOrUpdate(message);
		this.getHibernateTemplate().flush();
	}

	public void save(Message message) {
		message.updateModificationData();
		this.getHibernateTemplate().save(message);
		this.getHibernateTemplate().flush();
	}
	public Message getById(Long messageId) {
		return (Message) getHibernateTemplate().get(Message.class,messageId);

	}

	public void delete(Message message) {
		this.getHibernateTemplate().delete(message);
	}

	public List findByNamedQuery(String name, Long forumId) {
		return this.getHibernateTemplate().findByNamedQuery(name, forumId);
	}

	/**
	 * Delete all messages in special forum.
	 * @param forumUuid 
	 * 		The forum UUID which messages will be deleted in this method.
	 */
	public void deleteForumMessage(Long forumUuid) {
		List list = findByNamedQuery("allMessagesByForum",forumUuid);
		this.getHibernateTemplate().deleteAll(list);
	}

	public List getAuthoredMessage(Long forumId) {
		return findByNamedQuery("allAuthoredMessagesOfForum", forumId);
	}

	public List getRootTopics(Long sessionId) {
		return this.getHibernateTemplate().find(SQL_QUERY_FIND_ROOT_TOPICS, sessionId);
	}
	public List getTopicsFromAuthor(Long forumId) {
		return this.getHibernateTemplate().find(SQL_QUERY_FIND_TOPICS_FROM_AUTHOR, forumId);
	}

	public void deleteById(Long uid) {
		Message msg = getById(uid);
		if(msg != null){
			this.getHibernateTemplate().delete(msg);
			this.getHibernateTemplate().flush();
		}
	}

	public List getChildrenTopics(Long parentId) {
		return this.getHibernateTemplate().find(SQL_QUERY_FIND_CHILDREN, parentId);
	}

	public List getByUserAndSession(Long userId, Long sessionId) {
		return this.getHibernateTemplate().find(SQL_QUERY_BY_USER_SESSION, new Object[]{userId,sessionId});
	}
	
	public List getBySession(Long sessionId) {
		return this.getHibernateTemplate().find(SQL_QUERY_BY_SESSION, sessionId);
	}
	


}
