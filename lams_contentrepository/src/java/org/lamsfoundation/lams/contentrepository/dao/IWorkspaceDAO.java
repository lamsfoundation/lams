/*
 * Created on Dec 20, 2004
 */
package org.lamsfoundation.lams.contentrepository.dao;

import java.io.Serializable;
import java.util.List;

import org.lamsfoundation.lams.contentrepository.CrWorkspace;


/**
 * Data access routines for Workspaces.
 * 
 * @author Fiona Malikoff
 */
public interface IWorkspaceDAO {

	/** Get a workspace. 
	 * 
	 * @param workspaceName
	 * @return first (and expected only) workspace with this name. 
	 */
	public CrWorkspace findByName(String workspaceName);
	
	/** Get all the nodes for a workspace.
	 * 
	 * Can't just get the workspace as the hibernate implementation 
	 * will lazy load the nodes, and so getNodes() will be missing 
	 * the necessary info when workspace is returned to the calling 
	 * code. 
	 * 
	 * @param workspaceId
	 * @return first (and expected only) workspace with this name. 
	 */
	public List findWorkspaceNodes(Long workspaceId);

	/** Finds an object. Return null if not found (note: this
	 * is not the standard behaviour for Spring and Hibernate combined.)
	 * @param objClass
	 * @param id
	 * @return object built from database
	 */
	public Object find(Class objClass, Serializable id);

	public void insert(Object object);		

	public void update(Object object);

	public void delete(Object object);

	public List findAll(Class objClass);
	
}
