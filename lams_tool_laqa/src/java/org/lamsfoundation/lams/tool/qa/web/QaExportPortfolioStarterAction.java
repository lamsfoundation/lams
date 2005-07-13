/*
 * Created on 8/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Ozgur Demirtas
 * 
 * <lams base path>/<tool's export portfolio url>&mode=learner&toolSessionId=231&userId=<learners user id>
*/


package org.lamsfoundation.lams.tool.qa.web;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaStringComparator;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;

public class QaExportPortfolioStarterAction extends Action implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaExportPortfolioStarterAction.class.getName());
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, QaApplicationException, ToolException 
	{
		/**
		 * retrive the service
		 */
    	IQaService qaService=null;
   		qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
   	    logger.debug("retrieved qaService : " + qaService);
   	    request.getSession().setAttribute(TOOL_SERVICE, qaService);	

    	    	
    	/**
	     * persist time zone information to session scope. 
	     */
	    QaUtils.persistTimeZone(request);
    	
	    /**
	     * mark the http session as an authoring activity 
	     */
	    //request.getSession().setAttribute(TARGET_MODE,TARGET_MODE_EXPORT_PORTFOLIO);
	    
	    /**
	     * obtain and setup the current user's data 
	     */

	    String userId="";
		userId=request.getParameter(USER_ID);
		logger.debug("userId: " + userId);
	    try
		{
	    	User user=QaUtils.createStandardUser(new Integer(userId));
	    	request.getSession().setAttribute(TOOL_USER, user);
		}
	    catch(NumberFormatException e)
		{
	    	persistError(request,"error.userId.notNumeric");
			request.setAttribute(USER_EXCEPTION_USERID_NOTNUMERIC, new Boolean(true));
			logger.debug("forwarding to: " + PORTFOLIO_REPORT);
			return (mapping.findForward(PORTFOLIO_REPORT));
		}
		
	    
	    if ((userId == null) || (userId.length()==0))
		{
	    	logger.debug("error: The tool expects userId");
	    	persistError(request,"error.userId.required");
	    	request.setAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE, new Boolean(true));
			logger.debug("forwarding to: " + PORTFOLIO_REPORT);
			return (mapping.findForward(PORTFOLIO_REPORT));
		}
		logger.debug("TOOL_USER is:" + request.getSession().getAttribute(TOOL_USER));
		
		String mode="";
		mode=request.getParameter(MODE);
		logger.debug("mode: " + mode);
		boolean useToolSessionId=false;
		if ((mode != null) && mode.equalsIgnoreCase(LEARNER))
		{
			logger.debug("mode is:" + mode + " use toolSessionId");
			useToolSessionId=true;
		}
		else if ((mode != null) && mode.equalsIgnoreCase(TEACHER))
		{
			logger.debug("mode is:" + mode + " use toolContentId");
			useToolSessionId=false;
		}
		else
		{
			logger.debug("Warning mode is: unknown");
			persistError(request,"error.mode.required");
			request.setAttribute(USER_EXCEPTION_MODE_REQUIRED, new Boolean(true));
			logger.debug("forwarding to: " + PORTFOLIO_REPORT);
			return (mapping.findForward(PORTFOLIO_REPORT));
		}
		request.getSession().setAttribute(MODE,mode);
		
		String strToolSessionId="";
		Long toolSessionId=null;
		if (useToolSessionId == true)
		{
			logger.debug("reading TOOL_SESSION_ID");
			strToolSessionId=request.getParameter(TOOL_SESSION_ID);
			logger.debug("toolSessionId :" + strToolSessionId);
		
		    try
			{
			    if ((strToolSessionId != null) && (strToolSessionId.length() > 0)) 
			    {
			    	toolSessionId=new Long(strToolSessionId);
			    	request.getSession().setAttribute(TOOL_SESSION_ID, toolSessionId);
			    }
			    else
			    {
				    persistError(request,"error.toolSessionId.required");
					request.setAttribute(USER_EXCEPTION_TOOLSESSIONID_REQUIRED, new Boolean(true));
					logger.debug("forwarding to: " + PORTFOLIO_REPORT);
					return (mapping.findForward(PORTFOLIO_REPORT));
			    }
		    }
		    catch(NumberFormatException e)
			{
		    	persistError(request,"error.sessionId.numberFormatException");
				request.setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true));
				logger.debug("forwarding to: " + PORTFOLIO_REPORT);
				return (mapping.findForward(PORTFOLIO_REPORT));
			}
			logger.debug("final toolSessionId :" + toolSessionId);
		}
		
		String strToolContentId="";
		Long toolContentId=null;
		
		if (useToolSessionId == false)
		{
			logger.debug("reading TOOL_CONTENT_ID");
			strToolContentId=request.getParameter(TOOL_CONTENT_ID);
	    	logger.debug("TOOL_CONTENT_ID: " + strToolContentId);
	    	
		    try
			{
			    if ((strToolContentId != null) && (strToolContentId.length() > 0)) 
			    {
			    	if (!QaUtils.existsContent(new Long(strToolContentId).longValue(), request))
			    	{
			    		persistError(request,"error.content.doesNotExist");
			    		request.setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true));
						logger.debug("forwarding to: " + PORTFOLIO_REPORT);
						return (mapping.findForward(PORTFOLIO_REPORT));
			    	}
			    	request.getSession().setAttribute(TOOL_CONTENT_ID, new Long(strToolContentId));	
			    }
			    else
			    {
			    	persistError(request,"error.contentId.required");
					request.setAttribute(USER_EXCEPTION_CONTENTID_REQUIRED, new Boolean(true));
					logger.debug("forwarding to: " + PORTFOLIO_REPORT);
					return (mapping.findForward(PORTFOLIO_REPORT));
			    }
		    }
		    catch(NumberFormatException e)
			{
		    	persistError(request,"error.contentId.numberFormatException");
				request.setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true));
				logger.debug("forwarding to: " + PORTFOLIO_REPORT);
				return (mapping.findForward(PORTFOLIO_REPORT));
			}
		}
	
		/**
			at this point we have session attributes TOOL_CONTENT_ID, TOOL_SESSION_ID and TOOL_USER AND MODE ready to use  
		*/
	
		mode=(String)request.getSession().getAttribute(MODE);
		logger.debug("mode is: " + mode);
			
		toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
		logger.debug("toolContentId: " + toolContentId);
		
		toolSessionId=(Long)request.getSession().getAttribute(TOOL_SESSION_ID);
		logger.debug("toolSessionId: " + toolSessionId);

		int toolSessionCount=0;				
		ArrayList toolSessions = new ArrayList();
		if (mode.equalsIgnoreCase(LEARNER))
		{
			request.getSession().setAttribute(TARGET_MODE, TARGET_MODE_LEARNING);
			logger.debug("generate portfolio for mode: " + mode);
			ArrayList singleToolSession= new ArrayList();
			singleToolSession.add(toolSessionId);
			logger.debug("singleToolSession: " + singleToolSession);
			toolSessions=singleToolSession;
			logger.debug("toolSessions: " + toolSessions);
			toolSessionCount=1;
		}
		else
		{
			request.getSession().setAttribute(TARGET_MODE, TARGET_MODE_MONITORING);
			logger.debug("generate portfolio for mode: " + mode);
			/** we already know that this content exists in the db */
			QaContent qa=qaService.loadQa(toolContentId.longValue());
			logger.debug("qa: " + qa);
			toolSessions= (ArrayList)qaService.getToolSessionsForContent(qa);
			logger.debug("toolSessions: " + toolSessions);
			toolSessionCount=toolSessions.size();
		}
		logger.debug("final toolSessions: " + toolSessions);
		logger.debug("toolSessionCount: " + toolSessionCount);
		
		if (toolSessionCount == 0)
		{
	    	persistError(request,"error.content.noToolSessions");
			request.setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true));
			logger.debug("forwarding to: " + PORTFOLIO_REPORT);
			return (mapping.findForward(PORTFOLIO_REPORT));
		}
		
		Map mapToolSessions= new TreeMap(new QaStringComparator());
		request.getSession().setAttribute(MAP_TOOL_SESSIONS,mapToolSessions);
		LearningUtil learningUtil= new LearningUtil();
		for (int toolSessionIdIndex=1; toolSessionIdIndex <= toolSessionCount; toolSessionIdIndex++)
		{
			logger.debug("toolSessionIdIndex: " + toolSessionIdIndex);
			Long currentToolSession=(Long)toolSessions.get(toolSessionIdIndex);
			logger.debug("current currentToolSession: " + currentToolSession);
			request.getSession().setAttribute(TOOL_SESSION_ID, currentToolSession);
			
			learningUtil.buidLearnerReport(request, toolSessionIdIndex);
		}
		

		return (mapping.findForward(PORTFOLIO_REPORT));		
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
