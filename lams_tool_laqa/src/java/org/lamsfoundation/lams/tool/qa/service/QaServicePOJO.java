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
package org.lamsfoundation.lams.tool.qa.service;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.BasicToolVO;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.dao.IQaContentDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueContentDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueUsrDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaSessionDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO;
import org.lamsfoundation.lams.tool.qa.web.QaMonitoringAction;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.dao.DataAccessException;


/**
 * The POJO implementation of Survey service. All business logics of survey tool
 * are implemented in this class. It translate the request from presentation
 * layer and perform approporiate database operation.
 * 
 * Two construtors are provided in this class. The constuctor with Hibernate
 * session object allows survey tool to handle long run application transaction.
 * The developer can store Hibernate session in http session and pass across
 * different http request. This implementation also make the testing out side 
 * JBoss container much easier.
 * 
 * Every method is implemented as a Hibernate session transaction. It open an
 * new persistent session or connect to existing persistent session in the 
 * begining and it close or disconnect to the persistent session in the end.
 * 
 * @author ozgurd
 *  
 */

public class QaServicePOJO implements
                              IQaService, ToolContentManager, ToolSessionManager
               
{
	   
	private IQaContentDAO 		qaDAO;
    private IQaQueContentDAO 	qaQueContentDAO;
    private IQaSessionDAO 		qaSessionDAO;
    private IQaQueUsrDAO 		qaQueUsrDAO;
    private IQaUsrRespDAO 		qaUsrRespDAO;
    
    private IUserManagementService userManagementService;
    private ILamsToolService toolService;
        
    static Logger logger = Logger.getLogger(QaMonitoringAction.class.getName());


    public QaServicePOJO(){}
    
    public void setQaDAO(IQaContentDAO qaDAO)
    {
        this.qaDAO = qaDAO;
    }
    
    public void setQaQueContentDAO(IQaQueContentDAO qaQueContentDAO)
    {
        this.qaQueContentDAO = qaQueContentDAO;
    }

    public void setQaSessionDAO(IQaSessionDAO qaSessionDAO)
    {
        this.qaSessionDAO = qaSessionDAO;
    }
    
    public void setQaQueUsrDAO(IQaQueUsrDAO qaQueUsrDAO)
    {
        this.qaQueUsrDAO = qaQueUsrDAO;
    }
    
    public void setQaUsrRespDAO(IQaUsrRespDAO qaUsrRespDAO)
    {
        this.qaUsrRespDAO = qaUsrRespDAO;
    }
    
    
    public void setUserManagementService(IUserManagementService userManagementService)
    {
        this.userManagementService = userManagementService;
    }
    
    public void setToolService(ILamsToolService toolService)
    {
        this.toolService = toolService;
    }
    
    
    public void createQa(QaContent qaContent)
    {
        try
        {
            qaDAO.saveQa(qaContent);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading qa content: "
                                                         + e.getMessage(),
														   e);
        }
    }
	
    
    public QaContent retrieveQa(long toolContentId)
    {
        try
        {
            return qaDAO.getQaById(toolContentId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading qa content: "
                                                         + e.getMessage(),
														   e);
        }
    }

    /**
     * same as retrieveQa(long toolContentId) except that returns null if not found
     */
    public QaContent loadQa(long toolContentId)
    {
        try
        {
            return qaDAO.loadQaById(toolContentId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading qa content: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    
    public void createQaQue(QaQueContent qaQueContent)
    {
        try
        {
        	qaQueContentDAO.createQueContent(qaQueContent);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is creating qa content: "
                                                         + e.getMessage(),
														   e);
        }
    }
  
    public void createQaSession(QaSession qaSession)
    {
        try
        {
        	qaSessionDAO.CreateQaSession(qaSession);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is creating qa session: "
                                                         + e.getMessage(),
														   e);
        }
    }

    public void createQaQueUsr(QaQueUsr qaQueUsr)
    {
	   try
        {
	   		qaQueUsrDAO.createUsr(qaQueUsr);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is creating qa QueUsr: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public QaQueUsr loadQaQueUsr(Long userId)
    {
	   try
        {
	   		QaQueUsr qaQueUsr=qaQueUsrDAO.loadQaQueUsrById(userId.longValue());
	   		return qaQueUsr;
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading qa QueUsr: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public void createQaUsrResp(QaUsrResp qaUsrResp)
    {
        try
        {
        	qaUsrRespDAO.createUserResponse(qaUsrResp);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is creating qa UsrResp: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public QaUsrResp retrieveQaUsrResp(long responseId)
    {
    	   try
	        {
    	   		QaUsrResp qaUsrResp=qaUsrRespDAO.retrieveQaUsrResp(responseId);
		   		return qaUsrResp;
	        }
	        catch (DataAccessException e)
	        {
	            throw new QaApplicationException("Exception occured when lams is loading qa qaUsrResp: "
	                                                         + e.getMessage(),
															   e);
	        }
    	
    }
    
	public void updateQaUsrResp(QaUsrResp qaUsrResp)
    {
        try
        {
        	qaUsrRespDAO.updateUserResponse(qaUsrResp);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is updating qa UsrResp: "
                                                         + e.getMessage(),
														   e);
        }
    }
	
    
    
    public QaQueContent retrieveQaQue(long qaQueContentId)
    {
        try
        {
            return qaQueContentDAO.getQaQueById(qaQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading qa question content: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public QaSession retrieveQaSession(long qaSessionId)
    {
    	try
        {
            return qaSessionDAO.getQaSessionById(qaSessionId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading qa session : "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    
    public QaSession retrieveQaSessionOrNullById(long qaSessionId)
    {
    	try
        {
            return qaSessionDAO.getQaSessionOrNullById(qaSessionId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading qa session : "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    
    
    public QaContent retrieveQaBySession(long qaSessionId)
    {
        try
        {
            return qaDAO.getQaBySession(new Long(qaSessionId));
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading qa: "
            								+ e.getMessage(),
                                              e);
        }
    }
    
    
    public void updateQa(QaContent qa)
    {
        try
        {
            qaDAO.updateQa(qa);
        }
        catch(DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is updating"
                                                 + " the qa content: "
                                                 + e.getMessage(),e);
        }
    }

    
    public void updateQaSession(QaSession qaSession)
    {
    	try
        {
    		logger.debug(logger + " " + this.getClass().getName() +  "before updateQaSession: " + qaSession);
            qaSessionDAO.UpdateQaSession(qaSession);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is updating qa session : "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public void deleteQa(QaContent qa)
    {
    	try
        {
            qaDAO.deleteQa(qa);
        }
        catch(DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is deleting"
                                                 + " the qa content: "
                                                 + e.getMessage(),e);
        }
    }
    
    public void deleteQaById(Long qaId)
    {
    	try
        {
            qaDAO.removeQaById(qaId);
        }
        catch(DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is deleting"
                                                 + " the qa content: "
                                                 + e.getMessage(),e);
        }
    }

    
    public void removeUserResponse(QaUsrResp resp)
	{
    	try
        {
    		qaUsrRespDAO.removeUserResponse(resp);
        }
        catch(DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is deleting"
                                                 + " the resp: "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public void deleteUsrRespByQueId(Long qaQueId)
    {
    	try
        {
    		qaUsrRespDAO.removeUserResponseByQaQueId(qaQueId);
        }
        catch(DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is deleting"
                                                 + " the resp: "
                                                 + e.getMessage(),e);
        }
    }
    
    public void deleteQaQueUsr(QaQueUsr qaQueUsr)
    {
    	try
        {
    		qaQueUsrDAO.deleteQaQueUsr(qaQueUsr);
        }
        catch(DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is deleting"
                                                 + " the user: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    
    public int countTotalNumberOfUserResponsed(QaContent qa)
    {
        try
        {
            return qaDAO.countUserResponsed(qa);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured in [countTotalNumberOfUserResponsed]: "
                                                         + e.getMessage(),
                                                 e);
        }
    }

    
    public User getCurrentUserData(String username) throws QaApplicationException
    {
        try
        {
        	logger.debug(logger + " " + this.getClass().getName() +  "getCurrentUserData: " + username);
        	/**
             * this will return null if the username not found
             */
        	User user=userManagementService.getUserByLogin(username);
        	if (user  == null)
        	{
        		logger.debug(logger + " " + this.getClass().getName() +  "No user with the username: "+ username +  " exists.");
        		throw new QaApplicationException("No user with that username exists.");
        	}
        	return user;	 
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Unable to find current user information"
	                                             + " Root Cause: ["
	                                             + e.getMessage() + "]",
                                                 e);
        }
    }

    
    public Lesson getCurrentLesson(long lessonId)
    {
        try
        {
            //this is a mock implementation to make the project compile and 
            //work. When the Lesson service is ready, we need to switch to 
            //real service implmenation.
            return new Lesson();
            //return lsDAO.find(lsessionId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading"
                                                 + " learning session:"
                                                 + e.getMessage(),
                                                 e);
        }
    }

    
    /**
     * Persistent user response into database.
     * @see com.webmcq.ld.tool.survey.ISurveyService#saveUserResponses(java.util.List,
     *      long, long)
     */
    public void saveUserResponses(List responses, long toolSessionId, User user)
    {
        try
        {
            QaSession curSession = qaSessionDAO.getQaSessionById(toolSessionId);

            curSession.setSession_status(QaSession.INCOMPLETE);
            //remove the question user no longer exist 
            curSession.removeQueUsersBy(responses);
            //update the existing question user.
            curSession.updateQueUsersBy(responses);
            //add new question users
            curSession.addNewQueUsersBy(responses);
            //persist the updates
            qaSessionDAO.UpdateQaSession(curSession);

        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is saving"
                                                         + " user responses: "
                                                         + e.getMessage(),
                                                 e);
        }
    }
	
    
    public int getQaClassSize(Long qaContentId)
    {
        //pre-condition validation
        if (qaContentId == null)
            throw new QaApplicationException("We can't calculate number"
                    + "of potential qa learner with null qa content id.");
/**   	
        try
        {
            return toolService.getAllPotentialLearners(surveyContentId.longValue())
                              .size();
        }
        catch (LamsToolServiceException e)
        {
            throw new SurveyApplicationException("Exception occured when lams is caculating"
                                                         + " potential survey learners: "
                                                         + e.getMessage(),
                                                 e);
        }
*/
        //TODO need to change to above implementation once it is done.
        return 10;
    }

    
    
	// public QaSession retrieveQaSession(long toolSessionId){}
	
	
	public void saveQaContent(QaContent qa)
    {
        try
        {
            qaDAO.saveQa(qa);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is saving"
                                                 + " the qa content: "
                                                 + e.getMessage(),e);
        }
    }
    
	public QaQueUsr retrieveQaQueUsr(long qaQaQueUsrId)
    {
    	try
        {
            return qaQueUsrDAO.getQaQueUsrById(qaQaQueUsrId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading qa que usr: "
                                                         + e.getMessage(),
														   e);
        }
    	
    }
	
	
	public int countSessionUser(QaSession qaSession)
	{
		try
        {
            return qaQueUsrDAO.countSessionUser(qaSession);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is counting users in  the session "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	/**
	 * checks the paramter content in the user responses table 
	 * @param qa
	 * @return
	 * @throws QaApplicationException
	 */
	public boolean studentActivityOccurredGlobal(QaContent qaContent) throws QaApplicationException
	{
		logger.debug(logger + " " + this.getClass().getName() +  "using qaContent: " + qaContent);
        Iterator questionIterator=qaContent.getQaQueContents().iterator();
        while (questionIterator.hasNext())
        {
        	QaQueContent qaQueContent=(QaQueContent)questionIterator.next(); 
        	logger.debug(logger + " " + this.getClass().getName() +  "iterated question : " + qaQueContent);
        	Iterator responsesIterator=qaQueContent.getQaUsrResps().iterator();
        	while (responsesIterator.hasNext())
        	{
        		logger.debug(logger + " " + this.getClass().getName() +  "there is at least one response");
        		/**
        		 * proved the fact that there is at least one response for this content.
        		 */
        		return true;
        	}
        } 
        logger.debug(logger + " " + this.getClass().getName() +  "there is no response for this content");
		return false;
	}
	

	public int countIncompleteSession(QaContent qa) throws QaApplicationException
	{
		logger.debug(logger + " " + this.getClass().getName() +  " " + "start of countIncompleteSession: " + qa);
		logger.debug(logger + " " + this.getClass().getName() +  " " + "QaContentId: " + qa.getQaContentId());
		int countIncompleteSession=qaSessionDAO.countIncompleteSession(qa);
		logger.debug(logger + " " + this.getClass().getName() +  " " + "countIncompleteSession: " + countIncompleteSession);
		return countIncompleteSession;
	}
	
	/**
	 * checks the parameter content in the tool sessions table
	 * 
	 * find out if any student has ever used (logged in through the url  and replied) to this content
	 * return true even if you have only one content passed as parameter referenced in the tool sessions table
	 * @param qa
	 * @return boolean
	 * @throws QaApplicationException
	 */
	public boolean studentActivityOccurred(QaContent qa) throws QaApplicationException
	{
		logger.debug(logger + " " + this.getClass().getName() +  " " + "start of studentActivityOccurred: " + qa);
		logger.debug(logger + " " + this.getClass().getName() +  " " + "QaContentId: " + qa.getQaContentId());
		int countStudentActivity=qaSessionDAO.studentActivityOccurred(qa);
		logger.debug(logger + " " + this.getClass().getName() +  " " + "countIncompleteSession: " + countStudentActivity);
		if (countStudentActivity > 0)
			return true;
		return false;
	}
	
	
	/**
	 * TESTED
	 * gets called ONLY when a lesson is being created in monitoring mode. 
	 * Should create the new content(toContent) based on what the author has created her content with. In q/a tool's case
	 * that is content + question's content but not user responses. The deep copy should go only as far as
	 * default content (or author created content) already goes.
	 * ToolContentManager CONTRACT
	 * 
	 * 
	 * similar to public void removeToolContent(Long toolContentId)
	 * gets called by Container+Flash
	 * 
	 */
    public void copyToolContent(Long fromContentId, Long toContentId) throws QaApplicationException
    {
    	logger.debug(logger + " " + this.getClass().getName() +  " " + "start of copyToolContent with ids: " + fromContentId + " and " + toContentId);

        if (fromContentId == null || toContentId == null)
            throw new QaApplicationException("Failed to copy content"
                    + " based on null toolSessionId or null toolContentId");
        try
        {
            QaContent fromContent = qaDAO.loadQaById(fromContentId.longValue());
            if (fromContent == null)
            {
            	logger.debug(logger + " " + this.getClass().getName() +  " " + "WARNING!, retrieved fromContent is null: ");
            	throw new QaApplicationException("WARNING! Fail to create fromContent. Can't continue!");
            }
            else
            {
	            logger.debug(logger + " " + this.getClass().getName() +  " " + "retrieved fromContent: " + fromContent);
	            QaContent toContent = QaContent.newInstance(fromContent,toContentId);
	            if (toContent == null)
	            {
	            	logger.debug(logger + " " + this.getClass().getName() +  " " + "WARNING!, retrieved fromContent is null: ");
	            	throw new QaApplicationException("WARNING! Fail to create toContent. Can't continue!");
	            }
	            else
	            {
	            	logger.debug(logger + " " + this.getClass().getName() +  " " + "retrieved toContent: " + toContent);
    	            qaDAO.saveQa(toContent);
    	            logger.debug(logger + " " + this.getClass().getName() +  " " + "toContent has been saved successfully: " + toContent);
	            }
            }
            logger.debug(logger + " " + this.getClass().getName() +  " " + "end of copyToolContent with ids: " + fromContentId + " and " + toContentId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is copying content between content ids: fromContentId: " + fromContentId + 
            								" and " + toContentId 
                                                  	+ e.getMessage(),e);
        }
    }


    
    /**
     * TO BE DEFINED-FUTURE API
     * 
     * update the tool session status to COMPLETE for this tool session
     * IMPLEMENT THIS!!!! Is this from ToolContentManager???
     * 

     * @param Long toolSessionId
     */
    public void setAsForceCompleteSession(Long toolSessionId) throws QaApplicationException
    {
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " request for setAsForceCompleteSession has come for toolSessionId: " + toolSessionId);
    	
    	QaSession qaSession=retrieveQaSessionOrNullById(toolSessionId.longValue());	
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " retrieved  qaSession is : " + qaSession);
    	qaSession.setSession_status(QaSession.COMPLETED);
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " updated  qaSession to COMPLETED : ");
    	updateQaSession(qaSession);
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " updated  qaSession to COMPLETED in the db : ");
   }

    
    /**
     * TO BE DEFINED
     * TESTED
     * update the tool session status to COMPLETE for this user
     * IMPLEMENT THIS!!!! Is this from ToolContentManager???
     * 

     * @param userId
     */
    public void setAsForceComplete(Long userId) throws QaApplicationException
    {
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " request for setAsForceComplete has come for userId: " + userId);
    	QaQueUsr qaQueUsr=loadQaQueUsr(userId);
    	
    	if (qaQueUsr != null)
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  " " + " retrieved qaQueUsr : " + qaQueUsr);
        	logger.debug(logger + " " + this.getClass().getName() +  " " + " retrieved qaQueUsr  has the tool session : " + qaQueUsr.getQaSession());
        	QaSession qaSession=qaQueUsr.getQaSession();
        	if (qaSession != null)
        	{
        		Long usersToolSessionId=qaSession.getQaSessionId();
            	logger.debug(logger + " " + this.getClass().getName() +  " " + " retrieved  tool session has tool session id : " + usersToolSessionId);
            	
            	qaSession=retrieveQaSessionOrNullById(usersToolSessionId.longValue());	
            	logger.debug(logger + " " + this.getClass().getName() +  " " + " retrieved  qaSession is : " + qaSession);
            	qaSession.setSession_status(QaSession.COMPLETED);
            	logger.debug(logger + " " + this.getClass().getName() +  " " + " updated  qaSession to COMPLETED : ");
            	updateQaSession(qaSession);
            	logger.debug(logger + " " + this.getClass().getName() +  " " + " updated  qaSession to COMPLETED in the db : ");
            	QaContent qaContent=qaSession.getQaContent();
            	logger.debug(logger + " " + this.getClass().getName() +  " " + " qaSession uses qaContent : " + qaContent);
            	logger.debug(logger + " " + this.getClass().getName() +  " " + " qaSession uses qaContentId : " + qaContent.getQaContentId());
            	
            	/**
            	 * if all the sessions of this content is COMPLETED, unlock the content
            	 * 
            	 */
            	int countIncompleteSession=countIncompleteSession(qaContent);
            	logger.debug(logger + " " + this.getClass().getName() +  " " + " qaSession countIncompleteSession : " + countIncompleteSession);
            	
            	if (countIncompleteSession == 0)
            	{
            		qaContent.setContentLocked(false);
            		updateQa(qaContent);
                	logger.debug(logger + " " + this.getClass().getName() +  " " + " qaContent has been updated for contentLocked" + qaContent);
            	}
        	}
        	else
        	{
        		logger.debug(logger + " " + this.getClass().getName() +  " " + " WARNING!!!: retrieved qaSession is null.");
        		throw new QaApplicationException("Fail to setAsForceComplete"
                        + " based on null qaSession.");
        	}
    	}
    	else
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  " " + " WARNING!!!: retrieved qaQueUsr is null.");
            throw new QaApplicationException("Fail to setAsForceComplete"
                    + " based on null qaQueUsr.");
    	}
    }
    
    
    public void unsetAsDefineLater(Long toolContentId) throws QaApplicationException
    {
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " request for unsetAsDefineLater with toolContentId: " + toolContentId);
    	if  (toolContentId == null)
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  " " + " WARNING!!!: retrieved toolContentId is null.");
            throw new QaApplicationException("Fail to setAsDefineLater"
                    + " based on null toolContentId.");
    	}
    	QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
    	if (qaContent == null)
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  " " + " WARNING!!!: retrieved qaContent is null.");
            throw new QaApplicationException("Fail to unsetAsDefineLater"
                    + " based on null qaContent.");
    	}
    	qaContent.setDefineLater(false);
    	updateQa(qaContent);
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " qaContent has been updated for unsetAsDefineLater: " + qaContent);
    }
    
    /**
     * TESTED
     * set the defineLater to true on this content
     * 
     * @param toolContentId
     * return void
     */
    public void setAsDefineLater(Long toolContentId) throws QaApplicationException
    {
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " request for setAsDefineLater with toolContentId: " + toolContentId);
    	if  (toolContentId == null)
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  " " + " WARNING!!!: retrieved toolContentId is null.");
            throw new QaApplicationException("Fail to setAsDefineLater"
                    + " based on null toolContentId.");
    	}
    	QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
    	if (qaContent == null)
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  " " + " WARNING!!!: retrieved qaContent is null.");
            throw new QaApplicationException("Fail to setAsDefineLater"
                    + " based on null qaContent.");
    	}
    	qaContent.setDefineLater(true);
    	updateQa(qaContent);
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " qaContent has been updated for defineLater: " + qaContent);
    }

    /**
     * TESTED
     * set the runOffline to true on this content
     * 
     * @param toolContentId
     * return void
     */
    public void setAsRunOffline(Long toolContentId) throws QaApplicationException
    {
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " request for setAsRunOffline with toolContentId:" + toolContentId);
    	if  (toolContentId == null)
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  " " + " WARNING!!!: retrieved toolContentId is null.");
            throw new QaApplicationException("Fail to setAsRunOffline"
                    + " based on null toolContentId.");
    	}
    	QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
    	if (qaContent == null)
    	{
    		logger.debug(logger + " " + this.getClass().getName() +  " " + " WARNING!!!: retrieved qaContent is null.");
            throw new QaApplicationException("Fail to setAsRunOffline"
                    + " based on null qaContent.");
    	}
    	qaContent.setRunOffline(true);
    	updateQa(qaContent);
    	logger.debug(logger + " " + this.getClass().getName() +  " " + " qaContent has been updated for RunOffline: " + qaContent);
    }

        
    
    /** 
     * TESTED
     * gets automatically called only in monitoring mode when the author chooses to delete a lesson.
     * 
     * The idea is to remove content + its relevant sessions + in q/a tools's case the question's content from the db. 
     * ToolContentManager CONTRACT
     *  this gets called automatically by Flash when a deletion is detected in the tool interface.
     */
    public void removeToolContent(Long toolContentId) 
    {
    	logger.debug(logger + " " + this.getClass().getName() +  " " + "start of removeToolContent with toolContentId: " + toolContentId);
    	
    	QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
    	logger.debug(logger + " " + this.getClass().getName() +  "retrieving qaContent: " + qaContent);
    	
    	if (qaContent != null)
    	{
    		Iterator sessionIterator=qaContent.getQaSessions().iterator();
            while (sessionIterator.hasNext())
            {
            	QaSession qaSession=(QaSession)sessionIterator.next(); 
            	logger.debug(logger + " " + this.getClass().getName() +  "iterated qaSession : " + qaSession);
            	
            	Iterator sessionUsersIterator=qaSession.getQaQueUsers().iterator();
            	while (sessionUsersIterator.hasNext())
            	{
            		QaQueUsr qaQueUsr=(QaQueUsr) sessionUsersIterator.next();
            		logger.debug(logger + " " + this.getClass().getName() +  "iterated qaQueUsr : " + qaQueUsr);
            		
            		Iterator sessionUsersResponsesIterator=qaQueUsr.getQaUsrResps().iterator();
            		while (sessionUsersResponsesIterator.hasNext())
                	{
            			QaUsrResp qaUsrResp=(QaUsrResp)sessionUsersResponsesIterator.next();
            			logger.debug(logger + " " + this.getClass().getName() +  "iterated qaUsrResp : " + qaUsrResp);
            			removeUserResponse(qaUsrResp);
            			logger.debug(logger + " " + this.getClass().getName() +  "removed qaUsrResp : " + qaUsrResp);
                	}
            	}
            }
            
            logger.debug(logger + " " + this.getClass().getName() +  " " + "removed all existing responses of toolContent with toolContentId:" + 
            																toolContentId);                
            qaDAO.removeQa(toolContentId);        
            logger.debug(logger + " " + this.getClass().getName() +  " " + "removed qaContent:" + qaContent);
    	}
    }
    
    /**
	 * it is possible that the tool session id already exists in the tool sessions table
	 * as the users from the same session are involved.
	 * existsSession(long toolSessionId)
	 * @param toolSessionId
	 * @return boolean
	 */
	protected boolean existsSession(long toolSessionId)
	{
		QaSession qaSession=retrieveQaSessionOrNullById(toolSessionId);
    	
	    if (qaSession == null) 
	    {
	    	logger.debug(logger + " " + this.getClass().getName() +  "qaSession does not exist yet: " + toolSessionId);
	    	return false;
	    }
	    else
	    {
	    	logger.debug(logger + " " + this.getClass().getName() +  "retrieving an existing qaSession: " + qaSession + " " + toolSessionId);
	    }
		return true;	
	}
    
    /**
     * TESTED
     * ToolSessionManager CONTRACT : creates a tool session with the incoming toolSessionId in the tool session table
     * 
     * gets called only in the Learner mode.
     * All the learners in the same group have the same toolSessionId.
     * 
     */
    public void createToolSession(Long toolSessionId, Long toolContentId) throws QaApplicationException
    {
    	logger.debug(logger + " " + this.getClass().getName() +  " " + "start of createToolSession with ids: " + toolSessionId + " and " + toolContentId);
        if (toolSessionId == null || toolContentId == null)
            throw new QaApplicationException("Fail to create a qa session"
                    + " based on null toolSessionId or toolContentId");

        	logger.debug("Start to create qa session based on toolSessionId["
                + toolSessionId.longValue() + "] and toolContentId["
                + toolContentId.longValue() + "]");
        try
        {
            QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
            logger.debug(logger + " " + this.getClass().getName() +  " " + "created qaContent: " + qaContent);
            
            if (qaContent == null)
            {
            	logger.debug(logger + " " + this.getClass().getName() +  " " + "WARNING!!: qaContent is null! . " +
            								"We can't create a new tool session based on a null qa content. Can't continue!");
            	throw new QaApplicationException("WARNING! Fail to create tool session since there is no content with toolContentId:" + toolContentId);
            }

            
            /**
             * create a new a new tool session if it does not already exist in the tool session table
             */
            if (!existsSession(toolSessionId.longValue()))
            {
            	QaSession qaSession = new QaSession(toolSessionId,
                        new Date(System.currentTimeMillis()),
                        QaSession.INCOMPLETE,
                        qaContent,
                        new TreeSet());
    
			    logger.debug(logger + " " + this.getClass().getName() +  " " + "created qaSession: " + qaSession);
			    qaSessionDAO.CreateQaSession(qaSession);
			    logger.debug(logger + " " + this.getClass().getName() +  " " + "created qaSession in the db: " + qaSession);	
            }
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is creating"
                                                         + " a qa Session: "
                                                         + e.getMessage(),e);
        }
		
    }

    
    /**FIX THIS ONE!!!!
     * TO BE TESTED
     * ToolSessionManager CONTRACT
     * gets called only in the Learner mode.
     * 
     * Call controller service to complete the qa session
     * @see org.lamsfoundation.lams.tool.ToolSessionManager#leaveToolSession(java.lang.Long)
     */
    public String leaveToolSession(Long toolSessionId,User learner) throws QaApplicationException 
    {
        logger.debug(logger + " " + this.getClass().getName() +  " " + "start of leaveToolSession with toolSessionId:" + toolSessionId);
        logger.debug(logger + " " + this.getClass().getName() +  " " + "start of leaveToolSession with learner:" + learner);
    	try
		{
    		/*
    		String nextUrl=learnerService.completeToolSession(toolSessionId,learner);
    		logger.debug(logger + " " + this.getClass().getName() +  " " + "nextUrl: " + nextUrl);
    		return nextUrl;
    		*/
    		return "completionUrl";
    	}
    	catch(DataAccessException e)
		{
    		throw new QaApplicationException("Exception occured when user is leaving tool session: "
                    + e.getMessage(),e);
		}
        
    }

    /**
     * ToolSessionManager CONTRACT
     * 
     */
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * ToolSessionManager CONTRACT
     * 
     */
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
    {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("not yet implemented");
    }
    
    public BasicToolVO getToolBySignature(String toolSignature) throws QaApplicationException
    {
    	logger.debug(logger + " " + this.getClass().getName() +  "attempt retrieving tool with signature : " + toolSignature);
    	BasicToolVO tool=toolService.getToolBySignature(toolSignature);
    	logger.debug(logger + " " + this.getClass().getName() +  " " + "retrieved tool: " + tool);
	    return tool;
    }
    
    public long getToolDefaultContentIdBySignature(String toolSignature) throws QaApplicationException
    {
    	long contentId=0;
    	contentId=toolService.getToolDefaultContentIdBySignature(toolSignature);
    	logger.debug(logger + " " + this.getClass().getName() +  " " + "tool default contentId : " + contentId);
	    return contentId;
    }
    

    /**
	 * @return Returns the qaDAO.
	 */
	public IQaContentDAO getQaDAO() {
		return qaDAO;
	}
	/**
	 * @return Returns the qaSessionDAO.
	 */
	public IQaSessionDAO getQaSessionDAO() {
		return qaSessionDAO;
	}
	/**
	 * @return Returns the qaUsrRespDAO.
	 */
	public IQaUsrRespDAO getQaUsrRespDAO() {
		return qaUsrRespDAO;
	}

	
	public void buildLearnerReport()
	{
		
	}
	
}
