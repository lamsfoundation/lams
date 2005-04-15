/*
 * Created on Mar 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceDAO;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkspaceDAO extends BaseDAO implements IWorkspaceDAO{
	
	private static final String TABLENAME="lams_workspace";
	private static final String FIND_BY_ROOT_FOLDER = "from " + TABLENAME + " in class " + Workspace.class.getName() +
													  " where root_folder_id=?";
													  

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IWorkspaceDAO#getWorkspaceByID(java.lang.Integer)
	 */
	public Workspace getWorkspaceByID(Integer workspaceID) {
		return (Workspace) super.find(Workspace.class,workspaceID);		
		
	}
	public Workspace getWorkspaceByRootFolderID(Integer rootFolderID){
		List list = this.getHibernateTemplate().find(FIND_BY_ROOT_FOLDER,rootFolderID);
		return (Workspace)list.get(0);	
	}
}
