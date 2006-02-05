	/*
 * Created on Jul 25, 2005
 */
package org.lamsfoundation.lams.tool.mc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * @author Ozgur Demirtas
 *
 * enables  the learner and teacher to export the contents of the mcq tool.
 */

public class McExportAction extends LamsDispatchAction implements McAppConstants{
    
    static Logger logger = Logger.getLogger(McExportAction.class.getName());

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
    	McUtils.cleanUpSessionAbsolute(request);
    	
    	IMcService mcService = McUtils.getToolService(request);
		logger.debug("retrieving mcService from session: " + mcService);
		if (mcService == null)
		{
			mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		    logger.debug("retrieving mcService from proxy: " + mcService);
		    request.getSession().setAttribute(TOOL_SERVICE, mcService);		
		}

		McExportForm exportForm = (McExportForm)form;
		
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
		
		McSession mcSession=mcService.findMcSessionById(toolSessionId);
		logger.debug("mcSession: " + mcSession);
		if (mcSession == null)
		{
        	persistError(request, "error.toolSession.doesNoExist");
        	McUtils.cleanUpSessionAbsolute(request);
        	return (mapping.findForward(ERROR_LIST));
		}
		
		Long toolSessionUid= mcSession.getUid();
		logger.debug("mcSession is identified by : " + toolSessionUid);
		
		Long exportUserId =(Long)request.getSession().getAttribute(EXPORT_USER_ID);
		logger.debug("exportUserId : " + exportUserId);
		
		McQueUsr mcQueUsr=mcService.getMcUserBySession(exportUserId, toolSessionUid);
		logger.debug("existing tool user mcQueUsr : " + mcQueUsr);
		if (mcQueUsr == null)
		{
        	persistError(request, "error.learner.user.doesNoExist");
        	McUtils.cleanUpSessionAbsolute(request);
        	return (mapping.findForward(ERROR_LIST));
		}
		
		logger.debug("getting mcContent based on toolSessionId : " + toolSessionId);
		McContent mcContent=mcService.retrieveMcBySessionId(toolSessionId); 
		logger.debug("mcContent : " + mcContent);
		if (mcContent == null)
		{
			persistError(request, "error.content.doesNotExist");
        	McUtils.cleanUpSessionAbsolute(request);
        	return (mapping.findForward(ERROR_LIST));
		}
               
        exportForm.populateForm(mcContent);
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
    	McUtils.cleanUpSessionAbsolute(request);
    	
    	IMcService mcService = McUtils.getToolService(request);
		logger.debug("retrieving mcService from session: " + mcService);
		if (mcService == null)
		{
			mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		    logger.debug("retrieving mcService from proxy: " + mcService);
		    request.getSession().setAttribute(TOOL_SERVICE, mcService);		
		}

		McExportForm exportForm = (McExportForm)form;

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
		McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("mcContent:" + mcContent);
		
		if (mcContent == null)
		{
			persistError(request, "error.content.doesNotExist");
        	McUtils.cleanUpSessionAbsolute(request);
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
        String strToolSessionId=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
        
        if ((strToolSessionId == null) || (strToolSessionId.length() == 0)) 
        {
        	persistError(request, "error.toolSessionId.required");
        	McUtils.cleanUpSessionAbsolute(request);
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
        		persistError(request, "error.sessionId.numberFormatException");
        		logger.debug("add error.sessionId.numberFormatException to ActionMessages.");
        		McUtils.cleanUpSessionAbsolute(request);
            	return false;
    		}
        }
        
        /*USER_ID should be added to AttributeNames*/
        String userId=request.getParameter(USER_ID);
    	logger.debug("userId: " + userId);
    	if ((userId == null) || (userId.length() == 0)) 
    	{
    		persistError(request, "error.learner.userId.required");
    		McUtils.cleanUpSessionAbsolute(request);
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
	    	persistError(request, "error.contentId.required");
	    	McUtils.cleanUpSessionAbsolute(request);
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
	    		persistError(request, "error.contentId.numberFormatException");
	    		logger.debug("add error.contentId.numberFormatException to ActionMessages.");
	    		McUtils.cleanUpSessionAbsolute(request);
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
