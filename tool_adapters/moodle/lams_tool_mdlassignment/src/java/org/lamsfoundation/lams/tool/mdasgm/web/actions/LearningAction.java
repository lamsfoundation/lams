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

package org.lamsfoundation.lams.tool.mdasgm.web.actions;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mdasgm.dto.MdlAssignmentDTO;
import org.lamsfoundation.lams.tool.mdasgm.model.MdlAssignment;
import org.lamsfoundation.lams.tool.mdasgm.model.MdlAssignmentSession;
import org.lamsfoundation.lams.tool.mdasgm.model.MdlAssignmentUser;
import org.lamsfoundation.lams.tool.mdasgm.service.IMdlAssignmentService;
import org.lamsfoundation.lams.tool.mdasgm.service.MdlAssignmentServiceProxy;
import org.lamsfoundation.lams.tool.mdasgm.util.MdlAssignmentConstants;
import org.lamsfoundation.lams.tool.mdasgm.util.MdlAssignmentException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * @author
 * @version
 * 
 * @struts.action path="/learning" parameter="dispatch" scope="request"
 *                name="learningForm"
 * @struts.action-forward name="mdlAssignment" path="tiles:/learning/main"
 * @struts.action-forward name="runOffline" path="tiles:/learning/runOffline"
 * @struts.action-forward name="defineLater" path="tiles:/learning/defineLater"
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static final boolean MODE_OPTIONAL = false;

    private static final String TOOL_APP_URL = Configuration.get(ConfigurationKeys.SERVER_URL) + "/tool/"
	    + MdlAssignmentConstants.TOOL_SIGNATURE + "/";

    //public static final String RELATIVE_LEARNER_URL = "mod/assignment/view.php?";

    public static final String RELATIVE_LEARNER_URL = "course/lamsframes.php?";
    public static final String MOODLE_VIEW_URL = "mod/assignment/view.php";
    public static final String RELATIVE_TEACHER_URL = "mod/assignment/submissions.php?";

    private IMdlAssignmentService mdlAssignmentService;

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// set up mdlAssignmentService
	if (mdlAssignmentService == null) {
	    mdlAssignmentService = MdlAssignmentServiceProxy.getMdlAssignmentService(this.getServlet()
		    .getServletContext());
	}

	// Retrieve the session and content.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);
	MdlAssignmentSession mdlAssignmentSession = mdlAssignmentService.getSessionBySessionId(toolSessionID);
	if (mdlAssignmentSession == null) {
	    throw new MdlAssignmentException("Cannot retreive session with toolSessionID: " + toolSessionID);
	}

	MdlAssignment mdlAssignment = mdlAssignmentSession.getMdlAssignment();
	MdlAssignmentUser mdlAssignmentUser = getCurrentUser(toolSessionID);

	// check defineLater
	if (mdlAssignment.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	MdlAssignmentDTO mdlAssignmentDTO = new MdlAssignmentDTO();
	request.setAttribute("mdlAssignmentDTO", mdlAssignmentDTO);

	// Set the content in use flag.
	if (!mdlAssignment.isContentInUse()) {
	    mdlAssignment.setContentInUse(new Boolean(true));
	    mdlAssignmentService.saveOrUpdateMdlAssignment(mdlAssignment);
	}

	// check runOffline
	if (mdlAssignment.isRunOffline()) {
	    return mapping.findForward("runOffline");
	}

	if (mdlAssignment.getExtToolContentId() != null) {
	    try {
		String responseUrl = mdlAssignmentService.getExtServerUrl(mdlAssignment.getExtLmsId());

		if (mode.equals(ToolAccessMode.TEACHER)) {
		    responseUrl += RELATIVE_TEACHER_URL;
		} else if (mode.equals(ToolAccessMode.LEARNER) || mode.equals(ToolAccessMode.AUTHOR)) {
		    responseUrl += RELATIVE_LEARNER_URL;
		}

		String returnUrl = TOOL_APP_URL + "learning.do?" + AttributeNames.PARAM_TOOL_SESSION_ID + "="
			+ toolSessionID.toString() + "&dispatch=finishActivity";

		String encodedMoodleRelativePath = URLEncoder.encode(MOODLE_VIEW_URL, "UTF8");

		returnUrl = URLEncoder.encode(returnUrl, "UTF8");

		responseUrl += "&id=" + mdlAssignmentSession.getExtSessionId() + "&returnUrl=" + returnUrl + "&dest="
			+ encodedMoodleRelativePath + "&is_learner=1" + "&isFinished="
			+ mdlAssignmentUser.isFinishedActivity();

		log.debug("Redirecting for mdl assignment learner: " + responseUrl);
		response.sendRedirect(responseUrl);
	    } catch (Exception e) {
		log.error("Could not redirect to mdl assignment authoring", e);
	    }
	} else {
	    throw new MdlAssignmentException("External content id null for learner");
	}
	return null;
    }

    private MdlAssignmentUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	MdlAssignmentUser mdlAssignmentUser = mdlAssignmentService.getUserByUserIdAndSessionId(new Long(user
		.getUserID().intValue()), toolSessionId);

	if (mdlAssignmentUser == null) {
	    MdlAssignmentSession mdlAssignmentSession = mdlAssignmentService.getSessionBySessionId(toolSessionId);
	    mdlAssignmentUser = mdlAssignmentService.createMdlAssignmentUser(user, mdlAssignmentSession);
	}

	return mdlAssignmentUser;
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, "toolSessionID");

	MdlAssignmentUser mdlAssignmentUser = getCurrentUser(toolSessionID);

	if (mdlAssignmentUser != null) {
	    mdlAssignmentUser.setFinishedActivity(true);
	    mdlAssignmentService.saveOrUpdateMdlAssignmentUser(mdlAssignmentUser);
	} else {
	    log.error("finishActivity(): couldn't find MdlAssignmentUser with id: " + mdlAssignmentUser.getUserId()
		    + "and toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = MdlAssignmentServiceProxy.getMdlAssignmentSessionManager(getServlet()
		.getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, mdlAssignmentUser.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new MdlAssignmentException(e);
	} catch (ToolException e) {
	    throw new MdlAssignmentException(e);
	} catch (IOException e) {
	    throw new MdlAssignmentException(e);
	}

	return null; // TODO need to return proper page.
    }
}
