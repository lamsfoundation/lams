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
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.web.LearningUtil;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
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
	 * if the passed toolContentId exists in the db, we need to get the relevant data into the Map 
	 * if not, create the default Map 
	*/
    public ActionForward unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
    {
    	VoteUtils.cleanUpUserExceptions(request);
	 	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	 	IVoteService voteService =VoteUtils.getToolService(request);
	 	VoteUtils.persistRichText(request);	 	
	 	voteAuthoringForm.resetUserAction();
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
		
		setContentInUse(request);
		VoteLearningForm voteLearningForm = (VoteLearningForm) form;
	 	IVoteService voteService =VoteUtils.getToolService(request);
	 	
	 	/* process the answers */
		Map mapGeneralCheckedOptionsContent=(Map) request.getSession().getAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
    	logger.debug("final mapGeneralCheckedOptionsContent: " + mapGeneralCheckedOptionsContent);
    	
    	Long toolContentId=(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("toolContentId: " + toolContentId);
    			
    	//Map mapLeanerAssessmentResults=LearningUtil.assess(request, mapGeneralCheckedOptionsContent, toolContentId);
    	//Map mapLeanerAssessmentResults=null;
    	//logger.debug("mapLeanerAssessmentResults: " + mapLeanerAssessmentResults);
    	logger.debug("assesment complete");
    	

    	boolean isUserDefined=LearningUtil.doesUserExists(request);
    	logger.debug("isUserDefined");
    	if (isUserDefined == false)
    	{
    		LearningUtil.createUser(request);
    		logger.debug("created user in the db");
    	}
    	VoteQueUsr voteQueUsr=LearningUtil.getUser(request);
    	logger.debug("voteQueUsr: " + voteQueUsr);
    	

    	LearningUtil.createAttempt(request, voteQueUsr, mapGeneralCheckedOptionsContent);
    	logger.debug("created user attempt in the db");
    	
    	voteLearningForm.resetCommands();
		return (mapping.findForward(INDIVIDUAL_REPORT));
    }

    
    public ActionForward selectOption(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
   {
        logger.debug("dispatching selectOption...");
    	VoteUtils.cleanUpUserExceptions(request);
    	VoteLearningForm voteLearningForm = (VoteLearningForm) form;
	 	IVoteService voteService =VoteUtils.getToolService(request);
	 	
    	voteLearningForm.resetParameters();
    	LearningUtil.readParameters(request, voteLearningForm);
    	
    	
		logger.debug("doing getOptionCheckBoxSelected");
		setContentInUse(request);
		voteLearningForm.resetCommands();
		LearningUtil.selectOptionsCheckBox(request,voteLearningForm, voteLearningForm.getQuestionIndex());
    	voteLearningForm.resetCommands();	
 		return (mapping.findForward(LOAD_LEARNER));
   }


    public ActionForward displayVote(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
   {
    	VoteUtils.cleanUpUserExceptions(request);
    	VoteLearningForm voteLearningForm = (VoteLearningForm) form;
	 	IVoteService voteService =VoteUtils.getToolService(request);
	 	
    	voteLearningForm.resetParameters();
    	LearningUtil.readParameters(request, voteLearningForm);
    	
    	if (voteLearningForm.getContinueOptionsCombined() != null)
    	{
    		setContentInUse(request);
    		return continueOptionsCombined(mapping, form, request, response);
    	}
    	else if (voteLearningForm.getOptionCheckBoxSelected() != null)
    	{
    		logger.debug("doing getOptionCheckBoxSelected");
    		setContentInUse(request);
    		voteLearningForm.resetCommands();
    		LearningUtil.selectOptionsCheckBox(request,voteLearningForm, voteLearningForm.getQuestionIndex());
    	}

    	else if (voteLearningForm.getRedoQuestions() != null)
    	{
    		setContentInUse(request);
    		//return redoQuestions(mapping, form, request, response);
    		return null;
    	}
    	else if (voteLearningForm.getRedoQuestionsOk() != null)
    	{
    		setContentInUse(request);
    		logger.debug("requested redoQuestionsOk, user is sure to redo the questions.");
    		voteLearningForm.resetCommands();
    		//return redoQuestions(request, voteLearningForm, mapping);
    		return null;
    	}
    	else if (voteLearningForm.getViewAnswers() != null)
    	{
    		setContentInUse(request);
    		//return viewAnswers(mapping, form, request, response);
    		return null;
    	}
    	else if (voteLearningForm.getViewSummary() != null)
    	{
    		setContentInUse(request);
			//return viewSummary(mapping, form, request, response);
    		return null;    		
    	}
    	else if (voteLearningForm.getLearnerFinished() != null)
    	{
    		logger.debug("requested learner finished, the learner should be directed to next activity.");
    		
    		Long toolSessionId = (Long) request.getSession().getAttribute(TOOL_SESSION_ID);
    		String userID=(String) request.getSession().getAttribute(USER_ID);
    		logger.debug("attempting to leave/complete session with toolSessionId:" + toolSessionId + " and userID:"+userID);
    		
    		VoteUtils.cleanUpSessionAbsolute(request);
    		
    		String nextUrl=null;
    		try
    		{
    			nextUrl=voteService.leaveToolSession(toolSessionId, new Long(userID));
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
    	else if (voteLearningForm.getDonePreview() != null)
    	{
    		logger.debug("requested  donePreview.");
        	voteLearningForm.resetCommands();
        	VoteUtils.cleanUpSessionAbsolute(request);
        	return (mapping.findForward(LEARNING_STARTER));
    	}
    	else if (voteLearningForm.getDoneLearnerProgress() != null)
    	{
    		logger.debug("requested  doneLearnerProgress.");
        	voteLearningForm.resetCommands();
        	VoteUtils.cleanUpSessionAbsolute(request);
        	return (mapping.findForward(LEARNING_STARTER));
    	}
    	
    	voteLearningForm.resetCommands();	
 		return (mapping.findForward(LOAD_LEARNER));
   }
    
    
    
    protected void setContentInUse(HttpServletRequest request)
    {
    	IVoteService voteService =VoteUtils.getToolService(request);
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("toolContentId:" + toolContentId);
    	
    	VoteContent voteContent=voteService.retrieveVote(toolContentId);
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
    public void persistError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
    
}
    