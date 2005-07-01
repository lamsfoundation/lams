/*
 * Created on 8/03/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * 
 * Monitoring screen does have 4 tabs:
 * 		Summary		: default tab, either all sessions by default or single sessions selected, enable edit responses and hide responses
 * 		Instructions: Online+offline instructions+multipe file uploads + new table
 * 		Edit Activity : points to the definelater url= authoring url basic tab
 * 		Stats		
 * 
 * 
 * SUMMARY_TOOL_SESSIONS keeps all the passed toolSessionIds in an arrayList.
 */

package org.lamsfoundation.lams.tool.qa.web;
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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaStringComparator;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.usermanagement.User;

public class QaMonitoringStarterAction extends Action implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaMonitoringStarterAction.class.getName());
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, QaApplicationException 
	{
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
    	logger.debug(logger + " " + this.getClass().getName() +  "started monitoring mode : " + qaMonitoringForm);
    	
		/**
		 * retrive the service
		 */
    	IQaService qaService=null;
    	qaService =(IQaService)request.getSession().getAttribute(TOOL_SERVICE);
    	if (qaService == null)
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  "qaService is not found yet:");
    		qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
    	    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaService from the session: " + qaService);
    	    request.getSession().setAttribute(TOOL_SERVICE, qaService);	
    	}
    	logger.debug(logger + " " + this.getClass().getName() +  "retrieved qaService: " + qaService);
	    
	    /**
	     * mark the http session as an authoring activity 
	     */
	    request.getSession().setAttribute(TARGET_MODE,TARGET_MODE_MONITORING);
	    
	    /**
	     * obtain and setup the current user's data 
	     */

	    String userId="";
	    User toolUser=(User)request.getSession().getAttribute(TOOL_USER);
	    if (toolUser != null)
	    	userId=toolUser.getUserId().toString();
		else
		{
			userId=request.getParameter(USER_ID);
		    try
			{
		    	User user=QaUtils.createAuthoringUser(new Integer(userId));
		    	request.getSession().setAttribute(TOOL_USER, user);
			}
		    catch(NumberFormatException e)
			{
		    	persistError(request,"error.userId.notNumeric");
				request.setAttribute(USER_EXCEPTION_USERID_NOTNUMERIC, new Boolean(true));
				logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + LOAD_QUESTIONS);
				return (mapping.findForward(MONITORING_REPORT));
			}
		}
	    
	    if ((userId == null) || (userId.length()==0))
		{
	    	logger.debug(logger + " " + this.getClass().getName() +  "error: The tool expects userId");
	    	persistError(request,"error.authoringUser.notAvailable");
	    	request.setAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_ERROR);
			return (mapping.findForward(MONITORING_REPORT));
		}
		logger.debug(logger + " " + this.getClass().getName() +  "TOOL_USER is:" + request.getSession().getAttribute(TOOL_USER));
		
		String toolContentId=request.getParameter(TOOL_CONTENT_ID);
	    logger.debug(logger + " " + this.getClass().getName() +  "TOOL_CONTENT_ID: " + toolContentId);
	    
	    Long initialMonitoringContentId=(Long) request.getSession().getAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID);
	    logger.debug(logger + " " + this.getClass().getName() +  "INITIAL_MONITORING_TOOL_CONTENT_ID: " + initialMonitoringContentId);
	    
	    if ((toolContentId == null) || (toolContentId.length() == 0))
	    {
	    	logger.debug(logger + "Warning!: toolContentId is not available!");
	    }
	    else if (initialMonitoringContentId != null)
	    {
	    	logger.debug(logger + " " + this.getClass().getName() +  "using INITIAL_MONITORING_TOOL_CONTENT_ID: " + initialMonitoringContentId);
	    	toolContentId=initialMonitoringContentId.toString();
	    }

	    try
		{
		    if ((toolContentId != null) && (toolContentId.length() > 0)) 
		    {
		    	logger.debug(logger + "Since TOOL_CONTENT_ID has been passed explicitly we will refer to that one instead of the one derived from tool sessions");
		    	if (!QaUtils.existsContent(new Long(toolContentId).longValue(), request))
		    	{
		    		persistError(request,"error.content.doesNotExist");
		    		request.setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true));
					logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_ERROR);
					return (mapping.findForward(MONITORING_REPORT));
		    	}
		    	request.getSession().setAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID, new Long(toolContentId));	
		    }
	    }
	    catch(NumberFormatException e)
		{
	    	persistError(request,"error.numberFormatException");
			request.setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_ERROR);
			return (mapping.findForward(MONITORING_REPORT));
		}
		logger.debug(logger + "final INITIAL_MONITORING_TOOL_CONTENT_ID: " + 	request.getSession().getAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID));
		
		/**
		 * load the values for online and offline instructions
		 */
		initialMonitoringContentId=(Long)request.getSession().getAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID);
		if (initialMonitoringContentId != null)
		{
			QaContent qaContent=qaService.retrieveQa(initialMonitoringContentId.longValue());
			logger.debug(logger + " " + this.getClass().getName() +  "qaContent: " + qaContent);
			request.getSession().setAttribute(MONITORED_OFFLINE_INSTRUCTIONS, qaContent.getOfflineInstructions());
			request.getSession().setAttribute(MONITORED_ONLINE_INSTRUCTIONS, qaContent.getOnlineInstructions());
			logger.debug(logger + " " + this.getClass().getName() +  "session updated with online/offline instructions");
		}
		
		/**
		 * find out if only content id but no tool sessions has been passed
		 * since with the updated tool contract only userId+toolContentId is passed
		 * this will always return true 
		 */
		
		boolean isOnlyContentIdAvailable = isOnlyContentIdAvailable(request);
		logger.debug(logger + "final isOnlyContentIdAvailable: " + isOnlyContentIdAvailable);
		
		request.getSession().setAttribute(NO_TOOL_SESSIONS_AVAILABLE, new Boolean(false));
		if (isOnlyContentIdAvailable == false)
		{
			/**
			 * this block of code will normally never run!
			 */
			logger.debug(logger + "Warning! We are not supposed to reach here.");
			/*
			logger.debug(logger + "found sessions: isOnlyContentIdAvailable: " + isOnlyContentIdAvailable);
			if ((toolContentId != null) && (toolContentId.length() > 0)) 
			{
				boolean isToolSessionCompatibleToContent=isToolSessionCompatibleToContent(new Long(toolContentId), request);
				logger.debug(logger + "isToolSessionCompatibleToContent: " + isToolSessionCompatibleToContent);
				if (!isToolSessionCompatibleToContent)
				{
			    	persistError(request,"error.contentAndToolSession.notCompatible");
					request.setAttribute(USER_EXCEPTION_UNCOMPATIBLE_IDS, new Boolean(true));
					logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_ERROR);
					return (mapping.findForward(MONITORING_REPORT));
				}
			}
			
			Boolean userExceptionNumberFormat=(Boolean)request.getAttribute(USER_EXCEPTION_NUMBERFORMAT);
			logger.debug(logger + "userExceptionNumberFormat: " + userExceptionNumberFormat);
			if ((userExceptionNumberFormat != null) && (userExceptionNumberFormat.booleanValue()))
			{
				persistError(request,"error.numberFormatException");
				logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_ERROR);
				return (mapping.findForward(MONITORING_REPORT));
			}
			
			Boolean userExceptionWrongFormat=(Boolean)request.getAttribute(USER_EXCEPTION_WRONG_FORMAT);
			logger.debug(logger + "userExceptionWrongFormat: " + userExceptionWrongFormat);
			if ((userExceptionWrongFormat != null) && (userExceptionWrongFormat.booleanValue()))
			{
				persistError(request,"error.toolSessions.wrongFormat");
				logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_ERROR);
				return (mapping.findForward(MONITORING_REPORT));
			}
			
			Boolean userExceptionToolSessionDoesNotExist=(Boolean)request.getAttribute(USER_EXCEPTION_TOOLSESSION_DOESNOTEXIST);
			logger.debug(logger + "userExceptionToolSessionDoesNotExist: " + userExceptionToolSessionDoesNotExist);
			if ((userExceptionToolSessionDoesNotExist != null) && (userExceptionToolSessionDoesNotExist.booleanValue()))
			{
				persistError(request,"error.toolSessions.doesNotExist");
				logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_ERROR);
				return (mapping.findForward(MONITORING_REPORT));
			}
			qaMonitoringForm.setSummary("summary");
			*/
		}
		else
		{
			logger.debug(logger + "isOnlyContentIdAvailable: " + isOnlyContentIdAvailable);
			logger.debug(logger + "no tool sessions passed and they will be populated from toolContentId.");
			qaMonitoringForm.resetUserAction();
		/*
			persistError(request,"error.content.onlyContentAndNoSessions");
    		request.setAttribute(USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS, new Boolean(true));
    		request.getSession().setAttribute(NO_TOOL_SESSIONS_AVAILABLE, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_REPORT);
			request.getSession().setAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID, new Long(toolContentId));
			logger.debug(logger + "INITIAL_MONITORING_TOOL_CONTENT_ID: " + request.getSession().getAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID));
			return (mapping.findForward(MONITORING_REPORT));
		*/
			logger.debug(logger + "getting qaContent for toolContentId: " + toolContentId);
			QaContent qaContent=qaService.loadQa(new Long(toolContentId).longValue());
			logger.debug(logger + "retrieved qaContent: " + qaContent);
			List listToolSessionIds=qaService.getToolSessionsForContent(qaContent);
			logger.debug(logger + "retrieved listToolSessionIds: " + listToolSessionIds);
			
			Map originalSessionList= new TreeMap(new QaStringComparator());
			Iterator sessionIdsIterator=listToolSessionIds.iterator();
			int sessionIdCounter=1;
			while (sessionIdsIterator.hasNext())
			{
				Long derivedToolSessionId=(Long) sessionIdsIterator.next();
				logger.debug("derivedToolSessionId: " + derivedToolSessionId);
				originalSessionList.put(new Integer(sessionIdCounter).toString(), derivedToolSessionId.toString());
				sessionIdCounter++;
			}
			logger.debug(logger + "constructed originalSessionList: " + originalSessionList);
			
			if (originalSessionList.size() == 0)
				request.getSession().setAttribute(NO_TOOL_SESSIONS_AVAILABLE, new Boolean(true));
			else
				request.getSession().setAttribute(ORIGINAL_TOOL_SESSIONS,originalSessionList);			
			
			qaMonitoringForm.setSummary("summary");
		}
		
	    String strFromToolContentId="";
	    String strToToolContentId="";
	  	    
	    /**
	     * simulate Monitoring Service bean by calling the interface methods here
	     */
	    	if (qaMonitoringForm.getStartLesson() != null)
	    	{
	    		qaMonitoringForm.resetUserAction();
	    		/**
	    	     * In deployment, we won't be passing FROM_TOOL_CONTENT_ID, TO_TOOL_CONTENT_ID and TOOL_SESSION_ID from url
	    	     * the Monitoring Service bean calls:
	    	     * copyToolContent(Long fromContentId, Long toContentId)  
	    	     */
	    	    strFromToolContentId=request.getParameter(FROM_TOOL_CONTENT_ID);
	    		logger.debug(logger + " " + this.getClass().getName() +  "FROM_TOOL_CONTENT_ID: " + strFromToolContentId);
	    	    if (strFromToolContentId == null)
	    	    {
	    	    	throw new QaApplicationException("Exception occured: " +
	    	    			"Tool expects a legitimate FROM_TOOL_CONTENT_ID from the container. Can't continue!");
	    	    }
	    	    
	    	    strToToolContentId=request.getParameter(TO_TOOL_CONTENT_ID);
	    	    logger.debug(logger + " " + this.getClass().getName() +  "TO_TOOL_CONTENT_ID: " + strToToolContentId);
	    	    if (strToToolContentId == null)
	    	    {
	    	    	throw new QaApplicationException("Exception occured: " +
	    	    			"Tool expects a legitimate TO_TOOL_CONTENT_ID from the container. Can't continue!");
	    	    }
	    		qaService.copyToolContent(new Long(strFromToolContentId), new Long(strToToolContentId));
		    	
		    	/** calls to these two methods will be made from Monitoring Service bean optionally depending on
		    	 *  the the tool is setup for DefineLater and (or) RunOffline 
		    	 */
	    		
	    		/**
	    		 * TESTED to work
	    		 * qaService.setAsDefineLater(new Long(strToToolContentId));
		    	   qaService.setAsRunOffline(new Long(strToToolContentId));
		    	 * 
		    	 */
	    	}
	    	else if (qaMonitoringForm.getDeleteLesson() != null)
	    	{
	    		qaMonitoringForm.resetUserAction();
	    		/**
	    		 * TESTED to work
		    	 */
	    		strToToolContentId=request.getParameter(TO_TOOL_CONTENT_ID);
	    	    logger.debug(logger + " " + this.getClass().getName() +  "TO_TOOL_CONTENT_ID: " + strToToolContentId);
	    	    if (strToToolContentId == null)
	    	    {
	    	    	throw new QaApplicationException("Exception occured: " +
	    	    			"Tool expects a legitimate TO_TOOL_CONTENT_ID from the container. Can't continue!");
	    	    }
	    		qaService.removeToolContent(new Long(strToToolContentId));
	    	}
	    	/**
	    	 *forceComplete is an API call to service bean from monitoring environment with userId as the parameter
	    	 */
	    	else if (qaMonitoringForm.getForceComplete() != null)
	    	{
	    		/**
	    		 * Parameter: userId
	    		 */
	    		qaMonitoringForm.resetUserAction();
	    		logger.debug(logger + " " + this.getClass().getName() +  "request for forceComplete");
	    		userId=request.getParameter(MONITOR_USER_ID);
	    		logger.debug(logger + " " + this.getClass().getName() +  "MONITOR_USER_ID: " + userId);
	    		qaService.setAsForceComplete(new Long(userId));
	    		logger.debug(logger + " " + this.getClass().getName() +  "end of setAsForceComplete with userId: " + userId);
	    	}
	    	/**
	    	 * summary tab is one of the main tabs in monitoring screen, summary is the default tab
	    	 */
	    	else if (qaMonitoringForm.getSummary() != null)
	    	{
	    		qaMonitoringForm.resetUserAction();
	    		logger.debug(logger + " " + this.getClass().getName() +  "do generateToolSessionDataMap");
	    		QaMonitoringAction  qaMonitoringAction= new QaMonitoringAction();
	    		return qaMonitoringAction.generateToolSessionDataMap(mapping,form,request,response);
	    	}
	    	else if (qaMonitoringForm.getInstructions() != null)
	    	{
	    		qaMonitoringForm.resetUserAction();
	    		logger.debug(logger + " " + this.getClass().getName() +  "request for instructions");
	    	}
	    	else if (qaMonitoringForm.getEditActivity() != null)
	    	{
	    		qaMonitoringForm.resetUserAction();
	    		logger.debug(logger + " " + this.getClass().getName() +  "request for editActivity");
	    	}
	    	else if (qaMonitoringForm.getStats() != null)
	    	{
	    		qaMonitoringForm.resetUserAction();
	    		logger.debug(logger + " " + this.getClass().getName() +  "request for stats");
	    	}
		return null;
	}


	public boolean isOnlyContentIdAvailable(HttpServletRequest request)
	{
		boolean existsContentId=false;
		String strToolContentId=request.getParameter(TOOL_CONTENT_ID);
		if ((strToolContentId != null) && (strToolContentId.length() > 0))
			existsContentId=true;
		
		boolean existsToolSession=false;
		for (int toolSessionIdCounter=1; toolSessionIdCounter < MAX_TOOL_SESSION_COUNT.intValue(); toolSessionIdCounter++)
		{
			String strToolSessionId=request.getParameter(TOOL_SESSION_ID + toolSessionIdCounter);
			if ((strToolSessionId != null) && (strToolSessionId.length() > 0))
			{
				existsToolSession=true;
			}
		}
		
		if (existsContentId && (!existsToolSession))
		{
    		logger.debug(logger + " " + this.getClass().getName() +  "OnlyContentIdAvailable");
			return true;
		}
		else
		{
			logger.debug(logger + " " + this.getClass().getName() +  "Not OnlyContentIdAvailable");
			return false;
		}
	}
	
	/**
	 * verify that toolSession and content is compatible
	 */
	public boolean isToolSessionCompatibleToContent(Long toolContentId, HttpServletRequest request)
	{
		IQaService qaService =QaUtils.getToolService(request);
		for (int toolSessionIdCounter=1; toolSessionIdCounter < MAX_TOOL_SESSION_COUNT.intValue(); toolSessionIdCounter++)
		{
			String strToolSessionId=request.getParameter(TOOL_SESSION_ID + toolSessionIdCounter);
		    logger.debug(logger + " " + this.getClass().getName() +  "TOOL_SESSION_ID: " + strToolSessionId);
		    if ((strToolSessionId != null) && (strToolSessionId.length() > 0))
		    {
		    	QaSession qaSession=null;
		    	try
				{
		    		qaSession=qaService.retrieveQaSessionOrNullById(new Long(strToolSessionId).longValue());
				}
		    	catch(NumberFormatException e)
				{
					request.setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true));
				}

		    	if (qaSession != null)
		    	{
		    		QaContent qaContent=qaSession.getQaContent();
		    	    logger.debug(logger + " " + this.getClass().getName() +  "using qaContent: " + qaContent);
		    	    logger.debug(logger + " " + this.getClass().getName() +  "qaContent id versus toolSession's content id: " + toolContentId + 
		    	    														" versus " + qaContent.getQaContentId());
		    	    if (!qaContent.getQaContentId().equals(toolContentId))
		    	    {
		    	    	logger.debug(logger + " " + this.getClass().getName() +  "qaContent and toolSesion not compatible:");
		    	    	return false;
		    	    }
		    	}
		    	else
		    	{
		    		logger.debug(logger + " " + this.getClass().getName() +  "error: TOOL_SESSION_ID passed does not refer to an existing tool session.");
		    		request.setAttribute(USER_EXCEPTION_TOOLSESSION_DOESNOTEXIST, new Boolean(true));
		    		break;
		    	}
		    }
		    
		    if ((strToolSessionId == null) && (toolSessionIdCounter == 1))
		    {
		    	logger.debug(logger + " " + this.getClass().getName() +  "error: TOOL_SESSION_IDs not passed in the right format");
		    	request.setAttribute(USER_EXCEPTION_WRONG_FORMAT, new Boolean(true));
		    	break;
		    }
		    if ((strToolSessionId != null) && (strToolSessionId.length() == 0) && (toolSessionIdCounter == 1))
		    {
		    	logger.debug(logger + " " + this.getClass().getName() +  "error: TOOL_SESSION_IDs not passed in the right format");
		    	request.setAttribute(USER_EXCEPTION_WRONG_FORMAT, new Boolean(true));
		    	break;
		    }
		}
		logger.debug(logger + " " + this.getClass().getName() +  "qaContent and toolSesion compatible:");
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
		logger.debug(logger + " " + this.getClass().getName() +  "add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}  
