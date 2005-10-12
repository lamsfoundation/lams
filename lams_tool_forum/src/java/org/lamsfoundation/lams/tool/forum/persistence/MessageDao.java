/*
 * Created on Jun 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author conradb
 */
public class MessageDao extends HibernateDaoSupport {

	public void saveOrUpdate(Message message) {
		message.updateModificationData();
		this.getHibernateTemplate().saveOrUpdate(message);
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

}
