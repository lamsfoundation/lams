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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.notebook.web.actions;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookDTO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.service.NotebookServiceProxy;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.tool.notebook.util.NotebookException;
import org.lamsfoundation.lams.tool.notebook.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
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
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;

    private INotebookService notebookService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	LearningForm learningForm = (LearningForm) form;

	// 'toolSessionID' and 'mode' paramters are expected to be present.
	// TODO need to catch exceptions and handle errors.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,
		LearningAction.MODE_OPTIONAL);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// set up notebookService
	if (notebookService == null) {
	    notebookService = NotebookServiceProxy.getNotebookService(this.getServlet().getServletContext());
	}

	// Retrieve the session and content.
	NotebookSession notebookSession = notebookService.getSessionBySessionId(toolSessionID);
	if (notebookSession == null) {
	    throw new NotebookException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}

	Notebook notebook = notebookSession.getNotebook();

	// check defineLater
	if (notebook.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set mode, toolSessionID and NotebookDTO
	request.setAttribute("mode", mode.toString());
	learningForm.setToolSessionID(toolSessionID);

	NotebookDTO notebookDTO = new NotebookDTO();
	notebookDTO.title = notebook.getTitle();
	notebookDTO.instructions = notebook.getInstructions();
	notebookDTO.allowRichEditor = notebook.isAllowRichEditor();
	notebookDTO.lockOnFinish = notebook.isLockOnFinished();

	request.setAttribute("notebookDTO", notebookDTO);

	// Set the content in use flag.
	if (!notebook.isContentInUse()) {
	    notebook.setContentInUse(new Boolean(true));
	    notebookService.saveOrUpdateNotebook(notebook);
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request,
		getServlet().getServletContext());

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
	    learningForm.setEntryText(nbEntry.getEntry());
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
		return mapping.findForward("submissionDeadline");
	    }
	}

	return mapping.findForward("notebook");
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

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");
	LearningForm learningForm = (LearningForm) form;
	NotebookUser notebookUser = getCurrentUser(toolSessionID);

	if (learningForm.getContentEditable()) {
	    // TODO fix idType to use real value not 999
	    if (notebookUser.getEntryUID() == null) {
		notebookUser.setEntryUID(notebookService.createNotebookEntry(toolSessionID,
			CoreNotebookConstants.NOTEBOOK_TOOL, NotebookConstants.TOOL_SIGNATURE,
			notebookUser.getUserId().intValue(), learningForm.getEntryText()));
	    } else {
		// update existing entry.
		notebookService.updateEntry(notebookUser.getEntryUID(), learningForm.getEntryText());
	    }

	    notebookUser.setFinishedActivity(true);
	    notebookService.saveOrUpdateNotebookUser(notebookUser);
	}

	ToolSessionManager sessionMgrService = NotebookServiceProxy
		.getNotebookSessionManager(getServlet().getServletContext());

	try {
	    String nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, notebookUser.getUserId());
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