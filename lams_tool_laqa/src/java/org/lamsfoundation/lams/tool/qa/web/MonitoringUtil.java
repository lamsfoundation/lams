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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.qa.QaAllGroupsDTO;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.qa.QaMonitoredUserDTO;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaStatsDTO;
import org.lamsfoundation.lams.tool.qa.QaStringComparator;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;

/**
 * 
 * Keeps all operations needed for Monitoring mode.
 * 
 * @author Ozgur Demirtas
 * 
 */
public class MonitoringUtil implements QaAppConstants {
    static Logger logger = Logger.getLogger(MonitoringUtil.class.getName());

    /**
     * determine whether all the tool sessions for a particular content has been
     * COMPLETED boolean isSessionsSync(HttpServletRequest request, long
     * toolContentId)
     * 
     * @param toolContentId
     * @return boolean
     */
    public boolean isSessionsSync(QaContent qaContent) {
	/*
	 * iterate all the tool sessions, if even one session is INCOMPLETE, the function returns false
	 */
	if (qaContent != null) {
	    Iterator sessionIterator = qaContent.getQaSessions().iterator();
	    while (sessionIterator.hasNext()) {
		QaSession qaSession = (QaSession) sessionIterator.next();
		if (qaSession.getSession_status().equalsIgnoreCase(QaSession.INCOMPLETE))
		    return false;
	    }
	}

	return true;
    }

    /**
     * updateResponse(HttpServletRequest request, String responseId, String
     * updatedResponse)
     * 
     * @param qaService
     * @param responseId
     * @param updatedResponse
     */
    public void updateResponse(IQaService qaService, String responseId, String updatedResponse) {
	logger.debug("load response with responseId: " + new Long(responseId).longValue());
	QaUsrResp qaUsrResp = qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	qaUsrResp.setAnswer(updatedResponse);
	qaService.updateQaUsrResp(qaUsrResp);
    }

    /**
     * hideResponse(HttpServletRequest request, String responseId)
     * 
     * @param qaService
     * @param responseId
     */
    public void hideResponse(IQaService qaService, String responseId) {
	logger.debug("load response with responseId for hiding: " + new Long(responseId).longValue());
	QaUsrResp qaUsrResp = qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	qaUsrResp.setHidden(true);
	qaService.updateQaUsrResp(qaUsrResp);
    }

    /**
     * unHideResponse(HttpServletRequest request, String responseId)
     * 
     * @param request
     * @param responseId
     */
    public void unHideResponse(IQaService qaService, String responseId) {
	logger.debug("load response with responseId for un-hiding: " + new Long(responseId).longValue());
	QaUsrResp qaUsrResp = qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	qaUsrResp.setHidden(false);
	qaService.updateQaUsrResp(qaUsrResp);
    }

    /**
     * populates all the tool sessions in a map
     * populateToolSessions(HttpServletRequest request, McContent mcContent)
     * 
     * @param request
     * @param mcContent
     * @return Map
     */
    public static Map populateToolSessions(HttpServletRequest request, QaContent qaContent, IQaService qaService) {
	List sessionsList = qaService.getSessionNamesFromContent(qaContent);
	logger.debug("sessionsList size is:..." + sessionsList.size());

	Map sessionsMap = QaUtils.convertToStringMap(sessionsList, "String");

	if (sessionsMap.isEmpty()) {
	    logger.debug("sessionsMap size is 0:");
	    sessionsMap.put(new Long(1).toString(), "None");
	} else {
	    logger.debug("sessionsMap has some entries: " + sessionsMap.size());
	    sessionsMap.put(new Long(sessionsMap.size() + 1).toString(), "All");
	}
	return sessionsMap;
    }

    /**
     * populates all the tool sessions in a map
     * populateToolSessions(HttpServletRequest request, McContent mcContent)
     * 
     * @param request
     * @param mcContent
     * @return Map
     */
    public static Map populateToolSessionsId(HttpServletRequest request, QaContent qaContent, IQaService qaService) {
	List sessionsList = qaService.getSessionsFromContent(qaContent);
	logger.debug("sessionsList size is:..." + sessionsList.size());

	Map sessionsMap = QaUtils.convertToStringMap(sessionsList, "Long");
	logger.debug("sessionsMap size:..." + sessionsMap.size());

	if (sessionsMap.isEmpty()) {
	    logger.debug("sessionsMap size is 0:");
	    sessionsMap.put(new Long(1).toString(), "None");
	} else {
	    logger.debug("sessionsMap has some entries: " + sessionsMap.size());
	    sessionsMap.put(new Long(sessionsMap.size() + 1).toString(), "All");
	}
	return sessionsMap;
    }

    /**
     * ends up populating the attempt history for all the users of all the tool
     * sessions for a content buildGroupsQuestionData(HttpServletRequest
     * request, McContent mcContent)
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
	logger.debug("buildGroupsQuestionData: " + currentSessionId);

	List listQuestions = qaService.getAllQuestionEntries(qaContent.getUid());

	String sessionName = "";
	if ((currentSessionId != null) && (!currentSessionId.equals("All"))) {
	    QaSession qaSession = qaService.retrieveQaSessionOrNullById(new Long(currentSessionId).longValue());

	    sessionName = qaSession.getSession_name();
	}
	request.setAttribute(CURRENT_SESSION_NAME, sessionName);

	List listMonitoredAnswersContainerDTO = new LinkedList();

	Iterator itListQuestions = listQuestions.iterator();
	while (itListQuestions.hasNext()) {
	    QaQueContent qaQueContent = (QaQueContent) itListQuestions.next();

	    if (qaQueContent != null) {
		QaMonitoredAnswersDTO qaMonitoredAnswersDTO = new QaMonitoredAnswersDTO();
		qaMonitoredAnswersDTO.setQuestionUid(qaQueContent.getUid().toString());
		qaMonitoredAnswersDTO.setQuestion(qaQueContent.getQuestion());
		qaMonitoredAnswersDTO.setSessionName(sessionName);

		logger.debug("using allUsersData to retrieve users data: " + isUserNamesVisible);
		Map questionAttemptData = buildGroupsAttemptData(request, qaContent, qaService, qaQueContent,
			qaQueContent.getUid().toString(), isUserNamesVisible, isLearnerRequest, currentSessionId,
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
	    QaQueContent qaQueContent, String questionUid, boolean isUserNamesVisible, boolean isLearnerRequest,
	    String currentSessionId, String userId) {
	logger.debug("doing buildGroupsAttemptData...");

	Map mapMonitoredAttemptsContainerDTO = new TreeMap(new QaStringComparator());
	List listMonitoredAttemptsContainerDTO = new LinkedList();

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
				Map sessionUsersAttempts = populateSessionUsersAttempts(request, qaService, qaSession
					.getQaSessionId(), listUsers, questionUid, isUserNamesVisible,
					isLearnerRequest, userId);
				listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
			    }
			}
		    }
		} else if (!currentSessionId.equals("All")) {
		    QaSession qaSession = qaService
			    .retrieveQaSession(new Long(currentSessionId.toString()).longValue());

		    List listUsers = qaService.getUserBySessionOnly(qaSession);

		    Map sessionUsersAttempts = populateSessionUsersAttempts(request, qaService, new Long(
			    currentSessionId), listUsers, questionUid, isUserNamesVisible, isLearnerRequest, userId);
		    listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
		}
	    }
	} else {
	    /*request is for learner report, use only the passed tool session in the report. */
	    logger.debug("using currentSessionId for the learner report:" + currentSessionId);
	    QaSession qaSession = qaService.retrieveQaSession(new Long(currentSessionId).longValue());
	    if (qaSession != null) {
		List listUsers = null;
		if (qaContent.isShowOtherAnswers()) {
		    listUsers = qaService.getUserBySessionOnly(qaSession);
		} else {
		    listUsers = new ArrayList<QaQueUsr>();
		    QaQueUsr currentUser = qaService.getQaUserBySession(new Long(userId).longValue(), qaSession
			    .getUid());
		    if (currentUser != null)
			listUsers.add(currentUser);
		}
		logger.debug("listQaUsers for session id:" + qaSession.getQaSessionId() + " = " + listUsers);
		Map sessionUsersAttempts = populateSessionUsersAttempts(request, qaService, qaSession.getQaSessionId(),
			listUsers, questionUid, isUserNamesVisible, isLearnerRequest, userId);
		listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);
	    }
	}

	mapMonitoredAttemptsContainerDTO = convertToMap(listMonitoredAttemptsContainerDTO);
	return mapMonitoredAttemptsContainerDTO;
    }

    /**
     * Populates all the user's attempt data of a particular tool session
     * populateSessionUsersAttempts(HttpServletRequest request,List listUsers)
     * User id is needed if isUserNamesVisible is false && is learnerRequest =
     * true, as it is required to work out if the data being analysed is the
     * current user.
     * 
     * @param request
     * @param listUsers
     * @return List
     */
    public static Map populateSessionUsersAttempts(HttpServletRequest request, IQaService qaService, Long sessionId,
	    List listUsers, String questionUid, boolean isUserNamesVisible, boolean isLearnerRequest, String userId) {

	logger.debug("doing populateSessionUsersAttempts...");

	Map mapMonitoredUserContainerDTO = new TreeMap(new QaStringComparator());
	List listMonitoredUserContainerDTO = new LinkedList();
	Iterator itUsers = listUsers.iterator();

	while (itUsers.hasNext()) {
	    QaQueUsr qaQueUsr = (QaQueUsr) itUsers.next();

	    if (qaQueUsr != null) {
		List listUserAttempts = qaService.getAttemptsForUserAndQuestionContent(qaQueUsr.getUid(), new Long(
			questionUid));

		Iterator itAttempts = listUserAttempts.iterator();
		while (itAttempts.hasNext()) {
		    QaUsrResp qaUsrResp = (QaUsrResp) itAttempts.next();

		    if (qaUsrResp != null) {
			QaMonitoredUserDTO qaMonitoredUserDTO = new QaMonitoredUserDTO();
			qaMonitoredUserDTO.setAttemptTime(qaUsrResp.getAttemptTime());
			qaMonitoredUserDTO.setTimeZone(qaUsrResp.getTimezone());
			qaMonitoredUserDTO.setUid(qaUsrResp.getResponseId().toString());

			if (!isUserNamesVisible && isLearnerRequest
				&& !userId.equals(qaQueUsr.getQueUsrId().toString())) {
			    logger.debug("this is  not current user, put his name as blank.");
			    qaMonitoredUserDTO.setUserName("        ");
			} else {
			    logger.debug("this is current user, put his name normally.");
			    qaMonitoredUserDTO.setUserName(qaQueUsr.getFullname());
			}

			qaMonitoredUserDTO.setQueUsrId(qaQueUsr.getUid().toString());
			qaMonitoredUserDTO.setSessionId(sessionId.toString());
			qaMonitoredUserDTO.setResponse(qaUsrResp.getAnswer());

			String responsePresentable = QaUtils.replaceNewLines(qaUsrResp.getAnswer());
			qaMonitoredUserDTO.setResponsePresentable(responsePresentable);

			qaMonitoredUserDTO.setQuestionUid(questionUid);
			qaMonitoredUserDTO.setVisible(new Boolean(qaUsrResp.isVisible()).toString());
			listMonitoredUserContainerDTO.add(qaMonitoredUserDTO);
		    }
		}
	    }
	}
	mapMonitoredUserContainerDTO = convertToMcMonitoredUserDTOMap(listMonitoredUserContainerDTO);
	return mapMonitoredUserContainerDTO;
    }

    public static Map convertToMcMonitoredUserDTOMap(List list) {
	Map map = new TreeMap(new QaStringComparator());

	Iterator listIterator = list.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    QaMonitoredUserDTO data = (QaMonitoredUserDTO) listIterator.next();
	    map.put(mapIndex.toString(), data);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    public static Map removeNewLinesMap(Map map) {
	Map newMap = new TreeMap(new QaStringComparator());

	Iterator itMap = map.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    logger.debug("using the  summary tool sessions pair: " + pairs.getKey() + " = " + pairs.getValue());
	    newMap.put(pairs.getKey(), QaUtils.replaceNewLines(pairs.getValue().toString()));
	}
	return newMap;
    }

    public static Map convertToMap(List list) {
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
		logger.debug("qaSession id: " + qaSession.getQaSessionId());

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

    public static void buildQaStatsDTO(HttpServletRequest request, IQaService qaService, QaContent qaContent) {
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
	logger.debug("countAllUsers: " + countAllUsers);
	logger.debug("countSessionComplete: " + countSessionComplete);

	qaStatsDTO.setCountAllUsers(new Integer(countAllUsers).toString());
	qaStatsDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());

	request.setAttribute(QA_STATS_DTO, qaStatsDTO);
    }

    public static void generateGroupsSessionData(HttpServletRequest request, IQaService qaService, QaContent qaContent,
	    boolean forExport) {

	List listAllGroupsDTO = buildGroupBasedSessionData(request, qaContent, qaService);

	request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);

	if (forExport)
	    request.getSession().setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);
    }

    public static List buildGroupBasedSessionData(HttpServletRequest request, QaContent qaContent, IQaService qaService) {
	logger.debug("buildGroupBasedSessionData: " + qaContent.getUid());
	List listQuestions = qaService.getAllQuestionEntries(qaContent.getUid());

	List listAllGroupsContainerDTO = new LinkedList();

	Iterator iteratorSession = qaContent.getQaSessions().iterator();
	while (iteratorSession.hasNext()) {
	    QaSession qaSession = (QaSession) iteratorSession.next();
	    String currentSessionId = qaSession.getQaSessionId().toString();

	    String currentSessionName = qaSession.getSession_name();

	    QaAllGroupsDTO qaAllGroupsDTO = new QaAllGroupsDTO();
	    List listMonitoredAnswersContainerDTO = new LinkedList();

	    if (qaSession != null) {
		Iterator itListQuestions = listQuestions.iterator();
		while (itListQuestions.hasNext()) {
		    QaQueContent qaQueContent = (QaQueContent) itListQuestions.next();

		    if (qaQueContent != null) {
			QaMonitoredAnswersDTO qaMonitoredAnswersDTO = new QaMonitoredAnswersDTO();
			qaMonitoredAnswersDTO.setQuestionUid(qaQueContent.getUid().toString());
			qaMonitoredAnswersDTO.setQuestion(qaQueContent.getQuestion());
			qaMonitoredAnswersDTO.setSessionId(currentSessionId);
			qaMonitoredAnswersDTO.setSessionName(currentSessionName);

			Map questionAttemptData = buildGroupsAttemptData(request, qaContent, qaService, qaQueContent,
				qaQueContent.getUid().toString(), true, false, currentSessionId, null);
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
