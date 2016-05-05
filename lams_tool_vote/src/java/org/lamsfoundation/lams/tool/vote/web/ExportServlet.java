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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.ExportPortfolioDTO;
import org.lamsfoundation.lams.tool.vote.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.vote.dto.SessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;

/**
 * Enables exporting portfolio for teacher and learner modes.
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
	String basePath = WebUtil.getBaseServerURL() + request.getContextPath();

	if (StringUtils.equals(mode, ToolAccessMode.LEARNER.toString())) {
	    learner(request, response, directoryName, cookies);
	} else if (StringUtils.equals(mode, ToolAccessMode.TEACHER.toString())) {
	    teacher(request, response, directoryName, cookies);
	}

	writeResponseToFile(basePath + "/export/exportportfolio.jsp", directoryName, FILENAME, cookies);

	return FILENAME;
    }

    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	IVoteService voteService = VoteServiceProxy.getVoteService(getServletContext());

	if ((userID == null) || (toolSessionID == null)) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    ExportServlet.logger.error(error);
	    throw new VoteApplicationException(error);
	}

	VoteSession voteSession = voteService.getSessionBySessionId(toolSessionID);

	// If the learner hasn't voted yet, then they won't exist in the session.
	// Yet we might be asked for their page, as the activity has been commenced.
	// So need to do a "blank" page in that case
	VoteQueUsr learner = voteService.getVoteUserBySession(userID, voteSession.getUid());

	ExportPortfolioDTO exportPortfolioDTO = new ExportPortfolioDTO();
	if ((learner != null) && learner.isFinalScreenRequested()) {

	    VoteContent content = voteSession.getVoteContent();

	    if (content == null) {
		String error = "The content for this activity has not been defined yet.";
		ExportServlet.logger.error(error);
		throw new VoteApplicationException(error);
	    }

	    LinkedList<SessionDTO> sessionDTOs = getSessionDTOs(request, content, voteService, toolSessionID, userID);
	    exportPortfolioDTO.setSessionDtos(sessionDTOs);

	    boolean userExceptionNoToolSessions = !voteService.studentActivityOccurredGlobal(content);
	    exportPortfolioDTO.setUserExceptionNoToolSessions(userExceptionNoToolSessions);

	    boolean isGroupedActivity = voteService.isGroupedActivity(content.getVoteContentId());
	    request.getSession().setAttribute(VoteAppConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);

	    List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(content, userID);
	    request.getSession().setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

	} else {
	    // thise field is needed for the jsp.
	    exportPortfolioDTO.setUserExceptionNoToolSessions(false);
	}

	exportPortfolioDTO.setPortfolioExportMode("learner");
	request.getSession().setAttribute(VoteAppConstants.EXPORT_PORTFOLIO_DTO, exportPortfolioDTO);
    }

    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	IVoteService voteService = VoteServiceProxy.getVoteService(getServletContext());

	if (toolContentID == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    ExportServlet.logger.error(error);
	    throw new VoteApplicationException(error);
	}

	VoteContent content = voteService.getVoteContent(toolContentID);
	if (content == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    ExportServlet.logger.error(error);
	    throw new VoteApplicationException(error);
	}

	ExportPortfolioDTO exportPortfolioDTO = new ExportPortfolioDTO();
	exportPortfolioDTO.setPortfolioExportMode("teacher");

	LinkedList<SessionDTO> sessionDTOs = getSessionDTOs(request, content, voteService, null, null);
	exportPortfolioDTO.setSessionDtos(sessionDTOs);

	boolean userExceptionNoToolSessions = !voteService.studentActivityOccurredGlobal(content);
	exportPortfolioDTO.setUserExceptionNoToolSessions(userExceptionNoToolSessions);
	request.getSession().setAttribute(VoteAppConstants.EXPORT_PORTFOLIO_DTO, exportPortfolioDTO);

	boolean isGroupedActivity = voteService.isGroupedActivity(content.getVoteContentId());
	request.getSession().setAttribute(VoteAppConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);

	List<ReflectionDTO> reflectionsContainerDTO = voteService.getReflectionData(content, null);
	request.getSession().setAttribute(VoteAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
    }

    public LinkedList<SessionDTO> getSessionDTOs(HttpServletRequest request, VoteContent voteContent,
	    IVoteService voteService, Long currentSessionId, Long userId) {

	Set<VoteSession> sessions = new HashSet<VoteSession>();
	if (currentSessionId == null) {
	    sessions = voteContent.getVoteSessions();
	} else {
	    VoteSession voteSession = voteService.getSessionBySessionId(currentSessionId);
	    sessions.add(voteSession);
	}

	LinkedList<SessionDTO> sessionDTOs = new LinkedList<SessionDTO>();
	for (VoteSession session : sessions) {

	    SessionDTO sessionDTO = new SessionDTO();
	    sessionDTO.setSessionId(session.getVoteSessionId().toString());
	    sessionDTO.setSessionName(session.getSession_name());

	    List<VoteMonitoredAnswersDTO> openVotes = voteService.getOpenVotes(voteContent.getUid(),
		    session.getVoteSessionId(), userId);
	    sessionDTO.setOpenVotes(openVotes);

	    List<VoteMonitoredAnswersDTO> answerDTOs = new LinkedList<VoteMonitoredAnswersDTO>();
	    for (VoteQueContent question : (Set<VoteQueContent>) voteContent.getVoteQueContents()) {
		Long questionUid = question.getUid();

		List<VoteUsrAttempt> userAttempts;
		if (userId == null) {
		    /* request is for monitoring summary */
		    userAttempts = voteService.getAttemptsForQuestionContentAndSessionUid(questionUid,
			    session.getUid());

		} else {
		    /* request is for learner report, use only the passed tool session in the report */
		    VoteQueUsr user = voteService.getVoteUserBySession(userId, session.getUid());
		    userAttempts = voteService.getAttemptsForUserAndQuestionContent(user.getUid(), questionUid);
		}

		Map<Integer, VoteUsrAttempt> questionAttempts = new HashMap<Integer, VoteUsrAttempt>();
		Integer i = 0;
		for (VoteUsrAttempt userAttempt : userAttempts) {
		    questionAttempts.put(i++, userAttempt);
		}

		VoteMonitoredAnswersDTO answerDTO = new VoteMonitoredAnswersDTO();
		answerDTO.setQuestionUid(question.getUid().toString());
		answerDTO.setQuestion(question.getQuestion());
		answerDTO.setQuestionAttempts(questionAttempts);
		answerDTOs.add(answerDTO);
	    }
	    sessionDTO.setAnswers(answerDTOs);
	    ;

	    sessionDTOs.add(sessionDTO);
	}

	return sessionDTOs;
    }
}