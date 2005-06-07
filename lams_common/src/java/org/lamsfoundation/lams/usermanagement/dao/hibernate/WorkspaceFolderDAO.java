/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
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
