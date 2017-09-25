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


package org.lamsfoundation.lams.tool.notebook.web.actions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.notebook.dto.NotebookSessionsDTO;
import org.lamsfoundation.lams.tool.notebook.model.Notebook;
import org.lamsfoundation.lams.tool.notebook.model.NotebookUser;
import org.lamsfoundation.lams.tool.notebook.service.INotebookService;
import org.lamsfoundation.lams.tool.notebook.service.NotebookServiceProxy;
import org.lamsfoundation.lams.tool.notebook.util.NotebookConstants;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 *
 *
 *
 *
 */
public class MonitoringAction extends LamsDispatchAction {

    private static Logger log = Logger.getLogger(MonitoringAction.class);
    private static String noEntryText = null; // access via getNoEntryText()

    public INotebookService notebookService;

    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();
	Long toolContentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
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
	    request.setAttribute(NotebookConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING, DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	return mapping.findForward("success");
    }

    public ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {

	setupService();
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

	JSONObject responsedata = new JSONObject();
	int totalRows = notebookService.getCountUsersBySession(toolSessionId, searchString);
	responsedata.put("total_rows", totalRows);
	responsedata.put("page", page);
	responsedata.put("total", Math.ceil((float) totalRows / size));

	JSONArray rows = new JSONArray();
	// our code expects the first page to be 0 but jqgrid uses 1 for the first page.
	List<Object[]> users = notebookService.getUsersForTablesorter(toolSessionId, page > 0 ? page - 1 : 0, size,
		sorting, searchString);

	String noEntry = getNoEntryText();

	int id = 1;
	for (Object[] userAndReflection : users) {

	    JSONObject responseRow = new JSONObject();

	    NotebookUser user = (NotebookUser) userAndReflection[0];
	    responseRow.put("id", id++);
	    responseRow.put(NotebookConstants.PARAM_USER_UID, user.getUid());
	    responseRow.put(NotebookConstants.PARAM_NAME,
		    StringEscapeUtils.escapeHtml(user.getLastName() + " " + user.getFirstName()));
	    if (userAndReflection.length > 1 && userAndReflection[1] != null) {
		responseRow.put(NotebookConstants.PARAM_ENTRY, userAndReflection[1]);
	    }
	    if (user.getTeachersComment() != null && user.getTeachersComment().length() > 0) {
		responseRow.put(NotebookConstants.PARAM_COMMENT, user.getTeachersComment());
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
		responseRow.put(NotebookConstants.ATTR_PORTRAIT_ID, userAndReflection[3]);
	    }
	    rows.put(responseRow);
	}
	responsedata.put("rows", rows);
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
    public ActionForward saveTeacherComment(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	setupService();

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
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {

	setupService();

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
	response.setContentType("text/plain;charset=utf-8");
	response.getWriter().print(formattedDate);
	return null;
    }

    /** Get the statistics for monitoring */
    public ActionForward getStatistics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	setupService();
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);

	boolean isGroupedActivity = notebookService.isGroupedActivity(contentID);
	request.setAttribute("isGroupedActivity", isGroupedActivity);

	request.setAttribute("statisticList", notebookService.getStatisticsBySession(contentID));

	return mapping.findForward("statistic");
    }

    /**
     * set up notebookService
     */
    private void setupService() {
	if (notebookService == null) {
	    notebookService = NotebookServiceProxy.getNotebookService(this.getServlet().getServletContext());
	}
    }

    private String getNoEntryText() {
	if (noEntryText == null) {
	    WebApplicationContext wac = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServlet().getServletContext());
	    noEntryText = ((MessageService) wac.getBean("notebookMessageService")).getMessage("label.no.entry");
	}
	return noEntryText;
    }
}
