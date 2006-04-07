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
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.tool.vote.service.VoteServiceProxy;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * @author Ozgur Demirtas
 *
 * enables  the learner and teacher to export the contents of the voting tool.
 */

public class VoteExportAction extends LamsDispatchAction implements VoteAppConstants{
    
    static Logger logger = Logger.getLogger(VoteExportAction.class.getName());

    public ActionForward unspecified(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)
    {
    	VoteUtils.cleanUpUserExceptions(request);
        return mapping.findForward(EXPORT_PORTFOLIO);
    }
    

    /**
     * provides export portfolio functionality for learner
     * learner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward learner(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
    	VoteUtils.cleanUpUserExceptions(request);
    	logger.debug("dispatching export portfolio for learner...");
    	VoteUtils.cleanUpSessionAbsolute(request);
    	
    	IVoteService mcService = VoteUtils.getToolService(request);
		logger.debug("retrieving mcService from session: " + mcService);
		if (mcService == null)
		{
			mcService = VoteServiceProxy.getMcService(getServlet().getServletContext());
		    logger.debug("retrieving mcService from proxy: " + mcService);
		    request.getSession().setAttribute(TOOL_SERVICE, mcService);		
		}

		VoteExportForm exportForm = (VoteExportForm)form;
		
       /* required parameters are toolSessionId and userId */
		boolean parametersCorrect=validateLearnerExportParameters(form, request);
		logger.debug("learner parametersCorrect: " + parametersCorrect);
		if (parametersCorrect == false)
		{
			logger.debug("learner parameters are not properly passed: " + parametersCorrect);
			return (mapping.findForward(ERROR_LIST));
		}
		
		Long toolSessionId =(Long)request.getSession().getAttribute(TOOL_SESSION_ID);
		logger.debug("toolSessionId: " + toolSessionId);
		
		VoteSession mcSession=mcService.findMcSessionById(toolSessionId);
		logger.debug("mcSession: " + mcSession);
		if (mcSession == null)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
        	persistError(request, "error.toolSession.doesNoExist");
        	request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
        	return (mapping.findForward(ERROR_LIST));
		}
		
		Long toolSessionUid= mcSession.getUid();
		logger.debug("mcSession is identified by : " + toolSessionUid);
		
		Long exportUserId =(Long)request.getSession().getAttribute(EXPORT_USER_ID);
		logger.debug("exportUserId : " + exportUserId);
		
		VoteQueUsr mcQueUsr=mcService.getMcUserBySession(exportUserId, toolSessionUid);
		logger.debug("existing tool user mcQueUsr : " + mcQueUsr);
		if (mcQueUsr == null)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			request.getSession().setAttribute(USER_EXCEPTION_LEARNER_REQUIRED, new Boolean(true).toString());
        	persistError(request, "error.learner.user.doesNoExist");
        	return (mapping.findForward(ERROR_LIST));
		}
		
		logger.debug("getting mcContent based on toolSessionId : " + toolSessionId);
		VoteContent mcContent=mcService.retrieveMcBySessionId(toolSessionId); 
		logger.debug("mcContent : " + mcContent);
		if (mcContent == null)
		{
        	VoteUtils.cleanUpSessionAbsolute(request);
			persistError(request, "error.content.doesNotExist");
			request.getSession().setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true).toString());
        	return (mapping.findForward(ERROR_LIST));
		}
               
        exportForm.populateForm(mcContent);
        logger.debug("ppulated from with mcContent : " + mcContent.getTitle() + " and " + mcContent.getContent());
        return mapping.findForward(EXPORT_PORTFOLIO);
    }
    
    
    /**
     * provides export portfolio functionality for teacher 
     * teacher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward teacher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    {
    	VoteUtils.cleanUpUserExceptions(request);
        //given the toolcontentId as a parameter
    	logger.debug("dispatching export portfolio for teacher...");
    	VoteUtils.cleanUpSessionAbsolute(request);
    	
    	IVoteService mcService = VoteUtils.getToolService(request);
		logger.debug("retrieving mcService from session: " + mcService);
		if (mcService == null)
		{
			mcService = VoteServiceProxy.getMcService(getServlet().getServletContext());
		    logger.debug("retrieving mcService from proxy: " + mcService);
		    request.getSession().setAttribute(TOOL_SERVICE, mcService);		
		}

		VoteExportForm exportForm = (VoteExportForm)form;

		/* required parameters are toolSessionId and userId */
		boolean parametersCorrect=validateTeacherExportParameters(form, request);
		logger.debug("learner parametersCorrect: " + parametersCorrect);
		if (parametersCorrect == false)
		{
			logger.debug("teacher parameters are not properly passed: " + parametersCorrect);
			return (mapping.findForward(ERROR_LIST));
		}

		Long toolContentId =(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
		logger.debug("toolContentId: " + toolContentId);
		VoteContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("mcContent:" + mcContent);
		
		if (mcContent == null)
		{
			VoteUtils.cleanUpSessionAbsolute(request);
			persistError(request, "error.content.doesNotExist");
			request.getSession().setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true).toString());
        	return (mapping.findForward(ERROR_LIST));
		}
        
        exportForm.populateForm(mcContent);
  
   		return mapping.findForward(EXPORT_PORTFOLIO);
    }

    
    
    /**
     * validates learner export portfolio parameters
     * validateLearnerExportParameters(ActionForm form, HttpServletRequest request)
     * 
     * @param form
     * @param request
     * @return boolean
     */
    protected boolean validateLearnerExportParameters(ActionForm form, HttpServletRequest request)
    {
    	VoteUtils.cleanUpUserExceptions(request);
        String strToolSessionId=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
        
        if ((strToolSessionId == null) || (strToolSessionId.length() == 0)) 
        {
        	VoteUtils.cleanUpSessionAbsolute(request);
        	request.getSession().setAttribute(USER_EXCEPTION_TOOLSESSIONID_REQUIRED, new Boolean(true).toString());
        	persistError(request, "error.toolSessionId.required");
        	return false;
        }
        else
        {
        	try
    		{
        		long testToolSessionId=new Long(strToolSessionId).longValue();
        		request.getSession().setAttribute(TOOL_SESSION_ID,new Long(strToolSessionId));	
    		}
        	catch(NumberFormatException e)
    		{
        		VoteUtils.cleanUpSessionAbsolute(request);
        		request.getSession().setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true).toString());
        		persistError(request, "error.sessionId.numberFormatException");
        		logger.debug("add error.sessionId.numberFormatException to ActionMessages.");
        		return false;
    		}
        }
        
        /*USER_ID should be added to AttributeNames*/
        String userId=request.getParameter(USER_ID);
    	logger.debug("userId: " + userId);
    	if ((userId == null) || (userId.length() == 0)) 
    	{
    		VoteUtils.cleanUpSessionAbsolute(request);
    		persistError(request, "error.learner.userId.required");
    		request.getSession().setAttribute(USER_EXCEPTION_USER_DOESNOTEXIST, new Boolean(true).toString());
        	return false;
    	}
    	request.getSession().setAttribute(EXPORT_USER_ID,new Long(userId));
    	
    	return true;
    }
    
    
    /**
     * validates teacher export portfolio parameters 
     * validateTeacherExportParameters(ActionForm form, HttpServletRequest request)
     * 
     * @param form
     * @param request
     * @return boolean
     */
    protected boolean validateTeacherExportParameters(ActionForm form, HttpServletRequest request)
    {
    	VoteUtils.cleanUpUserExceptions(request);
    	String strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
    	logger.debug("strToolContentId: " + strToolContentId);
    	 
	    if ((strToolContentId == null) || (strToolContentId.length() == 0)) 
	    {
	    	VoteUtils.cleanUpSessionAbsolute(request);
	    	persistError(request, "error.contentId.required");
	    	request.getSession().setAttribute(USER_EXCEPTION_CONTENTID_REQUIRED, new Boolean(true).toString());
	    	return false;
	    }
	    else
	    {
	    	try
			{
	    		long toolContentId=new Long(strToolContentId).longValue();
		    	logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
		    	request.getSession().setAttribute(TOOL_CONTENT_ID,new Long(toolContentId));	
			}
	    	catch(NumberFormatException e)
			{
	    		VoteUtils.cleanUpSessionAbsolute(request);
	    		request.getSession().setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true).toString());
	    		persistError(request, "error.numberFormatException");
	    		logger.debug("add error.contentId.numberFormatException to ActionMessages.");
	    		return false;
			}
	    }
	    return true;
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
