package org.lamsfoundation.lams.workspace.service;

import java.io.IOException;

import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.workspace.exception.WorkspaceFolderException;

/**
 * @author Manpreet Minhas
 */
public interface IWorkspaceManagementService {
	
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
	 * <li> MONITORING In which case only those learning Designs that are 
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
	 * @throws IOException
	 */
	public String getFolderContents(Integer userID, Integer workspaceFolderID, Integer mode)throws IOException;
	
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

}
