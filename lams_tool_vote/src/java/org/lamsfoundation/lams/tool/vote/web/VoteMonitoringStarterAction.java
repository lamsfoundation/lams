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
import org.lamsfoundation.lams.tool.vote.EditActivityDTO;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteNominationContentDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;


/**
 * 
 * <p> Starts up the monitoring module </p>
 * 
 * @author Ozgur Demirtas
 */
public class VoteMonitoringStarterAction extends Action implements VoteAppConstants {
	static Logger logger = Logger.getLogger(VoteMonitoringStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, VoteApplicationException 
	{
		VoteUtils.cleanUpSessionAbsolute(request);

		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		
		MessageService messageService = VoteServiceProxy.getMessageService(getServlet().getServletContext());
		
		VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
		
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();
		
	    ActionForward validateParameters=validateParameters(request, mapping, voteMonitoringForm);
	    
	    if (validateParameters != null)
	    {
	    	return validateParameters;
	    }

		boolean initData=initialiseMonitoringData(mapping, form, request, response, 
		        voteService, voteGeneralMonitoringDTO);
		if (initData == false)
			return (mapping.findForward(ERROR_LIST));
		
		voteMonitoringForm.setCurrentTab("1");
		voteGeneralMonitoringDTO.setCurrentTab("1");
		
	    String toolContentID=voteMonitoringForm.getToolContentID();
		
		voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		//Come back to fix here
		voteGeneralMonitoringDTO.setCountAllUsers(new Integer(0).toString());
		voteGeneralMonitoringDTO.setCountSessionComplete(new Integer(0).toString());
		
		VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
		
		voteMonitoringAction.initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		voteMonitoringAction.initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
	    
		/*true means there is at least 1 response*/
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
		    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
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
		
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
		
		/*this section is needed for Edit Activity screen, from  here... */
		voteGeneralAuthoringDTO.setActivityTitle(voteGeneralMonitoringDTO.getActivityTitle());
		voteGeneralAuthoringDTO.setActivityInstructions(voteGeneralMonitoringDTO.getActivityInstructions());
		voteGeneralAuthoringDTO.setDefaultOptionContent(voteGeneralMonitoringDTO.getDefaultOptionContent());
		voteGeneralAuthoringDTO.setMapOptionsContent(voteGeneralMonitoringDTO.getMapOptionsContent());
		voteGeneralAuthoringDTO.setActiveModule(MONITORING);
		
		Map mapOptionsContent=voteGeneralMonitoringDTO.getMapOptionsContent();
	    int maxIndex=mapOptionsContent.size();
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		/*...till here */
		
		//voteMonitoringAction.prepareReflectionData(request, voteContent, voteService, null, false);
		
		return voteMonitoringAction.submitSession(mapping, voteMonitoringForm,  request, response, voteService, messageService, voteGeneralMonitoringDTO);
	}

	
	public boolean initialiseMonitoringData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,
	        IVoteService voteService, VoteGeneralMonitoringDTO voteGeneralMonitoringDTO)
	{
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
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		
		if (voteContent == null)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			voteGeneralMonitoringDTO.setUserExceptionContentDoesNotExist(new Boolean(true).toString());
			return false;
		}
		
		
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		}
		
		voteGeneralMonitoringDTO.setActivityTitle(voteContent.getTitle());
		voteGeneralMonitoringDTO.setActivityInstructions(voteContent.getInstructions());

		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			voteGeneralMonitoringDTO.setUserExceptionContentInUse(new Boolean(true).toString());
			voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
		}
		else
		{
		    voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		}

	    /*
		 * get the nominations 
		 * section  is needed for the Edit tab's View Only mode, starts here
		 */
		
		SessionMap sessionMap = new SessionMap();
	    sessionMap.put(ACTIVITY_TITLE_KEY, voteContent.getTitle());
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, voteContent.getInstructions());
	    	    
	    voteMonitoringForm.setHttpSessionID(sessionMap.getSessionID());
	    request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

		
		List listNominationContentDTO= new  LinkedList();
		
		Map mapOptionsContent= new TreeMap(new VoteComparator());
		mapOptionsContent.clear();
		Iterator queIterator=voteContent.getVoteQueContents().iterator();
		Long mapIndex=new Long(1);
		while (queIterator.hasNext())
		{
		    VoteNominationContentDTO voteNominationContentDTO=new VoteNominationContentDTO();
		    
			VoteQueContent voteQueContent=(VoteQueContent) queIterator.next();
			if (voteQueContent != null)
			{
				mapOptionsContent.put(mapIndex.toString(),voteQueContent.getQuestion());

				voteNominationContentDTO.setQuestion(voteQueContent.getQuestion());
				voteNominationContentDTO.setDisplayOrder(new Integer(voteQueContent.getDisplayOrder()).toString());
				listNominationContentDTO.add(voteNominationContentDTO);
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		voteGeneralMonitoringDTO.setMapOptionsContent(mapOptionsContent);
		/* ends here*/
		
		
		request.setAttribute(LIST_NOMINATION_CONTENT_DTO,listNominationContentDTO);
		sessionMap.put(LIST_NOMINATION_CONTENT_DTO_KEY, listNominationContentDTO);
		
		request.setAttribute(TOTAL_NOMINATION_COUNT, new Integer(listNominationContentDTO.size()));
		
		
		VoteMonitoringAction voteMonitoringAction= new VoteMonitoringAction();
		voteMonitoringAction.refreshSummaryData(request, voteContent, voteService, true, false, 
		        null, null, false, null, voteGeneralMonitoringDTO, null);
		
		voteMonitoringAction.refreshStatsData(request, voteService, voteGeneralMonitoringDTO);
		
	    voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(false).toString());

    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		isContentInUse=VoteUtils.isContentInUse(voteContent);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

        voteMonitoringAction.prepareReflectionData(request, voteContent, voteService, null, false, "All");

	    
	    
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(voteService, voteContent);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
		    
		    if (userExceptionNoToolSessions.equals("true"))
		    {
		        request.setAttribute(NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    }

		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}

	MonitoringUtil.buildVoteStatsDTO(request, voteService, voteContent);

	return true;
	}


	protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping, VoteMonitoringForm voteMonitoringForm)
	{
    	
    	String strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
    	 
	    if ((strToolContentId == null) || (strToolContentId.length() == 0)) 
	    {
	    	VoteUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST));
	    }
	    else
	    {
	    	try
			{
		    	voteMonitoringForm.setToolContentID(strToolContentId);
			}
	    	catch(NumberFormatException e)
			{
	    		logger.error("add error.numberFormatException to ActionMessages.");
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
		logger.error("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}  
