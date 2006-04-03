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
package org.lamsfoundation.lams.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manpreet Minhas
 */
public interface IBaseDAO {
	
	/**
	 * Insert an object into the database. Should only be used if the object has not
	 * been persisted previously.
	 * 
	 * @param object The object to be inserted
	 */
	public void insert(Object object);
	/**
	 * Update a previously inserted object into the database.
	 * @param object The object to be updated
	 */
	public void update(Object object);
	/**
	 * Insert or update an object into the database. It is up to the persistence
	 * engine to decide whether to insert or update.
	 * @param object The object to be inserted/updated
	 */
	public void insertOrUpdate(Object object);
	/**
	 * Remove an object from the database.
	 * @param object The object to be deleted
	 */
	public void delete(Object object);
	
	/**
	 * Find an object. If the object is not found 
	 * then it will return null
	 * @param objClass
	 * @param id
	 */
	public Object find(Class objClass, Serializable id);
	
	/**
	 * @param objClass
	 */
	public List findAll(Class objClass);
}
