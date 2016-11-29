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
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralToolContentHandler;
import org.lamsfoundation.lams.util.FileUtilException;
import org.lamsfoundation.lams.util.PortraitUtils;
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
    private static CentralToolContentHandler centralToolContentHandler;
    private static int LARGEST_DIMENSION = 120;

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
	PortraitSaveAction.log.debug(
		"got file: " + fileName + " of type: " + file.getContentType() + " with size: " + file.getFileSize());

	User user = getService().getUserByLogin(request.getRemoteUser());

	// check if file is an image using the MIME content type
	String mediaType = file.getContentType().split("/", 2)[0];
	if (!mediaType.equals("image")) {
	    errors.add("file", new ActionMessage("error.portrait.not.image"));
	    saveErrors(request, errors);
	    return mapping.findForward("errors");
	}

	// resize picture
	InputStream is = PortraitUtils.resizePicture(file.getInputStream(), PortraitSaveAction.LARGEST_DIMENSION);
	if (is == null) {
	    errors.add("file", new ActionMessage("error.general.1"));
	    saveErrors(request, errors);
	    return mapping.findForward("errors");
	}

	// write to content repository
	NodeKey node = null;
	if ((file != null) && !StringUtils.isEmpty(fileName)) {
	    try {
		fileName = fileName.substring(0, fileName.indexOf('.')) + ".jpg";
		node = getCentralToolContentHandler().uploadFile(is, fileName, file.getContentType());
		is.close();
	    } catch (Exception e) {
		request.setAttribute("errorMessage", e.getMessage());
		return mapping.findForward("error.system");
	    }
	}

	PortraitSaveAction.log.debug("saved file with uuid: " + node.getUuid() + " and version: " + node.getVersion());

	// delete old portrait file (we only want to keep the user's current portrait)
	if (user.getPortraitUuid() != null) {
	    getCentralToolContentHandler().deleteFile(user.getPortraitUuid());
	}
	user.setPortraitUuid(node.getUuid());
	getService().saveUser(user);

	return mapping.findForward("profile");
    }

    /**
     * Save portrait taken from web camera.
     */
    public ActionForward saveWebcamPortrait(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// check if there is a session with logged in user
	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	User user = getService().getUserByLogin(request.getRemoteUser());
	if ((userDTO == null) || (user == null)) {
	    throw new UserAccessDeniedException("User hasn't been logged in");
	}

	// check if the file is an image using the MIME content type
	String mediaType = request.getContentType().split("/", 2)[0];
	if (!mediaType.equals("image")) {
	    throw new FileUtilException("The file is not an image.");
	}

	// check if there is an input stream
	InputStream is = request.getInputStream();
	if (is == null) {
	    throw new FileUtilException("Sorry, there has been an error.");
	}

	// write to content repository
	String fileName = user.getFullName() + " portrait.jpg";
	NodeKey node = getCentralToolContentHandler().uploadFile(is, fileName, "image/jpeg");
	is.close();

	log.debug("saved file with uuid: " + node.getUuid() + " and version: " + node.getVersion());

	// delete old portrait file (we only want to keep the user's current portrait)
	if (user.getPortraitUuid() != null) {
	    getCentralToolContentHandler().deleteFile(user.getPortraitUuid());
	}
	user.setPortraitUuid(node.getUuid());
	getService().saveUser(user);

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
	if (PortraitSaveAction.service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    PortraitSaveAction.service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return service;
    }
}
