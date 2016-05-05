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

/* $$Id$$ */

package org.lamsfoundation.lams.tool.forum.web.actions;

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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.config.ForwardConfig;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.forum.dto.MessageDTO;
import org.lamsfoundation.lams.tool.forum.dto.SessionDTO;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumReport;
import org.lamsfoundation.lams.tool.forum.persistence.ForumToolSession;
import org.lamsfoundation.lams.tool.forum.persistence.ForumUser;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.tool.forum.service.IForumService;
import org.lamsfoundation.lams.tool.forum.util.ForumConstants;
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
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {

    private static Logger log = Logger.getLogger(MonitoringAction.class);

    private IForumService forumService;

    /**
     * Action method entry.
     */
    @Override
    public final ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	String param = mapping.getParameter();

	if (param.equals("init")) {
	    return init(mapping, form, request, response);
	}
	// refresh statistic page by Ajax call.
	if (param.equals("statistic")) {
	    return statistic(mapping, form, request, response);
	}
	if (param.equals("getUsers")) {
	    return getUsers(mapping, form, request, response);
	}

	// ***************** Marks Functions ********************
	if (param.equals("downloadMarks")) {
	    return downloadMarks(mapping, form, request, response);
	}
	if (param.equals("viewUserMark")) {
	    return viewUserMark(mapping, form, request, response);
	}
	if (param.equals("editMark")) {
	    return editMark(mapping, form, request, response);
	}
	if (param.equals("updateMark")) {
	    return updateMark(mapping, form, request, response);
	}

	if (param.equals("releaseMark")) {
	    return releaseMark(mapping, form, request, response);
	}

	// ***************** Miscellaneous ********************
	if (param.equals("viewTopic")) {
	    return viewTopic(mapping, form, request, response);
	}
	if (param.equals("viewTopicTree")) {
	    return viewTopicTree(mapping, form, request, response);
	}

	// **************** Date restriction *****************
	if (param.equals("setSubmissionDeadline")) {
	    return setSubmissionDeadline(mapping, form, request, response);
	}

	return mapping.findForward("error");
    }

    /**
     * The initial method for monitoring
     */
    private ActionForward init(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

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

	return mapping.findForward("load");
    }

    /**
     * The initial method for monitoring. List all users according to given Content ID.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private void summary(HttpServletRequest request) {
	Long toolContentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	forumService = getForumService();

	// create sessionMap
	SessionMap<String, Object> sessionMap = new SessionMap<String, Object>();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Forum forum = forumService.getForumByContentId(toolContentId);
	sessionMap.put("forum", forum);

	List<ForumToolSession> sessions = forumService.getSessionsByContentId(toolContentId);

	Set<SessionDTO> sessionDtos = new TreeSet<SessionDTO>(new SessionDTOComparator());
	// build a map with all users in the forumSessionList
	for (ForumToolSession session : sessions) {
	    Long sessionId = session.getSessionId();
	    SessionDTO sessionDto = new SessionDTO();

	    sessionDto.setSessionID(sessionId);
	    sessionDto.setSessionName(session.getSessionName());

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
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO learnerDto = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		    .getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    sessionMap.put(ForumConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	}

	boolean isGroupedActivity = forumService.isGroupedActivity(toolContentId);
	sessionMap.put("isGroupedActivity", isGroupedActivity);
    }

    /**
     * Refreshes user list.
     */
    public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {
	forumService = getForumService();
	String sessionMapId = WebUtil.readStrParam(request, ForumConstants.ATTR_SESSION_MAP_ID);
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(sessionMapId);

	// teacher timezone
	HttpSession ss = SessionManager.getSession();
	org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		.getAttribute(AttributeNames.USER);
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
	List<Object[]> users = forumService.getUsersForTablesorter(sessionId, page, size, sorting, searchString,
		forum.isReflectOnActivity());

	JSONArray rows = new JSONArray();

	JSONObject responcedata = new JSONObject();
	responcedata.put("total_rows", forumService.getCountUsersBySession(sessionId, searchString));

	for (Object[] userAndReflection : users) {

	    JSONObject responseRow = new JSONObject();

	    ForumUser user = (ForumUser) userAndReflection[0];

	    responseRow.put(ForumConstants.ATTR_USER_UID, user.getUid());
	    responseRow.put("userName", StringEscapeUtils.escapeHtml(user.getLastName() + " " + user.getFirstName()));

	    int numberOfPosts = 0;
	    boolean isAnyPostsMarked = false;
	    if (topicsByUser.get(user) != null) {

		// sort messages by date
		TreeSet<MessageDTO> messages = new TreeSet<MessageDTO>(new MessageDTOByDateComparator());
		messages.addAll(topicsByUser.get(user));

		MessageDTO lastMessage = messages.last();

		// format lastEdited date
		Date lastMessageDate = lastMessage.getMessage().getUpdated();
		lastMessageDate = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, lastMessageDate);
		DateFormat dateFormatter = new SimpleDateFormat("d MMMM yyyy h:mm:ss a");
		responseRow.put("lastMessageDate", dateFormatter.format(lastMessageDate));

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

	    if (userAndReflection.length > 1 && userAndReflection[1] != null) {
		responseRow.put("notebookEntry", StringEscapeUtils.escapeHtml((String) userAndReflection[1]));
	    }
	    rows.put(responseRow);
	}
	responcedata.put("rows", rows);
	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responcedata.toString()));
	return null;
    }

    /**
     * Download marks for all users in a speical session.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward downloadMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long sessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	forumService = getForumService();
	List topicList = forumService.getAllTopicsFromSession(sessionID);
	// construct Excel file format and download
	ActionMessages errors = new ActionMessages();
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
			cell.setCellValue(getMessageService().getMessage("lable.topic.title.subject"));
			sheet.setColumnWidth(idx, 8000);
			++idx;

			cell = row.createCell(idx);
			cell.setCellValue(getMessageService().getMessage("lable.topic.title.author"));
			sheet.setColumnWidth(idx, 8000);
			++idx;

			cell = row.createCell(idx);
			cell.setCellValue(getMessageService().getMessage("label.download.marks.heading.date"));
			sheet.setColumnWidth(idx, 8000);
			++idx;

			cell = row.createCell(idx);
			cell.setCellValue(getMessageService().getMessage("label.download.marks.heading.marks"));
			sheet.setColumnWidth(idx, 8000);
			++idx;

			cell = row.createCell(idx);
			cell.setCellValue(getMessageService().getMessage("label.download.marks.heading.comments"));
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
			cell.setCellValue(dto.getMessage().getReport().getComment());
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
	    MonitoringAction.log.error(e);
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("monitoring.download.error", e.toString()));
	}

	if (!errors.isEmpty()) {
	    saveErrors(request, errors);
	    request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionID);
	    return mapping.getInputForward();
	}

	return null;
    }

    /**
     * View activity for content.
     *
     * @param request
     */
    private void viewActivity(HttpServletRequest request) {
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	forumService = getForumService();
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
     *
     * @param request
     */
    private void viewInstructions(HttpServletRequest request) {
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	forumService = getForumService();
	Forum forum = forumService.getForumByContentId(toolContentID);
	ForumForm forumForm = new ForumForm();
	forumForm.setForum(forum);

	request.setAttribute("forumBean", forumForm);
    }

    /**
     * Show statisitc page for a session.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward statistic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	statistic(request);
	return mapping.findForward("success");
    }

    /**
     * Performs all necessary actions for showing statistic page.
     *
     * @param request
     */
    private void statistic(HttpServletRequest request) {
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));

	forumService = getForumService();
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
	    for (; iter.hasNext();) {
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

	    SessionDTO sessionDto = new SessionDTO();
	    sessionDto.setSessionID(session.getSessionId());
	    sessionDto.setSessionName(session.getSessionName());

	    sessionTopicsMap.put(sessionDto, topicList);
	    sessionAvaMarkMap.put(session.getSessionId(), averMark);
	    sessionTotalMsgMap.put(session.getSessionId(), new Integer(totalMsg));
	}
	request.setAttribute("topicList", sessionTopicsMap);
	request.setAttribute("markAverage", sessionAvaMarkMap);
	request.setAttribute("totalMessage", sessionTotalMsgMap);
    }

    /**
     * View all messages under one topic.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward viewTopicTree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long rootTopicId = WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID);
	forumService = getForumService();
	// get root topic list
	List<MessageDTO> msgDtoList = forumService.getTopicThread(rootTopicId);
	request.setAttribute(ForumConstants.AUTHORING_TOPIC_THREAD, msgDtoList);

	return mapping.findForward("success");
    }

    /**
     * View topic subject, content and attachement.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward viewTopic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long msgUid = new Long(WebUtil.readLongParam(request, ForumConstants.ATTR_TOPIC_ID));

	forumService = getForumService();
	Message topic = forumService.getMessage(msgUid);

	request.setAttribute(ForumConstants.AUTHORING_TOPIC, MessageDTO.getMessageDTO(topic));
	return mapping.findForward("success");
    }

    private ActionForward releaseMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	// get service then update report table
	forumService = getForumService();
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
	    out.write(getMessageService().getMessage("msg.mark.released", new String[] { sessionName }));
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
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward viewUserMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
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
	forumService = getForumService();
	List<MessageDTO> messages = forumService.getMessagesByUserUid(userUid, sessionId);
	request.setAttribute(ForumConstants.ATTR_MESSAGES, messages);

	ForumUser user = forumService.getUser(userUid);
	request.setAttribute(ForumConstants.ATTR_USER, user);

	return mapping.findForward("success");
    }

    /**
     * Edit a special user's mark.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward editMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MarkForm markForm = (MarkForm) form;
	SessionMap<String, Object> sessionMap = (SessionMap<String, Object>) request.getSession()
		.getAttribute(markForm.getSessionMapID());
	String updateMode = (String) sessionMap.get(ForumConstants.PARAM_UPDATE_MODE);
	// view forum mode
	if (StringUtils.isBlank(updateMode)) {
	    sessionMap.put(ForumConstants.PARAM_UPDATE_MODE, ForumConstants.MARK_UPDATE_FROM_FORUM);
	    sessionMap.put(ForumConstants.ATTR_ROOT_TOPIC_UID, markForm.getTopicID());
	}

	// get Message and User from database
	forumService = getForumService();
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

	// Should we show the reflection or not? We shouldn't show it when the View Forum screen is accessed
	// from the Monitoring Summary screen, but we should when accessed from the Learner Progress screen.
	// Need to constantly past this value on, rather than hiding just the once, as the View Forum
	// screen has a refresh button. Need to pass it through the view topic screen and dependent screens
	// as it has a link from the view topic screen back to View Forum screen.
	boolean hideReflection = WebUtil.readBooleanParam(request, ForumConstants.ATTR_HIDE_REFLECTION, false);
	sessionMap.put(ForumConstants.ATTR_HIDE_REFLECTION, hideReflection);

	return mapping.findForward("success");
    }

    /**
     * Update mark for a special user
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward updateMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MarkForm markForm = (MarkForm) form;

	request.setAttribute(ForumConstants.ATTR_SESSION_MAP_ID, markForm.getSessionMapID());
	String markStr = markForm.getMark();
	Float mark = null;
	ActionMessages errors = new ActionMessages();
	if (StringUtils.isBlank(markStr)) {
	    ActionMessage error = new ActionMessage("error.valueReqd");
	    errors.add("report.mark", error);
	} else {
	    try {
		mark = NumberUtil.getLocalisedFloat(markStr, request.getLocale());
	    } catch (Exception e) {
		ActionMessage error = new ActionMessage("error.mark.invalid.number");
		errors.add("report.mark", error);
	    }
	}

	forumService = getForumService();
	// echo back to web page
	Message msg = forumService.getMessage(markForm.getTopicID());
	ForumUser user = msg.getCreatedBy();

	request.setAttribute(ForumConstants.ATTR_USER, user);
	if (!errors.isEmpty()) {
	    // each back to web page
	    request.setAttribute(ForumConstants.ATTR_TOPIC, MessageDTO.getMessageDTO(msg));
	    saveErrors(request, errors);
	    return mapping.getInputForward();
	}

	// update message report

	forumService = getForumService();
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
	forumService.updateContainedReport(msg);

	// echo back to topic list page: it depends which screen is come from: view special user mark, or view all user
	// marks.
	if (StringUtils.equals(updateMode, ForumConstants.MARK_UPDATE_FROM_USER)) {
	    List<MessageDTO> messages = forumService.getMessagesByUserUid(user.getUid(), sessionId);
	    request.setAttribute(ForumConstants.ATTR_MESSAGES, messages);
	    // listMark
	    return mapping.findForward("success");

	} else { // mark from view forum
		     // display root topic rather than leaf one
	    Long rootTopicId = forumService.getRootTopicId(msg.getUid());

	    ForwardConfig redirectConfig = mapping.findForwardConfig("viewTopic");
	    ActionRedirect redirect = new ActionRedirect(redirectConfig);
	    redirect.addParameter(ForumConstants.ATTR_SESSION_MAP_ID, markForm.getSessionMapID());
	    redirect.addParameter(ForumConstants.ATTR_USER, user);
	    redirect.addParameter(ForumConstants.ATTR_TOPIC_ID, rootTopicId);
	    return redirect;
	}

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
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	forumService = getForumService();

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Forum forum = forumService.getForumByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, ForumConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		    .getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	}
	forum.setSubmissionDeadline(tzSubmissionDeadline);
	forumService.updateForum(forum);

	return null;
    }

    // ==========================================================================================
    // Utility methods
    // ==========================================================================================

    /**
     * Get Forum Service.
     *
     * @return
     */
    private IForumService getForumService() {
	if (forumService == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    forumService = (IForumService) wac.getBean(ForumConstants.FORUM_SERVICE);
	}
	return forumService;
    }

    /**
     * @param topics
     * @return
     */
    private Map<ForumUser, List<MessageDTO>> getTopicsSortedByAuthor(List<MessageDTO> topics) {
	Map<ForumUser, List<MessageDTO>> topicsByUser = new TreeMap<ForumUser, List<MessageDTO>>(
		new ForumUserComparator());
	for (MessageDTO topic : topics) {
	    if (topic.getMessage().getIsAuthored()) {
		continue;
	    }
	    topic.getMessage().getReport();
	    ForumUser user = (ForumUser) topic.getMessage().getCreatedBy().clone();

	    List<MessageDTO> topicsByUserExist = topicsByUser.get(user);
	    if (topicsByUserExist == null) {
		topicsByUserExist = new ArrayList<MessageDTO>();
		topicsByUser.put(user, topicsByUserExist);
	    }
	    topicsByUserExist.add(topic);
	}
	return topicsByUser;
    }

    /**
     * Return ResourceService bean.
     */
    private MessageService getMessageService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (MessageService) wac.getBean("forumMessageService");
    }
}
