/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.mc.McAllGroupsDTO;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McCandidateAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.mc.McMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.mc.McMonitoredUserDTO;
import org.lamsfoundation.lams.tool.mc.McSessionMarkDTO;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
import org.lamsfoundation.lams.tool.mc.McUserMarkDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
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
public class MonitoringUtil implements McAppConstants {

    /**
     * 
     * ends up populating the attempt history for all the users of all the tool sessions for a content
     * 
     * @param request
     * @param mcContent
     * @return List
     */
    public static List<McMonitoredAnswersDTO> buildGroupsQuestionData(McContent mcContent, IMcService mcService) {
	// will be building groups question data for content

	List<McQueContent> questions = mcService.getAllQuestionEntries(mcContent.getUid());

	List<McMonitoredAnswersDTO> monitoredAnswersDTOs = new LinkedList<McMonitoredAnswersDTO>();

	for (McQueContent question : questions) {

	    if (question != null) {
		McMonitoredAnswersDTO monitoredAnswersDTO = new McMonitoredAnswersDTO();
		monitoredAnswersDTO.setQuestionUid(question.getUid().toString());
		monitoredAnswersDTO.setQuestion(question.getQuestion());
		monitoredAnswersDTO.setMark(question.getMark().toString());

		List<McCandidateAnswersDTO> listCandidateAnswersDTO = mcService.populateCandidateAnswersDTO(question
			.getUid());
		monitoredAnswersDTO.setCandidateAnswersCorrect(listCandidateAnswersDTO);

		Map<String, List<McMonitoredUserDTO>> questionAttemptData = new TreeMap<String, List<McMonitoredUserDTO>>(
			new McStringComparator());

		for (McSession session : (Set<McSession>) mcContent.getMcSessions()) {
		    Set<McQueUsr> users = session.getMcQueUsers();
		    List<McMonitoredUserDTO> monitoredUserDTOs = new LinkedList<McMonitoredUserDTO>();
		    for (McQueUsr user : users) {
			McMonitoredUserDTO monitoredUserDTO = getUserAttempt(mcService, user, session,
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
    public static List buildGroupsQuestionDataForExportLearner(McContent mcContent, IMcService mcService,
	    McSession mcSession, McQueUsr mcQueUsr) {

	List<McQueContent> questions = mcService.getAllQuestionEntries(mcContent.getUid());

	List listMonitoredAnswersContainerDTO = new LinkedList();

	Iterator<McQueContent> itListQuestions = questions.iterator();
	while (itListQuestions.hasNext()) {
	    McQueContent question = itListQuestions.next();

	    if (question != null) {
		McMonitoredAnswersDTO monitoredAnswersDTO = new McMonitoredAnswersDTO();
		monitoredAnswersDTO.setQuestionUid(question.getUid().toString());
		monitoredAnswersDTO.setQuestion(question.getQuestion());
		monitoredAnswersDTO.setMark(question.getMark().toString());

		List<McCandidateAnswersDTO> listCandidateAnswersDTO = mcService.populateCandidateAnswersDTO(question.getUid());
		monitoredAnswersDTO.setCandidateAnswersCorrect(listCandidateAnswersDTO);

		// Get the attempts for this user. The maps must match the maps in buildGroupsAttemptData or the jsp
		// won't work.
		McMonitoredUserDTO mcMonitoredUserDTO = getUserAttempt(mcService, mcQueUsr, mcSession,
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
     * @param request
     * @param mcContent
     * @param mcService
     * @return
     */
    public static List<McSessionMarkDTO> buildGroupsMarkData(McContent mcContent, IMcService mcService) {
	List<McSessionMarkDTO> listMonitoredMarksContainerDTO = new LinkedList<McSessionMarkDTO>();
	Set<McSession> sessions = mcContent.getMcSessions();
	Iterator<McSession> sessionsIterator = sessions.iterator();
	int numQuestions = mcContent.getMcQueContents().size();

	while (sessionsIterator.hasNext()) {
	    McSession mcSession = sessionsIterator.next();

	    McSessionMarkDTO mcSessionMarkDTO = new McSessionMarkDTO();
	    mcSessionMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
	    mcSessionMarkDTO.setSessionName(mcSession.getSession_name().toString());

	    Set<McQueUsr> sessionUsers = mcSession.getMcQueUsers();
	    Iterator<McQueUsr> usersIterator = sessionUsers.iterator();

	    Map<String, McUserMarkDTO> mapSessionUsersData = new TreeMap<String, McUserMarkDTO>(new McStringComparator());
	    Long mapIndex = new Long(1);

	    while (usersIterator.hasNext()) {
		McQueUsr user = usersIterator.next();

		McUserMarkDTO mcUserMarkDTO = new McUserMarkDTO();
		mcUserMarkDTO.setSessionId(mcSession.getMcSessionId().toString());
		mcUserMarkDTO.setSessionName(mcSession.getSession_name().toString());
		mcUserMarkDTO.setFullName(user.getFullname());
		mcUserMarkDTO.setUserName(user.getUsername());
		mcUserMarkDTO.setQueUsrId(user.getUid().toString());

		// The marks for the user must be listed in the display order of the question.
		// Other parts of the code assume that the questions will be in consecutive display
		// order starting 1 (e.g. 1, 2, 3, not 1, 3, 4) so we set up an array and use
		// the ( display order - 1) as the index (arrays start at 0, rather than 1 hence -1)
		// The user must answer all questions, so we can assume that they will have marks
		// for all questions or no questions.
		// At present there can only be one answer for each question but there may be more
		// than one in the future and if so, we don't want to count the mark twice hence
		// we need to check if we've already processed this question in the total.
		Integer[] userMarks = new Integer[numQuestions];
		String[] answeredOptions = new String[numQuestions];
		Date attemptTime = null;
		List<McUsrAttempt> finalizedUserAttempts = mcService.getFinalizedUserAttempts(user);
		long totalMark = 0;
		for (McUsrAttempt attempt : finalizedUserAttempts) {
		    Integer displayOrder = attempt.getMcQueContent().getDisplayOrder();
		    int arrayIndex = displayOrder != null && displayOrder.intValue() > 0 ? displayOrder.intValue() - 1
			    : 1;
		    if (userMarks[arrayIndex] == null) {

			// We get the mark for the attempt if the answer is correct and we don't allow
			// retries, or if the answer is correct and the learner has met the passmark if
			// we do allow retries.
			boolean isRetries = mcSession.getMcContent().isRetries();
			Integer mark = attempt.getMarkForShow(isRetries);
			userMarks[arrayIndex] = mark;
			totalMark += mark.intValue();
			
			// find out the answered option's sequential letter - A,B,C...
			String answeredOptionLetter = "";
			int optionCount = 1;
			for (McOptsContent option : (Set<McOptsContent>) attempt.getMcQueContent().getMcOptionsContents()) {
			    if (attempt.getMcOptionsContent().getUid().equals(option.getUid())) {
				answeredOptionLetter = String.valueOf((char) (optionCount + 'A' - 1));
				break;
			    }
			    optionCount++;
			}
			answeredOptions[arrayIndex] = answeredOptionLetter;
		    }
		    // get the attempt time, (NB all questions will have the same attempt time)
		    // Not efficient, since we assign this value for each attempt
		    attemptTime = attempt.getAttemptTime();
		}

		mcUserMarkDTO.setMarks(userMarks);
		mcUserMarkDTO.setAnsweredOptions(answeredOptions);
		mcUserMarkDTO.setAttemptTime(attemptTime);
		mcUserMarkDTO.setTotalMark(new Long(totalMark));

		mapSessionUsersData.put(mapIndex.toString(), mcUserMarkDTO);
		mapIndex = new Long(mapIndex.longValue() + 1);
	    }

	    mcSessionMarkDTO.setUserMarks(mapSessionUsersData);
	    listMonitoredMarksContainerDTO.add(mcSessionMarkDTO);
	}

	return listMonitoredMarksContainerDTO;
    }

    /**
     * 
     */
    public static McMonitoredUserDTO getUserAttempt(IMcService mcService, McQueUsr mcQueUsr,
	    McSession mcSession, Long questionUid) {

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

    /**
     * @param request
     * @param mcService
     * @param mcContent
     */
    public static void setSessionUserCount(McContent mcContent, McGeneralMonitoringDTO mcGeneralMonitoringDTO) {
	int countSessionComplete = 0;
	int countAllUsers = 0;
	Iterator iteratorSession = mcContent.getMcSessions().iterator();
	while (iteratorSession.hasNext()) {
	    McSession mcSession = (McSession) iteratorSession.next();

	    if (mcSession != null) {

		if (mcSession.getSessionStatus().equals(COMPLETED)) {
		    countSessionComplete++;
		}
		countAllUsers += mcSession.getMcQueUsers().size();
	    }
	}

	mcGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers));
	mcGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete));

	if (countSessionComplete > 0)
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(Boolean.FALSE.toString());
    }

    /**
     * 
     * @param mcService
     * @param mcContent
     * @return
     */
    public static boolean notebookEntriesExist(IMcService mcService, McContent mcContent) {
	Iterator iteratorSession = mcContent.getMcSessions().iterator();
	while (iteratorSession.hasNext()) {
	    McSession mcSession = (McSession) iteratorSession.next();

	    if (mcSession != null) {

		Iterator iteratorUser = mcSession.getMcQueUsers().iterator();
		while (iteratorUser.hasNext()) {
		    McQueUsr mcQueUsr = (McQueUsr) iteratorUser.next();

		    if (mcQueUsr != null) {
			NotebookEntry notebookEntry = mcService.getEntry(mcSession.getMcSessionId(),
				CoreNotebookConstants.NOTEBOOK_TOOL, MY_SIGNATURE, new Integer(mcQueUsr.getQueUsrId()
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

    /**
     * @param request
     * @param mcContent
     * @param mcService
     * @return
     */
    public static List buildGroupBasedSessionData(McContent mcContent, IMcService mcService) {
	List listQuestions = mcService.getAllQuestionEntries(mcContent.getUid());

	List listAllGroupsContainerDTO = new LinkedList();

	Iterator iteratorSession = mcContent.getMcSessions().iterator();
	while (iteratorSession.hasNext()) {
	    McSession mcSession = (McSession) iteratorSession.next();
	    String currentSessionId = mcSession.getMcSessionId().toString();

	    String currentSessionName = mcSession.getSession_name();

	    McAllGroupsDTO mcAllGroupsDTO = new McAllGroupsDTO();
	    List listMonitoredAnswersContainerDTO = new LinkedList();

	    if (mcSession != null) {
		Iterator itListQuestions = listQuestions.iterator();
		while (itListQuestions.hasNext()) {
		    McQueContent mcQueContent = (McQueContent) itListQuestions.next();

		    if (mcQueContent != null) {
			McMonitoredAnswersDTO mcMonitoredAnswersDTO = new McMonitoredAnswersDTO();
			mcMonitoredAnswersDTO.setQuestionUid(mcQueContent.getUid().toString());
			mcMonitoredAnswersDTO.setQuestion(mcQueContent.getQuestion());
			mcMonitoredAnswersDTO.setSessionId(currentSessionId);
			mcMonitoredAnswersDTO.setSessionName(currentSessionName);

			Map questionAttemptData = new TreeMap();

			mcMonitoredAnswersDTO.setQuestionAttempts(questionAttemptData);

			listMonitoredAnswersContainerDTO.add(mcMonitoredAnswersDTO);
		    }
		}
	    }
	    mcAllGroupsDTO.setGroupData(listMonitoredAnswersContainerDTO);
	    mcAllGroupsDTO.setSessionName(currentSessionName);
	    mcAllGroupsDTO.setSessionId(currentSessionId);

	    listAllGroupsContainerDTO.add(mcAllGroupsDTO);

	}
	return listAllGroupsContainerDTO;
    }

    /**
     * Sets up auxiliary parameters. Used by all monitoring action methods.
     * 
     * @param request
     * @param mcContent
     * @param mcService
     */
    protected static void setupAllSessionsData(HttpServletRequest request, McContent mcContent, IMcService mcService) {
	List listMonitoredAnswersContainerDTO = MonitoringUtil.buildGroupsQuestionData(mcContent, mcService);
	request.setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);

	List listMonitoredMarksContainerDTO = MonitoringUtil.buildGroupsMarkData(mcContent, mcService);
	request.setAttribute(LIST_MONITORED_MARKS_CONTAINER_DTO, listMonitoredMarksContainerDTO);

	request.setAttribute(HR_COLUMN_COUNT, new Integer(mcContent.getMcQueContents().size() + 2).toString());

	String strPassMark = "";
	Integer passMark = mcContent.getPassMark();
	if (passMark == null)
	    strPassMark = " ";
	else if ((passMark != null) && (passMark.equals("0")))
	    strPassMark = " ";
	else
	    strPassMark = passMark.toString();
	if (strPassMark.trim().equals("0"))
	    strPassMark = " ";
	request.setAttribute(PASSMARK, strPassMark);

	// setting up the advanced summary for LDEV-1662
	request.setAttribute("questionsSequenced", mcContent.isQuestionsSequenced());
	request.setAttribute("showMarks", mcContent.isShowMarks());
	request.setAttribute("randomize", mcContent.isRandomize());
	request.setAttribute("displayAnswers", mcContent.isDisplayAnswers());
	request.setAttribute("retries", mcContent.isRetries());
	request.setAttribute("reflect", mcContent.isReflect());
	request.setAttribute("reflectionSubject", mcContent.getReflectionSubject());
	request.setAttribute("passMark", mcContent.getPassMark());
	request.setAttribute("toolContentID", mcContent.getMcContentId());

	// setting up Date and time restriction in activities
	HttpSession ss = SessionManager.getSession();
	Date submissionDeadline = mcContent.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    request.setAttribute("submissionDeadline", tzSubmissionDeadline.getTime());
	}

	boolean isGroupedActivity = mcService.isGroupedActivity(new Long(mcContent.getMcContentId()));
	request.setAttribute("isGroupedActivity", isGroupedActivity);
    }
}
