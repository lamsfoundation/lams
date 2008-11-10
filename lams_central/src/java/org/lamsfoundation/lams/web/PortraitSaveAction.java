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

/* $Id$ */
package org.lamsfoundation.lams.web;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CentralToolContentHandler;
import org.lamsfoundation.lams.util.PortraitUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author jliew
 * @author Andrey Balan
 * 
 * @struts:action path="/saveportrait" name="PortraitActionForm" input=".portrait" scope="request" validate="false"
 * 
 * @struts:action-forward name="profile" path="/index.do?state=active&amp;tab=profile"
 * @struts:action-forward name="errors" path="/index.do?state=active&amp;tab=portrait"
 */
public class PortraitSaveAction extends Action {

    private static Logger log = Logger.getLogger(PortraitSaveAction.class);
    private static IUserManagementService service;
    private static CentralToolContentHandler centralToolContentHandler;
    private static int LARGEST_DIMENSION = 120;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (isCancelled(request)) {
	    return mapping.findForward("profile");
	}

	ActionMessages errors = new ActionMessages();

	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	PortraitSaveAction.centralToolContentHandler = (CentralToolContentHandler) wac
		.getBean("centralToolContentHandler");

	PortraitActionForm portraitForm = (PortraitActionForm) form;
	FormFile file = portraitForm.getFile();
	String fileName = file.getFileName();
	PortraitSaveAction.log.debug("got file: " + fileName + " of type: " + file.getContentType() + " with size: "
		+ file.getFileSize());

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
		node = PortraitSaveAction.centralToolContentHandler.uploadFile(is, fileName, file.getContentType(),
			IToolContentHandler.TYPE_ONLINE);
		is.close();
	    } catch (Exception e) {
		request.setAttribute("errorMessage", e.getMessage());
		return mapping.findForward("error.system");
	    }
	}

	PortraitSaveAction.log.debug("saved file with uuid: " + node.getUuid() + " and version: " + node.getVersion());

	// delete old portrait file (we only want to keep the user's current portrait)
	if (user.getPortraitUuid() != null) {
	    PortraitSaveAction.centralToolContentHandler.deleteFile(user.getPortraitUuid());
	}
	user.setPortraitUuid(node.getUuid());
	getService().save(user);

	return mapping.findForward("profile");
    }

    private IUserManagementService getService() {
	if (PortraitSaveAction.service == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		    .getServletContext());
	    PortraitSaveAction.service = (IUserManagementService) ctx.getBean("userManagementService");
	}
	return PortraitSaveAction.service;
    }
}

