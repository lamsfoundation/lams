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
package org.lamsfoundation.lams.tool.qa.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaAllGroupsDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaMonitoredUserDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaStatsDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.util.QaSessionComparator;
import org.lamsfoundation.lams.tool.qa.util.QaStringComparator;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * Keeps all operations needed for Monitoring mode.
 * 
 * @author Ozgur Demirtas
 * 
 */
public class MonitoringUtil implements QaAppConstants {

    /**
     * populates all the tool sessions in a map
     * 
     * @param request
     * @param mcContent
     * @return Map
     */
    public static Map populateToolSessions(HttpServletRequest request, QaContent qaContent, IQaService qaService) {
	List sessionsList = qaService.getSessionNamesFromContent(qaContent);
	
	Map sessionsMap = QaUtils.convertToStringMap(sessionsList, "String");
	if (sessionsMap.isEmpty()) {
	    sessionsMap.put(new Long(1).toString(), "None");
	} else {
	    sessionsMap.put(new Long(sessionsMap.size() + 1).toString(), "All");
	}
	return sessionsMap;
    }

    /**
     * populates all the tool sessions in a map
     * 
     * @param request
     * @param mcContent
     * @return Map
     */
    public static Map populateToolSessionsId(HttpServletRequest request, QaContent qaContent, IQaService qaService) {
	List sessionsList = qaService.getSessionsFromContent(qaContent);

	Map sessionsMap = QaUtils.convertToStringMap(sessionsList, "Long");
	if (sessionsMap.isEmpty()) {
	    sessionsMap.put(new Long(1).toString(), "None");
	} else {
	    sessionsMap.put(new Long(sessionsMap.size() + 1).toString(), "All");
	}
	return sessionsMap;
    }

    /**
     * ends up populating the attempt history for all the users of all the tool
     * sessions for a content 
     * 
     * User id is needed if isUserNamesVisible is false && is learnerRequest =
     * true, as it is required to work out if the data being analysed is the
     * current user.
     * 
     * @param request
     * @param mcContent
     * @return List
     */
    public static List buildGroupsQuestionData(HttpServletRequest request, QaContent qaContent, IQaService qaService,
	    boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId) {
	List listQuestions = qaService.getAllQuestionEntries(qaContent.getUid());

	String sessionName = "";
	if ((currentSessionId != null) && (!currentSessionId.equals("All"))) {
	    QaSession qaSession = qaService.getSessionById(new Long(currentSessionId).longValue());
	    sessionName = qaSession.getSession_name();
	}
	request.setAttribute(CURRENT_SESSION_NAME, sessionName);

	List listMonitoredAnswersContainerDTO = new LinkedList();

	Iterator itListQuestions = listQuestions.iterator();
	while (itListQuestions.hasNext()) {
	    QaQueContent qaQuestion = (QaQueContent) itListQuestions.next();

	    if (qaQuestion != null) {
		QaMonitoredAnswersDTO qaMonitoredAnswersDTO = new QaMonitoredAnswersDTO();
		qaMonitoredAnswersDTO.setQuestionUid(qaQuestion.getUid().toString());
		qaMonitoredAnswersDTO.setQuestion(qaQuestion.getQuestion());
		qaMonitoredAnswersDTO.setFeedback(qaQuestion.getFeedback());
		qaMonitoredAnswersDTO.setSessionName(sessionName);

		Map questionAttemptData = buildGroupsAttemptData(request, qaContent, qaService,
			qaQuestion.getUid().toString(), isUserNamesVisible, isLearnerRequest, currentSessionId,
			userId);
		qaMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
		listMonitoredAnswersContainerDTO.add(qaMonitoredAnswersDTO);

	    }
	}
	return listMonitoredAnswersContainerDTO;
    }

    /**
     * User id is needed if learnerRequest = true, as it is required to work out
     * if the data being analysed is the current user (for not show other names)
     * or to work out which is the user's answers (for not show all answers).
     */
    public static Map buildGroupsAttemptData(HttpServletRequest request, QaContent qaContent, IQaService qaService,
	    String questionUid, boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId,
	    String userId) {
	List<Map<String, QaMonitoredUserDTO>> listMonitoredAttemptsContainerDTO = new LinkedList<Map<String, QaMonitoredUserDTO>>();

	Map summaryToolSessions = populateToolSessionsId(request, qaContent, qaService);

	Iterator itMap = summaryToolSessions.entrySet().iterator();

	/*request is for monitoring summary */
	if (!isLearnerRequest) {

	    if (currentSessionId != null) {
		if (currentSessionId.equals("All")) {
		    while (itMap.hasNext()) {
			Map.Entry pairs = (Map.Entry) itMap.next();

			if (!(pairs.getValue().toString().equals("None"))
				&& !(pairs.getValue().toString().equals("All"))) {
			    QaSession qaSession = qaService.retrieveQaSession(new Long(pairs.getValue().toString())
				    .longValue());

			    if (qaSession != null) {
				List listUsers = qaService.getUserBySessionOnly(qaSession);
				Map<String, QaMonitoredUserDTO> sessionUsersAttempts = populateSessionUsersAttempts(
					qaService, qaSession.getQaSessionId(), listUsers, questionUid,
					isUserNamesVisible, isLearnerRequest, userId);
				listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
			    }
			}
		    }
		} else if (!currentSessionId.equals("All")) {
		    QaSession qaSession = qaService
			    .retrieveQaSession(new Long(currentSessionId.toString()).longValue());

		    List listUsers = qaService.getUserBySessionOnly(qaSession);

		    Map sessionUsersAttempts = populateSessionUsersAttempts(qaService, new Long(
			    currentSessionId), listUsers, questionUid, isUserNamesVisible, isLearnerRequest, userId);
		    listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
		}
	    }
	} else {
	    /*request is for learner report, use only the passed tool session in the report. */
	    QaSession qaSession = qaService.retrieveQaSession(new Long(currentSessionId).longValue());
	    if (qaSession != null) {
		List listUsers = null;
		if (qaContent.isShowOtherAnswers()) {
		    listUsers = qaService.getUserBySessionOnly(qaSession);
		} else {
		    listUsers = new ArrayList<QaQueUsr>();
		    QaQueUsr currentUser = qaService.getUserByIdAndSession(new Long(userId).longValue(), qaSession
			    .getQaSessionId());
		    if (currentUser != null)
			listUsers.add(currentUser);
		}
		Map sessionUsersAttempts = populateSessionUsersAttempts(qaService, qaSession.getQaSessionId(),
			listUsers, questionUid, isUserNamesVisible, isLearnerRequest, userId);
		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
	    }
	}

	return convertToMap(listMonitoredAttemptsContainerDTO);
    }

    /**
     * Populates all the user's attempt data of a particular tool session.
     * User id is needed if isUserNamesVisible is false && is learnerRequest =
     * true, as it is required to work out if the data being analysed is the
     * current user.
     */
    private static Map<String, QaMonitoredUserDTO> populateSessionUsersAttempts(IQaService qaService, Long sessionId,
	    List<QaQueUsr> listUsers, String questionUid, boolean isUserNamesVisible, boolean isLearnerRequest, String userId) {

	List<QaMonitoredUserDTO> listMonitoredUserContainerDTO = new LinkedList<QaMonitoredUserDTO>();
	QaContent qaContent = qaService.getQaContentBySessionId(sessionId);
	
	for (QaQueUsr qaQueUsr : (List<QaQueUsr>) listUsers) {
	    QaUsrResp response = qaService.getResponseByUserAndQuestion(qaQueUsr.getQueUsrId(), new Long(questionUid));
	    if (response != null) {
		QaMonitoredUserDTO qaMonitoredUserDTO = new QaMonitoredUserDTO();
		qaMonitoredUserDTO.setAttemptTime(response.getAttemptTime());
		qaMonitoredUserDTO.setTimeZone(response.getTimezone());
		qaMonitoredUserDTO.setUid(response.getResponseId().toString());

		if (!isUserNamesVisible && isLearnerRequest && !userId.equals(qaQueUsr.getQueUsrId().toString())) {
		    // this is not current user, put his name as blank
		    qaMonitoredUserDTO.setUserName("        ");
		} else {
		    // this is current user, put his name normally
		    qaMonitoredUserDTO.setUserName(qaQueUsr.getFullname());
		}

		qaMonitoredUserDTO.setQueUsrId(qaQueUsr.getQueUsrId().toString());
		qaMonitoredUserDTO.setSessionId(sessionId.toString());
		qaMonitoredUserDTO.setResponse(response.getAnswer());

		String responsePresentable = QaUtils.replaceNewLines(response.getAnswer());
		qaMonitoredUserDTO.setResponsePresentable(responsePresentable);

		qaMonitoredUserDTO.setQuestionUid(questionUid);
		qaMonitoredUserDTO.setVisible(new Boolean(response.isVisible()).toString());

		// set averageRating
		if (qaContent.isAllowRateAnswers()) {
		    AverageRatingDTO averageRating = qaService.getAverageRatingDTOByResponse(response.getResponseId());
		    qaMonitoredUserDTO.setAverageRating(averageRating.getRating());
		    qaMonitoredUserDTO.setNumberOfVotes(averageRating.getNumberOfVotes());
		}

		listMonitoredUserContainerDTO.add(qaMonitoredUserDTO);
	    }
	}
	return convertToMcMonitoredUserDTOMap(listMonitoredUserContainerDTO);
    }

    private static Map<String, QaMonitoredUserDTO> convertToMcMonitoredUserDTOMap(List<QaMonitoredUserDTO> list) {
	Map<String, QaMonitoredUserDTO> map = new TreeMap<String, QaMonitoredUserDTO>(new QaStringComparator());

	Long mapIndex = new Long(1);
	for (QaMonitoredUserDTO data : list) {
	    map.put(mapIndex.toString(), data);
	    mapIndex = mapIndex + 1;
	}
	return map;
    }

    public static Map removeNewLinesMap(Map map) {
	Map newMap = new TreeMap(new QaStringComparator());

	Iterator itMap = map.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    newMap.put(pairs.getKey(), QaUtils.replaceNewLines(pairs.getValue().toString()));
	}
	return newMap;
    }

    private static Map convertToMap(List list) {
	Map map = new TreeMap(new QaStringComparator());

	Iterator listIterator = list.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    Map data = (Map) listIterator.next();
	    map.put(mapIndex.toString(), data);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    public static boolean notebookEntriesExist(IQaService qaService, QaContent qaContent) {
	Iterator iteratorSession = qaContent.getQaSessions().iterator();
	while (iteratorSession.hasNext()) {
	    QaSession qaSession = (QaSession) iteratorSession.next();

	    if (qaSession != null) {
		Iterator iteratorUser = qaSession.getQaQueUsers().iterator();
		while (iteratorUser.hasNext()) {
		    QaQueUsr qaQueUsr = (QaQueUsr) iteratorUser.next();

		    if (qaQueUsr != null) {

			NotebookEntry notebookEntry = qaService.getEntry(qaSession.getQaSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(qaQueUsr.getQueUsrId()
					.intValue()));
			if (notebookEntry != null) {
			    return true;
			}

		    }
		}
	    }
	}
	return false;
    }

    private static void buildQaStatsDTO(HttpServletRequest request, IQaService qaService, QaContent qaContent) {
	QaStatsDTO qaStatsDTO = new QaStatsDTO();

	int countSessionComplete = 0;
	int countAllUsers = 0;
	Iterator iteratorSession = qaContent.getQaSessions().iterator();
	while (iteratorSession.hasNext()) {
	    QaSession qaSession = (QaSession) iteratorSession.next();

	    if (qaSession != null) {

		if (qaSession.getSession_status().equals(COMPLETED)) {
		    ++countSessionComplete;
		}

		Iterator iteratorUser = qaSession.getQaQueUsers().iterator();
		while (iteratorUser.hasNext()) {
		    QaQueUsr qaQueUsr = (QaQueUsr) iteratorUser.next();

		    if (qaQueUsr != null) {
			++countAllUsers;
		    }
		}
	    }
	}

	qaStatsDTO.setCountAllUsers(new Integer(countAllUsers).toString());
	qaStatsDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());

	request.setAttribute(QA_STATS_DTO, qaStatsDTO);
    }

    public static void setUpMonitoring(HttpServletRequest request, IQaService qaService, QaContent qaContent) {
	
	// setting up the advanced summary for LDEV-1662
	request.setAttribute(QaAppConstants.ATTR_CONTENT, qaContent);
	
	boolean isGroupedActivity = qaService.isGroupedActivity(qaContent.getQaContentId());
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	buildQaStatsDTO(request, qaService, qaContent);
	
	//generateGroupsSessionData
	List listAllGroupsDTO = buildGroupBasedSessionData(request, qaContent, qaService);
	request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);
	   
    //set SubmissionDeadline, if any
    if (qaContent.getSubmissionDeadline() != null) {
    	Date submissionDeadline = qaContent.getSubmissionDeadline();
    	HttpSession ss = SessionManager.getSession();
    	UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
    	TimeZone teacherTimeZone = teacher.getTimeZone();
    	Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
    	request.setAttribute(QaAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
    }
    }
    
    public static List<QaAllGroupsDTO> buildGroupBasedSessionData(HttpServletRequest request, QaContent qaContent, IQaService qaService) {
	List<QaQueContent> listQuestions = qaService.getAllQuestionEntries(qaContent.getUid());

	List<QaAllGroupsDTO> listAllGroupsContainerDTO = new LinkedList<QaAllGroupsDTO>();

	Set<QaSession> sessions = new TreeSet<QaSession>(new QaSessionComparator());
	sessions.addAll(qaContent.getQaSessions());
	for (QaSession session : sessions) {
	    String currentSessionId = session.getQaSessionId().toString();

	    String currentSessionName = session.getSession_name();

	    QaAllGroupsDTO qaAllGroupsDTO = new QaAllGroupsDTO();
	    List<QaMonitoredAnswersDTO> listMonitoredAnswersContainerDTO = new LinkedList<QaMonitoredAnswersDTO>();

	    if (session != null) {
		Iterator<QaQueContent> itListQuestions = listQuestions.iterator();
		while (itListQuestions.hasNext()) {
		    QaQueContent qaQuestion = itListQuestions.next();

		    if (qaQuestion != null) {
			QaMonitoredAnswersDTO qaMonitoredAnswersDTO = new QaMonitoredAnswersDTO();
			qaMonitoredAnswersDTO.setQuestionUid(qaQuestion.getUid().toString());
			qaMonitoredAnswersDTO.setQuestion(qaQuestion.getQuestion());
			qaMonitoredAnswersDTO.setFeedback(qaQuestion.getFeedback());
			qaMonitoredAnswersDTO.setSessionId(currentSessionId);
			qaMonitoredAnswersDTO.setSessionName(currentSessionName);

			Map questionAttemptData = buildGroupsAttemptData(request, qaContent, qaService, qaQuestion
				.getUid().toString(), true, false, currentSessionId, null);
			qaMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
			listMonitoredAnswersContainerDTO.add(qaMonitoredAnswersDTO);
		    }
		}
	    }

	    qaAllGroupsDTO.setGroupData(listMonitoredAnswersContainerDTO);
	    qaAllGroupsDTO.setSessionName(currentSessionName);
	    qaAllGroupsDTO.setSessionId(currentSessionId);
	    listAllGroupsContainerDTO.add(qaAllGroupsDTO);

	}
	return listAllGroupsContainerDTO;
    }

}
