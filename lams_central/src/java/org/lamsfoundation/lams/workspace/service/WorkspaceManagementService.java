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

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.authoring.service.IAuthoringFullService;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
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
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.workspace.WorkspaceFolderContent;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.web.WorkspaceController;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Manpreet Minhas
 */
public class WorkspaceManagementService implements IWorkspaceManagementService {

    protected Logger log = Logger.getLogger(WorkspaceManagementService.class.getName());

    public static final Integer AUTHORING = new Integer(1);
    public static final Integer MONITORING = new Integer(2);

    protected IBaseDAO baseDAO;
    protected ILearningDesignDAO learningDesignDAO;

    protected IAuthoringFullService authoringFullService;
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
     * @param authoringFullService
     *            The authoringFullService to set.
     */
    public void setAuthoringService(IAuthoringFullService authoringFullService) {
	this.authoringFullService = authoringFullService;
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
    public void deleteResource(Long resourceID, String resourceType, Integer userID) throws IOException {
	if (FolderContentDTO.DESIGN.equals(resourceType)) {
	    deleteLearningDesign(resourceID, userID);
	} else if (FolderContentDTO.FOLDER.equals(resourceType)) {
	    deleteFolder(new Integer(resourceID.intValue()), userID);
	} else if (FolderContentDTO.FILE.equals(resourceType)) {
	    deleteWorkspaceFolderContent(resourceID);
	}
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
     * @throws IOException
     */
    private void deleteFolder(Integer folderID, Integer userID) throws IOException {
	WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, folderID);
	User user = (User) userMgmtService.findById(User.class, userID);
	deleteFolder(workspaceFolder, user, isSysAuthorAdmin(user));
    }

    private void deleteFolder(WorkspaceFolder workspaceFolder, User user, boolean isSysAuthorAdmin) throws IOException {
	if (!getPermissions(workspaceFolder, user).equals(WorkspaceFolder.OWNER_ACCESS)) {
	    throw new IOException("User is not authorised to delete a folder");
	}
	deleteFolderContents(workspaceFolder, user, isSysAuthorAdmin);
    }

    /**
     * This method will try to delete the <code>folder</code>. If the folder is not empty it will attempt to delete all
     * resources inside the folder. If the user does not have permission to delete that resource then an error will be
     * returned indicating which resource it was unable to delete. If all resources inside that <code>folder</code> are
     * successfully deleted, then the folder will be deleted and an error will be returned indicating it was
     * successfully deleted.
     *
     * @param folder
     */
    private void deleteFolderContents(WorkspaceFolder folder, User user, boolean isSysAuthorAdmin) throws IOException {
	boolean isDeleteSuccessful = true;

	if (!folder.isEmpty()) {
	    if (folder.hasSubFolders()) {
		Set subFolders = folder.getChildWorkspaceFolders();
		Iterator subFolderIterator = subFolders.iterator();
		while (subFolderIterator.hasNext()) {
		    WorkspaceFolder subFolder = (WorkspaceFolder) subFolderIterator.next();
		    deleteFolder(subFolder, user, isSysAuthorAdmin);
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
			    } else {
				folder.getLearningDesigns().remove(learningDesign);
				learningDesign.setWorkspaceFolder(null);
			    }
			}
		    }
		}

		// this call will delete the files and learning designs which were removed from the collection above.
		baseDAO.update(folder);
	    }
	}

	// it will only delete this folder if all the files/folder/learningDesigns are all deleted
	if (isDeleteSuccessful) {
	    baseDAO.delete(folder);
	}
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
	Vector<FolderContentDTO> contentDTO = new Vector<>();
	if (user != null) {
	    Integer permissions = getPermissions(workspaceFolder, user);
	    if (permissions != WorkspaceFolder.NO_ACCESS) {
		getFolderContent(workspaceFolder, permissions, mode, contentDTO, user);
		if (workspaceFolder.hasSubFolders()) {
		    getSubFolderDetails(workspaceFolder, user, contentDTO, skipFolder);
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
    public String getFolderContentsJSON(Integer folderID, Integer userID, boolean allowInvalidDesigns)
	    throws IOException, UserAccessDeniedException, RepositoryCheckedException {
	return getFolderContentsJSON(folderID, userID, allowInvalidDesigns, false, "all");
    }

    @Override
    public String getFolderContentsJSON(Integer folderID, Integer userID, boolean allowInvalidDesigns,
	    String designType) throws IOException, UserAccessDeniedException, RepositoryCheckedException {

	return getFolderContentsJSON(folderID, userID, allowInvalidDesigns, false, designType);
    }

    public String getFolderContentsJSON(Integer folderID, Integer userID, boolean allowInvalidDesigns,
	    boolean designsOnly, String designType)
	    throws IOException, UserAccessDeniedException, RepositoryCheckedException {
	ObjectNode result = JsonNodeFactory.instance.objectNode();
	Vector<FolderContentDTO> folderContents = null;

	// folderID == null: get all user accessible folders
	// folderID == -1: get the learning designs at the root of the user's home folder - used for simplified deployment
	if ((folderID == null) || (folderID == -1)) {

	    FolderContentDTO userFolder = getUserWorkspaceFolder(userID);

	    if (folderID == null) {
		folderContents = new Vector<>(3);

		if (userFolder != null) {
		    folderContents.add(userFolder);
		}

		FolderContentDTO myGroupsFolder = new FolderContentDTO(messageService.getMessage("organisations"),
			messageService.getMessage("folder"), null, null, FolderContentDTO.FOLDER,
			WorkspaceController.ORG_FOLDER_ID.longValue(), WorkspaceFolder.READ_ACCESS, null);

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
	} else if (folderID.equals(WorkspaceController.ORG_FOLDER_ID)) {
	    folderContents = getAccessibleOrganisationWorkspaceFolders(userID);
	    Collections.sort(folderContents);

	    if (folderContents.size() == 1) {
		FolderContentDTO folder = folderContents.firstElement();
		if (folder.getResourceID().equals(WorkspaceController.ROOT_ORG_FOLDER_ID)) {
		    return getFolderContentsJSON(WorkspaceController.ROOT_ORG_FOLDER_ID, userID, allowInvalidDesigns);
		}
	    }
	} else {
	    WorkspaceFolder folder = getWorkspaceFolder(folderID);
	    Integer mode = allowInvalidDesigns ? WorkspaceManagementService.AUTHORING
		    : WorkspaceManagementService.MONITORING;
	    folderContents = getFolderContents(userID, folder, mode);
	    Collections.sort(folderContents);
	}

	User user = (User) baseDAO.find(User.class, userID);

	// fill JSON object with folders and LDs
	for (FolderContentDTO folderContent : folderContents) {
	    String contentType = folderContent.getResourceType();
	    if (FolderContentDTO.FOLDER.equals(contentType) && !designsOnly) {
		ObjectNode subfolderJSON = JsonNodeFactory.instance.objectNode();
		subfolderJSON.put("name", folderContent.getName() == null ? "" : folderContent.getName());
		subfolderJSON.put("isRunSequencesFolder",
			WorkspaceFolder.RUN_SEQUENCES.equals(folderContent.getResourceTypeID() == null ? null
				: folderContent.getResourceTypeID().intValue()));
		subfolderJSON.put("folderID", folderContent.getResourceID().intValue());
		subfolderJSON.put("canModify", WorkspaceFolder.OWNER_ACCESS.equals(folderContent.getPermissionCode())
			|| ((user != null) && isSysAuthorAdmin(user)));
		result.withArray("folders").add(subfolderJSON);
	    } else if (FolderContentDTO.DESIGN.equals(contentType)) {
		if (folderContent.getDesignType() == null) {
		    folderContent.setDesignType(WorkspaceManagementService.DEFAULT_DESIGN_TYPE);
		}
		// no designType: get only authored LDs
		// designType=all: get all template LDs
		// designType=someting: get only "something" templateLDs
		if (designType == null
			? folderContent.getDesignType().equals(WorkspaceManagementService.DEFAULT_DESIGN_TYPE)
			: (designType.equals(WorkspaceManagementService.ALL_DESIGN_TYPES)
				|| designType.equals(folderContent.getDesignType()))) {
		    ObjectNode learningDesignJSON = JsonNodeFactory.instance.objectNode();
		    learningDesignJSON.put("name", folderContent.getName() == null ? "" : folderContent.getName());
		    learningDesignJSON.put("learningDesignId", folderContent.getResourceID());
		    JsonUtil.putOpt(learningDesignJSON, "type", folderContent.getDesignType());
		    learningDesignJSON.put("date", folderContent.getLastModifiedDateTime().toString());
		    learningDesignJSON.put("canModify",
			    WorkspaceFolder.OWNER_ACCESS.equals(folderContent.getPermissionCode())
				    || ((user != null) && isSysAuthorAdmin(user)));
		    learningDesignJSON.put("readOnly", folderContent.getReadOnly());
		    result.withArray("learningDesigns").add(learningDesignJSON);
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
    public String getPagedLearningDesignsJSON(Integer userID, boolean allowInvalidDesigns, String searchString,
	    int page, int size, String sortName, String sortDate) throws IOException {
	ArrayNode resultJSON = JsonNodeFactory.instance.arrayNode();
	Pattern searchPattern = searchString != null
		? Pattern.compile(Pattern.quote(searchString), Pattern.CASE_INSENSITIVE)
		: null;
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
		    ObjectNode learningDesignJSON = JsonNodeFactory.instance.objectNode();
		    learningDesignJSON.put("name", HtmlUtils.htmlEscape(design.getTitle()));
		    learningDesignJSON.put("learningDesignId", design.getLearningDesignId());
		    learningDesignJSON.put("type", design.getDesignType() != null ? design.getDesignType()
			    : WorkspaceManagementService.DEFAULT_DESIGN_TYPE);
		    learningDesignJSON.put("date", design.getLastModifiedDateTime().toString());
		    resultJSON.add(learningDesignJSON);
		}
	    }

	    // what is the total number (so the pager knows whether to allow paging)
	    // if we did a search, then no paging just return the whole lot.
	    // otherwise need the whole count from the db.
	    numDesigns = searchPattern != null ? resultJSON.size()
		    : learningDesignDAO.countAllLearningDesigns(folderId, !allowInvalidDesigns);
	}

	ObjectNode completeResult = JsonNodeFactory.instance.objectNode();
	completeResult.put("total_rows", numDesigns);
	if (resultJSON.size() > 0) {
	    completeResult.set("rows", resultJSON);
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
	return userMgmtService.hasRoleInOrganisation(user, Role.ROLE_APPADMIN);
    }

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
    public void copyResource(Long resourceID, String resourceType, Integer copyType, Integer targetFolderID,
	    Integer userID) throws LearningDesignException, UserException, WorkspaceFolderException, IOException {
	if (FolderContentDTO.DESIGN.equals(resourceType)) {
	    authoringFullService.copyLearningDesign(resourceID,
		    copyType != null ? copyType : new Integer(LearningDesign.COPY_TYPE_NONE), userID, targetFolderID,
		    false);
	} else if (FolderContentDTO.FOLDER.equals(resourceType)) {
	    copyFolder(new Integer(resourceID.intValue()), targetFolderID, userID);
	}
    }

    @Override
    public void moveResource(Long resourceID, String resourceType, Integer targetFolderID)
	    throws WorkspaceFolderException {
	WorkspaceFolder targetFolder = getWorkspaceFolder(targetFolderID);
	if (FolderContentDTO.DESIGN.equals(resourceType)) {
	    LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(resourceID);
	    learningDesign.setWorkspaceFolder(targetFolder);
	    learningDesignDAO.insertOrUpdate(learningDesign);
	} else if (FolderContentDTO.FOLDER.equals(resourceType)) {
	    WorkspaceFolder folder = getWorkspaceFolder(resourceID.intValue());
	    WorkspaceFolder parent = targetFolder.getParentWorkspaceFolder();
	    while (parent != null) {
		if (parent.equals(folder)) {
		    throw new WorkspaceFolderException("Can not move a folder into its descendant");
		}
		parent = parent.getParentWorkspaceFolder();
	    }
	    folder.setParentWorkspaceFolder(targetFolder);
	    baseDAO.update(folder);
	}
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
     * @throws IOException
     * @throws WorkspaceFolderException
     * @throws UserException
     * @throws UserAccessDeniedException
     * @throws LearningDesignException
     */
    public void copyFolder(Integer folderID, Integer targetFolderID, Integer userID) throws IOException,
	    WorkspaceFolderException, LearningDesignException, UserAccessDeniedException, UserException {
	if (isUserAuthorizedToModifyFolderContents(targetFolderID, userID)) {
	    WorkspaceFolder workspaceFolder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, folderID);
	    if (workspaceFolder != null) {
		WorkspaceFolder newFolder = createFolder(targetFolderID, workspaceFolder.getName(), userID);
		copyRootContent(workspaceFolder, newFolder, userID);
		if (workspaceFolder.hasSubFolders()) {
		    createSubFolders(workspaceFolder, newFolder, userID);
		}
	    } else {
		throw new WorkspaceFolderException("Workspace folder not found, ID: " + folderID);
	    }
	} else {
	    throw new IOException("User " + userID + " is not authorized to copy folder " + targetFolderID);
	}
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
	User user = (User) baseDAO.find(User.class, userID);
	if (user != null) {
	    WorkspaceFolder targetParent = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, folderID);
	    if (targetParent != null) {
		Integer permissions = getPermissions(targetParent, user);
		return !permissions.equals(WorkspaceFolder.NO_ACCESS)
			&& !permissions.equals(WorkspaceFolder.READ_ACCESS);
	    } else {
		throw new WorkspaceFolderException();
	    }
	} else {
	    throw new UserException();
	}
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
		authoringFullService.copyLearningDesign(design, new Integer(LearningDesign.COPY_TYPE_NONE), user,
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
     * @throws IOException
     */
    private void deleteLearningDesign(Long learningDesignID, Integer userID) throws IOException {
	User user = (User) baseDAO.find(User.class, userID);
	if (user == null) {
	    throw new IOException("User could not be found, ID: " + userID);
	}

	LearningDesign learningDesign = learningDesignDAO.getLearningDesignById(learningDesignID);
	if (learningDesign == null) {
	    throw new IOException("Learning Design could not be found, ID: " + learningDesignID);
	}

	if (learningDesign.getUser().getUserId().equals(user.getUserId()) || isSysAuthorAdmin(user)) {
	    if (learningDesign.getReadOnly()) {
		throw new IOException("Learning Design is read-only, ID: " + learningDesignID);
	    }
	    learningDesignDAO.delete(learningDesign);
	} else {
	    throw new IOException("User is not authorized to delete Learning Design, ID: " + learningDesignID);
	}
    }

    /**
     * This method deletes all versions of the given content (FILE/PACKAGE) fom the repository.
     *
     * @param folderContentID
     *            The content to be deleted
     * @throws Exception
     */
    private void deleteWorkspaceFolderContent(Long folderContentID) throws IOException {
	WorkspaceFolderContent workspaceFolderContent = (WorkspaceFolderContent) baseDAO
		.find(WorkspaceFolderContent.class, folderContentID);
	if (workspaceFolderContent != null) {
	    baseDAO.delete(workspaceFolderContent);
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
	Vector<FolderContentDTO> folders = new Vector<>();

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
	    if (role.isAuthor() || role.isAppAdmin() || role.isGroupManager()) {
		return true;
	    }
	}
	return false;
    }

    /**
     * @throws WorkspaceFolderException
     * @throws UserException
     * @see org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService#renameResource(Long, String, String,
     *      Integer)
     */
    @Override
    public void renameResource(Long resourceID, String resourceType, String newName, Integer userID)
	    throws IOException, UserException, WorkspaceFolderException {
	if (FolderContentDTO.DESIGN.equals(resourceType)) {
	    renameLearningDesign(resourceID, newName, userID);
	} else if (FolderContentDTO.FOLDER.equals(resourceType)) {
	    renameWorkspaceFolder(new Integer(resourceID.intValue()), newName, userID);
	}
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
     * @throws IOException
     * @throws WorkspaceFolderException
     * @throws UserException
     */
    public void renameWorkspaceFolder(Integer workspaceFolderID, String newName, Integer userID)
	    throws IOException, UserException, WorkspaceFolderException {
	WorkspaceFolder folder = (WorkspaceFolder) baseDAO.find(WorkspaceFolder.class, workspaceFolderID);
	if (folder != null) {
	    WorkspaceFolder parent = folder.getParentWorkspaceFolder();
	    if ((parent != null) && isUserAuthorizedToModifyFolderContents(workspaceFolderID, userID)) {
		if (!ifNameExists(parent, newName)) {
		    folder.setName(newName);
		    baseDAO.update(folder);
		} else {
		    throw new IOException("Name already exists");
		}
	    } else {
		throw new IOException(
			"User " + userID + " is not authorized to modify folder contents " + workspaceFolderID);
	    }
	} else {
	    throw new IOException("Could not find folder, ID: " + workspaceFolderID);
	}
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
     * @throws IOException
     * @throws WorkspaceFolderException
     * @throws UserException
     */
    public void renameLearningDesign(Long learningDesignID, String title, Integer userID)
	    throws IOException, UserException, WorkspaceFolderException {
	LearningDesign design = learningDesignDAO.getLearningDesignById(learningDesignID);
	Integer folderID = null;
	if (design != null) {
	    folderID = design.getWorkspaceFolder().getWorkspaceFolderId();
	    if (isUserAuthorizedToModifyFolderContents(folderID, userID)) {
		design.setTitle(title);
		learningDesignDAO.update(design);
	    } else {
		throw new IOException("User " + userID + " + is not authorized to modify folder " + folderID);
	    }
	} else {
	    throw new IOException("Could not find Learning Design, ID: " + learningDesignID);
	}
    }
}