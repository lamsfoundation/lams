/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.OpenTextAnswerDTO;
import org.lamsfoundation.lams.tool.vote.dto.SummarySessionDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteQuestionDTO;
import org.lamsfoundation.lams.tool.vote.model.VoteContent;
import org.lamsfoundation.lams.tool.vote.model.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.model.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Ozgur Demirtas
 */
@Controller
@RequestMapping("/monitoring")
public class MonitoringController implements VoteAppConstants {
    private static Logger logger = Logger.getLogger(MonitoringController.class.getName());

    @Autowired
    private IVoteService voteService;

    @RequestMapping(path = "/hideOpenVote")
    @ResponseBody
    public String hideOpenVote(HttpServletRequest request, HttpServletResponse response) {
	return toggleHideShow(request, response, false);
    }

    @RequestMapping(path = "/showOpenVote")
    @ResponseBody
    public String showOpenVote(HttpServletRequest request, HttpServletResponse response) {
	return toggleHideShow(request, response, true);
    }

    private String toggleHideShow(HttpServletRequest request, HttpServletResponse response, boolean show) {
	Long currentUid = WebUtil.readLongParam(request, "currentUid");
	logger.info("Current Uid" + currentUid);

	VoteUsrAttempt voteUsrAttempt = voteService.getAttemptByUID(currentUid);

	voteUsrAttempt.setVisible(show);
	voteService.updateVoteUsrAttempt(voteUsrAttempt);
	String nextActionMethod;
	if (show) {
	    nextActionMethod = "hideOptionVote";
	    voteService.showOpenVote(voteUsrAttempt);
	} else {
	    nextActionMethod = "showOpenVote";
	    voteService.hideOpenVote(voteUsrAttempt);
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("currentUid", currentUid);
	responseJSON.put("nextActionMethod", nextActionMethod);
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }

    @RequestMapping("/getVoteNomination")
    public String getVoteNomination(HttpServletRequest request) {
	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	MonitoringController.repopulateRequestParameters(request, voteGeneralMonitoringDTO);

	Long questionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_QUESTION_UID, false);
	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);

	VoteQueContent nomination = voteService.getQuestionByUid(questionUid);
	request.setAttribute("nominationText", nomination.getQuestion());

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
	request.setAttribute(VoteAppConstants.ATTR_QUESTION_UID, questionUid);
	if (sessionUid != null) {
	    request.setAttribute(VoteAppConstants.ATTR_SESSION_UID, sessionUid);
	}
	return "/monitoring/VoteNominationViewer";
    }

    @RequestMapping(path = "/getVoteNominationsJSON")
    @ResponseBody
    public String getVoteNominationsJSON(HttpServletRequest request, HttpServletResponse response) {

	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);
	if (sessionUid == 0L) {
	    sessionUid = null;
	    logger.info("Setting sessionUid to null");
	}

	Long questionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_QUESTION_UID, false);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	Integer sortByDate = WebUtil.readIntParam(request, "column[1]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = VoteAppConstants.SORT_BY_DEFAULT;
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? VoteAppConstants.SORT_BY_NAME_ASC : VoteAppConstants.SORT_BY_NAME_DESC;
	} else if (sortByDate != null) {
	    sorting = sortByDate.equals(0) ? VoteAppConstants.SORT_BY_DATE_ASC : VoteAppConstants.SORT_BY_DATE_DESC;
	}

	//return user list according to the given sessionID
	List<Object[]> users = voteService.getUserAttemptsForTablesorter(sessionUid, questionUid, page, size, sorting,
		searchString);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total_rows", voteService.getCountUsersBySession(sessionUid, questionUid, searchString));

	for (Object[] userAndAnswers : users) {

	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();
	    responseRow.put(VoteAppConstants.ATTR_USER_ID, (Integer) userAndAnswers[0]);
	    responseRow.put(VoteAppConstants.ATTR_USER_NAME, HtmlUtils.htmlEscape((String) userAndAnswers[2]));
	    responseRow.put(VoteAppConstants.ATTR_ATTEMPT_TIME,
		    DateUtil.convertToStringForJSON((Date) userAndAnswers[3], request.getLocale()));
	    responseRow.put(VoteAppConstants.ATTR_ATTEMPT_TIME_TIMEAGO,
		    DateUtil.convertToStringForTimeagoJSON((Date) userAndAnswers[3]));
	    responseRow.put(VoteAppConstants.ATTR_PORTRAIT_ID, (String) userAndAnswers[4]);
	    rows.add(responseRow);
	}
	responseJSON.set("rows", rows);
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }

    @RequestMapping("/statistics")
    public String statistics(HttpServletRequest request) {
	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);

	request.setAttribute("isGroupedActivity", voteService.isGroupedActivity(toolContentID));
	request.setAttribute(VoteAppConstants.VOTE_STATS_DTO, voteService.getStatisticsBySession(toolContentID));
	return "/monitoring/Stats";
    }

    @RequestMapping(path = "/getOpenTextNominationsJSON")
    @ResponseBody
    public String getOpenTextNominationsJSON(HttpServletRequest request, HttpServletResponse response) {

	Long sessionUid = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SESSION_UID, true);
	if (sessionUid == 0L) {
	    logger.info("Setting sessionUid to null");
	    sessionUid = null;
	}

	Long contentUid = WebUtil.readLongParam(request, VoteAppConstants.TOOL_CONTENT_UID, false);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByEntry = WebUtil.readIntParam(request, "column[0]", true);
	Integer sortByName = WebUtil.readIntParam(request, "column[1]", true);
	Integer sortByDate = WebUtil.readIntParam(request, "column[2]", true);
	Integer sortByVisible = WebUtil.readIntParam(request, "column[3]", true);
	String searchStringVote = request.getParameter("fcol[0]");
	String searchStringUsername = request.getParameter("fcol[1]");

	int sorting = VoteAppConstants.SORT_BY_DEFAULT;
	if (sortByEntry != null) {
	    sorting = sortByEntry.equals(0) ? VoteAppConstants.SORT_BY_ENTRY_ASC : VoteAppConstants.SORT_BY_ENTRY_DESC;
	}
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? VoteAppConstants.SORT_BY_NAME_ASC : VoteAppConstants.SORT_BY_NAME_DESC;
	} else if (sortByDate != null) {
	    sorting = sortByDate.equals(0) ? VoteAppConstants.SORT_BY_DATE_ASC : VoteAppConstants.SORT_BY_DATE_DESC;
	} else if (sortByVisible != null) {
	    sorting = sortByVisible.equals(0) ? VoteAppConstants.SORT_BY_VISIBLE_ASC
		    : VoteAppConstants.SORT_BY_VISIBLE_DESC;
	}

	//return user list according to the given sessionID
	List<OpenTextAnswerDTO> users = voteService.getUserOpenTextAttemptsForTablesorter(sessionUid, contentUid, page,
		size, sorting, searchStringVote, searchStringUsername);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	responseJSON.put("total_rows", voteService.getCountUsersForOpenTextEntries(sessionUid, contentUid,
		searchStringVote, searchStringUsername));

	for (OpenTextAnswerDTO userAndAttempt : users) {
	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();

	    responseRow.put("uid", userAndAttempt.getUserUid());
	    responseRow.put(VoteAppConstants.ATTR_USER_NAME, HtmlUtils.htmlEscape(userAndAttempt.getFullName()));

	    responseRow.put("userEntryUid", userAndAttempt.getUserEntryUid());
	    responseRow.put("userEntry", HtmlUtils.htmlEscape(userAndAttempt.getUserEntry()));
	    responseRow.put(VoteAppConstants.ATTR_ATTEMPT_TIME,
		    DateUtil.convertToStringForJSON(userAndAttempt.getAttemptTime(), request.getLocale()));
	    responseRow.put(VoteAppConstants.ATTR_ATTEMPT_TIME_TIMEAGO,
		    DateUtil.convertToStringForTimeagoJSON(userAndAttempt.getAttemptTime()));
	    responseRow.put("visible", userAndAttempt.isVisible());
	    responseRow.put("portraitId", userAndAttempt.getPortraitId());

	    rows.add(responseRow);
	}
	responseJSON.set("rows", rows);
	response.setContentType("application/json;charset=UTF-8");
	return responseJSON.toString();
    }

    @RequestMapping(path = "/setSubmissionDeadline", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String setSubmissionDeadline(HttpServletRequest request) {
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	VoteContent voteContent = voteService.getVoteContent(contentID);

	Long dateParameter = WebUtil.readLongParam(request, VoteAppConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		    .getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	}
	voteContent.setSubmissionDeadline(tzSubmissionDeadline);
	voteService.updateVote(voteContent);

	return formattedDate;
    }

    @RequestMapping("/start")
    public String start(HttpServletRequest request) {
	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	// initialiseMonitoringData
	voteGeneralMonitoringDTO.setRequestLearningReport(Boolean.FALSE.toString());

	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	logger.warn("Make sure ToolContentId is passed" + toolContentID);
	VoteContent voteContent = voteService.getVoteContent(new Long(toolContentID));

	if (voteContent == null) {
	    logger.error("Vote Content does not exist");
	    voteGeneralMonitoringDTO.setUserExceptionContentDoesNotExist(Boolean.TRUE.toString());
	    return "/error";
	}

	voteGeneralMonitoringDTO.setActivityTitle(voteContent.getTitle());
	voteGeneralMonitoringDTO.setActivityInstructions(voteContent.getInstructions());

	/* this section is related to summary tab. Starts here. */

	SortedSet<SummarySessionDTO> sessionDTOs = voteService.getMonitoringSessionDTOs(new Long(toolContentID));
	request.setAttribute("sessionDTOs", sessionDTOs);

	request.setAttribute(VoteAppConstants.VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	// setting up the advanced summary for LDEV-1662
	request.setAttribute("useSelectLeaderToolOuput", voteContent.isUseSelectLeaderToolOuput());
	request.setAttribute("lockOnFinish", voteContent.isLockOnFinish());
	request.setAttribute("allowText", voteContent.isAllowText());
	request.setAttribute("maxNominationCount", voteContent.getMaxNominationCount());
	request.setAttribute("minNominationCount", voteContent.getMinNominationCount());
	request.setAttribute("showResults", voteContent.isShowResults());
	request.setAttribute("toolContentID", voteContent.getVoteContentId());

	// setting up the SubmissionDeadline
	if (voteContent.getSubmissionDeadline() != null) {
	    Date submissionDeadline = voteContent.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(VoteAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute(VoteAppConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	voteGeneralMonitoringDTO.setCurrentTab("1");

	if (sessionDTOs.size() > 0) {
	    voteGeneralMonitoringDTO.setUserExceptionContentInUse(Boolean.TRUE.toString());
	}

	/*
	 * get the nominations section is needed for the Edit tab's View Only mode, starts here
	 */
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	sessionMap.put(VoteAppConstants.ACTIVITY_TITLE_KEY, voteContent.getTitle());
	sessionMap.put(VoteAppConstants.ACTIVITY_INSTRUCTIONS_KEY, voteContent.getInstructions());
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	List<VoteQuestionDTO> listQuestionDTO = new LinkedList<>();

	Iterator<VoteQueContent> queIterator = voteContent.getVoteQueContents().iterator();
	while (queIterator.hasNext()) {
	    VoteQuestionDTO voteQuestionDTO = new VoteQuestionDTO();

	    VoteQueContent voteQueContent = queIterator.next();
	    if (voteQueContent != null) {

		voteQuestionDTO.setQuestion(voteQueContent.getQuestion());
		voteQuestionDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
		listQuestionDTO.add(voteQuestionDTO);
	    }
	}

	request.setAttribute(VoteAppConstants.LIST_QUESTION_DTO, listQuestionDTO);
	sessionMap.put(VoteAppConstants.LIST_QUESTION_DTO, listQuestionDTO);

	// this section is needed for Edit Activity screen
	voteGeneralAuthoringDTO.setActivityTitle(voteGeneralMonitoringDTO.getActivityTitle());
	voteGeneralAuthoringDTO.setActivityInstructions(voteGeneralMonitoringDTO.getActivityInstructions());

	MonitoringController.repopulateRequestParameters(request, voteGeneralMonitoringDTO);

	boolean isGroupedActivity = voteService.isGroupedActivity(new Long(toolContentID));
	request.setAttribute("isGroupedActivity", isGroupedActivity);
	request.setAttribute("isAllowText", voteContent.isAllowText());

	return "/monitoring/MonitoringMaincontent";
    }

    @RequestMapping(path = "/displayChangeLeaderForGroupDialogFromActivity")
    public String displayChangeLeaderForGroupDialogFromActivity(
	    @RequestParam(name = AttributeNames.PARAM_TOOL_SESSION_ID) long toolSessionId) {
	// tell Change Leader dialog in Leader Selection tool which learner has already reached this activity
	String availableLearners = voteService.getSessionBySessionId(toolSessionId).getVoteQueUsers().stream()
		.collect(Collectors.mapping(user -> Long.toString(user.getQueUsrId()), Collectors.joining(",")));

	return new StringBuilder("redirect:").append(Configuration.get(ConfigurationKeys.SERVER_URL))
		.append("tool/lalead11/monitoring/displayChangeLeaderForGroupDialogFromActivity.do?toolSessionId=")
		.append(toolSessionId).append("&availableLearners=").append(availableLearners).toString();
    }

    @RequestMapping(path = "/changeLeaderForGroup", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void changeLeaderForGroup(@RequestParam(name = AttributeNames.PARAM_TOOL_SESSION_ID) long toolSessionId,
	    @RequestParam long leaderUserId) {
	voteService.changeLeaderForGroup(toolSessionId, leaderUserId);
    }

    public static Map<String, VoteMonitoredUserDTO> convertToVoteMonitoredUserDTOMap(List<VoteMonitoredUserDTO> list) {
	Map<String, VoteMonitoredUserDTO> map = new TreeMap<>(new VoteComparator());

	Iterator<VoteMonitoredUserDTO> listIterator = list.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    ;
	    VoteMonitoredUserDTO data = listIterator.next();

	    map.put(mapIndex.toString(), data);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    public static void repopulateRequestParameters(HttpServletRequest request,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) {
	String toolContentID = request.getParameter(VoteAppConstants.TOOL_CONTENT_ID);
	voteGeneralMonitoringDTO.setToolContentID(toolContentID);

	String responseId = request.getParameter(VoteAppConstants.RESPONSE_ID);
	voteGeneralMonitoringDTO.setResponseId(responseId);

	String currentUid = request.getParameter(VoteAppConstants.CURRENT_UID);
    }
}
