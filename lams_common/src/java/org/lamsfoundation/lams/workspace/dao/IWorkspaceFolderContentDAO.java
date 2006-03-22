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
package org.lamsfoundation.lams.workspace.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;

/**
 * @author Manpreet Minhas
 */
public interface IWorkspaceFolderContentDAO extends IBaseDAO {
	
	/**
	 * This method returns the <code>WorkspaceFolderContent</code>
	 * corresponding to the given <code>folderContentID</code>
	 * 
	 * @param folderContentID The <code>folder_content_id</code> of the required object
	 * @return WorkspaceFolderContent The populated <code>WorkspaceFolderContent</code> object
	 */
	public WorkspaceFolderContent getWorkspaceFolderContentByID(Long folderContentID);
	
	/**
	 * This method returns the content for the given <code>WorkspaceFolder</code>
	 * with given <code>workspaceFolderID</code>.
	 * 
	 * @param workspaceFolderID The <code>WorkspaceFolder</code> whose content is requested 
	 * @return List The requested content
	 */
	public List getContentByWorkspaceFolder(Long workspaceFolderID);
	
	/**
	 * This method returns the content of the <code>WorkspaceFolder</code>
	 * with given <code>workspaceFolderID</code>, which is of requested <code>mimeType</code>
	 * 
	 * @param workspaceFolderID The <code>WorkspaceFolder</code> whose content is requested
	 * @param mimeType The <code>mimeType</code> of the requested content
	 * @return List The requested content
	 */
	public List getContentByTypeFromWorkspaceFolder(Long workspaceFolderID, String mimeType);
	
	/**
	 * This method deletes the content with the given UUID, versionID
	 * from the content table.
	 * 
	 * @param uuid The UUID of the content to be deleted
	 * @param versionID The versionID of the content to be deleted
	 * @param folderContentID The folder_content_id to be deleted 
	 * @return
	 */
	public int deleteContentWithVersion(Long uuid, Long versionID, Long folderContentID);

}
