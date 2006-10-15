/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.service;
import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.AccessDeniedException;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.ICredentials;
import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.contentrepository.ItemExistsException;
import org.lamsfoundation.lams.contentrepository.ItemNotFoundException;
import org.lamsfoundation.lams.contentrepository.LoginException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.learningdesign.service.ExportToolContentException;
import org.lamsfoundation.lams.learningdesign.service.IExportToolContentService;
import org.lamsfoundation.lams.learningdesign.service.ImportToolContentException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolContentImport102Manager;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.mc.dao.IMcContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcOptionsContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcQueContentDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcSessionDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUploadedFileDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUserDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcUsrAttemptDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McOptsContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McSession;
import org.lamsfoundation.lams.tool.mc.pojos.McUploadedFile;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.McApplicationException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;
import org.springframework.dao.DataAccessException;

/**
 * 
 * @author Ozgur Demirtas
 * 
 * The POJO implementation of Mc service. All business logics of MCQ tool
 * are implemented in this class. It translate the request from presentation
 * layer and perform appropriate database operation.
 * 
 */
public class McServicePOJO implements
                              IMcService, ToolContentManager, ToolSessionManager, ToolContentImport102Manager, McAppConstants
               
{
	static Logger logger = Logger.getLogger(McServicePOJO.class.getName());
	
	/*repository access related constants */
	private final String repositoryUser 		= "lamc11";
	private final char[] repositoryId 			= {'l','a','m','c','_','1', '1'}; 
	private final String repositoryWorkspace 	= "lamc11";
	private IRepositoryService repositoryService;
	private ICredentials cred;
	
	private IMcContentDAO			mcContentDAO; 
	private IMcQueContentDAO		mcQueContentDAO; 
	private IMcOptionsContentDAO 	mcOptionsContentDAO;	
	private IMcSessionDAO			mcSessionDAO;
	private IMcUserDAO				mcUserDAO;
	private IMcUsrAttemptDAO		mcUsrAttemptDAO;
	private IMcUploadedFileDAO  	mcUploadedFileDAO;
	
	private IAuditService auditService;
    private IUserManagementService 	userManagementService;
    private ILearnerService 		learnerService;
    private ILamsToolService 		toolService;
    private IToolContentHandler mcToolContentHandler = null;
    private IExportToolContentService exportContentService;
	
    private ICoreNotebookService coreNotebookService;
    
    public McServicePOJO(){}
    
    public void configureContentRepository() throws McApplicationException {
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
  			throw new McApplicationException(error,e);
          }
      }
    
    
    public void createMc(McContent mcContent) throws McApplicationException
    {
        try
        {
        	logger.debug("using mcContent defineLater:" + mcContent.isDefineLater());
        	mcContentDAO.saveMcContent(mcContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is creating mc content: "
                                                         + e.getMessage(),
														   e);
        }
    }
	
    
    public McContent retrieveMc(Long toolContentId) throws McApplicationException
    {
        try
        {
            return mcContentDAO.findMcContentById(toolContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is loading mc content: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public void  updateMcContent(McContent mcContent) throws McApplicationException
    {
        try
        {
            mcContentDAO.updateMcContent(mcContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is updating mc content: "
                                                         + e.getMessage(),
														   e);
        }
    }

    
    public void createMcQue(McQueContent mcQueContent) throws McApplicationException
    {
        try
        {
        	mcQueContentDAO.saveMcQueContent(mcQueContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is creating mc que content: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public void updateMcQueContent(McQueContent mcQueContent) throws McApplicationException
    {
        try
        {
        	mcQueContentDAO.updateMcQueContent(mcQueContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is updating mc que content: "
                                                         + e.getMessage(),
														   e);
        }        
        
    }

    
    public List retrieveMcQueContentsByToolContentId(long mcContentId) throws McApplicationException
	{
        try
        {
            return mcQueContentDAO.getMcQueContentsByContentId(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is loading mc que usr: "
                                                         + e.getMessage(),
                                                           e);
        }
    }

    
    
    public McQueContent getQuestionContentByDisplayOrder(final Long displayOrder, final Long mcContentUid) throws McApplicationException
	{
        try
        {
        	return mcQueContentDAO.getQuestionContentByDisplayOrder(displayOrder, mcContentUid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting mc que content by display order: "
                                                         + e.getMessage(),
														   e);
        }    	
	}
    
    
    public McQueContent getMcQueContentByUID(Long uid) throws McApplicationException
    {
        try
        {
        	return mcQueContentDAO.getMcQueContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting mc que content by uid: "
                                                         + e.getMessage(),
														   e);
        }	
    }
    
    
    public List getAllQuestionEntriesSorted(final long mcContentId) throws McApplicationException
	{
 	   try
       {
           return mcQueContentDAO.getAllQuestionEntriesSorted(mcContentId);
       }
       catch (DataAccessException e)
       {
           throw new McApplicationException("Exception occured when lams is getting all question entries: "
                                                        + e.getMessage(),
														   e);
       }
	}
    
    
    
    public void saveOrUpdateMcQueContent(McQueContent mcQueContent) throws McApplicationException
	{
    	try
        {
        	mcQueContentDAO.saveOrUpdateMcQueContent(mcQueContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is updating mc que content: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public void removeQuestionContentByMcUid(final Long mcContentUid) throws McApplicationException
	{
    	try
        {
        	mcQueContentDAO.removeQuestionContentByMcUid(mcContentUid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is removing mc que content by mc content id: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    public void resetAllQuestions(final Long mcContentUid) throws McApplicationException
	{
    	try
        {
        	mcQueContentDAO.resetAllQuestions(mcContentUid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is resetting all questions: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    
    public void cleanAllQuestions(final Long mcContentUid) throws McApplicationException
	{
    	try
        {
        	mcQueContentDAO.cleanAllQuestions(mcContentUid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is cleaning all questions: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public List getNextAvailableDisplayOrder(final long mcContentId) throws McApplicationException
	{
    	try
        {
        	return mcQueContentDAO.getNextAvailableDisplayOrder(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting the next available display order: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
  
    public void createMcSession(McSession mcSession) throws McApplicationException
    {
        try
        {
        	mcSessionDAO.saveMcSession(mcSession);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is creating mc session: "
                                                         + e.getMessage(),
														   e);
        }
    }

    
    public McSession getMcSessionByUID(Long uid) throws McApplicationException
    {
        try
        {
        	return mcSessionDAO.getMcSessionByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting mcSession my uid: "
                                                         + e.getMessage(),
														   e);
        }
    }


    public void createMcQueUsr(McQueUsr mcQueUsr) throws McApplicationException
    {
	   try
        {
	   		mcUserDAO.saveMcUser(mcQueUsr);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is creating mc QueUsr: "
                                                         + e.getMessage(),
														   e);
        }
    }

    public void updateMcQueUsr(McQueUsr mcQueUsr) throws McApplicationException
    {
	   try
        {
	   		mcUserDAO.updateMcUser(mcQueUsr);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is updating mc QueUsr: "
                                                         + e.getMessage(),
														   e);
        }
    }

    
    public McQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionId) throws McApplicationException
	{
 	   try
       {
	   		return mcUserDAO.getMcUserBySession(queUsrId, mcSessionId);
       }
       catch (DataAccessException e)
       {
           throw new McApplicationException("Exception occured when lams is getting mc QueUsr: "
                                                        + e.getMessage(),
														   e);
       }    	
	}
    
    
    public McQueUsr getMcUserByUID(Long uid) throws McApplicationException
    {
	   try
        {
	   		return mcUserDAO.getMcUserByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting the mc QueUsr by uid."
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public McQueUsr retrieveMcQueUsr(Long userId) throws McApplicationException
    {
	   try
        {
	   		McQueUsr mcQueUsr=mcUserDAO.findMcUserById(userId);
	   		return mcQueUsr;
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is retrieving McQueUsr: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public void createMcUsrAttempt(McUsrAttempt mcUsrAttempt) throws McApplicationException
    {
        try
        {
        	mcUsrAttemptDAO.saveMcUsrAttempt(mcUsrAttempt);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is creating mc UsrAttempt: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public List getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long mcQueContentId) throws McApplicationException
	{
        try
        {
        	return mcUsrAttemptDAO.getAttemptsForUserAndQuestionContent(queUsrId, mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting mc UsrAttempt by user id and que content id: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
	public void updateMcUsrAttempt(McUsrAttempt mcUsrAttempt) throws McApplicationException
    {
        try
        {
        	mcUsrAttemptDAO.updateMcUsrAttempt(mcUsrAttempt);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is updating mc UsrAttempt: "
                                                         + e.getMessage(),
														   e);
        }
    }
	
   
	public List getHighestMark(Long queUsrId) throws McApplicationException
	{
        try
        {
        	return mcUsrAttemptDAO.getHighestMark(queUsrId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting the learner's highest mark: "
                                                         + e.getMessage(),
														   e);
        }
	}
	

	public List getHighestAttemptOrder(Long queUsrId) throws McApplicationException
	{
        try
        {
        	return mcUsrAttemptDAO.getHighestAttemptOrder(queUsrId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting the learner's highest attempt order: "
                                                         + e.getMessage(),
														   e);
        }
	}

	
	public List getAttemptsForUser(final Long queUsrId) throws McApplicationException
	{
        try
        {
        	return mcUsrAttemptDAO.getAttemptsForUser(queUsrId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting the attempts by user id: "
                                                         + e.getMessage(),
														   e);
        }
		
	}
	
	
	public List getAttemptForQueContent(final Long queUsrId, final Long mcQueContentId) throws McApplicationException
	{
        try
        {
        	return mcUsrAttemptDAO.getAttemptForQueContent(queUsrId, mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting the learner's attempts by user id and que content id: "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	public List getAttemptByAttemptOrder(final Long queUsrId, final Long mcQueContentId, final Integer attemptOrder) throws McApplicationException
	{
        try
        {
        	return mcUsrAttemptDAO.getAttemptByAttemptOrder(queUsrId, mcQueContentId, attemptOrder);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting the learner's attempts by user id and que content id and attempt order: "
                                                         + e.getMessage(),
														   e);
        }
	}
	
    
    public McQueContent retrieveMcQueContentByUID(Long uid) throws McApplicationException
    {
        try
        {
            return mcQueContentDAO.getMcQueContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is retrieving by uid  mc question content: "
                                                         + e.getMessage(),
														   e);
        }
    }
   
    
    public void cleanAllQuestionsSimple(final Long mcContentId) throws McApplicationException
	{
    	try
        {
            mcQueContentDAO.cleanAllQuestionsSimple(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is cleaning mc question content by mcContentId : "
                                                         + e.getMessage(),
														   e);
        }    	
	}
    
    public List getAllQuestionEntries(final Long uid) throws McApplicationException
	{
	   try
        {
            return mcQueContentDAO.getAllQuestionEntries(uid.longValue());
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting by uid  mc question content: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public void removeMcQueContentByUID(Long uid) throws McApplicationException
	{
 	   try
       {
           mcQueContentDAO.removeMcQueContentByUID(uid);
       }
       catch (DataAccessException e)
       {
           throw new McApplicationException("Exception occured when lams is removing by uid  mc question content: "
                                                        + e.getMessage(),
														   e);
       }
	}
   

    public List refreshQuestionContent(final Long mcContentId) throws McApplicationException
	{
        try
        {
            return mcQueContentDAO.refreshQuestionContent(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is refreshing  mc question content: "
                                                         + e.getMessage(),
														   e);
        }
    	
	}
    
    public void removeMcQueContent(McQueContent mcQueContent) throws McApplicationException
	{
    	try
        {
            mcQueContentDAO.removeMcQueContent(mcQueContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is removing mc question content: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    public void removeMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException
    {
    	try
        {
            mcOptionsContentDAO.removeMcOptionsContent(mcOptsContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is removing"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
    }
    
    public List populateCandidateAnswersDTO(Long mcQueContentId) throws McApplicationException
    {
    	try
        {
            return mcOptionsContentDAO.populateCandidateAnswersDTO(mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is populating candidate answers dto"
                                                 + e.getMessage(),e);
        }
    }

    public List getPersistedSelectedOptions(Long mcQueContentId) throws McApplicationException
	{
    	try
        {
            return mcOptionsContentDAO.getPersistedSelectedOptions(mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is gettong persisted selected"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
    	
	}
    
    
    
    public McQueContent getQuestionContentByQuestionText(final String question, final Long mcContentId)
    {
        try
        {
            return mcQueContentDAO.getQuestionContentByQuestionText(question, mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is retrieving question content by question text: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public McSession retrieveMcSession(Long mcSessionId) throws McApplicationException
    {
    	try
        {
            return mcSessionDAO.findMcSessionById(mcSessionId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is retrieving by id mc session : "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public McSession findMcSessionById(Long mcSessionId) throws McApplicationException
	{
    	try
        {
            return mcSessionDAO.findMcSessionById(mcSessionId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is retrieving by id mc session : "
                                                         + e.getMessage(),
														   e);
        }
    	
	}
   
    public List getMcUserBySessionOnly(final McSession mcSession) throws McApplicationException
    {
     	try
        {
            return mcUserDAO.getMcUserBySessionOnly(mcSession);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is retrieving users by session: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public McContent retrieveMcBySessionId(Long mcSessionId) throws McApplicationException
    {
        try
        {
        	return mcContentDAO.getMcContentBySession(mcSessionId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is retrieving mc by session id: "
            								+ e.getMessage(),
                                              e);
        }
    }
   
    public List getSessionNamesFromContent(McContent mcContent) throws McApplicationException
    {
        try
        {
        	return mcSessionDAO.getSessionNamesFromContent(mcContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting session names from content: "
                                                         + e.getMessage(),
														   e);
        }
    }

    
    
    public void updateMc(McContent mc) throws McApplicationException
    {
        try
        {
            mcContentDAO.updateMcContent(mc);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is updating"
                                                 + " the mc content: "
                                                 + e.getMessage(),e);
        }
    }

    
    public void updateMcSession(McSession mcSession) throws McApplicationException
    {
    	try
        {
            mcSessionDAO.updateMcSession(mcSession);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is updating mc session : "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public void deleteMc(McContent mc) throws McApplicationException
    {
    	try
        {
            mcContentDAO.removeMc(mc);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is removing"
                                                 + " the mc content: "
                                                 + e.getMessage(),e);
        }
    }
    
    public void deleteMcById(Long mcId) throws McApplicationException
    {
    	try
        {
            mcContentDAO.removeMcById(mcId);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is removing by id"
                                                 + " the mc content: "
                                                 + e.getMessage(),e);
        }
    }

    
    public int countSessionComplete(McContent mcContent) throws McApplicationException
	{
		try
        {
			return mcSessionDAO.countSessionComplete(mcContent);
        }
		catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is counting incomplete sessions"
                                                 + e.getMessage(),e);
        }		
	}

    
	public int countSessionComplete() throws McApplicationException
	{
		try
        {
			return mcSessionDAO.countSessionComplete();
        }
		catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is counting incomplete sessions"
                                                 + e.getMessage(),e);
        }		
	}

    public int countSessionIncomplete() throws McApplicationException
	{
		try
        {
			return mcSessionDAO.countSessionIncomplete();
        }
		catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is counting incomplete sessions"
                                                 + e.getMessage(),e);
        }    	
	}
    
    public void deleteMcSession(McSession mcSession) throws McApplicationException 
	{
		try
        {
			mcSessionDAO.removeMcSession(mcSession);
        }
		catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is deleting"
                                                 + " the mc session: "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public void removeAttempt (McUsrAttempt attempt) throws McApplicationException
	{
    	try
        {
    		mcUsrAttemptDAO.removeMcUsrAttempt(attempt);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is removing"
                                                 + " the attempt: "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public List getMarks() throws McApplicationException
	{
    	try
        {
    		return mcUsrAttemptDAO.getMarks();
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting marks "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public McUsrAttempt getAttemptWithLastAttemptOrderForUserInSession(Long queUsrUid, final Long mcSessionUid) throws McApplicationException
    {
    	try
        {
    		return mcUsrAttemptDAO.getAttemptWithLastAttemptOrderForUserInSession(queUsrUid, mcSessionUid);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting last attempt order for user in session"
                                                 + e.getMessage(),e);
        }        
    }

    
    public List getUserAttemptsForQuestionContentAndSessionUid(final Long queUsrUid,  final Long mcQueContentId, final Long mcSessionUid)
    {
    	try
        {
    		return mcUsrAttemptDAO.getUserAttemptsForQuestionContentAndSessionUid(queUsrUid, mcQueContentId, mcSessionUid);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting marks based on userid, sessionid and que content id "
                                                 + e.getMessage(),e);
        }
    }
    
    
    public List getAttemptsForUserOnHighestAttemptOrderInSession(final Long queUsrUid, final Long mcSessionUid, final Integer attemptOrder) throws McApplicationException
    {
    	try
        {
    		return mcUsrAttemptDAO.getAttemptsForUserOnHighestAttemptOrderInSession(queUsrUid, mcSessionUid, attemptOrder);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is finding whether attempt is correct based on userid, sessionid"
                                                 + e.getMessage(),e);
        }        
    }
    
    public List getAttemptsOnHighestAttemptOrder(final Long queUsrUid,  final Long mcQueContentId, final Long mcSessionUid, final Integer attemptOrder) throws McApplicationException
    {
    	try
        {
    		return mcUsrAttemptDAO.getAttemptsOnHighestAttemptOrder(queUsrUid, mcQueContentId, mcSessionUid, attemptOrder);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting attempts on hightest attempt order"
                                                 + e.getMessage(),e);
        }        
    }
    
    public List getAttemptsForUserInSession(final Long queUsrUid, final Long mcSessionUid) throws McApplicationException
    {
    	try
        {
    		return mcUsrAttemptDAO.getAttemptsForUserInSession(queUsrUid, mcSessionUid);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is finding whether attempt is correct based on userid, sessionid"
                                                 + e.getMessage(),e);
        }
    }
    

    public boolean getUserAttemptCorrectForQuestionContentAndSessionUid(final Long queUsrUid,  final Long mcQueContentId, final Long mcSessionUid,  final Integer attemptOrder) throws McApplicationException
    {
    	try
        {
    		return mcUsrAttemptDAO.getUserAttemptCorrectForQuestionContentAndSessionUid(queUsrUid, mcQueContentId, mcSessionUid, attemptOrder);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is finding whether attempt is correct based on userid, sessionid and que content id "
                                                 + e.getMessage(),e);
        }
    }
    
    public void deleteMcQueUsr(McQueUsr mcQueUsr) throws McApplicationException
    {
    	try
        {
    		mcUserDAO.removeMcUser(mcQueUsr);
        }
        catch(DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is removing"
                                                 + " the user: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    public void saveMcContent(McContent mc) throws McApplicationException
    {
        try
        {
            mcContentDAO.saveMcContent(mc);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is saving"
                                                 + " the mc content: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    public List findMcOptionsContentByQueId(Long mcQueContentId) throws McApplicationException
    {
    	try
        {
            List list=mcOptionsContentDAO.findMcOptionsContentByQueId(mcQueContentId);
            return list;
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is finding by que id"
                                                 + " the mc options: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    public McOptsContent getMcOptionsContentByUID(Long uid) throws McApplicationException
	{
    	try
        {
            return mcOptionsContentDAO.getMcOptionsContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting opt content by uid"
                                                 + e.getMessage(),e);
        }
	}
    
    public List findMcOptionUidsByQueId(Long mcQueContentId)  throws McApplicationException
    {
    	try
        {
            return mcOptionsContentDAO.findMcOptionUidsByQueId(mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting opt uids"
                                                 + e.getMessage(),e);
        }
    }
    
    public McQueContent findMcQuestionContentByUid(Long uid) throws McApplicationException
    {
    	try
        {
            return mcQueContentDAO.findMcQuestionContentByUid(uid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting que content by uid"
                                                 + e.getMessage(),e);
        }
    }
    
    public McOptsContent findMcOptionsContentByUid(Long uid) throws McApplicationException
    {
    	try
        {
            return mcOptionsContentDAO.findMcOptionsContentByUid(uid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting opts by uid"
                                                 + e.getMessage(),e);
        }        
    }
    
    public void saveMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException
	{
    	try
        {
            mcOptionsContentDAO.saveMcOptionsContent(mcOptsContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is saving"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
	}
    
    public McOptsContent getOptionContentByOptionText(final String option, final Long mcQueContentUid)
    {
    	try
        {
            return mcOptionsContentDAO.getOptionContentByOptionText(option, mcQueContentUid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is returning the"
                                                 + " option by option text: "
                                                 + e.getMessage(),e);
        }
    }
    

   public List getCorrectOption(Long mcQueContentId)
   {
		try
	    {
	        return mcOptionsContentDAO.getCorrectOption(mcQueContentId);
	    }
	    catch (DataAccessException e)
	    {
	        throw new McApplicationException("Exception occured when lams is returning the "
	                                             + " correct option: "
	                                             + e.getMessage(),e);
	    }
   }
    
    public void updateMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException
	{
    	try
        {
            mcOptionsContentDAO.updateMcOptionsContent(mcOptsContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is updating"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
	}

    public List getSessionsFromContent(McContent mcContent) throws McApplicationException
	{
    	try
        {
            return mcSessionDAO.getSessionsFromContent(mcContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting"
                                                 + " the mc sessions list: "
                                                 + e.getMessage(),e);
        }
	}

    public List findMcOptionCorrectByQueId(Long mcQueContentId) throws McApplicationException
    {
    	try
        {
            return mcOptionsContentDAO.findMcOptionCorrectByQueId(mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is getting correct/incorrect options list"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
        
    }
    
    public void deleteMcOptionsContent(McOptsContent mcOptsContent) throws McApplicationException
	{
    	try
        {
            mcOptionsContentDAO.removeMcOptionsContent(mcOptsContent);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is removing"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public List findMcOptionNamesByQueId(Long mcQueContentId) throws McApplicationException
	{
    	try
        {
            return mcOptionsContentDAO.findMcOptionNamesByQueId(mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is finding"
                                                 + " the mc options name: "
                                                 + e.getMessage(),e);
        }
    	
	}
    
    
    public void removeMcOptionsContentByQueId(Long mcQueContentId) throws McApplicationException
    {
    	try
        {
            mcOptionsContentDAO.removeMcOptionsContentByQueId(mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is removing by que id"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    public void deleteMcOptionsContentByUID(Long uid) throws McApplicationException
	{
    	try
        {
            mcOptionsContentDAO.removeMcOptionsContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is removing by uid"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
	}


    public int countUserComplete(McContent mcContent) throws McApplicationException
	{
 	   try
       {
	   		return mcUserDAO.countUserComplete(mcContent);
       }
       catch (DataAccessException e)
       {
           throw new McApplicationException("Exception occured when lams is retrieving completed user count: "
                                                        + e.getMessage(),
														   e);
       }
	}
    
    
    
    public int getTotalNumberOfUsers(McContent mcContent) throws McApplicationException
	{
 	   try
       {
	   		return mcUserDAO.getTotalNumberOfUsers(mcContent);
       }
       catch (DataAccessException e)
       {
           throw new McApplicationException("Exception occured when lams is retrieving total number of McQueUsr: "
                                                        + e.getMessage(),
														   e);
       }
	}
    
    
    public int getTotalNumberOfUsers() throws McApplicationException
	{
 	   try
       {
	   		return mcUserDAO.getTotalNumberOfUsers();
       }
       catch (DataAccessException e)
       {
           throw new McApplicationException("Exception occured when lams is retrieving total number of McQueUsr: "
                                                        + e.getMessage(),
														   e);
       }
	}
    
    public User getCurrentUserData(String username) throws McApplicationException
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
        		logger.error("No user with the username: "+ username +  " exists.");
        		throw new McApplicationException("No user with that username exists.");
        	}
        	return user;	 
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Unable to find current user information"
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
     * @throws McApplicationException
     */
    public Lesson getCurrentLesson(long lessonId) throws McApplicationException
    {
        try
        {
            /**this is a mock implementation to make the project compile and 
            work. When the Lesson service is ready, we need to switch to 
            real service implementation.
            */
            return new Lesson();
            /**return lsDAO.find(lsessionId); */
        }
        catch (DataAccessException e)
        {
            throw new McApplicationException("Exception occured when lams is loading"
                                                 + " learning session:"
                                                 + e.getMessage(),
                                                 e);
        }
    }

    
	/**
	 * checks the parameter content in the user responses table 
	 * @param mcContent
	 * @return boolean
	 * @throws McApplicationException
	 */
	public boolean studentActivityOccurredGlobal(McContent mcContent) throws McApplicationException
	{
		Iterator questionIterator=mcContent.getMcQueContents().iterator();
        while (questionIterator.hasNext())
        {
        	McQueContent mcQueContent=(McQueContent)questionIterator.next(); 
        	Iterator attemptsIterator=mcQueContent.getMcUsrAttempts().iterator();
        	while (attemptsIterator.hasNext())
        	{
        		logger.debug("there is at least one attempt");
        		/**
        		 * proved the fact that there is at least one attempt for this content.
        		 */
        		return true;
        	}
        } 
        logger.debug("there is no response for this content");
		return false;
	}
	

	public int countIncompleteSession(McContent mc) throws McApplicationException
	{
		//int countIncompleteSession=mcSessionDAO.countIncompleteSession(mc);
		int countIncompleteSession=2;
		return countIncompleteSession;
	}
	
	/**
	 * checks the parameter content in the tool sessions table
	 * 
	 * find out if any student has ever used (logged in through the url  and replied) to this content
	 * return true even if you have only one content passed as parameter referenced in the tool sessions table
	 * @param mc
	 * @return boolean
	 * @throws McApplicationException
	 */
	public boolean studentActivityOccurred(McContent mc) throws McApplicationException
	{
		//int countStudentActivity=mcSessionDAO.studentActivityOccurred(mc);
		int countStudentActivity=2;
		
		if (countStudentActivity > 0)
			return true;
		return false;
	}
	

	/**
	 * implemented as part of the Tool Contract
	 * copyToolContent(Long fromContentId, Long toContentId) throws ToolException
	 * @param fromContentId
	 * @param toContentId
	 * @return 
	 * @throws ToolException
	 * 
	 */
	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException
    {
    	logger.debug("start of copyToolContent with ids: " + fromContentId + " and " + toContentId);

    	if (fromContentId == null)
        {
        	logger.error("fromContentId is null.");
        	logger.debug("attempt retrieving tool's default content id with signatute : " + MY_SIGNATURE);
        	long defaultContentId=0;
			try
			{
				defaultContentId=getToolDefaultContentIdBySignature(MY_SIGNATURE);
				fromContentId= new Long(defaultContentId);
			}
			catch(Exception e)
			{
				logger.error("default content id has not been setup for signature: " +  MY_SIGNATURE);
				throw new ToolException("WARNING! default content has not been setup for signature" + MY_SIGNATURE + " Can't continue!");
			}
        }
        
        if (toContentId == null)
        {
        	logger.error("throwing ToolException: toContentId is null");
			throw new ToolException("toContentId is missing");
        }
        logger.debug("final - copyToolContent using ids: " + fromContentId + " and " + toContentId);
            
        try
        {
            McContent fromContent = mcContentDAO.findMcContentById(fromContentId);
        
            if (fromContent == null)
            {
            	logger.error("fromContent is null.");
            	logger.error("attempt retrieving tool's default content id with signatute : " + MY_SIGNATURE);
            	long defaultContentId=0;
    			try
    			{
    				defaultContentId=getToolDefaultContentIdBySignature(MY_SIGNATURE);
    				fromContentId= new Long(defaultContentId);
    			}
    			catch(Exception e)
    			{
    				logger.error("default content id has not been setup for signature: " +  MY_SIGNATURE);
    				throw new ToolException("WARNING! default content has not been setup for signature" + MY_SIGNATURE + " Can't continue!");
    			}
    			
    			fromContent = mcContentDAO.findMcContentById(fromContentId);
    			logger.debug("using fromContent: " + fromContent);
            }
            
            logger.debug("final - retrieved fromContent: " + fromContent);
            logger.debug("final - before new instance using " + fromContent + " and " + toContentId);
            logger.debug("final - before new instance using mcToolContentHandler: " + mcToolContentHandler);
            
            try
			{
            	McContent toContent = McContent.newInstance(mcToolContentHandler, fromContent,toContentId);
                if (toContent == null)
                {
                	logger.debug("throwing ToolException: WARNING!, retrieved toContent is null.");
                	throw new ToolException("WARNING! Fail to create toContent. Can't continue!");
                }
                else
                {
                	logger.debug("retrieved toContent: " + toContent);
    	            mcContentDAO.saveMcContent(toContent);
    	            logger.debug("toContent has been saved successfully: " + toContent);
                }
                logger.debug("end of copyToolContent with ids: " + fromContentId + " and " + toContentId);

			}
        	catch(ItemNotFoundException e)
			{
        		logger.error("exception occurred: " +  e);
			}
        	catch(RepositoryCheckedException e)
			{
        		logger.error("exception occurred: " +  e);
			}
        }
        catch (DataAccessException e)
        {
        	logger.error("throwing ToolException: Exception occured when lams is copying content between content ids.");
            throw new ToolException("Exception occured when lams is copying content between content ids."); 
        }
    }

	
    /**
     * implemented as part of the tool contract. Removes content and uploaded files from the content repository.
     * removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException
	 * @param toContentId
	 * @param removeSessionData 
	 * @return 
	 * @throws ToolException 
     */    
    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException
	{
    	logger.debug("start of removeToolContent with toolContentId: " + toolContentId + "removeSessionData: " + removeSessionData);
    	
    	if (toolContentId == null)
    	{
    		logger.error("toolContentId is null");
    		throw new ToolException("toolContentId is missing");
    	}
    	
    	McContent mcContent = mcContentDAO.findMcContentById(toolContentId);
    	logger.debug("retrieving mcContent: " + mcContent);
    	
    	if (mcContent != null)
    	{
            logger.error("start deleting any uploaded file for this content from the content repository");
        	Iterator filesIterator=mcContent.getMcAttachments().iterator();
        	while (filesIterator.hasNext())
        	{
        		McUploadedFile mcUploadedFile=(McUploadedFile) filesIterator.next();
        		logger.debug("iterated mcUploadedFile : " + mcUploadedFile);
        		String filesUuid=mcUploadedFile.getUuid(); 
        		if ((filesUuid != null) && (filesUuid.length() > 0))
        		{
        			try
					{
        				mcToolContentHandler.deleteFile(new Long(filesUuid));	
					}
        			catch(RepositoryCheckedException e)
					{
        				logger.error("exception occured deleting files from content repository : " + e);
        				throw new ToolException("undeletable file in the content repository");
					}
        		}
        	}
        	logger.debug("end deleting any uploaded files for this content.");
    		
    		Iterator sessionIterator=mcContent.getMcSessions().iterator();
            while (sessionIterator.hasNext())
            {
            	if (removeSessionData == false)
            	{
            		logger.debug("removeSessionData is false, throwing SessionDataExistsException.");
            		throw new SessionDataExistsException();	
            	}
            	
            	McSession mcSession=(McSession)sessionIterator.next(); 
            	logger.debug("iterated mcSession : " + mcSession);
            	
            	Iterator sessionUsersIterator=mcSession.getMcQueUsers().iterator();
            	while (sessionUsersIterator.hasNext())
            	{
            		McQueUsr mcQueUsr=(McQueUsr) sessionUsersIterator.next();
            		logger.debug("iterated mcQueUsr : " + mcQueUsr);
            		
            		Iterator sessionUsersAttemptsIterator=mcQueUsr.getMcUsrAttempts().iterator();
            		while (sessionUsersAttemptsIterator.hasNext())
                	{
            			McUsrAttempt mcUsrAttempt=(McUsrAttempt)sessionUsersAttemptsIterator.next();
            			logger.debug("iterated mcUsrAttempt : " + mcUsrAttempt);
            			removeAttempt(mcUsrAttempt);
            			logger.debug("removed mcUsrAttempt : " + mcUsrAttempt);
                	}
            	}
            }
            logger.debug("removed all existing responses of toolContent with toolContentId:" + 
            																toolContentId);   
            mcContentDAO.removeMcById(toolContentId);        
            logger.debug("removed mcContent:" + mcContent);
    	}
    	else
    	{
        	logger.error("Warning!!!, We should have not come here. mcContent is null.");
        	throw new ToolException("toolContentId is missing");
    	}
	}


    
    /**
     * TO BE DEFINED-FUTURE API
     * gets called from monitoring module
     * 
     * update the tool session status to COMPLETE for this tool session
     * 
     * @param Long toolSessionId
     */
    public void setAsForceCompleteSession(Long toolSessionId) throws McApplicationException
    {
    	McSession mcSession=retrieveMcSession(toolSessionId);	
    	mcSession.setSessionStatus(McSession.COMPLETED);
    	updateMcSession(mcSession);
   }

    
    /**
     * TO BE DEFINED
     * 
     * update the tool session status to COMPLETE for this user
     * IMPLEMENT THIS!!!! Is this from ToolContentManager???
     * 

     * @param userId
     */
    public void setAsForceComplete(Long userId) throws McApplicationException
    {
    	McQueUsr mcQueUsr=retrieveMcQueUsr(userId);
    	
    	if (mcQueUsr != null)
    	{
    		logger.error("retrieved mcQueUsr : " + mcQueUsr);
        	logger.error("retrieved mcQueUsr  has the tool session : " + mcQueUsr.getMcSession());
        	McSession mcSession=mcQueUsr.getMcSession();
        	if (mcSession != null)
        	{
        		Long usersToolSessionId=mcSession.getMcSessionId();
            	logger.debug("retrieved  tool session has tool session id : " + usersToolSessionId);
            	
            	mcSession=retrieveMcSession(usersToolSessionId);	
            	logger.debug("retrieved  mcSession is : " + mcSession);
            	mcSession.setSessionStatus(McSession.COMPLETED);
            	logger.debug("updated  mcSession to COMPLETED : ");
            	updateMcSession(mcSession);
            	logger.debug("updated  mcSession to COMPLETED in the db : ");
            	
            	McContent mcContent=mcSession.getMcContent();
            	logger.debug("mcSession uses mcContent : " + mcContent);
            	logger.debug("mcSession uses mcContentId : " + mcContent.getMcContentId());
            	
            	/**
            	 * if all the sessions of this content is COMPLETED, unlock the content
            	 * 
            	 */
            	int countIncompleteSession=countIncompleteSession(mcContent);
            	logger.debug("mcSession countIncompleteSession : " + countIncompleteSession);
            	
            	if (countIncompleteSession == 0)
            	{
            		mcContent.setContentInUse(false);
            		updateMc(mcContent);
                	logger.debug("mcContent has been updated for contentInUse" + mcContent);
            	}
        	}
        	else
        	{
        		logger.error("WARNING!: retrieved mcSession is null.");
        		throw new McApplicationException("Fail to setAsForceComplete"
                        + " based on null mcSession.");
        	}
    	}
    	else
    	{
    		logger.error("WARNING!: retrieved mcQueUsr is null.");
            throw new McApplicationException("Fail to setAsForceComplete"
                    + " based on null mcQueUsr.");
    	}
    }
    
    
    public void unsetAsDefineLater(Long toolContentId) throws McApplicationException
    {
    	logger.debug("request for unsetAsDefineLater with toolContentId: " + toolContentId);
    	if  (toolContentId == null)
    	{
    		logger.error("WARNING!: retrieved toolContentId is null.");
            throw new McApplicationException("Fail to setAsDefineLater"
                    + " based on null toolContentId.");
    	}
    	McContent mcContent = mcContentDAO.findMcContentById(toolContentId);
    	if (mcContent == null)
    	{
    		logger.error("WARNING!!!: retrieved mcContent is null.");
            throw new McApplicationException("Fail to unsetAsDefineLater"
                    + " based on null mcContent.");
    	}
    	mcContent.setDefineLater(false);
    	createMc(mcContent);
    	logger.debug("mcContent has been updated for unsetAsDefineLater: " + mcContent);
    }
    
    
    /**
     * Implemented as part of the tool contract. Sets the defineLater to true on this content.
     * setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException
     * @param toolContentId
     * @return 
     * @throws ToolException
     */
    public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException
    {
    	logger.debug("request for setAsDefineLater with toolContentId: " + toolContentId);
    	if  (toolContentId == null)
    	{
    		logger.error("throwing DataMissingException: WARNING!: retrieved toolContentId is null.");
            throw new DataMissingException("toolContentId is missing");
    	}
    	
    	McContent mcContent=retrieveMc(toolContentId);
    	if (mcContent == null)
    	{
    		logger.error("throwing DataMissingException: WARNING!: retrieved mcContent is null.");
            throw new DataMissingException("mcContent is missing");
    	}
    	mcContent.setDefineLater(true);
    	saveMcContent(mcContent);
    	logger.debug("success: end of setAsDefineLater on toolContentId:" + toolContentId);
    }
    

    /**
     * Implemented as part of the tool contract. Sets the runOffline to true on this content.
     * setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException
     * 
     * @param toolContentId
     * return 
     * @throws ToolException 
     */
    public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException
    {
    	logger.debug("request for setAsRunOffline with toolContentId:" + toolContentId);
    	if  (toolContentId == null)
    	{
    		logger.error("throwing DataMissingException: WARNING!: retrieved toolContentId is null.");
            throw new DataMissingException("toolContentId is missing");
    	}
    	McContent mcContent = mcContentDAO.findMcContentById(toolContentId);
    	if (mcContent == null)
    	{
    		logger.error("throwing DataMissingException: WARNING!: retrieved mcContent is null.");
            throw new DataMissingException("mcContent is missing");
    	}
    	mcContent.setRunOffline(true);
    	saveMcContent(mcContent);
    	logger.debug("success: end of setAsRunOffline on toolContentId:" + toolContentId);
    }
    
    
	/**
     * Export the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws DataMissingException if no tool content matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */

	public void exportToolContent(Long toolContentId, String rootPath) throws DataMissingException, ToolException {
		McContent toolContentObj = mcContentDAO.findMcContentById(toolContentId);
 		if(toolContentObj == null) {
 			long defaultContentId=getToolDefaultContentIdBySignature(MY_SIGNATURE);
 			toolContentObj = mcContentDAO.findMcContentById(defaultContentId);
 		}
 		if(toolContentObj == null)
 			throw new DataMissingException("Unable to find default content for the multiple choice tool");
 		
		try {
			//set ToolContentHandler as null to avoid copy file node in repository again.
			toolContentObj = McContent.newInstance(null,toolContentObj,toolContentId);
			toolContentObj.setMcSessions(null);
			Set<McUploadedFile> files = toolContentObj.getMcAttachments();
			for(McUploadedFile file : files){
				file.setMcContent(null);
			}
			exportContentService.registerFileClassForExport(McUploadedFile.class.getName(),"uuid",null);
			exportContentService.exportToolContent( toolContentId, toolContentObj,mcToolContentHandler, rootPath);
		} catch (ExportToolContentException e) {
			throw new ToolException(e);
		} catch (ItemNotFoundException e) {
			throw new ToolException(e);
		} catch (RepositoryCheckedException e) {
			throw new ToolException(e);
		}		
	}

    /**
     * Import the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws ToolException if any other error occurs
     */
	public void importToolContent(Long toolContentId, Integer newUserUid, String toolContentPath) throws ToolException {
		try {
			exportContentService.registerFileClassForImport(McUploadedFile.class.getName()
					,"uuid",null,"fileName","fileProperty",null,null);
			
			Object toolPOJO =  exportContentService.importToolContent(toolContentPath,mcToolContentHandler);
			if(!(toolPOJO instanceof McContent))
				throw new ImportToolContentException("Import MC tool content failed. Deserialized object is " + toolPOJO);
			McContent toolContentObj = (McContent) toolPOJO;
			
//			reset it to new toolContentId
			toolContentObj.setMcContentId(toolContentId);
			toolContentObj.setCreatedBy(newUserUid);
			Set<McUploadedFile> files = toolContentObj.getMcAttachments();
			for(McUploadedFile file : files){
				//file.setMcContentId(toolContentId);
				file.setMcContent(toolContentObj);
			}
			mcContentDAO.saveMcContent(toolContentObj);
		} catch (ImportToolContentException e) {
			throw new ToolException(e);
		}
	}



	/**
	 * it is possible that the tool session id already exists in the tool sessions table
	 * as the users from the same session are involved.
	 * existsSession(long toolSessionId)
	 * @param toolSessionId
	 * @return boolean
	 */
	public boolean existsSession(Long toolSessionId) 
	{
		McSession mcSession= retrieveMcSession(toolSessionId);
    	
	    if (mcSession == null) 
	    {
	    	logger.error("mcSession does not exist yet: " + toolSessionId);
	    	return false;
	    }
	    else
	    {
	    	logger.debug("retrieving an existing mcSession: " + mcSession + " " + toolSessionId);
	    }
		return true;	
	}
    
    /**
     * Implemented as part of the tool contract.
     * Gets called only in the Learner mode.
     * All the learners in the same group have the same toolSessionId.
     * 
     * @param toolSessionId the generated tool session id.
     * @param toolSessionName the tool session name.
     * @param toolContentId the tool content id specified.
     * @throws ToolException if an error occurs e.g. defaultContent is missing. 
     * 
     */
	public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException
    {
    	logger.debug("start of createToolSession with ids: " + toolSessionId + " and " + toolContentId);
    	logger.debug("toolSessionName: " + toolSessionName);
    	
    	if (toolSessionId == null)
    	{
    		logger.error("toolSessionId is null");
    		throw new ToolException("toolSessionId is missing");
    	}
    	
    	long defaultContentId=0;
    	if (toolContentId == null)
        {
        	logger.error("toolContentId is null.");
        	logger.error("attempt retrieving tool's default content id with signatute : " + MY_SIGNATURE);
        
			try
			{
				defaultContentId=getToolDefaultContentIdBySignature(MY_SIGNATURE);
				toolContentId=new Long(defaultContentId);
				logger.debug("updated toolContentId to: " + toolContentId);
			}
			catch(Exception e)
			{
				logger.error("default content id has not been setup for signature: " +  MY_SIGNATURE);
				throw new ToolException("WARNING! default content has not been setup for signature" + MY_SIGNATURE + " Can't continue!");
			}
        }
    	logger.debug("final toolSessionId and toolContentId: " +  toolSessionId + " " + toolContentId);
    	
        McContent mcContent = mcContentDAO.findMcContentById(toolContentId);
        logger.debug("retrieved mcContent: " + mcContent);
        
        if (mcContent == null)
        {
        	logger.error("mcContent is null.");
        	logger.error("attempt retrieving tool's default content id with signatute : " + MY_SIGNATURE);
        
			try
			{
				defaultContentId=getToolDefaultContentIdBySignature(MY_SIGNATURE);
				toolContentId=new Long(defaultContentId);
				logger.debug("updated toolContentId to: " + toolContentId);
			}
			catch(Exception e)
			{
				logger.error("default content id has not been setup for signature: " +  MY_SIGNATURE);
				throw new ToolException("WARNING! default content has not been setup for signature" + MY_SIGNATURE + " Can't continue!");
			}

			mcContent = mcContentDAO.findMcContentById(toolContentId);
        }
        logger.debug("final - retrieved mcContent: " + mcContent);

            
        /*
         * create a new a new tool session if it does not already exist in the tool session table
         */
        if (!existsSession(toolSessionId))
        {
        	try
			{
        		McSession mcSession = new McSession(toolSessionId,
                        new Date(System.currentTimeMillis()),
                        McSession.INCOMPLETE,
                        toolSessionName,
                        mcContent,
                        new TreeSet());

    		    logger.debug("created mcSession: " + mcSession);
    		    mcSessionDAO.saveMcSession(mcSession);
    		    logger.debug("created mcSession in the db: " + mcSession);	
	
        	}
        	catch(Exception e)
			{
        		logger.error("Error creating new toolsession in the db");
				throw new ToolException("Error creating new toolsession in the db: " + e);
			}
        }
    }


    /**
     * Implemented as part of the tool contract.
     * removeToolSession(Long toolSessionId) throws DataMissingException, ToolException
     * @param toolSessionId
     * @param toolContentId 
     * return 
     * @throws ToolException
     */
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException
	{
    	logger.debug("start of removeToolSession with id: " + toolSessionId);
    	if (toolSessionId == null)
    	{
    		logger.error("toolSessionId is null");
    		throw new DataMissingException("toolSessionId is missing");
    	}
    	
    	
    	McSession mcSession=null;
    	try
		{
    		mcSession=retrieveMcSession(toolSessionId);
    		logger.debug("retrieved mcSession: " + mcSession);
		}
    	catch(McApplicationException e)
		{
    		throw new DataMissingException("error retrieving mcSession: " + e);
		}
    	catch(Exception e)
		{
    		throw new ToolException("error retrieving mcSession: " + e);
		}
    	
    	if (mcSession == null)
    	{
    		logger.error("mcSession is null");
    		throw new DataMissingException("mcSession is missing");
    	}
    	
    	try
		{
    		mcSessionDAO.removeMcSession(mcSession);
        	logger.debug("mcSession " + mcSession + " has been deleted successfully.");	
		}
    	catch(McApplicationException e)
		{
    		throw new ToolException("error deleting mcSession:" + e);
		}
	}
	

    /**
     * Implemtented as part of the tool contract.
     * leaveToolSession(Long toolSessionId,Long learnerId) throws DataMissingException, ToolException
     * @param toolSessionId
     * @param learnerId 
     * return String
     * @throws ToolException
     * 
     */
    public String leaveToolSession(Long toolSessionId,Long learnerId) throws DataMissingException, ToolException 
    {
        logger.debug("start of leaveToolSession with toolSessionId:" + toolSessionId + " and learnerId:" + learnerId);
        logger.debug("make sure learnerService is available. Is it?" + learnerService);
        
        if (learnerService == null)
			return "dummyNextUrl";
        
        if (learnerId == null)
    	{
    		logger.error("learnerId is null");
    		throw new DataMissingException("learnerId is missing");
    	}
        
        if (toolSessionId == null)
    	{
    		logger.error("toolSessionId is null");
    		throw new DataMissingException("toolSessionId is missing");
    	}
        
        McSession mcSession=null;
    	try
		{
    		mcSession=retrieveMcSession(toolSessionId);
    		logger.debug("retrieved mcSession: " + mcSession);
		}
    	catch(McApplicationException e)
		{
    		throw new DataMissingException("error retrieving mcSession: " + e);
		}
    	catch(Exception e)
		{
    		throw new ToolException("error retrieving mcSession: " + e);
		}
    	mcSession.setSessionStatus(COMPLETED);
    	mcSessionDAO.updateMcSession(mcSession);
    	logger.debug("updated mcSession to COMPLETED" + mcSession);
    	
    	String nextUrl= learnerService.completeToolSession(toolSessionId,learnerId);
    	logger.debug("nextUrl: " + nextUrl);
    	if (nextUrl == null)
    	{
    		logger.error("nextUrl is null");
    		throw new ToolException("nextUrl is null");
    	}
    	return nextUrl;
    }
    

    /**
     * exportToolSession(Long toolSessionId) throws DataMissingException, ToolException
     * @param toolSessionId
     * return ToolSessionExportOutputData
     * @throws ToolException
     */
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException
    {
        throw new ToolException("not yet implemented");
    }

    
    /**
     * exportToolSession(Long toolSessionId) throws DataMissingException, ToolException
     * @param toolSessionIds
     * return ToolSessionExportOutputData
     * @throws ToolException
     */
    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException
    {
        throw new ToolException("not yet implemented");

    }
    
    public IToolVO getToolBySignature(String toolSignature) throws McApplicationException
    {
    	logger.debug("attempt retrieving tool with signature : " + toolSignature);
    	IToolVO tool=toolService.getToolBySignature(toolSignature);
    	logger.debug("retrieved tool: " + tool);
	    return tool;
    }
    
    public long getToolDefaultContentIdBySignature(String toolSignature) throws McApplicationException
    {
    	long contentId=0;
    	contentId=toolService.getToolDefaultContentIdBySignature(toolSignature);
    	logger.debug("tool default contentId : " + contentId);
	    return contentId;
    }

    public McQueContent getToolDefaultQuestionContent(long contentId) throws McApplicationException
    {
    	McQueContent mcQueContent=mcQueContentDAO.getToolDefaultQuestionContent(contentId);
    	logger.debug("retrieved mcQueContent : " + mcQueContent);
    	return mcQueContent; 
    }

    
    public List getToolSessionsForContent(McContent mc)
    {
    	logger.debug("attempt retrieving listToolSessionIds for : " + mc);
    	List listToolSessionIds=mcSessionDAO.getSessionsFromContent(mc);
    	return listToolSessionIds;
    }
    

    public void removeAttachment(McContent content, McUploadedFile attachment) throws RepositoryCheckedException
	{
	    try
	    {
			attachment.setMcContent(null);
			content.getMcAttachments().remove(attachment);
			mcToolContentHandler.deleteFile(new Long(attachment.getUuid()));
			saveMcContent(content);
	    }
	    catch (DataAccessException e)
	    {
	        throw new McApplicationException("EXCEPTION: An exception has occurred while trying to remove this attachment"
	                + e.getMessage(), e);
	    }
	}
    
    
    public NodeKey uploadFile(InputStream istream, String filename, String contentType, String fileType) throws RepositoryCheckedException
	{
	    return mcToolContentHandler.uploadFile(istream, filename, contentType, fileType); 
	}
    
    
    public NodeKey copyFile(Long uuid) throws RepositoryCheckedException
	{
	    return mcToolContentHandler.copyFile(uuid);
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
	public ITicket getRepositoryLoginTicket() throws McApplicationException {
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
			throw new McApplicationException("Access Denied to repository."
					+ e.getMessage());
		} catch (WorkspaceNotFoundException e) {
			throw new McApplicationException("Workspace not found."
					+ e.getMessage());
		} catch (LoginException e) {
			throw new McApplicationException("Login failed." + e.getMessage());
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
			throws McApplicationException {
		ITicket ticket = getRepositoryLoginTicket();
		logger.debug("retrieved ticket: " + ticket);
		try {
			String files[] = repositoryService.deleteVersion(ticket, uuid,versionID);
			logger.debug("retrieved files: " + files);
		} catch (Exception e) {
			throw new McApplicationException(
					"Exception occured while deleting files from"
							+ " the repository " + e.getMessage());
		}
	}
	
	
	/**
	 * This method is called everytime a new content has to be added to the
	 * repository. In order to do so first of all a valid ticket is obtained
	 * from the Repository hence authenticating the tool(SubmitFiles) and then
	 * the corresponding file is added to the repository.
	 * 
	 * @param stream
	 *            The <code>InputStream</code> representing the data to be
	 *            added
	 * @param fileName
	 *            The name of the file being added
	 * @param mimeType
	 *            The MIME type of the file (eg. TXT, DOC, GIF etc)
	 * @return NodeKey Represents the two part key - UUID and Version.
	 * @throws SubmitFilesException
	 */
	public NodeKey uploadFileToRepository(InputStream stream, String fileName) throws McApplicationException {
		logger.debug("attempt getting the ticket");
		ITicket ticket = getRepositoryLoginTicket();
		logger.debug("retrieved ticket: " + ticket);
		
		try {
			NodeKey nodeKey = repositoryService.addFileItem(ticket, stream,
					fileName, null, null);
			logger.debug("retrieved nodeKey from repository service: " + nodeKey);
			return nodeKey;
		} catch (Exception e) {
			throw new McApplicationException("Exception occured while trying to"
					+ " upload file into the repository" + e.getMessage());
		}
	}

	public InputStream downloadFile(Long uuid, Long versionID)throws McApplicationException{
		ITicket ticket = getRepositoryLoginTicket();		
		try{
			IVersionedNode node = repositoryService.getFileItem(ticket,uuid,null);
			logger.debug("retrieved node: " + node);
			return node.getFile();
		}catch(AccessDeniedException e){
			throw new McApplicationException("AccessDeniedException occured while trying to download file " + e.getMessage());
		}catch(FileException e){
			throw new McApplicationException("FileException occured while trying to download file " + e.getMessage());
		}catch(ItemNotFoundException e){
			throw new McApplicationException("ItemNotFoundException occured while trying to download file " + e.getMessage());			
		}
	}
	
	
	
	/**
	 * adds a new entry to the uploaded files table
	 */
	public void persistFile(String uuid, boolean isOnlineFile, String fileName, McContent mcContent) throws McApplicationException {
		
		logger.debug("attempt persisting file to the db: " + uuid + " " + isOnlineFile + " " + fileName + " " + mcContent);
		McUploadedFile mcUploadedFile= new McUploadedFile(uuid, isOnlineFile, fileName, mcContent);
		logger.debug("created mcUploadedFile: " + mcUploadedFile);
		mcUploadedFileDAO.saveUploadFile(mcUploadedFile);
		logger.debug("persisted mcUploadedFile: " + mcUploadedFile);
	}

	/**
	 * 
	 * removes all the entries in the uploaded files table
	 */
	public void cleanUploadedFilesMetaData() throws McApplicationException {
		logger.debug("attempt cleaning up uploaded file meta data table from the db");
		mcUploadedFileDAO.cleanUploadedFilesMetaData();
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
		McServicePOJO.logger = logger;
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
	
	/*
	   !!! COMPLETE THIS !!!
	public IMcUploadedFileDAO getMcUploadedFileDAO() {
		return mcUploadedFileDAO;
	}
	
	public void setMcUploadedFileDAO(IMcUploadedFileDAO mcUploadedFileDAO) {
		this.mcUploadedFileDAO = mcUploadedFileDAO;
	}
	
	*/
	
	
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
	/**
	 * @return Returns the mcContentDAO.
	 */
	public IMcContentDAO getMcContentDAO() {
		return mcContentDAO;
	}
	/**
	 * @param mcContentDAO The mcContentDAO to set.
	 */
	public void setMcContentDAO(IMcContentDAO mcContentDAO) {
		this.mcContentDAO = mcContentDAO;
	}
	/**
	 * @return Returns the mcOptionsContentDAO.
	 */
	public IMcOptionsContentDAO getMcOptionsContentDAO() {
		return mcOptionsContentDAO;
	}
	/**
	 * @param mcOptionsContentDAO The mcOptionsContentDAO to set.
	 */
	public void setMcOptionsContentDAO(IMcOptionsContentDAO mcOptionsContentDAO) {
		this.mcOptionsContentDAO = mcOptionsContentDAO;
	}
	/**
	 * @return Returns the mcQueContentDAO.
	 */
	public IMcQueContentDAO getMcQueContentDAO() {
		return mcQueContentDAO;
	}
	/**
	 * @param mcQueContentDAO The mcQueContentDAO to set.
	 */
	public void setMcQueContentDAO(IMcQueContentDAO mcQueContentDAO) {
		this.mcQueContentDAO = mcQueContentDAO;
	}
	/**
	 * @return Returns the mcSessionDAO.
	 */
	public IMcSessionDAO getMcSessionDAO() {
		return mcSessionDAO;
	}
	/**
	 * @param mcSessionDAO The mcSessionDAO to set.
	 */
	public void setMcSessionDAO(IMcSessionDAO mcSessionDAO) {
		this.mcSessionDAO = mcSessionDAO;
	}
	/**
	 * @return Returns the mcUserDAO.
	 */
	public IMcUserDAO getMcUserDAO() {
		return mcUserDAO;
	}
	/**
	 * @param mcUserDAO The mcUserDAO to set.
	 */
	public void setMcUserDAO(IMcUserDAO mcUserDAO) {
		this.mcUserDAO = mcUserDAO;
	}
	/**
	 * @return Returns the mcUsrAttemptDAO.
	 */
	public IMcUsrAttemptDAO getMcUsrAttemptDAO() {
		return mcUsrAttemptDAO;
	}
	/**
	 * @param mcUsrAttemptDAO The mcUsrAttemptDAO to set.
	 */
	public void setMcUsrAttemptDAO(IMcUsrAttemptDAO mcUsrAttemptDAO) {
		this.mcUsrAttemptDAO = mcUsrAttemptDAO;
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
	 * @return Returns the mcUploadedFileDAO.
	 */
	public IMcUploadedFileDAO getMcUploadedFileDAO() {
		return mcUploadedFileDAO;
	}
	/**
	 * @param mcUploadedFileDAO The mcUploadedFileDAO to set.
	 */
	public void setMcUploadedFileDAO(IMcUploadedFileDAO mcUploadedFileDAO) {
		this.mcUploadedFileDAO = mcUploadedFileDAO;
	}

	/**
	 * @return Returns the mcToolContentHandler.
	 */
	public IToolContentHandler getMcToolContentHandler() {
		return mcToolContentHandler;
	}
	/**
	 * @param mcToolContentHandler The mcToolContentHandler to set.
	 */
	public void setMcToolContentHandler(IToolContentHandler mcToolContentHandler) {
		this.mcToolContentHandler = mcToolContentHandler;
	}
	/**
	 * @return Returns the learnerService.
	 */
	public ILearnerService getLearnerService() {
		return learnerService;
	}
	/**
	 * @param learnerService The learnerService to set.
	 */
	public void setLearnerService(ILearnerService learnerService) {
		this.learnerService = learnerService;
	}

	public IExportToolContentService getExportContentService() {
		return exportContentService;
	}

	public void setExportContentService(IExportToolContentService exportContentService) {
		this.exportContentService = exportContentService;
	}

	/* ===============Methods implemented from ToolContentImport102Manager =============== */
	

    /**
     * Import the data for a 1.0.2 Chat
     */
    public void import102ToolContent(Long toolContentId, UserDTO user, Hashtable importValues)
    {
    	Date now = new Date();
    	McContent toolContentObj = new McContent();
    	toolContentObj.setContentInUse(Boolean.FALSE.booleanValue());
    	toolContentObj.setCreatedBy(user.getUserID().longValue());
    	toolContentObj.setCreationDate(now);
    	toolContentObj.setDefineLater(Boolean.FALSE.booleanValue());
    	toolContentObj.setInstructions(WebUtil.convertNewlines((String)importValues.get(ToolContentImport102Manager.CONTENT_BODY)));
    	toolContentObj.setOfflineInstructions(null);
    	toolContentObj.setOnlineInstructions(null);
 	    toolContentObj.setReflect(Boolean.FALSE);
    	toolContentObj.setReflectionSubject(null);
    	toolContentObj.setRunOffline(Boolean.FALSE.booleanValue());
    	toolContentObj.setTitle((String)importValues.get(ToolContentImport102Manager.CONTENT_TITLE));
    	
    	toolContentObj.setContent(null);
    	toolContentObj.setUpdateDate(now);
    	toolContentObj.setMcContentId(toolContentId);
    	toolContentObj.setQuestionsSequenced(false);
    	// I can't find a use for setShowReport anywhere
    	toolContentObj.setShowReport(false);
    	
    	
		try {
			Boolean bool = WDDXProcessor.convertToBoolean(importValues, ToolContentImport102Manager.CONTENT_Q_ALLOW_REDO);
	    	toolContentObj.setRetries(bool!=null?bool:false);

	    	bool = WDDXProcessor.convertToBoolean(importValues, ToolContentImport102Manager.CONTENT_Q_FEEDBACK);
	    	//toolContentObj.setShowFeedback(bool!=null?bool:false);

	    	Integer minPassMark = WDDXProcessor.convertToInteger(importValues, ToolContentImport102Manager.CONTENT_Q_MIN_PASSMARK);
	    	toolContentObj.setPassMark(minPassMark != null ? minPassMark : new Integer(0));
		
	    	// leave as empty, no need to set them to anything.
	    	//setMcUploadedFiles(Set mcSessions);
	    	//setMcSessions(Set mcSessions);
	    	
			mcContentDAO.saveMcContent(toolContentObj);

			// set up questions  	
	    	Vector questions = (Vector) importValues.get(CONTENT_Q_QUESTION_INFO);
	    	if ( questions != null ) {
	    		
	    		Iterator iter = questions.iterator();
	    		while (iter.hasNext()) {
	    			Hashtable questionMap = (Hashtable) iter.next();
	    			create102Question(questionMap, toolContentObj );
				}    	
	
	    	}
    	
		} catch (WDDXProcessorConversionException e) {
	   		logger.error("Unable to content for activity "+toolContentObj.getTitle()+"properly due to a WDDXProcessorConversionException.",e);
    		throw new ToolException("Invalid import data format for activity "+toolContentObj.getTitle()+"- WDDX caused an exception. Some data from the design will have been lost. See log for more details.");
 		}

		mcContentDAO.saveMcContent(toolContentObj);
    }


    private void create102Question( Hashtable questionMap, McContent toolContentObj) throws WDDXProcessorConversionException {
		McQueContent question = new McQueContent();
		question.setDisplayOrder( WDDXProcessor.convertToInteger(questionMap, ToolContentImport102Manager.CONTENT_Q_ORDER) );
		
		question.setFeedback((String)questionMap.get(CONTENT_Q_FEEDBACK));
		question.setQuestion(WebUtil.convertNewlines((String)questionMap.get(CONTENT_Q_QUESTION)));
		
		// In 1.0.2 all questions are implicitly assumed to be 1 and be of equal weight
		// I think the weights are actually ignored, so just set to 0 (FM) 
		question.setWeight( 0 );
		question.setMark( 1 );
		
		String correctAnswer = (String)questionMap.get(CONTENT_Q_ANSWER);

		Vector candidates = (Vector)questionMap.get(CONTENT_Q_CANDIDATES);
		if ( candidates != null ) {
			Iterator candIterator = candidates.iterator();
			while (candIterator.hasNext()) {
				Hashtable candidate = (Hashtable) candIterator.next();
				String optionText = (String)candidate.get(CONTENT_Q_ANSWER);
				// 1.0.2 has a display order but 2.0 doesn't ToolContentImport102Manager.CONTENT_Q_ORDER
				McOptsContent options = new McOptsContent();
				options.setCorrectOption(correctAnswer != null && correctAnswer.equals(optionText));
				options.setMcQueOptionText(optionText);
				options.setMcQueContent(question);
				question.getMcOptionsContents().add(options);
			}
		}
		
		toolContentObj.getMcQueContents().add(question);
		question.setMcContent(toolContentObj);
		question.setMcContentId(toolContentObj.getUid());
    }
    
    /** Set the description, throws away the title value as this is not supported in 2.0 */
    public void setReflectiveData(Long toolContentId, String title, String description) 
    		throws ToolException, DataMissingException {
    	
    	McContent toolContentObj = null;
    	if ( toolContentId != null ) {
    		toolContentObj=retrieveMc(toolContentId);
    	}
    	if ( toolContentObj == null ) {
    		throw new DataMissingException("Unable to set reflective data titled "+title
	       			+" on activity toolContentId "+toolContentId
	       			+" as the tool content does not exist.");
    	}
    	
    	toolContentObj.setReflect(Boolean.TRUE);
    	toolContentObj.setReflectionSubject(description);
    }
   

    
    public List retrieveMcUploadedFiles(McContent mc) throws McApplicationException {
        try {
            return mcUploadedFileDAO.retrieveMcUploadedFiles(mc);
        }
        catch (DataAccessException e) {
            throw new McApplicationException("Exception occured when lams is loading mc uploaded files: "
                                                       + e.getMessage(),
                                                         e);
        }
    }

    
    
    /**
     * adds a new entry to the uploaded files table
     */
    public void persistFile(McContent content, McUploadedFile file) throws McApplicationException {
    	logger.debug("in persistFile: " + file);
    	logger.debug("in persistFile, content: " + content);
    	
    	content.getMcAttachments().add(file);
        file.setMcContent(content);
        mcContentDAO.saveOrUpdateMc(content);
        logger.debug("persisted mcUploadedFile: " + file);
    }
    
    
    /**
     * removes an entry from the uploaded files table
     */
    public void removeFile(Long submissionId) throws McApplicationException {
        mcUploadedFileDAO.removeUploadFile(submissionId);
        logger.debug("removed mcUploadedFile: " + submissionId);
    }
    

	public Long createNotebookEntry(Long id, Integer idType, String signature,
			Integer userID, String entry) {
	    logger.debug("coreNotebookService: " + coreNotebookService);
		return coreNotebookService.createNotebookEntry(id, idType, signature, userID, "", entry);
	}
	
	
	

	public NotebookEntry getEntry(Long id, Integer idType, String signature,
			Integer userID) {
		
		List<NotebookEntry> list = coreNotebookService.getEntry(id, idType, signature, userID);
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

    
    /**
     * @return Returns the coreNotebookService.
     */
    public ICoreNotebookService getCoreNotebookService() {
        return coreNotebookService;
    }
    /**
     * @param coreNotebookService The coreNotebookService to set.
     */
    public void setCoreNotebookService(ICoreNotebookService coreNotebookService) {
        this.coreNotebookService = coreNotebookService;
    }
    
    /**
     * @return Returns the auditService.
     */
    public IAuditService getAuditService() {
        return auditService;
    }
    /**
     * @param auditService The auditService to set.
     */
    public void setAuditService(IAuditService auditService) {
        this.auditService = auditService;
    }

}