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

package org.lamsfoundation.lams.tool.notebook.web.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookDTO;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookEntryDTO;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookSessionDTO;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookUserDTO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.service.NotebookServiceProxy;
import org.lamsfoundation.lams.tool.notebook.util.NotebookException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

    private static final long serialVersionUID = -2829707715037631881L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "notebook_main.html";

    private INotebookService notebookService;

    @Override
    protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	if (notebookService == null) {
	    notebookService = NotebookServiceProxy.getNotebookService(getServletContext());
	}

	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		doLearnerExport(request, response, directoryName, cookies);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		doTeacherExport(request, response, directoryName, cookies);
	    }
	} catch (NotebookException e) {
	    logger.error("Cannot perform export for notebook tool.");
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
	writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    private void doLearnerExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws NotebookException {

	logger.debug("doExportLearner: toolContentID:" + toolSessionID);

	// check if toolContentID available
	if (toolSessionID == null) {
	    String error = "Tool Session ID is missing. Unable to continue";
	    logger.error(error);
	    throw new NotebookException(error);
	}

	NotebookSession notebookSession = notebookService.getSessionBySessionId(toolSessionID);

	Notebook notebook = notebookSession.getNotebook();

	Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	NotebookUser notebookUser = notebookService.getUserByUserIdAndSessionId(userID, toolSessionID);

	NotebookEntry notebookEntry = notebookService.getEntry(notebookUser.getEntryUID());

	// construct dto's
	NotebookDTO notebookDTO = new NotebookDTO();
	notebookDTO.setTitle(notebook.getTitle());
	notebookDTO.setInstructions(notebook.getInstructions());

	NotebookSessionDTO sessionDTO = new NotebookSessionDTO();
	sessionDTO.setSessionName(notebookSession.getSessionName());
	sessionDTO.setSessionID(notebookSession.getSessionId());

	// If the user hasn't put in their entry yet, notebookEntry will be null;
	NotebookUserDTO userDTO = notebookEntry != null ? new NotebookUserDTO(notebookUser, notebookEntry)
		: new NotebookUserDTO(notebookUser);

	sessionDTO.getUserDTOs().add(userDTO);
	notebookDTO.getSessionDTOs().add(sessionDTO);

	request.getSession().setAttribute("notebookDTO", notebookDTO);
    }

    private void doTeacherExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws NotebookException {

	logger.debug("doExportTeacher: toolContentID:" + toolContentID);

	// check if toolContentID available
	if (toolContentID == null) {
	    String error = "Tool Content ID is missing. Unable to continue";
	    logger.error(error);
	    throw new NotebookException(error);
	}

	Notebook notebook = notebookService.getNotebookByContentId(toolContentID);

	NotebookDTO notebookDTO = new NotebookDTO(notebook);

	// add the notebookEntry for each user in each session
	for (NotebookSessionDTO session : notebookDTO.getSessionDTOs()) {
	    for (NotebookUserDTO user : session.getUserDTOs()) {
		NotebookEntry entry = notebookService.getEntry(user.getEntryUID());
		if (entry != null) {
		    NotebookEntryDTO entryDTO = new NotebookEntryDTO(entry);
		    user.setEntryDTO(entryDTO);
		}
	    }
	}

	request.getSession().setAttribute("notebookDTO", notebookDTO);
    }

}
