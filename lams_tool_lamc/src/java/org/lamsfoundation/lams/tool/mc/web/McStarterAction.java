/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * McStarterAction loads the default content and initializes the presentation Map.
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
	<!--Authoring Starter Action: initializes the authoring module -->
   <action path="/authoringStarter" 
   			type="org.lamsfoundation.lams.tool.mc.web.McStarterAction" 
   			name="McAuthoringForm" 
   			input=".starter"> 
	
		<exception
	        key="error.exception.McApplication"
	        type="org.lamsfoundation.lams.tool.mc.McApplicationException"
	        handler="org.lamsfoundation.lams.tool.mc.web.CustomStrutsExceptionHandler"
	        path=".mcErrorBox"
	        scope="request"
	      />

		<exception
	        key="error.exception.McApplication"
	        type="java.lang.NullPointerException"
	        handler="org.lamsfoundation.lams.tool.mc.web.CustomStrutsExceptionHandler"
	        path=".mcErrorBox"
	        scope="request"
	      />	         			

	  	<forward
		    name="load"
		    path=".questions"
		    redirect="true"
	  	/>

	  	<forward
		    name="loadMonitoring"
		    path=".monitoringContent"
		    redirect="true"
	  	/>
	  	
	  	<forward
		    name="loadLearner"
		    path=".answers"
		    redirect="true"
	  	/>

	  	<forward
	        name="starter"
	        path=".starter"
	        redirect="true"
	     />
	     
	     <forward
	        name="preview"
	        path=".preview"
	        redirect="true"
	     />
	  	
	  	<forward
		    name="errorList"
		    path=".mcErrorBox"
		    redirect="true"
	  	/>
	</action>  
  

	The author is given warnings when the content in use by learners OR when the content is being edited in the Monitoring interface.  
*/

/* TO DO: enable show feedback on questions.*/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;
import java.io.IOException;
import java.util.Date;
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
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McStringComparator;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.web.util.AttributeNames;


public class McStarterAction extends Action implements McAppConstants {
	/*
	 * This class is reused by defineLater and monitoring modules as well. 
	 */
	static Logger logger = Logger.getLogger(McStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, McApplicationException {

		McUtils.cleanUpSessionAbsolute(request);
		logger.debug("init authoring mode. removed attributes...");
		
		McAuthoringForm mcAuthoringForm = (McAuthoringForm) form;
		
		IMcService mcService = (IMcService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("mcService: " + mcService);
		if (mcService == null)
		{
			logger.debug("will retrieve mcService");
			mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		    logger.debug("retrieving mcService from cache: " + mcService);
		}
	    request.getSession().setAttribute(TOOL_SERVICE, mcService);

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
		 
		initialiseAttributes(request, mcAuthoringForm);
    	/* determine whether the request is from Monitoring url Edit Activity.
		 * null sourceMcStarter indicates that the request is from authoring url.
		 * */
		
		String sourceMcStarter = (String) request.getAttribute(SOURCE_MC_STARTER);
		logger.debug("sourceMcStarter: " + sourceMcStarter);

		mcAuthoringForm.resetRadioBoxes();
		
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
			    	logger.debug("using MCQ defaultContentId: " + defaultContentId);
			    	strToolContentId=defaultContentId;
			    }
		    }
	    	logger.debug("final strToolContentId: " + strToolContentId);
		    
		    if ((strToolContentId == null) || (strToolContentId.equals(""))) 
		    {
		    	McUtils.cleanUpSessionAbsolute(request);
		    	request.getSession().setAttribute(USER_EXCEPTION_CONTENTID_REQUIRED, new Boolean(true).toString());
		    	persistError(request,"error.contentId.required");
		    	McUtils.cleanUpSessionAbsolute(request);
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
		    	McUtils.cleanUpSessionAbsolute(request);
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
		    		mcService.copyToolContent(fromContentId, toContentId);	
				}
		    	catch(ToolException e)
				{
		    		McUtils.cleanUpSessionAbsolute(request);
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
		    		mcService.removeToolContent(fromContentId, true);
				}
		    	catch(ToolException e)
				{
		    		McUtils.cleanUpSessionAbsolute(request);
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
		    		mcService.setAsDefineLater(fromContentId);
				}
		    	catch(ToolException e)
				{
		    		McUtils.cleanUpSessionAbsolute(request);
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
		    		mcService.setAsRunOffline(fromContentId);
				}
		    	catch(ToolException e)
				{
		    		McUtils.cleanUpSessionAbsolute(request);
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
		
			if (!existsContent(toolContentId, request))
			{
				logger.debug("retrieving default content");
				retrieveDefaultContent(request, mcAuthoringForm);
			}
			else
			{
				logger.debug("retrieving existing content for: " + toolContentId);
				McContent mcContent=mcService.retrieveMc(new Long(toolContentId));
				logger.debug("existing mcContent:" + mcContent);
		
				/* it is possible that the content is being used by some learners. In this situation, the content  is marked as "in use" and 
				   content in use is not modifiable*/ 
				boolean isContentInUse=McUtils.isContentInUse(mcContent);
				logger.debug("isContentInUse:" + isContentInUse);
				
				if (isContentInUse == true)
				{
					McUtils.cleanUpSessionAbsolute(request);
			    	request.getSession().setAttribute(USER_EXCEPTION_CONTENT_IN_USE, new Boolean(true).toString());
			    	persistError(request,"error.content.inUse");
					logger.debug("forwarding to: " + ERROR_LIST);
					return (mapping.findForward(ERROR_LIST));
				}
				
				/* do not make these tests if the request is coming from monitoring url for Edit Activity*/
				if (  (sourceMcStarter == null) ||
					  ((sourceMcStarter != null) && !sourceMcStarter.equals("monitoring"))
				   )
				{
					
					/* it is possible that the content is being EDITED in the monitoring interface. In this situation, the content is not modifiable*/ 
					boolean isDefineLater=McUtils.isDefineLater(mcContent);
					logger.debug("isDefineLater:" + isDefineLater);
					String defineLater= (String)request.getSession().getAttribute(ACTIVE_MODULE);
					logger.debug("the url mode is :" + defineLater);

					/* we should allow content to be edited if the url mode is define Later*/
					if (!defineLater.equals(DEFINE_LATER))
					{
						logger.debug("the url mode is :" + defineLater);
						if (isDefineLater == true)
						{
					    	McUtils.cleanUpSessionAbsolute(request);
					    	request.getSession().setAttribute(USER_EXCEPTION_CONTENT_BEING_MODIFIED, new Boolean(true).toString());
					    	persistError(request,"error.content.beingModified");
							logger.debug("forwarding to: " + ERROR_LIST);
							return (mapping.findForward(ERROR_LIST));
						}						
					}
				}
				
				retrieveExistingContent(request, mcAuthoringForm, toolContentId, mcContent);
			}
		}
		mcAuthoringForm.resetUserAction();
		
		logger.debug("will return to jsp with: " + sourceMcStarter);
		String destination=McUtils.getDestination(sourceMcStarter);
		logger.debug("destination: " + destination);
		return (mapping.findForward(destination));
	} 

	/**
	 * each tool has a signature. MCQ tool's signature is stored in MY_SIGNATURE. The default tool content id and 
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
		IMcService mcService =McUtils.getToolService(request);
		/* retrieve the default content id based on tool signature */
		long contentId=0;
		try
		{
			logger.debug("attempt retrieving tool with signature : " + MY_SIGNATURE);
			contentId=mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
			logger.debug("retrieved tool default contentId: " + contentId);
			if (contentId == 0) 
			{
				McUtils.cleanUpSessionAbsolute(request);
				logger.debug("Exception occured: No default options content");
	    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
	    		persistError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));
			}
		}
		catch(Exception e)
		{
			McUtils.cleanUpSessionAbsolute(request);
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
			McContent mcContent=mcService.retrieveMc(new Long(contentId));
			if (mcContent == null)
			{
				McUtils.cleanUpSessionAbsolute(request);
				logger.debug("Exception occured: No default options content");
	    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
	    		persistError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));
			}
			logger.debug("using mcContent: " + mcContent);
			logger.debug("using mcContent uid: " + mcContent.getUid());
			contentUID=mcContent.getUid().longValue();
		}
		catch(Exception e)
		{
			McUtils.cleanUpSessionAbsolute(request);
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
			McQueContent mcQueContent=mcService.getToolDefaultQuestionContent(contentUID);
			logger.debug("using mcQueContent: " + mcQueContent);
			if (mcQueContent == null)
			{
				McUtils.cleanUpSessionAbsolute(request);
				logger.debug("Exception occured: No default options content");
	    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
	    		persistError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));
			}
			logger.debug("using mcQueContent uid: " + mcQueContent.getUid());
			queContentUID=mcQueContent.getUid().longValue();
			request.getSession().setAttribute(DEFAULT_QUESTION_UID, new Long(queContentUID));
			logger.debug("DEFAULT_QUESTION_UID: " + queContentUID);
		}
		catch(Exception e)
		{
			McUtils.cleanUpSessionAbsolute(request);
			logger.debug("Exception occured: No default options content");
    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
    		persistError(request,"error.defaultContent.notSetup");
			return (mapping.findForward(ERROR_LIST));
		}
		
		
		/* retrieve default options content */
		try
		{
			logger.debug("retrieve the default options content based on default question content UID: " + queContentUID);
			List list=mcService.findMcOptionsContentByQueId(new Long(queContentUID));
			logger.debug("using options list: " + list);
			if (list == null)
			{
				McUtils.cleanUpSessionAbsolute(request);
				logger.debug("Exception occured: No default options content");
	    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
	    		persistError(request,"error.defaultContent.notSetup");
				return (mapping.findForward(ERROR_LIST));
			}
			
		}
		catch(Exception e)
		{
			McUtils.cleanUpSessionAbsolute(request);
			logger.debug("Exception occured: No default options content");
    		request.getSession().setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true).toString());
    		persistError(request,"error.defaultContent.notSetup");
			return (mapping.findForward(ERROR_LIST));
		}		
		
		logger.debug("MCQ tool has the default content id: " + contentId);
		request.getSession().setAttribute(DEFAULT_CONTENT_ID, new Long(contentId).toString());
		return null;
	}
	

	/**
	 * existsContent(long toolContentId)
	 * @param long toolContentId
	 * @return boolean
	 * determine whether a specific toolContentId exists in the db
	 */
	protected boolean existsContent(long toolContentId, HttpServletRequest request)
	{
		IMcService mcService =McUtils.getToolService(request);
		McContent mcContent=mcService.retrieveMc(new Long(toolContentId));
	    if (mcContent == null) 
	    	return false;
	    
		return true;	
	}
	
	
	/**
	 * retrieves the contents of an existing content from the db and prepares it for presentation
	 * retrieveExistingContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm, long toolContentId)
	 * 
	 * @param request
	 * @param mcAuthoringForm
	 * @param toolContentId
	 */
	protected void retrieveExistingContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm, long toolContentId, McContent mcContent)
	{
		logger.debug("retrieving existing content:" + mcContent);
		IMcService mcService =McUtils.getToolService(request);
		logger.debug("mcService:" + mcService);
		
		request.getSession().setAttribute(IS_REVISITING_USER, new Boolean(true));
		/*to find out whether the content is being modified or not*/
		request.getSession().setAttribute(DEFINE_LATER, new Boolean(mcContent.isDefineLater()));

		/*used in authoring mode basic tab*/
		request.getSession().setAttribute(RICHTEXT_TITLE,mcContent.getTitle());
		request.getSession().setAttribute(RICHTEXT_INSTRUCTIONS,mcContent.getInstructions());
		
		logger.debug("passMark:" +mcContent.getPassMark());
		if (mcContent.getPassMark() == null)
			mcContent.setPassMark(new Integer(0));
		
		mcAuthoringForm.setPassmark(mcContent.getPassMark().toString());
		request.getSession().setAttribute(PASSMARK, mcContent.getPassMark()); //Integer
		
		/*used in advanced tab*/
		request.getSession().setAttribute(RICHTEXT_REPORT_TITLE,mcContent.getReportTitle());
	    request.getSession().setAttribute(RICHTEXT_END_LEARNING_MSG,mcContent.getEndLearningMessage());
		
	    /* used in instructions tab*/
		request.getSession().setAttribute(RICHTEXT_OFFLINEINSTRUCTIONS,mcContent.getOfflineInstructions());
		request.getSession().setAttribute(RICHTEXT_ONLINEINSTRUCTIONS,mcContent.getOnlineInstructions());
		
		Date creationDate=mcContent.getCreationDate();
		logger.debug("creationDate:" + creationDate);
		request.getSession().setAttribute(CREATION_DATE,creationDate);
		
		logger.debug("RICHTEXT_TITLE:" + request.getSession().getAttribute(RICHTEXT_TITLE));
		logger.debug("getting name lists based on uid:" + mcContent.getUid());
		
		List listUploadedOffFiles= mcService.retrieveMcUploadedOfflineFilesName(mcContent.getUid());
		logger.debug("existing listUploadedOfflineFileNames:" + listUploadedOffFiles);
		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES,listUploadedOffFiles);
		
		List listUploadedOnFiles= mcService.retrieveMcUploadedOnlineFilesName(mcContent.getUid());
		logger.debug("existing listUploadedOnlineFileNames:" + listUploadedOnFiles);
		request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES,listUploadedOnFiles);
		
		/* set radioboxes in Advanced tab*/
		AuthoringUtil.setRadioboxes(mcContent, mcAuthoringForm);
				
		McUtils.populateUploadedFilesData(request, mcContent);
	    logger.debug("populated UploadedFilesData");
	    Map mapWeights= AuthoringUtil.rebuildWeightsMapfromDB(request, new Long(toolContentId));
    	logger.debug("Check the mapWeights: " + mapWeights);
    	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
    	
    	/*get existing feedback maps*/
    	Map mapIncorrectFeedback = AuthoringUtil.rebuildIncorrectFeedbackMapfromDB(request, new Long(toolContentId));
    	logger.debug("existing mapIncorrectFeedback:" + mapIncorrectFeedback);
    	request.getSession().setAttribute(MAP_INCORRECT_FEEDBACK, mapIncorrectFeedback);
    	
    	Map mapCorrectFeedback = AuthoringUtil.rebuildCorrectFeedbackMapfromDB(request, new Long(toolContentId));
    	logger.debug("existing mapCorrectFeedback:" + mapCorrectFeedback);
    	request.getSession().setAttribute(MAP_CORRECT_FEEDBACK, mapCorrectFeedback);
    	
    	
    	Map mapQuestionsContent=AuthoringUtil.rebuildQuestionMapfromDB(request, new Long(toolContentId));
    	logger.debug("mapQuestionsContent:" + mapQuestionsContent);
    	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
    	logger.debug("starter initialized the existing Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
    	
	    AuthoringUtil.refreshMaps(request, toolContentId);
	    AuthoringUtil.assignStaterMapsToCurrentMaps(request);
	    
	    /*process offline files metadata*/
	    List listOfflineFilesMetaData=mcService.getOfflineFilesMetaData(mcContent.getUid());
	    logger.debug("existing listOfflineFilesMetaData, to be structured as McAttachmentDTO: " + listOfflineFilesMetaData);
	    listOfflineFilesMetaData=AuthoringUtil.populateMetaDataAsAttachments(listOfflineFilesMetaData);
	    logger.debug("populated listOfflineFilesMetaData: " + listOfflineFilesMetaData);
	    request.getSession().setAttribute(LIST_OFFLINEFILES_METADATA, listOfflineFilesMetaData);
	    
	    List listUploadedOfflineFileNames=AuthoringUtil.populateMetaDataAsFilenames(listOfflineFilesMetaData);
	    logger.debug("returned from db listUploadedOfflineFileNames: " + listUploadedOfflineFileNames);
	    request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES, listUploadedOfflineFileNames);
	    
	    /*process online files metadata*/
	    List listOnlineFilesMetaData=mcService.getOnlineFilesMetaData(mcContent.getUid());
	    logger.debug("existing listOnlineFilesMetaData, to be structured as McAttachmentDTO: " + listOnlineFilesMetaData);
	    listOnlineFilesMetaData=AuthoringUtil.populateMetaDataAsAttachments(listOnlineFilesMetaData);
	    logger.debug("populated listOnlineFilesMetaData: " + listOnlineFilesMetaData);
	    request.getSession().setAttribute(LIST_ONLINEFILES_METADATA, listOnlineFilesMetaData);
	    
	    List listUploadedOnlineFileNames=AuthoringUtil.populateMetaDataAsFilenames(listOnlineFilesMetaData);
	    logger.debug("returned from db listUploadedOnlineFileNames: " + listUploadedOnlineFileNames);
	    request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES, listUploadedOnlineFileNames);
	}
	
	
	/**
	 * retrieves the contents of the default content from the db and prepares it for presentation
	 * retrieveDefaultContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
	 * 
	 * @param request
	 * @param mcAuthoringForm
	 */
	protected void retrieveDefaultContent(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
	{
		IMcService mcService = (IMcService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("mcService: " + mcService);
		if (mcService == null)
		{
			logger.debug("will retrieve mcService");
			mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		    logger.debug("retrieving mcService from cache: " + mcService);
		}

		request.getSession().setAttribute(IS_REVISITING_USER, new Boolean(false));
		
		request.getSession().setAttribute(DEFINE_LATER_EDIT_ACTIVITY, new Boolean(false));
		logger.debug("setting DEFINE_LATER_EDIT_ACTIVITY to false.");
		
		long contentId=0;
		logger.debug("getting default content");
		contentId=mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
		logger.debug("contentId:" + contentId);
		McContent mcContent=mcService.retrieveMc(new Long(contentId));
		logger.debug("mcContent:" + mcContent);
		
		/* reset all radioboxes to false*/
		mcAuthoringForm.resetRadioBoxes();
		logger.debug("all radioboxes are reset");
		
		request.getSession().setAttribute(RICHTEXT_TITLE,mcContent.getTitle());
		request.getSession().setAttribute(RICHTEXT_INSTRUCTIONS,mcContent.getInstructions());
		request.getSession().setAttribute(DEFINE_LATER, new Boolean(mcContent.isDefineLater()));
		request.getSession().setAttribute(RICHTEXT_OFFLINEINSTRUCTIONS,mcContent.getOfflineInstructions());
	    request.getSession().setAttribute(RICHTEXT_ONLINEINSTRUCTIONS,mcContent.getOnlineInstructions());
		request.getSession().setAttribute(RETRIES, new Boolean(mcContent.isRetries()));
		request.getSession().setAttribute(PASSMARK, mcContent.getPassMark()); //Integer
		request.getSession().setAttribute(RICHTEXT_REPORT_TITLE,mcContent.getReportTitle());
		request.getSession().setAttribute(RICHTEXT_END_LEARNING_MSG,mcContent.getEndLearningMessage());
		request.getSession().setAttribute(RICHTEXT_INCORRECT_FEEDBACK,"");
		request.getSession().setAttribute(RICHTEXT_CORRECT_FEEDBACK,"");

		logger.debug("passMark:" +mcContent.getPassMark());
		if (mcContent.getPassMark() == null)
			mcContent.setPassMark(new Integer(0));

		mcAuthoringForm.setPassmark(mcContent.getPassMark().toString());
		request.getSession().setAttribute(PASSMARK, mcContent.getPassMark()); //Integer
		

		logger.debug("PASSMARK:" + request.getSession().getAttribute(PASSMARK));
		logger.debug("RICHTEXT_TITLE:" + request.getSession().getAttribute(RICHTEXT_TITLE));
		logger.debug("getting default content");
	    
	    /* this is already done in  mcAuthoringForm.resetRadioBoxes()*/ 
	    mcAuthoringForm.setUsernameVisible(OFF);
	    mcAuthoringForm.setQuestionsSequenced(OFF);
		mcAuthoringForm.setSynchInMonitor(OFF);
		mcAuthoringForm.setRetries(OFF);
		mcAuthoringForm.setShowFeedback(OFF);
		mcAuthoringForm.setSln(OFF);
				
		/* collect options for the default question content into a Map*/
		McQueContent mcQueContent=mcService.getToolDefaultQuestionContent(mcContent.getUid().longValue());
		logger.debug("mcQueContent:" + mcQueContent);
		
		/* mcQueContent can not be null since here it was verified before*/ 
		
		Map mapQuestionsContent= new TreeMap(new McComparator());
		mapQuestionsContent.put(new Long(1).toString(), mcQueContent.getQuestion());
		request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
		logger.debug("starter initialized the Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
		
		/* hold all he options for this question*/
		List list=mcService.findMcOptionsContentByQueId(mcQueContent.getUid());
		logger.debug("options list:" + list);

		Map mapOptionsContent= new TreeMap(new McStringComparator());
		Iterator listIterator=list.iterator();
		Long mapIndex=new Long(1);
		while (listIterator.hasNext())
		{
			McOptsContent mcOptsContent=(McOptsContent)listIterator.next();
			logger.debug("option text:" + mcOptsContent.getMcQueOptionText());
			mapOptionsContent.put(mapIndex.toString(),mcOptsContent.getMcQueOptionText());
			mapIndex=new Long(mapIndex.longValue()+1);
		}
		request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
		Map mapDefaultOptionsContent=mapOptionsContent;
		request.getSession().setAttribute(MAP_DEFAULTOPTIONS_CONTENT, mapDefaultOptionsContent);
		logger.debug("starter initialized the Options Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT));
		logger.debug("starter initialized the Default Options Map: " + request.getSession().getAttribute(MAP_DEFAULTOPTIONS_CONTENT));
		
		Map mapWeights= new TreeMap(new McComparator());
		/* reset all the weights to 0*/
		long mapCounter=0;
		for (long i=1; i <= MAX_QUESTION_COUNT ; i++)
		{
			mapCounter++;
			mapWeights.put(new Long(mapCounter).toString(), new Integer(0));
		}	
		request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
		logger.debug("MAP_WEIGHTS:" + request.getSession().getAttribute(MAP_WEIGHTS));	
	}
	
	
	/**
	 * initialisation
	 * initialiseAttributes(HttpServletRequest request)
	 * 
	 * @param request
	 */
	protected void initialiseAttributes(HttpServletRequest request, McAuthoringForm mcAuthoringForm)
	{
		logger.debug("starting initialiseAttributes...");
		mcAuthoringForm.setEditOptionsMode(new Integer(0).toString());
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		
		/* needs to run only once per tool*/ 
		/* McUtils.configureContentRepository(request, mcService); */
		
		/* these two are for Instructions jsp */
		LinkedList listUploadedOfflineFileNames= new LinkedList();
		LinkedList listUploadedOnlineFileNames= new LinkedList();
		
		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES,listUploadedOfflineFileNames);
		request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES,listUploadedOnlineFileNames);
		
		LinkedList listOfflineFilesMetaData= new LinkedList();
		LinkedList listOnlineFilesMetaData= new LinkedList();
		request.getSession().setAttribute(LIST_OFFLINEFILES_METADATA, listOfflineFilesMetaData);
		request.getSession().setAttribute(LIST_ONLINEFILES_METADATA, listOnlineFilesMetaData);
		
		Map mapGeneralOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
		
		Map mapGeneralSelectedOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		
		Map mapStartupGeneralOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_STARTUP_GENERAL_OPTIONS_CONTENT, mapStartupGeneralOptionsContent);
		
		Map mapStartupGeneralSelectedOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT, mapStartupGeneralSelectedOptionsContent);
		
		Map mapDisabledQuestions= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_DISABLED_QUESTIONS, mapDisabledQuestions);
		
		Map mapWeights= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
		
		Map mapCheckBoxStates= new TreeMap(new McComparator());
		mapCheckBoxStates.put("1" ,"INCORRECT");
		mapCheckBoxStates.put("2" ,"CORRECT");
		request.getSession().setAttribute(MAP_CHECKBOX_STATES, mapCheckBoxStates);

		Map mapSelectedOptions= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
		
		Map mapIncorrectFeedback= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_INCORRECT_FEEDBACK, mapIncorrectFeedback);
		
		Map mapCorrectFeedback= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_CORRECT_FEEDBACK, mapCorrectFeedback);
		
		mcAuthoringForm.setEditOptionsMode(new Integer(0).toString());
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("resetting  EDIT_OPTIONS_MODE to 0");
	}
	
	
	/**
	 * is reused by defineLater and monitoring urls to activate defineLater module 
	 * 
	 * executeDefineLater(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response, IMcService mcService) 
		throws IOException, ServletException, McApplicationException
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param mcService
	 * @return ActionForward
	 * @throws IOException
	 * @throws ServletException
	 * @throws McApplicationException
	 */
	public ActionForward executeDefineLater(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response, IMcService mcService) 
		throws IOException, ServletException, McApplicationException {
		logger.debug("passed mcService: " + mcService);
		request.getSession().setAttribute(TOOL_SERVICE, mcService);

		return execute(mapping, form, request, response);
	}
	
	
	public ActionForward executeGetMonitoringTab(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException, McApplicationException
	{
		logger.debug("CURRENT_MONITORING_TAB: " + request.getSession().getAttribute(CURRENT_MONITORING_TAB));
		logger.debug("starting executeGetMonitoringTab for: " + LOAD_MONITORING_CONTENT);
		return (mapping.findForward(LOAD_MONITORING_CONTENT));
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
