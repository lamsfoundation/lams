/*
 * Created on Feb 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.usermanagement.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;

/**
 * @author Manpreet Minhas
 */
public class WorkspaceFolderDAO extends BaseDAO implements IWorkspaceFolderDAO {
	
	private final String TABLENAME ="lams_workspace_folder";
	private final String FIND_BY_PARENT = "from " + TABLENAME +
												 " in class " + WorkspaceFolder.class.getName() +
												 " where parent_folder_id=?";
	private final String FIND_BY_USER = "from " + TABLENAME +
										" in class " + WorkspaceFolder.class.getName() +
										" where user_id=?";
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO#getWorkspaceFolderByID(java.lang.Long)
	 */
	public WorkspaceFolder getWorkspaceFolderByID(Integer workspaceFolderID) {
		return (WorkspaceFolder) super.find(WorkspaceFolder.class,workspaceFolderID);				
	}
	public WorkspaceFolder getRunSequencesFolderForUser(Integer userID){
		String query = "from WorkspaceFolder wf where wf.userID=? AND wf.workspaceFolderType=2";
		List list = this.getHibernateTemplate().find(query,userID); 
		if(list.size()!=0)
			return (WorkspaceFolder)list.get(0);
		else
			return null;
	}
	public List getWorkspaceFolderByParentFolder(Integer parentFolderID){		
		return this.getHibernateTemplate().find(FIND_BY_PARENT,parentFolderID);		
	}
	public List getWorkspaceFolderByUser(Integer userID){
		return this.getHibernateTemplate().find(FIND_BY_USER,userID);
	}
}
