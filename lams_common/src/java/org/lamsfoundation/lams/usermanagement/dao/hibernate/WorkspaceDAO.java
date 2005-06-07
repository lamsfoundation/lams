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
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceDAO;

/**
 * @author Manpreet Minhas
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
