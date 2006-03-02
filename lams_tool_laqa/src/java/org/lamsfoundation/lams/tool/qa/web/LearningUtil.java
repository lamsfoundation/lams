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
package org.lamsfoundation.lams.tool.qa.web;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaStringComparator;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * Keeps all operations needed for Learning mode. 
 * @author Ozgur Demirtas
 *
 */
public class LearningUtil implements QaAppConstants{
	static Logger logger = Logger.getLogger(LearningUtil.class.getName());

    /**
     * createUsersAndResponses(Map mapAnswers, HttpServletRequest request)
     * create users of the responses
     * @param mapAnswers, request
     * return void
     *
     */
    protected void createUsersAndResponses(Map mapAnswers, HttpServletRequest request, IQaService qaService)
    {
        logger.debug("createUsers-retrieving qaService: " + qaService);
        
	    HttpSession ss = SessionManager.getSession();
	    /* get back login user DTO */
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
    	logger.debug("retrieving toolUser: " + toolUser);
    	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());
    	logger.debug("retrieving toolUser username: " + toolUser.getLogin());
    	/*
    	 * !!double check this!!
    	 */
    	String userName=toolUser.getLogin(); 
    	String fullName= toolUser.getFirstName() + " " + toolUser.getLastName();
    	logger.debug("retrieving toolUser fullname: " + fullName);
    	
    	Long userId=new Long(toolUser.getUserID().longValue());
    	
        /*
         * retrive contentId from the http session
         */
        logger.debug("createUsers-attempt retrieving toolContentId: " + request.getSession().getAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID));
        Long toolContentId=(Long)request.getSession().getAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID);
        /*
         * obtain QaContent to be used in creating QaQueUsr
         */  
        QaContent qaContent=qaService.retrieveQa(toolContentId.longValue());
        logger.debug("createUsers-retrieving qaContent: " + qaContent);

        /*
         * get QaSession to be used in creating QaQueUsr
         */
        Long toolSessionId=(Long)request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
        logger.debug("createUsers-retrieving toolSessionId: " + toolSessionId);
        QaSession qaSession = qaService.retrieveQaSessionOrNullById(toolSessionId.longValue()); 
        logger.debug("createUsers-retrieving qaSession: " + qaSession);
        
        Iterator contentIterator=qaContent.getQaQueContents().iterator();
    	logger.debug("createUsers-attempt iteration questions");
    	
    	QaQueUsr qaQueUsr= new QaQueUsr(userId,
										userName,
										fullName,
										null, 
										qaSession, 
										new TreeSet());
						    	
    	logger.debug("createQaQueUsr - qaQueUsr: " + qaQueUsr);
    	
    	/*note that it is possible for a user to already exist from another tool session. In this case don't add any more user record*/
    	QaQueUsr qaQueUsrLocal=qaService.getQaQueUsrById(userId.longValue());
    	logger.debug("qaQueUsrLocal: " + qaQueUsrLocal);
    	
    	if ((qaQueUsr != null) && (qaQueUsrLocal == null)) 
        {
        	qaService.createQaQueUsr(qaQueUsr);
            logger.debug("createUsers-qaQueUsr created in the db: " + qaQueUsr);	
        }
    	else
    	{
    		logger.debug("assign user");
    		qaQueUsr=qaQueUsrLocal;
    	}
    	
        while (contentIterator.hasNext())
    	{
    		QaQueContent qaQueContent=(QaQueContent)contentIterator.next();
    		if (qaQueContent != null)
    		{
        		String question=qaQueContent.getQuestion();
        		String displayOrder=new Long(qaQueContent.getDisplayOrder()).toString();
        		String answer=(String)mapAnswers.get(displayOrder);
        		
                logger.debug("iterationg question-answers: displayOrder: " + displayOrder + 
         													 " question: " + question + " answer: " + answer);
        		
                String timezoneId=(String)request.getSession().getAttribute(TIMEZONE_ID);
                if (timezoneId == null) timezoneId="";
                
                
            	QaUsrResp qaUsrResp= new QaUsrResp(answer,false,
						new Date(System.currentTimeMillis()),
						timezoneId,
						qaQueContent,
						qaQueUsr); 

				logger.debug("iterationg qaUsrResp: " + qaUsrResp);
				if (qaUsrResp != null)
				{
					qaService.createQaUsrResp(qaUsrResp);
					logger.debug("created qaUsrResp in the db");	
				}
                
    		}
        }
        logger.debug("both the users and their responses created in the db");
    }

    
    /**
     * creates a data structure to hold learner report data
     * buidLearnerReport(HttpServletRequest request, Map mapQuestions, Map mapAnswers)
     * 
     * calls processUserResponses() to do some of the work
     * @param HttpServletRequest request, Map mapQuestions, Map mapAnswers
     * return void
     *
     */
    protected void buidLearnerReport(HttpServletRequest request, int toolSessionIdCounter, IQaService qaService)
    {    	
    	/*
    	 * find out the current tool session
    	 */
    	Long toolSessionId=(Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
    	logger.debug("processUserResponses using toolSessionId: " + toolSessionId);
    	
    	/*
    	 * holds all the tool sessions passed in from the url
    	 * used in monitoring mode
    	 */
    	Map mapToolSessions=(Map)request.getSession().getAttribute(MAP_TOOL_SESSIONS);
		logger.debug("retrieving MAP_TOOL_SESSIONS from the session: " + mapToolSessions);
		
		/*
    	 * keys of the Map refers to questions and values refers to user info and their entries
    	 * used in learning mode
    	 */
    	Map mapMainReport= new TreeMap(new QaStringComparator());
    	logger.debug("mapMainReport created with QaStringComparator");
    	
    	Long toolContentId=(Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID);
    	logger.debug("current toolContentId: " + toolContentId);
    	QaContent qaContent=qaService.loadQa(toolContentId.longValue());
    	logger.debug("retrieve qaContent: " + qaContent);
    	
        /*
         * Reason for commentting the line below. When the user response is updated in monitor
         * qaContent.getQaQueContents() would failed for some reason. Hence I've created a new
         * method retrieveQaQueContentsByToolContentId() to work around this problem. I'm not
         * sure why qaContent.getQaQueContents() fails but I assume it's a transation problem. 
         * 
         * Anthony
         */
        //Set qaQueContents = qaContent.getQaQueContents();
        List qaQueContents = qaService.retrieveQaQueContentsByToolContentId(toolContentId.longValue());
    	Iterator contentIterator=qaQueContents.iterator();
        
    	logger.debug("content iteration count: " + qaQueContents.size());
    	
    	int questionIndex=0;
    	/*
    	 * used to iterate responses to questions within the tool sessions in the jsp level in the monitoring mode
    	 */
    	Map mapQuestions= new TreeMap(new QaStringComparator());
    	logger.debug("mapQuestions created with QaStringComparator");
    	
    	/*
    	 * used to iterate questions within the tool sessions in the jsp level in the monitoring mode
    	 */
    	Map mapMonitoringQuestions= new TreeMap(new QaStringComparator());
    	logger.debug("mapMonitoringQuestions created with QaStringComparator");
    	
    	while (contentIterator.hasNext())
    	{
    		QaQueContent qaQueContent=(QaQueContent)contentIterator.next();
        	logger.debug("retrieve qaQueContent: " + qaQueContent);
        	if (qaQueContent != null)
    		{
        		/*
        		 * obtain All responses from ALL users to this question
        		 */
        		Set qaUsrResps=qaQueContent.getQaUsrResps();
        		logger.debug("the set of user responses qaUsrResps: " + qaUsrResps);
        		
        		/*
        		 * deal with all responses
        		 */
        		questionIndex=questionIndex+1;
        		logger.debug("current questionIndex: " + questionIndex);
        		
        		String targetMode=(String) request.getSession().getAttribute(TARGET_MODE);
            	logger.debug("TARGET_MODE: " + targetMode);
            	
            	logger.debug("buidLearnerReport for TARGET_MODE: " + targetMode);
            	if (targetMode.equalsIgnoreCase(TARGET_MODE_MONITORING))
            	{
            		Map mapUserResponses=processUserResponses(request, qaUsrResps, questionIndex, new TreeMap());
            		/*
            		 * Map mapQuestions for holding the question index /responses pair 
            		 */
            		mapQuestions.put(new Integer(questionIndex).toString(),mapUserResponses);
            		logger.debug("getting mapQuestions: " + mapQuestions);
            		/*
            		 * Map mapQuestions for holding the questions themselves 
            		 */
            		mapMonitoringQuestions.put(new Integer(questionIndex).toString(),qaQueContent.getQuestion());
            		logger.debug("getting mapMonitoringQuestions: " + mapMonitoringQuestions);
            		request.getSession().setAttribute(MAP_MONITORING_QUESTIONS,mapMonitoringQuestions);
            		
            		mapToolSessions.put(toolSessionId.toString(), mapQuestions);
            		logger.debug("mapToolSessionsupdated: " + mapToolSessions);
            		request.getSession().setAttribute(MAP_TOOL_SESSIONS,mapToolSessions);
            		logger.debug("MAP_TOOL_SESSIONS  in session updated: " + mapToolSessions);	
            	}
            	else
            	{
            		Map mapUserResponses= new TreeMap(new QaComparator());
            		mapUserResponses=processUserResponses(request, qaUsrResps, questionIndex, mapUserResponses);
            		mapMainReport.put(qaQueContent.getQuestion(), mapUserResponses);	
            	}
        	}
    	}
		logger.debug("mapMainReport: " + mapMainReport);
		request.getSession().setAttribute(MAP_MAIN_REPORT, mapMainReport);
    }
    
    
    /**
     * This method prepares the report to eb presented in both leraning and monitoring mode.
     * In learning mode in places the curresnt user's data in the first row.
     * In monitoring mode, it just presents data as it reads into the Map as usual.
     * 
     * 
     * Reporting in Learning mode is different than reporting in monitoring mode in two main respects:
     * 1- Use of username visible applies only to learning mode
     * 2- Placing the current user's name first in the generated list only applies to learning mode 
     *  
     * prepares ALL responses to a particular question and 
     * makes it available to the UI.
     * processUserResponses(Set qaUsrResps)
     * return void
     * @param qaUsrResps
     */
    protected Map processUserResponses(HttpServletRequest request, Set qaUsrResps, int questionIndex, Map mapUserResponses)
    {
    	logger.debug("will be processing user responses: ");
    	
    	/*
    	 * find out the current tool session
    	 */
    	Long toolSessionId=(Long) request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
    	logger.debug("processUserResponses using toolSessionId: " + toolSessionId);
    	
    	/*
    	 * find out the tool's mode. We produce different reports for learning and monitoring
    	 */
    	String targetMode=(String)request.getSession().getAttribute(TARGET_MODE);
    	logger.debug("TARGET_MODE: " + targetMode);
    	
    	if (targetMode.equalsIgnoreCase(TARGET_MODE_MONITORING))
		{
    		logger.debug("processUserResponses for TARGET_MODE: " + targetMode);
    		
    		if (qaUsrResps.isEmpty())
        		logger.debug("the set of user responses for a particular question is empty.");	
        	
        	Iterator itResps=qaUsrResps.iterator();
        	/*
        	 * obtain each response and the user that replied with that response 
        	 */
        	int responseIndex=0;
        	while (itResps.hasNext())
        	{
        		QaUsrResp qaUsrResp=(QaUsrResp)itResps.next();
        		logger.debug("using qaUsrResp: " + qaUsrResp + " with responseIndex: " + responseIndex);
        		/*
        		 * Don't include the blank answers in the report. Make sure it is the requirement.
        		 */
        		if ((qaUsrResp != null) && (qaUsrResp.getAnswer() != null) && (!qaUsrResp.getAnswer().equals("")))
        		{
        			logger.debug("iterated qaUsrResp:" + qaUsrResp);
        			logger.debug("isResponseHidden:   " + qaUsrResp.isHidden());
    	    		QaQueUsr qaQueUsr=qaUsrResp.getQaQueUser();
    	    		/* find out what you need to display: fullname or login (userName)? */
    	    		logger.debug("iterated qaQueUsr fullName:" + qaQueUsr.getFullname());
    	    		logger.debug("iterated qaQueUsr userName:" + qaQueUsr.getUsername());
    	    		logger.debug("iterated qaQueUsr userName:" + qaQueUsr.getQaSession());
    	    		logger.debug("using responseIndex: " + responseIndex);
    	    		
    	    		QaSession qaSession=qaQueUsr.getQaSession();
    	    		if (toolSessionId.equals(qaSession.getQaSessionId()))
    	    		{
    	    			responseIndex=responseIndex+1;
		    			logger.debug("responseIndex incremented to: " + responseIndex);
		    			mapUserResponses.put(new Integer(responseIndex).toString(), qaQueUsr.getFullname() + " " + qaUsrResp.getAttemptTime() + " " + qaUsrResp.getAnswer());
		    			logger.debug("setting attemptTime:   " + "aTime" + questionIndex +""+ responseIndex + "---------" + qaUsrResp.getAttemptTime());
		    			logger.debug("setting final monitoring report data with:" + toolSessionId + " " + questionIndex + " " + responseIndex);
		    			
		    			request.getSession().setAttribute(FULLNAME + toolSessionId + "" + questionIndex +""+ responseIndex, qaQueUsr.getFullname());	
		    			request.getSession().setAttribute(ANSWER + toolSessionId + "" + questionIndex +""+ responseIndex, qaUsrResp.getAnswer());
		    			request.getSession().setAttribute(ATIME + toolSessionId + "" + questionIndex +""+ responseIndex, qaUsrResp.getAttemptTime());
		    			request.getSession().setAttribute(TIMEZONE_ID + questionIndex + "" + responseIndex, qaUsrResp.getTimezone());
		    			request.getSession().setAttribute(RESPONSE_ID + toolSessionId + "" + questionIndex +""+ responseIndex, qaUsrResp.getResponseId());
		    			
		    			boolean isResponseHidden=qaUsrResp.isHidden();
		    			logger.debug("isResponseHidden:   " + isResponseHidden);
	    				request.getSession().setAttribute(RESPONSE_HIDDEN + toolSessionId + "" + questionIndex +""+ responseIndex, new Boolean(isResponseHidden));
    	    		}
    	    	}
        	 }
    	}
    	else if (targetMode.equalsIgnoreCase(TARGET_MODE_LEARNING))
    	{
    		logger.debug("processUserResponses for TARGET_MODE: " + targetMode);
    		/*
        	 * find out whos is the current user. Important to know for reporting responses in learning mode 
        	 */
    	    HttpSession ss = SessionManager.getSession();
    	    /* get back login user DTO */
    	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
        	logger.debug("retrieving toolUser: " + toolUser + " userName: " + toolUser.getLogin());
        	/*
        	 * !!double check if String userName=toolUser.getLogin(); 
        	 */
        	
        	/*
        	 * see whether you have to report only the current learner's name and make invisible all the other learner's names in the report. 
        	 * A boolean true isUsernameVisible indicates that all user names to be displayed.
        	 * Only applies to learning mode.
        	 */
        	
        	Boolean isUsernameVisible=(Boolean)request.getSession().getAttribute(IS_USERNAME_VISIBLE);
        	logger.debug("IS_USERNAME_VISIBLE: " + isUsernameVisible);
    		
    		
        	if (qaUsrResps.isEmpty())
        		logger.debug("the set of user responses for a particular question is empty.");	
        	
        	Iterator itResps=qaUsrResps.iterator();
        	/*
        	 * obtain each response and the user that replied with that response 
        	 */
        	int responseIndex=0;
        	while (itResps.hasNext())
        	{
        		QaUsrResp qaUsrResp=(QaUsrResp)itResps.next();
        		logger.debug("using qaUsrResp: " + qaUsrResp + " with responseIndex: " + responseIndex);
        		/*
        		 * Don't include the blank answers in the report. Make sure it is the requirement.
        		 */
        		if ((qaUsrResp != null) && (!qaUsrResp.isHidden()) && (qaUsrResp.getAnswer() != null) && (!qaUsrResp.getAnswer().equals("")))
        		{
        			logger.debug("iterated qaUsrResp:" + qaUsrResp);
    	    		QaQueUsr qaQueUsr=qaUsrResp.getQaQueUser();
    	    		//find out what you need to display: fullname or login (userName)?
    	    		logger.debug("iterated qaQueUsr fullName:" + qaQueUsr.getFullname());
    	    		logger.debug("iterated qaQueUsr userName:" + qaQueUsr.getUsername());
    	    		logger.debug("iterated qaQueUsr userName:" + qaQueUsr.getQaSession());
    	    		logger.debug("using responseIndex: " + responseIndex);
    	    		
    	    		/*
    	    		 * find out if the current tool user's login(userName) is the same as the iterated user's userName.
    	    		 * If there is a match we need to display that person first in the report
    	    		 * !!do this when User objkect issue is resolved!!!
    	    		 */
	    		
    	    		/*
	    			 * these are all the other users. See if we have the permission to display their names.
	    			 * if we are permitted, get ready to display all available information
	    			 */
    	    			
    	    		/*
    	    		 * get user's tool session
    	    		 */
    	    		QaSession qaSession=qaQueUsr.getQaSession();
    	    		logger.debug("iterated qaQueUsr userName:" + qaSession.getQaSessionId());
    	    		logger.debug("incoming toolSessionId versus user's toolSessionId:" + toolSessionId + " versus " + qaSession.getQaSessionId());
    	    		    	    		
    	    		logger.debug("toolSessionId versus iterated session id: " + toolSessionId + "--" + qaSession.getQaSessionId());
    	    		logger.debug("learner report includes only those responses in the same tool session: " + toolSessionId);
    	    		if (toolSessionId.equals(qaSession.getQaSessionId()))
    	    		{
    	    			
	    	    			responseIndex=responseIndex+1;
	    	    			logger.debug("responseIndex incremented to: " + responseIndex);
			    			if (isUsernameVisible.booleanValue())
			    			{
			    				logger.debug("IS_USERNAME_VISIBLE:" + isUsernameVisible.booleanValue());
		    	    			mapUserResponses.put(new Integer(responseIndex).toString(), qaQueUsr.getFullname() + " " + qaUsrResp.getAttemptTime() + " " + qaUsrResp.getAnswer());
		        	    		logger.debug("Building request level response data with: " + 
		        	    															"fullname" + questionIndex +""+ responseIndex);
		        	    		request.getSession().setAttribute(FULLNAME + questionIndex +""+ responseIndex, qaQueUsr.getFullname());
		        	    	
			    			}
			    			else /* we won't display the usernames of these users*/
			    			{
			    				logger.debug("IS_USERNAME_VISIBLE:" + isUsernameVisible.booleanValue());
			    				mapUserResponses.put(new Integer(responseIndex).toString(), " " + qaUsrResp.getAttemptTime() + " " + qaUsrResp.getAnswer());
		        	    		logger.debug("Building request level response data with: " + 
		        	    															"aTime" + questionIndex +""+ responseIndex);
			    			}
			    			/*
			    			 * place these whether username visible or not
			    			 */
			    			
			    			logger.debug("setting attemptTime:   " + "aTime" + questionIndex +""+ responseIndex + 
			    																"---------" + qaUsrResp.getAttemptTime() +"-----" + qaUsrResp.getTimezone() );
			    			
			    			request.getSession().setAttribute(ANSWER + questionIndex +""+ responseIndex, qaUsrResp.getAnswer());
			    			request.getSession().setAttribute(ATIME + questionIndex +""+ responseIndex, qaUsrResp.getAttemptTime());
			    			request.getSession().setAttribute(FORMATTED_ATIME + questionIndex +""+ responseIndex, QaUtils.getFormattedDateString(qaUsrResp.getAttemptTime()));
			    			logger.debug("setting formattedDatetime");
			    			request.getSession().setAttribute(TIMEZONE_ID + questionIndex +""+ responseIndex, qaUsrResp.getTimezone());
						    			
		    	    		if (qaQueUsr.getUsername().equalsIgnoreCase(toolUser.getLogin()))
		    	    		{
		    	    			request.getSession().setAttribute(FULLNAME + questionIndex +""+ responseIndex, qaQueUsr.getFullname());
		    	    			logger.debug("include fullName for current learner:   " + "fullName" + questionIndex +""+ responseIndex + "---" + qaQueUsr.getFullname());
		    	    			logger.debug("current learner:" + qaQueUsr.getUsername());
		    	    			request.getSession().setAttribute(CURRENTLEARNER_FULLNAME , qaQueUsr.getFullname());
		    	    			logger.debug("current learner fullname:" + qaQueUsr.getFullname());
		    	    		}
    	    		}
    	    	}
        	 }
    	}
    	logger.debug("Learner report MAP_USER_RESPONSES: " + mapUserResponses);
    	return mapUserResponses;
   	}

    
    /**
	 * feedBackAnswersProgress(HttpServletRequest request, int currentQuestionIndex)
	 * give user feedback on the remaining questions
	 * @param qaLearningForm
	 * return void
	 */
    protected void feedBackAnswersProgress(HttpServletRequest request, int currentQuestionIndex)
    {
    	String totalQuestionCount=(String)request.getSession().getAttribute(TOTAL_QUESTION_COUNT);
    	logger.debug("totalQuestionCount: " + totalQuestionCount);
    	int remainingQuestionCount=new Long(totalQuestionCount).intValue() - currentQuestionIndex +1;
    	logger.debug("remainingQuestionCount: " + remainingQuestionCount);
    	String userFeedback="";
    	if (remainingQuestionCount != 0)
    		userFeedback= "Remaining question count: " + remainingQuestionCount;
    	else
    		userFeedback= "End of the questions.";
    			
    	request.getSession().setAttribute(USER_FEEDBACK, userFeedback);
    }
    
    public void lockContent(long toolContentId, IQaService qaService)
    {
    	QaContent qaContent=qaService.loadQa(toolContentId);
    	logger.debug("retrieve qaContent: " + qaContent);
    	
        qaContent.setContentLocked(true);
        logger.debug("content with id : " + toolContentId + "has been marked LOCKED");
        qaService.updateQa(qaContent);
        logger.debug("content with id : " + toolContentId + "has been marked LOCKED and updated in the db");
    }

}
