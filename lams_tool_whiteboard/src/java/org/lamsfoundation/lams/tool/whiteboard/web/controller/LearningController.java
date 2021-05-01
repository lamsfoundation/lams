/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
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

package org.lamsfoundation.lams.tool.whiteboard.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.whiteboard.WhiteboardConstants;
import org.lamsfoundation.lams.tool.whiteboard.model.Whiteboard;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardSession;
import org.lamsfoundation.lams.tool.whiteboard.model.WhiteboardUser;
import org.lamsfoundation.lams.tool.whiteboard.service.IWhiteboardService;
import org.lamsfoundation.lams.tool.whiteboard.service.WhiteboardApplicationException;
import org.lamsfoundation.lams.tool.whiteboard.service.WhiteboardService;
import org.lamsfoundation.lams.tool.whiteboard.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/learning")
public class LearningController {

    private static Logger log = Logger.getLogger(LearningController.class);

    @Autowired
    private IWhiteboardService whiteboardService;

    /**
     * Read Whiteboard data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     * @throws UnsupportedEncodingException
     *
     */
    @RequestMapping("/start")
    private String start(HttpServletRequest request, HttpServletResponse response)
	    throws WhiteboardApplicationException, UnsupportedEncodingException {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(WhiteboardConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long toolSessionId = WebUtil.readLongParam(request, WhiteboardConstants.PARAM_TOOL_SESSION_ID);
	Whiteboard whiteboard = whiteboardService.getWhiteboardBySessionId(toolSessionId);
	WhiteboardSession session = whiteboardService.getWhiteboardSessionBySessionId(toolSessionId);
	sessionMap.put(WhiteboardConstants.ATTR_TOOL_CONTENT_ID, whiteboard.getContentId());

	// get back the whiteboard and item list and display them on page
	WhiteboardUser user = null;
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	// get back login user DTO
	HttpSession ss = SessionManager.getSession();
	UserDTO currentUserDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // whiteboardUser may be null if the user was force completed.
	    user = getSpecifiedUser(toolSessionId, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = whiteboardService.getUserByIDAndSession(currentUserDto.getUserID().longValue(), toolSessionId);
	    if (user == null) {
		user = new WhiteboardUser(currentUserDto, session);
		whiteboardService.saveOrUpdate(user);
	    }
	}

	// support for leader select feature
	WhiteboardUser leader = whiteboard.isUseSelectLeaderToolOuput()
		? whiteboardService.checkLeaderSelectToolForSessionLeader(user, toolSessionId)
		: null;
	// forwards to the leaderSelection page
	if (whiteboard.isUseSelectLeaderToolOuput() && leader == null && !mode.isTeacher()) {
	    // get group users and store it to request as DTO objects
	    List<WhiteboardUser> groupUsers = whiteboardService.getUsersBySession(toolSessionId);
	    List<User> groupUserDtos = new ArrayList<>();
	    for (WhiteboardUser groupUser : groupUsers) {
		User groupUserDto = new User();
		groupUserDto.setUserId(groupUser.getUserId().intValue());
		groupUserDto.setFirstName(groupUser.getFirstName());
		groupUserDto.setLastName(groupUser.getLastName());
		groupUserDtos.add(groupUserDto);
	    }
	    request.setAttribute(WhiteboardConstants.ATTR_GROUP_USERS, groupUserDtos);
	    request.setAttribute(WhiteboardConstants.ATTR_WHITEBOARD, whiteboard);
	    return "pages/learning/waitforleader";
	}

	boolean isUserLeader = user != null && leader != null && user.getUid().equals(leader.getUid());
	boolean hasEditRight = !whiteboard.isUseSelectLeaderToolOuput()
		|| whiteboard.isUseSelectLeaderToolOuput() && isUserLeader;
	sessionMap.put(WhiteboardConstants.ATTR_HAS_EDIT_RIGHT, hasEditRight);
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	sessionMap.put(WhiteboardConstants.ATTR_REFLECTION_ON, whiteboard.isReflectOnActivity());
	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, whiteboardService.isLastActivity(toolSessionId));
	sessionMap.put(WhiteboardConstants.ATTR_WHITEBOARD, whiteboard);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);

	// reflection information
	String entryText = new String();
	if (user != null) {
	    NotebookEntry notebookEntry = whiteboardService.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    WhiteboardConstants.TOOL_SIGNATURE, user.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}
	sessionMap.put(WhiteboardConstants.ATTR_REFLECTION_INSTRUCTION, whiteboard.getReflectInstructions());
	sessionMap.put(WhiteboardConstants.ATTR_REFLECTION_ENTRY, entryText);

	if (whiteboard.isGalleryWalkEnabled() && mode != null && mode.isAuthor()) {
	    String[] galleryWalkParameterValuesArray = request.getParameterValues("galleryWalk");
	    if (galleryWalkParameterValuesArray != null) {
		Collection<String> galleryWalkParameterValues = Set.of(galleryWalkParameterValuesArray);

		if (!whiteboard.isGalleryWalkStarted() && galleryWalkParameterValues.contains("forceStart")) {
		    whiteboard.setGalleryWalkStarted(true);
		    whiteboardService.saveOrUpdate(whiteboard);
		}

		if (!whiteboard.isGalleryWalkFinished() && galleryWalkParameterValues.contains("forceFinish")) {
		    whiteboard.setGalleryWalkFinished(true);
		    whiteboardService.saveOrUpdate(whiteboard);
		}
	    }
	}

	if (whiteboard.isGalleryWalkStarted()) {
//	    List<SessionDTO> groupList = null;
//	    try {
//		groupList = whiteboardService.getSummary(whiteboard.getContentId(), user.getUserId());
//	    } catch (HibernateOptimisticLockingFailureException e) {
//		log.warn("Ignoring error caused probably by creating Gallery Walk criteria", e);
//		// simply run the transaction again
//		groupList = whiteboardService.getSummary(whiteboard.getContentId(), user.getUserId());
//	    }
//	    request.setAttribute(WhiteboardConstants.ATTR_SUMMARY_LIST, groupList);
//
//	    if (currentUserDto == null) {
//		currentUserDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
//	    }
//	    Cookie cookie = whiteboardService.createEtherpadCookieForMonitor(currentUserDto, whiteboard.getContentId());
//	    response.addCookie(cookie);
	    return "pages/learning/galleryWalk";
	}

	// check whether finish lock is on/off
	boolean finishedLock = whiteboard.getLockWhenFinished() && (user != null) && user.isSessionFinished();
	sessionMap.put(WhiteboardConstants.ATTR_TITLE, whiteboard.getTitle());
	sessionMap.put(WhiteboardConstants.ATTR_INSTRUCTIONS, whiteboard.getInstructions());
	sessionMap.put(WhiteboardConstants.ATTR_FINISH_LOCK, finishedLock);
	sessionMap.put(WhiteboardConstants.ATTR_LOCK_ON_FINISH, whiteboard.getLockWhenFinished());
	sessionMap.put(WhiteboardConstants.ATTR_USER_FINISHED, (user != null) && user.isSessionFinished());

	// add define later support
	if (whiteboard.isDefineLater()) {
	    return "pages/learning/definelater";
	}

	// set contentInUse flag to true!
	whiteboard.setContentInUse(true);
	whiteboard.setDefineLater(false);
	whiteboardService.saveOrUpdate(whiteboard);

	//time limit
	boolean isTimeLimitExceeded = whiteboardService.checkTimeLimitExceeded(whiteboard, user.getUserId().intValue());
	request.setAttribute("timeLimitExceeded", isTimeLimitExceeded);

	String whiteboardServerUrl = whiteboardService.getWhiteboardServerUrl();
	request.setAttribute("whiteboardServerUrl", whiteboardServerUrl);

	String whiteboardAuthorName = WhiteboardService.getWhiteboardAuthorName(currentUserDto);
	request.setAttribute("whiteboardAuthorName", whiteboardAuthorName);

	// This is just a convention used for Whiteboard canvases in lessons.
	// Authored canvases are recognised by their corresponding tool content ID without session ID part.
	String wid = whiteboard.getContentId() + "-" + toolSessionId;
	if (!hasEditRight) {
	    wid = whiteboardService.getWhiteboardReadOnlyWid(wid);
	}
	request.setAttribute("wid", wid);
	String whiteboardAccessTokenHash = whiteboardService.getWhiteboardAccessTokenHash(wid, null);
	request.setAttribute("whiteboardAccessToken", whiteboardAccessTokenHash);

	if (StringUtils.isNotBlank(whiteboard.getSourceWid())) {
	    String whiteboardCopyAccessTokenHash = whiteboardService.getWhiteboardAccessTokenHash(wid,
		    whiteboard.getSourceWid());
	    request.setAttribute("whiteboardCopyAccessToken", whiteboardCopyAccessTokenHash);
	}

	return "pages/learning/learning";
    }

    /**
     * Checks Leader Progress
     */
    @RequestMapping("/checkLeaderProgress")
    private String checkLeaderProgress(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// boolean isLeaderResponseFinalized = whiteboardService.isLeaderResponseFinalized(toolSessionId);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	// responseJSON.put(WhiteboardConstants.ATTR_IS_LEADER_RESPONSE_FINALIZED, isLeaderResponseFinalized);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(responseJSON);
	return responseJSON.toString();
    }

    /**
     * Finish learning session.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/finish")
    private String finish(HttpServletRequest request) {

	// get back SessionMap
	String sessionMapID = request.getParameter(WhiteboardConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = whiteboardService.finishToolSession(sessionId, userID);
	    request.setAttribute(WhiteboardConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (WhiteboardApplicationException e) {
	    LearningController.log.error("Failed get next activity url:" + e.getMessage());
	}

	return "pages/learning/finish";
    }

    /**
     * Display empty reflection form.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/newReflection")
    private String newReflection(@ModelAttribute("reflectionForm") ReflectionForm reflectionForm,
	    HttpServletRequest request) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, WhiteboardConstants.ATTR_SESSION_MAP_ID);
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	reflectionForm.setUserID(user.getUserID());
	reflectionForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = whiteboardService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		WhiteboardConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    reflectionForm.setEntryText(entry.getEntry());
	}

	return "pages/learning/notebook";
    }

    /**
     * Submit reflection form input database.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/submitReflection")
    private String submitReflection(@ModelAttribute("reflectionForm") ReflectionForm reflectionForm,
	    HttpServletRequest request) {
	Integer userId = reflectionForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, WhiteboardConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	NotebookEntry entry = whiteboardService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		WhiteboardConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    whiteboardService.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    WhiteboardConstants.TOOL_SIGNATURE, userId, reflectionForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(reflectionForm.getEntryText());
	    entry.setLastModified(new Date());
	    whiteboardService.updateEntry(entry);
	}

	return finish(request);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************

    private WhiteboardUser getSpecifiedUser(Long sessionId, Integer userId) {
	WhiteboardUser whiteboardUser = whiteboardService.getUserByIDAndSession(userId.longValue(), sessionId);
	if (whiteboardUser == null) {
	    LearningController.log.error(
		    "Unable to find specified user for Whiteboard activity. Screens are likely to fail. Session ID = "
			    + sessionId + ", user ID = " + userId);
	}
	return whiteboardUser;
    }
}
