/* 
  Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt 
*/

package org.lamsfoundation.lams.contentrepository.service;

import java.io.InputStream;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.ItemExistsException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.RepositoryRuntimeException;
import org.lamsfoundation.lams.contentrepository.ValidationException;
import org.lamsfoundation.lams.contentrepository.ValueFormatException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;

/**
 * Tool access to the repository.
 * <p> 
 * Runtime exceptions will trigger the transaction to be marked for rollback.
 * Checked exceptions will NOT trigger the transaction to be marked for 
 * rollback so it is up to the calling code to trigger a rollback if needed.
 * This allows the calling code to handle the exception and continue the
 * transaction.
 */
public interface IRepositoryService {

	/** String used to define service in Spring context */
	public static final String REPOSITORY_SERVICE_ID = "repositoryService";
	/** String used to define the path to the content repository context file. */
	public static final String REPOSITORY_CONTEXT_PATH = 
		"/org/lamsfoundation/lams/contentrepository/applicationContext.xml";
	/** String used to define the path to the local datasource version of the 
	 * lams.jar context file*/
	public static final String CORE_CONTEXT_PATH = 
		"/org/lamsfoundation/lams/applicationContext.xml";
	/** String used to define the path to the local datasource version of the 
	 * lams.jar context file*/
	public static final String CORE_LOCAL_CONTEXT_PATH = 
		"/org/lamsfoundation/lams/localApplicationContext.xml";
	
    /**
     * Login, creating a new ticket for the given credentials and specified
     * workspace. If login fails, a LoginException is thrown and
     * no valid ticket is generated.  The credentials object in the ticket
     * does not contain the password.
     * 
 	 * It does not clear the password in the input credentials object.
 	 * 
     * @param credentials   The credentials of the user
     * @param workspaceName the name of a workspace.
     * @return a valid {@link ITicket} for the user to access the repository.
     * @throws LoginException Login authentication fails.
     * @throws AccessDeniedException User is not allowed to access this workspace.
     * @throws WorkspaceNotFoundException Workspace name doesn't exist.
     */
    public ITicket login(ICredentials credentials, String workspaceName)
	    throws LoginException, AccessDeniedException, WorkspaceNotFoundException;

    /**
     * Create a new workspace, with the tool identified in the creditials 
	 * as the owner.     
	 * It does not clear the password in the credentials
	 * @param credentials this user/password must already exist in the repository. Password will be checked.
	 * @param workspaceName 
	 * @throws LoginException if credentials are not authorised to add/access the new workspace.
	 * @throws ItemExistsException if the workspace already exists.
	 * @throws RepositoryCheckedException if parameters are missing.	 
     */
    public void addWorkspace(ICredentials credentials, String workspaceName)
	    throws LoginException, AccessDeniedException, ItemExistsException, RepositoryCheckedException;
 
    /**
     * Create a new repository "user" - usually a tool.
	 * 
	 * The password must be at least 6 chars.
	 * 
	 * This method will not wipe out the password in the newCredential object.
	 * 
	 * At this stage it is publically accessable - may need to move
	 * it to a private management tool if considered to insecure.
     *    
	 * @param newCredential this user/password will be added to the repository
	 * @throws RepositoryCheckedException if parameters are missing.	 
	 * @throws ItemExistsException if the credential already exists.
     */
	public void createCredentials(ICredentials newCredential) 			
		throws AccessDeniedException, RepositoryCheckedException, ItemExistsException;

    /**
	 * Update a credential. Name cannot change, so really only the password changes
	 * 
	 * The password must be at least 6 chars.
	 * 
	 * @param oldCredential the current user/password 
	 * @param newCredential the new user/password
	 * @throws LoginException if the oldCredential fails login test (e.g. wrong password)
	 * @throws RepositoryCheckedException if one of the credentials objects are missing
     */
	public void updateCredentials(ICredentials oldCredential, ICredentials newCredential)
		throws AccessDeniedException, RepositoryCheckedException;

	/** 
	 * Add a new file to the repository. This will create 
	 * a completely new entry (node) in the repository, starting
	 * with version 1. 
	 * @param ticket ticket issued on login. Identifies tool and workspace - mandatory 
	 * @param istream new file, as an input stream - mandatory
	 * @param mimeType mime type of file - optional
	 * @param versionDescription human readable comment about the version - optional
	 * @return nodeKey (uuid and version)
     * @throws AccessDeniedException if ticket doesn't allow this action
     * @throws FileException if unable to save node due to file error
     * @throws InvalidParameterException if a required parameter is missing
     * @throws RepositoryRuntimeException if any internal errors have occured 
	 */
	public abstract NodeKey addFileItem(ITicket ticket, InputStream istream, String filename,
					String mimeType, String versionDescription) 
			throws FileException, AccessDeniedException, InvalidParameterException;

	/** 
	 * Add a new package of files to the repository. If startFile
	 * is not supplied, then it is assumed to be index.html.
	 *    
	 * The directory separator character in the paths of the files in the package
	 * will be converted to "/" so that a web style path can be used to
	 * access the file.
	 * 
	 * @param ticket ticket issued on login. Identifies tool and workspace - mandatory 
	 * @param dirPath directory path containing files - mandatory
	 * @param startFile relative path of initial file - optional
	 * @param versionDescription human readable comment about the version - optional
	 * @return nodeKey (uuid and version)
     * @throws AccessDeniedException if ticket doesn't allow this action
     * @throws FileException if unable to save node due to file error
     * @throws InvalidParameterException if a required parameter is missing
     * @throws RepositoryRuntimeException if any internal errors have occured 
	 */
	public abstract NodeKey addPackageItem(ITicket ticket, String dirPath, String startFile,
				String versionDescription) 
		throws AccessDeniedException, InvalidParameterException, FileException;

    /** 
     * Update an existing file in the repository. This will create
     * a new version of this file.
	 * @param ticket ticket issued on login. Identifies tool and workspace - mandatory 
     * @param uuid unique id of the file - mandatory
     * @param istream new file, as an input stream - mandatory
     * @param versionDescription human readable comment about the version - optional
     * @param mimeType mime type of file - optional
     * @throws AccessDeniedException if ticket doesn't allow this action
     * @throws ItemNotFoundException if node with uuid cannot be found
     * @throws FileException if unable to save node due to file error
     * @throws InvalidParameterException if a required parameter is missing
     * @throws RepositoryRuntimeException if any internal errors have occured 
     */
	public NodeKey updateFileItem(ITicket ticket, Long uuid, String filename, 
	   				InputStream istream, String mimeType, 
					String versionDescription) 
	   		throws AccessDeniedException, ItemNotFoundException, 
					FileException, InvalidParameterException;

	/** 
	 * Add a new package of files to the repository. If startFile
	 * is not supplied, then it is assumed to be index.html.
	 * 
	 * The directory separator character in the paths of the files in the package
	 * will be converted to "/" so that a web style path can be used to
	 * access the file.
	 * 
	 * @param ticket ticket issued on login. Identifies tool and workspace - mandatory 
	 * @param uuid unique id of the package - mandatory
	 * @param dirPath directory path containing files - mandatory
	 * @param startFile relative path of initial file - optional
	 * @param versionDescription human readable comment about the version - optional
	 * @return nodeKey (uuid and version)
     * @throws AccessDeniedException if ticket doesn't allow this action
     * @throws ItemNotFoundException if node with uuid cannot be found
     * @throws FileException if unable to save node due to file error
     * @throws InvalidParameterException if a required parameter is missing
     * @throws RepositoryRuntimeException if any internal errors have occured 
	 */
	public abstract NodeKey updatePackageItem(ITicket ticket, Long uuid, String dirPath,
			String startFile, String versionDescription)
			throws AccessDeniedException, ItemNotFoundException, 
			FileException, InvalidParameterException ;

    /**
     * Sets the property to a value, based on the specified type.  Removes the property if the value is null.
     *
     * @param ticket Mandatory
     * @param uuid Mandatory
     * @param versionId Mandatory
     * @param name  The name of a property of this node
     * @param value The value to be assigned
     * @param type  The type of the property. See org.lamsfoundation.lams.contentrepository.PropertyType
     * for values.
     * @throws ValueFormatException if the type or format of a value
     * is incompatible with the type of the specified property or if
     * value is incompatible with (i.e. can not be converted to) type.
     */
    public void setProperty(ITicket ticket, Long uuid, Long versionId, String name, Object value, int type) 
		throws  AccessDeniedException, ItemNotFoundException, ValidationException ;

	/** 
	 * Get an item from the repository based on the UUID. This
	 * may be either a file or package node.  
	 *  
	 * @param ticket ticket issued on login. Identifies tool and workspace - mandatory 
	 * @param uuid id of the file/package - mandatory
	 * @param version desired version - if null gets latest version
	 * @return node.
	 */
	public abstract IVersionedNode getFileItem(ITicket ticket, Long uuid, Long version)
			throws AccessDeniedException, ItemNotFoundException, FileException;

	
	/** 
	 * Get an item from the repository based on the UUID and relative
	 * path. Only used to get the content from a package. The 
	 * UUID is the id of the package, and relPath is the relative
	 * path within the package. 
	 * 
	 * If the item is a package and relPath is null, return the package node.
	 * 
	 * The relPath must be specified in web format ie use a separator
	 * of "/" not "\".
	 *  
	 * @param ticket ticket issued on login. Identifies tool and workspace - mandatory 
	 * @param uuid id of the package - mandatory
	 * @param versionId desired version - if null gets latest version. This is the version
	 * of the package node, not the related file.
	 * @param relPath relative path within the package - if null, 
	 * 		returns start file.
     * @throws AccessDeniedException if ticket doesn't allow this action
     * @throws ItemNotFoundException if node with uuid cannot be found
     * @throws FileException if unable to save node due to file error
     * @throws RepositoryRuntimeException if any internal errors have occured 
	 * @return node.
	 */
	public abstract IVersionedNode getFileItem(ITicket ticket, Long uuid, Long versionId, String relPath) 
		throws AccessDeniedException, ItemNotFoundException, FileException;

	/**
	 * Return a list of all the nodes for a package. The first in the list
	 * is the package node. The others are in arbitrary order.
	 * 
	 * @param ticket
	 * @param uuid uuid of the package node
	 * @param version version of the package node
	 * @return list of all nodes for package.
	 * @throws AccessDeniedException
	 * @throws ItemNotFoundException
	 * @throws FileException
	 */
	public List getPackageNodes(ITicket ticket, Long uuid, Long version) throws AccessDeniedException,
			ItemNotFoundException, FileException;

	/** Get the history for a node. Quite intensive operation the
	 * first time it is run on a node as it has to build all the 
	 * data structures.
	 * @param ticket ticket issued on login. Identifies tool and workspace - mandatory 
	 * @return SortedSet of IVersionDetail objects, ordered by version
	 */
	public SortedSet getVersionHistory(ITicket ticket, Long uuid)
				throws ItemNotFoundException,AccessDeniedException  ;

    /** Delete the current version of a node, returning a list of the files
     * that could not be deleted properly. If it is a package node, the child 
     * nodes will be deleted. 
     * 
     * You cannot delete the child of a package node - you have to delete the
     * whole package.
     * 
     * If after deleting the version, we find that there a no other versions
     * for a node, then delete the node. 
     * 
     * A file missing from the disk is ignored, allowing nodes with lost files
     * to be deleted.
     * 
     * @throws AccessDeniedException if ticket doesn't allow this action
     * @throws ItemNotFoundException if node with uuid cannot be found
     * @throws InvalidParameterException if a required parameter is missing
     * @throws RepositoryRuntimeException if any internal errors have occured 
     * @return the list of file(paths) that could not be deleted. The db entries
     * will have been deleted but these files could not be deleted. */
	public String[] deleteVersion(ITicket ticket, Long uuid, Long version) 
		throws AccessDeniedException, InvalidParameterException, ItemNotFoundException;

    /** Delete a node and all its versions, returning a list of the files
     * that could not be deleted properly. If it is a package node, the child 
     * nodes will be deleted. 
     * 
     * A file missing from the disk is ignored, allowing nodes with lost files
     * to be deleted.
     * 
     * @throws AccessDeniedException if ticket doesn't allow this action
     * @throws ItemNotFoundException if node with uuid cannot be found
     * @throws InvalidParameterException if a required parameter is missing
     * @throws RepositoryRuntimeException if any internal errors have occured 
     * @return the list of file(paths) that could not be deleted. The db entries
     * will have been deleted but these files could not be deleted. */
	public String[] deleteNode(ITicket ticket, Long uuid) 
		throws AccessDeniedException, InvalidParameterException, ItemNotFoundException;

    /**
     * Finish using this ticket. No more updates may be used with this ticket
	 * after logout(). Allows any resources to be freed.
	 * @param ticket ticket issued on login. Identifies tool and workspace - mandatory 
     */
    public void logout(ITicket ticket) throws AccessDeniedException;
    
    /** Get a complete list of all nodes in the workspace and their
     * version histories.
     * <p>
     * Warning: Once a workspace gets a lot of nodes, this will be
     * a very very expensive call!!!!!
     * </p>
	 * @param ticket ticket issued on login. Identifies tool and workspace - mandatory 
     * @return SortedMap key Long uuid, value IVersionDetail version history 
     */
    public SortedMap getNodeList(ITicket ticket) throws AccessDeniedException ;
}
