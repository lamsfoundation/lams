/*
 * Created on Dec 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.springframework.orm.hibernate.support.HibernateDaoSupport;

import org.lamsfoundation.lams.learningdesign.dao.IBaseDAO;

/**
 * @author manpreet
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BaseDAO extends HibernateDaoSupport implements IBaseDAO {

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IBaseDAO#insert(java.lang.Object)
	 */
	public void insert(Object object) {
		this.getHibernateTemplate().save(object);		
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IBaseDAO#update(java.lang.Object)
	 */
	public void update(Object object) {
		this.getHibernateTemplate().update(object);
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IBaseDAO#delete(java.lang.Object)
	 */
	public void delete(Object object) {
		this.getHibernateTemplate().delete(object);
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IBaseDAO#find(java.lang.Class, java.lang.Long)
	 */
	public Object find(Class objClass, Serializable id) {
		return this.getHibernateTemplate().load(objClass,id);		
	}

	/* 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IBaseDAO#findAll(java.lang.Class)
	 */
	public List findAll(Class objClass) {
		String query="from obj in class " + objClass.getName(); 
		return this.getHibernateTemplate().find(query);
	}

}
