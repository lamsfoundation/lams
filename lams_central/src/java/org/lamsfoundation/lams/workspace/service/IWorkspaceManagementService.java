/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General License for more details.
 *
 * You should have received a copy of the GNU General License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.workspace.service;

import java.io.IOException;
import java.util.Vector;


import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;

/**
 * @author Manpreet Minhas
 */
public interface IWorkspaceManagementService {
    /**
     * I18n Message service. The Workspace action class needs access to the message service.
     *
     * @param messageSource
     */
    MessageService getMessageService();

    /**
     * Get the workspace folder for a particular id. Does not check the user permissions - that will be checked if you
     * try to get anything from the folder.
     */
    WorkspaceFolder getWorkspaceFolder(Integer workspaceFolderID);

    /**
     * This method returns the contents of the folder with given <code>workspaceFolderID</code> depending upon the
     * <code>mode</code>. Before it does so, it checks whether the given <code>User</code> is authorized to perform this
     * action.
     *
     * The <code>mode</code> can be either of the following
     * <ul>
     * <li>AUTHORING In which case all the Learning Designs in the given folder are returned OR</li>
     * <li>MONITORING In which case only those Learning Designs that are valid are returned</li>
     * </ul>
     *
     *
     * <p>
     * <b>Note: </b>It only returns the top level contents. To navigate to the contents of the sub-folders of the given
     * <code>WorkspaceFolder</code> we have to call this method again with there <code>workspaceFolderID</code>
     * </p>
     *
     * <p>
     * <b>For Example:</b>
     * </p>
     * <p>
     * For a folder with given tree structure<b>
     *
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
     * </pre>
     *
     * </b> This function will only retun A1, A2 and A3 and to get the contents if A1 we have to again call this method
     * with workspaceFolderID of A1.
     *
     * @param userID
     *            The <code>user_id</code> of the <code>User</code> who has requested the contents
     * @param workspaceFolder
     *            The <code>WorkspaceFolder</code> whose contents are requested
     * @param mode
     *            It can be either 1(AUTHORING) or 2(MONITORING)
     * @return A list of the FolderContentDTOs
     * @throws Exception
     */
    Vector getFolderContents(Integer userID, WorkspaceFolder workspaceFolder, Integer mode)
	    throws UserAccessDeniedException, RepositoryCheckedException;

    /**
     * This method does the same as getFolderContents() except that it doesn't return the home directory as a content of
     * the folder. This is useful to Authoring as when the user's organisation is listed, the client doesn't want the
     * home directory returned as the client already knows about the folder from the getAccessibleWorkspaceFolders()
     * call.
     *
     * @param userID
     *            The <code>user_id</code> of the <code>User</code> who has requested the contents
     * @param workspaceFolder
     *            The <code>WorkspaceFolder</code> whose contents are requested
     * @param mode
     *            It can be either 1(AUTHORING) or 2(MONITORING)
     * @return A list of the FolderContentDTOs
     * @throws Exception
     */
    Vector<FolderContentDTO> getFolderContentsExcludeHome(Integer userID, WorkspaceFolder workspaceFolder, Integer mode)
	    throws UserAccessDeniedException, RepositoryCheckedException;

    /**
     * Returns Folder Contents in JSON format. If folderID == null, returns the top level folders available to the user.
     * If folderID == -1, return only the learning designs in the root of the user's folder Otherwise returns the
     * learning designs and subfolders of the given folder. Sample output:
     */
    String getFolderContentsJSON(Integer folderID, Integer userID, boolean allowInvalidDesigns)
	    throws IOException, UserAccessDeniedException, RepositoryCheckedException;

    /**
     * Returns Folder Contents in JSON format, restricted by designType (used for Integrations) If folderID == null,
     * returns the top level folders available to the user. If folderID == -1, return only the learning designs in the
     * root of the user's folder Otherwise returns the learning designs and subfolders of the given folder. Sample
     * output:
     */
    String getFolderContentsJSON(Integer folderID, Integer userID, boolean allowInvalidDesigns, String designType)
	    throws IOException, UserAccessDeniedException, RepositoryCheckedException;

    /**
     * Returns a section of the learning designs in the root of the user's personal folder. Returns the data in in JSON
     * format. If searchString is not null, it does a case insenstive "contains" comparison on name (title). Otherwise
     * sortName and sortDate should be null, ASC or DESC and the data will be paged. Sample output:
     * {"total_rows":2,"rows":[{"learningDesignId":19,"description":"Team Based Learning: Blah Blah","name":"Blah Blah",
     * "date":"2015-07-13 10:16:35.0"},{"learningDesignId":18,"description":"Team Based Learning Design: BBBBBB",
     * "name":"TBL_BBBBB","date":"2015-07-13 10:07:41.0"}]}
     */
    public String getPagedLearningDesignsJSON(Integer userID, boolean allowInvalidDesigns, String searchString,
	    int page, int size, String sortName, String sortDate) throws IOException;

    /**
     * This method creates a new folder under the given parentFolder inside the user's default workspace.
     *
     * @param parentFolderID
     *            The <code>parent_folder_id</code> of the <code>WorkspaceFolder</code> under which the new folder has
     *            to be created
     * @param name
     *            The name of the new folder
     * @param userID
     *            The <code>user_id</code> of the <code>User</code> who owns this folder
     * @return WorkspaceFolder The <code>WorkspaceFolder</code> freshly created
     * @throws UserException
     * @throws WorkspaceFolderException
     */
    WorkspaceFolder createFolder(Integer parentFolderID, String name, Integer userID)
	    throws UserException, WorkspaceFolderException;

    /**
     * This method deletes an entry from a workspace. It may be a folder, a learning design or an arbitrary resource. It
     * works out what the resource is and then calls the other deletion methods. Currently folders, files and learning
     * designs are supported - all other types return an error.
     * <p>
     *
     * @param resourceID
     *            The <code>id</code> to be deleted. May be a learning design id or a folder id.
     * @param resourceType
     *            The resource type sent to the client in the FolderContentDTO.
     * @param userID
     *            The <code>User</code> who has requested this operation
     * @throws IOException
     */
    void deleteResource(Long resourceID, String resourceType, Integer userID) throws IOException;

    /**
     * This method copies one folder inside another folder, or a learning design to another folder. If it is a learning
     * design then it also needs the copyType parameter.
     *
     * @param resourceID
     *            The <code>WorkspaceFolder</code> or <code>LearningDesign</code> to be copied.
     * @param resourceType
     *            The resource type sent to the client in the FolderContentDTO.
     * @param copyType
     *            Is this an ordinary learning design or a runtime learning design.
     * @param targetFolderID
     *            The parent <code>WorkspaceFolder</code> under which it has to be copied
     * @param userID
     *            The <code>User</code> who has requested this opeartion
     * @throws IOException
     * @throws WorkspaceFolderException
     * @throws UserException
     * @throws LearningDesignException
     */
    void copyResource(Long resourceID, String resourceType, Integer copyType, Integer targetFolderID, Integer userID)
	    throws IOException, LearningDesignException, UserException, WorkspaceFolderException;

    /**
     * Changes the parent of given Learning Design or WorkspaceFolder to the target WorkspaceFolder
     */
    void moveResource(Long resourceID, String resourceType, Integer targetFolderID) throws WorkspaceFolderException;

    /**
     * This method returns a list of organisation workspace folders.
     *
     * The organisation folders returned are determined based on whether the user has "write" access. A user can
     * write/save his content in an organisation folder the user is a MEMBER of the organisation to which the folder
     * belongs and the user has one or all of the follwing roles (APPADMIN. ADMIN, AUTHOR, STAFF, TEACHER)
     *
     * @param userID
     *            The <code>user_id</code> of the user for whom the folders have to fetched
     * @return List of folders, in a format suitable for WDDX
     * @throws IOException
     */
    Vector getAccessibleOrganisationWorkspaceFolders(Integer userID) throws IOException;

    Vector getAccessibleOrganisationWorkspaceFolders(User user) throws IOException;

    /**
     * This method returns the root workspace folder for a particular user.
     *
     * @param userID
     *            The <code>user_id</code> of the user for whom the folders have to fetched
     * @return FolderContentDTO for the user's root workspace folder
     * @throws IOException
     */
    FolderContentDTO getUserWorkspaceFolder(Integer userID) throws IOException;

    /**
     * This method returns the workspace folder for the server.
     *
     * @param userID
     *            The <code>user_id</code> of the user for whom the folders have to fetched
     * @return FolderContentDTO for the workspace folder
     * @throws IOException
     */
    FolderContentDTO getPublicWorkspaceFolder(Integer userID) throws IOException;

    /**
     * This method renames the workspaceFolder/learning design with the given <code>resourceID</code> to
     * <code>newName</code>.
     * <p>
     * Currently only folders and learning designs are supported - all other types return an error.
     *
     * @param resourceID
     *            The <code>id</code> to be moved. May be a learning design id or a folder id.
     * @param resourceType
     *            The resource type sent to the client in the FolderContentDTO.
     * @param targetFolderID
     *            The WorkspaceFolder under which it has to be moved
     * @param userID
     *            The User who has requested this opeartion
     * @throws IOException
     * @throws WorkspaceFolderException
     * @throws UserException
     */
    void renameResource(Long resourceID, String resourceType, String newName, Integer userID)
	    throws IOException, UserException, WorkspaceFolderException;

    /**
     * This method checks whether the user is authorized to create a new folder or learning design or modify the
     * existing contents under the given WorkspaceFolder.
     *
     * @param folderID
     *            The <code>workspace_folder_id</code> of the <code>WorkspaceFolder<code>
     * 				   under which the User wants to create/copy folder, file, update learning design, etc
     * @param userID The <code>User</code> being checked
     * @return boolean A boolean value indicating whether or not the <code>User</code> is authorized
     * @throws UserException
     * @throws WorkspaceFolderException
     */
    boolean isUserAuthorizedToModifyFolderContents(Integer folderID, Integer userID)
	    throws UserException, WorkspaceFolderException;
}
