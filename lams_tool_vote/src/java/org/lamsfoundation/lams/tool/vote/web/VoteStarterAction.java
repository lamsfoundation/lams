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

/**
 * @author Ozgur Demirtas
 * 
 * VoteStarterAction loads the default content and initializes the presentation Map.
 * Initializes the tool's authoring mode
 * Requests can come either from authoring environment or from the monitoring environment for Edit Activity screen.
 * 
 * 
 * <THIS SECTION IS COPIED FROM: http://lamscvs.melcoe.mq.edu.au:8090/display/lams/Tool+Contract>
 * Authoring URL 
 *
 * The tool must supply an authoring module, which will be called to create new content or edit existing content. It will be called by an authoring URL using the following format:

    * <lams base path>/<tool's authoringurl>&toolContentID=123


 * The initial data displayed on the authoring screen for a new tool content id may be the default tool content.
 *
 * Authoring UI data consists of general Activity data fields and the Tool specific data fields.
 * The authoring interface will have three tabs. The mandatory (and suggested) fields are given. 
 * Each tool will have its own fields which it will add on any of the three tabs, as appropriate to the tabs' function.

 * Basic: Displays the basic set of fields that are needed for the tool, and it could be expected that a new LAMS user would use. 
 * Mandatory fields: Title, Instructions.
 * 
 * Advanced: Displays the extra fields that would be used by experienced LAMS users. Optional fields: Lock On Finish, Make Responses Anonymous
 * Instructions: Displays the "instructions" fields for teachers. Mandatory fields: Online instructions, Offline instructions, Document upload. 
 * See Instructions. The standard LAMS tools will use a DHTML layout for the Authoring tabs. For consistency of look and feel, 
 * we prefer all tools to use the DHTML, or at least make the tabs look and behave like the DHTML layout. The javascript for the tabs is 
 * available as "/include/javascript/tabcontroller.js" from the central web app (e.g. http://blah.org/lams/include/javascript/tabcontroller.js).
 *
 * The "Define Later" and "Run Offline" options are set on the Flash authoring part, and not on the tool's authoring screens.
 * Preview The tool must be able to show the specified content as if it was running in a lesson. It will be the learner url with tool access 
 * mode set to ToolAccessMode.AUTHOR.
 *
 * Export The tool must be able to export its tool content for part of the overall learning design export.
 * The format of the serialization for export is XML. Tool will define extra namespace inside the <Content> element to add a new data element (type).
 *  Inside the data element, it can further define more structures and types as it seems fit.

 * The data elements must be "version" aware. The data elements must be "type" aware if they are to be shared between Tools.
 * LAMS Xpress (Ernie, could you put something in here. You explain it better than I do!)
 *
 * Data Exchange At present, there is no data exchange format between tools. Therefore if..  
 * 
 * 
 * Tool path The URL path for the tool should be <lamsroot>/tool/$TOOL_SIG.  
 * 

	The author is given warnings when the content in use by learners OR when the content is being edited in the Monitoring interface.
	
	<!--Authoring Starter Action: initializes the authoring module -->
   <action path="/authoringStarter" 
   			type="org.lamsfoundation.lams.tool.vote.web.VoteStarterAction" 
   			name="VoteAuthoringForm" 
	      	scope="request"
   			input="/index.jsp"> 
	
	  	<forward
		    name="load"
		    path="/authoring/AuthoringMaincontent.jsp"
		    redirect="false"
	  	/>

	  	<forward
		    name="loadMonitoring"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="false"
	  	/>

	  	<forward
		    name="loadMonitoringEditActivity"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="false"
	  	/>
	  	
	  	<forward
		    name="loadLearner"
		    path="/learning/AnswersContent.jsp"
		    redirect="false"
	  	/>

	  	<forward
	        name="starter"
	        path="/index.jsp"
		    redirect="false"
	     />
	     
	     <forward
	        name="preview"
	        path="/learning/Preview.jsp"
		    redirect="false"
	     />
	  	
	  	<forward
		    name="errorList"
		    path="/VoteErrorBox.jsp"
		    redirect="false"
	  	/>
	</action>  
	

*/

package org.lamsfoundation.lams.tool.vote.web;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteNominationContentDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;


public class VoteStarterAction extends Action implements VoteAppConstants {
	/*
	 * This class is reused by defineLater and monitoring modules as well. 
	 */
	static Logger logger = Logger.getLogger(VoteStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, VoteApplicationException {

		VoteUtils.cleanUpSessionAbsolute(request);
		logger.debug("init authoring mode. removed attributes...");
		
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO = new VoteGeneralAuthoringDTO();
		
		String contentFolderID = WebUtil.readStrParam(request, AttributeNames.PARAM_CONTENT_FOLDER_ID);
		logger.debug("contentFolderID: " + contentFolderID);
		voteAuthoringForm.setContentFolderID(contentFolderID);
		
		VoteAction voteAction= new VoteAction();
		voteAction.repopulateRequestParameters(request, voteAuthoringForm, voteGeneralAuthoringDTO);
	

		
		logger.debug("getting voteService now: servlet is: " + getServlet());
		IVoteService voteService=null;
		if (getServlet() != null)
		    voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		else
		    voteService=voteAuthoringForm.getVoteService();
		
		logger.debug("final voteService: " + voteService);
		    
	    voteAuthoringForm.setSubmissionAttempt(new Boolean(false).toString());
	    voteAuthoringForm.setSbmtSuccess(new Boolean(false).toString());
	    
	    voteGeneralAuthoringDTO.setSubmissionAttempt(new Boolean(false).toString());
	    voteGeneralAuthoringDTO.setSbmtSuccess(new Boolean(false).toString());
	    voteGeneralAuthoringDTO.setContentFolderID(contentFolderID);
	    
	    String servletPath=request.getServletPath();
		logger.debug("getServletPath: "+ servletPath);
		if (servletPath.indexOf("authoringStarter") > 0)
		{
			logger.debug("request is for authoring module");
			voteAuthoringForm.setActiveModule(AUTHORING);
			voteGeneralAuthoringDTO.setActiveModule(AUTHORING);
			
			voteAuthoringForm.setDefineLaterInEditMode(new Boolean(true).toString());
			voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		}
		else
		{
			logger.debug("request is for define later module. either direct or by monitoring module");
			voteAuthoringForm.setActiveModule(DEFINE_LATER);
			voteGeneralAuthoringDTO.setActiveModule(DEFINE_LATER);
			
			voteAuthoringForm.setDefineLaterInEditMode(new Boolean(false).toString());
			voteGeneralAuthoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
		}
		 
		initialiseAttributes(request, voteGeneralAuthoringDTO, voteService);
		
		
	    SessionMap sessionMap = new SessionMap();
	    sessionMap.put(ATTACHMENT_LIST_KEY, new ArrayList());
	    sessionMap.put(DELETED_ATTACHMENT_LIST_KEY, new ArrayList());
	    sessionMap.put(ACTIVITY_TITLE_KEY, "");
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, "");
	    voteAuthoringForm.setHttpSessionID(sessionMap.getSessionID());
	    voteGeneralAuthoringDTO.setHttpSessionID(sessionMap.getSessionID());
		
    	/* determine whether the request is from Monitoring url Edit Activity.
		 * null sourceVoteStarter indicates that the request is from authoring url.
		 * */
		
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);

		voteAuthoringForm.resetRadioBoxes();
		voteAuthoringForm.setExceptionMaxNominationInvalid(new Boolean(false).toString());
		voteGeneralAuthoringDTO.setExceptionMaxNominationInvalid(new Boolean(false).toString());
		
		ActionForward validateSignature=readSignature(request,mapping, voteService, voteAuthoringForm);
		logger.debug("validateSignature:  " + validateSignature);
		if (validateSignature != null)
		{
			logger.debug("validateSignature not null : " + validateSignature);
			return validateSignature;
		}
		else
		{
		    logger.debug("no problems getting the default content, will render authoring screen");
		    String strToolContentId="";
		    /*the authoring url must be passed a tool content id*/
		    strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
		    logger.debug("strToolContentId: " + strToolContentId);
		    
		    /* this will be fixed when making changes to Monitoring module */
		    if (strToolContentId == null)
		    {
		    	/*watch out for a possibility that the original request for authoring module is coming 
		    	 * from monitoring url */
		    	logger.debug("we should IDEALLY not arrive here. The TOOL_CONTENT_ID is NOT available.");
		    	/*use default content instead of giving a warning*/
		    	String defaultContentId=voteAuthoringForm.getDefaultContentId(); 
		    	logger.debug("using Voting defaultContentId: " + defaultContentId);
		    	strToolContentId=defaultContentId;
		    }
	    	logger.debug("final strToolContentId: " + strToolContentId);
		    
		    if ((strToolContentId == null) || (strToolContentId.equals(""))) 
		    {
		    	VoteUtils.cleanUpSessionAbsolute(request);
		    	//saveInRequestError(request,"error.contentId.required");
		    	VoteUtils.cleanUpSessionAbsolute(request);
				logger.debug("forwarding to: " + ERROR_LIST);
				return (mapping.findForward(ERROR_LIST));
		    }
		    
		    /* Process incoming tool content id. 
		     * Either exists or not exists in the db yet, a toolContentID must be passed to the tool from the container */
		    long toolContentID=0;
	    	try
			{
		    	toolContentID=new Long(strToolContentId).longValue();
		    	logger.debug("passed TOOL_CONTENT_ID : " + toolContentID);
		    	voteAuthoringForm.setToolContentID(new Long(strToolContentId).toString());
		    	voteGeneralAuthoringDTO.setToolContentID(new Long(strToolContentId).toString());
	    	}
	    	catch(NumberFormatException e)
			{
		    	VoteUtils.cleanUpSessionAbsolute(request);
		    	saveInRequestError(request,"error.numberFormatException");
				logger.debug("forwarding to: " + ERROR_LIST);
				return (mapping.findForward(ERROR_LIST));
			}

	    	/*
			 * find out if the passed tool content id exists in the db 
			 * present user either a first timer screen with default content data or fetch the existing content.
			 * 
			 * if the toolcontentid does not exist in the db, create the default Map,
			 * there is no need to check if the content is in use in this case.
			 * It is always unlocked -> not in use since it is the default content.
			*/
	    	Map mapOptionsContent= new TreeMap(new VoteComparator());
			logger.debug("mapOptionsContent: " + mapOptionsContent);
		
			if (!existsContent(toolContentID, request, voteService))
			{
				logger.debug("getting default content");
				/*fetch default content*/
				String defaultContentIdStr=voteAuthoringForm.getDefaultContentIdStr(); 
				logger.debug("will get content for defaultContentIdStr:" + defaultContentIdStr);
	            retrieveContent(request, voteService, voteAuthoringForm, voteGeneralAuthoringDTO, mapOptionsContent, 
	                    new Long(defaultContentIdStr).longValue(), sessionMap);
	            
			}
			else
			{
	        	logger.debug("getting existing content");
	        	/* it is possible that the content is in use by learners.*/
	        	VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentId));
	        	logger.debug("voteContent: " + voteContent);
	        	if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
	    		{
	        		VoteUtils.cleanUpSessionAbsolute(request);
	    			logger.debug("student activity occurred on this content:" + voteContent);
	    	    	saveInRequestError(request, "error.content.inUse");
					return (mapping.findForward(ERROR_LIST));
	    		}
	        	
	        	if (servletPath.indexOf("authoringStarter") > 0)
	        	{
		        	boolean isDefineLater=VoteUtils.isDefineLater(voteContent);
		        	logger.debug("isDefineLater:" + isDefineLater);
		        	if (isDefineLater == true)
		        	{
		        		VoteUtils.cleanUpSessionAbsolute(request);
		    			logger.debug("student activity occurred on this content:" + voteContent);
		    	    	saveInRequestError(request, "error.content.inUse");
						return (mapping.findForward(ERROR_LIST));
		        	    
		        	}
	        	}
	        	
	        	logger.debug("will get content for strToolContentId:" + strToolContentId);
	            retrieveContent(request, voteService, voteAuthoringForm, voteGeneralAuthoringDTO, mapOptionsContent, 
	                    new Long(strToolContentId).longValue(), sessionMap);
			}
		}
		    
		voteAuthoringForm.resetUserAction();

		if (voteAuthoringForm != null)
		    voteAuthoringForm.setCurrentTab("1");
		
		logger.debug("will return to jsp with: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);
		logger.debug("active module is: " + voteAuthoringForm.getActiveModule());

		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("persisting sessionMap into session: " + sessionMap);
		request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
		return (mapping.findForward(destination));
	} 
	
	
	
	protected void initialiseAttributes(HttpServletRequest request, VoteGeneralAuthoringDTO voteGeneralAuthoringDTO, IVoteService voteService)
	{
		logger.debug("starting initialiseAttributes...");
		
		/* for development: needs to run only once per tool*/ 
		/* VoteUtils.configureContentRepository(request, voteService); */
		
		/* these two are for Instructions jsp */
		LinkedList listUploadedOfflineFileNames= new LinkedList();
		LinkedList listUploadedOnlineFileNames= new LinkedList();
		
		voteGeneralAuthoringDTO.setListUploadedOfflineFileNames(listUploadedOfflineFileNames);
		voteGeneralAuthoringDTO.setListUploadedOnlineFileNames (listUploadedOnlineFileNames);
		
		LinkedList listOfflineFilesMetaData= new LinkedList();
		LinkedList listOnlineFilesMetaData= new LinkedList();
		
		voteGeneralAuthoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);
		voteGeneralAuthoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);
	}

	

	/**
	 * each tool has a signature. Voting tool's signature is stored in MY_SIGNATURE. The default tool content id and 
	 * other depending content ids are obtained in this method.
	 * if all the default content has been setup properly the method saves DEFAULT_CONTENT_ID in the session.
	 * 
	 * readSignature(HttpServletRequest request, ActionMapping mapping)
	 * @param request
	 * @param mapping
	 * @return ActionForward
	 */
	public ActionForward readSignature(HttpServletRequest request, ActionMapping mapping, IVoteService voteService,
	        VoteAuthoringForm voteAuthoringForm)
	{
		logger.debug("start reading tool signature: " + voteService);
		/*
		 * retrieve the default content id based on tool signature
		 */
		long defaultContentID=0;
		try
		{
			logger.debug("attempt retrieving tool with signatute : " + MY_SIGNATURE);
            defaultContentID=voteService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
			logger.debug("retrieved tool default contentId: " + defaultContentID);
			if (defaultContentID == 0)
			{
				VoteUtils.cleanUpSessionAbsolute(request);
				logger.debug("default content id has not been setup");
				saveInRequestError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));	
			}
		}
		catch(Exception e)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			logger.debug("error getting the default content id: " + e.getMessage());
			saveInRequestError(request,"error.defaultContent.notSetup");
			logger.debug("forwarding to: " + ERROR_LIST);
			return (mapping.findForward(ERROR_LIST));
		}

		
		/* retrieve uid of the content based on default content id determined above */
		long contentUID=0;
		try
		{
			logger.debug("retrieve uid of the content based on default content id determined above: " + defaultContentID);
			VoteContent voteContent=voteService.retrieveVote(new Long(defaultContentID));
			logger.debug("voteContent: " + voteContent);
			if (voteContent == null)
			{
			    logger.debug("voteContent is null: " + voteContent);
				VoteUtils.cleanUpSessionAbsolute(request);
				logger.debug("Exception occured: No default content");
				saveInRequestError(request,"error.defaultContent.notSetup");
	    		return (mapping.findForward(ERROR_LIST));
			}
			logger.debug("using voteContent: " + voteContent);
			logger.debug("using mcContent uid: " + voteContent.getUid());
			contentUID=voteContent.getUid().longValue();
			logger.debug("contentUID: " + contentUID);
		}
		catch(Exception e)
		{
		    logger.debug("other problems: " + e);
			VoteUtils.cleanUpSessionAbsolute(request);
			logger.debug("Exception occured: No default question content");
			saveInRequestError(request,"error.defaultContent.notSetup");
			logger.debug("forwarding to: " + ERROR_LIST);
			return (mapping.findForward(ERROR_LIST));
		}

		logger.debug("Voting tool has the default content id: " + defaultContentID);
		
		voteAuthoringForm.setDefaultContentId(new Long(defaultContentID).toString());
		voteAuthoringForm.setDefaultContentIdStr(new Long(defaultContentID).toString());
		return null;
	}

	
	
	protected void retrieveContent(HttpServletRequest request, IVoteService voteService, 
	        VoteAuthoringForm voteAuthoringForm, VoteGeneralAuthoringDTO voteGeneralAuthoringDTO, 
	        Map mapOptionsContent, long toolContentID, SessionMap sessionMap)
	{
		logger.debug("starting retrieve content for toolContentID: " + toolContentID);
		logger.debug("voteService: " + voteService);

	    logger.debug("getting existing content with id:" + toolContentID);
	    VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
		logger.debug("voteContent: " + voteContent);
		
		
		VoteUtils.readContentValues(request, voteContent, voteAuthoringForm, voteGeneralAuthoringDTO);
		logger.debug("form title is: : " + voteAuthoringForm.getTitle());
		
        voteAuthoringForm.setIsDefineLater(new Boolean(voteContent.isDefineLater()).toString());
        voteGeneralAuthoringDTO.setIsDefineLater(new Boolean(voteContent.isDefineLater()).toString());
        
		if (voteContent.getTitle() == null)
		{
		    voteGeneralAuthoringDTO.setActivityTitle(DEFAULT_VOTING_TITLE);
		    voteAuthoringForm.setTitle(DEFAULT_VOTING_TITLE);
		}
		else
		{
		    voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
		    voteAuthoringForm.setTitle(voteContent.getTitle());
		}

		
		if (voteContent.getInstructions() == null)
		{
		    voteGeneralAuthoringDTO.setActivityInstructions(DEFAULT_VOTING_INSTRUCTIONS);
		    voteAuthoringForm.setInstructions(DEFAULT_VOTING_INSTRUCTIONS);
		}
		else
		{
		    voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());
		    voteAuthoringForm.setInstructions(voteContent.getInstructions());
		}
		
		sessionMap.put(ACTIVITY_TITLE_KEY, voteGeneralAuthoringDTO.getActivityTitle());
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, voteGeneralAuthoringDTO.getActivityInstructions());

		
		voteAuthoringForm.setReflectionSubject(voteContent.getReflectionSubject());
		voteGeneralAuthoringDTO.setReflectionSubject(voteContent.getReflectionSubject());

		List listNominationContentDTO= new  LinkedList();
		
	    /*
		 * get the nominations 
		 */
		logger.debug("setting existing content data from the db");
		mapOptionsContent.clear();
		Iterator queIterator=voteContent.getVoteQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapOptionsContent: " + mapOptionsContent);
		while (queIterator.hasNext())
		{
		    VoteNominationContentDTO voteNominationContentDTO=new VoteNominationContentDTO();
		    
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			if (voteQueContent != null)
			{
				logger.debug("question: " + voteQueContent.getQuestion());
				mapOptionsContent.put(mapIndex.toString(),voteQueContent.getQuestion());
				
				
				voteNominationContentDTO.setQuestion(voteQueContent.getQuestion());
				voteNominationContentDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
	    		listNominationContentDTO.add(voteNominationContentDTO);
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(mapOptionsContent.size()));
		logger.debug("listNominationContentDTO: " + listNominationContentDTO);
		request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);

		
		logger.debug("Map initialized with existing contentid to: " + mapOptionsContent);
		voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
		sessionMap.put(MAP_OPTIONS_CONTENT_KEY, mapOptionsContent);
		
		int maxIndex=mapOptionsContent.size();
		voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);
        
        voteGeneralAuthoringDTO.setRichTextOfflineInstructions(voteContent.getOfflineInstructions());
        voteGeneralAuthoringDTO.setRichTextOnlineInstructions(voteContent.getOnlineInstructions());
        
        voteAuthoringForm.setOfflineInstructions(voteContent.getOfflineInstructions());
        voteAuthoringForm.setOnlineInstructions(voteContent.getOnlineInstructions());
        
        
	    if ((voteContent.getOnlineInstructions() == null) || (voteContent.getOnlineInstructions().length() == 0))
	    {
	        voteGeneralAuthoringDTO.setRichTextOnlineInstructions(DEFAULT_ONLINE_INST);
	        voteAuthoringForm.setOnlineInstructions(DEFAULT_ONLINE_INST);
	        sessionMap.put(ONLINE_INSTRUCTIONS_KEY, DEFAULT_ONLINE_INST);
	    }
	        
	    if ((voteContent.getOfflineInstructions() == null) || (voteContent.getOfflineInstructions().length() == 0))
	    {
	        voteGeneralAuthoringDTO.setRichTextOfflineInstructions(DEFAULT_OFFLINE_INST);
	        voteAuthoringForm.setOfflineInstructions(DEFAULT_OFFLINE_INST);
	        sessionMap.put(OFFLINE_INSTRUCTIONS_KEY, DEFAULT_OFFLINE_INST);
	    }
        
	    
		sessionMap.put(ONLINE_INSTRUCTIONS_KEY, voteContent.getOnlineInstructions());
		sessionMap.put(OFFLINE_INSTRUCTIONS_KEY, voteContent.getOfflineInstructions());

		List attachmentList = voteService.retrieveVoteUploadedFiles(voteContent);
		voteGeneralAuthoringDTO.setAttachmentList(attachmentList);
		voteGeneralAuthoringDTO.setDeletedAttachmentList(new ArrayList());

	    sessionMap.put(ATTACHMENT_LIST_KEY, attachmentList);
	    sessionMap.put(DELETED_ATTACHMENT_LIST_KEY, new ArrayList());

		voteAuthoringForm.resetUserAction();
	}
	
	public ActionForward executeDefineLater(ActionMapping mapping, VoteAuthoringForm voteAuthoringForm, 
			HttpServletRequest request, HttpServletResponse response) 
		throws IOException, ServletException, VoteApplicationException {
		logger.debug("calling execute..." + voteAuthoringForm);
		return execute(mapping, voteAuthoringForm, request, response);
	}
	

	protected boolean existsContent(long toolContentID, HttpServletRequest request, IVoteService voteService)
	{
		VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
	    if (voteContent == null) 
	    	return false;
	    
		return true;	
	}

	
	/**
     * saves error messages to request scope
     * @param request
     * @param message
     */
	public void saveInRequestError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}  
