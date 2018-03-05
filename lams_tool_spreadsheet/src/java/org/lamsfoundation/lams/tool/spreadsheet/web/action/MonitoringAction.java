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


package org.lamsfoundation.lams.tool.spreadsheet.web.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.tool.spreadsheet.SpreadsheetConstants;
import org.lamsfoundation.lams.tool.spreadsheet.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.spreadsheet.dto.Summary;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetMark;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetSession;
import org.lamsfoundation.lams.tool.spreadsheet.model.SpreadsheetUser;
import org.lamsfoundation.lams.tool.spreadsheet.service.ISpreadsheetService;
import org.lamsfoundation.lams.tool.spreadsheet.web.form.MarkForm;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.HtmlUtils;

public class MonitoringAction extends Action {
    public static Logger log = Logger.getLogger(MonitoringAction.class);

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, JSONException {
	String param = mapping.getParameter();

	request.setAttribute("initialTabId", WebUtil.readLongParam(request, AttributeNames.PARAM_CURRENT_TAB, true));

	if (param.equals("summary")) {
	    return summary(mapping, form, request, response);
	}
	if (param.equals("getUsers")) {
	    return getUsers(mapping, form, request, response);
	}
	if (param.equals("doStatistic")) {
	    return doStatistic(mapping, form, request, response);
	}
	if (param.equals("viewAllMarks")) {
	    return viewAllMarks(mapping, form, request, response);
	}
	if (param.equals("releaseMarks")) {
	    return releaseMarks(mapping, form, request, response);
	}
	if (param.equals("downloadMarks")) {
	    return downloadMarks(mapping, form, request, response);
	}
	if (param.equals("editMark")) {
	    return editMark(mapping, form, request, response);
	}
	if (param.equals("saveMark")) {
	    return saveMark(mapping, form, request, response);
	}
	return mapping.findForward(SpreadsheetConstants.ERROR);
    }

    private ActionForward summary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	//initial Session Map 
	SessionMap sessionMap = new SessionMap();
	request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	request.setAttribute(SpreadsheetConstants.ATTR_SESSION_MAP_ID, sessionMap.getSessionID());

	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	ISpreadsheetService service = getSpreadsheetService();
	List<Summary> summaryList = service.getSummary(contentId);

	List<StatisticDTO> statisticList = getSpreadsheetService().getStatistics(contentId);
	request.setAttribute(SpreadsheetConstants.ATTR_STATISTIC_LIST, statisticList);

	Spreadsheet spreadsheet = service.getSpreadsheetByContentId(contentId);

	//cache into sessionMap
	sessionMap.put(SpreadsheetConstants.ATTR_SUMMARY_LIST, summaryList);
	sessionMap.put(SpreadsheetConstants.PAGE_EDITABLE, spreadsheet.isContentInUse());
	sessionMap.put(SpreadsheetConstants.ATTR_RESOURCE, spreadsheet);
	sessionMap.put(SpreadsheetConstants.ATTR_TOOL_CONTENT_ID, contentId);
	sessionMap.put(AttributeNames.PARAM_CONTENT_FOLDER_ID,
		WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID));
	sessionMap.put(SpreadsheetConstants.ATTR_IS_GROUPED_ACTIVITY, service.isGroupedActivity(contentId));

	return mapping.findForward(SpreadsheetConstants.SUCCESS);
    }

    private ActionForward getUsers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws JSONException, IOException {

	Long sessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);

	// paging parameters of tablesorter
	int size = WebUtil.readIntParam(request, "size");
	int page = WebUtil.readIntParam(request, "page");
	Integer sortByName = WebUtil.readIntParam(request, "column[0]", true);
	Integer sortByMarked = WebUtil.readIntParam(request, "column[1]", true);
	String searchString = request.getParameter("fcol[0]");

	int sorting = SpreadsheetConstants.SORT_BY_NO;
	if (sortByName != null) {
	    sorting = sortByName.equals(0) ? SpreadsheetConstants.SORT_BY_USERNAME_ASC
		    : SpreadsheetConstants.SORT_BY_USERNAME_DESC;
	} else if (sortByMarked != null) {
	    sorting = sortByMarked.equals(0) ? SpreadsheetConstants.SORT_BY_MARKED_ASC
		    : SpreadsheetConstants.SORT_BY_MARKED_DESC;
	}

	//return user list according to the given sessionID
	ISpreadsheetService service = getSpreadsheetService();
	Spreadsheet spreadsheet = service.getSpreadsheetByContentId(contentId);
	List<Object[]> users = service.getUsersForTablesorter(sessionID, page, size, sorting, searchString,
		spreadsheet.isReflectOnActivity());

	JSONArray rows = new JSONArray();
	JSONObject responsedata = new JSONObject();
	responsedata.put("total_rows", service.getCountUsersBySession(sessionID, searchString));

	for (Object[] userAndReflection : users) {

	    JSONObject responseRow = new JSONObject();

	    SpreadsheetUser user = (SpreadsheetUser) userAndReflection[0];
	    responseRow.put(SpreadsheetConstants.ATTR_USER_UID, user.getUid());
	    responseRow.put(SpreadsheetConstants.ATTR_USER_ID, user.getUserId());
	    responseRow.put(SpreadsheetConstants.ATTR_USER_NAME, HtmlUtils.htmlEscape(user.getFullUsername()));
	    if (user.getUserModifiedSpreadsheet() != null) {
		responseRow.put("userModifiedSpreadsheet", "true");
		if (user.getUserModifiedSpreadsheet().getMark() != null) {
		    responseRow.put("mark",
			    NumberUtil.formatLocalisedNumber(user.getUserModifiedSpreadsheet().getMark().getMarks(),
				    request.getLocale(), SpreadsheetConstants.MARK_NUM_DEC_PLACES));
		}
	    }

	    if (userAndReflection.length > 1 && userAndReflection[1] != null) {
		responseRow.put("reflection", HtmlUtils.htmlEscape((String)userAndReflection[1]));
	    }

	    if (userAndReflection.length > 2 && userAndReflection[2] != null) {
		responseRow.put("portraitId", userAndReflection[2]);
	    }

	    rows.put(responseRow);
	}
	responsedata.put("rows", rows);
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(new String(responsedata.toString()));
	return null;
    }

    /**
     * AJAX call to refresh statistic page.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward doStatistic(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long contentId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	ISpreadsheetService service = getSpreadsheetService();

	List<StatisticDTO> statisticList = service.getStatistics(contentId);
	request.setAttribute(SpreadsheetConstants.ATTR_STATISTIC_LIST, statisticList);
	request.setAttribute(SpreadsheetConstants.ATTR_IS_GROUPED_ACTIVITY, service.isGroupedActivity(contentId));

	return mapping.findForward(SpreadsheetConstants.SUCCESS);
    }

    /**
     * View mark of all learner from same tool content ID.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward viewAllMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long sessionId = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	ISpreadsheetService service = getSpreadsheetService();

	//return FileDetailsDTO list according to the given sessionID
	List<SpreadsheetUser> userList = service.getUserListBySessionId(sessionId);
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionId);
	request.setAttribute(SpreadsheetConstants.ATTR_USER_LIST, userList);
	request.setAttribute(SpreadsheetConstants.ATTR_SESSION_MAP_ID, 
		WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_SESSION_MAP_ID));
	
	return mapping.findForward("viewAllMarks");
    }

    /**
     * Release mark
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward releaseMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	//get service then update report table
	ISpreadsheetService service = getSpreadsheetService();
	Long sessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));

	service.releaseMarksForSession(sessionID);
	try {
	    response.setContentType("text/html;charset=utf-8");
	    PrintWriter out = response.getWriter();
	    SpreadsheetSession session = service.getSessionBySessionId(sessionID);
	    String sessionName = "";
	    if (session != null) {
		sessionName = session.getSessionName();
	    }
	    out.write(service.getMessageService().getMessage("msg.mark.released", new String[] { sessionName }));
	    out.flush();
	} catch (IOException e) {
	}

	return null;
    }

    /**
     * Download Spreadsheet marks by MS Excel file format.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward downloadMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long sessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	//return user list according to the given sessionID
	ISpreadsheetService service = getSpreadsheetService();
	List<SpreadsheetUser> userList = service.getUserListBySessionId(sessionID);

	//construct Excel file format and download
	String errors = null;
	try {
	    //create an empty excel file
	    HSSFWorkbook wb = new HSSFWorkbook();
	    HSSFSheet sheet = wb.createSheet("Marks");
	    sheet.setColumnWidth(0, 5000);
	    sheet.setColumnWidth(2, 8000);
	    HSSFRow row;
	    HSSFCell cell;

	    int idx = 0;

	    row = sheet.createRow(idx++);
	    cell = row.createCell(0);
	    cell.setCellValue(service.getMessageService().getMessage("label.monitoring.downloadmarks.learner.name"));

	    cell = row.createCell(1);
	    cell.setCellValue(service.getMessageService().getMessage("label.monitoring.downloadmarks.marks"));

	    cell = row.createCell(2);
	    cell.setCellValue(service.getMessageService().getMessage("label.monitoring.downloadmarks.comments"));

	    for (SpreadsheetUser user : userList) {
		if (user.getUserModifiedSpreadsheet() != null && user.getUserModifiedSpreadsheet().getMark() != null) {
		    SpreadsheetMark mark = user.getUserModifiedSpreadsheet().getMark();
		    row = sheet.createRow(idx++);

		    int count = 0;

		    cell = row.createCell(count++);
		    cell.setCellValue(user.getLoginName());

		    //sheet.setColumnWidth(count,8000);

		    cell = row.createCell(count++);
		    if (mark.getMarks() != null) {
			cell.setCellValue(NumberUtil.formatLocalisedNumber(mark.getMarks(), request.getLocale(),
				SpreadsheetConstants.MARK_NUM_DEC_PLACES));
		    } else {
			cell.setCellValue("");
		    }

		    cell = row.createCell(count++);
		    cell.setCellValue(mark.getComments());
		}
	    }

	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    wb.write(bos);

	    //construct download file response header
	    String fileName = "marks" + sessionID + ".xls";
	    String mineType = "application/vnd.ms-excel";
	    String header = "attachment; filename=\"" + fileName + "\";";
	    response.setContentType(mineType);
	    response.setHeader("Content-Disposition", header);

	    byte[] data = bos.toByteArray();
	    response.getOutputStream().write(data, 0, data.length);
	    response.getOutputStream().flush();
	} catch (Exception e) {
	    MonitoringAction.log.error(e);
	    errors = new ActionMessage("monitoring.download.error", e.toString()).toString();
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

    public ActionForward editMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String sessionMapID = WebUtil.readStrParam(request, SpreadsheetConstants.ATTR_SESSION_MAP_ID);
	Long userUid = WebUtil.readLongParam(request, SpreadsheetConstants.ATTR_USER_UID);
	SpreadsheetUser user = getSpreadsheetService().getUser(userUid);

	//		if((user == null) || (user.getUserModifiedSpreadsheet() == null)){
	//			ActionErrors errors = new ActionErrors();
	//			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage(SpreadsheetConstants.ERROR_MSG_MARKS_BLANK));			
	//			this.addErrors(request,errors);
	//			return mapping.findForward("error");
	//		}

	MarkForm markForm = (MarkForm) form;
	markForm.setSessionMapID(sessionMapID);
	markForm.setUserUid(user.getUid());
	markForm.setUserName(user.getFullUsername());

	String code = null;
	if (user.getUserModifiedSpreadsheet() != null) {
	    markForm.setCode(user.getUserModifiedSpreadsheet().getUserModifiedSpreadsheet());

	    if (user.getUserModifiedSpreadsheet().getMark() != null) {
		SpreadsheetMark mark = user.getUserModifiedSpreadsheet().getMark();
		markForm.setMarks(NumberUtil.formatLocalisedNumber(mark.getMarks(), request.getLocale(),
			SpreadsheetConstants.MARK_NUM_DEC_PLACES));
		markForm.setComments(mark.getComments());
	    }

	}
	request.setAttribute(SpreadsheetConstants.ATTR_CODE, code);
	
	return mapping.findForward(SpreadsheetConstants.SUCCESS);
    }

    public ActionForward saveMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	MarkForm markForm = (MarkForm) form;

	Float markFloat = null;
	String markComment = null;

	// get the mark details, validating as we go.
	ActionErrors errors = new ActionErrors();
	String markStr = markForm.getMarks();
	if (StringUtils.isBlank(markStr)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(SpreadsheetConstants.ERROR_MSG_MARKS_BLANK));
	} else {
	    try {
		markFloat = NumberUtil.getLocalisedFloat(markStr, request.getLocale());
	    } catch (Exception e) {
		errors.add(ActionMessages.GLOBAL_MESSAGE,
			new ActionMessage(SpreadsheetConstants.ERROR_MSG_MARKS_INVALID_NUMBER));
	    }
	}

	markComment = markForm.getComments();
	if (StringUtils.isBlank(markComment)) {
	    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(SpreadsheetConstants.ERROR_MSG_COMMENTS_BLANK));
	}

	if (!errors.isEmpty()) {
	    this.addErrors(request, errors);
	    return mapping.findForward("editMark");
	}

	// passed validation so proceed to save
	Long userUid = markForm.getUserUid();
	SpreadsheetUser user = getSpreadsheetService().getUser(userUid);
	if (user != null && user.getUserModifiedSpreadsheet() != null) {
	    //check whether it is "edit(old item)" or "add(new item)"			
	    SpreadsheetMark mark;
	    if (user.getUserModifiedSpreadsheet().getMark() == null) { //new mark
		mark = new SpreadsheetMark();
		user.getUserModifiedSpreadsheet().setMark(mark);
	    } else { //edit
		mark = user.getUserModifiedSpreadsheet().getMark();
	    }

	    mark.setMarks(markFloat);
	    mark.setComments(markComment);

	    getSpreadsheetService().saveOrUpdateUserModifiedSpreadsheet(user.getUserModifiedSpreadsheet());
	}

	request.setAttribute("mark", NumberUtil.formatLocalisedNumber(markFloat, request.getLocale(),
		SpreadsheetConstants.MARK_NUM_DEC_PLACES)); // reduce it to the standard number of decimal places for redisplay
	request.setAttribute("userUid", userUid);

	//set session map ID so that itemlist.jsp can get sessionMAP
	request.setAttribute(SpreadsheetConstants.ATTR_SESSION_MAP_ID, markForm.getSessionMapID());

	return mapping.findForward(SpreadsheetConstants.SUCCESS);
    }

    // *************************************************************************************
    // Private method
    // *************************************************************************************

    /**
     * Save statistic information into request
     * 
     * @param request
     * @param submitFilesSessionList
     */
    private void statistic(HttpServletRequest request, List submitFilesSessionList) {
	Long contentId = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	List<StatisticDTO> statisticList = getSpreadsheetService().getStatistics(contentId);

    }

    private ISpreadsheetService getSpreadsheetService() {
	WebApplicationContext wac = WebApplicationContextUtils
		.getRequiredWebApplicationContext(getServlet().getServletContext());
	return (ISpreadsheetService) wac.getBean(SpreadsheetConstants.RESOURCE_SERVICE);
    }

}
