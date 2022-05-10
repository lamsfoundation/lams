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

package org.lamsfoundation.lams.tool.leaderselection.web.controller;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionSession;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.lamsfoundation.lams.tool.leaderselection.service.ILeaderselectionService;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionException;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/learning")
public class LearningController {

    @Autowired
    private ILeaderselectionService leaderselectionService;

    @RequestMapping(value = "")
    public String unspecified(HttpServletRequest request) throws Exception {

	// 'toolSessionID' and 'mode' paramters are expected to be present.
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, false);
	// set up service

	// Retrieve the session and content.
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	LeaderselectionSession session = leaderselectionService.getSessionBySessionId(toolSessionId);
	if (session == null) {
	    throw new LeaderselectionException("Cannot retrieve session with toolSessionID" + toolSessionId);
	}

	Leaderselection content = session.getLeaderselection();
	request.setAttribute(LeaderselectionConstants.ATTR_CONTENT, content);

	// check defineLater
	if (content.isDefineLater()) {
	    return "pages/learning/defineLater";
	}

	// set mode, toolSessionID and LeaderselectionDTO
	request.setAttribute("mode", mode.toString());
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);

	// Set the content in use flag.
	if (!content.isContentInUse()) {
	    content.setContentInUse(true);
	    leaderselectionService.saveOrUpdateLeaderselection(content);
	}

	LeaderselectionUser user;
	if (mode.equals(ToolAccessMode.TEACHER)) {
	    Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID, false);
	    user = leaderselectionService.getUserByUserIdAndSessionId(userID, toolSessionId);
	} else {
	    user = getCurrentUser(toolSessionId);
	}

	LeaderselectionUser groupLeader = session.getGroupLeader();
	request.setAttribute(LeaderselectionConstants.ATTR_GROUP_LEADER, groupLeader);
	Collection<User> groupUsers = leaderselectionService.getAllGroupUsers(toolSessionId);
	request.setAttribute(LeaderselectionConstants.ATTR_GROUP_USERS, groupUsers);
	request.setAttribute(LeaderselectionConstants.ATTR_TOOL_SESSION_ID, toolSessionId);

	// checks whether to display dialog prompting to become a leader
	boolean isSelectLeaderActive = (groupLeader == null) && !user.isFinishedActivity() && !mode.isTeacher();
	request.setAttribute("isSelectLeaderActive", isSelectLeaderActive);
	request.setAttribute(AttributeNames.ATTR_IS_LAST_ACTIVITY,
		leaderselectionService.isLastActivity(toolSessionId));
	return "pages/learning/leaderselection";
    }

    /**
     * Sets current user as a leader of a group.
     *
     * @throws JSONException
     */
    @RequestMapping(value = "/becomeLeader")
    @ResponseStatus(code = HttpStatus.OK)
    public void becomeLeader(HttpServletRequest request) throws IOException {
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	LeaderselectionSession session = leaderselectionService.getSessionBySessionId(toolSessionId);

	LeaderselectionUser groupLeader = session.getGroupLeader();
	// check there is no leader yet. Just in case somebody has pressed "Yes" button faster
	if (groupLeader == null) {
	    LeaderselectionUser user = getCurrentUser(toolSessionId);
	    leaderselectionService.setGroupLeader(user.getUid(), toolSessionId);
	}
    }

    @RequestMapping(value = "/finishActivity")
    public void finishActivity(HttpServletRequest request, HttpServletResponse response) {
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	String nextActivityUrl;
	try {
	    nextActivityUrl = leaderselectionService.finishToolSession(toolSessionID, user.getUserID().longValue());
	    response.sendRedirect(nextActivityUrl);
	} catch (DataMissingException e) {
	    throw new LeaderselectionException(e);
	} catch (ToolException e) {
	    throw new LeaderselectionException(e);
	} catch (IOException e) {
	    throw new LeaderselectionException(e);
	}
    }

    private LeaderselectionUser getCurrentUser(Long toolSessionId) {
	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	// attempt to retrieve user using userId and toolSessionId
	LeaderselectionUser leaderselectionUser = leaderselectionService
		.getUserByUserIdAndSessionId(user.getUserID().longValue(), toolSessionId);

	if (leaderselectionUser == null) {
	    LeaderselectionSession leaderselectionSession = leaderselectionService.getSessionBySessionId(toolSessionId);
	    leaderselectionUser = leaderselectionService.createLeaderselectionUser(user, leaderselectionSession);
	}

	return leaderselectionUser;
    }
}
