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
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.exception.InvalidParameterException;
import org.lamsfoundation.lams.contentrepository.exception.RepositoryCheckedException;
import org.lamsfoundation.lams.logevent.LogEvent;
import org.lamsfoundation.lams.logevent.service.ILogEventService;
import org.lamsfoundation.lams.usermanagement.Role;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.imgscalr.ResizePictureUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jliew
 * @author Andrey Balan
 */
@Controller
@RequestMapping(path = "/saveportrait", method = RequestMethod.POST)
public class PortraitSaveController {
    private static Logger log = Logger.getLogger(PortraitSaveController.class);
    private static final String PORTRAIT_DELETE_AUDIT_KEY = "audit.delete.portrait";

    @Autowired
    private IUserManagementService userManagementService;
    @Autowired
    private ILogEventService logEventService;
    @Autowired
    @Qualifier("centralMessageService")
    private MessageService messageService;
    @Autowired
    private IToolContentHandler centralToolContentHandler;

    /**
     * Upload portrait image.
     */
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String unspecified(@ModelAttribute("PortraitActionForm") PortraitActionForm portraitForm,
	    HttpServletRequest request, HttpServletResponse response)
	    throws IOException, InvalidParameterException, RepositoryCheckedException {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();

	MultipartFile file = portraitForm.getFile();
	String fileName = file.getOriginalFilename();
	log.debug("got file: " + fileName + " of type: " + file.getContentType() + " with size: " + file.getSize());

	User user = userManagementService.getUserByLogin(request.getRemoteUser());

	// check if file is an image using the MIME content type
	String mediaType = file.getContentType().split("/", 2)[0];
	if (!mediaType.equals("image")) {
	    errorMap.add("file", messageService.getMessage("error.portrait.not.image"));
	    request.setAttribute("errorMap", errorMap);
	    return "forward:/index.do?redirect=portrait";
	}

	// check file exists
	InputStream is = file.getInputStream();
	if (is == null) {
	    errorMap.add("file", messageService.getMessage("error.general.1"));
	    request.setAttribute("errorMap", errorMap);
	    return "forward:/index.do?redirect=portrait";
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
	    originalFileNode = centralToolContentHandler.uploadFile(is, fileNameWithoutExt + "_original.jpg",
		    "image/jpeg", true);
	    is.close();
	    log.debug("saved file with uuid: " + originalFileNode.getUuid() + " and portrait uuid "
		    + originalFileNode.getPortraitUuid() + " and version: " + originalFileNode.getVersion());

	    //resize to the large size
	    is = ResizePictureUtil.resize(file.getInputStream(), CommonConstants.PORTRAIT_LARGEST_DIMENSION_LARGE);
	    NodeKey node = centralToolContentHandler.updateFile(originalFileNode.getUuid(), is,
		    fileNameWithoutExt + "_large.jpg", "image/jpeg");
	    is.close();
	    log.debug("saved file with uuid: " + node.getUuid() + " and version: " + node.getVersion());

	    //resize to the medium size
	    is = ResizePictureUtil.resize(file.getInputStream(), CommonConstants.PORTRAIT_LARGEST_DIMENSION_MEDIUM);
	    node = centralToolContentHandler.updateFile(node.getUuid(), is, fileNameWithoutExt + "_medium.jpg",
		    "image/jpeg");
	    is.close();
	    log.debug("saved file with uuid: " + node.getUuid() + " and version: " + node.getVersion());

	    //resize to the small size
	    is = ResizePictureUtil.resize(file.getInputStream(), CommonConstants.PORTRAIT_LARGEST_DIMENSION_SMALL);
	    node = centralToolContentHandler.updateFile(node.getUuid(), is, fileNameWithoutExt + "_small.jpg",
		    "image/jpeg");
	    is.close();
	    log.debug("saved file with uuid: " + node.getUuid() + " and version: " + node.getVersion());

	}

	// delete old portrait file (we only want to keep the user's current portrait)
	if (user.getPortraitUuid() != null) {
	    centralToolContentHandler.deleteFile(user.getPortraitUuid());
	}
	user.setPortraitUuid(UUID.fromString(originalFileNode.getPortraitUuid()));
	userManagementService.saveUser(user);

	return "forward:/index.do?redirect=portrait";
    }

    /** Called from sysadmin to delete an inappropriate portrait */
    @RequestMapping(path = "/deletePortrait", method = RequestMethod.POST)
    public String deletePortrait(HttpServletRequest request, HttpServletResponse response) throws IOException {
	Integer userId = WebUtil.readIntParam(request, "userId", true);

	// check user is sysadmin
	if (!(request.isUserInRole(Role.SYSADMIN))) {
	    log.error("Attempt to delete a portrait by user that is not sysadmin. User is " + request.getRemoteUser()
		    + " portrait to be deleted is for user " + userId + ".");
	    return deleteResponse(response, "error");
	}

	String responseValue = "deleted";
	User userToModify = (User) userManagementService.findById(User.class, userId);
	if (userToModify != null && userToModify.getPortraitUuid() != null) {

	    UserDTO sysadmin = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	    Object[] args = new Object[] { userToModify.getFullName(), userToModify.getLogin(),
		    userToModify.getUserId(), userToModify.getPortraitUuid() };
	    String auditMessage = messageService.getMessage(PORTRAIT_DELETE_AUDIT_KEY, args);
	    logEventService.logEvent(LogEvent.TYPE_USER_ORG_ADMIN, sysadmin.getUserID(), userId, null, null,
		    auditMessage);

	    try {
		centralToolContentHandler.deleteFile(userToModify.getPortraitUuid());
		userToModify.setPortraitUuid(null);
		userManagementService.saveUser(userToModify);
	    } catch (Exception e) {
		log.error("Unable to delete a portrait for user " + userId + ".", e);
		return deleteResponse(response, "error");
	    }
	}
	return deleteResponse(response, responseValue);
    }

    private String deleteResponse(HttpServletResponse response, String data) throws IOException {
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().write(data);
	return null;
    }
}