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
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.ExportPortfolioDTO;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
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
	 	return null;
    }

    
    public ActionForward submitSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response,
            IVoteService voteService,
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO
            ) throws IOException,
                                         ServletException
	{
        logger.debug("calling submitSession...voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
        commonSubmitSessionCode(form, request, voteService,voteGeneralMonitoringDTO);
        logger.debug("post commonSubmitSessionCode: " +voteGeneralMonitoringDTO);
    	return (mapping.findForward(LOAD_MONITORING));
	}
    

    protected void commonSubmitSessionCode(ActionForm form, HttpServletRequest request,IVoteService voteService,
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException, ServletException
    {
    	logger.debug("starting  commonSubmitSessionCode...voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
    	
		logger.debug("voteService:" + voteService);
		VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
		
		repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
		logger.debug("done repopulateRequestParameters");

    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

	    String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);


		if (currentMonitoredToolSession.equals("All"))
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
		    
		    logger.debug("generate DTO for All sessions: ");
		    List listVoteAllSessionsDTO=MonitoringUtil.prepareChartDTO(request, voteService, voteMonitoringForm, voteContent.getVoteContentId());
		    logger.debug("listVoteAllSessionsDTO: " + listVoteAllSessionsDTO);
		    voteGeneralMonitoringDTO.setListVoteAllSessionsDTO(listVoteAllSessionsDTO);
	    }
	    else
	    {
		    logger.debug("preparing chart data for content id: " + voteContent.getVoteContentId());
		    logger.debug("preparing chart data for currentMonitoredToolSession: " + currentMonitoredToolSession);
		    
		    VoteSession voteSession=voteService.retrieveVoteSession(new Long(currentMonitoredToolSession));
    		logger.debug("voteSession uid:" + voteSession.getUid());
		    MonitoringUtil.prepareChartData(request, voteService, voteMonitoringForm, voteContent.getVoteContentId().toString(), 
		            voteSession.getUid().toString(), null, voteGeneralMonitoringDTO);
		    
		    logger.debug("post prepareChartData, voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);

		    refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true, null, 
		            voteGeneralMonitoringDTO, null);
		    logger.debug("session_name: " + voteSession.getSession_name());
		    voteGeneralMonitoringDTO.setGroupName(voteSession.getSession_name());
		    logger.debug("post refreshSummaryData, voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	    }
		logger.debug("SELECTION_CASE: " + voteGeneralMonitoringDTO.getSelectionCase());
	    
		voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
	    voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());
	    voteGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
	    voteGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());
	    
		
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, voteContent, voteService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);

		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);

		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		
		/*setting editable screen properties*/
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
		voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());

		Map mapOptionsContent= new TreeMap(new VoteComparator());
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
	    		    voteGeneralAuthoringDTO.setDefaultOptionContent(voteQueContent.getQuestion());
	    		}
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		
		logger.debug("mapOptionsContent: " + mapOptionsContent);
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

		voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
		
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
			voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		}

		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
		
		logger.debug("end of commonSubmitSessionCode, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
    }
    
    
    public ActionForward submitSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) 
    		throws IOException,ServletException
	{
        logger.debug("dispathcing submitSession..");
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
        logger.debug("voteService: " +voteService);
        
        VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
        
        commonSubmitSessionCode(form, request, voteService,voteGeneralMonitoringDTO);
        logger.debug("post commonSubmitSessionCode: " +voteGeneralMonitoringDTO);

        return (mapping.findForward(LOAD_MONITORING));	
	}

    
	public void refreshSummaryData(HttpServletRequest request, VoteContent voteContent, IVoteService voteService, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId, 
			boolean showUserEntriesBySession, VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO, 
			VoteGeneralMonitoringDTO voteGeneralMonitoringDTO, ExportPortfolioDTO exportPortfolioDTO)
	{
	    logger.debug("doing refreshSummaryData." + voteGeneralLearnerFlowDTO);
	    logger.debug("voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
	    logger.debug("exportPortfolioDTO:" + exportPortfolioDTO);
	    
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}
		logger.debug("voteService: " + voteService);
		
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
				
		/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, voteContent, voteService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		
		if (voteService.studentActivityOccurredStandardAndOpen(voteContent))
		{
		    if (voteGeneralMonitoringDTO != null)
		        voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
		    if (voteGeneralMonitoringDTO != null)
		        voteGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
				logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}
		
		String userExceptionNoToolSessions=voteGeneralMonitoringDTO.getUserExceptionNoToolSessions();
		logger.debug("userExceptionNoToolSessions: " + userExceptionNoToolSessions);
		if (exportPortfolioDTO != null)
		{
		    exportPortfolioDTO.setUserExceptionNoToolSessions(userExceptionNoToolSessions);
		}
		
		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
	    logger.debug("currentSessionId: " + currentSessionId);
	    
	    if ((currentSessionId != null) && (!currentSessionId.equals("All")))
	    {
		    VoteSession voteSession= voteService.retrieveVoteSession(new Long(currentSessionId));
		    logger.debug("voteSession:" + voteSession);
		    if (voteGeneralMonitoringDTO != null)
		        voteGeneralMonitoringDTO.setGroupName(voteSession.getSession_name());
	    }
	    else
	    {
	        voteGeneralMonitoringDTO.setGroupName("All Groups");
	    }
	    
	    logger.debug("using allUsersData to retrieve data: " + isUserNamesVisible);
	    List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionData(request, voteContent, 
	    		isUserNamesVisible, isLearnerRequest, currentSessionId, userId, voteService);
	    logger.debug("listMonitoredAnswersContainerDTO: " + listMonitoredAnswersContainerDTO);	    
	    /* ends here. */
	    
	    logger.debug("decide processing user entered values based on isLearnerRequest: " + isLearnerRequest);

	    List listUserEntries=processUserEnteredNominations(voteService, voteContent, currentSessionId, showUserEntriesBySession, userId, isLearnerRequest);
	    logger.debug("listUserEntries: " + listUserEntries);
	    
	    if (voteGeneralLearnerFlowDTO != null)
	    {
	        logger.debug("placing dtos within the voteGeneralLearnerFlowDTO: ");
	        voteGeneralLearnerFlowDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	        voteGeneralLearnerFlowDTO.setListUserEntries(listUserEntries);;
	    }

	    
	    if (exportPortfolioDTO != null)
	    {
	        logger.debug("placing dtos within the exportPortfolioDTO: ");
	        exportPortfolioDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
	        exportPortfolioDTO.setListUserEntries(listUserEntries);;
	    }
	    
	    
	    if (voteGeneralMonitoringDTO != null)
	    {
	        voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);
	        voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);
	        voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	        voteGeneralMonitoringDTO.setCurrentMonitoredToolSession("All");
	        voteGeneralMonitoringDTO.setListMonitoredAnswersContainerDto(listMonitoredAnswersContainerDTO);
		    voteGeneralMonitoringDTO.setListUserEntries(listUserEntries);
		    
		    voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(false).toString());
		    if (listUserEntries.size() > 0)
		    {
		        voteGeneralMonitoringDTO.setExistsOpenVotes(new Boolean(true).toString());
		    }
	    }
	    
	    logger.debug("final voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
	    logger.debug("final voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		boolean isContentInUse=VoteUtils.isContentInUse(voteContent);
		logger.debug("isContentInUse:" + isContentInUse);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
			voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		}

	    logger.debug("end of refreshSummaryData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
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
    	    	    logger.debug("incoming content uid versus localUserSession's content id: " + voteContent.getUid() + " versus " +  localUserSession.getVoteContentId());

	    	    	if (showUserEntriesBySession == false)
	    	    	{
	    	    	    logger.debug("showUserEntriesBySession is false");	    	    	    
	    	    	    logger.debug("show user  entries  by same content only");
	    	    	    if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString()))
	    	    	    {
			    			//voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime().toString());
	    	    	        voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
			    			voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
			    			voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
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
	    	    	        if (voteContent.getUid().toString().equals(localUserSession.getVoteContentId().toString()))
		    	    	    {
			    	    	    logger.debug("showUserEntriesByUserId is true");
			    	    	    if (userSessionId.equals(currentSessionId))
			    	    	    {
			    	    	        String localUserId=voteUsrAttempt.getVoteQueUsr().getQueUsrId().toString(); 
			    	    	        if (userId.equals(localUserId))
				    	    	    {
				    	    	        logger.debug("this is requested by user id: "  + userId);
						    			//voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime().toString());
				    	    	        voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
						    			voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
						    			voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
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
					    			//voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime().toString());
			    	    	        voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
					    			voteMonitoredUserDTO.setTimeZone(voteUsrAttempt.getTimeZone());
					    			voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
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

	
    public void initSummaryContent(String toolContentID,
            HttpServletRequest request,
            IVoteService voteService,
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException,
                                         ServletException
	{
    	logger.debug("start  initSummaryContent...toolContentID: " + toolContentID);
    	logger.debug("dispatching getSummary...voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);
   	    	
		logger.debug("voteService: " + voteService);
	    logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);
		
    	/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, voteContent, voteService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		voteGeneralMonitoringDTO.setSummaryToolSessions(summaryToolSessions);
		
		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, voteContent, voteService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		voteGeneralMonitoringDTO.setSummaryToolSessionsId(summaryToolSessionsId);
	    /* ends here. */
	    
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

		voteGeneralMonitoringDTO.setCurrentMonitoringTab("summary");
    	logger.debug("end  initSummaryContent...");
	}
    
    
    public void initInstructionsContent(String toolContentID,
            HttpServletRequest request,
            IVoteService voteService,
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException,
                                         ServletException
	{
    	logger.debug("starting initInstructionsContent...");
    	logger.debug("dispatching getInstructions.., voteGeneralMonitoringDTO:" + voteGeneralMonitoringDTO);

		logger.debug("voteService: " + voteService);
		if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
			logger.debug("retrieving voteService from session: " + voteService);
		}

	    logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);
		
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
		
    	refreshInstructionsData(request, voteContent, voteService, voteGeneralMonitoringDTO);
    	logger.debug("post refreshInstructionsData, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

    	voteGeneralMonitoringDTO.setCurrentMonitoringTab("instructions");
    	logger.debug("ending  initInstructionsContent...");
	}

	public void refreshInstructionsData(HttpServletRequest request, VoteContent voteContent, 
	        IVoteService voteService, VoteGeneralMonitoringDTO voteGeneralMonitoringDTO)
	{
		logger.debug("starting refreshInstructionsData, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
	    voteGeneralMonitoringDTO.setRichTextOfflineInstructions(voteContent.getOfflineInstructions());
	    voteGeneralMonitoringDTO.setRichTextOnlineInstructions(voteContent.getOnlineInstructions());
	    
        /*process offline files metadata*/
	    List listOfflineFilesMetaData=voteService.getOfflineFilesMetaData(voteContent.getUid());
	    logger.debug("existing listOfflineFilesMetaData, to be structured as VoteAttachmentDTO: " + listOfflineFilesMetaData);
	    listOfflineFilesMetaData=AuthoringUtil.populateMetaDataAsAttachments(listOfflineFilesMetaData);
	    logger.debug("populated listOfflineFilesMetaData: " + listOfflineFilesMetaData);
	    voteGeneralMonitoringDTO.setListOfflineFilesMetadata(listOfflineFilesMetaData);
	    
	    List listUploadedOfflineFileNames=AuthoringUtil.populateMetaDataAsFilenames(listOfflineFilesMetaData);
	    logger.debug("returned from db listUploadedOfflineFileNames: " + listUploadedOfflineFileNames);
	    voteGeneralMonitoringDTO.setListUploadedOfflineFileNames(listUploadedOfflineFileNames);
	    
	    /*process online files metadata*/
	    List listOnlineFilesMetaData=voteService.getOnlineFilesMetaData(voteContent.getUid());
	    logger.debug("existing listOnlineFilesMetaData, to be structured as VoteAttachmentDTO: " + listOnlineFilesMetaData);
	    listOnlineFilesMetaData=AuthoringUtil.populateMetaDataAsAttachments(listOnlineFilesMetaData);
	    logger.debug("populated listOnlineFilesMetaData: " + listOnlineFilesMetaData);
	    voteGeneralMonitoringDTO.setListOnlineFilesMetadata(listOnlineFilesMetaData);
	    
	    List listUploadedOnlineFileNames=AuthoringUtil.populateMetaDataAsFilenames(listOnlineFilesMetaData);
	    logger.debug("returned from db listUploadedOnlineFileNames: " + listUploadedOnlineFileNames);
	    voteGeneralMonitoringDTO.setListUploadedOnlineFileNames(listUploadedOnlineFileNames);

	    logger.debug("end of refreshInstructionsData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
	}

    
    public void initStatsContent(String toolContentID,
            HttpServletRequest request,
            IVoteService voteService, 
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) throws IOException,
                                         ServletException
	{
    	logger.debug("starting  initStatsContent...:" + toolContentID);
    	logger.debug("dispatching getStats..." + request);
		logger.debug("voteService: " + voteService);
		
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);
		
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

    	refreshStatsData(request, voteService,voteGeneralMonitoringDTO);
    	logger.debug("post refreshStatsData, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO );
    	
    	voteGeneralMonitoringDTO.setCurrentMonitoringTab("stats");
    	logger.debug("ending  initStatsContent...");
	}
    
    
	public void refreshStatsData(HttpServletRequest request, IVoteService voteService, 
	        VoteGeneralMonitoringDTO voteGeneralMonitoringDTO)
	{
	    logger.debug("starting refreshStatsData: " + voteService);
		/* it is possible that no users has ever logged in for the activity yet*/
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
	    	voteGeneralMonitoringDTO.setUserExceptionNoStudentActivity(new Boolean(true).toString());
		}
		
		voteGeneralMonitoringDTO.setCountAllUsers(new Integer(countAllUsers).toString());
 		
		int countSessionComplete=voteService.countSessionComplete();
		logger.debug("countSessionComplete: " + countSessionComplete);
		voteGeneralMonitoringDTO.setCountSessionComplete(new Integer(countSessionComplete).toString());
		
		logger.debug("end of refreshStatsData, voteGeneralMonitoringDTO" + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
	}
	
	
    public ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
    {
    	logger.debug("dispatching editActivityQuestions...");

        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	    
		repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
		logger.debug("done repopulateRequestParameters");

		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
		String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
	    
	    VoteUtils.setDefineLater(request, true, voteService, toolContentID.toString());
    	logger.debug("voteContent: " + voteContent);
    	
		/*true means there is at least 1 response*/
    	if (voteContent != null)
    	{
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
    	}

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");
    	
	    voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());
	    voteGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
	    logger.debug("submit success is false");
	    
	    
		initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		
		/*setting editable screen properties*/
		VoteGeneralAuthoringDTO voteGeneralAuthoringDTO= new VoteGeneralAuthoringDTO();
		voteGeneralAuthoringDTO.setActivityTitle(voteContent.getTitle());
		voteGeneralAuthoringDTO.setActivityInstructions(voteContent.getInstructions());

		Map mapOptionsContent= new TreeMap(new VoteComparator());
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
	    		    voteGeneralAuthoringDTO.setDefaultOptionContent(voteQueContent.getQuestion());
	    		}
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		
		logger.debug("mapOptionsContent: " + mapOptionsContent);
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);
    	voteGeneralAuthoringDTO.setMaxOptionIndex(maxIndex);

		voteGeneralAuthoringDTO.setMapOptionsContent(mapOptionsContent);
		voteGeneralAuthoringDTO.setActiveModule(MONITORING);
		
		logger.debug("voteGeneralAuthoringDTO: " + voteGeneralAuthoringDTO);
		request.setAttribute(VOTE_GENERAL_AUTHORING_DTO, voteGeneralAuthoringDTO);
	    
        return (mapping.findForward(LOAD_MONITORING));
    }
	
    
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
    	logger.debug("dispatching proxy submitAllContent...");

    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

		repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
		logger.debug("done repopulateRequestParameters");

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");

    	VoteAction voteAction= new VoteAction(); 
    	
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

		logger.debug("final submit status :" +voteMonitoringForm.getSbmtSuccess());
		
		
		String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

		initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);


		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		
		logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
		
        return (mapping.findForward(destination));	
    }
    
    
    public ActionForward addNewNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
    	logger.debug("dispatching proxy addNewNomination...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);


    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
	    
	    voteMonitoringForm.setSbmtSuccess(new Boolean(false).toString());
	    
	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

		/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);

    	VoteAction voteAction= new VoteAction(); 
    	
    	boolean isNewNominationAdded=voteAction.isNewNominationAdded(mapping, form, request, response);
		logger.debug("isNewNominationAdded:" + isNewNominationAdded);
		
		String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

		initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

        return (mapping.findForward(destination));
    }


    public ActionForward removeNomination(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
    	logger.debug("dispatching proxy removeNomination...");
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

    	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
    	
    	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
    	
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
		
		String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

		initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);


		return (mapping.findForward(destination));
    }

    
    public ActionForward moveNominationDown(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy moveNominationDown...");

    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);
    	
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");
    	
    	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
    	VoteAction voteAction= new VoteAction(); 

    	/* determine whether the request is from Monitoring url Edit Activity*/
		String sourceVoteStarter = (String) request.getAttribute(SOURCE_VOTE_STARTER);
		logger.debug("sourceVoteStarter: " + sourceVoteStarter);
		String destination=VoteUtils.getDestination(sourceVoteStarter);
		logger.debug("destination: " + destination);
    	
		boolean isMoveNominationDown=voteAction.isMoveNominationDown(mapping, form, request, response);
		logger.debug("isMoveNominationDown :" +isMoveNominationDown);
		
		String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

		initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		return (mapping.findForward(destination));
    }
    
    
    public ActionForward moveNominationUp(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy moveNominationUp...");
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);
    	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();

    	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

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

		String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

		initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		voteGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(true).toString());
		
	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
		
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
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO = new VoteGeneralMonitoringDTO();
    	repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
    	
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

        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

        VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();

	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

	    if (voteService == null)
		{
			logger.debug("will retrieve voteService");
			voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		}
		logger.debug("voteService: " + voteService);

    	voteMonitoringForm.setShowOpenVotesSection(new Boolean(true).toString());
    	voteGeneralMonitoringDTO.setShowOpenVotesSection(new Boolean(true).toString());
    	
    	logger.debug("showOpen votes set to true: ");
    	
		String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);

	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);

    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

		refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, 
		        null, true, null,voteGeneralMonitoringDTO, null);
		
		
		initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		
		if (currentMonitoredToolSession.equals("All"))
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	    }
	    else
	    {
	        voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	    }

		voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	    
	    logger.debug("ending editActivityQuestions, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	    logger.debug("ending viewOpenVotes, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
	    logger.debug("fwd'ing to LOAD_MONITORING: " + LOAD_MONITORING);
    	return (mapping.findForward(LOAD_MONITORING));
     }
    

    public ActionForward closeOpenVotes(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching closeOpenVotes...");
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();
		
	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
	    
    	voteMonitoringForm.setShowOpenVotesSection(new Boolean(false).toString());
    	voteGeneralMonitoringDTO.setShowOpenVotesSection(new Boolean(false).toString());
    	
    	logger.debug("showOpen votes set to false: ");
    	
		String toolContentID =voteMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
    	
    	initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		
	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);
    	
    	logger.debug("ending closeOpenVotes, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
        
    	return (mapping.findForward(LOAD_MONITORING));
     }
    
    

    public ActionForward hideOpenVote(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching hideOpenVote...");
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
	    
	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
		
    	String currentUid=voteMonitoringForm.getCurrentUid();
    	logger.debug("currentUid: " + currentUid);
    	
        VoteUsrAttempt voteUsrAttempt =voteService.getAttemptByUID(new Long(currentUid));
        logger.debug("voteUsrAttempt: " + voteUsrAttempt);
        
        voteUsrAttempt.setVisible(false);
        voteService.updateVoteUsrAttempt(voteUsrAttempt);
        logger.debug("hiding the user entry : " + voteUsrAttempt.getUserEntry());
        voteService.hideOpenVote(voteUsrAttempt);
        
        String toolContentID=voteMonitoringForm.getToolContentID();
        logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);

    	initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);

		
    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

		refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, true, null, 
		        voteGeneralMonitoringDTO, null);
		
		if (currentMonitoredToolSession.equals("All"))
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	    }
	    else
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	    }

	    voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
		
	    voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	    
	    logger.debug("voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

	    logger.debug("submitting session to refresh the data from the database: ");
    	return submitSession(mapping, form,  request, response, voteService, voteGeneralMonitoringDTO);
     }

    
    public ActionForward showOpenVote(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     {
        logger.debug("dispatching showOpenVote...");
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
	    
	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);
	    
    	String currentUid=voteMonitoringForm.getCurrentUid();
    	logger.debug("currentUid: " + currentUid);
    	
        VoteUsrAttempt voteUsrAttempt =voteService.getAttemptByUID(new Long(currentUid));
        logger.debug("voteUsrAttempt: " + voteUsrAttempt);
        voteUsrAttempt.setVisible(true);
        
        voteService.updateVoteUsrAttempt(voteUsrAttempt);
        voteService.showOpenVote(voteUsrAttempt);
        logger.debug("voteUsrAttempt: " + voteUsrAttempt);

        String toolContentID=voteMonitoringForm.getToolContentID();
        logger.debug("toolContentID: " + toolContentID);
	    
	    VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
		logger.debug("existing voteContent:" + voteContent);
		
    	initSummaryContent(toolContentID , request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		initInstructionsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initInstructionsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		initStatsContent(toolContentID, request, voteService, voteGeneralMonitoringDTO);
		logger.debug("post initStatsContent, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);


    	String currentMonitoredToolSession=voteMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);

		refreshSummaryData(request, voteContent, voteService, true, false, currentMonitoredToolSession, null, 
		        true, null, voteGeneralMonitoringDTO, null);
		
		if (currentMonitoredToolSession.equals("All"))
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(2));
	    }
	    else
	    {
		    voteGeneralMonitoringDTO.setSelectionCase(new Long(1));
	    }

		voteGeneralMonitoringDTO.setCurrentMonitoredToolSession(currentMonitoredToolSession);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		
	    logger.debug("submitting session to refresh the data from the database: ");
	    return submitSession(mapping, form,  request, response, voteService, voteGeneralMonitoringDTO);
    }


    public ActionForward getVoteNomination(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws IOException,
            ServletException, ToolException
    {
        logger.debug("dispatching getVoteNomination...");
        
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
    	logger.debug("voteService :" +voteService);
    	
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
	    logger.debug("voteMonitoringForm :" +voteMonitoringForm);
	    voteMonitoringForm.setVoteService(voteService);

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO= new VoteGeneralMonitoringDTO();
	    repopulateRequestParameters(request, voteMonitoringForm, voteGeneralMonitoringDTO);

        String questionUid=request.getParameter("questionUid");
        String sessionUid=request.getParameter("sessionUid");
        
        logger.debug("questionUid: " + questionUid);
        logger.debug("sessionUid: " + sessionUid);

		List userNames=voteService.getStandardAttemptUsersForQuestionContentAndSessionUid(new Long(questionUid), new Long(sessionUid));
		logger.debug("userNames: " + userNames);
		List listVotedLearnersDTO= new LinkedList();
		
        Iterator userIterator=userNames.iterator();
    	while (userIterator.hasNext())
    	{
    	    VoteUsrAttempt voteUsrAttempt=(VoteUsrAttempt)userIterator.next();
    	    VoteMonitoredUserDTO voteMonitoredUserDTO= new VoteMonitoredUserDTO();
    	    voteMonitoredUserDTO.setUserName(voteUsrAttempt.getVoteQueUsr().getFullname());
    	    //voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime().toString());
    	    voteMonitoredUserDTO.setAttemptTime(voteUsrAttempt.getAttemptTime());
    	    listVotedLearnersDTO.add(voteMonitoredUserDTO);
    	}
		logger.debug("listVoteAllSessionsDTO: " + listVotedLearnersDTO);
		
		voteGeneralMonitoringDTO.setMapStudentsVoted(listVotedLearnersDTO);
		voteGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
	    logger.debug("ending getVoteNomination, voteGeneralMonitoringDTO: " + voteGeneralMonitoringDTO);
		request.setAttribute(VOTE_GENERAL_MONITORING_DTO, voteGeneralMonitoringDTO);

		logger.debug("fdwing to: " + VOTE_NOMINATION_VIEWER);
		return (mapping.findForward(VOTE_NOMINATION_VIEWER));          
    }
    
    
    protected void repopulateRequestParameters(HttpServletRequest request, VoteMonitoringForm voteMonitoringForm, 
            VoteGeneralMonitoringDTO voteGeneralMonitoringDTO)
    {
        logger.debug("starting repopulateRequestParameters");
	
        String toolContentID=request.getParameter(TOOL_CONTENT_ID);
        logger.debug("toolContentID: " + toolContentID);
        voteMonitoringForm.setToolContentID(toolContentID);
        voteGeneralMonitoringDTO.setToolContentID(toolContentID);
        
        String activeModule=request.getParameter(ACTIVE_MODULE);
        logger.debug("activeModule: " + activeModule);
        voteMonitoringForm.setActiveModule(activeModule);
        voteGeneralMonitoringDTO.setActiveModule(activeModule);
        
        String defineLaterInEditMode=request.getParameter(DEFINE_LATER_IN_EDIT_MODE);
        logger.debug("defineLaterInEditMode: " + defineLaterInEditMode);
        voteMonitoringForm.setDefineLaterInEditMode(defineLaterInEditMode);
        voteGeneralMonitoringDTO.setDefineLaterInEditMode(defineLaterInEditMode);

        String isToolSessionChanged=request.getParameter(IS_TOOL_SESSION_CHANGED);
        logger.debug("isToolSessionChanged: " + isToolSessionChanged);
        voteMonitoringForm.setIsToolSessionChanged(isToolSessionChanged);
        voteGeneralMonitoringDTO.setIsToolSessionChanged(isToolSessionChanged);

        String responseId=request.getParameter(RESPONSE_ID);
        logger.debug("responseId: " + responseId);
        voteMonitoringForm.setResponseId(responseId);
        voteGeneralMonitoringDTO.setResponseId(responseId);

        String currentUid=request.getParameter(CURRENT_UID);
        logger.debug("currentUid: " + currentUid);
        voteMonitoringForm.setCurrentUid(currentUid);
        voteGeneralMonitoringDTO.setCurrentUid(currentUid);
    }    
}
    