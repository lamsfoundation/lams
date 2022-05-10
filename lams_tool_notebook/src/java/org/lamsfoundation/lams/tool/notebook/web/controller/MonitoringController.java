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

package org.lamsfoundation.lams.tool.notebook.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookPrintDTO;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookPrintDTO.NotebookPrintUserDTO;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookSessionsDTO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookSession;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
    private static String noEntryText = null; // access via getNoEntryText()

    @Autowired
    private INotebookService notebookService;

    @Autowired
    private IUserManagementService userManagementService;

    @Autowired
    @Qualifier("notebookMessageService")
    private MessageService messageService;

    @RequestMapping(value = "")
    public String unspecified(HttpServletRequest request) {

	Long toolContentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	request.setAttribute("contentFolderID", contentFolderID);

	Notebook notebook = notebookService.getNotebookByContentId(toolContentID);
	if (notebook == null) {
	    // TODO error page.
	}

	boolean isGroupedActivity = notebookService.isGroupedActivity(toolContentID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	NotebookSessionsDTO notebookDTO = new NotebookSessionsDTO(notebook);
	Long currentTab = WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true);
	notebookDTO.setCurrentTab(currentTab != null ? currentTab : 1);

	request.setAttribute("notebookDTO", notebookDTO);

	Date submissionDeadline = notebook.getSubmissionDeadline();
	if (submissionDeadline != null) {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(NotebookConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    request.setAttribute(NotebookConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	return "pages/monitoring/monitoring";
    }

    @RequestMapping(value = "/getUsers")
    public String getUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long toolSessionId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));

	boolean hasSearch = WebUtil.readBooleanParam(request, "_search", false);
	String searchString = hasSearch ? request.getParameter(NotebookConstants.PARAM_NAME) : null;
	int page = WebUtil.readIntParam(request, "page");
	int size = WebUtil.readIntParam(request, "rows");
	int sorting = NotebookConstants.SORT_BY_NO;
	String sidx = request.getParameter("sidx");
	String sord = request.getParameter("sord");
	if (sidx != null) {
	    if (sidx.equals(NotebookConstants.PARAM_NAME)) {
		sorting = sord != null && sord.equals(NotebookConstants.ASC) ? NotebookConstants.SORT_BY_USERNAME_ASC
			: NotebookConstants.SORT_BY_USERNAME_DESC;
	    } else if (sidx.equals(NotebookConstants.PARAM_MODIFIED_DATE)) {
		sorting = sord != null && sord.equals(NotebookConstants.ASC) ? NotebookConstants.SORT_BY_DATE_ASC
			: NotebookConstants.SORT_BY_DATE_DESC;
	    } else if (sidx.equals(NotebookConstants.PARAM_COMMENT_SORT)) {
		sorting = sord != null && sord.equals(NotebookConstants.ASC) ? NotebookConstants.SORT_BY_COMMENT_ASC
			: NotebookConstants.SORT_BY_COMMENT_DESC;
	    }
	}

	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	int totalRows = notebookService.getCountUsersBySession(toolSessionId, searchString);
	responsedata.put("total_rows", totalRows);
	responsedata.put("page", page);
	responsedata.put("total", Math.ceil((float) totalRows / size));

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	// our code expects the first page to be 0 but jqgrid uses 1 for the first page.
	List<Object[]> users = notebookService.getUsersEntriesDates(toolSessionId, page > 0 ? page - 1 : 0, size,
		sorting, searchString);

	String noEntry = getNoEntryText();

	int id = 1;
	for (Object[] userAndReflection : users) {

	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();

	    NotebookUser user = (NotebookUser) userAndReflection[0];
	    responseRow.put("id", id++);
	    responseRow.put(NotebookConstants.PARAM_USER_UID, user.getUid());
	    responseRow.put(NotebookConstants.PARAM_NAME,
		    HtmlUtils.htmlEscape(user.getLastName() + " " + user.getFirstName()));
	    if (userAndReflection.length > 1 && userAndReflection[1] != null) {
		responseRow.put(NotebookConstants.PARAM_ENTRY, HtmlUtils.htmlEscape((String) userAndReflection[1]));
	    }
	    if (user.getTeachersComment() != null && user.getTeachersComment().length() > 0) {
		responseRow.put(NotebookConstants.PARAM_COMMENT, HtmlUtils.htmlEscape(user.getTeachersComment()));
	    }

	    if (userAndReflection.length > 2 && userAndReflection[2] != null) {
		Date modifiedDate = (Date) userAndReflection[2];
		responseRow.put(NotebookConstants.PARAM_MODIFIED_DATE,
			DateUtil.convertToStringForJSON(modifiedDate, request.getLocale()));
		responseRow.put(NotebookConstants.PARAM_MODIFIED_DATE_TIMEAGO,
			DateUtil.convertToStringForTimeagoJSON(modifiedDate));
	    } else {
		responseRow.put(NotebookConstants.PARAM_MODIFIED_DATE, noEntry);
	    }

	    responseRow.put(NotebookConstants.ATTR_USER_ID, user.getUserId());
	    if (userAndReflection.length > 3 && userAndReflection[3] != null) {
		responseRow.put(NotebookConstants.ATTR_PORTRAIT_ID, (String) userAndReflection[3]);
	    }
	    rows.add(responseRow);
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(responsedata.toString());
	return null;

    }

    /**
     * Updates a user's mark or feedback for an entire lesson
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/saveTeacherComment", method = RequestMethod.POST)
    public String saveTeacherComment(HttpServletRequest request) throws Exception {

	String teachersComment = WebUtil.readStrParam(request, "value", true);
	Long userUid = WebUtil.readLongParam(request, NotebookConstants.PARAM_USER_UID);
	boolean isNotifyLearner = WebUtil.readBooleanParam(request, "isNotifyLearner");
	NotebookUser user = notebookService.getUserByUID(userUid);

	//check user had available notebook entry and teachersComment is not blank
	if ((user.getEntryUID() == null) && StringUtils.isNotBlank(teachersComment)) {
	    return null;
	}

	user.setTeachersComment(teachersComment);
	notebookService.saveOrUpdateNotebookUser(user);

	if (isNotifyLearner) {
	    notebookService.notifyUser(user.getUserId().intValue(), teachersComment);
	}

	return null;
    }

    /**
     * Set Submission Deadline
     */
    @RequestMapping(path = "/setSubmissionDeadline", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String setSubmissionDeadline(HttpServletRequest request) {
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Notebook notebook = notebookService.getNotebookByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, NotebookConstants.ATTR_SUBMISSION_DEADLINE, true);
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
	notebook.setSubmissionDeadline(tzSubmissionDeadline);
	notebookService.saveOrUpdateNotebook(notebook);

	return formattedDate;
    }

    /** Get the statistics for monitoring */

    @RequestMapping(value = "/getStatistics")
    public String getStatistics(HttpServletRequest request) {

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);

	boolean isGroupedActivity = notebookService.isGroupedActivity(contentID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	request.setAttribute("statisticList", notebookService.getStatisticsBySession(contentID));

	return "pages/monitoring/statisticpart";
    }

    @RequestMapping("/showPrintDialog")
    public String showPrintDialog(@RequestParam(AttributeNames.PARAM_TOOL_CONTENT_ID) long toolContentID, Model model) {
	Notebook notebook = notebookService.getNotebookByContentId(toolContentID);
	NotebookPrintDTO printDTO = new NotebookPrintDTO();
	printDTO.setTitle(notebook.getTitle());
	printDTO.setInstructions(notebook.getInstructions());
	printDTO.setAllowRichEditor(notebook.isAllowRichEditor());

	boolean isGroupedActivity = notebookService.isGroupedActivity(toolContentID);
	printDTO.setGroupedActivity(isGroupedActivity);

	for (NotebookSession session : notebook.getNotebookSessions()) {
	    List<Object[]> users = notebookService.getUsersEntriesDates(session.getSessionId(), null, null,
		    NotebookConstants.SORT_BY_USERNAME_ASC, null);
	    List<NotebookPrintUserDTO> printUserDTOs = new LinkedList<>();
	    for (Object[] userData : users) {
		NotebookUser user = (NotebookUser) userData[0];
		NotebookPrintUserDTO printUserDTO = new NotebookPrintUserDTO();
		printUserDTO.setFirstName(user.getFirstName());
		printUserDTO.setLastName(user.getLastName());
		printUserDTO.setEmail(userManagementService.getUserById(user.getUserId().intValue()).getEmail());
		printUserDTO.setEntry((String) userData[1]);
		printUserDTO.setEntryModifiedDate((Date) userData[2]);
		printUserDTO.setTeacherComment(user.getTeachersComment());
		printUserDTOs.add(printUserDTO);
	    }

	    printDTO.getUsersBySession().put(session.getSessionName(), printUserDTOs);
	}
	model.addAttribute("printDTO", printDTO);

	return "pages/monitoring/print";
    }

    /**
     * set up notebookService
     */

    private String getNoEntryText() {
	if (noEntryText == null) {
	    noEntryText = messageService.getMessage("label.no.entry");
	}
	return noEntryText;
    }

}
