/***************************************************************************
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License version 2 as
published by the Free Software Foundation.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
USA

http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.qa.web.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.dto.GroupDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaQuestionDTO;
import org.lamsfoundation.lams.tool.qa.dto.QaStatsDTO;
import org.lamsfoundation.lams.tool.qa.model.QaContent;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.lamsfoundation.lams.tool.qa.model.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.model.QaSession;
import org.lamsfoundation.lams.tool.qa.model.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.util.QaSessionComparator;
import org.lamsfoundation.lams.tool.qa.util.QaUtils;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author Ozgur Demirtas
 */
@Controller
@RequestMapping("/monitoring")
public class MonitoringController implements QaAppConstants {
    private static Logger logger = Logger.getLogger(MonitoringController.class.getName());

    @Autowired
    private IQaService qaService;

    @RequestMapping("/monitoring")
    private String execute(HttpServletRequest request) throws ServletException {
	QaUtils.cleanUpSessionAbsolute(request);

	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);

	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	if ((strToolContentID == null) || (strToolContentID.length() == 0)) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    throw new ServletException("No Tool Content ID found");
	}

	QaContent qaContent = qaService.getQaContent(new Long(strToolContentID).longValue());
	if (qaContent == null) {
	    QaUtils.cleanUpSessionAbsolute(request);
	    throw new ServletException("Data not initialised in Monitoring");
	}	

	/* this section is related to summary tab. Starts here. */
//	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
//	sessionMap.put(ACTIVITY_TITLE_KEY, qaContent.getTitle());
//	sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, qaContent.getInstructions());
//
//	qaMonitoringForm.setHttpSessionID(sessionMap.getSessionID());
//	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

	List questionDTOs = new LinkedList();
	for (QaQueContent question : qaContent.getQaQueContents()) {
	    QaQuestionDTO questionDTO = new QaQuestionDTO(question);
	    questionDTOs.add(questionDTO);
	}
	request.setAttribute(LIST_QUESTION_DTOS, questionDTOs);
//	sessionMap.put(LIST_QUESTION_DTOS, questionDTOs);
//	request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(questionDTOs.size()));

	//session dto list
	List<GroupDTO> groupDTOs = new LinkedList<>();
	Set<QaSession> sessions = new TreeSet<>(new QaSessionComparator());
	sessions.addAll(qaContent.getQaSessions());
	for (QaSession session : sessions) {
	    String sessionId = session.getQaSessionId().toString();
	    String sessionName = session.getSession_name();

	    GroupDTO groupDTO = new GroupDTO();
	    groupDTO.setSessionName(sessionName);
	    groupDTO.setSessionId(sessionId);
	    groupDTOs.add(groupDTO);
	}
	request.setAttribute(LIST_ALL_GROUPS_DTO, groupDTOs);

	// setting up the advanced summary
	request.setAttribute(QaAppConstants.ATTR_CONTENT, qaContent);

	boolean isGroupedActivity = qaService.isGroupedActivity(qaContent.getQaContentId());
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	//ratings stuff
	boolean isRatingsEnabled = qaService.isRatingsEnabled(qaContent);
	request.setAttribute("isRatingsEnabled", isRatingsEnabled);

	//comments stuff
	boolean isCommentsEnabled = qaService.isCommentsEnabled(qaContent.getQaContentId());
	request.setAttribute("isCommentsEnabled", isCommentsEnabled);

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

	// set SubmissionDeadline, if any
	if (qaContent.getSubmissionDeadline() != null) {
	    Date submissionDeadline = qaContent.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(QaAppConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute(QaAppConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	return "monitoring/MonitoringMaincontent";
    }

    @RequestMapping("/updateResponse")
    public String updateResponse(HttpServletRequest request) throws IOException, ServletException {

	Long responseUid = WebUtil.readLongParam(request, QaAppConstants.RESPONSE_UID);
	String updatedResponse = request.getParameter("updatedResponse");
	QaUsrResp qaUsrResp = qaService.getResponseById(responseUid);

	/*
	 * write out the audit log entry. If you move this after the update of the response, then make sure you update
	 * the audit call to use a copy of the original answer
	 */
	Long toolContentId = null;
	if (qaUsrResp.getQaQuestion() != null && qaUsrResp.getQaQuestion().getQaContent() != null) {
	    toolContentId = qaUsrResp.getQaQuestion().getQaContent().getQaContentId();
	}
	qaService.getLogEventService().logChangeLearnerContent(qaUsrResp.getQaQueUser().getQueUsrId(),
		qaUsrResp.getQaQueUser().getUsername(), toolContentId, qaUsrResp.getAnswer(), updatedResponse);

	qaUsrResp.setAnswer(updatedResponse);
	qaService.updateUserResponse(qaUsrResp);

	return null;
    }

    @RequestMapping("/updateResponseVisibility")
    public String updateResponseVisibility(HttpServletRequest request)
	    throws IOException, ServletException, ToolException {

	Long responseUid = WebUtil.readLongParam(request, QaAppConstants.RESPONSE_UID);
	boolean isHideItem = WebUtil.readBooleanParam(request, QaAppConstants.IS_HIDE_ITEM);
	qaService.updateResponseVisibility(responseUid, isHideItem);

	return null;
    }

    /**
     * Set Submission Deadline
     */
    @RequestMapping(path = "/setSubmissionDeadline", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String setSubmissionDeadline(HttpServletRequest request) {
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaContent content = qaService.getQaContent(contentID);

	Long dateParameter = WebUtil.readLongParam(request, QaAppConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	} else {
	    //set showOtherAnswersAfterDeadline to false
	    content.setShowOtherAnswersAfterDeadline(false);
	}
	content.setSubmissionDeadline(tzSubmissionDeadline);
	qaService.saveOrUpdateQaContent(content);

	return formattedDate;
    }

    /**
     * Set Submission Deadline
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/setShowOtherAnswersAfterDeadline")
    public String setShowOtherAnswersAfterDeadline(HttpServletRequest request) {

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	QaContent content = qaService.getQaContent(contentID);

	boolean showOtherAnswersAfterDeadline = WebUtil.readBooleanParam(request,
		QaAppConstants.PARAM_SHOW_OTHER_ANSWERS_AFTER_DEADLINE);
	content.setShowOtherAnswersAfterDeadline(showOtherAnswersAfterDeadline);
	qaService.saveOrUpdateQaContent(content);

	return null;
    }

    /**
     * Get Paged Reflections
     *
     * @param request
     * @return
     */
    @RequestMapping(path = "/getReflectionsJSON")
    @ResponseBody
    public String getReflectionsJSON(HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException, ToolException {

	Long toolSessionId = WebUtil.readLongParam(request, QaAppConstants.TOOL_SESSION_ID);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = QaAppConstants.SORT_BY_NO;
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? QaAppConstants.SORT_BY_USERNAME_ASC : QaAppConstants.SORT_BY_USERNAME_DESC;
	}

	//return user list according to the given sessionID
	List<Object[]> users = qaService.getUserReflectionsForTablesorter(toolSessionId, page, size, sorting,
		searchString);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows", qaService.getCountUsersBySessionWithSearch(toolSessionId, searchString));

	for (Object[] userAndReflection : users) {
	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();
	    responseRow.put("username", HtmlUtils.htmlEscape((String) userAndReflection[1]));
	    if (userAndReflection.length > 2 && userAndReflection[2] != null) {
		String reflection = HtmlUtils.htmlEscape((String) userAndReflection[2]);
		responseRow.put(QaAppConstants.NOTEBOOK, reflection.replaceAll("\n", "<br>"));
	    }
	    rows.add(responseRow);
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=UTF-8");
	return responsedata.toString();
    }

    /**
     * Start to download the page that has an HTML version of the answers. Calls answersDownload
     * which forwards to the jsp to download the file.
     *
     * @throws ServletException
     */
    @RequestMapping("/getPrintAnswers")
    public String getPrintAnswers(HttpServletRequest request) throws ServletException {

	Long allUserIdValue = -1L;

	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	QaSession qaSession = qaService.getSessionById(toolSessionID);
	QaContent qaContent = qaSession.getQaContent();

	Long questionUid = WebUtil.readLongParam(request, "questionUid");
	QaQueContent question = null;
	for (QaQueContent check : qaContent.getQaQueContents()) {
	    if (check.getUid().equals(questionUid)) {
		question = check;
		break;
	    }
	}

	if (question == null) {
	    logger.error("Cannot display printable answers as we cannot find question details for toolSessionId "
		    + toolSessionID + " questionUid " + questionUid);
	    throw new ServletException("Question details missing.");
	}

	QaQuestionDTO questionDTO = new QaQuestionDTO(question);
	request.setAttribute(QaAppConstants.QUESTION_DTO, questionDTO);

	List<QaUsrResp> responses = qaService.getResponsesForTablesorter(qaContent.getQaContentId(), toolSessionID,
		questionUid, allUserIdValue, qaContent.isUseSelectLeaderToolOuput(), 1, 0,
		QaAppConstants.SORT_BY_USERNAME_ASC, null);
	request.setAttribute(QaAppConstants.RESPONSES, responses);
	request.setAttribute(QaAppConstants.ATTR_CONTENT, qaContent);

	boolean isAllowRateAnswers = qaContent.isAllowRateAnswers();
	boolean isCommentsEnabled = false;
	if (isAllowRateAnswers) {
	    Set<LearnerItemRatingCriteria> criterias = qaContent.getRatingCriterias();
	    for (LearnerItemRatingCriteria criteria : criterias) {
		if (criteria.isCommentRating()) {
		    isCommentsEnabled = true;
		    break;
		}
	    }
	}
	request.setAttribute("isCommentsEnabled", isCommentsEnabled);

	// handle rating criterias - even though we may have searched on ratings earlier we can't use the average ratings
	// calculated as they may have been averages over more than one criteria.
	Map<Long, Collection> criteriaMap = null;
	Map<Long, List<RatingCommentDTO>> commentMap = null;
	if (isAllowRateAnswers && !responses.isEmpty()) {
	    //create itemIds list
	    List<Long> itemIds = new LinkedList<>();
	    for (QaUsrResp usrResponse : responses) {
		itemIds.add(usrResponse.getResponseId());
	    }
	    List<ItemRatingDTO> itemRatingDtos = qaService.getRatingCriteriaDtos(qaContent.getQaContentId(),
		    toolSessionID, itemIds, true, allUserIdValue);
	    if (itemRatingDtos.size() > 0) {
		criteriaMap = new HashMap<>();
		commentMap = new HashMap<>();
		for (ItemRatingDTO itemRatingDto : itemRatingDtos) {
		    criteriaMap.put(itemRatingDto.getItemId(), itemRatingDto.getCriteriaDtos());
		    commentMap.put(itemRatingDto.getItemId(), itemRatingDto.getCommentDtos());
		}
	    }
	}
	request.setAttribute("criteriaMap", criteriaMap);
	request.setAttribute("commentMap", commentMap);
	request.setAttribute(QaAppConstants.ATTR_CONTENT, qaContent);

	return "monitoring/PrintAnswers";
    }

}