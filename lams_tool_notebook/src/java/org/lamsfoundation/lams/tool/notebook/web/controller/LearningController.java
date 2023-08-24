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

package org.lamsfoundation.lams.tool.notebook.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookDTO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.util.NotebookException;
import org.lamsfoundation.lams.tool.notebook.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/learning")
public class LearningController {

    private static final boolean MODE_OPTIONAL = false;

    @Autowired
    private INotebookService notebookService;

    @RequestMapping(value = "")
    public String unspecified(@ModelAttribute("messageForm") LearningForm messageForm, HttpServletRequest request)
	    throws Exception {

	// 'toolSessionID' and 'mode' paramters are expected to be present.
	// TODO need to catch exceptions and handle errors.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,
		LearningController.MODE_OPTIONAL);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// Retrieve the session and content.
	NotebookSession notebookSession = notebookService.getSessionBySessionId(toolSessionID);
	if (notebookSession == null) {
	    throw new NotebookException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}

	Notebook notebook = notebookSession.getNotebook();

	// set mode, toolSessionID and NotebookDTO
	request.setAttribute("mode", mode.toString());
	messageForm.setToolSessionID(toolSessionID);

	NotebookDTO notebookDTO = new NotebookDTO();
	notebookDTO.title = notebook.getTitle();
	notebookDTO.instructions = notebook.getInstructions();
	notebookDTO.allowRichEditor = notebook.isAllowRichEditor();
	notebookDTO.lockOnFinish = notebook.isLockOnFinished();
	notebookDTO.forceResponse = notebook.isForceResponse();
	request.setAttribute("notebookDTO", notebookDTO);

	// check defineLater
	if (notebook.isDefineLater()) {
	    return "pages/learning/defineLater";
	}

	// Set the content in use flag.
	if (!notebook.isContentInUse()) {
	    notebook.setContentInUse(true);
	    notebookService.saveOrUpdateNotebook(notebook);
	}

	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY, notebookService.isLastActivity(toolSessionID));

	NotebookUser notebookUser;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    notebookUser = notebookService.getUserByUserIdAndSessionId(userID, toolSessionID);
	} else {
	    notebookUser = getCurrentUser(toolSessionID);
	}

	// get any existing notebook entry
	NotebookEntry nbEntry = null;
	if (notebookUser != null) {
	    nbEntry = notebookService.getEntry(notebookUser.getEntryUID());
	}
	if (nbEntry != null) {
	    messageForm.setEntryText(nbEntry.getEntry());
	}

	// set readOnly flag.
	if (mode.equals(ToolAccessMode.TEACHER) || (notebook.isLockOnFinished() && notebookUser.isFinishedActivity())) {
	    request.setAttribute("contentEditable", false);
	} else {
	    request.setAttribute("contentEditable", true);
	}

	if (notebookUser != null) {
	    // get teacher's comment if available
	    request.setAttribute("teachersComment", notebookUser.getTeachersComment());

	    request.setAttribute("finishedActivity", notebookUser.isFinishedActivity());
	    request.setAttribute(AttributeNames.ATTR_LEARNER_CONTENT_FOLDER,
		    notebookService.getLearnerContentFolder(toolSessionID, notebookUser.getUserId()));
	}

	// date and time restriction
	Date submissionDeadline = notebook.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    Date currentLearnerDate = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, new Date());
	    notebookDTO.submissionDeadline = tzSubmissionDeadline;

	    // calculate whether deadline has passed, and if so forward to "submissionDeadline"
	    if (currentLearnerDate.after(tzSubmissionDeadline)) {
		return "pages/learning/submissionDeadline";
	    }
	}
	request.setAttribute("messageForm", messageForm);

	return "pages/learning/notebook";
    }

    private NotebookUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	NotebookUser notebookUser = notebookService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionId);

	if (notebookUser == null) {
	    NotebookSession notebookSession = notebookService.getSessionBySessionId(toolSessionId);
	    notebookUser = notebookService.createNotebookUser(user, notebookSession);
	}

	return notebookUser;
    }

    @RequestMapping(value = "/finishActivity")
    public String finishActivity(@ModelAttribute("messageForm") LearningForm messageForm, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");
	NotebookUser notebookUser = getCurrentUser(toolSessionID);

	try {
	    String nextActivityUrl = notebookService.finishToolSession(notebookUser, messageForm.getContentEditable(),
		    messageForm.getEntryText());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new NotebookException(e);
	} catch (ToolException e) {
	    throw new NotebookException(e);
	} catch (IOException e) {
	    throw new NotebookException(e);
	}

	return null; // TODO need to return proper page.
    }
}