/* *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
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
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;

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
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.McUtils;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;

/**
 * * @author Ozgur Demirtas
 * 
 * <p>Action class that controls the logic of tool behavior. </p>
 * 
 * <p>Note that Struts action class only has the responsibility to navigate 
 * page flow. All database operation should go to service layer and data 
 * transformation from domain model to struts form bean should go to form 
 * bean class. This ensure clean and maintainable code.
 * </p>
 * 
 * <code>SystemException</code> is thrown whenever an known error condition is
 * identified. No system exception error handling code should appear in the 
 * Struts action class as all of them are handled in 
 * <code>CustomStrutsExceptionHandler<code>.
 * 
 *    <!--Monitoring Main Action: interacts with the Monitoring module user -->
   <action 	path="/monitoring" 
   			type="org.lamsfoundation.lams.tool.mc.web.McMonitoringAction" 
   			name="McMonitoringForm" 
	      	scope="session"
   			input=".monitoringContent"
      		parameter="dsp"
      		unknown="false"
      		validate="true"> 

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
		    name="errorList"
		    path=".mcErrorBox"
		    redirect="true"
	  	/>
	</action>  

 * 
*/
public class McMonitoringAction extends LamsDispatchAction implements McAppConstants
{
	static Logger logger = Logger.getLogger(McMonitoringAction.class.getName());
	
	 /** 
     * <p>Default struts dispatch method.</p> 
     * 
     * <p>It is assuming that progress engine should pass in the tool access
     * mode and the tool session id as http parameters.</p>
     * 
     * @param mapping An ActionMapping class that will be used by the Action class to tell
     * the ActionServlet where to send the end-user.
     *
     * @param form The ActionForm class that will contain any data submitted
     * by the end-user via a form.
     * @param request A standard Servlet HttpServletRequest class.
     * @param response A standard Servlet HttpServletResponse class.
     * @return An ActionForward class that will be returned to the ActionServlet indicating where
     *         the user is to go next.
     * @throws IOException
     * @throws ServletException
     * @throws McApplicationException the known runtime exception 
     * 
	 * unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
                                                            
	 * main content/question content management and workflow logic
	 * 
	*/
    public ActionForward unspecified(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
    {
    	logger.debug("dispatching unspecified...");
	 	McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
	 	return null;
    }
    
    
    /**
     * gets called when the user selects a group from dropdown box in the summary tab 
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
    	McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
    	String currentMonitoredToolSession=mcMonitoringForm.getSelectedToolSessionId(); 
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
     * reuses the define later functionality 
     * editActivity(ActionMapping mapping,
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
    	McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
    	IMcService mcService =McUtils.getToolService(request);
	 	
 		request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
 		
		McStarterAction mcStarterAction= new McStarterAction();
	    request.getSession().setAttribute(TOOL_SERVICE, mcService);
	    Long toolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
	    	    
	    request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    return mcStarterAction.executeDefineLater(mapping, form, request, response, mcService);
	}

    
    /**
     * editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
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
    public ActionForward editActivityQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         ToolException
	 {
    	logger.debug("dispatching editActivityQuestions..");
	 	IMcService mcService =McUtils.getToolService(request);
	 	
	 	request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	 	

		request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(true));
		McUtils.setDefineLater(request, true);
		
		request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
		logger.debug("setting  EDIT_OPTIONS_MODE to 0");
		
		return (mapping.findForward(LOAD_MONITORING));
	}
    
    /**
     * switches to summary tab of the monitoring url
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
    	logger.debug("dispatching getSummary...");
    	McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
 		request.setAttribute(CURRENT_MONITORING_TAB, "summary");   
 		return (mapping.findForward(LOAD_MONITORING));
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
    	logger.debug("dispatching getInstructions...");
    	McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
 		request.setAttribute(CURRENT_MONITORING_TAB, "instructions");
 		
 		return (mapping.findForward(LOAD_MONITORING));
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
    	logger.debug("dispatching getStats...");
    	McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
	 	IMcService mcService =McUtils.getToolService(request);
	 	
 		request.setAttribute(CURRENT_MONITORING_TAB, "stats");
 		return (mapping.findForward(LOAD_MONITORING));
	}
    
    
    /**
     * acts as a proxy to authoring addNewQuestion(....)
     * addNewQuestion(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addNewQuestion(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching proxy addNewQuestion...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.addNewQuestion(mapping, form, request, response);
	}
    
    /**
     * acts as a proxy to authoring removeQuestion(....)
     * removeQuestion(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeQuestion(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching proxy removeQuestion...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
    	
	    McAction mcAction = new McAction();
    	return mcAction.removeQuestion(mapping, form, request, response);
	}
    
    
    /**
     * acts as a proxy to authoring editOptions(...)
     * editOptions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward editOptions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy editOptions...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.editOptions(mapping, form, request, response);
    }
    
    
    /**
     * acts as a proxy to authoring addOption(...) 
     *  addOption(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward addOption(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy addOption...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.addOption(mapping, form, request, response);
    }
    
    
    /** acts as a proxy to authoring removeOption(...) 
     * removeOption(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward removeOption(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy removeOption...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.removeOption(mapping, form, request, response);
    }
    
    
    /**
     * acts as a proxy to authoring moveQuestionDown(...) 
     * moveQuestionDown(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward moveQuestionDown(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy moveQuestionDown...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.moveQuestionDown(mapping, form, request, response);
    }
    
    
    /**
     * acts as a proxy to authoring moveQuestionUp(...) 
     * moveQuestionUp(ActionMapping mapping,
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
    public ActionForward moveQuestionUp(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy moveQuestionUp...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.moveQuestionUp(mapping, form, request, response);
    }
    
    /**
     * acts as a proxy to authoring doneOptions(...)
     * doneOptions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward doneOptions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy doneOptions...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.doneOptions(mapping, form, request, response);
    }

    
    /**
     * acts as a proxy to authoring submitQuestions(...)
     * submitQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward submitQuestions(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException

    {
    	logger.debug("dispatching proxy submitQuestions...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    /*set the current tab*/
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
	    /* present the view-only screen first */
		request.getSession().setAttribute(ACTIVE_MODULE, DEFINE_LATER);
		request.getSession().setAttribute(DEFINE_LATER_IN_EDIT_MODE, new Boolean(false));
	     
    	McAction mcAction = new McAction();
    	return mcAction.submitQuestions(mapping, form, request, response);
    }
    
    /**
     * acts as a proxy to deleteOfflineFile(...) 
     * deleteOfflineFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteOfflineFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException

    {
    	logger.debug("dispatching proxy deleteOfflineFile...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.deleteOfflineFile(mapping, form, request, response);
    }
    
    /**
     * acts as a proxy to authoring deleteOnlineFile(...) 
     * deleteOnlineFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward deleteOnlineFile(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException

    {
    	logger.debug("dispatching proxy deleteOnlineFile...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.deleteOnlineFile(mapping, form, request, response);
    }
    
    
    /**
     * acts as a proxy to authoring submitOfflineFiles(...)
     * submitOfflineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         RepositoryCheckedException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     * @throws RepositoryCheckedException
     */
    public ActionForward submitOfflineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         RepositoryCheckedException
    {
    	logger.debug("dispatching proxy deleteOnlineFile...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.submitOfflineFiles(mapping, form, request, response);
    }
    
    /**
     * acts as a proxy to authoring submitOnlineFiles(...) 
     * submitOnlineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         RepositoryCheckedException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     * @throws RepositoryCheckedException
     */
    public ActionForward submitOnlineFiles(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException,
                                         RepositoryCheckedException
    {
    	logger.debug("dispatching proxy submitOnlineFiles...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.submitOnlineFiles(mapping, form, request, response);
    }

    /**
     * acts as a proxy to authoring doneAdvancedTab(...) 
     * doneAdvancedTab(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward doneAdvancedTab(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy doneAdvancedTab...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.doneAdvancedTab(mapping, form, request, response);
    }

    
    /**
     * acts as a proxy to authoring doneInstructionsTab(...)
     * doneInstructionsTab(ActionMapping mapping,
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
    public ActionForward doneInstructionsTab(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	logger.debug("dispatching proxy doneInstructionsTab...");
    	request.setAttribute(SOURCE_MC_STARTER, "monitoring");
	    logger.debug("SOURCE_MC_STARTER: monitoring");
	    
	    request.setAttribute(CURRENT_MONITORING_TAB, "editActivity");
	    
    	McAction mcAction = new McAction();
    	return mcAction.doneInstructionsTab(mapping, form, request, response);
    }
    
    
    /**
     * user is done with Monitoring environment
     * doneMonitoring(ActionMapping mapping,
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
    public ActionForward doneMonitoring(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
    {
    	MonitoringUtil.cleanupMonitoringSession(request);
    	/*forward outside of the app. Currently it is index.jsp */
    	return (mapping.findForward(LOAD_STARTER)); 
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
    