/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.learningdesign.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author Manpreet Minhas
 */
public interface IBaseDAO {
	
	/**
	 * @param object The object to be inserted
	 */
	public void insert(Object object);
	/**
	 * @param object The object to be updated
	 */
	public void update(Object object);
	/**
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
