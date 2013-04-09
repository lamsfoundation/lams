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
/* $Id$ */

package org.lamsfoundation.lams.tool.dimdim.web.actions;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.dimdim.dto.ContentDTO;
import org.lamsfoundation.lams.tool.dimdim.dto.NotebookEntryDTO;
import org.lamsfoundation.lams.tool.dimdim.dto.UserDTO;
import org.lamsfoundation.lams.tool.dimdim.model.Dimdim;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimSession;
import org.lamsfoundation.lams.tool.dimdim.model.DimdimUser;
import org.lamsfoundation.lams.tool.dimdim.service.DimdimServiceProxy;
import org.lamsfoundation.lams.tool.dimdim.service.IDimdimService;
import org.lamsfoundation.lams.tool.dimdim.util.Constants;
import org.lamsfoundation.lams.tool.dimdim.util.DimdimException;
import org.lamsfoundation.lams.tool.dimdim.util.DimdimUtil;
import org.lamsfoundation.lams.tool.dimdim.web.forms.LearningForm;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author Anthony Sukkar
 * 
 * @struts.action path="/learning" parameter="dispatch" scope="request" name="learningForm"
 * @struts.action-forward name="dimdim" path="tiles:/learning/main"
 * @struts.action-forward name="notebook" path="tiles:/learning/notebook"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 * @struts.action-forward name="generalMessage" path="tiles:/general/message"
 */
public class LearningAction extends DispatchAction {

    private static final Logger logger = Logger.getLogger(LearningAction.class);

    private IDimdimService dimdimService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// set up dimdimService
	dimdimService = DimdimServiceProxy.getDimdimService(this.getServlet().getServletContext());

	return super.execute(mapping, form, request, response);
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	DimdimUser user = getCurrentUser(toolSessionID);

	if (user != null) {

	    LearningForm learningForm = (LearningForm) form;

	    if (user.getNotebookEntryUID() == null) {
		user.setNotebookEntryUID(dimdimService.createNotebookEntry(toolSessionID,
			CoreNotebookConstants.NOTEBOOK_TOOL, Constants.TOOL_SIGNATURE, user.getUserId().intValue(),
			learningForm.getEntryText()));
	    } else {
		// update existing entry.
		dimdimService.updateNotebookEntry(user.getNotebookEntryUID(), learningForm.getEntryText());
	    }

	    user.setFinishedActivity(true);
	    dimdimService.saveOrUpdateDimdimUser(user);
	} else {
	    logger.error("finishActivity(): couldn't find/create DimdimUser in toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = DimdimServiceProxy.getDimdimSessionManager(getServlet()
		.getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, user.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new DimdimException(e);
	} catch (ToolException e) {
	    throw new DimdimException(e);
	} catch (IOException e) {
	    throw new DimdimException(e);
	}

	return null;
    }

    private DimdimUser getCurrentUser(Long toolSessionId) {
	org.lamsfoundation.lams.usermanagement.dto.UserDTO lamsUserDTO = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) SessionManager
		.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	DimdimUser user = dimdimService.getUserByUserIdAndSessionId(new Long(lamsUserDTO.getUserID().intValue()),
		toolSessionId);

	if (user == null) {
	    DimdimSession dimdimSession = dimdimService.getSessionBySessionId(toolSessionId);
	    user = dimdimService.createDimdimUser(lamsUserDTO, dimdimSession);
	}

	return user;
    }

    public ActionForward openLearnerMeeting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// get user uid parameter
	Long uid = WebUtil.readLongParam(request, Constants.PARAM_USER_UID);
	DimdimUser user = dimdimService.getUserByUID(uid);

	String meetingKey = DimdimUtil.getMeetingKey(user.getDimdimSession().getSessionId());
	org.lamsfoundation.lams.usermanagement.dto.UserDTO lamsUserDTO = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) SessionManager
		.getSession().getAttribute(AttributeNames.USER);
	String meetingURL = dimdimService.getDimdimJoinConferenceURL(lamsUserDTO, meetingKey);

	if (meetingURL != null) {
	    response.sendRedirect(meetingURL);
	} else {
	    logger.error("startAction did not return a url to start the meeting");
	    request.setAttribute(Constants.ATTR_MESSAGE_KEY, "message.unableToStartLesson");
	    return mapping.findForward("generalMessage");
	}

	return null;
    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	LearningForm lrnForm = (LearningForm) form;

	// set the finished flag
	DimdimUser user = getCurrentUser(lrnForm.getToolSessionID());
	ContentDTO contentDTO = new ContentDTO(user.getDimdimSession().getDimdim());

	request.setAttribute(Constants.ATTR_CONTENT_DTO, contentDTO);

	NotebookEntry notebookEntry = dimdimService.getNotebookEntry(user.getNotebookEntryUID());

	if (notebookEntry != null) {
	    lrnForm.setEntryText(notebookEntry.getEntry());
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(lrnForm.getToolSessionID(), request, getServlet()
		.getServletContext());
	
	return mapping.findForward("notebook");
    }

    public ActionForward openPreviewMeeting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// get user uid parameter
	Long uid = WebUtil.readLongParam(request, Constants.PARAM_USER_UID);
	DimdimUser user = dimdimService.getUserByUID(uid);
	DimdimSession session = user.getDimdimSession();

	// Get LAMS userDTO
	org.lamsfoundation.lams.usermanagement.dto.UserDTO lamsUserDTO = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) SessionManager
		.getSession().getAttribute(AttributeNames.USER);

	// Start a new dimdim web meeting
	String meetingKey = DimdimUtil.getMeetingKey(session.getSessionId());
	String returnURL = DimdimUtil.getReturnURL(request);
	Integer maxAttendeeMikes = session.getDimdim().getMaxAttendeeMikes();

	String meetingURL = dimdimService.getDimdimStartConferenceURL(lamsUserDTO, meetingKey, returnURL,
		maxAttendeeMikes);

	if (meetingURL != null) {
	    response.sendRedirect(meetingURL);
	} else {
	    logger.error("startAction did not return a url to start the meeting");
	    throw new DimdimException("Unable to start meeting");
	}

	return null;
    }

    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// save the reflection entry and call the notebook.

	LearningForm lrnForm = (LearningForm) form;

	DimdimUser user = getCurrentUser(lrnForm.getToolSessionID());
	Long toolSessionID = user.getDimdimSession().getSessionId();
	Integer userID = user.getUserId().intValue();

	// check for existing notebook entry
	NotebookEntry entry = dimdimService.getNotebookEntry(user.getNotebookEntryUID());

	if (entry == null) {
	    // create new entry
	    Long entryUID = dimdimService.createNotebookEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		    Constants.TOOL_SIGNATURE, userID, lrnForm.getEntryText());
	    user.setNotebookEntryUID(entryUID);
	    dimdimService.saveOrUpdateDimdimUser(user);
	} else {
	    // update existing entry
	    entry.setEntry(lrnForm.getEntryText());
	    entry.setLastModified(new Date());
	    dimdimService.updateNotebookEntry(entry);
	}

	return finishActivity(mapping, form, request, response);
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	LearningForm learningForm = (LearningForm) form;

	// 'toolSessionID' and 'mode' parameters are expected to be present.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// Retrieve the session and content.
	DimdimSession session = dimdimService.getSessionBySessionId(toolSessionID);
	if (session == null) {
	    throw new DimdimException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}

	Dimdim dimdim = session.getDimdim();

	// check defineLater
	if (dimdim.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set mode, toolSessionID and DimdimDTO
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	learningForm.setToolSessionID(toolSessionID);

	ContentDTO contentDTO = new ContentDTO();
	contentDTO.setTitle(dimdim.getTitle());
	contentDTO.setInstructions(dimdim.getInstructions());
	contentDTO.setLockOnFinish(dimdim.isLockOnFinished());
	contentDTO.setReflectOnActivity(dimdim.isReflectOnActivity());
	contentDTO.setReflectInstructions(dimdim.getReflectInstructions());

	request.setAttribute(Constants.ATTR_CONTENT_DTO, contentDTO);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request, getServlet()
		.getServletContext());

	// Set the content in use flag.
	if (!dimdim.isContentInUse()) {
	    dimdim.setContentInUse(true);
	    dimdimService.saveOrUpdateDimdim(dimdim);
	}

	// check runOffline
	if (dimdim.isRunOffline()) {
	    return mapping.findForward("runOffline");
	}

	DimdimUser user;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    user = dimdimService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    user = getCurrentUser(toolSessionID);
	}

	// get any existing notebook entries and create userDTO
	NotebookEntry entry = dimdimService.getNotebookEntry(user.getNotebookEntryUID());
	UserDTO userDTO = new UserDTO(user);
	if (entry != null) {
	    userDTO.setNotebookEntryDTO(new NotebookEntryDTO(entry));
	}
	request.setAttribute(Constants.ATTR_USER_DTO, userDTO);

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

	return mapping.findForward("dimdim");
    }
}
