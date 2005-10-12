package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * ForumDao
 * @author conradb
 *
 *
 */
public class ForumDao extends HibernateDaoSupport {

	public void saveOrUpdate(Forum forum) {
		forum.updateModificationData();
		this.getHibernateTemplate().saveOrUpdate(forum);
	}

	public Forum getById(Long forumId) {
		return (Forum) getHibernateTemplate().get(Forum.class,forumId);
	}
	/**
	 * NOTE: before call this method, must be sure delete all messages in this forum.
	 * Example code like this:
	 * <pre>
	 * <code>messageDao.deleteForumMessage(forum.getUuid());</code>
	 * </pre>
	 * @param forum
	 */
	public void delete(Forum forum) {
		this.getHibernateTemplate().delete(forum);
	}

	public List findByNamedQuery(String name) {
		return this.getHibernateTemplate().findByNamedQuery(name);
	}

}
