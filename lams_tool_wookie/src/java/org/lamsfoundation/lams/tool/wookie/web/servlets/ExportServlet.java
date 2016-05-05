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

/* $Id$ */

package org.lamsfoundation.lams.tool.wookie.web.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.wookie.dto.WookieDTO;
import org.lamsfoundation.lams.tool.wookie.dto.WookieSessionDTO;
import org.lamsfoundation.lams.tool.wookie.dto.WookieUserDTO;
import org.lamsfoundation.lams.tool.wookie.model.Wookie;
import org.lamsfoundation.lams.tool.wookie.model.WookieSession;
import org.lamsfoundation.lams.tool.wookie.model.WookieUser;
import org.lamsfoundation.lams.tool.wookie.service.IWookieService;
import org.lamsfoundation.lams.tool.wookie.service.WookieServiceProxy;
import org.lamsfoundation.lams.tool.wookie.util.WookieConstants;
import org.lamsfoundation.lams.tool.wookie.util.WookieException;
import org.lamsfoundation.lams.tool.wookie.util.WookieUtil;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

    private static final long serialVersionUID = -2829707715037631881L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "wookie_main.html";

    private IWookieService wookieService;

    @Override
    protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	if (wookieService == null) {
	    wookieService = WookieServiceProxy.getWookieService(getServletContext());
	}

	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		doLearnerExport(request, response, directoryName, cookies);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		doTeacherExport(request, response, directoryName, cookies);
	    }
	} catch (WookieException e) {
	    logger.error("Cannot perform export for wookie tool.");
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
	writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    private void doLearnerExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws WookieException {

	logger.debug("doExportLearner: toolContentID:" + toolSessionID);

	// check if toolContentID available
	if (toolSessionID == null) {
	    String error = "Tool Session ID is missing. Unable to continue";
	    logger.error(error);
	    throw new WookieException(error);
	}

	WookieSession wookieSession = wookieService.getSessionBySessionId(toolSessionID);

	Wookie wookie = wookieSession.getWookie();

	UserDTO lamsUserDTO = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	WookieUser wookieUser = wookieService.getUserByUserIdAndSessionId(new Long(lamsUserDTO.getUserID()),
		toolSessionID);

	//NotebookEntry wookieEntry = wookieService.getEntry(wookieUser.getEntryUID());

	// construct dto's
	WookieDTO wookieDTO = new WookieDTO(wookie);

	WookieSessionDTO sessionDTO = new WookieSessionDTO();
	sessionDTO.setSessionName(wookieSession.getSessionName());
	sessionDTO.setSessionID(wookieSession.getSessionId());

	// If the user hasn't put in their entry yet, wookieEntry will be null;
	WookieUserDTO userDTO = new WookieUserDTO(wookieUser);

	sessionDTO.getUserDTOs().add(userDTO);
	wookieDTO.getSessionDTOs().add(sessionDTO);

	NotebookEntry notebookEntry = wookieService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		WookieConstants.TOOL_SIGNATURE, userDTO.getUserId().intValue());

	if (notebookEntry != null) {
	    userDTO.setNotebookEntry(notebookEntry.getEntry());
	    userDTO.setFinishedReflection(true);
	}

	String imageFileArray[] = new String[2];
	imageFileArray[0] = wookieDTO.getImageFileName();

	if (userDTO.getImageFileName() != null) {
	    imageFileArray[1] = userDTO.getImageFileName();
	}

	request.getSession().setAttribute("userWidgetURL", wookieUser.getUserWidgetURL());
	request.getSession().setAttribute("widgetHeight", wookieSession.getWidgetHeight());
	request.getSession().setAttribute("widgetWidth", wookieSession.getWidgetWidth());
	request.getSession().setAttribute("widgetMaximise", wookieSession.getWidgetMaximise());
	request.getSession().setAttribute("wookieDTO", wookieDTO);
    }

    private void doTeacherExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws WookieException {

	logger.debug("doExportTeacher: toolContentID:" + toolContentID);

	// check if toolContentID available
	if (toolContentID == null) {
	    String error = "Tool Content ID is missing. Unable to continue";
	    logger.error(error);
	    throw new WookieException(error);
	}

	Wookie wookie = wookieService.getWookieByContentId(toolContentID);

	WookieDTO wookieDTO = new WookieDTO(wookie);

	for (WookieSessionDTO sessionDTO : wookieDTO.getSessionDTOs()) {
	    Long toolSessionID = sessionDTO.getSessionID();

	    // Initiate the wookie widget for the monitor
	    if (!StringUtils.isEmpty(sessionDTO.getWidgetSharedDataKey())) {
		String sessionUserWidgetUrl = initiateWidget(sessionDTO.getWidgetIdentifier(),
			sessionDTO.getWidgetSharedDataKey());
		sessionDTO.setSessionUserWidgetUrl(sessionUserWidgetUrl);
	    }

	    for (WookieUserDTO userDTO : sessionDTO.getUserDTOs()) {
		// get the notebook entry.
		NotebookEntry notebookEntry = wookieService.getEntry(toolSessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
			WookieConstants.TOOL_SIGNATURE, userDTO.getUserId().intValue());
		if (notebookEntry != null) {
		    userDTO.notebookEntry = notebookEntry.getEntry();
		    userDTO.setFinishedReflection(true);
		}

	    }
	}

	// Set a flag if there is only one session
	boolean multipleSessionFlag = false;
	if (wookieDTO.getSessionDTOs() != null && wookieDTO.getSessionDTOs().size() > 1) {
	    multipleSessionFlag = true;
	}
	request.setAttribute("multipleSessionFlag", multipleSessionFlag);

	request.getSession().setAttribute("wookieDTO", wookieDTO);
    }

    private String initiateWidget(String wookieIdentifier, String sharedDataKey) throws WookieException {
	try {

	    String wookieUrl = wookieService.getWookieURL();
	    String wookieKey = wookieService.getWookieAPIKey();

	    wookieUrl += WookieConstants.RELATIVE_URL_WIDGET_SERVICE;

	    String returnXML = WookieUtil.getWidget(wookieUrl, wookieKey, wookieIdentifier, getUser(), sharedDataKey,
		    true);
	    return WookieUtil.getWidgetUrlFromXML(returnXML);

	} catch (Exception e) {
	    logger.error("Problem intitating widget for learner" + e);
	    throw new WookieException(e);
	}
    }

    private UserDTO getUser() {
	return (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
    }

}
