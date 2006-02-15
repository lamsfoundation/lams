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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.ItemExistsException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceDAO;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;
import org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.dto.UpdateContentDTO;

/**
 * @author Manpreet Minhas
 */
public class WorkspaceManagementService implements IWorkspaceManagementService{
	
	protected Logger log = Logger.getLogger(WorkspaceManagementService.class.getName());

	private FlashMessage flashMessage;
	
	private static final String MSG_KEY_MOVE = "moveResource";
	private static final String MSG_KEY_COPY = "copyResource";
	private static final String MSG_KEY_RENAME = "renameResource";
	private static final String MSG_KEY_DELETE = "deleteResource";
	
	public static final Integer AUTHORING = new Integer(1);
	public static final Integer MONITORING = new Integer(2);
	
	protected IUserDAO userDAO;	
	protected ILearningDesignDAO learningDesignDAO;
	protected IWorkspaceFolderDAO workspaceFolderDAO;
	protected IWorkspaceDAO workspaceDAO;
	protected IOrganisationDAO organisationDAO;
	protected IUserOrganisationDAO userOrganisationDAO;
	
	protected IWorkspaceFolderContentDAO workspaceFolderContentDAO;	
		
	protected IAuthoringService authoringService;
	protected IRepositoryService repositoryService;
	protected IUserManagementService userMgmtService;
	protected MessageService messageService;
	
	/**
	 * i18n Message service
	 * @param messageSource
	 */
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	/**
	 * @param workspaceFolderContentDAO The workspaceFolderContentDAO to set.
	 */
	public void setWorkspaceFolderContentDAO(
			IWorkspaceFolderContentDAO workspaceFolderContentDAO) {
		this.workspaceFolderContentDAO = workspaceFolderContentDAO;
	}
	/**
	 * @param authoringService The authoringService to set.
	 */
	public void setAuthoringService(IAuthoringService authoringService) {
		this.authoringService = authoringService;
	}	
	/**
	 * @param organisationDAO The organisationDAO to set.
	 */
	public void setOrganisationDAO(IOrganisationDAO organisationDAO) {
		this.organisationDAO = organisationDAO;
	}
	/**
	 * @param workspaceDAO The workspaceDAO to set.
	 */
	public void setWorkspaceDAO(IWorkspaceDAO workspaceDAO) {
		this.workspaceDAO = workspaceDAO;
	}
	/**
	 * @param workspaceFolderDAO The workspaceFolderDAO to set.
	 */
	public void setWorkspaceFolderDAO(IWorkspaceFolderDAO workspaceFolderDAO) {
		this.workspaceFolderDAO = workspaceFolderDAO;
	}
	/**
	 * @param learningDesignDAO The learningDesignDAO to set.
	 */
	public void setLearningDesignDAO(ILearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}
	/**
	 * @param userDAO The userDAO to set.
	 */
	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	/**
	 * @param userOrganisationDAO The userOrganisationDAO to set.
	 */
	public void setUserOrganisationDAO(IUserOrganisationDAO userOrganisationDAO) {
		this.userOrganisationDAO = userOrganisationDAO;
	}
	
	public void setUserMgmtService(IUserManagementService userMgmtService) {
		this.userMgmtService = userMgmtService;
	}
	
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#deleteResource(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	public String deleteResource(Long resourceID, String resourceType, Integer userID) throws IOException {

		String errorMessage = null;

		if ( resourceID == null || resourceType == null || userID== null ) {
			errorMessage = "deleteResource(Long resourceID, String resourceType, Integer userID) requires a value for resourceID, resourceType and userID";
		} else if ( FolderContentDTO.DESIGN.equals(resourceType) ) {
			return deleteLearningDesign(resourceID, userID);
		} else if ( FolderContentDTO.FOLDER.equals(resourceType) ) {
			return deleteFolder(new Integer(resourceID.intValue()), userID);
		} else if ( FolderContentDTO.FILE.equals(resourceType) ) {
			return 	deleteWorkspaceFolderContent(resourceID);
		} else if ( FolderContentDTO.LESSON.equals(resourceType) ) {
			// TODO implement delete lesson
			errorMessage = "LAMS does not support deleting a lesson via the workspace interface. ";			
		}
		
		FlashMessage message = new FlashMessage(MSG_KEY_DELETE,
					"Cannot delete the resource: "+errorMessage,
					FlashMessage.ERROR);		
		return message.serializeMessage();
	
	}
	
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
	public String deleteFolder(Integer folderID, Integer userID)throws IOException{		
		WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(folderID);
		User user = userDAO.getUserById(userID);
		if(user!=null){
			if(!getPermissions(workspaceFolder,user).equals(WorkspaceFolder.OWNER_ACCESS)){
				flashMessage = FlashMessage.getUserNotAuthorized(MSG_KEY_DELETE,userID);
			}else{
				if(workspaceFolder!=null){
					if(isRootFolder(workspaceFolder))
						flashMessage = new FlashMessage(MSG_KEY_DELETE,
														"Cannot delete this folder as it is the Root folder.",
														FlashMessage.ERROR);
					else{
						flashMessage = deleteFolderContents(workspaceFolder, userID);
					}
				}else
					flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(MSG_KEY_DELETE,folderID);
			}
		}else
			flashMessage = FlashMessage.getNoSuchUserExists(MSG_KEY_DELETE,userID);
			
		return flashMessage.serializeMessage();
	}
	/**
	 * This method will try to delete the <code>folder</code>. If the folder is not empty
	 * it will attempt to delete all resources inside the folder. If the user does not
	 * have permission to delete that resource then a FlashMessage will be returned
	 * indicating which resource it was unable to delete.
	 * If all resources inside that <code>folder</code> are successfully deleted, then
	 * the folder will be deleted and a FlashMessage will be returned indicating it was 
	 * successfully deleted.
	 * TODO: put all flash messages in a message resource file so it can be
	 * internationalised
	 * @param folder
	 */
	private FlashMessage deleteFolderContents(WorkspaceFolder folder, Integer userID) throws IOException
	{
		boolean isDeleteSuccessful = true;
		
		Integer folderID = folder.getWorkspaceFolderId();
		
		if (!folder.isEmpty())
		{
			if (folder.hasSubFolders())
			{
				Set subFolders = folder.getChildWorkspaceFolders();
				Iterator subFolderIterator = subFolders.iterator();
				while (subFolderIterator.hasNext())
				{
					WorkspaceFolder subFolder = (WorkspaceFolder)subFolderIterator.next();
					deleteFolder(subFolder.getWorkspaceFolderId(), userID);					
				}
			}
			else
			{
				
				//delete all files within the directory
				Set folderContents = folder.getFolderContent();
				Iterator i = folderContents.iterator();
				while (i.hasNext())
				{
					WorkspaceFolderContent folderContent = (WorkspaceFolderContent)i.next();
					folderContent.setWorkspaceFolder(null);
					folder.getFolderContent().remove(folderContent);
				}
				
				//delete all learning designs within folder (if created by the user)
				Set learningDesigns = folder.getLearningDesigns();				
				Iterator j = learningDesigns.iterator();
				while (j.hasNext())
				{
					LearningDesign learningDesign = (LearningDesign)j.next();
					//have to ensure that the user has permission to delete the learning design
					//method taken from deleteLearningDesign
					//code has been duplicated because it is not desirable to return a string.
					Long learningDesignID = learningDesign.getLearningDesignId();
					if (learningDesign != null) {
						if (learningDesign.getUser().getUserId().equals(userID))
						{
							if (learningDesign.getReadOnly().booleanValue()) {
								isDeleteSuccessful = false;
								flashMessage = new FlashMessage(MSG_KEY_DELETE,
																"Cannot delete design with learning_design_id of :" + learningDesignID +
																" as it is READ ONLY.", FlashMessage.ERROR);
							}
							else
							{
								folder.getLearningDesigns().remove(learningDesign);
								learningDesign.setWorkspaceFolder(null);
							}
						}
					}
				}
				
				workspaceFolderDAO.update(folder); //this call will delete the files and learningdesigns which were removed from the collection above.
				
			}
			
		}
		if(isDeleteSuccessful) //it will only delete this folder if all the files/folder/learningDesigns are all deleted
		{
			workspaceFolderDAO.delete(folder);
			flashMessage = new FlashMessage(MSG_KEY_DELETE,"Folder deleted:" + folderID);
		}
				
		return flashMessage;
	
	}
	
	
	/**
	 * This method checks if the given <code>workspaceFolder</code> is the
	 * root folder of any <code>Organisation</code> or <code>User</code>
	 * 
	 * @param workspaceFolder The <code>workspaceFolder</code> to be checked
	 * @return boolean The boolean value indicating whether it is a 
	 * 		  root folder or not. 
	 */
	private boolean isRootFolder(WorkspaceFolder workspaceFolder){
		try{
			Workspace workspace = workspaceDAO.getWorkspaceByRootFolderID(workspaceFolder.getWorkspaceFolderId());
			if(workspace!=null)
				return true;
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Get the workspace folder for a particular id. Does not check the user permissions - that will be checked if you try to get
	 * anything from the folder.
	 */
	public WorkspaceFolder getWorkspaceFolder(Integer workspaceFolderID) {
		return workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderID);
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getFolderContentsExcludeHome(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public Vector getFolderContentsExcludeHome(Integer userID, WorkspaceFolder folder, Integer mode)throws UserAccessDeniedException, RepositoryCheckedException {
		User user = userDAO.getUserById(userID);
		return getFolderContentsInternal(user, folder, mode, "getFolderContentsExcludeHome", user.getWorkspace().getRootFolder());
	}
	 
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getFolderContents(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public Vector getFolderContents(Integer userID, WorkspaceFolder folder, Integer mode)throws UserAccessDeniedException, RepositoryCheckedException {
		User user = userDAO.getUserById(userID);
		return getFolderContentsInternal(user, folder, mode, "getFolderContents", null);
	}
	
	
	/**
	 * Get the contents of a folder. Internal method used for both getFolderContentsExcludeHome() and getFolderContents().
	 * If skipContentId is not null, then skip any contents found with this id. 
	 * @throws UserAccessDeniedException 
	 * @throws RepositoryCheckedException 
	 */
	public Vector getFolderContentsInternal(User user, WorkspaceFolder workspaceFolder, Integer mode, String methodName, WorkspaceFolder skipFolder)throws UserAccessDeniedException, RepositoryCheckedException {
		Vector contentDTO = new Vector();
		if(user!=null){
			Integer permissions = getPermissions(workspaceFolder,user);
			if(permissions!=WorkspaceFolder.NO_ACCESS){					
				getFolderContent(workspaceFolder,permissions,mode,contentDTO);
				if(workspaceFolder.hasSubFolders())
					getSubFolderDetails(workspaceFolder,user,contentDTO, skipFolder);	
				Vector repositoryContent = getContentsFromRepository(workspaceFolder.getWorkspaceFolderId(),permissions);
				if(repositoryContent!=null)
					contentDTO.addAll(repositoryContent);
			} else {
					throw new UserAccessDeniedException(user);
			}
		}else {
			throw new UserAccessDeniedException(user);
		}
		return contentDTO;
		
	}	
	private void getFolderContent(WorkspaceFolder workspaceFolder, Integer permissions, Integer mode,Vector contentDTO){
		
		List designs = null;
		
		if(AUTHORING.equals(mode))
			designs = learningDesignDAO.getAllLearningDesignsInFolder(workspaceFolder.getWorkspaceFolderId());
		else
			designs = learningDesignDAO.getAllValidLearningDesignsInFolder(workspaceFolder.getWorkspaceFolderId());
		
		getFolderContentDTO(designs,permissions,contentDTO);
	}
	/** 
	 * Get the folders in the given workspaceFolder. If skipContentId is not null, then skip any contents found with this id.
	 */ 
	private void getSubFolderDetails(WorkspaceFolder workspaceFolder,User user, Vector subFolderContent, WorkspaceFolder skipFolder){
		Integer skipFolderID = skipFolder != null ? skipFolder.getWorkspaceFolderId() : null;
		Iterator iterator = workspaceFolder.getChildWorkspaceFolders().iterator();
		while(iterator.hasNext()){
			WorkspaceFolder subFolder = (WorkspaceFolder)iterator.next();
			if ( skipFolderID==null || ! skipFolderID.equals(subFolder.getWorkspaceFolderId()) ) {
				Integer permissions = getPermissions(subFolder, user);
				if ( permissions!=WorkspaceFolder.NO_ACCESS) {
					subFolderContent.add(new FolderContentDTO(subFolder,permissions));
				}
			}
		}		
	}
	/**
	 * This method returns the permissions specific to the given
	 * <code>workspaceFolder</code> for the given user.  
	 * 
	 * @param workspaceFolder The workspaceFolder for which we need the permissions
	 * @param user The user for whom these permissions are set.
	 * @return Integer The permissions
	 */
	public Integer getPermissions(WorkspaceFolder workspaceFolder, User user){
		Integer permission = null;
		WorkspaceFolder userRootFolder = user.getWorkspace().getRootFolder();
		
		if(isUserOwner(workspaceFolder,user))
			permission = WorkspaceFolder.OWNER_ACCESS;
		else if (isSubFolder(workspaceFolder,userRootFolder))
		{			
			if(isRunSequencesFolder(workspaceFolder,user))
				permission = WorkspaceFolder.READ_ACCESS;
			else
				permission = WorkspaceFolder.OWNER_ACCESS;
		}
		else if (isParentOrganisationFolder(workspaceFolder,user))
			permission = WorkspaceFolder.MEMBERSHIP_ACCESS;
		else if(user.hasMemberAccess(workspaceFolder))
				permission = WorkspaceFolder.MEMBERSHIP_ACCESS;
		else
			permission = WorkspaceFolder.NO_ACCESS;
		
		return permission;
	}
	/** This method checks if the given workspaceFolder is a subFolder of the
	 * given rootFolder*/
	private boolean isSubFolder(WorkspaceFolder workspaceFolder,WorkspaceFolder rootFolder){
		List subFolders = workspaceFolderDAO.getWorkspaceFolderByParentFolder(rootFolder.getWorkspaceFolderId());
		Iterator iterator = subFolders.iterator();
		while(iterator.hasNext()){
			WorkspaceFolder subFolder = (WorkspaceFolder)iterator.next();
			if(subFolder.getWorkspaceFolderId()==workspaceFolder.getWorkspaceFolderId())
				return true;			
		}
		return false;
		
	}
	/**
	 * Assuming that there is ONLY ONE runSequences folder per user,
	 * which is created at the time the User is created, this method
	 * checks if the given <code>workspaceFolder</code> is the runSequences folder 
	 * for the given user.
	 * 
	 * @param workspaceFolder
	 * @param user
	 * @return
	 */
	public boolean isRunSequencesFolder(WorkspaceFolder workspaceFolder, User user){
		WorkspaceFolder runSequencesFolder = workspaceFolderDAO.getRunSequencesFolderForUser(user.getUserId());
		if(workspaceFolder.getWorkspaceFolderId()==runSequencesFolder.getWorkspaceFolderId())
			return true;
		else
			return false;
	}
	/**
	 * This method checks if the given <code>workspaceFolder</code> is the
	 * workspaceFolder of the parentOrganisation of which the user is a member.
	 *  
	 * @param workspaceFolder
	 * @param user
	 * @return
	 */
	public boolean isParentOrganisationFolder(WorkspaceFolder workspaceFolder, User user){
		WorkspaceFolder parentOrganisationFolder = user.getBaseOrganisation().getWorkspace().getRootFolder();
		if(parentOrganisationFolder.getWorkspaceFolderId()==workspaceFolder.getWorkspaceFolderId())
			return true;
		else
			return false;
	}	
	private Vector getFolderContentDTO(List designs, Integer permissions,Vector folderContent){		
		Iterator iterator = designs.iterator();
		while(iterator.hasNext()){
			LearningDesign design = (LearningDesign)iterator.next();
			folderContent.add(new FolderContentDTO(design,permissions));			
		}
		return folderContent;
		
	}
	/**
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#copyResource(Long, String, Integer, Integer, Integer)
	 */
	public String copyResource(Long resourceID, String resourceType, Integer copyType, Integer targetFolderID, Integer userID)throws IOException {
		String errorMessage = null;

		if ( resourceID == null || targetFolderID == null || resourceType == null || userID== null ) {
			errorMessage = "copyResource(Long resourceID,Integer targetFolderID, String resourceType, Integer userID) requires a value for resourceID, targetFolderID, resourceType and userID";
		} else if ( FolderContentDTO.DESIGN.equals(resourceType) ) {
			return copyLearningDesign(resourceID,targetFolderID,copyType,userID);
		} else if ( FolderContentDTO.FOLDER.equals(resourceType) ) {
			return copyFolder(new Integer(resourceID.intValue()), targetFolderID, userID);
		} else if ( FolderContentDTO.FILE.equals(resourceType) ) {
			// TODO implement delete file resource
			errorMessage = "LAMS does not support copying a file via the workspace interface. ";			
		} else if ( FolderContentDTO.LESSON.equals(resourceType) ) {
			// TODO implement delete lesson
			errorMessage = "LAMS does not support copying a lesson via the workspace interface. ";			
		}
		
		FlashMessage message = new FlashMessage(MSG_KEY_COPY,
					"Cannot copy the resource: "+errorMessage,
					FlashMessage.ERROR);		
		return message.serializeMessage();
	
	}

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
	 * @param targetFolderID The parent <code>WorkspaceFolder</code> under 
	 * 					  which it has to be copied
	 * @param userID The <code>User</code> who has requested this opeartion
	 * @return String The acknowledgement/error message to be sent to FLASH
	 * @throws IOException
	 */
	public String copyFolder(Integer folderID,Integer targetFolderID,Integer userID)throws IOException{
		try{
			if(isUserAuthorized(targetFolderID,userID)){
				WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(folderID);
				if(workspaceFolder!=null){
					WorkspaceFolder newFolder = createFolder(targetFolderID,workspaceFolder.getName(),userID);
					copyRootContent(workspaceFolder,newFolder,userID);				
					if(workspaceFolder.hasSubFolders())
						createSubFolders(workspaceFolder, newFolder,userID);
					flashMessage = new FlashMessage(MSG_KEY_COPY,newFolder.getWorkspaceFolderId());
				}else
					throw new WorkspaceFolderException();				
			}else
				flashMessage = FlashMessage.getUserNotAuthorized(MSG_KEY_COPY, userID);
		}catch(UserException ue){
			flashMessage = FlashMessage.getNoSuchUserExists(MSG_KEY_COPY,userID);
		}catch(WorkspaceFolderException we){
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(MSG_KEY_COPY,folderID);
		}
		return flashMessage.serializeMessage();
	}
	
	/**
	 * This method copies a learning design to another folder. Call the AuthoringService
	 * to do the copy.
	 * 
	 * @param folderID The <code>WorkspaceFolder</code> to be copied.
	 * @param newFolderID The parent <code>WorkspaceFolder</code> under 
	 * 					  which it has to be copied
	 * @param userID The <code>User</code> who has requested this opeartion
	 * @return String The acknowledgement/error message to be sent to FLASH
	 * @throws IOException
	 */
	public String copyLearningDesign(Long designID,Integer targetFolderID,Integer copyType,Integer userID)throws IOException{
		FlashMessage message = null;
		try { 
			LearningDesign ld = authoringService.copyLearningDesign(designID, 
					copyType != null ? copyType : new Integer(LearningDesign.COPY_TYPE_NONE), 
							userID, targetFolderID, false);
			message = new FlashMessage(MSG_KEY_COPY,ld.getLearningDesignId());
			
		} catch ( Exception e ) {
			log.error("copyLearningDesign() unable to copy learning design due to an error. "
				+ "designID="+designID
				+ "copyType="+copyType
				+ "targetFolderID="+targetFolderID
				+ "userID="+userID ,e);

			message = new FlashMessage(MSG_KEY_COPY, "Unable to copy learning design due to an error "+e.getMessage(), FlashMessage.ERROR);

		}

		return message.serializeMessage();
	}

	/**
	 * This method checks whether the user is authorized to create 
	 * a new folder under the given WorkspaceFolder.
	 * 
	 * @param folderID The <code>workspace_folder_id</code> of the <code>WorkspaceFolder<code>
	 * 				   under which the User wants to create/copy folder
	 * @param userID The <code>User</code> being checked
	 * @return boolean A boolean value indicating whether or not the <code>User</code> is authorized
	 * @throws UserException
	 * @throws WorkspaceFolderException
	 */
	private boolean isUserAuthorized(Integer folderID, Integer userID)throws UserException, WorkspaceFolderException{
		boolean authorized = false;
		User user = userDAO.getUserById(userID);
		if(user!=null){
			WorkspaceFolder targetParent = workspaceFolderDAO.getWorkspaceFolderByID(folderID);
			if(targetParent!=null){
				Integer permissions = getPermissions(targetParent,user);
				if(!permissions.equals(WorkspaceFolder.NO_ACCESS)&&
						!permissions.equals(WorkspaceFolder.READ_ACCESS))
					authorized = true;
			}else
				throw new WorkspaceFolderException();
		}else
			throw new UserException();
		
		return authorized;
	}
	private Hashtable createCopyFolderPacket(Long newResourceID, Integer targetFolderID){
		Hashtable packet = new Hashtable();
		packet.put("newResourceID", newResourceID);
		packet.put("targetFolderID",targetFolderID);
		return packet;
	}	
	public boolean isUserOwner(WorkspaceFolder workspaceFolder, User user){				
		List folders = workspaceFolderDAO.getWorkspaceFolderByUser(user.getUserId());
		if(folders!=null && folders.size()!=0){
			Iterator iterator =folders.iterator();
			while(iterator.hasNext()){
				WorkspaceFolder folder = (WorkspaceFolder)iterator.next();
				if(folder.getWorkspaceFolderId()==workspaceFolder.getWorkspaceFolderId())
					return true;
			}
		}
		return false;
	}
	public void copyRootContent(WorkspaceFolder workspaceFolder,WorkspaceFolder targetWorkspaceFolder, Integer userID)throws UserException{
		User user = userDAO.getUserById(userID);
		if(user==null)
			throw new UserException("No such user with a userID of " + userID + "exists");
		
		List designs = learningDesignDAO.getAllLearningDesignsInFolder(workspaceFolder.getWorkspaceFolderId());
		if(designs!=null && designs.size()!=0){
			Iterator iterator = designs.iterator();
			while(iterator.hasNext()){
				LearningDesign design = (LearningDesign)iterator.next();
				authoringService.copyLearningDesign(design,
													new Integer(LearningDesign.COPY_TYPE_NONE),
													user,targetWorkspaceFolder, false);
			}
		}
	}
	public WorkspaceFolder createFolder(Integer parentFolderID, String name, Integer userID) throws UserException,WorkspaceFolderException{
		WorkspaceFolder parentFolder = workspaceFolderDAO.getWorkspaceFolderByID(parentFolderID);		
		User user =null;
		Workspace workspace =null;		
		
		if(parentFolder!=null){			
			
			boolean nameExists = true;
			while(nameExists){
				nameExists = ifNameExists(parentFolder,name);
				if(nameExists)
					name =name + "C";
				else
					break;
			}
			
			user =  userDAO.getUserById(userID);
			if(user!=null){
				workspace = user.getWorkspace();
				WorkspaceFolder workspaceFolder = new WorkspaceFolder(name,
																	  workspace.getWorkspaceId(),
																	  parentFolder,
																	  userID,
																	  new Date(),
																	  new Date(),
																	  WorkspaceFolder.NORMAL);
				workspaceFolderDAO.insert(workspaceFolder);
				return workspaceFolder;	
			}else
				throw new UserException("No such user with userID of " + userID  + "exists");
			
		}else
			throw new WorkspaceFolderException("No such workspaceFolder with a workspace_folder_id of " + parentFolderID +" exists");
	}
	/**
	 * TODO For now assuming that the folder to be created would be of type NORMAL.
	 * But the type has to be passed in the near future indicating what kind of 
	 * folder should be created (NORMAL/RUN SEQUENCES)
	 * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#createFolder(java.lang.Integer, java.lang.String, java.lang.Integer)
	 */
	public String createFolderForFlash(Integer parentFolderID, String name, Integer userID)throws IOException{		
		try{
			WorkspaceFolder newFolder = createFolder(parentFolderID,name,userID);
			Hashtable table = new Hashtable();
			table.put("folderID",newFolder.getWorkspaceFolderId());
			table.put("name",newFolder.getName());			
			flashMessage = new FlashMessage("createFolderForFlash",table);
			
		}catch(WorkspaceFolderException we){
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists("createFolderForFlash",parentFolderID);
		}catch(UserException ue){
			flashMessage = FlashMessage.getNoSuchUserExists("createFolderForFlash",userID);
		}		
		return flashMessage.serializeMessage();
	}
	private boolean ifNameExists(WorkspaceFolder targetFolder,String folderName){
		List folders = workspaceFolderDAO.getWorkspaceFolderByParentFolder(targetFolder.getWorkspaceFolderId());
		if(folders!=null && folders.size()!=0){
			Iterator iterator = folders.iterator();
			while(iterator.hasNext()){
				WorkspaceFolder folder = (WorkspaceFolder)iterator.next();
				if(folder.getName().equalsIgnoreCase(folderName))
					return true;
			}
		}
		return false;
	}
	public void createSubFolders(WorkspaceFolder workspaceFolder, WorkspaceFolder newFolder, Integer userID) throws UserException, WorkspaceFolderException{
		Iterator subFoldersIterator = workspaceFolder.getChildWorkspaceFolders().iterator();
		while(subFoldersIterator.hasNext()){
			WorkspaceFolder subFolder = (WorkspaceFolder)subFoldersIterator.next();
			WorkspaceFolder newSubFolder = createFolder(newFolder.getWorkspaceFolderId(),subFolder.getName(),userID);
			copyRootContent(subFolder,newSubFolder,userID);
			if(subFolder.hasSubFolders())
				createSubFolders(subFolder,newSubFolder,userID);
		}
	}
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
	 * 
	 * TODO Deleting a LearningDesign would mean deleting all its corresponding
	 * activities, transitions and the content related to such activities.
	 * Deletion of content has to be yet taken care of. Since Tools manage there
	 * own content.Just need to cross-check this once tools are functional
     *
	 * @param learningDesignID The <code>learning_design_id</code> of the
	 * 						   <code>LearningDesign</code> to be deleted.
	 * @param userID The <code>user_id</code> of the <code>User</code> who has
	 * 				 requested this opeartion 
	 * @return String The acknowledgement/error message in WDDX format for FLASH
	 * @throws IOException
	 */
	public String deleteLearningDesign(Long learningDesignID, Integer userID)throws IOException{
		User user = userDAO.getUserById(userID);
		if(user!=null){
			LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignID);
		
			if(learningDesign!=null){
				if(learningDesign.getUser().getUserId().equals(user.getUserId())){
					if(learningDesign.getReadOnly().booleanValue()){
						flashMessage = new FlashMessage(MSG_KEY_DELETE,
														"Cannot delete design with learning_design_id of:" + learningDesignID +
														" as it is READ ONLY.",
														FlashMessage.ERROR);
					}else{
						learningDesignDAO.delete(learningDesign);
						flashMessage = new FlashMessage(MSG_KEY_DELETE,"Learning Design deleted: "+ learningDesignID);
					}
				}else
					flashMessage = FlashMessage.getUserNotAuthorized(MSG_KEY_DELETE,userID);
		}else
			flashMessage = FlashMessage.getNoSuchLearningDesignExists(MSG_KEY_DELETE,learningDesignID);
		}else
			flashMessage = FlashMessage.getNoSuchUserExists(MSG_KEY_DELETE,userID);
		
		return flashMessage.serializeMessage();
	}
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
	public String moveResource(Long resourceID,Integer targetFolderID, String resourceType, Integer userID)throws IOException{
		String errorMessage = null;

		if ( resourceID == null || targetFolderID == null || resourceType == null || userID== null ) {
			errorMessage = "moveResource(Long resourceID,Integer targetFolderID, String resourceType, Integer userID) requires a value for resourceID, targetFolderID, resourceType and userID";
		} else if ( FolderContentDTO.DESIGN.equals(resourceType) ) {
			return moveLearningDesign(resourceID, targetFolderID, userID);
		} else if ( FolderContentDTO.FOLDER.equals(resourceType) ) {
			return moveFolder(new Integer(resourceID.intValue()), targetFolderID, userID);
		} else if ( FolderContentDTO.FILE.equals(resourceType) ) {
			// TODO implement delete file resource
			errorMessage = "LAMS does not support moving a file via the workspace interface. ";			
		} else if ( FolderContentDTO.LESSON.equals(resourceType) ) {
			// TODO implement delete lesson
			errorMessage = "LAMS does not support moving a lesson via the workspace interface. ";			
		}
		
		FlashMessage message = new FlashMessage(MSG_KEY_MOVE,
					"Cannot move the resource: "+errorMessage,
					FlashMessage.ERROR);		
		return message.serializeMessage();
	
	}

	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#moveFolder(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public String moveFolder(Integer currentFolderID,Integer targetFolderID,Integer userID)throws IOException{
		try{
			if(isUserAuthorized(targetFolderID,userID)){
				WorkspaceFolder currentFolder = workspaceFolderDAO.getWorkspaceFolderByID(currentFolderID);
				if(currentFolder!=null){
					WorkspaceFolder targetFolder = workspaceFolderDAO.getWorkspaceFolderByID(targetFolderID);
					currentFolder.setParentWorkspaceFolder(targetFolder);
					workspaceFolderDAO.update(currentFolder);
					flashMessage = new FlashMessage(MSG_KEY_MOVE, targetFolderID);
				}else
					throw new WorkspaceFolderException();
			}else
				flashMessage = FlashMessage.getUserNotAuthorized(MSG_KEY_MOVE, userID);			
		}catch(UserException ue){
			flashMessage = FlashMessage.getNoSuchUserExists(MSG_KEY_MOVE, userID);
		}catch(WorkspaceFolderException we){
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(MSG_KEY_MOVE,targetFolderID);			
		}
		return flashMessage.serializeMessage();
	}
	
	/** Set up the content repository - create the workspace and the credentials. */
	  private void configureContentRepository(ICredentials cred) throws RepositoryCheckedException {
			try {
				repositoryService.createCredentials(cred);
				repositoryService.addWorkspace(cred,IWorkspaceManagementService.REPOSITORY_WORKSPACE);
			} catch (ItemExistsException ie) {
			    log.warn("Tried to configure repository but it "
			    		+" appears to be already configured."
			    		+"Workspace name "+IWorkspaceManagementService.REPOSITORY_WORKSPACE
			    		+". Exception thrown by repository being ignored. ", ie);
			} catch (RepositoryCheckedException e) {
			    log.error("Error occured while trying to configure repository."
			    		+"Workspace name "+IWorkspaceManagementService.REPOSITORY_WORKSPACE
						+" Unable to recover from error: "+e.getMessage(), e);
				throw e;
			}
	    }

	/**
	 * This method verifies the credentials of the Workspace Manager
	 * and gives him the Ticket to login and access the Content Repository.
	 * A valid ticket is needed in order to access the content from the repository.
	 * This method would be called evertime the user(Workspace Manager) receives 
	 * a request to get the contents of the Folder or to add/update a file into
	 * the <code>WorkspaceFodler</code> (Repository).
	 * 
	 * If the workspace/credential hasn't been set up, then it will be set up automatically.
	 *  
	 * @return ITicket The ticket for repostory access
	 */
	private ITicket getRepositoryLoginTicket(){
		repositoryService = RepositoryProxy.getLocalRepositoryService();
		ICredentials credentials = new SimpleCredentials(IWorkspaceManagementService.REPOSITORY_USERNAME,
				 										 IWorkspaceManagementService.REPOSITORY_PASSWORD.toCharArray());		
		ITicket ticket = null;
		
		try{
			try {
				ticket = repositoryService.login(credentials,IWorkspaceManagementService.REPOSITORY_WORKSPACE);
			} catch(WorkspaceNotFoundException we){
				log.error("Content Repository workspace "+IWorkspaceManagementService.REPOSITORY_WORKSPACE
				        +" not configured. Attempting to configure now.");
				configureContentRepository(credentials);
				ticket = repositoryService.login(credentials,IWorkspaceManagementService.REPOSITORY_WORKSPACE);
			}
		} catch ( RepositoryCheckedException e ) {
			log.error("Unable to get ticket for workspace "+IWorkspaceManagementService.REPOSITORY_WORKSPACE,e);
			return null;
		}
		return ticket;
	}
	
	/**
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#createWorkspaceFolderContent(java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	public String createWorkspaceFolderContent(Integer contentTypeID,String name,String description,Integer workspaceFolderID, String mimeType, String path) throws Exception{
		// TODO add some validation so that a non-unique name doesn't result in an index violation 
		// bit hard for the user to understand.
		
		WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderID);
		if(workspaceFolder!=null){
			if (!contentAlreadyExists(workspaceFolder, name, mimeType))
			{
				WorkspaceFolderContent workspaceFolderContent = new WorkspaceFolderContent(contentTypeID,name,description,new Date(),new Date(),mimeType,workspaceFolder);
				workspaceFolderContentDAO.insert(workspaceFolderContent);
			
		
	
				try{
				InputStream stream = new FileInputStream(path);
				NodeKey nodeKey = addFileToRepository(stream,name,mimeType);
				workspaceFolderContent.setUuid(nodeKey.getUuid());
				workspaceFolderContent.setVersionID(nodeKey.getVersion());
				workspaceFolderContentDAO.update(workspaceFolderContent);
				
				UpdateContentDTO contentDTO = new UpdateContentDTO(nodeKey.getUuid(), nodeKey.getVersion(),workspaceFolderContent.getFolderContentID());
				flashMessage = new FlashMessage("createWorkspaceFolderContent",contentDTO);
				
				}catch(AccessDeniedException ae){
				flashMessage = new FlashMessage("createWorkspaceFolderContent",
								"Exception occured while creating workspaceFolderContent: "+ ae.getMessage(),
								FlashMessage.CRITICAL_ERROR);				
				}catch(FileException fe){
				flashMessage = new FlashMessage("createWorkspaceFolderContent",
								"Exception occured while creating workspaceFolderContent: "+ fe.getMessage(),
								FlashMessage.CRITICAL_ERROR);
				
				}catch(InvalidParameterException ip){
				flashMessage = new FlashMessage("createWorkspaceFolderContent",
								"Exception occured while creating workspaceFolderContent: "+ ip.getMessage(),
								FlashMessage.CRITICAL_ERROR);
				}
			}
			else
			{
				flashMessage = new FlashMessage("createWorkspaceFolderContent",
						"The resource " + name + " already exists in the repository: ", 
						FlashMessage.ERROR);
			}
		}
		else
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists("createWorkspaceFolderContent",workspaceFolderID);
		
		return flashMessage.serializeMessage();
	}
	/**
	 * Checks whether the content already exists. It will just check the name and mime type
	 * of the content that is to be created.
	 * If there exists a content with the same mime type and name, then it will return true,
	 * otherwise return false.
	 * @param workspaceFolder
	 * @param name
	 * @param mimeType
	 * @return
	 */
	private boolean contentAlreadyExists(WorkspaceFolder workspaceFolder, String name, String mimeType)
	{
		Set contentsInFolder = workspaceFolder.getFolderContent();
		Iterator i = contentsInFolder.iterator();
		while(i.hasNext())
		{
			WorkspaceFolderContent existingFolderContent = (WorkspaceFolderContent)i.next();
			if (existingFolderContent.getMimeType().equalsIgnoreCase(mimeType) && existingFolderContent.getName().equalsIgnoreCase(name))
			{
				log.error("Content already exists in the db");
				return true;
			}
		}
		return false; 
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#updateWorkspaceFolderContent(java.lang.Long, java.io.InputStream)
	 */
	public String updateWorkspaceFolderContent(Long folderContentID,String path)throws Exception{
		InputStream stream = new FileInputStream(path);
		WorkspaceFolderContent workspaceFolderContent = workspaceFolderContentDAO.getWorkspaceFolderContentByID(folderContentID);
		if(workspaceFolderContent!=null){
			NodeKey nodeKey = updateFileInRepository(workspaceFolderContent,stream);
			UpdateContentDTO contentDTO = new UpdateContentDTO(nodeKey.getUuid(), nodeKey.getVersion(),folderContentID);
			flashMessage = new FlashMessage("updateWorkspaceFolderContent",contentDTO);
		}else
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderContentExsists("updateWorkspaceFolderContent",folderContentID);
		return flashMessage.serializeMessage();
	}
	
	/**
	 * This method is called everytime a new content has to be
	 * added to the repository. In order to do so first of all 
	 * a valid ticket is obtained from the Repository hence
	 * authenticating the user(WorkspaceManager) and then 
	 * the corresponding file is added to the repository.
	 *  
	 * @param stream The <code>InputStream</code> representing the data to be added
	 * @param fileName The name of the file being added
	 * @param mimeType The MIME type of the file (eg. TXT, DOC, GIF etc)
	 * @return NodeKey Represents the two part key - UUID and Version.
	 * @throws AccessDeniedException
	 * @throws FileException
	 * @throws InvalidParameterException
	 */
	private NodeKey addFileToRepository(InputStream stream, String fileName, String mimeType)throws AccessDeniedException,
																							FileException,InvalidParameterException{
		ITicket ticket = getRepositoryLoginTicket();
		NodeKey nodeKey = repositoryService.addFileItem(ticket,stream,fileName,mimeType,null);
		return nodeKey;
	}
	/**
	 * This method is called everytime some content has to be
	 * updated into the repository. In order to do so first of all 
	 * a valid ticket is obtained from the Repository hence
	 * authenticating the user(WorkspaceManager) and then 
	 * the corresponding file is updated to the repository.
	 * 
	 * @param workspaceFolderContent The content to be updated
	 * @param stream stream The <code>InputStream</code> representing the data to be updated
	 * @return NodeKey Represents the two part key - UUID and Version.
	 * @throws Exception
	 */
	private NodeKey updateFileInRepository(WorkspaceFolderContent workspaceFolderContent,
									   InputStream stream)throws Exception{		
		ITicket ticket = getRepositoryLoginTicket();
		NodeKey nodeKey = repositoryService.updateFileItem(ticket,workspaceFolderContent.getUuid(),
														   workspaceFolderContent.getName(),
														   stream,workspaceFolderContent.getMimeType(),null);
		workspaceFolderContent.setUuid(nodeKey.getUuid());
		workspaceFolderContent.setVersionID(nodeKey.getVersion());
		workspaceFolderContentDAO.update(workspaceFolderContent);
		return nodeKey;
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#deleteContentWithVersion(java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	public String deleteContentWithVersion(Long uuid, Long versionToBeDeleted,Long folderContentID)throws Exception{
		WorkspaceFolderContent workspaceFolderContent = workspaceFolderContentDAO.getWorkspaceFolderContentByID(folderContentID);
		if (workspaceFolderContent != null)
		{
			Long databaseVersion = workspaceFolderContent.getVersionID();
			
			ITicket ticket = getRepositoryLoginTicket();
			String files[]=null;
			try{
				files = repositoryService.deleteVersion(ticket,uuid,versionToBeDeleted);
			}catch(ItemNotFoundException ie){
				flashMessage = new FlashMessage("deleteContentWithVersion",
												"No such content with versionID of " + versionToBeDeleted + " found in repository",
												FlashMessage.ERROR);
			   return flashMessage.serializeMessage();
			}
		
		
			/*
			 * ItemNotFoundException exception will be thrown if the version deleted
			 * above was the only version available in the repository. If that is the
			 * case the corresponding record from the database should also be deleted
			 * 
			 * If ItemNotFoundException is not thrown that means there are some other
			 * versions of the content that are available, and it returns the latest 
			 * version.
			 * 
			 * If databaseVersion is same as versionToBeDeleted we update the database with
			 * the next available latest version , IF NOT no changes are made to the
			 * database.
			 */
		
			try{			
				IVersionedNode latestAvailableNode = repositoryService.getFileItem(ticket,uuid,null);
				Long latestAvailableVersion = latestAvailableNode.getVersion();
				if(databaseVersion.equals(versionToBeDeleted)){
					workspaceFolderContent.setVersionID(latestAvailableVersion);
					workspaceFolderContentDAO.update(workspaceFolderContent);
				}
				flashMessage = new FlashMessage("deleteContentWithVersion","Content Successfully deleted");			
			}catch(ItemNotFoundException ie){
				workspaceFolderContentDAO.delete(workspaceFolderContent);
				flashMessage = new FlashMessage("deleteContentWithVersion","Content Successfully deleted");
			}
		}
		else
		{
			flashMessage = new FlashMessage("deleteContentWithVersion","Content Successfully deleted");
		}
		return  flashMessage.serializeMessage();		
	}
	
	/**
	 * This method deletes all versions of the given content (FILE/PACKAGE)
	 * fom the repository. 
	 * 
	 * @param folderContentID The content to be deleted
	 * @return String Acknowledgement/error message in WDDX format for FLASH
	 * @throws Exception
	 */
	public String deleteWorkspaceFolderContent(Long folderContentID) throws IOException {
		WorkspaceFolderContent workspaceFolderContent = workspaceFolderContentDAO.getWorkspaceFolderContentByID(folderContentID);
		if(workspaceFolderContent!=null){
			workspaceFolderContentDAO.delete(workspaceFolderContent);
			flashMessage = new FlashMessage(MSG_KEY_DELETE,"Content deleted");
		}else
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderContentExsists(MSG_KEY_DELETE,folderContentID);
		
		return flashMessage.serializeMessage();
		
	}
	/**
	 * TODO
	 * This method returns the contents of the given folder from the
	 * repository. As of now I am assuming that a folder contains only
	 * FILES and not PACKAGES. This method would be modified in the near
	 * future to return a list of PACKAGES contained as well.
	 *  
	 * For every file contained within the given <code>WorkspaceFolder</code> 
	 * this method also returns a list of all its availabe versions.
	 * 
	 * @param workspaceFolderID The <code>WorkspaceFolder</code> whose contents have been
	 * 							requested from the Repositor
	 * @param permissions The permissions on this WorkspaceFolder and hence all its contents
	 * @return Vector A collection of required information.
	 * @throws AccessDeniedException 
	 * @throws RepositoryCheckedException 
	 * @throws Exception
	 */
	private Vector getContentsFromRepository(Integer workspaceFolderID, Integer permissions) throws RepositoryCheckedException{
		List content = workspaceFolderContentDAO.getContentByWorkspaceFolder(new Long(workspaceFolderID.longValue()));
		if(content.size()==0)
			return null;
		else{
			ITicket ticket = getRepositoryLoginTicket();
			Vector repositoryContent = new Vector();
			Iterator contentIterator = content.iterator();
			while(contentIterator.hasNext()){
				WorkspaceFolderContent workspaceFolderContent = (WorkspaceFolderContent)contentIterator.next();
				SortedSet set = repositoryService.getVersionHistory(ticket,workspaceFolderContent.getUuid());				
				repositoryContent.add(new FolderContentDTO(permissions, workspaceFolderContent, set));
			}
			return repositoryContent;
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getAccessibleOrganisationWorkspaceFolders(java.lang.Integer)
	 */
	public Vector getAccessibleOrganisationWorkspaceFolders(Integer userID)	throws IOException {
		User user = userDAO.getUserById(userID);
		Vector folders = new Vector();
		
		if (user != null) {
			// Get a list of organisations of which the given user is a member
			List userMemberships = userOrganisationDAO.getUserOrganisationsByUser(user);
			if (userMemberships != null) {
				Iterator memberships = userMemberships.iterator();
				while (memberships.hasNext()) {
					UserOrganisation member = (UserOrganisation) memberships.next();
					// Get a list of roles that the user has in this organisation
					Set roles = member.getUserOrganisationRoles();
					
					/*Check if the user has write access, which is available
					 * only if the user has an AUTHOR, TEACHER or STAFF role. If
					 * he has acess add that folder to the list.
					 */
					if (hasWriteAccess(roles)) {
						WorkspaceFolder orgFolder = member.getOrganisation().getWorkspace().getRootFolder();
						folders.add(new FolderContentDTO(orgFolder,getPermissions(orgFolder,user)));
					}
				}
			} else {
				log.warn("getAccessibleWorkspaceFolders: Trying to get user memberships for user "+userID+". User doesn't belong to any organisations. Returning no folders.");
			}
		} else {
			log.warn("getAccessibleWorkspaceFolders: User "+userID+" does not exist. Returning no folders.");
		}
		
		return folders;
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getUserWorkspaceFolder(java.lang.Integer)
	 */
	public FolderContentDTO getUserWorkspaceFolder(Integer userID)	throws IOException {
		User user = userDAO.getUserById(userID);
		
		if (user != null) {
			//add the user's own folder to the list
			WorkspaceFolder privateFolder = user.getWorkspace().getRootFolder();
			Integer permissions = getPermissions(privateFolder,user);
			return new FolderContentDTO(privateFolder, permissions);
			
		} else {
			log.warn("getAccessibleWorkspaceFolders: User "+userID+" does not exist. Returning no folders.");
		}
		
		return null;
	}

	/**
	 * This a utility method that checks whether user has write access. He can
	 * save his contents to a folder only if he is an AUTHOR,TEACHER or STAFF
	 * 
	 * @param roles
	 * 			  Set of roles that the user has
	 * @return boolean
	 * 			     A boolean value indicating whether the user has "write"
	 * 				access or not.
	 */
	private boolean hasWriteAccess(Set roles) {
		boolean access = false;
		Iterator roleIterator = roles.iterator();
		while (roleIterator.hasNext()) {
			UserOrganisationRole userOrganisationRole = (UserOrganisationRole) roleIterator.next();
			Role role = userOrganisationRole.getRole();
			if (role.isAuthor() || role.isStaff() || role.isTeacher())
				access = true;
		}
		return access;
	}	
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
	public String moveLearningDesign(Long learningDesignID,
									 Integer targetWorkspaceFolderID,
									 Integer userID) throws IOException {
		try{
			if(isUserAuthorized(targetWorkspaceFolderID,userID)){
				LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignID);
				if (learningDesign != null) {
					WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(targetWorkspaceFolderID);
					if(workspaceFolder != null){
						learningDesign.setWorkspaceFolder(workspaceFolder);
						learningDesignDAO.update(learningDesign);
						flashMessage = new FlashMessage(MSG_KEY_MOVE, targetWorkspaceFolderID);
					}else
						flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(MSG_KEY_MOVE,targetWorkspaceFolderID); 
			   }else
			   		flashMessage = FlashMessage.getNoSuchLearningDesignExists(MSG_KEY_MOVE, learningDesignID);
			}else
				flashMessage = FlashMessage.getUserNotAuthorized(MSG_KEY_MOVE,userID);
		}catch(UserException ue){
			flashMessage = FlashMessage.getNoSuchUserExists(MSG_KEY_MOVE, userID);
		}catch(WorkspaceFolderException we){
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(MSG_KEY_MOVE,targetWorkspaceFolderID);
		}
		return flashMessage.serializeMessage();
	}
	/**
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#renameResource(Long, String, String, Integer)
	 */
	public String renameResource(Long resourceID, String resourceType, String newName, Integer userID)throws IOException{
		String errorMessage = null;

		if ( resourceID == null || newName == null || resourceType == null || userID== null ) {
			errorMessage = "renameResource(Long resourceID,Integer newName, String resourceType, Integer userID) requires a value for resourceID, targetFolderID, resourceType and userID";
		} else if ( FolderContentDTO.DESIGN.equals(resourceType) ) {
			return renameLearningDesign(resourceID, newName, userID);
		} else if ( FolderContentDTO.FOLDER.equals(resourceType) ) {
			return renameWorkspaceFolder(new Integer(resourceID.intValue()), newName, userID);
		} else if ( FolderContentDTO.FILE.equals(resourceType) ) {
			// TODO implement delete file resource
			errorMessage = "LAMS does not support renaming a file via the workspace interface. ";			
		} else if ( FolderContentDTO.LESSON.equals(resourceType) ) {
			// TODO implement delete lesson
			errorMessage = "LAMS does not support renaming a lesson via the workspace interface. ";			
		}
		
		FlashMessage message = new FlashMessage(MSG_KEY_RENAME,
					"Cannot rename the resource: "+errorMessage,
					FlashMessage.ERROR);		
		return message.serializeMessage();
	
	}
	/**
	 * This method renames the <code>workspaceFolder</code> with the
	 * given <code>workspaceFolderID</code> to <code>newName</code>.
	 * But before it does that it checks if the user is authorized to
	 * do so.
	 * 
	 * @param workspaceFolderID The <code>workspaceFolder</code> to be renamed
	 * @param newName The <code>newName</code> to be assigned
	 * @param userID The <code>User</code> who requested this operation
	 * @return String Acknowledgement/error message in WDDX format for FLASH
	 * @throws IOException
	 */
	public String renameWorkspaceFolder(Integer workspaceFolderID,String newName,Integer userID)throws IOException{
		try{
			WorkspaceFolder folder = workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderID);
			if(folder!=null){
				WorkspaceFolder parent = folder.getParentWorkspaceFolder();
				if(parent!=null){
					if(isUserAuthorized(workspaceFolderID,userID)){
						if(!ifNameExists(parent,newName)){
							folder.setName(newName);
							workspaceFolderDAO.update(folder);
							flashMessage = new FlashMessage(MSG_KEY_RENAME,newName);
						}else
							flashMessage = new FlashMessage(MSG_KEY_RENAME,
															"A folder with given name '" + newName + "' already exists.",
															FlashMessage.ERROR);
					}else
						flashMessage = FlashMessage.getUserNotAuthorized(MSG_KEY_RENAME,userID);
				}else
					flashMessage = new FlashMessage(MSG_KEY_RENAME,
													"Cannot rename the ROOT folder",
													FlashMessage.ERROR);
			}else
				flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(MSG_KEY_RENAME,workspaceFolderID);
		}catch(UserException ue){
			flashMessage = FlashMessage.getNoSuchUserExists(MSG_KEY_RENAME, userID);
		}catch(WorkspaceFolderException we){
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(MSG_KEY_RENAME,workspaceFolderID);
		}
		return flashMessage.serializeMessage();
	}
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
	public String renameLearningDesign(Long learningDesignID, String title,Integer userID)throws IOException {
		LearningDesign design = learningDesignDAO.getLearningDesignById(learningDesignID);	
		Integer folderID = null;
		try{
			if(design!=null){
				folderID =  design.getWorkspaceFolder().getWorkspaceFolderId();
				if(isUserAuthorized(folderID,userID)){
					design.setTitle(title);
					learningDesignDAO.update(design);
					flashMessage = new FlashMessage(MSG_KEY_RENAME,title);
				}else
					flashMessage = FlashMessage.getUserNotAuthorized(MSG_KEY_RENAME,userID);
			}else
				flashMessage = FlashMessage.getNoSuchLearningDesignExists(MSG_KEY_RENAME,learningDesignID);			
		}catch(UserException ue){
			flashMessage = FlashMessage.getNoSuchUserExists(MSG_KEY_RENAME, userID);
		}catch(WorkspaceFolderException we){
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(MSG_KEY_RENAME,folderID);
		}
		return flashMessage.serializeMessage();
	}
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getWorkspace(java.lang.Integer)
	 */
	public String getWorkspace(Integer userID) throws IOException {
		User user = userDAO.getUserById(userID);
		if (user != null) {
			Workspace workspace = user.getWorkspace();
			flashMessage = new FlashMessage("getWorkspace", workspace.getWorkspaceDTO());
		} else
			flashMessage = FlashMessage.getNoSuchUserExists("getWorkspace",userID);
		return flashMessage.serializeMessage();
	}
	
	/**
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getOrganisationsByUserRole(Integer, String)
	 */
	public String getOrganisationsByUserRole(Integer userID, String role) throws IOException
	{
		User user = userDAO.getUserById(userID);
		Vector organisations = new Vector();
		if (user!=null) {
			
			Iterator iterator = userMgmtService.getOrganisationsForUserByRole(user, role).iterator();
			
			while (iterator.hasNext()) {
				Organisation organisation = (Organisation) iterator.next();
				organisations.add(organisation.getOrganisationDTO());
			}
			flashMessage = new FlashMessage(
					"getOrganisationsByUserRole", organisations);
		} else
			flashMessage = FlashMessage.getNoSuchUserExists(
					"getOrganisationsByUserRole", userID);

		return flashMessage.serializeMessage();
		
	}
	
	/** 
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getUsersFromOrganisationByRole(Integer, String)
	 */
	public String getUsersFromOrganisationByRole(Integer organisationID, String role) throws IOException
	{
		return userMgmtService.getUsersFromOrganisationByRole(organisationID, role);
	}
	
	
	
}
