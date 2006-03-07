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
package org.lamsfoundation.lams.tool.qa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * @author Ozgur Demirtas
 *
 * enables  the learner and teacher to export the contents of the mcq tool.
 */

public class QaExportAction extends LamsDispatchAction implements QaAppConstants{
    
    static Logger logger = Logger.getLogger(QaExportAction.class.getName());

    public ActionForward unspecified(
    		ActionMapping mapping,
    		ActionForm form,
    		HttpServletRequest request,
    		HttpServletResponse response)
    {
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
    	logger.debug("dispatching export portfolio for learner...");
    	QaUtils.cleanUpSessionAbsolute(request);
    	
		IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("qaService: " + qaService);
		if (qaService == null)
		{
			logger.debug("will retrieve qaService");
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
			logger.debug("retrieving qaService from session: " + qaService);
		}


		QaExportForm exportForm = (QaExportForm)form;
		
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
		
		QaSession qaSession=qaService.retrieveQaSessionOrNullById(toolSessionId.longValue());
	    logger.debug("retrieving qaSession: " + qaSession);
		
	    if (qaSession == null)
		{
    		QaUtils.cleanUpSessionAbsolute(request);
	    	request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());        		
	    	persistError(request, "error.toolSession.doesNoExist");
    		return (mapping.findForward(ERROR_LIST));
		}
		
		Long toolSessionUid= qaSession.getUid();
		logger.debug("qaSession is identified by : " + toolSessionUid);
		
		Long exportUserId =(Long)request.getSession().getAttribute(EXPORT_USER_ID);
		logger.debug("exportUserId : " + exportUserId);
		
		QaQueUsr qaQueUsr=qaService.getQaUserBySession(exportUserId, toolSessionUid);
		logger.debug("existing tool user qaQueUsr : " + qaQueUsr);
		if (qaQueUsr == null)
		{
			QaUtils.cleanUpSessionAbsolute(request);
			request.getSession().setAttribute(USER_EXCEPTION_USER_DOESNOTEXIST, new Boolean(true).toString());
        	persistError(request, "error.learningUser.notAvailable");
        	return (mapping.findForward(ERROR_LIST));
		}
		
		logger.debug("getting qaContent based on toolSessionId : " + toolSessionId);
		QaContent qaContent=qaService.retrieveQaBySession(toolSessionId.longValue()); 
		logger.debug("qaContent : " + qaContent);
		if (qaContent == null)
		{
			QaUtils.cleanUpSessionAbsolute(request);
			request.getSession().setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true).toString());
			persistError(request, "error.content.doesNotExist");
        	return (mapping.findForward(ERROR_LIST));
		}
               
        exportForm.populateForm(qaContent);
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
        //given the toolcontentId as a parameter
    	logger.debug("dispatching export portfolio for teacher...");
    	QaUtils.cleanUpSessionAbsolute(request);
    	
    	IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("qaService: " + qaService);
		if (qaService == null)
		{
			logger.debug("will retrieve qaService");
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
			logger.debug("retrieving qaService from session: " + qaService);
		}
		QaExportForm exportForm = (QaExportForm)form;

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
		
		QaContent qaContent=qaService.loadQa(toolContentId.longValue());
		logger.debug("existing qaContent:" + qaContent);
		if (qaContent == null)
		{
			QaUtils.cleanUpSessionAbsolute(request);
			request.getSession().setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true).toString());
			persistError(request, "error.content.doesNotExist");
        	return (mapping.findForward(ERROR_LIST));
		}
        
        exportForm.populateForm(qaContent);
  
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
        String strToolSessionId=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
        
        if ((strToolSessionId == null) || (strToolSessionId.length() == 0)) 
        {
        	QaUtils.cleanUpSessionAbsolute(request);
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
        		QaUtils.cleanUpSessionAbsolute(request);
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
    		QaUtils.cleanUpSessionAbsolute(request);
	    	request.getSession().setAttribute(USER_EXCEPTION_USER_DOESNOTEXIST, new Boolean(true).toString());    		
    		persistError(request, "error.learner.userId.required");
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
    	String strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
    	logger.debug("strToolContentId: " + strToolContentId);
    	 
	    if ((strToolContentId == null) || (strToolContentId.length() == 0)) 
	    {
    		QaUtils.cleanUpSessionAbsolute(request);
	    	request.getSession().setAttribute(USER_EXCEPTION_CONTENTID_REQUIRED, new Boolean(true).toString());    		
	    	persistError(request, "error.contentId.required");
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
	    		QaUtils.cleanUpSessionAbsolute(request);
	    		request.getSession().setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true).toString());
	    		persistError(request, "error.contentId.numberFormatException");
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
