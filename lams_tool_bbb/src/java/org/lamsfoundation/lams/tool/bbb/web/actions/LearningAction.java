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


package org.lamsfoundation.lams.tool.bbb.web.actions;

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
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.bbb.dto.ContentDTO;
import org.lamsfoundation.lams.tool.bbb.dto.NotebookEntryDTO;
import org.lamsfoundation.lams.tool.bbb.dto.UserDTO;
import org.lamsfoundation.lams.tool.bbb.model.Bbb;
import org.lamsfoundation.lams.tool.bbb.model.BbbSession;
import org.lamsfoundation.lams.tool.bbb.model.BbbUser;
import org.lamsfoundation.lams.tool.bbb.service.BbbServiceProxy;
import org.lamsfoundation.lams.tool.bbb.service.IBbbService;
import org.lamsfoundation.lams.tool.bbb.util.BbbException;
import org.lamsfoundation.lams.tool.bbb.util.BbbUtil;
import org.lamsfoundation.lams.tool.bbb.util.Constants;
import org.lamsfoundation.lams.tool.bbb.web.forms.LearningForm;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Ernie Ghiglione
 *
 *
 *
 *
 *
 *
 */
public class LearningAction extends DispatchAction {

    private static final Logger logger = Logger.getLogger(LearningAction.class);

    private IBbbService bbbService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// set up bbbService
	bbbService = BbbServiceProxy.getBbbService(this.getServlet().getServletContext());

	return super.execute(mapping, form, request, response);
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	BbbUser user = getCurrentUser(toolSessionID);

	if (user != null) {

	    LearningForm learningForm = (LearningForm) form;

	    if (user.getNotebookEntryUID() == null) {
		user.setNotebookEntryUID(
			bbbService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
				Constants.TOOL_SIGNATURE, user.getUserId().intValue(), learningForm.getEntryText()));
	    } else {
		// update existing entry.
		bbbService.updateNotebookEntry(user.getNotebookEntryUID(), learningForm.getEntryText());
	    }

	    user.setFinishedActivity(true);
	    bbbService.saveOrUpdateBbbUser(user);
	} else {
	    logger.error("finishActivity(): couldn't find/create BbbUser in toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = BbbServiceProxy.getBbbSessionManager(getServlet().getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, user.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new BbbException(e);
	} catch (ToolException e) {
	    throw new BbbException(e);
	} catch (IOException e) {
	    throw new BbbException(e);
	}

	return null;
    }

    private BbbUser getCurrentUser(Long toolSessionId) {
	org.lamsfoundation.lams.usermanagement.dto.UserDTO lamsUserDTO = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) SessionManager
		.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	BbbUser user = bbbService.getUserByUserIdAndSessionId(new Long(lamsUserDTO.getUserID().intValue()),
		toolSessionId);

	if (user == null) {
	    BbbSession bbbSession = bbbService.getSessionBySessionId(toolSessionId);
	    user = bbbService.createBbbUser(lamsUserDTO, bbbSession);
	}

	return user;
    }

    public ActionForward openLearnerMeeting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// get user uid parameter
	Long uid = WebUtil.readLongParam(request, Constants.PARAM_USER_UID);
	BbbUser user = bbbService.getUserByUID(uid);

	org.lamsfoundation.lams.usermanagement.dto.UserDTO lamsUserDTO = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) SessionManager
		.getSession().getAttribute(AttributeNames.USER);

	BbbSession session = bbbService.getSessionBySessionId(user.getBbbSession().getSessionId());

	String password = session.getAttendeePassword();
	String meetingKey = BbbUtil.getMeetingKey(user.getBbbSession().getSessionId(), password);

	String meetingURL = bbbService.getJoinMeetingURL(lamsUserDTO, meetingKey, password);
	response.sendRedirect(meetingURL);
	return null;

    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	LearningForm lrnForm = (LearningForm) form;

	// set the finished flag
	BbbUser user = getCurrentUser(lrnForm.getToolSessionID());
	ContentDTO contentDTO = new ContentDTO(user.getBbbSession().getBbb());

	request.setAttribute(Constants.ATTR_CONTENT_DTO, contentDTO);

	NotebookEntry notebookEntry = bbbService.getNotebookEntry(user.getNotebookEntryUID());

	if (notebookEntry != null) {
	    lrnForm.setEntryText(notebookEntry.getEntry());
	}

	WebUtil.putActivityPositionInRequestByToolSessionId(lrnForm.getToolSessionID(), request,
		getServlet().getServletContext());

	return mapping.findForward("notebook");

    }

    public ActionForward openPreviewMeeting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// get user uid parameter
	Long uid = WebUtil.readLongParam(request, Constants.PARAM_USER_UID);
	BbbUser user = bbbService.getUserByUID(uid);
	BbbSession session = user.getBbbSession();

	// Get LAMS userDTO
	org.lamsfoundation.lams.usermanagement.dto.UserDTO lamsUserDTO = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) SessionManager
		.getSession().getAttribute(AttributeNames.USER);

	// create random strings for attendee and moderator passwords
	String attendeePassword = RandomPasswordGenerator.nextPassword(20);
	String moderatorPassword = RandomPasswordGenerator.nextPassword(20);
	MessageResources resources = MessageResources.getMessageResources(Constants.APP_RESOURCES);
	String meetingKey = BbbUtil.getMeetingKey(session.getSessionId(), attendeePassword);

	// Get default localized welcome message

	String welcomeMessage = resources.getMessage("activity.welcome.message");

	logger.debug("Creating new room with meetingKey =" + meetingKey);

	String meetingStartResponse = bbbService.startConference(meetingKey, attendeePassword, moderatorPassword,
		BbbUtil.getReturnURL(request), welcomeMessage);

	if (meetingStartResponse == Constants.RESPONSE_SUCCESS) {
	    session.setMeetingCreated(true);
	    session.setAttendeePassword(attendeePassword);
	    session.setModeratorPassword(moderatorPassword);
	    String redirectUrl = bbbService.getJoinMeetingURL(lamsUserDTO, meetingKey, moderatorPassword);
	    // redirect to meeting
	    response.sendRedirect(redirectUrl);
	} else {
	    logger.error("startAction did not return a url to start the meeting");
	    throw new BbbException("Unable to start meeting");
	}

	return null;
    }

    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// save the reflection entry and call the notebook.

	LearningForm lrnForm = (LearningForm) form;

	BbbUser user = getCurrentUser(lrnForm.getToolSessionID());
	Long toolSessionID = user.getBbbSession().getSessionId();
	Integer userID = user.getUserId().intValue();

	// check for existing notebook entry
	NotebookEntry entry = bbbService.getNotebookEntry(user.getNotebookEntryUID());

	if (entry == null) {
	    // create new entry
	    Long entryUID = bbbService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    Constants.TOOL_SIGNATURE, userID, lrnForm.getEntryText());
	    user.setNotebookEntryUID(entryUID);
	    bbbService.saveOrUpdateBbbUser(user);
	} else {
	    // update existing entry
	    entry.setEntry(lrnForm.getEntryText());
	    entry.setLastModified(new Date());
	    bbbService.updateNotebookEntry(entry);
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
	BbbSession session = bbbService.getSessionBySessionId(toolSessionID);
	if (session == null) {
	    throw new BbbException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}

	Bbb bbb = session.getBbb();

	// check defineLater
	if (bbb.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set mode, toolSessionID and BbbDTO
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	learningForm.setToolSessionID(toolSessionID);

	ContentDTO contentDTO = new ContentDTO();
	contentDTO.setTitle(bbb.getTitle());
	contentDTO.setInstructions(bbb.getInstructions());
	contentDTO.setLockOnFinish(bbb.isLockOnFinished());
	contentDTO.setReflectOnActivity(bbb.isReflectOnActivity());
	contentDTO.setReflectInstructions(bbb.getReflectInstructions());

	request.setAttribute(Constants.ATTR_CONTENT_DTO, contentDTO);

	// Set the content in use flag.
	if (!bbb.isContentInUse()) {
	    bbb.setContentInUse(true);
	    bbbService.saveOrUpdateBbb(bbb);
	}

	WebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request, getServlet().getServletContext());

	BbbUser user;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    user = bbbService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    user = getCurrentUser(toolSessionID);
	}

	// get any existing notebook entries and create userDTO
	NotebookEntry entry = bbbService.getNotebookEntry(user.getNotebookEntryUID());
	UserDTO userDTO = new UserDTO(user);
	if (entry != null) {
	    userDTO.setNotebookEntryDTO(new NotebookEntryDTO(entry));
	}
	request.setAttribute(Constants.ATTR_USER_DTO, userDTO);

	String securitySalt = bbbService.getConfigValue(Constants.CFG_SECURITYSALT);
	if (securitySalt == null) {
	    logger.error("Security Salt value " + Constants.CFG_SECURITYSALT + " returned null");
	}

	String dispatchValue = new String();
	boolean meetingOpen = false;
	if (mode.isAuthor()) {
	    dispatchValue = "openPreviewMeeting";
	    meetingOpen = true;
	} else {
	    if (session.isMeetingCreated()) {
		dispatchValue = "openLearnerMeeting";
		meetingOpen = true;
	    } // otherwise, meeting has not been started in monitoring
	}

	String meetingURL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/" + Constants.TOOL_SIGNATURE
		+ "/learning.do?dispatch=" + dispatchValue + "&" + Constants.PARAM_USER_UID + "=" + user.getUid();

	request.setAttribute(Constants.ATTR_MEETING_OPEN, meetingOpen);
	request.setAttribute(Constants.ATTR_MEETING_URL, meetingURL);

	// set toolSessionID in request
	request.setAttribute(Constants.ATTR_TOOL_SESSION_ID, session.getSessionId());

	return mapping.findForward("bbb");
    }
}
