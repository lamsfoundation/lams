/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.authoring.web.AuthoringConstants;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteAttachmentDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteNominationContentDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.tool.vote.util.VoteToolContentHandler;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * * @author Ozgur Demirtas
 * 
 * <p>Action class that controls the logic of tool behavior. </p>
 * 
 * <p>Note that Struts action class only has the responsibility to navigate 
 * page flow. All database operation should go to service layer and data 
 * transformation from domain model to struts form bean should go to form 
 * bean class. This ensure clean and maintainable code.
 * </p>
 * 
 * <code>SystemException</code> is thrown whenever an known error condition is
 * identified. No system exception error handling code should appear in the 
 * Struts action class as all of them are handled in 
 * <code>CustomStrutsExceptionHandler<code>.
 * 
 * 
	<!--Authoring Main Action : interacts with the authoring module user-->
	<action path="/authoring"
	      	type="org.lamsfoundation.lams.tool.vote.web.VoteAction"
    	  	name="VoteAuthoringForm"
	      	scope="request"
    	  	input="/authoring/AuthoringMaincontent.jsp"
      		parameter="dispatch">

	      <forward
	        name="load"
	        path="/authoring/AuthoringMaincontent.jsp"
		    redirect="false"
	      />
	    
	      <forward
	        name="starter"
	        path="/index.jsp"
		    redirect="false"
	      />
	      
	  	<forward
		    name="loadMonitoring"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="false"
	  	/>
	  	
	     <forward
	        name="preview"
	        path="/learning/Preview.jsp"
		    redirect="false"
	     />
	     
	    <forward
			name="newNominationBox"
			path="/authoring/newNominationBox.jsp"
			redirect="false"
	    />

	    <forward
			name="editNominationBox"
			path="/authoring/editNominationBox.jsp"
			redirect="false"
	    />
	  	
	  	<forward
		    name="errorList"
		    path="/VoteErrorBox.jsp"
		    redirect="false"
	  	/>
    </action>

 * 
*/
public class VoteAction extends LamsDispatchAction implements VoteAppConstants
{
	static Logger logger = Logger.getLogger(VoteAction.class.getName());
	
	private VoteToolContentHandler toolContentHandler;
	
	 /** 
     * <p>Default struts dispatch method.</p> 
     * 
     * <p>It is assuming that progress engine should pass in the tool access
     * mode and the tool session id as http parameters.</p>
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     *
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     * @throws VoteApplicationException the known runtime exception 
     * 
	 * unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
                                                            
	 * main content/question content management and workflow logic
	 * 
	 * if the passed toolContentID exists in the db, we need to get the relevant data into the Map 
	 * if not, create the default Map 
	*/
    public ActionForward unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
    {
    	VoteUtils.cleanUpUserExceptions(request);
	 	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	 	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
	 	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	 	SessionMap sessionMap= new SessionMap();
	 	VoteUtils.saveRichText(request, voteGeneralAuthoringDTO,sessionMap);	 	
	 	voteAuthoringForm.resetUserAction();
	 	return null;
    }

    
    /**
     * persists offline files
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws RepositoryCheckedException
     */
    public ActionForward submitOfflineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         RepositoryCheckedException
    {
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching submitOfflineFile...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	logger.debug("voteAuthoringForm :" +voteAuthoringForm);
    	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
    	
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);

    	
    	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
    	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
	    
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);

    	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
    	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
    	

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO, sessionMap);
	 	
		logger.debug("will uploadFile for offline file:");
 		VoteAttachmentDTO voteAttachmentDTO=AuthoringUtil.uploadFile(request, voteService, voteAuthoringForm, true, sessionMap);
 		logger.debug("returned voteAttachmentDTO:" + voteAttachmentDTO);
 		logger.debug("returned sessionMap:" + sessionMap);

 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		
 		if (voteAttachmentDTO != null)
 		{
 		   listOfflineFilesMetaData.add(voteAttachmentDTO);    
 		}
 		
 		logger.debug("listOfflineFilesMetaData after add:" + listOfflineFilesMetaData);
 		sessionMap.put(LIST_OFFLINEFILES_METADATA_KEY, listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);
 		
		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		
		
 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

 		if (voteAttachmentDTO == null)
 		{
 			ActionMessages errors= new ActionMessages();
 			errors= new ActionMessages();
 			voteGeneralAuthoringDTO.setUserExceptionFilenameEmpty(new Boolean(true).toString());
 			errors.add(Globals.ERROR_KEY,new ActionMessage("error.fileName.empty"));
 			saveErrors(request,errors);
 			voteAuthoringForm.resetUserAction();
 			persistInRequestError(request,"error.fileName.empty");

 			logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
 			request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

 	   	    return (mapping.findForward(destination));	
 		}
 		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);

 		
 		voteAuthoringForm.resetUserAction();
   	    return (mapping.findForward(destination));    
    }

    
    /**
     * persists online files
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws RepositoryCheckedException
     */
    public ActionForward submitOnlineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         RepositoryCheckedException
    {
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching submitOnlineFiles...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	logger.debug("voteAuthoringForm :" +voteAuthoringForm);
    	
    	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
    	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
  
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);

    	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);

	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);

    	
    	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
    	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
    	

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO, sessionMap);
	 	
		logger.debug("will uploadFile for online file:");
 		VoteAttachmentDTO voteAttachmentDTO=AuthoringUtil.uploadFile(request, voteService, voteAuthoringForm, false, sessionMap);
 		logger.debug("returned voteAttachmentDTO:" + voteAttachmentDTO);
 		logger.debug("returned sessionMap:" + sessionMap);

 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		
 		if (voteAttachmentDTO != null)
 		{
 		   listOnlineFilesMetaData.add(voteAttachmentDTO);    
 		}
 		logger.debug("listOnlineFilesMetaData after add:" + listOnlineFilesMetaData);
 		
 		sessionMap.put(LIST_ONLINEFILES_METADATA_KEY, listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);
 		
		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		
 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

 		if (voteAttachmentDTO == null)
 		{
 			ActionMessages errors= new ActionMessages();
 			errors= new ActionMessages();
 			voteGeneralAuthoringDTO.setUserExceptionFilenameEmpty(new Boolean(true).toString());
 			errors.add(Globals.ERROR_KEY,new ActionMessage("error.fileName.empty"));
 			saveErrors(request,errors);
 			voteAuthoringForm.resetUserAction();
 			persistInRequestError(request,"error.fileName.empty");

 			logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
 			request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

 		    return (mapping.findForward(destination));	
 		}

		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);

        voteAuthoringForm.resetUserAction();
   	    return (mapping.findForward(destination));
    }


    /**
     * removes an offline file from the jsp
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteOfflineFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException

    {
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching deleteOfflineFile...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	logger.debug("voteAuthoringForm :" +voteAuthoringForm);
    	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);

    	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
    	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
	    
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);
	    
    	
    	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
    	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
	 	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);
	 	
		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO, sessionMap);
	 	
	 	String uuid =voteAuthoringForm.getUuid();
	 	logger.debug("uuid:" + uuid);
	 	
	 	List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		listOfflineFilesMetaData=AuthoringUtil.removeFileItem(listOfflineFilesMetaData, uuid);
 		logger.debug("listOfflineFilesMetaData after remove:" + listOfflineFilesMetaData);
 		sessionMap.put(LIST_OFFLINEFILES_METADATA_KEY, listOfflineFilesMetaData);
 		
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		
 		List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);

 		
        voteAuthoringForm.resetUserAction();
    	
        return (mapping.findForward(destination));
    }

	/**
	 * deletes an online file from the jsp
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
    public ActionForward deleteOnlineFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException

    {
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching deleteOnlineFile...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	logger.debug("voteAuthoringForm :" +voteAuthoringForm);
    	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();

		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
    	
    	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
    	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
	    Map mapOptionsContent=(Map)sessionMap.get(MAP_OPTIONS_CONTENT_KEY);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
	    
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
    	
    	String firstEntry=(String)mapOptionsContent.get("1");
    	logger.debug("firstEntry: " +  firstEntry);
    	voteGeneralAuthoringDTO.setDefaultOptionContent(firstEntry);
	    

    	voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
    	voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		VoteUtils.saveRichText(request, voteGeneralAuthoringDTO, sessionMap);
	 	
	 	String uuid =voteAuthoringForm.getUuid();
	 	logger.debug("uuid:" + uuid);
	 	
	 	List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		listOnlineFilesMetaData=AuthoringUtil.removeFileItem(listOnlineFilesMetaData, uuid);
 		logger.debug("listOnlineFilesMetaData after remove:" + listOnlineFilesMetaData);
 		sessionMap.put(LIST_ONLINEFILES_METADATA_KEY, listOnlineFilesMetaData);
 		
 		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);

		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());
		
 		List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);

		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(httpSessionID, sessionMap);
 		
        voteAuthoringForm.resetUserAction();
        
        return (mapping.findForward(destination));
    }

    /**
     * used in define later mode to switch from view-only to editable screen
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
    {
    	logger.debug("dispatching editActivityQuestions...");
    	
    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
		logger.debug("voteAuthoringForm: " + voteAuthoringForm);
		
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
    	
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);

		String activeModule=DEFINE_LATER;
		
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
		
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

		
        logger.debug("title: " + voteContent.getTitle());
        logger.debug("instructions: " + voteContent.getInstructions());
        
        voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
        voteAuthoringForm.setTitle(voteContent.getTitle());
        
        voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());
        
        sessionMap.put(ACTIVITY_TITLE_KEY, voteContent.getTitle());
        sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, voteContent.getInstructions());
        
		voteAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
     	voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
     	
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);

		VoteUtils.setDefineLater(request, true, strToolContentID, voteService);

		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setCurrentTab("1");

	    List listNominationContentDTO= new  LinkedList();

		Iterator queIterator=voteContent.getVoteQueContents().iterator();
		while (queIterator.hasNext())
		{
		    VoteNominationContentDTO voteNominationContentDTO=new VoteNominationContentDTO();
		    
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			if (voteQueContent != null)
			{
				logger.debug("question: " + voteQueContent.getQuestion());
				logger.debug("displayorder: " + new Integer(voteQueContent.getDisplayOrder()).toString());
	    		
	    		voteNominationContentDTO.setQuestion(voteQueContent.getQuestion());
	    		voteNominationContentDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
	    		//voteNominationContentDTO.setFeedback(voteQueContent.getFeedback());
	    		listNominationContentDTO.add(voteNominationContentDTO);
			}
		}
		logger.debug("listNominationContentDTO: " + listNominationContentDTO);
		request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
		
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		request.getSession().setAttribute(httpSessionID, sessionMap);
		
		logger.debug("before fwding to jsp, voteAuthoringForm: " + voteAuthoringForm);
		logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

		logger.debug("forwarding to : " + LOAD_QUESTIONS);
		return mapping.findForward(LOAD_QUESTIONS);
    }


    /**
     * repopulateRequestParameters
     * reads and saves request parameters
     * 
     * @param request
     * @param voteAuthoringForm
     * @param voteGeneralAuthoringDTO
     */
    protected void repopulateRequestParameters(HttpServletRequest request, VoteAuthoringForm voteAuthoringForm, 
            VoteGeneralAuthoringDTO voteGeneralAuthoringDTO)
    {
        logger.debug("starting repopulateRequestParameters");
        
        String toolContentID=request.getParameter(TOOL_CONTENT_ID);
        logger.debug("toolContentID: " + toolContentID);
        voteAuthoringForm.setToolContentID(toolContentID);
        voteGeneralAuthoringDTO.setToolContentID(toolContentID);
        
        String activeModule=request.getParameter(ACTIVE_MODULE);
        logger.debug("activeModule: " + activeModule);
        voteAuthoringForm.setActiveModule(activeModule);
        voteGeneralAuthoringDTO.setActiveModule(activeModule);
        
        String httpSessionID=request.getParameter(HTTP_SESSION_ID);
        logger.debug("httpSessionID: " + httpSessionID);
        voteAuthoringForm.setHttpSessionID(httpSessionID);
        voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);

        String defineLaterInEditMode=request.getParameter(DEFINE_LATER_IN_EDIT_MODE);
        logger.debug("defineLaterInEditMode: " + defineLaterInEditMode);
        voteAuthoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
        voteGeneralAuthoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);
        
        String voteChangable=request.getParameter(VOTE_CHANGABLE);
        logger.debug("voteChangable: " + voteChangable);
        voteAuthoringForm.setVoteChangable(voteChangable);
        voteGeneralAuthoringDTO.setVoteChangable(voteChangable);
        
        String lockOnFinish=request.getParameter(LOCK_ON_FINISH);
        logger.debug("lockOnFinish: " + lockOnFinish);
        voteAuthoringForm.setLockOnFinish(lockOnFinish);
        voteGeneralAuthoringDTO.setLockOnFinish(lockOnFinish);
        
        String allowText=request.getParameter(ALLOW_TEXT);
        logger.debug("allowText: " + allowText);
        voteAuthoringForm.setAllowText(allowText);
        voteGeneralAuthoringDTO.setAllowText(allowText);
        
        String maxNominationCount=request.getParameter(MAX_NOMINATION_COUNT);
        logger.debug("maxNominationCount: " + maxNominationCount);
        voteAuthoringForm.setMaxNominationCount(maxNominationCount);
        voteGeneralAuthoringDTO.setMaxNominationCount(maxNominationCount);
        
		String reflect=request.getParameter("reflect");
		logger.debug("reflect: " + reflect);
		voteAuthoringForm.setReflect(reflect);
		voteGeneralAuthoringDTO.setReflect(reflect);

		String reflectionSubject=request.getParameter("reflectionSubject");
		logger.debug("reflectionSubject: " + reflectionSubject);
		voteAuthoringForm.setReflectionSubject(reflectionSubject);
		voteGeneralAuthoringDTO.setReflectionSubject(reflectionSubject);


		String offlineInstructions=request.getParameter(OFFLINE_INSTRUCTIONS);
		logger.debug("offlineInstructions: " + offlineInstructions);
		voteAuthoringForm.setOfflineInstructions(offlineInstructions);
		voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);

		String onlineInstructions=request.getParameter(ONLINE_INSTRUCTIONS);
		logger.debug("onlineInstructions: " + onlineInstructions);
		voteAuthoringForm.setOnlineInstructions(onlineInstructions);
		voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
    }    
    
            
    /**
     * persists error messages to request scope
     * @param request
     * @param message
     */
    public void persistInRequestError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
    
    
    /**
     * moveNominationDown
     * 
     * moves a nomination down in the authoring list
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveNominationDown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching moveNominationDown");
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
		
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);

	    
	    List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
	    logger.debug("listNominationContentDTO: " + listNominationContentDTO);
	    
	    listNominationContentDTO=AuthoringUtil.swapNodes(listNominationContentDTO, questionIndex, "down");
	    logger.debug("listNominationContentDTO after swap: " + listNominationContentDTO);
	    
	    listNominationContentDTO=AuthoringUtil.reorderSimpleListNominationContentDTO(listNominationContentDTO);
	    logger.debug("listNominationContentDTO after reordersimple: " + listNominationContentDTO);

	    sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String richTextTitle = request.getParameter(TITLE);
		logger.debug("richTextTitle: " + richTextTitle);
	    
		String richTextInstructions = request.getParameter(INSTRUCTIONS);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    
		
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));		
		logger.debug("voteContent: " + voteContent);

	
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
		logger.debug("activeModule: " + activeModule);
  		if (activeModule.equals(AUTHORING))
  		{
  		    String onlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
  		    logger.debug("onlineInstructions: " + onlineInstructions);
  		    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
  		
  		    String offlineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
  		    logger.debug("offlineInstructions: " + offlineInstructions);
  		    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);
  		    
  		    List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
  		    logger.debug("attachmentList: " + attachmentList);
  		    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
  		    logger.debug("deletedAttachmentList: " + deletedAttachmentList);
  		
  		    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
  		    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

  			String strOnlineInstructions= request.getParameter("onlineInstructions");
  			String strOfflineInstructions= request.getParameter("offlineInstructions");
  			logger.debug("onlineInstructions: " + strOnlineInstructions);
  			logger.debug("offlineInstructions: " + strOnlineInstructions);
  			voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
  			voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
  		}
	    
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    request.getSession().setAttribute(httpSessionID, sessionMap);
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setCurrentTab("1");
	
	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		logger.debug("listNominationContentDTO now: " + listNominationContentDTO);
	
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		

		logger.debug("fwd ing to LOAD_NOMINATIONS: " + LOAD_QUESTIONS);
	    return (mapping.findForward(LOAD_QUESTIONS));
    }


    /**
     * moveNominationUp
     * 
     * moves a nomination up in the authoring list
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveNominationUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispatching moveNominationUp");
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
		
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
	    
	    List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
	    logger.debug("listNominationContentDTO: " + listNominationContentDTO);
	    
	    listNominationContentDTO=AuthoringUtil.swapNodes(listNominationContentDTO, questionIndex, "up");
	    logger.debug("listNominationContentDTO after swap: " + listNominationContentDTO);
	    
	    listNominationContentDTO=AuthoringUtil.reorderSimpleListNominationContentDTO(listNominationContentDTO);
	    logger.debug("listNominationContentDTO after reordersimple: " + listNominationContentDTO);

	    sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String richTextTitle = request.getParameter(TITLE);
		logger.debug("richTextTitle: " + richTextTitle);
	    
		String richTextInstructions = request.getParameter(INSTRUCTIONS);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    
		
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));		
		logger.debug("voteContent: " + voteContent);

	
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
		logger.debug("activeModule: " + activeModule);
  		if (activeModule.equals(AUTHORING))
  		{
  		    String onlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
  		    logger.debug("onlineInstructions: " + onlineInstructions);
  		    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
  		
  		    String offlineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
  		    logger.debug("offlineInstructions: " + offlineInstructions);
  		    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);
  		    
  		    List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
  		    logger.debug("attachmentList: " + attachmentList);
  		    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
  		    logger.debug("deletedAttachmentList: " + deletedAttachmentList);
  		
  		    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
  		    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);

  			String strOnlineInstructions= request.getParameter("onlineInstructions");
  			String strOfflineInstructions= request.getParameter("offlineInstructions");
  			logger.debug("onlineInstructions: " + strOnlineInstructions);
  			logger.debug("offlineInstructions: " + strOnlineInstructions);
  			voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
  			voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
  		}
	    
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    request.getSession().setAttribute(httpSessionID, sessionMap);
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setCurrentTab("1");
	
	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		logger.debug("listNominationContentDTO now: " + listNominationContentDTO);
	
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		

		logger.debug("fwd ing to LOAD_NOMINATIONS: " + LOAD_QUESTIONS);
	    return (mapping.findForward(LOAD_QUESTIONS));
    }
    
    
    /**
     * removeNomination
     * 
     * removes a nomination from the authoring list
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
	logger.debug("dispatching removeNomination");
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
		
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
	    
	    List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
	    logger.debug("listNominationContentDTO: " + listNominationContentDTO);
	
	    VoteNominationContentDTO voteNominationContentDTO= null;
	    Iterator listIterator=listNominationContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        voteNominationContentDTO= (VoteNominationContentDTO)listIterator.next();
	        logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	        logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());
	        
	        String question=voteNominationContentDTO.getNomination();
	        String displayOrder=voteNominationContentDTO.getDisplayOrder();
	        logger.debug("displayOrder:" + displayOrder);
	        
	        if ((displayOrder != null) && (!displayOrder.equals("")))
			{
	            if (displayOrder.equals(questionIndex))
	            {
	                break;
	            }
	            
			}
	    }
	    
	    logger.debug("voteNominationContentDTO found:" + voteNominationContentDTO);
	    voteNominationContentDTO.setNomination("");
	    logger.debug("listNominationContentDTO after remove:" + listNominationContentDTO);
	  
	    
	    listNominationContentDTO=AuthoringUtil.reorderListNominationContentDTO(listNominationContentDTO, questionIndex );
	    logger.debug("listNominationContentDTO reordered:" + listNominationContentDTO);
	    
	
	    sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String richTextTitle = request.getParameter(TITLE);
		logger.debug("richTextTitle: " + richTextTitle);
	    
		String richTextInstructions = request.getParameter(INSTRUCTIONS);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    
		
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
	    logger.debug("activeModule: " + activeModule);
		if (activeModule.equals(AUTHORING))
		{
	        String onlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
	        logger.debug("onlineInstructions: " + onlineInstructions);
		    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);

	        String offlineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
	        logger.debug("offlineInstructions: " + offlineInstructions);
		    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);
		    
		    
	        List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
		    logger.debug("attachmentList: " + attachmentList);
		    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
		    logger.debug("deletedAttachmentList: " + deletedAttachmentList);

		    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
		    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);
		    
			String strOnlineInstructions= request.getParameter("onlineInstructions");
			String strOfflineInstructions= request.getParameter("offlineInstructions");
			logger.debug("onlineInstructions: " + strOnlineInstructions);
			logger.debug("offlineInstructions: " + strOnlineInstructions);
			voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
			voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
		}
	    
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    request.getSession().setAttribute(httpSessionID, sessionMap);
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setCurrentTab("1");
	
	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		logger.debug("voteNominationContentDTO now: " + voteNominationContentDTO);
	    logger.debug("listNominationContentDTO now: " + listNominationContentDTO);
	
	    voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	    
		logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
		
		logger.debug("fwd ing to LOAD_QUESTIONS: " + LOAD_QUESTIONS);
	    return (mapping.findForward(LOAD_QUESTIONS));
    }
    
    /**
     * newEditableNominationBox
     * 
     * enables editing a nomination
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newEditableNominationBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
        logger.debug("dispathcing newEditableNominationBox");
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
		
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);

		
		String questionIndex=request.getParameter("questionIndex");
		logger.debug("questionIndex: " + questionIndex);
		
		voteAuthoringForm.setEditableNominationIndex(questionIndex);
		
        List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
        logger.debug("listNominationContentDTO: " + listNominationContentDTO);

        String editableNomination="";
        String editableFeedback="";
	    Iterator listIterator=listNominationContentDTO.iterator();
	    while (listIterator.hasNext())
	    {
	        VoteNominationContentDTO voteNominationContentDTO= (VoteNominationContentDTO)listIterator.next();
	        logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	        logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());
	        String question=voteNominationContentDTO.getNomination();
	        String displayOrder=voteNominationContentDTO.getDisplayOrder();
	        
	        if ((displayOrder != null) && (!displayOrder.equals("")))
    		{
	            if (displayOrder.equals(questionIndex))
	            {
	                editableFeedback=voteNominationContentDTO.getFeedback();
	                editableNomination=voteNominationContentDTO.getNomination();
	                logger.debug("editableFeedback found :" + editableFeedback);
	                break;
	            }
	            
    		}
	    }
	    logger.debug("editableFeedback found :" + editableFeedback);
	    logger.debug("editableNomination found :" + editableNomination);

        
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);


		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
		

		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);

		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
		
		voteGeneralAuthoringDTO.setEditableNominationText(editableNomination);
		voteGeneralAuthoringDTO.setEditableNominationFeedback (editableFeedback);
		voteAuthoringForm.setFeedback(editableFeedback);

	    voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	    
		logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
		logger.debug("activeModule: " + activeModule);
	    if (activeModule.equals(AUTHORING))
	    {
			String strOnlineInstructions= request.getParameter("onlineInstructions");
			String strOfflineInstructions= request.getParameter("offlineInstructions");
			logger.debug("onlineInstructions: " + strOnlineInstructions);
			logger.debug("offlineInstructions: " + strOnlineInstructions);
			voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
			voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	    }
		
		logger.debug("fwd ing to editNominationBox: ");
        return (mapping.findForward("editNominationBox"));
    }


    /**
     * newNominationBox
     * 
     * enables adding a new nomination
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward newNominationBox(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
        logger.debug("dispathcing newNominationBox");
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
		
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);


		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);

		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
		
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);

		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
		logger.debug("activeModule: " + activeModule);
	    if (activeModule.equals(AUTHORING))
	    {
			String strOnlineInstructions= request.getParameter("onlineInstructions");
			String strOfflineInstructions= request.getParameter("offlineInstructions");
			logger.debug("onlineInstructions: " + strOnlineInstructions);
			logger.debug("offlineInstructions: " + strOnlineInstructions);
			voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
			voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	    }

	    
	    voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	    
		logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

	    
        List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
        logger.debug("listNominationContentDTO: " + listNominationContentDTO);

		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
		logger.debug("fwd ing to newNominationBox: ");
        return (mapping.findForward("newNominationBox"));
    }

    /**
     * addSingleNomination
     * 
     * enables adding a new nomination to the authoring nominations list
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
	public ActionForward addSingleNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispathcing addSingleNomination");
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));		
		logger.debug("voteContent: " + voteContent);
	
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		
		voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
		
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    
	    List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
	    logger.debug("listNominationContentDTO: " + listNominationContentDTO);
	    
	    String newNomination=request.getParameter("newNomination");
	    logger.debug("newNomination: " + newNomination);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);
	    
	    
	    int listSize=listNominationContentDTO.size();
	    logger.debug("listSize: " + listSize);
	    
	    if ((newNomination != null) && (newNomination.length() > 0))
	    {
	        boolean duplicates=AuthoringUtil.checkDuplicateNominations(listNominationContentDTO, newNomination);
	        logger.debug("duplicates: " + duplicates);
	        
	        if (!duplicates)
	        {
			    VoteNominationContentDTO voteNominationContentDTO=new VoteNominationContentDTO();
			    voteNominationContentDTO.setDisplayOrder(new Long(listSize+1).toString());
			    voteNominationContentDTO.setFeedback(feedback);
			    voteNominationContentDTO.setNomination(newNomination);
			    
			    listNominationContentDTO.add(voteNominationContentDTO);
			    logger.debug("updated listNominationContentDTO: " + listNominationContentDTO);	            
	        }
	        else
	        {
	            logger.debug("entry duplicate, not adding");

	        }
	    }
	    else
	    {
	        logger.debug("entry blank, not adding");

	    }
	    
	    
	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	
			
		String richTextTitle=(String)sessionMap.get(ACTIVITY_TITLE_KEY);
		logger.debug("richTextTitle: " + richTextTitle);
		String richTextInstructions=(String)sessionMap.get(ACTIVITY_INSTRUCTIONS_KEY);
		logger.debug("richTextInstructions: " + richTextInstructions);
		
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);

		
		logger.debug("activeModule: " + activeModule);
	    if (activeModule.equals(AUTHORING))
	    {
		    String onlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
		    logger.debug("onlineInstructions: " + onlineInstructions);
		    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
		
		    String offlineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
		    logger.debug("offlineInstructions: " + offlineInstructions);
		    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);
		    
		    List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
		    logger.debug("attachmentList: " + attachmentList);
		
		    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
		    logger.debug("deletedAttachmentList: " + deletedAttachmentList);
		
		    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
		    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);
		    
		    String strOnlineInstructions= request.getParameter("onlineInstructions");
			String strOfflineInstructions= request.getParameter("offlineInstructions");
			logger.debug("onlineInstructions: " + strOnlineInstructions);
			logger.debug("offlineInstructions: " + strOfflineInstructions);
			voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
			voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	    }
	
	
	    voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setCurrentTab("1");
		
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
	    
		logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	    
	    logger.debug("httpSessionID: " + httpSessionID);
	    logger.debug("sessionMap: " + sessionMap);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
	    logger.debug("voteGeneralAuthoringDTO.getMapNominationContent(); " + voteGeneralAuthoringDTO.getMapNominationContent());
	    
	    request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
	
		logger.debug("fwd ing to LOAD_QUESTIONS: " + LOAD_QUESTIONS);
	    return (mapping.findForward(LOAD_QUESTIONS));
	}
    
    
	/**
	 * saveSingleNomination
	 * saves a new or updated nomination in the authoring nominations list
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public ActionForward saveSingleNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		
		logger.debug("dispathcing saveSingleNomination");
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
	
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		
		String editNominationBoxRequest=request.getParameter("editNominationBoxRequest");
		logger.debug("editNominationBoxRequest: " + editNominationBoxRequest);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
		
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		
		voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
		
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    
        List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
        logger.debug("listNominationContentDTO: " + listNominationContentDTO);
        
        
	    String newNomination=request.getParameter("newNomination");
	    logger.debug("newNomination: " + newNomination);
	    
	    String feedback=request.getParameter("feedback");
	    logger.debug("feedback: " + feedback);

	    
	    String editableNominationIndex=request.getParameter("editableNominationIndex");
	    logger.debug("editableNominationIndex: " + editableNominationIndex);
	    

	    if ((newNomination != null) && (newNomination.length() > 0))
	    {
		        if ((editNominationBoxRequest != null) && (editNominationBoxRequest.equals("false")))
		        {
		            logger.debug("request for add and save");
			        boolean duplicates=AuthoringUtil.checkDuplicateNominations(listNominationContentDTO, newNomination);
			        logger.debug("duplicates: " + duplicates);
		            
			        if (!duplicates)
			        {
					    VoteNominationContentDTO voteNominationContentDTO= null;
					    Iterator listIterator=listNominationContentDTO.iterator();
					    while (listIterator.hasNext())
					    {
					        voteNominationContentDTO= (VoteNominationContentDTO)listIterator.next();
					        logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
					        logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getQuestion());
					        
					        String question=voteNominationContentDTO.getNomination();
					        String displayOrder=voteNominationContentDTO.getDisplayOrder();
					        logger.debug("displayOrder:" + displayOrder);
					        
					        if ((displayOrder != null) && (!displayOrder.equals("")))
				    		{
					            if (displayOrder.equals(editableNominationIndex))
					            {
					                break;
					            }
					            
				    		}
					    }
					    logger.debug("voteNominationContentDTO found:" + voteNominationContentDTO);
					    
					    voteNominationContentDTO.setQuestion(newNomination);
					    voteNominationContentDTO.setFeedback(feedback);
					    voteNominationContentDTO.setDisplayOrder(editableNominationIndex);
					    
					    listNominationContentDTO=AuthoringUtil.reorderUpdateListNominationContentDTO(listNominationContentDTO, voteNominationContentDTO, editableNominationIndex);
					    logger.debug("post reorderUpdateListNominationContentDTO listNominationContentDTO: " + listNominationContentDTO);
			        }
			        else
			        {
			            logger.debug("duplicate question entry, not adding");
			        }
		        }
			    else
			    {
			        	logger.debug("request for edit and save.");
					    VoteNominationContentDTO voteNominationContentDTO= null;
					    Iterator listIterator=listNominationContentDTO.iterator();
					    while (listIterator.hasNext())
					    {
					        voteNominationContentDTO= (VoteNominationContentDTO)listIterator.next();
					        logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
					        logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());
					        
					        String question=voteNominationContentDTO.getNomination();
					        String displayOrder=voteNominationContentDTO.getDisplayOrder();
					        logger.debug("displayOrder:" + displayOrder);
					        
					        if ((displayOrder != null) && (!displayOrder.equals("")))
				    		{
					            if (displayOrder.equals(editableNominationIndex))
					            {
					                break;
					            }
					            
				    		}
					    }
					    logger.debug("voteNominationContentDTO found:" + voteNominationContentDTO);
					    
					    voteNominationContentDTO.setNomination(newNomination);
					    voteNominationContentDTO.setFeedback(feedback);
					    voteNominationContentDTO.setDisplayOrder(editableNominationIndex);
					    
					    listNominationContentDTO=AuthoringUtil.reorderUpdateListNominationContentDTO(listNominationContentDTO, voteNominationContentDTO, editableNominationIndex);
					    logger.debug("post reorderUpdateListNominationContentDTO listNominationContentDTO: " + listNominationContentDTO);			        
			  }
	    }
        else
        {
            logger.debug("entry blank, not adding");
        }
	    

	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    logger.debug("listNominationContentDTO now: " + listNominationContentDTO);
	    
	    
		String richTextTitle=(String)sessionMap.get(ACTIVITY_TITLE_KEY);
		logger.debug("richTextTitle: " + richTextTitle);
		String richTextInstructions=(String)sessionMap.get(ACTIVITY_INSTRUCTIONS_KEY);
		logger.debug("richTextInstructions: " + richTextInstructions);
		
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	
	    
	    logger.debug("activeModule: " + activeModule);
	    if (activeModule.equals(AUTHORING))
	    {
		    String onlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
		    logger.debug("onlineInstructions: " + onlineInstructions);
		    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
		
		    String offlineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
		    logger.debug("offlineInstructions: " + offlineInstructions);
		    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);
		    
		    List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
		    logger.debug("attachmentList: " + attachmentList);
		
		    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
		    logger.debug("deletedAttachmentList: " + deletedAttachmentList);
		
		    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
		    voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);
		    
			String strOnlineInstructions= request.getParameter("onlineInstructions");
			String strOfflineInstructions= request.getParameter("offlineInstructions");
			logger.debug("onlineInstructions: " + strOnlineInstructions);
			logger.debug("offlineInstructions: " + strOfflineInstructions);
			voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
			voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);

	    }
	    
	
	    voteGeneralAuthoringDTO.setEditActivityEditMode(new Boolean(true).toString());
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setCurrentTab("1");
     	
		voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	    
	    logger.debug("httpSessionID: " + httpSessionID);
	    logger.debug("sessionMap: " + sessionMap);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    logger.debug("voteGeneralAuthoringDTO.getMapNominationContent(); " + voteGeneralAuthoringDTO.getMapNominationContent());
		
	    request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
	    
		
		logger.debug("fwd ing to LOAD_QUESTIONS: " + LOAD_QUESTIONS);
	    return (mapping.findForward(LOAD_QUESTIONS));
	}

	/**
	 * submitAllContent
	 * 
	 * persists the nominations list and other user selections in the db.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
		logger.debug("dispathcing submitAllContent :" +form);
		
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
	
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
	
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
	
		
	    List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
	    logger.debug("listNominationContentDTO: " + listNominationContentDTO);
	    
	    Map mapNominationContent=AuthoringUtil.extractMapNominationContent(listNominationContentDTO);
	    logger.debug("extracted mapNominationContent: " + mapNominationContent);
	            
	
	    Map mapFeedback=AuthoringUtil.extractMapFeedback(listNominationContentDTO);
	    logger.debug("extracted mapFeedback: " + mapFeedback);
	
	    ActionMessages errors = new ActionMessages();
	    logger.debug("mapNominationContent size: " + mapNominationContent.size());
	    
	    if (mapNominationContent.size() == 0)
	    {
	        ActionMessage error = new ActionMessage("nominations.none.submitted");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	    }
	    
	    
	    String maxNomCount=voteAuthoringForm.getMaxNominationCount();
	    logger.debug("maxNomCount:" + maxNomCount);
	    
	    if (activeModule != null)
	    {
		    if (activeModule.equals(AUTHORING))
		    {
			    if (maxNomCount != null)
			    {
				    if (maxNomCount.equals("0"))
				    {
				        ActionMessage error = new ActionMessage("maxNomination.invalid");
						errors.add(ActionMessages.GLOBAL_MESSAGE, error);				        
				    }
				    
			    	try
					{
			    		int intMaxNomCount=new Integer(maxNomCount).intValue();
				    	logger.debug("intMaxNomCount : " +intMaxNomCount);
					}
			    	catch(NumberFormatException e)
					{
				        ActionMessage error = new ActionMessage("maxNomination.invalid");
						errors.add(ActionMessages.GLOBAL_MESSAGE, error);				        
					}
			    }
		    }
	    }
	    
	    boolean nominationsDuplicate=AuthoringUtil.verifyDuplicateNominations(mapNominationContent);
	    logger.debug("nominationsDuplicate :" +nominationsDuplicate);
	    
	    if (nominationsDuplicate == true)
	    {
	        ActionMessage error = new ActionMessage("nominations.duplicate");
			errors.add(ActionMessages.GLOBAL_MESSAGE, error);				        
	    }
	    
	    logger.debug("errors: " + errors);
	    
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	 	
	 	VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
	 	repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	 	
	 	
	    logger.debug("activeModule: " + activeModule);
	    if (activeModule.equals(AUTHORING))
	    {
			List attachmentListBackup= new  ArrayList();
	        List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
		    logger.debug("attachmentList: " + attachmentList);
		    attachmentListBackup=attachmentList;
		    
		    List deletedAttachmentListBackup= new  ArrayList();
		    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
		    logger.debug("deletedAttachmentList: " + deletedAttachmentList);
		    deletedAttachmentListBackup=deletedAttachmentList;
		    
	        String onlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
	        logger.debug("onlineInstructions: " + onlineInstructions);
		    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
	
	        String offlineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
	        logger.debug("offlineInstructions: " + offlineInstructions);
		    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);
	
		    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
			voteGeneralAuthoringDTO.setDeletedAttachmentList(deletedAttachmentList);
			
			String strOnlineInstructions= request.getParameter("onlineInstructions");
			String strOfflineInstructions= request.getParameter("offlineInstructions");
			logger.debug("onlineInstructions: " + strOnlineInstructions);
			logger.debug("offlineInstructions: " + strOfflineInstructions);
			voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
			voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
	
	    }
	 	
	 	
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
	 	String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	 	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
	    voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
	    
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	    
	    voteGeneralAuthoringDTO.setMapNominationContent(mapNominationContent);
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
	    
		logger.debug("voteGeneralAuthoringDTO now: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
	 	logger.debug("there are no issues with input, continue and submit data");
	
	    VoteContent voteContentTest=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContentTest: " + voteContentTest);
	
		logger.debug("errors: " + errors);
	 	if(!errors.isEmpty()){
			saveErrors(request, errors);
	        logger.debug("errors saved: " + errors);
		}
	 	
	
	 	VoteContent voteContent=voteContentTest;
	 	if(errors.isEmpty()){
	 	    logger.debug("errors is empty: " + errors);
		 	/*to remove deleted entries in the questions table based on mapNominationContent */
	        authoringUtil.removeRedundantNominations(mapNominationContent, voteService, voteAuthoringForm, request, strToolContentID);
	        logger.debug("end of removing unused entries... ");
	
	        voteContent=authoringUtil.saveOrUpdateVoteContent(mapNominationContent, mapFeedback, voteService, voteAuthoringForm, request, voteContentTest, strToolContentID);
	        logger.debug("voteContent: " + voteContent);
	        
	        
	        long defaultContentID=0;
			logger.debug("attempt retrieving tool with signatute : " + MY_SIGNATURE);
	        defaultContentID=voteService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
			logger.debug("retrieved tool default contentId: " + defaultContentID);
			
	        if (voteContent != null)
	        {
	            voteGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
	        }
			logger.debug("updated voteGeneralAuthoringDTO to: " + voteGeneralAuthoringDTO);
			
		    authoringUtil.reOrganizeDisplayOrder(mapNominationContent, voteService, voteAuthoringForm, voteContent);    
	
	        logger.debug("activeModule: " + activeModule);
	        if (activeModule.equals(AUTHORING))
	        {
	    	    logger.debug("since it is authoring save the attachments: ");
	
	            List attachmentList= (List)sessionMap.get(ATTACHMENT_LIST_KEY);
	    	    logger.debug("attachmentList: " + attachmentList);
	
	    	    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
	
	            List attachments=saveAttachments(voteContent,attachmentList, deletedAttachmentList, mapping, request);
	            logger.debug("attachments: " + attachments);
	        }
	        
		    logger.debug("strToolContentID: " + strToolContentID);
	        VoteUtils.setDefineLater(request, false, strToolContentID, voteService);
	        logger.debug("define later set to false");
	        
			if (activeModule.equals(AUTHORING))
			{
		        logger.debug("standard authoring close");
			    request.setAttribute(AuthoringConstants.LAMS_AUTHORING_SUCCESS_FLAG,Boolean.TRUE);
			    voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
			}
			else
			{
			    logger.debug("go back to view only screen");
			    voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
			}
	        
	 	}
	 	else
	 	{
	 	   logger.debug("errors is not empty: " + errors);
	 	   
	 	   if (voteContent != null)
	 	   {
		        long defaultContentID=0;
				logger.debug("attempt retrieving tool with signatute : " + MY_SIGNATURE);
		        defaultContentID=voteService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
				logger.debug("retrieved tool default contentId: " + defaultContentID);
				
		        if (voteContent != null)
		        {
		            voteGeneralAuthoringDTO.setDefaultContentIdStr(new Long(defaultContentID).toString());
		        }
	
	 	   }
	 	}
	    
	
	    voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(1).toString());
	    
	    voteAuthoringForm.resetUserAction();
	    voteGeneralAuthoringDTO.setMapNominationContent(mapNominationContent);
	    
	    logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	
		request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	    
	    request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setCurrentTab("1");
	    
		logger.debug("forwarding to :" + LOAD_QUESTIONS);
	    return mapping.findForward(LOAD_QUESTIONS);
    }
	

    
    private VoteToolContentHandler getToolContentHandler()
    {
        if ( toolContentHandler == null ) {
              WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(getServlet().getServletContext());
              toolContentHandler = (VoteToolContentHandler) wac.getBean("voteToolContentHandler");
            }
            return toolContentHandler;
    }
    
    /**
     * saveAttachments
     * 
     * persists uploaded files
     * 
     * @param voteContent
     * @param attachmentList
     * @param deletedAttachmentList
     * @param mapping
     * @param request
     * @return
     */
    private List saveAttachments (VoteContent voteContent, 
            List attachmentList, List deletedAttachmentList,
            ActionMapping mapping, HttpServletRequest request) {

    	logger.debug("start saveAttachments, attachmentList " + attachmentList);
    	logger.debug("start deletedAttachmentList, deletedAttachmentList " + deletedAttachmentList);
    	
        if(attachmentList==null || deletedAttachmentList==null)
            return null;
        
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
        logger.debug("voteService: " + voteService);
        
        if ( deletedAttachmentList != null ) {
        	logger.debug("deletedAttachmentList is iterated...");
            Iterator iter = deletedAttachmentList.iterator();
            while (iter.hasNext()) {
                VoteUploadedFile attachment = (VoteUploadedFile) iter.next();
                logger.debug("attachment: " + attachment);
                
                if ( attachment.getSubmissionId() != null ) {
                    voteService.removeFile(attachment.getSubmissionId());
                }
            }
            deletedAttachmentList.clear();
            logger.error("cleared attachment list.");
        }
        
        
        if ( attachmentList != null ) 
        {
        	logger.debug("attachmentList is iterated...");
            Iterator iter = attachmentList.iterator();
            while (iter.hasNext()) {
                VoteUploadedFile attachment = (VoteUploadedFile) iter.next();
            	logger.debug("attachment: " + attachment);
            	logger.debug("attachment submission id: " + attachment.getSubmissionId());

                if ( attachment.getSubmissionId() == null ) {
                    /* add entry to tool table - file already in content repository */
                	logger.debug("calling persistFile with  attachment: " + attachment);
                    voteService.persistFile(voteContent, attachment);
                }
            }
        }
            
        return deletedAttachmentList;
    }
    
    
    /**
     * deleteFile
     * 
     * removes an uploaded file
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
        logger.debug("dispatching deleteFile");
    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
        
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

        String httpSessionID=request.getParameter(HTTP_SESSION_ID);
        logger.debug("httpSessionID: " + httpSessionID);
        voteAuthoringForm.setHttpSessionID(httpSessionID);

		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
		
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);


		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		List listQuestionContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
        logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
        
        request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listQuestionContentDTO);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
		

		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO(); 
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		
		repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
		
		voteGeneralAuthoringDTO.setSbmtSuccess( new Integer(0).toString());
        

		String onlineInstructions=(String) sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
		logger.debug("onlineInstructions: " + onlineInstructions);

		String offlineInstructions=(String) sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
		logger.debug("offlineInstructions: " + offlineInstructions);
		
		
		logger.debug("onlineInstructions: " + onlineInstructions);
		logger.debug("offlineInstructions: " + offlineInstructions);
		voteAuthoringForm.setOnlineInstructions(onlineInstructions);
		voteAuthoringForm.setOfflineInstructions(offlineInstructions);
	    voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
	    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);


	    String richTextTitle=(String)sessionMap.get(ACTIVITY_TITLE_KEY);
	    String richTextInstructions=(String)sessionMap.get(ACTIVITY_INSTRUCTIONS_KEY);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
		
		sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);		
		
		
        long uuid = WebUtil.readLongParam(request, UUID);

	    List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
	    logger.debug("attachmentList: " + attachmentList);
	    
	    if(attachmentList == null)
	        attachmentList = new ArrayList();


	    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
	    logger.debug("deletedAttachmentList: " + deletedAttachmentList);

	    if(deletedAttachmentList == null)
            deletedAttachmentList = new ArrayList();
        
        /* move the file's details from the attachment collection to the deleted attachments collection
         the attachment will be delete on saving. */
	    
        deletedAttachmentList = VoteUtils.moveToDelete(Long.toString(uuid), attachmentList, deletedAttachmentList );
        logger.debug("post moveToDelete, attachmentList: " + attachmentList);
	    logger.debug("post moveToDelete, deletedAttachmentList: " + deletedAttachmentList);
        

        sessionMap.put(ATTACHMENT_LIST_KEY,attachmentList);
        sessionMap.put(DELETED_ATTACHMENT_LIST_KEY,deletedAttachmentList);
        
	    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);

		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setCurrentTab("3");
		

		logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);

		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listQuestionContentDTO.size()));
		
        voteAuthoringForm.resetUserAction();
        logger.debug("fwd ing to LOAD_QUESTIONS: " + LOAD_QUESTIONS);
        
        return (mapping.findForward(LOAD_QUESTIONS));
    }

    
    /**
     * addNewFile
     * 
     * adds a new uploaded file to the uploadedfiles list
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addNewFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
	    logger.debug("dispathching addNewFile");
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	    
		IVoteService voteService =VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
	
		String httpSessionID=voteAuthoringForm.getHttpSessionID();
		logger.debug("httpSessionID: " + httpSessionID);
		
		SessionMap sessionMap=(SessionMap)request.getSession().getAttribute(httpSessionID);
		logger.debug("sessionMap: " + sessionMap);
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
	
		String activeModule=request.getParameter(ACTIVE_MODULE);
		logger.debug("activeModule: " + activeModule);
		
		String onlineInstructions=request.getParameter(ONLINE_INSTRUCTIONS);
		logger.debug("onlineInstructions: " + onlineInstructions);
		
		String offlineInstructions=request.getParameter(OFFLINE_INSTRUCTIONS);
		logger.debug("offlineInstructions: " + offlineInstructions);
		
		sessionMap.put(ONLINE_INSTRUCTIONS_KEY, onlineInstructions);
		sessionMap.put(OFFLINE_INSTRUCTIONS, offlineInstructions);
	
	    List listNominationContentDTO=(List)sessionMap.get(LIST_NOMINATION_CONTENT_DTO_KEY);
	    logger.debug("listNominationContentDTO: " + listNominationContentDTO);
	    
	    request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		
		String strToolContentID=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		logger.debug("strToolContentID: " + strToolContentID);
		
		VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentID));
		logger.debug("voteContent: " + voteContent);
	
		
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
		
		voteGeneralAuthoringDTO.setOnlineInstructions(onlineInstructions);
	    voteGeneralAuthoringDTO.setOfflineInstructions(offlineInstructions);
	    
	    repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	
		voteGeneralAuthoringDTO.setSbmtSuccess(new Integer(0).toString());
		
		String richTextTitle = request.getParameter(TITLE);
	    String richTextInstructions = request.getParameter(INSTRUCTIONS);
	
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    voteGeneralAuthoringDTO.setActivityTitle(richTextTitle);
	    voteAuthoringForm.setTitle(richTextTitle);
	    
		voteGeneralAuthoringDTO.setActivityInstructions(richTextInstructions);
			
	    sessionMap.put(ACTIVITY_TITLE_KEY, richTextTitle);
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, richTextInstructions);
	
	    List attachmentList=(List)sessionMap.get(ATTACHMENT_LIST_KEY);
	    logger.debug("attachmentList: " + attachmentList);
	    List deletedAttachmentList=(List)sessionMap.get(DELETED_ATTACHMENT_LIST_KEY);
	    logger.debug("deletedAttachmentList: " + deletedAttachmentList);
	    
	    addFileToContentRepository(request, voteAuthoringForm, attachmentList, deletedAttachmentList, sessionMap, voteGeneralAuthoringDTO);
	    logger.debug("post addFileToContentRepository, attachmentList: " + attachmentList);
	    logger.debug("post addFileToContentRepository, deletedAttachmentList: " + deletedAttachmentList);
	
	    sessionMap.put(ATTACHMENT_LIST_KEY,attachmentList);
	    sessionMap.put(DELETED_ATTACHMENT_LIST_KEY,deletedAttachmentList);
	    
	    voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
	    
	    request.getSession().setAttribute(httpSessionID, sessionMap);
	
		
		voteGeneralAuthoringDTO.setToolContentID(strToolContentID);
		voteGeneralAuthoringDTO.setHttpSessionID(httpSessionID);
		voteGeneralAuthoringDTO.setActiveModule(activeModule);
		
		voteAuthoringForm.setToolContentID(strToolContentID);
		voteAuthoringForm.setHttpSessionID(httpSessionID);
		voteAuthoringForm.setActiveModule(activeModule);
		voteAuthoringForm.setCurrentTab("3");
		
	
		logger.debug("before saving final voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	
	    voteAuthoringForm.resetUserAction();
		
	    String strOnlineInstructions= request.getParameter("onlineInstructions");
		String strOfflineInstructions= request.getParameter("offlineInstructions");
		logger.debug("onlineInstructions: " + strOnlineInstructions);
		logger.debug("offlineInstructions: " + strOnlineInstructions);
		voteAuthoringForm.setOnlineInstructions(strOnlineInstructions);
		voteAuthoringForm.setOfflineInstructions(strOfflineInstructions);
	
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
	    logger.debug("fwd ing to LOAD_QUESTIONS: " + LOAD_QUESTIONS);
	    return (mapping.findForward(LOAD_QUESTIONS));
    }

    
    /**
     * addFileToContentRepository
     * 
     * persists files data in the content repository
     * 
     * @param request
     * @param voteAuthoringForm
     * @param attachmentList
     * @param deletedAttachmentList
     * @param sessionMap
     * @param voteGeneralAuthoringDTO
     */
    public void addFileToContentRepository(HttpServletRequest request, VoteAuthoringForm voteAuthoringForm, 
            List attachmentList, List deletedAttachmentList, SessionMap sessionMap, 
            VoteGeneralAuthoringDTO voteGeneralAuthoringDTO)
    {
        logger.debug("attempt addFileToContentRepository");
        logger.debug("attachmentList: " + attachmentList);
        logger.debug("deletedAttachmentList: " + deletedAttachmentList);
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
        logger.debug("voteService: " + voteService);
        
        if(attachmentList == null)
            attachmentList = new ArrayList();
        
        if(deletedAttachmentList == null)
            deletedAttachmentList = new ArrayList();
        
        FormFile uploadedFile = null;
        boolean isOnlineFile = false;
        String fileType = null;
        if(voteAuthoringForm.getTheOfflineFile() != null && voteAuthoringForm.getTheOfflineFile().getFileSize() > 0 ){
            logger.debug("theOfflineFile is available: ");
            uploadedFile = voteAuthoringForm.getTheOfflineFile();
            logger.debug("uploadedFile: " + uploadedFile);
            fileType = IToolContentHandler.TYPE_OFFLINE;
        }
        else if(voteAuthoringForm.getTheOnlineFile() != null && voteAuthoringForm.getTheOnlineFile().getFileSize() > 0 ){
            logger.debug("theOnlineFile is available: ");
            uploadedFile = voteAuthoringForm.getTheOnlineFile();
            logger.debug("uploadedFile: " + uploadedFile);
            isOnlineFile = true;
            fileType = IToolContentHandler.TYPE_ONLINE;
        }
        else
            /*no file uploaded*/
            return;
        
        logger.debug("uploadedFile.getFileName(): " + uploadedFile.getFileName());
        
        /* if a file with the same name already exists then move the old one to deleted */
        deletedAttachmentList = VoteUtils.moveToDelete(uploadedFile.getFileName(), isOnlineFile, attachmentList, deletedAttachmentList );
        logger.debug("deletedAttachmentList: " + deletedAttachmentList);

        try
        {
            /* This is a new file and so is saved to the content repository. Add it to the 
             attachments collection, but don't add it to the tool's tables yet.
             */
            NodeKey node = getToolContentHandler().uploadFile(uploadedFile.getInputStream(), uploadedFile.getFileName(), 
                    uploadedFile.getContentType(), fileType); 
            VoteUploadedFile file = new VoteUploadedFile();
            String fileName=uploadedFile.getFileName();
            logger.debug("fileName: " + fileName);
            logger.debug("fileName length: " + fileName.length());
            
            if ((fileName != null) && (fileName.length() > 30))
            {
                fileName=fileName.substring(0, 31);
                logger.debug("shortened fileName: " + fileName);
            }
            	
            file.setFileName(fileName);
            file.setFileOnline(isOnlineFile);
            file.setUuid(node.getUuid().toString());
            /* file.setVersionId(node.getVersion()); */ 
            
            /* add the files to the attachment collection - if one existed, it should have already been removed. */
            attachmentList.add(file);
            
            /* reset the fields so that more files can be uploaded */
            voteAuthoringForm.setTheOfflineFile(null);
            voteAuthoringForm.setTheOnlineFile(null);
        }
        catch (FileNotFoundException e) {
            logger.error("Unable to uploadfile",e);
            throw new RuntimeException("Unable to upload file, exception was "+e.getMessage());
        } catch (IOException e) {
            logger.error("Unable to uploadfile",e);
            throw new RuntimeException("Unable to upload file, exception was "+e.getMessage());
        } catch (RepositoryCheckedException e) {
            logger.error("Unable to uploadfile",e);
            throw new RuntimeException("Unable to upload file, exception was "+e.getMessage());
        }
    }
}
    
