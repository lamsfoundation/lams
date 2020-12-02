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

package org.lamsfoundation.lams.tool.dokumaran.web.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.etherpad.EtherpadException;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.dto.SessionDTO;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranSession;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;
import org.lamsfoundation.lams.tool.dokumaran.service.DokumaranApplicationException;
import org.lamsfoundation.lams.tool.dokumaran.service.IDokumaranService;
import org.lamsfoundation.lams.tool.dokumaran.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
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

/**
 * @author Steve.Ni
 */
@Controller
@RequestMapping("/learning")
public class LearningController {

    private static Logger log = Logger.getLogger(LearningController.class);

    @Autowired
    private IDokumaranService dokumaranService;

    /**
     * Read dokumaran data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     *
     * @throws EtherpadException
     *
     * @throws DokumaranConfigurationException
     * @throws URISyntaxException
     *
     */
    @RequestMapping("/start")
    private String start(HttpServletRequest request, HttpServletResponse response)
	    throws DokumaranApplicationException, EtherpadException {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(DokumaranConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long toolSessionId = WebUtil.readLongParam(request, DokumaranConstants.PARAM_TOOL_SESSION_ID);
	Dokumaran dokumaran = dokumaranService.getDokumaranBySessionId(toolSessionId);
	DokumaranSession session = dokumaranService.getDokumaranSessionBySessionId(toolSessionId);

	// get back the dokumaran and item list and display them on page
	DokumaranUser user = null;
	boolean isFirstTimeAccess = false;
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	// get back login user DTO
	HttpSession ss = SessionManager.getSession();
	UserDTO currentUserDto = null;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // dokumaranUser may be null if the user was force completed.
	    user = getSpecifiedUser(toolSessionId, WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    currentUserDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    user = dokumaranService.getUserByIDAndSession(new Long(currentUserDto.getUserID().intValue()),
		    toolSessionId);
	    if (user == null) {
		user = new DokumaranUser(currentUserDto, session);
		dokumaranService.saveUser(user);
		isFirstTimeAccess = true;
	    }
	}

	// support for leader select feature
	List<DokumaranUser> leaders = dokumaran.isUseSelectLeaderToolOuput()
		? dokumaranService.checkLeaderSelectToolForSessionLeader(user, new Long(toolSessionId).longValue(),
			isFirstTimeAccess)
		: new ArrayList<>();
	// forwards to the leaderSelection page
	if (dokumaran.isUseSelectLeaderToolOuput() && leaders.isEmpty() && !mode.isTeacher()) {
	    // get group users and store it to request as DTO objects
	    List<DokumaranUser> groupUsers = dokumaranService.getUsersBySession(toolSessionId);
	    List<User> groupUserDtos = new ArrayList<>();
	    for (DokumaranUser groupUser : groupUsers) {
		User groupUserDto = new User();
		groupUserDto.setFirstName(groupUser.getFirstName());
		groupUserDto.setLastName(groupUser.getLastName());
		groupUserDtos.add(groupUserDto);
	    }
	    request.setAttribute(DokumaranConstants.ATTR_GROUP_USERS, groupUserDtos);
	    request.setAttribute(DokumaranConstants.ATTR_DOKUMARAN, dokumaran);
	    return "pages/learning/waitforleader";
	}
	//time limit is set but hasn't yet launched by a teacher - show waitForTimeLimitLaunch page
	if (dokumaran.getTimeLimit() > 0 && dokumaran.getTimeLimitLaunchedDate() == null) {
	    return "pages/learning/waitForTimeLimitLaunch";
	}

	boolean isUserLeader = (user != null) && dokumaranService.isUserLeader(leaders, user.getUserId());
	boolean hasEditRight = !dokumaran.isUseSelectLeaderToolOuput()
		|| dokumaran.isUseSelectLeaderToolOuput() && isUserLeader;
	sessionMap.put(DokumaranConstants.ATTR_HAS_EDIT_RIGHT, hasEditRight);
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	sessionMap.put(DokumaranConstants.ATTR_TOOL_CONTENT_ID, dokumaran.getContentId());
	sessionMap.put(DokumaranConstants.ATTR_REFLECTION_ON, dokumaran.isReflectOnActivity());
	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, dokumaranService.isLastActivity(toolSessionId));
	sessionMap.put(DokumaranConstants.ATTR_DOKUMARAN, dokumaran);

	// get the API key from the config table and add it to the session
	String etherpadServerUrl = Configuration.get(ConfigurationKeys.ETHERPAD_SERVER_URL);
	String etherpadApiKey = Configuration.get(ConfigurationKeys.ETHERPAD_API_KEY);
	if (StringUtils.isBlank(etherpadServerUrl) || StringUtils.isBlank(etherpadApiKey)) {
	    return "pages/learning/notconfigured";
	}
	request.setAttribute(DokumaranConstants.KEY_ETHERPAD_SERVER_URL, etherpadServerUrl);

	if (dokumaran.isGalleryWalkStarted() && !dokumaran.isGalleryWalkFinished()) {
	    List<SessionDTO> groupList = dokumaranService.getSummary(dokumaran.getContentId());
	    request.setAttribute(DokumaranConstants.ATTR_SUMMARY_LIST, groupList);
	    if (currentUserDto == null) {
		currentUserDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    }
	    Cookie cookie = dokumaranService.createEtherpadCookieForMonitor(currentUserDto, dokumaran.getContentId());
	    response.addCookie(cookie);
	    return "pages/learning/galleryWalk";
	}

	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	// check whether finish lock is on/off
	boolean finishedLock = dokumaran.getLockWhenFinished() && (user != null) && user.isSessionFinished();
	sessionMap.put(DokumaranConstants.ATTR_TITLE, dokumaran.getTitle());
	sessionMap.put(DokumaranConstants.ATTR_INSTRUCTIONS, dokumaran.getInstructions());
	sessionMap.put(DokumaranConstants.ATTR_FINISH_LOCK, finishedLock);
	sessionMap.put(DokumaranConstants.ATTR_LOCK_ON_FINISH, dokumaran.getLockWhenFinished());
	sessionMap.put(DokumaranConstants.ATTR_USER_FINISHED, (user != null) && user.isSessionFinished());

	sessionMap.put(DokumaranConstants.ATTR_IS_LEADER_RESPONSE_FINALIZED,
		dokumaranService.isLeaderResponseFinalized(leaders));

	// reflection information
	String entryText = new String();
	if (user != null) {
	    NotebookEntry notebookEntry = dokumaranService.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    DokumaranConstants.TOOL_SIGNATURE, user.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}
	sessionMap.put(DokumaranConstants.ATTR_REFLECTION_INSTRUCTION, dokumaran.getReflectInstructions());
	sessionMap.put(DokumaranConstants.ATTR_REFLECTION_ENTRY, entryText);

	// add define later support
	if (dokumaran.isDefineLater()) {
	    return "pages/learning/definelater";
	}

	// set contentInUse flag to true!
	dokumaran.setContentInUse(true);
	dokumaran.setDefineLater(false);
	dokumaranService.saveOrUpdateDokumaran(dokumaran);

	//time limit
	boolean isTimeLimitEnabled = hasEditRight && !finishedLock && dokumaran.getTimeLimit() != 0;
	long secondsLeft = isTimeLimitEnabled ? dokumaranService.getSecondsLeft(dokumaran) : 0;
	request.setAttribute(DokumaranConstants.ATTR_SECONDS_LEFT, secondsLeft);

	boolean isTimeLimitExceeded = dokumaranService.checkTimeLimitExceeded(dokumaran);

	String padId = session.getPadId();
	//in case of non-leader or finished lock or isTimeLimitExceeded - show Etherpad in readonly mode
	if (dokumaran.isUseSelectLeaderToolOuput() && !isUserLeader || finishedLock || isTimeLimitExceeded) {
	    padId = session.getEtherpadReadOnlyId();
	    //in case Etherpad didn't have enough time to initialize - show notconfigured.jsp
	    if (padId == null) {
		return "pages/learning/notconfigured";
	    }
	}

	request.setAttribute(DokumaranConstants.ATTR_PAD_ID, padId);

	//add new sessionID cookie in order to access pad
	if (user != null) {
	    Cookie etherpadSessionCookie = dokumaranService.createEtherpadCookieForLearner(user, session);
	    response.addCookie(etherpadSessionCookie);
	}

	return "pages/learning/learning";
    }

    /**
     * Checks Leader Progress
     */
    @RequestMapping("/checkLeaderProgress")
    private String checkLeaderProgress(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	boolean isLeaderResponseFinalized = dokumaranService.isLeaderResponseFinalized(toolSessionId);

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put(DokumaranConstants.ATTR_IS_LEADER_RESPONSE_FINALIZED, isLeaderResponseFinalized);
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
	String sessionMapID = request.getParameter(DokumaranConstants.ATTR_SESSION_MAP_ID);
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

	    nextActivityUrl = dokumaranService.finishToolSession(sessionId, userID);
	    request.setAttribute(DokumaranConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (DokumaranApplicationException e) {
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
	String sessionMapID = WebUtil.readStrParam(request, DokumaranConstants.ATTR_SESSION_MAP_ID);
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	reflectionForm.setUserID(user.getUserID());
	reflectionForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = dokumaranService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		DokumaranConstants.TOOL_SIGNATURE, user.getUserID());

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

	String sessionMapID = WebUtil.readStrParam(request, DokumaranConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	NotebookEntry entry = dokumaranService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		DokumaranConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    dokumaranService.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    DokumaranConstants.TOOL_SIGNATURE, userId, reflectionForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(reflectionForm.getEntryText());
	    entry.setLastModified(new Date());
	    dokumaranService.updateEntry(entry);
	}

	return finish(request);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************

    private DokumaranUser getSpecifiedUser(Long sessionId, Integer userId) {
	DokumaranUser dokumaranUser = dokumaranService.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (dokumaranUser == null) {
	    LearningController.log.error(
		    "Unable to find specified user for dokumaran activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return dokumaranUser;
    }
}
