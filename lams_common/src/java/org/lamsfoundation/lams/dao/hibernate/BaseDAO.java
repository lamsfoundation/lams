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
/* $Id$ */
package org.lamsfoundation.lams.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.lamsfoundation.lams.dao.IBaseDAO;

/**
 * @author Manpreet Minhas
 */
public class BaseDAO extends HibernateDaoSupport implements IBaseDAO {

	/**  
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IBaseDAO#insert(java.lang.Object)
	 */
	public void insert(Object object) {
		this.getHibernateTemplate().save(object);	
	}

	/**
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IBaseDAO#update(java.lang.Object)
	 */
	public void update(Object object) {
		this.getHibernateTemplate().update(object);
	}

	/**
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IBaseDAO#insertOrUpdate(java.lang.Object)
	 */
	public void insertOrUpdate(Object object) {
		this.getHibernateTemplate().saveOrUpdate(object);
	}

	/** 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IBaseDAO#delete(java.lang.Object)
	 */
	public void delete(Object object) {
		this.getHibernateTemplate().delete(object);
	}

	/**
	 * Find an object. If the object is not found, 
	 * then it will return null
	 * @param objClass
	 * @param id
	 */
	public Object find(Class objClass, Serializable id) {
		return this.getHibernateTemplate().get(objClass,id);
		
	}

	/** 
	 * @see org.lamsfoundation.lams.learningdesign.dao.interfaces.IBaseDAO#findAll(java.lang.Class)
	 */
	public List findAll(Class objClass) {
		String query="from obj in class " + objClass.getName(); 
		return this.getHibernateTemplate().find(query);
	}

}
