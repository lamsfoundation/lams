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

package org.lamsfoundation.lams.tool.mc.web;

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

import org.apache.commons.lang.StringEscapeUtils;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.mc.dto.SessionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.util.McSessionComparator;
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
     * Escapes all characters that may brake JS code on assigning Java value to JS String variable (particularly
     * escapes all quotes in the following way \").
     */
    public static void escapeQuotes(McUsrAttempt userAttempt) {

	McQueContent question = userAttempt.getMcQueContent();
	McOptsContent option = userAttempt.getMcOptionsContent();

	String questionText = question.getQuestion();
	if (questionText != null) {
	    String escapedQuestion = StringEscapeUtils.escapeJavaScript(questionText);
	    question.setEscapedQuestion(escapedQuestion);
	}

	String optionText = option.getMcQueOptionText();
	if (optionText != null) {
	    String escapedOptionText = StringEscapeUtils.escapeJavaScript(optionText);
	    option.setEscapedOptionText(escapedOptionText);
	}
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

	if (countSessionComplete > 0) {
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(Boolean.FALSE.toString());
	}
    }

    /**
     * Sets up auxiliary parameters. Used by all monitoring action methods.
     *
     * @param request
     * @param content
     * @param mcService
     */
    protected static void setupAllSessionsData(HttpServletRequest request, McContent content, IMcService mcService) {

	//set up sessionDTOs list
	Set<McSession> sessions = new TreeSet<McSession>(new McSessionComparator());
	sessions.addAll(content.getMcSessions());
	List<SessionDTO> sessionDtos = new LinkedList<SessionDTO>();
	for (McSession session : sessions) {
	    SessionDTO sessionDto = new SessionDTO();
	    sessionDto.setSessionId(session.getMcSessionId());
	    sessionDto.setSessionName(session.getSession_name());

	    sessionDtos.add(sessionDto);
	}
	request.setAttribute(SESSION_DTOS, sessionDtos);

	request.setAttribute(HR_COLUMN_COUNT, new Integer(content.getMcQueContents().size() + 2).toString());

	String strPassMark = "";
	Integer passMark = content.getPassMark();
	if (passMark == null) {
	    strPassMark = " ";
	} else if ((passMark != null) && (passMark.equals("0"))) {
	    strPassMark = " ";
	} else {
	    strPassMark = passMark.toString();
	}
	if (strPassMark.trim().equals("0")) {
	    strPassMark = " ";
	}
	request.setAttribute(PASSMARK, strPassMark);

	// setting up the advanced summary

	request.setAttribute(ATTR_CONTENT, content);
	request.setAttribute("questionsSequenced", content.isQuestionsSequenced());
	request.setAttribute("showMarks", content.isShowMarks());
	request.setAttribute("useSelectLeaderToolOuput", content.isUseSelectLeaderToolOuput());
	request.setAttribute("prefixAnswersWithLetters", content.isPrefixAnswersWithLetters());
	request.setAttribute("randomize", content.isRandomize());
	request.setAttribute("displayAnswers", content.isDisplayAnswers());
	request.setAttribute("retries", content.isRetries());
	request.setAttribute("reflect", content.isReflect());
	request.setAttribute("reflectionSubject", content.getReflectionSubject());
	request.setAttribute("passMark", content.getPassMark());
	request.setAttribute("toolContentID", content.getMcContentId());

	// setting up Date and time restriction in activities
	HttpSession ss = SessionManager.getSession();
	Date submissionDeadline = content.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    request.setAttribute(McAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute(McAppConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING, DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));

	}

	//prepare toolOutputDefinitions and activityEvaluation
	List<String> toolOutputDefinitions = new ArrayList<String>();
	toolOutputDefinitions.add(McAppConstants.OUTPUT_NAME_LEARNER_MARK);
	toolOutputDefinitions.add(McAppConstants.OUTPUT_NAME_LEARNER_ALL_CORRECT);
	String activityEvaluation = mcService.getActivityEvaluation(content.getMcContentId());
	request.setAttribute(McAppConstants.ATTR_TOOL_OUTPUT_DEFINITIONS, toolOutputDefinitions);
	request.setAttribute(McAppConstants.ATTR_ACTIVITY_EVALUATION, activityEvaluation);

	boolean isGroupedActivity = mcService.isGroupedActivity(new Long(content.getMcContentId()));
	request.setAttribute("isGroupedActivity", isGroupedActivity);
    }

    /**
     * Populates a sorted map of the tool session where the key is the mcSessionId and the value is name of the session.
     * If no sessions exists, there will be a single entry "None", otherwise on the end of the list will be the entry
     * "All"
     */
    public static Map<String, String> populateToolSessions(McContent mcContent) {
	Map<String, String> sessionsMap = new TreeMap<String, String>();
	Iterator iter = mcContent.getMcSessions().iterator();
	while (iter.hasNext()) {
	    McSession elem = (McSession) iter.next();
	    sessionsMap.put(elem.getMcSessionId().toString(), elem.getSession_name());
	}

	if (sessionsMap.isEmpty()) {
	    sessionsMap.put("None", "None");
	} else {
	    sessionsMap.put("All", "All");
	}

	return sessionsMap;

    }
}
