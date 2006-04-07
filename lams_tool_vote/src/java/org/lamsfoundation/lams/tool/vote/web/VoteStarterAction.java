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

/* TO DO: enable show feedback on questions.*/

package org.lamsfoundation.lams.tool.vote.web;
import java.io.IOException;
import java.util.List;

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
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;


public class VoteStarterAction extends Action implements VoteAppConstants {
	/*
	 * This class is reused by defineLater and monitoring modules as well. 
	 */
	static Logger logger = Logger.getLogger(VoteStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, VoteApplicationException {

		return null;
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
		IVoteService mcService =VoteUtils.getToolService(request);
		/* retrieve the default content id based on tool signature */
		long contentId=0;
		try
		{
			logger.debug("attempt retrieving tool with signature : " + MY_SIGNATURE);
			contentId=mcService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
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
			VoteContent mcContent=mcService.retrieveMc(new Long(contentId));
			if (mcContent == null)
			{
				VoteUtils.cleanUpSessionAbsolute(request);
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
			VoteQueContent mcQueContent=mcService.getToolDefaultQuestionContent(contentUID);
			logger.debug("using mcQueContent: " + mcQueContent);
			if (mcQueContent == null)
			{
				VoteUtils.cleanUpSessionAbsolute(request);
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
			List list=mcService.findMcOptionsContentByQueId(new Long(queContentUID));
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
		return null;
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
