/*
 * Created on Apr 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.workspace.service;

import java.io.IOException;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IWorkspaceManagementService {
	
	public String getFolderContents(Integer userID, Integer workspaceFolderID, Integer mode)throws IOException;
	
	/**
	 * This method creates a new folder under the given parentFolder
	 * inside the user's default workspace.
	 * 
	 * @param parentFolderID The parent_folder_id under which the new folder 
	 * 						 has to be created
	 * @param name The name of the new folder
	 * @param userID The user_id of the user for whom the folder is being created
	 * @return String The folder_id and name of the newly created folder in WDDX format 
	 * @throws IOException
	 */
	public String createFolderForFlash(Integer parentFolderID, String name, Integer userID)throws IOException;

}
