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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.ToolOutput;
import org.lamsfoundation.lams.tool.ToolSessionExportOutputData;
import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.ReflectionDTO;
import org.lamsfoundation.lams.tool.vote.SessionDTO;
import org.lamsfoundation.lams.tool.vote.VoteApplicationException;
import org.lamsfoundation.lams.tool.vote.VoteGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.vote.VoteMonitoredAnswersDTO;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueUsr;
import org.lamsfoundation.lams.tool.vote.pojos.VoteSession;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUsrAttempt;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author Ozgur Demirtas
 * 
 * Interface that defines the contract Voting service provider must follow.
 */
public interface IVoteService {
    
    /**
     * @param user
     * @param toolSessionId
     * @return
     */
    boolean isUserGroupLeader(VoteQueUsr user, Long toolSessionId);
    
    /**
     * Set specified user as a leader. Also the previous leader (if any) is marked as non-leader.
     * 
     * @param userId
     * @param toolSessionID
     */
    VoteQueUsr checkLeaderSelectToolForSessionLeader(VoteQueUsr user, Long toolSessionID);
    
    /**
     * Check user has the same answers logs as group leader. If not - creates missing ones. 
     * 
     * @param user
     * @param leader
     */
    void copyAnswersFromLeader(VoteQueUsr user, VoteQueUsr leader);
    
    /**
     * Generates chart data for the learner module and monitoring module Summary tab (Individual Sessions mode)
     * 
     * @param request
     * @param voteService
     * @param toolContentID
     * @param toolSessionUid
     */
    VoteGeneralLearnerFlowDTO prepareChartData(HttpServletRequest request, Long toolContentID, Long toolSessionUid,
	    VoteGeneralLearnerFlowDTO voteGeneralLearnerFlowDTO);
    
    /**
     * Generates data for all sessions in the Monitoring Summary, including all sessions summary.
     * 
     * @param toolContentID
     * @return
     */
    LinkedList<SessionDTO> getSessionDTOs(Long toolContentID);

    List<VoteMonitoredAnswersDTO> processUserEnteredNominations(VoteContent voteContent, String currentSessionId,
	    boolean showUserEntriesBySession, String userId, boolean showUserEntriesByUserId);
    
    List<ReflectionDTO> getReflectionData(VoteContent voteContent, Long userID);

    void createVote(VoteContent voteContent) throws VoteApplicationException;

    VoteContent retrieveVote(Long toolContentID) throws VoteApplicationException;

    VoteUsrAttempt getAttemptByUID(Long uid) throws VoteApplicationException;

    void createVoteQue(VoteQueContent voteQueContent) throws VoteApplicationException;

    void createVoteSession(VoteSession voteSession) throws VoteApplicationException;

    void createVoteQueUsr(VoteQueUsr voteQueUsr) throws VoteApplicationException;

    List getUserEnteredVotesForSession(final String userEntry, final Long voteSessionUid);

    VoteQueUsr getVoteUserBySession(final Long queUsrId, final Long sessionUid)
	    throws VoteApplicationException;

    VoteQueUsr retrieveVoteQueUsr(Long userId) throws VoteApplicationException;

    List getVoteUserBySessionUid(final Long voteSessionUid) throws VoteApplicationException;

    int getCompletedVoteUserBySessionUid(final Long voteSessionUid) throws VoteApplicationException;

    VoteQueContent getQuestionContentByDisplayOrder(final Long displayOrder, final Long voteContentUid)
	    throws VoteApplicationException;

    Set getAttemptsForUserAndSessionUseOpenAnswer(final Long userUid, final Long sessionUid);

   Set getSessionUserEntriesSet(final Long voteSessionUid) throws VoteApplicationException;

    void createVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) throws VoteApplicationException;

    void updateVoteUsrAttempt(VoteUsrAttempt voteUsrAttempt) throws VoteApplicationException;

    List getUserRecords(final String userEntry) throws VoteApplicationException;

    List<VoteQueUsr> getUserBySessionOnly(final VoteSession voteSession) throws VoteApplicationException;

    void hideOpenVote(VoteUsrAttempt voteUsrAttempt) throws VoteApplicationException;

    void showOpenVote(VoteUsrAttempt voteUsrAttempt) throws VoteApplicationException;

    void updateVoteQueContent(VoteQueContent voteQueContent) throws VoteApplicationException;

    int getAttemptsForQuestionContent(final Long voteQueContentId) throws VoteApplicationException;

    boolean studentActivityOccurredStandardAndOpen(VoteContent voteContent) throws VoteApplicationException;

    int getUserEnteredVotesCountForContent(final Long voteContentUid) throws VoteApplicationException;

    List getStandardAttemptUsersForQuestionContentAndSessionUid(final Long voteQueContentId,
	    final Long voteSessionUid);

    int getStandardAttemptsForQuestionContentAndSessionUid(final Long voteQueContentId, final Long voteSessionId)
	    throws VoteApplicationException;

    int getSessionEntriesCount(final Long voteSessionId) throws VoteApplicationException;

    List getAttemptsForUserAndQuestionContent(final Long userUid, final Long questionUid)
	    throws VoteApplicationException;

    VoteUsrAttempt getAttemptForUserAndQuestionContentAndSession(final Long queUsrId,
	    final Long voteQueContentId, final Long toolSessionUid) throws VoteApplicationException;

    List retrieveVoteQueContentsByToolContentId(long qaContentId) throws VoteApplicationException;

    VoteQueContent retrieveVoteQueContentByUID(Long uid) throws VoteApplicationException;

    void removeVoteQueContent(VoteQueContent voteQueContent) throws VoteApplicationException;

    VoteQueContent getVoteQueContentByUID(Long uid) throws VoteApplicationException;

    void saveOrUpdateVoteQueContent(VoteQueContent voteQueContent) throws VoteApplicationException;

    void removeQuestionContentByVoteUid(final Long voteContentUid) throws VoteApplicationException;

    void cleanAllQuestionsSimple(final Long voteContentUid) throws VoteApplicationException;

    void resetAllQuestions(final Long voteContentUid) throws VoteApplicationException;

    void cleanAllQuestions(final Long voteContentUid) throws VoteApplicationException;

    Set getUserEntries() throws VoteApplicationException;

    List<VoteUsrAttempt> getStandardAttemptsForQuestionContentAndContentUid(final Long voteQueContentId);

    List getSessionUserEntries(final Long voteSessionId) throws VoteApplicationException;

    VoteQueContent getQuestionContentByQuestionText(final String question, final Long voteContentUid);

    void removeVoteQueContentByUID(Long uid) throws VoteApplicationException;

    VoteQueUsr getVoteUserByUID(Long uid) throws VoteApplicationException;

    void updateVoteUser(VoteQueUsr voteUser) throws VoteApplicationException;

    void removeAttemptsForUserandSession(final Long queUsrId, final Long voteSessionId)
	    throws VoteApplicationException;

    List getAllQuestionEntries(final Long voteContentId) throws VoteApplicationException;

    VoteQueUsr getVoteQueUsrById(long voteQueUsrId) throws VoteApplicationException;

    VoteSession retrieveVoteSession(Long voteSessionId) throws VoteApplicationException;

    VoteContent retrieveVoteBySessionId(Long voteSessionId) throws VoteApplicationException;

    void updateVote(VoteContent vote) throws VoteApplicationException;

    void updateVoteSession(VoteSession voteSession) throws VoteApplicationException;

    VoteSession getVoteSessionByUID(Long uid) throws VoteApplicationException;

    /**
     * Get the count of all the potential learners for the vote session. This will include the people that have never
     * logged into the lesson. Not great, but it is a better estimate of how many users there will be eventually than
     * the number of people already known to the tool.
     * 
     * @param voteSessionId
     *                The tool session id
     */
    int getVoteSessionPotentialLearnersCount(Long voteSessionId) throws VoteApplicationException;

    void deleteVote(VoteContent vote) throws VoteApplicationException;

    void deleteVoteById(Long voteId) throws VoteApplicationException;

    void deleteVoteSession(VoteSession voteSession) throws VoteApplicationException;

    List getSessionNamesFromContent(VoteContent voteContent) throws VoteApplicationException;

    void removeAttempt(VoteUsrAttempt attempt) throws VoteApplicationException;

    void deleteVoteQueUsr(VoteQueUsr voteQueUsr) throws VoteApplicationException;

    User getCurrentUserData(String username) throws VoteApplicationException;

    int getTotalNumberOfUsers() throws VoteApplicationException;

    Lesson getCurrentLesson(long lessonId) throws VoteApplicationException;

    void saveVoteContent(VoteContent vote) throws VoteApplicationException;

    boolean studentActivityOccurredGlobal(VoteContent voteContent) throws VoteApplicationException;

    int countIncompleteSession(VoteContent vote) throws VoteApplicationException;

    boolean studentActivityOccurred(VoteContent vote) throws VoteApplicationException;

    void copyToolContent(Long fromContentId, Long toContentId) throws ToolException;

    void removeToolContent(Long toolContentID, boolean removeSessionData) throws SessionDataExistsException,
	    ToolException;

    boolean existsSession(Long toolSessionId);

    void createToolSession(Long toolSessionId, String toolSessionName, Long toolContentID) throws ToolException;

    void removeToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    String leaveToolSession(Long toolSessionId, Long learnerId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(Long toolSessionId) throws DataMissingException, ToolException;

    ToolSessionExportOutputData exportToolSession(List toolSessionIds) throws DataMissingException,
	    ToolException;

    IToolVO getToolBySignature(String toolSignature) throws VoteApplicationException;

    long getToolDefaultContentIdBySignature(String toolSignature) throws VoteApplicationException;

    VoteQueContent getToolDefaultQuestionContent(long contentId) throws VoteApplicationException;

    List getToolSessionsForContent(VoteContent vote);

    List getAttemptsForUser(final Long userUid) throws VoteApplicationException;

    int countSessionComplete() throws VoteApplicationException;

    List getAllQuestionEntriesSorted(final long voteContentId) throws VoteApplicationException;

    List<Long> getSessionsFromContent(VoteContent mcContent) throws VoteApplicationException;

    Set getAttemptsForUserAndSession(final Long queUsrId, final Long voteSessionUid)
	    throws VoteApplicationException;

    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    NotebookEntry getEntry(Long id, Integer idType, String signature, Integer userID);

    void removeNominationsFromCache(VoteContent voteContent);

    ToolOutput getToolInput(Long requestingToolContentId, Integer learnerId);

    List<DataFlowObject> getDataFlowObjects(Long toolContentId);

    void saveDataFlowObjectAssigment(DataFlowObject assignedDataFlowObject);

    DataFlowObject getAssignedDataFlowObject(Long toolContentId);
    
    /**
     * Returns whether activity is grouped and therefore it is expected more than one tool session.
     *  
     * @param toolContentID
     * @return
     */
    boolean isGroupedActivity(long toolContentID);
}