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

package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.CentralToolContentHandler;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.imgscalr.ResizePictureUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 * @author Andrey Balan
 */
public class PortraitSaveAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(PortraitSaveAction.class);
    private static IUserManagementService service;
    private static IAuditService auditService;
    private static MessageService messageService;
    private static CentralToolContentHandler centralToolContentHandler;
    private static int LARGEST_DIMENSION_ORIGINAL = 400;
    private static int LARGEST_DIMENSION_LARGE = 200;
    private static int LARGEST_DIMENSION_MEDIUM = 80;
    private static int LARGEST_DIMENSION_SMALL = 35;
    private static final String PORTRAIT_DELETE_AUDIT_KEY = "audit.delete.portrait";

    /**
     * Upload portrait image.
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (isCancelled(request)) {
	    return mapping.findForward("profile");
	}

	ActionMessages errors = new ActionMessages();

	PortraitActionForm portraitForm = (PortraitActionForm) form;
	FormFile file = portraitForm.getFile();
	String fileName = file.getFileName();
	log.debug("got file: " + fileName + " of type: " + file.getContentType() + " with size: " + file.getFileSize());

	User user = getService().getUserByLogin(request.getRemoteUser());

	// check if file is an image using the MIME content type
	String mediaType = file.getContentType().split("/", 2)[0];
	if (!mediaType.equals("image")) {
	    errors.add("file", new ActionMessage("error.portrait.not.image"));
	    saveErrors(request, errors);
	    return mapping.findForward("errors");
	}

	// check file exists
	InputStream is = file.getInputStream();
	if (is == null) {
	    errors.add("file", new ActionMessage("error.general.1"));
	    saveErrors(request, errors);
	    return mapping.findForward("errors");
	}

	// write to content repository
	NodeKey originalFileNode = null;
	if ((file != null) && !StringUtils.isEmpty(fileName)) {
	    
	    //Create nice file name. If file name equals to "blob" - it means it was uploaded using webcam
	    String fileNameWithoutExt;
	    if (fileName.equals("blob")) {
		HttpSession ss = SessionManager.getSession();
		UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
		fileNameWithoutExt = userDTO.getLogin() + "_portrait";

	    } else {
		fileNameWithoutExt = fileName.substring(0, fileName.indexOf('.'));
	    }

	    // upload to the content repository
	    originalFileNode = getCentralToolContentHandler().uploadFile(is, fileNameWithoutExt + "_original.png",
		    "image/png");
	    is.close();
	    log.debug("saved file with uuid: " + originalFileNode.getUuid() + " and version: "
		    + originalFileNode.getVersion());

	    //resize to the large size
	    is = ResizePictureUtil.resize(file.getInputStream(), LARGEST_DIMENSION_LARGE);
	    NodeKey node = getCentralToolContentHandler().updateFile(originalFileNode.getUuid(), is,
		    fileNameWithoutExt + "_large.png", "image/png");
	    is.close();
	    log.debug("saved file with uuid: " + node.getUuid() + " and version: " + node.getVersion());

	    //resize to the medium size
	    is = ResizePictureUtil.resize(file.getInputStream(), LARGEST_DIMENSION_MEDIUM);
	    node = getCentralToolContentHandler().updateFile(node.getUuid(), is, fileNameWithoutExt + "_medium.png",
		    "image/png");
	    is.close();
	    log.debug("saved file with uuid: " + node.getUuid() + " and version: " + node.getVersion());

	    //resize to the small size
	    is = ResizePictureUtil.resize(file.getInputStream(), LARGEST_DIMENSION_SMALL);
	    node = getCentralToolContentHandler().updateFile(node.getUuid(), is, fileNameWithoutExt + "_small.png",
		    "image/png");
	    is.close();
	    log.debug("saved file with uuid: " + node.getUuid() + " and version: " + node.getVersion());

	}

	// delete old portrait file (we only want to keep the user's current portrait)
	if (user.getPortraitUuid() != null) {
	    getCentralToolContentHandler().deleteFile(user.getPortraitUuid());
	}
	user.setPortraitUuid(originalFileNode.getUuid());
	getService().saveUser(user);

	return mapping.findForward("profile");
    }
    
    /** Called from sysadmin to delete an inappropriate portrait */
    public ActionForward deletePortrait(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer userId = WebUtil.readIntParam(request, "userId", true);
	
	// check user is sysadmin
	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    log.error("Attempt to delete a portrait by user that is not sysadmin. User is "+request.getRemoteUser()+" portrait to be deleted is for user "+userId+".");
	    return deleteResponse(response, "error");
	}

	String responseValue = "deleted";
	User userToModify = (User) getService().findById(User.class, userId);
	if (userToModify != null && userToModify.getPortraitUuid() != null) {
	    
	    Object[] args = new Object[] { userToModify.getFullName(), userToModify.getLogin(), userToModify.getUserId(), userToModify.getPortraitUuid() };
	    String auditMessage = getMessageService().getMessage(PORTRAIT_DELETE_AUDIT_KEY, args);
	    getAuditService().log(CentralConstants.MODULE_NAME, auditMessage);

	    try {
		getCentralToolContentHandler().deleteFile(userToModify.getPortraitUuid());
            	userToModify.setPortraitUuid(null);
            	getService().saveUser(userToModify);
	    } catch (Exception e) {
		log.error("Unable to delete a portrait for user "+userId+".", e);
		return deleteResponse(response, "error");
	    }
	}
	return deleteResponse(response, responseValue);
    }

    private ActionForward deleteResponse(HttpServletResponse response, String data) throws IOException {
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().write(data);
	return null;
    }
    
    private CentralToolContentHandler getCentralToolContentHandler() {
	if (centralToolContentHandler == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    centralToolContentHandler = (CentralToolContentHandler) wac.getBean("centralToolContentHandler");
	}
	return centralToolContentHandler;
    }

    private IUserManagementService getService() {
	if (service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return service;
    }
    
    private IAuditService getAuditService() {
	if (PortraitSaveAction.auditService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    PortraitSaveAction.auditService = (IAuditService) ctx.getBean("auditService");
	}
	return PortraitSaveAction.auditService;
    }

    private MessageService getMessageService() {
	if (PortraitSaveAction.messageService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    PortraitSaveAction.messageService = (MessageService) ctx.getBean("centralMessageService");
	}
	return PortraitSaveAction.messageService;
    }

}
