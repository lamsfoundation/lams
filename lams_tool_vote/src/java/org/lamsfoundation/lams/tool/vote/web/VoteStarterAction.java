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
*/

package org.lamsfoundation.lams.tool.vote.web;
import java.io.IOException;
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
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteOptsContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.web.util.AttributeNames;


public class VoteStarterAction extends Action implements VoteAppConstants {
	/*
	 * This class is reused by defineLater and monitoring modules as well. 
	 */
	static Logger logger = Logger.getLogger(VoteStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, VoteApplicationException {

		VoteUtils.cleanUpSessionAbsolute(request);
		logger.debug("init authoring mode. removed attributes...");
		
		IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("voteService: " + voteService);
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		    logger.debug("retrieving voteService from cache: " + voteService);
		}
	    request.getSession().setAttribute(TOOL_SERVICE, voteService);

		String servletPath=request.getServletPath();
		logger.debug("getServletPath: "+ servletPath);
		if (servletPath.indexOf("authoringStarter") > 0)
		{
			logger.debug("request is for authoring module");
			request.getSession().setAttribute(ACTIVE_MODULE, AUTHORING);
			request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(true));
			request.getSession().setAttribute(SHOW_AUTHORING_TABS,new Boolean(true).toString());
		}
		else
		{
			logger.debug("request is for define later module. either direct or by monitoring module");
			request.getSession().setAttribute(ACTIVE_MODULE, DEFINE_LATER);
			request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(false));
			request.getSession().setAttribute(SHOW_AUTHORING_TABS,new Boolean(false).toString());			
		}
		 
		initialiseAttributes(request);
    	/* determine whether the request is from Monitoring url Edit Activity.
		 * null sourceVoteStarter indicates that the request is from authoring url.
		 * */
		
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);

		
		VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
		voteAuthoringForm.resetRadioBoxes();
		
		ActionForward validateSignature=readSignature(request,mapping);
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
		    
		    if (strToolContentId == null)
		    {
		    	/*it is possible that the original request for authoring module is coming from monitoring url which keeps the
		    	 TOOL_CONTENT_ID in the session*/
		    	Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
			    logger.debug("toolContentId: " + toolContentId);
			    if (toolContentId != null)
			    {
			    	strToolContentId= toolContentId.toString();
				    logger.debug("cached strToolContentId from the session: " + strToolContentId);	
			    }
			    else
			    {
			    	logger.debug("we should IDEALLY not arrive here. The TOOL_CONTENT_ID is NOT available from the url or the session.");
			    	/*use default content instead of giving a warning*/
			    	String defaultContentId=(String) request.getSession().getAttribute(DEFAULT_CONTENT_ID);
			    	logger.debug("using Voting defaultContentId: " + defaultContentId);
			    	strToolContentId=defaultContentId;
			    }
		    }
	    	logger.debug("final strToolContentId: " + strToolContentId);
		    
		    if ((strToolContentId == null) || (strToolContentId.equals(""))) 
		    {
		    	VoteUtils.cleanUpSessionAbsolute(request);
		    	request.getSession().setAttribute(USER_EXCEPTION_CONTENTID_REQUIRED, new Boolean(true).toString());
		    	persistError(request,"error.contentId.required");
		    	VoteUtils.cleanUpSessionAbsolute(request);
				logger.debug("forwarding to: " + ERROR_LIST);
				return (mapping.findForward(ERROR_LIST));
		    }
		    
		    /* Process incoming tool content id. 
		     * Either exists or not exists in the db yet, a toolContentId must be passed to the tool from the container */
		    long toolContentId=0;
	    	try
			{
		    	toolContentId=new Long(strToolContentId).longValue();
		    	logger.debug("passed TOOL_CONTENT_ID : " + toolContentId);
		    	request.getSession().setAttribute(TOOL_CONTENT_ID, new Long(strToolContentId));
	    	}
	    	catch(NumberFormatException e)
			{
		    	VoteUtils.cleanUpSessionAbsolute(request);
		    	request.getSession().setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true).toString());
		    	persistError(request,"error.numberFormatException");
				logger.debug("forwarding to: " + ERROR_LIST);
				return (mapping.findForward(ERROR_LIST));
			}

	    	
	    	/* 	note: copyToolContent and removeToolContent code is redundant for production.
	    	 *  test whether the authoring level tool contract:
	    	 	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;
	    	 * 	is working or not
	    	 *  
	    	 * test code starts from here... 
	    	 */	
	    	String copyToolContent= (String) request.getParameter(COPY_TOOL_CONTENT);
	    	logger.debug("copyToolContent: " + copyToolContent);
	    	
	    	if ((copyToolContent != null) && (copyToolContent.equals("1")))
			{
		    	logger.debug("user request to copy the content");
		    	Long fromContentId=new Long(strToolContentId);
		    	logger.debug("fromContentId: " + fromContentId);
		    	
		    	Long toContentId=new Long(9876);
		    	logger.debug("toContentId: " + toContentId);
		    	
		    	try
				{
		    		voteService.copyToolContent(fromContentId, toContentId);	
				}
		    	catch(ToolException e)
				{
		    		VoteUtils.cleanUpSessionAbsolute(request);
		    		logger.debug("error copying the content: " + e);
				}
			}
	    	
	    	String removeToolContent= (String) request.getParameter(REMOVE_TOOL_CONTENT);
	    	logger.debug("removeToolContent: " + removeToolContent);
	    	
	    	if ((removeToolContent != null) && (removeToolContent.equals("1")))
			{
		    	logger.debug("user request to remove the content");
		    	Long fromContentId=new Long(strToolContentId);
		    	logger.debug("fromContentId: " + fromContentId);

		    	try
				{
		    		voteService.removeToolContent(fromContentId, true);
				}
		    	catch(ToolException e)
				{
		    		VoteUtils.cleanUpSessionAbsolute(request);
		    		logger.debug("error removing the content: " + e);
				}
			}
	    	
	    	String setDefineLater= (String) request.getParameter("setDefineLater");
	    	logger.debug("setDefineLater: " + setDefineLater);
	    	
	    	if ((setDefineLater != null) && (setDefineLater.equals("1")))
			{
		    	logger.debug("user request to set content as define later");
		    	Long fromContentId=new Long(strToolContentId);
		    	logger.debug("fromContentId: " + fromContentId);

		    	try
				{
		    		voteService.setAsDefineLater(fromContentId);
				}
		    	catch(ToolException e)
				{
		    		VoteUtils.cleanUpSessionAbsolute(request);
		    		logger.debug("error setting the define later on the content: " + e);
				}
			}

	    	
	    	String strSetRunoffline= (String) request.getParameter("strSetRunoffline");
	    	logger.debug("strSetRunoffline: " + strSetRunoffline);
	    	
	    	if ((setDefineLater != null) && (setDefineLater.equals("1")))
			{
		    	logger.debug("user request to set content as run offline");
		    	Long fromContentId=new Long(strToolContentId);
		    	logger.debug("fromContentId: " + fromContentId);

		    	try
				{
		    		voteService.setAsRunOffline(fromContentId);
				}
		    	catch(ToolException e)
				{
		    		VoteUtils.cleanUpSessionAbsolute(request);
		    		logger.debug("error setting the run offline on the content: " + e);
				}
			}
	    	/* ...testing code ends here*/
	    	

	    	/*
			 * find out if the passed tool content id exists in the db 
			 * present user either a first timer screen with default content data or fetch the existing content.
			 * 
			 * if the toolcontentid does not exist in the db, create the default Map,
			 * there is no need to check if the content is in use in this case.
			 * It is always unlocked -> not in use since it is the default content.
			*/
	    	Map mapQuestionContent= new TreeMap(new VoteComparator());
			logger.debug("mapQuestionContent: " + mapQuestionContent);
		
			if (!existsContent(toolContentId, request))
			{
				logger.debug("getting default content");
				/*fetch default content*/
				String defaultContentIdStr=(String) request.getSession().getAttribute(DEFAULT_CONTENT_ID_STR);
				logger.debug("defaultContentIdStr:" + defaultContentIdStr);
				logger.debug("will get content for defaultContentIdStr:" + defaultContentIdStr);
	            retrieveContent(request, mapping, voteAuthoringForm, mapQuestionContent, new Long(defaultContentIdStr).longValue());
			}
			else
			{
	        	logger.debug("getting existing content");
	        	/* it is possible that the content is in use by learners.*/
	        	VoteContent voteContent=voteService.retrieveVote(new Long(strToolContentId));
	        	logger.debug("voteContent: " + voteContent);
	        	if (voteService.studentActivityOccurredGlobal(voteContent))
	    		{
	        		VoteUtils.cleanUpSessionAbsolute(request);
	    			logger.debug("student activity occurred on this content:" + voteContent);
	    	    	request.getSession().setAttribute(USER_EXCEPTION_CONTENT_IN_USE, new Boolean(true).toString());    			
		    		persistError(request, "error.content.inUse");
		    		logger.debug("add error.content.inUse to ActionMessages.");
					return (mapping.findForward(ERROR_LIST));
	    		}
	        	logger.debug("will get content for strToolContentId:" + strToolContentId);
	            retrieveContent(request, mapping, voteAuthoringForm, mapQuestionContent, new Long(strToolContentId).longValue());
			}
		}
		    
		voteAuthoringForm.resetUserAction();
		
		logger.debug("will return to jsp with: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);
		return (mapping.findForward(destination));
	} 
	
	
	
	protected void initialiseAttributes(HttpServletRequest request)
	{
		logger.debug("starting initialiseAttributes...");
		
		/* needs to run only once per tool*/ 
		/* VoteUtils.configureContentRepository(request, voteService); */
		
		/* these two are for Instructions jsp */
		LinkedList listUploadedOfflineFileNames= new LinkedList();
		LinkedList listUploadedOnlineFileNames= new LinkedList();
		
		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES,listUploadedOfflineFileNames);
		request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES,listUploadedOnlineFileNames);
		
		LinkedList listOfflineFilesMetaData= new LinkedList();
		LinkedList listOnlineFilesMetaData= new LinkedList();
		request.getSession().setAttribute(LIST_OFFLINEFILES_METADATA, listOfflineFilesMetaData);
		request.getSession().setAttribute(LIST_ONLINEFILES_METADATA, listOnlineFilesMetaData);
	}

	

	/**
	 * each tool has a signature. Voting tool's signature is stored in MY_SIGNATURE. The default tool content id and 
	 * other depending content ids are obtained in this method.
	 * if all the default content has been setup properly the method persists DEFAULT_CONTENT_ID in the session.
	 * 
	 * readSignature(HttpServletRequest request, ActionMapping mapping)
	 * @param request
	 * @param mapping
	 * @return ActionForward
	 */
	public ActionForward readSignature(HttpServletRequest request, ActionMapping mapping)
	{
		IVoteService voteService =VoteUtils.getToolService(request);
		/* retrieve the default content id based on tool signature */
		long contentId=0;
		try
		{
			logger.debug("attempt retrieving tool with signature : " + MY_SIGNATURE);
			contentId=voteService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
			logger.debug("retrieved tool default contentId: " + contentId);
			if (contentId == 0) 
			{
				VoteUtils.cleanUpSessionAbsolute(request);
				logger.debug("Exception occured: No default options content");
	    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
	    		persistError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));
			}
		}
		catch(Exception e)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			logger.debug("Exception occured: No default options content");
    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
    		persistError(request,"error.defaultContent.notSetup");
			return (mapping.findForward(ERROR_LIST));
		}

		/* retrieve uid of the content based on default content id determined above */
		long contentUID=0;
		try
		{
			logger.debug("retrieve uid of the content based on default content id determined above: " + contentId);
			VoteContent voteContent=voteService.retrieveVote(new Long(contentId));
			if (voteContent == null)
			{
				VoteUtils.cleanUpSessionAbsolute(request);
				logger.debug("Exception occured: No default options content");
	    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
	    		persistError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));
			}
			logger.debug("using voteContent: " + voteContent);
			logger.debug("using voteContent uid: " + voteContent.getUid());
			contentUID=voteContent.getUid().longValue();
		}
		catch(Exception e)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			logger.debug("Exception occured: No default options content");
    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
    		persistError(request,"error.defaultContent.notSetup");
			return (mapping.findForward(ERROR_LIST));
		}
				
		
		/* retrieve uid of the default question content  */
		long queContentUID=0;
		try
		{
			logger.debug("retrieve the default question content based on default content UID: " + queContentUID);
			VoteQueContent voteQueContent=voteService.getToolDefaultQuestionContent(contentUID);
			logger.debug("using voteQueContent: " + voteQueContent);
			if (voteQueContent == null)
			{
				VoteUtils.cleanUpSessionAbsolute(request);
				logger.debug("Exception occured: No default options content");
	    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
	    		persistError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));
			}
			logger.debug("using voteQueContent uid: " + voteQueContent.getUid());
			queContentUID=voteQueContent.getUid().longValue();
			request.getSession().setAttribute(DEFAULT_QUESTION_UID, new Long(queContentUID));
			logger.debug("DEFAULT_QUESTION_UID: " + queContentUID);
		}
		catch(Exception e)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			logger.debug("Exception occured: No default options content");
    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
    		persistError(request,"error.defaultContent.notSetup");
			return (mapping.findForward(ERROR_LIST));
		}
		
		
		/* retrieve default options content */
		try
		{
			logger.debug("retrieve the default options content based on default question content UID: " + queContentUID);
			List list=voteService.findVoteOptionsContentByQueId(new Long(queContentUID));
			logger.debug("using options list: " + list);
			if (list == null)
			{
				VoteUtils.cleanUpSessionAbsolute(request);
				logger.debug("Exception occured: No default options content");
	    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
	    		persistError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));
			}
			
		}
		catch(Exception e)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			logger.debug("Exception occured: No default options content");
    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
    		persistError(request,"error.defaultContent.notSetup");
			return (mapping.findForward(ERROR_LIST));
		}		
		
		logger.debug("Voting tool has the default content id: " + contentId);
		request.getSession().setAttribute(DEFAULT_CONTENT_ID, new Long(contentId).toString());
		request.getSession().setAttribute(DEFAULT_CONTENT_ID_STR, new Long(contentId).toString());
		return null;
	}

	
	
	protected void retrieveContent(HttpServletRequest request, ActionMapping mapping, VoteAuthoringForm voteAuthoringForm, Map mapQuestionContent, long toolContentId)
	{
		logger.debug("starting retrieveExistingContent for toolContentId: " + toolContentId);

		IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("voteService: " + voteService);
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}
	    request.getSession().setAttribute(TOOL_SERVICE, voteService);

	    logger.debug("getting existing content with id:" + toolContentId);
	    VoteContent voteContent = voteService.retrieveVote(new Long(toolContentId));
		logger.debug("voteContent: " + voteContent);
		
		VoteUtils.setDefaultSessionAttributes(request, voteContent, voteAuthoringForm);
		logger.debug("form title is: : " + voteAuthoringForm.getTitle());
		
        request.getSession().setAttribute(IS_DEFINE_LATER, new Boolean(voteContent.isDefineLater()));
	    
	    voteAuthoringForm.setTitle(voteContent.getTitle());
		voteAuthoringForm.setInstructions(voteContent.getInstructions());
		
		if (voteContent.getTitle() == null)
		{
			request.getSession().setAttribute(ACTIVITY_TITLE, "Questions and Answers");
			request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, "Please answer the questions.");
		}
		else
		{
			request.getSession().setAttribute(ACTIVITY_TITLE, voteContent.getTitle());
			request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, voteContent.getInstructions());			
		}

		
		logger.debug("Title is: " + voteContent.getTitle());
		logger.debug("Instructions is: " + voteContent.getInstructions());
		if ((voteAuthoringForm.getTitle() == null) || (voteAuthoringForm.getTitle().equals("")))
		{
			logger.debug("resetting title");
			String activityTitle=(String)request.getSession().getAttribute(ACTIVITY_TITLE);
			logger.debug("activityTitle: " + activityTitle);
			voteAuthoringForm.setTitle(activityTitle);
		}
	    
		if ((voteAuthoringForm.getInstructions() == null) || (voteAuthoringForm.getInstructions().equals("")))
		{
			logger.debug("resetting instructions");
			String activityInstructions=(String)request.getSession().getAttribute(ACTIVITY_INSTRUCTIONS);
			logger.debug("activityInstructions: " + activityInstructions);
			voteAuthoringForm.setInstructions(activityInstructions);
		}

		
	    /*
		 * get the posting content 
		 */
		logger.debug("setting existing content data from the db");
		mapQuestionContent.clear();
		Iterator queIterator=voteContent.getVoteQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapQuestionContent: " + mapQuestionContent);
		while (queIterator.hasNext())
		{
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			if (voteQueContent != null)
			{
				logger.debug("question: " + voteQueContent.getQuestion());
	    		mapQuestionContent.put(mapIndex.toString(),voteQueContent.getQuestion());
	    		/**
	    		 * make the first entry the default(first) one for jsp
	    		 */
	    		if (mapIndex.longValue() == 1)
	    		{
	    		    request.getSession().setAttribute(DEFAULT_QUESTION_CONTENT, voteQueContent.getQuestion());
	    		    request.getSession().setAttribute(POSTING, voteQueContent.getQuestion());
	    		}
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		logger.debug("Map initialized with existing contentid to: " + mapQuestionContent);
		request.getSession().setAttribute(MAP_QUESTION_CONTENT, mapQuestionContent);
		logger.debug("starter initialized the Comparable Map: " + request.getSession().getAttribute("mapQuestionContent") );
		
		
		/* collect options for the default question content into a Map*/
		VoteQueContent voteQueContent=voteService.getToolDefaultQuestionContent(voteContent.getUid().longValue());
		logger.debug("voteQueContent:" + voteQueContent);

		/* hold all he options for this question*/
		List list=voteService.findVoteOptionsContentByQueId(voteQueContent.getUid());
		logger.debug("options list:" + list);

		Map mapOptionsContent= new TreeMap(new VoteComparator());
		Iterator listIterator=list.iterator();
		Long mapOptsIndex=new Long(1);
		while (listIterator.hasNext())
		{
			VoteOptsContent voteOptsContent=(VoteOptsContent)listIterator.next();
			logger.debug("option text:" + voteOptsContent.getVoteQueOptionText());
			mapOptionsContent.put(mapOptsIndex.toString(),voteOptsContent.getVoteQueOptionText());
			
    		if (mapOptsIndex.longValue() == 1)
    			request.getSession().setAttribute(DEFAULT_OPTION_CONTENT, voteOptsContent.getVoteQueOptionText());
			
			mapIndex=new Long(mapOptsIndex.longValue()+1);
		}
		
		logger.debug("DEFAULT_QUESTION_CONTENT: " + request.getSession().getAttribute(DEFAULT_QUESTION_CONTENT));
		logger.debug("DEFAULT_OPTION_CONTENT: " + request.getSession().getAttribute(DEFAULT_OPTION_CONTENT));
		request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
		Map mapDefaultOptionsContent=mapOptionsContent;
		request.getSession().setAttribute(MAP_DEFAULTOPTIONS_CONTENT, mapDefaultOptionsContent);
		logger.debug("starter initialized the Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
		logger.debug("starter initialized the Default Options Map: " + request.getSession().getAttribute(MAP_DEFAULTOPTIONS_CONTENT));


		logger.debug("final title: " + voteAuthoringForm.getTitle());
		logger.debug("final ins: " + voteAuthoringForm.getInstructions());
		
		/*
		 * load questions page
		 */
		voteAuthoringForm.resetUserAction();
	}

	protected boolean existsContent(long toolContentId, HttpServletRequest request)
	{
		IVoteService voteService =VoteUtils.getToolService(request);
		VoteContent voteContent=voteService.retrieveVote(new Long(toolContentId));
	    if (voteContent == null) 
	    	return false;
	    
		return true;	
	}

	
	/**
     * persists error messages to request scope
     * @param request
     * @param message
     */
	public void persistError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}  
