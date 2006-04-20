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
import java.util.List;

import org.lamsfoundation.lams.contentrepository.ITicket;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteOptsContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * @author Ozgur Demirtas
 * 
 * Interface that defines the contract that all Voting service provider must follow.
 */
public interface IVoteService 
{
	public void configureContentRepository() throws VoteApplicationException;    
        
    public void createVote(VoteContent mcContent) throws VoteApplicationException;
    
    public VoteContent retrieveVote(Long toolContentId) throws VoteApplicationException;
    
    public void createVoteQue(VoteQueContent mcQueContent) throws VoteApplicationException;
    
    public void createVoteSession(VoteSession mcSession) throws VoteApplicationException;
  
    public void createVoteQueUsr(VoteQueUsr mcQueUsr) throws VoteApplicationException;
    
    public VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long mcSessionId) throws VoteApplicationException;
    
 	public VoteQueUsr retrieveVoteQueUsr(Long userId) throws VoteApplicationException;
    
    public void createVoteUsrAttempt(VoteUsrAttempt mcUsrAttempt) throws VoteApplicationException;
    
    public void updateVoteUsrAttempt(VoteUsrAttempt mcUsrAttempt) throws VoteApplicationException;
    
    public List getUserBySessionOnly(final VoteSession voteSession) throws VoteApplicationException;
    
    public void updateVoteQueContent(VoteQueContent voteQueContent) throws VoteApplicationException;
    
    public List getAttemptsForUserAndQuestionContent(final Long queUsrId, final Long mcQueContentId) throws VoteApplicationException;
    
    public List retrieveVoteQueContentsByToolContentId(long qaContentId) throws VoteApplicationException;
    
    public VoteQueContent retrieveVoteQueContentByUID(Long uid) throws VoteApplicationException;
	
    public void removeVoteQueContent(VoteQueContent mcQueContent) throws VoteApplicationException;
    
    public VoteQueContent getVoteQueContentByUID(Long uid) throws VoteApplicationException;
    
    public void saveOrUpdateVoteQueContent(VoteQueContent mcQueContent) throws VoteApplicationException;
    
    public void removeQuestionContentByVoteUid(final Long mcContentUid) throws VoteApplicationException;
    
    public VoteOptsContent getVoteOptionsContentByUID(Long uid) throws VoteApplicationException;
    
    public void cleanAllQuestionsSimple(final Long mcContentUid) throws VoteApplicationException;
    
    public void resetAllQuestions(final Long mcContentUid) throws VoteApplicationException;
    
    public void cleanAllQuestions(final Long mcContentUid) throws VoteApplicationException;
    
    public VoteQueContent getQuestionContentByQuestionText(final String question, final Long mcContentUid);
    
    public void removeVoteQueContentByUID(Long uid) throws VoteApplicationException;
    
    public VoteQueUsr getVoteUserByUID(Long uid) throws VoteApplicationException;
    
    public List getAllQuestionEntries(final Long mcContentId) throws VoteApplicationException;
    
    public VoteQueUsr getVoteQueUsrById(long voteQueUsrId) throws VoteApplicationException;
    
    public VoteSession retrieveVoteSession(Long mcSessionId) throws VoteApplicationException;
    
    public VoteContent retrieveVoteBySessionId(Long mcSessionId) throws VoteApplicationException;
    
    public void updateVote(VoteContent mc) throws VoteApplicationException;
    
    public void updateVoteSession(VoteSession mcSession) throws VoteApplicationException;
    
    public List getVoteUserBySessionOnly(final VoteSession mcSession) throws VoteApplicationException;
    
    public VoteSession getVoteSessionByUID(Long uid) throws VoteApplicationException;
    
    public void deleteVote(VoteContent mc) throws VoteApplicationException;
    
    public void deleteVoteById(Long mcId) throws VoteApplicationException;
    
    public void deleteVoteSession(VoteSession mcSession) throws VoteApplicationException;
    
    public List getSessionNamesFromContent(VoteContent mcContent) throws VoteApplicationException;
    
    public void removeAttempt (VoteUsrAttempt attempt) throws VoteApplicationException;
	
    public void deleteVoteQueUsr(VoteQueUsr mcQueUsr) throws VoteApplicationException;
    
	public User getCurrentUserData(String username) throws VoteApplicationException;
	
	public int getTotalNumberOfUsers() throws VoteApplicationException;
    
    public Lesson getCurrentLesson(long lessonId) throws VoteApplicationException;
    
    public void saveVoteContent(VoteContent mc) throws VoteApplicationException;
    
	public boolean studentActivityOccurredGlobal(VoteContent mcContent) throws VoteApplicationException;
	
	public int countIncompleteSession(VoteContent mc) throws VoteApplicationException;
	
	public boolean studentActivityOccurred(VoteContent mc) throws VoteApplicationException;
	
	public void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;
    
    public void setAsDefineLater(Long toolContentId) throws DataMissingException, ToolException;
    
    public void setAsRunOffline(Long toolContentId) throws DataMissingException, ToolException;

    public void removeToolContent(Long toolContentId, boolean removeSessionData) throws SessionDataExistsException, ToolException;
	
    public boolean existsSession(Long toolSessionId); 
   
    public void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentId) throws ToolException;
    
    public void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    public String leaveToolSession(Long toolSessionId,Long learnerId) throws DataMissingException, ToolException; 

    public ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    public ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException, ToolException;
    
    public IToolVO getToolBySignature(String toolSignature) throws VoteApplicationException;
    
    public long getToolDefaultContentIdBySignature(String toolSignature) throws VoteApplicationException;
    
    public VoteQueContent getToolDefaultQuestionContent(long contentId) throws VoteApplicationException;

    public List getToolSessionsForContent(VoteContent mc);
    
    public ITicket getRepositoryLoginTicket() throws VoteApplicationException;
	
	public void deleteFromRepository(Long uuid, Long versionID);
	
	public NodeKey uploadFileToRepository(InputStream stream, String fileName) throws VoteApplicationException;
	
	public InputStream downloadFile(Long uuid, Long versionID) throws VoteApplicationException;
	
	public String getFileUuid(String filename) throws VoteApplicationException;
	
	public List retrieveVoteUploadedOfflineFilesUuid(Long mcContentId) throws VoteApplicationException;
	
	public List retrieveVoteUploadedOnlineFilesUuid(Long mcContentId) throws VoteApplicationException;
	
	public List retrieveVoteUploadedOfflineFilesName(Long mcContentId) throws VoteApplicationException;
	
	public List retrieveVoteUploadedOnlineFilesName(Long mcContentId) throws VoteApplicationException;
	
	public List retrieveVoteUploadedFiles(Long mcContentId, boolean fileOnline) throws VoteApplicationException;
	
	public void cleanUploadedFilesMetaData() throws VoteApplicationException;
    
    public void persistFile(String uuid, boolean isOnlineFile, String fileName, VoteContent mcContent) throws VoteApplicationException;
    
    public List getAttemptForQueContent(final Long queUsrId, final Long mcQueContentId) throws VoteApplicationException;
	
	public List getAttemptsForUser(final Long queUsrId) throws VoteApplicationException;
	
	public int countSessionComplete() throws VoteApplicationException;
	
	public VoteSession findVoteSessionById(Long mcSessionId) throws VoteApplicationException;
	
	public int countSessionIncomplete() throws VoteApplicationException;
    
    public NodeKey uploadFile(InputStream istream, String filename, String contentType, String fileType) throws RepositoryCheckedException;
    
    public NodeKey copyFile(Long uuid) throws RepositoryCheckedException;
    
    public void removeOffLineFile(String filename, Long mcContentId) throws VoteApplicationException;
    
    public void removeOnLineFile(String filename, Long mcContentId) throws VoteApplicationException;
    
    public boolean isOffLineFilePersisted(String filename) throws VoteApplicationException;
    
    public boolean isOnLineFilePersisted(String filename) throws VoteApplicationException;
    
    public boolean isUuidPersisted(String uuid) throws VoteApplicationException;
    
    public List getOnlineFilesMetaData(Long mcContentId) throws VoteApplicationException;
    
    public List getOfflineFilesMetaData(Long mcContentId) throws VoteApplicationException;
    
    public List getSessionsFromContent(VoteContent mcContent) throws VoteApplicationException;

}

