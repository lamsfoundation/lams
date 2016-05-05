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

package org.lamsfoundation.lams.tool.mc.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McMonitoredUserDTO;
import org.lamsfoundation.lams.tool.mc.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
import org.lamsfoundation.lams.tool.mc.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.servlet.AbstractExportPortfolioServlet;

/**
 * <p>
 * Enables exporting portfolio for teacher and learner modes.
 * </p>
 *
 * @author Ozgur Demirtas
 */

public class ExportServlet extends AbstractExportPortfolioServlet implements McAppConstants {
    static Logger logger = Logger.getLogger(ExportServlet.class.getName());
    private static final long serialVersionUID = -17790L;
    private final String FILENAME = "mcq_main.html";

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

    /**
     * learner(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
     *
     * generates report for learner mode
     *
     * @param request
     * @param response
     * @param directoryName
     * @param cookies
     */
    public void learner(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	IMcService mcService = McServiceProxy.getMcService(getServletContext());

	if (userID == null || toolSessionID == null) {
	    String error = "Tool session Id or user Id is null. Unable to continue";
	    logger.error(error);
	    throw new McApplicationException(error);
	}

	McSession mcSession = mcService.getMcSessionById(toolSessionID);

	// If the learner hasn't selected any options yet, then they won't exist in the session.
	// Yet we might be asked for their page, as the activity has been commenced. So need to do a "blank" page in
	// that case
	McQueUsr learner = mcService.getMcUserBySession(userID, mcSession.getUid());

	McContent content = mcSession.getMcContent();

	if (content == null) {
	    String error = "The content for this activity has not been defined yet.";
	    logger.error(error);
	    throw new McApplicationException(error);
	}

	request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "learner");

	if (learner != null) {
	    List listMonitoredAnswersContainerDTO = ExportServlet.buildGroupsQuestionDataForExportLearner(content,
		    mcService, mcSession, learner);
	    request.getSession().setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);

	    request.getSession().setAttribute(LEARNER_MARK, learner.getLastAttemptTotalMark());
	    request.getSession().setAttribute(LEARNER_NAME, learner.getFullname());
	    request.getSession().setAttribute(PASSMARK, content.getPassMark().toString());

	    List<ReflectionDTO> reflectionsContainerDTO = mcService.getReflectionList(content, userID);
	    request.getSession().setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);
	}

    }

    /**
     * teacher(HttpServletRequest request, HttpServletResponse response, String directoryName, Cookie[] cookies)
     *
     * generates report for teacher mode
     *
     * @param request
     * @param response
     * @param directoryName
     * @param cookies
     */
    public void teacher(HttpServletRequest request, HttpServletResponse response, String directoryName,
	    Cookie[] cookies) {

	IMcService mcService = McServiceProxy.getMcService(getServletContext());

	if (toolContentID == null) {
	    String error = "Tool Content Id is missing. Unable to continue";
	    logger.error(error);
	    throw new McApplicationException(error);
	}

	McContent content = mcService.getMcContent(toolContentID);

	if (content == null) {
	    String error = "Data is missing from the database. Unable to Continue";
	    logger.error(error);
	    throw new McApplicationException(error);
	}

	List<McMonitoredAnswersDTO> listMonitoredAnswersContainerDTO = ExportServlet.buildGroupsQuestionData(content,
		mcService);
	request.getSession().setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);

	List<McSessionMarkDTO> listMonitoredMarksContainerDTO = mcService.buildGroupsMarkData(content, true);
	request.getSession().setAttribute(LIST_MONITORED_MARKS_CONTAINER_DTO, listMonitoredMarksContainerDTO);

	request.getSession().setAttribute(PASSMARK, content.getPassMark().toString());
	request.getSession().setAttribute(PORTFOLIO_EXPORT_MODE, "teacher");

	List<ReflectionDTO> reflectionsContainerDTO = mcService.getReflectionList(content, userID);
	request.getSession().setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

	writeOutSessionData(request, response, content, mcService, directoryName);
    }

    /**
     * creates create spreadsheet of class data
     *
     * @param request
     * @param response
     * @param directoryName
     */
    public void writeOutSessionData(HttpServletRequest request, HttpServletResponse response, McContent mcContent,
	    IMcService mcService, String directoryName) {
	String fileName = "lams_mcq_All_" + toolContentID + ".xls";

	OutputStream out = null;
	try {
	    out = new FileOutputStream(directoryName + File.separator + fileName);

	    byte[] spreadsheet = mcService.prepareSessionDataSpreadsheet(mcContent);
	    out.write(spreadsheet);

	    request.getSession().setAttribute(PORTFOLIO_EXPORT_DATA_FILENAME, fileName);
	} catch (FileNotFoundException e) {
	    logger.error("Exception creating spreadsheet: ", e);
	} catch (IOException e) {
	    logger.error("exception creating spreadsheet: ", e);
	} catch (Exception e) {
	    logger.error("exception creating spreadsheet: ", e);
	} finally {
	    try {
		if (out != null) {
		    out.close();
		}
	    } catch (IOException e) {
	    }
	}

    }

    /**
     *
     * ends up populating the attempt history for all the users of all the tool sessions for a content
     *
     * @param request
     * @param mcContent
     * @return List
     */
    private static List<McMonitoredAnswersDTO> buildGroupsQuestionData(McContent mcContent, IMcService mcService) {
	// will be building groups question data for content

	List<McQueContent> questions = mcService.getQuestionsByContentUid(mcContent.getUid());

	List<McMonitoredAnswersDTO> monitoredAnswersDTOs = new LinkedList<McMonitoredAnswersDTO>();

	for (McQueContent question : questions) {

	    if (question != null) {
		McMonitoredAnswersDTO monitoredAnswersDTO = new McMonitoredAnswersDTO();
		monitoredAnswersDTO.setQuestionUid(question.getUid().toString());
		monitoredAnswersDTO.setQuestion(question.getQuestion());
		monitoredAnswersDTO.setMark(question.getMark().toString());

		List<McOptionDTO> listCandidateAnswersDTO = mcService.getOptionDtos(question.getUid());
		monitoredAnswersDTO.setCandidateAnswersCorrect(listCandidateAnswersDTO);

		Map<String, List<McMonitoredUserDTO>> questionAttemptData = new TreeMap<String, List<McMonitoredUserDTO>>(
			new McStringComparator());

		for (McSession session : (Set<McSession>) mcContent.getMcSessions()) {
		    Set<McQueUsr> users = session.getMcQueUsers();
		    List<McMonitoredUserDTO> monitoredUserDTOs = new LinkedList<McMonitoredUserDTO>();
		    for (McQueUsr user : users) {
			McMonitoredUserDTO monitoredUserDTO = ExportServlet.getUserAttempt(mcService, user, session,
				question.getUid());
			monitoredUserDTOs.add(monitoredUserDTO);
		    }

		    questionAttemptData.put(session.getSession_name(), monitoredUserDTOs);
		}

		monitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
		monitoredAnswersDTOs.add(monitoredAnswersDTO);

	    }
	}
	return monitoredAnswersDTOs;
    }

    /**
     *
     * @param request
     * @param mcContent
     * @param mcService
     * @param mcSession
     * @param mcQueUsr
     * @return
     */
    private static List buildGroupsQuestionDataForExportLearner(McContent mcContent, IMcService mcService,
	    McSession mcSession, McQueUsr mcQueUsr) {

	List<McQueContent> questions = mcService.getQuestionsByContentUid(mcContent.getUid());

	List listMonitoredAnswersContainerDTO = new LinkedList();

	Iterator<McQueContent> itListQuestions = questions.iterator();
	while (itListQuestions.hasNext()) {
	    McQueContent question = itListQuestions.next();

	    if (question != null) {
		McMonitoredAnswersDTO monitoredAnswersDTO = new McMonitoredAnswersDTO();
		monitoredAnswersDTO.setQuestionUid(question.getUid().toString());
		monitoredAnswersDTO.setQuestion(question.getQuestion());
		monitoredAnswersDTO.setMark(question.getMark().toString());

		List<McOptionDTO> listCandidateAnswersDTO = mcService.getOptionDtos(question.getUid());
		monitoredAnswersDTO.setCandidateAnswersCorrect(listCandidateAnswersDTO);

		// Get the attempts for this user. The maps must match the maps in buildGroupsAttemptData or the jsp
		// won't work.
		McMonitoredUserDTO mcMonitoredUserDTO = ExportServlet.getUserAttempt(mcService, mcQueUsr, mcSession,
			question.getUid());
		List<McMonitoredUserDTO> listMonitoredUserContainerDTO = new LinkedList();
		listMonitoredUserContainerDTO.add(mcMonitoredUserDTO);
		Map questionAttemptData = new TreeMap(new McStringComparator());
		questionAttemptData.put(mcSession.getSession_name(), listMonitoredUserContainerDTO);

		monitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
		listMonitoredAnswersContainerDTO.add(monitoredAnswersDTO);
	    }
	}
	return listMonitoredAnswersContainerDTO;
    }

    /**
     *
     */
    private static McMonitoredUserDTO getUserAttempt(IMcService mcService, McQueUsr mcQueUsr, McSession mcSession,
	    Long questionUid) {

	McMonitoredUserDTO mcMonitoredUserDTO = new McMonitoredUserDTO();
	if (mcQueUsr != null) {
	    mcMonitoredUserDTO.setUserName(mcQueUsr.getFullname());
	    mcMonitoredUserDTO.setSessionId(mcSession.getMcSessionId().toString());
	    mcMonitoredUserDTO.setQuestionUid(questionUid.toString());
	    mcMonitoredUserDTO.setQueUsrId(mcQueUsr.getUid().toString());

	    McUsrAttempt userAttempt = mcService.getUserAttemptByQuestion(mcQueUsr.getUid(), questionUid);

	    if (!mcQueUsr.isResponseFinalised() || (userAttempt == null)) {

		mcMonitoredUserDTO.setMark(new Integer(0));

	    } else {

		// At present, we expect there to be only one answer to a question but there
		// could be more in the future - if that happens then we need to change
		// String to a list of Strings.

		// We get the mark for the attempt if the answer is correct and we don't allow
		// retries, or if the answer is correct and the learner has met the passmark if
		// we do allow retries.

		String userAnswer = userAttempt.getMcOptionsContent().getMcQueOptionText();
		boolean isRetries = mcSession.getMcContent().isRetries();
		mcMonitoredUserDTO.setMark(userAttempt.getMarkForShow(isRetries));
		mcMonitoredUserDTO.setIsCorrect(new Boolean(userAttempt.isAttemptCorrect()).toString());
		mcMonitoredUserDTO.setUserAnswer(userAnswer);
	    }

	}

	return mcMonitoredUserDTO;
    }
}
