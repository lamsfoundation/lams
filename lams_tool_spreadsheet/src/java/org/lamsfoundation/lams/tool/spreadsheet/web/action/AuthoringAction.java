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
package org.lamsfoundation.lams.tool.spreadsheet.web.action;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.service.ISpreadsheetService;
import org.lamsfoundation.lams.tool.spreadsheet.service.UploadSpreadsheetFileException;
import org.lamsfoundation.lams.tool.spreadsheet.web.form.SpreadsheetForm;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.FileValidatorUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 */
public class AuthoringAction extends Action {

    private static Logger log = Logger.getLogger(AuthoringAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	String param = mapping.getParameter();
	// -----------------------Spreadsheet Author function ---------------------------
	if (param.equals("start")) {
	    ToolAccessMode mode = getAccessMode(request);
	    // teacher mode "check for new" button enter.
	    if (mode != null)
		request.setAttribute(AttributeNames.ATTR_MODE, mode.toString());
	    else
		request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.AUTHOR.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("definelater")) {
	    // update define later flag to true
	    Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	    ISpreadsheetService service = getSpreadsheetService();
	    Spreadsheet spreadsheet = service.getSpreadsheetByContentId(contentId);

	    spreadsheet.setDefineLater(true);
	    service.saveOrUpdateSpreadsheet(spreadsheet);

	    request.setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER.toString());
	    return start(mapping, form, request, response);
	}
	if (param.equals("initPage")) {
	    return initPage(mapping, form, request, response);
	}

	if (param.equals("updateContent")) {
	    return updateContent(mapping, form, request, response);
	}

	return mapping.findForward(SpreadsheetConstants.ERROR);
    }

    /**
     * Read spreadsheet data from database and put them into HttpSession. It will redirect to init.do directly after
     * this method run successfully.
     * 
     * This method will avoid read database again and lost un-saved resouce item lost when user "refresh page",
     * 
     * @throws ServletException
     * 
     */
    private ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {

	// save toolContentID into HTTPSession
	Long contentId = new Long(WebUtil.readLongParam(request, SpreadsheetConstants.PARAM_TOOL_CONTENT_ID));

	// get back the spreadsheet and item list and display them on page
	ISpreadsheetService service = getSpreadsheetService();

	Spreadsheet spreadsheet = null;
	SpreadsheetForm spreadsheetForm = (SpreadsheetForm) form;

	// Get contentFolderID and save to form.
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	spreadsheetForm.setContentFolderID(contentFolderID);

	// initial Session Map
	SessionMap sessionMap = new SessionMap();
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
	return mapping.findForward(SpreadsheetConstants.SUCCESS);
    }

    /**
     * Display same entire authoring page content from HttpSession variable.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward initPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws ServletException {
	String sessionMapID = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	SpreadsheetForm existForm = (SpreadsheetForm) sessionMap.get(SpreadsheetConstants.ATTR_RESOURCE_FORM);

	SpreadsheetForm spreadsheetForm = (SpreadsheetForm) form;
	try {
	    PropertyUtils.copyProperties(spreadsheetForm, existForm);
	} catch (Exception e) {
	    throw new ServletException(e);
	}

	ToolAccessMode mode = getAccessMode(request);
	if (mode.isAuthor())
	    return mapping.findForward(SpreadsheetConstants.SUCCESS);
	else
	    return mapping.findForward(SpreadsheetConstants.DEFINE_LATER);
    }

    /**
     * This method will persist all inforamtion in this authoring page, include all spreadsheet item, information etc.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    private ActionForward updateContent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	SpreadsheetForm spreadsheetForm = (SpreadsheetForm) (form);

	// get back sessionMAP
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(spreadsheetForm.getSessionMapID());

	ToolAccessMode mode = getAccessMode(request);

	ActionMessages errors = validate(spreadsheetForm, mapping, request);
	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    if (mode.isAuthor())
		return mapping.findForward("author");
	    else
		return mapping.findForward("monitor");
	}

	Spreadsheet spreadsheet = spreadsheetForm.getSpreadsheet();
	ISpreadsheetService service = getSpreadsheetService();

	// **********************************Get Spreadsheet PO*********************
	Spreadsheet spreadsheetPO = service.getSpreadsheetByContentId(spreadsheetForm.getSpreadsheet().getContentId());
	if (spreadsheetPO == null) {
	    // new Spreadsheet, create it.
	    spreadsheetPO = spreadsheet;
	    spreadsheetPO.setCreated(new Timestamp(new Date().getTime()));
	    spreadsheetPO.setUpdated(new Timestamp(new Date().getTime()));
	} else {
	    if (mode.isAuthor()) {
		Long uid = spreadsheetPO.getUid();
		PropertyUtils.copyProperties(spreadsheetPO, spreadsheet);
		// get back UID
		spreadsheetPO.setUid(uid);
	    } else { // if it is Teacher, then just update basic tab content (definelater)
		spreadsheetPO.setInstructions(spreadsheet.getInstructions());
		spreadsheetPO.setCode(spreadsheet.getCode());
		spreadsheetPO.setTitle(spreadsheet.getTitle());
		// change define later status
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

	request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG, Boolean.TRUE);
	if (mode.isAuthor())
	    return mapping.findForward("author");
	else
	    return mapping.findForward("monitor");
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    /**
     * Return SpreadsheetService bean.
     */
    private ISpreadsheetService getSpreadsheetService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (ISpreadsheetService) wac.getBean(SpreadsheetConstants.RESOURCE_SERVICE);
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
	if (StringUtils.equalsIgnoreCase(modeStr, ToolAccessMode.TEACHER.toString()))
	    mode = ToolAccessMode.TEACHER;
	else
	    mode = ToolAccessMode.AUTHOR;
	return mode;
    }

    private ActionMessages validate(SpreadsheetForm spreadsheetForm, ActionMapping mapping, HttpServletRequest request) {
	ActionMessages errors = new ActionMessages();
	// if (StringUtils.isBlank(spreadsheetForm.getSpreadsheet().getTitle())) {
	// ActionMessage error = new ActionMessage("error.resource.item.title.blank");
	// errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	// }

	// define it later mode(TEACHER) skip below validation.
	String modeStr = request.getParameter(AttributeNames.ATTR_MODE);
	if (StringUtils.equals(modeStr, ToolAccessMode.TEACHER.toString())) {
	    return errors;
	}

	// Some other validation outside basic Tab.

	return errors;
    }

}
