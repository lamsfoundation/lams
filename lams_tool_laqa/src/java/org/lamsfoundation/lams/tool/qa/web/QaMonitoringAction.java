/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
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
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaStringComparator;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;

public class QaMonitoringAction extends DispatchAction implements QaAppConstants
{
	static Logger logger = Logger.getLogger(QaMonitoringAction.class.getName());

	public static String SELECTBOX_SELECTED_TOOL_SESSION ="selectBoxSelectedToolSession";
	public static Integer READABLE_TOOL_SESSION_COUNT = new Integer(1);
 
	public ActionForward generateToolSessionDataMap(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
	{
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		MonitoringUtil monitoringUtil = new MonitoringUtil();
		logger.debug(logger + " " + this.getClass().getName() +  "calling findSelectedMonitoringTab");
		monitoringUtil.findSelectedMonitoringTab(form, request);
		logger.debug(logger + " " + this.getClass().getName() +  "called findSelectedMonitoringTab");

		/**
		 * load the values for online and offline instructions
		 */
		handleInstructionsScreen(mapping, form, request);
		IQaService qaService =QaUtils.getToolService(request);
		Long initialMonitoringContentId=(Long)request.getSession().getAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID);
		if (initialMonitoringContentId != null)
		{
			QaContent qaContent=qaService.retrieveQa(initialMonitoringContentId.longValue());
			logger.debug(logger + " " + this.getClass().getName() +  "qaContent: " + qaContent);
			request.getSession().setAttribute(MONITORED_OFFLINE_INSTRUCTIONS, qaContent.getOfflineInstructions());
			request.getSession().setAttribute(MONITORED_ONLINE_INSTRUCTIONS, qaContent.getOnlineInstructions());
			logger.debug(logger + " " + this.getClass().getName() +  "session updated with on/off instructions");
		}
		

		/**
		 * determine what screen(tab) to generate
		 */
		String choiceMonitoring=(String)request.getSession().getAttribute(CHOICE_MONITORING);
		logger.debug(logger + " " + this.getClass().getName() +  "CHOICE_MONITORING: " + choiceMonitoring);
		
		if (choiceMonitoring.equalsIgnoreCase(CHOICE_TYPE_MONITORING_SUMMARY))
		{
			logger.debug(logger + " " + this.getClass().getName() +  "will generate summary screen");
			return generateSummaryScreen(mapping, form, request, response);
		}
		else if (choiceMonitoring.equalsIgnoreCase(CHOICE_TYPE_MONITORING_INSTRUCTIONS))
		{
			logger.debug(logger + " " + this.getClass().getName() +  "will generate instructions screen");
			request.getSession().setAttribute(MONITORING_INSTRUCTIONS_VISITED, new Boolean(true));
			return generateInstructionsScreen(mapping, form, request, response);
		}
		else if (choiceMonitoring.equalsIgnoreCase(CHOICE_TYPE_MONITORING_EDITACTIVITY))
		{
			logger.debug(logger + " " + this.getClass().getName() +  "will generate editActivity screen");
			request.getSession().setAttribute(MONITORING_EDITACTIVITY_VISITED, new Boolean(true));
			return generateEditActivityScreen(mapping, form, request, response);
		}
		else if (choiceMonitoring.equalsIgnoreCase(CHOICE_TYPE_MONITORING_STATS))
		{
			logger.debug(logger + " " + this.getClass().getName() +  "will generate stats screen");
			request.getSession().setAttribute(MONITORING_STATS_VISITED, new Boolean(true));
			return generateStatsScreen(mapping, form, request, response);
		}
		
		return null;
	}

	
	public ActionForward generateSummaryScreen(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
	{
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		
		Boolean noToolSessionsAvailable=(Boolean)request.getSession().getAttribute(NO_TOOL_SESSIONS_AVAILABLE);
		logger.debug(logger + " " + this.getClass().getName() +  "generateSummaryScreen has noToolSessionsAvailable: " + noToolSessionsAvailable);
		if ((noToolSessionsAvailable !=null) && (noToolSessionsAvailable.booleanValue()))
		{
			qaMonitoringForm.resetUserAction();
			logger.debug(logger + " " + this.getClass().getName() + "detected noToolSessionsAvailable:" + noToolSessionsAvailable);
			persistError(request,"error.content.onlyContentAndNoSessions");
			request.setAttribute(USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_REPORT);
			return (mapping.findForward(MONITORING_REPORT));	
		}
		
		MonitoringUtil monitoringUtil = new MonitoringUtil();
		String responseId=qaMonitoringForm.getResponseId();
		String hiddenResponseId=qaMonitoringForm.getHiddenResponseId();
		String unHiddenResponseId=qaMonitoringForm.getUnHiddenResponseId();
		
		if (qaMonitoringForm.getEditReport() != null)
		{
			logger.debug(logger + " " + this.getClass().getName() + "responseId for editReport" + responseId);
			request.getSession().setAttribute(DATAMAP_EDITABLE_RESPONSE_ID, responseId);
		}
		else if (qaMonitoringForm.getUpdateReport() != null)
		{
			logger.debug(logger + " " + this.getClass().getName() +  "request for responseUpdate with id: "  
																  + request.getSession().getAttribute(DATAMAP_EDITABLE_RESPONSE_ID));
			responseId=(String)request.getSession().getAttribute(DATAMAP_EDITABLE_RESPONSE_ID);
			String updatedResponse=qaMonitoringForm.getUpdatedResponse();
			monitoringUtil.updateResponse(request, responseId, updatedResponse);
			request.getSession().setAttribute(DATAMAP_EDITABLE_RESPONSE_ID, "");
			logger.debug(logger + " " + this.getClass().getName() +  "end of  updateResponse with: " + responseId + "-" + updatedResponse);
		}
		
		if (qaMonitoringForm.getHideReport() != null)
		{
			logger.debug(logger + " " + this.getClass().getName() + "hiddenResponseId for hideReport" + hiddenResponseId);
			request.getSession().setAttribute(DATAMAP_HIDDEN_RESPONSE_ID, hiddenResponseId);
			monitoringUtil.hideResponse(request, hiddenResponseId);
		}
		
		if (qaMonitoringForm.getUnhideReport() != null)
		{
			logger.debug(logger + " " + this.getClass().getName() + "hiddenResponseId for unHideReport" + unHiddenResponseId);
			request.getSession().setAttribute(DATAMAP_HIDDEN_RESPONSE_ID, "");
			monitoringUtil.unHideResponse(request, unHiddenResponseId);
		}
		
		
		logger.debug(logger + " " + this.getClass().getName() +  "DATAMAP_EDITABLE_RESPONSE_ID: " + 	
																request.getSession().getAttribute(DATAMAP_EDITABLE_RESPONSE_ID));
		
		logger.debug(logger + " " + this.getClass().getName() +  "DATAMAP_HIDDEN_RESPONSE_ID: " + 	
																request.getSession().getAttribute(DATAMAP_HIDDEN_RESPONSE_ID));
		
		qaMonitoringForm.setUpdatedResponse("");
		logger.debug(logger + " " + this.getClass().getName() +  "request for summary");
		
		String isToolSessionChanged=request.getParameter(IS_TOOL_SESSION_CHANGED);
		logger.debug(logger + " " + this.getClass().getName() +  "IS_TOOL_SESSION_CHANGED - before loop: " + isToolSessionChanged);
		
		/**
		 * sessionList holds all the toolSessionIds passed to summary page to be presented in a drop-down box.
		 */
		Map sessionList = new TreeMap();
		int SELECTION_CASE=0;
		
		logger.debug(logger + " " + this.getClass().getName() +  "isNonDefaultScreensVisited: " + monitoringUtil.isNonDefaultScreensVisited(request));
		
		
		/**
		 * Following conditions test which entry has been selected in the monitoring mode-summary screen drop box.
		 * is Default page 
		 */
		String selectionCase=(String)request.getSession().getAttribute("selectionCase");
		logger.debug(logger + " " + this.getClass().getName() +  "current selectionCase: " + selectionCase);
		
		boolean sessionListReadable=false;
		if ((isToolSessionChanged == null) && !monitoringUtil.isNonDefaultScreensVisited(request))
		{
			logger.debug(logger + " " + this.getClass().getName() +  "First case based on null. Gets rendered only once in http session life time");
			logger.debug(logger + " " + this.getClass().getName() +  "summary to use MAX_TOOL_SESSION_COUNT");
			/**
			 * initialize sessionList with "All", which is the default option.
			 */
			sessionList.put("All", "All");
			READABLE_TOOL_SESSION_COUNT=MAX_TOOL_SESSION_COUNT;
			SELECTION_CASE=1;
		}
		else if ((isToolSessionChanged == null) && monitoringUtil.isNonDefaultScreensVisited(request))
		{
			logger.debug(logger + " " + this.getClass().getName() +  "Other tabs visited. Gets rendered for all tool sessions");
			sessionList.put("All", "All");
			READABLE_TOOL_SESSION_COUNT=MAX_TOOL_SESSION_COUNT;
			SELECTION_CASE=2;
			sessionListReadable=true;
		}
		else if ((isToolSessionChanged != null) && isToolSessionChanged.equalsIgnoreCase("1"))
		{
			String selectedToolSessionId=(String)request.getParameter("toolSessionId1");
			logger.debug(logger + " " + this.getClass().getName() +  "selectedToolSessionId" + selectedToolSessionId);
			/**
			 * is "All" selected 
			 */
			if (selectedToolSessionId.equalsIgnoreCase("All"))
			{
				logger.debug(logger + " " + this.getClass().getName() +  "Second case");
				logger.debug(logger + " " + this.getClass().getName() +  "summary to use MAX_TOOL_SESSION_COUNT");
				/**
				 * initialize sessionList with "All", which is the default option.
				 */
				sessionList.put("All", "All");
				READABLE_TOOL_SESSION_COUNT=MAX_TOOL_SESSION_COUNT;
				SELECTION_CASE=2;
				sessionListReadable=true;
			}
			else
			{
				/**
				 * is a single session id selected 
				 */
				logger.debug(logger + " " + this.getClass().getName() +  "Third case");
				logger.debug(logger + " " + this.getClass().getName() +  "selectedToolSessionId" + selectedToolSessionId);
				READABLE_TOOL_SESSION_COUNT=new Integer(2);
				SELECTION_CASE=3;
				request.getSession().setAttribute(CURRENT_MONITORED_TOOL_SESSION,selectedToolSessionId); 
				logger.debug(logger + " " + this.getClass().getName() +  "CURRENT_MONITORED_TOOL_SESSION " + selectedToolSessionId);
			}
		}
		else if (isToolSessionChanged.equals("")  && (selectionCase.equals("3"))) 
		{
			/**
			 * is a single session id selected 
			 */
			logger.debug(logger + " " + this.getClass().getName() +  "case with single session and edit or update selected");
			READABLE_TOOL_SESSION_COUNT=new Integer(2);
			SELECTION_CASE=3;
		}
		else if (isToolSessionChanged.equals("")) 
		{
			logger.debug(logger + " " + this.getClass().getName() +  "All is selected");
			sessionList.put("All", "All");
			READABLE_TOOL_SESSION_COUNT=MAX_TOOL_SESSION_COUNT;
			SELECTION_CASE=2;
			sessionListReadable=true;
		}
		logger.debug(logger + " " + this.getClass().getName() +  "SELECTION_CASE: " + SELECTION_CASE);
		
		if ((qaMonitoringForm.getEditReport() == null) && (qaMonitoringForm.getUpdateReport() == null))
		{
			logger.debug(logger + " " + this.getClass().getName() +  "no editReport or updateReport selected");
			request.getSession().setAttribute(DATAMAP_EDITABLE_RESPONSE_ID, "");
			request.getSession().setAttribute("selectionCase",new Long(SELECTION_CASE).toString());
		}
		qaMonitoringForm.resetUserAction();
		
		/**
		 * holds all the toolSessionIds passed in the form toolSessionId1, toolSessionId2 etc.
		 */
		Map mapToolSessions= new TreeMap(new QaStringComparator());
		request.getSession().setAttribute(MAP_TOOL_SESSIONS,mapToolSessions);
		logger.debug(logger + " " + this.getClass().getName() +  "MAP_TOOL_SESSIONS placed into session");
		
		Map mapUserResponses= new TreeMap(new QaStringComparator());
		request.getSession().setAttribute(MAP_USER_RESPONSES,mapUserResponses);
		logger.debug(logger + " " + this.getClass().getName() +  "MAP_USER_RESPONSES placed into session");
		logger.debug(logger + " " + this.getClass().getName() +  "request for contributeLesson");
		/**
		 * monitoring reads all the toolSessionsIds appended one after other until it finds a null one. The cap was limited to 500.
		 * This is the case all the tool sessions are using the same content.
		 * 
		 * The final Map mapToolSessions holds the Map mapQuestions which in turn holds the Map mapUserResponses.
		 * The key of  mapToolSessions:  incremental numbers
		 * The key of  mapQuestions:  	 questions themselves
		 * The key of  mapUserResponses: incremental numbers
		 * 
		 */
	
		IQaService qaService = QaUtils.getToolService(request);
	    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaService: " + qaService);
	    
	    if (qaService == null)
	    {
	    	qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaService from proxy: " + qaService);
		    request.getSession().setAttribute(TOOL_SERVICE, qaService);	
	    }
	    
	    request.getSession().setAttribute(NO_AVAILABLE_SESSIONS,new Boolean(false));
	    logger.debug(logger + " " + this.getClass().getName() +  "NO_AVAILABLE_SESSIONS: " + false);
	    
	    logger.debug(logger + " " + this.getClass().getName() +  "retrieving ORIGINAL_TOOL_SESSIONS");
	    Map originalSessionList=(Map)request.getSession().getAttribute(ORIGINAL_TOOL_SESSIONS);
	    logger.debug(logger + " " + this.getClass().getName() +  "retrieved ORIGINAL_TOOL_SESSIONS : " + originalSessionList);
		/**
		 *  monitoredToolSessionsCounter holds the total number of valid toolSessionIds passed to the monitoring url
		 */
		logger.debug(logger + " " + this.getClass().getName() +  "READABLE_TOOL_SESSION_COUNT: " + READABLE_TOOL_SESSION_COUNT);
		int monitoredToolSessionsCounter=0;
		for (int toolSessionIdCounter=1; toolSessionIdCounter < READABLE_TOOL_SESSION_COUNT.intValue(); toolSessionIdCounter++)
		{
			logger.debug(logger + " " + this.getClass().getName() +  "toolSessionIdCounter: " + toolSessionIdCounter);
			String strToolSessionId=(String) originalSessionList.get(""+toolSessionIdCounter);
			logger.debug(logger + " " + this.getClass().getName() +  "original strToolSessionId: " + strToolSessionId);
					    
		    String strRetrievableToolSessionId="";
		    /**
		     * catering for un-formatted monitoring url
		     * Watch for case where the "All" is selected in the drop-down.
		     */
		    logger.debug(logger + " " + this.getClass().getName() +  "SELECTION_CASE: " + SELECTION_CASE);
		    if ((strToolSessionId == null) && (SELECTION_CASE == 1))
		    {
		    	logger.debug(logger + " " + this.getClass().getName() +  "un-formatted monitoring url, exiting...");
		    	break;
		    }
		    else if  ((!sessionListReadable) &&  ((strToolSessionId == null) || (strToolSessionId.length() == 0)))
			{
		    	logger.debug(logger + " " + this.getClass().getName() +  "un-formatted monitoring url, exiting...");
		    	break;
			}
		    else
		    {
		    	if (sessionListReadable)
		    	{
		    		logger.debug(logger + " " + this.getClass().getName() +  "sessionListReadable is true.");
		    		logger.debug(logger + " " + this.getClass().getName() +  "strToolSessionId is All: " + strToolSessionId);
		    		sessionList=(Map)request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS);
		    		logger.debug(logger + " " + this.getClass().getName() +  "toolSessionIdCounter: " + toolSessionIdCounter);
		    		logger.debug(logger + " " + this.getClass().getName() +  "sessionList size: " + sessionList.size());
		    		
		    		if (toolSessionIdCounter==sessionList.size())
		    		{
		    			logger.debug(logger + " " + this.getClass().getName() +  "sessionList size equals toolSessionIdCounter, exiting...");
		    			break;
		    		}
		    			
		    		logger.debug(logger + " " + this.getClass().getName() +  "sessionList: " + sessionList);
		    		strToolSessionId=(String)sessionList.get("Group" + toolSessionIdCounter);
		    		logger.debug(logger + " " + this.getClass().getName() +  "strToolSessionId from sessionList: " + strToolSessionId);
		    	}
		    	
		    	strRetrievableToolSessionId=strToolSessionId;
		    	logger.debug(logger + " " + this.getClass().getName() +  "retrievableStrToolSessionId: " + strRetrievableToolSessionId);
		    	
		    	QaSession qaSession=qaService.retrieveQaSessionOrNullById(new Long(strRetrievableToolSessionId).longValue());
	    	    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaSession: " + qaSession);
	    	    
	    	    if (qaSession !=null)
	    	    {
	    	    	monitoredToolSessionsCounter++;
	    	    	request.getSession().setAttribute(TOOL_SESSION_ID, new Long(strToolSessionId));
		    	    logger.debug(logger + " " + this.getClass().getName() +  "TOOL_SESSION_ID in session");
		    	    
		    		if (READABLE_TOOL_SESSION_COUNT.equals(MAX_TOOL_SESSION_COUNT))
		    		{
		    			logger.debug(logger + " " + this.getClass().getName() +  "default screen - READABLE_TOOL_SESSION_COUNT equals MAX_TOOL_SESSION_COUNT");
		    	    	/**
			    	     * add the current toolSessionId to the arraylist for the drop-down box
			    	     */
			    	    sessionList.put("Group" + monitoredToolSessionsCounter, strToolSessionId);
			    	    logger.debug(logger + " " + this.getClass().getName() +  "sessionList Map new entry, strToolSessionId added to the list: " + toolSessionIdCounter + "->" + strToolSessionId );	
		    		}
		    	    
		    	    /**
		    	     * get to content from the tool session
		    	     */
		    		QaContent qaContent=qaSession.getQaContent();
		    	    logger.debug(logger + " " + this.getClass().getName() +  "using qaContent: " + qaContent);
		    	    logger.debug(logger + " " + this.getClass().getName() +  "Monitor - contribute will be using TOOL_CONTENT_ID: " + qaContent.getQaContentId());
		    	    
		    	    /**
		    	     * editActivity-defineLater screen depends on MONITORED_CONTENT_ID
		    	     */
		    	    request.getSession().setAttribute(MONITORED_CONTENT_ID,qaContent.getQaContentId());
		    	    
		    	    /**
		    	     * place it into TOOL_CONTENT_ID session attribute since learningUtil.buidLearnerReport(request) depends on it
		    	     * to generate a report
		    	     */
		    	    request.getSession().setAttribute(TOOL_CONTENT_ID,qaContent.getQaContentId());
		    	    
		    	    /**
		    	     * this is to convince jsp although usernameVisible applies only to learning mode
		    	     */
		    	    request.getSession().setAttribute(IS_USERNAME_VISIBLE, new Boolean(true));
		    	    
		    	    logger.debug(logger + " " + this.getClass().getName() +  "REPORT_TITLE_MONITOR: " + qaContent.getMonitoringReportTitle());
		    	    request.getSession().setAttribute(REPORT_TITLE_MONITOR,qaContent.getMonitoringReportTitle());
		    	    
		    	    
		    	    /**
		    	     * check if the author requires that the all tool sessions must be COMPLETED before the report in Monitoring
		    	     */
		    	    boolean isAllSessionsCompleted=monitoringUtil.isSessionsSync(request,qaContent.getQaContentId().longValue());
		    	    logger.debug(logger + " " + this.getClass().getName() +  "Monitor - contribute will be using isAllSessionsCompleted: " + isAllSessionsCompleted);
		    	    
		    	    logger.debug(logger + " " + this.getClass().getName() +  "Monitor - contribute will be using isSynchInMonitor: " + qaContent.isSynchInMonitor());
		    	    /**
		    	     * if the author requires syncronization but not all the sessions are COMPLETED give an error
		    	     */
		    	    if (qaContent.isSynchInMonitor() && (!isAllSessionsCompleted))
		    	    {
		    	    	request.getSession().setAttribute(CHECK_ALL_SESSIONS_COMPLETED, new Boolean(true));
		    	    	request.getSession().setAttribute(IS_ALL_SESSIONS_COMPLETED, new Boolean(isAllSessionsCompleted));
	
		    	    	ActionMessages errors= new ActionMessages();
	            		errors.add(Globals.ERROR_KEY, new ActionMessage("error.synchInMonitor"));
	        			logger.debug(logger + " " + this.getClass().getName() +  "add synchInMonitor to ActionMessages: ");
	        			saveErrors(request,errors);	    	    	
		    	    }
		    	    else
		    	    {
		    	    	request.getSession().setAttribute(CHECK_ALL_SESSIONS_COMPLETED, new Boolean(false));
		    	    }
		    	    logger.debug(logger + " " + this.getClass().getName() + "IS_ALL_SESSIONS_COMPLETED:" + request.getSession().getAttribute(IS_ALL_SESSIONS_COMPLETED));
		    	    logger.debug(logger + " " + this.getClass().getName() + "CHECK_ALL_SESSIONS_COMPLETED" + request.getSession().getAttribute(CHECK_ALL_SESSIONS_COMPLETED));
		    	    		
		    	    LearningUtil learningUtil= new LearningUtil();
		    		/**
		    		 * generate a report for the Author/Teacher
		    		 */
		    	    logger.debug(logger + " " + this.getClass().getName() + "calling buidMonitoringReport with toolSessionIdCounter:" + toolSessionIdCounter);
		            learningUtil.buidLearnerReport(request, toolSessionIdCounter);
	    	    }
		    }
		}
		
			/**
			 * store the arrayList in the session
			 */
			if (READABLE_TOOL_SESSION_COUNT.equals(MAX_TOOL_SESSION_COUNT))
			{
				logger.debug(logger + " " + this.getClass().getName() + "sessionList storable");
				request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS,sessionList);
				logger.debug(logger + " " + this.getClass().getName() + "SUMMARY_TOOL_SESSIONS stored into the session:" + request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS));
			}
			
				mapToolSessions=(Map)request.getSession().getAttribute(MAP_TOOL_SESSIONS);
				logger.debug(logger + " " + this.getClass().getName() + "before forwarding MAP_TOOL_SESSIONS:" + mapToolSessions);
				
				if (mapToolSessions.size() == 0)
				{
					request.getSession().setAttribute(NO_AVAILABLE_SESSIONS,new Boolean(true));
				    logger.debug(logger + " " + this.getClass().getName() +  "NO_AVAILABLE_SESSIONS: " +true);
				    ActionMessages errors= new ActionMessages();
            		errors.add(Globals.ERROR_KEY, new ActionMessage("error.noStudentActivity"));
        			logger.debug(logger + " " + this.getClass().getName() +  "add error.noStudentActivity to ActionMessages");
            		saveErrors(request,errors);
				}
				
				Boolean noAvailableSessions=(Boolean) request.getSession().getAttribute(NO_AVAILABLE_SESSIONS);
				logger.debug(logger + " " + this.getClass().getName() + "before forwarding NO_AVAILABLE_SESSIONS:" + noAvailableSessions);
				
				Map mapMonitoringQuestions=(Map)request.getSession().getAttribute(MAP_MONITORING_QUESTIONS);
				logger.debug(logger + " " + this.getClass().getName() + "before forwarding MAP_MONITORING_QUESTIONS:" + mapMonitoringQuestions);
				
				String targetMode=(String )request.getSession().getAttribute(TARGET_MODE);
				logger.debug(logger + " " + this.getClass().getName() +  "TARGET_MODE: " + targetMode);
				return (mapping.findForward(MONITORING_REPORT));
	}


	public ActionForward generateInstructionsScreen(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
	{
		logger.debug(logger + " " + this.getClass().getName() +  "will generateInstructionsScreen ");
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		
		Long initialMonitoringContentId=(Long)request.getSession().getAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID);
		if (initialMonitoringContentId == null) 
		{
			logger.debug(logger + " " + this.getClass().getName() +  "missing content id:");
			persistError(request,"error.tab.contentId.required");
			request.setAttribute(USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_ERROR);
			return (mapping.findForward(MONITORING_REPORT));		
		}
		
		qaMonitoringForm.resetUserAction();
		return (mapping.findForward(MONITORING_REPORT));
	}


	public ActionForward generateEditActivityScreen(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
	{
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		
		request.getSession().setAttribute(CONTENT_LOCKED, new Boolean(false));
		IQaService qaService=(IQaService)request.getSession().getAttribute(TOOL_SERVICE);
	    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaService: " + qaService);
		
		Long toolContentId=(Long)request.getSession().getAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID);
	    logger.debug(logger + " " + this.getClass().getName() +  "toolContentId: " + toolContentId);
		
		if (toolContentId == null)
		{
		/**
		*  toolContentId is not available from the toolSessions passed to the monitoring url.
		*  in this case,  toolContentId must have been passed separetely 
		*/
			Long monitoredContentId=(Long)request.getSession().getAttribute(MONITORED_CONTENT_ID);
			logger.debug(logger + " " + this.getClass().getName() +  "will generateEditActivityScreen: " + monitoredContentId);
			    
	    	    if (monitoredContentId == null)
	    	    {
	    	    	throw new QaApplicationException("Exception occured: " +
	    	    			"Tool expects a legitimate TOOL_CONTENT_ID from the container since it is also not available from the toolSession(s) passed. Can't continue!");
	    	    }
				toolContentId=monitoredContentId;
		}
		request.getSession().setAttribute(MONITORED_CONTENT_ID, toolContentId);
		
		logger.debug(logger + " " + this.getClass().getName() +  "will use monitoredContentId: " + toolContentId);
		QaContent qaContent=qaService.loadQa(toolContentId.longValue());
		logger.debug(logger + " " + this.getClass().getName() +  "will use qaContent: " + qaContent);
		
		request.getSession().setAttribute(IS_MONITORING_DEFINE_LATER, new Boolean(qaContent.isDefineLater()));
		logger.debug(logger + " " + this.getClass().getName() +  "IS_MONITORING_DEFINE_LATER: " + request.getSession().getAttribute(IS_MONITORING_DEFINE_LATER));
		
		logger.debug(logger + " " + this.getClass().getName() +  "calling studentActivityOccurredGlobal with: " + qaContent);
		boolean studentActivity=qaService.studentActivityOccurredGlobal(qaContent);
		logger.debug(logger + " " + this.getClass().getName() +  "studentActivity on content: " + studentActivity);
		
		qaMonitoringForm.resetUserAction();
		if (studentActivity == false)
		{
			/**
			 * forward to Authoring Basic tab
			 */
			QaStarterAction qaStarterAction = new QaStarterAction();
			QaAuthoringForm qaAuthoringForm = new QaAuthoringForm();
			logger.debug(logger + " " + this.getClass().getName() +  "forward to Authoring Basic tab ");
			ActionForward actionForward=qaStarterAction.startMonitoringSummary(mapping, qaAuthoringForm, request, response);
			logger.debug(logger + " " + this.getClass().getName() +  "actionForward: " + actionForward);
			return (actionForward);
		}
		else
		{
			logger.debug(logger + " " + this.getClass().getName() +  "forward to warning screen as the content is not allowed to be modified.");
			ActionMessages errors= new ActionMessages();
			errors.add(Globals.ERROR_KEY, new ActionMessage("error.content.inUse"));
			saveErrors(request,errors);
			request.getSession().setAttribute(CONTENT_LOCKED, new Boolean(true));
			request.setAttribute(START_MONITORING_SUMMARY_REQUEST, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forward to:" + LOAD);
			return (mapping.findForward(LOAD));
		}
	}
	
	
	public ActionForward generateStatsScreen(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
	{
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		Boolean noToolSessionsAvailable=(Boolean)request.getSession().getAttribute(NO_TOOL_SESSIONS_AVAILABLE);
		if ((noToolSessionsAvailable !=null) && (noToolSessionsAvailable.booleanValue()))
		{
			qaMonitoringForm.resetUserAction();
			logger.debug(logger + " " + this.getClass().getName() + "detected noToolSessionsAvailable:" + noToolSessionsAvailable);
			persistError(request,"error.content.onlyContentAndNoSessions");
			request.setAttribute(USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS, new Boolean(true));
			logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_ERROR);
			return (mapping.findForward(MONITORING_REPORT));	
		}
		
		IQaService qaService=(IQaService)request.getSession().getAttribute(TOOL_SERVICE);
	    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaService: " + qaService);
	    
	    Map mapStats= new TreeMap(new QaStringComparator());
		request.getSession().setAttribute(MAP_STATS,mapStats);
		
		Map sessionList=(Map)request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS);
		for (int toolSessionIdCounter=1; toolSessionIdCounter < READABLE_TOOL_SESSION_COUNT.intValue(); toolSessionIdCounter++)
		{
			String strToolSessionId=(String)sessionList.get("Group" + toolSessionIdCounter);
			logger.debug(logger + " " + this.getClass().getName() +  "strToolSessionId from http session: " + strToolSessionId);
			if ((strToolSessionId != null) && (strToolSessionId.length() > 0))
			{
		    	QaSession qaSession=qaService.retrieveQaSessionOrNullById(new Long(strToolSessionId).longValue());
	    	    logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaSession: " + qaSession);
	    	    if (qaSession != null)
	    	    {
	    	    	logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaSession: " + qaSession);
	    	    	int countSessionUser=qaService.countSessionUser(qaSession);
	    	    	logger.debug(logger + " " + this.getClass().getName() +  "countSessionUser: " + countSessionUser);
	    	    	mapStats.put(strToolSessionId, new Integer(countSessionUser).toString());
	    	    	request.getSession().setAttribute(MAP_STATS,mapStats);
	    	    }
				
			}
		}
		mapStats=(Map)request.getSession().getAttribute(MAP_STATS);
		logger.debug(logger + " " + this.getClass().getName() +  "final MAP_STATS: " + mapStats);
		qaMonitoringForm.resetUserAction();
		return (mapping.findForward(MONITORING_REPORT));
	}

	
	/**
	 * markDataMapAsEditable(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * 
	 * 
	 * marks the dataMap so that jsp renders the summary screen  as editable
	 */
	public ActionForward markDataMapAsEditable(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
	{
		request.getSession().setAttribute(DATAMAP_EDITABLE, new Boolean(true));
		logger.debug(logger + " " + this.getClass().getName() +  "will generate editable summary screen");
		return generateSummaryScreen(mapping, form, request, response);
	}
	
	
	public ActionForward handleInstructionsScreen(ActionMapping mapping,ActionForm form, HttpServletRequest request)
	{
		/**
		 * update online and offline instuctions content if there is a request.
		 */
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		IQaService qaService =QaUtils.getToolService(request);
		if (qaMonitoringForm.getSubmitMonitoringInstructions() != null)
		{
			logger.debug(logger + " " + this.getClass().getName() +  "request for prosssing Monitoring instructions :");
			qaMonitoringForm.setSubmitMonitoringInstructions(null);
			
			logger.debug(logger + " " + this.getClass().getName() +  "online instructions :" + qaMonitoringForm.getOnlineInstructions());
			logger.debug(logger + " " + this.getClass().getName() +  "offline instructions :" + qaMonitoringForm.getOfflineInstructions());
			Long initialMonitoringContentId=(Long)request.getSession().getAttribute(INITIAL_MONITORING_TOOL_CONTENT_ID);
			if (initialMonitoringContentId == null) 
			{
				logger.debug(logger + " " + this.getClass().getName() +  "missing content id:");
				persistError(request,"error.tab.contentId.required");
				request.setAttribute(USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED, new Boolean(true));
				logger.debug(logger + " " + this.getClass().getName() +  "forwarding to: " + MONITORING_ERROR);
				return (mapping.findForward(MONITORING_REPORT));		
			}
			else
			{
				/**
				 * update the content
				 */
				logger.debug(logger + " " + this.getClass().getName() +  "content id: " +  initialMonitoringContentId);
				QaContent qaContent=qaService.retrieveQa(initialMonitoringContentId.longValue());
				logger.debug(logger + " " + this.getClass().getName() +  "qaContent: " + qaContent);
				qaContent.setOnlineInstructions(qaMonitoringForm.getOnlineInstructions());
				qaContent.setOfflineInstructions(qaMonitoringForm.getOfflineInstructions());
				qaService.updateQa(qaContent);
				logger.debug(logger + " " + this.getClass().getName() +  "qaContent updated in the db");
				request.getSession().setAttribute(MONITORED_OFFLINE_INSTRUCTIONS, qaContent.getOfflineInstructions());
				request.getSession().setAttribute(MONITORED_ONLINE_INSTRUCTIONS, qaContent.getOnlineInstructions());
				logger.debug(logger + " " + this.getClass().getName() +  "session updated with on/off instructions");
				request.setAttribute(MONITORING_INSTRUCTIONS_UPDATE_MESSAGE, new Boolean(true));
				return (mapping.findForward(MONITORING_REPORT));
			}
		}
		logger.debug(logger + " " + this.getClass().getName() +  "end of online-offline instructions content handling");
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
		logger.debug(logger + " " + this.getClass().getName() +  "add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}

}