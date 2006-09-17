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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

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
 * @author Ozgur Demirtas
 * 
 * 
 *    <!--Learning Main Action: interacts with the Learning module user -->
	<action	path="/learning"
		    type="org.lamsfoundation.lams.tool.vote.web.VoteLearningAction"
			name="VoteLearningForm"
      		scope="request"
      		input="/learning/AnswersContent.jsp"
      		parameter="dispatch">
      		
	  	<forward
		    name="loadLearner"
		    path="/learning/AnswersContent.jsp"
		    redirect="false"
	  	/>
	  	
	  	<forward
		    name="allNominations"
		    path="/learning/AllNominations.jsp"
		    redirect="false"
	  	/>

	  	
	  	<forward
		    name="individualReport"
		    path="/learning/IndividualLearnerResults.jsp"
		    redirect="false"
	  	/>
	  	
	  	<forward
		    name="redoQuestions"
		    path="/learning/RedoQuestions.jsp"
		    redirect="false"
	  	/>

	  	<forward
		    name="viewAnswers"
		    path="/learning/ViewAnswers.jsp"
		    redirect="false"
	  	/>


	  	<forward
		    name="exitPage"
		    path="/learning/ExitLearning.jsp"
		    redirect="false"
	  	/>
	  	

	  	<forward
	        name="starter"
	        path="/index.jsp"
		    redirect="false"
	     />

	  	<forward
		    name="learningStarter"
		    path="/learningIndex.jsp"
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

 * 
*/
public class VoteLearningAction extends LamsDispatchAction implements VoteAppConstants
{
	static Logger logger = Logger.getLogger(VoteLearningAction.class.getName());
	
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
        VoteLearningForm voteLearningForm = (VoteLearningForm) form;
        voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
        
        repopulateRequestParameters(request,voteLearningForm);
    	
        VoteUtils.cleanUpUserExceptions(request);
    	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	 	return null;
    }

    
    public ActionForward viewAllResults(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{   
        VoteUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching viewAllResults...");
		VoteLearningForm voteLearningForm = (VoteLearningForm) form;
		VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO= new VoteGeneralLearnerFlowDTO();
		
		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
		voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
		
		voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
		voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
		
		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("retrieving voteService from session: " + voteService);

    	repopulateRequestParameters(request,voteLearningForm);
    	
        String toolSessionID=request.getParameter(TOOL_SESSION_ID);
        logger.debug("toolSessionID: " + toolSessionID);
        voteLearningForm.setToolSessionID(toolSessionID);
        
        String userID=request.getParameter(USER_ID);
    	logger.debug("userID: " + userID);
    	voteLearningForm.setUserID(userID);
    	
    	VoteSession voteSession=voteService.retrieveVoteSession(new Long(toolSessionID));
        logger.debug("retrieving voteSession: " + voteSession);
        
        VoteContent voteContent=voteSession.getVoteContent();
        logger.debug("retrieving voteContent: " + voteContent);
        
	    voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	    voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

        Long toolContentID=voteContent.getVoteContentId();
        logger.debug("toolContentID: " + toolContentID);
        
        setContentInUse(request,voteService, toolContentID);
        
        
        Long toolSessionUid=voteSession.getUid();
        logger.debug("toolSessionUid: " + toolSessionUid);
        
    	VoteQueUsr existingVoteQueUsr=voteService.getVoteUserBySession(new Long(userID), voteSession.getUid());
    	logger.debug("existingVoteQueUsr: " + existingVoteQueUsr);
    	
    	existingVoteQueUsr.setFinalScreenRequested(true);
    	voteService.updateVoteUser(existingVoteQueUsr);
    	logger.debug("final screen requested by: " + existingVoteQueUsr);


		Set userAttempts=voteService.getAttemptsForUserAndSession(existingVoteQueUsr.getUid(), toolSessionUid);
		logger.debug("userAttempts: "+ userAttempts);
		request.setAttribute(LIST_GENERAL_CHECKED_OPTIONS_CONTENT, userAttempts);
		
	    logger.debug("voteSession uid:" + voteSession.getUid());
	    logger.debug("calling prepareChartData:" +  voteContent.getVoteContentId() + " " +  voteSession.getUid());

	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();
	    MonitoringUtil.prepareChartData(request, voteService, null, voteContent.getVoteContentId().toString(), 
	            voteSession.getUid().toString(), voteGeneralLearnerFlowDTO, voteGeneralMonitoringDTO);
	    logger.debug("end of  prepareChartData:" +  voteContent.getVoteContentId() + " " +  voteSession.getUid());
		
	    
		voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
		voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());
	    
	    voteLearningForm.resetCommands();
    	
    	logger.debug("view-only voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
 		request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO,voteGeneralLearnerFlowDTO);

    	
    	logger.debug("fwding to ALL_NOMINATIONS: "+ ALL_NOMINATIONS);
	    return (mapping.findForward(ALL_NOMINATIONS));
    }
    
    
    public ActionForward viewAnswers(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{   
        VoteUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching viewAnswers...");
		
		VoteLearningForm voteLearningForm = (VoteLearningForm) form;
		VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO= new VoteGeneralLearnerFlowDTO();
		
		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
		voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
		
		voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
		voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
		
		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("retrieving voteService from session: " + voteService);

    	repopulateRequestParameters(request,voteLearningForm);
    	
        String toolSessionID=request.getParameter(TOOL_SESSION_ID);
        logger.debug("toolSessionID: " + toolSessionID);
        voteLearningForm.setToolSessionID(toolSessionID);
        
        String userID=request.getParameter(USER_ID);
    	logger.debug("userID: " + userID);
    	voteLearningForm.setUserID(userID);
    	
    	String revisitingUser=request.getParameter(REVISITING_USER);
    	logger.debug("revisitingUser: " + revisitingUser);
    	voteLearningForm.setRevisitingUser(revisitingUser);

    	VoteSession voteSession=voteService.retrieveVoteSession(new Long(toolSessionID));
        logger.debug("retrieving voteSession: " + voteSession);
        
        VoteContent voteContent=voteSession.getVoteContent();
        logger.debug("retrieving voteContent: " + voteContent);
        
	    voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	    voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

        
        Long toolContentID=voteContent.getVoteContentId();
        logger.debug("toolContentID: " + toolContentID);
    	
	 	setContentInUse(request, voteService, toolContentID);
	 	
	 	if (revisitingUser.equals("true"))
	 	{
	 	    logger.debug("this is a revisiting user, get the nominations from the db: " + revisitingUser);
	    	logger.debug("toolContentID: " + toolContentID);

	    	VoteQueUsr voteQueUsr=LearningUtil.getUser(request, voteService);
	    	logger.debug("voteQueUsr: " + voteQueUsr);

	    	List attempts=voteService.getAttemptsForUser(voteQueUsr.getUid());
	    	logger.debug("attempts: " + attempts);
	    	
	    	Map mapQuestionsContent= new TreeMap(new VoteComparator());
			Iterator listIterator=attempts.iterator();
			int order=0;
	    	while (listIterator.hasNext())
	    	{
	    	    VoteUsrAttempt attempt=(VoteUsrAttempt)listIterator.next();
	        	logger.debug("attempt: " + attempt);
	        	VoteQueContent voteQueContent=attempt.getVoteQueContent();
	        	logger.debug("voteQueContent: " + voteQueContent);        	
	        	order++;
	    		if (voteQueContent != null)
	    		{
	            	mapQuestionsContent.put(new Integer(order).toString(),voteQueContent.getQuestion());
	    		}
	    	}
	    	request.setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapQuestionsContent);
	 	}
	 	else
	 	{
	 	   logger.debug("this is not a revisiting user: " + revisitingUser);
	 	}
	 	
	 	voteLearningForm.resetCommands();
	 	
	 	logger.debug("final voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
 		request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO,voteGeneralLearnerFlowDTO);
	 	
    	logger.debug("fwd'ing to : " + VIEW_ANSWERS);
		return (mapping.findForward(VIEW_ANSWERS));
    }
    
    
    public ActionForward redoQuestionsOk(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{   
        VoteUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching redoQuestionsOk...");

		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("retrieving voteService from session: " + voteService);

		VoteLearningForm voteLearningForm = (VoteLearningForm) form;
		VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO= new VoteGeneralLearnerFlowDTO();

    	repopulateRequestParameters(request,voteLearningForm);
    	
        String toolSessionID=request.getParameter(TOOL_SESSION_ID);
        logger.debug("toolSessionID: " + toolSessionID);
        voteLearningForm.setToolSessionID(toolSessionID);
        
        String userID=request.getParameter(USER_ID);
    	logger.debug("userID: " + userID);
    	voteLearningForm.setUserID(userID);
    	
    	String revisitingUser=request.getParameter(REVISITING_USER);
    	logger.debug("revisitingUser: " + revisitingUser);
    	voteLearningForm.setRevisitingUser(revisitingUser);

		
		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
		voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
		
		voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
		voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());

		
    	VoteSession voteSession=voteService.retrieveVoteSession(new Long(toolSessionID));
        logger.debug("retrieving voteSession: " + voteSession);
        
        VoteContent voteContent=voteSession.getVoteContent();
        logger.debug("retrieving voteContent: " + voteContent);
		
	    voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	    voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

        Long toolContentID=voteContent.getVoteContentId();
        logger.debug("toolContentID: " + toolContentID);
		
	 	setContentInUse(request, voteService, toolContentID);
		logger.debug("requested redoQuestionsOk, user is sure to redo the questions.");

		voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
		voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());
	    
		voteLearningForm.resetCommands();

		logger.debug("final voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
 		request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO,voteGeneralLearnerFlowDTO);
		return redoQuestions(mapping, form, request, response);
	}

    
    public ActionForward learnerFinished(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{   
        logger.debug("dispatching learnerFinished");
		logger.debug("requested learner finished, the learner should be directed to next activity.");

		VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO= new VoteGeneralLearnerFlowDTO();
		VoteLearningForm voteLearningForm = (VoteLearningForm) form;
		
		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
		voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
	 	
		voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
		voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());

		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("retrieving voteService from session: " + voteService);

		
    	repopulateRequestParameters(request,voteLearningForm);
    	
        String toolSessionID=request.getParameter(TOOL_SESSION_ID);
        logger.debug("toolSessionID: " + toolSessionID);
        voteLearningForm.setToolSessionID(toolSessionID);
        
        VoteSession voteSession=voteService.retrieveVoteSession(new Long(toolSessionID));
        logger.debug("retrieving voteSession: " + voteSession);
        
        String userID=request.getParameter(USER_ID);
    	logger.debug("userID: " + userID);
    	voteLearningForm.setUserID(userID);
    	
    	VoteQueUsr voteQueUsr=voteService.getVoteUserBySession(new Long(userID), voteSession.getUid());
        logger.debug("voteQueUsr:" + voteQueUsr);
        
        
	    /* it is possible that voteQueUsr can be null if the content is set as runoffline and reflection is on*/
	    if (voteQueUsr == null)
	    {
    		logger.debug("attempt creating  user record since it must exist for the runOffline + reflection screens");
		    HttpSession ss = SessionManager.getSession();

		    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    	logger.debug("retrieving toolUser: " + toolUser);
	    	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());
	    	logger.debug("retrieving toolUser username: " + toolUser.getLogin());

	    	String userName=toolUser.getLogin(); 
	    	String fullName= toolUser.getFirstName() + " " + toolUser.getLastName();
	    	logger.debug("retrieving toolUser fullname: " + fullName);
	    	
	    	Long userId=new Long(toolUser.getUserID().longValue());
	    	logger.debug("retrieving toolUser fullname: " + fullName);
	    	
	    	 voteQueUsr= new VoteQueUsr(new Long(userID), 
	    	        userName, 
	    	        fullName,  
	    	        voteSession, 
					new TreeSet());		
    		voteService.createVoteQueUsr(voteQueUsr);
	    	logger.debug("createVoteQueUsr - voteQueUsr: " + voteQueUsr);
	    	logger.debug("session uid: " + voteSession.getUid());
	    }

        
        voteQueUsr.setResponseFinalised(true);
        voteService.updateVoteUser(voteQueUsr);
        logger.debug("user's response is finalised:" + voteQueUsr);
    	
    	String revisitingUser=request.getParameter(REVISITING_USER);
    	logger.debug("revisitingUser: " + revisitingUser);
    	voteLearningForm.setRevisitingUser(revisitingUser);
    	
        
        VoteContent voteContent=voteSession.getVoteContent();
        logger.debug("retrieving voteContent: " + voteContent);
        
	    voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	    voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

		logger.debug("attempting to leave/complete session with toolSessionID:" + toolSessionID + " and userID:"+userID);
		
		voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
		voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());
	    
		logger.debug("final voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
 		request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO,voteGeneralLearnerFlowDTO);

		
		VoteUtils.cleanUpSessionAbsolute(request);
		
		String nextUrl=null;
		try
		{
			nextUrl=voteService.leaveToolSession(new Long(toolSessionID), new Long(userID));
			logger.debug("nextUrl: "+ nextUrl);
		}
		catch (DataMissingException e)
		{
			logger.debug("failure getting nextUrl: "+ e);
    		voteLearningForm.resetCommands();
			//throw new ServletException(e);
    		return (mapping.findForward(LEARNING_STARTER));
		}
		catch (ToolException e)
		{
			logger.debug("failure getting nextUrl: "+ e);
    		voteLearningForm.resetCommands();
			//throw new ServletException(e);
    		return (mapping.findForward(LEARNING_STARTER));        		
		}
		catch (Exception e)
		{
			logger.debug("unknown exception getting nextUrl: "+ e);
    		voteLearningForm.resetCommands();
			//throw new ServletException(e);
    		return (mapping.findForward(LEARNING_STARTER));        		
		}

		logger.debug("success getting nextUrl: "+ nextUrl);
		voteLearningForm.resetCommands();
		
		/* pay attention here*/
		logger.debug("redirecting to the nextUrl: "+ nextUrl);
		response.sendRedirect(nextUrl);
		
		return null;

    }

    public ActionForward continueOptionsCombined(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{   
        VoteUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching continueOptionsCombined...");
		
		VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO= new VoteGeneralLearnerFlowDTO();
		VoteLearningForm voteLearningForm = (VoteLearningForm) form;
		
		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("retrieving voteService from session: " + voteService);

    	repopulateRequestParameters(request,voteLearningForm);
    	
        String toolSessionID=request.getParameter(TOOL_SESSION_ID);
        logger.debug("toolSessionID: " + toolSessionID);
        voteLearningForm.setToolSessionID(toolSessionID);
        
        String userID=request.getParameter(USER_ID);
    	logger.debug("userID: " + userID);
    	voteLearningForm.setUserID(userID);
    	
    	String maxNominationCount=request.getParameter(MAX_NOMINATION_COUNT);
    	logger.debug("maxNominationCount: " + maxNominationCount);
    	voteLearningForm.setMaxNominationCount(maxNominationCount);
    	
    	String userEntry=request.getParameter(USER_ENTRY);
    	logger.debug("userEntry: " + userEntry);
    	voteLearningForm.setUserEntry(userEntry);


    	VoteSession voteSession=voteService.retrieveVoteSession(new Long(toolSessionID));
        logger.debug("retrieving voteSession: " + voteSession);

		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
		voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());

		voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
		voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());
		

		Collection<String> voteDisplayOrderIds = voteLearningForm.votesAsCollection();
		logger.debug("Checkbox votes "+voteDisplayOrderIds);
		
		// check number of votes
		int castVoteCount= voteDisplayOrderIds!=null ? voteDisplayOrderIds.size() : 0;
    	logger.debug("userEntry: " + userEntry);
    	
    	if ((userEntry != null) && (userEntry.length() > 0))
    	{
    	    logger.debug("userEntry available: " + userEntry);
    	    ++castVoteCount;
    	}
    	logger.debug("castVoteCount post user entry count: " + castVoteCount);
    	logger.debug("maxNominationCount: " + maxNominationCount);
    	
    	int intMaxNominationCount=0;
    	if (maxNominationCount != null)
    	    intMaxNominationCount=new Integer(maxNominationCount).intValue();
    	logger.debug("intMaxNominationCount: " + intMaxNominationCount);
    	logger.debug("intMaxNominationCount versus current voting count: " + intMaxNominationCount + " versus " + castVoteCount );

    	if (castVoteCount > intMaxNominationCount )
    	{
    	    voteLearningForm.setMaxNominationCountReached(new Boolean(true).toString());
    	    voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(true).toString());
    	    persistInRequestError(request, "error.maxNominationCount.reached");
	        logger.debug("give warning,  max nom count reached...");
    	    logger.debug("fwd'ing to: " + LOAD_LEARNER);
    	    return (mapping.findForward(LOAD_LEARNER));
    	}

        VoteContent voteContent=voteSession.getVoteContent();
        logger.debug("retrieving voteContent: " + voteContent);

	    voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	    voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

        Long toolContentID=voteContent.getVoteContentId();
        logger.debug("toolContentID: " + toolContentID);

        Long toolContentUID=voteContent.getUid();
        logger.debug("toolContentUID: " + toolContentUID);
    	logger.debug("userEntry: " + userEntry);

    	setContentInUse(request,voteService, toolContentID);

    	boolean userEntryAvailable=false;
    	if ((userEntry != null) && (userEntry.length() > 0))
    	{
    	    logger.debug("userEntry available: " + userEntry);
    	    userEntryAvailable=true;
    	}
    	logger.debug("userEntryAvailable " + userEntryAvailable);

    	Long toolSessionUid=voteSession.getUid();
        logger.debug("toolSessionUid: " + toolSessionUid);

    	
    	boolean isUserDefined=false;
    	logger.debug("userID: " + userID);
    	VoteQueUsr existingVoteQueUsr=voteService.getVoteUserBySession(new Long(userID), voteSession.getUid());
    	logger.debug("existingVoteQueUsr: " + existingVoteQueUsr);
    	
    	if (existingVoteQueUsr != null)
    	    isUserDefined=true;
    	
    	logger.debug("isUserDefined: " + isUserDefined);
    	
    	
    	VoteQueUsr voteQueUsr=null;
    	if (isUserDefined == false)
    	{
    	    voteQueUsr=LearningUtil.createUser(request, voteService, new Long(toolSessionID));
    		logger.debug("created user in the db");
    		logger.debug("new create");
    	}
    	else
    	{
    	    voteQueUsr=existingVoteQueUsr;
    	    logger.debug("assign");
    	}
    	
    	logger.debug("voteQueUsr: " + voteQueUsr);
    	logger.debug("voteQueUsr is : " + voteQueUsr);
    	
    	if (existingVoteQueUsr != null)
    	{
    	    logger.debug("attempt removing attempts for user id and session id:" + existingVoteQueUsr.getUid() + " " + voteSession.getUid() );
    	    voteService.removeAttemptsForUserandSession(existingVoteQueUsr.getUid(), voteSession.getUid());
        	logger.debug("votes deleted for user: " + voteQueUsr.getUid());
    	}
    	
    	/* to mimize changes to working code, convert the String[] array to the mapGeneralCheckedOptionsContent structure */ 
    	Map mapGeneralCheckedOptionsContent = LearningUtil.buildQuestionContentMap(request, voteService, voteContent, voteDisplayOrderIds);
    	logger.debug("mapGeneralCheckedOptionsContent size: " + mapGeneralCheckedOptionsContent.size());
    	
    	if (mapGeneralCheckedOptionsContent.size() > 0)
    	{
    	    LearningUtil.createAttempt(request, voteService, voteQueUsr, mapGeneralCheckedOptionsContent, 
    	            userEntry, false, voteSession, toolContentUID);
    	}
    	
    	
    	if ((mapGeneralCheckedOptionsContent.size() == 0  && (userEntryAvailable == true)))
    	{
    		logger.debug("mapGeneralCheckedOptionsContent size is 0");
    		Map mapLeanerCheckedOptionsContent= new TreeMap(new VoteComparator());
    	
			logger.debug("after mapsize check  mapLeanerCheckedOptionsContent " + mapLeanerCheckedOptionsContent);
			if (userEntry.length() > 0)
			{
			    logger.debug("creating entry for: " + userEntry);
			    LearningUtil.createAttempt(request, voteService, voteQueUsr, mapLeanerCheckedOptionsContent, userEntry, 
			            true, voteSession, toolContentUID);    
			}
    	}
    	
    	if ((mapGeneralCheckedOptionsContent.size() > 0) && (userEntryAvailable == true))
    	{
    		logger.debug("mapGeneralCheckedOptionsContent size is > 0" + userEntry);
    		Map mapLeanerCheckedOptionsContent= new TreeMap(new VoteComparator());
    	
			logger.debug("after mapsize check  mapLeanerCheckedOptionsContent " + mapLeanerCheckedOptionsContent);
			if (userEntry.length() > 0)
			{
			    logger.debug("creating entry for: " + userEntry);
			    LearningUtil.createAttempt(request, voteService, voteQueUsr, mapLeanerCheckedOptionsContent, userEntry,
			            false, voteSession, toolContentUID);    
			}
    	}
    
    	
    	logger.debug("created user attempt in the db");
    	
    	/* put the map in the request ready for the next screen */
    	request.setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);
    	logger.debug("final mapGeneralCheckedOptionsContent: " + mapGeneralCheckedOptionsContent);
    	voteLearningForm.setMapGeneralCheckedOptionsContent(mapGeneralCheckedOptionsContent);
    	voteGeneralLearnerFlowDTO.setMapGeneralCheckedOptionsContent(mapGeneralCheckedOptionsContent);
    	
    	voteLearningForm.setNominationsSubmited(new Boolean(true).toString());
    	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(true).toString());
    	
    	VoteGeneralMonitoringDTO voteGeneralMonitoringDTO=new VoteGeneralMonitoringDTO();
    	logger.debug("calling  prepareChartData: " + toolContentID);
    	MonitoringUtil.prepareChartData(request, voteService, null, toolContentID.toString(), toolSessionUid.toString(), 
    	        voteGeneralLearnerFlowDTO, voteGeneralMonitoringDTO);
    	
    	voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
		voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());
	    
    	logger.debug("fwding to INDIVIDUAL_REPORT: " + INDIVIDUAL_REPORT);
    	voteLearningForm.resetCommands();
    	
	 	logger.debug("final voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
 		request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO,voteGeneralLearnerFlowDTO);

    	return (mapping.findForward(INDIVIDUAL_REPORT));
    }

     public ActionForward redoQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
   {
        logger.debug("dispatching redoQuestions...");
    	VoteUtils.cleanUpUserExceptions(request);

    	VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO= new VoteGeneralLearnerFlowDTO();
    	VoteLearningForm voteLearningForm = (VoteLearningForm) form;
    	
		IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("retrieving voteService from session: " + voteService);

    	repopulateRequestParameters(request,voteLearningForm);
    	
        String toolSessionID=request.getParameter(TOOL_SESSION_ID);
        logger.debug("toolSessionID: " + toolSessionID);
        voteLearningForm.setToolSessionID(toolSessionID);
        
        String userID=request.getParameter(USER_ID);
    	logger.debug("userID: " + userID);
    	voteLearningForm.setUserID(userID);
    	
    	String revisitingUser=request.getParameter(REVISITING_USER);
    	logger.debug("revisitingUser: " + revisitingUser);
    	voteLearningForm.setRevisitingUser(revisitingUser);

    	
    	VoteSession voteSession=voteService.retrieveVoteSession(new Long(toolSessionID));
        logger.debug("retrieving voteSession: " + voteSession);
        
        VoteContent voteContent=voteSession.getVoteContent();
        logger.debug("retrieving voteContent: " + voteContent);

	    voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	    voteGeneralLearnerFlowDTO.setActivityInstructions(voteContent.getInstructions());

    	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
    	voteLearningForm.setMaxNominationCountReached(new Boolean(false).toString());
    	
    	voteGeneralLearnerFlowDTO.setNominationsSubmited(new Boolean(false).toString());
    	voteGeneralLearnerFlowDTO.setMaxNominationCountReached(new Boolean(false).toString());

    	Long toolContentID=voteContent.getVoteContentId();
    	logger.debug("toolContentID:" + toolContentID);
    	
    	Map mapQuestionsContent= new TreeMap(new VoteComparator());
    	mapQuestionsContent=LearningUtil.buildQuestionContentMap(request, voteService, voteContent,null);
	    logger.debug("mapQuestionsContent: " + mapQuestionsContent);
		
		request.setAttribute(MAP_QUESTION_CONTENT_LEARNER, mapQuestionsContent);
		logger.debug("MAP_QUESTION_CONTENT_LEARNER: " +  request.getAttribute(MAP_QUESTION_CONTENT_LEARNER));
		logger.debug("voteContent has : " + mapQuestionsContent.size() + " entries.");
		
		Map mapGeneralCheckedOptionsContent= new TreeMap(new VoteComparator());
	    request.setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);
	    
	    voteLearningForm.setUserEntry("");
	    
	    voteGeneralLearnerFlowDTO.setReflection(new Boolean(voteContent.isReflect()).toString());
		voteGeneralLearnerFlowDTO.setReflectionSubject(voteContent.getReflectionSubject());
	    
	    logger.debug("fwd'ing to LOAD_LEARNER : " + LOAD_LEARNER);
	    voteLearningForm.resetCommands();
	    
	 	logger.debug("final voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
 		request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO,voteGeneralLearnerFlowDTO);

	    return (mapping.findForward(LOAD_LEARNER));
   }

    
    
    protected void setContentInUse(HttpServletRequest request, IVoteService voteService, Long toolContentID)
    {
    	logger.debug("toolContentID:" + toolContentID);
    	
    	VoteContent voteContent=voteService.retrieveVote(toolContentID);
    	logger.debug("voteContent:" + voteContent);
    	voteContent.setContentInUse(true);
    	logger.debug("content has been set to inuse");
    	voteService.saveVoteContent(voteContent);
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

    
    
    public ActionForward submitReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException, ToolException
	{ 
        logger.debug("dispatching submitReflection...");
    	VoteLearningForm voteLearningForm = (VoteLearningForm) form;        
    	
    	repopulateRequestParameters(request,voteLearningForm);
    	
    	IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);

	 	String toolSessionID=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	 	logger.debug("toolSessionID: " + toolSessionID);
	 	voteLearningForm.setToolSessionID(toolSessionID);

	 	String userID=request.getParameter("userID");
	 	logger.debug("userID: " + userID);	 	
	 	voteLearningForm.setUserID(userID);
	 	
	 	String reflectionEntry=request.getParameter(ENTRY_TEXT);
	 	logger.debug("reflectionEntry: " + reflectionEntry);

	 	VoteSession voteSession=voteService.retrieveVoteSession(new Long(toolSessionID));
	    logger.debug("retrieving voteSession: " + voteSession);
	
	    VoteQueUsr voteQueUsr=voteService.getVoteUserBySession(new Long(userID), voteSession.getUid());
	    logger.debug("voteQueUsr:" + voteQueUsr);
	    
	    /* it is possible that voteQueUsr can be null if the content is set as runoffline and reflection is on*/
	    if (voteQueUsr == null)
	    {
    		logger.debug("attempt creating  user record since it must exist for the runOffline + reflection screens");
		    HttpSession ss = SessionManager.getSession();

		    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    	logger.debug("retrieving toolUser: " + toolUser);
	    	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());
	    	logger.debug("retrieving toolUser username: " + toolUser.getLogin());

	    	String userName=toolUser.getLogin(); 
	    	String fullName= toolUser.getFirstName() + " " + toolUser.getLastName();
	    	logger.debug("retrieving toolUser fullname: " + fullName);
	    	
	    	Long userId=new Long(toolUser.getUserID().longValue());
	    	logger.debug("retrieving toolUser fullname: " + fullName);
	    	
	    	 voteQueUsr= new VoteQueUsr(new Long(userID), 
	    	        userName, 
	    	        fullName,  
	    	        voteSession, 
					new TreeSet());		
    		voteService.createVoteQueUsr(voteQueUsr);
	    	logger.debug("createVoteQueUsr - voteQueUsr: " + voteQueUsr);
	    	logger.debug("session uid: " + voteSession.getUid());
	    }
	    
	    logger.debug("voteQueUsr:" + voteQueUsr);
	    logger.debug("toolSessionID:" + toolSessionID);
	    logger.debug("CoreNotebookConstants.NOTEBOOK_TOOL:" + CoreNotebookConstants.NOTEBOOK_TOOL);
	    logger.debug("MY_SIGNATURE:" + MY_SIGNATURE);
	    logger.debug("userID:" + userID);
	    logger.debug("reflectionEntry:" + reflectionEntry);
	    
		voteService.createNotebookEntry(new Long(toolSessionID), CoreNotebookConstants.NOTEBOOK_TOOL,
				MY_SIGNATURE, new Integer(userID), reflectionEntry);
	    
		voteLearningForm.resetUserActions(); /*resets all except submitAnswersContent */
	    return learnerFinished(mapping, form, request, response);
	}
    
    
    public ActionForward forwardtoReflection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException, ToolException
	{
        logger.debug("dispatching forwardtoReflection...");
        VoteLearningForm voteLearningForm = (VoteLearningForm) form;
        IVoteService voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
		logger.debug("voteService: " + voteService);        
        
	 	String toolSessionID=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	 	logger.debug("toolSessionID: " + toolSessionID);
	 	
	 	VoteSession voteSession=voteService.retrieveVoteSession(new Long(toolSessionID));
	 	logger.debug("retrieving voteSession: " + voteSession);
	    
	    VoteContent voteContent=voteSession.getVoteContent();
	    logger.debug("using voteContent: " + voteContent);
	    
	    VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO= new VoteGeneralLearnerFlowDTO();
	    voteGeneralLearnerFlowDTO.setActivityTitle(voteContent.getTitle());
	    
	    String reflectionSubject=voteContent.getReflectionSubject();
	    reflectionSubject=VoteUtils.replaceNewLines(reflectionSubject);
	    
	    voteGeneralLearnerFlowDTO.setReflectionSubject(reflectionSubject);
	    request.setAttribute(VOTE_GENERAL_LEARNER_FLOW_DTO, voteGeneralLearnerFlowDTO);
	    
		logger.debug("final voteGeneralLearnerFlowDTO: " + voteGeneralLearnerFlowDTO);
		voteLearningForm.resetCommands();
        
		logger.debug("fwd'ing to: " + NOTEBOOK);
        return (mapping.findForward(NOTEBOOK));
	}    
    
    protected void repopulateRequestParameters(HttpServletRequest request, VoteLearningForm voteLearningForm)
    {
        logger.debug("starting repopulateRequestParameters");
        
        String toolSessionID=request.getParameter(TOOL_SESSION_ID);
        logger.debug("toolSessionID: " + toolSessionID);
        voteLearningForm.setToolSessionID(toolSessionID);
        
        String userID=request.getParameter(USER_ID);
    	logger.debug("userID: " + userID);
    	voteLearningForm.setUserID(userID);

    	String revisitingUser=request.getParameter(REVISITING_USER);
    	logger.debug("revisitingUser: " + revisitingUser);
    	voteLearningForm.setRevisitingUser(revisitingUser);

    	String previewOnly=request.getParameter(PREVIEW_ONLY);
    	logger.debug("previewOnly: " + previewOnly);
    	voteLearningForm.setPreviewOnly(previewOnly);

    	String maxNominationCount=request.getParameter(MAX_NOMINATION_COUNT);
    	logger.debug("maxNominationCount: " + maxNominationCount);
    	voteLearningForm.setMaxNominationCount(maxNominationCount);

    	String allowTextEntry=request.getParameter(ALLOW_TEXT_ENTRY);
    	logger.debug("allowTextEntry: " + allowTextEntry);
    	voteLearningForm.setAllowTextEntry(allowTextEntry);
    	
    	String voteChangable=request.getParameter(VOTE_CHANGABLE);
    	logger.debug("voteChangable: " + voteChangable);
    	voteLearningForm.setVoteChangable(voteChangable);

    	String lockOnFinish=request.getParameter(LOCK_ON_FINISH);
    	logger.debug("lockOnFinish: " + lockOnFinish);
    	voteLearningForm.setLockOnFinish(lockOnFinish);

    	String reportViewOnly=request.getParameter(REPORT_VIEW_ONLY);
    	logger.debug("reportViewOnly: " + reportViewOnly);
    	voteLearningForm.setReportViewOnly(reportViewOnly);
    	
    	String userEntry=request.getParameter(USER_ENTRY);
    	logger.debug("userEntry: " + userEntry);
    	voteLearningForm.setUserEntry(userEntry);
    }
}


