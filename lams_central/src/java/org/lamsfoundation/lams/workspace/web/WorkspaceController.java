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

package org.lamsfoundation.lams.workspace.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Manpreet Minhas
 */
@Controller
@RequestMapping("/workspace")
public class WorkspaceController {

    protected Logger log = Logger.getLogger(WorkspaceController.class.getName());

    public static final String RESOURCE_ID = "resourceID";
    public static final String RESOURCE_TYPE = "resourceType";
    public static final String ROLE_DELIMITER = ",";
    /**
     * Special value for folderID on getFolderContents(). Triggers getting the dummy value for the organisations (see
     * ORG_FOLDER_ID) and the user's private folder. See the method for more details.
     */
    public static final Integer BOOTSTRAP_FOLDER_ID = new Integer(-1);
    /**
     * Special value for folderID on getFolderContents(). Triggers getting the organisation folders that are available
     * to a user. See the method for more details.
     */
    public static final Integer ORG_FOLDER_ID = new Integer(-2);
    public static final Integer ROOT_ORG_FOLDER_ID = new Integer(1);

    @Autowired
    private IWorkspaceManagementService workspaceManagementService;

    private Integer getUserId() {
	// return new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    /**
     * Is the folder id one of our special dummy ids? If so, we can't process the normal create, copy, paste, move
     * functions on this folder.
     */
    private boolean checkResourceDummyValue(Long folderID, String resourceType) throws IOException {
	return FolderContentDTO.FOLDER.equals(resourceType) && (WorkspaceController.BOOTSTRAP_FOLDER_ID.equals(folderID)
		|| WorkspaceController.ORG_FOLDER_ID.equals(folderID));
    }

    /**
     * For details please refer to org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws ServletException
     * @throws IOException
     * @throws WorkspaceFolderException
     * @throws UserException
     */
    @ResponseBody
    @RequestMapping(path = "/createFolder", method = RequestMethod.POST)
    public void createFolder(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException, UserException, WorkspaceFolderException {
	Integer parentFolderID = WebUtil.readIntParam(request, "parentFolderID", false);
	String folderName = WebUtil.readStrParam(request, "name", false);
	Integer userID = getUserId();
	WorkspaceFolder newFolder = workspaceManagementService.createFolder(parentFolderID, folderName, userID);
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().write(newFolder.getWorkspaceFolderId().toString());
    }

    /**
     * For details please refer to org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws ServletException
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping(path = "/deleteResource", method = RequestMethod.POST)
    public void deleteResource(HttpServletRequest request) throws ServletException, IOException {
	Long resourceID = new Long(WebUtil.readLongParam(request, WorkspaceController.RESOURCE_ID));
	String resourceType = WebUtil.readStrParam(request, WorkspaceController.RESOURCE_TYPE);

	boolean isDummyValue = checkResourceDummyValue(resourceID, FolderContentDTO.FOLDER);
	if (isDummyValue) {
	    throw new IOException("Can not remove this resource.");
	}

	workspaceManagementService.deleteResource(resourceID, resourceType, getUserId());
    }

    /**
     * For details please refer to org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws ServletException
     * @throws IOException
     * @throws WorkspaceFolderException
     * @throws UserException
     * @throws LearningDesignException
     */
    @ResponseBody
    @RequestMapping(path = "/copyResource", method = RequestMethod.POST)
    public void copyResource(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException, LearningDesignException, UserException, WorkspaceFolderException {
	Long resourceID = WebUtil.readLongParam(request, WorkspaceController.RESOURCE_ID, false);
	String resourceType = WebUtil.readStrParam(request, WorkspaceController.RESOURCE_TYPE, false);
	Integer targetFolderID = WebUtil.readIntParam(request, "targetFolderID", false);

	if (targetFolderID == null) {
	    log.error("Can not copy resource, missing target folder ID");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing target folder ID");
	}

	Integer copyType = WebUtil.readIntParam(request, "copyType", true);
	Integer userID = getUserId();
	workspaceManagementService.copyResource(resourceID, resourceType, copyType, targetFolderID, userID);
    }

    @ResponseBody
    @RequestMapping(path = "/moveResource", method = RequestMethod.POST)
    public void moveResource(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Long resourceID = WebUtil.readLongParam(request, WorkspaceController.RESOURCE_ID, false);
	String resourceType = WebUtil.readStrParam(request, WorkspaceController.RESOURCE_TYPE, false);
	Integer targetFolderID = WebUtil.readIntParam(request, "targetFolderID", false);

	if (targetFolderID == null) {
	    log.error("Can not move resource, missing target folder ID");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing target folder ID");
	}

	try {
	    workspaceManagementService.moveResource(resourceID, resourceType, targetFolderID);
	} catch (WorkspaceFolderException e) {
	    log.error("Error while moving a resource", e);
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error while moving a resource");
	}
    }

    @ResponseBody
    @RequestMapping(path = "/renameResource", method = RequestMethod.POST)
    public void renameResource(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException, UserException, WorkspaceFolderException {
	Integer userID = getUserId();
	Long resourceID = new Long(WebUtil.readLongParam(request, WorkspaceController.RESOURCE_ID));
	String resourceType = WebUtil.readStrParam(request, WorkspaceController.RESOURCE_TYPE);

	boolean isDummyValue = checkResourceDummyValue(resourceID, FolderContentDTO.FOLDER);
	if (isDummyValue) {
	    throw new IOException("Can not rename this resource");
	}
	String name = WebUtil.readStrParam(request, "name");
	workspaceManagementService.renameResource(resourceID, resourceType, name, userID);

    }
}
