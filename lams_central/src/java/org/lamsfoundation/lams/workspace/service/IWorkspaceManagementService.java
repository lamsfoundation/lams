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
	
	/**
	 * This method deletes a <code>LearningDesign</code> with given <code>learningDesignID</code>
	 * provied the <code>User</code> is authorized to do so.
	 * <p><b>Note:</b></p>
	 * <p><ul>
	 * <li>The <code>LearningDesign</code> should not be readOnly,
	 * 	   indicating that a <code>Lesson</code> has already been started
	 * </li>
	 * <li>The given <code>LearningDesign</code> should not be acting as a 
	 * 	   parent to any other existing <code>LearningDesign's</code></li>
	 * </ul></p>
	 * @param learningDesignID The <code>learning_design_id</code> of the
	 * 						   <code>LearningDesign</code> to be deleted.
	 * @param userID The <code>user_id</code> of the <code>User</code> who has
	 * 				 requested this opeartion 
	 * @return String The acknowledgement/error message in WDDX format for FLASH
	 * @throws IOException
	 */
	public String deleteLearningDesign(Long learningDesignID, Integer userID)throws IOException;
	
	/**
	 * This method copies one folder inside another folder. To be able to
	 * successfully perform this action following conditions must be met in the
	 * order they are listed.
	 * <p><ul>
	 * <li> The target <code>WorkspaceFolder</code> must exists</li>
	 * <li>The <code>User</code> with the given <code>userID</code> 
	 *     must have OWNER or MEMBERSHIP rights for that <code>WorkspaceFolder</code>
	 * 	   to be authorized to do so.</li>
	 * <ul></p>
	 * 
	 * <p><b>Note: </b> By default the copied folder has the same name as that of the 
	 * one being copied. But in case the target <code>WorkspaceFolder</code> already has 
	 * a folder with the same name, an additional "C" is appended to the name of the folder
	 * thus created. </p>
	 * 
	 * @param folderID The <code>WorkspaceFolder</code> to be copied.
	 * @param newFolderID The parent <code>WorkspaceFolder</code> under 
	 * 					  which it has to be copied
	 * @param userID The <code>User</code> who has requested this opeartion
	 * @return String The acknowledgement/error message to be sent to FLASH
	 * @throws IOException
	 */
	public String copyFolder(Integer folderID,Integer newFolderID,Integer userID)throws IOException;
	
	
	/**
	 * This method moves the given <code>WorkspaceFolder</code> with <code>currentFolderID</code>
	 * under the WorkspaceFolder with <code>targetFolderID</code>.But before it does so it checks
	 * whether the <code>User</code> is authorized to do so.
	 *  
	 * <p>
	 * <b>Note: </b> This method doesn't actually copies the content from one place to another.
	 * All it does is change the <code>parent_workspace_folder_id</code> of the currentFolder
	 * to that of the <code>targetFolder</code></p> 
	 * 
	 * @param currentFolderID The WorkspaceFolder to be moved
	 * @param targetFolderID The WorkspaceFolder under which it has to be moved
	 * @param userID The User who has requested this opeartion
	 * @return String The acknowledgement/error message to be sent to FLASH
	 * @throws IOException
	 */
	public String moveFolder(Integer currentFolderID,Integer targetFolderID,Integer userID)throws IOException;
	
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
	 * @param createDateTime The date and time this content was created
	 * @param lastModifiedDate The date and time this content was last modified 
	 * @param workspaceFolderID The container(<code>workspaceFolder</code>)which
	 * 							holds this content
	 * @param mimeType The MIME type of the file 
	 * @param path The physical location of the file from where it has to be read
	 * @return String The acknowledgement/error message in WDDX format for the 
	 * 				  FLASH client.
	 * @throws Exception
	 */
	public String createWorkspaceFolderContent(Integer contentTypeID,String name,
											   String description,Date createDateTime,
											   Date lastModifiedDate,Integer workspaceFolderID,
											   String mimeType, String path)throws Exception;
	
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
	 * This method deletes all versions of the given content (FILE/PACKAGE)
	 * fom the repository. 
	 * 
	 * @param folderContentID The content to be deleted
	 * @return String Acknowledgement/error message in WDDX format for FLASH
	 * @throws Exception
	 */
	public String deleteWorkspaceFolderContent(Long folderContentID)throws Exception;
	
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
	 * <li>RUN_SEQUENCES The folder in which user stores his lessons</li>
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
	 * This method moves a Learning Design from one workspace 
	 * folder to another.But before it does that it checks whether 
	 * the given User is authorized to do so. 
	 * 
	 * Nothing is physically moved from one folder to another. 
	 * It just changes the <code>workspace_folder_id</code> for the 
	 * given learningdesign in the <code>lams_learning_design_table</code>
	 * if the <code>User</code> is authorized to do so.
	 * 
	 * @param learningDesignID The <code>learning_design_id</code> of the
	 * 							design to be moved
	 * @param targetWorkspaceFolderID The <code>workspaceFolder</code> under
	 * 								  which it has to be moved.
	 * @param userID The <code>User</code> who is requesting this operation
	 * @return String Acknowledgement/error message in WDDX format for FLASH  
	 * @throws IOException
	 */
	public String moveLearningDesign(Long learningDesignID,Integer targetWorkspaceFolderID,Integer userID) throws IOException;
	
	/**
	 * This method renames the <code>workspaceFolder</code> with the
	 * given <code>workspaceFodlerID</code> to <code>newName</code>.
	 * But before it does that it checks if the user is authorized to
	 * do so.
	 * 
	 * @param workspaceFolderID The <code>workspaceFolder</code> to be renamed
	 * @param newName The <code>newName</code> to be assigned
	 * @param userID The <code>User</code> who requested this operation
	 * @return String Acknowledgement/error message in WDDX format for FLASH
	 * @throws IOException
	 */
	public String renameWorkspaceFolder(Integer workspaceFolderID,String newName,Integer userID)throws IOException;
	
	/**
	 * This method renames the Learning design with given <code>learningDesignID</code>
	 * to the new <code>title</code>. But before it does that it checks if the user 
	 * is authorized to do so.
	 * 
	 * @param learningDesignID The <code>learning_design_id</code> of the
	 * 							design to be renamed
	 * @param title The new title
	 * @param userID The <code>User</code> who requested this operation
	 * @return String Acknowledgement/error message in WDDX format for FLASH
	 * @throws IOException
	 */
	public String renameLearningDesign(Long learningDesignID, String title,Integer userID)throws IOException;
	
	/**
	 * This method returns the workspace for the given User
	 * 
	 * @param userID The <code>userID</code> of the 
	 * @return String The required information in WDDX format
	 * @throws IOException
	 */
	public String getWorkspace(Integer userID) throws IOException;

}
