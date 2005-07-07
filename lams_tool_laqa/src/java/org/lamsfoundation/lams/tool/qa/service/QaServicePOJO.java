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
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
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
 * @author Ozgur Demirtas
 *  
 */

public class QaServicePOJO implements
                              IQaService, ToolContentManager, ToolSessionManager, QaAppConstants
               
{
	private IQaContentDAO 		qaDAO;
    private IQaQueContentDAO 	qaQueContentDAO;
    private IQaSessionDAO 		qaSessionDAO;
    private IQaQueUsrDAO 		qaQueUsrDAO;
    private IQaUsrRespDAO 		qaUsrRespDAO;
    
    private IUserManagementService userManagementService;
    private ILamsToolService toolService;
        
    static Logger logger = Logger.getLogger(QaServicePOJO.class.getName());


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
    
    
    public void createQa(QaContent qaContent) throws QaApplicationException
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
	
    
    public QaContent retrieveQa(long toolContentId) throws QaApplicationException
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
    public QaContent loadQa(long toolContentId) throws QaApplicationException
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
    
    
    
    public void createQaQue(QaQueContent qaQueContent) throws QaApplicationException
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
  
    public void createQaSession(QaSession qaSession) throws QaApplicationException
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

    public void createQaQueUsr(QaQueUsr qaQueUsr) throws QaApplicationException
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
    
    
    public QaQueUsr loadQaQueUsr(Long userId) throws QaApplicationException
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
    
    
    public void createQaUsrResp(QaUsrResp qaUsrResp) throws QaApplicationException
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
    
    
    public QaUsrResp retrieveQaUsrResp(long responseId) throws QaApplicationException
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
    
	public void updateQaUsrResp(QaUsrResp qaUsrResp) throws QaApplicationException
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
	
    
    
    public QaQueContent retrieveQaQue(long qaQueContentId) throws QaApplicationException
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
    
    
    public QaSession retrieveQaSession(long qaSessionId) throws QaApplicationException
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
    
    public QaSession retrieveQaSessionOrNullById(long qaSessionId) throws QaApplicationException
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
    
    
    public QaContent retrieveQaBySession(long qaSessionId) throws QaApplicationException
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
    
    
    public void updateQa(QaContent qa) throws QaApplicationException
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

    
    public void updateQaSession(QaSession qaSession) throws QaApplicationException
    {
    	try
        {
    		logger.debug("before updateQaSession: " + qaSession);
            qaSessionDAO.UpdateQaSession(qaSession);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is updating qa session : "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public void deleteQa(QaContent qa) throws QaApplicationException
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
    
    public void deleteQaById(Long qaId) throws QaApplicationException
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

    public void deleteQaSession(QaSession QaSession) throws QaApplicationException 
	{
		try
        {
			qaSessionDAO.deleteQaSession(QaSession);
        }
		catch(DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is deleting"
                                                 + " the qa session: "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public void removeUserResponse(QaUsrResp resp) throws QaApplicationException
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
    
    
    public void deleteUsrRespByQueId(Long qaQueId) throws QaApplicationException
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
    
    public void deleteQaQueUsr(QaQueUsr qaQueUsr) throws QaApplicationException
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
    
    
    
    public int countTotalNumberOfUserResponsed(QaContent qa) throws QaApplicationException
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
        	logger.debug("getCurrentUserData: " + username);
        	/**
             * this will return null if the username not found
             */
        	User user=userManagementService.getUserByLogin(username);
        	if (user  == null)
        	{
        		logger.debug("No user with the username: "+ username +  " exists.");
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

    /**
     * 
     * Unused method
     * @param lessonId
     * @return
     * @throws QaApplicationException
     */
    public Lesson getCurrentLesson(long lessonId) throws QaApplicationException
    {
        try
        {
            /**this is a mock implementation to make the project compile and 
            work. When the Lesson service is ready, we need to switch to 
            real service implmenation.
            */
            return new Lesson();
            /**return lsDAO.find(lsessionId); */
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading"
                                                 + " learning session:"
                                                 + e.getMessage(),
                                                 e);
        }
    }

    public void saveQaContent(QaContent qa) throws QaApplicationException
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
    
	public QaQueUsr retrieveQaQueUsr(long qaQaQueUsrId) throws QaApplicationException
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
	
	
	public int countSessionUser(QaSession qaSession) throws QaApplicationException
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
		logger.debug("using qaContent: " + qaContent);
        Iterator questionIterator=qaContent.getQaQueContents().iterator();
        while (questionIterator.hasNext())
        {
        	QaQueContent qaQueContent=(QaQueContent)questionIterator.next(); 
        	logger.debug("iterated question : " + qaQueContent);
        	Iterator responsesIterator=qaQueContent.getQaUsrResps().iterator();
        	while (responsesIterator.hasNext())
        	{
        		logger.debug("there is at least one response");
        		/**
        		 * proved the fact that there is at least one response for this content.
        		 */
        		return true;
        	}
        } 
        logger.debug("there is no response for this content");
		return false;
	}
	

	public int countIncompleteSession(QaContent qa) throws QaApplicationException
	{
		logger.debug("start of countIncompleteSession: " + qa);
		logger.debug("qaContentId: " + qa.getQaContentId());
		int countIncompleteSession=qaSessionDAO.countIncompleteSession(qa);
		logger.debug("countIncompleteSession: " + countIncompleteSession);
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
		logger.debug("start of studentActivityOccurred: " + qa);
		logger.debug("qaContentId: " + qa.getQaContentId());
		int countStudentActivity=qaSessionDAO.studentActivityOccurred(qa);
		logger.debug("countIncompleteSession: " + countStudentActivity);
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
	
    public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException
    {
    	logger.debug("start of copyToolContent with ids: " + fromContentId + " and " + toContentId);

    	if (fromContentId == null)
        {
        	logger.debug("fromContentId is null.");
        	logger.debug("attempt retrieving tool's default content id with signatute : " + MY_SIGNATURE);
        	long defaultContentId=0;
			try
			{
				defaultContentId=getToolDefaultContentIdBySignature(MY_SIGNATURE);
				fromContentId= new Long(defaultContentId);
			}
			catch(Exception e)
			{
				logger.debug("default content id has not been setup for signature: " +  MY_SIGNATURE);
				throw new ToolException("WARNING! default content has not been setup for signature" + MY_SIGNATURE + " Can't continue!");
			}
        }
        
        if (toContentId == null)
        {
        	logger.debug("throwing ToolException: toContentId is null");
			throw new ToolException("toContentId is missing");
        }
        logger.debug("final - copyToolContent using ids: " + fromContentId + " and " + toContentId);
            
        try
        {
            QaContent fromContent = qaDAO.loadQaById(fromContentId.longValue());
        
            if (fromContent == null)
            {
            	logger.debug("fromContent is null.");
            	logger.debug("attempt retrieving tool's default content id with signatute : " + MY_SIGNATURE);
            	long defaultContentId=0;
    			try
    			{
    				defaultContentId=getToolDefaultContentIdBySignature(MY_SIGNATURE);
    				fromContentId= new Long(defaultContentId);
    			}
    			catch(Exception e)
    			{
    				logger.debug("default content id has not been setup for signature: " +  MY_SIGNATURE);
    				throw new ToolException("WARNING! default content has not been setup for signature" + MY_SIGNATURE + " Can't continue!");
    			}
    			
    			fromContent = qaDAO.loadQaById(fromContentId.longValue());
    			logger.debug("using fromContent: " + fromContent);
            }
            
            logger.debug("final - retrieved fromContent: " + fromContent);
            logger.debug("final - before new instance using " + fromContent + " and " + toContentId);
            QaContent toContent = QaContent.newInstance(fromContent,toContentId);
            if (toContent == null)
            {
            	logger.debug("throwing ToolException: WARNING!, retrieved toContent is null.");
            	throw new ToolException("WARNING! Fail to create toContent. Can't continue!");
            }
            else
            {
            	logger.debug("retrieved toContent: " + toContent);
	            qaDAO.saveQa(toContent);
	            logger.debug("toContent has been saved successfully: " + toContent);
            }
            logger.debug("end of copyToolContent with ids: " + fromContentId + " and " + toContentId);
        }
        catch (DataAccessException e)
        {
        	logger.debug("throwing ToolException: Exception occured when lams is copying content between content ids.");
            throw new ToolException("Exception occured when lams is copying content between content ids."); 
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
    	logger.debug("rrequest for setAsForceCompleteSession has come for toolSessionId: " + toolSessionId);
    	
    	QaSession qaSession=retrieveQaSessionOrNullById(toolSessionId.longValue());	
    	logger.debug("retrieved  qaSession is : " + qaSession);
    	qaSession.setSession_status(QaSession.COMPLETED);
    	logger.debug("updated  qaSession to COMPLETED : ");
    	updateQaSession(qaSession);
    	logger.debug("updated  qaSession to COMPLETED in the db : ");
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
    	logger.debug("request for setAsForceComplete has come for userId: " + userId);
    	QaQueUsr qaQueUsr=loadQaQueUsr(userId);
    	
    	if (qaQueUsr != null)
    	{
    		logger.debug("retrieved qaQueUsr : " + qaQueUsr);
        	logger.debug("retrieved qaQueUsr  has the tool session : " + qaQueUsr.getQaSession());
        	QaSession qaSession=qaQueUsr.getQaSession();
        	if (qaSession != null)
        	{
        		Long usersToolSessionId=qaSession.getQaSessionId();
            	logger.debug("retrieved  tool session has tool session id : " + usersToolSessionId);
            	
            	qaSession=retrieveQaSessionOrNullById(usersToolSessionId.longValue());	
            	logger.debug("retrieved  qaSession is : " + qaSession);
            	qaSession.setSession_status(QaSession.COMPLETED);
            	logger.debug("updated  qaSession to COMPLETED : ");
            	updateQaSession(qaSession);
            	logger.debug("updated  qaSession to COMPLETED in the db : ");
            	QaContent qaContent=qaSession.getQaContent();
            	logger.debug("qaSession uses qaContent : " + qaContent);
            	logger.debug("qaSession uses qaContentId : " + qaContent.getQaContentId());
            	
            	/**
            	 * if all the sessions of this content is COMPLETED, unlock the content
            	 * 
            	 */
            	int countIncompleteSession=countIncompleteSession(qaContent);
            	logger.debug("qaSession countIncompleteSession : " + countIncompleteSession);
            	
            	if (countIncompleteSession == 0)
            	{
            		qaContent.setContentLocked(false);
            		updateQa(qaContent);
                	logger.debug("qaContent has been updated for contentLocked" + qaContent);
            	}
        	}
        	else
        	{
        		logger.debug("WARNING!: retrieved qaSession is null.");
        		throw new QaApplicationException("Fail to setAsForceComplete"
                        + " based on null qaSession.");
        	}
    	}
    	else
    	{
    		logger.debug("WARNING!: retrieved qaQueUsr is null.");
            throw new QaApplicationException("Fail to setAsForceComplete"
                    + " based on null qaQueUsr.");
    	}
    }
    
    
    public void unsetAsDefineLater(Long toolContentId) throws QaApplicationException
    {
    	logger.debug("request for unsetAsDefineLater with toolContentId: " + toolContentId);
    	if  (toolContentId == null)
    	{
    		logger.debug("WARNING!: retrieved toolContentId is null.");
            throw new QaApplicationException("Fail to setAsDefineLater"
                    + " based on null toolContentId.");
    	}
    	QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
    	if (qaContent == null)
    	{
    		logger.debug("WARNING!!!: retrieved qaContent is null.");
            throw new QaApplicationException("Fail to unsetAsDefineLater"
                    + " based on null qaContent.");
    	}
    	qaContent.setDefineLater(false);
    	updateQa(qaContent);
    	logger.debug("qaContent has been updated for unsetAsDefineLater: " + qaContent);
    }
    
    /**
     * TESTED
     * set the defineLater to true on this content
     * 
     * @param toolContentId
     * return void
     */
    public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException
    {
    	logger.debug("request for setAsDefineLater with toolContentId: " + toolContentId);
    	if  (toolContentId == null)
    	{
    		logger.debug("throwing DataMissingException: WARNING!: retrieved toolContentId is null.");
            throw new DataMissingException("toolContentId is missing");
    	}
    	QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
    	if (qaContent == null)
    	{
    		logger.debug("throwing DataMissingException: WARNING!: retrieved qaContent is null.");
            throw new DataMissingException("qaContent is missing");
    	}
    	qaContent.setDefineLater(true);
    	updateQa(qaContent);
    	logger.debug("qaContent has been updated for defineLater: " + qaContent);
    }

    /**
     * TESTED
     * set the runOffline to true on this content
     * 
     * @param toolContentId
     * return void
     */
    public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException
    {
    	logger.debug("request for setAsRunOffline with toolContentId:" + toolContentId);
    	if  (toolContentId == null)
    	{
    		logger.debug("throwing DataMissingException: WARNING!: retrieved toolContentId is null.");
            throw new DataMissingException("toolContentId is missing");
    	}
    	QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
    	if (qaContent == null)
    	{
    		logger.debug("throwing DataMissingException: WARNING!: retrieved qaContent is null.");
            throw new DataMissingException("qaContent is missing");
    	}
    	qaContent.setRunOffline(true);
    	updateQa(qaContent);
    	logger.debug("qaContent has been updated for runOffline: " + qaContent);
    }

        
    
    /** 
     * !!! UNUSED !!!
     * TESTED
     * gets automatically called only in monitoring mode when the author chooses to delete a lesson.
     * 
     * The idea is to remove content + its relevant sessions + in q/a tools's case the question's content from the db. 
     * ToolContentManager CONTRACT
     *  this gets called automatically by Flash when a deletion is detected in the tool interface.
     */
    public void removeToolContent(Long toolContentId) 
    {
    	logger.debug("start of removeToolContent with toolContentId: " + toolContentId);
    	
    	QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
    	logger.debug("retrieving qaContent: " + qaContent);
    	
    	if (qaContent != null)
    	{
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
            			removeUserResponse(qaUsrResp);
            			logger.debug("removed qaUsrResp : " + qaUsrResp);
                	}
            	}
            }
            
            logger.debug("removed all existing responses of toolContent with toolContentId:" + 
            																toolContentId);                
            qaDAO.removeQa(toolContentId);        
            logger.debug("removed qaContent:" + qaContent);
    	}
    }
    
    
    /*
     * DOUBLE CHECK!
     * Will need an update on the core tool signature: reason : when  qaContent is null throw an exception 
     *  (non-Javadoc)
     * @see org.lamsfoundation.lams.tool.ToolContentManager#removeToolContent(java.lang.Long, boolean)
     */
    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException
	{
    	logger.debug("start of: removeToolContent(Long toolContentId, boolean removeSessionData");
    	logger.debug("start of removeToolContent with toolContentId: " + toolContentId + "removeSessionData: " + removeSessionData);
    	
    	if (toolContentId == null)
    	{
    		logger.debug("toolContentId is null");
    		throw new ToolException("toolContentId is missing");
    	}
    	
    	QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
    	logger.debug("retrieving qaContent: " + qaContent);
    	
    	if (qaContent != null)
    	{
    		Iterator sessionIterator=qaContent.getQaSessions().iterator();
            while (sessionIterator.hasNext())
            {
            	if (removeSessionData == false)
            	{
            		logger.debug("removeSessionData is false, throwing SessionDataExistsException.");
            		throw new SessionDataExistsException();	
            	}
            	
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
            			removeUserResponse(qaUsrResp);
            			logger.debug("removed qaUsrResp : " + qaUsrResp);
                	}
            	}
            }
            
            logger.debug("removed all existing responses of toolContent with toolContentId:" + 
            																toolContentId);                
            qaDAO.removeQa(toolContentId);        
            logger.debug("removed qaContent:" + qaContent);
    	}
    	else
    	{
        	logger.debug("Warning!!!, We should have not come here. qaContent is null.");
        	throw new ToolException("toolContentId is missing");
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
	    	logger.debug("qaSession does not exist yet: " + toolSessionId);
	    	return false;
	    }
	    else
	    {
	    	logger.debug("retrieving an existing qaSession: " + qaSession + " " + toolSessionId);
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
    public void createToolSession(Long toolSessionId, Long toolContentId) throws ToolException
    {
    	logger.debug("start of createToolSession with ids: " + toolSessionId + " and " + toolContentId);
    	if (toolSessionId == null)
    	{
    		logger.debug("toolSessionId is null");
    		throw new ToolException("toolSessionId is missing");
    	}
    	
    	long defaultContentId=0;
    	if (toolContentId == null)
        {
        	logger.debug("toolContentId is null.");
        	logger.debug("attempt retrieving tool's default content id with signatute : " + MY_SIGNATURE);
        
			try
			{
				defaultContentId=getToolDefaultContentIdBySignature(MY_SIGNATURE);
				toolContentId=new Long(defaultContentId);
				logger.debug("updated toolContentId to: " + toolContentId);
			}
			catch(Exception e)
			{
				logger.debug("default content id has not been setup for signature: " +  MY_SIGNATURE);
				throw new ToolException("WARNING! default content has not been setup for signature" + MY_SIGNATURE + " Can't continue!");
			}
        }
    	logger.debug("final toolSessionId and toolContentId: " +  toolSessionId + " " + toolContentId);
    	
        QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
        logger.debug("retrieved qaContent: " + qaContent);
        
        if (qaContent == null)
        {
        	logger.debug("qaContent is null.");
        	logger.debug("attempt retrieving tool's default content id with signatute : " + MY_SIGNATURE);
        
			try
			{
				defaultContentId=getToolDefaultContentIdBySignature(MY_SIGNATURE);
				toolContentId=new Long(defaultContentId);
				logger.debug("updated toolContentId to: " + toolContentId);
			}
			catch(Exception e)
			{
				logger.debug("default content id has not been setup for signature: " +  MY_SIGNATURE);
				throw new ToolException("WARNING! default content has not been setup for signature" + MY_SIGNATURE + " Can't continue!");
			}

			qaContent = qaDAO.loadQaById(toolContentId.longValue());
        }
        logger.debug("final - retrieved qaContent: " + qaContent);

            
        /**
         * create a new a new tool session if it does not already exist in the tool session table
         */
        if (!existsSession(toolSessionId.longValue()))
        {
        	try
			{
        		QaSession qaSession = new QaSession(toolSessionId,
                        new Date(System.currentTimeMillis()),
                        QaSession.INCOMPLETE,
                        qaContent,
                        new TreeSet());

    		    logger.debug("created qaSession: " + qaSession);
    		    qaSessionDAO.CreateQaSession(qaSession);
    		    logger.debug("created qaSession in the db: " + qaSession);	
	
        	}
        	catch(Exception e)
			{
        		logger.debug("Error creating new toolsession in the db");
				throw new ToolException("Error creating new toolsession in the db: " + e);
			}
        }
    }

    
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException
	{
    	logger.debug("start of removeToolSession with id: " + toolSessionId);
    	if (toolSessionId == null)
    	{
    		logger.debug("toolSessionId is null");
    		throw new DataMissingException("toolSessionId is missing");
    	}
    	
    	QaSession qaSession=null;
    	try
		{
    		qaSession=retrieveQaSessionOrNullById(toolSessionId.longValue());
    		logger.debug("retrieved qaSession: " + qaSession);
		}
    	catch(QaApplicationException e)
		{
    		throw new DataMissingException("error retrieving qaSession: " + e);
		}
    	catch(Exception e)
		{
    		throw new ToolException("error retrieving qaSession: " + e);
		}
    	
    	if (qaSession == null)
    	{
    		logger.debug("qaSession is null");
    		throw new DataMissingException("qaSession is missing");
    	}
    	
    	try
		{
    		qaSessionDAO.deleteQaSession(qaSession);
        	logger.debug("qaSession " + qaSession + " has been deleted successfully.");	
		}
    	catch(QaApplicationException e)
		{
    		throw new ToolException("error deleting qaSession:" + e);
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
    public String leaveToolSession(Long toolSessionId,User learner) throws DataMissingException, ToolException 
    {
        logger.debug("start of leaveToolSession with toolSessionId:" + toolSessionId);
        logger.debug("start of leaveToolSession with learner:" + learner);
        
        if (toolSessionId == null)
    	{
    		logger.debug("toolSessionId is null");
    		throw new DataMissingException("toolSessionId is missing");
    	}
        
        if (learner == null)
    	{
    		logger.debug("learner is null");
    		throw new DataMissingException("learner is missing");
    	}
        
    	try
		{
    		/*
    		String nextUrl=learnerService.completeToolSession(toolSessionId,learner);
    		logger.debug(logger + " " + this.getClass().getName() +  " " + "nextUrl: " + nextUrl);
    		return nextUrl;
    		*/
    		return "nextUrl";
    	}
    	catch(DataAccessException e)
		{
    		throw new ToolException("Exception occured when user is leaving tool session: " + e);
		}
        
    }

    /**
     * ToolSessionManager CONTRACT
     * 
     */
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException
    {
        throw new ToolException("not yet implemented");
    }

    /**
     * ToolSessionManager CONTRACT
     * 
     */
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException
    {

        throw new ToolException("not yet implemented");
    }
    
    public BasicToolVO getToolBySignature(String toolSignature) throws QaApplicationException
    {
    	logger.debug("attempt retrieving tool with signature : " + toolSignature);
    	BasicToolVO tool=toolService.getToolBySignature(toolSignature);
    	logger.debug("retrieved tool: " + tool);
	    return tool;
    }
    
    public long getToolDefaultContentIdBySignature(String toolSignature) throws QaApplicationException
    {
    	long contentId=0;
    	logger.debug("before attempting retrieving tool with signature : " + toolSignature);
    	contentId=toolService.getToolDefaultContentIdBySignature(toolSignature);
    	logger.debug("tool default contentId : " + contentId);
	    return contentId;
    }

    public QaQueContent getToolDefaultQuestionContent(long contentId) throws QaApplicationException
    {
    	logger.debug("before attempting retrieving QaQueContent with contentId : " + contentId);
    	QaQueContent qaQueContent=qaQueContentDAO.getToolDefaultQuestionContent(contentId);
    	logger.debug("retrieved QaQueContent : " + qaQueContent);
    	return qaQueContent; 
    }

    
    public List getToolSessionsForContent(QaContent qa)
    {
    	logger.debug("attempt retrieving listToolSessionIds for : " + qa);
    	List listToolSessionIds=qaSessionDAO.getToolSessionsForContent(qa);
    	return listToolSessionIds;
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
