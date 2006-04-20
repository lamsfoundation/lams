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
import java.util.List;
import java.util.Map;

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
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * * @author Ozgur Demirtas
 * 
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
    
	public void refreshSummaryData(HttpServletRequest request, VoteContent voteContent, IVoteService voteService, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId)
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

		
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
				
		/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, voteContent, voteService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		
		
		if (voteService.studentActivityOccurredGlobal(voteContent))
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
	    		isUserNamesVisible, isLearnerRequest, currentSessionId, userId);
	    request.getSession().setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);
	    logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));
	    /* ends here. */
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
		
    	refreshInstructionsData(request, voteContent);

    	request.getSession().setAttribute(CURRENT_MONITORING_TAB, "instructions");
    	logger.debug("ending  initInstructionsContent...");
	}

	public void refreshInstructionsData(HttpServletRequest request, VoteContent voteContent)
	{
	    request.getSession().setAttribute(RICHTEXT_ONLINEINSTRUCTIONS,voteContent.getOnlineInstructions());
	    request.getSession().setAttribute(RICHTEXT_OFFLINEINSTRUCTIONS,voteContent.getOfflineInstructions());
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
    	request.getSession().setAttribute(IS_MONITORED_CONTENT_IN_USE, new Boolean(false).toString());

		request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(true));
		VoteUtils.setDefineLater(request, true);
        return (mapping.findForward(LOAD_MONITORING));
    }
	
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
    	logger.debug("dispatching proxy submitAllContent...");
    	request.getSession().setAttribute(ACTIVE_MODULE, DEFINE_LATER);
    	
    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");

    	VoteAction voteAction= new VoteAction(); 
    	return voteAction.submitAllContent(mapping, form, request, response);
    	
    }
    
    public ActionForward addNewOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
    	logger.debug("dispatching proxy editActivityQuestions...");

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");
    	
    	VoteAction voteAction= new VoteAction(); 
    	return voteAction.addNewOption(mapping, form, request, response);
    }


    public ActionForward removeOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
    	logger.debug("dispatching proxy removeOption...");
    	VoteMonitoringForm voteMonitoringForm = (VoteMonitoringForm) form;
		logger.debug("voteMonitoringForm: " + voteMonitoringForm);
		String optIndex=voteMonitoringForm.getOptIndex() ;
		logger.debug("optIndex: " + optIndex);
		request.getSession().setAttribute(REMOVABLE_QUESTION_INDEX, optIndex);

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");

    	VoteAction voteAction= new VoteAction(); 
    	return voteAction.removeOption(mapping, form, request, response);
    }

    
    public ActionForward moveOptionDown(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy moveOptionDown...");

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");
    	
    	VoteAction voteAction= new VoteAction(); 
    	return voteAction.moveOptionDown(mapping, form, request, response);
    }
    
    public ActionForward moveOptionUp(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy moveOptionUp...");

    	request.setAttribute(SOURCE_VOTE_STARTER, "monitoring");
	    logger.debug("SOURCE_VOTE_STARTER: monitoring");
    	
    	VoteAction voteAction= new VoteAction(); 
    	return voteAction.moveOptionUp(mapping, form, request, response);
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
    