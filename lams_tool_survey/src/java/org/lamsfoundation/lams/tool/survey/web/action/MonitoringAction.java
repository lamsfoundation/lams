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

/* $Id$ */
package org.lamsfoundation.lams.tool.survey.web.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
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
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MonitoringAction extends Action {

    public ISurveyService surveyService;
    private static final String MSG_LABEL_QUESTION = "label.question";
    private static final String MSG_LABEL_OPEN_RESPONSE = "label.open.response";
    private static final String MSG_LABEL_SESSION_NAME = "label.session.name";
    private static final String MSG_LABEL_POSSIBLE_ANSWERS = "message.possible.answers";
    private static final String MSG_LABEL_LEARNER = "label.learner";

    public static Logger log = Logger.getLogger(MonitoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {
	String param = mapping.getParameter();

	if (param.equals("summary")) {
	    return summary(mapping, form, request, response);
	}

	if (param.equals("listAnswers")) {
	    return listAnswers(mapping, form, request, response);
	}
	if (param.equals("getAnswersJSON")) {
	    return getAnswersJSON(mapping, form, request, response);
	}

	if (param.equals("listReflections")) {
	    return listReflections(mapping, form, request, response);
	}
	if (param.equals("getReflectionsJSON")) {
	    return getReflectionsJSON(mapping, form, request, response);
	}

	if (param.equals("exportSurvey")) {
	    return exportSurvey(mapping, form, request, response);
	}

	if (param.equals("setSubmissionDeadline")) {
	    return setSubmissionDeadline(mapping, form, request, response);
	}

	return mapping.findForward(SurveyConstants.ERROR);
    }

    /**
     * Summary page action.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

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
	ISurveyService service = getSurveyService();

	// get summary 
	SortedMap<SurveySession, List<AnswerDTO>> summary = service.getSummary(contentId);

	// get survey
	Survey survey = service.getSurveyByContentId(contentId);

	// get statistic
	SortedMap<SurveySession, Integer> statis = service.getStatistic(contentId);

	// cache into sessionMap
	sessionMap.put(SurveyConstants.ATTR_SUMMARY_LIST, summary);
	sessionMap.put(SurveyConstants.ATTR_STATISTIC_LIST, statis);
	sessionMap.put(SurveyConstants.PAGE_EDITABLE, new Boolean(SurveyWebUtils.isSurveyEditable(survey)));
	sessionMap.put(SurveyConstants.ATTR_SURVEY, survey);
	sessionMap.put(AttributeNames.PARAM_TOOL_CONTENT_ID, contentId);
	sessionMap.put(SurveyConstants.ATTR_IS_GROUPED_ACTIVITY, service.isGroupedActivity(contentId));

	// check if there is submission deadline
	Date submissionDeadline = survey.getSubmissionDeadline();

	if (submissionDeadline != null) {

	    UserDTO learnerDto = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone learnerTimeZone = learnerDto.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(learnerTimeZone, submissionDeadline);
	    MonitoringAction.log.info("Time:" + tzSubmissionDeadline.getTime());
	    // store submission deadline to sessionMap
	    sessionMap.put(SurveyConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());

	}

	return mapping.findForward(SurveyConstants.SUCCESS);
    }

    private ActionForward listAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	Long questionUid = WebUtil.readLongParam(request, SurveyConstants.ATTR_QUESTION_UID);

	// get user list
	ISurveyService service = getSurveyService();
	SurveyQuestion question = service.getQuestion(questionUid);
	request.setAttribute(SurveyConstants.ATTR_QUESTION, question);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	return mapping.findForward(SurveyConstants.SUCCESS);
    }
    private ActionForward getAnswersJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	Long questionUid = WebUtil.readLongParam(request, SurveyConstants.ATTR_QUESTION_UID);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	String searchString = request.getParameter("fcol[0]"); 
	
	int sorting = SurveyConstants.SORT_BY_DEAFAULT;
	if ( sortByName != null ) 
	    sorting = sortByName.equals(0) ? SurveyConstants.SORT_BY_NAME_ASC : SurveyConstants.SORT_BY_NAME_DESC; 

	//return user list according to the given sessionID
	ISurveyService service = getSurveyService();
	SurveyQuestion question = service.getQuestion(questionUid);
	List<Object[]> users = service.getQuestionAnswersForTablesorter(sessionId, questionUid, page, size, sorting, searchString);
	
	JSONArray rows = new JSONArray();
	JSONObject responsedata = new JSONObject();
	responsedata.put("total_rows", service.getCountUsersBySession(sessionId, searchString));

	for (Object[] userAndAnswers: users) {

	    JSONObject responseRow = new JSONObject();
	    
	    SurveyUser user = (SurveyUser) userAndAnswers[0];
	    responseRow.put(SurveyConstants.ATTR_USER_NAME, StringEscapeUtils.escapeHtml(user.getLastName() + " " + user.getFirstName()));

	    if ( userAndAnswers.length > 1 && userAndAnswers[1] != null) {
		responseRow.put("choices", SurveyWebUtils.getChoiceList((String)userAndAnswers[1]));
	    } 
	    if ( userAndAnswers.length > 2 && userAndAnswers[2] != null) {
		// Data is handled differently in learner depending on whether it is an extra text added
		// to a multiple choice, or a free text entry. So need to handle the output differently.
		// See learner/result.jsp and its handling of question.type == 3 vs question.appendText 
		String answer;
		if ( question.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY )  {
		    // don't escape as it was escaped & BR'd before saving
		    answer = (String)userAndAnswers[2];
		} else {
		    // need to escape it, as it isn't escaped in the database
		    answer = StringEscapeUtils.escapeHtml((String)userAndAnswers[2]);
		    answer = answer.replaceAll("\n", "<br>");
		}
		responseRow.put("answerText", answer);
	    } 
	    rows.put(responseRow);
	}
	responsedata.put("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    private ActionForward listReflections(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);
	
	ISurveyService service = getSurveyService();
	Survey survey = service.getSurveyBySessionId(sessionId);
	
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	return mapping.findForward(SurveyConstants.SUCCESS);
    }
    
    private ActionForward getReflectionsJSON(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {
	
	Long sessionId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	String searchString = request.getParameter("fcol[0]"); 
	
	int sorting = SurveyConstants.SORT_BY_DEAFAULT;
	if ( sortByName != null ) 
	    sorting = sortByName.equals(0) ? SurveyConstants.SORT_BY_NAME_ASC : SurveyConstants.SORT_BY_NAME_DESC; 

	//return user list according to the given sessionID
	ISurveyService service = getSurveyService();
	List<Object[]> users = service.getUserReflectionsForTablesorter(sessionId, page, size, sorting, searchString);
	
	JSONArray rows = new JSONArray();
	JSONObject responsedata = new JSONObject();
	responsedata.put("total_rows", service.getCountUsersBySession(sessionId, searchString));

	for (Object[] userAndReflection : users) {

	    JSONObject responseRow = new JSONObject();
	    
	    SurveyUser user = (SurveyUser) userAndReflection[0];
	    responseRow.put(SurveyConstants.ATTR_USER_NAME, StringEscapeUtils.escapeHtml(user.getLastName() + " " + user.getFirstName()));

	    if ( userAndReflection.length > 1 && userAndReflection[1] != null) {
		String reflection = StringEscapeUtils.escapeHtml((String)userAndReflection[1]);
		responseRow.put(SurveyConstants.ATTR_REFLECTION, reflection.replaceAll("\n", "<br>"));
	    }
		
	    rows.put(responseRow);
	}
	responsedata.put("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }
    
    /**
     * Export Excel format survey data.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    private ActionForward exportSurvey(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long toolSessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));

	ISurveyService service = getSurveyService();

	SortedMap<SurveySession, SortedMap<SurveyQuestion, List<AnswerDTO>>> groupList = service
		.exportBySessionId(toolSessionID);
	String errors = null;
	MessageService resource = getMessageService();
	try {
	    // create an empty excel file
	    Workbook workbook = new SXSSFWorkbook();
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
		cell.setCellValue(removeHTMLTags(survey.getTitle()));

		// survey instruction
		row = sheet.createRow(idx++);
		cell = row.createCell(0);
		cell.setCellValue(removeHTMLTags(survey.getInstructions()));

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
		cell.setCellValue(resource.getMessage(MonitoringAction.MSG_LABEL_SESSION_NAME));
		cell = row.createCell(1);
		cell.setCellValue(removeHTMLTags(session.getSessionName()));

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
		    cell.setCellValue(resource.getMessage(MonitoringAction.MSG_LABEL_QUESTION) + " " + questionIdx);
		    cell = row.createCell(1);
		    cell.setCellValue(removeHTMLTags(question.getDescription()));

		    // display options content
		    Set<SurveyOption> options = question.getOptions();

		    row = sheet.createRow(idx++);
		    cell = row.createCell(0);
		    cell.setCellValue(resource.getMessage(MonitoringAction.MSG_LABEL_POSSIBLE_ANSWERS));

		    int optionIdx = 0;
		    for (SurveyOption option : options) {
			optionIdx++;
			row = sheet.createRow(idx++);
			cell = row.createCell(0);
			cell.setCellValue(SurveyConstants.OPTION_SHORT_HEADER + optionIdx);
			cell = row.createCell(1);
			cell.setCellValue(removeHTMLTags(option.getDescription()));
		    }
		    if (question.isAppendText() || question.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
			optionIdx++;
			row = sheet.createRow(idx++);
			cell = row.createCell(0);
			cell.setCellValue(SurveyConstants.OPTION_SHORT_HEADER + optionIdx);
			cell = row.createCell(1);
			cell.setCellValue(resource.getMessage(MonitoringAction.MSG_LABEL_OPEN_RESPONSE));
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
		    cell.setCellValue(resource.getMessage(MonitoringAction.MSG_LABEL_LEARNER));
		    // increase one more option number if there are open entry option
		    int optionsNum = options.size();
		    if (question.isAppendText() || question.getType() == SurveyConstants.QUESTION_TYPE_TEXT_ENTRY) {
			optionsNum++;
		    }
		    for (cellIdx = 1; cellIdx <= optionsNum; cellIdx++) {
			cell = row.createCell(cellIdx);
			cell.setCellValue(SurveyConstants.OPTION_SHORT_HEADER + cellIdx);
		    }

		    // display all users' answers for this question in multiple rows
		    for (AnswerDTO answer : answers) {
			row = sheet.createRow(idx++);
			cellIdx = 0;
			cell = row.createCell(cellIdx);
			cell.setCellValue(answer.getReplier().getLoginName());
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
				cell.setCellValue(removeHTMLTags(answer.getAnswer().getAnswerText()));
			    }
			}

		    }
		}
	    } 

	    String fileName = "lams_survey_" + toolSessionID + ".xlsx";
	    response.setContentType("application/x-download");
	    response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	    
	    ServletOutputStream out = response.getOutputStream();
	    workbook.write(out);
	    out.close();
	    
	} catch (Exception e) {
	    MonitoringAction.log.error(e);
	    errors = new ActionMessage("error.monitoring.export.excel", e.toString()).toString();
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
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	surveyService = getSurveyService();

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	Survey survey = surveyService.getSurveyByContentId(contentID);

	Long dateParameter = WebUtil.readLongParam(request, SurveyConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	}
	survey.setSubmissionDeadline(tzSubmissionDeadline);
	surveyService.saveOrUpdateSurvey(survey);

	return null;
    }

    /**
     * Removes all the html tags from a string
     * 
     * @param string
     * @return
     */
    private String removeHTMLTags(String string) {
	return string.replaceAll("\\<.*?>", "").replaceAll("&nbsp;", " ");
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************
    private ISurveyService getSurveyService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (ISurveyService) wac.getBean(SurveyConstants.SURVEY_SERVICE);
    }

    /**
     * Return ResourceService bean.
     */
    private MessageService getMessageService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (MessageService) wac.getBean("lasurvMessageService");
    }
}
