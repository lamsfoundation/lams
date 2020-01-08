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

package org.lamsfoundation.lams.tool.survey.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.lamsfoundation.lams.tool.survey.SurveyConstants;
import org.lamsfoundation.lams.tool.survey.dto.AnswerDTO;
import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.model.SurveyOption;
import org.lamsfoundation.lams.tool.survey.model.SurveyQuestion;
import org.lamsfoundation.lams.tool.survey.model.SurveySession;
import org.lamsfoundation.lams.tool.survey.model.SurveyUser;
import org.lamsfoundation.lams.tool.survey.service.ISurveyService;
import org.lamsfoundation.lams.tool.survey.util.SurveyWebUtils;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    private static final String MSG_LABEL_QUESTION = "label.question";
    private static final String MSG_LABEL_OPEN_RESPONSE = "label.open.response";
    private static final String MSG_LABEL_SESSION_NAME = "label.session.name";
    private static final String MSG_LABEL_POSSIBLE_ANSWERS = "message.possible.answers";
    private static final String MSG_LABEL_LEARNER_NAME = "monitoring.label.user.name";
    private static final String MSG_LABEL_LOGIN = "monitoring.label.user.loginname";
    private static final String MSG_LABEL_TIMESTAMP = "label.timestamp";

    public static Logger log = Logger.getLogger(MonitoringController.class);

    @Autowired
    @Qualifier("lasurvSurveyService")
    private ISurveyService surveyService;

    @Autowired
    @Qualifier("lasurvMessageService")
    private MessageService messageService;

    /**
     * Summary page action.
     */
    @RequestMapping(value = "/summary")
    private String summary(HttpServletRequest request) {
	// get session from shared session.
	HttpSession ss = SessionManager.getSession();

	// initial Session Map
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(SurveyConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());
	// save contentFolderID into session
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));

	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);

	// get summary
	SortedMap<SurveySession, List<AnswerDTO>> summary = surveyService.getSummary(contentId);

	// get survey
	Survey survey = surveyService.getSurveyByContentId(contentId);

	// get statistic
	SortedMap<SurveySession, Integer> statis = surveyService.getStatistic(contentId);

	// cache into sessionMap
	sessionMap.put(SurveyConstants.ATTR_SUMMARY_LIST, summary);
	sessionMap.put(SurveyConstants.ATTR_STATISTIC_LIST, statis);
	sessionMap.put(SurveyConstants.PAGE_EDITABLE, new Boolean(SurveyWebUtils.isSurveyEditable(survey)));
	sessionMap.put(SurveyConstants.ATTR_SURVEY, survey);
	sessionMap.put(AttributeNames.PARAM_TOOL_CONTENT_ID, contentId);
	sessionMap.put(SurveyConstants.ATTR_IS_GROUPED_ACTIVITY, surveyService.isGroupedActivity(contentId));

	// check if there is submission deadline
	Date submissionDeadline = survey.getSubmissionDeadline();

	if (submissionDeadline != null) {

	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    MonitoringController.log.info("Time:" + tzSubmissionDeadline.getTime());
	    // store submission deadline to sessionMap
	    sessionMap.put(SurveyConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	    sessionMap.put(SurveyConstants.ATTR_SUBMISSION_DEADLINE_DATESTRING,
		    DateUtil.convertToStringForJSON(submissionDeadline, request.getLocale()));
	}

	return "pages/monitoring/monitoring";
    }

    @RequestMapping(value = "/listAnswers")
    private String listAnswers(HttpServletRequest request) {
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	Long questionUid = WebUtil.readLongParam(request, SurveyConstants.ATTR_QUESTION_UID);

	// get user list
	SurveyQuestion question = surveyService.getQuestion(questionUid);
	request.setAttribute(SurveyConstants.ATTR_QUESTION, question);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	return "pages/monitoring/listanswers";
    }

    @RequestMapping(value = "/getAnswersJSON")
    private String getAnswersJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	Long questionUid = WebUtil.readLongParam(request, SurveyConstants.ATTR_QUESTION_UID);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = SurveyConstants.SORT_BY_DEAFAULT;
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? SurveyConstants.SORT_BY_NAME_ASC : SurveyConstants.SORT_BY_NAME_DESC;
	}

	// return user list according to the given sessionID
	SurveyQuestion question = surveyService.getQuestion(questionUid);
	List<Object[]> users = surveyService.getQuestionAnswersForTablesorter(sessionId, questionUid, page, size,
		sorting, searchString);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows", surveyService.getCountUsersBySession(sessionId, searchString));

	for (Object[] userAndAnswers : users) {

	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();

	    SurveyUser user = (SurveyUser) userAndAnswers[0];
	    responseRow.put(SurveyConstants.ATTR_USER_NAME,
		    HtmlUtils.htmlEscape(user.getLastName() + " " + user.getFirstName()));
	    responseRow.put(SurveyConstants.ATTR_USER_ID, user.getUserId());

	    if (userAndAnswers.length > 1 && userAndAnswers[1] != null) {
		responseRow.put("choices",
			JsonUtil.readArray(SurveyWebUtils.getChoiceList((String) userAndAnswers[1])));
	    }
	    if (userAndAnswers.length > 2 && userAndAnswers[2] != null) {
		// Data is handled differently in learner depending on whether
		// it is an extra text added
		// to a multiple choice, or a free text entry. So need to handle
		// the output differently.
		// See learner/result.jsp and its handling of question.type == 3
		// vs question.appendText
		String answer;
		if (question.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
		    // don't escape as it was escaped & BR'd before saving
		    answer = (String) userAndAnswers[2];
		} else {
		    // need to escape it, as it isn't escaped in the database
		    answer = HtmlUtils.htmlEscape((String) userAndAnswers[2]);
		    answer = answer.replaceAll("\n", "<br>");
		}
		responseRow.put("answerText", answer);
	    }
	    if (userAndAnswers.length > 3 && userAndAnswers[3] != null) {
		responseRow.put(SurveyConstants.ATTR_PORTRAIT_ID, ((Number) userAndAnswers[3]).longValue());
	    }
	    rows.add(responseRow);
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    @RequestMapping(value = "/listReflections")
    private String listReflections(HttpServletRequest request) {
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	Survey survey = surveyService.getSurveyBySessionId(sessionId);

	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	return "pages/monitoring/listreflections";
    }

    @RequestMapping(value = "/getReflectionsJSON")
    private String getReflectionsJSON(HttpServletRequest request, HttpServletResponse response) throws IOException {

	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = SurveyConstants.SORT_BY_DEAFAULT;
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? SurveyConstants.SORT_BY_NAME_ASC : SurveyConstants.SORT_BY_NAME_DESC;
	}

	// return user list according to the given sessionID
	List<Object[]> users = surveyService.getUserReflectionsForTablesorter(sessionId, page, size, sorting,
		searchString);

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	ObjectNode responsedata = JsonNodeFactory.instance.objectNode();
	responsedata.put("total_rows", surveyService.getCountUsersBySession(sessionId, searchString));

	for (Object[] userAndReflection : users) {

	    ObjectNode responseRow = JsonNodeFactory.instance.objectNode();

	    SurveyUser user = (SurveyUser) userAndReflection[0];
	    responseRow.put(SurveyConstants.ATTR_USER_NAME,
		    HtmlUtils.htmlEscape(user.getLastName() + " " + user.getFirstName()));
	    responseRow.put(SurveyConstants.ATTR_USER_ID, user.getUserId());

	    if (userAndReflection.length > 1 && userAndReflection[1] != null) {
		String reflection = HtmlUtils.htmlEscape((String) userAndReflection[1]);
		responseRow.put(SurveyConstants.ATTR_REFLECTION, reflection.replaceAll("\n", "<br>"));
	    }

	    if (userAndReflection.length > 2 && userAndReflection[2] != null) {
		responseRow.put(SurveyConstants.ATTR_PORTRAIT_ID, ((Number) userAndReflection[2]).longValue());
	    }

	    rows.add(responseRow);
	}
	responsedata.set("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    /**
     * Export Excel format survey data.
     */
    @RequestMapping(path = "/exportSurvey", method = RequestMethod.POST)
    private String exportSurvey(HttpServletRequest request, HttpServletResponse response) {
	Long toolSessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> groupList = surveyService
		.exportBySessionId(toolSessionID);
	String errors = null;
	try {
	    // create an empty excel file
	    Workbook workbook = new SXSSFWorkbook();

	    // Date format for the timestamp field
	    CellStyle dateStyle = workbook.createCellStyle();
	    dateStyle.setDataFormat((short) 0x16); // long date/time format e.g. DD/MM/YYYY MM:HH

	    Sheet sheet = workbook.createSheet("Survey");
	    sheet.setColumnWidth(0, 5000);
	    Row row;
	    Cell cell;
	    int idx = 0;
	    Set<Entry<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>>> entries = groupList.entrySet();
	    for (Entry<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> entry : entries) {
		SurveySession session = entry.getKey();
		SortedMap<SurveyQuestion, List<AnswerDTO>> map = entry.getValue();
		// display survey title, instruction and questions
		Survey survey = session.getSurvey();
		// survey title
		row = sheet.createRow(idx++);
		cell = row.createCell(0);
		cell.setCellValue(SurveyWebUtils.removeHTMLTags(survey.getTitle()));

		// survey instruction
		row = sheet.createRow(idx++);
		cell = row.createCell(0);
		cell.setCellValue(SurveyWebUtils.removeHTMLTags(survey.getInstructions()));

		// display 2 empty row
		row = sheet.createRow(idx++);
		cell = row.createCell(0);
		cell.setCellValue("");
		row = sheet.createRow(idx++);
		cell = row.createCell(0);
		cell.setCellValue("");

		// display session name
		row = sheet.createRow(idx++);
		cell = row.createCell(0);
		cell.setCellValue(messageService.getMessage(MonitoringController.MSG_LABEL_SESSION_NAME));
		cell = row.createCell(1);
		cell.setCellValue(SurveyWebUtils.removeHTMLTags(session.getSessionName()));

		// begin to display question and its answers
		Set<Entry<SurveyQuestion, List<AnswerDTO>>> questionEntries = map.entrySet();
		int questionIdx = 0;
		for (Entry<SurveyQuestion, List<AnswerDTO>> questionEntry : questionEntries) {
		    // display 1 empty row
		    row = sheet.createRow(idx++);
		    cell = row.createCell(0);
		    cell.setCellValue("");

		    questionIdx++;
		    SurveyQuestion question = questionEntry.getKey();
		    List<AnswerDTO> answers = questionEntry.getValue();

		    // display question content
		    row = sheet.createRow(idx++);
		    cell = row.createCell(0);
		    cell.setCellValue(
			    messageService.getMessage(MonitoringController.MSG_LABEL_QUESTION) + " " + questionIdx);
		    cell = row.createCell(1);
		    cell.setCellValue(SurveyWebUtils.removeHTMLTags(question.getDescription()));

		    // display options content
		    Set<SurveyOption> options = question.getOptions();

		    row = sheet.createRow(idx++);
		    cell = row.createCell(0);
		    cell.setCellValue(messageService.getMessage(MonitoringController.MSG_LABEL_POSSIBLE_ANSWERS));

		    int optionIdx = 0;
		    for (SurveyOption option : options) {
			optionIdx++;
			row = sheet.createRow(idx++);
			cell = row.createCell(0);
			cell.setCellValue(SurveyConstants.OPTION_SHORT_HEADER + optionIdx);
			cell = row.createCell(1);
			cell.setCellValue(SurveyWebUtils.removeHTMLTags(option.getDescription()));
		    }
		    if (question.isAppendText() || question.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
			optionIdx++;
			row = sheet.createRow(idx++);
			cell = row.createCell(0);
			cell.setCellValue(SurveyConstants.OPTION_SHORT_HEADER + optionIdx);
			cell = row.createCell(1);
			cell.setCellValue(messageService.getMessage(MonitoringController.MSG_LABEL_OPEN_RESPONSE));
		    }

		    // display 1 empty row
		    row = sheet.createRow(idx++);
		    cell = row.createCell(0);
		    cell.setCellValue("");

		    // //////////////////////////
		    // display answer list
		    // //////////////////////////
		    // first display option title : a1 , a2, a3 etc

		    int cellIdx = 0;
		    row = sheet.createRow(idx++);
		    cell = row.createCell(cellIdx);
		    cell.setCellValue(messageService.getMessage(MonitoringController.MSG_LABEL_LOGIN));
		    cellIdx++;
		    cell = row.createCell(cellIdx);
		    cell.setCellValue(messageService.getMessage(MonitoringController.MSG_LABEL_LEARNER_NAME));
		    cellIdx++;
		    cell = row.createCell(cellIdx);
		    cell.setCellValue(messageService.getMessage(MonitoringController.MSG_LABEL_TIMESTAMP));

		    int optionsNum = options.size();

		    int iterOpts;
		    for (iterOpts = 1; iterOpts <= optionsNum; iterOpts++) {
			cellIdx++;
			cell = row.createCell(cellIdx);
			cell.setCellValue(SurveyConstants.OPTION_SHORT_HEADER + iterOpts);
		    }

		    // display all users' answers for this question in multiple
		    // rows
		    for (AnswerDTO answer : answers) {
			row = sheet.createRow(idx++);
			cellIdx = 0;
			cell = row.createCell(cellIdx);
			cell.setCellValue(answer.getReplier().getLoginName());
			cellIdx++;
			cell = row.createCell(cellIdx);
			cell.setCellValue(
				answer.getReplier().getLastName() + ", " + answer.getReplier().getFirstName());
			cellIdx++;
			cell = row.createCell(cellIdx);
			cell.setCellStyle(dateStyle);
			cell.setCellValue(answer.getAnswer().getUpdateDate());
			// for answer's options
			for (SurveyOption option : options) {
			    cellIdx++;
			    cell = row.createCell(cellIdx);
			    if (answer.getAnswer() == null) {
				break;
			    }
			    String[] choices = answer.getAnswer().getChoices();
			    for (String choice : choices) {
				if (StringUtils.equals(choice, option.getUid().toString())) {
				    cell.setCellValue("X");
				}
			    }
			}
			// for textEntry option
			if (question.isAppendText() || question.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
			    cell = row.createCell(++cellIdx);
			    if (answer.getAnswer() != null) {
				cell.setCellValue(SurveyWebUtils.removeHTMLTags(answer.getAnswer().getAnswerText()));
			    }
			}

		    }
		}
	    }

	    // set cookie that will tell JS script that export has been finished
	    String downloadTokenValue = WebUtil.readStrParam(request, "downloadTokenValue");
	    Cookie fileDownloadTokenCookie = new Cookie("fileDownloadToken", downloadTokenValue);
	    fileDownloadTokenCookie.setPath("/");
	    response.addCookie(fileDownloadTokenCookie);

	    String fileName = "lams_survey_" + toolSessionID + ".xlsx";
	    response.setContentType("application/x-download");
	    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

	    ServletOutputStream out = response.getOutputStream();
	    workbook.write(out);
	    out.close();

	} catch (Exception e) {
	    MonitoringController.log.error(e);
	    MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	    errorMap.add("GLOBAL",
		    messageService.getMessage("error.monitoring.export.excel", new Object[] { e.toString() }));
	    errors = errorMap.toString();
	}

	if (errors != null) {
	    try {
		PrintWriter out = response.getWriter();
		out.write(errors);
		out.flush();
	    } catch (IOException e) {
	    }
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
	Survey survey = surveyService.getSurveyByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, SurveyConstants.ATTR_SUBMISSION_DEADLINE, true);
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
	survey.setSubmissionDeadline(tzSubmissionDeadline);
	surveyService.saveOrUpdateSurvey(survey);

	return formattedDate;
    }
}
