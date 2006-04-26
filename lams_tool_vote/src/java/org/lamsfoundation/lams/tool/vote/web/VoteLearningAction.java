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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
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
        VoteLearningForm voteLearningForm = (VoteLearningForm) form;
        voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
    	
        VoteUtils.cleanUpUserExceptions(request);
    	VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
    	voteLearningForm.setRevisitingPageActive(new Boolean(false).toString());
    	
	 	IVoteService voteService =VoteUtils.getToolService(request);
	 	VoteUtils.persistRichText(request);	 	
	 	voteAuthoringForm.resetUserAction();
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
		voteLearningForm.setRevisitingPageActive(new Boolean(false).toString());
		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
		
		setContentInUse(request);
		IVoteService voteService =VoteUtils.getToolService(request);
	 	
    	voteLearningForm.resetCommands();
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
		voteLearningForm.setRevisitingPageActive(new Boolean(false).toString());
		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	 	IVoteService voteService =VoteUtils.getToolService(request);
	 	setContentInUse(request);
	 	
	 	String isRevisitingUser=voteLearningForm.getRevisitingUser();
	 	logger.debug("isRevisitingUser: " + isRevisitingUser);
	 	
	 	if (isRevisitingUser.equals("true"))
	 	{
	 	    logger.debug("this is a revisiting user, get the nominations from the db: " + isRevisitingUser);
		 	Long toolContentId=(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    	logger.debug("toolContentId: " + toolContentId);

	    	VoteQueUsr voteQueUsr=LearningUtil.getUser(request);
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
	    	request.getSession().setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapQuestionsContent);
	 	}
	 	else
	 	{
	 	   logger.debug("this is not a revisiting user: " + isRevisitingUser);
	 	}
	 	
	 	voteLearningForm.resetCommands();
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
		
		VoteLearningForm voteLearningForm = (VoteLearningForm) form;
		voteLearningForm.setRevisitingPageActive(new Boolean(false).toString());
		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	 	IVoteService voteService =VoteUtils.getToolService(request);


	 	setContentInUse(request);
		logger.debug("requested redoQuestionsOk, user is sure to redo the questions.");
		voteLearningForm.resetCommands();
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

		VoteLearningForm voteLearningForm = (VoteLearningForm) form;
		voteLearningForm.setRevisitingPageActive(new Boolean(false).toString());
		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	 	IVoteService voteService =VoteUtils.getToolService(request);
	 	
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

    public ActionForward nominateVotes(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{   
        VoteUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching nominateVotes...");
		
		VoteLearningForm voteLearningForm = (VoteLearningForm) form;
		voteLearningForm.setRevisitingPageActive(new Boolean(false).toString());
		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	 	IVoteService voteService =VoteUtils.getToolService(request);
		return (mapping.findForward(INDIVIDUAL_REPORT));
	}

    
    public ActionForward continueOptionsCombined(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{   
        VoteUtils.cleanUpUserExceptions(request);
		logger.debug("dispatching continueOptionsCombined...");
		
		VoteLearningForm voteLearningForm = (VoteLearningForm) form;
		voteLearningForm.setRevisitingPageActive(new Boolean(false).toString());
		voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	 	IVoteService voteService =VoteUtils.getToolService(request);
	 	
		setContentInUse(request);
	 	Map mapGeneralCheckedOptionsContent=(Map) request.getSession().getAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
    	logger.debug("final mapGeneralCheckedOptionsContent: " + mapGeneralCheckedOptionsContent);
    	
    	Long toolContentId=(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("toolContentId: " + toolContentId);
    			
    	String userEntry=voteLearningForm.getUserEntry();
    	logger.debug("userEntry: " + userEntry);
    	
    	boolean userEntryAvailable=false;
    	if ((userEntry != null) && (userEntry.length() > 0))
    	{
    	    logger.debug("userEntry available: " + userEntry);
    	    userEntryAvailable=true;
    	}
    	logger.debug("userEntryAvailable " + userEntryAvailable);
    	
    	
    	boolean isUserDefined=LearningUtil.doesUserExists(request);
    	logger.debug("isUserDefined");
    	if (isUserDefined == false)
    	{
    		LearningUtil.createUser(request);
    		logger.debug("created user in the db");
    	}
    	VoteQueUsr voteQueUsr=LearningUtil.getUser(request);
    	logger.debug("voteQueUsr: " + voteQueUsr);
    	
    	String maxNominationCount=voteLearningForm.getMaxNominationCount();
    	logger.debug("current maxNominationCount: " + maxNominationCount);
    	
    	int intMaxNominationCount=0;
    	if (maxNominationCount != null)
    	    intMaxNominationCount=new Integer(maxNominationCount).intValue();
    	logger.debug("intMaxNominationCount: " + intMaxNominationCount);
    	
    	int nominationCount=0;
    	if (intMaxNominationCount != 0)
    	{
            	nominationCount=voteService.getLastNominationCount(voteQueUsr.getUid());
            	logger.debug("current nominationCount: " + nominationCount);

        	    Long toolSessionId=(Long)request.getSession().getAttribute(TOOL_SESSION_ID);
        	    logger.debug("current toolSessionId: " + toolSessionId);
        	    logger.debug("current user's voteSession: " + voteQueUsr.getVoteSession());
        	    logger.debug("current user's toolSessionId: " + voteQueUsr.getVoteSession().getVoteSessionId());
        	    
        	    if (voteQueUsr.getVoteSession().getVoteSessionId().toString().equals(toolSessionId.toString()))
        	    {
                	if (nominationCount >= intMaxNominationCount)
                	{
            	        logger.debug("this is a the same user.");
            	        logger.debug("max nom count reached...");
                	    logger.debug("fwd'ing to: " + EXIT_PAGE);
                	    return (mapping.findForward(EXIT_PAGE));
                	}
        	    }
    	}
    	
    	int newNominationCount=nominationCount+1;
    	logger.debug("newNominationCount: " + newNominationCount);
    	logger.debug("creating attemps with mapGeneralCheckedOptionsContent " + mapGeneralCheckedOptionsContent);
    	voteService.removeAttemptsForUser(voteQueUsr.getUid());
    	logger.debug("nominations deleted for user: " + voteQueUsr.getUid());
    	LearningUtil.createAttempt(request, voteQueUsr, mapGeneralCheckedOptionsContent, userEntry, newNominationCount, false);

    	logger.debug("using nominationCount: " + newNominationCount);
    	
    	if ((mapGeneralCheckedOptionsContent.size() == 0  && (userEntryAvailable == true)))
    	{
    		logger.debug("mapGeneralCheckedOptionsContent size is 0");
    		Map mapLeanerCheckedOptionsContent= new TreeMap(new VoteComparator());
    		mapLeanerCheckedOptionsContent.put("101", userEntry);

			logger.debug("after mapsize check  mapLeanerCheckedOptionsContent " + mapLeanerCheckedOptionsContent);
			logger.debug("using nominationCount: " + nominationCount);
			if (userEntry.length() > 0)
			{
			    logger.debug("creating entry for: " + userEntry);
			    LearningUtil.createAttempt(request, voteQueUsr, mapLeanerCheckedOptionsContent, userEntry, newNominationCount, true);    
			}
    	}
    	if ((mapGeneralCheckedOptionsContent.size() > 0) && (userEntryAvailable == true))
    	{
    		logger.debug("mapGeneralCheckedOptionsContent size is > 0" + userEntry);
    		Map mapLeanerCheckedOptionsContent= new TreeMap(new VoteComparator());
    		mapLeanerCheckedOptionsContent.put("102", userEntry);

			logger.debug("after mapsize check  mapLeanerCheckedOptionsContent " + mapLeanerCheckedOptionsContent);
			logger.debug("using nominationCount: " + nominationCount);
			if (userEntry.length() > 0)
			{
			    logger.debug("creating entry for: " + userEntry);
			    LearningUtil.createAttempt(request, voteQueUsr, mapLeanerCheckedOptionsContent, userEntry, newNominationCount, false);    
			}
    	}

    	
    	logger.debug("created user attempt in the db");
    	voteLearningForm.resetCommands();

    	mapGeneralCheckedOptionsContent=(Map) request.getSession().getAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT);
    	logger.debug("final mapGeneralCheckedOptionsContent: " + mapGeneralCheckedOptionsContent);
    	
    	voteLearningForm.setNominationsSubmited(new Boolean(true).toString());
    	logger.debug("fwd ing to: " + ALL_NOMINATIONS);
    	return (mapping.findForward(ALL_NOMINATIONS));
    }

    public ActionForward redoQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
   {
        logger.debug("dispatching redoQuestions...");
    	VoteUtils.cleanUpUserExceptions(request);
    	VoteLearningForm voteLearningForm = (VoteLearningForm) form;
    	voteLearningForm.setRevisitingPageActive(new Boolean(false).toString());
    	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
	 	IVoteService voteService =VoteUtils.getToolService(request);
	 	
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("toolContentId:" + toolContentId);
    	
    	VoteContent voteContent=voteService.retrieveVote(toolContentId);
    	logger.debug("voteContent:" + voteContent);

    	Map mapQuestionsContent= new TreeMap(new VoteComparator());
    	mapQuestionsContent=LearningUtil.buildQuestionContentMap(request,voteContent);
	    logger.debug("mapQuestionsContent: " + mapQuestionsContent);
		
		request.getSession().setAttribute(MAP_QUESTION_CONTENT_LEARNER, mapQuestionsContent);
		logger.debug("MAP_QUESTION_CONTENT_LEARNER: " +  request.getSession().getAttribute(MAP_QUESTION_CONTENT_LEARNER));
		logger.debug("voteContent has : " + mapQuestionsContent.size() + " entries.");
		
		Map mapGeneralCheckedOptionsContent= new TreeMap(new VoteComparator());
	    request.getSession().setAttribute(MAP_GENERAL_CHECKED_OPTIONS_CONTENT, mapGeneralCheckedOptionsContent);
	    
	    voteLearningForm.setUserEntry("");
	    
	    String previewOnly=(String)request.getSession().getAttribute(PREVIEW_ONLY);
	    logger.debug("previewOnly : " + previewOnly);
	    if (previewOnly != null)
	    {
		    if (previewOnly.equals("true"))
		    {
		        logger.debug("request is for previewOnly : " + previewOnly);
		        return (mapping.findForward(PREVIEW));
		    }
	    }
	    return (mapping.findForward(LOAD_LEARNER));
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
    	voteLearningForm.setRevisitingPageActive(new Boolean(false).toString());
    	voteLearningForm.setNominationsSubmited(new Boolean(false).toString());
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
    