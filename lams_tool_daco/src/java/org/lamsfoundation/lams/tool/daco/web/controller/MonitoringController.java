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

package org.lamsfoundation.lams.tool.daco.web.controller;

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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import org.lamsfoundation.lams.util.CommonConstants;
import org.lamsfoundation.lams.util.FileUtil;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
@RequestMapping("/monitoring")
public class MonitoringController {
    public static Logger log = Logger.getLogger(MonitoringController.class);

    @Autowired
    private IDacoService dacoService;

    @RequestMapping("/listRecords")
    protected String listRecords(HttpServletRequest request) {
	return listRecords(request, false);
    }

    @RequestMapping("/changeView")
    protected String changeView(HttpServletRequest request) {
	return listRecords(request, true);
    }

    private String listRecords(HttpServletRequest request, boolean changeView) {

	String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Long toolSessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID, true);
	Long userId = WebUtil.readLongParam(request, DacoConstants.USER_ID, true);
	Integer sortOrder = WebUtil.readIntParam(request, DacoConstants.ATTR_SORT, true);
	if (sortOrder == null) {
	    sortOrder = DacoConstants.SORT_BY_NO;
	}

	sessionMap.put(DacoConstants.ATTR_MONITORING_SUMMARY,
		dacoService.getAnswersAsRecords(toolSessionId, userId, sortOrder));
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

	return "pages/monitoring/listRecords";
    }

    @RequestMapping("/summary")
    protected String summary(HttpServletRequest request, HttpServletResponse response) {
	// initial Session Map
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
	Daco daco = dacoService.getDacoByContentId(contentId);

	List<MonitoringSummarySessionDTO> monitoringSummaryList = dacoService.getMonitoringSummary(contentId,
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
	    boolean isGroupedActivity = dacoService.isGroupedActivity(contentId);
	    sessionMap.put(DacoConstants.ATTR_IS_GROUPED_ACTIVITY, isGroupedActivity);
	    sessionMap.put(DacoConstants.ATTR_DACO, daco);
	    sessionMap.put(AttributeNames.PARAM_TOOL_CONTENT_ID, contentId);
	    sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		    WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	}
	return "pages/monitoring/monitoring";
    }

    @RequestMapping(path = "/getUsers")
    @ResponseBody
    protected String getUsers(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ServletException {

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

	Daco daco = dacoService.getDacoByContentId(contentId);

	List<Object[]> users = dacoService.getUsersForTablesorter(sessionId, page, size, sorting, searchString,
		daco.isReflectOnActivity());

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();

	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows", dacoService.getCountUsersBySession(sessionId, searchString));

	for (Object[] userAndReflection : users) {

	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();

	    DacoUser user = (DacoUser) userAndReflection[0];

	    responseRow.put(DacoConstants.USER_ID, user.getUserId());
	    responseRow.put(DacoConstants.USER_FULL_NAME, HtmlUtils.htmlEscape(user.getFullName()));

	    if (userAndReflection.length > 1 && userAndReflection[1] != null) {
		responseRow.put(DacoConstants.RECORD_COUNT, (Integer) userAndReflection[1]);
	    } else {
		responseRow.put(DacoConstants.RECORD_COUNT, 0);
	    }

	    if (userAndReflection.length > 2 && userAndReflection[2] != null) {
		responseRow.put(DacoConstants.NOTEBOOK_ENTRY, HtmlUtils.htmlEscape((String) userAndReflection[2]));
	    }
	    if (userAndReflection.length > 3 && userAndReflection[3] != null) {
		responseRow.put(DacoConstants.PORTRAIT_ID, (String) userAndReflection[3]);
	    }
	    rows.add(responseRow);
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=UTF-8");
	return responsedata.toString();
    }

    @RequestMapping("/viewReflection")
    protected String viewReflection(HttpServletRequest request) {
	String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Integer userId = WebUtil.readIntParam(request, DacoConstants.USER_ID);
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	DacoUser user = dacoService.getUserByUserIdAndSessionId(userId.longValue(), sessionId);
	NotebookEntry notebookEntry = dacoService.getEntry(sessionId, CoreNotebookConstants.NOTEBOOK_TOOL,
		DacoConstants.TOOL_SIGNATURE, userId);

	MonitoringSummaryUserDTO userDTO = new MonitoringSummaryUserDTO(null, userId, user.getFullName(), null);
	userDTO.setReflectionEntry(notebookEntry.getEntry());
	sessionMap.put(DacoConstants.ATTR_USER, userDTO);
	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/monitoring/notebook";
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
    @RequestMapping("/exportToSpreadsheet")
    protected String exportToSpreadsheet(HttpServletRequest request, HttpServletResponse response)
	    throws IOException, ParseException {
	// Get required parameters
	String sessionMapID = request.getParameter(DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);
	Daco daco = (Daco) sessionMap.get(DacoConstants.ATTR_DACO);

	// Prepare headers and column names
	String title = dacoService.getLocalisedMessage(DacoConstants.KEY_LABEL_EXPORT_FILE_TITLE, null);
	String dateHeader = dacoService.getLocalisedMessage(DacoConstants.KEY_LABEL_EXPORT_FILE_DATE, null);

	Set<DacoQuestion> questions = daco.getDacoQuestions();
	HashMap<Long, Integer> questionUidToSpreadsheetColumnIndex = new HashMap<>();
	// First two columns are "user" and date when was the answer added
	int columnIndex = 2;
	String[] columnNames = new String[questions.size() + 2];
	columnNames[0] = dacoService.getLocalisedMessage(DacoConstants.KEY_LABEL_EXPORT_FILE_USER, null);
	columnNames[1] = dacoService.getLocalisedMessage(DacoConstants.KEY_LABEL_EXPORT_FILE_ANSWER_DATE, null);
	for (DacoQuestion question : questions) {
	    questionUidToSpreadsheetColumnIndex.put(question.getUid(), columnIndex);
	    columnNames[columnIndex] = WebUtil.removeHTMLtags(question.getDescription());
	    columnIndex++;
	}

	// Some strings used in building cell values
	String longitudeHeader = dacoService.getLocalisedMessage(DacoConstants.KEY_LABEL_LEARNING_LONGLAT_LONGITUDE,
		null);
	String longitudeUnit = dacoService.getLocalisedMessage(DacoConstants.KEY_LABEL_LEARNING_LONGLAT_LONGITUDE_UNIT,
		null);
	String latitudeHeader = dacoService.getLocalisedMessage(DacoConstants.KEY_LABEL_LEARNING_LONGLAT_LATITUDE,
		null);
	String latitudeUnit = dacoService.getLocalisedMessage(DacoConstants.KEY_LABEL_LEARNING_LONGLAT_LATITUDE_UNIT,
		null);

	List<Object[]> rows = new LinkedList<>();
	// We get all sessions with all users with all their records from the given Daco content
	List<MonitoringSummarySessionDTO> monitoringSummary = dacoService.getSummaryForExport(daco.getContentId(),
		null);
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
				    List<DacoAnswerOption> answerOptions = new LinkedList<>(
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
				    List<DacoAnswerOption> answerOptions = new LinkedList<>(
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
	response.setContentType(CommonConstants.RESPONSE_CONTENT_TYPE_DOWNLOAD);
	response.setHeader(CommonConstants.HEADER_CONTENT_DISPOSITION,
		CommonConstants.HEADER_CONTENT_ATTACHMENT + fileName);
	MonitoringController.log.debug("Exporting to a spreadsheet tool content with UID: " + daco.getUid());
	ServletOutputStream out = response.getOutputStream();

	// Export to XLS
	String sheetName = dacoService.getLocalisedMessage(DacoConstants.KEY_LABEL_EXPORT_FILE_SHEET, null);
	DacoExcelUtil.exportToExcel(out, sheetName, title, dateHeader, columnNames, data);

	// Return the file inside response, but not any JSP page
	return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping("/getQuestionSummaries")
    protected String getQuestionSummaries(HttpServletRequest request) {
	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	DacoUser user;
	Long userId = WebUtil.readLongParam(request, DacoConstants.USER_ID, true);
	user = userId != null ? dacoService.getUserByUserIdAndSessionId(userId, sessionId) : null;

	if (user != null) {
	    List<QuestionSummaryDTO> summaries = dacoService.getQuestionSummaries(user.getUid());
	    sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, summaries);
	    Integer totalRecordCount = dacoService.getGroupRecordCount(sessionId);
	    sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, totalRecordCount);
	    Integer userRecordCount = dacoService.getRecordNum(userId, sessionId);
	    sessionMap.put(DacoConstants.RECORD_COUNT, userRecordCount);
	    sessionMap.put(DacoConstants.USER_FULL_NAME, user.getFullName());
	} else {
	    sessionMap.put(DacoConstants.ATTR_QUESTION_SUMMARIES, new LinkedList<QuestionSummaryDTO>());
	    sessionMap.put(DacoConstants.ATTR_TOTAL_RECORD_COUNT, 0);
	    sessionMap.put(DacoConstants.RECORD_COUNT, 0);
	    sessionMap.put(DacoConstants.USER_FULL_NAME, "");
	}

	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/monitoring/questionSummaries";
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
    @RequestMapping("/statistic")
    private String statistic(HttpServletRequest request) {

	String sessionMapID = WebUtil.readStrParam(request, DacoConstants.ATTR_SESSION_MAP_ID);
	SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(sessionMapID);

	Long contentId = sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID) == null
		? WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID)
		: (Long) sessionMap.get(AttributeNames.PARAM_TOOL_CONTENT_ID);
	Daco daco = dacoService.getDacoByContentId(contentId);
	List<MonitoringSummarySessionDTO> sessList = dacoService.getSessionStatistics(daco.getUid());

	request.setAttribute(DacoConstants.ATTR_SESSION_SUMMARIES, sessList);
	request.setAttribute(DacoConstants.ATTR_SESSION_MAP_ID, sessionMapID);
	return "pages/monitoring/statistics";
    }

}
