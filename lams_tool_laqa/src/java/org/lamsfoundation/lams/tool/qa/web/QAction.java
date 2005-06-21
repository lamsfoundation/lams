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
import java.util.Date;
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
import org.apache.struts.actions.DispatchAction;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * 
 * monitoring to support modification and hiding of user responses
 * 
 */
/**
 * 
 * once lams_learning is erady and appContext file is src/ then FINISH toool session will work.
 * 
 */
/**
 * 
 * content_locked is done 
 */
/**
 * 
 * done: removed styling, except error messages and table centering
 * 
 */
/**
 * The tool's Spring configuration file: qaCompactApplicationContext.xml
 * Main service bean of the tool is: org.lamsfoundation.lams.tool.qa.service.QaServicePOJO
 * 
 * done: config file is read from classpath
 */


/**
 * 
 *  the tool's web.xml will be modified to have classpath to learning service.
 * This is how the tool gets the definition of "learnerService"
 */
/**
 * 
 * when to reset define later and synchin monitor etc..
 *  
 */

/** make sure the tool gets called on:
 *	setAsForceComplete(Long userId) throws QaApplicationException 
 */


/**
 * 
 * User Issue:
 * Right now:
 * 1- the tool gets the request object from the container.
 * 2- Principal principal = req.getUserPrincipal();
 * 3- String username = principal.getName();
 * 4- User userCompleteData = qaService.getCurrentUserData(userName);
 * 5- write back userCompleteData.getUserId()
 */


/**
 * 
 * JBoss Issue: 
 * Currently getUserPrincipal() returns null and ServletRequest.isUserInRole() always returns false on unsecured pages, 
 * even after the user has been authenticated.
 * http://jira.jboss.com/jira/browse/JBWEB-19 
 */

/**
 * TO DO: definelater to be created completely at monitor time?
 * populating User id from the tool url. 
 * 
 */

/**
 * eliminate the calls to:
 * authoringUtil.simulatePropertyInspector_RunOffline(request);
 * authoringUtil.simulatePropertyInspector_setAsDefineLater(request);
 */


/**
 * 
 * @author ozgurd
 *
 * TOOL PARAMETERS: ?? (toolAccessMode) ??
 * Authoring environment: toolContentId
 * Learning environment: toolSessionId + toolContentId  
 * Monitoring environment: toolContentId / Contribute tab:toolSessionId(s)
 * 	 
 * 
 */

/**
 * Note: the tool must support deletion of an existing content from within the authoring environment.
 * The current support for this is by implementing the tool contract : removeToolContent(Long toolContentId)
 */


/**
 * 
 * We have had to simulate container bahaviour in development stage by calling 
 * createToolSession and leaveToolSession from the web layer. These will go once the tool is 
 * in deployment environment.
 * 
 * 
 * CHECK: leaveToolSession and relavent LearnerService may need to be defined in the spring config file.
 * 
 */


/**
 * 
 * GROUPING SUPPORT: Find out what to do.
 */

/**
 * 
 * take force_offline and define_later outside of the tool
 */

/**
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
 * @author ozgurd
 * ----------------XDoclet Tags--------------------
 * ----------------XDoclet Tags--------------------
 */
public class QAction extends DispatchAction implements QaAppConstants
{
	static Logger logger = Logger.getLogger(QAction.class.getName());

	public static Integer READABLE_TOOL_SESSION_COUNT = new Integer(1);
	
    /** 
     * <p>Struts dispatch method.</p> 
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
     * @throws QaApplicationException the known runtime exception 
     * 
     */
    
	/**
	 * loadQ(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
                                                            
	 * return ActionForward
	 * main content/question content management and workflow logic
	 * 
	 * if the passed toolContentId exists in the db, we need to get the relevant data into the Map 
	 * if not, create the default Map 
	*/
	
    public ActionForward loadQ(ActionMapping mapping,
                               ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException,
                                                            ServletException
    {
    	
    	AuthoringUtil authoringUtil= new AuthoringUtil();
    	
    	/**
		 * find out which tab is the active one.
		 */ 
		authoringUtil.findSelectedTab(mapping,
									form,
						            request,
						            response);
    	
    	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
    	logger.debug(logger + " " + this.getClass().getName() +  "loadQ: form read: " + qaAuthoringForm);
    	
    	
    	/**
    	 * the status of define later is determined from the property inspector and 
    	 * by now, we know whether it is on or off
    	 * 
    	 * enable-disable tool html elements based on "define later" status
    	 */
    	boolean defineLaterStatus=QaUtils.getDefineLaterStatus();
    	
    	Boolean defineLater=new Boolean(defineLaterStatus);
    	logger.debug(logger + " " + this.getClass().getName() +  "defineLater: " + defineLater);
    	if (defineLater.equals(new Boolean(false)))
		{
    		request.getSession().setAttribute(IS_DEFINE_LATER,"false");
    		request.getSession().setAttribute(DISABLE_TOOL,"");
		}
    	else
    	{
    		request.getSession().setAttribute(IS_DEFINE_LATER,"true");
    		request.getSession().setAttribute(DISABLE_TOOL,"disabled");	
    	}
    	
    	//retrieve the default question content map 
        Map mapQuestionContent=(Map)request.getSession().getAttribute(MAP_QUESTION_CONTENT);
        logger.debug(logger + " " + this.getClass().getName() +  "MAP_QUESTION_CONTENT:" + request.getSession().getAttribute(MAP_QUESTION_CONTENT));
        
        String userAction="";
        if (qaAuthoringForm.getAddContent() != null)
        {
        	userAction=ADD_NEW_QUESTION;
        }
        else if (qaAuthoringForm.getRemoveContent() != null)
        {
        	userAction=REMOVE_QUESTION;
        }
        else if (qaAuthoringForm.getRemoveAllContent() != null)
        {
        	userAction=REMOVE_ALL_CONTENT;
        }
        else if (qaAuthoringForm.getSubmitTabDone() != null)
        {
        	userAction=SUBMIT_TAB_DONE;
        }
        else if (qaAuthoringForm.getSubmitAllContent() != null)
        {
        	userAction=SUBMIT_ALL_CONTENT;
        }
        logger.debug(logger + " " + this.getClass().getName() +  "user action is: " + userAction);

        //add a new question to Map
        if (userAction.equalsIgnoreCase(ADD_NEW_QUESTION)) 
		{
        	request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));
        	authoringUtil.reconstructQuestionContentMapForAdd(mapQuestionContent, request);
        }//delete a question
        else if (userAction.equalsIgnoreCase(REMOVE_QUESTION))
		{
        	request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));
        	authoringUtil.reconstructQuestionContentMapForRemove(mapQuestionContent, request, qaAuthoringForm);
		} //remove selected content
        else if (userAction.equalsIgnoreCase(REMOVE_ALL_CONTENT))
		{
        	authoringUtil.removeAllDBContent(request);
        	QaUtils.cleanupSession(request);
        	//reset all user actions
            qaAuthoringForm.resetUserAction();
        	return (mapping.findForward(LOAD_STARTER));
		}
        else if (userAction.equalsIgnoreCase(SUBMIT_TAB_DONE))
        {
        	logger.debug(logger + " " + this.getClass().getName() +  "user is done with this tab.");
        	//reset all user actions
        	qaAuthoringForm.resetUserAction();
        	return (mapping.findForward(LOAD_QUESTIONS));
        }//submit questions contained in the Map
        else if (userAction.equalsIgnoreCase(SUBMIT_ALL_CONTENT))
        {
        	ActionMessages errors= new ActionMessages();

        	/**
        	 * full form validation should be performed only in standard authoring mode, but not in monitoring EditActivity
        	 */

        	if ((qaAuthoringForm.getTitle() == null) || (qaAuthoringForm.getTitle().length() == 0))
    		{
        		errors.add(Globals.ERROR_KEY,new ActionMessage("error.title"));
    			logger.debug(logger + " " + this.getClass().getName() +  "add title to ActionMessages:");
    		}

    		if ((qaAuthoringForm.getInstructions() == null) || (qaAuthoringForm.getInstructions().length() == 0))
    		{
    			errors.add(Globals.ERROR_KEY, new ActionMessage("error.instructions"));
    			logger.debug(logger + " " + this.getClass().getName() +  "add instructions to ActionMessages: ");
    		}

    		/**
    		 * enforce that the first (default) question entry is not empty
    		 */
    		String defaultQuestionEntry =request.getParameter("questionContent0");
    		if ((defaultQuestionEntry == null) || (defaultQuestionEntry.length() == 0))
    		{
    			errors.add(Globals.ERROR_KEY, new ActionMessage("error.defaultquestion.empty"));
    			logger.debug(logger + " " + this.getClass().getName() +  "add error.defaultquestion.empty to ActionMessages: ");
    		}
        	
        	Boolean renderMonitoringEditActivity=(Boolean)request.getSession().getAttribute(RENDER_MONITORING_EDITACTIVITY);
    		if ((renderMonitoringEditActivity != null) && (!renderMonitoringEditActivity.booleanValue()))
    		{

        		if ((qaAuthoringForm.getReportTitle() == null) || (qaAuthoringForm.getReportTitle().length() == 0))
        		{
        			errors.add(Globals.ERROR_KEY, new ActionMessage("error.reportTitle"));
        			logger.debug(logger + " " + this.getClass().getName() +  "add reportTitle to ActionMessages: ");
        		}
        		
        		if ((qaAuthoringForm.getMonitoringReportTitle() == null) || (qaAuthoringForm.getMonitoringReportTitle().length() == 0))
        		{
        			errors.add(Globals.ERROR_KEY, new ActionMessage("error.monitorReportTitle"));
        			logger.debug(logger + " " + this.getClass().getName() +  "add monitorReportTitle to ActionMessages: ");
        		}
    		}
    		/**
    		 * end of error validation
    		 */
    		
    		saveErrors(request,errors);
    		if (errors.size() > 0)  
    		{
    			logger.debug(logger + " " + this.getClass().getName() +  "returning back to from to fix errors:");
    			request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(true));
    			return (mapping.findForward(LOAD_QUESTIONS));
    		}

        	/**
        	 * look after defineLater flag
        	 */
    		IQaService qaService =QaUtils.getToolService(request);
	    	Long monitoredContentId=(Long)request.getSession().getAttribute(MONITORED_CONTENT_ID);
		    logger.debug(logger + " " + this.getClass().getName() +  "MONITORED_CONTENT_ID: " + monitoredContentId);
		    if (monitoredContentId != null)
		    {
		    	qaService.unsetAsDefineLater(monitoredContentId);
			    logger.debug(logger + " " + this.getClass().getName() +  "MONITORED_CONTENT_ID has been unset as defineLater: ");
		    }
		    
    		authoringUtil.reconstructQuestionContentMapForSubmit(mapQuestionContent, request);
        	//delete existing content from the database
        	authoringUtil.removeAllDBContent(request);
        	//create-recreate the content in the db 
        	QaContent qaContent=authoringUtil.createContent(mapQuestionContent, request, qaAuthoringForm);
        	authoringUtil.createQuestionContent(mapQuestionContent, request, qaContent);
        	
        	//giving the user a feedback
            errors.clear();
            errors.add(Globals.ERROR_KEY, new ActionMessage("submit.successful")); 
    		logger.debug(logger + " " + this.getClass().getName() +  "submit successful: ");
    		saveErrors(request,errors);
        }
        else
        {
        	logger.debug(logger + " " + this.getClass().getName() +  "Warning!: Uncatered-for user action: " + userAction);
        }

        //reset all user actions
        qaAuthoringForm.resetUserAction();
    	
        /*
        ToolAccessMode mode = WebUtil.readToolAccessModeParam(request, WebUtil.PARAM_MODE,MODE_OPTIONAL);
        logger.debug(logger + " " + this.getClass().getName() +  "retrieving mode: " + mode);
        */
        
        return (mapping.findForward(LOAD_QUESTIONS));
    }
    
    
    /**
     * This method manages the presentation Map for the learner mode.
     * The dispatch method to decide which view should be shown to the user.
     * The deicision is based on tool access mode and tool session status.
     * The tool access mode is lams concept and should comes from progress
     * engine, whereas tool session status is tool's own session state
     * concept and should be loaded from database and setup by
     * <code>loadQuestionnaire</code>.
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
     */
    public ActionForward displayQ(ActionMapping mapping,
                                              ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws IOException,
                                                                        ServletException
    {
    	/**
    	 * if the content is not ready yet, don't even proceed.
    	 * check the define later status
    	 */
    	Boolean defineLater=(Boolean)request.getSession().getAttribute(IS_DEFINE_LATER);
    	logger.debug(logger + " " + this.getClass().getName() +  "learning-defineLater: " + defineLater);
    	if (defineLater.booleanValue() == true)
    	{
    		persistError(request,"error.defineLater");
    		return (mapping.findForward(LOAD));
    	}
    	
    	LearningUtil learningUtil= new LearningUtil();
    	QaLearningForm qaLearningForm = (QaLearningForm) form;
    	
    	//retrieve the default question content map 
        Map mapQuestions=(Map)request.getSession().getAttribute(MAP_QUESTION_CONTENT_LEARNER);
        logger.debug(logger + " " + this.getClass().getName() +  "MAP_QUESTION_CONTENT_LEARNER:" + request.getSession().getAttribute(MAP_QUESTION_CONTENT_LEARNER));
        
    	//retrieve the answers Map
        Map mapAnswers=(Map)request.getSession().getAttribute(MAP_ANSWERS);
        logger.debug(logger + " " + this.getClass().getName() +  "MAP_ANSWERS:" + mapAnswers);

        //obtain author's question listing preference
		String questionListingMode=(String) request.getSession().getAttribute(QUESTION_LISTING_MODE);
		//maintain Map either based on sequential listing or based on combined listing
		if (questionListingMode.equalsIgnoreCase(QUESTION_LISTING_MODE_SEQUENTIAL))
		{
			logger.debug(logger + " " + this.getClass().getName() +  "QUESTION_LISTING_MODE_SEQUENTIAL");
			
			int currentQuestionIndex=new Long(qaLearningForm.getCurrentQuestionIndex()).intValue();
	        logger.debug(logger + " " + this.getClass().getName() +  "currentQuestionIndex is: " + currentQuestionIndex);
	        logger.debug(logger + " " + this.getClass().getName() +  "getting answer for question: " + currentQuestionIndex + "as: " +  qaLearningForm.getAnswer());
	        logger.debug(logger + " " + this.getClass().getName() +  "mapAnswers size:" + mapAnswers.size());
	        
	        if  (mapAnswers.size() >= currentQuestionIndex)
	        {
	        	logger.debug(logger + " " + this.getClass().getName() +  "mapAnswers size:" + mapAnswers.size() + " and currentQuestionIndex: " + currentQuestionIndex);
	        	mapAnswers.remove(new Long(currentQuestionIndex).toString());
	        }
	    	logger.debug(logger + " " + this.getClass().getName() +  "before adding to mapAnswers: " + mapAnswers);
	    	mapAnswers.put(new Long(currentQuestionIndex).toString(), qaLearningForm.getAnswer());
	        logger.debug(logger + " " + this.getClass().getName() +  "adding new answer:" + qaLearningForm.getAnswer() + " to mapAnswers.");
			
			if (qaLearningForm.getGetNextQuestion() != null)
	        	currentQuestionIndex++;	
	        else if (qaLearningForm.getGetPreviousQuestion() != null)
	        	currentQuestionIndex--;
	        
			request.getSession().setAttribute(CURRENT_ANSWER, mapAnswers.get(new Long(currentQuestionIndex).toString()));
	        logger.debug(logger + " " + this.getClass().getName() +  "currentQuestionIndex will be: " + currentQuestionIndex);
	        request.getSession().setAttribute(CURRENT_QUESTION_INDEX, new Long(currentQuestionIndex));
	        learningUtil.feedBackAnswersProgress(request,currentQuestionIndex);
	        qaLearningForm.resetUserActions(); /**resets all except submitAnswersContent */ 
		}
		else
		{
			logger.debug(logger + " " + this.getClass().getName() +  "QUESTION_LISTING_MODE_COMBINED");
			for (int questionIndex=INITIAL_QUESTION_COUNT.intValue(); questionIndex<= mapQuestions.size(); questionIndex++ )
			{
				String answer=request.getParameter("answer" + questionIndex);
				logger.debug(logger + " " + this.getClass().getName() +  " answer for question " + questionIndex + " is:" + answer);
				mapAnswers.put(new Long(questionIndex).toString(), answer);
			}
		}
        
		/**
		 *  At this point the Map holding learner responses is ready. So place that into the session.
		 */
		request.getSession().setAttribute(MAP_ANSWERS, mapAnswers);
		
		/**
		 * Learner submits the responses to the questions. 
		 */
        if (qaLearningForm.getSubmitAnswersContent() != null)
        {
        	logger.debug(logger + " " + this.getClass().getName() +  "submit the responses: " + mapAnswers);
        	//recreate the users and responses
        	learningUtil.createUsersAndResponses(mapAnswers, request);
            //reset all user actions
            qaLearningForm.resetUserActions();
            //also reset this button
            qaLearningForm.setSubmitAnswersContent(null);
            //start generating a report for the Learner
            learningUtil.buidLearnerReport(request,1);
            
            learningUtil.lockContent(request);
            logger.debug(logger + " " + this.getClass().getName() +  "content has been locked");
        	return (mapping.findForward(LEARNER_REPORT));
        }
        /**
		 * Simulate learner leaving the current tool session. This will normally gets called by the container by 
		 * leaveToolSession(toolSessionId, user) 
		 */
        else if (qaLearningForm.getEndLearning() != null) 
        {
        		/**
            	 * The learner is done with the tool session. The tool needs to clean-up.
            	 */
            	
                Long toolSessionId=(Long)request.getSession().getAttribute(TOOL_SESSION_ID);
                User user=(User)request.getSession().getAttribute(TOOL_USER);
                logger.debug(logger + " " + this.getClass().getName() +  "Simulating container behaviour by calling  " +
                														 "leaveToolSession() with toolSessionId: " +  toolSessionId + " and " +
    																	 "user: " + user);
                
                /**
                 * mark the tool session as COMPLETE
                 */
                IQaService qaService =QaUtils.getToolService(request);
                QaSession qaSession=qaService.retrieveQaSessionOrNullById(toolSessionId.longValue());
                qaSession.setSession_end_date(new Date(System.currentTimeMillis()));
                qaSession.setSession_status(COMPLETED);
                qaService.updateQaSession(qaSession);
                logger.debug(logger + " " + this.getClass().getName() +  "tool session has been marked COMPLETE: " + qaSession);
                /*
                ILearnerService learnerService =LearnerServiceProxy.getLearnerService(getServlet().getServletContext());
                logger.debug(logger + " " + this.getClass().getName() +  "learnerService: " + learnerService);
                learnerService.completeToolSession(toolSessionId, user);
                logger.debug(logger + " " + this.getClass().getName() +  "completeToolSession on learnerService has finished: ");
                */
        	}
        	
            /**
             * Also cleanup session attributes
             */
            QaUtils.cleanupSession(request);
        
        //reset all user actions
        qaLearningForm.resetUserActions();
        
    	return (mapping.findForward(LOAD));
    }
    
    
    /**
     * used to load Monitoring tabs back once the controller moves to Edit Activity.
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    public ActionForward loadMonitoring(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                      ServletException
  {
    	QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
    	
    	if (qaAuthoringForm.getSummaryMonitoring() != null) 
    	{
            logger.debug(logger + " " + this.getClass().getName() +  "request for getSummaryMonitoring, start proxying...");
            logger.debug(logger + " " + this.getClass().getName() +  "NO_AVAILABLE_SESSIONS: " +
            								request.getSession().getAttribute(NO_AVAILABLE_SESSIONS));
            
            Boolean noAvailableSessions=(Boolean)request.getSession().getAttribute(NO_AVAILABLE_SESSIONS);
            logger.debug(logger + " " + this.getClass().getName() +  "NO_AVAILABLE_SESSIONS: " +noAvailableSessions);
            qaAuthoringForm.resetUserAction();
            if ((noAvailableSessions != null) && (noAvailableSessions.booleanValue()))
            {
            	persistError(request,"error.noStudentActivity");
            	request.setAttribute(START_MONITORING_SUMMARY_REQUEST, new Boolean(true));
        		request.setAttribute(STOP_RENDERING_QUESTIONS, new Boolean(true));
        		return (mapping.findForward(LOAD));
            }
            else
            {
            	logger.debug(logger + " " + this.getClass().getName() +  "NO_AVAILABLE_SESSIONS: " +false);
            	QaMonitoringAction qaMonitoringAction=new QaMonitoringAction();
            	QaMonitoringForm qaMonitoringForm=new QaMonitoringForm();
            	return qaMonitoringAction.generateToolSessionDataMap(mapping,qaMonitoringForm,request,response);
            }
    	}
    	else if (qaAuthoringForm.getInstructionsMonitoring() != null)
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  "QAction- request for getInstructionsMonitoring()");
    		qaAuthoringForm.resetUserAction();
    		QaMonitoringAction qaMonitoringAction=new QaMonitoringAction();
        	QaMonitoringForm qaMonitoringForm=new QaMonitoringForm();
        	qaMonitoringForm.setInstructions("instructions");
        	return qaMonitoringAction.generateToolSessionDataMap(mapping,qaMonitoringForm,request,response);
    	}
    	else if (qaAuthoringForm.getEditActivityMonitoring() != null) 
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  "QAction-request for getEditActivityMonitoring()");
			QaStarterAction qaStarterAction = new QaStarterAction();
			logger.debug(logger + " " + this.getClass().getName() +  "forward to Authoring Basic tab ");
			ActionForward actionForward=qaStarterAction.startMonitoringSummary(mapping, qaAuthoringForm, request, response);
			logger.debug(logger + " " + this.getClass().getName() +  "actionForward: " + actionForward);
			qaAuthoringForm.resetUserAction();
			return (actionForward);
    	}
    	else if (qaAuthoringForm.getStatsMonitoring() != null)
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  "TO BE FIXED request for getStatsMonitoring()");
    		qaAuthoringForm.resetUserAction();
    		QaMonitoringAction qaMonitoringAction=new QaMonitoringAction();
        	QaMonitoringForm qaMonitoringForm=new QaMonitoringForm();
        	qaMonitoringForm.setStats("stats");
        	return qaMonitoringAction.generateToolSessionDataMap(mapping,qaMonitoringForm,request,response);
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
		logger.debug(logger + " " + this.getClass().getName() +  "add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	} 
}
