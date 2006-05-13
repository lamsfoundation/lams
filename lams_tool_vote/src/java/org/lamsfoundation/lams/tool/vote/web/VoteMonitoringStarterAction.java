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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web;

import java.io.IOException;
import java.util.Iterator;
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
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * 
 * @author Ozgur Demirtas
 * starts up the monitoring module
 */

public class VoteMonitoringStarterAction extends Action implements VoteAppConstants {
	static Logger logger = Logger.getLogger(VoteMonitoringStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, VoteApplicationException 
	{
		logger.debug("init VoteMonitoringStarterAction...");
		VoteUtils.cleanUpSessionAbsolute(request);
		
		
	    ActionForward validateParameters=validateParameters(request, mapping);
	    logger.debug("validateParamaters: " + validateParameters);
	    if (validateParameters != null)
	    {
	    	return validateParameters;
	    }

		boolean initData=initialiseMonitoringData(mapping, form, request, response);
		logger.debug("initData: " + initData);
		if (initData == false)
			return (mapping.findForward(ERROR_LIST));
		
		VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
		logger.debug("voteMonitoringForm: " + voteMonitoringForm);
		voteMonitoringForm.setCurrentTab("1");
		logger.debug("setting current tab to 1: ");
		
		
		request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
		request.getSession().setAttribute(COUNT_ALL_USERS, new Integer(0).toString());
		request.getSession().setAttribute(COUNT_SESSION_COMPLETE, new Integer(0).toString());
		
		VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
		logger.debug("calling initSummaryContent.");
		voteMonitoringAction.initSummaryContent(mapping, form, request, response);
		logger.debug("calling initInstructionsContent.");
		voteMonitoringAction.initInstructionsContent(mapping, form, request, response);
		logger.debug("calling initStatsContent.");
		voteMonitoringAction.initStatsContent(mapping, form, request, response);
		

		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);

		logger.debug("calling  prepareChartData: " + toolContentId);
	    
    	VoteContent voteContent=voteService.retrieveVote(toolContentId);
		/*true means there is at least 1 response*/
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
				request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(false).toString());
				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

		
		request.getSession().setAttribute(ACTIVE_MODULE, MONITORING);
		voteMonitoringForm.setSelectedToolSessionId("All");
		return voteMonitoringAction.submitSession(mapping, form,  request, response);
	}

	
	public boolean initialiseMonitoringData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("start initializing  monitoring data...");
		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		request.getSession().setAttribute(TOOL_SERVICE, voteService);
		
		request.getSession().setAttribute(CURRENT_MONITORING_TAB, "summary");
		request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
		request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(false));
		request.getSession().setAttribute(REQUEST_LEARNING_REPORT, new Boolean(false).toString());
		
		request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
	    VoteUtils.persistTimeZone(request);
		
		/* we have made sure TOOL_CONTENT_ID is passed  */
	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
		logger.debug("existing voteContent:" + voteContent);
		
		if (voteContent == null)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			request.getSession().setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true).toString());
			persistError(request, "error.content.doesNotExist");
			return false;
		}
		
		
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		
		
		request.getSession().setAttribute(IS_MONITORED_CONTENT_IN_USE, new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
	    	persistError(request,"error.content.inUse");
	    	request.getSession().setAttribute(IS_MONITORED_CONTENT_IN_USE, new Boolean(true).toString());
		}
		
		
		if (voteContent.getTitle() == null)
		{
			request.getSession().setAttribute(ACTIVITY_TITLE, "Voting Title");
			request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, "Voting Instructions");
		}
		else
		{
			request.getSession().setAttribute(ACTIVITY_TITLE, voteContent.getTitle());
			request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, voteContent.getInstructions());			
		}

		
		if (voteService.studentActivityOccurredGlobal(voteContent))
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			logger.debug("student activity occurred on this content:" + voteContent);
			request.getSession().setAttribute(USER_EXCEPTION_CONTENT_IN_USE, new Boolean(true).toString());
			
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			logger.debug("error.noLearnerActivity must be displayed");
		}

	    /*
		 * get the nominations 
		 * section  is needed for the Edit tab's View Only mode, starts here
		 */
		Map mapOptionsContent= new TreeMap(new VoteComparator());
		logger.debug("setting existing content data from the db");
		mapOptionsContent.clear();
		Iterator queIterator=voteContent.getVoteQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapOptionsContent: " + mapOptionsContent);
		while (queIterator.hasNext())
		{
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			if (voteQueContent != null)
			{
				logger.debug("question: " + voteQueContent.getQuestion());
				mapOptionsContent.put(mapIndex.toString(),voteQueContent.getQuestion());
	    		/**
	    		 * make the first entry the default(first) one for jsp
	    		 */
	    		if (mapIndex.longValue() == 1)
	    		{
	    		    request.getSession().setAttribute(DEFAULT_OPTION_CONTENT, voteQueContent.getQuestion());
	    		}
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		logger.debug("Map initialized with existing contentid to: " + mapOptionsContent);
		request.getSession().setAttribute(MAP_OPTIONS_CONTENT, mapOptionsContent);
		logger.debug("starter initialized the Comparable Map: " + request.getSession().getAttribute(MAP_OPTIONS_CONTENT) );
		/* ends here*/
		
		
		VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
		logger.debug("refreshing summary data...");
		voteMonitoringAction.refreshSummaryData(request, voteContent, voteService, true, false, null, null, false);
		
		logger.debug("refreshing stats data...");
		voteMonitoringAction.refreshStatsData(request);
		
		logger.debug("refreshing instructions data...");
		voteMonitoringAction.refreshInstructionsData(request, voteContent);
		    		
	    logger.debug("end initializing  monitoring data...");
		return true;
	}


	protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping)
	{
		logger.debug("start validating monitoring parameters...");
    	
    	String strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
    	logger.debug("strToolContentId: " + strToolContentId);
    	 
	    if ((strToolContentId == null) || (strToolContentId.length() == 0)) 
	    {
	    	persistError(request, "error.contentId.required");
	    	VoteUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST));
	    }
	    else
	    {
	    	try
			{
	    		long toolContentId=new Long(strToolContentId).longValue();
		    	logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
		    	request.getSession().setAttribute(TOOL_CONTENT_ID,new Long(toolContentId));	
			}
	    	catch(NumberFormatException e)
			{
	    		persistError(request, "error.contentId.numberFormatException");
	    		logger.debug("add error.contentId.numberFormatException to ActionMessages.");
	    		VoteUtils.cleanUpSessionAbsolute(request);
				return (mapping.findForward(ERROR_LIST));
			}
	    }
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
