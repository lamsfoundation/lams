/***************************************************************************
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
 * ************************************************************************
 */
package org.lamsfoundation.lams.usermanagement.dao;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.usermanagement.Workspace;

/**
 * @author Manpreet Minhas
 *
 */
public interface IWorkspaceDAO extends IBaseDAO{
	
	/**
	 * Returns the workspace corresponding to the given
	 * workspace_id
	 * 
	 * @param workspaceID The workspace_id of the workspace being 
	 * 					  looked for
	 * @return Workspace The populated workspace object 
	 */
	public Workspace getWorkspaceByID(Integer workspaceID);	
	
	/**
	 * Returns the workspace corresponding to the given
	 * root_folder_id
	 * 
	 * @param rootFolderID The root_folder_id of the workspace
	 * 					   being looked for
	 * @return Workspace The populated workspace object 
	 */
	public Workspace getWorkspaceByRootFolderID(Integer rootFolderID);

}
