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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.web;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.qa.GeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
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
	
	public static void saveFormRequestData(HttpServletRequest request, QaLearningForm qaLearningForm)
	{
	 	String toolSessionID=request.getParameter("toolSessionID");
	 	logger.debug("toolSessionID: " + toolSessionID);
	 	qaLearningForm.setToolSessionID(toolSessionID);

	 	String httpSessionID=request.getParameter("httpSessionID");
	 	logger.debug("httpSessionID: " + httpSessionID);
	 	qaLearningForm.setHttpSessionID(httpSessionID);
	 	
	 	String totalQuestionCount=request.getParameter("totalQuestionCount");
	 	logger.debug("totalQuestionCount: " + totalQuestionCount);
	 	qaLearningForm.setTotalQuestionCount(totalQuestionCount);
	 	

	 	logger.debug("done saving form request data.");
	}

	
    public static GeneralLearnerFlowDTO buildGeneralLearnerFlowDTO(QaContent qaContent)
    {
        logger.debug("starting buildMcGeneralLearnerFlowDTO: " + qaContent);
        GeneralLearnerFlowDTO generalLearnerFlowDTO =new GeneralLearnerFlowDTO();
        generalLearnerFlowDTO.setActivityTitle(qaContent.getTitle());
        generalLearnerFlowDTO.setActivityInstructions(qaContent.getInstructions());
        generalLearnerFlowDTO.setReportTitleLearner(qaContent.getReportTitle());
        
        if (qaContent.isQuestionsSequenced()) 
            generalLearnerFlowDTO.setQuestionListingMode(QUESTION_LISTING_MODE_SEQUENTIAL);
        else
            generalLearnerFlowDTO.setQuestionListingMode(QUESTION_LISTING_MODE_COMBINED);
        
        
        generalLearnerFlowDTO.setUserNameVisible(new Boolean(qaContent.isUsernameVisible()).toString()); 
        generalLearnerFlowDTO.setActivityOffline(new Boolean(qaContent.isRunOffline()).toString());
        
        logger.debug("continue buildGeneralLearnerFlowDTO: " + qaContent);
        generalLearnerFlowDTO.setTotalQuestionCount(new Integer(qaContent.getQaQueContents().size()));
        logger.debug("final generalLearnerFlowDTO: " + generalLearnerFlowDTO);
        
        Map mapQuestions= new TreeMap(new QaComparator());
        
    	Iterator contentIterator=qaContent.getQaQueContents().iterator();
    	while (contentIterator.hasNext())
    	{
    		QaQueContent qaQueContent=(QaQueContent)contentIterator.next();
    		if (qaQueContent != null)
    		{
    			int displayOrder=qaQueContent.getDisplayOrder();
        		if (displayOrder != 0)
        		{
        			/*
    	    		 *  add the question to the questions Map in the displayOrder
    	    		 */
            		mapQuestions.put(new Integer(displayOrder).toString(),qaQueContent.getQuestion());
        		}
    		}
    	}
		
    	generalLearnerFlowDTO.setMapQuestionContentLearner(mapQuestions);
        return generalLearnerFlowDTO;
    }

    /**
     * createUsersAndResponses(Map mapAnswers, HttpServletRequest request)
     * create users of the responses
     * @param mapAnswers, request
     * return void
     *
     */
    protected void createUsersAndResponses(Map mapAnswers, HttpServletRequest request, IQaService qaService, 
            Long toolContentID, Long toolSessionID)
    {
        logger.debug("createUsers-retrieving qaService: " + qaService);
        logger.debug("mapAnswers: " + mapAnswers);
        logger.debug("toolContentID: " + toolContentID);
        logger.debug("toolSessionID: " + toolSessionID);
        
	    HttpSession ss = SessionManager.getSession();
	    /* get back login user DTO */
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
    	logger.debug("retrieving toolUser: " + toolUser);
    	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());
    	logger.debug("retrieving toolUser username: " + toolUser.getLogin());

    	String userName=toolUser.getLogin(); 
    	String fullName= toolUser.getFirstName() + " " + toolUser.getLastName();
    	logger.debug("retrieving toolUser fullname: " + fullName);
    	
    	Long userId=new Long(toolUser.getUserID().longValue());
    	
        /*
         * obtain QaContent to be used in creating QaQueUsr
         */  
        QaContent qaContent=qaService.retrieveQa(toolContentID.longValue());
        logger.debug("createUsers-retrieving qaContent: " + qaContent);

        QaSession qaSession = qaService.retrieveQaSessionOrNullById(toolSessionID.longValue()); 
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
    	
    	logger.debug("session uid: " + qaSession.getUid());
    	/*note that it is possible for a user to already exist from another tool session. In this case don't add any more user record*/
    	QaQueUsr qaQueUsrLocal=qaService.getQaUserBySession(userId, qaSession.getUid());
    	logger.debug("qaQueUsrLocal: " + qaQueUsrLocal);
    	
    	if ((qaQueUsr != null) && (qaQueUsrLocal == null)) 
        {
    	    qaQueUsr=createUser(request, toolSessionID, qaService);
            logger.debug("created qaQueUsr: " + qaQueUsr);	
        }
    	else
    	{
    		logger.debug("assign user");
    		qaQueUsr=qaQueUsrLocal;
    	}
    	
    	logger.debug("qaQueUsr uid:" + qaQueUsr.getUid());
    	
    	
    	boolean isResponseFinalized=qaQueUsr.isResponseFinalized();
    	logger.debug("isResponseFinalized: " + isResponseFinalized);
    	
    	if (!isResponseFinalized)
    	{
    	  	logger.debug("isResponseFinalized is false, so creating the responses: ");
            while (contentIterator.hasNext())
        	{
        		QaQueContent qaQueContent=(QaQueContent)contentIterator.next();
        		if (qaQueContent != null)
        		{
        		    logger.debug("qaQueContent uid:" + qaQueContent.getUid());
        		    
            		String question=qaQueContent.getQuestion();
            		String displayOrder=new Long(qaQueContent.getDisplayOrder()).toString();
            		String answer=(String)mapAnswers.get(displayOrder);
            		
                    logger.debug("iterationg question-answers: displayOrder: " + displayOrder + 
             													 " question: " + question + " answer: " + answer);
            		
                    String timezoneId="";
                    
                    List attempts=qaService.getAttemptsForUserAndQuestionContent(qaQueUsr.getUid(), qaQueContent.getUid());
                    logger.debug("attempts:" + attempts);
                    
                    if ((attempts != null) && (attempts.size() > 0))
                    {
                        logger.debug("this user already responsed to q/a in this session:");
                    }
                    else
                    {
                        logger.debug("creating response.");
                    	QaUsrResp qaUsrResp= new QaUsrResp(answer,false,
        						new Date(System.currentTimeMillis()),
        						timezoneId,
        						qaQueContent,
        						qaQueUsr,
        						true); 

        				logger.debug("iterationg qaUsrResp: " + qaUsrResp);
        				if (qaUsrResp != null)
        				{
        					qaService.createQaUsrResp(qaUsrResp);
        					logger.debug("created qaUsrResp in the db");	
        				}
                    }
        		}
            }

    	}
    	
		logger.debug("current user is: " + qaQueUsr);
		if (qaQueUsr != null)
		{
			qaQueUsr.setResponseFinalized(true);
			logger.debug("finalized user input");
			qaService.updateQaQueUsr(qaQueUsr);
		}
    }

    
    public static QaQueUsr createUser(HttpServletRequest request, Long toolSessionID, IQaService qaService)
	{
        logger.debug("creating a new user in the tool db, toolSessionID: " + toolSessionID);
        logger.debug("qaService: " + qaService);
		
	    Long queUsrId=QaUtils.getUserId();
		String username=QaUtils.getUserName();
		String fullname=QaUtils.getUserFullName();
		
		QaSession qaSession=qaService.retrieveQaSessionOrNullById(toolSessionID.longValue());
		logger.debug("qaSession: " + qaSession);
		
		QaQueUsr qaQueUsr= new QaQueUsr(queUsrId,
		        username,
		        fullname,
				null, 
				qaSession, 
				new TreeSet());

		qaService.createQaQueUsr(qaQueUsr);
		logger.debug("created qaQueUsr in the db: " + qaQueUsr);
		return qaQueUsr;
	}

    
    /**
	 * feedBackAnswersProgress(HttpServletRequest request, int currentQuestionIndex)
	 * give user feedback on the remaining questions
	 * @param qaLearningForm
	 * return void
	 */
    protected String feedBackAnswersProgress(HttpServletRequest request, int currentQuestionIndex, String totalQuestionCount)
    {
    	logger.debug("totalQuestionCount: " + totalQuestionCount);
    	int remainingQuestionCount=new Long(totalQuestionCount).intValue() - currentQuestionIndex +1;
    	logger.debug("remainingQuestionCount: " + remainingQuestionCount);
    	String userFeedback="";
    	if (remainingQuestionCount != 0)
    		userFeedback= "Remaining question count: " + remainingQuestionCount;
    	else
    		userFeedback= "End of the questions.";
    			
    	return userFeedback;
    }
    
    public void setContentInUse(long toolContentID, IQaService qaService)
    {
    	QaContent qaContent=qaService.loadQa(toolContentID);
    	logger.debug("retrieve qaContent: " + qaContent);
    	
        qaContent.setContentLocked(true);
        logger.debug("content with id : " + toolContentID + "has been marked LOCKED");
        qaService.updateQa(qaContent);
        logger.debug("content with id : " + toolContentID + "has been marked LOCKED and updated in the db");
    }
}
