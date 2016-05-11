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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Manpreet Minhas
 *
 *
 */
public class WorkspaceAction extends LamsDispatchAction {

    protected Logger log = Logger.getLogger(WorkspaceAction.class.getName());

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

    private Integer getUserId() {
	// return new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	return user != null ? user.getUserID() : null;
    }

    public IWorkspaceManagementService getWorkspaceManagementService() {
	WebApplicationContext webContext = WebApplicationContextUtils
		.getRequiredWebApplicationContext(this.getServlet().getServletContext());
	return (IWorkspaceManagementService) webContext.getBean("workspaceManagementService");
    }

    /**
     * Is the folder id one of our special dummy ids? If so, we can't process the normal create, copy, paste, move
     * functions on this folder.
     */
    private boolean checkResourceDummyValue(Long folderID, String resourceType) throws IOException {
	return FolderContentDTO.FOLDER.equals(resourceType) && (WorkspaceAction.BOOTSTRAP_FOLDER_ID.equals(folderID)
		|| WorkspaceAction.ORG_FOLDER_ID.equals(folderID));
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
    public ActionForward createFolder(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response)
	    throws ServletException, IOException, UserException, WorkspaceFolderException {
	Integer parentFolderID = WebUtil.readIntParam(request, "parentFolderID", false);
	String folderName = WebUtil.readStrParam(request, "name", false);
	Integer userID = getUserId();
	WorkspaceFolder newFolder = getWorkspaceManagementService().createFolder(parentFolderID, folderName, userID);
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().write(newFolder.getWorkspaceFolderId().toString());
	return null;
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
    public ActionForward deleteResource(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	Long resourceID = new Long(WebUtil.readLongParam(request, WorkspaceAction.RESOURCE_ID));
	String resourceType = WebUtil.readStrParam(request, WorkspaceAction.RESOURCE_TYPE);

	boolean isDummyValue = checkResourceDummyValue(resourceID, FolderContentDTO.FOLDER);
	if (isDummyValue) {
	    throw new IOException("Can not remove this resource.");
	}

	getWorkspaceManagementService().deleteResource(resourceID, resourceType, getUserId());
	return null;

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
    public ActionForward copyResource(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response)
	    throws ServletException, IOException, LearningDesignException, UserException, WorkspaceFolderException {
	Long resourceID = WebUtil.readLongParam(request, WorkspaceAction.RESOURCE_ID, false);
	String resourceType = WebUtil.readStrParam(request, WorkspaceAction.RESOURCE_TYPE, false);
	Integer targetFolderID = WebUtil.readIntParam(request, "targetFolderID", false);

	if (targetFolderID == null) {
	    log.error("Can not create folder, parent folder ID is NULL");
	    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parent folder ID is NULL");
	    return null;
	}

	Integer copyType = WebUtil.readIntParam(request, "copyType", true);
	Integer userID = getUserId();
	getWorkspaceManagementService().copyResource(resourceID, resourceType, copyType, targetFolderID, userID);
	return null;
    }

    public ActionForward renameResource(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response)
	    throws IOException, ServletException, UserException, WorkspaceFolderException {
	Integer userID = getUserId();
	Long resourceID = new Long(WebUtil.readLongParam(request, WorkspaceAction.RESOURCE_ID));
	String resourceType = WebUtil.readStrParam(request, WorkspaceAction.RESOURCE_TYPE);

	boolean isDummyValue = checkResourceDummyValue(resourceID, FolderContentDTO.FOLDER);
	if (isDummyValue) {
	    throw new IOException("Can not rename this resource");
	}
	String name = WebUtil.readStrParam(request, "name");
	getWorkspaceManagementService().renameResource(resourceID, resourceType, name, userID);

	return null;
    }
}