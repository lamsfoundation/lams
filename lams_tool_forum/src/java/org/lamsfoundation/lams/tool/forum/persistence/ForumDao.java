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

	public void delete(Forum forum) {
		//TODO: delete this forum message
//		this.getHibernateTemplate().
//		this.getSession().createQuery()
		this.getHibernateTemplate().delete(forum);
	}

	public List findByNamedQuery(String name) {
		return this.getHibernateTemplate().findByNamedQuery(name);
	}

}
