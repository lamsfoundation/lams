/*
 * Created on Dec 20, 2004
 */
package org.lamsfoundation.lams.contentrepository.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.dao.INodeDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;



/**
 * Data access routines for Nodes
 * 
 * @author Fiona Malikoff
 */
public class NodeDAO extends HibernateDaoSupport implements INodeDAO  {

	protected Logger log = Logger.getLogger(NodeDAO.class);	

	public Object find(Class objClass, Serializable id) {
		return this.getHibernateTemplate().load(objClass,id);
	}
	
	public void insert(Object object) {
		this.getHibernateTemplate().saveOrUpdate(object);		
	}

	public void update(Object object) {
		this.getHibernateTemplate().update(object);
	}

	public void delete(Object object) {
		this.getHibernateTemplate().delete(object);
	}

	public List findAll(Class objClass) {
		String query="from obj in class " + objClass.getName(); 
		return this.getHibernateTemplate().find(query);
	}
	
}
