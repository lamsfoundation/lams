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

package org.lamsfoundation.lams.tool.zoom.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.zoom.dto.ContentDTO;
import org.lamsfoundation.lams.tool.zoom.dto.ZoomUserDTO;
import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.model.ZoomSession;
import org.lamsfoundation.lams.tool.zoom.model.ZoomUser;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;
import org.lamsfoundation.lams.tool.zoom.util.ZoomConstants;
import org.lamsfoundation.lams.tool.zoom.util.ZoomUtil;
import org.lamsfoundation.lams.tool.zoom.web.forms.LearningForm;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/learning")
public class LearningController {

    private static final Logger logger = Logger.getLogger(LearningController.class);

    @Autowired
    private IZoomService zoomService;

    @Autowired
    @Qualifier("zoomMessageService")
    private MessageService messageService;

    @RequestMapping("finishActivity")
    public String finishActivity(@ModelAttribute LearningForm learningForm, HttpServletRequest request)
	    throws IOException {
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	ZoomUser user = getCurrentUser(toolSessionID);

	if (user != null) {
	    user.setFinishedActivity(true);
	    zoomService.saveOrUpdateZoomUser(user);
	} else {
	    logger.error("finishActivity(): couldn't find/create ZoomUser in toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = (ToolSessionManager) zoomService;

	String nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, user.getUserId().longValue());
	return "redirect:" + nextActivityUrl;
    }

    private ZoomUser getCurrentUser(Long toolSessionId) {
	org.lamsfoundation.lams.usermanagement.dto.UserDTO lamsUserDTO = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) SessionManager
		.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	ZoomUser user = zoomService.getUserByUserIdAndSessionId(lamsUserDTO.getUserID(), toolSessionId);

	if (user == null) {
	    ZoomSession zoomSession = zoomService.getSessionBySessionId(toolSessionId);
	    user = zoomService.createZoomUser(lamsUserDTO, zoomSession);
	}

	return user;
    }

    @RequestMapping("/start")
    public String start(@ModelAttribute LearningForm learningForm, HttpServletRequest request) throws Exception {
	// 'toolSessionID' and 'mode' parameters are expected to be present.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// Retrieve the session and content.
	ZoomSession session = zoomService.getSessionBySessionId(toolSessionID);
	if (session == null) {
	}

	Zoom zoom = session.getZoom();

	// set mode, toolSessionID and ZoomDTO
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	learningForm.setToolSessionID(toolSessionID);

	ContentDTO contentDTO = new ContentDTO();
	contentDTO.setTitle(zoom.getTitle());
	contentDTO.setInstructions(zoom.getInstructions());

	request.setAttribute(ZoomConstants.ATTR_CONTENT_DTO, contentDTO);

	// Set the content in use flag.
	if (!zoom.isContentInUse()) {
	    zoom.setContentInUse(true);
	    zoomService.saveOrUpdateZoom(zoom);
	}

	boolean isLastActivity = zoomService.isLastActivity(toolSessionID);
	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, isLastActivity);

	ZoomUser user;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Integer userID = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false);
	    user = zoomService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    user = getCurrentUser(toolSessionID);
	}

	ZoomUserDTO userDTO = new ZoomUserDTO(user);
	request.setAttribute(ZoomConstants.ATTR_USER_DTO, userDTO);
	// set toolSessionID in request
	request.setAttribute(ZoomConstants.ATTR_TOOL_SESSION_ID, session.getSessionId());

	MultiValueMap<String, String> errorMap = null;
	try {
	    if (mode.isAuthor() || !zoom.isStartInMonitor()) {
		// start a meeting just like a monitor would
		errorMap = ZoomUtil.startMeeting(zoomService, messageService, zoom, request);
		if (!errorMap.isEmpty()) {
		    request.setAttribute("errorMap", errorMap);
		}
	    }
	    if (!mode.isAuthor() && (errorMap == null || errorMap.isEmpty())) {
		// register a learner for the meeting
		String meetingURL = user.getMeetingJoinUrl();
		if (meetingURL == null && zoom.getMeetingId() != null) {
		    meetingURL = zoomService.registerUser(zoom.getUid(), user.getUid(), session.getSessionName());
		}
		// if start in monitor is not set, this overwrites the URL set in ZoomUtil.startMeeting() above
		request.setAttribute(ZoomConstants.ATTR_MEETING_URL, meetingURL);
		request.setAttribute(ZoomConstants.ATTR_MEETING_PASSWORD, zoom.getMeetingPassword());
	    }

	} catch (Exception e) {
	    logger.error("Exception while entering a Zoom meeting", e);
	    if (errorMap == null) {
		errorMap = new LinkedMultiValueMap<>();
		request.setAttribute("errorMap", errorMap);
	    }
	    errorMap.add("GLOBAL", e.getMessage() == null ? e.toString() : e.getMessage());
	    request.setAttribute("skipContent", true);
	}
	return "pages/learning/learning";
    }
}
