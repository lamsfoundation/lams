/*
 * Created on Dec 20, 2004
 */
package org.lamsfoundation.lams.contentrepository.dao;

import java.io.Serializable;
import java.util.List;

import org.lamsfoundation.lams.contentrepository.CrNode;
import org.lamsfoundation.lams.contentrepository.CrNodeVersion;

/**
 * Data access routines for Nodes, versions and properties
 * 
 * @author Fiona Malikoff
 */
public interface INodeDAO {

	public Object find(Class objClass, Serializable id);

	public void insert(Object object);		

	public void update(Object object);

	public void delete(Object object);

	public List findAll(Class objClass);
	
	public List findChildNodes(CrNodeVersion parentNodeVersion);
	public CrNode findChildNode(CrNodeVersion parentNodeVersion, String relPath);

	
}
