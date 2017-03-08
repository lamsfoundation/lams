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

package org.lamsfoundation.lams.tool.scribe.web.actions;

import java.io.IOException;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeDTO;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeSessionDTO;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeUserDTO;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;
import org.lamsfoundation.lams.tool.scribe.model.ScribeUser;
import org.lamsfoundation.lams.tool.scribe.service.IScribeService;
import org.lamsfoundation.lams.tool.scribe.service.ScribeServiceProxy;
import org.lamsfoundation.lams.tool.scribe.util.ScribeConstants;
import org.lamsfoundation.lams.tool.scribe.util.ScribeException;
import org.lamsfoundation.lams.tool.scribe.util.ScribeUtils;
import org.lamsfoundation.lams.tool.scribe.web.forms.LearningForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;

    private IScribeService scribeService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	// 'toolSessionID' and 'mode' paramters are expected to be present.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE,
		LearningAction.MODE_OPTIONAL);

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// set up scribeService
	if (scribeService == null) {
	    scribeService = ScribeServiceProxy.getScribeService(this.getServlet().getServletContext());
	}
	scribeService.createReportEntry(toolSessionID);

	// Retrieve the session and content.
	ScribeSession scribeSession = scribeService.getSessionBySessionId(toolSessionID);
	if (scribeSession == null) {
	    throw new ScribeException("Cannot retrieve session with toolSessionID" + toolSessionID);
	}
	Scribe scribe = scribeSession.getScribe();

	// check defineLater
	if (scribe.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// Ensure that the content in use flag is set.
	if (!scribe.isContentInUse()) {
	    scribe.setContentInUse(new Boolean(true));
	    scribeService.saveOrUpdateScribe(scribe);
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionID, request,
		getServlet().getServletContext());

	// Retrieve the current user
	ScribeUser scribeUser = getCurrentUser(toolSessionID);

	// check whether scribe has been appointed
	while (scribeSession.getAppointedScribe() == null) {
	    // check autoSelectScribe
	    if (scribe.isAutoSelectScribe() == false) {
		// learner needs to wait until a scribe has been appointed by
		// teacher.
		return mapping.findForward("waitForScribe");

	    } else {
		// appoint the currentUser as the scribe
		scribeSession.setAppointedScribe(scribeUser);
		scribeService.saveOrUpdateScribeSession(scribeSession);
	    }
	}

	// setup dto's forms and attributes.
	((LearningForm) form).setToolSessionID(scribeSession.getSessionId());
	request.setAttribute("MODE", mode.toString());
	setupDTOs(request, scribeSession, scribeUser);

	// check force complete
	if (scribeSession.isForceComplete()) {
	    // go to report page
	    if (scribeSession.getScribe().isShowAggregatedReports()) {
		setupOtherGroupReportDTO(request, scribeSession, scribeUser);
	    }
	    return mapping.findForward("report");
	}

	// check if user has started activity
	if (!scribeUser.isStartedActivity()) {
	    if (scribeSession.getAppointedScribe().getUid() == scribeUser.getUid()) {
		request.setAttribute("role", "scribe");
	    } else {
		request.setAttribute("role", "learner");
	    }
	    return mapping.findForward("instructions");
	}

	// check if current user is the scribe.
	if (scribeSession.getAppointedScribe().getUid() == scribeUser.getUid()) {
	    return mapping.findForward("scribe");
	} else {
	    return mapping.findForward("learning");
	}

    }

    private ScribeUser getCurrentUser(Long toolSessionID) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionID
	ScribeUser scribeUser = scribeService.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()),
		toolSessionID);

	if (scribeUser == null) {
	    ScribeSession scribeSession = scribeService.getSessionBySessionId(toolSessionID);
	    scribeUser = scribeService.createScribeUser(user, scribeSession);
	}

	return scribeUser;
    }

    public ActionForward startActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	LearningForm lrnForm = (LearningForm) form;
	Long toolSessionID = lrnForm.getToolSessionID();

	ScribeSession scribeSession = scribeService.getSessionBySessionId(toolSessionID);
	ScribeUser scribeUser = getCurrentUser(toolSessionID);

	// setup dto's, forms and attributes.
	lrnForm.setToolSessionID(scribeSession.getSessionId());
	request.setAttribute("MODE", lrnForm.getMode());
	setupDTOs(request, scribeSession, scribeUser);

	// update scribe user and go to instructions page
	scribeUser.setStartedActivity(true);
	scribeService.saveOrUpdateScribeUser(scribeUser);

	// check if current user is the scribe.
	if (scribeSession.getAppointedScribe().getUid() == scribeUser.getUid()) {
	    return mapping.findForward("scribe");
	}

	return mapping.findForward("learning");
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	LearningForm lrnForm = (LearningForm) form;

	// set the finished flag
	ScribeUser scribeUser = scribeService.getUserByUID(lrnForm.getScribeUserUID());
	if (scribeUser != null) {
	    scribeUser.setFinishedActivity(true);
	    scribeService.saveOrUpdateScribeUser(scribeUser);
	} else {
	    LearningAction.log
		    .error("finishActivity(): couldn't find ScribeUser with uid: " + lrnForm.getScribeUserUID());
	}

	ToolSessionManager sessionMgrService = ScribeServiceProxy
		.getScribeSessionManager(getServlet().getServletContext());

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(user.getUserID().longValue());
	Long toolSessionID = scribeUser.getScribeSession().getSessionId();

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, userID);
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new ScribeException(e);
	} catch (ToolException e) {
	    throw new ScribeException(e);
	} catch (IOException e) {
	    throw new ScribeException(e);
	}

	return null; // TODO need to return proper page.
    }

    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	LearningForm lrnForm = (LearningForm) form;

	// set the finished flag
	ScribeUser scribeUser = scribeService.getUserByUID(lrnForm.getScribeUserUID());
	ScribeDTO scribeDTO = new ScribeDTO(scribeUser.getScribeSession().getScribe());

	request.setAttribute("scribeDTO", scribeDTO);

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(scribeUser.getScribeSession().getSessionId(),
		request, getServlet().getServletContext());

	return mapping.findForward("notebook");
    }

    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// save the reflection entry and call the notebook.

	LearningForm lrnForm = (LearningForm) form;

	ScribeUser scribeUser = scribeService.getUserByUID(lrnForm.getScribeUserUID());

	scribeService.createNotebookEntry(scribeUser.getScribeSession().getSessionId(),
		CoreNotebookConstants.NOTEBOOK_TOOL, ScribeConstants.TOOL_SIGNATURE, scribeUser.getUserId().intValue(),
		lrnForm.getEntryText());

	return finishActivity(mapping, form, request, response);
    }

    public ActionForward forceCompleteActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	LearningForm lrnForm = (LearningForm) form;
	ScribeUser scribeUser = scribeService.getUserByUID(lrnForm.getScribeUserUID());
	ScribeSession session = scribeUser.getScribeSession();

	if (session.getAppointedScribe().getUid() == scribeUser.getUid()) {
	    session.setForceComplete(true);
	} else {
	    // TODO need to implement this.
	    LearningAction.log
		    .error("ScribeUserUID: " + scribeUser.getUid() + " is not allowed to forceComplete this session");
	}

	request.setAttribute("MODE", lrnForm.getMode());
	setupDTOs(request, session, scribeUser);
	scribeService.saveOrUpdateScribeUser(scribeUser);

	LearningWebsocketServer.sendCloseRequest(session.getSessionId());

	if (session.getScribe().isShowAggregatedReports()) {
	    setupOtherGroupReportDTO(request, session, scribeUser);
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(session.getSessionId(), request,
		getServlet().getServletContext());

	return mapping.findForward("report");
    }

    // Private methods.

    /**
     * Set up all the DTO relating to this session. Doesn't set up the DTO containing the reports of the other groups.
     */
    private void setupDTOs(HttpServletRequest request, ScribeSession scribeSession, ScribeUser scribeUser) {

	ScribeDTO scribeDTO = new ScribeDTO(scribeSession.getScribe());
	request.setAttribute("scribeDTO", scribeDTO);

	ScribeSessionDTO sessionDTO = new ScribeSessionDTO(scribeSession);
	request.setAttribute("scribeSessionDTO", sessionDTO);

	ScribeUserDTO scribeUserDTO = new ScribeUserDTO(scribeUser);
	if (scribeUser.isFinishedActivity()) {
	    // get the notebook entry.
	    NotebookEntry notebookEntry = scribeService.getEntry(scribeSession.getSessionId(),
		    CoreNotebookConstants.NOTEBOOK_TOOL, ScribeConstants.TOOL_SIGNATURE,
		    scribeUser.getUserId().intValue());
	    if (notebookEntry != null) {
		scribeUserDTO.notebookEntry = notebookEntry.getEntry();
	    }
	}
	request.setAttribute("scribeUserDTO", scribeUserDTO);
    }

    /**
     * Create a map of the reports (in ScribeSessionDTO format) for all the other groups/sessions, where the key is the
     * group/session name. The code ensures that the session name is unique, adding the session id if necessary. It will
     * only include the finalized reports.
     */
    private void setupOtherGroupReportDTO(HttpServletRequest request, ScribeSession scribeSession,
	    ScribeUser scribeUser) {
	TreeMap<String, ScribeSessionDTO> otherScribeSessions = ScribeUtils.getReportDTOs(scribeSession);
	if (otherScribeSessions.size() > 0) {
	    request.setAttribute("otherScribeSessions", otherScribeSessions.values());
	}
    }

}
