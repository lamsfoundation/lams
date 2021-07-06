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

package org.lamsfoundation.lams.tool.peerreview.web.controller;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.peerreview.PeerreviewConstants;
import org.lamsfoundation.lams.tool.peerreview.model.Peerreview;
import org.lamsfoundation.lams.tool.peerreview.model.PeerreviewUser;
import org.lamsfoundation.lams.tool.peerreview.service.IPeerreviewService;
import org.lamsfoundation.lams.tool.peerreview.web.form.PeerreviewForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private static final String START_PATH = "/pages/authoring/start";
    private static final String AUTHORING_PATH = "/pages/authoring/authoring";

    @Autowired
    @Qualifier("peerreviewService")
    private IPeerreviewService service;

    @RequestMapping("/start")
    public String start(@ModelAttribute PeerreviewForm peerreviewForm, HttpServletRequest request, HttpSession session)
	    throws ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	return doStart(peerreviewForm, request, session);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String defineLater(@ModelAttribute PeerreviewForm peerreviewForm, HttpServletRequest request,
	    HttpSession session) throws ServletException {
	// update define later flag to true
	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	Peerreview peerreview = service.getPeerreviewByContentId(contentId);

	peerreview.setDefineLater(true);
	service.saveOrUpdatePeerreview(peerreview);

	//audit log the teacher has started editing activity in monitor
	service.auditLogStartEditingActivityInMonitor(contentId);

	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	return doStart(peerreviewForm, request, session);
    }

    /**
     * Read peerreview data from database and put them into HttpSession. It will
     * redirect to init.do directly after this method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item
     * lost when user "refresh page",
     *
     * @throws ServletException
     */
    private String doStart(PeerreviewForm peerreviewForm, HttpServletRequest request, HttpSession session)
	    throws ServletException {

	Peerreview peerreview = null;

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, PeerreviewConstants.PARAM_TOOL_CONTENT_ID));

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	peerreviewForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	session.setAttribute(sessionMap.getSessionID(), sessionMap);
	peerreviewForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    peerreview = service.getPeerreviewByContentId(contentId);
	    // if peerreview does not exist, try to use default content instead.
	    if (peerreview == null) {
		peerreview = service.getDefaultContent(contentId);
	    }

	    peerreviewForm.setPeerreview(peerreview);
	} catch (Exception e) {
	    AuthoringController.log.error(e);
	    throw new ServletException(e);
	}

	// get rating criterias from DB
	List<RatingCriteria> ratingCriterias = service.getRatingCriterias(contentId);
	sessionMap.put(AttributeNames.ATTR_RATING_CRITERIAS, ratingCriterias);

	sessionMap.put(PeerreviewConstants.ATTR_PEERREVIEW_FORM, peerreviewForm);
	session.setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));

	return START_PATH;
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     *
     * @throws ServletException
     */
    @RequestMapping("/init")
    @SuppressWarnings("unchecked")
    public String initPage(@ModelAttribute PeerreviewForm peerreviewForm, HttpServletRequest request,
	    HttpSession session) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, PeerreviewConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session.getAttribute(sessionMapID);
	PeerreviewForm existForm = (PeerreviewForm) sessionMap.get(PeerreviewConstants.ATTR_PEERREVIEW_FORM);

	try {
	    PropertyUtils.copyProperties(peerreviewForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return AUTHORING_PATH;
    }

    /**
     * This method will persist all information in this authoring page, include
     * all peer review item, information etc.
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute PeerreviewForm peerreviewForm, HttpServletRequest request,
	    HttpSession session) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

	// get back sessionMAP
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) session
		.getAttribute(peerreviewForm.getSessionMapID());

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	Peerreview peerreview = peerreviewForm.getPeerreview();

	// **********************************Get Peerreview PO*********************
	Peerreview peerreviewPO = service.getPeerreviewByContentId(peerreviewForm.getPeerreview().getContentId());
	if (peerreviewPO == null) {
	    // new Peerreview, create it
	    peerreviewPO = peerreview;
	    peerreviewPO.setCreated(new Timestamp(new Date().getTime()));
	    peerreviewPO.setUpdated(new Timestamp(new Date().getTime()));

	} else {
	    Long uid = peerreviewPO.getUid();
	    PropertyUtils.copyProperties(peerreviewPO, peerreview);
	    // get back UID
	    peerreviewPO.setUid(uid);

	    // if it's a teacher - change define later status
	    if (mode.isTeacher()) {
		peerreviewPO.setDefineLater(false);
	    }

	    peerreviewPO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// get back login user DTO
	UserDTO user = (UserDTO) session.getAttribute(AttributeNames.USER);
	PeerreviewUser peerreviewUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue()),
		peerreviewForm.getPeerreview().getContentId());
	if (peerreviewUser == null) {
	    peerreviewUser = new PeerreviewUser(user, peerreviewPO);
	}

	peerreviewPO.setCreatedBy(peerreviewUser);

	// finally persist peerreviewPO
	service.saveOrUpdatePeerreview(peerreviewPO);

	// ************************* Handle rating criterias *******************
	Long contentId = peerreview.getContentId();
	List<RatingCriteria> oldCriterias = (List<RatingCriteria>) sessionMap.get(AttributeNames.ATTR_RATING_CRITERIAS);
	service.saveRatingCriterias(request, oldCriterias, contentId);

	peerreviewForm.setPeerreview(peerreviewPO);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	return AUTHORING_PATH;
    }

}
