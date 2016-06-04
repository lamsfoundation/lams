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
import org.lamsfoundation.lams.tool.bbb.dto.ContentDTO;
import org.lamsfoundation.lams.tool.bbb.dto.NotebookEntryDTO;//import org.lamsfoundation.lams.tool.bbb.dto.UserDTO;
import org.lamsfoundation.lams.tool.bbb.dto.UserDTO;
import org.lamsfoundation.lams.tool.bbb.model.Bbb;
import org.lamsfoundation.lams.tool.bbb.model.BbbSession;
import org.lamsfoundation.lams.tool.bbb.model.BbbUser;
import org.lamsfoundation.lams.tool.bbb.service.BbbServiceProxy;
import org.lamsfoundation.lams.tool.bbb.service.IBbbService;
import org.lamsfoundation.lams.tool.bbb.util.BbbException;
import org.lamsfoundation.lams.tool.bbb.util.BbbUtil;
import org.lamsfoundation.lams.tool.bbb.util.Constants;
import org.lamsfoundation.lams.tool.bbb.web.forms.MonitoringForm;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 *
 *
 *
 *
 *
 */
public class MonitoringAction extends DispatchAction {

    private static final Logger logger = Logger.getLogger(MonitoringAction.class);

    private IBbbService bbbService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// set up bbbService
	bbbService = BbbServiceProxy.getBbbService(this.getServlet().getServletContext());

	return super.execute(mapping, form, request, response);
    }

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);

	Bbb bbb = bbbService.getBbbByContentId(toolContentID);

	if (bbb == null) {
	    logger.error("Unable to find tool content with id :" + toolContentID);
	    throw new BbbException("Invalid value for " + AttributeNames.PARAM_TOOL_CONTENT_ID);
	}

	ContentDTO contentDT0 = new ContentDTO(bbb);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	contentDT0.setCurrentTab(currentTab);

	contentDT0.setGroupedActivity(bbbService.isGroupedActivity(toolContentID));

	request.setAttribute(Constants.ATTR_CONTENT_DTO, contentDT0);
	request.setAttribute(Constants.ATTR_CONTENT_FOLDER_ID, contentFolderID);


	return mapping.findForward("success");
    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long uid = new Long(WebUtil.readLongParam(request, Constants.PARAM_USER_UID));

	BbbUser user = bbbService.getUserByUID(uid);
	NotebookEntry entry = bbbService.getNotebookEntry(user.getNotebookEntryUID());

	UserDTO userDTO = new UserDTO(user);
	userDTO.setNotebookEntryDTO(new NotebookEntryDTO(entry));

	request.setAttribute(Constants.ATTR_USER_DTO, userDTO);

	return mapping.findForward("bbb_display");
    }

    public ActionForward startMeeting(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	MonitoringForm monitoringForm = (MonitoringForm) form;

	// get bbb session
	BbbSession session = bbbService.getSessionBySessionId(monitoringForm.getToolSessionID());

	// Get LAMS userDTO
	org.lamsfoundation.lams.usermanagement.dto.UserDTO lamsUserDTO = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) SessionManager
		.getSession().getAttribute(AttributeNames.USER);

	String meetingKey;

	// if the meeting is already created, redirect the monitor to the meeting directly
	if (session.isMeetingCreated()) {
	    String moderatorPassword = session.getModeratorPassword();
	    String attendeePassword = session.getAttendeePassword();
	    meetingKey = BbbUtil.getMeetingKey(session.getSessionId(), attendeePassword);
	    logger.debug("Meeting Room is already created with meetingKey=" + meetingKey);
	    String redirectUrl = bbbService.getJoinMeetingURL(lamsUserDTO, meetingKey, moderatorPassword);
	    // redirect to meeting
	    response.sendRedirect(redirectUrl);
	    return null;
	}

	// create random strings for attendee and moderator passwords
	String attendeePassword = RandomPasswordGenerator.nextPassword(20);
	String moderatorPassword = RandomPasswordGenerator.nextPassword(20);
	MessageResources resources = MessageResources.getMessageResources(Constants.APP_RESOURCES);
	meetingKey = BbbUtil.getMeetingKey(session.getSessionId(), attendeePassword);

	// Get default localized welcome message

	String welcomeMessage = resources.getMessage("activity.welcome.message");

	logger.debug("Creating new room with meetingKey =" + meetingKey);

	String returnURL = BbbUtil.getReturnURL(request);

	logger.debug("Returl URL= " + returnURL);

	String meetingStartResponse = bbbService.startConference(meetingKey, attendeePassword, moderatorPassword,
		returnURL, welcomeMessage);

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

	bbbService.saveOrUpdateBbbSession(session);

	return null;
    }
}
