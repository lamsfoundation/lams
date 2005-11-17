/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
package org.lamsfoundation.lams.tool.sbmt.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.tool.ToolAccessMode;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesSession;
import org.lamsfoundation.lams.tool.sbmt.dto.AuthoringDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.StatisticDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * @author Manpreet Minhas
 * @struts.action
 * 				path="/monitoring"
 * 				parameter="method"
 * 				scope="request"
 * 				name="emptyForm" 				
 * 
 * @struts.action-forward name="userlist" path="/monitoring/alluserlist.jsp"
 * @struts.action-forward name="userMarks" path="/monitoring/usermarkslist.jsp"
 * @struts.action-forward name="updateMarks" path="/monitoring/updatemarks.jsp"
 * @struts.action-forward name="allUserMarks" path="/monitoring/viewallmarks.jsp"
 * 
 * @struts.action-forward name="instructions" path="/monitoring/instructions.jsp"
 * 
 * @struts.action-forward name="showActivity" path="/monitoring/showactivity.jsp"
 * @struts.action-forward name="editActivity" path="/monitoring/editactivity.jsp"
 * @struts.action-forward name="success" path="/monitoring/success.jsp"
 * 
 * @struts.action-forward name="statistic" path="/monitoring/statistic.jsp"
 * 
 */
public class MonitoringAction extends DispatchAction {
	
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
        return userList(mapping, form, request, response);
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
        Long contentID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		submitFilesService = getSubmitFilesService();
		//List userList = submitFilesService.getUsers(sessionID);
        List submitFilesSessionList = submitFilesService.getSubmitFilesSessionByContentID(contentID);
        Map sessionUserMap = new HashMap();
        
        //build a map with all users in the submitFilesSessionList
        Iterator it = submitFilesSessionList.iterator();
        while(it.hasNext()){
            Long sessionID = ((SubmitFilesSession)it.next()).getSessionID();
            List userList = submitFilesService.getUsers(sessionID);
            sessionUserMap.put(sessionID, userList);
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
		return mapping.findForward("updateMarks");
	}
	
	public ActionForward updateMarks(ActionMapping mapping,
							   ActionForm form,
							   HttpServletRequest request,
							   HttpServletResponse response){
		//check whether the mark is validate
		Long marks = null;
		ActionMessages errors = new ActionMessages();  
		try {
			marks = new Long(WebUtil.readLongParam(request,"marks"));
		} catch (IllegalArgumentException e) {
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("monitoring.mark.input.error",e.getMessage()));
		}
		//if marks is invalid long, then throw error message directly.
		String comments = WebUtil.readStrParam(request,"comments",true);
		if(!errors.isEmpty()){
			//to echo back to error page.
			FileDetailsDTO details = (FileDetailsDTO) request.getAttribute("fileDetails");
			if(details != null)
				details.setComments(comments);
			saveErrors(request,errors);
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
		//todo: need display some success info
		return mapping.findForward("userMarks");
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

		getAuthoringActivity(form, request);
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
		
		getAuthoringActivity(form, request);
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
		submitFilesService.updateSubmitFilesContent(content);
		
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
		
		Long sessionID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_SESSION_ID));
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
		StatisticDTO dto = new StatisticDTO();
		dto.setMarkedCount(markedCount);
		dto.setNotMarkedCount(notMarkedCount);
		dto.setTotalUploadedFiles(markedCount+notMarkedCount);
		request.setAttribute("statistic",dto);
		return mapping.findForward("statistic");
	}
	/**
	 * @param form
	 * @param request
	 */
	private void getAuthoringActivity(ActionForm form, HttpServletRequest request) {
		Long contentID = new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
		
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
	
	
}
