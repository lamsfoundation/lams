/*
 * Created on Dec 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author manpreet
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
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
	 * @param objClass
	 * @param id
	 */
	public Object find(Class objClass, Serializable id);
	/**
	 * @param objClass
	 */
	public List findAll(Class objClass);
}
