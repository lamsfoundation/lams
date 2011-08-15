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

package org.lamsfoundation.lams.tool.sbmt.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.dto.AuthoringDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SessionDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SubmitUserDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
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
 * @author Manpreet Minhas
 * @struts.action path="/monitoring" parameter="method" scope="request"
 *                validate="false" name="SbmtMonitoringForm"
 * 
 * @struts.action-forward name="listMark" path="/monitoring/mark/mark.jsp"
 * @struts.action-forward name="listAllMarks" path="/monitoring/mark/allmarks.jsp"
 * 
 * @struts.action-forward name="success" path="/monitoring/monitoring.jsp"
 * 
 * @struts.action-forward name="statistic" path="/monitoring/parts/statisticpart.jsp"
 * 
 * @struts.action-forward name="viewReflect" path="/monitoring/notebook.jsp"
 */
public class MonitoringAction extends LamsDispatchAction {

    public ISubmitFilesService submitFilesService;

    private class SessionComparator implements Comparator<SessionDTO> {
	public int compare(SessionDTO o1, SessionDTO o2) {
	    if (o1 != null && o2 != null) {
		return o1.getSessionName().compareTo(o2.getSessionName());
	    } else if (o1 != null) {
		return 1;
	    } else {
		return -1;
	    }
	}
    }

    /**
     * Default ActionForward for Monitor
     */
    @Override
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
	Long contentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	submitFilesService = getSubmitFilesService();

	request.setAttribute(AttributeNames.PARAM_CONTENT_FOLDER_ID, contentFolderID);

	// List userList = submitFilesService.getUsers(sessionID);
	List submitFilesSessionList = submitFilesService.getSubmitFilesSessionByContentID(contentID);
	summary(request, submitFilesSessionList);
	statistic(request, submitFilesSessionList);

	// instruction
	SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(contentID);
	// if this content does not exist, then reset the contentID to current value to keep it on HTML page.
	persistContent.setContentID(contentID);

	AuthoringDTO authorDto = new AuthoringDTO(persistContent);
	request.setAttribute(SbmtConstants.AUTHORING_DTO, authorDto);
	request.setAttribute(SbmtConstants.PAGE_EDITABLE, persistContent.isContentInUse());
	request.setAttribute(SbmtConstants.ATTR_IS_GROUPED_ACTIVITY, submitFilesService.isGroupedActivity(contentID));

	//set SubmissionDeadline, if any
	if (persistContent.getSubmissionDeadline() != null) {
	    Date submissionDeadline = persistContent.getSubmissionDeadline();
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    Date tzSubmissionDeadline = DateUtil.convertToTimeZoneFromDefault(teacherTimeZone, submissionDeadline);
	    request.setAttribute(SbmtConstants.ATTR_SUBMISSION_DEADLINE, tzSubmissionDeadline.getTime());
	}

	DynaActionForm smbtMonitoringForm = (DynaActionForm) form;
	// smbtMonitoringForm.set("currentTab", WebUtil.readStrParam(request, AttributeNames.PARAM_CURRENT_TAB,true));

	return mapping.findForward("success");
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
	Long contentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
	submitFilesService = getSubmitFilesService();

	// List userList = submitFilesService.getUsers(sessionID);
	List submitFilesSessionList = submitFilesService.getSubmitFilesSessionByContentID(contentID);
	statistic(request, submitFilesSessionList);

	return mapping.findForward("statistic");

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

	// get service then update report table
	submitFilesService = getSubmitFilesService();
	Long sessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	submitFilesService.releaseMarksForSession(sessionID);

	try {
	    response.setContentType("text/html;charset=utf-8");
	    PrintWriter out = response.getWriter();
	    SubmitFilesSession session = submitFilesService.getSessionById(sessionID);
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

    /**
     * Download submit file marks by MS Excel file format.
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
	submitFilesService = getSubmitFilesService();
	// return FileDetailsDTO list according to the given sessionID
	Map userFilesMap = submitFilesService.getFilesUploadedBySession(sessionID, request.getLocale());
	// construct Excel file format and download
	String errors = null;
	try {
	    // create an empty excel file
	    HSSFWorkbook wb = new HSSFWorkbook();
	    HSSFSheet sheet = wb.createSheet("Marks");
	    sheet.setColumnWidth(0, 5000);
	    HSSFRow row;
	    HSSFCell cell;

	    Iterator iter = userFilesMap.values().iterator();
	    Iterator dtoIter;

	    int idx = 0;

	    row = sheet.createRow(idx++);
	    cell = row.createCell(2);
	    cell.setCellValue(getMessageService().getMessage("label.learner.fileName"));

	    cell = row.createCell(3);
	    cell.setCellValue(getMessageService().getMessage("label.learner.fileDescription"));

	    cell = row.createCell(4);
	    cell.setCellValue(getMessageService().getMessage("label.learner.marks"));

	    cell = row.createCell(5);
	    cell.setCellValue(getMessageService().getMessage("label.learner.comments"));

	    while (iter.hasNext()) {
		List list = (List) iter.next();
		dtoIter = list.iterator();

		while (dtoIter.hasNext()) {
		    FileDetailsDTO dto = (FileDetailsDTO) dtoIter.next();
		    row = sheet.createRow(idx++);

		    int count = 0;

		    cell = row.createCell(count++);
		    cell.setCellValue(dto.getOwner().getFirstName() + " " + dto.getOwner().getLastName());

		    ++count;

		    sheet.setColumnWidth(count, 8000);

		    cell = row.createCell(count++);
		    cell.setCellValue(dto.getFilePath());

		    cell = row.createCell(count++);
		    cell.setCellValue(dto.getFileDescription());

		    cell = row.createCell(count++);

		    String marks = dto.getMarks();
		    cell.setCellValue(marks != null ? marks : "");

		    cell = row.createCell(count++);
		    cell.setCellValue(dto.getComments());
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
	} catch (Exception e) {
	    LamsDispatchAction.log.error(e);
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
	submitFilesService = getSubmitFilesService();
	
	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	SubmitFilesContent content = submitFilesService.getSubmitFilesContent(contentID);
	
	Long dateParameter = WebUtil.readLongParam(request, SbmtConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    UserDTO teacher = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	}
	content.setSubmissionDeadline(tzSubmissionDeadline);
	submitFilesService.saveOrUpdateContent(content);

	return null;
    }

    // **********************************************************
    // Mark udpate/view methods
    // **********************************************************
    /**
     * Display special user's marks information.
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward listMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long sessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	Integer userID = WebUtil.readIntParam(request, "userID");

	submitFilesService = getSubmitFilesService();
	// return FileDetailsDTO list according to the given userID and sessionID
	List files = submitFilesService.getFilesUploadedByUser(userID, sessionID, request.getLocale());

	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionID);
	request.setAttribute("report", files);
	return mapping.findForward("listMark");
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
    public ActionForward listAllMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	Long sessionID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID));
	submitFilesService = getSubmitFilesService();
	// return FileDetailsDTO list according to the given sessionID
	Map userFilesMap = submitFilesService.getFilesUploadedBySession(sessionID, request.getLocale());
	request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID, sessionID);
	// request.setAttribute("user",submitFilesService.getUserDetails(userID));
	request.setAttribute("reports", userFilesMap);

	return mapping.findForward("listAllMarks");

    }

    // **********************************************************
    // Private methods
    // **********************************************************

    private ISubmitFilesService getSubmitFilesService() {
	return SubmitFilesServiceProxy.getSubmitFilesService(this.getServlet().getServletContext());
    }

    /**
     * Return ResourceService bean.
     */
    private MessageService getMessageService() {
	WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet()
		.getServletContext());
	return (MessageService) wac.getBean("sbmtMessageService");
    }

    /**
     * Save file mark information into HttpRequest
     * 
     * @param request
     * @param sessionID
     * @param userID
     * @param detailID
     * @param updateMode
     */
    private void setMarkPage(HttpServletRequest request, Long sessionID, Long userID, Long detailID, String updateMode) {

    }

    /**
     * Save statistic information into request
     * 
     * @param request
     * @param submitFilesSessionList
     */
    private void statistic(HttpServletRequest request, List submitFilesSessionList) {
	Iterator it;
	Map<SessionDTO, StatisticDTO> sessionStatisticMap = new TreeMap<SessionDTO, StatisticDTO>(
		this.new SessionComparator());

	// build a map with all users in the submitFilesSessionList
	it = submitFilesSessionList.iterator();
	while (it.hasNext()) {

	    SubmitFilesSession sfs = (SubmitFilesSession) it.next();
	    Long sessionID = sfs.getSessionID();
	    String sessionName = sfs.getSessionName();

	    // return FileDetailsDTO list according to the given sessionID
	    Map userFilesMap = submitFilesService.getFilesUploadedBySession(sessionID, request.getLocale());
	    Iterator iter = userFilesMap.values().iterator();
	    Iterator dtoIter;
	    int notMarkedCount = 0;
	    int markedCount = 0;
	    while (iter.hasNext()) {
		List list = (List) iter.next();
		dtoIter = list.iterator();
		while (dtoIter.hasNext()) {
		    FileDetailsDTO dto = (FileDetailsDTO) dtoIter.next();
		    if (dto.getMarks() == null) {
			notMarkedCount++;
		    } else {
			markedCount++;
		    }
		}
	    }
	    StatisticDTO statisticDto = new StatisticDTO();
	    SessionDTO sessionDto = new SessionDTO();
	    statisticDto.setMarkedCount(markedCount);
	    statisticDto.setNotMarkedCount(notMarkedCount);
	    statisticDto.setTotalUploadedFiles(markedCount + notMarkedCount);
	    sessionDto.setSessionID(sessionID);
	    sessionDto.setSessionName(sessionName);
	    sessionStatisticMap.put(sessionDto, statisticDto);
	}

	request.setAttribute("statisticList", sessionStatisticMap);
    }

    /**
     * Save Summary information into HttpRequest.
     * 
     * @param request
     * @param submitFilesSessionList
     */
    private void summary(HttpServletRequest request, List submitFilesSessionList) {
	Map<SessionDTO, List> sessionUserMap = new TreeMap<SessionDTO, List>(this.new SessionComparator());

	// build a map with all users in the submitFilesSessionList
	Iterator it = submitFilesSessionList.iterator();
	while (it.hasNext()) {
	    SessionDTO sessionDto = new SessionDTO();
	    SubmitFilesSession sfs = (SubmitFilesSession) it.next();

	    Long sessionID = sfs.getSessionID();
	    sessionDto.setSessionID(sessionID);
	    sessionDto.setSessionName(sfs.getSessionName());

	    boolean hasReflect = sfs.getContent().isReflectOnActivity();
	    Map<SubmitUser, FileDetailsDTO> userFilesMap = submitFilesService.getFilesUploadedBySession(sessionID,
		    request.getLocale());

	    // construct LearnerDTO list
	    List<SubmitUser> userList = submitFilesService.getUsersBySession(sessionID);
	    List<SubmitUserDTO> learnerList = new ArrayList<SubmitUserDTO>();
	    for (SubmitUser user : userList) {
		SubmitUserDTO learnerDto = new SubmitUserDTO(user);
		learnerDto.setHasRefection(hasReflect);

		learnerDto.setAnyFilesMarked(false);
		List<FileDetailsDTO> files = (List<FileDetailsDTO>) userFilesMap.get(user);
		if (files != null && files.size() > 0) {
		    for (FileDetailsDTO file : files) {
			if (file.getMarks() != null && file.getMarks().trim().length() > 0) {
			    learnerDto.setAnyFilesMarked(true);
			    break;
			}
		    }
		}

		learnerList.add(learnerDto);
	    }
	    sessionUserMap.put(sessionDto, learnerList);
	}

	// request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
	request.setAttribute("sessionUserMap", sessionUserMap);
    }

    public ActionForward viewReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	Long userUid = WebUtil.readLongParam(request, SbmtConstants.ATTR_USER_UID);
	Long sessionID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_SESSION_ID);

	submitFilesService = getSubmitFilesService();
	SubmitUser userDto = submitFilesService.getUserByUid(userUid);

	submitFilesService = getSubmitFilesService();
	NotebookEntry notebookEntry = submitFilesService.getEntry(sessionID, CoreNotebookConstants.NOTEBOOK_TOOL,
		SbmtConstants.TOOL_SIGNATURE, userDto.getUserID());

	SubmitFilesSession session = submitFilesService.getSessionById(sessionID);

	SubmitUserDTO userDTO = new SubmitUserDTO(userDto);
	if (notebookEntry == null) {
	    userDTO.setFinishReflection(false);
	    userDTO.setReflect(null);
	} else {
	    userDTO.setFinishReflection(true);
	    userDTO.setReflect(notebookEntry.getEntry());
	}
	userDTO.setReflectInstrctions(session.getContent().getReflectInstructions());

	request.setAttribute("userDTO", userDTO);
	return mapping.findForward("viewReflect");
    }
}
