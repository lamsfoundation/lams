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

package org.lamsfoundation.lams.tool.spreadsheet.web.controller;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetSession;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.model.UserModifiedSpreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.service.ISpreadsheetService;
import org.lamsfoundation.lams.tool.spreadsheet.service.SpreadsheetApplicationException;
import org.lamsfoundation.lams.tool.spreadsheet.web.form.ReflectionForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;

/**
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/learning")
public class LearningController {

    private static Logger log = Logger.getLogger(LearningController.class);

    @Autowired
    @Qualifier("spreadsheetService")
    private ISpreadsheetService service;

    /**
     * Read spreadsheet data from database and put them into HttpSession. It will redirect to init.do directly after
     * this method run successfully.
     *
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     */
    @RequestMapping("/start")
    public String start(@ModelAttribute ReflectionForm reflectionForm, HttpServletRequest request) {

	//initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	//save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(SpreadsheetConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(SpreadsheetConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);

	//get back the spreadsheet and item list and display them on page
	SpreadsheetUser spreadsheetUser = null;
	if (mode != null && mode.isTeacher()) {
	    // monitoring mode - user is specified in URL
	    // spreadsheetUser may be null if the user was force completed.
	    spreadsheetUser = getSpecifiedUser(service, sessionId,
		    WebUtil.readIntParam(request, AttributeNames.PARAM_USER_ID, false));
	} else {
	    spreadsheetUser = getCurrentUser(service, sessionId);
	}

	Spreadsheet spreadsheet;
	spreadsheet = service.getSpreadsheetBySessionId(sessionId);

	//check whehter finish lock is on/off
	boolean lock = spreadsheet.getLockWhenFinished() && spreadsheetUser != null
		&& spreadsheetUser.isSessionFinished();

	// get notebook entry
	String entryText = new String();
	if (spreadsheetUser != null) {
	    NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    SpreadsheetConstants.TOOL_SIGNATURE, spreadsheetUser.getUserId().intValue());
	    if (notebookEntry != null) {
		entryText = notebookEntry.getEntry();
	    }
	}

	//basic information
	sessionMap.put(SpreadsheetConstants.ATTR_TITLE, spreadsheet.getTitle());
	sessionMap.put(SpreadsheetConstants.ATTR_RESOURCE_INSTRUCTION, spreadsheet.getInstructions());
	sessionMap.put(SpreadsheetConstants.ATTR_FINISH_LOCK, lock);
	sessionMap.put(SpreadsheetConstants.ATTR_LOCK_ON_FINISH, spreadsheet.getLockWhenFinished());
	sessionMap.put(SpreadsheetConstants.ATTR_USER_FINISHED,
		spreadsheetUser != null && spreadsheetUser.isSessionFinished());

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(AttributeNames.ATTR_MODE, mode);
	//reflection information
	sessionMap.put(SpreadsheetConstants.ATTR_REFLECTION_ON, spreadsheet.isReflectOnActivity());
	sessionMap.put(SpreadsheetConstants.ATTR_REFLECTION_INSTRUCTION, spreadsheet.getReflectInstructions());
	sessionMap.put(SpreadsheetConstants.ATTR_REFLECTION_ENTRY, entryText);

	//add define later support
	if (spreadsheet.isDefineLater()) {
	    return "pages/learning/definelater";
	}

	//set contentInUse flag to true
	if (!spreadsheet.isContentInUse()) {
	    spreadsheet.setContentInUse(true);
	    service.saveOrUpdateSpreadsheet(spreadsheet);
	}

	String code;
	if (spreadsheet.isLearnerAllowedToSave() && (spreadsheetUser.getUserModifiedSpreadsheet() != null)) {
	    code = spreadsheetUser.getUserModifiedSpreadsheet().getUserModifiedSpreadsheet();
	} else {
	    code = spreadsheet.getCode();
	}
	sessionMap.put(SpreadsheetConstants.ATTR_CODE, HtmlUtils.htmlEscape(code));
	sessionMap.put(SpreadsheetConstants.ATTR_RESOURCE, spreadsheet);

	if ((spreadsheetUser != null) && (spreadsheetUser.getUserModifiedSpreadsheet() != null)
		&& (spreadsheetUser.getUserModifiedSpreadsheet().getMark() != null)) {
	    request.setAttribute(SpreadsheetConstants.ATTR_USER_IS_MARKED, true);
	    if ((spreadsheetUser.getUserModifiedSpreadsheet().getMark().getDateMarksReleased() != null)) {
		request.setAttribute(SpreadsheetConstants.ATTR_USER_MARK,
			spreadsheetUser.getUserModifiedSpreadsheet().getMark());
	    }
	} else {
	    request.setAttribute(SpreadsheetConstants.ATTR_USER_IS_MARKED, false);
	}

	sessionMap.put(AttributeNames.ATTR_IS_LAST_ACTIVITY, service.isLastActivity(sessionId));

	return "pages/learning/learning";
    }

    /**
     * Finish learning session.
     */
    @RequestMapping("/saveUserSpreadsheet")
    public String saveUserSpreadsheet(@ModelAttribute ReflectionForm reflectionForm, HttpServletRequest request) {

	//get back SessionMap
	String sessionMapID = request.getParameter(SpreadsheetConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	//get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	boolean userFinished = (Boolean) sessionMap.get(SpreadsheetConstants.ATTR_USER_FINISHED);

	//save learner changes in spreadsheet if such option is activated in spreadsheet
	Spreadsheet spreadsheet = (Spreadsheet) sessionMap.get(SpreadsheetConstants.ATTR_RESOURCE);
	Spreadsheet spreadsheetPO = service.getSpreadsheetByContentId(spreadsheet.getContentId());
	if (spreadsheetPO.isLearnerAllowedToSave() && !mode.isTeacher()
		&& !(spreadsheet.getLockWhenFinished() && userFinished)) {

	    SpreadsheetUser spreadsheetUser = getCurrentUser(service, sessionId);
	    UserModifiedSpreadsheet userModifiedSpreadsheet = spreadsheetUser.getUserModifiedSpreadsheet();

	    if (spreadsheetUser.getUserModifiedSpreadsheet() == null
		    || spreadsheetUser.getUserModifiedSpreadsheet().getMark() == null) {
		if (userModifiedSpreadsheet == null) {
		    userModifiedSpreadsheet = new UserModifiedSpreadsheet();
		}
		String code = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_CODE);
		userModifiedSpreadsheet.setUserModifiedSpreadsheet(code);
		spreadsheetUser.setUserModifiedSpreadsheet(userModifiedSpreadsheet);
		service.saveOrUpdateUser(spreadsheetUser);
	    }
	}

	String typeOfAction = WebUtil.readStrParam(request, "typeOfAction");
	String conf;
	if ("finishSession".equals(typeOfAction)) {
	    conf = "finishSession.do";
	} else if ("continueReflect".equals(typeOfAction)) {
	    conf = "newReflection.do";
	} else {
	    conf = "start.do";
	}

	String redirect = "redirect:/learning/" + conf;
	redirect = WebUtil.appendParameterToURL(redirect, SpreadsheetConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	redirect = WebUtil.appendParameterToURL(redirect, AttributeNames.PARAM_TOOL_SESSION_ID, sessionId.toString());
	redirect = WebUtil.appendParameterToURL(redirect, AttributeNames.ATTR_MODE, mode.toString());
	return redirect;
    }

    /**
     * Finish learning session.
     */
    @RequestMapping("/finishSession")
    public void finishSession(HttpServletRequest request, HttpServletResponse response) throws IOException, SpreadsheetApplicationException {

	//get back SessionMap
	String sessionMapID = request.getParameter(SpreadsheetConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);

	//get mode and ToolSessionID from sessionMAP
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// get sessionId from HttpServletRequest
	HttpSession ss = SessionManager.getSession();
	UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	Long userID = new Long(userDTO.getUserID().longValue());

	String nextActivityUrl = service.finishToolSession(sessionId, userID);
	response.sendRedirect(nextActivityUrl);
    }

    /**
     * Display empty reflection form.
     */
    @RequestMapping("/newReflection")
    public String newReflection(@ModelAttribute ReflectionForm reflectionForm, HttpServletRequest request) {

	//get session value
	String sessionMapID = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_SESSION_MAP_ID);

	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	reflectionForm.setUserID(user.getUserID());
	reflectionForm.setSessionMapID(sessionMapID);

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long toolSessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = service.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		SpreadsheetConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    reflectionForm.setEntryText(entry.getEntry());
	}

	return "pages/learning/notebook";
    }

    /**
     * Submit reflection form input database.
     */
    @RequestMapping(path = "/submitReflection", method = RequestMethod.POST)
    public void submitReflection(@ModelAttribute ReflectionForm reflectionForm, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, SpreadsheetApplicationException {

	Integer userId = reflectionForm.getUserID();

	String sessionMapID = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		SpreadsheetConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    SpreadsheetConstants.TOOL_SIGNATURE, userId, reflectionForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(reflectionForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	finishSession(request, response);
    }

    //*************************************************************************************
    // Private methods
    //*************************************************************************************

    private SpreadsheetUser getCurrentUser(ISpreadsheetService service, Long sessionId) {
	//try to get form system session
	HttpSession ss = SessionManager.getSession();
	//get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	SpreadsheetUser spreadsheetUser = service.getUserByIDAndSession(user.getUserID().longValue(), sessionId);

	if (spreadsheetUser == null) {
	    SpreadsheetSession session = service.getSessionBySessionId(sessionId);
	    spreadsheetUser = new SpreadsheetUser(user, session);
	    service.saveOrUpdateUser(spreadsheetUser);
	}
	return spreadsheetUser;
    }

    private SpreadsheetUser getSpecifiedUser(ISpreadsheetService service, Long sessionId, Integer userId) {
	SpreadsheetUser spreadsheetUser = service.getUserByIDAndSession(userId.longValue(), sessionId);
	if (spreadsheetUser == null) {
	    log.error("Unable to find specified user for spreadsheet activity. Screens are likely to fail. SessionId="
		    + sessionId + " UserId=" + userId);
	}
	return spreadsheetUser;
    }
}
