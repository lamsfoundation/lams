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

package org.lamsfoundation.lams.tool.vote.service;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.dao.IVoteContentDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteQueContentDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteSessionDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUploadedFileDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUserDAO;
import org.lamsfoundation.lams.tool.vote.dao.IVoteUsrAttemptDAO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.audit.IAuditService;
import org.springframework.dao.DataAccessException;

/**
 * 
 * @author Ozgur Demirtas
 * 
 * The POJO implementation of Voting service. All business logic of Voting tool
 * is implemented in this class. It translates the request from presentation
 * layer and performs appropriate database operation.
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
	private IVoteSessionDAO			voteSessionDAO;
	private IVoteUserDAO			voteUserDAO;
	private IVoteUsrAttemptDAO		voteUsrAttemptDAO;
    
	private IVoteUploadedFileDAO  	voteUploadedFileDAO;
    private IUserManagementService 	userManagementService;
    private ILearnerService 		learnerService;
    private IAuditService 			auditService;
    private ILamsToolService 		toolService;
    private IToolContentHandler voteToolContentHandler = null;
    
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
    
    
    public void createVote(VoteContent voteContent) throws VoteApplicationException
    {
        try
        {
        	logger.debug("using voteContent defineLater:" + voteContent.isDefineLater());
        	voteContentDAO.saveVoteContent(voteContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is creating vote content: "
                                                         + e.getMessage(),
														   e);
        }
    }
	
    
    public VoteContent retrieveVote(Long toolContentId) throws VoteApplicationException
    {
        try
        {
            return voteContentDAO.findVoteContentById(toolContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading vote content: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public void  updateVoteContent(VoteContent voteContent) throws VoteApplicationException
    {
        try
        {
            voteContentDAO.updateVoteContent(voteContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating vote content: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public VoteQueContent getQuestionContentByDisplayOrder(final Long displayOrder, final Long voteContentUid) throws VoteApplicationException
	{
        try
        {
        	return voteQueContentDAO.getQuestionContentByDisplayOrder(displayOrder, voteContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting vote que content by display order: "
                                                         + e.getMessage(),
														   e);
        }    	
	}
    
    
    public VoteQueUsr getVoteQueUsrById(long voteQueUsrId) throws VoteApplicationException
	{
 	   try
       {
	   		VoteQueUsr voteQueUsr=voteUserDAO.getVoteQueUsrById(voteQueUsrId);
	   		return voteQueUsr;
       }
       catch (DataAccessException e)
       {
           throw new VoteApplicationException("Exception occured when lams is getting vote QueUsr: "
                                                        + e.getMessage(),
														   e);
       }
	}
    
    public List retrieveVoteQueContentsByToolContentId(long voteContentId) throws VoteApplicationException
	{
        try
        {
            return voteQueContentDAO.getVoteQueContentsByContentId(voteContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading vote que usr: "
                                                         + e.getMessage(),
                                                           e);
        }
    }

    public void createVoteQue(VoteQueContent voteQueContent) throws VoteApplicationException
    {
        try
        {
        	voteQueContentDAO.saveVoteQueContent(voteQueContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is creating vote que content: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public List getContentEntries(final Long voteContentUid) throws VoteApplicationException 
    {
        try
        {
        	return voteUsrAttemptDAO.getContentEntries(voteContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting content entries: "
                                                         + e.getMessage(),
														   e);
        }
    }

    
    public Set getUserEntries() throws VoteApplicationException 
    {
        try
        {
        	return voteUsrAttemptDAO.getUserEntries();
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting user entries: "
                                                         + e.getMessage(),
														   e);
        }
    }
   

    public int getCompletedSessionEntriesCount(final Long voteSessionUid) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getCompletedSessionEntriesCount(voteSessionUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting completed session user entries count: "
                                                         + e.getMessage(),
														   e);
        }        
    }
    
    
    public boolean isVoteVisibleForSession(final String userEntry, final Long voteSessionUid) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.isVoteVisibleForSession(userEntry, voteSessionUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is finding if the open vote is hidden: "
                                                         + e.getMessage(),
														   e);
        }        
    }
    
    public List getSessionUserEntries(final Long voteSessionUid) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getSessionUserEntries(voteSessionUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting session user entries: "
                                                         + e.getMessage(),
														   e);
        }        
    }
    
    
    public Set getSessionUserEntriesSet(final Long voteSessionUid) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getSessionUserEntriesSet(voteSessionUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting session user entries: "
                                                         + e.getMessage(),
														   e);
        }        
    }
    
    public VoteQueContent getVoteQueContentByUID(Long uid) throws VoteApplicationException
    {
        try
        {
        	return voteQueContentDAO.getVoteQueContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting vote que content by uid: "
                                                         + e.getMessage(),
														   e);
        }	
    }
    
    
    public void saveOrUpdateVoteQueContent(VoteQueContent voteQueContent) throws VoteApplicationException
	{
    	try
        {
        	voteQueContentDAO.saveOrUpdateVoteQueContent(voteQueContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating vote que content: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public List getUserBySessionOnly(final VoteSession voteSession) throws VoteApplicationException
    {
  	   try
       {
	   		return voteUserDAO.getUserBySessionOnly(voteSession);
       }
       catch (DataAccessException e)
       {
           throw new VoteApplicationException("Exception occured when lams is getting vote QueUsr by vote session "
                                                        + e.getMessage(),
														   e);
       }
    }
    
    
    public void removeQuestionContentByVoteUid(final Long voteContentUid) throws VoteApplicationException
	{
    	try
        {
        	voteQueContentDAO.removeQuestionContentByVoteUid(voteContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing vote que content by vote content id: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    public void resetAllQuestions(final Long voteContentUid) throws VoteApplicationException
	{
    	try
        {
        	voteQueContentDAO.resetAllQuestions(voteContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is resetting all questions: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    
    public void cleanAllQuestions(final Long voteContentUid) throws VoteApplicationException
	{
    	try
        {
        	voteQueContentDAO.cleanAllQuestions(voteContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is cleaning all questions: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public void createVoteSession(VoteSession voteSession) throws VoteApplicationException
    {
        try
        {
        	voteSessionDAO.saveVoteSession(voteSession);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is creating vote session: "
                                                         + e.getMessage(),
														   e);
        }
    }

    
    public VoteSession getVoteSessionByUID(Long uid) throws VoteApplicationException
    {
        try
        {
        	return voteSessionDAO.getVoteSessionByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting voteSession by uid: "
                                                         + e.getMessage(),
														   e);
        }
    }

    public int getVoteSessionPotentialLearnersCount(Long voteSessionId) throws VoteApplicationException
    {
        try
        {
        	Set potentialLearners = toolService.getAllPotentialLearners(voteSessionId.longValue());
        	return potentialLearners != null ? potentialLearners.size() : 0;
        }
        catch (LamsToolServiceException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting count of potential voteSession learners: "
                                                         + e.getMessage(),
														   e);
        }
    }

    public void createVoteQueUsr(VoteQueUsr voteQueUsr) throws VoteApplicationException
    {
	   try
        {
			auditService.logShowEntry(MY_SIGNATURE, voteQueUsr.getQueUsrId(), 
			        voteQueUsr.getUsername(), voteQueUsr.toString());

	   		voteUserDAO.saveVoteUser(voteQueUsr);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is creating vote QueUsr: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long voteSessionId) throws VoteApplicationException
	{
 	   try
       {
	   		return voteUserDAO.getVoteUserBySession(queUsrId, voteSessionId);
       }
       catch (DataAccessException e)
       {
           throw new VoteApplicationException("Exception occured when lams is getting vote QueUsr: "
                                                        + e.getMessage(),
														   e);
       }    	
	}
    
    
    public VoteQueUsr getVoteUserByUID(Long uid) throws VoteApplicationException
    {
	   try
        {
	   		return voteUserDAO.getVoteUserByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting the vote QueUsr by uid."
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public VoteQueUsr retrieveVoteQueUsr(Long userId) throws VoteApplicationException
    {
	   try
        {
	   		VoteQueUsr voteQueUsr=voteUserDAO.findVoteUserById(userId);
	   		return voteQueUsr;
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving VoteQueUsr: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public void createVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) throws VoteApplicationException
    {
        try
        {
			auditService.logShowEntry(MY_SIGNATURE, voteUsrAttempt.getQueUsrId(), 
			        voteUsrAttempt.getVoteQueUsr().getUsername(), voteUsrAttempt.toString());

        	voteUsrAttemptDAO.saveVoteUsrAttempt(voteUsrAttempt);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is creating vote UsrAttempt: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public void removeAttemptsForUser(final Long queUsrId) throws VoteApplicationException
    {
        try
        {
        	voteUsrAttemptDAO.removeAttemptsForUser(queUsrId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing voteUsrAttempts: "
                                                         + e.getMessage(),
														   e);
        }
    }

    public int getAllEntriesCount() throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getAllEntriesCount();
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting all attempts entries count: "
                                                         + e.getMessage(),
														   e);
        }        
    }
    
    
    public int getStandardAttemptsForQuestionContentAndContentUid(final Long voteQueContentId, final Long voteContentUid)
    {
        try
        {
        	return voteUsrAttemptDAO.getStandardAttemptsForQuestionContentAndContentUid(voteQueContentId, voteContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting all standard attempts entries count: "
                                                         + e.getMessage(),
														   e);
        }
        
    }
    
    public int getSessionEntriesCount(final Long voteSessionUid) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getSessionEntriesCount(voteSessionUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting all attempts session entries count: "
                                                         + e.getMessage(),
														   e);
        }        
    }

    
    public VoteUsrAttempt getAttemptByUID(Long uid) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getAttemptByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting attemptby uid: "
                                                         + e.getMessage(),
														   e);
        }        
    }
    
    
    public int getUserRecordsEntryCount(final String userEntry) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getUserRecordsEntryCount(userEntry);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting userrecords entry count: "
                                                         + e.getMessage(),
														   e);
        }
        
    }

    
    public int getSessionUserRecordsEntryCount(final String userEntry, final Long voteSessionUid, IVoteService voteService) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getSessionUserRecordsEntryCount(userEntry, voteSessionUid, voteService);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting session userrecords entry count: "
                                                         + e.getMessage(),
														   e);
        }
        
    }
    
    public int getAttemptsForQuestionContent(final Long voteQueContentId) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getAttemptsForQuestionContent(voteQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting vote UsrAttempt by question content id only: "
                                                         + e.getMessage(),
														   e);
        }        
    }
    
    public int getStandardAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId, final Long voteSessionUid) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getStandardAttemptsForQuestionContentAndSessionUid(voteQueContentId, voteSessionUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting vote UsrAttempt by question content id and session uid: "
                                                         + e.getMessage(),
														   e);
        }        
    }

    
    
    public VoteUsrAttempt getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId) throws VoteApplicationException
	{
        try
        {
        	return voteUsrAttemptDAO.getAttemptsForUserAndQuestionContent(queUsrId, voteQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting vote UsrAttempt by user id and que content id: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public List getUserRecords(final String userEntry) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getUserRecords(userEntry);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting user records for user entry: "
                                                         + e.getMessage(),
														   e);
        }
    }
    

    public List getAttemptsListForUserAndQuestionContent(final Long queUsrId, final Long voteQueContentId) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getAttemptsListForUserAndQuestionContent(queUsrId, voteQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting vote UsrAttempt by user id and que content id: "
                                                         + e.getMessage(),
														   e);
        }
        
    }

    public VoteUsrAttempt getAttemptsForUserAndQuestionContentAndSession(final Long queUsrId, final Long voteQueContentId, final Long toolSessionUid) throws VoteApplicationException
    {
        try
        {
        	return voteUsrAttemptDAO.getAttemptsForUserAndQuestionContentAndSession(queUsrId, voteQueContentId, toolSessionUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating vote UsrAttempt: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
	public void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) throws VoteApplicationException
    {
        try
        {
			auditService.logShowEntry(MY_SIGNATURE, voteUsrAttempt.getQueUsrId(), 
			        voteUsrAttempt.getVoteQueUsr().getUsername(), voteUsrAttempt.toString());

        	voteUsrAttemptDAO.updateVoteUsrAttempt(voteUsrAttempt);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating vote UsrAttempt: "
                                                         + e.getMessage(),
														   e);
        }
    }
	
	public void removeAttemptsForUserandSession(final Long queUsrId, final Long voteSessionId) throws VoteApplicationException
	{
    	try
        {
            voteUsrAttemptDAO.removeAttemptsForUserandSession(queUsrId, voteSessionId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing by user and votesession id : "
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
	
	public List getUserEnteredVotesForSession(final String userEntry, final Long voteSessionUid)
	{
        try
        {
        	return voteUsrAttemptDAO.getUserEnteredVotesForSession(userEntry, voteSessionUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting user entered votes for session: "
                                                         + e.getMessage(),
														   e);
        }	    
	    
	}
	
	
	public List getAttemptForQueContent(final Long queUsrId, final Long voteQueContentId) throws VoteApplicationException
	{
        try
        {
        	return voteUsrAttemptDAO.getAttemptForQueContent(queUsrId, voteQueContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting the learner's attempts by user id and que content id: "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	public int getUserEnteredVotesCountForContent(final Long voteContentUid) throws VoteApplicationException
	{
        try
        {
        	return voteUsrAttemptDAO.getUserEnteredVotesCountForContent(voteContentUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting the user entered votes count:"
                                                         + e.getMessage(),
														   e);
        }
	    
	}
	
    
    public VoteQueContent retrieveVoteQueContentByUID(Long uid) throws VoteApplicationException
    {
        try
        {
            return voteQueContentDAO.getVoteQueContentByUID(uid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving by uid  vote question content: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public void updateVoteQueContent(VoteQueContent voteQueContent) throws VoteApplicationException
	{
    	try
        {
        	voteQueContentDAO.updateVoteQueContent(voteQueContent);

        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating vote content by question: "
                                                         + e.getMessage(),
														   e);
        }
    	
	}

    public void cleanAllQuestionsSimple(final Long voteContentId) throws VoteApplicationException
	{
    	try
        {
            voteQueContentDAO.cleanAllQuestionsSimple(voteContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is cleaning vote question content by voteContentId : "
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
            throw new VoteApplicationException("Exception occured when lams is getting by uid  vote question content: "
                                                         + e.getMessage(),
														   e);
        }
	}
    
    
    public void removeVoteQueContentByUID(Long uid) throws VoteApplicationException
	{
 	   try
       {
           voteQueContentDAO.removeVoteQueContentByUID(uid);
       }
       catch (DataAccessException e)
       {
           throw new VoteApplicationException("Exception occured when lams is removing by uid  vote question content: "
                                                        + e.getMessage(),
														   e);
       }
	}
   

    public void removeVoteQueContent(VoteQueContent voteQueContent) throws VoteApplicationException
	{
    	try
        {
            voteQueContentDAO.removeVoteQueContent(voteQueContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing vote question content: "
                                                         + e.getMessage(),
														   e);
        }
	}
    

    public VoteQueContent getQuestionContentByQuestionText(final String question, final Long voteContentId)
    {
        try
        {
            return voteQueContentDAO.getQuestionContentByQuestionText(question, voteContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving question content by question text: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public VoteSession retrieveVoteSession(Long voteSessionId) throws VoteApplicationException
    {
    	try
        {
            return voteSessionDAO.findVoteSessionById(voteSessionId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving by id vote session : "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public VoteSession findVoteSessionById(Long voteSessionId) throws VoteApplicationException
	{
    	try
        {
            return voteSessionDAO.findVoteSessionById(voteSessionId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving by id vote session : "
                                                         + e.getMessage(),
														   e);
        }
    	
	}
    
    public int getCompletedVoteUserBySessionUid(final Long voteSessionUid) throws VoteApplicationException
    {
     	try
        {
            return voteUserDAO.getCompletedVoteUserBySessionUid(voteSessionUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving completed users by session uid: "
                                                         + e.getMessage(),
														   e);
        }
    }

	public List getVoteUserBySessionUid(final Long voteSessionUid) throws VoteApplicationException
	{
     	try
        {
            return voteUserDAO.getVoteUserBySessionUid(voteSessionUid);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving users by session uid: "
                                                         + e.getMessage(),
														   e);
        }	    
	}

    
    public List getVoteUserBySessionOnly(final VoteSession voteSession) throws VoteApplicationException
    {
     	try
        {
            return voteUserDAO.getVoteUserBySessionOnly(voteSession);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving users by session: "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    
    public VoteContent retrieveVoteBySessionId(Long voteSessionId) throws VoteApplicationException
    {
        try
        {
        	return voteContentDAO.getVoteContentBySession(voteSessionId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving vote by session id: "
            								+ e.getMessage(),
                                              e);
        }
    }
   
    public List getSessionNamesFromContent(VoteContent voteContent) throws VoteApplicationException
    {
        try
        {
        	return voteSessionDAO.getSessionNamesFromContent(voteContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting session names from content: "
                                                         + e.getMessage(),
														   e);
        }
    }

    
    
    public void updateVote(VoteContent vote) throws VoteApplicationException
    {
        try
        {
            voteContentDAO.updateVoteContent(vote);
        }
        catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating"
                                                 + " the vote content: "
                                                 + e.getMessage(),e);
        }
    }

    
    public void updateVoteSession(VoteSession voteSession) throws VoteApplicationException
    {
    	try
        {
            voteSessionDAO.updateVoteSession(voteSession);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is updating vote session : "
                                                         + e.getMessage(),
														   e);
        }
    }
    
    public void deleteVote(VoteContent vote) throws VoteApplicationException
    {
    	try
        {
            voteContentDAO.removeVote(vote);
        }
        catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing"
                                                 + " the vote content: "
                                                 + e.getMessage(),e);
        }
    }
    
    public void deleteVoteById(Long voteId) throws VoteApplicationException
    {
    	try
        {
            voteContentDAO.removeVoteById(voteId);
        }
        catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing by id"
                                                 + " the vote content: "
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
    
    public void deleteVoteSession(VoteSession voteSession) throws VoteApplicationException 
	{
		try
        {
			voteSessionDAO.removeVoteSession(voteSession);
        }
		catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is deleting"
                                                 + " the vote session: "
                                                 + e.getMessage(),e);
        }
	}
    
    
    public void removeAttempt (VoteUsrAttempt attempt) throws VoteApplicationException
	{
    	try
        {
			auditService.logShowEntry(MY_SIGNATURE, attempt.getQueUsrId(), 
			        attempt.getVoteQueUsr().getUsername(), attempt.toString());

    		voteUsrAttemptDAO.removeVoteUsrAttempt(attempt);
        }
        catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing"
                                                 + " the attempt: "
                                                 + e.getMessage(),e);
        }
	}
    
    public int  getLastNominationCount(Long userId) throws VoteApplicationException
    {
	   try
        {
	   		int lastNomCount=voteUsrAttemptDAO.getLastNominationCount(userId);
	   		return lastNomCount;
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is retrieving lastNominationount: "
                                                         + e.getMessage(),
														   e);
        }
    }
    

    
    public void deleteVoteQueUsr(VoteQueUsr voteQueUsr) throws VoteApplicationException
    {
    	try
        {
			auditService.logShowEntry(MY_SIGNATURE, voteQueUsr.getQueUsrId(), 
			        voteQueUsr.getUsername(), voteQueUsr.toString());
    	    
    		voteUserDAO.removeVoteUser(voteQueUsr);
        }
        catch(DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing"
                                                 + " the user: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    public void saveVoteContent(VoteContent vote) throws VoteApplicationException
    {
        try
        {
            voteContentDAO.saveVoteContent(vote);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is saving"
                                                 + " the vote content: "
                                                 + e.getMessage(),e);
        }
    }
    
    
    
    public List getSessionsFromContent(VoteContent voteContent) throws VoteApplicationException
	{
    	try
        {
            return voteSessionDAO.getSessionsFromContent(voteContent);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is getting"
                                                 + " the vote sessions list: "
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
           throw new VoteApplicationException("Exception occured when lams is retrieving total number of VoteQueUsr: "
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
	 * @param voteContent
	 * @return boolean
	 * @throws VoteApplicationException
	 */
	public boolean studentActivityOccurredGlobal(VoteContent voteContent) throws VoteApplicationException
	{
	    logger.debug("voteContent uid: " +  voteContent.getUid());
		Iterator questionIterator=voteContent.getVoteQueContents().iterator();
		
        while (questionIterator.hasNext())
        {
        	VoteQueContent voteQueContent=(VoteQueContent)questionIterator.next(); 
        	Iterator attemptsIterator=voteQueContent.getVoteUsrAttempts().iterator();
        	while (attemptsIterator.hasNext())
        	{
        		logger.debug("there is at least one attempt for the standard nominamtions");
        		/**
        		 * proved the fact that there is at least one attempt for this content.
        		 */
        		return true;
        	}
        }
        return false;
	}
	
	public boolean studentActivityOccurredStandardAndOpen(VoteContent voteContent) throws VoteApplicationException
	{
	    logger.debug("voteContent uid: " +  voteContent.getUid());
	    boolean studentActivityOccurredGlobal= studentActivityOccurredGlobal(voteContent);
	    logger.debug("studentActivityOccurredGlobal: " +  studentActivityOccurredGlobal);
        
        int userEnteredVotesCount=getUserEnteredVotesCountForContent(voteContent.getUid());
		logger.debug("userEnteredVotesCount: " + userEnteredVotesCount);
		
		if ((studentActivityOccurredGlobal == true) || (userEnteredVotesCount > 0))
		{
		    return true;
		}
        
        logger.debug("there is no votes/nominations for this content");
		return false;
	}
	

	public int countIncompleteSession(VoteContent vote) throws VoteApplicationException
	{
		//int countIncompleteSession=voteSessionDAO.countIncompleteSession(vote);
		int countIncompleteSession=2;
		return countIncompleteSession;
	}
	
	/**
	 * checks the parameter content in the tool sessions table
	 * 
	 * find out if any student has ever used (logged in through the url  and replied) to this content
	 * return true even if you have only one content passed as parameter referenced in the tool sessions table
	 * @param vote
	 * @return boolean
	 * @throws VoteApplicationException
	 */
	public boolean studentActivityOccurred(VoteContent vote) throws VoteApplicationException
	{
		//int countStudentActivity=voteSessionDAO.studentActivityOccurred(vote);
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
            VoteContent fromContent = voteContentDAO.findVoteContentById(fromContentId);
        
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
    			
    			fromContent = voteContentDAO.findVoteContentById(fromContentId);
    			logger.debug("using fromContent: " + fromContent);
            }
            
            logger.debug("final - retrieved fromContent: " + fromContent);
            logger.debug("final - before new instance using " + fromContent + " and " + toContentId);
            logger.debug("final - before new instance using voteToolContentHandler: " + voteToolContentHandler);
            
            try
			{
            	VoteContent toContent = VoteContent.newInstance(voteToolContentHandler, fromContent,toContentId);
                if (toContent == null)
                {
                	logger.debug("throwing ToolException: WARNING!, retrieved toContent is null.");
                	throw new ToolException("WARNING! Fail to create toContent. Can't continue!");
                }
                else
                {
                	logger.debug("retrieved toContent: " + toContent);
    	            voteContentDAO.saveVoteContent(toContent);
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
    	
    	VoteContent voteContent = voteContentDAO.findVoteContentById(toolContentId);
    	logger.debug("retrieving voteContent: " + voteContent);
    	
    	if (voteContent != null)
    	{
            logger.error("start deleting any uploaded file for this content from the content repository");
        	Iterator filesIterator=voteContent.getVoteAttachments().iterator();
        	while (filesIterator.hasNext())
        	{
        		VoteUploadedFile voteUploadedFile=(VoteUploadedFile) filesIterator.next();
        		logger.debug("iterated voteUploadedFile : " + voteUploadedFile);
        		String filesUuid=voteUploadedFile.getUuid(); 
        		if ((filesUuid != null) && (filesUuid.length() > 0))
        		{
        			try
					{
        				voteToolContentHandler.deleteFile(new Long(filesUuid));	
					}
        			catch(RepositoryCheckedException e)
					{
        				logger.error("exception occured deleting files from content repository : " + e);
        				throw new ToolException("undeletable file in the content repository");
					}
        		}
        	}
        	logger.debug("end deleting any uploaded files for this content.");
    		
    		Iterator sessionIterator=voteContent.getVoteSessions().iterator();
            while (sessionIterator.hasNext())
            {
            	if (removeSessionData == false)
            	{
            		logger.debug("removeSessionData is false, throwing SessionDataExistsException.");
            		throw new SessionDataExistsException();	
            	}
            	
            	VoteSession voteSession=(VoteSession)sessionIterator.next(); 
            	logger.debug("iterated voteSession : " + voteSession);
            	
            	Iterator sessionUsersIterator=voteSession.getVoteQueUsers().iterator();
            	while (sessionUsersIterator.hasNext())
            	{
            		VoteQueUsr voteQueUsr=(VoteQueUsr) sessionUsersIterator.next();
            		logger.debug("iterated voteQueUsr : " + voteQueUsr);
            		
            		Iterator sessionUsersAttemptsIterator=voteQueUsr.getVoteUsrAttempts().iterator();
            		while (sessionUsersAttemptsIterator.hasNext())
                	{
            			VoteUsrAttempt voteUsrAttempt=(VoteUsrAttempt)sessionUsersAttemptsIterator.next();
            			logger.debug("iterated voteUsrAttempt : " + voteUsrAttempt);
            			removeAttempt(voteUsrAttempt);
            			logger.debug("removed voteUsrAttempt : " + voteUsrAttempt);
                	}
            	}
            }
            logger.debug("removed all existing responses of toolContent with toolContentId:" + 
            																toolContentId);   
            voteContentDAO.removeVoteById(toolContentId);        
            logger.debug("removed voteContent:" + voteContent);
    	}
    	else
    	{
        	logger.error("Warning!!!, We should have not come here. voteContent is null.");
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
     * Import the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws ToolException if any other error occurs
     */
	public String exportToolContent(List toolContentId) throws ToolException {
		// TODO Auto-generated method stub
		return null;
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
    	
    	VoteContent voteContent=retrieveVote(toolContentId);
    	if (voteContent == null)
    	{
    		logger.error("throwing DataMissingException: WARNING!: retrieved voteContent is null.");
            throw new DataMissingException("voteContent is missing");
    	}
    	voteContent.setDefineLater(true);
    	saveVoteContent(voteContent);
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
    	VoteContent voteContent = voteContentDAO.findVoteContentById(toolContentId);
    	if (voteContent == null)
    	{
    		logger.error("throwing DataMissingException: WARNING!: retrieved voteContent is null.");
            throw new DataMissingException("voteContent is missing");
    	}
    	voteContent.setRunOffline(true);
    	saveVoteContent(voteContent);
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
		VoteSession voteSession= retrieveVoteSession(toolSessionId);
    	
	    if (voteSession == null) 
	    {
	    	logger.error("voteSession does not exist yet: " + toolSessionId);
	    	return false;
	    }
	    else
	    {
	    	logger.debug("retrieving an existing voteSession: " + voteSession + " " + toolSessionId);
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
    	
        VoteContent voteContent = voteContentDAO.findVoteContentById(toolContentId);
        logger.debug("retrieved voteContent: " + voteContent);
        
        if (voteContent == null)
        {
        	logger.error("voteContent is null.");
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

			voteContent = voteContentDAO.findVoteContentById(toolContentId);
        }
        logger.debug("final - retrieved voteContent: " + voteContent);

            
        /*
         * create a new a new tool session if it does not already exist in the tool session table
         */
        if (!existsSession(toolSessionId))
        {
        	try
			{
        		VoteSession voteSession = new VoteSession(toolSessionId,
                        new Date(System.currentTimeMillis()),
                        VoteSession.INCOMPLETE,
                        toolSessionName,
                        voteContent,
                        new TreeSet());

    		    logger.debug("created voteSession: " + voteSession);
    		    voteSessionDAO.saveVoteSession(voteSession);
    		    logger.debug("created voteSession in the db: " + voteSession);	
	
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
    	
    	
    	VoteSession voteSession=null;
    	try
		{
    		voteSession=retrieveVoteSession(toolSessionId);
    		logger.debug("retrieved voteSession: " + voteSession);
		}
    	catch(VoteApplicationException e)
		{
    		throw new DataMissingException("error retrieving voteSession: " + e);
		}
    	catch(Exception e)
		{
    		throw new ToolException("error retrieving voteSession: " + e);
		}
    	
    	if (voteSession == null)
    	{
    		logger.error("voteSession is null");
    		throw new DataMissingException("voteSession is missing");
    	}
    	
    	try
		{
    		voteSessionDAO.removeVoteSession(voteSession);
        	logger.debug("voteSession " + voteSession + " has been deleted successfully.");	
		}
    	catch(VoteApplicationException e)
		{
    		throw new ToolException("error deleting voteSession:" + e);
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
        
        VoteSession voteSession=null;
    	try
		{
    		voteSession=retrieveVoteSession(toolSessionId);
    		logger.debug("retrieved voteSession: " + voteSession);
		}
    	catch(VoteApplicationException e)
		{
    		throw new DataMissingException("error retrieving voteSession: " + e);
		}
    	catch(Exception e)
		{
    		throw new ToolException("error retrieving voteSession: " + e);
		}
    	voteSession.setSessionStatus(COMPLETED);
    	voteSessionDAO.updateVoteSession(voteSession);
    	logger.debug("updated voteSession to COMPLETED" + voteSession);
    	
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
    	VoteQueContent voteQueContent=voteQueContentDAO.getToolDefaultQuestionContent(contentId);
    	logger.debug("retrieved voteQueContent : " + voteQueContent);
    	return voteQueContent; 
    }

    
    public List getToolSessionsForContent(VoteContent vote)
    {
    	logger.debug("attempt retrieving listToolSessionIds for : " + vote);
    	List listToolSessionIds=voteSessionDAO.getSessionsFromContent(vote);
    	return listToolSessionIds;
    }
    

    public void removeAttachment(VoteContent content, VoteUploadedFile attachment) throws RepositoryCheckedException
	{
	    try
	    {
			attachment.setVoteContent(null);
			content.getVoteAttachments().remove(attachment);
			voteToolContentHandler.deleteFile(new Long(attachment.getUuid()));
			saveVoteContent(content);
	    }
	    catch (DataAccessException e)
	    {
	        throw new VoteApplicationException("EXCEPTION: An exception has occurred while trying to remove this attachment"
	                + e.getMessage(), e);
	    }
	}
    
    
    public NodeKey uploadFile(InputStream istream, String filename, String contentType, String fileType) throws RepositoryCheckedException
	{
	    return voteToolContentHandler.uploadFile(istream, filename, contentType, fileType); 
	}
    
    
    public NodeKey copyFile(Long uuid) throws RepositoryCheckedException
	{
	    return voteToolContentHandler.copyFile(uuid);
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
	
	
	public void removeOffLineFile(String filename, Long voteContentId) throws VoteApplicationException
	{
	    try
        {
            voteUploadedFileDAO.removeOffLineFile(filename, voteContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is removing offline filename"
                                                         + e.getMessage(),
														   e);
        }
	}
    
    public void removeOnLineFile(String filename, Long voteContentId) throws VoteApplicationException
	{
	    try
        {
            voteUploadedFileDAO.removeOnLineFile(filename, voteContentId);
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
	
	
	public List getOnlineFilesMetaData(Long voteContentId) throws VoteApplicationException
	{
		return voteUploadedFileDAO.getOnlineFilesMetaData(voteContentId);
	}
    
	
    public List getOfflineFilesMetaData(Long voteContentId) throws VoteApplicationException
	{
    	return voteUploadedFileDAO.getOfflineFilesMetaData(voteContentId);
	}
	
	public boolean isUuidPersisted(String uuid) throws VoteApplicationException
	{
		return voteUploadedFileDAO.isUuidPersisted(uuid);
	}
	
	/**
	 * adds a new entry to the uploaded files table
	 */
	public void persistFile(String uuid, boolean isOnlineFile, String fileName, VoteContent voteContent) throws VoteApplicationException {
		
		logger.debug("attempt persisting file to the db: " + uuid + " " + isOnlineFile + " " + fileName + " " + voteContent);
		VoteUploadedFile voteUploadedFile= new VoteUploadedFile(uuid, isOnlineFile, fileName, voteContent);
		logger.debug("created voteUploadedFile: " + voteUploadedFile);
		voteUploadedFileDAO.saveUploadFile(voteUploadedFile);
		logger.debug("persisted voteUploadedFile: " + voteUploadedFile);
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
	
	
	public List retrieveVoteUploadedFiles(Long voteContentId, boolean fileOnline) throws VoteApplicationException {
        try
        {
            return voteUploadedFileDAO.retrieveVoteUploadedFiles(voteContentId, fileOnline);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading vote uploaded files: "
                                                         + e.getMessage(),
														   e);
        }
	}

	public List retrieveVoteUploadedOfflineFilesUuid(Long voteContentId) throws VoteApplicationException {
		try
        {
            return voteUploadedFileDAO.retrieveVoteUploadedOfflineFilesUuid(voteContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading vote uploaded files: offline + uuids "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	public List retrieveVoteUploadedOnlineFilesUuid(Long voteContentId) throws VoteApplicationException {
		try
        {
            return voteUploadedFileDAO.retrieveVoteUploadedOnlineFilesUuid(voteContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading vote uploaded files: online + uuids "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	public List retrieveVoteUploadedOfflineFilesName(Long voteContentId) throws VoteApplicationException {
		try
        {
            return voteUploadedFileDAO.retrieveVoteUploadedOfflineFilesName(voteContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading vote uploaded files: offline + fileNames "
                                                         + e.getMessage(),
														   e);
        }
	}

	
	public List retrieveVoteUploadedOnlineFilesName(Long voteContentId) throws VoteApplicationException {
    	try
        {
            return voteUploadedFileDAO.retrieveVoteUploadedOnlineFilesName(voteContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading vote uploaded files: online + fileNames "
                                                         + e.getMessage(),
														   e);
        }
	}
	
	
	/*
	public List retrieveVoteUploadedOfflineFilesUuidPlusFilename(Long voteContentId) throws VoteApplicationException {
		try
        {
            return voteUploadedFileDAO.retrieveVoteUploadedOfflineFilesUuidPlusFilename(voteContentId);
        }
        catch (DataAccessException e)
        {
            throw new VoteApplicationException("Exception occured when lams is loading vote uploaded offline file uuid plus filename:  "
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
	 * @return Returns the voteToolContentHandler.
	 */
	public IToolContentHandler getVoteToolContentHandler() {
		return voteToolContentHandler;
	}
	/**
	 * @param voteToolContentHandler The voteToolContentHandler to set.
	 */
	public void setVoteToolContentHandler(IToolContentHandler voteToolContentHandler) {
		this.voteToolContentHandler = voteToolContentHandler;
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
