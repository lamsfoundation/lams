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


package org.lamsfoundation.lams.tool.daco.web.action;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.daco.DacoConstants;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummarySessionDTO;
import org.lamsfoundation.lams.tool.daco.dto.MonitoringSummaryUserDTO;
import org.lamsfoundation.lams.tool.daco.dto.QuestionSummaryDTO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswer;
import org.lamsfoundation.lams.tool.daco.model.DacoAnswerOption;
import org.lamsfoundation.lams.tool.daco.model.DacoQuestion;
import org.lamsfoundation.lams.tool.daco.model.DacoUser;
import org.lamsfoundation.lams.tool.daco.service.IDacoService;
import org.lamsfoundation.lams.tool.daco.util.DacoExcelUtil;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.CentralConstants;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {
    public static Logger log = Logger.getLogger(MonitoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ParseException, JSONException {
	String param = mapping.getParameter();

	if (param.equals("summary")) {
	    return summary(mapping, request);
	}
	if (param.equals("getUsers")) {
	    return getUsers(mapping, form, request, response);
	}
	if (param.equals("viewReflection")) {
	    return viewReflection(mapping, request);
	}

	if (param.equals("listRecords")) {
	    return listRecords(mapping, request);
	}
	if (param.equals("changeView")) {
	    return changeView(mapping, request);
	}
	if (param.equals("getQuestionSummaries")) {
	    return getQuestionSummaries(mapping, request);
	}
	if (param.equals("statistic")) {
	    return statistic(mapping, request);
	}
	if (param.equals("exportToSpreadsheet")) {
	    return exportToSpreadsheet(request, response);
	}

	return mapping.findForward(DacoConstants.ERROR);
    }

    protected ActionForward listRecords(ActionMapping mapping, HttpServletRequest request) {
	return listRecords(mapping, request, false);
    }

    protected ActionForward changeView(ActionMapping mapping, HttpServletRequest request) {
	return listRecords(mapping, request, true);
    }

    private ActionForward listRecords(ActionMapping mapping, HttpServletRequest request, boolean changeView) {

	String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID, true);
	Long userId = WebUtil.readLongParam(request, DacoConstants.USER_ID, true);
	Integer sortOrder = WebUtil.readIntParam(request, DacoConstants.ATTR_SORT, true);
	if (sortOrder == null) {
	    sortOrder = DacoConstants.SORT_BY_NO;
	}

	sessionMap.put(DacoConstants.ATTR_MONITORING_SUMMARY,
		getDacoService().getAnswersAsRecords(toolSessionId, userId, sortOrder));
	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, toolSessionId);
	request.setAttribute(DacoConstants.ATTR_SORT, sortOrder);
	request.setAttribute(DacoConstants.USER_ID, userId);

	if (changeView) {
	    String currentView = (String) sessionMap.get(DacoConstants.ATTR_LEARNING_VIEW);
	    if (DacoConstants.LEARNING_VIEW_HORIZONTAL.equals(currentView)) {
		sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_VERTICAL);
	    } else {
		sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_HORIZONTAL);
	    }
	}

	return mapping.findForward(DacoConstants.SUCCESS);
    }

    protected ActionForward summary(ActionMapping mapping, HttpServletRequest request) {
	// initial Session Map
	IDacoService service = getDacoService();
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID, true);

	boolean newSession = sessionMapID == null || request.getSession().getAttribute(sessionMapID) == null;
	SessionMap sessionMap = null;
	if (newSession) {
	    sessionMap = new SessionMap();
	    sessionMapID = sessionMap.getSessionID();
	    request.getSession().setAttribute(sessionMapID, sessionMap);
	    sessionMap.put(DacoConstants.ATTR_LEARNING_VIEW, DacoConstants.LEARNING_VIEW_VERTICAL);
	} else {
	    sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	}

	Long contentId = sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID) == null
		? WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID)
		: (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);
	Daco daco = service.getDacoByContentId(contentId);

	List<MonitoringSummarySessionDTO> monitoringSummaryList = service.getMonitoringSummary(contentId,
		DacoConstants.MONITORING_SUMMARY_MATCH_NONE);

	Long userUid = WebUtil.readLongParam(request, DacoConstants.USER_UID, true);
	if (userUid == null) {
	    userUid = (Long) sessionMap.get(DacoConstants.USER_UID);
	    request.setAttribute(DacoConstants.ATTR_MONITORING_CURRENT_TAB, 1);
	} else {
	    request.setAttribute(DacoConstants.ATTR_MONITORING_CURRENT_TAB, 3);
	}

	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	sessionMap.put(DacoConstants.USER_UID, userUid);
	sessionMap.put(DacoConstants.PAGE_EDITABLE, !daco.isContentInUse());
	sessionMap.put(DacoConstants.ATTR_MONITORING_SUMMARY, monitoringSummaryList);

	if (newSession) {
	    boolean isGroupedActivity = service.isGroupedActivity(contentId);
	    sessionMap.put(DacoConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	    sessionMap.put(DacoConstants.ATTR_DACO, daco);
	    sessionMap.put(AttributeNames.PARAM_TOOL_CONTENT_ID, contentId);
	    sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		    WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	}
	return mapping.findForward(DacoConstants.SUCCESS);
    }

    protected ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse res) throws IOException, ServletException, JSONException {

	IDacoService service = getDacoService();
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID, true);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	Long contentId = sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID) == null
		? WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID)
		: (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer isSort1 = WebUtil.readIntParam(request, "column[0]", true);
	Integer isSort2 = WebUtil.readIntParam(request, "column[1]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = DacoConstants.SORT_BY_NO;
	if ((isSort1 != null) && isSort1.equals(0)) {
	    sorting = DacoConstants.SORT_BY_USER_NAME_ASC;

	} else if ((isSort1 != null) && isSort1.equals(1)) {
	    sorting = DacoConstants.SORT_BY_USER_NAME_DESC;

	} else if ((isSort2 != null) && isSort2.equals(0)) {
	    sorting = DacoConstants.SORT_BY_NUM_RECORDS_ASC;

	} else if ((isSort2 != null) && isSort2.equals(1)) {
	    sorting = DacoConstants.SORT_BY_NUM_RECORDS_DESC;
	}

	Daco daco = service.getDacoByContentId(contentId);

	List<Object[]> users = service.getUsersForTablesorter(sessionId, page, size, sorting, searchString,
		daco.isReflectOnActivity());

	JSONArray rows = new JSONArray();

	JSONObject responsedata = new JSONObject();
	responsedata.put("total_rows", service.getCountUsersBySession(sessionId, searchString));

	for (Object[] userAndReflection : users) {

	    JSONObject responseRow = new JSONObject();

	    DacoUser user = (DacoUser) userAndReflection[0];

	    responseRow.put(DacoConstants.USER_ID, user.getUserId());
	    responseRow.put(DacoConstants.USER_FULL_NAME, StringEscapeUtils.escapeHtml(user.getFullName()));

	    if (userAndReflection.length > 1 && userAndReflection[1] != null) {
		responseRow.put(DacoConstants.RECORD_COUNT, userAndReflection[1]);
	    } else {
		responseRow.put(DacoConstants.RECORD_COUNT, 0);
	    }

	    if (userAndReflection.length > 2 && userAndReflection[2] != null) {
		responseRow.put(DacoConstants.NOTEBOOK_ENTRY,
			StringEscapeUtils.escapeHtml((String) userAndReflection[2]));
	    }
	    rows.put(responseRow);
	}
	responsedata.put("rows", rows);
	res.setContentType("application/json;charset=utf-8");
	res.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    protected ActionForward viewReflection(ActionMapping mapping, HttpServletRequest request) {
	String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Integer userId = WebUtil.readIntParam(request, DacoConstants.USER_ID);
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	IDacoService service = getDacoService();
	DacoUser user = service.getUserByUserIdAndSessionId(userId.longValue(), sessionId);
	NotebookEntry notebookEntry = service.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		DacoConstants.TOOL_SIGNATURE, userId);

	MonitoringSummaryUserDTO userDTO = new MonitoringSummaryUserDTO(null, userId, user.getFullName(), null);
	userDTO.setReflectionEntry(notebookEntry.getEntry());
	sessionMap.put(DacoConstants.ATTR_USER, userDTO);
	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(DacoConstants.SUCCESS);
    }

    private IDacoService getDacoService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (IDacoService) wac.getBean(DacoConstants.DACO_SERVICE);
    }

    /**
     * Exports all learners' data to an Excel or CSV file.
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws JXLException
     * @throws ParseException
     */
    protected ActionForward exportToSpreadsheet(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ParseException {
	// Get required parameters
	String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Daco daco = (Daco) sessionMap.get(DacoConstants.ATTR_DACO);
	IDacoService service = getDacoService();

	// Prepare headers and column names
	String title = service.getLocalisedMessage(DacoConstants.KEY_LABEL_EXPORT_FILE_TITLE, null);
	String dateHeader = service.getLocalisedMessage(DacoConstants.KEY_LABEL_EXPORT_FILE_DATE, null);

	Set<DacoQuestion> questions = daco.getDacoQuestions();
	HashMap<Long, Integer> questionUidToSpreadsheetColumnIndex = new HashMap<Long, Integer>();
	// First two columns are "user" and date when was the answer added
	int columnIndex = 2;
	String[] columnNames = new String[questions.size() + 2];
	columnNames[0] = service.getLocalisedMessage(DacoConstants.KEY_LABEL_EXPORT_FILE_USER, null);
	columnNames[1] = service.getLocalisedMessage(DacoConstants.KEY_LABEL_EXPORT_FILE_ANSWER_DATE, null);
	for (DacoQuestion question : questions) {
	    questionUidToSpreadsheetColumnIndex.put(question.getUid(), columnIndex);
	    columnNames[columnIndex] = WebUtil.removeHTMLtags(question.getDescription());
	    columnIndex++;
	}

	// Some strings used in building cell values
	String longitudeHeader = service.getLocalisedMessage(DacoConstants.KEY_LABEL_LEARNING_LONGLAT_LONGITUDE, null);
	String longitudeUnit = service.getLocalisedMessage(DacoConstants.KEY_LABEL_LEARNING_LONGLAT_LONGITUDE_UNIT,
		null);
	String latitudeHeader = service.getLocalisedMessage(DacoConstants.KEY_LABEL_LEARNING_LONGLAT_LATITUDE, null);
	String latitudeUnit = service.getLocalisedMessage(DacoConstants.KEY_LABEL_LEARNING_LONGLAT_LATITUDE_UNIT, null);

	List<Object[]> rows = new LinkedList<Object[]>();
	// We get all sessions with all users with all their records from the given Daco content
	List<MonitoringSummarySessionDTO> monitoringSummary = service.getSummaryForExport(daco.getContentId(), null);
	// Get current user's locale to format numbers properly
	Locale monitoringUserLocale = null;
	HttpSession ss = SessionManager.getSession();
	if (ss != null) {
	    UserDTO systemUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (systemUser != null) {
		monitoringUserLocale = new Locale(systemUser.getLocaleLanguage(), systemUser.getLocaleCountry());
	    }
	}

	for (MonitoringSummarySessionDTO summarySession : monitoringSummary) {
	    // Maybe we'll need delimiter between sessions one day - here is the place to add an empty row
	    for (MonitoringSummaryUserDTO user : summarySession.getUsers()) {
		List<List<DacoAnswer>> records = user.getRecords();
		for (int rowIndex = 0; rowIndex < records.size(); rowIndex++) {
		    Object[] row = new Object[questions.size() + 2];
		    row[0] = user.getFullName();

		    List<DacoAnswer> record = records.get(rowIndex);
		    for (int answerIndex = 0; answerIndex < record.size(); answerIndex++) {
			DacoAnswer answer = record.get(answerIndex);
			// we set the date of the whole row to the latest from all the participating answers
			if (row[1] == null) {
			    row[1] = answer.getCreateDate();
			} else {
			    Date currentDate = (Date) row[1];
			    Date newDate = answer.getCreateDate();
			    if (currentDate.compareTo(newDate) < 0) {
				row[1] = newDate;
			    }
			}
			Object cell = null;
			String answerString = answer.getAnswer();
			columnIndex = questionUidToSpreadsheetColumnIndex.get(answer.getQuestion().getUid());
			// we extract answers and add them to "data" array in readable form
			switch (answer.getQuestion().getType()) {
			    case DacoConstants.QUESTION_TYPE_NUMBER:
				if (!StringUtils.isBlank(answerString)) {
				    Short fractionDigits = answer.getQuestion().getDigitsDecimal();
				    if (fractionDigits == null) {
					fractionDigits = Short.MAX_VALUE;
				    }
				    cell = NumberUtil.formatLocalisedNumber(Double.parseDouble(answerString),
					    monitoringUserLocale, fractionDigits);
				}
				break;
			    case DacoConstants.QUESTION_TYPE_DATE:
				if (!StringUtils.isBlank(answerString)) {
				    cell = DacoConstants.DEFAULT_DATE_FORMAT.parse(answerString);
				}
				break;
			    case DacoConstants.QUESTION_TYPE_CHECKBOX:
				if (!StringUtils.isBlank(answerString)) {
				    DacoQuestion question = answer.getQuestion();
				    DacoQuestion currentQuestion = question;
				    List<DacoAnswerOption> answerOptions = new LinkedList<DacoAnswerOption>(
					    question.getAnswerOptions());
				    StringBuilder cellStringBuilder = new StringBuilder();
				    // instead of number, we create a comma-separated string of chosen options
				    do {
					try {
					    int chosenAnswer = Integer.parseInt(answerString) - 1;
					    String chosenAnswerOption = answerOptions.get(chosenAnswer)
						    .getAnswerOption();
					    cellStringBuilder.append(chosenAnswerOption).append(", ");
					} catch (Exception e) {
					    log.error("exportToSpreadsheet encountered '" + e
						    + "' while parsing checkbox answer; answer was " + answerString);
					}
					answerIndex++;
					// LDEV-3648 If the checkbox is the last entry, then there won't be any more answers so don't trigger an out of bounds exception!
					if (answerIndex < record.size()) {
					    answer = record.get(answerIndex);
					    currentQuestion = answer.getQuestion();
					    answerString = answer.getAnswer();
					}
				    } while (answerIndex < record.size() && currentQuestion.equals(question));
				    // we went one answer too far, so we go back
				    answerIndex--;
				    cell = (cellStringBuilder.length() > 1 ? cellStringBuilder
					    .delete(cellStringBuilder.length() - 2, cellStringBuilder.length())
					    .toString() : cellStringBuilder.toString());
				}
				break;
			    case DacoConstants.QUESTION_TYPE_LONGLAT:
				// Both longitude and latitude go in the same cell
				if (StringUtils.isBlank(answerString)) {
				    // If longitude was not entered, then latitude also is blank, so skip the next answer
				    answerIndex++;
				} else {
				    StringBuilder cellStringBuilder = new StringBuilder(longitudeHeader).append(' ')
					    .append(answerString).append(' ').append(longitudeUnit).append("; ");
				    answerIndex++;
				    answer = record.get(answerIndex);
				    cellStringBuilder.append(latitudeHeader).append(' ').append(answer.getAnswer())
					    .append(' ').append(latitudeUnit);
				    cell = cellStringBuilder.toString();
				}
				break;
			    case DacoConstants.QUESTION_TYPE_FILE:
			    case DacoConstants.QUESTION_TYPE_IMAGE:
				if (!StringUtils.isBlank(answer.getFileName())) {
				    // Just get the file name, instead of the real file
				    cell = answer.getFileName();
				}
				break;
			    case DacoConstants.QUESTION_TYPE_RADIO:
			    case DacoConstants.QUESTION_TYPE_DROPDOWN:
				if (!StringUtils.isBlank(answerString)) {
				    List<DacoAnswerOption> answerOptions = new LinkedList<DacoAnswerOption>(
					    answer.getQuestion().getAnswerOptions());
				    try {
					int chosenAnswer = Integer.parseInt(answerString) - 1;
					cell = answerOptions.get(chosenAnswer).getAnswerOption();
				    } catch (Exception e) {
					log.error("exportToSpreadsheet encountered '" + e
						+ "' while parsing dropdown or radio answer; answer was "
						+ answerString);
				    }
				}
				break;
			    default:
				cell = answer.getAnswer();
				break;
			}
			row[columnIndex] = cell;
		    }
		    rows.add(row);
		}

	    }
	}
	// Convert from Collection to array
	Object[][] data = rows.toArray(new Object[][] {});

	// Prepare response headers
	String fileName = DacoConstants.EXPORT_TO_SPREADSHEET_FILE_NAME;
	fileName = FileUtil.encodeFilenameForDownload(request, fileName);
	response.setContentType(CentralConstants.RESPONSE_CONTENT_TYPE_DOWNLOAD);
	response.setHeader(CentralConstants.HEADER_CONTENT_DISPOSITION,
		CentralConstants.HEADER_CONTENT_ATTACHMENT + fileName);
	MonitoringAction.log.debug("Exporting to a spreadsheet tool content with UID: " + daco.getUid());
	ServletOutputStream out = response.getOutputStream();

	// Export to XLS
	String sheetName = service.getLocalisedMessage(DacoConstants.KEY_LABEL_EXPORT_FILE_SHEET, null);
	DacoExcelUtil.exportToExcel(out, sheetName, title, dateHeader, columnNames, data);

	// Return the file inside response, but not any JSP page
	return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected ActionForward getQuestionSummaries(ActionMapping mapping, HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	IDacoService service = getDacoService();

	DacoUser user;
	Long userId = WebUtil.readLongParam(request, DacoConstants.USER_ID, true);
	user = userId != null ? service.getUserByUserIdAndSessionId(userId, sessionId) : null;

	if (user != null) {
	    List<QuestionSummaryDTO> summaries = service.getQuestionSummaries(user.getUid());
	    sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, summaries);
	    Integer totalRecordCount = service.getGroupRecordCount(sessionId);
	    sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, totalRecordCount);
	    Integer userRecordCount = service.getRecordNum(userId, sessionId);
	    sessionMap.put(DacoConstants.RECORD_COUNT, userRecordCount);
	    sessionMap.put(DacoConstants.USER_FULL_NAME, user.getFullName());
	} else {
	    sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, new LinkedList<QuestionSummaryDTO>());
	    sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, 0);
	    sessionMap.put(DacoConstants.RECORD_COUNT, 0);
	    sessionMap.put(DacoConstants.USER_FULL_NAME, "");
	}

	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(DacoConstants.SUCCESS);
    }

    /**
     * Show statistics page.
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward statistic(ActionMapping mapping, HttpServletRequest request) {

	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	IDacoService service = getDacoService();

	Long contentId = sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID) == null
		? WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID)
		: (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);
	Daco daco = service.getDacoByContentId(contentId);
	List<MonitoringSummarySessionDTO> sessList = service.getSessionStatistics(daco.getUid());

	request.setAttribute(DacoConstants.ATTR_SESSION_SUMMARIES, sessList);
	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return mapping.findForward(DacoConstants.SUCCESS);
    }

}
