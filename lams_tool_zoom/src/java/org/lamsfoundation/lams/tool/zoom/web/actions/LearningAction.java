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

package org.lamsfoundation.lams.tool.zoom.web.actions;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.lamsfoundation.lams.integration.security.RandomPasswordGenerator;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.zoom.dto.ContentDTO;
import org.lamsfoundation.lams.tool.zoom.dto.NotebookEntryDTO;
import org.lamsfoundation.lams.tool.zoom.dto.ZoomUserDTO;
import org.lamsfoundation.lams.tool.zoom.model.Zoom;
import org.lamsfoundation.lams.tool.zoom.model.ZoomSession;
import org.lamsfoundation.lams.tool.zoom.model.ZoomUser;
import org.lamsfoundation.lams.tool.zoom.service.IZoomService;
import org.lamsfoundation.lams.tool.zoom.service.ZoomServiceProxy;
import org.lamsfoundation.lams.tool.zoom.util.ZoomConstants;
import org.lamsfoundation.lams.tool.zoom.web.forms.LearningForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class LearningAction extends DispatchAction {

    private static final Logger logger = Logger.getLogger(LearningAction.class);

    private IZoomService zoomService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// set up zoomService
	zoomService = ZoomServiceProxy.getZoomService(this.getServlet().getServletContext());

	return super.execute(mapping, form, request, response);
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	ZoomUser user = getCurrentUser(toolSessionID);

	if (user != null) {

	    LearningForm learningForm = (LearningForm) form;

	    if (user.getNotebookEntryUID() == null) {
		user.setNotebookEntryUID(zoomService.createNotebookEntry(toolSessionID,
			CoreNotebookConstants.NOTEBOOK_TOOL, ZoomConstants.TOOL_SIGNATURE, user.getUserId().intValue(),
			learningForm.getEntryText()));
	    } else {
		// update existing entry.
		zoomService.updateNotebookEntry(user.getNotebookEntryUID(), learningForm.getEntryText());
	    }

	    user.setFinishedActivity(true);
	    zoomService.saveOrUpdateZoomUser(user);
	} else {
	    logger.error("finishActivity(): couldn't find/create ZoomUser in toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = ZoomServiceProxy.getZoomSessionManager(getServlet().getServletContext());

	String nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, user.getUserId().longValue());
	response.sendRedirect(nextActivityUrl);

	return null;
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

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	LearningForm lrnForm = (LearningForm) form;

	// set the finished flag
	ZoomUser user = getCurrentUser(lrnForm.getToolSessionID());
	ContentDTO contentDTO = new ContentDTO(user.getZoomSession().getZoom());

	request.setAttribute(ZoomConstants.ATTR_CONTENT_DTO, contentDTO);

	NotebookEntry notebookEntry = zoomService.getNotebookEntry(user.getNotebookEntryUID());

	if (notebookEntry != null) {
	    lrnForm.setEntryText(notebookEntry.getEntry());
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(lrnForm.getToolSessionID(), request,
		getServlet().getServletContext());

	return mapping.findForward("notebook");

    }

    public ActionForward openPreviewMeeting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// get user uid parameter
	Long uid = WebUtil.readLongParam(request, ZoomConstants.PARAM_USER_UID);
	ZoomUser user = zoomService.getUserByUID(uid);
	ZoomSession session = user.getZoomSession();

	// Get LAMS userDTO
	org.lamsfoundation.lams.usermanagement.dto.UserDTO lamsUserDTO = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) SessionManager
		.getSession().getAttribute(AttributeNames.USER);

	// create random strings for attendee and moderator passwords
	String attendeePassword = RandomPasswordGenerator.nextPassword(20);
	String moderatorPassword = RandomPasswordGenerator.nextPassword(20);
	MessageResources resources = MessageResources.getMessageResources(ZoomConstants.APP_RESOURCES);

	// Get default localized welcome message

	String welcomeMessage = resources.getMessage("activity.welcome.message");

	return null;
    }

    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	// save the reflection entry and call the notebook.

	LearningForm lrnForm = (LearningForm) form;

	ZoomUser user = getCurrentUser(lrnForm.getToolSessionID());
	Long toolSessionID = user.getZoomSession().getSessionId();
	Integer userID = user.getUserId().intValue();

	// check for existing notebook entry
	NotebookEntry entry = zoomService.getNotebookEntry(user.getNotebookEntryUID());

	if (entry == null) {
	    // create new entry
	    Long entryUID = zoomService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    ZoomConstants.TOOL_SIGNATURE, userID, lrnForm.getEntryText());
	    user.setNotebookEntryUID(entryUID);
	    zoomService.saveOrUpdateZoomUser(user);
	} else {
	    // update existing entry
	    entry.setEntry(lrnForm.getEntryText());
	    entry.setLastModified(new Date());
	    zoomService.updateNotebookEntry(entry);
	}

	return finishActivity(mapping, form, request, response);
    }

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	LearningForm learningForm = (LearningForm) form;

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
	contentDTO.setReflectOnActivity(zoom.isReflectOnActivity());
	contentDTO.setReflectInstructions(zoom.getReflectInstructions());

	request.setAttribute(ZoomConstants.ATTR_CONTENT_DTO, contentDTO);

	// Set the content in use flag.
	if (!zoom.isContentInUse()) {
	    zoom.setContentInUse(true);
	    zoomService.saveOrUpdateZoom(zoom);
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request,
		getServlet().getServletContext());

	ZoomUser user;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Integer userID = WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false);
	    user = zoomService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    user = getCurrentUser(toolSessionID);
	}

	// get any existing notebook entries and create userDTO
	NotebookEntry entry = zoomService.getNotebookEntry(user.getNotebookEntryUID());
	ZoomUserDTO userDTO = new ZoomUserDTO(user);
	if (entry != null) {
	    userDTO.setNotebookEntryDTO(new NotebookEntryDTO(entry));
	}
	request.setAttribute(ZoomConstants.ATTR_USER_DTO, userDTO);
	// set toolSessionID in request
	request.setAttribute(ZoomConstants.ATTR_TOOL_SESSION_ID, session.getSessionId());

//	String dispatchValue = new String();
//	boolean meetingOpen = false;
//	if (mode.isAuthor()) {
//	    dispatchValue = "openPreviewMeeting";
//	    meetingOpen = true;
//	} else {
//	}

//	String startURL = zoom.getMeetingStartUrl();
//
//	if (zoom.getMeetingId() != null) {
//	    zoomService.chooseApiKeys(zoom.getUid());
//	    startURL = zoomService.createMeeting(zoom.getUid());
//	}

	String meetingURL = user.getMeetingJoinUrl();
	if (meetingURL == null && zoom.getMeetingId() != null) {
	    meetingURL = zoomService.registerUser(zoom.getUid(), user.getUid(), session.getSessionName());
	}

//	else {
//	    meetingURL = startURL;
//	    zoom.setMeetingStartUrl(null);
//	    zoomService.saveOrUpdateZoom(zoom);
//	}
	request.setAttribute(ZoomConstants.ATTR_MEETING_URL, meetingURL);

	return mapping.findForward("zoom");
    }
}
