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
package org.lamsfoundation.lams.workspace.service;

import java.io.IOException;
import java.util.Date;

import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;

/**
 * @author Manpreet Minhas
 */
public interface IWorkspaceManagementService {
	
	public static final String REPOSITORY_USERNAME ="workspaceManager";
	public static final String REPOSITORY_PASSWORD ="flashClient";
	public static final String REPOSITORY_WORKSPACE="FlashClientsWorkspace";
	
	/**
	 * This method returns the contents of the folder with given
	 * <code>workspaceFolderID</code> depending upon the <code>mode</code>. 
	 * Before it does so, it checks whether the given <code>User</code> 
	 * is authorized to perform this action.
	 * 
	 * The <code>mode</code> can be either of the following
	 * <ul>
	 * <li> AUTHORING In which case all the Learning Designs in the given
	 * 		folder are returned OR</li>
	 * <li> MONITORING In which case only those Learning Designs that are 
	 * 		valid are returned </li>
	 * </ul>
	 *  
	 * 
	 * <p><b>Note: </b>It only returns the top level contents. To navigate to
	 * the contents of the sub-folders of the given <code>WorkspaceFolder</code>
	 * we have to call this method again with there <code>workspaceFolderID</code></p>
	 * 
	 * <p><b>For Example:</b></p>
	 * <p> For a folder with given tree structure<b>
	 * <pre>
	 *                       A	 						
	 * 					     |
	 * 				--------------------
	 * 				|        |          |
	 * 			   A1		A2         A3
	 * 				|
	 * 			---------
	 * 			|		|
	 * 		   AA1	   AA2
	 * </pre></b>
	 * This function will only retun A1, A2 and A3 and to get the contents if A1 we have to again call this
	 * method with workspaceFolderID of A1. 
	 * 
	 * @param userID The <code>user_id</code> of the <code>User</code> who has requested the contents
	 * @param workspaceFolderID The <code>workspace_folder_id</code> of the <code>WorkspaceFolder</code>
	 * 							whose contents are requested
	 * @param mode It can be either 1(AUTHORING) or 2(MONITORING)
	 * @return String The required information in WDDX format 
	 * @throws Exception
	 */
	public String getFolderContents(Integer userID, Integer workspaceFolderID, Integer mode)throws Exception;
	
	/**
	 * This method does the same as getFolderContents() except that it doesn't return
	 * the home directory as a content of the folder. This is useful to Flash as when the user's organisation 
	 * is listed, the client doesn't want the home directory returned as the client already knows about the 
	 * folder from the getAccessibleWorkspaceFolders() call.
	 * 
	 * @param userID The <code>user_id</code> of the <code>User</code> who has requested the contents
	 * @param workspaceFolderID The <code>workspace_folder_id</code> of the <code>WorkspaceFolder</code>
	 * 							whose contents are requested
	 * @param mode It can be either 1(AUTHORING) or 2(MONITORING)
	 * @return String The required information in WDDX format 
	 * @throws Exception
	 */
	public String getFolderContentsExcludeHome(Integer userID, Integer workspaceFolderID, Integer mode)throws Exception;
	
	/**
	 * This method creates a new folder under the given parentFolder
	 * inside the user's default workspace.
	 * 
	 * @param parentFolderID The <code>parent_folder_id</code> of the <code>WorkspaceFolder</code>
	 * 						 under which the new folder has to be created
	 * @param name The name of the new folder
	 * @param userID The <code>user_id</code> of the <code>User</code> who owns this folder
	 * @return WorkspaceFolder The <code>WorkspaceFolder</code> freshly created 
	 * @throws UserException
	 * @throws WorkspaceFolderException
	 */
	public WorkspaceFolder createFolder(Integer parentFolderID, String name, Integer userID) throws UserException,WorkspaceFolderException;
	
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
	 * This method deletes an entry from a workspace. It may be a folder,
	 * a learning design or an arbitrary resource. It works out what the resource
	 * is and then calls the other deletion methods. Currently folders, files and learning
	 * designs are supported - all other types return an error.
	 * <p>
	 * @param resourceID The <code>id</code> to be deleted. May be a learning design id
	 * or a folder id.
	 * @param resourceType The resource type sent to the client in the FolderContentDTO. 
	 * @param userID The <code>User</code> who has requested this operation
	 * @return String The acknowledgement/error message in WDDX format for FLASH
	 * @throws IOException
	 */
	public String deleteResource(Long resourceID, String resourceType, Integer userID)
		throws IOException;

	/**
	 * This method copies one folder inside another folder, or a learning design 
	 * to another folder. If it is a learning design then it also needs the copyType parameter.
	 *  		String wddxPacket = workspaceManagementService.copyResource(resourceID,resourceType,copyType,targetFolderID,userID);

	 * @param resourceID The <code>WorkspaceFolder</code> or <code>LearningDesign</code> to be copied.
	 * @param resourceType The resource type sent to the client in the FolderContentDTO. 
	 * @param copyType Is this an ordinary learning design or a runtime learning design.  
	 * @param targetFolderID The parent <code>WorkspaceFolder</code> under 
	 * 					  which it has to be copied
	 * @param userID The <code>User</code> who has requested this opeartion
	 * @return String The acknowledgement/error message to be sent to FLASH
	 * @throws IOException
	 */
	public String copyResource(Long resourceID, String resourceType, Integer copyType, Integer targetFolderID, Integer userID)throws IOException;
	
	
	/**
	 * This method moves an entry (folder, design, etc) etc from a workspace. It may be a folder,
	 * a learning design or an arbitrary resource. under the WorkspaceFolder with <code>targetFolderID</code>.
	 * But before it does so it checks whether the <code>User</code> is authorized to do so.
	 * <p>
	 * Currently only folders and learning 	designs are supported - all other types return an error.
	 * 
	 * @param resourceID The <code>id</code> to be moved. May be a learning design id
	 * or a folder id.
	 * @param resourceType The resource type sent to the client in the FolderContentDTO. 
	 * @param targetFolderID The WorkspaceFolder under which it has to be moved
	 * @param userID The User who has requested this opeartion
	 * @return String The acknowledgement/error message in WDDX format for FLASH
	 * @throws IOException
	 */
	public String moveResource(Long resourceID, Integer targetFolderID, String resourceType, Integer userID)
		throws IOException;

	/**
	 * This method is called every time a new content is inserted into the
	 * given workspaceFolder. The content to be inserted can be either of 
	 * type FILE or PACKAGE, which is indicated by <code>contentTypeID</code>.
	 * A value of 1 indicates a FILE and a value of 2 indicates PACKAGE.
	 * 
	 * After updating the database with the corresponding entry of the new content
	 * this method then updates the Repository as well.
	 *  
	 * @param contentTypeID The type of content being added.(FILE/PACKAGE)
	 * @param name The name of the file
	 * @param description The description of the file
	 * @param workspaceFolderID The container(<code>workspaceFolder</code>)which
	 * 							holds this content
	 * @param mimeType The MIME type of the file 
	 * @param path The physical location of the file from where it has to be read
	 * @return String The acknowledgement/error message in WDDX format for the 
	 * 				  FLASH client.
	 * @throws Exception
	 */
	public String createWorkspaceFolderContent(Integer contentTypeID, String name, String description, Integer workspaceFolderID, String mimeType, String path) throws Exception;
	
	/**
	 * This method updates an existing file(<code>workspaceFolderContet</code>)
	 * with new contents in the Reositiory. 
	 * 
	 * @param folderContentID The <code>folder_content_id</code> of the file 
	 * 						  to be updated
	 * @param path The physical location of the file from where it has to be read
	 * @return String The acknowledgement/error message in WDDX format for FLASH
	 * @throws Exception
	 */
	public String updateWorkspaceFolderContent(Long folderContentID,String path)throws Exception;
	
	/**
	 * This method is called when the user knows which version of the
	 * <code>worksapceFolderContent</code> he wants to delete.
	 * 
	 * The content to be deleted can be one of the following 
	 * <ol>
	 * 		<li> The only version available in the repository,
	 * 		     In which case both the records from the repository
	 * 			  and the database should be deleted.</li>
	 * 		<li> The latest version in the repository, in which case
	 * 			 the database should be updated with the next available
	 * 			 latest version.</li>
	 * 		<li> One of the available versions in the repository, in which 
	 * 			 case the corresponding version is deleted from the repository
	 * 			 and database is not effected </li>
	 * </ol>
	 * @param uuid The uuid of the <code>workspaceFolderContent</code>
	 * @param versionToBeDeleted The versionID of the <code>workspaceFolderContent</code>
	 * @param folderContentID The <code>folder_content_id</code> of the content to be deleted
	 * @return String Acknowledgement/error message in WDDX format for FLASH 
	 * @throws Exception
	 */	
	public String deleteContentWithVersion(Long uuid, Long versionToBeDeleted,Long folderContentID)throws Exception;
	
	/**
	 * This method returns a list of workspace folders for which 
	 * the user has "write" access. A user can write/save his content
	 * in a folder in one of the following cases
	 * <ol>
	 * 	  <li>He is the OWNER of the given workspace folder</li>
	 * 	  <li>He is a MEMBER of the organisation to which the
	 * 			folder belongs and he has one or all of the follwing 
	 * 			roles (SYSADMIN. ADMIN, AUTHOR, STAFF, TEACHER)</li>	  		
	 * </ol>
	 * 
	 * The information returned is categorized under 3 main heads
	 * <ul>
	 * <li>PRIVATE The folder which belongs to the given User</li>
	 * <li>ORGANISATIONS List of folders(root folder only) which belong 
	 * to organisations of which user is a member.</li>
	 * </ul>
	 * 
	 * @param userID The <code>user_id</code> of the user for whom the
	 * 				 folders have to fetched
	 * @return String The required information in WDDX format
	 * @throws IOException
	 */
	public String getAccessibleWorkspaceFolders(Integer userID)throws IOException;
	
	/**
	 * This method returns a list of workspace folders for which 
	 * the user has "write" access. A user can write/save his content
	 * in a folder in one of the following cases
	 * <ol>
	 * 	  <li>He is the OWNER of the given workspace folder</li>
	 * 	  <li>He is a MEMBER of the organisation to which the
	 * 			folder belongs and he has one or all of the follwing 
	 * 			roles (SYSADMIN. ADMIN, AUTHOR, STAFF, TEACHER)</li>	  		
	 * </ol>
	 * 
	 * The information returned is categorized under 3 main heads
	 * <ul>
	 * <li>PRIVATE The folder which belongs to the given User</li>
	 * <li>ORGANISATIONS List of folders(root folder only) which belong 
	 * to organisations of which user is a member.</li>
	 * </ul>
	 * 
	 * This is a modified version which returns FolderContentDTO, rather than UserAccessFoldersDTO
	 * 
	 * @param userID The <code>user_id</code> of the user for whom the
	 * 				 folders have to fetched
	 * @return String The required information in WDDX format
	 * @throws IOException
	 */
	public String getAccessibleWorkspaceFoldersNew(Integer userID)throws IOException;

	/**
	 * This method renames the workspaceFolder/learning design with the
	 * given <code>resourceID</code> to <code>newName</code>.
	 * <p>
	 * Currently only folders and learning 	designs are supported - all other 
	 * types return an error.
	 * 
	 * @param resourceID The <code>id</code> to be moved. May be a learning design id
	 * or a folder id.
	 * @param resourceType The resource type sent to the client in the FolderContentDTO. 
	 * @param targetFolderID The WorkspaceFolder under which it has to be moved
	 * @param userID The User who has requested this opeartion
	 * @return String The acknowledgement/error message in WDDX format for FLASH
	 * @throws IOException
	 */
	public String renameResource(Long resourceID, String resourceType, String newName, Integer userID)
		throws IOException;


	/**
	 * This method returns the workspace for the given User
	 * 
	 * @param userID The <code>userID</code> of the 
	 * @return String The required information in WDDX format
	 * @throws IOException
	 */
	public String getWorkspace(Integer userID) throws IOException;
	
	/** 
	 * Retrieves the list of organisations in which the user has the specified role.
	 * in WDDX format
	 * @param userID
	 * @param role
	 * @return
	 */
	public String getOrganisationsByUserRole(Integer userID, String role) throws IOException;
	
	/**
	 * Returns the users within the Organisation with <code>organisationID</code>
	 * and <code>role</code> in WDDX format
	 * @param organisationID
	 * @param role
	 * @return
	 */
	public String getUsersFromOrganisationByRole(Integer organisationID, String role) throws IOException;

}
