/****************************************************************
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.Iterator;
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
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.web.util.AttributeNames;


/**
 * 
 * @author Ozgur Demirtas
 * starts up the monitoring module
 * 
 *  <action
		path="/monitoringStarter"
		type="org.lamsfoundation.lams.tool.qa.web.QaMonitoringStarterAction"
		name="QaMonitoringForm"
		scope="session"
		parameter="method"
		unknown="false"
		validate="false">
		
		<exception
			key="error.exception.QaApplication"
			type="org.lamsfoundation.lams.tool.qa.QaApplicationException"
			handler="org.lamsfoundation.lams.tool.qa.web.CustomStrutsExceptionHandler"
			path="/SystemErrorContent.jsp"
			scope="request"
		/>
		    
		<exception
		    key="error.exception.QaApplication"
		    type="java.lang.NullPointerException"
		    handler="org.lamsfoundation.lams.tool.qa.web.CustomStrutsExceptionHandler"
		    path="/SystemErrorContent.jsp"
		    scope="request"
		/>	         			
		
	  	<forward
		    name="loadMonitoring"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="true"
	  	/>
	      
   	  	<forward
		    name="errorList"
		    path="/QaErrorBox.jsp"
		    redirect="true"
	  	/>
	</action>

 *
 */

public class QaMonitoringStarterAction extends Action implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaMonitoringStarterAction.class.getName());

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, QaApplicationException 
	{
		logger.debug("init QaMonitoringStarterAction...");
		QaUtils.cleanUpSessionAbsolute(request);
		
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
		
		QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		logger.debug("qaMonitoringForm: " + qaMonitoringForm);
		qaMonitoringForm.setCurrentTab("1");
		logger.debug("setting current tab to 1: ");
		
		
		request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
		
		QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
		logger.debug("calling initSummaryContent.");
		qaMonitoringAction.initSummaryContent(mapping, form, request, response);
		logger.debug("calling initInstructionsContent.");
		qaMonitoringAction.initInstructionsContent(mapping, form, request, response);
		logger.debug("calling initStatsContent.");
		qaMonitoringAction.initStatsContent(mapping, form, request, response);
		

		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		
		/* we have made sure TOOL_CONTENT_ID is passed  */
	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    QaContent qaContent=qaService.loadQa(toolContentId.longValue());
	    
		logger.debug("existing qaContent:" + qaContent);
		Map mapQuestionContent= new TreeMap(new QaComparator());
		logger.debug("mapQuestionContent: " + mapQuestionContent);
	    /*
		 * get the existing question content
		 */
		logger.debug("setting existing content data from the db");
		mapQuestionContent.clear();
		Iterator queIterator=qaContent.getQaQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapQuestionContent: " + mapQuestionContent);
		while (queIterator.hasNext())
		{
			QaQueContent qaQueContent=(QaQueContent) queIterator.next();
			if (qaQueContent != null)
			{
				logger.debug("question: " + qaQueContent.getQuestion());
	    		mapQuestionContent.put(mapIndex.toString(),qaQueContent.getQuestion());
	    		/**
	    		 * make the first entry the default(first) one for jsp
	    		 */
	    		if (mapIndex.longValue() == 1)
	    			request.getSession().setAttribute(DEFAULT_QUESTION_CONTENT, qaQueContent.getQuestion());
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		logger.debug("Map initialized with existing contentid to: " + mapQuestionContent);
		logger.debug("callling presentInitialUserInterface for the existing content.");
		
		request.getSession().setAttribute(MAP_QUESTION_CONTENT, mapQuestionContent);
		logger.debug("execute initialized the Comparable Map: " + request.getSession().getAttribute("mapQuestionContent") );
		
		/*true means there is at least 1 response*/
		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(false).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to false");
		}
		else
		{
			request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
			logger.debug("USER_EXCEPTION_NO_TOOL_SESSIONS is set to true");
		}

		
		request.getSession().setAttribute(ACTIVE_MODULE, MONITORING);
		qaMonitoringForm.setActiveModule(MONITORING);
		return (mapping.findForward(LOAD_MONITORING));	
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
		logger.debug("start initializing  monitoring data...");
		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		request.getSession().setAttribute(TOOL_SERVICE, qaService);
		
		request.getSession().setAttribute(CURRENT_MONITORING_TAB, "summary");
		request.getSession().setAttribute(EDIT_RESPONSE, new Boolean(false));
		
		request.getSession().setAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS, new Boolean(true).toString());
				/*
	     * persist time zone information to session scope. 
	     */
	    QaUtils.persistTimeZone(request);
		
		/* we have made sure TOOL_CONTENT_ID is passed  */
	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    QaContent qaContent=qaService.loadQa(toolContentId.longValue());
		logger.debug("existing qaContent:" + qaContent);
		
		if (qaContent == null)
		{
			QaUtils.cleanUpSessionAbsolute(request);
			request.getSession().setAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST, new Boolean(true).toString());
			persistError(request, "error.content.doesNotExist");
			return false;
		}
		
		
		
		if (qaContent.getTitle() == null)
		{
			request.getSession().setAttribute(ACTIVITY_TITLE, "Questions and Answers");
			request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, "Please answer the questions.");
		}
		else
		{
			request.getSession().setAttribute(ACTIVITY_TITLE, qaContent.getTitle());
			request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, qaContent.getInstructions());			
		}

		
		if (qaService.studentActivityOccurred(qaContent))
		{
			QaUtils.cleanUpSessionAbsolute(request);
			logger.debug("student activity occurred on this content:" + qaContent);
			request.getSession().setAttribute(USER_EXCEPTION_CONTENT_IN_USE, new Boolean(true).toString());
		}
		
		QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
		logger.debug("refreshing summary data...");
		qaMonitoringAction.refreshSummaryData(request, qaContent, qaService, true, false, null, null);
		
		logger.debug("refreshing stats data...");
		qaMonitoringAction.refreshStatsData(request);
		
		logger.debug("refreshing instructions data...");
		qaMonitoringAction.refreshInstructionsData(request, qaContent);
		
		logger.debug("populating online and ofline files data for intructions tab");
		QaUtils.populateUploadedFilesData(request, qaContent, qaService);

		
		boolean isContentInUse=QaUtils.isContentInUse(qaContent);
		logger.debug("isContentInUse:" + isContentInUse);
		
		request.getSession().setAttribute(IS_MONITORED_CONTENT_IN_USE, new Boolean(false).toString());
		if (isContentInUse == true)
		{
			logger.debug("monitoring url does not allow editActivity since the content is in use.");
	    	persistError(request,"error.content.inUse");
	    	request.getSession().setAttribute(IS_MONITORED_CONTENT_IN_USE, new Boolean(true).toString());
		}

		logger.debug("final IS_MONITORED_CONTENT_IN_USE: " + request.getSession().getAttribute(IS_MONITORED_CONTENT_IN_USE));
	    logger.debug("end initializing  monitoring data...");
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
	    	persistError(request, "error.contentId.required");
	    	QaUtils.cleanUpSessionAbsolute(request);
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
	    		persistError(request, "error.contentId.numberFormatException");
	    		logger.debug("add error.contentId.numberFormatException to ActionMessages.");
	    		QaUtils.cleanUpSessionAbsolute(request);
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
