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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dto.AuthoringDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.SessionDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;


/**
 * @author Manpreet Minhas
 * @struts.action
 * 				path="/monitoring"
 * 				parameter="method"
 * 				scope="request"
 * 				name="SbmtMonitoringForm" 				
 * 
 * @struts.action-forward name="userlist" path="/monitoring/monitoring.jsp"
 * @struts.action-forward name="userMarks" path="/monitoring/monitoring.jsp"
 * @struts.action-forward name="updateMarks" path="/monitoring/monitoring.jsp"
 * @struts.action-forward name="allUserMarks" path="/monitoring/monitoring.jsp"
 * 
 * @struts.action-forward name="instructions" path="/monitoring/monitoring.jsp"
 * @struts.action-forward name="showActivity" path="/monitoring/monitoring.jsp"
 * @struts.action-forward name="editActivity" path="/monitoring/monitoring.jsp"
 * @struts.action-forward name="success" path="/monitoring/monitoring.jsp"
 * @struts.action-forward name="load" path="/monitoring/monitoring.jsp"
 * 
 * @struts.action-forward name="statistic" path="/monitoring/monitoring.jsp"
 * 
 * 
 */
public class MonitoringAction extends LamsDispatchAction {
	
	public ISubmitFilesService submitFilesService;
	
    
    /**
     * Default ActionForward for Monitor
     *  (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward unspecified(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
    {
    	Long contentID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
    	request.getSession().setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID,contentID);
        
        return doTabs(mapping, form, request, response);
    }
    
    private ActionForward doTabs(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
    	 this.userList(mapping, form, request, response);
         this.instructions(mapping, form, request, response);
         this.showActivity(mapping, form, request, response);
         this.statistic(mapping, form, request, response);
         
         return mapping.findForward("load");
    }
    
	/**
	 * List all user for monitor staff choose which user need to do report marking.
	 * It is first step to do report marking.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward userList(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
		//Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));		
        Long contentID = (Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID);
		submitFilesService = getSubmitFilesService();
		//List userList = submitFilesService.getUsers(sessionID);
        List submitFilesSessionList = submitFilesService.getSubmitFilesSessionByContentID(contentID);
        Map sessionUserMap = new HashMap();
        
        //build a map with all users in the submitFilesSessionList
        Iterator it = submitFilesSessionList.iterator();
        while(it.hasNext()){
        	SessionDTO sessionDto = new SessionDTO();
        	SubmitFilesSession sfs = (SubmitFilesSession)it.next();
        	
            Long sessionID = sfs.getSessionID();
            sessionDto.setSessionID(sessionID);
            sessionDto.setSessionName(sfs.getSessionName());
            List userList = submitFilesService.getUsers(sessionID);
            sessionUserMap.put(sessionDto, userList);
        }
        
		//request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
		request.setAttribute("sessionUserMap",sessionUserMap);
		
		return mapping.findForward("userlist");
	}
	/**
	 * Display special user's marks information. 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward getFilesUploadedByUser(ActionMapping mapping,
							   ActionForm form,
							   HttpServletRequest request,
							   HttpServletResponse response){
		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		Long userID = new Long(WebUtil.readLongParam(request,"userID"));

		submitFilesService = getSubmitFilesService();
		//return FileDetailsDTO list according to the given userID and sessionID
		List files = submitFilesService.getFilesUploadedByUser(userID,sessionID);
		UserDTO userDto = submitFilesService.getUserDetails(userID);
		
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
		request.setAttribute("user",userDto);
		request.setAttribute("userReport",files);
		
		setTab(request);
		doTabs(mapping, form, request, response);
		return mapping.findForward("userMarks");
	}
	/**
	 * Display update mark initial page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward markFile(ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response){
		
		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		Long userID = new Long(WebUtil.readLongParam(request,"userID"));
		Long detailID = new Long(WebUtil.readLongParam(request,"detailID"));
		
		submitFilesService = getSubmitFilesService();
		
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
		request.setAttribute("user",submitFilesService.getUserDetails(userID));
		request.setAttribute("fileDetails",submitFilesService.getFileDetails(detailID));
		
		setTab(request);
		doTabs(mapping, form, request, response);
		return mapping.findForward("updateMarks");
	}
	
	public ActionForward updateMarks(ActionMapping mapping,
							   ActionForm form,
							   HttpServletRequest request,
							   HttpServletResponse response){
		//check whether the mark is validate
		Long marks = null;
		DynaActionForm sbmtMonitoringForm = (DynaActionForm) form;
		
		ActionMessages errors = new ActionMessages();  
		try {
			marks = new Long(WebUtil.readLongParam(request,"marks"));
		} catch (IllegalArgumentException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.mark.invalid.number"));
		}
		//if marks is invalid long, then throw error message directly.
		String comments = WebUtil.readStrParam(request,"comments",true);
		if(!errors.isEmpty()){
			//to echo back to error page.
			FileDetailsDTO details = (FileDetailsDTO) request.getAttribute("fileDetails");
			if(details != null)
				details.setComments(comments);
			saveErrors(request,errors);
			
			setTab(request);
			doTabs(mapping, form, request, response);
			sbmtMonitoringForm.set("method", "markFile");
			return mapping.findForward("updateMarks");
		}
		
		//get other request parameters
		String reportIDStr = request.getParameter("reportID");
		Long reportID = new Long(-1);
		if(!StringUtils.isEmpty(reportIDStr))
			reportID = Long.valueOf(reportIDStr);

		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		Long userID = new Long(WebUtil.readLongParam(request,"userID"));
		
		//get service then update report table
		submitFilesService = getSubmitFilesService();
		
		submitFilesService.updateMarks(reportID,marks,comments);
		List report = submitFilesService.getFilesUploadedByUser(userID,sessionID);
		request.setAttribute("userReport",report);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
		UserDTO userDto = submitFilesService.getUserDetails(userID);
		request.setAttribute("user",userDto);
		
		setTab(request);
		doTabs(mapping, form, request, response);
		return mapping.findForward("userMarks");
	}
	public ActionForward viewAllMarks(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
		
		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		submitFilesService = getSubmitFilesService();
		//return FileDetailsDTO list according to the given sessionID
		Map userFilesMap = submitFilesService.getFilesUploadedBySession(sessionID);
		request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
//		request.setAttribute("user",submitFilesService.getUserDetails(userID));
		request.setAttribute("report",userFilesMap);
		
		setTab(request);
		doTabs(mapping, form, request, response);
		return mapping.findForward("allUserMarks");

	}
	public ActionForward releaseMarks(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){

		//get service then update report table
		submitFilesService = getSubmitFilesService();
		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		submitFilesService.releaseMarksForSession(sessionID);
		
		setTab(request);
		
		//echo message back
		return doTabs(mapping, form, request, response);
	}
	public ActionForward downloadMarks(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
		
		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
		submitFilesService = getSubmitFilesService();
		//return FileDetailsDTO list according to the given sessionID
		Map userFilesMap = submitFilesService.getFilesUploadedBySession(sessionID);
		//construct Excel file format and download
		ActionMessages errors = new ActionMessages();
		try {
			//create an empty excel file
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Marks");
			sheet.setColumnWidth((short)0,(short)5000);
			HSSFRow row,row1=null,row2=null,row3=null,row4=null;
			HSSFCell cell;
			Iterator iter = userFilesMap.values().iterator();
			Iterator dtoIter; 
			boolean first = true;
			int idx = 0;
			int fileCount = 0;
			while(iter.hasNext()){
				List list = (List) iter.next();
				dtoIter = list.iterator();
				first = true;
				
				while(dtoIter.hasNext()){
					FileDetailsDTO dto = (FileDetailsDTO) dtoIter.next();
					if(first){
						row = sheet.createRow(idx++);
						cell = row.createCell((short) 0);
						cell.setCellValue(dto.getUserDTO().getFirstName()+" "+dto.getUserDTO().getLastName());
						sheet.addMergedRegion(new Region(idx-1,(short)0,idx-1, (short)1));
						first = false;
						row1 = sheet.createRow(idx+1);
						cell = row1.createCell((short) 0);
						cell.setCellValue("File name");
						row2 = sheet.createRow(idx+2);
						cell = row2.createCell((short) 0);
						cell.setCellValue("File description");
						row3 = sheet.createRow(idx+3);
						cell = row3.createCell((short) 0);
						cell.setCellValue("Marks");
						row4 = sheet.createRow(idx+4);
						cell = row4.createCell((short) 0);
						cell.setCellValue("Comments");
						idx += 6;
						fileCount = 0;
					}
					++fileCount;
					sheet.setColumnWidth((short)fileCount,(short)8000);
					cell = row1.createCell((short) fileCount);
					cell.setCellValue(dto.getFilePath());
					
					cell = row2.createCell((short) fileCount);
					cell.setCellValue(dto.getFileDescription());
					
					cell = row3.createCell((short) fileCount);
					if(dto.getMarks() != null)
						cell.setCellValue(new Double(dto.getMarks().toString()).doubleValue());
					else
						cell.setCellValue("");
					
					cell = row4.createCell((short) fileCount);
					cell.setCellValue(dto.getComments());
				}
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			wb.write(bos);
			//construct download file response header
			String fileName = "marks" + sessionID+".xls";
			String mineType = "application/vnd.ms-excel";
			String header = "attachment; filename=\"" + fileName + "\";";
			response.setContentType(mineType);
			response.setHeader("Content-Disposition",header);

			byte[] data = bos.toByteArray();
			response.getOutputStream().write(data,0,data.length);
			response.getOutputStream().flush();
		} catch (IOException e) {
			log.error(e);
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("monitoring.download.error",e.toString()));
		}

		if(!errors.isEmpty()){
			saveErrors(request,errors);
			request.setAttribute(AttributeNames.PARAM_TOOL_SESSION_ID,sessionID);
			setTab(request);
			doTabs(mapping, form, request, response);
			return mapping.findForward("userlist");
		}
			
		return null;
	}
	/**
	 * Display online/offline instruction information from Authoring. This page is read-only.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward instructions(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
		
		Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		
		//get back the upload file list and display them on page
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this
				.getServlet().getServletContext());
		
		SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(contentID);
		//if this content does not exist, then reset the contentID to current value to keep it on HTML page.
		persistContent.setContentID(contentID); 
		
		AuthoringDTO authorDto = new AuthoringDTO(persistContent);
		request.setAttribute(SbmtConstants.AUTHORING_DTO,authorDto);
		return mapping.findForward("instructions");
	}
	/**
	 * Display acitivty from authoring. The information will be same with "Basic" tab in authoring page.
	 * This page is read-only.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward showActivity(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){

		Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		getAuthoringActivity(contentID, request);
		return mapping.findForward("showActivity");
	}
	/**
	 * Provide editable page for activity. The information will be same with "Basic" tab in authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward editActivity(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
		Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		getAuthoringActivity(contentID, request);
		String mode = request.getParameter("mode");
		
		setTab(request);
		
		// do other tabs
		doTabs(mapping, form, request, response);
		
		if(StringUtils.equals(mode,"definelater"))
			return mapping.findForward("definelater");
		else
			return mapping.findForward("editActivity");
	}
	/**
	 * Update activity to database. The information will be same with "Basic" tab in authoring page.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward updateActivity(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
		Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		String title = WebUtil.readStrParam(request,"title");
		String instructions = WebUtil.readStrParam(request,"instructions");

		//get back the upload file list and display them on page
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this
				.getServlet().getServletContext());
		SubmitFilesContent content = submitFilesService.getSubmitFilesContent(contentID);
		content.setTitle(title);
		content.setInstruction(instructions);
		submitFilesService.saveOrUpdateContent(content);
		
		getAuthoringActivity(contentID, request);
		setTab(request);
		doTabs(mapping, form, request, response);
		
		String mode = request.getParameter("mode");
		if(StringUtils.equals(mode,"definelater"))
			return mapping.findForward("definelatersuccess");
		else
			return mapping.findForward("success");
	}
	/**
	 * Provide statistic information. Includes:<br>
	 * <li>Files not marked</li> 
	 * <li>Files marked</li> 
	 * <li>Total Files</li> 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward statistic(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
	    Long contentID = new Long(WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID));
		submitFilesService = getSubmitFilesService();
		// List userList = submitFilesService.getUsers(sessionID);
		List submitFilesSessionList = submitFilesService.getSubmitFilesSessionByContentID(contentID);
		Map sessionStatisticMap = new HashMap();

		// build a map with all users in the submitFilesSessionList
		Iterator it = submitFilesSessionList.iterator();
		while (it.hasNext()) {
			
			SubmitFilesSession sfs = (SubmitFilesSession) it.next();
			Long sessionID = sfs.getSessionID();
			String sessionName = sfs.getSessionName();
				
			submitFilesService = getSubmitFilesService();
			//return FileDetailsDTO list according to the given sessionID
			Map userFilesMap = submitFilesService.getFilesUploadedBySession(sessionID);
			Iterator iter = userFilesMap.values().iterator();
			Iterator dtoIter; 
			int notMarkedCount = 0;
			int markedCount = 0;
			while(iter.hasNext()){
				List list = (List) iter.next();
				dtoIter = list.iterator();
				while(dtoIter.hasNext()){
					FileDetailsDTO dto = (FileDetailsDTO) dtoIter.next();
					if(dto.getMarks() == null)
						notMarkedCount++;
					else
						markedCount++;
				}
			}
			StatisticDTO statisticDto = new StatisticDTO();
			SessionDTO sessionDto = new SessionDTO();
			statisticDto.setMarkedCount(markedCount);
			statisticDto.setNotMarkedCount(notMarkedCount);
			statisticDto.setTotalUploadedFiles(markedCount+notMarkedCount);
			sessionDto.setSessionID(sessionID);
			sessionDto.setSessionName(sessionName);
			sessionStatisticMap.put(sessionDto,statisticDto);
		}

		request.setAttribute("statisticList",sessionStatisticMap);
		return mapping.findForward("statistic");
	}
	/**
	 * @param request 
	 * @param form
	 * @param request
	 */
	private void getAuthoringActivity(Long contentID, ServletRequest request) {
		
		//get back the upload file list and display them on page
		submitFilesService = SubmitFilesServiceProxy.getSubmitFilesService(this
				.getServlet().getServletContext());
		
		SubmitFilesContent persistContent = submitFilesService.getSubmitFilesContent(contentID);
		//if this content does not exist, then reset the contentID to current value to keep it on HTML page.
		persistContent.setContentID(contentID);
		AuthoringDTO authorDto = new AuthoringDTO(persistContent);
		request.setAttribute(SbmtConstants.AUTHORING_DTO,authorDto);
	}
	private ISubmitFilesService getSubmitFilesService(){
		return SubmitFilesServiceProxy
			   .getSubmitFilesService(this.getServlet()
			   .getServletContext());
	}
	
	private void setTab(HttpServletRequest request) {
		String currTab = request.getParameter("currentTab");
		request.setAttribute("currentTab",currTab);
	}
	
}
