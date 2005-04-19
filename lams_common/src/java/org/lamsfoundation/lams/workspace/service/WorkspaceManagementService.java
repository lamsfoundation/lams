/*
 * Created on Apr 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.workspace.service;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.Workspace;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dao.IOrganisationDAO;
import org.lamsfoundation.lams.usermanagement.dao.IUserDAO;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceDAO;
import org.lamsfoundation.lams.usermanagement.dao.IWorkspaceFolderDAO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.util.wddx.WDDXTAGS;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.exception.WorkspaceFolderException;

/**
 * @author Manpreet Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WorkspaceManagementService implements IWorkspaceManagementService{
	
	private FlashMessage flashMessage;
	
	public static final Integer AUTHORING = new Integer(1);
	public static final Integer MONITORING = new Integer(2);
	
	protected IUserDAO userDAO;	
	protected ILearningDesignDAO learningDesignDAO;
	protected IWorkspaceFolderDAO workspaceFolderDAO;
	protected IWorkspaceDAO workspaceDAO;
	protected IOrganisationDAO organisationDAO;
	
	protected IAuthoringService authoringService;
	
	
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
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#deleteFolder(java.lang.Integer, java.lang.Integer)
	 */
	public String deleteFolder(Integer folderID, Integer userID)throws IOException{		
		WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(folderID);
		User user = userDAO.getUserById(userID);
		if(user!=null){
			if(!getPermissions(workspaceFolder,user).equals(WorkspaceFolder.OWNER_ACCESS)){
				flashMessage = FlashMessage.getUserNotAuthorized("deleteFolder",userID);
			}else{
				if(workspaceFolder!=null){
					if(isRootFolder(workspaceFolder))
						flashMessage = new FlashMessage("deleteFolder",
														"Cannot delete this folder as it is the Root folder.",
														FlashMessage.ERROR);
					else{
						if(!workspaceFolder.isEmpty())
							flashMessage = new FlashMessage("deleteFolder",
															"Cannot delete folder with folder_id of: " + folderID +
															" as it is not empty. Please delete its contents first.",
															FlashMessage.ERROR);
						else{
							workspaceFolderDAO.delete(workspaceFolder);
							flashMessage = new FlashMessage("deleteFolder","Folder deleted:" + folderID);
						}
					}
				}else
					flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists("deleteFolder",folderID);
			}
		}else
			flashMessage = FlashMessage.getNoSuchUserExists("deleteFolder",userID);
			
		return flashMessage.serializeMessage();
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
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getFolderContents(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public String getFolderContents(Integer userID, Integer workspaceFolderID, Integer mode)throws IOException{
		User user = userDAO.getUserById(userID);
		WorkspaceFolder workspaceFolder = null;
		Integer permissions = null;
		if(user!=null){
			workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderID);
			if(workspaceFolder!=null){
				permissions = getPermissions(workspaceFolder,user);
				if(permissions!=WorkspaceFolder.NO_ACCESS){					
					Vector contentDTO = new Vector();
					getFolderContent(workspaceFolder,permissions,mode,contentDTO);
					if(workspaceFolder.hasSubFolders())
						getSubFolderDetails(workspaceFolder,permissions,contentDTO);					
					flashMessage = new FlashMessage("getFolderContents",createFolderContentPacket(workspaceFolder,contentDTO));
				}
				else
					flashMessage = new FlashMessage("getFolderContents",
													"Access Denied for user with user_id:" + userID,
													FlashMessage.ERROR);
			}
			else
				flashMessage = new FlashMessage("getFolderContents",
												"No such workspaceFolder with workspace_folder_id of:" + workspaceFolderID + " exists",
												FlashMessage.ERROR);
		}else
			flashMessage = FlashMessage.getNoSuchUserExists("getFolderContents",userID);
		return flashMessage.serializeMessage();
		
	}	
	private void getFolderContent(WorkspaceFolder workspaceFolder, Integer permissions, Integer mode,Vector contentDTO){
		
		Integer parentFolderId = workspaceFolder.getParentWorkspaceFolder()!=null?
								 workspaceFolder.getParentWorkspaceFolder().getWorkspaceFolderId():
								 WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER;	
		List designs = null;
		
		if(mode==AUTHORING)
			designs = learningDesignDAO.getAllLearningDesignsInFolder(workspaceFolder.getWorkspaceFolderId());
		else
			designs = learningDesignDAO.getAllValidLearningDesignsInFolder(workspaceFolder.getWorkspaceFolderId());
		
		getFolderContentDTO(designs,permissions,contentDTO);
	}
	private void getSubFolderDetails(WorkspaceFolder workspaceFolder,Integer permissions, Vector subFolderContent){				
		Iterator iterator = workspaceFolder.getChildWorkspaceFolders().iterator();
		while(iterator.hasNext()){
			WorkspaceFolder subFolder = (WorkspaceFolder)iterator.next();
			subFolderContent.add(new FolderContentDTO(subFolder,permissions));
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
	private Hashtable createFolderContentPacket(WorkspaceFolder workspaceFolder, Vector contents){
		Hashtable packet = new Hashtable();
		packet.put("parentWorkspaceFolderID", workspaceFolder.getParentWorkspaceFolder().getWorkspaceFolderId());
		packet.put("workspaceFolderID", workspaceFolder.getWorkspaceFolderId());
		packet.put("contents", contents);
		return packet;
	}	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#copyFolder(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	public String copyFolder(Integer folderID,Integer newFolderID,Integer userID)throws IOException{
		try{
			if(isUserAuthorized(newFolderID,userID)){
				WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(folderID);
				if(workspaceFolder!=null){
					WorkspaceFolder newFolder = createFolder(newFolderID,workspaceFolder.getName(),userID);
					copyRootContent(workspaceFolder,newFolder,userID);				
					if(workspaceFolder.hasSubFolders())
						createSubFolders(workspaceFolder, newFolder,userID);
					flashMessage = new FlashMessage("copyFolder",createCopyFolderPacket(newFolder));
				}else
					throw new WorkspaceFolderException();				
			}else
				flashMessage = FlashMessage.getUserNotAuthorized("copyFolder", userID);
		}catch(UserException ue){
			flashMessage = FlashMessage.getNoSuchUserExists("copyFolder",userID);
		}catch(WorkspaceFolderException we){
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists("copyFolder",folderID);
		}
		return flashMessage.serializeMessage();
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
	private Hashtable createCopyFolderPacket(WorkspaceFolder workspaceFolder){
		Hashtable packet = new Hashtable();
		packet.put("workspaceFolderID", workspaceFolder.getWorkspaceFolderId());
		packet.put("workspaceID",workspaceFolder.getWorkspaceID());
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
													user,targetWorkspaceFolder);
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
	 * TODO Deleting a LearningDesign would mean deleting all its corresponding
	 * activities, transitions and the content related to such activities.
	 * Deletion of content has to be yet taken care of. Since Tools manage there
	 * own content.Just need to cross-check this once tools are functional
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#deleteLearningDesign(java.lang.Long)
	 */
	public String deleteLearningDesign(Long learningDesignID, Integer userID)throws IOException{
		User user = userDAO.getUserById(userID);
		if(user!=null){
			LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignID);
			if(learningDesign!=null){
				if(learningDesign.getUser().getUserId().equals(user.getUserId())){
					if(learningDesign.getReadOnly().booleanValue()){
						flashMessage = new FlashMessage("deleteLearningDesign",
														"Cannot delete design with learning_design_id of:" + learningDesignID +
														" as it is READ ONLY.",
														FlashMessage.ERROR);
					}else{
						List list = learningDesignDAO.getLearningDesignsByParent(learningDesignID);
						if(list==null || list.size()==0)
							learningDesignDAO.delete(learningDesign);
						else
							flashMessage = new FlashMessage("deleteLearningDesign",
															"Cannot delete design with learning_design_id of:" + learningDesignID +
															" as it is a PARENT.",
															FlashMessage.ERROR);
					}
				}else
					flashMessage = FlashMessage.getUserNotAuthorized("deleteLearningDesign",userID);
		}else
			flashMessage = FlashMessage.getNoSuchLearningDesignExists("deleteLearningDesign",learningDesignID);
		}else
			flashMessage = FlashMessage.getNoSuchUserExists("deleteLearningDesign",userID);
		
		return flashMessage.serializeMessage();
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
					flashMessage = new FlashMessage("moveFolder",currentFolderID);
				}else
					throw new WorkspaceFolderException();
			}else
				flashMessage = FlashMessage.getUserNotAuthorized("moveFolder", userID);			
		}catch(UserException ue){
			flashMessage = FlashMessage.getNoSuchUserExists("moveFolder", userID);
		}catch(WorkspaceFolderException we){
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists("moveFolder",targetFolderID);			
		}
		return flashMessage.serializeMessage();
	}
}
