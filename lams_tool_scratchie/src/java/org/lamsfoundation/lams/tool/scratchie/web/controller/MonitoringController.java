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

package org.lamsfoundation.lams.tool.scratchie.web.controller;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.dto.QbStatsActivityDTO;
import org.lamsfoundation.lams.qb.service.IQbService;
import org.lamsfoundation.lams.tool.scratchie.ScratchieConstants;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.BurningQuestionItemDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.GroupSummary;
import org.lamsfoundation.lams.tool.scratchie.dto.LeaderResultsDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.OptionDTO;
import org.lamsfoundation.lams.tool.scratchie.dto.ReflectDTO;
import org.lamsfoundation.lams.tool.scratchie.model.Scratchie;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieConfigItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieItem;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieSession;
import org.lamsfoundation.lams.tool.scratchie.model.ScratchieUser;
import org.lamsfoundation.lams.tool.scratchie.service.IScratchieService;
import org.lamsfoundation.lams.tool.scratchie.util.ScratchieItemComparator;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.AlphanumComparator;
import org.lamsfoundation.lams.util.Configuration;
import org.lamsfoundation.lams.util.ConfigurationKeys;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelSheet;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
    public static Logger log = Logger.getLogger(MonitoringController.class);

    @Autowired
    private IScratchieService scratchieService;

    @Autowired
    private IQbService qbService;

    @RequestMapping("/summary")
    private String summary(HttpServletRequest request, Model model) {
	// initialize Session Map
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	model.addAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<GroupSummary> summaryList = scratchieService.getMonitoringSummary(contentId);

	Scratchie scratchie = scratchieService.getScratchieByContentId(contentId);
	Set<ScratchieUser> learners = scratchieService.getAllLeaders(contentId);

	//set SubmissionDeadline, if any
	if (scratchie.getSubmissionDeadline() != null) {
	    Date submissionDeadline = scratchie.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    model.addAttribute(ScratchieConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    model.addAttribute(ScratchieConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	// cache into sessionMap
	boolean isGroupedActivity = scratchieService.isGroupedActivity(contentId);
	sessionMap.put(ScratchieConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	sessionMap.put(ScratchieConstants.ATTR_SUMMARY_LIST, summaryList);
	sessionMap.put(ScratchieConstants.ATTR_SCRATCHIE, scratchie);
	sessionMap.put(ScratchieConstants.ATTR_LEARNERS, learners);
	sessionMap.put(ScratchieConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	sessionMap.put(ScratchieConstants.ATTR_REFLECTION_ON, scratchie.isReflectOnActivity());

	ScratchieConfigItem hideTitles = scratchieService.getConfigItem(ScratchieConfigItem.KEY_HIDE_TITLES);
	sessionMap.put(ScratchieConfigItem.KEY_HIDE_TITLES, Boolean.valueOf(hideTitles.getConfigValue()));

	// Create BurningQuestionsDtos if BurningQuestions is enabled.
	if (scratchie.isBurningQuestionsEnabled()) {
	    List<BurningQuestionItemDTO> burningQuestionItemDtos = scratchieService.getBurningQuestionDtos(scratchie,
		    null, true, true);
	    MonitoringController.setUpBurningQuestions(burningQuestionItemDtos);
	    request.setAttribute(ScratchieConstants.ATTR_BURNING_QUESTION_ITEM_DTOS, burningQuestionItemDtos);
	}

	// Create reflectList if reflection is enabled.
	if (scratchie.isReflectOnActivity()) {
	    List<ReflectDTO> reflections = scratchieService.getReflectionList(contentId);
	    sessionMap.put(ScratchieConstants.ATTR_REFLECTIONS, reflections);
	}

	Map<String, Object> modelAttributes = scratchieService.prepareStudentChoicesData(scratchie);
	model.addAllAttributes(modelAttributes);

	return "pages/monitoring/monitoring";
    }

    @RequestMapping("/itemSummary")
    private String itemSummary(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Long itemUid = WebUtil.readLongParam(request, ScratchieConstants.ATTR_ITEM_UID);
	if (itemUid.equals(-1L)) {
	    return null;
	}
	ScratchieItem item = scratchieService.getScratchieItemByUid(itemUid);
	request.setAttribute(ScratchieConstants.ATTR_ITEM, item);

	Long contentId = (Long) sessionMap.get(ScratchieConstants.ATTR_TOOL_CONTENT_ID);
	List<GroupSummary> summaryList = scratchieService.getGroupSummariesByItem(contentId, itemUid);

	// escape JS sensitive characters in option descriptions
	for (GroupSummary summary : summaryList) {
	    for (OptionDTO optionDto : summary.getOptionDtos()) {
		String escapedAnswer = StringEscapeUtils.escapeJavaScript(optionDto.getAnswer()).replace("\\r\\n",
			"<br>");
		optionDto.setAnswer(escapedAnswer);
	    }
	}

	request.setAttribute(ScratchieConstants.ATTR_SUMMARY_LIST, summaryList);
	return "pages/monitoring/parts/itemSummary";
    }

    @RequestMapping(path = "/saveUserMark", method = RequestMethod.POST)
    private String saveUserMark(HttpServletRequest request) {
	if ((request.getParameter(ScratchieConstants.PARAM_NOT_A_NUMBER) == null)
		&& !StringUtils.isEmpty(request.getParameter(ScratchieConstants.ATTR_USER_ID))
		&& !StringUtils.isEmpty(request.getParameter(ScratchieConstants.PARAM_SESSION_ID))) {

	    Long userId = WebUtil.readLongParam(request, ScratchieConstants.ATTR_USER_ID);
	    Long sessionId = WebUtil.readLongParam(request, ScratchieConstants.PARAM_SESSION_ID);
	    Integer newMark = Integer.valueOf(request.getParameter(ScratchieConstants.PARAM_MARK));
	    scratchieService.changeUserMark(userId, sessionId, newMark);
	}

	return null;
    }

    /**
     * Set Submission Deadline
     */
    @RequestMapping(path = "/setSubmissionDeadline", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    private String setSubmissionDeadline(HttpServletRequest request) {
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Scratchie scratchie = scratchieService.getScratchieByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, ScratchieConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	}
	scratchie.setSubmissionDeadline(tzSubmissionDeadline);
	scratchieService.saveOrUpdateScratchie(scratchie);

	return formattedDate;
    }

    /**
     * Exports tool results into excel.
     */
    @RequestMapping(path = "/exportExcel", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    private void exportExcel(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);
	Scratchie scratchie = (Scratchie) sessionMap.get(ScratchieConstants.ATTR_SCRATCHIE);

	List<ExcelSheet> sheets = scratchieService.exportExcel(scratchie.getContentId());

	String fileName = "scratchie_export.xlsx";
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);

	response.setContentType("application/x-download");
	response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	// set cookie that will tell JS script that export has been finished
	WebUtil.setFileDownloadTokenCookie(request, response);

	// Code to generate file and write file contents to response
	ServletOutputStream out = response.getOutputStream();
	ExcelUtil.createExcel(out, sheets, null, false);
    }

    /**
     * Get the mark summary with data arranged in bands. Can be displayed graphically or in a table.
     */
    @RequestMapping("/getMarkChartData")
    private String getMarkChartData(HttpServletRequest request, HttpServletResponse res)
	    throws IOException, ServletException {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Scratchie scratchie = (Scratchie) sessionMap.get(ScratchieConstants.ATTR_SCRATCHIE);
	List<Integer> results = null;

	if (scratchie != null) {
	    results = scratchieService.getMarksArray(scratchie.getContentId());
	}

	ObjectNode responseJSON = JsonNodeFactory.instance.objectNode();
	if (results != null) {
	    responseJSON.set("data", JsonUtil.readArray(results));
	} else {
	    responseJSON.set("data", JsonUtil.readArray(new Integer[0]));
	}

	res.setContentType("application/json;charset=utf-8");
	res.getWriter().write(responseJSON.toString());
	return null;

    }

    @RequestMapping("/statistic")
    private String statistic(HttpServletRequest request) {
	SessionMap<String, Object> sessionMap = getSessionMap(request);

	Scratchie scratchie = (Scratchie) sessionMap.get(ScratchieConstants.ATTR_SCRATCHIE);
	if (scratchie != null) {
	    LeaderResultsDTO leaderDto = scratchieService.getLeaderResultsDTOForLeaders(scratchie.getContentId());
	    sessionMap.put("leaderDto", leaderDto);

	    Set<ScratchieItem> items = new TreeSet<>(new ScratchieItemComparator());
	    items.addAll(scratchie.getScratchieItems());
	    List<QbStatsActivityDTO> qbStats = new LinkedList<>();

	    for (ScratchieItem item : items) {
		QbStatsActivityDTO questionStats = qbService.getActivityStatsByContentId(scratchie.getContentId(),
			item.getQbQuestion().getUid());
		questionStats.setQbQuestion(item.getQbQuestion());
		qbStats.add(questionStats);
	    }
	    request.setAttribute("qbStats", qbStats);
	}
	return "pages/monitoring/parts/statisticpart";
    }

    @RequestMapping(path = "/updateTimeLimit", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateTimeLimit(@RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    @RequestParam int relativeTimeLimit, @RequestParam(required = false) Long absoluteTimeLimit) {
	if (relativeTimeLimit < 0) {
	    throw new InvalidParameterException(
		    "Relative time limit must not be negative and it is " + relativeTimeLimit);
	}
	if (absoluteTimeLimit != null && relativeTimeLimit != 0) {
	    throw new InvalidParameterException(
		    "Relative time limit must not be provided when absolute time limit is set");
	}

	Scratchie scratchie = scratchieService.getScratchieByContentId(toolContentId);
	scratchie.setRelativeTimeLimit(relativeTimeLimit);
	// set time limit as seconds from start of epoch, using current server time zone
	scratchie.setAbsoluteTimeLimit(absoluteTimeLimit == null ? null
		: LocalDateTime.ofEpochSecond(absoluteTimeLimit, 0, OffsetDateTime.now().getOffset()));
	scratchieService.saveOrUpdateScratchie(scratchie);
    }

    @RequestMapping(path = "/getPossibleIndividualTimeLimitUsers", method = RequestMethod.GET)
    @ResponseBody
    public String getPossibleIndividualTimeLimitUsers(
	    @RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId,
	    @RequestParam(name = "term") String searchString) {

	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	String groupLabel = scratchieService.getMessage("monitoring.label.group") + " \"";
	for (ScratchieSession session : scratchieService.getSessionsByContentId(toolContentId)) {
	    if (session.getSessionName().toLowerCase().contains(searchString.toLowerCase())) {
		ObjectNode groupJSON = JsonNodeFactory.instance.objectNode();
		groupJSON.put("label", groupLabel + session.getSessionName() + "\"");
		groupJSON.put("value", "group-" + session.getSessionId());
		responseJSON.add(groupJSON);
	    }
	}
	return responseJSON.toString();
    }

    @RequestMapping(path = "/getExistingIndividualTimeLimitUsers", method = RequestMethod.GET)
    @ResponseBody
    public String getExistingIndividualTimeLimitUsers(
	    @RequestParam(name = AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentId) {

	String groupLabel = scratchieService.getMessage("monitoring.label.group") + " \"";
	ArrayNode responseJSON = JsonNodeFactory.instance.arrayNode();
	for (ScratchieSession session : scratchieService.getSessionsByContentId(toolContentId)) {
	    if (session.getTimeLimitAdjustment() == null) {
		continue;
	    }
	    ObjectNode userJSON = JsonNodeFactory.instance.objectNode();
	    userJSON.put("sessionId", session.getSessionId());
	    userJSON.put("adjustment", session.getTimeLimitAdjustment());
	    userJSON.put("name", groupLabel + session.getSessionName() + "\"");

	    responseJSON.add(userJSON);
	}
	return responseJSON.toString();
    }

    @RequestMapping(path = "/updateIndividualTimeLimit", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateIndividualTimeLimit(@RequestParam String itemId,
	    @RequestParam(required = false) Integer adjustment) {

	String[] itemIdParts = itemId.split("-");
	ScratchieSession session = scratchieService.getScratchieSessionBySessionId(Long.valueOf(itemIdParts[1]));
	session.setTimeLimitAdjustment(adjustment);
	scratchieService.saveOrUpdateScratchieSession(session);
    }

    @RequestMapping(path = "/displayChangeLeaderForGroupDialogFromActivity")
    public String displayChangeLeaderForGroupDialogFromActivity(
	    @RequestParam(name = AttributeNames.PARAM_TOOL_SESSION_ID) long toolSessionId) {
	// tell Change Leader dialog in Leader Selection tool which learner has already reached this activity
	String availableLearners = scratchieService.getUsersBySession(toolSessionId).stream()
		.collect(Collectors.mapping(user -> Long.toString(user.getUserId()), Collectors.joining(",")));

	return new StringBuilder("redirect:").append(Configuration.get(ConfigurationKeys.SERVER_URL))
		.append("tool/lalead11/monitoring/displayChangeLeaderForGroupDialogFromActivity.do?toolSessionId=")
		.append(toolSessionId).append("&availableLearners=").append(availableLearners).toString();
    }

    @RequestMapping(path = "/changeLeaderForGroup", method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    private void changeLeaderForGroup(@RequestParam(name = AttributeNames.PARAM_TOOL_SESSION_ID) long toolSessionId,
	    @RequestParam long leaderUserId) {
	scratchieService.changeLeaderForGroup(toolSessionId, leaderUserId);
    }

    @SuppressWarnings("unchecked")
    private SessionMap<String, Object> getSessionMap(HttpServletRequest request) {
	String sessionMapID = request.getParameter(ScratchieConstants.ATTR_SESSION_MAP_ID);
	request.setAttribute(ScratchieConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return (SessionMap<String, Object>) request.getSession().getAttribute(sessionMapID);
    }

    static void setUpBurningQuestions(List<BurningQuestionItemDTO> burningQuestionItemDtos) {
	//unescape previously escaped session names
	for (BurningQuestionItemDTO burningQuestionItemDto : burningQuestionItemDtos) {
	    List<BurningQuestionDTO> burningQuestionDtos = burningQuestionItemDto.getBurningQuestionDtos();

	    for (BurningQuestionDTO burningQuestionDto : burningQuestionItemDto.getBurningQuestionDtos()) {

		String escapedBurningQuestion = StringEscapeUtils
			.unescapeJavaScript(burningQuestionDto.getEscapedBurningQuestion());
		burningQuestionDto.setEscapedBurningQuestion(escapedBurningQuestion);

		String sessionName = StringEscapeUtils.unescapeJavaScript(burningQuestionDto.getSessionName());
		burningQuestionDto.setSessionName(sessionName);
	    }

	    Collections.sort(burningQuestionDtos, new Comparator<BurningQuestionDTO>() {
		@Override
		public int compare(BurningQuestionDTO o1, BurningQuestionDTO o2) {
		    return new AlphanumComparator().compare(o1.getSessionName(), o2.getSessionName());
		}
	    });
	}
    }
}
