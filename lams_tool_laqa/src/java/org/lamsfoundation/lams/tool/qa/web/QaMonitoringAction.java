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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
 /**
 * @author Ozgur Demirtas
 * 
 * 	 <action
	      path="/monitoring"
	      type="org.lamsfoundation.lams.tool.qa.web.QaMonitoringAction"
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
          name="load"
          path="/AuthoringMaincontent.jsp"
          redirect="false"
        />
	  	
	  	<forward
          name="loadViewOnly"
          path="/authoring/AuthoringTabsHolder.jsp"
          redirect="false"
        />
	  	
	    <forward
	        name="starter"
	        path="/index.jsp"
	        redirect="true"
	     />
	      
   	  	<forward
		    name="errorList"
		    path="/QaErrorBox.jsp"
		    redirect="true"
	  	/>
	</action>

 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.tool.qa.util.QAConstants;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.util.AttributeNames;


public class QaMonitoringAction extends LamsDispatchAction implements QaAppConstants
{
	static Logger logger = Logger.getLogger(QaMonitoringAction.class.getName());

	public static String SELECTBOX_SELECTED_TOOL_SESSION ="selectBoxSelectedToolSession";
	public static Integer READABLE_TOOL_SESSION_COUNT = new Integer(1);
 
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException, ToolException{
    	logger.debug("dispatching unspecified...");
	 	return null;
    }
        
    
	/**
	 * switches to Stats tab of the Monitoring url
	 * getStats(ActionMapping mapping,
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
	 */
    public ActionForward getStats(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching getStats..." + request);
    	
    	initStatsContent(mapping, form, request, response);
    	return (mapping.findForward(LOAD_MONITORING));
	}

    
    
    public void initStatsContent(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("starting  initStatsContent...");
    	logger.debug("dispatching getStats..." + request);
    	
    	IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("qaService: " + qaService);
		if (qaService == null)
		{
			logger.debug("will retrieve qaService");
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
			logger.debug("retrieving qaService from session: " + qaService);
		}

	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    QaContent qaContent=qaService.loadQa(toolContentId.longValue());
		logger.debug("existing qaContent:" + qaContent);
		
		
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

    	refreshStatsData(request);
    	request.getSession().setAttribute(EDIT_RESPONSE, new Boolean(false));
    	
    	request.getSession().setAttribute(CURRENT_MONITORING_TAB, "stats");
    	logger.debug("ending  initStatsContent...");
	}

    
    
    
    /**
     * switches to instructions tab of the monitoring url.
     * getInstructions(ActionMapping mapping,
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
     */
    public ActionForward getInstructions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching getInstructions..." + request);
    	initInstructionsContent(mapping, form, request, response);

    	return (mapping.findForward(LOAD_MONITORING));
	}

    
    public void initInstructionsContent(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("starting initInstructionsContent...");
    	logger.debug("dispatching getInstructions..." + request);

    	IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("qaService: " + qaService);
		if (qaService == null)
		{
			logger.debug("will retrieve qaService");
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
			logger.debug("retrieving qaService from session: " + qaService);
		}

	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    QaContent qaContent=qaService.loadQa(toolContentId.longValue());
		logger.debug("existing qaContent:" + qaContent);
		
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
		
    	refreshInstructionsData(request, qaContent);
    	request.getSession().setAttribute(EDIT_RESPONSE, new Boolean(false));

    	request.getSession().setAttribute(CURRENT_MONITORING_TAB, "instructions");
    	logger.debug("ending  initInstructionsContent...");
	 	
	}

    /**
     * activates editActivity screen
     * ActionForward editActivity(ActionMapping mapping,
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
     */
    public ActionForward editActivity(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching editActivity...");
    	
    	IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("qaService: " + qaService);
		if (qaService == null)
		{
			logger.debug("will retrieve qaService");
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
			logger.debug("retrieving qaService from session: " + qaService);
		}
	    request.getSession().setAttribute(TOOL_SERVICE, qaService);
	 	request.getSession().setAttribute(CURRENT_MONITORING_TAB, "editActivity");
 		
		QaStarterAction qaStarterAction= new QaStarterAction();

		Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    	    
	    request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
		/* it is possible that the content is being used by some learners. In this situation, the content  is marked as "in use" and 
		   content in use is not modifiable*/ 
		QaContent qaContent=qaService.loadQa(toolContentId.longValue());
		logger.debug("qaContent:" + qaContent);
		
		if (qaService.studentActivityOccurredGlobal(qaContent))
		{
			logger.debug("student activity occurred on this content:" + qaContent);
			request.getSession().setAttribute(USER_EXCEPTION_CONTENT_IN_USE, new Boolean(true).toString());
			logger.debug("forwarding to: " + LOAD_MONITORING);
			return (mapping.findForward(LOAD_MONITORING));
		}
		request.getSession().setAttribute(ACTIVITY_TITLE, qaContent.getTitle());
	    request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS, qaContent.getInstructions());
		
	    return qaStarterAction.executeDefineLater(mapping, form, request, response, qaService);
	}

    
    
    /**
     * switches to summary tab of the monitoring url
     * 
     * getSummary(ActionMapping mapping,
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
     */
    public ActionForward getSummary(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("start  getSummary...");
    	initSummaryContent(mapping, form, request, response);
 		return (mapping.findForward(LOAD_MONITORING));
	}
    

    
    public void initSummaryContent(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("start  initSummaryContent...");
    	
    	logger.debug("dispatching getSummary..." + request);
    	request.getSession().setAttribute(EDIT_RESPONSE, new Boolean(false));
    	
		IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("qaService: " + qaService);
		if (qaService == null)
		{
			logger.debug("will retrieve qaService");
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
			logger.debug("retrieving qaService from session: " + qaService);
		}
		
    	Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    logger.debug("toolContentId: " + toolContentId);
	    
	    QaContent qaContent=qaService.loadQa(toolContentId.longValue());
		logger.debug("existing qaContent:" + qaContent);
		
    	/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
	    logger.debug("SUMMARY_TOOL_SESSIONS: " + request.getSession().getAttribute(SUMMARY_TOOL_SESSIONS));
	    /* ends here. */
	    
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

    	request.getSession().setAttribute(CURRENT_MONITORING_TAB, "summary");
    	logger.debug("end  initSummaryContent...");
	}

    
    public ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
    {
    	logger.debug("dispatching editActivityQuestions...");
    	request.getSession().setAttribute(IS_MONITORED_CONTENT_IN_USE, new Boolean(false).toString());

		request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(true).toString());
		QaUtils.setDefineLater(request, true);
        return (mapping.findForward(LOAD_MONITORING));
    }
    
    public ActionForward addNewQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
	{
    	logger.debug("dispatching proxy editActivityQuestions...");
    	QaAction qaAction= new QaAction(); 
    	return qaAction.addNewQuestion(mapping, form, request, response);
	}

    
    public ActionForward removeQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
    	logger.debug("dispatching proxy removeQuestion...");
    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
		logger.debug("qaMonitoringForm: " + qaMonitoringForm);
		String questionIndex=qaMonitoringForm.getQuestionIndex();
		logger.debug("questionIndex: " + questionIndex);
		request.getSession().setAttribute(REMOVABLE_QUESTION_INDEX, questionIndex);
    	
    	QaAction qaAction= new QaAction(); 
    	return qaAction.removeQuestion(mapping, form, request, response);
    }
    
    public ActionForward submitAllContent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException {
    	logger.debug("dispatching proxy submitAllContent...");
    	request.getSession().setAttribute(ACTIVE_MODULE, DEFINE_LATER);
    	request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(false).toString());
    	QaAction qaAction= new QaAction(); 
    	return qaAction.submitAllContent(mapping, form, request, response);
    	
    }

    /**
     * gets called when the user selects a group from dropdown box in the summary tab 
     * 
     * submitSession(ActionMapping mapping,
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
     */
    public ActionForward submitSession(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching submitSession...");
    	request.getSession().setAttribute(EDIT_RESPONSE, new Boolean(false));
    	
    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
	 	String currentMonitoredToolSession=qaMonitoringForm.getSelectedToolSessionId(); 
	    logger.debug("currentMonitoredToolSession: " + currentMonitoredToolSession);
	    
	    if (currentMonitoredToolSession.equals("All"))
	    {
		    request.getSession().setAttribute(SELECTION_CASE, new Long(2));
	    }
	    else
	    {
	    	/* SELECTION_CASE == 1 indicates a selected group other than "All" */
		    request.getSession().setAttribute(SELECTION_CASE, new Long(1));
	    }
	    logger.debug("SELECTION_CASE: " + request.getSession().getAttribute(SELECTION_CASE));
	    
	    
	    request.getSession().setAttribute(CURRENT_MONITORED_TOOL_SESSION, currentMonitoredToolSession);
	    logger.debug("CURRENT_MONITORED_TOOL_SESSION: " + request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION));
	    
    	return (mapping.findForward(LOAD_MONITORING));	
	}

    
	/**
	 * enables the user to edit responses
	 * ActionForward editResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
    public ActionForward editResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching editResponse...");
    	
    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
	 	
	    String responseId=qaMonitoringForm.getResponseId();
	    logger.debug("responseId: " + responseId);
	    request.getSession().setAttribute(EDIT_RESPONSE, new Boolean(true));
	    request.getSession().setAttribute(EDITABLE_RESPONSE_ID, responseId);
	    
	    refreshUserInput(request);
	    
	    return (mapping.findForward(LOAD_MONITORING));	
	}
    

    /**
     * enables the user to update responses 
     * ActionForward updateResponse(ActionMapping mapping,
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
     */
    public ActionForward updateResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching updateResponse...");

    	IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
    	logger.debug("qaService: " + qaService);

    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
	 	
	    String responseId=qaMonitoringForm.getResponseId();
	    logger.debug("responseId: " + responseId);
	    
	    String updatedResponse=request.getParameter("updatedResponse");
	    logger.debug("updatedResponse: " + updatedResponse);
	    QaUsrResp qaUsrResp= qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	    logger.debug("qaUsrResp: " + qaUsrResp);
	    
	    // write out the audit log entry. If you move this after the update of the response,
	    // then make sure you update the audit call to use a copy of the original answer
	    qaService.getAuditService().logChange(QAConstants.TOOL_SIGNATURE,
	    		qaUsrResp.getQaQueUser().getQueUsrId(),qaUsrResp.getQaQueUser().getUsername(),
	    		qaUsrResp.getAnswer(), updatedResponse);

	    qaUsrResp.setAnswer(updatedResponse);
	    qaService.updateQaUsrResp(qaUsrResp);
	    logger.debug("response updated.");
	    
	    request.getSession().setAttribute(EDIT_RESPONSE, new Boolean(false));
	    
	    refreshUserInput(request);
	    return (mapping.findForward(LOAD_MONITORING));	
	}

    /**
     * enables the user to delete responses
     * ActionForward deleteResponse(ActionMapping mapping,
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
     */
    public ActionForward deleteResponse(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching deleteResponse...");
    	request.getSession().setAttribute(EDIT_RESPONSE, new Boolean(false));

    	IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
    	logger.debug("qaService: " + qaService);

    	QaMonitoringForm qaMonitoringForm = (QaMonitoringForm) form;
	 	
	    String responseId=qaMonitoringForm.getResponseId();
	    logger.debug("responseId: " + responseId);
	    
	    QaUsrResp qaUsrResp= qaService.retrieveQaUsrResp(new Long(responseId).longValue());
	    logger.debug("qaUsrResp: " + qaUsrResp);
	  
	    qaService.removeUserResponse(qaUsrResp);
	    logger.debug("response deleted.");
	    
	    logger.debug("CURRENT_MONITORED_TOOL_SESSION: " + request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION));
	    
	    refreshUserInput(request);
    	return (mapping.findForward(LOAD_MONITORING));	
	}

    
    /**
     * refreshUserInput(HttpServletRequest request)
     * @param request
     */
    public void refreshUserInput(HttpServletRequest request)
    {
    	IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
    	logger.debug("qaService: " + qaService);
        
        Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
        logger.debug("toolContentId: " + toolContentId);
        
        QaContent qaContent=qaService.loadQa(toolContentId.longValue());
    	logger.debug("existing qaContent:" + qaContent);
    	
        List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionData(request, qaContent, true, false, null, null);
        request.getSession().setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);
        logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));
    }
    
    
    /**
     * ?TEMPORARY: allows to exit monitoring url 
     * ActionForward doneMonitoring(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
                                         
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward doneMonitoring(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	QaUtils.cleanUpSessionAbsolute(request);
    	/*check this again */
    	return (mapping.findForward(LOAD_MONITORING)); 
    }

    
	/**
     * persists error messages to request scope
     * persistError(HttpServletRequest request, String message)
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
  
	
    /**
     * boolean isOnlyContentIdAvailable(HttpServletRequest request)
     * @param request
     * @return boolean
     */
    public boolean isOnlyContentIdAvailable(HttpServletRequest request)
    {
        boolean existsContentId=false;
        String strToolContentId=request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
        if ((strToolContentId != null) && (strToolContentId.length() > 0))
            existsContentId=true;
        
        boolean existsToolSession=false;
        for (int toolSessionIdCounter=1; toolSessionIdCounter < MAX_TOOL_SESSION_COUNT.intValue(); toolSessionIdCounter++)
        {
            String strToolSessionId=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID + toolSessionIdCounter);
            if ((strToolSessionId != null) && (strToolSessionId.length() > 0))
            {
                existsToolSession=true;
            }
        }
        
        if (existsContentId && (!existsToolSession))
        {
            logger.debug("OnlyContentIdAvailable");
            return true;
        }
        else
        {
            logger.debug("Not OnlyContentIdAvailable");
            return false;
        }
    }
    
    
    /**
     * calls learning action endLearning functionality 
     * ActionForward endLearning(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws ToolException
     */
    public ActionForward endLearning(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException, ToolException
    {
    	logger.debug("dispatching proxy endLearning to learnng module...");
    	IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("qaService: " + qaService);
		if (qaService == null)
		{
			logger.debug("will retrieve qaService");
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
			logger.debug("retrieving qaService from session: " + qaService);
		}

    	QaLearningAction qaLearningAction= new QaLearningAction();
    	qaLearningAction.endLearning(request, qaService, response);
    	return null; 
    }
    
    
    /**
     * populates data for summary screen
     * refreshSummaryData(HttpServletRequest request, QaContent qaContent, IQaService qaService, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId)
			
     * @param request
     * @param qaContent
     * @param qaService
     * @param isUserNamesVisible
     * @param isLearnerRequest
     * @param currentSessionId
     * @param userId
     */
	public void refreshSummaryData(HttpServletRequest request, QaContent qaContent, IQaService qaService, 
			boolean isUserNamesVisible, boolean isLearnerRequest, String currentSessionId, String userId)
	{
		if (qaService == null)
		{
			logger.debug("will retrieve qaService");
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
			logger.debug("retrieving qaService from session: " + qaService);
		}
		if (qaService == null)
		{
	    	qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
			logger.debug("qaService: " + qaService);
		}

		
		logger.debug("isUserNamesVisible: " + isUserNamesVisible);
		logger.debug("isLearnerRequest: " + isLearnerRequest);
				
		/* this section is related to summary tab. Starts here. */
		Map summaryToolSessions=MonitoringUtil.populateToolSessions(request, qaContent, qaService);
		logger.debug("summaryToolSessions: " + summaryToolSessions);
		request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS, summaryToolSessions);
		
		
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
		
	    	    
	    Map summaryToolSessionsId=MonitoringUtil.populateToolSessionsId(request, qaContent, qaService);
		logger.debug("summaryToolSessionsId: " + summaryToolSessionsId);
		request.getSession().setAttribute(SUMMARY_TOOL_SESSIONS_ID, summaryToolSessionsId);
	    	
	    /* SELECTION_CASE == 2 indicates start up */
	    request.getSession().setAttribute(SELECTION_CASE, new Long(2));
	    logger.debug("SELECTION_CASE: " + request.getSession().getAttribute(SELECTION_CASE));
	    
	    /* Default to All for tool Sessions so that all tool sessions' summary information gets displayed when the module starts up */
	    request.getSession().setAttribute(CURRENT_MONITORED_TOOL_SESSION, "All");
	    logger.debug("CURRENT_MONITORED_TOOL_SESSION: " + request.getSession().getAttribute(CURRENT_MONITORED_TOOL_SESSION));
	    
	    
	    logger.debug("using allUsersData to retrieve data: " + isUserNamesVisible);
	    List listMonitoredAnswersContainerDTO=MonitoringUtil.buildGroupsQuestionData(request, qaContent, 
	    		isUserNamesVisible, isLearnerRequest, currentSessionId, userId);
	    request.getSession().setAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO, listMonitoredAnswersContainerDTO);
	    logger.debug("LIST_MONITORED_ANSWERS_CONTAINER_DTO: " + request.getSession().getAttribute(LIST_MONITORED_ANSWERS_CONTAINER_DTO));
	    /* ends here. */
	}

	
    /**
     * populates data for stats screen
     * refreshStatsData(HttpServletRequest request)
     * @param request
     */
	public void refreshStatsData(HttpServletRequest request)
	{
		/* it is possible that no users has ever logged in for the activity yet*/
		IQaService qaService = (IQaService)request.getSession().getAttribute(TOOL_SERVICE);
		logger.debug("qaService: " + qaService);
		if (qaService == null)
		{
			logger.debug("will retrieve qaService");
			qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
			logger.debug("retrieving qaService from session: " + qaService);
		}

		
	    int countAllUsers=qaService.getTotalNumberOfUsers();
		logger.debug("countAllUsers: " + countAllUsers);
		
		if (countAllUsers == 0)
		{
	    	logger.debug("error: countAllUsers is 0");
	    	request.getSession().setAttribute(USER_EXCEPTION_NO_STUDENT_ACTIVITY, new Boolean(true));
		}
		
		request.getSession().setAttribute(COUNT_ALL_USERS, new Integer(countAllUsers).toString());
		
		int countSessionComplete=qaService.countSessionComplete();
		logger.debug("countSessionComplete: " + countSessionComplete);
		request.getSession().setAttribute(COUNT_SESSION_COMPLETE, new Integer(countSessionComplete).toString());
	}
	
	
	/**
	 * populates data for instructions screen
	 * @param request
	 * @param qaContent
	 */
	public void refreshInstructionsData(HttpServletRequest request, QaContent qaContent)
	{
	    request.getSession().setAttribute(RICHTEXT_ONLINEINSTRUCTIONS,qaContent.getOnlineInstructions());
	    request.getSession().setAttribute(RICHTEXT_OFFLINEINSTRUCTIONS,qaContent.getOfflineInstructions());
	}

    

}