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
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * 
 * <p> Starts up the monitoring module </p>
 * 
 * @author Ozgur Demirtas
 * 
    
    <!--Monitoring Starter Action: initializes the Monitoring module -->
   <action 	path="/monitoringStarter" 
   			type="org.lamsfoundation.lams.tool.vote.web.VoteMonitoringStarterAction" 
	      	scope="request"
   			name="VoteMonitoringForm" 
      		unknown="false"
      		validate="false"
   			input="/monitoringIndex.jsp"> 

	  	<forward
			name="loadMonitoring"
			path="/monitoring/MonitoringMaincontent.jsp"
			redirect="false"
	  	/>
	
	  	<forward
			name="refreshMonitoring"
			path="/monitoring/MonitoringMaincontent.jsp"
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

public class VoteMonitoringStarterAction extends Action implements VoteAppConstants {
	static Logger logger = Logger.getLogger(VoteMonitoringStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, VoteApplicationException 
	{
		logger.debug("init VoteMonitoringStarterAction...");
		VoteUtils.cleanUpSessionAbsolute(request);

		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);
		
		VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
		logger.debug("voteMonitoringForm: " + voteMonitoringForm);
		
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();
		
	    ActionForward validateParameters=validateParameters(request, mapping, voteMonitoringForm);
	    logger.debug("validateParamaters: " + validateParameters);
	    
	    if (validateParameters != null)
	    {
	    	return validateParameters;
	    }

		boolean initData=initialiseMonitoringData(mapping, form, request, response, 
		        voteService, voteGeneralMonitoringDTO);
		logger.debug("initData: " + initData);
		if (initData == false)
			return (mapping.findForward(ERROR_LIST));
		
		voteMonitoringForm.setCurrentTab("1");
		voteGeneralMonitoringDTO.setCurrentTab("1");
		logger.debug("setting current tab to 1: ");
		
	    String toolContentID=voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
		
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		//Come back to fix here
		voteGeneralMonitoringDTO.setCountAllUsers(new Integer(0).toString());
		voteGeneralMonitoringDTO.setCountSessionComplete(new Integer(0).toString());
		
		VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
		
		logger.debug("calling initSummaryContent.");
		voteMonitoringAction.initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		voteMonitoringAction.initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		voteMonitoringAction.initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
	    logger.debug("voteContent: " + voteContent);
	    
		/*true means there is at least 1 response*/
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
		    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

		voteMonitoringForm.setActiveModule(MONITORING);
		voteGeneralMonitoringDTO.setActiveModule(MONITORING);
		
		voteMonitoringForm.setSelectedToolSessionId("All");
		voteGeneralMonitoringDTO.setSelectedToolSessionId("All");
		
		voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());
		voteGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
		
		voteGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());
		voteMonitoringForm.setShowOpenVotesSection(new Boolean(false).toString());
		voteGeneralMonitoringDTO.setIsPortfolioExport(new Boolean(false).toString());
		
		logger.debug("calling submitSession:" + toolContentID);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
		
		/*this section is needed for Edit Activity screen, from  here... */
		logger.debug("for copy using voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		voteGeneralAuthoringDTO.setActivityTitle(voteGeneralMonitoringDTO.getActivityTitle());
		voteGeneralAuthoringDTO.setActivityInstructions(voteGeneralMonitoringDTO.getActivityInstructions());
		voteGeneralAuthoringDTO.setDefaultOptionContent(voteGeneralMonitoringDTO.getDefaultOptionContent());
		voteGeneralAuthoringDTO.setMapOptionsContent(voteGeneralMonitoringDTO.getMapOptionsContent());
		voteGeneralAuthoringDTO.setActiveModule(MONITORING);
		
		Map mapOptionsContent=voteGeneralMonitoringDTO.getMapOptionsContent();
		logger.debug("mapOptionsContent: " + mapOptionsContent);
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		/*...till here */
		
		logger.debug("calling submitSession with selectedToolSessionId" + voteMonitoringForm.getSelectedToolSessionId());
		return voteMonitoringAction.submitSession(mapping, voteMonitoringForm,  request, response, voteService, voteGeneralMonitoringDTO);
	}

	
	public boolean initialiseMonitoringData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,
	        IVoteService voteService, VoteGeneralMonitoringDTO voteGeneralMonitoringDTO)
	{
	    logger.debug("start initializing  monitoring data...voteService: " + voteService);
		VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
		voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());		

		voteGeneralMonitoringDTO.setCurrentMonitoringTab("summary");
		voteGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
		voteGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		
	    VoteUtils.saveTimeZone(request);
		
		/* we have made sure TOOL_CONTENT_ID is passed  */
	    String toolContentID=voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);
		
		if (voteContent == null)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			voteGeneralMonitoringDTO.setUserExceptionContentDoesNotExist(new Boolean(true).toString());
			persistInRequestError(request, "error.content.doesNotExist");
			return false;
		}
		
		
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
			persistInRequestError(request,"error.content.inUse");
			voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		}
		
		voteGeneralMonitoringDTO.setActivityTitle(voteContent.getTitle());
		voteGeneralMonitoringDTO.setActivityInstructions(voteContent.getInstructions());

		logger.debug("checking student activity on the standard nominations:" + voteContent);
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			logger.debug("student activity occurred on this content:" + voteContent);
			voteGeneralMonitoringDTO.setUserExceptionContentInUse(new Boolean(true).toString());
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
		    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
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

				if (mapIndex.longValue() == 1)
	    		{
	    		    voteGeneralMonitoringDTO.setDefaultOptionContent(voteQueContent.getQuestion()); 
	    		}
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		logger.debug("Map initialized with existing contentid to: " + mapOptionsContent);
		voteGeneralMonitoringDTO.setMapOptionsContent(mapOptionsContent);
		/* ends here*/
		
		
		VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
		logger.debug("refreshing summary data...");
		voteMonitoringAction.refreshSummaryData(request, voteContent, voteService, true, false, 
		        null, null, false, null, voteGeneralMonitoringDTO, null);
		logger.debug("post refreshSummaryData, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("refreshing stats data...");
		voteMonitoringAction.refreshStatsData(request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post refreshStatsData, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("refreshing instructions data...");
		voteMonitoringAction.refreshInstructionsData(request, voteContent, voteService, voteGeneralMonitoringDTO);
		    		
	    logger.debug("end initializing  monitoring data...");
	    voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(false).toString());

	    logger.debug("post refreshes, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		return true;
	}


	protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping, VoteMonitoringForm voteMonitoringForm)
	{
		logger.debug("start validating monitoring parameters...");
    	
    	String strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
    	logger.debug("strToolContentId: " + strToolContentId);
    	 
	    if ((strToolContentId == null) || (strToolContentId.length() == 0)) 
	    {
	        persistInRequestError(request, "error.contentId.required");
	    	VoteUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST));
	    }
	    else
	    {
	    	try
			{
	    		long toolContentID=new Long(strToolContentId).longValue();
		    	logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentID));
		    	voteMonitoringForm.setToolContentID(strToolContentId);
			}
	    	catch(NumberFormatException e)
			{
	    	    persistInRequestError(request, "error.numberFormatException");
	    		logger.debug("add error.numberFormatException to ActionMessages.");
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
	public void persistInRequestError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}  
