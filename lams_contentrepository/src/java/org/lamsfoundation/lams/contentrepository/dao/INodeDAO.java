/*
 * Created on Dec 20, 2004
 */
package org.lamsfoundation.lams.contentrepository.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Data access routines for Nodes, versions and properties
 * 
 * @author Fiona Malikoff
 */
public interface INodeDAO {

	/** Finds an object. Return null if not found (note: this
	 * is not the standard behaviour for Spring and Hibernate combined.)
	 */
	public Object find(Class objClass, Serializable id);

	public void insert(Object object);		

	public void update(Object object);

	public void delete(Object object);

	public List findAll(Class objClass);
	
}
