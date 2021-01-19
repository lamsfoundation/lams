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
import java.security.InvalidParameterException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.tool.leaderselection.dto.LeaderselectionDTO;
import org.lamsfoundation.lams.tool.leaderselection.dto.LeaderselectionSessionDTO;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionSession;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.lamsfoundation.lams.tool.leaderselection.service.ILeaderselectionService;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionConstants;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
    private static Logger log = Logger.getLogger(MonitoringController.class);

    @Autowired
    private ILeaderselectionService leaderselectionService;

    @Autowired
    private ILamsToolService toolService;

    @Autowired
    @Qualifier("leaderselectionMessageService")
    private MessageService messageService;

    @RequestMapping(value = "")
    public String unspecified(HttpServletRequest request) {

	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(LeaderselectionConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	Leaderselection content = leaderselectionService.getContentByContentId(toolContentID);
	if (content == null) {
	    // TODO error page.
	}

	// // cache into sessionMap
	boolean isGroupedActivity = leaderselectionService.isGroupedActivity(toolContentID);
	sessionMap.put(LeaderselectionConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	// sessionMap.put(ScratchieConstants.PAGE_EDITABLE, scratchie.isContentInUse());
	// sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	// sessionMap.put(ScratchieConstants.ATTR_TOOL_CONTENT_ID, toolContentID);
	// sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
	// WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	// sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ON, scratchie.isReflectOnActivity());

	LeaderselectionDTO leaderselectionDT0 = new LeaderselectionDTO(content);
	sessionMap.put("leaderselectionDT0", leaderselectionDT0);

	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	leaderselectionDT0.setCurrentTab(currentTab);

	request.setAttribute("leaderselectionDTO", leaderselectionDT0);
	request.setAttribute("contentFolderID", contentFolderID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	return "pages/monitoring/monitoring";
    }

    /**
     * Show leaders manage page
     */
    @RequestMapping(value = "/manageLeaders")
    public String manageLeaders(HttpServletRequest request) {
	String sessionMapID = request.getParameter(LeaderselectionConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(LeaderselectionConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	return "/pages/monitoring/manageLeaders";
    }

    /**
     * Save selected users as a leaders
     *
     * @throws IOException
     * @throws JSONException
     */
    @RequestMapping(path = "/saveLeaders", method = RequestMethod.POST)
    public String saveLeaders(HttpServletRequest request) throws IOException {
	String sessionMapID = request.getParameter(LeaderselectionConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	request.setAttribute(LeaderselectionConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	LeaderselectionDTO leaderselectionDT0 = (LeaderselectionDTO) sessionMap.get("leaderselectionDT0");
	for (LeaderselectionSessionDTO sessionDto : leaderselectionDT0.getSessionDTOs()) {
	    Long toolSessionId = sessionDto.getSessionID();
	    Long leaderUserUid = WebUtil.readLongParam(request, "sessionId" + toolSessionId, true);

	    // save selected users as a leaders
	    if (leaderUserUid != null) {
		leaderselectionService.setGroupLeader(leaderUserUid, toolSessionId);
	    }
	}

	return null;
    }

    @RequestMapping("/displayChangeLeaderForGroupDialogFromActivity")
    public String displayChangeLeaderForGroupDialog(@RequestParam long toolSessionId, Model model) {
	Long leaderSelectionToolContentId = toolService.getNearestLeaderSelectionToolContentId(toolSessionId);
	if (leaderSelectionToolContentId == null) {
	    throw new InvalidParameterException(
		    "No matching Leader Selection found for activity with tool session ID " + toolSessionId);
	}
	Group group = toolService.getGroup(toolSessionId);
	if (group == null) {
	    throw new InvalidParameterException("No group found for tool session ID " + toolSessionId);
	}

	model.addAttribute("toolSessionId", toolSessionId);

	return displayChangeLeaderForGroupDialog(leaderSelectionToolContentId, group.getGroupId(), model);
    }

    @RequestMapping("/displayChangeLeaderForGroupDialog")
    public String displayChangeLeaderForGroupDialog(@RequestParam long leaderSelectionToolContentId,
	    @RequestParam long groupId, Model model) {
	Leaderselection leaderSelection = leaderselectionService.getContentByContentId(leaderSelectionToolContentId);
	if (leaderSelection == null) {
	    throw new InvalidParameterException(
		    "No Leader Selection found with tool content ID " + leaderSelectionToolContentId);
	}

	Group group = null;
	LeaderselectionSession targetSession = null;
	for (LeaderselectionSession session : leaderSelection.getLeaderselectionSessions()) {
	    group = toolService.getGroup(session.getSessionId());
	    if (group.getGroupId().equals(groupId)) {
		targetSession = session;
		break;
	    }
	}

	if (targetSession == null) {
	    throw new InvalidParameterException("No session for group with ID " + groupId
		    + " found for Leader Selection with tool content ID " + leaderSelectionToolContentId);
	}

	model.addAttribute("groupId", groupId);
	model.addAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, leaderSelectionToolContentId);
	model.addAttribute("groupName", targetSession.getSessionName());
	model.addAttribute("groupLeader", targetSession.getGroupLeader());
	model.addAttribute("members", targetSession.getUsers());

	return "/pages/monitoring/changeLeaderDialog";
    }

    /**
     * Save selected user as a leader. Called from an external tool activity.
     */
    @RequestMapping(path = "/changeLeaderForGroup", method = RequestMethod.POST)
    @ResponseBody
    public String changeLeaderForGroup(@RequestParam(name = AttributeNames.PARAM_USER_ID) long leaderUserId,
	    @RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId, HttpServletRequest request,
	    HttpServletResponse response) {
	LeaderselectionUser user = leaderselectionService.getUserByUserIdAndContentId(leaderUserId, toolContentId);

	// save selected user as a leader
	boolean isSuccessful = false;
	if (user != null) {
	    Long toolSessionId = user.getLeaderselectionSession().getSessionId();
	    log.info("Changing group leader for toolSessionId=" + toolSessionId + ". New leader's userUid="
		    + leaderUserId);
	    isSuccessful = leaderselectionService.setGroupLeader(user.getUid(), toolSessionId);
	}

	// build JSON
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("isSuccessful", isSuccessful);
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }
}