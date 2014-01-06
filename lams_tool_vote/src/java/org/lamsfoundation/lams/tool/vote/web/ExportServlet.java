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

package org.lamsfoundation.lams.tool.vote.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.vote.ExportPortfolioDTO;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;

/**
 * <p>
 * Enables exporting portfolio for teacher and learner modes.
 * </p>
 * 
 * @author Ozgur Demirtas
 */

public class ExportServlet extends AbstractExportPortfolioServlet implements VoteAppConstants {
    static Logger logger = Logger.getLogger(ExportServlet.class.getName());
    private static final long serialVersionUID = -1529093489007108983L;
    private final String FILENAME = "vote_main.html";

    @Override
    public String doExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	String basePath = WebUtil.getBaseServerURL()
		+ request.getContextPath();

	if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
	    learner(request, response, directoryName, cookies);
	} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
	    teacher(request, response, directoryName, cookies);
	}

	writeResponseToFile(basePath + "/export/exportportfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    @Override
    protected String doOfflineExport(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {
	if ((toolContentID == null) && (toolSessionID == null)) {
	    ExportServlet.logger.error("Tool content Id or and session Id are null. Unable to activity title");
	} else {
	    IVoteService service = VoteServiceProxy.getVoteService(getServletContext());

	    VoteContent content = null;
	    if (toolContentID != null) {
		content = service.retrieveVote(toolContentID);
	    } else {
		VoteSession session = service.retrieveVoteSession(toolSessionID);
		if (session != null) {
		    content = session.getVoteContent();
		}
	    }
	    if (content != null) {
		activityTitle = content.getTitle();
	    }
	}
	return super.doOfflineExport(request, response, directoryName, cookies);
    }

    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies) {
	ExportPortfolioDTO exportPortfolioDTO = new ExportPortfolioDTO();

	exportPortfolioDTO.setPortfolioExportMode("learner");

	IVoteService voteService = VoteServiceProxy.getVoteService(getServletContext());
	MessageService messageService = VoteServiceProxy.getMessageService(getServletContext());

	if ((userID == null) || (toolSessionID == null)) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    ExportServlet.logger.error(error);
	    throw new VoteApplicationException(error);
	}

	VoteSession voteSession = voteService.retrieveVoteSession(toolSessionID);

	// If the learner hasn't voted yet, then they won't exist in the session.
	// Yet we might be asked for their page, as the activity has been commenced.
	// So need to do a "blank" page in that case
	VoteQueUsr learner = voteService.getVoteUserBySession(userID, voteSession.getUid());

	if ((learner != null) && learner.isFinalScreenRequested()) {

	    VoteContent content = voteSession.getVoteContent();

	    if (content == null) {
		String error = "The content for this activity has not been defined yet.";
		ExportServlet.logger.error(error);
		throw new VoteApplicationException(error);
	    }

	    exportPortfolioDTO.setAllowText(content.isAllowText());

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

	    VoteMonitoringAction voteMonitoringAction = new VoteMonitoringAction();
	    voteMonitoringAction.refreshSummaryData(request, content, voteService, true, true,
		    toolSessionID.toString(), userID.toString(), true, null, voteGeneralMonitoringDTO,
		    exportPortfolioDTO);

	    voteMonitoringAction.prepareReflectionData(request, content, voteService, userID.toString(), true, "");
	} else {
	    // thise field is needed for the jsp.
	    exportPortfolioDTO.setUserExceptionNoToolSessions("false");
	}

	request.getSession().setAttribute(VoteAppConstants.EXPORT_PORTFOLIO_DTO, exportPortfolioDTO);
    }

    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies) {
	ExportPortfolioDTO exportPortfolioDTO = new ExportPortfolioDTO();
	exportPortfolioDTO.setPortfolioExportMode("teacher");

	IVoteService voteService = VoteServiceProxy.getVoteService(getServletContext());
	MessageService messageService = VoteServiceProxy.getMessageService(getServletContext());

	if (toolContentID == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    ExportServlet.logger.error(error);
	    throw new VoteApplicationException(error);
	}

	VoteContent content = voteService.retrieveVote(toolContentID);

	if (content == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    ExportServlet.logger.error(error);
	    throw new VoteApplicationException(error);
	}

	exportPortfolioDTO.setAllowText(content.isAllowText());

	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	VoteMonitoringAction voteMonitoringAction = new VoteMonitoringAction();

	voteMonitoringAction.refreshSummaryData(request, content, voteService, true, false, null, null, false, null,
		voteGeneralMonitoringDTO, exportPortfolioDTO);

	request.getSession().setAttribute(VoteAppConstants.EXPORT_PORTFOLIO_DTO, exportPortfolioDTO);

	voteMonitoringAction.prepareReflectionData(request, content, voteService, null, true, "All");
    }
}