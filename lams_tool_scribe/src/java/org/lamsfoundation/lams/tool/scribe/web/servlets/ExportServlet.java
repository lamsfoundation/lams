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

package org.lamsfoundation.lams.tool.scribe.web.servlets;

import java.util.Iterator;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeDTO;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeSessionDTO;
import org.lamsfoundation.lams.tool.scribe.dto.ScribeUserDTO;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;
import org.lamsfoundation.lams.tool.scribe.model.ScribeUser;
import org.lamsfoundation.lams.tool.scribe.service.IScribeService;
import org.lamsfoundation.lams.tool.scribe.service.ScribeServiceProxy;
import org.lamsfoundation.lams.tool.scribe.util.ScribeConstants;
import org.lamsfoundation.lams.tool.scribe.util.ScribeException;
import org.lamsfoundation.lams.tool.scribe.util.ScribeUtils;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

	private static final long serialVersionUID = -2829707715037631881L;

	private static Logger logger = Logger.getLogger(ExportServlet.class);

	private final String FILENAME = "scribe_main.html";

	private IScribeService scribeService;

	protected String doExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies) {

		if (scribeService == null) {
			scribeService = ScribeServiceProxy
					.getScribeService(getServletContext());
		}

		String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
		try {
			if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
				request.getSession().setAttribute(AttributeNames.ATTR_MODE,
						ToolAccessMode.LEARNER);
				doLearnerExport(request, response, directoryName, cookies);
				writeResponseToFile(basePath + "/pages/export/learner.jsp",
						directoryName, FILENAME, cookies);
			} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER
					.toString())) {
				request.getSession().setAttribute(AttributeNames.ATTR_MODE,
						ToolAccessMode.TEACHER);
				doTeacherExport(request, response, directoryName, cookies);
				writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp",
						directoryName, FILENAME, cookies);
			}
		} catch (ScribeException e) {
			logger.error("Cannot perform export for scribe tool.");
		} finally {
			// clear any session objects
			request.getSession().removeAttribute("scribeUserDTO");
			request.getSession().removeAttribute("scribeSessionDTO");
			request.getSession().removeAttribute("scribeDTO");
			request.getSession().removeAttribute("otherScribeSessions");
		}


		return FILENAME;
	}
	
	private void doLearnerExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies)
			throws ScribeException {

		logger.debug("doLearnerExport: toolContentID:" + toolSessionID);

		// check if toolContentID available
		if (toolSessionID == null) {
			String error = "Tool Session ID is missing. Unable to continue";
			logger.error(error);
			throw new ScribeException(error);
		}

		ScribeSession scribeSession = scribeService
				.getSessionBySessionId(toolSessionID);

		// get the scribe user
		ScribeUser scribeUser = scribeService.getUserByUserIdAndSessionId(userID, toolSessionID);

		Scribe scribe = scribeSession.getScribe();
		ScribeDTO scribeDTO = new ScribeDTO(scribeSession.getScribe());
		request.getSession().setAttribute("scribeDTO", scribeDTO);
		
		ScribeSessionDTO sessionDTO = ScribeUtils.createSessionDTO(scribeSession);
		request.getSession().setAttribute("scribeSessionDTO", sessionDTO);

		ScribeUserDTO scribeUserDTO = new ScribeUserDTO(scribeUser);
		if (scribeUser.isFinishedActivity()) {
			// get the notebook entry.
			NotebookEntry notebookEntry = scribeService.getEntry(scribeSession
					.getSessionId(), CoreNotebookConstants.NOTEBOOK_TOOL,
					ScribeConstants.TOOL_SIGNATURE, scribeUser.getUserId()
							.intValue());
			if (notebookEntry != null) {
				scribeUserDTO.notebookEntry = notebookEntry.getEntry();
			}
		}
		request.getSession().setAttribute("scribeUserDTO", scribeUserDTO);

		if ( scribe.isShowAggregatedReports() ) {

			TreeMap<String, ScribeSessionDTO> otherScribeSessions = ScribeUtils.getReportDTOs(scribeSession);
			if ( otherScribeSessions.size() > 0 ) {
				request.getSession().setAttribute("otherScribeSessions", otherScribeSessions.values());
			}

		}
		
	}

	private void doTeacherExport(HttpServletRequest request,
			HttpServletResponse response, String directoryName, Cookie[] cookies)
			throws ScribeException {

		logger.debug("doExportTeacher: toolContentID:" + toolContentID);

		// check if toolContentID available
		if (toolContentID == null) {
			String error = "Tool Content ID is missing. Unable to continue";
			logger.error(error);
			throw new ScribeException(error);
		}

		Scribe scribe = scribeService.getScribeByContentId(toolContentID);
		ScribeDTO scribeDTO = new ScribeDTO(scribe);
		for (Iterator iter = scribe.getScribeSessions().iterator(); iter
				.hasNext();) {

			ScribeSession session = (ScribeSession) iter.next();
			ScribeSessionDTO sessionDTO = new ScribeSessionDTO(session);
			
			int numberOfVotes = 0;
			for (Iterator iterator = session.getScribeUsers().iterator(); iterator
					.hasNext();) {
				ScribeUser user = (ScribeUser) iterator.next();
				
				// count the votes
				if (user.isReportApproved()) {
					numberOfVotes++;
				}
				
				// if reflectOnActivity is enabled add all userDTO.
				if (session.getScribe().isReflectOnActivity()) {

					ScribeUserDTO userDTO = new ScribeUserDTO(user);
					// get the entry.
					NotebookEntry entry = scribeService.getEntry(session
							.getSessionId(),
							CoreNotebookConstants.NOTEBOOK_TOOL,
							ScribeConstants.TOOL_SIGNATURE, user.getUserId()
									.intValue());
					if (entry != null) {
						userDTO.finishedReflection = true;
						userDTO.notebookEntry = entry.getEntry();
					} else {
						userDTO.finishedReflection = false;
					}
					sessionDTO.getUserDTOs().add(userDTO);
				}
			}
			
			int numberOfLearners = session.getScribeUsers().size();
			
			sessionDTO.setNumberOfVotes(numberOfVotes);
			sessionDTO.setNumberOfLearners(numberOfLearners);
			sessionDTO.setVotePercentage(ScribeUtils.calculateVotePercentage(numberOfVotes, numberOfLearners));
			scribeDTO.getSessionDTOs().add(sessionDTO);
		}
		request.getSession().setAttribute("scribeDTO", scribeDTO);
	}
}
