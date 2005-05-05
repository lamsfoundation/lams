/*
 * Created on Apr 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.workspace.dao;

import java.util.List;

import org.lamsfoundation.lams.learningdesign.dao.IBaseDAO;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;

/**
 * @author Manprete Minhas
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
	
	public int deleteContentWithVersion(Long uuid, Long versionID, Long folderContentID);

}
