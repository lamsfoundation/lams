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
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;

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
    
    
    public ActionForward addNewOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
		logger.debug("dispathcing addNewOption");
		
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
	    
	    mapOptionsContent=(Map)request.getSession().getAttribute(MAP_OPTIONS_CONTENT);
	    logger.debug("final mapOptionsContent: " + mapOptionsContent);
	    return (mapping.findForward(LOAD_QUESTIONS));
    }


    public ActionForward removeOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    {
		logger.debug("doing removeOption ");
		request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
		
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

	    
		AuthoringUtil authoringUtil= new AuthoringUtil();
	    VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	    Map mapOptionsContent=(Map)request.getSession().getAttribute(MAP_OPTIONS_CONTENT);
	    logger.debug("mapOptionsContent: " + mapOptionsContent);
	    
	    //request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));  
	    authoringUtil.reconstructOptionContentMapForRemove(mapOptionsContent, request, voteAuthoringForm);
	    
	    mapOptionsContent=(Map)request.getSession().getAttribute(MAP_OPTIONS_CONTENT);
	    logger.debug("final mapOptionsContent: " + mapOptionsContent);
	    
	    return (mapping.findForward(LOAD_QUESTIONS));
    }

    
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
	
		logger.debug("starting submitAllContent :" +form);
		request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
	    VoteAuthoringForm voteAuthoringForm = (VoteAuthoringForm) form;
	    logger.debug("voteAuthoringForm :" +voteAuthoringForm);
	    
	    IVoteService voteService = (IVoteService)request.getSession().getAttribute(TOOL_SERVICE);
	    if (voteService == null)        
	    	voteService = VoteServiceProxy.getVoteService(getServlet().getServletContext());
	    logger.debug("voteService :" +voteService);
	    
	           	
	    AuthoringUtil authoringUtil= new AuthoringUtil();
	    Map mapOptionsContent=(Map)request.getSession().getAttribute(MAP_OPTIONS_CONTENT);
	    logger.debug("mapOptionsContent :" +mapOptionsContent);
	    
	    if (mapOptionsContent == null)
	        mapOptionsContent= new TreeMap(new VoteComparator());
	    logger.debug("mapOptionsContent :" +mapOptionsContent);
	    
	    ActionMessages errors= new ActionMessages();
	    /* full form validation should be performed only in standard authoring mode, but not in monitoring EditActivity */
	    errors=validateSubmit(request, errors, voteAuthoringForm);
	
	    if (errors.size() > 0)  
	    {
	        logger.debug("returning back to from to fix errors:");
	        request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));
	        return (mapping.findForward(LOAD_QUESTIONS));
	    }
	    
	    List attachmentList = (List) request.getSession().getAttribute(ATTACHMENT_LIST);
	    List deletedAttachmentList = (List) request.getSession().getAttribute(DELETED_ATTACHMENT_LIST);
	
	    authoringUtil.reconstructOptionsContentMapForSubmit(mapOptionsContent, request);
	    logger.debug("before saveOrUpdateVoteContent.");
	    
	    boolean verifyDuplicatesOptionsMap=AuthoringUtil.verifyDuplicatesOptionsMap(mapOptionsContent);
	 	logger.debug("verifyDuplicatesOptionsMap: " + verifyDuplicatesOptionsMap);
	 	request.getSession().removeAttribute(USER_EXCEPTION_OPTIONS_DUPLICATE);
	 	if (verifyDuplicatesOptionsMap == false)
			{
			errors= new ActionMessages();
			errors.add(Globals.ERROR_KEY,new ActionMessage("error.options.duplicate"));
			request.getSession().setAttribute(USER_EXCEPTION_OPTIONS_DUPLICATE, new Boolean(true).toString());
			logger.debug("add error.options.duplicate to ActionMessages");
			saveErrors(request,errors);
			voteAuthoringForm.resetUserAction();
	        return (mapping.findForward(LOAD_QUESTIONS));
			}
	    
	 	logger.debug("submitting mapOptionsContent:" + mapOptionsContent);
	    
	    /*to remove deleted entries in the questions table based on mapQuestionContent */
	    authoringUtil.removeRedundantOptions(mapOptionsContent, voteService, voteAuthoringForm, request);
	    logger.debug("end of removing unused entries... ");
	    
	    VoteContent voteContent=authoringUtil.saveOrUpdateVoteContent(mapOptionsContent, voteService, voteAuthoringForm, request);
	    logger.debug("voteContent: " + voteContent);
		
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

	    
	    //List attacments=saveAttachments(voteContent, attachmentList, deletedAttachmentList, mapping, request);
	    //logger.debug("attacments: " + attacments);
	
	
	    errors.clear();
	    errors.add(Globals.ERROR_KEY, new ActionMessage("sbmt.successful"));
	    request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(1));
	    logger.debug("setting SUBMIT_SUCCESS to 1.");
	    
	    Long strToolContentId=(Long)request.getSession().getAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID);
	    logger.debug("strToolContentId: " + strToolContentId);
	    VoteUtils.setDefineLater(request, false);
	    
	    saveErrors(request,errors);
	    
	    VoteUtils.setDefineLater(request, false);
	    logger.debug("define later set to false");
	    
	    voteAuthoringForm.resetUserAction();
	    return mapping.findForward(LOAD_QUESTIONS);
    }

    
    
    protected ActionMessages validateSubmit(HttpServletRequest request, ActionMessages errors, VoteAuthoringForm voteAuthoringForm)
    {
    	request.getSession().setAttribute(SUBMIT_SUCCESS, new Integer(0));
        String title = voteAuthoringForm.getTitle();
        logger.debug("title: " + title);

        String instructions = voteAuthoringForm.getInstructions();
        logger.debug("instructions: " + instructions);
        
        if ((title == null) || (title.trim().length() == 0) || title.equalsIgnoreCase(RICHTEXT_BLANK))
        {
            errors.add(Globals.ERROR_KEY,new ActionMessage("error.title"));
            logger.debug("add title to ActionMessages");
        }

        if ((instructions == null) || (instructions.trim().length() == 0) || instructions.equalsIgnoreCase(RICHTEXT_BLANK))
        {
            errors.add(Globals.ERROR_KEY, new ActionMessage("error.instructions"));
            logger.debug("add instructions to ActionMessages: ");
        }

        /*
         * enforce that the first (default) question entry is not empty
         */
        String defaultOptionEntry =request.getParameter("optionContent0");
        if ((defaultOptionEntry == null) || (defaultOptionEntry.length() == 0))
        {
            errors.add(Globals.ERROR_KEY, new ActionMessage("error.defaultoption.empt"));
            logger.debug("add error.defaultoption.empt to ActionMessages: ");
        }
        
        Boolean renderMonitoringEditActivity=(Boolean)request.getSession().getAttribute(RENDER_MONITORING_EDITACTIVITY);
        if ((renderMonitoringEditActivity != null) && (!renderMonitoringEditActivity.booleanValue()))
        {

            if ((voteAuthoringForm.getReportTitle() == null) || (voteAuthoringForm.getReportTitle().length() == 0))
            {
                errors.add(Globals.ERROR_KEY, new ActionMessage("error.reportTitle"));
                logger.debug("add reportTitle to ActionMessages: ");
            }
            
            if ((voteAuthoringForm.getMonitoringReportTitle() == null) || (voteAuthoringForm.getMonitoringReportTitle().length() == 0))
            {
                errors.add(Globals.ERROR_KEY, new ActionMessage("error.monitorReportTitle"));
                logger.debug("add monitorReportTitle to ActionMessages: ");
            }
        }
        
        saveErrors(request,errors);
        return errors;
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
    