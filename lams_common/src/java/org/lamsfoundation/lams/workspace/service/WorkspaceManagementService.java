/*
 * Created on Apr 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.workspace.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.Vector;

import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
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
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;
import org.lamsfoundation.lams.workspace.dao.IWorkspaceFolderContentDAO;
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
	
	protected IWorkspaceFolderContentDAO workspaceFolderContentDAO;
	
	
	protected IAuthoringService authoringService;
	protected IRepositoryService repositoryService;
	
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
	public String getFolderContents(Integer userID, Integer workspaceFolderID, Integer mode)throws Exception{
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
					Vector repositoryContent = getContentsFromRepository(new Long(workspaceFolderID.intValue()),permissions);
					if(repositoryContent!=null)
						contentDTO.addAll(repositoryContent);
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
	/**
	 * This method verifies the credentials of the Workspace Manager
	 * and gives him the Ticket to login and access the Content Repository.
	 * A valid ticket is needed in order to access the content from the repository.
	 * This method would be called evertime the user(Workspace Manager) receives 
	 * a request to get the contents of the Folder or to add/update a file into
	 * the <code>WorkspaceFodler</code> (Repository).
	 *  
	 * @return ITicket The ticket for repostory access
	 */
	private ITicket getRepositoryLoginTicket(){
		repositoryService = RepositoryProxy.getLocalRepositoryService();
		ICredentials credentials = new SimpleCredentials(IWorkspaceManagementService.REPOSITORY_USERNAME,
				 										 IWorkspaceManagementService.REPOSITORY_PASSWORD.toCharArray());		
		try{
			ITicket ticket = repositoryService.login(credentials,IWorkspaceManagementService.REPOSITORY_WORKSPACE);
			return ticket;
		}catch(AccessDeniedException ae){
			ae.printStackTrace();
			return null;
		}catch(WorkspaceNotFoundException we){
			we.printStackTrace();
			return null;			
		}catch (LoginException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#createWorkspaceFolderContent(java.lang.Integer, java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	public String createWorkspaceFolderContent(Integer contentTypeID,String name,
											 String description,Date createDateTime,
											 Date lastModifiedDate,Integer workspaceFolderID,
											 String mimeType, String path)throws Exception{
		WorkspaceFolder workspaceFolder = workspaceFolderDAO.getWorkspaceFolderByID(workspaceFolderID);
		if(workspaceFolder!=null){
			WorkspaceFolderContent workspaceFolderContent = new WorkspaceFolderContent(contentTypeID,name,description,createDateTime,lastModifiedDate,mimeType,workspaceFolder);
			workspaceFolderContentDAO.insert(workspaceFolderContent);
			try{
				InputStream stream = new FileInputStream(path);
				NodeKey nodeKey = addFileToRepository(stream,name,mimeType);
				workspaceFolderContent.setUuid(nodeKey.getUuid());
				workspaceFolderContent.setVersionID(nodeKey.getUuid());
				workspaceFolderContentDAO.update(workspaceFolderContent);
				flashMessage = new FlashMessage("createWorkspaceFolderContent",nodeKey);
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
		}else
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists("createWorkspaceFolderContent",workspaceFolderID);
		return flashMessage.serializeMessage();
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
			flashMessage = new FlashMessage("updateWorkspaceFolderContent",nodeKey);
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
			if(files==null){
				if(databaseVersion.equals(versionToBeDeleted)){
					workspaceFolderContent.setVersionID(latestAvailableVersion);
					workspaceFolderContentDAO.update(workspaceFolderContent);
				}
				flashMessage = new FlashMessage("deleteContentWithVersion","Content Successfully deleted");
			}else
				flashMessage = new FlashMessage("deleteContentWithVersion",
												"Following files could not be deleted" + files,
												FlashMessage.CRITICAL_ERROR);
		}catch(ItemNotFoundException ie){
			workspaceFolderContentDAO.delete(workspaceFolderContent);
			flashMessage = new FlashMessage("deleteContentWithVersion","Content Successfully deleted");
		}
		
		return  flashMessage.serializeMessage();		
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#deleteWorkspaceFolderContent(java.lang.Long, boolean)
	 */
	public String deleteWorkspaceFolderContent(Long folderContentID)throws Exception{
		WorkspaceFolderContent workspaceFolderContent = workspaceFolderContentDAO.getWorkspaceFolderContentByID(folderContentID);
		if(workspaceFolderContent!=null){
			Long uuid = workspaceFolderContent.getUuid();
			Long versionID = workspaceFolderContent.getVersionID();
			ITicket ticket = getRepositoryLoginTicket();
			String files[] = repositoryService.deleteNode(ticket,uuid);
			if(files!=null){
				flashMessage = new FlashMessage("deleteWorkspaceFolderContent",
												"Follwing files could not be deleted" + files.toString(),
												FlashMessage.CRITICAL_ERROR);
			}else{
				
				flashMessage = new FlashMessage("deleteWorkspaceFolderContent","Content deleted");
			}
		}else
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderContentExsists("deleteWorkspaceFolderContent",folderContentID);
		
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
	 * @throws Exception
	 */
	private Vector getContentsFromRepository(Long workspaceFolderID, Integer permissions)throws Exception{
		List content = workspaceFolderContentDAO.getContentByWorkspaceFolder(workspaceFolderID);
		if(content.size()==0)
			return null;
		else{
			ITicket ticket = getRepositoryLoginTicket();
			Vector repositoryContent = new Vector();
			Iterator contentIterator = content.iterator();
			while(contentIterator.hasNext()){
				WorkspaceFolderContent workspaceFolderContent = (WorkspaceFolderContent)contentIterator.next();
				SortedSet set = repositoryService.getVersionHistory(ticket,workspaceFolderContent.getUuid());				
				repositoryContent.add(workspaceFolderContent.getFolderContentDTO(set,permissions));
			}
			return repositoryContent;
		}
	}
}
