package org.lamsfoundation.lams.workspace.service;

import java.io.IOException;

/**
 * @author Manpreet Minhas
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
	
	
	/**
	 * This method deletes the <code>WorkspaceFolder</code> with given
	 * <code>workspaceFolderID</code>. But before it does so it checks whether the
	 * <code>User</code> is authorized to perform this action <br>
	 *  
	 * <p><b>Note:</b><br></p><p>To be able to a delete a <code>WorkspaceFolder</code>
	 * successfully you have to keep the following things in mind
	 * <ul>
	 * <li>The folder to be deleted should be empty.</li>
	 * <li>It should not be the root folder of any <code>Organisation</code>
	 * 	   or <code>User</code>
	 * </li>
	 * </ul></p>
	 * 
	 * @param workspaceFolderID The <code>WorkspaceFolder</code> to be deleted
	 * @param userID The <code>User</code> who has requested this operation
	 * @return String The acknowledgement/error message in WDDX format for FLASH
	 * @throws IOException
	 */
	public String deleteFolder(Integer workspaceFolderID, Integer userID)throws IOException;

}
