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

package org.lamsfoundation.lams.tool.dokumaran.web.controller;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;
import org.lamsfoundation.lams.tool.dokumaran.service.IDokumaranService;
import org.lamsfoundation.lams.tool.dokumaran.web.form.DokumaranForm;
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

/**
 * @author Steve.Ni
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController {

    private static Logger log = Logger.getLogger(AuthoringController.class);

    @Autowired
    private IDokumaranService dokumaranService;

    /**
     * Read dokumaran data from database and put them into HttpSession. It will
     * redirect to init.do directly after this method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item
     * lost when user "refresh page",
     *
     * @throws ServletException
     *
     */
    @RequestMapping("/start")
    private String start(@ModelAttribute("authoringForm") DokumaranForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	return starting(authoringForm, request);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    private String definelater(@ModelAttribute("authoringForm") DokumaranForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	Dokumaran dokumaran = dokumaranService.getDokumaranByContentId(contentId);

	dokumaran.setDefineLater(true);
	dokumaranService.saveOrUpdate(dokumaran);

	//audit log the teacher has started editing activity in monitor
	dokumaranService.auditLogStartEditingActivityInMonitor(contentId);

	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	return starting(authoringForm, request);
    }

    private String starting(@ModelAttribute("authoringForm") DokumaranForm authoringForm, HttpServletRequest request)
	    throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, DokumaranConstants.PARAM_TOOL_CONTENT_ID));

	// get back the dokumaran and item list and display them on page

	Dokumaran dokumaran = null;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	authoringForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	authoringForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    dokumaran = dokumaranService.getDokumaranByContentId(contentId);
	    // if dokumaran does not exist, try to use default content instead.
	    if (dokumaran == null) {
		dokumaran = dokumaranService.getDefaultContent(contentId);
	    }

	    authoringForm.setDokumaran(dokumaran);
	} catch (Exception e) {
	    AuthoringController.log.error(e);
	    throw new ServletException(e);
	}

	sessionMap.put(DokumaranConstants.ATTR_RESOURCE_FORM, authoringForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	return "pages/authoring/start";
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */

    @RequestMapping("/init")
    private String initPage(@ModelAttribute("authoringForm") DokumaranForm authoringForm, HttpServletRequest request)
	    throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, DokumaranConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	DokumaranForm existForm = (DokumaranForm) sessionMap.get(DokumaranConstants.ATTR_RESOURCE_FORM);

	try {
	    PropertyUtils.copyProperties(authoringForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	authoringForm.setMode(mode.toString());

	return "pages/authoring/authoring";
    }

    /**
     * This method will persist all inforamtion in this authoring page, include
     * all dokumaran item, information etc.
     */
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    private String updateContent(@ModelAttribute("authoringForm") DokumaranForm authoringForm,
	    HttpServletRequest request) throws Exception {
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(authoringForm.getSessionMapID());
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	Dokumaran dokumaran = authoringForm.getDokumaran();

	// **********************************Get Dokumaran PO*********************
	Dokumaran dokumaranPO = dokumaranService.getDokumaranByContentId(dokumaran.getContentId());
	if (dokumaranPO == null) {
	    // new Dokumaran, create it
	    dokumaranPO = dokumaran;
	    dokumaranPO.setCreated(new Timestamp(new Date().getTime()));
	    dokumaranPO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    Long uid = dokumaranPO.getUid();
	    PropertyUtils.copyProperties(dokumaranPO, dokumaran);

	    // copyProperties() above may result in "collection assigned to two objects in a session" exception
	    // Below we remove reference to one of Assessment objects,
	    // so maybe there will be just one object in session when save is done
	    // If this fails, we may have to evict the object from session using DAO
	    authoringForm.setDokumaran(null);
	    dokumaran = null;
	    // get back UID
	    dokumaranPO.setUid(uid);

	    // if it's a teacher - change define later status
	    if (mode.isTeacher()) {
		dokumaranPO.setDefineLater(false);
	    }
	    dokumaranPO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	DokumaranUser dokumaranUser = dokumaranService.getUserByIDAndContent(new Long(user.getUserID().intValue()),
		dokumaranPO.getContentId());
	if (dokumaranUser == null) {
	    dokumaranUser = new DokumaranUser(user, dokumaranPO);
	}

	dokumaranPO.setCreatedBy(dokumaranUser);

	// ***************************** finally persist dokumaranPO again
	dokumaranService.saveOrUpdate(dokumaranPO);

	authoringForm.setDokumaran(dokumaranPO);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return "pages/authoring/authoring";
    }

}
