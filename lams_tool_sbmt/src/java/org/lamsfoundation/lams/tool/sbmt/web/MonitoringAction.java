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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dto.AuthoringDTO;
import org.lamsfoundation.lams.tool.sbmt.dto.FileDetailsDTO;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.tool.sbmt.util.SbmtConstants;
import org.lamsfoundation.lams.util.WebUtil;


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
 * @struts.action-forward name="instructions" path="/monitoring/instructions.jsp"
 * 
 * @struts.action-forward name="status" path="/Status.jsp"
 * 				
 */
public class MonitoringAction extends DispatchAction {
	
	public ISubmitFilesService submitFilesService;
	
	public ISubmitFilesService getSubmitFilesService(){
		return SubmitFilesServiceProxy
			   .getSubmitFilesService(this.getServlet()
			   .getServletContext());
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
		Long sessionID =new Long(WebUtil.readLongParam(request,"toolSessionID"));		
		submitFilesService = getSubmitFilesService();
		List userList = submitFilesService.getUsers(sessionID);
		request.getSession().setAttribute("toolSessionID",sessionID);
		request.getSession().setAttribute("USERLIST",userList);
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
		Long sessionID =new Long(WebUtil.readLongParam(request,"toolSessionID"));
		Long userID = new Long(WebUtil.readLongParam(request,"userID"));
		submitFilesService = getSubmitFilesService();
		//return FileDetailsDTO list according to the given userID and sessionID
		List files = submitFilesService.getFilesUploadedByUser(userID,sessionID);		
		request.getSession().setAttribute("toolSessionID",sessionID);
		request.getSession().setAttribute("user",
										  submitFilesService.getUserDetails(userID));
		request.getSession().setAttribute("userReport",files);
		return mapping.findForward("userMarks");
	}
	/**
	 * Display empty update mark page.
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
		Long sessionID =new Long(WebUtil.readLongParam(request,"toolSessionID"));
		Long userID = new Long(WebUtil.readLongParam(request,"userID"));
		Long detailID = new Long(WebUtil.readLongParam(request,"detailID"));
		
		submitFilesService = getSubmitFilesService();
		
		request.getSession().setAttribute("toolSessionID",sessionID);
		request.getSession().setAttribute("user",
							  submitFilesService.getUserDetails(userID));
		request.getSession().setAttribute("fileDetails",
							  submitFilesService.getFileDetails(detailID));
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
			FileDetailsDTO details = (FileDetailsDTO) request.getSession().getAttribute("fileDetails");
			if(details != null){
				details.setComments(comments);
			}
			saveErrors(request,errors);
			return mapping.findForward("updateMarks");
		}
		
		//get other request parameters
		String reportIDStr = request.getParameter("reportID");
		Long reportID = new Long(-1);
		if(!StringUtils.isEmpty(reportIDStr))
			reportID = Long.valueOf(reportIDStr);

		Long sessionID =new Long(WebUtil.readLongParam(request,"toolSessionID"));
		Long userID = new Long(WebUtil.readLongParam(request,"userID"));
		
		//get service then update report table
		submitFilesService = getSubmitFilesService();
		
		submitFilesService.updateMarks(reportID,marks,comments);
		List report = submitFilesService.getFilesUploadedByUser(userID,sessionID);
		request.getSession().setAttribute("userReport",report);
		request.getSession().setAttribute("toolSessionID",sessionID);
		request.getSession().setAttribute("userID",userID);		
		return mapping.findForward("userMarks");
	}
	/**
	 * Download upload file for a special submission detail.  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	
	public ActionForward downloadFile(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
		Long versionID =new Long(WebUtil.readLongParam(request,"versionID"));
		Long uuID = new Long(WebUtil.readLongParam(request,"uuID"));
		
		ISubmitFilesService service = getSubmitFilesService();
		IVersionedNode node = service.downloadFile(uuID,versionID);
		int len;
		ActionMessages errors = new ActionMessages();
		try {
			InputStream is = node.getFile();
			String mineType = node.getProperty("MIMETYPE").getString();
			response.setContentType(mineType);
			String header = "attachment; filename=\"" + node.getProperty("FILENAME").getString() + "\";";
			response.setHeader("Content-Disposition",header);
			byte[] data = new byte[4 * 1024];
			while(is != null && (len = is.read(data)) != -1){
				response.getOutputStream().write(data,0,len);
			}
			response.getOutputStream().flush();
		} catch (Exception e) {
			log.error(e);
			errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("monitoring.download.error",e.toString()));
		}
		//if download throw any exception, then display it in current page.
		if(!errors.isEmpty()){
			saveErrors(request,errors);
			return mapping.findForward("userMarks");
		}
			
		return null;
	}
	public ActionForward viewAllMarks(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
		
		Long sessionID =new Long(WebUtil.readLongParam(request,"toolSessionID"));
		submitFilesService = getSubmitFilesService();
		//return FileDetailsDTO list according to the given sessionID
		Map userFilesMap = submitFilesService.getFilesUploadedBySession(sessionID);		
		request.getSession().setAttribute("toolSessionID",sessionID);
//		request.getSession().setAttribute("user",
//										  submitFilesService.getUserDetails(userID));
		request.getSession().setAttribute("report",userFilesMap);
		return mapping.findForward("allUserMarks");

	}
	public ActionForward releaseMarks(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){

		//get service then update report table
		submitFilesService = getSubmitFilesService();
		Long sessionID =new Long(WebUtil.readLongParam(request,"toolSessionID"));
		submitFilesService.releaseMarksForSession(sessionID);
		//todo: need display some success info
		return mapping.findForward("userMarks");
	}
	public ActionForward downloadMarks(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
		return null;
	}
	
	public ActionForward instructions(ActionMapping mapping,
			   ActionForm form,
			   HttpServletRequest request,
			   HttpServletResponse response){
		
		Long contentID = new Long(WebUtil.readLongParam(request,"toolContentID"));
		
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
	//TODO
	public ActionForward getStatus(ActionMapping mapping,
							   ActionForm form,
							   HttpServletRequest request,
							   HttpServletResponse response){		
		Long sessionID =new Long(WebUtil.readLongParam(request,"toolSessionID"));
		submitFilesService = getSubmitFilesService();		
		ArrayList status = submitFilesService.getStatus(sessionID);
		request.getSession().setAttribute("toolSessionID",sessionID);		
		request.getSession().setAttribute("status",status);
		return mapping.findForward("status");
	}
	public ActionForward generateReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		Long sessionID = new Long(WebUtil.readLongParam(request,
				"toolSessionID"));
		submitFilesService = getSubmitFilesService();
		Hashtable report = submitFilesService.generateReport(sessionID);
		request.getSession().setAttribute("toolSessionID", sessionID);
		request.getSession().setAttribute("report", report);
		return mapping.findForward("allUserMarks");
	}

}
