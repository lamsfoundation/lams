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
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;


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
    	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

	 	String toolSessionID=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	 	logger.debug("toolSessionID: " + toolSessionID);
	 	qaLearningForm.setToolSessionID(toolSessionID);
	 	
	 	QaSession qaSession=qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());
	    logger.debug("retrieving qaSession: " + qaSession);
	 	
	    String toolContentID=qaSession.getQaContent().getQaContentId().toString();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    QaContent qaContent=qaSession.getQaContent();
	    logger.debug("using qaContent: " + qaContent);
	    
    	GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
    	
	    LearningUtil.saveFormRequestData(request,  qaLearningForm);
   	
 	    String totalQuestionCount=generalLearnerFlowDTO.getTotalQuestionCount().toString();
 	    logger.debug("totalQuestionCount: " + totalQuestionCount);
 	    int intTotalQuestionCount= new Integer(totalQuestionCount).intValue();
 	   
    	String questionListingMode=generalLearnerFlowDTO.getQuestionListingMode();
    	logger.debug("questionListingMode: " + questionListingMode);
    	
    	Map mapAnswers= new TreeMap(new QaComparator());
        /* if the listing mode is QUESTION_LISTING_MODE_COMBINED populate  the answers here*/
    	if (questionListingMode.equalsIgnoreCase(QUESTION_LISTING_MODE_COMBINED))
    	{
            logger.debug("the listing mode is combined.");
            for (int questionIndex=INITIAL_QUESTION_COUNT.intValue(); questionIndex<= intTotalQuestionCount; questionIndex++ )
            {
                String answer=request.getParameter("answer" + questionIndex);
                logger.debug("answer for question " + questionIndex + " is:" + answer);
                mapAnswers.put(new Long(questionIndex).toString(), answer);
            }
            logger.debug("final mapAnswers for the combined mode:" + mapAnswers);
    	}
    	else
    	{
    	    logger.debug("the listing mode is sequential");
    		if (totalQuestionCount.equals("1"))
    		{
    			logger.debug("totalQuestionCount is 1: " + qaLearningForm.getAnswer());
    			mapAnswers.put(new Long(1).toString(), qaLearningForm.getAnswer());
    		}
    		else 
    		{
    		    logger.debug("populating mapAnswers...");
    		    mapAnswers=populateAnswersMap(qaLearningForm, request, generalLearnerFlowDTO, true, true);
    		    logger.debug("mapAnswers: " + mapAnswers);
    		}
    		logger.debug("final mapAnswers for the sequential mode:" + mapAnswers);
    		
    	}
		logger.debug("using mapAnswers:" + mapAnswers);
    	generalLearnerFlowDTO.setMapAnswers(mapAnswers);

    	/*mapAnswers will be used in the viewAllAnswers screen*/
	    SessionMap sessionMap = new SessionMap();
	    sessionMap.put(MAP_ALL_RESULTS_KEY, mapAnswers);
	    request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	    qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
		generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());
    	
    	request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
    	
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
    	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

	 	String toolSessionID=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	 	logger.debug("toolSessionID: " + toolSessionID);
	 	qaLearningForm.setToolSessionID(toolSessionID);
	 	
	 	QaSession qaSession=qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());
	    logger.debug("retrieving qaSession: " + qaSession);
	 	
	    String toolContentID=qaSession.getQaContent().getQaContentId().toString();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    QaContent qaContent=qaSession.getQaContent();
	    logger.debug("using qaContent: " + qaContent);
	    
    	GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
    	
	    LearningUtil.saveFormRequestData(request,  qaLearningForm);
		
		String isUserNamesVisibleBoolean=generalLearnerFlowDTO.getUserNameVisible();
    	boolean isUserNamesVisible=new Boolean(isUserNamesVisibleBoolean).booleanValue();
    	logger.debug("isUserNamesVisible: " + isUserNamesVisible);

 	    String httpSessionID=qaLearningForm.getHttpSessionID();
 	    logger.debug("httpSessionID: " + httpSessionID);
 	    
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	    logger.debug("sessionMap: " + sessionMap);
	    
	    Map mapAnswers=(Map)sessionMap.get(MAP_ALL_RESULTS_KEY);
	    logger.debug("mapAnswers retrieved: " + mapAnswers);
    	
	    /*recreate the users and responses*/
        LearningUtil learningUtil= new LearningUtil();
        learningUtil.createUsersAndResponses(mapAnswers, request, qaService, new Long(toolContentID), new Long(toolSessionID));
        qaLearningForm.resetUserActions();
        qaLearningForm.setSubmitAnswersContent(null);
        
        learningUtil.setContentInUse(new Long(toolContentID).longValue(), qaService);
        logger.debug("content has been set in use");
        
        logger.debug("start generating learning report...");
        logger.debug("toolContentID: " + toolContentID);


    	QaMonitoringAction qaMonitoringAction= new QaMonitoringAction();
    	qaMonitoringAction.refreshSummaryData(request, qaContent, qaService, isUserNamesVisible, true, toolSessionID, null, generalLearnerFlowDTO);

		generalLearnerFlowDTO.setRequestLearningReport(new Boolean(true).toString());
		generalLearnerFlowDTO.setRequestLearningReportProgress(new Boolean(false).toString());

		request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
		
		logger.debug("final generalLearnerFlowDTO: " + generalLearnerFlowDTO);
		logger.debug("fwd'ing to: " + INDIVIDUAL_LEARNER_REPORT);
		
		logger.debug("remove map holding MAP_ALL_RESULTS_KEY, httpSessionID: " + httpSessionID);
		return (mapping.findForward(INDIVIDUAL_LEARNER_REPORT));
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
    	
		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

	 	String toolSessionID=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	 	logger.debug("toolSessionID: " + toolSessionID);
	 	qaLearningForm.setToolSessionID(toolSessionID);
	 	
	 	QaSession qaSession=qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());
	    logger.debug("retrieving qaSession: " + qaSession);
	 	
	    String toolContentID=qaSession.getQaContent().getQaContentId().toString();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    QaContent qaContent=qaSession.getQaContent();
	    logger.debug("using qaContent: " + qaContent);
	    
    	GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
    	
    	
    	LearningUtil.saveFormRequestData(request,  qaLearningForm);
    	populateAnswersMap(qaLearningForm, request, generalLearnerFlowDTO, true, false);
    	
        return (mapping.findForward(LOAD_LEARNER));
    }
    
    
    public Map populateAnswersMap(ActionForm form, HttpServletRequest request, GeneralLearnerFlowDTO generalLearnerFlowDTO, 
            boolean getNextQuestion, boolean learnerDone)
    {
        logger.debug("learnerDone: " + learnerDone);
        
        logger.debug("getNextQuestion: " + getNextQuestion);
    	QaLearningForm qaLearningForm = (QaLearningForm) form;

 	    String httpSessionID=qaLearningForm.getHttpSessionID();
 	    logger.debug("httpSessionID: " + httpSessionID);
 	    
	    SessionMap sessionMap = (SessionMap) request.getSession().getAttribute(httpSessionID);
	    logger.debug("sessionMap: " + sessionMap);
	    
	    Map mapSequentialAnswers=(Map)sessionMap.get(MAP_SEQUENTIAL_ANSWERS_KEY);
	    logger.debug("pre mapSequentialAnswers: " + mapSequentialAnswers);

        String currentQuestionIndex=qaLearningForm.getCurrentQuestionIndex(); 
        logger.debug("currentQuestionIndex:" + currentQuestionIndex);
        
        logger.debug("getting answer for question: " + currentQuestionIndex + "as: " +  qaLearningForm.getAnswer());
        logger.debug("mapSequentialAnswers size:" + mapSequentialAnswers.size());
        
                
        if  (mapSequentialAnswers.size() >= new Integer(currentQuestionIndex).intValue())
        {
            logger.debug("mapSequentialAnswers size:" + mapSequentialAnswers.size() + " and currentQuestionIndex: " + currentQuestionIndex);
            mapSequentialAnswers.remove(new Long(currentQuestionIndex).toString());
        }
        logger.debug("before adding to mapSequentialAnswers: " + mapSequentialAnswers);
        mapSequentialAnswers.put(new Long(currentQuestionIndex).toString(), qaLearningForm.getAnswer());
        logger.debug("adding new answer:" + qaLearningForm.getAnswer() + " to mapSequentialAnswers.");
        
        logger.debug("updated mapSequentialAnswers:" + mapSequentialAnswers);

        int intCurrentQuestionIndex=new Integer(currentQuestionIndex).intValue(); 
        logger.debug("intCurrentQuestionIndex:" + intCurrentQuestionIndex);
        
        if (getNextQuestion)
            intCurrentQuestionIndex++;
        else
            intCurrentQuestionIndex--;
            
        
        LearningUtil learningUtil= new LearningUtil();
        logger.debug("current map size:" + mapSequentialAnswers.size());
        
        String currentAnswer="";
        if (mapSequentialAnswers.size() >= intCurrentQuestionIndex)
        {
            currentAnswer=(String)mapSequentialAnswers.get(new Long(intCurrentQuestionIndex).toString());
        }
        logger.debug("currentAnswer:" + currentAnswer);
        generalLearnerFlowDTO.setCurrentAnswer(currentAnswer);
        
        logger.debug("currentQuestionIndex will be: " + intCurrentQuestionIndex);
        generalLearnerFlowDTO.setCurrentQuestionIndex(new Integer(intCurrentQuestionIndex));
        
        String totalQuestionCount=qaLearningForm.getTotalQuestionCount();
        logger.debug("totalQuestionCount: " + totalQuestionCount);
        
        String userFeedback=learningUtil.feedBackAnswersProgress(request,intCurrentQuestionIndex,totalQuestionCount);
        logger.debug("userFeedback: " + userFeedback);
        generalLearnerFlowDTO.setUserFeedback(userFeedback);
        
        qaLearningForm.resetUserActions(); /*resets all except submitAnswersContent */
        
	    sessionMap.put(MAP_SEQUENTIAL_ANSWERS_KEY, mapSequentialAnswers);
	    request.getSession().setAttribute(sessionMap.getSessionID(), sessionMap);
	    qaLearningForm.setHttpSessionID(sessionMap.getSessionID());
		generalLearnerFlowDTO.setHttpSessionID(sessionMap.getSessionID());

        request.setAttribute(GENERAL_LEARNER_FLOW_DTO, generalLearnerFlowDTO);
     
        return mapSequentialAnswers;
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
    	
		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

	 	String toolSessionID=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	 	logger.debug("toolSessionID: " + toolSessionID);
	 	qaLearningForm.setToolSessionID(toolSessionID);
	 	
	 	QaSession qaSession=qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());
	    logger.debug("retrieving qaSession: " + qaSession);
	 	
	    String toolContentID=qaSession.getQaContent().getQaContentId().toString();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    QaContent qaContent=qaSession.getQaContent();
	    logger.debug("using qaContent: " + qaContent);
	    
    	GeneralLearnerFlowDTO generalLearnerFlowDTO= LearningUtil.buildGeneralLearnerFlowDTO(qaContent);
	    logger.debug("generalLearnerFlowDTO: " + generalLearnerFlowDTO);
    	
    	LearningUtil.saveFormRequestData(request,  qaLearningForm);
    	populateAnswersMap(qaLearningForm, request, generalLearnerFlowDTO, false, false);
    	
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
    public ActionForward endLearning(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	throws IOException, ServletException, ToolException
	{ 
        logger.debug("dispatching endLearning...");
    	QaLearningForm qaLearningForm = (QaLearningForm) form;        

    	
    	IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("qaService: " + qaService);

	 	String toolSessionID=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	 	logger.debug("toolSessionID: " + toolSessionID);
	 	qaLearningForm.setToolSessionID(toolSessionID);
	 	
	 	QaSession qaSession=qaService.retrieveQaSessionOrNullById(new Long(toolSessionID).longValue());
	    logger.debug("retrieving qaSession: " + qaSession);
	 	
	    String toolContentID=qaSession.getQaContent().getQaContentId().toString();
	    logger.debug("toolContentID: " + toolContentID);
	    
	    QaContent qaContent=qaSession.getQaContent();
	    logger.debug("using qaContent: " + qaContent);
    	
	    LearningUtil.saveFormRequestData(request,  qaLearningForm);
	    
	    /*
	     * The learner is done with the tool session. The tool needs to clean-up.
	     */
	    HttpSession ss = SessionManager.getSession();
	    /*get back login user DTO*/
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    logger.debug("leaving the tool: " +
	                 "leaveToolSession() with toolSessionId: " +  toolSessionID + " and user: " + user);
	    
	    logger.debug("set status to COMPLETE");
	    
	    logger.debug("qaSession: " + qaSession);
        qaSession.setSession_end_date(new Date(System.currentTimeMillis()));
        qaSession.setSession_status(COMPLETED); 
        qaService.updateQaSession(qaSession);
        logger.debug("tool session has been marked COMPLETE: " + qaSession);

	    String httpSessionID=qaLearningForm.getHttpSessionID();
	    logger.debug("removing map with httpSessionID: " + httpSessionID);
	    request.getSession().removeAttribute(httpSessionID);

	    String nextActivityUrl = qaService.leaveToolSession(new Long(toolSessionID), new Long(user.getUserID().longValue()));
	    response.sendRedirect(nextActivityUrl);
	    
	    return null;
	}
}