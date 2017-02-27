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


package org.lamsfoundation.lams.tool.spreadsheet.web.action;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.config.ForwardConfig;
import org.lamsfoundation.lams.learning.web.bean.ActivityPositionDTO;
import org.lamsfoundation.lams.learning.web.util.LearningWebUtil;
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
import org.lamsfoundation.lams.tool.spreadsheet.web.form.SpreadsheetForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Andrey Balan
 */
public class LearningAction extends Action {

    private static Logger log = Logger.getLogger(LearningAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {

	String param = mapping.getParameter();
	//-----------------------Spreadsheet Learner function ---------------------------
	if (param.equals("start")) {
	    return start(mapping, form, request, response);
	}
	if (param.equals("saveUserSpreadsheet")) {
	    return saveUserSpreadsheet(mapping, form, request, response);
	}
	if (param.equals("finish")) {
	    return finish(mapping, form, request, response);
	}

	//================ Reflection =======================
	if (param.equals("newReflection")) {
	    return newReflection(mapping, form, request, response);
	}
	if (param.equals("submitReflection")) {
	    return submitReflection(mapping, form, request, response);
	}

	return mapping.findForward(SpreadsheetConstants.ERROR);
    }

    /**
     * Read spreadsheet data from database and put them into HttpSession. It will redirect to init.do directly after
     * this method run successfully.
     * 
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	//initial Session Map 
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	//save toolContentID into HTTPSession
	ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, AttributeNames.PARAM_MODE, true);

	Long sessionId = new Long(request.getParameter(SpreadsheetConstants.PARAM_TOOL_SESSION_ID));

	request.setAttribute(SpreadsheetConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	request.setAttribute(AttributeNames.ATTR_MODE, mode);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);

	//get back the spreadsheet and item list and display them on page
	ISpreadsheetService service = getSpreadsheetService();
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
	    return mapping.findForward(SpreadsheetConstants.DEFINE_LATER);
	}

	//set contentInUse flag to true
	if (!spreadsheet.isContentInUse()) {
	    spreadsheet.setContentInUse(true);
	    service.saveOrUpdateSpreadsheet(spreadsheet);
	}
	
	String code;
	if (spreadsheet.isLearnerAllowedToSave() && !mode.isTeacher()
		&& (spreadsheetUser.getUserModifiedSpreadsheet() != null)) {
	    code = spreadsheetUser.getUserModifiedSpreadsheet().getUserModifiedSpreadsheet();
	} else {
	    code = spreadsheet.getCode();
	}
	sessionMap.put(SpreadsheetConstants.ATTR_CODE, StringEscapeUtils.escapeHtml(code));
	sessionMap.put(SpreadsheetConstants.ATTR_RESOURCE, spreadsheet);

	if ((spreadsheetUser != null) && (spreadsheetUser.getUserModifiedSpreadsheet() != null)
		&& (spreadsheetUser.getUserModifiedSpreadsheet().getMark() != null)
		&& (spreadsheetUser.getUserModifiedSpreadsheet().getMark().getDateMarksReleased() != null)) {
	    request.setAttribute(SpreadsheetConstants.ATTR_USER_MARK,
		    spreadsheetUser.getUserModifiedSpreadsheet().getMark());
	}

	ActivityPositionDTO activityPosition = LearningWebUtil.putActivityPositionInRequestByToolSessionId(sessionId,
		request, getServlet().getServletContext());
	sessionMap.put(AttributeNames.ATTR_ACTIVITY_POSITION, activityPosition);

	return mapping.findForward(SpreadsheetConstants.SUCCESS);
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
    private ActionForward saveUserSpreadsheet(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	//get back SessionMap
	String sessionMapID = request.getParameter(SpreadsheetConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	//get mode and ToolSessionID from sessionMAP
	ToolAccessMode mode = (ToolAccessMode) sessionMap.get(AttributeNames.ATTR_MODE);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	boolean userFinished = (Boolean) sessionMap.get(SpreadsheetConstants.ATTR_USER_FINISHED);

	//save learner changes in spreadsheet if such option is activated in spreadsheet
	ISpreadsheetService service = getSpreadsheetService();
	Spreadsheet spreadsheet = (Spreadsheet) sessionMap.get(SpreadsheetConstants.ATTR_RESOURCE);
	Spreadsheet spreadsheetPO = service.getSpreadsheetByContentId(spreadsheet.getContentId());
	if (spreadsheetPO.isLearnerAllowedToSave() && !mode.isTeacher()
		&& !(spreadsheet.getLockWhenFinished() && userFinished)) {

	    SpreadsheetUser spreadsheetUser = getCurrentUser(service, sessionId);
	    UserModifiedSpreadsheet userModifiedSpreadsheet = new UserModifiedSpreadsheet();
	    String code = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_CODE);
	    userModifiedSpreadsheet.setUserModifiedSpreadsheet(code);
	    spreadsheetUser.setUserModifiedSpreadsheet(userModifiedSpreadsheet);
	    service.saveOrUpdateUser(spreadsheetUser);
	}

	String typeOfAction = WebUtil.readStrParam(request, "typeOfAction");
	ForwardConfig conf;
	if ("finishSession".equals(typeOfAction)) {
	    conf = mapping.findForwardConfig("finishSession");
	} else if ("continueReflect".equals(typeOfAction)) {
	    conf = mapping.findForwardConfig("continueReflect");
	} else {
	    conf = mapping.findForwardConfig("start");
	}
	ActionRedirect redirect = new ActionRedirect(conf);
	redirect.addParameter(SpreadsheetConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	redirect.addParameter(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	redirect.addParameter(AttributeNames.ATTR_MODE, mode);
	return redirect;
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

	//get back SessionMap
	String sessionMapID = request.getParameter(SpreadsheetConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);

	//get mode and ToolSessionID from sessionMAP
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	ISpreadsheetService service = getSpreadsheetService();
	// get sessionId from HttpServletRequest
	String nextActivityUrl = null;
	try {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO userDTO = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    Long userID = new Long(userDTO.getUserID().longValue());

	    nextActivityUrl = service.finishToolSession(sessionId, userID);
	    request.setAttribute(SpreadsheetConstants.ATTR_NEXT_ACTIVITY_URL, nextActivityUrl);
	} catch (SpreadsheetApplicationException e) {
	    log.error("Failed get next activity url:" + e.getMessage());
	}

	return mapping.findForward(SpreadsheetConstants.SUCCESS);
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

	//get session value
	String sessionMapID = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_SESSION_MAP_ID);

	ReflectionForm refForm = (ReflectionForm) form;
	HttpSession ss = SessionManager.getSession();
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);

	refForm.setUserID(user.getUserID());
	refForm.setSessionMapID(sessionMapID);

//		 get the existing reflection entry
	ISpreadsheetService submitFilesService = getSpreadsheetService();

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long toolSessionID = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	NotebookEntry entry = submitFilesService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		SpreadsheetConstants.TOOL_SIGNATURE, user.getUserID());

	if (entry != null) {
	    refForm.setEntryText(entry.getEntry());
	}

	return mapping.findForward(SpreadsheetConstants.SUCCESS);
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

	String sessionMapID = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);

	ISpreadsheetService service = getSpreadsheetService();

	// check for existing notebook entry
	NotebookEntry entry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		SpreadsheetConstants.TOOL_SIGNATURE, userId);

	if (entry == null) {
	    // create new entry
	    service.createNotebookEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		    SpreadsheetConstants.TOOL_SIGNATURE, userId, refForm.getEntryText());
	} else {
	    // update existing entry
	    entry.setEntry(refForm.getEntryText());
	    entry.setLastModified(new Date());
	    service.updateEntry(entry);
	}

	return finish(mapping, form, request, response);
    }

    //*************************************************************************************
    // Private methods
    //*************************************************************************************

    private ISpreadsheetService getSpreadsheetService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ISpreadsheetService) wac.getBean(SpreadsheetConstants.RESOURCE_SERVICE);
    }

    private SpreadsheetUser getCurrentUser(ISpreadsheetService service, Long sessionId) {
	//try to get form system session
	HttpSession ss = SessionManager.getSession();
	//get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	SpreadsheetUser spreadsheetUser = service.getUserByIDAndSession(new Long(user.getUserID().intValue()),
		sessionId);

	if (spreadsheetUser == null) {
	    SpreadsheetSession session = service.getSessionBySessionId(sessionId);
	    spreadsheetUser = new SpreadsheetUser(user, session);
	    service.saveOrUpdateUser(spreadsheetUser);
	}
	return spreadsheetUser;
    }

    private SpreadsheetUser getSpecifiedUser(ISpreadsheetService service, Long sessionId, Integer userId) {
	SpreadsheetUser spreadsheetUser = service.getUserByIDAndSession(new Long(userId.intValue()), sessionId);
	if (spreadsheetUser == null) {
	    log.error("Unable to find specified user for spreadsheet activity. Screens are likely to fail. SessionId="
		    + sessionId + " UserId=" + userId);
	}
	return spreadsheetUser;
    }

    /**
     * Get ToolAccessMode from HttpRequest parameters. Default value is AUTHOR mode.
     * 
     * @param request
     * @return
     */
    private ToolAccessMode getAccessMode(HttpServletRequest request) {
	ToolAccessMode mode;
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString())) {
	    mode = ToolAccessMode.TEACHER;
	} else {
	    mode = ToolAccessMode.AUTHOR;
	}
	return mode;
    }
}
