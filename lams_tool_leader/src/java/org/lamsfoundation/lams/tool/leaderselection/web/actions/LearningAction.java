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


package org.lamsfoundation.lams.tool.leaderselection.web.actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionSession;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.lamsfoundation.lams.tool.leaderselection.service.ILeaderselectionService;
import org.lamsfoundation.lams.tool.leaderselection.service.LeaderselectionServiceProxy;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 *
 *
 *
 */
public class LearningAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private ILeaderselectionService service;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	// 'toolSessionID' and 'mode' paramters are expected to be present.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);
	// set up service
	initService();

	// Retrieve the session and content.
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	LeaderselectionSession session = service.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    throw new LeaderselectionException("Cannot retrieve session with toolSessionID" + toolSessionId);
	}

	Leaderselection content = session.getLeaderselection();
	request.setAttribute(LeaderselectionConstants.ATTR_CONTENT, content);

	// check defineLater
	if (content.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set mode, toolSessionID and LeaderselectionDTO
	request.setAttribute("mode", mode.toString());
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);

	// Set the content in use flag.
	if (!content.isContentInUse()) {
	    content.setContentInUse(new Boolean(true));
	    service.saveOrUpdateLeaderselection(content);
	}

	LearningWebUtil.putActivityPositionInRequestByToolSessionId(toolSessionId, request,
		getServlet().getServletContext());

	LeaderselectionUser user;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    user = service.getUserByUserIdAndSessionId(userID, toolSessionId);
	} else {
	    user = getCurrentUser(toolSessionId);
	}

	LeaderselectionUser groupLeader = session.getGroupLeader();
	request.setAttribute(LeaderselectionConstants.ATTR_GROUP_LEADER, groupLeader);
	List<LeaderselectionUser> groupUsers = service.getUsersBySession(toolSessionId);
	request.setAttribute(LeaderselectionConstants.ATTR_GROUP_USERS, groupUsers);
	request.setAttribute(LeaderselectionConstants.ATTR_TOOL_SESSION_ID, toolSessionId);

	// checks whether to display dialog prompting to become a leader
	boolean isSelectLeaderActive = (groupLeader == null) && !user.isFinishedActivity() && !mode.isTeacher();
	request.setAttribute("isSelectLeaderActive", isSelectLeaderActive);

	return mapping.findForward("leaderselection");
    }

    /**
     * Sets current user as a leader of a group.
     * @throws JSONException 
     */
    public ActionForward becomeLeader(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, JSONException {
	initService();
	Long toolSessionId = new Long(request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID));
	LeaderselectionSession session = service.getSessionBySessionId(toolSessionId);

	LeaderselectionUser groupLeader = session.getGroupLeader();
	// check there is no leader yet. Just in case somebody has pressed "Yes" button faster
	if (groupLeader == null) {
	    LeaderselectionUser user = getCurrentUser(toolSessionId);
	    service.setGroupLeader(user.getUid(), toolSessionId);
	}

	return null;
    }

    public ActionForward finishActivity(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	LeaderselectionUser user = getCurrentUser(toolSessionID);

	if (user != null) {
	    user.setFinishedActivity(true);
	    service.saveOrUpdateUser(user);
	} else {
	    log.error("finishActivity(): couldn't find LeaderselectionUser with id: " + user.getUserId()
		    + "and toolSessionID: " + toolSessionID);
	}

	ToolSessionManager sessionMgrService = LeaderselectionServiceProxy
		.getLeaderselectionSessionManager(getServlet().getServletContext());

	String nextActivityUrl;
	try {
	    nextActivityUrl = sessionMgrService.leaveToolSession(toolSessionID, user.getUserId());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new LeaderselectionException(e);
	} catch (ToolException e) {
	    throw new LeaderselectionException(e);
	} catch (IOException e) {
	    throw new LeaderselectionException(e);
	}

	return null;
    }

    private void initService() {
	if (service == null) {
	    service = LeaderselectionServiceProxy.getLeaderselectionService(this.getServlet().getServletContext());
	}
    }

    private LeaderselectionUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	LeaderselectionUser leaderselectionUser = service
		.getUserByUserIdAndSessionId(new Long(user.getUserID().intValue()), toolSessionId);

	if (leaderselectionUser == null) {
	    LeaderselectionSession leaderselectionSession = service.getSessionBySessionId(toolSessionId);
	    leaderselectionUser = service.createLeaderselectionUser(user, leaderselectionSession);
	}

	return leaderselectionUser;
    }
}
