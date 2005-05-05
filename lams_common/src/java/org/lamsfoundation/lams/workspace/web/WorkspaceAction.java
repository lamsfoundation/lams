/***************************************************************************
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
 * ************************************************************************
 */
package org.lamsfoundation.lams.workspace.web;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Manpreet Minhas
 * @struts.action name = "WorkspaceAction"
 * 				  path = "/workspace"
 * 				  parameter = "method"
 * 				  validate = "false"
 * @struts.action-forward name = "success" path = "/index.jsp"
 */
public class WorkspaceAction extends DispatchAction {
	
	
	public IWorkspaceManagementService getWorkspaceManagementService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
		return (IWorkspaceManagementService) webContext.getBean("workspaceManagementService");
	}
	
	public ActionForward createWorkspaceFolder(ActionMapping mapping,
											   ActionForm form,
											   HttpServletRequest request,
											   HttpServletResponse response)throws ServletException,IOException{
		Integer parentFolderID = new Integer(WebUtil.readIntParam(request,"parentFolderID"));
		String folderName = (String)WebUtil.readStrParam(request,"name");
		Integer userID = new Integer(WebUtil.readIntParam(request,"userID"));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.createFolderForFlash(parentFolderID,folderName,userID);
		request.getSession().setAttribute("details",wddxPacket);
		return mapping.findForward("success");
	}
	
	public ActionForward getFolderContents(ActionMapping mapping,
										   ActionForm form,
										   HttpServletRequest request,
										   HttpServletResponse response)throws ServletException,Exception{
		Integer folderID = new Integer(WebUtil.readIntParam(request,"folderID"));
		Integer mode = new Integer(WebUtil.readIntParam(request,"mode"));		
		Integer userID = new Integer(WebUtil.readIntParam(request,"userID"));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.getFolderContents(userID,folderID,mode);
		request.getSession().setAttribute("details",wddxPacket);
		return mapping.findForward("success");		
	}
	
	public ActionForward deleteWorkspacFolder(ActionMapping mapping,
											   ActionForm form,
											   HttpServletRequest request,
											   HttpServletResponse response)throws ServletException, IOException{
		Integer folderID = new Integer(WebUtil.readIntParam(request,"folderID"));				
		Integer userID = new Integer(WebUtil.readIntParam(request,"userID"));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.deleteFolder(folderID,userID);
		request.getSession().setAttribute("details",wddxPacket);
		return mapping.findForward("success");
	}
	
	public ActionForward deleteLearningDesign(ActionMapping mapping,
											   ActionForm form,
											   HttpServletRequest request,
											   HttpServletResponse response)throws ServletException, IOException{
		Long learningDesignID = new Long(WebUtil.readIntParam(request,"learningDesignID"));				
		Integer userID = new Integer(WebUtil.readIntParam(request,"userID"));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.deleteLearningDesign(learningDesignID,userID);
		request.getSession().setAttribute("details",wddxPacket);
		return mapping.findForward("success");
		
	}
	
	public ActionForward copyFolder(ActionMapping mapping,
											   ActionForm form,
											   HttpServletRequest request,
											   HttpServletResponse response)throws ServletException, IOException{
		Integer folderID = new Integer(WebUtil.readIntParam(request,"folderID"));				
		Integer targetFolderID = new Integer(WebUtil.readIntParam(request,"targetFolderID"));
		Integer userID = new Integer(WebUtil.readIntParam(request,"userID"));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.copyFolder(folderID,targetFolderID,userID);
		return mapping.findForward("success");
	}
	
	public ActionForward moveFolder(ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)throws ServletException, IOException{
		Integer currentFolderID = new Integer(WebUtil.readIntParam(request,"currentFolderID"));				
		Integer targetFolderID = new Integer(WebUtil.readIntParam(request,"targetFolderID"));
		Integer userID = new Integer(WebUtil.readIntParam(request,"userID"));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.moveFolder(currentFolderID,targetFolderID,userID);
		return mapping.findForward("success");
	}
	
	public ActionForward createWorkspaceFolderContent(ActionMapping mapping,
													  ActionForm form,
													  HttpServletRequest request,
													  HttpServletResponse response)throws ServletException, Exception{
		Integer contentTypeID = new Integer(WebUtil.readIntParam(request,"contentType"));
		String name = WebUtil.readStrParam(request,"name");
		String description = WebUtil.readStrParam(request,"description");
		Date createDateTime = new Date(WebUtil.readStrParam(request,"createDateTime"));
		Date lastModifiedDate = new Date(WebUtil.readStrParam(request,"lastModifiedDateTime"));
		Integer workspaceFolderID =  new Integer(WebUtil.readIntParam(request,"workspaceFolderID"));
		String mimeType = WebUtil.readStrParam(request,"mimeType");
		String path = WebUtil.readStrParam(request,"path");
		
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String message = workspaceManagementService.createWorkspaceFolderContent(contentTypeID,name,description,
																				 createDateTime,lastModifiedDate,workspaceFolderID,
																				 mimeType,path);
		return mapping.findForward("success");
	}
	public ActionForward updateWorkspaceFolderContent(ActionMapping mapping,
											 ActionForm form,
											 HttpServletRequest request,
											 HttpServletResponse response)throws ServletException, Exception{
		Long folderContentID = new Long(WebUtil.readLongParam(request,"folderContentID"));
		String path = WebUtil.readStrParam(request,"path");
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String message = workspaceManagementService.updateWorkspaceFolderContent(folderContentID,path);
		return mapping.findForward("success");
	}
	public ActionForward deleteAllVersionsOfContent(ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response)throws ServletException, Exception{
		Long folderContentID = new Long(WebUtil.readLongParam(request,"folderContentID"));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String message = workspaceManagementService.deleteWorkspaceFolderContent(folderContentID);
		return mapping.findForward("success");
	}

}

