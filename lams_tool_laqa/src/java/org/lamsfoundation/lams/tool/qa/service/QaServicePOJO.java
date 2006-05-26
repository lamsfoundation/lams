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
package org.lamsfoundation.lams.tool.qa.service;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.ItemExistsException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.tool.IToolVO;
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
import org.lamsfoundation.lams.tool.qa.QaUploadedFile;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.dao.IQaContentDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueContentDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueUsrDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaSessionDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaUploadedFileDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO;
import org.lamsfoundation.lams.tool.qa.util.QAConstants;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.audit.IAuditService;
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

public class QaServicePOJO 
        implements IQaService, ToolContentManager, ToolSessionManager, QaAppConstants
               
{
	static Logger logger = Logger.getLogger(QaServicePOJO.class.getName());
	
	private final String repositoryUser 		= "laqa11";
	private final char[] repositoryId 			= {'l','a','q','a','_','1', '1'}; 
	private final String repositoryWorkspace 	= "laqa11";
	private IRepositoryService repositoryService;
	private ICredentials cred;
		
	private IQaContentDAO 		qaDAO;
    private IQaQueContentDAO 	qaQueContentDAO;
    private IQaSessionDAO 		qaSessionDAO;
    private IQaQueUsrDAO 		qaQueUsrDAO;
    private IQaUsrRespDAO 		qaUsrRespDAO;
    private IQaUploadedFileDAO  qaUploadedFileDAO;
    
    private IToolContentHandler qaToolContentHandler = null;
    private IUserManagementService userManagementService;
    private ILamsToolService toolService;
    private ILearnerService learnerService;
	private IAuditService auditService;

    public void configureContentRepository() throws QaApplicationException {
    	logger.debug("retrieved repService: " + repositoryService);
        cred = new SimpleCredentials(repositoryUser, repositoryId);
        logger.debug("retrieved cred: "+ cred);
          try 
		  {
          	repositoryService.createCredentials(cred);
          	logger.debug("created credentails.");
          	repositoryService.addWorkspace(cred,repositoryWorkspace);
          	logger.debug("created workspace.");
          } catch (ItemExistsException ie) {
              logger.warn("Tried to configure repository but it "
  	        		+" appears to be already configured. Exception thrown by repository being ignored. ", ie);
          } catch (RepositoryCheckedException e) {
              String error = "Error occured while trying to configure repository."
  				+" Unable to recover from error: "+e.getMessage();
  		    logger.error(error, e);
  			throw new QaApplicationException(error,e);
          }
      }
    
    
    public QaServicePOJO(){}
    
    
    public void createQa(QaContent qaContent) throws QaApplicationException
    {
        try
        {
            qaDAO.saveQa(qaContent);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is creating qa content: "
                                                         + e.getMessage(),
														   e);
        }
    }

    public QaContent getQaContentByUID(Long uid) throws QaApplicationException
    {
        try
        {
            return qaDAO.getQaContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is getting qa content by uid: "
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
    
    public void saveOrUpdateQa(QaContent qa) throws QaApplicationException
	{
        try
        {
            qaDAO.saveOrUpdateQa(qa);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is saveOrUpdating qa content: "
                                                         + e.getMessage(),
														   e);
        }
    	
	}

    /**
     * returns null if not found
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
    

    public QaQueContent getQuestionContentByQuestionText(final String question, Long contentUid) throws QaApplicationException
	{
        try
        {
        	return qaQueContentDAO.getQuestionContentByQuestionText(question, contentUid);

        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is getting qa content by question text: "
                                                         + e.getMessage(),
														   e);
        }
	}
    

    public QaQueContent getQuestionContentByDisplayOrder(Long displayOrder, Long contentUid) throws QaApplicationException
	{
        try
        {
        	return qaQueContentDAO.getQuestionContentByDisplayOrder(displayOrder, contentUid);

        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is getting qa content by question text: "
                                                         + e.getMessage(),
														   e);
        }
	}

    
    public void updateQaQueContent(QaQueContent qaQueContent) throws QaApplicationException
	{
    	try
        {
        	qaQueContentDAO.updateQaQueContent(qaQueContent);

        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is updating qa content by question: "
                                                         + e.getMessage(),
														   e);
        }
    	
	}

    
    public void createQaQue(QaQueContent qaQueContent) throws QaApplicationException
    {
        try
        {
        	logger.debug("attempt service createQaQue: " + qaQueContent);
        	qaQueContentDAO.createQueContent(qaQueContent);
        	logger.debug("after  servicecreateQaQue: " + qaQueContent);
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
    
    
    public List getSessionNamesFromContent(QaContent qaContent) throws QaApplicationException
    {
        try
        {
        	return qaSessionDAO.getSessionNamesFromContent(qaContent);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is getting session names from content: "
                                                         + e.getMessage(),
														   e);
        }
    }

    
    public String getSessionNameById(long qaSessionId) throws QaApplicationException
    {
        try
        {
        	return qaSessionDAO.getSessionNameById(qaSessionId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is getting session name: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public List getSessionsFromContent(QaContent qaContent) throws QaApplicationException
	{
    	try
        {
            return qaSessionDAO.getSessionsFromContent(qaContent);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is getting"
                                                 + " the qa sessions list: "
                                                 + e.getMessage(),e);
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
    
    
    public QaQueUsr getQaUserBySession(final Long queUsrId, final Long qaSessionId) throws QaApplicationException
	{
 	   try
       {
	   		return qaQueUsrDAO.getQaUserBySession(queUsrId, qaSessionId);
       }
       catch (DataAccessException e)
       {
           throw new QaApplicationException("Exception occured when lams is getting  qa QueUsr: "
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

    
    public QaQueUsr getQaQueUsrById(long qaQueUsrId) throws QaApplicationException
	{
 	   try
       {
	   		QaQueUsr qaQueUsr=qaQueUsrDAO.getQaQueUsrById(qaQueUsrId);
	   		return qaQueUsr;
       }
       catch (DataAccessException e)
       {
           throw new QaApplicationException("Exception occured when lams is getting qa QueUsr: "
                                                        + e.getMessage(),
														   e);
       }
	}
    
    public List getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long qaQueContentId) throws QaApplicationException
	{
        try
        {
        	return qaUsrRespDAO.getAttemptsForUserAndQuestionContent(queUsrId, qaQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is getting qa qaUsrRespDAO by user id and que content id: "
                                                         + e.getMessage(),
														   e);
        }
	}

    
    public List getUserBySessionOnly(final QaSession qaSession) throws QaApplicationException
    {
  	   try
       {
	   		return qaQueUsrDAO.getUserBySessionOnly(qaSession);
       }
       catch (DataAccessException e)
       {
           throw new QaApplicationException("Exception occured when lams is getting qa QueUsr by qa session "
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
	
	public int countSessionComplete() throws QaApplicationException
	{
		try
        {
			return qaSessionDAO.countSessionComplete();
        }
		catch(DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is counting complete sessions"
                                                 + e.getMessage(),e);
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

    
    public List retrieveQaQueContentsByToolContentId(long qaContentId) throws QaApplicationException
	{
        try
        {
            return qaQueContentDAO.getQaQueContentsByContentId(qaContentId);
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is loading qa que usr: "
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
    		auditService.logChange(QAConstants.TOOL_SIGNATURE,
    				resp.getQaQueUser().getQueUsrId(),resp.getQaQueUser().getUsername(),
    				resp.getAnswer(), null);
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
    
    public List retrieveQaUploadedFiles(QaContent qa) throws QaApplicationException {
        try {
            return qaUploadedFileDAO.retrieveQaUploadedFiles(qa);
        }
        catch (DataAccessException e) {
            throw new QaApplicationException("Exception occured when lams is loading qa uploaded files: "
                                                       + e.getMessage(),
                                                         e);
        }
    }

    public int getTotalNumberOfUsers() throws QaApplicationException
	{
 	   try
       {
	   		return qaQueUsrDAO.getTotalNumberOfUsers();
       }
       catch (DataAccessException e)
       {
           throw new QaApplicationException("Exception occured when lams is retrieving total number of QaQueUsr: "
                                                        + e.getMessage(),
														   e);
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

    
    public List getAllQuestionEntries(final Long uid) throws QaApplicationException
	{
	   try
        {
            return qaQueContentDAO.getAllQuestionEntries(uid.longValue());
        }
        catch (DataAccessException e)
        {
            throw new QaApplicationException("Exception occured when lams is getting by uid  qa question content: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public List getAllQuestionEntriesSorted(final long qaContentId) throws QaApplicationException
	{
 	   try
       {
           return qaQueContentDAO.getAllQuestionEntriesSorted(qaContentId);
       }
       catch (DataAccessException e)
       {
           throw new QaApplicationException("Exception occured when lams is getting all question entries: "
                                                        + e.getMessage(),
														   e);
       }
	}
    
    
    public void removeQaQueContent(QaQueContent qaQueContent) throws QaApplicationException
	{
 	   try
       {
           qaQueContentDAO.removeQaQueContent(qaQueContent);
       }
       catch (DataAccessException e)
       {
           throw new QaApplicationException("Exception occured when lams is removing question content: "
                                                        + e.getMessage(),
														   e);
       }
	}

    
    public User getCurrentUserData(String username) throws QaApplicationException
    {
        try
        {
        	logger.debug("getCurrentUserData: " + username);
        	/*
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
	 * boolean studentActivityOccurredGlobal(QaContent qaContent) throws QaApplicationException
	 * 
	 * @param qa
	 * @return boolean
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
        		return true;
        	}
        } 
        logger.debug("there is no response for this content");
		return false;
	}
	

	/**
	 * counts the number of sessions marked INCOMPLETE for a content
	 * int countIncompleteSession(QaContent qa) throws QaApplicationException
	 * 
	 * @param qa
	 * @return int
	 * @throws QaApplicationException
	 */
	public int countIncompleteSession(QaContent qa) throws QaApplicationException
	{
		logger.debug("start of countIncompleteSession: " + qa);
		logger.debug("qaContentId: " + qa.getQaContentId());
		int countIncompleteSession=qaSessionDAO.countSessionIncomplete();
		logger.debug("countIncompleteSession: " + countIncompleteSession);
		return countIncompleteSession;
	}
	
	/**
	 * checks the parameter content in the tool sessions table.
	 * find out if any student has ever used (logged in through the url  and replied) to this content
	 * return true even if you have only one content passed as parameter referenced in the tool sessions table
	 * 
	 * boolean studentActivityOccurred(QaContent qa) throws QaApplicationException
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
	 * gets called ONLY when a lesson is being created in monitoring mode. 
	 * Should create the new content(toContent) based on what the author has created her content with. In q/a tool's case
	 * that is content + question's content but not user responses. The deep copy should go only as far as
	 * default content (or author created content) already goes.
	 * ToolContentManager CONTRACT
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
            
            
            QaContent toContent = QaContent.newInstance(qaToolContentHandler, fromContent,toContentId);
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
        catch (ItemNotFoundException e)
        {
            throw new ToolException("Exception occured when lams is copying content between content ids."); 
        }
        catch (RepositoryCheckedException e)
        {
            throw new ToolException("Exception occured when lams is copying content between content ids."); 
        }
        
    }


    
    /**
     * setAsForceCompleteSession(Long toolSessionId) throws QaApplicationException
     * update the tool session status to COMPLETE for this tool session
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
     * setAsForceComplete(Long userId) throws QaApplicationException
     * update the tool session status to COMPLETE for this user
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
     * setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException
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
     * setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException
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
     * 
     * removeToolContent(Long toolContentId)
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
    
    public void removeQuestions(Long toolContentId) throws QaApplicationException 
	{
        QaContent qaContent = qaDAO.loadQaById(toolContentId.longValue());
        qaContent.setQaQueContents(new TreeSet());
        
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
        }
    }
    
    
    /**
     * removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException
     * Will need an update on the core tool signature: reason : when  qaContent is null throw an exception 
     * 
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
     * Export the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws DataMissingException if no tool content matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
 	public String exportToolContent(Long toolContentId) throws DataMissingException, ToolException {
		// TODO Auto-generated method stub
		return null;
	}

 	
 	/**
     * Export the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws ToolException if any other error occurs
     */
	public String exportToolContent(List toolContentId) throws ToolException {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Import the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws ToolException if any other error occurs
     */
	public String importToolContent(Long toolContentId, String reference, String directory) throws ToolException {
		// TODO Auto-generated method stub
		return null;
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
		logger.debug("toolSessionId: " + toolSessionId);
		QaSession qaSession=retrieveQaSessionOrNullById(toolSessionId);
		logger.debug("qaSession: " + qaSession);
    	
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
     * createToolSession(Long toolSessionId,String toolSessionName, Long toolContentId) throws ToolException
     * ToolSessionManager CONTRACT : creates a tool session with the incoming toolSessionId in the tool session table
     * 
     * gets called only in the Learner mode.
     * All the learners in the same group have the same toolSessionId.
     * 
     */
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException
    {
    	logger.debug("start of createToolSession with ids: " + toolSessionId + " and " + toolContentId);
    	logger.debug("toolSessionName: " + toolSessionName);
    	
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

        /*
         * create a new a new tool session if it does not already exist in the tool session table
         */
        if (!existsSession(toolSessionId.longValue()))
        {
        	try
			{
        		QaSession qaSession = new QaSession(toolSessionId,
                        new Date(System.currentTimeMillis()),
                        QaSession.INCOMPLETE,
                        toolSessionName,
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
    
    
    /**
     * Complete the tool session.
     * 
     * Part of the ToolSessionManager contract. Called by controller service to force complete the qa session, or 
     * by the web front end to complete the qa session
     * 
     */
    public String leaveToolSession(Long toolSessionId,Long learnerId) throws DataMissingException, ToolException 
    {
        logger.debug("start of leaveToolSession with toolSessionId:" + toolSessionId);
        logger.debug("start of leaveToolSession with learnerId:" + learnerId);
        
        if (toolSessionId == null)
    	{
    		logger.debug("toolSessionId is null");
    		throw new DataMissingException("toolSessionId is missing");
    	}
        
        if (learnerId == null)
    	{
    		logger.debug("learnerId is null");
    		throw new DataMissingException("learnerId is missing");
    	}
        
        QaSession qaSession = retrieveQaSessionOrNullById(toolSessionId.longValue());
        qaSession.setSession_end_date(new Date(System.currentTimeMillis()));
        qaSession.setSession_status(COMPLETED); 
        updateQaSession(qaSession);
        logger.debug("tool session has been marked COMPLETE: " + qaSession);
        
    	try
		{
    		String nextUrl = learnerService.completeToolSession(toolSessionId,learnerId);
    		logger.debug(logger + " " + this.getClass().getName() +  " " + "nextUrl: " + nextUrl);
    		return nextUrl;
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
    
    public IToolVO getToolBySignature(String toolSignature) throws QaApplicationException
    {
    	logger.debug("attempt retrieving tool with signature : " + toolSignature);
    	IToolVO tool=toolService.getToolBySignature(toolSignature);
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
	 * This method verifies the credentials of the SubmitFiles Tool and gives it
	 * the <code>Ticket</code> to login and access the Content Repository.
	 * 
	 * A valid ticket is needed in order to access the content from the
	 * repository. This method would be called evertime the tool needs to
	 * upload/download files from the content repository.
	 * 
	 * @return ITicket The ticket for repostory access
	 * @throws SubmitFilesException
	 */
	public ITicket getRepositoryLoginTicket() throws QaApplicationException {
		repositoryService = RepositoryProxy.getRepositoryService();
    	logger.debug("retrieved repositoryService : " + repositoryService);
		
		ICredentials credentials = new SimpleCredentials(
				repositoryUser,
				repositoryId);
		try {
			ITicket ticket = repositoryService.login(credentials,
					repositoryWorkspace);
			logger.debug("retrieved ticket: " + ticket);
			return ticket;
		} catch (AccessDeniedException e) {
			throw new QaApplicationException("Access Denied to repository."
					+ e.getMessage());
		} catch (WorkspaceNotFoundException e) {
			throw new QaApplicationException("Workspace not found."
					+ e.getMessage());
		} catch (LoginException e) {
			throw new QaApplicationException("Login failed." + e.getMessage());
		}
	}
	
	
	/**
	 * This method deletes the content with the given <code>uuid</code> and
	 * <code>versionID</code> from the content repository
	 * 
	 * @param uuid
	 *            The <code>uuid</code> of the node to be deleted
	 * @param versionID
	 *            The <code>version_id</code> of the node to be deleted.
	 * @throws SubmitFilesException
	 */
	public void deleteFromRepository(Long uuid, Long versionID)
			throws QaApplicationException {
		ITicket ticket = getRepositoryLoginTicket();
		logger.debug("retrieved ticket: " + ticket);
		try {
			String files[] = repositoryService.deleteVersion(ticket, uuid,versionID);
			logger.debug("retrieved files: " + files);
		} catch (Exception e) {
			throw new QaApplicationException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}
	
	
	public InputStream downloadFile(Long uuid, Long versionID)throws QaApplicationException{
		ITicket ticket = getRepositoryLoginTicket();		
		try{
			IVersionedNode node = repositoryService.getFileItem(ticket,uuid,null);
			logger.debug("retrieved node: " + node);
			return node.getFile();
		}catch(AccessDeniedException e){
			throw new QaApplicationException("AccessDeniedException occured while trying to download file " + e.getMessage());
		}catch(FileException e){
			throw new QaApplicationException("FileException occured while trying to download file " + e.getMessage());
		}catch(ItemNotFoundException e){
			throw new QaApplicationException("ItemNotFoundException occured while trying to download file " + e.getMessage());			
		}
	}
	
	/**
	 * adds a new entry to the uploaded files table
	 */
	public void persistFile(String uuid, boolean isOnlineFile, String fileName, QaContent qaContent) throws QaApplicationException {
		logger.debug("attempt persisting file to the db: " + uuid + " " + isOnlineFile + " " + fileName + " " + qaContent);
		QaUploadedFile qaUploadedFile= new QaUploadedFile(uuid, isOnlineFile, fileName, qaContent);
		logger.debug("created qaUploadedFile: " + qaUploadedFile);
		qaUploadedFileDAO.saveUploadFile(qaUploadedFile);
		logger.debug("persisted qaUploadedFile: " + qaUploadedFile);
	}
    
    
    /**
     * adds a new entry to the uploaded files table
     */
    public void persistFile(QaContent content, QaUploadedFile file) throws QaApplicationException {
    	logger.debug("in persistFile: " + file);
        content.getQaUploadedFiles().add(file);
        file.setQaContent(content);
        qaDAO.saveOrUpdateQa(content);
        logger.debug("persisted qaUploadedFile: " + file);
    }
    
    
    /**
     * removes an entry from the uploaded files table
     */
    public void removeFile(Long submissionId) throws QaApplicationException {
        qaUploadedFileDAO.removeUploadFile(submissionId);
        logger.debug("removed qaUploadedFile: " + submissionId);
    }
    

	/**
	 * removes all the entries in the uploaded files table
	 */
	public void cleanUploadedFilesMetaData() throws QaApplicationException {
		logger.debug("attempt cleaning up uploaded file meta data table from the db");
		qaUploadedFileDAO.cleanUploadedFilesMetaData();
		logger.debug("files meta data has been cleaned up");
	}
    
	
	/**
	 * @return Returns the logger.
	 */
	public static Logger getLogger() {
		return logger;
	}
	/**
	 * @param logger The logger to set.
	 */
	public static void setLogger(Logger logger) {
		QaServicePOJO.logger = logger;
	}
	/**
	 * @return Returns the cred.
	 */
	public ICredentials getCred() {
		return cred;
	}
	/**
	 * @param cred The cred to set.
	 */
	public void setCred(ICredentials cred) {
		this.cred = cred;
	}
	/**
	 * @return Returns the qaUploadedFileDAO.
	 */
	public IQaUploadedFileDAO getQaUploadedFileDAO() {
		return qaUploadedFileDAO;
	}
	/**
	 * @param qaUploadedFileDAO The qaUploadedFileDAO to set.
	 */
	public void setQaUploadedFileDAO(IQaUploadedFileDAO qaUploadedFileDAO) {
		this.qaUploadedFileDAO = qaUploadedFileDAO;
	}
	/**
	 * @return Returns the repositoryId.
	 */
	public char[] getRepositoryId() {
		return repositoryId;
	}
	/**
	 * @return Returns the repositoryUser.
	 */
	public String getRepositoryUser() {
		return repositoryUser;
	}
	/**
	 * @return Returns the repositoryWorkspace.
	 */
	public String getRepositoryWorkspace() {
		return repositoryWorkspace;
	}
	/**
	 * @return Returns the qaQueContentDAO.
	 */
	public IQaQueContentDAO getQaQueContentDAO() {
		return qaQueContentDAO;
	}
	/**
	 * @return Returns the qaQueUsrDAO.
	 */
	public IQaQueUsrDAO getQaQueUsrDAO() {
		return qaQueUsrDAO;
	}
	/**
	 * @return Returns the toolService.
	 */
	public ILamsToolService getToolService() {
		return toolService;
	}
	/**
	 * @return Returns the userManagementService.
	 */
	public IUserManagementService getUserManagementService() {
		return userManagementService;
	}


	public ILearnerService getLearnerService() {
		return learnerService;
	}


	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}
	
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

	/**
	 * @return Returns the repositoryService.
	 */
	public IRepositoryService getRepositoryService() {
		return repositoryService;
	}
	/**
	 * @param repositoryService The repositoryService to set.
	 */
	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

    
    
    public void setUserManagementService(IUserManagementService userManagementService)
    {
        this.userManagementService = userManagementService;
    }
    
    public void setToolService(ILamsToolService toolService)
    {
        this.toolService = toolService;
    }
	/**
	 * @return Returns the qaToolContentHandler.
	 */
	public IToolContentHandler getQaToolContentHandler() {
		return qaToolContentHandler;
	}
	/**
	 * @param qaToolContentHandler The qaToolContentHandler to set.
	 */
	public void setQaToolContentHandler(IToolContentHandler qaToolContentHandler) {
		this.qaToolContentHandler = qaToolContentHandler;
	}


	public IAuditService getAuditService() {
		return auditService;
	}


	public void setAuditService(IAuditService auditService) {
		this.auditService = auditService;
	}
	
	
}
