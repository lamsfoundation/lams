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

package org.lamsfoundation.lams.tool.forum.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.lamsfoundation.lams.tool.forum.ForumConstants;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.dto.SessionDTO;
import org.lamsfoundation.lams.tool.forum.model.Attachment;
import org.lamsfoundation.lams.tool.forum.model.Forum;
import org.lamsfoundation.lams.tool.forum.model.ForumReport;
import org.lamsfoundation.lams.tool.forum.model.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.model.ForumUser;
import org.lamsfoundation.lams.tool.forum.model.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumUserComparator;
import org.lamsfoundation.lams.tool.forum.util.ForumWebUtils;
import org.lamsfoundation.lams.tool.forum.util.MessageDTOByDateComparator;
import org.lamsfoundation.lams.tool.forum.util.SessionDTOComparator;
import org.lamsfoundation.lams.tool.forum.web.forms.ForumForm;
import org.lamsfoundation.lams.tool.forum.web.forms.MarkForm;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.excel.ExcelUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {

    private static Logger log = Logger.getLogger(MonitoringController.class);

    @Autowired
    private IForumService forumService;

    @Autowired
    @Qualifier("forumMessageService")
    private MessageService messageService;

    /**
     * The initial method for monitoring
     */
    @RequestMapping("/monitoring")
    public String init(HttpServletRequest request) {

	// set back tool content ID
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);

	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	request.setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, toolContentID);

	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	// perform the actions for all the tabs.
	summary(request);
	viewInstructions(request);
	viewActivity(request);
	// statistic(request);

	return "jsps/monitoring/monitoring";
    }

    /**
     * The initial method for monitoring. List all users according to given Content ID.
     */
    private void summary(HttpServletRequest request) {
	Long toolContentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	// create sessionMap
	SessionMap<String, Object> sessionMap = new SessionMap<>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Forum forum = forumService.getForumByContentId(toolContentId);
	sessionMap.put("forum", forum);

	List<ForumToolSession> sessions = forumService.getSessionsByContentId(toolContentId);

	Set<SessionDTO> sessionDtos = new TreeSet<>(new SessionDTOComparator());
	// build a map with all users in the forumSessionList
	for (ForumToolSession session : sessions) {
	    Long sessionId = session.getSessionId();
	    SessionDTO sessionDto = new SessionDTO(session);

	    // used for storing data for MonitoringAction.getUsers() serving tablesorter paging
	    List<MessageDTO> topics = forumService.getAllTopicsFromSession(sessionId);
	    Map<ForumUser, List<MessageDTO>> topicsByUser = getTopicsSortedByAuthor(topics);
	    sessionDto.setTopicsByUser(topicsByUser);

	    sessionDtos.add(sessionDto);
	}
	sessionMap.put(ForumConstants.ATTR_SESSION_DTOS, sessionDtos);

	// check if there is submission deadline
	Date submissionDeadline = forum.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    HttpSession ss = SessionManager.getSession();
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO learnerDto = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss.getAttribute(
		    AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    sessionMap.put(ForumConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    // use the unconverted time, as convertToStringForJSON() does the timezone conversion if needed
	    request.setAttribute(ForumConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	boolean isGroupedActivity = forumService.isGroupedActivity(toolContentId);
	sessionMap.put("isGroupedActivity", isGroupedActivity);
    }

    /**
     * Refreshes user list.
     */
    @RequestMapping("/getUsers")
    @ResponseBody
    public String getUsers(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {
	String sessionMapId = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);

	// teacher timezone
	HttpSession ss = SessionManager.getSession();
	org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss.getAttribute(
		AttributeNames.USER);
	TimeZone teacherTimeZone = teacher.getTimeZone();

	Long sessionId = WebUtil.readLongParam(request, "sessionId");

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSort1 = WebUtil.readIntParam(request, "column[0]", true);
	Integer isSort2 = WebUtil.readIntParam(request, "column[1]", true);
	Integer isSort3 = WebUtil.readIntParam(request, "column[2]", true);
	Integer isSort4 = WebUtil.readIntParam(request, "column[3]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = ForumConstants.SORT_BY_NO;
	if ((isSort1 != null) && isSort1.equals(0)) {
	    sorting = ForumConstants.SORT_BY_USER_NAME_ASC;

	} else if ((isSort1 != null) && isSort1.equals(1)) {
	    sorting = ForumConstants.SORT_BY_USER_NAME_DESC;

	} else if ((isSort2 != null) && isSort2.equals(0)) {
	    sorting = ForumConstants.SORT_BY_NUMBER_OF_POSTS_ASC;

	} else if ((isSort2 != null) && isSort2.equals(1)) {
	    sorting = ForumConstants.SORT_BY_NUMBER_OF_POSTS_DESC;

	} else if ((isSort3 != null) && isSort3.equals(0)) {
	    sorting = ForumConstants.SORT_BY_LAST_POSTING_ASC;

	} else if ((isSort3 != null) && isSort3.equals(1)) {
	    sorting = ForumConstants.SORT_BY_LAST_POSTING_DESC;

	} else if ((isSort4 != null) && isSort4.equals(0)) {
	    sorting = ForumConstants.SORT_BY_MARKED_ASC;

	} else if ((isSort4 != null) && isSort4.equals(1)) {
	    sorting = ForumConstants.SORT_BY_MARKED_DESC;
	}

	Set<SessionDTO> sessionDtos = (Set<SessionDTO>) sessionMap.get(ForumConstants.ATTR_SESSION_DTOS);
	SessionDTO currentSessionDto = null;
	for (SessionDTO sessionDto : sessionDtos) {
	    if (sessionDto.getSessionID().equals(sessionId)) {
		currentSessionDto = sessionDto;
		break;
	    }
	}
	Map<ForumUser, List<MessageDTO>> topicsByUser = currentSessionDto.getTopicsByUser();

	Forum forum = (Forum) sessionMap.get("forum");
	List<Object[]> users = forumService.getUsersForTablesorter(sessionId, page, size, sorting, searchString);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();

	ObjectNode responcedata = JsonNodeFactory.instance.objectNode();
	responcedata.put("total_rows", forumService.getCountUsersBySession(sessionId, searchString));

	for (Object[] userData : users) {

	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();

	    ForumUser user = (ForumUser) userData[0];

	    responseRow.put(ForumConstants.ATTR_USER_UID, user.getUid());
	    responseRow.put(ForumConstants.ATTR_USER_ID, user.getUserId());
	    responseRow.put("userName", HtmlUtils.htmlEscape(user.getFullName()));

	    int numberOfPosts = 0;
	    boolean isAnyPostsMarked = false;
	    if (topicsByUser.get(user) != null) {

		// sort messages by date
		TreeSet<MessageDTO> messages = new TreeSet<>(new MessageDTOByDateComparator());
		messages.addAll(topicsByUser.get(user));

		MessageDTO lastMessage = messages.last();

		// format lastEdited date
		Date lastMessageDate = lastMessage.getMessage().getUpdated();
		DateFormat dateFormatter = new SimpleDateFormat("d MMMM yyyy h:mm:ss a");
		responseRow.put("lastMessageDate", dateFormatter.format(lastMessageDate));
		responseRow.put("timeAgo", DateUtil.convertToStringForTimeagoJSON(lastMessageDate));
		numberOfPosts = messages.size();
		for (MessageDTO message : messages) {
		    if (message.getMark() != null) {
			isAnyPostsMarked = true;
			break;
		    }
		}
	    }
	    responseRow.put("anyPostsMarked", isAnyPostsMarked);
	    responseRow.put("numberOfPosts", numberOfPosts);

	    if (userData.length > 1 && userData[1] != null) {
		responseRow.put(ForumConstants.ATTR_PORTRAIT_ID, (String) userData[1]);
	    }

	    rows.add(responseRow);
	}
	responcedata.set("rows", rows);
	response.setContentType("application/json;charset=UTF-8");
	return responcedata.toString();
    }

    /**
     * Download marks for all users in a speical session.
     */
    @RequestMapping("/downloadMarks")
    public String downloadMarks(HttpServletRequest request, HttpServletResponse response) {

	Long sessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	List topicList = forumService.getAllTopicsFromSession(sessionID);
	// construct Excel file format and download
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	try {
	    // create an empty excel file
	    HSSFWorkbook wb = new HSSFWorkbook();

	    HSSFSheet sheet = wb.createSheet("Marks");
	    sheet.setColumnWidth(0, 5000);
	    HSSFRow row = null;
	    HSSFCell cell;
	    Iterator iter = getTopicsSortedByAuthor(topicList).values().iterator();
	    Iterator dtoIter;
	    boolean first = true;
	    int idx = 0;
	    int fileCount = 0;
	    while (iter.hasNext()) {
		List list = (List) iter.next();
		dtoIter = list.iterator();
		first = true;

		while (dtoIter.hasNext()) {
		    MessageDTO dto = (MessageDTO) dtoIter.next();
		    if (first) {
			first = false;
			row = sheet.createRow(0);
			cell = row.createCell(idx);
			cell.setCellValue(messageService.getMessage("lable.topic.title.subject"));
			sheet.setColumnWidth(idx, 8000);
			++idx;

			cell = row.createCell(idx);
			cell.setCellValue(messageService.getMessage("lable.topic.title.author"));
			sheet.setColumnWidth(idx, 8000);
			++idx;

			cell = row.createCell(idx);
			cell.setCellValue(messageService.getMessage("label.download.marks.heading.date"));
			sheet.setColumnWidth(idx, 8000);
			++idx;

			cell = row.createCell(idx);
			cell.setCellValue(messageService.getMessage("label.download.marks.heading.marks"));
			sheet.setColumnWidth(idx, 8000);
			++idx;

			cell = row.createCell(idx);
			cell.setCellValue(messageService.getMessage("label.download.marks.heading.comments"));
			sheet.setColumnWidth(idx, 8000);
			++idx;
		    }
		    ++fileCount;
		    idx = 0;
		    row = sheet.createRow(fileCount);
		    cell = row.createCell(idx++);
		    cell.setCellValue(dto.getMessage().getSubject());

		    cell = row.createCell(idx++);
		    cell.setCellValue(dto.getAuthor());

		    cell = row.createCell(idx++);
		    cell.setCellValue(DateFormat.getInstance().format(dto.getMessage().getCreated()));

		    cell = row.createCell(idx++);

		    if (dto.getMessage() != null && dto.getMessage().getReport() != null
			    && dto.getMessage().getReport().getMark() != null) {
			cell.setCellValue(NumberUtil.formatLocalisedNumber(dto.getMessage().getReport().getMark(),
				request.getLocale(), 2));
		    } else {
			cell.setCellValue("");
		    }

		    cell = row.createCell(idx++);
		    if (dto.getMessage() != null && dto.getMessage().getReport() != null) {
			cell.setCellValue(ExcelUtil.ensureCorrectCellLength(dto.getMessage().getReport().getComment()));
		    } else {
			cell.setCellValue("");
		    }
		}
	    }
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    wb.write(bos);
	    // construct download file response header
	    String fileName = "marks" + sessionID + ".xls";
	    String mineType = "application/vnd.ms-excel";
	    String header = "attachment; filename=\"" + fileName + "\";";
	    response.setContentType(mineType);
	    response.setHeader("Content-Disposition", header);

	    byte[] data = bos.toByteArray();
	    response.getOutputStream().write(data, 0, data.length);
	    response.getOutputStream().flush();
	} catch (IOException e) {
	    MonitoringController.log.error(e);
	    errorMap.add("GLOBAL", messageService.getMessage("monitoring.download.error", e.toString()));
	}

	if (!errorMap.isEmpty()) {
	    request.setAttribute("errorMap", errorMap);
	    request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionID);
	    return "jsps/monitoring/monitoring";
	}

	return null;
    }

    /**
     * View activity for content.
     */
    private void viewActivity(HttpServletRequest request) {
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	Forum forum = forumService.getForumByContentId(toolContentID);
	String title = forum.getTitle();
	String instruction = forum.getInstructions();

	boolean isForumEditable = ForumWebUtils.isForumEditable(forum);
	request.setAttribute(ForumConstants.PAGE_EDITABLE, new Boolean(isForumEditable));
	request.setAttribute("title", title);
	request.setAttribute("instruction", instruction);
    }

    /**
     * View instruction information for a content.
     */
    private void viewInstructions(HttpServletRequest request) {
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	Forum forum = forumService.getForumByContentId(toolContentID);
	ForumForm forumForm = new ForumForm();
	forumForm.setForum(forum);

	request.setAttribute("forumForm", forumForm);
    }

    /**
     * Performs all necessary actions for showing statistic page.
     */
    @RequestMapping("/statistic")
    public String statistic(HttpServletRequest request) {
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	Map sessionTopicsMap = new TreeMap<SessionDTO, List<MessageDTO>>(new SessionDTOComparator());
	Map sessionAvaMarkMap = new HashMap();
	Map sessionTotalMsgMap = new HashMap();

	List sessList = forumService.getSessionsByContentId(toolContentID);
	Iterator sessIter = sessList.iterator();
	while (sessIter.hasNext()) {
	    ForumToolSession session = (ForumToolSession) sessIter.next();
	    List topicList = forumService.getRootTopics(session.getSessionId());
	    Iterator iter = topicList.iterator();
	    int totalMsg = 0;
	    int msgNum;
	    float totalMsgMarkSum = 0;
	    float msgMarkSum = 0;
	    for (; iter.hasNext(); ) {
		MessageDTO msgDto = (MessageDTO) iter.next();
		// get all message under this topic
		List topicThread = forumService.getTopicThread(msgDto.getMessage().getUid());
		// loop all message under this topic
		msgMarkSum = 0;
		Iterator threadIter = topicThread.iterator();
		for (msgNum = 0; threadIter.hasNext(); msgNum++) {
		    MessageDTO dto = (MessageDTO) threadIter.next();
		    if (dto.getMark() != null) {
			msgMarkSum += dto.getMark().floatValue();
		    }
		}
		// summary to total mark
		totalMsgMarkSum += msgMarkSum;
		// set average mark to topic message DTO for display use
		msgDto.setMark(msgMarkSum / msgNum);
		totalMsg += msgNum;
	    }

	    float averMark = totalMsg == 0 ? 0 : totalMsgMarkSum / totalMsg;

	    SessionDTO sessionDto = new SessionDTO(session);

	    sessionTopicsMap.put(sessionDto, topicList);
	    sessionAvaMarkMap.put(session.getSessionId(), averMark);
	    sessionTotalMsgMap.put(session.getSessionId(), new Integer(totalMsg));
	}
	request.setAttribute("topicList", sessionTopicsMap);
	request.setAttribute("markAverage", sessionAvaMarkMap);
	request.setAttribute("totalMessage", sessionTotalMsgMap);
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID,
		WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID));

	return "jsps/monitoring/statisticpart";
    }

    /**
     * View all messages under one topic.
     */
    @RequestMapping("/viewTopicTree")
    public String viewTopicTree(HttpServletRequest request) {

	Long rootTopicId = WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID);
	// get root topic list
	List<MessageDTO> msgDtoList = forumService.getTopicThread(rootTopicId);
	request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);

	return "jsps/monitoring/message/viewtopictree";
    }

    /**
     * View topic subject, content and attachement.
     */
    @RequestMapping("/viewTopic")
    public String viewTopic(HttpServletRequest request) {

	Long msgUid = new Long(WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID));

	Message topic = forumService.getMessage(msgUid);

	request.setAttribute(ForumConstants.AUTHORING_TOPIC, MessageDTO.getMessageDTO(topic));
	return "jsps/learning/viewtopic";
    }

    @RequestMapping("/releaseMark")
    @ResponseBody
    public String releaseMark(HttpServletRequest request, HttpServletResponse response) {
	// get service then update report table

	Long sessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	forumService.releaseMarksForSession(sessionID);

	try {
	    response.setContentType("text/html;charset=utf-8");
	    PrintWriter out = response.getWriter();
	    ForumToolSession session = forumService.getSessionBySessionId(sessionID);
	    String sessionName = "";
	    if (session != null) {
		sessionName = session.getSessionName();
	    }
	    out.write(messageService.getMessage("msg.mark.released", new String[] { sessionName }));
	    out.flush();

	} catch (IOException e) {
	}
	return null;
    }

    // ==========================================================================================
    // View and update marks methods
    // ==========================================================================================

    /**
     * View a special user's mark
     */
    @RequestMapping("/viewUserMark")
    public String viewUserMark(@ModelAttribute MarkForm markForm, HttpServletRequest request) {
	Long userUid = new Long(WebUtil.readLongParam(request, ForumConstants.USER_UID));
	Long sessionId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));

	// create sessionMap
	String sessionMapId = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMapId);

	sessionMap.put(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	sessionMap.put(ForumConstants.PARAM_UPDATE_MODE, ForumConstants.MARK_UPDATE_FROM_USER);

	// get this user's all topics
	List<MessageDTO> messages = forumService.getMessagesByUserUid(userUid, sessionId);
	request.setAttribute(ForumConstants.ATTR_MESSAGES, messages);

	ForumUser user = forumService.getUser(userUid);
	request.setAttribute(ForumConstants.ATTR_USER, user);

	return "jsps/monitoring/viewmarks";
    }

    /**
     * Edit a special user's mark.
     */
    @RequestMapping("/editMark")
    public String editMark(@ModelAttribute MarkForm markForm, HttpServletRequest request) {

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(markForm.getSessionMapID());
	String updateMode = (String) sessionMap.get(ForumConstants.PARAM_UPDATE_MODE);
	// view forum mode
	if (StringUtils.isBlank(updateMode)) {
	    sessionMap.put(ForumConstants.PARAM_UPDATE_MODE, ForumConstants.MARK_UPDATE_FROM_FORUM);
	    sessionMap.put(ForumConstants.ATTR_ROOT_TOPIC_UID, markForm.getTopicID());
	}

	// get Message and User from database
	Message msg = forumService.getMessage(markForm.getTopicID());
	ForumUser user = msg.getCreatedBy();

	// echo back to web page
	if (msg.getReport() != null) {
	    if (msg.getReport().getMark() != null) {
		markForm.setMark(NumberUtil.formatLocalisedNumber(msg.getReport().getMark(), request.getLocale(), 2));
	    } else {
		markForm.setMark("");
	    }
	    markForm.setComment(msg.getReport().getComment());
	}

	// each back to web page
	request.setAttribute(ForumConstants.ATTR_TOPIC, MessageDTO.getMessageDTO(msg));
	request.setAttribute(ForumConstants.ATTR_USER, user);

	return "jsps/monitoring/updatemarks";
    }

    /**
     * Update mark for a special user
     */
    @RequestMapping(path = "/updateMark", method = RequestMethod.POST)
    public String updateMark(@ModelAttribute MarkForm markForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, markForm.getSessionMapID());
	String markStr = markForm.getMark();
	Float mark = null;
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	if (StringUtils.isBlank(markStr)) {
	    errorMap.add("GLOBAL", messageService.getMessage("error.valueReqd"));
	} else {
	    try {
		mark = NumberUtil.getLocalisedFloat(markStr, request.getLocale());
	    } catch (Exception e) {
		errorMap.add("GLOBAL", messageService.getMessage("error.mark.invalid.number"));
	    }
	}

	// echo back to web page
	Message msg = forumService.getMessage(markForm.getTopicID());
	ForumUser user = msg.getCreatedBy();

	request.setAttribute(ForumConstants.ATTR_USER, user);
	if (!errorMap.isEmpty()) {
	    // each back to web page
	    request.setAttribute(ForumConstants.ATTR_TOPIC, MessageDTO.getMessageDTO(msg));
	    request.setAttribute("errorMap", errorMap);
	    return "jsps/monitoring/updatemarks";
	}

	// update message report

	ForumReport report = msg.getReport();
	if (report == null) {
	    report = new ForumReport();
	    msg.setReport(report);
	}

	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(markForm.getSessionMapID());
	Long sessionId = (Long) sessionMap.get(AttributeNames.PARAM_TOOL_SESSION_ID);
	String updateMode = (String) sessionMap.get(ForumConstants.PARAM_UPDATE_MODE);

	report.setMark(mark);
	report.setComment(markForm.getComment());
	forumService.updateMark(msg);

	// echo back to topic list page: it depends which screen is come from: view special user mark, or view all user
	// marks.
	if (StringUtils.equals(updateMode, ForumConstants.MARK_UPDATE_FROM_USER)) {
	    List<MessageDTO> messages = forumService.getMessagesByUserUid(user.getUid(), sessionId);
	    request.setAttribute(ForumConstants.ATTR_MESSAGES, messages);
	    // listMark
	    return "jsps/monitoring/viewmarks";

	} else { // mark from view forum
	    // display root topic rather than leaf one
	    Long rootTopicId = forumService.getRootTopicId(msg.getUid());

	    String redirect = "redirect:/learning/viewTopic.do";
	    redirect = WebUtil.appendParameterToURL(redirect, ForumConstants.ATTR_SESSION_MAP_ID,
		    markForm.getSessionMapID());
	    redirect = WebUtil.appendParameterToURL(redirect, ForumConstants.ATTR_USER, user.toString());
	    redirect = WebUtil.appendParameterToURL(redirect, ForumConstants.ATTR_TOPIC_ID, rootTopicId.toString());
	    return redirect;
	}

    }

    /**
     * Set Submission Deadline
     */
    @RequestMapping(path = "/setSubmissionDeadline", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String setSubmissionDeadline(HttpServletRequest request) {
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Forum forum = forumService.getForumByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, ForumConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	String formattedDate = "";
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss.getAttribute(
		    AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	    formattedDate = DateUtil.convertToStringForJSON(tzSubmissionDeadline, request.getLocale());
	}
	forum.setSubmissionDeadline(tzSubmissionDeadline);
	forumService.updateForum(forum);

	return formattedDate;
    }

    // ==========================================================================================
    // Utility methods
    // ==========================================================================================

    private Map<ForumUser, List<MessageDTO>> getTopicsSortedByAuthor(List<MessageDTO> topics) {
	Map<ForumUser, List<MessageDTO>> topicsByUser = new TreeMap<>(new ForumUserComparator());
	for (MessageDTO topic : topics) {
	    if (topic.getMessage().getIsAuthored()) {
		continue;
	    }
	    topic.getMessage().getReport();
	    ForumUser user = (ForumUser) topic.getMessage().getCreatedBy().clone();

	    List<MessageDTO> topicsByUserExist = topicsByUser.get(user);
	    if (topicsByUserExist == null) {
		topicsByUserExist = new ArrayList<>();
		topicsByUser.put(user, topicsByUserExist);
	    }
	    topicsByUserExist.add(topic);
	}
	return topicsByUser;
    }
}