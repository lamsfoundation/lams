/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.workspace.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
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
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.dao.ILearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.usermanagement.Organisation;
import org.lamsfoundation.lams.usermanagement.OrganisationState;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.UserOrganisation;
import org.lamsfoundation.lams.usermanagement.UserOrganisationRole;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dto.OrganisationDTO;
import org.lamsfoundation.lams.usermanagement.dto.UserFlashDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.dto.UpdateContentDTO;
import org.lamsfoundation.lams.workspace.web.WorkspaceAction;

/**
 * @author Manpreet Minhas
 */
public class WorkspaceManagementService implements IWorkspaceManagementService {

    protected Logger log = Logger.getLogger(WorkspaceManagementService.class.getName());

    public static final Integer AUTHORING = new Integer(1);
    public static final Integer MONITORING = new Integer(2);

    protected IBaseDAO baseDAO;
    protected ILearningDesignDAO learningDesignDAO;

    protected IAuthoringService authoringService;
    protected IRepositoryService repositoryService;
    protected IUserManagementService userMgmtService;
    protected MessageService messageService;

    // To support integrations that need a type for the learning design, any designs that do not have a type field set,
    // return "default" in the type field. Also, the integrations can search for designs that do not have a type field
    // set by setting type = default. This setup is done in the FolderContentDTO. See LDEV-3523
    protected static final String DEFAULT_DESIGN_TYPE = "default";

    protected static final String ALL_DESIGN_TYPES = "all";

    /**
     * i18n Message service
     *
     * @param messageSource
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }

    /**
     * i18n Message service. The Workspace action class needs access to the message service.
     *
     * @param messageSource
     */
    @Override
    public MessageService getMessageService() {
	return messageService;
    }

    /**
     * @param workspaceFolderContentDAO
     *            The workspaceFolderContentDAO to set.
     */
    public void setBaseDAO(IBaseDAO baseDAO) {
	this.baseDAO = baseDAO;
    }

    /**
     * @param authoringService
     *            The authoringService to set.
     */
    public void setAuthoringService(IAuthoringService authoringService) {
	this.authoringService = authoringService;
    }

    /**
     * @param repositoryService
     *            The repositoryService to set.
     */
    public void setRepositoryService(IRepositoryService repositoryService) {
	this.repositoryService = repositoryService;
    }

    /**
     * @param learningDesignDAO
     *            The learningDesignDAO to set.
     */
    public void setLearningDesignDAO(ILearningDesignDAO learningDesignDAO) {
	this.learningDesignDAO = learningDesignDAO;
    }

    public void setUserMgmtService(IUserManagementService userMgmtService) {
	this.userMgmtService = userMgmtService;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#deleteResource(java.lang.Integer,
     *      java.lang.String, java.lang.Integer)
     */
    @Override
    public String deleteResource(Long resourceID, String resourceType, Integer userID) throws IOException {

	String errorMessage = null;

	if ((resourceID == null) || (resourceType == null) || (userID == null)) {
	    errorMessage = messageService.getMessage("delete.resource.error.value.miss");
	} else if (FolderContentDTO.DESIGN.equals(resourceType)) {
	    return deleteLearningDesignWDDX(resourceID, userID);
	} else if (FolderContentDTO.FOLDER.equals(resourceType)) {
	    return deleteFolder(new Integer(resourceID.intValue()), userID);
	} else if (FolderContentDTO.FILE.equals(resourceType)) {
	    return deleteWorkspaceFolderContent(resourceID);
	} else if (FolderContentDTO.LESSON.equals(resourceType)) {
	    // TODO implement delete lesson
	    errorMessage = messageService.getMessage("delete.lesson.error");
	}

	FlashMessage message = new FlashMessage(IWorkspaceManagementService.MSG_KEY_DELETE,
		messageService.getMessage("delete.resource.error", new Object[] { errorMessage }), FlashMessage.ERROR);
	return message.serializeMessage();

    }

    /**
     * This method deletes the <code>WorkspaceFolder</code> with given <code>workspaceFolderID</code>. But before it
     * does so it checks whether the <code>User</code> is authorized to perform this action <br>
     *
     * <p>
     * <b>Note:</b><br>
     * </p>
     * <p>
     * To be able to a delete a <code>WorkspaceFolder</code> successfully you have to keep the following things in mind
     * <ul>
     * <li>The folder to be deleted should be empty.</li>
     * <li>It should not be the root folder of any <code>Organisation</code> or <code>User</code></li>
     * </ul>
     * </p>
     *
     * @param workspaceFolderID
     *            The <code>WorkspaceFolder</code> to be deleted
     * @param userID
     *            The <code>User</code> who has requested this operation
     * @return String The acknowledgement/error message in WDDX format for FLASH
     * @throws IOException
     */
    private String deleteFolder(Integer folderID, Integer userID) throws IOException {
	WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, folderID);
	User user = (User) userMgmtService.findById(User.class, userID);
	FlashMessage flashMessage = null;
	if (user != null) {
	    flashMessage = deleteFolder(workspaceFolder, user, isSysAuthorAdmin(user));
	} else {
	    flashMessage = FlashMessage.getNoSuchUserExists(IWorkspaceManagementService.MSG_KEY_DELETE, userID);
	}

	return flashMessage.serializeMessage();
    }

    private FlashMessage deleteFolder(WorkspaceFolder workspaceFolder, User user, boolean isSysAuthorAdmin)
	    throws IOException {
	if (!getPermissions(workspaceFolder, user).equals(WorkspaceFolder.OWNER_ACCESS)) {
	    return FlashMessage.getUserNotAuthorized(IWorkspaceManagementService.MSG_KEY_DELETE, user.getUserId());
	} else {
	    if (workspaceFolder != null) {
		return deleteFolderContents(workspaceFolder, user, isSysAuthorAdmin);
	    } else {
		return FlashMessage.getNoSuchWorkspaceFolderExsists(IWorkspaceManagementService.MSG_KEY_DELETE,
			workspaceFolder.getWorkspaceFolderId());
	    }
	}
    }

    /**
     * This method will try to delete the <code>folder</code>. If the folder is not empty it will attempt to delete all
     * resources inside the folder. If the user does not have permission to delete that resource then a FlashMessage
     * will be returned indicating which resource it was unable to delete. If all resources inside that
     * <code>folder</code> are successfully deleted, then the folder will be deleted and a FlashMessage will be returned
     * indicating it was successfully deleted.
     *
     * @param folder
     */
    private FlashMessage deleteFolderContents(WorkspaceFolder folder, User user, boolean isSysAuthorAdmin)
	    throws IOException {
	FlashMessage flashMessage = null;
	boolean isDeleteSuccessful = true;

	Integer folderID = folder.getWorkspaceFolderId();

	if (!folder.isEmpty()) {
	    if (folder.hasSubFolders()) {
		Set subFolders = folder.getChildWorkspaceFolders();
		Iterator subFolderIterator = subFolders.iterator();
		while (subFolderIterator.hasNext()) {
		    WorkspaceFolder subFolder = (WorkspaceFolder) subFolderIterator.next();
		    flashMessage = deleteFolder(subFolder, user, isSysAuthorAdmin);
		    if (flashMessage.getMessageType() != FlashMessage.OBJECT_MESSAGE) {
			return flashMessage;
		    }
		}
	    } else {

		// delete all files within the directory
		Set folderContents = folder.getFolderContent();
		Iterator i = folderContents.iterator();
		while (i.hasNext()) {
		    WorkspaceFolderContent folderContent = (WorkspaceFolderContent) i.next();
		    folderContent.setWorkspaceFolder(null);
		    folder.getFolderContent().remove(folderContent);
		}

		// delete all learning designs within folder (if created by the user)
		Set learningDesigns = folder.getLearningDesigns();
		Iterator j = learningDesigns.iterator();
		while (j.hasNext()) {
		    LearningDesign learningDesign = (LearningDesign) j.next();
		    if (learningDesign != null) {
			if (isSysAuthorAdmin || learningDesign.getUser().getUserId().equals(user.getUserId())) {
			    if (learningDesign.getReadOnly().booleanValue()) {
				isDeleteSuccessful = false;
				flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_DELETE,
					messageService.getMessage("delete.learningdesign.error",
						new Object[] { learningDesign.getLearningDesignId() }),
					FlashMessage.ERROR);
			    } else {
				folder.getLearningDesigns().remove(learningDesign);
				learningDesign.setWorkspaceFolder(null);
			    }
			}
		    }
		}

		baseDAO.update(folder); // this call will delete the files and learning designs which were removed from
					// the collection above.

	    }

	}
	if (isDeleteSuccessful) // it will only delete this folder if all the files/folder/learningDesigns are all
				// deleted
	{
	    baseDAO.delete(folder);
	    flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_DELETE,
		    messageService.getMessage("folder.delete", new Object[] { folderID }));
	}

	return flashMessage;

    }

    @Override
    public WorkspaceFolder getWorkspaceFolder(Integer workspaceFolderID) {
	return (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, workspaceFolderID);
    }

    @Override
    public Vector<FolderContentDTO> getFolderContentsExcludeHome(Integer userID, WorkspaceFolder folder, Integer mode)
	    throws UserAccessDeniedException, RepositoryCheckedException {
	User user = (User) baseDAO.find(User.class, userID);
	return getFolderContentsInternal(user, folder, mode, "getFolderContentsExcludeHome", user.getWorkspaceFolder());
    }

    @Override
    public Vector<FolderContentDTO> getFolderContents(Integer userID, WorkspaceFolder folder, Integer mode)
	    throws UserAccessDeniedException, RepositoryCheckedException {
	User user = (User) baseDAO.find(User.class, userID);
	return getFolderContentsInternal(user, folder, mode, "getFolderContents", null);
    }

    /**
     * Get the contents of a folder. Internal method used for both getFolderContentsExcludeHome() and
     * getFolderContents(). If skipContentId is not null, then skip any contents found with this id.
     *
     * @throws UserAccessDeniedException
     * @throws RepositoryCheckedException
     */
    private Vector<FolderContentDTO> getFolderContentsInternal(User user, WorkspaceFolder workspaceFolder, Integer mode,
	    String methodName, WorkspaceFolder skipFolder)
	    throws UserAccessDeniedException, RepositoryCheckedException {
	Vector<FolderContentDTO> contentDTO = new Vector<FolderContentDTO>();
	if (user != null) {
	    Integer permissions = getPermissions(workspaceFolder, user);
	    if (permissions != WorkspaceFolder.NO_ACCESS) {
		getFolderContent(workspaceFolder, permissions, mode, contentDTO, user);
		if (workspaceFolder.hasSubFolders()) {
		    getSubFolderDetails(workspaceFolder, user, contentDTO, skipFolder);
		}
		Vector<FolderContentDTO> repositoryContent = getContentsFromRepository(workspaceFolder, permissions,
			user);
		if (repositoryContent != null) {
		    contentDTO.addAll(repositoryContent);
		}
	    } else {
		throw new UserAccessDeniedException(user);
	    }
	} else {
	    throw new UserAccessDeniedException(user);
	}

	Collections.sort(contentDTO);
	return contentDTO;

    }

    private void getFolderContent(WorkspaceFolder workspaceFolder, Integer folderPermissions, Integer mode,
	    Vector<FolderContentDTO> contentDTO, User user) {

	List designs = null;

	if (WorkspaceManagementService.AUTHORING.equals(mode)) {
	    designs = learningDesignDAO.getAllLearningDesignsInFolder(workspaceFolder.getWorkspaceFolderId());
	} else {
	    designs = learningDesignDAO.getAllValidLearningDesignsInFolder(workspaceFolder.getWorkspaceFolderId());
	}

	getFolderContentDTO(designs, folderPermissions, contentDTO, user);
    }

    /**
     * Get the folders in the given workspaceFolder. If skipContentId is not null, then skip any contents found with
     * this id.
     */
    private void getSubFolderDetails(WorkspaceFolder workspaceFolder, User user,
	    Vector<FolderContentDTO> subFolderContent, WorkspaceFolder skipFolder) {
	Integer skipFolderID = skipFolder != null ? skipFolder.getWorkspaceFolderId() : null;
	Iterator iterator = workspaceFolder.getChildWorkspaceFolders().iterator();
	while (iterator.hasNext()) {
	    WorkspaceFolder subFolder = (WorkspaceFolder) iterator.next();
	    if ((skipFolderID == null) || !skipFolderID.equals(subFolder.getWorkspaceFolderId())) {
		Integer permissions = getPermissions(subFolder, user);
		if (permissions != WorkspaceFolder.NO_ACCESS) {
		    subFolderContent.add(new FolderContentDTO(subFolder, permissions, user));
		}
	    }
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getFolderContentsJSON(Integer folderID, Integer userID, boolean allowInvalidDesigns)
	    throws JSONException, IOException, UserAccessDeniedException, RepositoryCheckedException {

	return getFolderContentsJSON(folderID, userID, allowInvalidDesigns, false, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getFolderContentsJSON(Integer folderID, Integer userID, boolean allowInvalidDesigns,
	    String designType)
	    throws JSONException, IOException, UserAccessDeniedException, RepositoryCheckedException {

	return getFolderContentsJSON(folderID, userID, allowInvalidDesigns, false, designType);
    }

    public String getFolderContentsJSON(Integer folderID, Integer userID, boolean allowInvalidDesigns,
	    boolean designsOnly, String designType)
	    throws JSONException, IOException, UserAccessDeniedException, RepositoryCheckedException {
	JSONObject result = new JSONObject();
	Vector<FolderContentDTO> folderContents = null;

	// folderID == null: get all user accessible folders
	// folderID == -1: get the learning designs at the root of the user's home folder - used for simplified
	// deployment
	if ((folderID == null) || (folderID == -1)) {

	    FolderContentDTO userFolder = getUserWorkspaceFolder(userID);

	    if (folderID == null) {
		folderContents = new Vector<FolderContentDTO>(3);

		if (userFolder != null) {
		    folderContents.add(userFolder);
		}

		FolderContentDTO myGroupsFolder = new FolderContentDTO(messageService.getMessage("organisations"),
			messageService.getMessage("folder"), null, null, FolderContentDTO.FOLDER,
			WorkspaceAction.ORG_FOLDER_ID.longValue(), WorkspaceFolder.READ_ACCESS, null);

		folderContents.add(myGroupsFolder);

		FolderContentDTO publicFolder = getPublicWorkspaceFolder(userID);
		if (publicFolder != null) {
		    folderContents.add(publicFolder);
		}

	    } else if (userFolder != null) {
		return getFolderContentsJSON(userFolder.getResourceID().intValue(), userID, allowInvalidDesigns, true,
			designType);

	    } // else we want to return an empty JSON, which will be done by falling through to the folderContents loop.

	    // special behaviour for organisation folders
	} else if (folderID.equals(WorkspaceAction.ORG_FOLDER_ID)) {
	    folderContents = getAccessibleOrganisationWorkspaceFolders(userID);
	    Collections.sort(folderContents);

	    if (folderContents.size() == 1) {
		FolderContentDTO folder = folderContents.firstElement();
		if (folder.getResourceID().equals(WorkspaceAction.ROOT_ORG_FOLDER_ID)) {
		    return getFolderContentsJSON(WorkspaceAction.ROOT_ORG_FOLDER_ID, userID, allowInvalidDesigns);
		}
	    }
	} else {
	    WorkspaceFolder folder = getWorkspaceFolder(folderID);
	    Integer mode = allowInvalidDesigns ? WorkspaceManagementService.AUTHORING
		    : WorkspaceManagementService.MONITORING;
	    folderContents = getFolderContents(userID, folder, mode);
	    Collections.sort(folderContents);
	}

	// fill JSON object with folders and LDs
	for (FolderContentDTO folderContent : folderContents) {
	    String contentType = folderContent.getResourceType();
	    if (FolderContentDTO.FOLDER.equals(contentType) && !designsOnly) {
		JSONObject subfolderJSON = new JSONObject();
		subfolderJSON.put("name", folderContent.getName());
		subfolderJSON.put("isRunSequencesFolder",
			WorkspaceFolder.RUN_SEQUENCES.equals(folderContent.getResourceTypeID().intValue()));
		subfolderJSON.put("folderID", folderContent.getResourceID().intValue());
		result.append("folders", subfolderJSON);
	    } else if (FolderContentDTO.DESIGN.equals(contentType)) {
		if (folderContent.getDesignType() == null) {
		    folderContent.setDesignType(WorkspaceManagementService.DEFAULT_DESIGN_TYPE);
		}
		// no designType: get only authored LDs
		// designType=all: get all template LDs
		// designType=someting: get only "something" templateLDs
		if (designType == null
			? folderContent.getDesignType().equals(WorkspaceManagementService.DEFAULT_DESIGN_TYPE)
			: (designType.equals(WorkspaceManagementService.ALL_DESIGN_TYPES) && !folderContent
				.getDesignType().equals(WorkspaceManagementService.DEFAULT_DESIGN_TYPE))
				|| designType.equals(folderContent.getDesignType())) {
		    JSONObject learningDesignJSON = new JSONObject();
		    learningDesignJSON.put("name", folderContent.getName());
		    learningDesignJSON.put("learningDesignId", folderContent.getResourceID());
		    learningDesignJSON.putOpt("type", folderContent.getDesignType());
		    learningDesignJSON.put("date", folderContent.getLastModifiedDateTime());
		    result.append("learningDesigns", learningDesignJSON);
		}
	    } else {
		if (log.isDebugEnabled()) {
		    log.debug("Unsupported folder content found, named \"" + folderContent.getName() + "\"");
		}
	    }
	}

	return result.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getPagedLearningDesignsJSON(Integer userID, boolean allowInvalidDesigns, String searchString,
	    int page, int size, String sortName, String sortDate) throws JSONException, IOException {

	JSONArray result = new JSONArray();
	Pattern searchPattern = searchString != null
		? Pattern.compile(Pattern.quote(searchString), Pattern.CASE_INSENSITIVE) : null;
	FolderContentDTO userFolder = getUserWorkspaceFolder(userID);
	long numDesigns = 0;

	if (userFolder != null) {
	    Integer mode = allowInvalidDesigns ? WorkspaceManagementService.AUTHORING
		    : WorkspaceManagementService.MONITORING;

	    int folderId = userFolder.getResourceID().intValue();
	    List designs = null;
	    if (WorkspaceManagementService.AUTHORING.equals(mode)) {
		if (searchPattern != null) {
		    designs = learningDesignDAO.getAllPagedLearningDesigns(folderId, null, null, sortName, sortDate);
		} else {
		    designs = learningDesignDAO.getAllPagedLearningDesigns(folderId, page, size, sortName, sortDate);
		}
	    } else {
		if (searchPattern != null) {
		    designs = learningDesignDAO.getValidPagedLearningDesigns(folderId, null, null, sortName, sortDate);
		} else {
		    designs = learningDesignDAO.getValidPagedLearningDesigns(folderId, page, size, sortName, sortDate);
		}
	    }

	    Iterator iterator = designs.iterator();
	    while (iterator.hasNext()) {
		LearningDesign design = (LearningDesign) iterator.next();
		if ((searchPattern == null) || (searchPattern.matcher(design.getTitle()).find())) {
		    JSONObject learningDesignJSON = new JSONObject();
		    learningDesignJSON.put("name", StringEscapeUtils.escapeHtml(design.getTitle()));
		    learningDesignJSON.put("learningDesignId", design.getLearningDesignId());
		    learningDesignJSON.putOpt("type", design.getDesignType() != null ? design.getDesignType()
			    : WorkspaceManagementService.DEFAULT_DESIGN_TYPE);
		    learningDesignJSON.put("date", design.getLastModifiedDateTime());
		    result.put(learningDesignJSON);
		}
	    }

	    // what is the total number (so the pager knows whether to allow paging)
	    // if we did a search, then no paging just return the whole lot.
	    // otherwise need the whole count from the db.
	    numDesigns = searchPattern != null ? result.length()
		    : learningDesignDAO.countAllLearningDesigns(folderId, !allowInvalidDesigns);
	}

	JSONObject completeResult = new JSONObject();
	completeResult.put("total_rows", numDesigns);
	if (result.length() > 0) {
	    completeResult.put("rows", result);
	}
	return completeResult.toString();
    }

    /**
     * This method returns the permissions specific to the given <code>workspaceFolder</code> for the given user.
     *
     * @param workspaceFolder
     *            The workspaceFolder for which we need the permissions
     * @param user
     *            The user for whom these permissions are set.
     * @return Integer The permissions
     */
    private Integer getPermissions(WorkspaceFolder workspaceFolder, User user) {
	Integer permission = null;

	if ((workspaceFolder == null) || (user == null)) {
	    log.debug("no access due to null value(s) in user or workspaceFolder");
	    permission = WorkspaceFolder.NO_ACCESS;
	} else {
	    if (WorkspaceFolder.RUN_SEQUENCES.equals(workspaceFolder.getWorkspaceFolderType())) {
		permission = WorkspaceFolder.READ_ACCESS;
	    } else if (WorkspaceFolder.PUBLIC_SEQUENCES.equals(workspaceFolder.getWorkspaceFolderType())) {
		permission = WorkspaceFolder.MEMBERSHIP_ACCESS;
	    } else if (workspaceFolder.getUserID().equals(user.getUserId())) {
		permission = WorkspaceFolder.OWNER_ACCESS;
	    } else if (isSysAuthorAdmin(user)) {
		permission = WorkspaceFolder.OWNER_ACCESS;
	    } else if (user.hasMemberAccess(workspaceFolder)) {
		permission = WorkspaceFolder.MEMBERSHIP_ACCESS;
	    } else {
		permission = WorkspaceFolder.NO_ACCESS;
	    }
	    if (log.isDebugEnabled()) {
		log.debug(user.getLogin() + " has " + permission + " access to " + workspaceFolder.getName());
	    }
	}

	return permission;
    }

    private boolean isSysAuthorAdmin(User user) {
	return userMgmtService.hasRoleInOrganisation(user, Role.ROLE_SYSADMIN);
    }

    /**
     * This method checks if the given workspaceFolder is a subFolder of the given rootFolder. Returns false if they are
     * the same folder.
     */
    /*
     * private boolean isSubFolder(WorkspaceFolder workspaceFolder,WorkspaceFolder rootFolder){
     * if ( rootFolder != null ) {
     * // is it the same folder?
     * if ( rootFolder.getWorkspaceFolderId().equals(workspaceFolder.getWorkspaceFolderId()) ) {
     * return false;
     * }
     * // check the parent hierarchy
     * WorkspaceFolder folder = workspaceFolder;
     * while ( folder != null && ! rootFolder.getWorkspaceFolderId().equals(folder.getWorkspaceFolderId()) ) {
     * folder = folder.getParentWorkspaceFolder();
     * }
     * return ( folder != null );
     * }
     * return false;
     *
     * }
     */
    private Vector getFolderContentDTO(List designs, Integer folderPermissions, Vector<FolderContentDTO> folderContent,
	    User user) {
	Iterator iterator = designs.iterator();
	while (iterator.hasNext()) {
	    LearningDesign design = (LearningDesign) iterator.next();
	    FolderContentDTO folder;
	    if ((design.getUser() != null) && design.getUser().equals(user)) {
		folder = new FolderContentDTO(design, WorkspaceFolder.OWNER_ACCESS, user);
	    } else {
		folder = new FolderContentDTO(design, folderPermissions, user);
	    }

	    folderContent.add(folder);
	}
	return folderContent;

    }

    @Override
    public String copyResource(Long resourceID, String resourceType, Integer copyType, Integer targetFolderID,
	    Integer userID) throws IOException {
	String errorMessage = null;

	if ((resourceID == null) || (targetFolderID == null) || (resourceType == null) || (userID == null)) {
	    errorMessage = messageService.getMessage("copy.resource.error.value.miss");
	} else if (FolderContentDTO.DESIGN.equals(resourceType)) {
	    return copyLearningDesign(resourceID, targetFolderID, copyType, userID);
	} else if (FolderContentDTO.FOLDER.equals(resourceType)) {
	    return copyFolder(new Integer(resourceID.intValue()), targetFolderID, userID);
	} else if (FolderContentDTO.FILE.equals(resourceType)) {
	    // TODO implement delete file resource
	    errorMessage = messageService.getMessage("copy.no.support");
	} else if (FolderContentDTO.LESSON.equals(resourceType)) {
	    // TODO implement delete lesson
	    errorMessage = messageService.getMessage("copy.no.support");
	}

	FlashMessage message = new FlashMessage(IWorkspaceManagementService.MSG_KEY_COPY,
		messageService.getMessage("copy.resource.error", new Object[] { errorMessage }), FlashMessage.ERROR);
	return message.serializeMessage();

    }

    /**
     * This method copies one folder inside another folder. To be able to successfully perform this action following
     * conditions must be met in the order they are listed.
     * <p>
     * <ul>
     * <li>The target <code>WorkspaceFolder</code> must exists</li>
     * <li>The <code>User</code> with the given <code>userID</code> must have OWNER or MEMBERSHIP rights for that
     * <code>WorkspaceFolder</code> to be authorized to do so.</li>
     * <ul>
     * </p>
     *
     * <p>
     * <b>Note: </b> By default the copied folder has the same name as that of the one being copied. But in case the
     * target <code>WorkspaceFolder</code> already has a folder with the same name, an additional "C" is appended to the
     * name of the folder thus created.
     * </p>
     *
     * @param folderID
     *            The <code>WorkspaceFolder</code> to be copied.
     * @param targetFolderID
     *            The parent <code>WorkspaceFolder</code> under which it has to be copied
     * @param userID
     *            The <code>User</code> who has requested this opeartion
     * @return String The acknowledgement/error message to be sent to FLASH
     * @throws IOException
     */
    public String copyFolder(Integer folderID, Integer targetFolderID, Integer userID) throws IOException {
	FlashMessage flashMessage = null;
	try {
	    if (isUserAuthorizedToModifyFolderContents(targetFolderID, userID)) {
		WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, folderID);
		if (workspaceFolder != null) {
		    WorkspaceFolder newFolder = createFolder(targetFolderID, workspaceFolder.getName(), userID);
		    copyRootContent(workspaceFolder, newFolder, userID);
		    if (workspaceFolder.hasSubFolders()) {
			createSubFolders(workspaceFolder, newFolder, userID);
		    }
		    flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_COPY,
			    newFolder.getWorkspaceFolderId());
		} else {
		    throw new WorkspaceFolderException();
		}
	    } else {
		flashMessage = FlashMessage.getUserNotAuthorized(IWorkspaceManagementService.MSG_KEY_COPY, userID);
	    }
	} catch (UserException ue) {
	    flashMessage = FlashMessage.getNoSuchUserExists(IWorkspaceManagementService.MSG_KEY_COPY, userID);
	} catch (WorkspaceFolderException we) {
	    flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(IWorkspaceManagementService.MSG_KEY_COPY,
		    folderID);
	}
	return flashMessage.serializeMessage();
    }

    /**
     * This method copies a learning design to another folder. Call the AuthoringService to do the copy.
     *
     * @param folderID
     *            The <code>WorkspaceFolder</code> to be copied.
     * @param newFolderID
     *            The parent <code>WorkspaceFolder</code> under which it has to be copied
     * @param userID
     *            The <code>User</code> who has requested this opeartion
     * @return String The acknowledgement/error message to be sent to FLASH
     * @throws IOException
     */
    public String copyLearningDesign(Long designID, Integer targetFolderID, Integer copyType, Integer userID)
	    throws IOException {
	FlashMessage message = null;
	try {
	    LearningDesign ld = authoringService.copyLearningDesign(designID,
		    copyType != null ? copyType : new Integer(LearningDesign.COPY_TYPE_NONE), userID, targetFolderID,
		    false);
	    message = new FlashMessage(IWorkspaceManagementService.MSG_KEY_COPY, ld.getLearningDesignId());

	} catch (Exception e) {
	    log.error("copyLearningDesign() unable to copy learning design due to an error. " + "designID=" + designID
		    + "copyType=" + copyType + "targetFolderID=" + targetFolderID + "userID=" + userID, e);

	    message = new FlashMessage(IWorkspaceManagementService.MSG_KEY_COPY,
		    messageService.getMessage("unable.copy", new Object[] { e.getMessage() }), FlashMessage.ERROR);

	}

	return message.serializeMessage();
    }

    /**
     * This method checks whether the user is authorized to create a new folder or learning design under the given
     * WorkspaceFolder.
     *
     * @param folderID
     *            The <code>workspace_folder_id</code> of the <code>WorkspaceFolder<code>
     * 				   under which the User wants to create/copy folder
     * &#64;param userID
     *            The <code>User</code> being checked
     * @return boolean A boolean value indicating whether or not the <code>User</code> is authorized
     * @throws UserException
     * @throws WorkspaceFolderException
     */
    @Override
    public boolean isUserAuthorizedToModifyFolderContents(Integer folderID, Integer userID)
	    throws UserException, WorkspaceFolderException {
	boolean authorized = false;
	User user = (User) baseDAO.find(User.class, userID);
	if (user != null) {
	    WorkspaceFolder targetParent = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, folderID);
	    if (targetParent != null) {
		Integer permissions = getPermissions(targetParent, user);
		if (!permissions.equals(WorkspaceFolder.NO_ACCESS)
			&& !permissions.equals(WorkspaceFolder.READ_ACCESS)) {
		    authorized = true;
		}
	    } else {
		throw new WorkspaceFolderException();
	    }
	} else {
	    throw new UserException();
	}

	return authorized;
    }

    public void copyRootContent(WorkspaceFolder workspaceFolder, WorkspaceFolder targetWorkspaceFolder, Integer userID)
	    throws UserException, LearningDesignException, UserAccessDeniedException, WorkspaceFolderException {
	User user = (User) baseDAO.find(User.class, userID);
	if (user == null) {
	    throw new UserException(messageService.getMessage("no.such.user", new Object[] { userID }));
	}

	List designs = learningDesignDAO.getAllLearningDesignsInFolder(workspaceFolder.getWorkspaceFolderId());
	if ((designs != null) && (designs.size() != 0)) {
	    Iterator iterator = designs.iterator();
	    while (iterator.hasNext()) {
		LearningDesign design = (LearningDesign) iterator.next();
		authoringService.copyLearningDesign(design, new Integer(LearningDesign.COPY_TYPE_NONE), user,
			targetWorkspaceFolder, false, null, null);
	    }
	}
    }

    @Override
    public WorkspaceFolder createFolder(Integer parentFolderID, String name, Integer userID)
	    throws UserException, WorkspaceFolderException {
	WorkspaceFolder parentFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, parentFolderID);

	if (parentFolder != null) {

	    boolean nameExists = true;
	    while (nameExists) {
		nameExists = ifNameExists(parentFolder, name);
		if (nameExists) {
		    name = name + "C";
		} else {
		    break;
		}
	    }

	    // set type of the new workspaceFolder
	    Integer newWorkspaceFolderType = parentFolder.getWorkspaceFolderType().equals(
		    WorkspaceFolder.PUBLIC_SEQUENCES) ? WorkspaceFolder.PUBLIC_SEQUENCES : WorkspaceFolder.NORMAL;

	    User user = (User) baseDAO.find(User.class, userID);
	    if (user != null) {
		WorkspaceFolder workspaceFolder = new WorkspaceFolder(name, parentFolder, userID, new Date(),
			new Date(), newWorkspaceFolderType);
		baseDAO.insert(workspaceFolder);
		return workspaceFolder;
	    }
	    throw new UserException(messageService.getMessage("no.such.user", new Object[] { userID }));

	}
	throw new WorkspaceFolderException(
		messageService.getMessage("no.such.workspace", new Object[] { parentFolderID }));
    }

    /**
     * TODO For now assuming that the folder to be created would be of type NORMAL. But the type has to be passed in the
     * near future indicating what kind of folder should be created (NORMAL/RUN SEQUENCES)
     *
     * @see org.lamsfoundation.lams.authoring.service.IAuthoringService#createFolder(java.lang.Integer,
     *      java.lang.String, java.lang.Integer)
     */
    @Override
    public String createFolderForFlash(Integer parentFolderID, String name, Integer userID) throws IOException {
	FlashMessage flashMessage = null;
	try {
	    WorkspaceFolder newFolder = createFolder(parentFolderID, name, userID);
	    Hashtable<String, Object> table = new Hashtable<String, Object>();
	    table.put("folderID", newFolder.getWorkspaceFolderId());
	    table.put("name", newFolder.getName());
	    flashMessage = new FlashMessage("createFolderForFlash", table);

	} catch (WorkspaceFolderException we) {
	    flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists("createFolderForFlash", parentFolderID);
	} catch (UserException ue) {
	    flashMessage = FlashMessage.getNoSuchUserExists("createFolderForFlash", userID);
	}
	return flashMessage.serializeMessage();
    }

    private boolean ifNameExists(WorkspaceFolder targetFolder, String folderName) {
	List folders = baseDAO.findByProperty(WorkspaceFolder.class, "parentWorkspaceFolder.workspaceFolderId",
		targetFolder.getWorkspaceFolderId());
	if ((folders != null) && (folders.size() != 0)) {
	    Iterator iterator = folders.iterator();
	    while (iterator.hasNext()) {
		WorkspaceFolder folder = (WorkspaceFolder) iterator.next();
		if (folder.getName().equalsIgnoreCase(folderName)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public void createSubFolders(WorkspaceFolder workspaceFolder, WorkspaceFolder newFolder, Integer userID)
	    throws UserException, WorkspaceFolderException {
	Iterator subFoldersIterator = workspaceFolder.getChildWorkspaceFolders().iterator();
	while (subFoldersIterator.hasNext()) {
	    WorkspaceFolder subFolder = (WorkspaceFolder) subFoldersIterator.next();
	    WorkspaceFolder newSubFolder = createFolder(newFolder.getWorkspaceFolderId(), subFolder.getName(), userID);
	    copyRootContent(subFolder, newSubFolder, userID);
	    if (subFolder.hasSubFolders()) {
		createSubFolders(subFolder, newSubFolder, userID);
	    }
	}
    }

    /**
     * This method deletes a <code>LearningDesign</code> with given <code>learningDesignID</code> provied the
     * <code>User</code> is authorized to do so.
     * <p>
     * <b>Note:</b>
     * </p>
     * <p>
     * <ul>
     * <li>The <code>LearningDesign</code> should not be readOnly, indicating that a <code>Lesson</code> has already
     * been started</li>
     * <li>The given <code>LearningDesign</code> should not be acting as a parent to any other existing
     * <code>LearningDesign's</code></li>
     * </ul>
     * </p>
     *
     * TODO Deleting a LearningDesign would mean deleting all its corresponding activities, transitions and the content
     * related to such activities. Deletion of content has to be yet taken care of. Since Tools manage there own
     * content.Just need to cross-check this once tools are functional
     *
     * @param learningDesignID
     *            The <code>learning_design_id</code> of the <code>LearningDesign</code> to be deleted.
     * @param userID
     *            The <code>user_id</code> of the <code>User</code> who has requested this opeartion
     * @return String The acknowledgement/error message in WDDX format for FLASH
     * @throws IOException
     */
    private String deleteLearningDesignWDDX(Long learningDesignID, Integer userID) throws IOException {
	User user = (User) baseDAO.find(User.class, userID);
	FlashMessage flashMessage = null;
	if (user != null) {
	    LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignID);

	    if (learningDesign != null) {
		if (learningDesign.getUser().getUserId().equals(user.getUserId()) || isSysAuthorAdmin(user)) {
		    if (learningDesign.getReadOnly().booleanValue()) {
			flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_DELETE,
				messageService.getMessage("learningdesign.readonly", new Object[] { learningDesignID }),
				FlashMessage.ERROR);
		    } else {
			learningDesignDAO.delete(learningDesign);
			flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_DELETE,
				messageService.getMessage("learningdesign.delete", new Object[] { learningDesignID }));
		    }
		} else {
		    flashMessage = FlashMessage.getUserNotAuthorized(IWorkspaceManagementService.MSG_KEY_DELETE,
			    user.getUserId());
		}
	    } else {
		flashMessage = FlashMessage.getNoSuchLearningDesignExists(IWorkspaceManagementService.MSG_KEY_DELETE,
			learningDesignID);
	    }
	} else {
	    flashMessage = FlashMessage.getNoSuchUserExists(IWorkspaceManagementService.MSG_KEY_DELETE,
		    user.getUserId());
	}

	return flashMessage.serializeMessage();
    }

    /**
     * This method moves the given <code>WorkspaceFolder</code> with <code>currentFolderID</code> under the
     * WorkspaceFolder with <code>targetFolderID</code>.But before it does so it checks whether the <code>User</code> is
     * authorized to do so.
     *
     * <p>
     * <b>Note: </b> This method doesn't actually copies the content from one place to another. All it does is change
     * the <code>parent_workspace_folder_id</code> of the currentFolder to that of the <code>targetFolder</code>
     * </p>
     *
     * @param currentFolderID
     *            The WorkspaceFolder to be moved
     * @param targetFolderID
     *            The WorkspaceFolder under which it has to be moved
     * @param userID
     *            The User who has requested this opeartion
     * @return String The acknowledgement/error message to be sent to FLASH
     * @throws IOException
     */
    @Override
    public String moveResource(Long resourceID, Integer targetFolderID, String resourceType, Integer userID)
	    throws IOException {
	String errorMessage = null;

	if ((resourceID == null) || (targetFolderID == null) || (resourceType == null) || (userID == null)) {
	    errorMessage = messageService.getMessage("move.resrouce.error.value.miss");
	} else if (FolderContentDTO.DESIGN.equals(resourceType)) {
	    return moveLearningDesign(resourceID, targetFolderID, userID);
	} else if (FolderContentDTO.FOLDER.equals(resourceType)) {
	    return moveFolder(new Integer(resourceID.intValue()), targetFolderID, userID);
	} else if (FolderContentDTO.FILE.equals(resourceType)) {
	    // TODO implement delete file resource
	    errorMessage = messageService.getMessage("unsupport.move");
	} else if (FolderContentDTO.LESSON.equals(resourceType)) {
	    // TODO implement delete lesson
	    errorMessage = messageService.getMessage("unsupport.move");
	}

	FlashMessage message = new FlashMessage(IWorkspaceManagementService.MSG_KEY_MOVE,
		messageService.getMessage("move.resource.error", new Object[] { errorMessage }), FlashMessage.ERROR);
	return message.serializeMessage();

    }

    /**
     */
    private String moveFolder(Integer currentFolderID, Integer targetFolderID, Integer userID) throws IOException {
	FlashMessage flashMessage = null;
	try {
	    if (currentFolderID == null) {
		throw new WorkspaceFolderException("Current folder ID is null");
	    }
	    if (targetFolderID == null) {
		throw new WorkspaceFolderException("Target folder ID is null");
	    }
	    if (currentFolderID.equals(targetFolderID)) {
		throw new WorkspaceFolderException("Current folder is the same as target folder");
	    }
	    if (isUserAuthorizedToModifyFolderContents(targetFolderID, userID)) {
		WorkspaceFolder currentFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, currentFolderID);
		if (currentFolder == null) {
		    throw new WorkspaceFolderException("No current folder found with ID: " + currentFolderID);
		}
		WorkspaceFolder targetFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, targetFolderID);
		if (targetFolder == null) {
		    throw new WorkspaceFolderException("No target folder found with ID: " + targetFolderID);
		}
		currentFolder.setParentWorkspaceFolder(targetFolder);
		baseDAO.update(currentFolder);

		flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_MOVE, targetFolderID);
	    } else {
		flashMessage = FlashMessage.getUserNotAuthorized(IWorkspaceManagementService.MSG_KEY_MOVE, userID);
	    }
	} catch (UserException ue) {
	    flashMessage = FlashMessage.getNoSuchUserExists(IWorkspaceManagementService.MSG_KEY_MOVE, userID);
	} catch (WorkspaceFolderException we) {
	    log.error(we);
	    flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(IWorkspaceManagementService.MSG_KEY_MOVE,
		    targetFolderID);
	}
	return flashMessage.serializeMessage();
    }

    /** Set up the content repository - create the workspace and the credentials. */
    private void configureContentRepository(ICredentials cred) throws RepositoryCheckedException {
	try {
	    repositoryService.createCredentials(cred);
	    repositoryService.addWorkspace(cred, IWorkspaceManagementService.REPOSITORY_WORKSPACE);
	} catch (ItemExistsException ie) {
	    log.warn("Tried to configure repository but it " + " appears to be already configured." + "Workspace name "
		    + IWorkspaceManagementService.REPOSITORY_WORKSPACE
		    + ". Exception thrown by repository being ignored. ", ie);
	} catch (RepositoryCheckedException e) {
	    log.error("Error occured while trying to configure repository." + "Workspace name "
		    + IWorkspaceManagementService.REPOSITORY_WORKSPACE + " Unable to recover from error: "
		    + e.getMessage(), e);
	    throw e;
	}
    }

    /**
     * This method verifies the credentials of the Workspace Manager and gives him the Ticket to login and access the
     * Content Repository. A valid ticket is needed in order to access the content from the repository. This method
     * would be called evertime the user(Workspace Manager) receives a request to get the contents of the Folder or to
     * add/update a file into the <code>WorkspaceFodler</code> (Repository).
     *
     * If the workspace/credential hasn't been set up, then it will be set up automatically.
     *
     * @return ITicket The ticket for repostory access
     */
    private ITicket getRepositoryLoginTicket() {
	ICredentials credentials = new SimpleCredentials(IWorkspaceManagementService.REPOSITORY_USERNAME,
		IWorkspaceManagementService.REPOSITORY_PASSWORD.toCharArray());
	ITicket ticket = null;

	try {
	    try {
		ticket = repositoryService.login(credentials, IWorkspaceManagementService.REPOSITORY_WORKSPACE);
	    } catch (WorkspaceNotFoundException we) {
		log.error("Content Repository workspace " + IWorkspaceManagementService.REPOSITORY_WORKSPACE
			+ " not configured. Attempting to configure now.");
		configureContentRepository(credentials);
		ticket = repositoryService.login(credentials, IWorkspaceManagementService.REPOSITORY_WORKSPACE);
	    }
	} catch (RepositoryCheckedException e) {
	    log.error("Unable to get ticket for workspace " + IWorkspaceManagementService.REPOSITORY_WORKSPACE, e);
	    return null;
	}
	return ticket;
    }

    @Override
    public String createWorkspaceFolderContent(Integer contentTypeID, String name, String description,
	    Integer workspaceFolderID, String mimeType, String path) throws Exception {
	FlashMessage flashMessage = null;

	// TODO add some validation so that a non-unique name doesn't result in an index violation
	// bit hard for the user to understand.

	WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, workspaceFolderID);
	if (workspaceFolder != null) {
	    if (!contentAlreadyExists(workspaceFolder, name, mimeType)) {
		WorkspaceFolderContent workspaceFolderContent = new WorkspaceFolderContent(contentTypeID, name,
			description, new Date(), new Date(), mimeType, workspaceFolder);
		baseDAO.insert(workspaceFolderContent);

		try {
		    InputStream stream = new FileInputStream(path);
		    NodeKey nodeKey = addFileToRepository(stream, name, mimeType);
		    workspaceFolderContent.setUuid(nodeKey.getUuid());
		    workspaceFolderContent.setVersionID(nodeKey.getVersion());
		    baseDAO.update(workspaceFolderContent);

		    UpdateContentDTO contentDTO = new UpdateContentDTO(nodeKey.getUuid(), nodeKey.getVersion(),
			    workspaceFolderContent.getFolderContentID());
		    flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_CREATE_WKF_CONTENT, contentDTO);

		} catch (AccessDeniedException ae) {
		    flashMessage = new FlashMessage(
			    IWorkspaceManagementService.MSG_KEY_CREATE_WKF_CONTENT, messageService
				    .getMessage("creating.workspace.folder.error", new Object[] { ae.getMessage() }),
			    FlashMessage.CRITICAL_ERROR);
		} catch (FileException fe) {
		    flashMessage = new FlashMessage(
			    IWorkspaceManagementService.MSG_KEY_CREATE_WKF_CONTENT, messageService
				    .getMessage("creating.workspace.folder.error", new Object[] { fe.getMessage() }),
			    FlashMessage.CRITICAL_ERROR);

		} catch (InvalidParameterException ip) {
		    flashMessage = new FlashMessage(
			    IWorkspaceManagementService.MSG_KEY_CREATE_WKF_CONTENT, messageService
				    .getMessage("creating.workspace.folder.error", new Object[] { ip.getMessage() }),
			    FlashMessage.CRITICAL_ERROR);
		}
	    } else {
		flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_CREATE_WKF_CONTENT,
			messageService.getMessage("resource.already.exist", new Object[] { name, workspaceFolderID }),
			FlashMessage.ERROR);
	    }
	} else {
	    flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(
		    IWorkspaceManagementService.MSG_KEY_CREATE_WKF_CONTENT, workspaceFolderID);
	}

	return flashMessage.serializeMessage();
    }

    /**
     * Checks whether the content already exists. It will just check the name and mime type of the content that is to be
     * created. If there exists a content with the same mime type and name, then it will return true, otherwise return
     * false.
     *
     * @param workspaceFolder
     * @param name
     * @param mimeType
     * @return
     */
    private boolean contentAlreadyExists(WorkspaceFolder workspaceFolder, String name, String mimeType) {
	Set contentsInFolder = workspaceFolder.getFolderContent();
	Iterator i = contentsInFolder.iterator();
	while (i.hasNext()) {
	    WorkspaceFolderContent existingFolderContent = (WorkspaceFolderContent) i.next();
	    if (existingFolderContent.getMimeType().equalsIgnoreCase(mimeType)
		    && existingFolderContent.getName().equalsIgnoreCase(name)) {
		log.error("Content already exists in the db");
		return true;
	    }
	}
	return false;
    }

    @Override
    public String updateWorkspaceFolderContent(Long folderContentID, String path) throws Exception {
	FlashMessage flashMessage = null;
	InputStream stream = new FileInputStream(path);
	WorkspaceFolderContent workspaceFolderContent = (WorkspaceFolderContent) baseDAO
		.find(WorkspaceFolderContent.class, folderContentID);
	if (workspaceFolderContent != null) {
	    NodeKey nodeKey = updateFileInRepository(workspaceFolderContent, stream);
	    UpdateContentDTO contentDTO = new UpdateContentDTO(nodeKey.getUuid(), nodeKey.getVersion(),
		    folderContentID);
	    flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_UPDATE_WKF_CONTENT, contentDTO);
	} else {
	    flashMessage = FlashMessage.getNoSuchWorkspaceFolderContentExsists(
		    IWorkspaceManagementService.MSG_KEY_UPDATE_WKF_CONTENT, folderContentID);
	}
	return flashMessage.serializeMessage();
    }

    /**
     * This method is called everytime a new content has to be added to the repository. In order to do so first of all a
     * valid ticket is obtained from the Repository hence authenticating the user(WorkspaceManager) and then the
     * corresponding file is added to the repository.
     *
     * @param stream
     *            The <code>InputStream</code> representing the data to be added
     * @param fileName
     *            The name of the file being added
     * @param mimeType
     *            The MIME type of the file (eg. TXT, DOC, GIF etc)
     * @return NodeKey Represents the two part key - UUID and Version.
     * @throws AccessDeniedException
     * @throws FileException
     * @throws InvalidParameterException
     */
    private NodeKey addFileToRepository(InputStream stream, String fileName, String mimeType)
	    throws AccessDeniedException, FileException, InvalidParameterException {
	ITicket ticket = getRepositoryLoginTicket();
	NodeKey nodeKey = repositoryService.addFileItem(ticket, stream, fileName, mimeType, null);
	return nodeKey;
    }

    /**
     * This method is called everytime some content has to be updated into the repository. In order to do so first of
     * all a valid ticket is obtained from the Repository hence authenticating the user(WorkspaceManager) and then the
     * corresponding file is updated to the repository.
     *
     * @param workspaceFolderContent
     *            The content to be updated
     * @param stream
     *            stream The <code>InputStream</code> representing the data to be updated
     * @return NodeKey Represents the two part key - UUID and Version.
     * @throws Exception
     */
    private NodeKey updateFileInRepository(WorkspaceFolderContent workspaceFolderContent, InputStream stream)
	    throws Exception {
	ITicket ticket = getRepositoryLoginTicket();
	NodeKey nodeKey = repositoryService.updateFileItem(ticket, workspaceFolderContent.getUuid(),
		workspaceFolderContent.getName(), stream, workspaceFolderContent.getMimeType(), null);
	workspaceFolderContent.setUuid(nodeKey.getUuid());
	workspaceFolderContent.setVersionID(nodeKey.getVersion());
	baseDAO.update(workspaceFolderContent);
	return nodeKey;
    }

    @Override
    public String deleteContentWithVersion(Long uuid, Long versionToBeDeleted, Long folderContentID) throws Exception {
	FlashMessage flashMessage = null;
	WorkspaceFolderContent workspaceFolderContent = (WorkspaceFolderContent) baseDAO
		.find(WorkspaceFolderContent.class, folderContentID);
	if (workspaceFolderContent != null) {
	    Long databaseVersion = workspaceFolderContent.getVersionID();

	    ITicket ticket = getRepositoryLoginTicket();
	    String files[] = null;
	    try {
		files = repositoryService.deleteVersion(ticket, uuid, versionToBeDeleted);
		if ((files != null) && (files.length > 0)) {
		    String error = "Trying to delete content uuid=" + uuid + " unable to delete files ";
		    for (String filename : files) {
			error = error + " " + filename;
		    }
		    log.warn(error);
		}
	    } catch (ItemNotFoundException ie) {
		flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_DELETE_VERSION, messageService
			.getMessage("no.such.content", new Object[] { versionToBeDeleted, folderContentID }),
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

	    try {
		IVersionedNode latestAvailableNode = repositoryService.getFileItem(ticket, uuid, null);
		Long latestAvailableVersion = latestAvailableNode.getVersion();
		if (databaseVersion.equals(versionToBeDeleted)) {
		    workspaceFolderContent.setVersionID(latestAvailableVersion);
		    baseDAO.update(workspaceFolderContent);
		}
		flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_DELETE_VERSION,
			messageService.getMessage("content.delete.success"));
	    } catch (ItemNotFoundException ie) {
		baseDAO.delete(workspaceFolderContent);
		flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_DELETE_VERSION,
			messageService.getMessage("content.delete.success"));
	    }
	} else {
	    flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_DELETE_VERSION,
		    messageService.getMessage("content.delete.success"));
	}
	return flashMessage.serializeMessage();
    }

    /**
     * This method deletes all versions of the given content (FILE/PACKAGE) fom the repository.
     *
     * @param folderContentID
     *            The content to be deleted
     * @return String Acknowledgement/error message in WDDX format for FLASH
     * @throws Exception
     */
    private String deleteWorkspaceFolderContent(Long folderContentID) throws IOException {
	FlashMessage flashMessage = null;
	WorkspaceFolderContent workspaceFolderContent = (WorkspaceFolderContent) baseDAO
		.find(WorkspaceFolderContent.class, folderContentID);
	if (workspaceFolderContent != null) {
	    baseDAO.delete(workspaceFolderContent);
	    flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_DELETE, "Content deleted");
	} else {
	    flashMessage = FlashMessage.getNoSuchWorkspaceFolderContentExsists(
		    IWorkspaceManagementService.MSG_KEY_DELETE, folderContentID);
	}

	return flashMessage.serializeMessage();

    }

    /**
     * TODO This method returns the contents of the given folder from the repository. As of now I am assuming that a
     * folder contains only FILES and not PACKAGES. This method would be modified in the near future to return a list of
     * PACKAGES contained as well.
     *
     * For every file contained within the given <code>WorkspaceFolder</code> this method also returns a list of all its
     * availabe versions.
     *
     * @param workspaceFolderID
     *            The <code>WorkspaceFolder</code> whose contents have been requested from the Repositor
     * @param permissions
     *            The permissions on this WorkspaceFolder and hence all its contents
     * @return Vector A collection of required information.
     * @throws AccessDeniedException
     * @throws RepositoryCheckedException
     * @throws Exception
     */
    private Vector<FolderContentDTO> getContentsFromRepository(WorkspaceFolder workspaceFolder, Integer permissions,
	    User user) throws RepositoryCheckedException {
	log.debug("Trying to get contents from folder " + workspaceFolder.getName());
	Set children = workspaceFolder.getChildWorkspaceFolders();
	if (children != null) {
	    log.debug(children.size() + "  child workspace folders");
	}
	Set content = workspaceFolder.getFolderContent();
	if ((content == null) || (content.size() == 0)) {
	    return null;
	} else {
	    ITicket ticket = getRepositoryLoginTicket();
	    Vector<FolderContentDTO> repositoryContent = new Vector<FolderContentDTO>();
	    Iterator contentIterator = content.iterator();
	    while (contentIterator.hasNext()) {
		WorkspaceFolderContent workspaceFolderContent = (WorkspaceFolderContent) contentIterator.next();
		SortedSet set = repositoryService.getVersionHistory(ticket, workspaceFolderContent.getUuid());
		repositoryContent.add(new FolderContentDTO(permissions, workspaceFolderContent, set, user));
	    }
	    return repositoryContent;
	}
    }

    /**
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getAccessibleOrganisationWorkspaceFolders(java.lang.Integer)
     */
    @Override
    public Vector getAccessibleOrganisationWorkspaceFolders(Integer userID) throws IOException {
	log.debug("User - " + userID);
	User user = (User) userMgmtService.findById(User.class, userID);
	return getAccessibleOrganisationWorkspaceFolders(user);
    }

    /**
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getAccessibleOrganisationWorkspaceFolders(java.lang.Integer)
     */
    @Override
    public Vector getAccessibleOrganisationWorkspaceFolders(User user) throws IOException {
	Vector<FolderContentDTO> folders = new Vector<FolderContentDTO>();

	if (user != null) {
	    // Get a list of organisations of which the given user is a member
	    Set userMemberships = user.getUserOrganisations();
	    if (userMemberships != null) {
		Iterator memberships = userMemberships.iterator();
		while (memberships.hasNext()) {
		    UserOrganisation member = (UserOrganisation) memberships.next();

		    // Check if the organisation has been removed or hidden
		    // Continue to add folder if organisation is active or archived
		    Organisation org = member.getOrganisation();

		    if (org != null) {
			Integer orgStateId = org.getOrganisationState().getOrganisationStateId();
			if (!(OrganisationState.HIDDEN.equals(orgStateId)
				|| OrganisationState.REMOVED.equals(orgStateId))) {

			    // Only courses have folders - classes don't!
			    WorkspaceFolder orgFolder = org.getNormalFolder();

			    if (orgFolder != null) {
				// Check if the user has write access, which is available
				// only if the user has an AUTHOR, TEACHER or STAFF role. If
				// user has access add that folder to the list.
				Set roles = member.getUserOrganisationRoles();
				if (hasWriteAccess(roles)) {
				    Integer permission = getPermissions(orgFolder, user);
				    if (!permission.equals(WorkspaceFolder.NO_ACCESS)) {
					folders.add(new FolderContentDTO(orgFolder, permission, user));
				    }
				}
			    }
			}
		    }

		}
	    } else {
		log.warn("getAccessibleOrganisationWorkspaceFolders: Trying to get user memberships for user "
			+ user.getUserId() + ". User doesn't belong to any organisations. Returning no folders.");
	    }
	} else {
	    log.warn("getAccessibleOrganisationWorkspaceFolders: User " + user.getUserId()
		    + " does not exist. Returning no folders.");
	}

	// sort folders by their names
	Collections.sort(folders);

	return folders;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getUserWorkspaceFolder(java.lang.Integer)
     */
    @Override
    public FolderContentDTO getUserWorkspaceFolder(Integer userID) throws IOException {
	User user = (User) baseDAO.find(User.class, userID);

	if (user != null) {
	    WorkspaceFolder workspaceFolder = user.getWorkspaceFolder();

	    if (workspaceFolder != null) {
		Integer permissions = getPermissions(workspaceFolder, user);
		return new FolderContentDTO(workspaceFolder, permissions, user);
	    }
	    log.warn("getUserWorkspaceFolder: User " + userID + " does not have a root folder. Returning no folders.");
	} else {
	    log.warn("getUserWorkspaceFolder: User " + userID + " does not exist. Returning no folders.");
	}

	return null;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getPublicWorkspaceFolder(java.lang.Integer)
     */
    @Override
    public FolderContentDTO getPublicWorkspaceFolder(Integer userID) throws IOException {
	User user = (User) baseDAO.find(User.class, userID);

	if (user != null) {
	    WorkspaceFolder publicFolder = null;
	    List list = baseDAO.findByProperty(WorkspaceFolder.class, "workspaceFolderType",
		    WorkspaceFolder.PUBLIC_SEQUENCES);

	    if ((list != null) && (list.size() > 0)) {
		publicFolder = (WorkspaceFolder) list.get(0);
	    }

	    if (publicFolder != null) {
		Integer permissions = getPermissions(publicFolder, user);
		return new FolderContentDTO(publicFolder, permissions, user);
	    }
	}

	return null;
    }

    /**
     * This a utility method that checks whether user has write access. He can save his contents to a folder only if he
     * is an AUTHOR,TEACHER or STAFF
     *
     * @param roles
     *            Set of roles that the user has
     * @return boolean A boolean value indicating whether the user has "write" access or not.
     */
    private boolean hasWriteAccess(Set roles) {
	Iterator roleIterator = roles.iterator();
	while (roleIterator.hasNext()) {
	    UserOrganisationRole userOrganisationRole = (UserOrganisationRole) roleIterator.next();
	    Role role = userOrganisationRole.getRole();
	    if (role.isAuthor() || role.isSysAdmin() || role.isGroupManager()) {
		return true;
	    }
	}
	return false;
    }

    /**
     * This method moves a Learning Design from one workspace folder to another.But before it does that it checks
     * whether the given User is authorized to do so.
     *
     * Nothing is physically moved from one folder to another. It just changes the <code>workspace_folder_id</code> for
     * the given learningdesign in the <code>lams_learning_design_table</code> if the <code>User</code> is authorized to
     * do so.
     *
     * @param learningDesignID
     *            The <code>learning_design_id</code> of the design to be moved
     * @param targetWorkspaceFolderID
     *            The <code>workspaceFolder</code> under which it has to be moved.
     * @param userID
     *            The <code>User</code> who is requesting this operation
     * @return String Acknowledgement/error message in WDDX format for FLASH
     * @throws IOException
     */
    public String moveLearningDesign(Long learningDesignID, Integer targetWorkspaceFolderID, Integer userID)
	    throws IOException {
	FlashMessage flashMessage = null;
	try {
	    if (isUserAuthorizedToModifyFolderContents(targetWorkspaceFolderID, userID)) {
		LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignID);
		if (learningDesign != null) {
		    WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class,
			    targetWorkspaceFolderID);
		    if (workspaceFolder != null) {
			learningDesign.setWorkspaceFolder(workspaceFolder);
			learningDesignDAO.update(learningDesign);
			flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_MOVE,
				targetWorkspaceFolderID);
		    } else {
			flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(
				IWorkspaceManagementService.MSG_KEY_MOVE, targetWorkspaceFolderID);
		    }
		} else {
		    flashMessage = FlashMessage.getNoSuchLearningDesignExists(IWorkspaceManagementService.MSG_KEY_MOVE,
			    learningDesignID);
		}
	    } else {
		flashMessage = FlashMessage.getUserNotAuthorized(IWorkspaceManagementService.MSG_KEY_MOVE, userID);
	    }
	} catch (UserException ue) {
	    flashMessage = FlashMessage.getNoSuchUserExists(IWorkspaceManagementService.MSG_KEY_MOVE, userID);
	} catch (WorkspaceFolderException we) {
	    flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(IWorkspaceManagementService.MSG_KEY_MOVE,
		    targetWorkspaceFolderID);
	}
	return flashMessage.serializeMessage();
    }

    /**
     * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#renameResource(Long, String, String,
     *      Integer)
     */
    @Override
    public String renameResource(Long resourceID, String resourceType, String newName, Integer userID)
	    throws IOException {
	String errorMessage = null;

	if ((resourceID == null) || (newName == null) || (resourceType == null) || (userID == null)) {
	    errorMessage = messageService.getMessage("rename.resource.error.miss.vaue");
	} else if (FolderContentDTO.DESIGN.equals(resourceType)) {
	    return renameLearningDesign(resourceID, newName, userID);
	} else if (FolderContentDTO.FOLDER.equals(resourceType)) {
	    return renameWorkspaceFolder(new Integer(resourceID.intValue()), newName, userID);
	} else if (FolderContentDTO.FILE.equals(resourceType)) {
	    // TODO implement delete file resource
	    errorMessage = messageService.getMessage("rename.resource.unspport");
	} else if (FolderContentDTO.LESSON.equals(resourceType)) {
	    // TODO implement delete lesson
	    errorMessage = messageService.getMessage("rename.resource.unspport");
	}

	FlashMessage message = new FlashMessage(IWorkspaceManagementService.MSG_KEY_RENAME,
		messageService.getMessage("rename.resource.error", new Object[] { errorMessage }), FlashMessage.ERROR);
	return message.serializeMessage();

    }

    /**
     * This method renames the <code>workspaceFolder</code> with the given <code>workspaceFolderID</code> to
     * <code>newName</code>. But before it does that it checks if the user is authorized to do so.
     *
     * @param workspaceFolderID
     *            The <code>workspaceFolder</code> to be renamed
     * @param newName
     *            The <code>newName</code> to be assigned
     * @param userID
     *            The <code>User</code> who requested this operation
     * @return String Acknowledgement/error message in WDDX format for FLASH
     * @throws IOException
     */
    public String renameWorkspaceFolder(Integer workspaceFolderID, String newName, Integer userID) throws IOException {
	FlashMessage flashMessage = null;
	try {
	    WorkspaceFolder folder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, workspaceFolderID);
	    if (folder != null) {
		WorkspaceFolder parent = folder.getParentWorkspaceFolder();
		if ((parent != null) && isUserAuthorizedToModifyFolderContents(workspaceFolderID, userID)) {
		    if (!ifNameExists(parent, newName)) {
			folder.setName(newName);
			baseDAO.update(folder);
			flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_RENAME, newName);
		    } else {
			flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_RENAME,
				messageService.getMessage("folder.already.exist", new Object[] { newName }),
				FlashMessage.ERROR);
		    }
		} else {
		    flashMessage = FlashMessage.getUserNotAuthorized(IWorkspaceManagementService.MSG_KEY_RENAME,
			    userID);
		}
	    } else {
		flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(IWorkspaceManagementService.MSG_KEY_RENAME,
			workspaceFolderID);
	    }
	} catch (UserException ue) {
	    flashMessage = FlashMessage.getNoSuchUserExists(IWorkspaceManagementService.MSG_KEY_RENAME, userID);
	} catch (WorkspaceFolderException we) {
	    flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(IWorkspaceManagementService.MSG_KEY_RENAME,
		    workspaceFolderID);
	}
	return flashMessage.serializeMessage();
    }

    /**
     * This method renames the Learning design with given <code>learningDesignID</code> to the new <code>title</code>.
     * But before it does that it checks if the user is authorized to do so.
     *
     * @param learningDesignID
     *            The <code>learning_design_id</code> of the design to be renamed
     * @param title
     *            The new title
     * @param userID
     *            The <code>User</code> who requested this operation
     * @return String Acknowledgement/error message in WDDX format for FLASH
     * @throws IOException
     */
    public String renameLearningDesign(Long learningDesignID, String title, Integer userID) throws IOException {
	FlashMessage flashMessage = null;
	LearningDesign design = learningDesignDAO.getLearningDesignById(learningDesignID);
	Integer folderID = null;
	try {
	    if (design != null) {
		folderID = design.getWorkspaceFolder().getWorkspaceFolderId();
		if (isUserAuthorizedToModifyFolderContents(folderID, userID)) {
		    design.setTitle(title);
		    learningDesignDAO.update(design);
		    flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_RENAME, title);
		} else {
		    flashMessage = FlashMessage.getUserNotAuthorized(IWorkspaceManagementService.MSG_KEY_RENAME,
			    userID);
		}
	    } else {
		flashMessage = FlashMessage.getNoSuchLearningDesignExists(IWorkspaceManagementService.MSG_KEY_RENAME,
			learningDesignID);
	    }
	} catch (UserException ue) {
	    flashMessage = FlashMessage.getNoSuchUserExists(IWorkspaceManagementService.MSG_KEY_RENAME, userID);
	} catch (WorkspaceFolderException we) {
	    flashMessage = FlashMessage.getNoSuchWorkspaceFolderExsists(IWorkspaceManagementService.MSG_KEY_RENAME,
		    folderID);
	}
	return flashMessage.serializeMessage();
    }

    /**
     * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getOrganisationsByUserRole(Integer,
     *      List<String>, Integer, List<Integer>)
     */
    @Override
    public String getOrganisationsByUserRole(Integer userID, List<String> roleNames, Integer courseId,
	    List<Integer> restrictToClassIds) throws IOException {
	FlashMessage flashMessage = null;
	User user = (User) baseDAO.find(User.class, userID);

	if (user != null) {
	    if (courseId == null) {
		flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_ORG_BY_ROLE,
			userMgmtService.getOrganisationsForUserByRole(user, roleNames));
	    } else {
		flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_ORG_BY_ROLE,
			userMgmtService.getOrganisationsForUserByRole(user, roleNames, courseId, restrictToClassIds));
	    }
	} else {
	    flashMessage = FlashMessage.getNoSuchUserExists(IWorkspaceManagementService.MSG_KEY_ORG_BY_ROLE, userID);
	}

	return flashMessage.serializeMessage();

    }

    /**
     * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getUserOrganisation(Integer, String)
     */
    @Override
    public String getUserOrganisation(Integer userID, Integer organisationId) throws IOException {
	FlashMessage flashMessage = null;
	User user = (User) baseDAO.find(User.class, userID);

	if (user != null) {
	    OrganisationDTO orgDTO = userMgmtService.getOrganisationForUserWithRole(user, organisationId);
	    if (orgDTO != null) {
		flashMessage = new FlashMessage(IWorkspaceManagementService.MSG_KEY_ORG, orgDTO);
	    } else {
		flashMessage = FlashMessage.getNoSuchOrganisationExists(IWorkspaceManagementService.MSG_KEY_ORG,
			organisationId);
	    }
	} else {
	    flashMessage = FlashMessage.getNoSuchUserExists(IWorkspaceManagementService.MSG_KEY_ORG_BY_ROLE, userID);
	}

	return flashMessage.serializeMessage();

    }

    /**
     * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#getUsersFromOrganisationByRole(Integer,
     *      String)
     */
    @Override
    public Vector<UserFlashDTO> getUsersFromOrganisationByRole(Integer organisationID, String roleName) {
	return userMgmtService.getUsersFromOrganisationByRole(organisationID, roleName, true, false);
    }

    /**
     * Get the workspace folders for a particular name. Does not check the user permissions - that will be checked if
     * you try to get anything from the folder.
     */
    @Override
    public List<WorkspaceFolder> getWorkspaceFolder(String workspaceFolderName) {
	return baseDAO.findByProperty(WorkspaceFolder.class, "name", workspaceFolderName);
    }

}
