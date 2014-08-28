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

import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.qa.dto.GroupDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaMonitoredUserDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaStatsDTO;
import org.lamsfoundation.lams.tool.qa.dto.ReflectionDTO;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.util.QaSessionComparator;
import org.lamsfoundation.lams.tool.qa.util.QaStringComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * Keeps all operations needed for Monitoring mode.
 * 
 * @author Ozgur Demirtas
 */
public class MonitoringUtil implements QaAppConstants {

    /**
     * User id is needed if learnerRequest = true, as it is required to work out if the data being analysed is the
     * current user (for not show other names) or to work out which is the user's answers (for not show all answers).
     */
    public static Map buildGroupsAttemptData(HttpServletRequest request, QaContent qaContent, IQaService qaService,
	    String questionUid, boolean isUserNamesVisible, boolean isLearnerRequest, String sessionId, String userId) {
	List<Map<String, QaMonitoredUserDTO>> listMonitoredAttemptsContainerDTO = new LinkedList<Map<String, QaMonitoredUserDTO>>();

	Map<Long, AverageRatingDTO> mapResponseIdToAverageRating = qaService.getAverageRatingDTOByQuestionAndSession(new Long(questionUid), new Long(sessionId));

	List<QaUsrResp> responses = new ArrayList<QaUsrResp>();
	if (!isLearnerRequest) {
	    /* request is for monitoring summary */

	    if (qaContent.isUseSelectLeaderToolOuput()) {
		QaSession session = qaService.getSessionById(new Long(sessionId).longValue());
		QaQueUsr groupLeader = session.getGroupLeader();
		if (groupLeader != null) {
		    QaUsrResp response = qaService.getResponseByUserAndQuestion(groupLeader.getQueUsrId(), new Long(questionUid));
		    if (response != null) {
			responses.add(response);			
		    }
		}
		
	    } else {
		responses = qaService.getResponseBySessionAndQuestion(new Long(sessionId), new Long(questionUid));
	    }
	    
	} else {
	    if (qaContent.isShowOtherAnswers()) {
		responses = qaService.getResponseBySessionAndQuestion(new Long(sessionId), new Long(questionUid));
		
	    } else {
		QaUsrResp response = qaService.getResponseByUserAndQuestion(new Long(userId), new Long(questionUid));
		if (response != null) {
		    responses.add(response);
		}
	    }
	}

	/**
	 * Populates all the user's attempt data of a particular tool session. User id is needed if isUserNamesVisible
	 * is false && is learnerRequest = true, as it is required to work out if the data being analysed is the current
	 * user.
	 */
	List<QaMonitoredUserDTO> qaMonitoredUserDTOs = new LinkedList<QaMonitoredUserDTO>();
	for (QaUsrResp response : responses) {
	    QaQueUsr user = response.getQaQueUser();
	    if (response != null) {
		QaMonitoredUserDTO qaMonitoredUserDTO = new QaMonitoredUserDTO();
		qaMonitoredUserDTO.setAttemptTime(response.getAttemptTime());
		qaMonitoredUserDTO.setTimeZone(response.getTimezone());
		qaMonitoredUserDTO.setUid(response.getResponseId().toString());

		if (!isUserNamesVisible && isLearnerRequest && !userId.equals(user.getQueUsrId().toString())) {
		    // this is not current user, put his name as blank
		    qaMonitoredUserDTO.setUserName("        ");
		} else {
		    // this is current user, put his name normally
		    qaMonitoredUserDTO.setUserName(user.getFullname());
		}

		qaMonitoredUserDTO.setQueUsrId(user.getQueUsrId().toString());
		qaMonitoredUserDTO.setSessionId(sessionId.toString());
		qaMonitoredUserDTO.setResponse(response.getAnswer());

		// String responsePresentable = QaUtils.replaceNewLines(response.getAnswer());
		qaMonitoredUserDTO.setResponsePresentable(response.getAnswer());

		qaMonitoredUserDTO.setQuestionUid(questionUid);
		qaMonitoredUserDTO.setVisible(new Boolean(response.isVisible()).toString());

		// set averageRating
		if (qaContent.isAllowRateAnswers()) {
		    
		    AverageRatingDTO averageRating = mapResponseIdToAverageRating.get(response.getResponseId());
//		    AverageRatingDTO averageRating = qaService.getAverageRatingDTOByResponse(response.getResponseId());
		    if (averageRating == null) {
			qaMonitoredUserDTO.setAverageRating("0");
			qaMonitoredUserDTO.setNumberOfVotes("0");
		    } else {
			qaMonitoredUserDTO.setAverageRating(averageRating.getRating());
			qaMonitoredUserDTO.setNumberOfVotes(averageRating.getNumberOfVotes());
		    }

		}

		qaMonitoredUserDTOs.add(qaMonitoredUserDTO);
	    }
	}

	// convert To McMonitoredUserDTOMap
	Map<String, QaMonitoredUserDTO> sessionUsersAttempts = new TreeMap<String, QaMonitoredUserDTO>(
		new QaStringComparator());
	Long mapIndex = new Long(1);
	for (QaMonitoredUserDTO data : qaMonitoredUserDTOs) {
	    sessionUsersAttempts.put(mapIndex.toString(), data);
	    mapIndex = mapIndex + 1;
	}
	listMonitoredAttemptsContainerDTO.add(sessionUsersAttempts);

	//convertToMap
	Map map = new TreeMap(new QaStringComparator());
	Iterator listIterator = listMonitoredAttemptsContainerDTO.iterator();
	Long mapIndex2 = new Long(1);
	while (listIterator.hasNext()) {
	    Map data = (Map) listIterator.next();
	    map.put(mapIndex2.toString(), data);
	    mapIndex2 = new Long(mapIndex2.longValue() + 1);
	}
	return map;
    }

    public static void setUpMonitoring(HttpServletRequest request, IQaService qaService, QaContent qaContent) {

	// setting up the advanced summary for LDEV-1662
	request.setAttribute(QaAppConstants.ATTR_CONTENT, qaContent);

	boolean isGroupedActivity = qaService.isGroupedActivity(qaContent.getQaContentId());
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	//buildQaStatsDTO
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

	// generateGroupsSessionData
	List<GroupDTO> listAllGroupsDTO = buildGroupBasedSessionData(request, qaContent, qaService);
	request.setAttribute(LIST_ALL_GROUPS_DTO, listAllGroupsDTO);

	List<ReflectionDTO> reflectionDTOs = qaService.getReflectList(qaContent, null);
	request.setAttribute(QaAppConstants.REFLECTIONS_CONTAINER_DTO, reflectionDTOs);

	// set SubmissionDeadline, if any
	if (qaContent.getSubmissionDeadline() != null) {
	    Date submissionDeadline = qaContent.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(QaAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	}
    }

    public static List<GroupDTO> buildGroupBasedSessionData(HttpServletRequest request, QaContent qaContent,
	    IQaService qaService) {
	List<QaQueContent> questions = qaService.getAllQuestionEntries(qaContent.getUid());
	List<GroupDTO> groupDTOs = new LinkedList<GroupDTO>();
	Set<QaSession> sessions = new TreeSet<QaSession>(new QaSessionComparator());
	sessions.addAll(qaContent.getQaSessions());
	for (QaSession session : sessions) {
	    String sessionId = session.getQaSessionId().toString();
	    String sessionName = session.getSession_name();

	    GroupDTO groupDTO = new GroupDTO();
	    List<QaMonitoredAnswersDTO> qaMonitoredAnswersDTOs = new LinkedList<QaMonitoredAnswersDTO>();

	    if (session != null) {
		Iterator<QaQueContent> itQuestions = questions.iterator();
		while (itQuestions.hasNext()) {
		    QaQueContent question = itQuestions.next();

		    QaMonitoredAnswersDTO qaMonitoredAnswersDTO = new QaMonitoredAnswersDTO();
		    qaMonitoredAnswersDTO.setQuestionUid(question.getUid().toString());
		    qaMonitoredAnswersDTO.setQuestion(question.getQuestion());
		    qaMonitoredAnswersDTO.setFeedback(question.getFeedback());
		    qaMonitoredAnswersDTO.setSessionId(sessionId);
		    qaMonitoredAnswersDTO.setSessionName(sessionName);

		    Map questionAttemptData = buildGroupsAttemptData(request, qaContent, qaService, question.getUid()
			    .toString(), true, false, sessionId, null);
		    qaMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
		    qaMonitoredAnswersDTOs.add(qaMonitoredAnswersDTO);
		}
	    }

	    groupDTO.setGroupData(qaMonitoredAnswersDTOs);
	    groupDTO.setSessionName(sessionName);
	    groupDTO.setSessionId(sessionId);
	    groupDTOs.add(groupDTO);
	}
	return groupDTOs;
    }

    /**
     * ends up populating the attempt history for all the users of all the tool sessions for a content
     * 
     * User id is needed if isUserNamesVisible is false && is learnerRequest = true, as it is required to work out if
     * the data being analysed is the current user.
     */
    public static List buildGroupsQuestionData(HttpServletRequest request, QaContent qaContent, IQaService qaService,
	    boolean isUserNamesVisible, boolean isLearnerRequest, String sessionId, String userId) {
	List listQuestions = qaService.getAllQuestionEntries(qaContent.getUid());

	List listMonitoredAnswersContainerDTO = new LinkedList();

	Iterator itListQuestions = listQuestions.iterator();
	while (itListQuestions.hasNext()) {
	    QaQueContent qaQuestion = (QaQueContent) itListQuestions.next();

	    if (qaQuestion != null) {
		QaMonitoredAnswersDTO qaMonitoredAnswersDTO = new QaMonitoredAnswersDTO();
		qaMonitoredAnswersDTO.setQuestionUid(qaQuestion.getUid().toString());
		qaMonitoredAnswersDTO.setQuestion(qaQuestion.getQuestion());
		qaMonitoredAnswersDTO.setFeedback(qaQuestion.getFeedback());

		Map questionAttemptData = MonitoringUtil.buildGroupsAttemptData(request, qaContent, qaService,
			qaQuestion.getUid().toString(), isUserNamesVisible, isLearnerRequest, sessionId, userId);
		qaMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);
		listMonitoredAnswersContainerDTO.add(qaMonitoredAnswersDTO);

	    }
	}
	return listMonitoredAnswersContainerDTO;
    }

}
