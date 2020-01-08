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

package org.lamsfoundation.lams.tool.spreadsheet.web.controller;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.service.ISpreadsheetService;
import org.lamsfoundation.lams.tool.spreadsheet.web.form.SpreadsheetForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CommonConstants;
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

/**
 */
@Controller
@RequestMapping("/authoring")
public class AuthoringController {

    private static Logger log = Logger.getLogger(AuthoringController.class);

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
    public String start(@ModelAttribute SpreadsheetForm spreadsheetForm, HttpServletRequest request)
	    throws ServletException {
	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	return readDatabaseData(spreadsheetForm, request);
    }

    @RequestMapping(path = "/definelater", method = RequestMethod.POST)
    public String defineLater(@ModelAttribute SpreadsheetForm spreadsheetForm, HttpServletRequest request)
	    throws ServletException {
	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	Spreadsheet spreadsheet = service.getSpreadsheetByContentId(contentId);

	spreadsheet.setDefineLater(true);
	service.saveOrUpdateSpreadsheet(spreadsheet);

	//audit log the teacher has started editing activity in monitor
	service.auditLogStartEditingActivityInMonitor(contentId);
	
	request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	return readDatabaseData(spreadsheetForm, request);
    }
    
    /**
     * Common method for "start" and "defineLater"
     */
    private String readDatabaseData(SpreadsheetForm spreadsheetForm, HttpServletRequest request) throws ServletException {
	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, SpreadsheetConstants.PARAM_TOOL_CONTENT_ID));

	Spreadsheet spreadsheet = null;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	spreadsheetForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	spreadsheetForm.setSessionMapID(sessionMap.getSessionID());

	try {
	    spreadsheet = service.getSpreadsheetByContentId(contentId);
	    // if spreadsheet does not exist, try to use default content instead.
	    if (spreadsheet == null) {
		spreadsheet = service.getDefaultContent(contentId);
	    }
	    spreadsheetForm.setSpreadsheet(spreadsheet);
	} catch (Exception e) {
	    log.error(e);
	    throw new ServletException(e);
	}

	sessionMap.put(SpreadsheetConstants.ATTR_RESOURCE_FORM, spreadsheetForm);
	request.getSession().setAttribute(AttributeNames.PARAM_NOTIFY_CLOSE_URL,
		request.getParameter(AttributeNames.PARAM_NOTIFY_CLOSE_URL));
	return "pages/authoring/start";
    }

    /**
     */
    @RequestMapping("/init")
    public String initPage(@ModelAttribute SpreadsheetForm spreadsheetForm, HttpServletRequest request)
	    throws ServletException {

	String sessionMapID = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapID);
	SpreadsheetForm existForm = (SpreadsheetForm) sessionMap.get(SpreadsheetConstants.ATTR_RESOURCE_FORM);

	try {
	    PropertyUtils.copyProperties(spreadsheetForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	return "pages/authoring/authoring";
    }

    /**
     * This method will persist all information in this authoring page, include all spreadsheet item, information etc.
     */
    @RequestMapping(path = "/updateContent", method = RequestMethod.POST)
    public String updateContent(@ModelAttribute SpreadsheetForm spreadsheetForm, HttpServletRequest request)
	    throws Exception {

	ToolAccessMode mode = WebUtil.readToolAccessModeAuthorDefaulted(request);
	request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());

	Spreadsheet spreadsheet = spreadsheetForm.getSpreadsheet();

	// **********************************Get Spreadsheet PO*********************
	Spreadsheet spreadsheetPO = service.getSpreadsheetByContentId(spreadsheetForm.getSpreadsheet().getContentId());
	if (spreadsheetPO == null) {
	    // new Spreadsheet, create it.
	    spreadsheetPO = spreadsheet;
	    spreadsheetPO.setCreated(new Timestamp(new Date().getTime()));
	    spreadsheetPO.setUpdated(new Timestamp(new Date().getTime()));

	} else {
	    Long uid = spreadsheetPO.getUid();
	    PropertyUtils.copyProperties(spreadsheetPO, spreadsheet);
	    // get back UID
	    spreadsheetPO.setUid(uid);

	    // if it's a teacher - change define later status
	    if (mode.isTeacher()) {
		spreadsheetPO.setDefineLater(false);
	    }

	    spreadsheetPO.setUpdated(new Timestamp(new Date().getTime()));
	}

	// *******************************Handle user*******************
	// try to get form system session
	HttpSession ss = SessionManager.getSession();
	// get back login user DTO
	UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	SpreadsheetUser spreadsheetUser = service.getUserByIDAndContent(new Long(user.getUserID().intValue()),
		spreadsheetForm.getSpreadsheet().getContentId());
	if (spreadsheetUser == null) {
	    spreadsheetUser = new SpreadsheetUser(user, spreadsheetPO);
	}

	spreadsheetPO.setCreatedBy(spreadsheetUser);

	// finally persist spreadsheetPO again
	service.saveOrUpdateSpreadsheet(spreadsheetPO);

	spreadsheetForm.setSpreadsheet(spreadsheetPO);

	request.setAttribute(CommonConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	return "pages/authoring/authoring";
    }

}
