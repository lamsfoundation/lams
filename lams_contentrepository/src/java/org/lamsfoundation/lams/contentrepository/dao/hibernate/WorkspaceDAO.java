/*
 * Created on Dec 20, 2004
 */
package org.lamsfoundation.lams.contentrepository.dao.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.CrWorkspace;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.dao.IWorkspaceDAO;
import org.springframework.orm.hibernate.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;



/**
 * Data access routines for Workspaces.
 * 
 * @author Fiona Malikoff
 */
public class WorkspaceDAO extends HibernateDaoSupport implements IWorkspaceDAO  {

	protected Logger log = Logger.getLogger(WorkspaceDAO.class);	

	/** 
	 * Get a workspace. Credentials to be checked by calling code.
	 * 
	 * @param workspaceName
	 * @return first (and expected only) workspace with this name. 
	 */
	public CrWorkspace findByName(String workspaceName) {

		log.debug("Getting workspace for name "+workspaceName);
		
		String queryString = "from CrWorkspace as w where w.name = ?";
		List workspaces = getHibernateTemplate().find(queryString,workspaceName);
		
		if(workspaces.size() == 0){
			log.debug("No workspaces found");
			return null;
		}else{
			CrWorkspace workspace = (CrWorkspace)workspaces.get(0);
			if ( log.isDebugEnabled() )
				log.debug("Returning workspace "+workspace.toString());
			return workspace;
		}
	}
	
	/** Get all the nodes for a workspace.
	 * 
	 * Can't just get the workspace as the hibernate implementation 
	 * will lazy load the nodes, and so getNodes() will be missing 
	 * the necessary info when workspace is returned to the calling 
	 * code. 
	 * 
	 * @param workspaceId
	 * @return Set of CrNodes applicable to this workspace. 
	 */
	public List findWorkspaceNodes(Long workspaceId) {
		CrWorkspace workspace = (CrWorkspace) find(CrWorkspace.class, workspaceId);
		Set nodes = workspace.getCrNodes();
		int size = nodes.size();
		return new ArrayList(nodes);
	}
	
	public Object find(Class objClass, Serializable id) {
		try {
			return this.getHibernateTemplate().load(objClass,id);		
		} catch (HibernateObjectRetrievalFailureException e ) {
			return null;
		}
	}

	public void insert(Object object) {
		this.getHibernateTemplate().save(object);		
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
	
	public void flushSession() throws RepositoryCheckedException {
		try {
			getSession().flush();
		} catch (Exception e) {
			log.error("Exception occured during flush. ",e);
			throw new RepositoryCheckedException("Unable to write changes to db successfully (flush).", e);
		} 
	}

}
