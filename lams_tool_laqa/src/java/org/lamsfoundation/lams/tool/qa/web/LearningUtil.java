/**
 * 
 * Keeps all operations needed for Learning mode. 
 * @author ozgurd
 *
 */
package org.lamsfoundation.lams.tool.qa.web;

import java.util.Date;
import java.util.Iterator;
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
import org.lamsfoundation.lams.usermanagement.User;
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
	 * NEVER USED: redundant method.
	 * @param request
	 */
	protected void deleteSessionUsersAndResponses(HttpServletRequest request)
    {
        IQaService qaService =QaUtils.getToolService(request);
        /**
         * retrive contentId from the http session
         */
        logger.debug("attempt retrieving toolContentId: " + request.getSession().getAttribute(TOOL_CONTENT_ID));
        Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
        QaContent qaContent=qaService.retrieveQa(toolContentId.longValue());
        
        logger.debug("retrieving qaContent: " + qaContent);
        Iterator sessionIterator=qaContent.getQaSessions().iterator();
        while (sessionIterator.hasNext())
        {
        	QaSession qaSession=(QaSession)sessionIterator.next(); 
        	logger.debug("iterated qaSession : " + qaSession);
        	
        	Iterator sessionUsersIterator=qaSession.getQaQueUsers().iterator();
        	while (sessionUsersIterator.hasNext())
        	{
        		QaQueUsr qaQueUsr=(QaQueUsr) sessionUsersIterator.next();
        		logger.debug("iterated qaQueUsr : " + qaQueUsr);
        		
        		Iterator sessionUsersResponsesIterator=qaQueUsr.getQaUsrResps().iterator();
        		while (sessionUsersResponsesIterator.hasNext())
            	{
        			QaUsrResp qaUsrResp=(QaUsrResp)sessionUsersResponsesIterator.next();
        			logger.debug("iterated qaUsrResp : " + qaUsrResp);
        			qaService.removeUserResponse(qaUsrResp);
        			logger.debug("removed qaUsrResp : " + qaUsrResp);
            	}
        	}
        }
        logger.debug("end of deleteSessionUsersAndResponses()");
    }


	/**
	 * NEVER USED: redundant method.
     * deletes the user of a particular question content and her responses
     * deleteExistingUsersAndResponses(HttpServletRequest request)
     * 
     * return void
     * @param request
     */
    protected void deleteExistingUsersAndResponses(HttpServletRequest request)
    {
        IQaService qaService =QaUtils.getToolService(request);
        /**
         * retrive contentId from the http session
         */
        logger.debug("attempt retrieving toolContentId: " + request.getSession().getAttribute(TOOL_CONTENT_ID));
        Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
        QaContent qaContent=qaService.retrieveQa(toolContentId.longValue());
        
        logger.debug("retrieving qaContent: " + qaContent);
        
        /**
         * get iterator of questions collection
         */
    	Iterator contentIterator=qaContent.getQaQueContents().iterator();
    	while (contentIterator.hasNext())
    	{
    		QaQueContent qaQueContent=(QaQueContent)contentIterator.next();
    		logger.debug("retrieving qaQueContent: " + qaQueContent);
    		if (qaQueContent != null)
    		{
    			Set qaQueUsers=qaQueContent.getQaQueUsers();
    			logger.debug("qaQueUsers size: " + qaQueUsers.size());
        		/**
        		 * iterate users and delete them
        		 */
        		Iterator usersIterator=qaQueUsers.iterator();
        		while (usersIterator.hasNext())
            	{
        			QaQueUsr qaQueUsr=(QaQueUsr) usersIterator.next();
        			logger.debug("retrieving qaQueUsr: " + qaQueUsr);
        			if (qaQueUsr != null)
        			{
        				Set qaUsrResps=qaQueUsr.getQaUsrResps();
    					logger.debug("retrieving user's all responses : " + qaUsrResps);
        				Iterator responsesIterator=qaUsrResps.iterator();
        				while (responsesIterator.hasNext())
        				{
        					QaUsrResp qaUsrResp=(QaUsrResp)responsesIterator.next();
        					if (qaUsrResp != null)
        					{
        						logger.debug("retrieving qaUsrResp: " + qaUsrResp);
        						qaService.removeUserResponse(qaUsrResp);
								logger.debug("removed qaUsrResp with id:"  + qaUsrResp.getResponseId());
        					}
        				}
        				qaService.deleteQaQueUsr(qaQueUsr);
        				logger.debug("deleted user: " + qaQueUsr);
        			}
            	}
    		}
    	}
    	logger.debug("end of deleteExistingUsersAndResponses()");
    }

    
    /**
     * createUsersAndResponses(Map mapAnswers, HttpServletRequest request)
     * create users of the responses
     * @param mapAnswers, request
     * return void
     *
     */
    protected void createUsersAndResponses(Map mapAnswers, HttpServletRequest request)
    {
    	IQaService qaService =QaUtils.getToolService(request);
        logger.debug("createUsers-retrieving qaService: " + qaService);
        
	    HttpSession ss = SessionManager.getSession();
	    //get back login user DTO
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
    	logger.debug("retrieving toolUser: " + toolUser);
    	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());
    	logger.debug("retrieving toolUser username: " + toolUser.getLogin());
    	/**
    	 * !!double check this!!
    	 */
    	String userName=toolUser.getLogin(); 
    	String fullName= toolUser.getFirstName() + " " + toolUser.getLastName();
    	logger.debug("retrieving toolUser fullname: " + fullName);
    	
    	Long userId=new Long(toolUser.getUserID().longValue());
    	
        /**
         * retrive contentId from the http session
         */
        logger.debug("createUsers-attempt retrieving toolContentId: " + request.getSession().getAttribute(TOOL_CONTENT_ID));
        Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
        /**
         * obtain QaContent to be used in creating QaQueUsr
         */  
        QaContent qaContent=qaService.retrieveQa(toolContentId.longValue());
        logger.debug("createUsers-retrieving qaContent: " + qaContent);

        /**
         * get QaSession to be used in creating QaQueUsr
         */
        Long toolSessionId=(Long)request.getSession().getAttribute(TOOL_SESSION_ID);
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
    	if (qaQueUsr != null)
        {
        	qaService.createQaQueUsr(qaQueUsr);
            logger.debug("createUsers-qaQueUsr created in the db: " + qaQueUsr);	
        }
    	
        while (contentIterator.hasNext())
    	{
    		QaQueContent qaQueContent=(QaQueContent)contentIterator.next();
    		if (qaQueContent != null)
    		{
    	        /**
                 * get the question
                 */
        		String question=qaQueContent.getQuestion();
        		/**
        		 * get the question's display order
        		 */
        		String displayOrder=new Long(qaQueContent.getDisplayOrder()).toString();
        		
        		/**
        		 * get the answer from the map of that displayOrder
        		 */
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
    protected void buidLearnerReport(HttpServletRequest request, int toolSessionIdCounter)
    {
    	IQaService qaService =QaUtils.getToolService(request);
    	
    	/**
    	 * find out the current tool session
    	 */
    	Long toolSessionId=(Long) request.getSession().getAttribute(TOOL_SESSION_ID);
    	logger.debug("processUserResponses using toolSessionId: " + toolSessionId);
    	
    	/**
    	 * holds all the tool sessions passed in from the url
    	 * used in monitoring mode
    	 */
    	Map mapToolSessions=(Map)request.getSession().getAttribute(MAP_TOOL_SESSIONS);
		logger.debug("retrieving MAP_TOOL_SESSIONS from the session: " + mapToolSessions);
		
		
		/**
    	 * keys of the Map refers to questions and values refers to user info and their entries
    	 * used in learning mode
    	 */
    	Map mapMainReport= new TreeMap(new QaStringComparator());
    	logger.debug("mapMainReport created with QaStringComparator");
    	
    	Long toolContentId=(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("current toolContentId: " + toolContentId);
    	QaContent qaContent=qaService.loadQa(toolContentId.longValue());
    	logger.debug("retrieve qaContent: " + qaContent);
    	
    	Iterator contentIterator=qaContent.getQaQueContents().iterator();
    	logger.debug("content iteration count: " + qaContent.getQaQueContents().size());
    	
    	int questionIndex=0;
    	/**
    	 * used to iterate responses to questions within the tool sessions in the jsp level in the monitoring mode
    	 */
    	Map mapQuestions= new TreeMap(new QaStringComparator());
    	logger.debug("mapQuestions created with QaStringComparator");
    	
    	/**
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
        		/**
        		 * obtain All responses from ALL users to this question
        		 */
        		Set qaUsrResps=qaQueContent.getQaUsrResps();
        		logger.debug("the set of user responses qaUsrResps: " + qaUsrResps);
        		
        		/**
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
            		/**
            		 * Map mapQuestions for holding the question index /responses pair 
            		 */
            		mapQuestions.put(new Integer(questionIndex).toString(),mapUserResponses);
            		logger.debug("getting mapQuestions: " + mapQuestions);
            		/**
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
    	
    	/**
    	 * find out the current tool session
    	 */
    	Long toolSessionId=(Long) request.getSession().getAttribute(TOOL_SESSION_ID);
    	logger.debug("processUserResponses using toolSessionId: " + toolSessionId);
    	
    	/**
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
        	/**
        	 * obtain each response and the user that replied with that response 
        	 */
        	int responseIndex=0;
        	while (itResps.hasNext())
        	{
        		QaUsrResp qaUsrResp=(QaUsrResp)itResps.next();
        		logger.debug("using qaUsrResp: " + qaUsrResp + " with responseIndex: " + responseIndex);
        		/**
        		 * Don't include the blank answers in the report. Make sure it is the requirement.
        		 */
        		if ((qaUsrResp != null) && (qaUsrResp.getAnswer() != null) && (!qaUsrResp.getAnswer().equals("")))
        		{
        			logger.debug("iterated qaUsrResp:" + qaUsrResp);
        			logger.debug("isResponseHidden:   " + qaUsrResp.isHidden());
    	    		QaQueUsr qaQueUsr=qaUsrResp.getQaQueUser();
    	    		//find out what you need to display: fullname or login (userName)?
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
    		/**
        	 * find out whos is the current user. Important to know for reporting responses in learning mode 
        	 */
    	    HttpSession ss = SessionManager.getSession();
    	    //get back login user DTO
    	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
        	logger.debug("retrieving toolUser: " + toolUser + " userName: " + toolUser.getLogin());
        	/**
        	 * !!double check if String userName=toolUser.getLogin(); 
        	 */
        	
        	/**
        	 * see whether you have to report only the current learner's name and make invisible all the other learner's names in the report. 
        	 * A boolean true isUsernameVisible indicates that all user names to be displayed.
        	 * Only applies to learning mode.
        	 */
        	
        	Boolean isUsernameVisible=(Boolean)request.getSession().getAttribute(IS_USERNAME_VISIBLE);
        	logger.debug("IS_USERNAME_VISIBLE: " + isUsernameVisible);
    		
    		
        	if (qaUsrResps.isEmpty())
        		logger.debug("the set of user responses for a particular question is empty.");	
        	
        	Iterator itResps=qaUsrResps.iterator();
        	/**
        	 * obtain each response and the user that replied with that response 
        	 */
        	int responseIndex=0;
        	while (itResps.hasNext())
        	{
        		QaUsrResp qaUsrResp=(QaUsrResp)itResps.next();
        		logger.debug("using qaUsrResp: " + qaUsrResp + " with responseIndex: " + responseIndex);
        		/**
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
    	    		
    	    		/**
    	    		 * find out if the current tool user's login(userName) is the same as the iterated user's userName.
    	    		 * If there is a match we need to display that person first in the report
    	    		 * !!do this when User objkect issue is resolved!!!
    	    		 */
	    		
    	    		/**
	    			 * these are all the other users. See if we have the permission to display their names.
	    			 * if we are permitted, get ready to display all available information
	    			 */
    	    			
    	    		/**
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
			    			else /** we won't display the usernames of these users*/
			    			{
			    				logger.debug("IS_USERNAME_VISIBLE:" + isUsernameVisible.booleanValue());
			    				mapUserResponses.put(new Integer(responseIndex).toString(), " " + qaUsrResp.getAttemptTime() + " " + qaUsrResp.getAnswer());
		        	    		logger.debug("Building request level response data with: " + 
		        	    															"aTime" + questionIndex +""+ responseIndex);
			    			}
			    			/**
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
    
    public void lockContent(HttpServletRequest request)
    {
    	IQaService qaService =QaUtils.getToolService(request);
        Long toolContentId=(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
    	logger.debug("current toolContentId: " + toolContentId);
    	QaContent qaContent=qaService.loadQa(toolContentId.longValue());
    	logger.debug("retrieve qaContent: " + qaContent);
    	
        qaContent.setContentLocked(true);
        logger.debug("content with id : " + toolContentId + "has been marked LOCKED");
        qaService.updateQa(qaContent);
        logger.debug("content with id : " + toolContentId + "has been marked LOCKED and updated in the db");
    }
}
