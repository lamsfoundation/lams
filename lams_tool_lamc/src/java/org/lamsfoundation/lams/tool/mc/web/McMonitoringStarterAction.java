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
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * 
 * @author Ozgur Demirtas
 * starts up the monitoring module
 * 
    <!--Monitoring Starter Action: initializes the Monitoring module -->
   <action 	path="/monitoringStarter" 
   			type="org.lamsfoundation.lams.tool.mc.web.McMonitoringStarterAction" 
   			name="McMonitoringForm" 
   			input=".monitoringStarter"> 

		<exception
	        key="error.exception.McApplication"
	        type="org.lamsfoundation.lams.tool.mc.McApplicationException"
	        handler="org.lamsfoundation.lams.tool.mc.web.CustomStrutsExceptionHandler"
	        path=".mcErrorBox"
	        scope="request"
	      />

		<exception
	        key="error.exception.McApplication"
	        type="java.lang.NullPointerException"
	        handler="org.lamsfoundation.lams.tool.mc.web.CustomStrutsExceptionHandler"
	        path=".mcErrorBox"
	        scope="request"
	      />	         			   			
   			
	  	<forward
		    name="loadMonitoring"
		    path=".monitoringContent"
		    redirect="true"
	  	/>

       <forward
	        name="load"
	        path=".questions"
	        redirect="true"
	    />
	    
	    <forward
	        name="starter"
	        path=".starter"
	        redirect="true"
	     />

	  	<forward
		    name="errorList"
		    path=".mcErrorBox"
		    redirect="true"
	  	/>
	</action>  

 *
 */

public class McMonitoringStarterAction extends Action implements McAppConstants {
	static Logger logger = Logger.getLogger(McMonitoringStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, McApplicationException 
	{
		McUtils.cleanUpSessionAbsolute(request);
		
	    ActionForward validateParameters=validateParameters(request, mapping);
	    logger.debug("validateParamaters: " + validateParameters);
	    if (validateParameters != null)
	    {
	    	return validateParameters;
	    }

		boolean initData=initialiseMonitoringData(mapping, form, request, response);
		logger.debug("initData: " + initData);
		if (initData == false)
			return (mapping.findForward(ERROR_LIST));
		
	    return (mapping.findForward(LOAD_MONITORING_CONTENT));	
	}

	
	/**
	 * initialises monitoring data mainly for jsp purposes 
	 * initialiseMonitoringData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return boolean
	 */
	public boolean initialiseMonitoringData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		IMcService mcService = (IMcService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("mcService: " + mcService);
		if (mcService == null)
		{
			logger.debug("will retrieve mcService");
			mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		    logger.debug("retrieving mcService from cache: " + mcService);
		}

	    request.getSession().setAttribute(TOOL_SERVICE, mcService);		
		McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
	    
	    /*
	     * persist time zone information to session scope. 
	     */
	    McUtils.persistTimeZone(request);

	    /* we have made sure TOOL_CONTENT_ID is passed  */
	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("existing mcContent:" + mcContent);
		
		if (mcContent == null)
		{
			McUtils.cleanUpSessionAbsolute(request);
    		request.getSession().setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true).toString());
    		persistError(request, "error.content.doesNotExist");
			return false;
		}
	    
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, mcContent);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		if (summaryToolSessions.isEmpty())
		{
			/* inform in the Summary tab that the tool has no active sessions */
			request.setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true));
		}
		
		
		/* this section is related to summary tab. Starts here. */ 
		request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
	    logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS));
	    
	    /* SELECTION_CASE == 2 indicates start up */
	    request.getSession().setAttribute(SELECTION_CASE, new Long(2));
	    logger.debug("SELECTION_CASE: " + request.getSession().getAttribute(SELECTION_CASE));
	    
	    /* Default to All for tool Sessions so that all tool sessions' summary information gets displayed when the module starts up */
	    request.getSession().setAttribute(CURRENT_MONITORED_TOOL_SESSION, "All");
	    logger.debug("CURRENT_MONITORED_TOOL_SESSION: " + request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION));
	    
	    List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionData(request, mcContent);
	    
	    request.getSession().setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);
	    logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));
	    /* ends here*/
	    
	    /* this section is related to instructions tab. Starts here. */
	    request.getSession().setAttribute(RICHTEXT_ONLINEINSTRUCTIONS,mcContent.getOnlineInstructions());
	    request.getSession().setAttribute(RICHTEXT_OFFLINEINSTRUCTIONS,mcContent.getOfflineInstructions());
	    /* ends here. */

	    
	    /* it is possible that no users has ever logged in for the activity yet*/ 
	    int countAllUsers=mcService.getTotalNumberOfUsers();
		logger.debug("countAllUsers: " + countAllUsers);
		
		if (countAllUsers == 0)
		{
	    	logger.debug("error: countAllUsers is 0");
	    	request.getSession().setAttribute(USER_EXCEPTION_NO_STUDENT_ACTIVITY, new Boolean(true));
		}
		
		/* it is possible that no users has ever attempted the activity yet*/
		int totalAttemptCount=MonitoringUtil.getTotalAttemptCount(request);
		logger.debug("totalAttemptCount: " + totalAttemptCount);
		if (totalAttemptCount == 0)
		{
	    	logger.debug("error: totalAttemptCount is 0");
	    	request.getSession().setAttribute(USER_EXCEPTION_NO_STUDENT_ACTIVITY, new Boolean(true));
		}
		
		/* this section is related to Stats tab. Starts here. */
		int countSessionComplete=mcService.countSessionComplete();
		logger.debug("countSessionComplete: " + countSessionComplete);
		
		int countMaxAttempt=1;
		logger.debug("countMaxAttempt: " + countMaxAttempt);
		
		int topMark=LearningUtil.getTopMark(request);
		logger.debug("topMark: " + topMark);
		
		int lowestMark=LearningUtil.getLowestMark(request);
		logger.debug("lowestMark: " + lowestMark);
		
		int averageMark=LearningUtil.getAverageMark(request);
		logger.debug("averageMark: " + averageMark);
		
		request.getSession().setAttribute(COUNT_ALL_USERS, new Integer(countAllUsers).toString());
		request.getSession().setAttribute(COUNT_SESSION_COMPLETE, new Integer(countSessionComplete).toString());
		request.getSession().setAttribute(COUNT_MAX_ATTEMPT, new Integer(countMaxAttempt).toString());
		request.getSession().setAttribute(TOP_MARK, new Integer(topMark).toString());
		request.getSession().setAttribute(LOWEST_MARK, new Integer(lowestMark).toString());
		request.getSession().setAttribute(AVERAGE_MARK, new Integer(averageMark).toString());
		/* ends here. */
		return true;
	}

	
	/**
	 * validates request paramaters based on tool contract
	 * validateParameters(HttpServletRequest request, ActionMapping mapping)
	 * 
	 * @param request
	 * @param mapping
	 * @return ActionForward
	 */
	protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping)
	{
		logger.debug("start validating monitoring parameters...");
		/*
	     * obtain and setup the current user's data 
	     */
		/*
	    String userID = "";
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if ((user == null) || (user.getUserID() == null))
	    {
	    	logger.debug("error: The tool expects userId");
	    	persistError(request,"error.learningUser.notAvailable");
	    	McUtils.cleanUpSessionAbsolute(request);
	    	return (mapping.findForward(ERROR_LIST));
	    }else
	    	userID = user.getUserID().toString();
	    
	    logger.debug("retrieved userId: " + userID);
    	request.getSession().setAttribute(USER_ID, userID);
    	*/
    	
    	String strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
    	logger.debug("strToolContentId: " + strToolContentId);
    	 
	    if ((strToolContentId == null) || (strToolContentId.length() == 0)) 
	    {
	    	McUtils.cleanUpSessionAbsolute(request);
	    	persistError(request, "error.contentId.required");
	    	request.getSession().setAttribute(USER_EXCEPTION_CONTENTID_REQUIRED, new Boolean(true).toString());
	    	return (mapping.findForward(ERROR_LIST));
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
	    		McUtils.cleanUpSessionAbsolute(request);
		    	request.getSession().setAttribute(USER_EXCEPTION_NUMBERFORMAT, new Boolean(true).toString());
	    		persistError(request, "error.numberFormatException");
	    		logger.debug("add error.numberFormatException to ActionMessages.");
				return (mapping.findForward(ERROR_LIST));
			}
	    }
	    return null;
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
