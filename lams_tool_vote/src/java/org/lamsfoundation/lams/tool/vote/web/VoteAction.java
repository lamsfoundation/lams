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
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
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
 

*/
public class VoteAction extends LamsDispatchAction implements VoteAppConstants
{
	/*
	 * when to reset define later and synchin monitor etc..
	 *
	 * make sure the tool gets called on:
	 * setAsForceComplete(Long userId) throws VoteApplicationException 
	 */
	static Logger logger = Logger.getLogger(VoteAction.class.getName());
	
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
	 	VoteAuthoringForm mcAuthoringForm = (VoteAuthoringForm) form;
	 	IVoteService mcService =VoteUtils.getToolService(request);
	 	AuthoringUtil.readData(request, mcAuthoringForm);	 	
	 	mcAuthoringForm.resetUserAction();
	 	
	 	
	 	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
	 	return null;
    }
    
    
    public ActionForward addNewQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
		logger.debug("dispathcing addNewQuestion");
		
		request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    Map mapOptionsContent=(Map)request.getSession().getAttribute(MAP_OPTIONS_CONTENT);
	
		String richTextTitle = request.getParameter("title");
	    String richTextInstructions = request.getParameter("instructions");
	    String richTextPosting = request.getParameter("posting");
	    
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    logger.debug("richTextPosting: " + richTextPosting);
	    
	    if (richTextTitle != null)
	    {
			request.getSession().setAttribute(ACTIVITY_TITLE, richTextTitle);
	    }
	
	    if (richTextInstructions != null)
	    {
			request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, richTextInstructions);
	    }

	    if (richTextPosting != null)
	    {
			request.getSession().setAttribute(POSTING, richTextPosting);
	    }

	    //request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));  
	    authoringUtil.reconstructOptionContentMapForAdd(mapOptionsContent, request);
	    
	    logger.debug("richTextInstructions: " + request.getSession().getAttribute(ACTIVITY_INSTRUCTIONS));
	    return (mapping.findForward(LOAD_QUESTIONS));
    }


    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
		logger.debug("doing removeQuestion ");
		request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
		
		String richTextTitle = request.getParameter("title");
	    String richTextInstructions = request.getParameter("instructions");
	    logger.debug("richTextTitle: " + richTextTitle);
	    logger.debug("richTextInstructions: " + richTextInstructions);
	    String richTextPosting = request.getParameter("posting");
	    
	    if (richTextTitle != null)
	    {
			request.getSession().setAttribute(ACTIVITY_TITLE, richTextTitle);
	    }
	
	    if (richTextInstructions != null)
	    {
			request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, richTextInstructions);
	    }
	    
	    if (richTextPosting != null)
	    {
			request.getSession().setAttribute(POSTING, richTextPosting);
	    }

	    
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	    Map mapOptionsContent=(Map)request.getSession().getAttribute(MAP_OPTIONS_CONTENT);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    
	    //request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));  
	    authoringUtil.reconstructOptionContentMapForRemove(mapOptionsContent, request, voteAuthoringForm);
	    
	    return (mapping.findForward(LOAD_QUESTIONS));
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
    