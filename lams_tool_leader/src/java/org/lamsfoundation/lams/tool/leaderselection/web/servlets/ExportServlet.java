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



package org.lamsfoundation.lams.tool.leaderselection.web.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.leaderselection.dto.LeaderselectionDTO;
import org.lamsfoundation.lams.tool.leaderselection.dto.LeaderselectionSessionDTO;
import org.lamsfoundation.lams.tool.leaderselection.dto.LeaderselectionUserDTO;
import org.lamsfoundation.lams.tool.leaderselection.model.Leaderselection;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionSession;
import org.lamsfoundation.lams.tool.leaderselection.model.LeaderselectionUser;
import org.lamsfoundation.lams.tool.leaderselection.service.ILeaderselectionService;
import org.lamsfoundation.lams.tool.leaderselection.service.LeaderselectionServiceProxy;
import org.lamsfoundation.lams.tool.leaderselection.util.LeaderselectionException;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;
import org.lamsfoundation.lams.web.util.AttributeNames;

public class ExportServlet extends AbstractExportPortfolioServlet {

    private static final long serialVersionUID = -2829707715037631881L;

    private static Logger logger = Logger.getLogger(ExportServlet.class);

    private final String FILENAME = "leaderselection_main.html";

    private ILeaderselectionService leaderselectionService;

    @Override
    protected String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	if (leaderselectionService == null) {
	    leaderselectionService = LeaderselectionServiceProxy.getLeaderselectionService(getServletContext());
	}

	try {
	    if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.LEARNER);
		doLearnerExport(request, response, directoryName, cookies);
	    } else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
		request.getSession().setAttribute(AttributeNames.ATTR_MODE, ToolAccessMode.TEACHER);
		doTeacherExport(request, response, directoryName, cookies);
	    }
	} catch (LeaderselectionException e) {
	    logger.error("Cannot perform export for leaderselection tool.");
	}

	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();
	writeResponseToFile(basePath + "/pages/export/exportPortfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    private void doLearnerExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws LeaderselectionException {

	logger.debug("doExportLearner: toolContentID:" + toolSessionID);

	// check if toolContentID available
	if (toolSessionID == null) {
	    String error = "Tool Session ID is missing. Unable to continue";
	    logger.error(error);
	    throw new LeaderselectionException(error);
	}

	LeaderselectionSession leaderselectionSession = leaderselectionService.getSessionBySessionId(toolSessionID);

	Leaderselection leaderselection = leaderselectionSession.getLeaderselection();

	Long userID = WebUtil.readLongParam(request, AttributeNames.PARAM_USER_ID);
	LeaderselectionUser leaderselectionUser = leaderselectionService.getUserByUserIdAndSessionId(userID,
		toolSessionID);

	// construct dto's
	LeaderselectionDTO leaderselectionDTO = new LeaderselectionDTO();
	leaderselectionDTO.setTitle(leaderselection.getTitle());
	leaderselectionDTO.setInstructions(leaderselection.getInstructions());

	LeaderselectionSessionDTO sessionDTO = new LeaderselectionSessionDTO();
	sessionDTO.setSessionName(leaderselectionSession.getSessionName());
	sessionDTO.setSessionID(leaderselectionSession.getSessionId());

	LeaderselectionUserDTO userDTO = new LeaderselectionUserDTO(leaderselectionUser);

	sessionDTO.getUserDTOs().add(userDTO);
	leaderselectionDTO.getSessionDTOs().add(sessionDTO);

	request.getSession().setAttribute("leaderselectionDTO", leaderselectionDTO);
    }

    private void doTeacherExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) throws LeaderselectionException {

	logger.debug("doExportTeacher: toolContentID:" + toolContentID);

	// check if toolContentID available
	if (toolContentID == null) {
	    String error = "Tool Content ID is missing. Unable to continue";
	    logger.error(error);
	    throw new LeaderselectionException(error);
	}

	Leaderselection leaderselection = leaderselectionService.getContentByContentId(toolContentID);

	LeaderselectionDTO leaderselectionDTO = new LeaderselectionDTO(leaderselection);

	request.getSession().setAttribute("leaderselectionDTO", leaderselectionDTO);
    }

}
