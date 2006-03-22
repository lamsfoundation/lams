/* 
  Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt 
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
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;



/**
 * Data access routines for Workspaces.
 * 
 * @author Fiona Malikoff
 */
public class WorkspaceDAO extends BaseDAO implements IWorkspaceDAO  {

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
			return super.find(objClass,id);
		} catch (HibernateObjectRetrievalFailureException e ) {
			return null;
		}
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
