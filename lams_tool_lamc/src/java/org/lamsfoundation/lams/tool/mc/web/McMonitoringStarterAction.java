/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
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
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McComparator;
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
		
		request.getSession().setAttribute(CURRENT_MONITORING_TAB, "summary");
		McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
		mcMonitoringForm.setEditOptionsMode(new Integer(0).toString());
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		
		request.getSession().setAttribute(ACTIVE_MODULE, DEFINE_LATER);
		request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(false));
		request.getSession().setAttribute(SHOW_AUTHORING_TABS,new Boolean(false).toString());			

		Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
		logger.debug("toolContentId: " + toolContentId);
		
		IMcService mcService = (IMcService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("mcService: " + mcService);
		if (mcService == null)
		{
			logger.debug("will retrieve mcService");
			mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		    logger.debug("retrieving mcService from cache: " + mcService);
		}

		McContent mcContent=mcService.retrieveMc(toolContentId);
		logger.debug("mcContent:" + mcContent);

		
	    /* it is possible that no users has ever logged in for the activity yet*/ 
	    int countAllUsers=mcService.getTotalNumberOfUsers();
		logger.debug("countAllUsers: " + countAllUsers);
		
		
		request.getSession().setAttribute(USER_EXCEPTION_NO_STUDENT_ACTIVITY, new Boolean(false));
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
		
		
		request.getSession().setAttribute(IS_REVISITING_USER, new Boolean(false));
		request.getSession().setAttribute(RICHTEXT_TITLE,mcContent.getTitle());
		request.getSession().setAttribute(RICHTEXT_INSTRUCTIONS,mcContent.getInstructions());
		request.getSession().setAttribute(PASSMARK, mcContent.getPassMark()); //Integer
		
    	Map mapQuestionsContent=AuthoringUtil.rebuildQuestionMapfromDB(request, toolContentId);
    	logger.debug("mapQuestionsContent:" + mapQuestionsContent);
    	request.getSession().setAttribute(MAP_QUESTIONS_CONTENT, mapQuestionsContent);
    	logger.debug("starter initialized the existing Questions Map: " + request.getSession().getAttribute(MAP_QUESTIONS_CONTENT));
    	
    	Map mapWeights= AuthoringUtil.rebuildWeightsMapfromDB(request, toolContentId);
    	logger.debug("Check the mapWeights: " + mapWeights);
    	request.getSession().setAttribute(MAP_WEIGHTS, mapWeights);
    	
    	Map mapGeneralOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapGeneralOptionsContent);
		
		Map mapGeneralSelectedOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapGeneralSelectedOptionsContent);
		
		Map mapStartupGeneralOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_STARTUP_GENERAL_OPTIONS_CONTENT, mapStartupGeneralOptionsContent);
		
		Map mapStartupGeneralSelectedOptionsContent= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT, mapStartupGeneralSelectedOptionsContent);
		
		Map mapDisabledQuestions= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_DISABLED_QUESTIONS, mapDisabledQuestions);
		
		Map mapCheckBoxStates= new TreeMap(new McComparator());
		mapCheckBoxStates.put("1" ,"INCORRECT");
		mapCheckBoxStates.put("2" ,"CORRECT");
		request.getSession().setAttribute(MAP_CHECKBOX_STATES, mapCheckBoxStates);

		Map mapSelectedOptions= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_SELECTED_OPTIONS, mapSelectedOptions);
		
		Map mapIncorrectFeedback= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_INCORRECT_FEEDBACK, mapIncorrectFeedback);
		
		Map mapCorrectFeedback= new TreeMap(new McComparator());
		request.getSession().setAttribute(MAP_CORRECT_FEEDBACK, mapCorrectFeedback);
		
		List listUploadedOffFiles= mcService.retrieveMcUploadedOfflineFilesName(mcContent.getUid());
		logger.debug("existing listUploadedOfflineFileNames:" + listUploadedOffFiles);
		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES,listUploadedOffFiles);
		
		List listUploadedOnFiles= mcService.retrieveMcUploadedOnlineFilesName(mcContent.getUid());
		logger.debug("existing listUploadedOnlineFileNames:" + listUploadedOnFiles);
		request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES,listUploadedOnFiles);
		
		List listOfflineFilesMetaData=mcService.getOfflineFilesMetaData(mcContent.getUid());
	    logger.debug("existing listOfflineFilesMetaData, to be structured as McAttachmentDTO: " + listOfflineFilesMetaData);
	    listOfflineFilesMetaData=AuthoringUtil.populateMetaDataAsAttachments(listOfflineFilesMetaData);
	    logger.debug("populated listOfflineFilesMetaData: " + listOfflineFilesMetaData);
	    request.getSession().setAttribute(LIST_OFFLINEFILES_METADATA, listOfflineFilesMetaData);
	    
	    List listUploadedOfflineFileNames=AuthoringUtil.populateMetaDataAsFilenames(listOfflineFilesMetaData);
	    logger.debug("returned from db listUploadedOfflineFileNames: " + listUploadedOfflineFileNames);
	    request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES, listUploadedOfflineFileNames);
	    
	    /*process online files metadata*/
	    List listOnlineFilesMetaData=mcService.getOnlineFilesMetaData(mcContent.getUid());
	    logger.debug("existing listOnlineFilesMetaData, to be structured as McAttachmentDTO: " + listOnlineFilesMetaData);
	    listOnlineFilesMetaData=AuthoringUtil.populateMetaDataAsAttachments(listOnlineFilesMetaData);
	    logger.debug("populated listOnlineFilesMetaData: " + listOnlineFilesMetaData);
	    request.getSession().setAttribute(LIST_ONLINEFILES_METADATA, listOnlineFilesMetaData);
	    
	    List listUploadedOnlineFileNames=AuthoringUtil.populateMetaDataAsFilenames(listOnlineFilesMetaData);
	    logger.debug("returned from db listUploadedOnlineFileNames: " + listUploadedOnlineFileNames);
	    request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES, listUploadedOnlineFileNames);
	    
		logger.debug("end of execute: ");
		logger.debug("SUMMARY_TOOL_SESSIONS_ID: " + request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS_ID));
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS));
		logger.debug("SELECTION_CASE: " + request.getSession().getAttribute(SELECTION_CASE));
		logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));

		boolean isContentInUse=McUtils.isContentInUse(mcContent);
		logger.debug("isContentInUse:" + isContentInUse);
		
		request.getSession().setAttribute(IS_MONITORED_CONTENT_IN_USE, new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
	    	persistError(request,"error.content.inUse");
	    	request.getSession().setAttribute(IS_MONITORED_CONTENT_IN_USE, new Boolean(true).toString());
		}
		logger.debug("final IS_MONITORED_CONTENT_IN_USE: " + request.getSession().getAttribute(IS_MONITORED_CONTENT_IN_USE));
		
    	/*get existing feedback maps*/
    	mapIncorrectFeedback = AuthoringUtil.rebuildIncorrectFeedbackMapfromDB(request,toolContentId);
    	logger.debug("existing mapIncorrectFeedback:" + mapIncorrectFeedback);
    	request.getSession().setAttribute(MAP_INCORRECT_FEEDBACK, mapIncorrectFeedback);
    	
    	mapCorrectFeedback = AuthoringUtil.rebuildCorrectFeedbackMapfromDB(request, toolContentId);
    	logger.debug("existing mapCorrectFeedback:" + mapCorrectFeedback);
    	request.getSession().setAttribute(MAP_CORRECT_FEEDBACK, mapCorrectFeedback);
		
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
		logger.debug("initialising MonitoringData...");
		
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
	    
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, mcContent, mcService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		if (summaryToolSessions.isEmpty())
		{
			/* inform in the Summary tab that the tool has no active sessions */
			request.setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true));
		}
		
		Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, mcContent, mcService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);
		
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
		request.getSession().setAttribute(USER_EXCEPTION_NO_STUDENT_ACTIVITY, new Boolean(false));
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
		
		logger.debug("end of initialiseMonitoringData: ");
		logger.debug("SUMMARY_TOOL_SESSIONS_ID: " + request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS_ID));
		logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS));
		logger.debug("SELECTION_CASE: " + request.getSession().getAttribute(SELECTION_CASE));
		logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));
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
