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
package org.lamsfoundation.lams.workspace.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.authoring.service.IAuthoringService;
import org.lamsfoundation.lams.learningdesign.exception.LearningDesignException;
import org.lamsfoundation.lams.usermanagement.exception.UserException;
import org.lamsfoundation.lams.usermanagement.exception.WorkspaceFolderException;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Manpreet Minhas
 * @struts.action 
 * 				  path = "/workspace"
 * 				  parameter = "method"
 * 				  validate = "false"
 * @struts.action-forward name = "success" path = "/index.jsp"
 */
public class WorkspaceAction extends DispatchAction {
	
	public static final String RESOURCE_ID = "resourceID";
	public static final String RESOURCE_TYPE = "resourceType";
	
	/** If you want the output given as a jsp, set the request parameter "jspoutput" to 
     * some value other than an empty string (e.g. 1, true, 0, false, blah). 
     * If you want it returned as a stream (ie for Flash), do not define this parameter
     */  
	public static String USE_JSP_OUTPUT = "jspoutput";
	
	
	/**
	 * @return
	 */
	public IWorkspaceManagementService getWorkspaceManagementService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
		return (IWorkspaceManagementService) webContext.getBean("workspaceManagementService");
	}
	
	/** Output the supplied WDDX packet. If the request parameter USE_JSP_OUTPUT
	 * is set, then it sets the session attribute "parameterName" to the wddx packet string.
	 * If  USE_JSP_OUTPUT is not set, then the packet is written out to the 
	 * request's PrintWriter.
	 *   
	 * @param mapping action mapping (for the forward to the success jsp)
	 * @param request needed to check the USE_JSP_OUTPUT parameter
	 * @param response to write out the wddx packet if not using the jsp
	 * @param wddxPacket wddxPacket or message to be sent/displayed
	 * @param parameterName session attribute to set if USE_JSP_OUTPUT is set
	 * @throws IOException
	 */
	private ActionForward outputPacket(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response,
	        		String wddxPacket, String parameterName) throws IOException {
	    String useJSP = WebUtil.readStrParam(request, USE_JSP_OUTPUT, true);
	    if ( useJSP != null && useJSP.length() >= 0 ) {
		    request.getSession().setAttribute(parameterName,wddxPacket);
		    return mapping.findForward("success");
	    } else {
	        PrintWriter writer = response.getWriter();
	        writer.println(wddxPacket);
	        return null;
	    }
	}
	/**
	 * For details please refer to 
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws IOException
	 */
	public ActionForward createFolderForFlash(ActionMapping mapping,
											   ActionForm form,
											   HttpServletRequest request,
											   HttpServletResponse response)throws ServletException,IOException{
		Integer parentFolderID = new Integer(WebUtil.readIntParam(request,"parentFolderID"));
		String folderName = (String)WebUtil.readStrParam(request,"name");
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.createFolderForFlash(parentFolderID,folderName,userID);		
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	
	/**
	 * For details please refer to 
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws Exception
	 */
	public ActionForward getFolderContents(ActionMapping mapping,
										   ActionForm form,
										   HttpServletRequest request,
										   HttpServletResponse response)throws ServletException,Exception{
		Integer folderID = new Integer(WebUtil.readIntParam(request,"folderID"));
		Integer mode = new Integer(WebUtil.readIntParam(request,"mode"));		
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.getFolderContents(userID,folderID,mode);		
		return outputPacket(mapping, request, response, wddxPacket, "details");		
	}
	
	/**
	 * For details please refer to 
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws Exception
	 */
	public ActionForward getFolderContentsExcludeHome(ActionMapping mapping,
										   ActionForm form,
										   HttpServletRequest request,
										   HttpServletResponse response)throws ServletException,Exception{
		Integer folderID = new Integer(WebUtil.readIntParam(request,"folderID"));
		Integer mode = new Integer(WebUtil.readIntParam(request,"mode"));		
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.getFolderContentsExcludeHome(userID,folderID,mode);		
		return outputPacket(mapping, request, response, wddxPacket, "details");		
	}

	/**
	 * For details please refer to
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws IOException
	 */
	public ActionForward deleteResource(ActionMapping mapping,
											   ActionForm form,
											   HttpServletRequest request,
											   HttpServletResponse response)throws ServletException, IOException{
		Long resourceID = new Long(WebUtil.readLongParam(request,RESOURCE_ID));				
		String resourceType = WebUtil.readStrParam(request,RESOURCE_TYPE);				
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.deleteResource(resourceID,resourceType,userID);		
        PrintWriter writer = response.getWriter();
        writer.println(wddxPacket);
        return null;
	}
	
	/**
	 * For details please refer to
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws IOException
	 */
	public ActionForward copyResource(ActionMapping mapping,
											   ActionForm form,
											   HttpServletRequest request,
											   HttpServletResponse response)throws ServletException, IOException{
		Long resourceID = new Long(WebUtil.readLongParam(request,RESOURCE_ID));				
		String resourceType = WebUtil.readStrParam(request,RESOURCE_TYPE);				
		Integer targetFolderID = new Integer(WebUtil.readIntParam(request,"targetFolderID"));
		Integer copyType = WebUtil.readIntParam(request, "copyType", true);
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.copyResource(resourceID,resourceType,copyType,targetFolderID,userID);
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	
	/**
	 * For details please refer to
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws IOException
	 */
	public ActionForward moveResource(ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)throws ServletException, IOException{
		Long resourceID = new Long(WebUtil.readLongParam(request,RESOURCE_ID));				
		String resourceType = WebUtil.readStrParam(request,RESOURCE_TYPE);				
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		Integer targetFolderID = new Integer(WebUtil.readIntParam(request,"targetFolderID"));

		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.moveResource(resourceID,targetFolderID,resourceType,userID);
        PrintWriter writer = response.getWriter();
        writer.println(wddxPacket);
        return null;
	}
	
	/**
	 * For details please refer to
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws Exception
	 */
	public ActionForward createWorkspaceFolderContent(ActionMapping mapping,
													  ActionForm form,
													  HttpServletRequest request,
													  HttpServletResponse response)throws ServletException, Exception{
		Integer contentTypeID = new Integer(WebUtil.readIntParam(request,"contentTypeID"));
		String name = WebUtil.readStrParam(request,"name");
		String description = WebUtil.readStrParam(request,"description");
		//Date createDateTime = DateUtil.convertFromString(WebUtil.readStrParam(request,"createDateTime"));
		//Date lastModifiedDate = DateUtil.convertFromString(WebUtil.readStrParam(request,"lastModifiedDateTime"));
		Integer workspaceFolderID =  new Integer(WebUtil.readIntParam(request,"workspaceFolderID"));
		String mimeType = WebUtil.readStrParam(request,"mimeType");
		String path = WebUtil.readStrParam(request,"path");
		
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.createWorkspaceFolderContent(contentTypeID,name,description,
																				 workspaceFolderID,
																				 mimeType,path);
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	/**
	 * For details please refer to
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws ServletException
	 * @throws Exception
	 */
	public ActionForward updateWorkspaceFolderContent(ActionMapping mapping,
											 ActionForm form,
											 HttpServletRequest request,
											 HttpServletResponse response)throws ServletException, Exception{
		Long folderContentID = new Long(WebUtil.readLongParam(request,"folderContentID"));
		String path = WebUtil.readStrParam(request,"path");
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.updateWorkspaceFolderContent(folderContentID,path);
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	/**
	 * For details please refer to
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 */
	public ActionForward getAccessibleWorkspaceFolders(ActionMapping mapping,
													   ActionForm form,
													   HttpServletRequest request,
													   HttpServletResponse response)throws IOException{
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.getAccessibleWorkspaceFolders(userID);		
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	/**
	 * For details please refer to
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 */
	public ActionForward getAccessibleWorkspaceFoldersNew(ActionMapping mapping,
													   ActionForm form,
													   HttpServletRequest request,
													   HttpServletResponse response)throws IOException{
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.getAccessibleWorkspaceFoldersNew(userID);		
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	
	/**
	 * For details please refer to
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 */
	public ActionForward renameResource(ActionMapping mapping,
											ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response)throws IOException{
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		Long resourceID = new Long(WebUtil.readLongParam(request,RESOURCE_ID));
		String resourceType = WebUtil.readStrParam(request,RESOURCE_TYPE);				
		String name = WebUtil.readStrParam(request,"name");
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.renameResource(resourceID,resourceType,name,userID);		
        PrintWriter writer = response.getWriter();
        writer.println(wddxPacket);
        return null;
	}
	
	/**
	 * For details please refer to
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 * @throws IOException
	 */
	public ActionForward getWorkspace(ActionMapping mapping,
									  ActionForm form,
									  HttpServletRequest request,
									  HttpServletResponse response)throws IOException{
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.getWorkspace(userID);		
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	
	/**
	 * For details please refer to
	 * org.lamsfoundation.lams.workspace.service.IWorkspaceManagementService
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ActionForward deleteContentWithVersion(ActionMapping mapping,
												  ActionForm form,
												  HttpServletRequest request,
												  HttpServletResponse response)throws Exception{
		Long uuID = new Long(WebUtil.readIntParam(request,"uuID"));
		Long versionID= new Long(WebUtil.readIntParam(request,"versionID"));
		Long folderContentID = new Long(WebUtil.readIntParam(request,"folderContentID"));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.deleteContentWithVersion(uuID,versionID,folderContentID);
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	
	
	public ActionForward getOrganisationsByUserRole(ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response)throws Exception{
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		String role = WebUtil.readStrParam(request, "role");
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.getOrganisationsByUserRole(userID, role);		
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}
	
	public ActionForward getUsersFromOrganisationByRole(ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response)throws Exception{
		Integer organisationID = new Integer(WebUtil.readIntParam(request,"organisationID"));
		String role = WebUtil.readStrParam(request, "role");
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.getUsersFromOrganisationByRole(organisationID, role);		
		return outputPacket(mapping, request, response, wddxPacket, "details");
	}

}

