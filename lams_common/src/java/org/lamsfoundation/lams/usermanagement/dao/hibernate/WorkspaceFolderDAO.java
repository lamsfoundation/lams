/*
 * Created on Feb 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkspaceFolderDAO extends HibernateDaoSupport implements IWorkspaceFolderDAO {

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO#getWorkspaceFolderByID(java.lang.Long)
	 */
	public WorkspaceFolder getWorkspaceFolderByID(Integer workspaceFolderID) {
		return (WorkspaceFolder)this.getHibernateTemplate().get(WorkspaceFolder.class,workspaceFolderID);		
	}

}
