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

package org.lamsfoundation.lams.tool.vote.service;
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
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.contentrepository.WorkspaceNotFoundException;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.contentrepository.service.IRepositoryService;
import org.lamsfoundation.lams.contentrepository.service.RepositoryProxy;
import org.lamsfoundation.lams.contentrepository.service.SimpleCredentials;
import org.lamsfoundation.lams.learning.service.ILearnerService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolContentManager;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.ToolSessionManager;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.dao.IVoteContentDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteOptionsContentDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteQueContentDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteSessionDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUploadedFileDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUserDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUsrAttemptDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteOptsContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.springframework.dao.DataAccessException;

/**
 * 
 * @author Ozgur Demirtas
 * 
 * The POJO implementation of Mc service. All business logics of Voting tool
 * are implemented in this class. It translate the request from presentation
 * layer and perform appropriate database operation.
 * 
 */
public class VoteServicePOJO implements
                              IVoteService, ToolContentManager, ToolSessionManager, VoteAppConstants
               
{
	static Logger logger = Logger.getLogger(VoteServicePOJO.class.getName());
	
	/*repository access related constants */
	private final String repositoryUser 		= "vote11";
	private final char[] repositoryId 			= {'v','o','t','e','_','1', '1'}; 
	private final String repositoryWorkspace 	= "vote11";
	private IRepositoryService repositoryService;
	private ICredentials cred;
	
	private IVoteContentDAO			voteContentDAO; 
	private IVoteQueContentDAO		voteQueContentDAO; 
	private IVoteOptionsContentDAO 	voteOptionsContentDAO;	
	private IVoteSessionDAO			voteSessionDAO;
	private IVoteUserDAO			voteUserDAO;
	private IVoteUsrAttemptDAO		voteUsrAttemptDAO;
    
	private IVoteUploadedFileDAO  	voteUploadedFileDAO;
    private IUserManagementService 	userManagementService;
    private ILearnerService 		learnerService;
    private ILamsToolService 		toolService;
    private IToolContentHandler mcToolContentHandler = null;
    
    public VoteServicePOJO(){}
    
    public void configureContentRepository() throws VoteApplicationException {
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
  			throw new VoteApplicationException(error,e);
          }
      }
    
    
    public void createMc(VoteContent mcContent) throws VoteApplicationException
    {
        try
        {
        	logger.debug("using mcContent defineLater:" + mcContent.isDefineLater());
        	voteContentDAO.saveMcContent(mcContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is creating mc content: "
                                                         + e.getMessage(),
														   e);
        }
    }
	
    
    public VoteContent retrieveMc(Long toolContentId) throws VoteApplicationException
    {
        try
        {
            return voteContentDAO.findMcContentById(toolContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading mc content: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public void  updateMcContent(VoteContent mcContent) throws VoteApplicationException
    {
        try
        {
            voteContentDAO.updateMcContent(mcContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating mc content: "
                                                         + e.getMessage(),
														   e);
        }
    }

    public void createMcQue(VoteQueContent mcQueContent) throws VoteApplicationException
    {
        try
        {
        	voteQueContentDAO.saveMcQueContent(mcQueContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is creating mc que content: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public VoteQueContent getQuestionContentByDisplayOrder(final Long displayOrder, final Long mcContentUid) throws VoteApplicationException
	{
        try
        {
        	return voteQueContentDAO.getQuestionContentByDisplayOrder(displayOrder, mcContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting mc que content by display order: "
                                                         + e.getMessage(),
														   e);
        }    	
	}
    
    
    public VoteQueContent getMcQueContentByUID(Long uid) throws VoteApplicationException
    {
        try
        {
        	return voteQueContentDAO.getMcQueContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting mc que content by uid: "
                                                         + e.getMessage(),
														   e);
        }	
    }
    
    
    public void saveOrUpdateMcQueContent(VoteQueContent mcQueContent) throws VoteApplicationException
	{
    	try
        {
        	voteQueContentDAO.saveOrUpdateMcQueContent(mcQueContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating mc que content: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public void removeQuestionContentByMcUid(final Long mcContentUid) throws VoteApplicationException
	{
    	try
        {
        	voteQueContentDAO.removeQuestionContentByMcUid(mcContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing mc que content by mc content id: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    public void resetAllQuestions(final Long mcContentUid) throws VoteApplicationException
	{
    	try
        {
        	voteQueContentDAO.resetAllQuestions(mcContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is resetting all questions: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    
    public void cleanAllQuestions(final Long mcContentUid) throws VoteApplicationException
	{
    	try
        {
        	voteQueContentDAO.cleanAllQuestions(mcContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is cleaning all questions: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public List getNextAvailableDisplayOrder(final long mcContentId) throws VoteApplicationException
	{
    	try
        {
        	return voteQueContentDAO.getNextAvailableDisplayOrder(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting the next available display order: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
  
    public void createMcSession(VoteSession mcSession) throws VoteApplicationException
    {
        try
        {
        	voteSessionDAO.saveMcSession(mcSession);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is creating mc session: "
                                                         + e.getMessage(),
														   e);
        }
    }

    
    public VoteSession getMcSessionByUID(Long uid) throws VoteApplicationException
    {
        try
        {
        	return voteSessionDAO.getMcSessionByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting mcSession my uid: "
                                                         + e.getMessage(),
														   e);
        }
    }


    public void createMcQueUsr(VoteQueUsr mcQueUsr) throws VoteApplicationException
    {
	   try
        {
	   		voteUserDAO.saveMcUser(mcQueUsr);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is creating mc QueUsr: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public VoteQueUsr getMcUserBySession(final Long queUsrId, final Long mcSessionId) throws VoteApplicationException
	{
 	   try
       {
	   		return voteUserDAO.getMcUserBySession(queUsrId, mcSessionId);
       }
       catch (DataAccessException e)
       {
           throw new VoteApplicationException("Exception occured when lams is getting mc QueUsr: "
                                                        + e.getMessage(),
														   e);
       }    	
	}
    
    
    public VoteQueUsr getMcUserByUID(Long uid) throws VoteApplicationException
    {
	   try
        {
	   		return voteUserDAO.getMcUserByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting the mc QueUsr by uid."
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public VoteQueUsr retrieveMcQueUsr(Long userId) throws VoteApplicationException
    {
	   try
        {
	   		VoteQueUsr mcQueUsr=voteUserDAO.findMcUserById(userId);
	   		return mcQueUsr;
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving McQueUsr: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public void createMcUsrAttempt(VoteUsrAttempt mcUsrAttempt) throws VoteApplicationException
    {
        try
        {
        	voteUsrAttemptDAO.saveMcUsrAttempt(mcUsrAttempt);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is creating mc UsrAttempt: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public List getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long mcQueContentId) throws VoteApplicationException
	{
        try
        {
        	return voteUsrAttemptDAO.getAttemptsForUserAndQuestionContent(queUsrId, mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting mc UsrAttempt by user id and que content id: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
	public void updateMcUsrAttempt(VoteUsrAttempt mcUsrAttempt) throws VoteApplicationException
    {
        try
        {
        	voteUsrAttemptDAO.updateMcUsrAttempt(mcUsrAttempt);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating mc UsrAttempt: "
                                                         + e.getMessage(),
														   e);
        }
    }
	
   
	public List getAttemptsForUser(final Long queUsrId) throws VoteApplicationException
	{
        try
        {
        	return voteUsrAttemptDAO.getAttemptsForUser(queUsrId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting the attempts by user id: "
                                                         + e.getMessage(),
														   e);
        }
		
	}
	
	
	public List getAttemptForQueContent(final Long queUsrId, final Long mcQueContentId) throws VoteApplicationException
	{
        try
        {
        	return voteUsrAttemptDAO.getAttemptForQueContent(queUsrId, mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting the learner's attempts by user id and que content id: "
                                                         + e.getMessage(),
														   e);
        }
	}
	
    
    public VoteQueContent retrieveMcQueContentByUID(Long uid) throws VoteApplicationException
    {
        try
        {
            return voteQueContentDAO.getMcQueContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving by uid  mc question content: "
                                                         + e.getMessage(),
														   e);
        }
    }
   
    
    public void cleanAllQuestionsSimple(final Long mcContentId) throws VoteApplicationException
	{
    	try
        {
            voteQueContentDAO.cleanAllQuestionsSimple(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is cleaning mc question content by mcContentId : "
                                                         + e.getMessage(),
														   e);
        }    	
	}
    
    public List getAllQuestionEntries(final Long uid) throws VoteApplicationException
	{
	   try
        {
            return voteQueContentDAO.getAllQuestionEntries(uid.longValue());
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting by uid  mc question content: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public void removeMcQueContentByUID(Long uid) throws VoteApplicationException
	{
 	   try
       {
           voteQueContentDAO.removeMcQueContentByUID(uid);
       }
       catch (DataAccessException e)
       {
           throw new VoteApplicationException("Exception occured when lams is removing by uid  mc question content: "
                                                        + e.getMessage(),
														   e);
       }
	}
   

    public List refreshQuestionContent(final Long mcContentId) throws VoteApplicationException
	{
        try
        {
            return voteQueContentDAO.refreshQuestionContent(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is refreshing  mc question content: "
                                                         + e.getMessage(),
														   e);
        }
    	
	}
    
    public void removeMcQueContent(VoteQueContent mcQueContent) throws VoteApplicationException
	{
    	try
        {
            voteQueContentDAO.removeMcQueContent(mcQueContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing mc question content: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    public void removeMcOptionsContent(VoteOptsContent mcOptsContent) throws VoteApplicationException
    {
    	try
        {
            voteOptionsContentDAO.removeMcOptionsContent(mcOptsContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
    }

    public List getPersistedSelectedOptions(Long mcQueContentId) throws VoteApplicationException
	{
    	try
        {
            return voteOptionsContentDAO.getPersistedSelectedOptions(mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is gettong persisted selected"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
    	
	}
    
    
    
    public VoteQueContent getQuestionContentByQuestionText(final String question, final Long mcContentId)
    {
        try
        {
            return voteQueContentDAO.getQuestionContentByQuestionText(question, mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving question content by question text: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public VoteSession retrieveMcSession(Long mcSessionId) throws VoteApplicationException
    {
    	try
        {
            return voteSessionDAO.findMcSessionById(mcSessionId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving by id mc session : "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public VoteSession findMcSessionById(Long mcSessionId) throws VoteApplicationException
	{
    	try
        {
            return voteSessionDAO.findMcSessionById(mcSessionId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving by id mc session : "
                                                         + e.getMessage(),
														   e);
        }
    	
	}
   
    public List getMcUserBySessionOnly(final VoteSession mcSession) throws VoteApplicationException
    {
     	try
        {
            return voteUserDAO.getMcUserBySessionOnly(mcSession);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving users by session: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public VoteContent retrieveMcBySessionId(Long mcSessionId) throws VoteApplicationException
    {
        try
        {
        	return voteContentDAO.getMcContentBySession(mcSessionId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving mc by session id: "
            								+ e.getMessage(),
                                              e);
        }
    }
   
    public List getSessionNamesFromContent(VoteContent mcContent) throws VoteApplicationException
    {
        try
        {
        	return voteSessionDAO.getSessionNamesFromContent(mcContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting session names from content: "
                                                         + e.getMessage(),
														   e);
        }
    }

    
    
    public void updateMc(VoteContent mc) throws VoteApplicationException
    {
        try
        {
            voteContentDAO.updateMcContent(mc);
        }
        catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating"
                                                 + " the mc content: "
                                                 + e.getMessage(),e);
        }
    }

    
    public void updateMcSession(VoteSession mcSession) throws VoteApplicationException
    {
    	try
        {
            voteSessionDAO.updateMcSession(mcSession);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating mc session : "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public void deleteMc(VoteContent mc) throws VoteApplicationException
    {
    	try
        {
            voteContentDAO.removeMc(mc);
        }
        catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing"
                                                 + " the mc content: "
                                                 + e.getMessage(),e);
        }
    }
    
    public void deleteMcById(Long mcId) throws VoteApplicationException
    {
    	try
        {
            voteContentDAO.removeMcById(mcId);
        }
        catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing by id"
                                                 + " the mc content: "
                                                 + e.getMessage(),e);
        }
    }

	public int countSessionComplete() throws VoteApplicationException
	{
		try
        {
			return voteSessionDAO.countSessionComplete();
        }
		catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is counting incomplete sessions"
                                                 + e.getMessage(),e);
        }		
	}

    public int countSessionIncomplete() throws VoteApplicationException
	{
		try
        {
			return voteSessionDAO.countSessionIncomplete();
        }
		catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is counting incomplete sessions"
                                                 + e.getMessage(),e);
        }    	
	}
    
    public void deleteMcSession(VoteSession mcSession) throws VoteApplicationException 
	{
		try
        {
			voteSessionDAO.removeMcSession(mcSession);
        }
		catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is deleting"
                                                 + " the mc session: "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public void removeAttempt (VoteUsrAttempt attempt) throws VoteApplicationException
	{
    	try
        {
    		voteUsrAttemptDAO.removeMcUsrAttempt(attempt);
        }
        catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing"
                                                 + " the attempt: "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public void deleteMcQueUsr(VoteQueUsr mcQueUsr) throws VoteApplicationException
    {
    	try
        {
    		voteUserDAO.removeMcUser(mcQueUsr);
        }
        catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing"
                                                 + " the user: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    public void saveMcContent(VoteContent mc) throws VoteApplicationException
    {
        try
        {
            voteContentDAO.saveMcContent(mc);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is saving"
                                                 + " the mc content: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    public List findMcOptionsContentByQueId(Long mcQueContentId) throws VoteApplicationException
    {
    	try
        {
            List list=voteOptionsContentDAO.findMcOptionsContentByQueId(mcQueContentId);
            return list;
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is finding by que id"
                                                 + " the mc options: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    public VoteOptsContent getMcOptionsContentByUID(Long uid) throws VoteApplicationException
	{
    	try
        {
            return getMcOptionsContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting opt content by uid"
                                                 + e.getMessage(),e);
        }
	}
    
    
    public void saveMcOptionsContent(VoteOptsContent mcOptsContent) throws VoteApplicationException
	{
    	try
        {
            voteOptionsContentDAO.saveMcOptionsContent(mcOptsContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is saving"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
	}
    
    public VoteOptsContent getOptionContentByOptionText(final String option, final Long mcQueContentUid)
    {
    	try
        {
            return voteOptionsContentDAO.getOptionContentByOptionText(option, mcQueContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is returning the"
                                                 + " option by option text: "
                                                 + e.getMessage(),e);
        }
    }
    

   public List getCorrectOption(Long mcQueContentId)
   {
		try
	    {
	        return voteOptionsContentDAO.getCorrectOption(mcQueContentId);
	    }
	    catch (DataAccessException e)
	    {
	        throw new VoteApplicationException("Exception occured when lams is returning the "
	                                             + " correct option: "
	                                             + e.getMessage(),e);
	    }
   }
    
    public void updateMcOptionsContent(VoteOptsContent mcOptsContent) throws VoteApplicationException
	{
    	try
        {
            voteOptionsContentDAO.updateMcOptionsContent(mcOptsContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
	}

    public List getSessionsFromContent(VoteContent mcContent) throws VoteApplicationException
	{
    	try
        {
            return voteSessionDAO.getSessionsFromContent(mcContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting"
                                                 + " the mc sessions list: "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public void deleteMcOptionsContent(VoteOptsContent mcOptsContent) throws VoteApplicationException
	{
    	try
        {
            voteOptionsContentDAO.removeMcOptionsContent(mcOptsContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public List findMcOptionNamesByQueId(Long mcQueContentId) throws VoteApplicationException
	{
    	try
        {
            return voteOptionsContentDAO.findMcOptionNamesByQueId(mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is finding"
                                                 + " the mc options name: "
                                                 + e.getMessage(),e);
        }
    	
	}
    
    
    public void removeMcOptionsContentByQueId(Long mcQueContentId) throws VoteApplicationException
    {
    	try
        {
            voteOptionsContentDAO.removeMcOptionsContentByQueId(mcQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing by que id"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    public void deleteMcOptionsContentByUID(Long uid) throws VoteApplicationException
	{
    	try
        {
            voteOptionsContentDAO.removeMcOptionsContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing by uid"
                                                 + " the mc options content: "
                                                 + e.getMessage(),e);
        }
	}

    
    public int getTotalNumberOfUsers() throws VoteApplicationException
	{
 	   try
       {
	   		return voteUserDAO.getTotalNumberOfUsers();
       }
       catch (DataAccessException e)
       {
           throw new VoteApplicationException("Exception occured when lams is retrieving total number of McQueUsr: "
                                                        + e.getMessage(),
														   e);
       }
	}
    
    public User getCurrentUserData(String username) throws VoteApplicationException
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
        		throw new VoteApplicationException("No user with that username exists.");
        	}
        	return user;	 
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Unable to find current user information"
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
     * @throws VoteApplicationException
     */
    public Lesson getCurrentLesson(long lessonId) throws VoteApplicationException
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
            throw new VoteApplicationException("Exception occured when lams is loading"
                                                 + " learning session:"
                                                 + e.getMessage(),
                                                 e);
        }
    }

    
	/**
	 * checks the parameter content in the user responses table 
	 * @param mcContent
	 * @return boolean
	 * @throws VoteApplicationException
	 */
	public boolean studentActivityOccurredGlobal(VoteContent mcContent) throws VoteApplicationException
	{
		Iterator questionIterator=mcContent.getVoteQueContents().iterator();
        while (questionIterator.hasNext())
        {
        	VoteQueContent mcQueContent=(VoteQueContent)questionIterator.next(); 
        	Iterator attemptsIterator=mcQueContent.getVoteUsrAttempts().iterator();
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
	

	public int countIncompleteSession(VoteContent mc) throws VoteApplicationException
	{
		//int countIncompleteSession=voteSessionDAO.countIncompleteSession(mc);
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
	 * @throws VoteApplicationException
	 */
	public boolean studentActivityOccurred(VoteContent mc) throws VoteApplicationException
	{
		//int countStudentActivity=voteSessionDAO.studentActivityOccurred(mc);
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
            VoteContent fromContent = voteContentDAO.findMcContentById(fromContentId);
        
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
    			
    			fromContent = voteContentDAO.findMcContentById(fromContentId);
    			logger.debug("using fromContent: " + fromContent);
            }
            
            logger.debug("final - retrieved fromContent: " + fromContent);
            logger.debug("final - before new instance using " + fromContent + " and " + toContentId);
            logger.debug("final - before new instance using mcToolContentHandler: " + mcToolContentHandler);
            
            try
			{
            	VoteContent toContent = VoteContent.newInstance(mcToolContentHandler, fromContent,toContentId);
                if (toContent == null)
                {
                	logger.debug("throwing ToolException: WARNING!, retrieved toContent is null.");
                	throw new ToolException("WARNING! Fail to create toContent. Can't continue!");
                }
                else
                {
                	logger.debug("retrieved toContent: " + toContent);
    	            voteContentDAO.saveMcContent(toContent);
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
    	
    	VoteContent mcContent = voteContentDAO.findMcContentById(toolContentId);
    	logger.debug("retrieving mcContent: " + mcContent);
    	
    	if (mcContent != null)
    	{
            logger.error("start deleting any uploaded file for this content from the content repository");
        	Iterator filesIterator=mcContent.getVoteAttachments().iterator();
        	while (filesIterator.hasNext())
        	{
        		VoteUploadedFile mcUploadedFile=(VoteUploadedFile) filesIterator.next();
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
    		
    		Iterator sessionIterator=mcContent.getVoteSessions().iterator();
            while (sessionIterator.hasNext())
            {
            	if (removeSessionData == false)
            	{
            		logger.debug("removeSessionData is false, throwing SessionDataExistsException.");
            		throw new SessionDataExistsException();	
            	}
            	
            	VoteSession mcSession=(VoteSession)sessionIterator.next(); 
            	logger.debug("iterated mcSession : " + mcSession);
            	
            	Iterator sessionUsersIterator=mcSession.getVoteQueUsers().iterator();
            	while (sessionUsersIterator.hasNext())
            	{
            		VoteQueUsr mcQueUsr=(VoteQueUsr) sessionUsersIterator.next();
            		logger.debug("iterated mcQueUsr : " + mcQueUsr);
            		
            		Iterator sessionUsersAttemptsIterator=mcQueUsr.getVoteUsrAttempts().iterator();
            		while (sessionUsersAttemptsIterator.hasNext())
                	{
            			VoteUsrAttempt mcUsrAttempt=(VoteUsrAttempt)sessionUsersAttemptsIterator.next();
            			logger.debug("iterated mcUsrAttempt : " + mcUsrAttempt);
            			removeAttempt(mcUsrAttempt);
            			logger.debug("removed mcUsrAttempt : " + mcUsrAttempt);
                	}
            	}
            }
            logger.debug("removed all existing responses of toolContent with toolContentId:" + 
            																toolContentId);   
            voteContentDAO.removeMcById(toolContentId);        
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
    public void setAsForceCompleteSession(Long toolSessionId) throws VoteApplicationException
    {
    	VoteSession mcSession=retrieveMcSession(toolSessionId);	
    	mcSession.setSessionStatus(VoteSession.COMPLETED);
    	updateMcSession(mcSession);
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
    	
    	VoteContent mcContent=retrieveMc(toolContentId);
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
    	VoteContent mcContent = voteContentDAO.findMcContentById(toolContentId);
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
	 * it is possible that the tool session id already exists in the tool sessions table
	 * as the users from the same session are involved.
	 * existsSession(long toolSessionId)
	 * @param toolSessionId
	 * @return boolean
	 */
	public boolean existsSession(Long toolSessionId) 
	{
		VoteSession mcSession= retrieveMcSession(toolSessionId);
    	
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
    	
        VoteContent mcContent = voteContentDAO.findMcContentById(toolContentId);
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

			mcContent = voteContentDAO.findMcContentById(toolContentId);
        }
        logger.debug("final - retrieved mcContent: " + mcContent);

            
        /*
         * create a new a new tool session if it does not already exist in the tool session table
         */
        if (!existsSession(toolSessionId))
        {
        	try
			{
        		VoteSession mcSession = new VoteSession(toolSessionId,
                        new Date(System.currentTimeMillis()),
                        VoteSession.INCOMPLETE,
                        toolSessionName,
                        mcContent,
                        new TreeSet());

    		    logger.debug("created mcSession: " + mcSession);
    		    voteSessionDAO.saveMcSession(mcSession);
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
    	
    	
    	VoteSession mcSession=null;
    	try
		{
    		mcSession=retrieveMcSession(toolSessionId);
    		logger.debug("retrieved mcSession: " + mcSession);
		}
    	catch(VoteApplicationException e)
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
    		voteSessionDAO.removeMcSession(mcSession);
        	logger.debug("mcSession " + mcSession + " has been deleted successfully.");	
		}
    	catch(VoteApplicationException e)
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
        
        VoteSession mcSession=null;
    	try
		{
    		mcSession=retrieveMcSession(toolSessionId);
    		logger.debug("retrieved mcSession: " + mcSession);
		}
    	catch(VoteApplicationException e)
		{
    		throw new DataMissingException("error retrieving mcSession: " + e);
		}
    	catch(Exception e)
		{
    		throw new ToolException("error retrieving mcSession: " + e);
		}
    	mcSession.setSessionStatus(COMPLETED);
    	voteSessionDAO.updateMcSession(mcSession);
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
    
    public IToolVO getToolBySignature(String toolSignature) throws VoteApplicationException
    {
    	logger.debug("attempt retrieving tool with signature : " + toolSignature);
    	IToolVO tool=toolService.getToolBySignature(toolSignature);
    	logger.debug("retrieved tool: " + tool);
	    return tool;
    }
    
    public long getToolDefaultContentIdBySignature(String toolSignature) throws VoteApplicationException
    {
    	long contentId=0;
    	contentId=toolService.getToolDefaultContentIdBySignature(toolSignature);
    	logger.debug("tool default contentId : " + contentId);
	    return contentId;
    }

    public VoteQueContent getToolDefaultQuestionContent(long contentId) throws VoteApplicationException
    {
    	VoteQueContent mcQueContent=voteQueContentDAO.getToolDefaultQuestionContent(contentId);
    	logger.debug("retrieved mcQueContent : " + mcQueContent);
    	return mcQueContent; 
    }

    
    public List getToolSessionsForContent(VoteContent mc)
    {
    	logger.debug("attempt retrieving listToolSessionIds for : " + mc);
    	List listToolSessionIds=voteSessionDAO.getSessionsFromContent(mc);
    	return listToolSessionIds;
    }
    

    public void removeAttachment(VoteContent content, VoteUploadedFile attachment) throws RepositoryCheckedException
	{
	    try
	    {
			attachment.setVoteContent(null);
			content.getVoteAttachments().remove(attachment);
			mcToolContentHandler.deleteFile(new Long(attachment.getUuid()));
			saveMcContent(content);
	    }
	    catch (DataAccessException e)
	    {
	        throw new VoteApplicationException("EXCEPTION: An exception has occurred while trying to remove this attachment"
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
	public ITicket getRepositoryLoginTicket() throws VoteApplicationException {
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
			throw new VoteApplicationException("Access Denied to repository."
					+ e.getMessage());
		} catch (WorkspaceNotFoundException e) {
			throw new VoteApplicationException("Workspace not found."
					+ e.getMessage());
		} catch (LoginException e) {
			throw new VoteApplicationException("Login failed." + e.getMessage());
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
			throws VoteApplicationException {
		ITicket ticket = getRepositoryLoginTicket();
		logger.debug("retrieved ticket: " + ticket);
		try {
			String files[] = repositoryService.deleteVersion(ticket, uuid,versionID);
			logger.debug("retrieved files: " + files);
		} catch (Exception e) {
			throw new VoteApplicationException(
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
	public NodeKey uploadFileToRepository(InputStream stream, String fileName) throws VoteApplicationException {
		logger.debug("attempt getting the ticket");
		ITicket ticket = getRepositoryLoginTicket();
		logger.debug("retrieved ticket: " + ticket);
		
		try {
			NodeKey nodeKey = repositoryService.addFileItem(ticket, stream,
					fileName, null, null);
			logger.debug("retrieved nodeKey from repository service: " + nodeKey);
			return nodeKey;
		} catch (Exception e) {
			throw new VoteApplicationException("Exception occured while trying to"
					+ " upload file into the repository" + e.getMessage());
		}
	}

	public InputStream downloadFile(Long uuid, Long versionID)throws VoteApplicationException{
		ITicket ticket = getRepositoryLoginTicket();		
		try{
			IVersionedNode node = repositoryService.getFileItem(ticket,uuid,null);
			logger.debug("retrieved node: " + node);
			return node.getFile();
		}catch(AccessDeniedException e){
			throw new VoteApplicationException("AccessDeniedException occured while trying to download file " + e.getMessage());
		}catch(FileException e){
			throw new VoteApplicationException("FileException occured while trying to download file " + e.getMessage());
		}catch(ItemNotFoundException e){
			throw new VoteApplicationException("ItemNotFoundException occured while trying to download file " + e.getMessage());			
		}
	}
	
	
	public void removeOffLineFile(String filename, Long mcContentId) throws VoteApplicationException
	{
	    try
        {
            voteUploadedFileDAO.removeOffLineFile(filename, mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing offline filename"
                                                         + e.getMessage(),
														   e);
        }
	}
    
    public void removeOnLineFile(String filename, Long mcContentId) throws VoteApplicationException
	{
	    try
        {
            voteUploadedFileDAO.removeOnLineFile(filename, mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing online filename"
                                                         + e.getMessage(),
														   e);
        }
	}
	
	public boolean isOffLineFilePersisted(String filename) throws VoteApplicationException
	{
	    try
        {
            return voteUploadedFileDAO.isOffLineFilePersisted(filename);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is checking if offline filename is persisted: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    public boolean isOnLineFilePersisted(String filename) throws VoteApplicationException
	{
        try
        {
            return voteUploadedFileDAO.isOnLineFilePersisted(filename);
        }
        catch (DataAccessException e)
        {
        	throw new VoteApplicationException("Exception occured when lams is checking if online filename is persisted: "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	public String getFileUuid(String filename) throws VoteApplicationException
	{
	    try
        {
            return voteUploadedFileDAO.getFileUuid(filename);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading uuid by filename: "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	public List getOnlineFilesMetaData(Long mcContentId) throws VoteApplicationException
	{
		return voteUploadedFileDAO.getOnlineFilesMetaData(mcContentId);
	}
    
	
    public List getOfflineFilesMetaData(Long mcContentId) throws VoteApplicationException
	{
    	return voteUploadedFileDAO.getOfflineFilesMetaData(mcContentId);
	}
	
	public boolean isUuidPersisted(String uuid) throws VoteApplicationException
	{
		return voteUploadedFileDAO.isUuidPersisted(uuid);
	}
	
	/**
	 * adds a new entry to the uploaded files table
	 */
	public void persistFile(String uuid, boolean isOnlineFile, String fileName, VoteContent mcContent) throws VoteApplicationException {
		
		logger.debug("attempt persisting file to the db: " + uuid + " " + isOnlineFile + " " + fileName + " " + mcContent);
		VoteUploadedFile mcUploadedFile= new VoteUploadedFile(uuid, isOnlineFile, fileName, mcContent);
		logger.debug("created mcUploadedFile: " + mcUploadedFile);
		voteUploadedFileDAO.saveUploadFile(mcUploadedFile);
		logger.debug("persisted mcUploadedFile: " + mcUploadedFile);
	}

	/**
	 * 
	 * removes all the entries in the uploaded files table
	 */
	public void cleanUploadedFilesMetaData() throws VoteApplicationException {
		logger.debug("attempt cleaning up uploaded file meta data table from the db");
		voteUploadedFileDAO.cleanUploadedFilesMetaData();
		logger.debug("files meta data has been cleaned up");
	}
	
	
	public List retrieveMcUploadedFiles(Long mcContentId, boolean fileOnline) throws VoteApplicationException {
        try
        {
            return voteUploadedFileDAO.retrieveMcUploadedFiles(mcContentId, fileOnline);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading mc uploaded files: "
                                                         + e.getMessage(),
														   e);
        }
	}

	public List retrieveMcUploadedOfflineFilesUuid(Long mcContentId) throws VoteApplicationException {
		try
        {
            return voteUploadedFileDAO.retrieveMcUploadedOfflineFilesUuid(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading mc uploaded files: offline + uuids "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	public List retrieveMcUploadedOnlineFilesUuid(Long mcContentId) throws VoteApplicationException {
		try
        {
            return voteUploadedFileDAO.retrieveMcUploadedOnlineFilesUuid(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading mc uploaded files: online + uuids "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	public List retrieveMcUploadedOfflineFilesName(Long mcContentId) throws VoteApplicationException {
		try
        {
            return voteUploadedFileDAO.retrieveMcUploadedOfflineFilesName(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading mc uploaded files: offline + fileNames "
                                                         + e.getMessage(),
														   e);
        }
	}

	
	public List retrieveMcUploadedOnlineFilesName(Long mcContentId) throws VoteApplicationException {
    	try
        {
            return voteUploadedFileDAO.retrieveMcUploadedOnlineFilesName(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading mc uploaded files: online + fileNames "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	/*
	public List retrieveMcUploadedOfflineFilesUuidPlusFilename(Long mcContentId) throws VoteApplicationException {
		try
        {
            return voteUploadedFileDAO.retrieveMcUploadedOfflineFilesUuidPlusFilename(mcContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading mc uploaded offline file uuid plus filename:  "
                                                         + e.getMessage(),
														   e);
        }
	}
	*/
	
	
	/**
	 * @return Returns the logger.
	 */
	public static Logger getLogger() {
		return logger;
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
	public IQaUploadedFileDAO getQaUploadedFileDAO() {
		return qaUploadedFileDAO;
	}
	
	public void setQaUploadedFileDAO(IQaUploadedFileDAO qaUploadedFileDAO) {
		this.qaUploadedFileDAO = qaUploadedFileDAO;
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
	 * @return Returns the voteSessionDAO.
	 */
	public IVoteSessionDAO getvoteSessionDAO() {
		return voteSessionDAO;
	}
	/**
	 * @param voteSessionDAO The voteSessionDAO to set.
	 */
	public void setvoteSessionDAO(IVoteSessionDAO voteSessionDAO) {
		this.voteSessionDAO = voteSessionDAO;
	}
	/**
	 * @return Returns the voteUserDAO.
	 */
	public IVoteUserDAO getvoteUserDAO() {
		return voteUserDAO;
	}
	/**
	 * @param voteUserDAO The voteUserDAO to set.
	 */
	public void setvoteUserDAO(IVoteUserDAO voteUserDAO) {
		this.voteUserDAO = voteUserDAO;
	}
	/**
	 * @return Returns the voteUsrAttemptDAO.
	 */
	public IVoteUsrAttemptDAO getvoteUsrAttemptDAO() {
		return voteUsrAttemptDAO;
	}
	/**
	 * @param voteUsrAttemptDAO The voteUsrAttemptDAO to set.
	 */
	public void setvoteUsrAttemptDAO(IVoteUsrAttemptDAO voteUsrAttemptDAO) {
		this.voteUsrAttemptDAO = voteUsrAttemptDAO;
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
	 * @return Returns the voteUploadedFileDAO.
	 */
	public IVoteUploadedFileDAO getvoteUploadedFileDAO() {
		return voteUploadedFileDAO;
	}
	/**
	 * @param voteUploadedFileDAO The voteUploadedFileDAO to set.
	 */
	public void setvoteUploadedFileDAO(IVoteUploadedFileDAO voteUploadedFileDAO) {
		this.voteUploadedFileDAO = voteUploadedFileDAO;
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
    /**
     * @return Returns the voteContentDAO.
     */
    public IVoteContentDAO getvoteContentDAO() {
        return voteContentDAO;
    }
    /**
     * @param voteContentDAO The voteContentDAO to set.
     */
    public void setvoteContentDAO(IVoteContentDAO voteContentDAO) {
        this.voteContentDAO = voteContentDAO;
    }

    /**
     * @return Returns the voteQueContentDAO.
     */
    public IVoteQueContentDAO getvoteQueContentDAO() {
        return voteQueContentDAO;
    }
    /**
     * @param voteQueContentDAO The voteQueContentDAO to set.
     */
    public void setvoteQueContentDAO(IVoteQueContentDAO voteQueContentDAO) {
        this.voteQueContentDAO = voteQueContentDAO;
    }


    /**
     * @return Returns the voteContentDAO.
     */
    public IVoteContentDAO getVoteContentDAO() {
        return voteContentDAO;
    }
    /**
     * @param voteContentDAO The voteContentDAO to set.
     */
    public void setVoteContentDAO(IVoteContentDAO voteContentDAO) {
        this.voteContentDAO = voteContentDAO;
    }
    /**
     * @return Returns the voteQueContentDAO.
     */
    public IVoteQueContentDAO getVoteQueContentDAO() {
        return voteQueContentDAO;
    }
    /**
     * @param voteQueContentDAO The voteQueContentDAO to set.
     */
    public void setVoteQueContentDAO(IVoteQueContentDAO voteQueContentDAO) {
        this.voteQueContentDAO = voteQueContentDAO;
    }
    /**
     * @return Returns the voteSessionDAO.
     */
    public IVoteSessionDAO getVoteSessionDAO() {
        return voteSessionDAO;
    }
    /**
     * @param voteSessionDAO The voteSessionDAO to set.
     */
    public void setVoteSessionDAO(IVoteSessionDAO voteSessionDAO) {
        this.voteSessionDAO = voteSessionDAO;
    }
    /**
     * @return Returns the voteUploadedFileDAO.
     */
    public IVoteUploadedFileDAO getVoteUploadedFileDAO() {
        return voteUploadedFileDAO;
    }
    /**
     * @param voteUploadedFileDAO The voteUploadedFileDAO to set.
     */
    public void setVoteUploadedFileDAO(IVoteUploadedFileDAO voteUploadedFileDAO) {
        this.voteUploadedFileDAO = voteUploadedFileDAO;
    }
    /**
     * @return Returns the voteUserDAO.
     */
    public IVoteUserDAO getVoteUserDAO() {
        return voteUserDAO;
    }
    /**
     * @param voteUserDAO The voteUserDAO to set.
     */
    public void setVoteUserDAO(IVoteUserDAO voteUserDAO) {
        this.voteUserDAO = voteUserDAO;
    }
    /**
     * @return Returns the voteUsrAttemptDAO.
     */
    public IVoteUsrAttemptDAO getVoteUsrAttemptDAO() {
        return voteUsrAttemptDAO;
    }
    /**
     * @param voteUsrAttemptDAO The voteUsrAttemptDAO to set.
     */
    public void setVoteUsrAttemptDAO(IVoteUsrAttemptDAO voteUsrAttemptDAO) {
        this.voteUsrAttemptDAO = voteUsrAttemptDAO;
    }
    /**
     * @param logger The logger to set.
     */
    public static void setLogger(Logger logger) {
        VoteServicePOJO.logger = logger;
    }
    
    /**
     * @return Returns the voteOptionsContentDAO.
     */
    public IVoteOptionsContentDAO getVoteOptionsContentDAO() {
        return voteOptionsContentDAO;
    }
    /**
     * @param voteOptionsContentDAO The voteOptionsContentDAO to set.
     */
    public void setVoteOptionsContentDAO(
            IVoteOptionsContentDAO voteOptionsContentDAO) {
        this.voteOptionsContentDAO = voteOptionsContentDAO;
    }
}
