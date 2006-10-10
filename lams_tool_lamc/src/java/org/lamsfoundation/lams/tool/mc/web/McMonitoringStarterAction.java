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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
import org.lamsfoundation.lams.tool.mc.EditActivityDTO;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.mc.McGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.mc.McQuestionContentDTO;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;


/**
 * 
 * <p> Starts up the monitoring module </p>
 * 
 * @author Ozgur Demirtas
 * 
    
    <!--Monitoring Starter Action: initializes the Monitoring module -->
   <action 	path="/monitoringStarter" 
   			type="org.lamsfoundation.lams.tool.mc.web.McMonitoringStarterAction" 
	      	scope="request"
   			name="McMonitoringForm" 
      		unknown="false"
      		validate="false"
   			input="/monitoringIndex.jsp"> 

	  	<forward
			name="loadMonitoring"
			path="/monitoring/MonitoringMaincontent.jsp"
			redirect="false"
	  	/>
	
	  	<forward
			name="refreshMonitoring"
			path="/monitoring/MonitoringMaincontent.jsp"
			redirect="false"
	  	/>
	         			
   			
	  	<forward
		    name="errorList"
	        path="/McErrorBox.jsp"
		    redirect="false"
	  	/>
	</action>  
  
 * 
 */

public class McMonitoringStarterAction extends Action implements McAppConstants {
	static Logger logger = Logger.getLogger(McMonitoringStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, McApplicationException 
	{
		logger.debug("init McMonitoringStarterAction...");
		McUtils.cleanUpSessionAbsolute(request);

		IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
		logger.debug("mcService: " + mcService);
		
		McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
		logger.debug("mcMonitoringForm: " + mcMonitoringForm);
		
		McGeneralAuthoringDTO mcGeneralAuthoringDTO= new McGeneralAuthoringDTO();
		McGeneralMonitoringDTO mcGeneralMonitoringDTO=new McGeneralMonitoringDTO();
		
	    ActionForward validateParameters=validateParameters(request, mapping, mcMonitoringForm);
	    logger.debug("validateParamaters: " + validateParameters);
	    
	    if (validateParameters != null)
	    {
	    	//return validateParameters;
	        logger.debug("error validating monitoring parameters");
	    }

		boolean initData=initialiseMonitoringData(mapping, form, request, response, 
		        mcService, mcGeneralMonitoringDTO);
		logger.debug("initData: " + initData);
		if (initData == false)
			//return (mapping.findForward(ERROR_LIST));
			logger.debug("error initialising monitoring parameters");
		
		mcMonitoringForm.setCurrentTab("1");
		mcGeneralMonitoringDTO.setCurrentTab("1");
		logger.debug("setting current tab to 1: ");
		
	    String toolContentID=mcMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
		
		mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		mcGeneralMonitoringDTO.setCountAllUsers(new Integer(0).toString());
		mcGeneralMonitoringDTO.setCountSessionComplete(new Integer(0).toString());
		
		McMonitoringAction mcMonitoringAction= new McMonitoringAction();
		
		logger.debug("calling initSummaryContent.");
		mcMonitoringAction.initSummaryContent(toolContentID , request, mcService, mcGeneralMonitoringDTO);
		logger.debug("post initSummaryContent, mcGeneralMonitoringDTO: " + mcGeneralMonitoringDTO);
		
		logger.debug("calling initInstructionsContent.");
		logger.debug("post initInstructionsContent, mcGeneralMonitoringDTO: " + mcGeneralMonitoringDTO);
		
		logger.debug("calling initStatsContent.");
		mcMonitoringAction.initStatsContent(toolContentID, request, mcService, mcGeneralMonitoringDTO);
		logger.debug("post initStatsContent, mcGeneralMonitoringDTO: " + mcGeneralMonitoringDTO);
		
	    McContent mcContent=mcService.retrieveMc(new Long(toolContentID));
	    logger.debug("mcContent: " + mcContent);
	    
		/*true means there is at least 1 response*/
		if (mcService.studentActivityOccurredGlobal(mcContent))
		{
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
		    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

		mcMonitoringForm.setActiveModule(MONITORING);
		mcGeneralMonitoringDTO.setActiveModule(MONITORING);
		
		mcMonitoringForm.setSelectedToolSessionId("All");
		mcGeneralMonitoringDTO.setSelectedToolSessionId("All");
		
		mcGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());
		mcGeneralMonitoringDTO.setIsPortfolioExport(new Boolean(false).toString());
		
		logger.debug("calling submitSession:" + toolContentID);
		request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);
		
		/*this section is needed for Edit Activity screen, from  here... */
		logger.debug("for copy using mcGeneralMonitoringDTO: " + mcGeneralMonitoringDTO);
		mcGeneralAuthoringDTO.setActivityTitle(mcGeneralMonitoringDTO.getActivityTitle());
		mcGeneralAuthoringDTO.setActivityInstructions(mcGeneralMonitoringDTO.getActivityInstructions());
		mcGeneralAuthoringDTO.setActiveModule(MONITORING);
		
		Map mapOptionsContent=mcGeneralMonitoringDTO.getMapOptionsContent();
		logger.debug("mapOptionsContent: " + mapOptionsContent);
	    int maxIndex=mapOptionsContent.size();
	    logger.debug("maxIndex: " + maxIndex);

	    logger.debug("mcGeneralAuthoringDTO: " + mcGeneralAuthoringDTO);
		request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);
		/*...till here */
		
		logger.debug("calling submitSession with selectedToolSessionId" + mcMonitoringForm.getSelectedToolSessionId());
		return mcMonitoringAction.submitSession(mapping, mcMonitoringForm,  request, response, mcService, mcGeneralMonitoringDTO);
	}


	/**
	 * initialiseMonitoringData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,
	        IMcService mcService, McGeneralMonitoringDTO mcGeneralMonitoringDTO)
	        
	   initialises monitoring data
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param mcService
	 * @param mcGeneralMonitoringDTO
	 * @return
	 */
	public boolean initialiseMonitoringData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,
	        IMcService mcService, McGeneralMonitoringDTO mcGeneralMonitoringDTO)
	{
	    logger.debug("start initializing  monitoring data...mcService: " + mcService);
		McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;

		mcGeneralMonitoringDTO.setCurrentMonitoringTab("summary");
		mcGeneralMonitoringDTO.setSbmtSuccess(new Boolean(false).toString());
		mcGeneralMonitoringDTO.setDefineLaterInEditMode(new Boolean(false).toString());
		mcGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());
		mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
		
	    //McUtils.saveTimeZone(request);
		
		/* we have made sure TOOL_CONTENT_ID is passed  */
	    String toolContentID=mcMonitoringForm.getToolContentID();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    McContent mcContent=mcService.retrieveMc(new Long(toolContentID));
		logger.debug("existing mcContent:" + mcContent);
		
		if (mcContent == null)
		{
			McUtils.cleanUpSessionAbsolute(request);
			mcGeneralMonitoringDTO.setUserExceptionContentDoesNotExist(new Boolean(true).toString());
			return false;
		}
		
		
		boolean isContentInUse=McUtils.isContentInUse(mcContent);
		logger.debug("isContentInUse:" + isContentInUse);
		mcGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
			mcGeneralMonitoringDTO.setIsMonitoredContentInUse(new Boolean(true).toString());
		}
		
		mcGeneralMonitoringDTO.setActivityTitle(mcContent.getTitle());
		mcGeneralMonitoringDTO.setActivityInstructions(mcContent.getInstructions());

		logger.debug("checking student activity on the standard Questions:" + mcContent);
		if (mcService.studentActivityOccurredGlobal(mcContent))
		{
			McUtils.cleanUpSessionAbsolute(request);
			logger.debug("student activity occurred on this content:" + mcContent);
			mcGeneralMonitoringDTO.setUserExceptionContentInUse(new Boolean(true).toString());
			mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
		    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

	    /*
		 * get the questions 
		 * section  is needed for the Edit tab's View Only mode, starts here
		 */
		
		SessionMap sessionMap = new SessionMap();
	    sessionMap.put(ACTIVITY_TITLE_KEY, mcContent.getTitle());
	    sessionMap.put(ACTIVITY_INSTRUCTIONS_KEY, mcContent.getInstructions());
	    	    
	    mcMonitoringForm.setHttpSessionID(sessionMap.getSessionID());
	    request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);

		
		List listQuestionContentDTO= new  LinkedList();
		
		Map mapOptionsContent= new TreeMap(new McComparator());
		logger.debug("setting existing content data from the db");
		mapOptionsContent.clear();
		Iterator queIterator=mcContent.getMcQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapOptionsContent: " + mapOptionsContent);
		while (queIterator.hasNext())
		{
		    McQuestionContentDTO mcContentDTO=new McQuestionContentDTO();
		    
			McQueContent mcQueContent=(McQueContent) queIterator.next();
			if (mcQueContent != null)
			{
				logger.debug("question: " + mcQueContent.getQuestion());
				mapOptionsContent.put(mapIndex.toString(),mcQueContent.getQuestion());

				mcContentDTO.setQuestion(mcQueContent.getQuestion());
				mcContentDTO.setDisplayOrder(mcQueContent.getDisplayOrder().toString());
				listQuestionContentDTO.add(mcContentDTO);
	    		
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		logger.debug("Map initialized with existing contentid to: " + mapOptionsContent);
		mcGeneralMonitoringDTO.setMapOptionsContent(mapOptionsContent);
		/* ends here*/
		
		
		logger.debug("listQuestionContentDTO: " + listQuestionContentDTO);
		request.setAttribute(LIST_QUESTION_CONTENT_DTO,listQuestionContentDTO);
		sessionMap.put(LIST_QUESTION_CONTENT_DTO_KEY, listQuestionContentDTO);
		
		request.setAttribute(TOTAL_QUESTION_COUNT, new Integer(listQuestionContentDTO.size()));
		
		
		McMonitoringAction mcMonitoringAction= new McMonitoringAction();
		logger.debug("refreshing summary data...");
		mcMonitoringAction.refreshSummaryData(request, mcContent, mcService, true, false, 
		        null, null, false, null, mcGeneralMonitoringDTO, null);
		logger.debug("post refreshSummaryData, mcGeneralMonitoringDTO: " + mcGeneralMonitoringDTO);
		
		//mcMonitoringAction.refreshStatsData(request, mcService, mcGeneralMonitoringDTO);
		
		logger.debug("end initializing  monitoring data...");
	    mcGeneralMonitoringDTO.setExistsOpenMcs(new Boolean(false).toString());
	    logger.debug("post refreshes, mcGeneralMonitoringDTO: " + mcGeneralMonitoringDTO);
	    
	    
    	EditActivityDTO editActivityDTO = new EditActivityDTO();
		isContentInUse=McUtils.isContentInUse(mcContent);
		logger.debug("isContentInUse:" + isContentInUse);
		if (isContentInUse == true)
		{
		    editActivityDTO.setMonitoredContentInUse(new Boolean(true).toString());
		}
		request.setAttribute(EDIT_ACTIVITY_DTO, editActivityDTO);

        mcMonitoringAction.prepareReflectionData(request, mcContent, mcService, null, false, "All");

	    
		boolean notebookEntriesExist=MonitoringUtil.notebookEntriesExist(mcService, mcContent);
		logger.debug("notebookEntriesExist : " + notebookEntriesExist);
		
		if (notebookEntriesExist)
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    
		    String userExceptionNoToolSessions=(String)mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();
		    logger.debug("userExceptionNoToolSessions : " + userExceptionNoToolSessions);
		    
		    if (userExceptionNoToolSessions.equals("true"))
		    {
		        logger.debug("there are no online student activity but there are reflections : ");
		        request.setAttribute(NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
		    }

		}
		else
		{
		    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
		}
	

	    /* SELECTION_CASE == 2 indicates start up */
	    request.setAttribute(SELECTION_CASE, new Long(2));
	    logger.debug("SELECTION_CASE: " + request.getAttribute(SELECTION_CASE));
	    
	    		
	    /* Default to All for tool Sessions so that all tool sessions' summary information gets displayed when the module starts up */
	    request.setAttribute(CURRENT_MONITORED_TOOL_SESSION, "All");
	    MonitoringUtil.setupAllSessionsData(request, mcContent,mcService);
		
		/**getting instructions screen content from here... */
		mcGeneralMonitoringDTO.setOnlineInstructions(mcContent.getOnlineInstructions());
		mcGeneralMonitoringDTO.setOfflineInstructions(mcContent.getOfflineInstructions());
		
        List attachmentList = mcService.retrieveMcUploadedFiles(mcContent);
        logger.debug("attachmentList: " + attachmentList);
        mcGeneralMonitoringDTO.setAttachmentList(attachmentList);
        mcGeneralMonitoringDTO.setDeletedAttachmentList(new ArrayList());
        /** ...till here **/

        MonitoringUtil.buildMcStatsDTO(request,mcService, mcContent);

		return true;
	}


	/**
	 * ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping, McMonitoringForm mcMonitoringForm)
	 * 
	 * @param request
	 * @param mapping
	 * @param mcMonitoringForm
	 * @return
	 */
	protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping, McMonitoringForm mcMonitoringForm)
	{
		logger.debug("start validating monitoring parameters...");
    	
    	String strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
    	logger.debug("strToolContentId: " + strToolContentId);
    	 
	    if ((strToolContentId == null) || (strToolContentId.length() == 0)) 
	    {
	    	McUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST));
	    }
	    else
	    {
	    	try
			{
	    		long toolContentID=new Long(strToolContentId).longValue();
		    	logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentID));
		    	mcMonitoringForm.setToolContentID(strToolContentId);
			}
	    	catch(NumberFormatException e)
			{
	    		logger.debug("add error.numberFormatException");
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
	public void persistInRequestError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}  
