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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.tool.sbmt.service.ISubmitFilesService;
import org.lamsfoundation.lams.tool.sbmt.service.SubmitFilesServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;


/**
 * @author Manpreet Minhas
 * @struts.action
 * 				path="/monitoring"
 * 				parameter="method"
 * 				scope="request"
 * 				name="emptyForm" 				
 * 
 * @struts.action-forward name="userlist" path="/userlist.jsp"
 * @struts.action-forward name="report" path="/allLearners.jsp"
 * @struts.action-forward name="status" path="/Status.jsp"
 * @struts.action-forward name="userReport" path="/userReport.jsp"
 * @struts.action-forward name="updateMarks" path="/updateMarks.jsp"
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
		return mapping.findForward("userReport");
	}
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
		Long sessionID =new Long(WebUtil.readLongParam(request,"toolSessionID"));
		String reportIDStr = request.getParameter("reportID");
		Long reportID = new Long(-1);
		if(!StringUtils.isEmpty(reportIDStr))
			reportID = Long.valueOf(reportIDStr);

		Long marks = new Long(WebUtil.readLongParam(request,"marks"));
		String comments = WebUtil.readStrParam(request,"comments");
		Long userID = new Long(WebUtil.readLongParam(request,"userID"));
		
		submitFilesService = getSubmitFilesService();
		
		submitFilesService.updateMarks(reportID,marks,comments);
		List report = submitFilesService.getFilesUploadedByUser(userID,sessionID);
		request.getSession().setAttribute("userReport",report);
		request.getSession().setAttribute("toolSessionID",sessionID);
		request.getSession().setAttribute("userID",userID);		
		return mapping.findForward("userReport");
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
		return mapping.findForward("report");
	}

}
