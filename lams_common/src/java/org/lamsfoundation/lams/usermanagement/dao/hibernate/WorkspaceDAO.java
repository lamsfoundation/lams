/*
 * Created on Mar 22, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

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

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IWorkspaceDAO#getWorkspaceByID(java.lang.Integer)
	 */
	public Workspace getWorkspaceByID(Integer workspaceID) {
		return (Workspace) super.find(Workspace.class,workspaceID);		
		
	}
}
