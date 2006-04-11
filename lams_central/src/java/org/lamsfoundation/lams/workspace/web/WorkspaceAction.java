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
package org.lamsfoundation.lams.workspace.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.usermanagement.WorkspaceFolder;
import org.lamsfoundation.lams.usermanagement.exception.UserAccessDeniedException;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.wddx.FlashMessage;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.workspace.dto.FolderContentDTO;
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
	
	/** 
	 * Special value for folderID on getFolderContents(). Triggers getting the 
	 * dummy value for the organisations (see ORG_FOLDER_ID) and the user's
	 * private folder. See the method for more details. 
	 */
	public static final Integer BOOTSTRAP_FOLDER_ID = new Integer(-1);
	
	/** 
	 * Special value for folderID on getFolderContents(). Triggers getting the 
	 * organisation folders that are available to a user. See
	 * the method for more details. 
	 */
	public static final Integer ORG_FOLDER_ID = new Integer(-2);

	/**
	 * @return
	 */
	public IWorkspaceManagementService getWorkspaceManagementService(){
		WebApplicationContext webContext = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServlet().getServletContext());
		return (IWorkspaceManagementService) webContext.getBean("workspaceManagementService");
	}
	
	/** Send the flash message back to Flash */
	private ActionForward returnWDDXPacket(FlashMessage flashMessage, HttpServletResponse response) throws IOException {
	        PrintWriter writer = response.getWriter();
	        writer.println(flashMessage.serializeMessage());
	        return null;
	}

	/** Send the flash message back to Flash */
	private ActionForward returnWDDXPacket(String serializedFlashMessage, HttpServletResponse response) throws IOException {
	        PrintWriter writer = response.getWriter();
	        writer.println(serializedFlashMessage);
	        return null;
	}

	/** Is the folder id one of our special dummy ids? If so, we can't process the normal create, copy, paste, move
	 * functions on this folder.
	 * @return error packet if it is a special id. Should return packet to Flash with no further processing.
	 */
	private String checkResourceNotDummyValue(String methodName, Integer folderID, String resourceType) throws IOException {
		return checkResourceNotDummyValue(methodName, new Long(folderID.longValue()), resourceType);
	}

	/** Is the folder id one of our special dummy ids? If so, we can't process the normal create, copy, paste, move
	 * functions on this folder.
	 */
	private String checkResourceNotDummyValue(String methodName, Long folderID, String resourceType) throws IOException {
		if ( FolderContentDTO.FOLDER.equals(resourceType) && (BOOTSTRAP_FOLDER_ID.equals(folderID) || ORG_FOLDER_ID.equals(folderID)) ) {
			FlashMessage msg = new FlashMessage(methodName, "FolderID "+folderID+" invalid for this call.",FlashMessage.ERROR);
			return msg.serializeMessage();
		}
		return null;
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
		String errorPacket = checkResourceNotDummyValue("createFolderForFlash", parentFolderID, FolderContentDTO.FOLDER);
		if ( errorPacket != null)
			return returnWDDXPacket(errorPacket, response);

		String folderName = (String)WebUtil.readStrParam(request,"name");
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String wddxPacket = workspaceManagementService.createFolderForFlash(parentFolderID,folderName,userID);		
		return returnWDDXPacket(wddxPacket, response);
	}
	
	/**
	 * getFolderContents returns the details of the folders, learning designs and files 
	 * contained in a folder. 
	 * 
	 * If getFolderContents gets the BOOTSTRAP_FOLDER_ID (-1), then it return the user's private
	 * folder and the root folder. 
	 * 
	 * If getFolderContents gets the ORG_FOLDER_ID (-2) then it will return all the workspace
	 * folders that the user can access.
	 * 
	 * This method handles the special values for the BOOTSTRAP_FOLDER_ID and the ORG_FOLDER_ID
	 * as these values are only meaningful to the Flash client - they are not meaningful to 
	 * the progress engine or the like. If we ever had to return this data to another client,
	 * the data may be returned in a different way.
	 * 
	 * The calls made to the service layer vary depending on what is required - this decouple's
	 * the client's special needs from the overall logic of the workspaces.
	 * 
	 * For more details please refer to 
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
										   HttpServletResponse response)throws ServletException,IOException{
		Integer folderID = new Integer(WebUtil.readIntParam(request,"folderID"));
		Integer mode = new Integer(WebUtil.readIntParam(request,"mode"));		
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
		String methodKey = "getFolderContents";
		Hashtable packet = null;
		
		try {
			if ( BOOTSTRAP_FOLDER_ID.equals(folderID )) {
				MessageService msgService = workspaceManagementService.getMessageService();

				// return back the dummy org DTO and the user's workspace folder
				Vector<FolderContentDTO> folders = new Vector<FolderContentDTO>();
				FolderContentDTO userFolder = workspaceManagementService.getUserWorkspaceFolder(userID);
				if ( userFolder != null )
					folders.add(userFolder);
				
				FolderContentDTO dummyOrgFolder = new  FolderContentDTO(msgService.getMessage("organisations"), 
						msgService.getMessage("folder"),
						null, null, 
						FolderContentDTO.FOLDER, new Long(ORG_FOLDER_ID.longValue()), WorkspaceFolder.READ_ACCESS,
						null);
				
				folders.add(dummyOrgFolder);
				
				packet = createFolderContentPacket(null, BOOTSTRAP_FOLDER_ID, folders);
				
			} else if ( ORG_FOLDER_ID.equals(folderID) ) {
				// return back all the organisation folders that the user can access
				Vector folders = workspaceManagementService.getAccessibleOrganisationWorkspaceFolders(userID);
				packet = createFolderContentPacket(BOOTSTRAP_FOLDER_ID, ORG_FOLDER_ID, folders);
				
			} else {
				// normal case - just return back the contents of this folder.
				WorkspaceFolder folder = workspaceManagementService.getWorkspaceFolder(folderID);
				if ( folder != null ) {
					Vector items;
						items = workspaceManagementService.getFolderContentsExcludeHome(userID,folder,mode);
					WorkspaceFolder parentWorkspaceFolder = folder.getParentWorkspaceFolder();
					packet = createFolderContentPacket(parentWorkspaceFolder!=null?parentWorkspaceFolder.getWorkspaceFolderId():null, 
							folder.getWorkspaceFolderId(), 
							items);
				} else {
					return returnWDDXPacket(FlashMessage.getNoSuchWorkspaceFolderContentExsists(methodKey,new Long(folderID.longValue())), response);		
				}
			}
		} catch (UserAccessDeniedException e) {
			return returnWDDXPacket(FlashMessage.getUserNotAuthorized(methodKey, userID), response);
		} catch (Exception e) {
			log.error("getFolderContents: Exception occured. userID "+userID+" folderID "+folderID, e);
			return returnWDDXPacket(FlashMessage.getExceptionOccured(methodKey, e.getMessage()), response);
		}
	
		return returnWDDXPacket(new FlashMessage(methodKey,packet), response);		
	}

	private Hashtable<String,Object> createFolderContentPacket(Integer parentWorkspaceFolderID, Integer workspaceFolderID, Vector contents){
		Hashtable<String,Object> packet = new Hashtable<String,Object>();
		
		if ( parentWorkspaceFolderID != null )
			packet.put("parentWorkspaceFolderID", parentWorkspaceFolderID);
		
		if ( workspaceFolderID != null )
			packet.put("workspaceFolderID", workspaceFolderID);
		
		if ( contents != null )
			packet.put("contents", contents);
		return packet;
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
		String errorPacket = checkResourceNotDummyValue("deleteResource", resourceID, FolderContentDTO.FOLDER);
		if ( errorPacket != null)
			return returnWDDXPacket(errorPacket, response);

		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		String wddxPacket = null;
		try {
			IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
			wddxPacket = workspaceManagementService.deleteResource(resourceID,resourceType,userID);		
		} catch (Exception e) {
			log.error("deleteResource: Exception occured. userID "+userID+" folderID "+resourceID, e);
			FlashMessage flashMessage = FlashMessage.getExceptionOccured(IWorkspaceManagementService.MSG_KEY_DELETE, e.getMessage());
			wddxPacket = flashMessage.serializeMessage();
		}
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
		String errorPacket = checkResourceNotDummyValue("copyResource", targetFolderID, FolderContentDTO.FOLDER);
		if ( errorPacket != null)
			return returnWDDXPacket(errorPacket, response);

		String wddxPacket  = null;
		Integer copyType = WebUtil.readIntParam(request, "copyType", true);
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		try {
			IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
			wddxPacket = workspaceManagementService.copyResource(resourceID,resourceType,copyType,targetFolderID,userID);
		} catch (Exception e) {
			log.error("deleteResource: Exception occured. userID "+userID+" folderID "+resourceID, e);
			FlashMessage flashMessage = FlashMessage.getExceptionOccured(IWorkspaceManagementService.MSG_KEY_DELETE, e.getMessage());
			wddxPacket = flashMessage.serializeMessage();
		}
		return returnWDDXPacket(wddxPacket, response);
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

		String errorPacket = checkResourceNotDummyValue("copyResource", targetFolderID, FolderContentDTO.FOLDER);
		if ( errorPacket != null)
			return returnWDDXPacket(errorPacket, response);

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
		
		String errorPacket = checkResourceNotDummyValue("createWorkspaceFolderContent", workspaceFolderID, FolderContentDTO.FOLDER);
		if ( errorPacket != null)
			return returnWDDXPacket(errorPacket, response);

		String wddxPacket = null;
		try {
			IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
			wddxPacket = workspaceManagementService.createWorkspaceFolderContent(contentTypeID,name,description,
																				 workspaceFolderID,
																				 mimeType,path);
		} catch (Exception e) {
			log.error("createWorkspaceFolderContent: Exception occured. contentTypeID "+contentTypeID+" name "+name+" workspaceFolderID "+workspaceFolderID, e);
			FlashMessage flashMessage = FlashMessage.getExceptionOccured(IWorkspaceManagementService.MSG_KEY_CREATE_WKF_CONTENT, e.getMessage());
			wddxPacket = flashMessage.serializeMessage();
		}
		return returnWDDXPacket(wddxPacket, response);
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

		String wddxPacket = null;
		try {
			IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
			wddxPacket = workspaceManagementService.updateWorkspaceFolderContent(folderContentID,path);
		} catch (Exception e) {
			log.error("updateWorkspaceFolderContent: Exception occured. path "+path+" folderContentID "+folderContentID, e);
			FlashMessage flashMessage = FlashMessage.getExceptionOccured(IWorkspaceManagementService.MSG_KEY_UPDATE_WKF_CONTENT, e.getMessage());
			wddxPacket = flashMessage.serializeMessage();
		}
		return returnWDDXPacket(wddxPacket, response);
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

		String errorPacket = checkResourceNotDummyValue("renameResource", resourceID, resourceType);
		if ( errorPacket != null)
			return returnWDDXPacket(errorPacket, response);

		String name = WebUtil.readStrParam(request,"name");
		String wddxPacket = null;
		try {
			IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
			wddxPacket = workspaceManagementService.renameResource(resourceID,resourceType,name,userID);		
		} catch (Exception e) {
			log.error("renameResource: Exception occured. userID "+userID+" resourceID "+resourceID+" resourceType "+resourceType, e);
			FlashMessage flashMessage = FlashMessage.getExceptionOccured(IWorkspaceManagementService.MSG_KEY_RENAME, e.getMessage());
			wddxPacket = flashMessage.serializeMessage();
		}
		return returnWDDXPacket(wddxPacket, response);
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

		String errorPacket = checkResourceNotDummyValue("deleteContentWithVersion", folderContentID, FolderContentDTO.FOLDER);
		if ( errorPacket != null)
			return returnWDDXPacket(errorPacket, response);

		String wddxPacket = null;
		try {
			IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
			wddxPacket = workspaceManagementService.deleteContentWithVersion(uuID,versionID,folderContentID);
		} catch (Exception e) {
			log.error("deleteContentWithVersion: Exception occured. uuID "+uuID+" versionID "+versionID+" folderContentID "+folderContentID, e);
			FlashMessage flashMessage = FlashMessage.getExceptionOccured(IWorkspaceManagementService.MSG_KEY_DELETE_VERSION, e.getMessage());
			wddxPacket = flashMessage.serializeMessage();
		}
		return returnWDDXPacket(wddxPacket, response);
	}
	
	
	public ActionForward getOrganisationsByUserRole(ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response)throws Exception{
		Integer userID = new Integer(WebUtil.readIntParam(request,AttributeNames.PARAM_USER_ID));
		String role = WebUtil.readStrParam(request, "role");
		String wddxPacket = null;
		try {
			IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
			wddxPacket = workspaceManagementService.getOrganisationsByUserRole(userID, role);		
		} catch (Exception e) {
			log.error("getOrganisationsByUserRole: Exception occured. userID "+userID+" role "+role, e);
			FlashMessage flashMessage = FlashMessage.getExceptionOccured(IWorkspaceManagementService.MSG_KEY_ORG_BY_ROLE, e.getMessage());
			wddxPacket = flashMessage.serializeMessage();
		}
		return returnWDDXPacket(wddxPacket, response);
	}
	
	public ActionForward getUsersFromOrganisationByRole(ActionMapping mapping,
			  ActionForm form,
			  HttpServletRequest request,
			  HttpServletResponse response)throws Exception{
		Integer organisationID = new Integer(WebUtil.readIntParam(request,"organisationID"));
		String role = WebUtil.readStrParam(request, "role");
		String wddxPacket = null;
		try {
			IWorkspaceManagementService workspaceManagementService = getWorkspaceManagementService();
			wddxPacket = workspaceManagementService.getUsersFromOrganisationByRole(organisationID, role);		
		} catch (Exception e) {
			log.error("getUsersFromOrganisationByRole: Exception occured. organisationID "+organisationID+" role "+role, e);
			FlashMessage flashMessage = FlashMessage.getExceptionOccured(IWorkspaceManagementService.MSG_KEY_USER_BY_ROLE, e.getMessage());
			wddxPacket = flashMessage.serializeMessage();
		}
		return returnWDDXPacket(wddxPacket, response);
	}

}

