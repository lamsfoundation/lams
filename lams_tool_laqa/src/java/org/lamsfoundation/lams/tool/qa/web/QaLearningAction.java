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
 * 	<action
      path="/learning"
      type="org.lamsfoundation.lams.tool.qa.web.QaLearningAction"
      name="QaLearningForm"
      scope="session"
      input="/learning/AnswersContent.jsp"
      parameter="method"
      unknown="false"
      validate="true">

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
		    name="loadLearner"
		    path="/learning/AnswersContent.jsp"	        
		    redirect="true"
	      />

	  	<forward
		    name="loadMonitoring"
		    path="/monitoring/MonitoringMaincontent.jsp"
		    redirect="true"
	  	/>
	      
	    <forward
	        name="learningStarter"
	        path="/learningIndex.jsp"
	        redirect="true"
	      />
      
	     <forward
	        name="learnerRep"
	        path="/monitoring/LearnerRep.jsp"
	        redirect="true"
	      />
	      
	      <forward
		    name="errorListLearner"
		    path="/QaErrorBox.jsp"
		    redirect="true"
	  	/>      
	</action>

 * 
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.web;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;


public class QaLearningAction extends LamsDispatchAction implements QaAppConstants
{
	static Logger logger = Logger.getLogger(QaLearningAction.class.getName());

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
        throws IOException, ServletException, ToolException{
    	logger.debug("dispatching unspecified...");
	 	return null;
    }
    

    /**
     * submits users responses
     * ActionForward submitAnswersContent(ActionMapping mapping,
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
    public ActionForward submitAnswersContent(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching submitAnswersContent..." + request);
    	QaLearningForm qaLearningForm = (QaLearningForm) form;
    	
    	Map mapQuestions=(Map)request.getSession().getAttribute(MAP_QUESTION_CONTENT_LEARNER);
        logger.debug("MAP_QUESTION_CONTENT_LEARNER:" + request.getSession().getAttribute(MAP_QUESTION_CONTENT_LEARNER));
        
        Map mapAnswers=(Map)request.getSession().getAttribute(MAP_ANSWERS);    	
        logger.debug(logger + " " + this.getClass().getName() +  "submit the responses: " + mapAnswers);
        
        String totalQuestionCount=(String)request.getSession().getAttribute(TOTAL_QUESTION_COUNT);
        logger.debug("totalQuestionCount: " + totalQuestionCount);
    	
        
    	String questionListingMode=(String) request.getSession().getAttribute(QUESTION_LISTING_MODE);
        /* if the listing mode is QUESTION_LISTING_MODE_COMBINED populate  the answers here*/
    	if (questionListingMode.equalsIgnoreCase(QUESTION_LISTING_MODE_COMBINED))
    	{
            logger.debug("the listing mode is combined.");
            for (int questionIndex=INITIAL_QUESTION_COUNT.intValue(); questionIndex<= mapQuestions.size(); questionIndex++ )
            {
                String answer=request.getParameter("answer" + questionIndex);
                logger.debug("answer for question " + questionIndex + " is:" + answer);
                mapAnswers.put(new Long(questionIndex).toString(), answer);
            }
    	}
    	else
    	{
    		if (totalQuestionCount.equals("1"))
    		{
    			logger.debug("totalQuestionCount is 1: " + qaLearningForm.getAnswer());
    			mapAnswers.put(new Long(1).toString(), qaLearningForm.getAnswer());
    		}
    		else 
    		{
    			logger.debug("totalQuestionCount is > 1: " + qaLearningForm.getAnswer());
    			int mapSize=mapAnswers.size();
    			logger.debug("mapSize: " + mapSize);
    			mapAnswers.put(new Long(mapSize+1).toString() , qaLearningForm.getAnswer());
    		}
    		
    	}
    	logger.debug(logger + " " + this.getClass().getName() +  "final mapAnswers: " + mapAnswers);
    	request.getSession().setAttribute(MAP_ANSWERS, mapAnswers);
    	
    	
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

	    /*recreate the users and responses*/
        LearningUtil learningUtil= new LearningUtil();
        learningUtil.createUsersAndResponses(mapAnswers, request, qaService);
        qaLearningForm.resetUserActions();
        qaLearningForm.setSubmitAnswersContent(null);
        
        learningUtil.setContentInUse(toolContentId.longValue(), qaService);
        logger.debug("content has been set in use");
        
        logger.debug("start generating learning report...");
        Long toolContentID=(Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID);
	    logger.debug("toolContentID: " + toolContentID);
	    
	   
	    String userID= (String)request.getSession().getAttribute(USER_ID);
		logger.debug("userID: " + userID);
		QaQueUsr qaQueUsr=qaService.getQaQueUsrById(new Long(userID).longValue());
		logger.debug("the current user qaQueUsr " + qaQueUsr + " and username: "  + qaQueUsr.getUsername());
		logger.debug("the current user qaQueUsr's session  " + qaQueUsr.getQaSession());
		String currentSessionId=qaQueUsr.getQaSession().getQaSessionId().toString();
		logger.debug("the current user SessionId  " + currentSessionId);
		    	    
	    
	    Boolean isUserNamesVisibleBoolean=(Boolean)request.getSession().getAttribute(IS_USERNAME_VISIBLE);
    	boolean isUserNamesVisible=isUserNamesVisibleBoolean.booleanValue();
    	logger.debug("isUserNamesVisible: " + isUserNamesVisible);
	    
    	QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
    	/*the report should have all the users' entries OR
    	 * the report should have only the current session's entries*/
    	qaMonitoringAction.refreshSummaryData(request, qaContent, qaService, isUserNamesVisible, true, currentSessionId, userID);
    	
		request.getSession().setAttribute(REQUEST_LEARNING_REPORT, new Boolean(true).toString());
		logger.debug("fwd'ing to." + INDIVIDUAL_LEARNER_RESULTS);
		return (mapping.findForward(INDIVIDUAL_LEARNER_RESULTS));
	}
    
	
    /**
	 * returns Learner Report for a session
	 * ActionForward viewAllResults(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException)
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
    public ActionForward viewAllResults(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException,
                                         ServletException
	{
    	logger.debug("dispatching viewAllResults...");
    	QaLearningForm qaLearningForm = (QaLearningForm) form;

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

		Long toolSessionId=(Long)request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
		
		Boolean isUserNamesVisibleBoolean=(Boolean)request.getSession().getAttribute(IS_USERNAME_VISIBLE);
    	boolean isUserNamesVisible=isUserNamesVisibleBoolean.booleanValue();
    	logger.debug("isUserNamesVisible: " + isUserNamesVisible);
    	
    	QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
    	qaMonitoringAction.refreshSummaryData(request, qaContent, qaService, isUserNamesVisible, true, toolSessionId.toString(), null);
    	
		request.getSession().setAttribute(REQUEST_LEARNING_REPORT, new Boolean(true).toString());
		request.getSession().setAttribute(REQUEST_LEARNING_REPORT_PROGRESS, new Boolean(false).toString());
		logger.debug("fwd'ing to for learner progress" + LEARNER_REPORT);
		return (mapping.findForward(LEARNER_REPORT));
	}

    
    
    /**
     * moves to the next question and modifies the map
     * ActionForward getNextQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    	throws IOException, ServletException, ToolException
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
    public ActionForward getNextQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    	throws IOException, ServletException, ToolException
	{
    	logger.debug("dispatching getNextQuestion...");
    	QaLearningForm qaLearningForm = (QaLearningForm) form;

    	Map mapAnswers=(Map)request.getSession().getAttribute(MAP_ANSWERS);
        logger.debug("MAP_ANSWERS:" + mapAnswers);
        
        int currentQuestionIndex=new Long(qaLearningForm.getCurrentQuestionIndex()).intValue();
        logger.debug("currentQuestionIndex is: " + currentQuestionIndex);
        logger.debug("getting answer for question: " + currentQuestionIndex + "as: " +  qaLearningForm.getAnswer());
        logger.debug("mapAnswers size:" + mapAnswers.size());
        
        if  (mapAnswers.size() >= currentQuestionIndex)
        {
            logger.debug("mapAnswers size:" + mapAnswers.size() + " and currentQuestionIndex: " + currentQuestionIndex);
            mapAnswers.remove(new Long(currentQuestionIndex).toString());
        }
        logger.debug("before adding to mapAnswers: " + mapAnswers);
        mapAnswers.put(new Long(currentQuestionIndex).toString(), qaLearningForm.getAnswer());
        logger.debug("adding new answer:" + qaLearningForm.getAnswer() + " to mapAnswers.");

        currentQuestionIndex++;
        
        LearningUtil learningUtil= new LearningUtil();
        request.getSession().setAttribute(CURRENT_ANSWER, mapAnswers.get(new Long(currentQuestionIndex).toString()));
        logger.debug("currentQuestionIndex will be: " + currentQuestionIndex);
        request.getSession().setAttribute(CURRENT_QUESTION_INDEX, new Long(currentQuestionIndex));
        learningUtil.feedBackAnswersProgress(request,currentQuestionIndex);
        qaLearningForm.resetUserActions(); /*resets all except submitAnswersContent */
        
        request.getSession().setAttribute(MAP_ANSWERS, mapAnswers);
        logger.debug("final MAP_ANSWERS: " + mapAnswers);
        
        return (mapping.findForward(LOAD_LEARNER));
    }
    
    
    /**
     * moves to the previous question and modifies the map
     * ActionForward getPreviousQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
		throws IOException, ServletException, ToolException
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
    public ActionForward getPreviousQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
		throws IOException, ServletException, ToolException
	{
		logger.debug("dispatching getPreviousQuestion...");
		QaLearningForm qaLearningForm = (QaLearningForm) form;
	
		Map mapAnswers=(Map)request.getSession().getAttribute(MAP_ANSWERS);
	    logger.debug("MAP_ANSWERS:" + mapAnswers);
	    
	    int currentQuestionIndex=new Long(qaLearningForm.getCurrentQuestionIndex()).intValue();
	    logger.debug("currentQuestionIndex is: " + currentQuestionIndex);
	    logger.debug("getting answer for question: " + currentQuestionIndex + "as: " +  qaLearningForm.getAnswer());
	    logger.debug("mapAnswers size:" + mapAnswers.size());
	    
	    if  (mapAnswers.size() >= currentQuestionIndex)
	    {
	        logger.debug("mapAnswers size:" + mapAnswers.size() + " and currentQuestionIndex: " + currentQuestionIndex);
	        mapAnswers.remove(new Long(currentQuestionIndex).toString());
	    }
	    logger.debug("before adding to mapAnswers: " + mapAnswers);
	    mapAnswers.put(new Long(currentQuestionIndex).toString(), qaLearningForm.getAnswer());
	    logger.debug("adding new answer:" + qaLearningForm.getAnswer() + " to mapAnswers.");
	
	    currentQuestionIndex--;
	    
	    LearningUtil learningUtil= new LearningUtil();
	    request.getSession().setAttribute(CURRENT_ANSWER, mapAnswers.get(new Long(currentQuestionIndex).toString()));
	    logger.debug("currentQuestionIndex will be: " + currentQuestionIndex);
	    request.getSession().setAttribute(CURRENT_QUESTION_INDEX, new Long(currentQuestionIndex));
	    learningUtil.feedBackAnswersProgress(request,currentQuestionIndex);
	    qaLearningForm.resetUserActions(); /*resets all except submitAnswersContent */
	    
	    request.getSession().setAttribute(MAP_ANSWERS, mapAnswers);
	    
	    return (mapping.findForward(LOAD_LEARNER));
    }
    
    
    /**
     * finishes the user's tool activity
     * endLearning(HttpServletRequest request, IQaService qaService, HttpServletResponse response) 
		throws IOException, ToolException
     * 
     * @param request
     * @param qaService
     * @param response
     * @throws IOException
     * @throws ToolException
     */
    public void endLearning(HttpServletRequest request, IQaService qaService, HttpServletResponse response) 
		throws IOException, ToolException
	{
    	logger.debug("dispatching endLearning...");
	    /*
	     * The learner is done with the tool session. The tool needs to clean-up.
	     */
		Long toolSessionId=(Long)request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
	    HttpSession ss = SessionManager.getSession();
	    /*get back login user DTO*/
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    logger.debug("leaving the tool: " +
	                 "leaveToolSession() with toolSessionId: " +  toolSessionId + " and user: " + user);
	    
	    logger.debug("set status to COMPLETE");
	    QaSession qaSession = qaService.retrieveQaSessionOrNullById(toolSessionId.longValue());
	    logger.debug("qaSession: " + qaSession);
        qaSession.setSession_end_date(new Date(System.currentTimeMillis()));
        qaSession.setSession_status(COMPLETED); 
        qaService.updateQaSession(qaSession);
        logger.debug("tool session has been marked COMPLETE: " + qaSession);
	    
	    String nextActivityUrl = qaService.leaveToolSession(toolSessionId, new Long(user.getUserID().longValue()));
	    response.sendRedirect(nextActivityUrl);
	}
}