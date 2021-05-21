/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.whiteboard.web.controller;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.whiteboard.WhiteboardConstants;
import org.lamsfoundation.lams.tool.whiteboard.model.Whiteboard;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardUser;
import org.lamsfoundation.lams.tool.whiteboard.service.IWhiteboardService;
import org.lamsfoundation.lams.tool.whiteboard.service.WhiteboardApplicationException;
import org.lamsfoundation.lams.tool.whiteboard.service.WhiteboardService;
import org.lamsfoundation.lams.tool.whiteboard.web.form.WhiteboardForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/authoring")
public class AuthoringController {

    private static Logger log = Logger.getLogger(AuthoringController.class);

    @Autowired
    private IWhiteboardService whiteboardService;

    /**
     * Read whiteboard data from database and put them into HttpSession. It will
     * redirect to init.do directly after this method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item
     * lost when user "refresh page",
     *
     */
    @RequestMapping("/start")
    private String start(@ModelAttribute("authoringForm") WhiteboardForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	return starting(authoringForm, request);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    private String definelater(@ModelAttribute("authoringForm") WhiteboardForm authoringForm,
	    HttpServletRequest request) throws ServletException {
	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Whiteboard whiteboard = whiteboardService.getWhiteboardByContentId(contentId);

	whiteboard.setDefineLater(true);
	whiteboardService.saveOrUpdate(whiteboard);

	//audit log the teacher has started editing activity in monitor
	whiteboardService.auditLogStartEditingActivityInMonitor(contentId);

	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	return starting(authoringForm, request);
    }

    private String starting(@ModelAttribute("authoringForm") WhiteboardForm authoringForm, HttpServletRequest request)
	    throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = WebUtil.readLongParam(request, WhiteboardConstants.PARAM_TOOL_CONTENT_ID);

	Whiteboard whiteboard = null;

	// get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	authoringForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	authoringForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    whiteboard = whiteboardService.getWhiteboardByContentId(contentId);

	    // if Whiteboard does not exist, try to use default content instead.
	    if (whiteboard == null) {
		whiteboard = whiteboardService.getDefaultContent(contentId);
	    }

	    authoringForm.setWhiteboard(whiteboard);

	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    String authorName = WhiteboardService.getWhiteboardAuthorName(user);
	    authoringForm.setAuthorName(authorName);

	} catch (Exception e) {
	    AuthoringController.log.error(e);
	    throw new ServletException(e);
	}

	sessionMap.put(WhiteboardConstants.ATTR_RESOURCE_FORM, authoringForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));

	return "pages/authoring/start";
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @throws WhiteboardApplicationException
     */

    @SuppressWarnings("unchecked")
    @RequestMapping("/init")
    private String initPage(@ModelAttribute("authoringForm") WhiteboardForm authoringForm, HttpServletRequest request)
	    throws ServletException, WhiteboardApplicationException {
	String sessionMapID = WebUtil.readStrParam(request, WhiteboardConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	WhiteboardForm existForm = (WhiteboardForm) sessionMap.get(WhiteboardConstants.ATTR_RESOURCE_FORM);

	try {
	    PropertyUtils.copyProperties(authoringForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	authoringForm.setMode(mode.toString());

	String whiteboardServerUrl = whiteboardService.getWhiteboardServerUrl();
	request.setAttribute("whiteboardServerUrl", whiteboardServerUrl);

	String wid = authoringForm.getWhiteboard().getContentId().toString();
	String whiteboardAccessTokenHash = whiteboardService.getWhiteboardAccessTokenHash(wid, null);
	request.setAttribute("whiteboardAccessToken", whiteboardAccessTokenHash);

	return "pages/authoring/authoring";
    }

    /**
     * This method will persist all information in this authoring page
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    private String updateContent(@ModelAttribute("authoringForm") WhiteboardForm authoringForm,
	    HttpServletRequest request) throws Exception {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(authoringForm.getSessionMapID());
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	Whiteboard whiteboard = authoringForm.getWhiteboard();

	// **********************************Get Whiteboard PO*********************
	Whiteboard whiteboardPO = whiteboardService.getWhiteboardByContentId(whiteboard.getContentId());
	if (whiteboardPO == null) {
	    // new Whiteboard, create it
	    whiteboardPO = whiteboard;
	    whiteboardPO.setCreated(new Timestamp(new Date().getTime()));
	    whiteboardPO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    Long uid = whiteboardPO.getUid();
	    PropertyUtils.copyProperties(whiteboardPO, whiteboard);

	    // copyProperties() above may result in "collection assigned to two objects in a session" exception
	    // Below we remove reference to one of Assessment objects,
	    // so maybe there will be just one object in session when save is done
	    // If this fails, we may have to evict the object from session using DAO
	    authoringForm.setWhiteboard(null);
	    whiteboard = null;
	    // get back UID
	    whiteboardPO.setUid(uid);

	    // if it's a teacher - change define later status
	    if (mode.isTeacher()) {
		whiteboardPO.setDefineLater(false);
	    }
	    whiteboardPO.setUpdated(new Timestamp(new Date().getTime()));
	}
	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	WhiteboardUser whiteboardUser = whiteboardService.getUserByIDAndContent(user.getUserID().longValue(),
		whiteboardPO.getContentId());
	if (whiteboardUser == null) {
	    whiteboardUser = new WhiteboardUser(user, whiteboardPO);
	}

	whiteboardPO.setCreatedBy(whiteboardUser);

	// ***************************** finally persist whiteboardPO again
	whiteboardService.saveOrUpdate(whiteboardPO);

	authoringForm.setWhiteboard(whiteboardPO);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return "pages/authoring/authoring";
    }
}