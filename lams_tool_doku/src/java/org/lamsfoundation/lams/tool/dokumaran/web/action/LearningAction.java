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

package org.lamsfoundation.lams.tool.dokumaran.web.action;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.dokumaran.DokumaranConstants;
import org.lamsfoundation.lams.tool.dokumaran.model.Dokumaran;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranConfigItem;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranSession;
import org.lamsfoundation.lams.tool.dokumaran.model.DokumaranUser;
import org.lamsfoundation.lams.tool.dokumaran.service.DokumaranApplicationException;
import org.lamsfoundation.lams.tool.dokumaran.service.DokumaranConfigurationException;
import org.lamsfoundation.lams.tool.dokumaran.service.DokumaranService;
import org.lamsfoundation.lams.tool.dokumaran.service.IDokumaranService;
import org.lamsfoundation.lams.tool.dokumaran.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.HashUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Steve.Ni
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    private static IDokumaranService dokumaranService;

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException, DokumaranConfigurationException, URISyntaxException {

	String param = mapping.getParameter();
	// -----------------------Dokumaran Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("checkLeaderProgress")) {
	    return checkLeaderProgress(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}

	// ================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(DokumaranConstants.ERROR);
    }

    /**
     * Read dokumaran data from database and put them into HttpSession. It will redirect to init.do directly after this
     * method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     * @throws DokumaranConfigurationException 
     * @throws URISyntaxException 
     *
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws DokumaranConfigurationException, URISyntaxException {

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(DokumaranConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);
	Long toolSessionId = new Long(request.getParameter(DokumaranConstants.PARAM_TOOL_SESSION_ID));
	IDokumaranService service = getDokumaranService();
	Dokumaran dokumaran = service.getDokumaranBySessionId(toolSessionId);
	DokumaranSession session = service.getDokumaranSessionBySessionId(toolSessionId);

	// get back the dokumaran and item list and display them on page
	DokumaranUser user = null;
	if ((mode != null) && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // dokumaranUser may be null if the user was force completed.
	    user = getSpecifiedUser(service, toolSessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    user = getCurrentUser(service, toolSessionId);
	}

	// support for leader select feature
	DokumaranUser groupLeader = dokumaran.isUseSelectLeaderToolOuput()
		? service.checkLeaderSelectToolForSessionLeader(user, new Long(toolSessionId).longValue())
		: null;
		// forwards to the leaderSelection page
	if (dokumaran.isUseSelectLeaderToolOuput() && (groupLeader == null) && !mode.isTeacher()) {
	
	    // get group users and store it to request as DTO objects
	    List<DokumaranUser> groupUsers = service.getUsersBySession(toolSessionId);
	    List<User> groupUserDtos = new ArrayList<User>();
	    for (DokumaranUser groupUser : groupUsers) {
		User groupUserDto = new User();
		groupUserDto.setFirstName(groupUser.getFirstName());
		groupUserDto.setLastName(groupUser.getLastName());
		groupUserDtos.add(groupUserDto);
	    }
	    request.setAttribute(DokumaranConstants.ATTR_GROUP_USERS, groupUserDtos);
	    request.setAttribute(DokumaranConstants.ATTR_DOKUMARAN, dokumaran);
	    return mapping.findForward("waitforleader");
	}
	sessionMap.put(DokumaranConstants.ATTR_GROUP_LEADER, groupLeader);
	boolean isUserLeader = session.isUserGroupLeader(user.getUid());

	// check whether finish lock is on/off
	boolean finishedLock = dokumaran.getLockWhenFinished() && (user != null) && user.isSessionFinished();
	boolean hasEditRight = !dokumaran.isUseSelectLeaderToolOuput()
		|| dokumaran.isUseSelectLeaderToolOuput() && isUserLeader;

	// basic information
	sessionMap.put(DokumaranConstants.ATTR_TITLE, dokumaran.getTitle());
	sessionMap.put(DokumaranConstants.ATTR_INSTRUCTIONS, dokumaran.getInstructions());
	sessionMap.put(DokumaranConstants.ATTR_FINISH_LOCK, finishedLock);
	sessionMap.put(DokumaranConstants.ATTR_LOCK_ON_FINISH, dokumaran.getLockWhenFinished());
	sessionMap.put(DokumaranConstants.ATTR_USER_FINISHED,
		(user != null) && user.isSessionFinished());
	sessionMap.put(DokumaranConstants.ATTR_HAS_EDIT_RIGHT, hasEditRight);
	sessionMap.put(DokumaranConstants.ATTR_IS_LEADER_RESPONSE_FINALIZED, groupLeader != null && groupLeader.isSessionFinished());
	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);

	// reflection information
	String entryText = new String();
	if (user != null) {
	    NotebookEntry notebookEntry = service.getEntry(toolSessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    DokumaranConstants.TOOL_SIGNATURE, user.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}
	sessionMap.put(DokumaranConstants.ATTR_REFLECTION_ON, dokumaran.isReflectOnActivity());
	sessionMap.put(DokumaranConstants.ATTR_REFLECTION_INSTRUCTION, dokumaran.getReflectInstructions());
	sessionMap.put(DokumaranConstants.ATTR_REFLECTION_ENTRY, entryText);

	// add define later support
	if (dokumaran.isDefineLater()) {
	    return mapping.findForward("defineLater");
	}

	// set contentInUse flag to true!
	dokumaran.setContentInUse(true);
	dokumaran.setDefineLater(false);
	service.saveOrUpdateDokumaran(dokumaran);

	ActivityPositionDTO activityPosition = LearningWebUtil
		.putActivityPositionInRequestByToolSessionId(toolSessionId, request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	sessionMap.put(DokumaranConstants.ATTR_DOKUMARAN, dokumaran);
	
	// get the API key from the config table and add it to the session
	DokumaranConfigItem etherpadServerUrlConfig = dokumaranService.getConfigItem(DokumaranConfigItem.KEY_ETHERPAD_URL);
	DokumaranConfigItem apiKeyConfig = dokumaranService.getConfigItem(DokumaranConfigItem.KEY_API_KEY);
	if (apiKeyConfig == null || apiKeyConfig.getConfigValue() == null || etherpadServerUrlConfig == null || etherpadServerUrlConfig.getConfigValue() == null) {
	    return mapping.findForward("notconfigured");
	}
	String etherpadServerUrl = etherpadServerUrlConfig.getConfigValue();
	request.setAttribute(DokumaranConstants.KEY_ETHERPAD_SERVER_URL, etherpadServerUrl);
	
	String padId = session.getPadId();
	//in case of non-leader or finished lock - show Etherpad in readonly mode
	if (dokumaran.isUseSelectLeaderToolOuput() && !isUserLeader || finishedLock) {
	    padId = session.getEtherpadReadOnlyId();
	    //in case Etherpad didn't have enough time to initialize - show notconfigured.jsp
	    if (padId == null) {
		return mapping.findForward("notconfigured");
	    }
	}
	request.setAttribute(DokumaranConstants.ATTR_PAD_ID, padId);
	
	//add new sessionID cookie in order to access pad
	Cookie etherpadSessionCookie = service.createEtherpadCookieForLearner(user, session);
	response.addCookie(etherpadSessionCookie);

	return mapping.findForward(DokumaranConstants.SUCCESS);
    }
    
    /**
     * Checks Leader Progress
     */
    private ActionForward checkLeaderProgress(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {

	IDokumaranService service = getDokumaranService();
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	DokumaranSession session = service.getDokumaranSessionBySessionId(toolSessionId);
	DokumaranUser leader = session.getGroupLeader();

	boolean isLeaderResponseFinalized = leader.isSessionFinished();

	JSONObject JSONObject = new JSONObject();
	JSONObject.put(DokumaranConstants.ATTR_IS_LEADER_RESPONSE_FINALIZED, isLeaderResponseFinalized);
	response.setContentType("application/x-json;charset=utf-8");
	response.getWriter().print(JSONObject);
	return null;
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
    private ActionForward finish(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get back SessionMap
	String sessionMapID = request.getParameter(DokumaranConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	// get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	IDokumaranService service = getDokumaranService();
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(user.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(DokumaranConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (DokumaranApplicationException e) {
	    LearningAction.log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(DokumaranConstants.SUCCESS);
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
    private ActionForward newReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	// get session value
	String sessionMapID = WebUtil.readStrParam(request, DokumaranConstants.ATTR_SESSION_MAP_ID);
	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

	// get the existing reflection entry
	IDokumaranService submitFilesService = getDokumaranService();

	SessionMap<String, Object> map = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) map.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		DokumaranConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(DokumaranConstants.SUCCESS);
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
    private ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	ReflectionForm refForm = (ReflectionForm) form;
	Integer userId = refForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, DokumaranConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	IDokumaranService service = getDokumaranService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		DokumaranConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    DokumaranConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finish(mapping, form, request, response);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************

    private IDokumaranService getDokumaranService() {
	if (LearningAction.dokumaranService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    LearningAction.dokumaranService = (IDokumaranService) wac.getBean(DokumaranConstants.RESOURCE_SERVICE);
	}
	return LearningAction.dokumaranService;
    }

    private DokumaranUser getCurrentUser(IDokumaranService service, Long sessionId) {
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	DokumaranUser dokumaranUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()), sessionId);

	if (dokumaranUser == null) {
	    DokumaranSession session = service.getDokumaranSessionBySessionId(sessionId);
	    dokumaranUser = new DokumaranUser(user, session);
	    service.createUser(dokumaranUser);
	}
	return dokumaranUser;
    }

    private DokumaranUser getSpecifiedUser(IDokumaranService service, Long sessionId, Integer userId) {
	DokumaranUser dokumaranUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (dokumaranUser == null) {
	    LearningAction.log
		    .error("Unable to find specified user for dokumaran activity. Screens are likely to fail. SessionId="
			    + sessionId + " UserId=" + userId);
	}
	return dokumaranUser;
    }
}
