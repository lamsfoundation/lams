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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
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
 *  @author Ozgur Demirtas
 * 
*/
public class VoteMonitoringAction extends LamsDispatchAction implements VoteAppConstants
{
	static Logger logger = Logger.getLogger(VoteMonitoringAction.class.getName());
	
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
	*/
    public ActionForward unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
    {
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching unspecified...");
    	request.getSession().setAttribute(IS_MONITORED_CONTENT_IN_USE, new Boolean(false).toString());
	 	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	 	return null;
    }
    
    
    public ActionForward submitSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching submitSession...");
		logger.debug("test3: MAP_STANDARD_NOMINATIONS_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_CONTENT));
		logger.debug("test3: MAP_STANDARD_RATES_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_RATES_CONTENT));
    	
    	IVoteService voteService=null;

	    voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("voteService: " + voteService);
		
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
    	
    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
		logger.debug("existing voteContent:" + voteContent);

	    /* SELECTION_CASE == 1 indicates a selected group other than "All" */
		if (currentMonitoredToolSession.equals("All"))
	    {
		    request.getSession().setAttribute(SELECTION_CASE, new Long(2));
		    logger.debug("generate DTO for All sessions: ");
		    List listVoteAllSessionsDTO=MonitoringUtil.prepareChartDTO(request, voteService, voteMonitoringForm, voteContent.getVoteContentId());
		    logger.debug("listVoteAllSessionsDTO: " + listVoteAllSessionsDTO);
		    
		    request.getSession().setAttribute(LIST_VOTE_ALLSESSIONS_DTO, listVoteAllSessionsDTO);
	    }
	    else
	    {
		    logger.debug("preparing chart data for content id: " + voteContent.getVoteContentId());
		    logger.debug("preparing chart data for currentMonitoredToolSession: " + currentMonitoredToolSession);
		    MonitoringUtil.prepareChartData(request, voteService, voteMonitoringForm, voteContent.getVoteContentId(), new Long(currentMonitoredToolSession));

		    refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true);
		    request.getSession().setAttribute(SELECTION_CASE, new Long(1));

	    }
	    logger.debug("SELECTION_CASE: " + request.getSession().getAttribute(SELECTION_CASE));
	    
	    
	    request.getSession().setAttribute(CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);
	    logger.debug("CURRENT_MONITORED_TOOL_SESSION: " + request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION));
	    
	    voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());
	    request.getSession().setAttribute(REQUEST_LEARNING_REPORT, new Boolean(false).toString());
	    
		logger.debug("test4: MAP_STANDARD_NOMINATIONS_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_NOMINATIONS_CONTENT));
		logger.debug("test4: MAP_STANDARD_RATES_CONTENT: " + request.getSession().getAttribute(MAP_STANDARD_RATES_CONTENT));

		logger.debug("test4: LIST_VOTE_ALLSESSIONS_DTO: " + request.getSession().getAttribute(LIST_VOTE_ALLSESSIONS_DTO));
    	return (mapping.findForward(LOAD_MONITORING));	
	}

    
	public void refreshSummaryData(HttpServletRequest request, VoteContent voteContent, IVoteService voteService, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId, boolean showUserEntriesBySession)
	{
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}
		if (voteService == null)
		{
	    	voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
			logger.debug("voteService: " + voteService);
		}

		logger.debug("voteService: " + voteService);
		
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
				
		/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, voteContent, voteService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		
		
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
			logger.debug("error.noLearnerActivity must be displayed");
		}
		
		
		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);
	    	
	    /* SELECTION_CASE == 2 indicates start up */
	    request.getSession().setAttribute(SELECTION_CASE, new Long(2));
	    logger.debug("SELECTION_CASE: " + request.getSession().getAttribute(SELECTION_CASE));
	    
	    /* Default to All for tool Sessions so that all tool sessions' summary information gets displayed when the module starts up */
	    request.getSession().setAttribute(CURRENT_MONITORED_TOOL_SESSION, "All");
	    logger.debug("CURRENT_MONITORED_TOOL_SESSION: " + request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION));
	    
	    
	    logger.debug("using allUsersData to retrieve data: " + isUserNamesVisible);
	    List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionData(request, voteContent, 
	    		isUserNamesVisible, isLearnerRequest, currentSessionId, userId, voteService);
	    request.getSession().setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);
	    logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));
	    /* ends here. */
	    
	    logger.debug("decide processing user entered values based on isLearnerRequest: " + isLearnerRequest);

	    List listUserEntries=processUserEnteredNominations(voteService, voteContent, currentSessionId, showUserEntriesBySession, userId, isLearnerRequest);
	    logger.debug("listUserEntries: " + listUserEntries);
	    request.getSession().setAttribute(LIST_USER_ENTRIES, listUserEntries);    
	}


	public List processUserEnteredNominations(IVoteService voteService, VoteContent voteContent, String currentSessionId, boolean showUserEntriesBySession, String userId, boolean showUserEntriesByUserId)
	{
	    logger.debug("start getting user entries, showUserEntriesBySession: " + showUserEntriesBySession);
	    logger.debug("start getting user entries, currentSessionId: " + currentSessionId);
	    logger.debug("start getting user entries, showUserEntriesByUserId: " + showUserEntriesByUserId);
	    logger.debug("start getting user entries, userId: " + userId);
	    logger.debug("start getting user entries, voteContent: " + voteContent);
	    logger.debug("start getting user entries, voteContent id: " + voteContent.getVoteContentId());
	    
	    Set userEntries=voteService.getUserEntries();
	    logger.debug("userEntries: " + userEntries);
	    
	    List listUserEntries=new LinkedList();
	    
	    Iterator itListQuestions = userEntries.iterator();
	    while (itListQuestions.hasNext())
	    {
	    	String  userEntry =(String)itListQuestions.next();
	    	logger.debug("userEntry:..." + userEntry);
	    	
	    	if ((userEntry != null) && (userEntry.length() > 0))
	    	{
	    		VoteMonitoredAnswersDTO voteMonitoredAnswersDTO= new VoteMonitoredAnswersDTO();
	    		logger.debug("adding user entry : " + userEntry);
	    		voteMonitoredAnswersDTO.setQuestion(userEntry);
	    		
	    		List userRecords=voteService.getUserRecords(userEntry);
	    		logger.debug("userRecords: " + userRecords);
	    		
	    		logger.debug("start processing user records: ");
	    		
	    		List listMonitoredUserContainerDTO= new LinkedList();
				
	    	    Iterator itUserRecords= userRecords.iterator();
	    	    while (itUserRecords.hasNext())
	    	    {
	    	        VoteMonitoredUserDTO voteMonitoredUserDTO = new VoteMonitoredUserDTO();
	    	        logger.debug("new DTO created");
	    	        VoteUsrAttempt voteUsrAttempt =(VoteUsrAttempt)itUserRecords.next();
	    	    	logger.debug("voteUsrAttempt: " + voteUsrAttempt);
	    	    	
    	    	    
    	    	    VoteSession localUserSession=voteUsrAttempt.getVoteQueUsr().getVoteSession();
    	    	    logger.debug("localUserSession: " + localUserSession);
    	    	    logger.debug("localUserSession's content id: " + localUserSession.getVoteContentId()); 
    	    	    logger.debug("incoming content id versus localUserSession's content id: " + voteContent.getVoteContentId() + " versus " +  localUserSession.getVoteContentId());

	    	    	if (showUserEntriesBySession == false)
	    	    	{
	    	    	    logger.debug("showUserEntriesBySession is false");	    	    	    
	    	    	    logger.debug("show user  entries  by same content only");
	    	    	    if (voteContent.getVoteContentId().toString().equals(localUserSession.getVoteContentId().toString()))
	    	    	    {
			    			voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime().toString());
			    			voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
			    			voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getUsername());
			    			voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
			    			voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
			    			voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
			    			voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());
	    	    	        listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
	    	    	    }
	    	    	}
	    	    	else
	    	    	{
	    	    	    logger.debug("showUserEntriesBySession is true: the case with learner export portfolio");
	    	    	    logger.debug("show user  entries  by same content and same session and same user");
	    	    	    String userSessionId=voteUsrAttempt.getVoteQueUsr().getVoteSession().getVoteSessionId().toString() ;
	    	    	    logger.debug("userSessionId versus currentSessionId: " + userSessionId + " versus " +  currentSessionId);
	    	    	    
	    	    	    if (showUserEntriesByUserId == true)
	    	    	    {
	    	    	        if (voteContent.getVoteContentId().toString().equals(localUserSession.getVoteContentId().toString()))
		    	    	    {
			    	    	    logger.debug("showUserEntriesByUserId is true");
			    	    	    if (userSessionId.equals(currentSessionId))
			    	    	    {
			    	    	        String localUserId=voteUsrAttempt.getVoteQueUsr().getQueUsrId().toString(); 
			    	    	        if (userId.equals(localUserId))
				    	    	    {
				    	    	        logger.debug("this is requested by user id: "  + userId);
						    			voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime().toString());
						    			voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
						    			voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getUsername());
						    			voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
						    			voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
						    			listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
						    			voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
						    			voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());		
						    			logger.debug("overriding the nomination text with 'Nomination Hidden' if needed");
						    			logger.debug("is entry visible: " + voteUsrAttempt.isVisible());
						    			if (voteUsrAttempt.isVisible() == false)
						    			{
						    			    voteMonitoredAnswersDTO.setQuestion("Nomination Hidden");
						    			    logger.debug("overwrote the nomination text");
						    			}
						    			    
				    	    	    }
			    	    	    }
		    	    	    }
	    	    	    }
	    	    	    else
	    	    	    {
	    	    	        logger.debug("showUserEntriesByUserId is false");
	    	    	        logger.debug("show user  entries  by same content and same session");
	    	    	        logger.debug("voteContent.getVoteContentId() versus localUserSession.getVoteContentId().toString(): " + 
	    	    	                voteContent.getVoteContentId() + " versus " + localUserSession.getVoteContentId());
	    	    	        if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString()))
	    	    	        {
			    	    	    if (userSessionId.equals(currentSessionId))
			    	    	    {
			    	    	        logger.debug("this is requested by session id: "  + currentSessionId);
					    			voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime().toString());
					    			voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
					    			voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getUsername());
					    			voteMonitoredUserDTO.setQueUsrId(voteUsrAttempt.getVoteQueUsr().getUid().toString());
					    			voteMonitoredUserDTO.setUserEntry(voteUsrAttempt.getUserEntry());
			    	    	        listMonitoredUserContainerDTO.add(voteMonitoredUserDTO);
					    			voteMonitoredUserDTO.setUid(voteUsrAttempt.getUid().toString());
					    			voteMonitoredUserDTO.setVisible(new Boolean(voteUsrAttempt.isVisible()).toString());					    			
			    	    	    }	    	    	            
	    	    	        }
	    	    	    }
	    	    	}
	    	    	
	    	    }
	    	    
	    		logger.debug("final listMonitoredUserContainerDTO: " + listMonitoredUserContainerDTO);
	    		
	    		logger.debug("final listMonitoredUserContainerDTO size: " + listMonitoredUserContainerDTO.size());
	    		if (listMonitoredUserContainerDTO.size() > 0)
	    		{
		    		logger.debug("adding user entry's data");
		    		Map mapMonitoredUserContainerDTO=MonitoringUtil.convertToVoteMonitoredUserDTOMap(listMonitoredUserContainerDTO);
		    		logger.debug("final user entry mapMonitoredUserContainerDTO:..." + mapMonitoredUserContainerDTO);

		    	    voteMonitoredAnswersDTO.setQuestionAttempts(mapMonitoredUserContainerDTO);
		    		listUserEntries.add(voteMonitoredAnswersDTO);	    		    
	    		}
			}
		}
	    logger.debug("finish getting user entries: " + listUserEntries);
	    return listUserEntries;
	}

	
    public void initSummaryContent(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("start  initSummaryContent...");
    	logger.debug("dispatching getSummary..." + request);
   	    	
		IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("voteService: " + voteService);
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}
		
    	Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
		logger.debug("existing voteContent:" + voteContent);
		
    	/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, voteContent, voteService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
	    logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS));
	    /* ends here. */
	    
		/*true means there is at least 1 response*/
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

    	request.getSession().setAttribute(CURRENT_MONITORING_TAB, "summary");
    	logger.debug("end  initSummaryContent...");
	}
    
    
    public void initInstructionsContent(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("starting initInstructionsContent...");
    	logger.debug("dispatching getInstructions..." + request);

    	IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("voteService: " + voteService);
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}

	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
		logger.debug("existing voteContent:" + voteContent);
		
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}
		
    	refreshInstructionsData(request, voteContent, voteService);

    	request.getSession().setAttribute(CURRENT_MONITORING_TAB, "instructions");
    	logger.debug("ending  initInstructionsContent...");
	}

	public void refreshInstructionsData(HttpServletRequest request, VoteContent voteContent, IVoteService voteService)
	{
	    request.getSession().setAttribute(RICHTEXT_ONLINEINSTRUCTIONS,voteContent.getOnlineInstructions());
	    request.getSession().setAttribute(RICHTEXT_OFFLINEINSTRUCTIONS,voteContent.getOfflineInstructions());
	    
        /*process offline files metadata*/
	    List listOfflineFilesMetaData=voteService.getOfflineFilesMetaData(voteContent.getUid());
	    logger.debug("existing listOfflineFilesMetaData, to be structured as VoteAttachmentDTO: " + listOfflineFilesMetaData);
	    listOfflineFilesMetaData=AuthoringUtil.populateMetaDataAsAttachments(listOfflineFilesMetaData);
	    logger.debug("populated listOfflineFilesMetaData: " + listOfflineFilesMetaData);
	    request.getSession().setAttribute(LIST_OFFLINEFILES_METADATA, listOfflineFilesMetaData);
	    
	    List listUploadedOfflineFileNames=AuthoringUtil.populateMetaDataAsFilenames(listOfflineFilesMetaData);
	    logger.debug("returned from db listUploadedOfflineFileNames: " + listUploadedOfflineFileNames);
	    request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES, listUploadedOfflineFileNames);
	    
	    /*process online files metadata*/
	    List listOnlineFilesMetaData=voteService.getOnlineFilesMetaData(voteContent.getUid());
	    logger.debug("existing listOnlineFilesMetaData, to be structured as VoteAttachmentDTO: " + listOnlineFilesMetaData);
	    listOnlineFilesMetaData=AuthoringUtil.populateMetaDataAsAttachments(listOnlineFilesMetaData);
	    logger.debug("populated listOnlineFilesMetaData: " + listOnlineFilesMetaData);
	    request.getSession().setAttribute(LIST_ONLINEFILES_METADATA, listOnlineFilesMetaData);
	    
	    List listUploadedOnlineFileNames=AuthoringUtil.populateMetaDataAsFilenames(listOnlineFilesMetaData);
	    logger.debug("returned from db listUploadedOnlineFileNames: " + listUploadedOnlineFileNames);
	    request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES, listUploadedOnlineFileNames);
	}

    
    public void initStatsContent(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("starting  initStatsContent...");
    	logger.debug("dispatching getStats..." + request);
    	
    	IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("voteService: " + voteService);
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}

	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
		logger.debug("existing voteContent:" + voteContent);
		
		
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

    	refreshStatsData(request);
    	
    	request.getSession().setAttribute(CURRENT_MONITORING_TAB, "stats");
    	logger.debug("ending  initStatsContent...");
	}
    
	public void refreshStatsData(HttpServletRequest request)
	{
		/* it is possible that no users has ever logged in for the activity yet*/
		IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("voteService: " + voteService);
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}

		
	    int countAllUsers=voteService.getTotalNumberOfUsers();
		logger.debug("countAllUsers: " + countAllUsers);
		
		if (countAllUsers == 0)
		{
	    	logger.debug("error: countAllUsers is 0");
	    	request.getSession().setAttribute(USER_EXCEPTION_NO_STUDENT_ACTIVITY, new Boolean(true));
		}
		
		request.getSession().setAttribute(COUNT_ALL_USERS, new Integer(countAllUsers).toString());
 		
		int countSessionComplete=voteService.countSessionComplete();
		logger.debug("countSessionComplete: " + countSessionComplete);
		request.getSession().setAttribute(COUNT_SESSION_COMPLETE, new Integer(countSessionComplete).toString());
	}
	
    public ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
    {
    	logger.debug("dispatching editActivityQuestions...");
    	
	    VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    
    	request.getSession().setAttribute(IS_MONITORED_CONTENT_IN_USE, new Boolean(false).toString());
		request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(true));
		
		VoteUtils.setDefineLater(request, true);
		
	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
    	IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
	    if (voteService == null)        
	    	voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	    logger.debug("voteService :" +voteService);


    	VoteContent voteContent=voteService.retrieveVote(toolContentId);
		/*true means there is at least 1 response*/
    	if (voteContent != null)
    	{
    		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
    		{
    				request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(false).toString());
    				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
    		}
    		else
    		{
    			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
    			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
    		}
    	}

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");
    	
	    voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());
	    logger.debug("submit success is false");
        return (mapping.findForward(LOAD_MONITORING));
    }
	
    
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
    	logger.debug("dispatching proxy submitAllContent...");
    	request.getSession().setAttribute(ACTIVE_MODULE, MONITORING);

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");

    	VoteAction voteAction= new VoteAction(); 
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());
	    voteMonitoringForm.setActiveModule(DEFINE_LATER);

		
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

		
		boolean isContentSubmitted=voteAction.submitContent(mapping, form, request, response);
		logger.debug("isContentSubmitted :" +isContentSubmitted);
		
		if (isContentSubmitted == true)
		    voteMonitoringForm.setSbmtSuccess(new Boolean(true).toString());

		request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(false));
		logger.debug("final submit status :" +voteMonitoringForm.getSbmtSuccess());
        return (mapping.findForward(destination));	
    }
    
    
    public ActionForward addNewNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
    	logger.debug("dispatching proxy addNewNomination...");

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");
    	
	    VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

    	VoteAction voteAction= new VoteAction(); 
    	
    	boolean isNewNominationAdded=voteAction.isNewNominationAdded(mapping, form, request, response);
		logger.debug("isNewNominationAdded:" + isNewNominationAdded);
        return (mapping.findForward(destination));
    }


    public ActionForward removeNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
    	logger.debug("dispatching proxy removeNomination...");
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
		logger.debug("voteMonitoringForm: " + voteMonitoringForm);
		String optIndex=voteMonitoringForm.getOptIndex() ;
		logger.debug("optIndex: " + optIndex);
		request.getSession().setAttribute(REMOVABLE_QUESTION_INDEX, optIndex);

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");

    	VoteAction voteAction= new VoteAction(); 
    	
		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);
		
		boolean isNominationRemoved=voteAction.isNominationRemoved(mapping, form, request, response);
		logger.debug("isNominationRemoved :" +isNominationRemoved);

		return (mapping.findForward(destination));
    }

    
    public ActionForward moveNominationDown(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy moveNominationDown...");

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");
    	
    	VoteAction voteAction= new VoteAction(); 

    	/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);
    	
		boolean isMoveNominationDown=voteAction.isMoveNominationDown(mapping, form, request, response);
		logger.debug("isMoveNominationDown :" +isMoveNominationDown);

		return (mapping.findForward(destination));
    }
    
    
    public ActionForward moveNominationUp(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy moveNominationUp...");

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");

    	/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

    	VoteAction voteAction= new VoteAction(); 
		boolean isMoveNominationUp=voteAction.isMoveNominationUp(mapping, form, request, response);
		logger.debug("isMoveNominationUp:" + isMoveNominationUp);
		
		return (mapping.findForward(destination));

    }
	
    /**
     * calls learning action endLearning functionality 
     * ActionForward endLearning(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
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
    public ActionForward endLearning(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
    {
    	logger.debug("dispatching proxy endLearning to learning module...");
    	IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("voteService: " + voteService);
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}

    	VoteLearningAction voteLearningAction= new VoteLearningAction();
    	return null; 
    }


    public ActionForward viewOpenVotes(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching viewOpenVotes...");
        IVoteService voteService=null;
	    voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		}
		logger.debug("voteService: " + voteService);
		
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
    	voteMonitoringForm.setShowOpenVotesSection(new Boolean(true).toString());
    	
    	logger.debug("showOpen votes set to true: ");
    	
    	Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
		logger.debug("existing voteContent:" + voteContent);

    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

		refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true);
		
		if (currentMonitoredToolSession.equals("All"))
	    {
		    request.getSession().setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
		    request.getSession().setAttribute(SELECTION_CASE, new Long(1));
	    }
	    logger.debug("SELECTION_CASE: " + request.getSession().getAttribute(SELECTION_CASE));

	    request.getSession().setAttribute(CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);
	    logger.debug("CURRENT_MONITORED_TOOL_SESSION: " + request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION));
        
    	return (mapping.findForward(LOAD_MONITORING));
     }
    

    public ActionForward closeOpenVotes(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching closeOpenVotes...");
        IVoteService voteService=null;
	    voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		}
		logger.debug("voteService: " + voteService);
		
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
    	voteMonitoringForm.setShowOpenVotesSection(new Boolean(false).toString());
    	
    	logger.debug("showOpen votes set to false: ");
        
    	return (mapping.findForward(LOAD_MONITORING));
     }
    
    

    public ActionForward hideOpenVote(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching hideOpenVote...");
        IVoteService voteService=null;
	    voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		}
		logger.debug("voteService: " + voteService);
		
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
    	String currentUid=voteMonitoringForm.getCurrentUid();
    	logger.debug("currentUid: " + currentUid);
        VoteUsrAttempt voteUsrAttempt =voteService.getAttemptByUID(new Long(currentUid));
        logger.debug("voteUsrAttempt: " + voteUsrAttempt);
        voteUsrAttempt.setVisible(false);
        voteService.updateVoteUsrAttempt(voteUsrAttempt);
        logger.debug("hiding the user entry : " + voteUsrAttempt.getUserEntry());
        voteService.hideOpenVote(voteUsrAttempt);
        
    	Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
		logger.debug("existing voteContent:" + voteContent);

    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

		refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true);
		
		if (currentMonitoredToolSession.equals("All"))
	    {
		    request.getSession().setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
		    request.getSession().setAttribute(SELECTION_CASE, new Long(1));
	    }
	    logger.debug("SELECTION_CASE: " + request.getSession().getAttribute(SELECTION_CASE));
	    request.getSession().setAttribute(CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);
	    logger.debug("CURRENT_MONITORED_TOOL_SESSION: " + request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION));
        
    	//return (mapping.findForward(LOAD_MONITORING));
	    logger.debug("submitting session to refresh the data from the database: ");
    	return submitSession(mapping, form,  request, response);
    	
     }

    public ActionForward showOpenVote(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching showOpenVote...");
        IVoteService voteService=null;
	    voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
		
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		}
		logger.debug("voteService: " + voteService);
		
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;

    	String currentUid=voteMonitoringForm.getCurrentUid();
    	logger.debug("currentUid: " + currentUid);
        VoteUsrAttempt voteUsrAttempt =voteService.getAttemptByUID(new Long(currentUid));
        logger.debug("voteUsrAttempt: " + voteUsrAttempt);
        voteUsrAttempt.setVisible(true);
        voteService.updateVoteUsrAttempt(voteUsrAttempt);
        voteService.showOpenVote(voteUsrAttempt);
        logger.debug("voteUsrAttempt: " + voteUsrAttempt);

    	Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    VoteContent voteContent=voteService.retrieveVote(toolContentId);
		logger.debug("existing voteContent:" + voteContent);

    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

		refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true);
		
		if (currentMonitoredToolSession.equals("All"))
	    {
		    request.getSession().setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
		    request.getSession().setAttribute(SELECTION_CASE, new Long(1));
	    }
	    logger.debug("SELECTION_CASE: " + request.getSession().getAttribute(SELECTION_CASE));

	    request.getSession().setAttribute(CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);
	    logger.debug("CURRENT_MONITORED_TOOL_SESSION: " + request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION));

    	//return (mapping.findForward(LOAD_MONITORING));
	    logger.debug("submitting session to refresh the data from the database: ");
	    return submitSession(mapping, form,  request, response);	    
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
    